package cn.edu.zjnu.acm.judge.generator;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.util.MatcherWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtMethod;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitPlatform.class)
@SpringBootTest(classes = Application.class)
public class MockGenerator {

    private static final Map<Class<?>, String> DEFAULT_VALUES = ImmutableMap.<Class<?>, String>builder()
            .put(boolean.class, "false")
            .put(byte.class, "0")
            .put(char.class, "0")
            .put(short.class, "0")
            .put(int.class, "0")
            .put(long.class, "0")
            .put(float.class, "0")
            .put(double.class, "0")
            .put(String.class, "\"\"")
            .build();
    private static final Class<?> MAIN_CLASS = Application.class;
    private static final Path OUTPUT_DIR = Paths.get("target/mock");

    private static boolean accept(Class<?> key, List<?> list) {
        return key.getPackage().getName().startsWith(MAIN_CLASS.getPackage().getName()) && key.getEnclosingClass() == null && !list.isEmpty();
    }

    private static String getDefaultValue(Class<?> type) {
        return DEFAULT_VALUES.getOrDefault(type, "null");
    }

    private static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> p) {
        return StreamSupport.stream(
                new UnorderedWhileSpliterator<>(p, stream.spliterator()), stream.isParallel()
        ).onClose(stream::close);
    }

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Test
    public void test() throws IOException {
        Gson gson = new Gson();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        Map<Class<?>, List<Info>> map = handlerMethods.entrySet().stream()
                .map(entry -> new Info(entry.getValue(), entry.getKey()))
                .collect(Collectors.groupingBy(info -> info.getHandlerMethod().getMethod().getDeclaringClass()));
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw, true);
        for (Map.Entry<Class<?>, List<Info>> entry : map.entrySet()) {
            Class<?> key = entry.getKey();
            List<Info> value = entry.getValue();
            if (!accept(key, value)) {
                continue;
            }
            value.sort(Comparator.comparing(info -> info.getHandlerMethod().getMethod(), Comparator.comparing(method -> method.getDeclaringClass().getName().replace(".", "/") + "." + method.getName() + ":" + org.springframework.asm.Type.getMethodDescriptor(method), toMethodComparator(key))));
            TestClass testClass = new TestClass(key,
                    "@AutoConfigureMockMvc",
                    "@RunWith(JUnitPlatform.class)",
                    "@Slf4j",
                    "@SpringBootTest(classes = " + MAIN_CLASS.getSimpleName() + ".class)",
                    "@Transactional",
                    "@WebAppConfiguration");

            testClass.addImport(MAIN_CLASS);
            testClass.addImport(AutoConfigureMockMvc.class);
            testClass.addImport(JUnitPlatform.class);
            testClass.addImport(RunWith.class);
            testClass.addImport(Slf4j.class);
            testClass.addImport(SpringBootTest.class);
            testClass.addImport(Transactional.class);
            testClass.addImport(WebAppConfiguration.class);

            testClass.addImport(Autowired.class);
            testClass.addField(MockMvc.class, "mvc", "@Autowired");

            for (Info info : value) {
                RequestMappingInfo requestMappingInfo = info.getRequestMappingInfo();
                Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();
                String requestMethod = requestMethods.isEmpty() ? "post" : requestMethods.iterator().next().toString().toLowerCase();
                HandlerMethod handlerMethod = info.getHandlerMethod();
                String url = gson.toJson(requestMappingInfo.getPatternsCondition().getPatterns().iterator().next());
                generate(key, requestMappingInfo, handlerMethod, url, testClass, requestMethod);
            }
            testClass.write(out);
            Path to = OUTPUT_DIR.resolve(key.getName().replace(".", "/") + "Test.java");
            Files.createDirectories(to.getParent());
            Files.write(to, sw.toString().replace("\t", "    ").getBytes(StandardCharsets.UTF_8));
            sw.getBuffer().setLength(0);
        }
    }

    private Comparator<String> toMethodComparator(Class<?> key) {
        AtomicInteger counter = new AtomicInteger();
        ClassPool classPool = ClassPool.getDefault();
        Map<String, Integer> map = Stream.concat(
                takeWhile(Stream.<Class<?>>iterate(key, Class::getSuperclass), parent -> parent != null && parent.getClassLoader() != null),
                Arrays.stream(key.getInterfaces())
        ).flatMap(type -> stream(classPool, type))
                .collect(Collectors.toMap(
                        method -> method.getDeclaringClass().getName().replace(".", "/") + "." + method.getName() + ":" + method.getSignature(),
                        __ -> counter.getAndIncrement()));
        return Comparator.comparingInt(s -> map.getOrDefault(s, Integer.MAX_VALUE));
    }

    private Stream<CtMethod> stream(ClassPool classPool, Class<?> type) {
        try (InputStream is = type.getClassLoader().getResourceAsStream(type.getName().replace(".", "/").concat(".class"))) {
            return is != null ? Arrays.stream(classPool.makeClass(is).getDeclaredMethods())
                    .filter(method -> !Modifier.isStatic(method.getModifiers()))
                    : Stream.empty();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private void generate(Class<?> key, RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod, String url, TestClass testClass, String lowerMethod) {
        Method method = handlerMethod.getMethod();
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        out.println("/**");
        out.println(" * Test of " + method.getName() + " method, of class " + key.getSimpleName() + ".");
        for (Class<?> type : method.getParameterTypes()) {
            testClass.addImport(type);
        }
        out.println(" *");
        out.println(" * {@link " + key.getSimpleName() + "#" + method.getName()
                + Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ", "(", ")}")));
        out.println(" */");
        testClass.addImport(Test.class);
        out.println("@Test");
        out.println("public void test" + StringUtils.capitalize(method.getName()) + "() throws Exception {");
        out.println("\tlog.info(\"" + method.getName() + "\");");

        List<String> variableDeclares = new ArrayList<>(4);
        Map<String, Class<?>> params = new LinkedHashMap<>(4);
        String body = null;
        Class<?> bodyType = null;
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        Parameter[] parameters = method.getParameters();
        List<String> files = new ArrayList<>(4);
        List<String> pathVariables = new ArrayList<>(4);
        Map<String, String> headers = Maps.newTreeMap();
        String locale = null;
        for (MethodParameter methodParameter : methodParameters) {
            Class<?> type = methodParameter.getParameterType();
            String typeName = type.getSimpleName();
            String name = "";
            testClass.addImport(type);
            boolean unknown = false;
            RequestParam requestParam = methodParameter.getParameterAnnotation(RequestParam.class);
            PathVariable pathVariable = methodParameter.getParameterAnnotation(PathVariable.class);
            RequestHeader requestHeader = methodParameter.getParameterAnnotation(RequestHeader.class);
            if (requestParam != null) {
                name = requestParam.value();
                if (name.isEmpty()) {
                    name = requestParam.name();
                }
            } else if (pathVariable != null) {
                name = pathVariable.value();
                if (name.isEmpty()) {
                    name = pathVariable.name();
                }
                if (name.isEmpty()) {
                    name = parameters[methodParameter.getParameterIndex()].getName();
                }
                pathVariables.add(name);
                variableDeclares.add("\t" + typeName + " " + name + " = " + getDefaultValue(type) + ";");
                continue;
            } else if (methodParameter.hasParameterAnnotation(RequestBody.class)) {
                body = "request";
                bodyType = type;
                variableDeclares.add("\t" + typeName + " request = " + getDefaultValue(type) + ";");
                continue;
            } else if (requestHeader != null) {
                name = requestHeader.value();
                if (name.isEmpty()) {
                    name = requestHeader.name();
                }
                if (name.isEmpty()) {
                    name = parameters[methodParameter.getParameterIndex()].getName();
                }
                String camelCase = camelCase(name);
                headers.put(name, camelCase);
                variableDeclares.add("\t" + typeName + " " + camelCase + " = " + getDefaultValue(type) + ";");
                continue;
            } else if (HttpServletResponse.class == type || HttpServletRequest.class == type) {
                continue;
            } else if (Locale.class == type) {
                locale = "locale";
                variableDeclares.add("\t" + typeName + " " + locale + " = Locale.getDefault();");
                continue;
            } else {
                unknown = true;
            }
            if (name.isEmpty()) {
                name = parameters[methodParameter.getParameterIndex()].getName();
            }
            if (unknown && type.getClassLoader() != null && type != MultipartFile.class) {
                ReflectionUtils.doWithFields(type, field -> process(field.getName(), camelCase(field.getName()), field.getType(), params, files, variableDeclares, testClass, method, lowerMethod),
                        field -> !Modifier.isStatic(field.getModifiers()));
                continue;
            } else if (unknown) {
                System.err.println("param " + methodParameter.getParameterIndex() + " with type " + typeName + " in " + method + " has no annotation");
            }
            process(name, camelCase(name), type, params, files, variableDeclares, testClass, method, lowerMethod);
        }
        for (String variableDeclare : variableDeclares) {
            out.println(variableDeclare);
        }
        testClass.addImport(MvcResult.class);
        if (files.isEmpty()) {
            testClass.addStaticImport(MockMvcRequestBuilders.class, lowerMethod);
            out.print("\tMvcResult result = mvc.perform(" + lowerMethod + "(" + url);
            for (String pathVariable : pathVariables) {
                out.print(", " + pathVariable);
            }
            out.print(")");
        } else {
            String methodName = "multipart";
            if (!ClassUtils.hasMethod(MockMvcRequestBuilders.class, "multipart", String.class, String[].class)) {
                methodName = "fileUpload";
            }
            testClass.addStaticImport(MockMvcRequestBuilders.class, methodName);
            out.print("\tMvcResult result = mvc.perform(" + methodName + "(" + url);
            for (String pathVariable : pathVariables) {
                out.print(", " + pathVariable);
            }
            out.print(")");
            for (String file : files) {
                out.print(".file(" + file + ")");
            }
        }

        boolean newLine = params.size() >= 2;
        for (Map.Entry<String, Class<?>> entry : params.entrySet()) {
            String paramName = entry.getKey();
            String variableName = camelCase(paramName);
            Class<?> paramType = entry.getValue();
            String value;
            if (paramType.isPrimitive()) {
                value = com.google.common.primitives.Primitives.wrap(paramType).getSimpleName() + ".toString(" + variableName + ")";
            } else if (paramType == String.class) {
                value = variableName;
            } else {
                testClass.addImport(Objects.class);
                value = "Objects.toString(" + variableName + ", \"\")";
            }
            if (newLine) {
                out.println();
                out.print("\t\t\t");
            }
            out.print(".param(\"" + paramName + "\", " + value + ")");
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            out.println();
            out.print("\t\t\t.header(\"" + entry.getKey() + "\", " + entry.getValue() + ")");
        }

        if (locale != null) {
            out.println();
            out.print("\t\t\t.locale(" + locale + ")");
        }

        switch (lowerMethod) {
            case "get":
            case "delete":
                if (body != null) {
                    System.err.println("RequestBody annotation found on " + method + " with request method " + lowerMethod);
                }
                if (!requestMappingInfo.getConsumesCondition().isEmpty()) {
                    System.err.println("request consumes " + requestMappingInfo.getConsumesCondition() + " found on " + method);
                }
        }
        if (body != null) {
            out.println();
            if (bodyType == String.class || bodyType == byte[].class) {
                out.print("\t\t\t.content(" + body + ")");
            } else {
                testClass.addField(ObjectMapper.class, "objectMapper", "@Autowired");
                out.print("\t\t\t.content(objectMapper.writeValueAsString(" + body + "))");
            }
            testClass.addImport(MediaType.class);
            out.print(".contentType(MediaType.APPLICATION_JSON)");
        }
        testClass.addStaticImport(MockMvcResultMatchers.class, "status");
        out.println(")");
        out.println("\t\t\t.andExpect(status().isOk())");
        out.println("\t\t\t.andReturn();");
        out.println("}");
        testClass.addMethod(sw.toString());
    }

    private String camelCase(String name) {
        String tmp = MatcherWrapper.matcher("[^a-zA-Z]+([a-zA-Z])", name).replaceAll(x -> x.group(1).toUpperCase());
        tmp = tmp.isEmpty() ? "" : Character.toLowerCase(tmp.charAt(0)) + tmp.substring(1);
        return "referer".equals(tmp) ? "referrer" : tmp;
    }

    private void process(String paramName, String variableName, Class<?> type, Map<String, Class<?>> requestParams, List<String> files, List<String> variableDeclares, TestClass testClass, Method method, String lowerMethod) {
        if (type == MultipartFile.class) {
            String msg = "upload a multipart file, but request method is %s, %s";
            assertThat(lowerMethod).withFailMessage(msg, lowerMethod, method)
                    .isEqualTo("post");

            testClass.addImport(MockMultipartFile.class);
            variableDeclares.add("\tbyte[] " + variableName + "Content = null;");
            variableDeclares.add("\tMockMultipartFile " + variableName + " = new MockMultipartFile(\"" + paramName + "\", " + variableName + "Content);");
            files.add(variableName);
        } else {
            requestParams.put(paramName, type);
            testClass.addImport(type);
            variableDeclares.add("\t" + type.getSimpleName() + " " + variableName + " = " + getDefaultValue(type) + ";");
        }
    }

    private static class Info {

        private final HandlerMethod handlerMethod;
        private final RequestMappingInfo requestMappingInfo;

        Info(HandlerMethod handlerMethod, RequestMappingInfo requestMappingInfo) {
            this.handlerMethod = handlerMethod;
            this.requestMappingInfo = requestMappingInfo;
        }

        HandlerMethod getHandlerMethod() {
            return handlerMethod;
        }

        RequestMappingInfo getRequestMappingInfo() {
            return requestMappingInfo;
        }

    }

}
