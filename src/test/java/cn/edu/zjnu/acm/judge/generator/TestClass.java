package cn.edu.zjnu.acm.judge.generator;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TestClass {

    private final String _package;
    private final String superclass;
    private final Set<String> imports = new TreeSet<>();
    private final String className;
    private final List<String> classAnnotations;
    private final Map<String, Field> fields = new LinkedHashMap<>(4);
    private final List<Method> methods = new ArrayList<>(4);

    public TestClass(Class<?> _for, String... annotations) {
        this(_for, Object.class, annotations);
    }

    public TestClass(Class<?> _for, Class<?> superclass, String... annotations) {
        this.className = _for.getSimpleName() + "Test";
        this.superclass = getSuperclass(superclass);
        this._package = _for.getPackage().getName();
        this.classAnnotations = new ArrayList<>(Arrays.asList(annotations));
    }

    private String getSuperclass(Class<?> superclass) {
        if (superclass == null || superclass == Object.class) {
            return null;
        } else if (superclass.isPrimitive()) {
            throw new IllegalArgumentException();
        } else {
            addImport(superclass);
            return superclass.getSimpleName();
        }
    }

    public void addImport(Class<?> _import) {
        Package aPackage = _import.getPackage();
        if (aPackage == null || aPackage.getName().equals("java.lang")) {
            return;
        }
        imports.add("import " + _import.getCanonicalName() + ";");
    }

    public void addImport(String _import) {
        imports.add("import " + _import + ";");
    }

    public void addStaticImport(String _import) {
        imports.add("import static " + _import + ";");
    }

    public void write(PrintWriter out) {
        out.println("package " + _package + ";");
        out.println();
        for (String aImport : imports) {
            out.println(aImport);
        }
        out.println();
        for (String classAnnotation : classAnnotations) {
            out.println(classAnnotation);
        }
        out.print("public class " + className + " ");
        if (superclass != null) {
            out.print("extends " + superclass + " ");
        }
        out.println("{");
        out.println();
        for (Field field : fields.values()) {
            field.write(out, 1);
        }
        out.println();
        for (Method method : methods) {
            method.write(out, 1);
            out.println();
        }
        out.println("}");
    }

    public void addField(Class<?> type, String name, String... annotations) {
        addImport(type);
        fields.put(name, new Field(type.getSimpleName(), name, Arrays.asList(annotations)));
    }

    public void addMethod(String methodContent) {
        methods.add(new Method(new BufferedReader(new StringReader(methodContent)).lines().collect(Collectors.toList())));
    }

    private static class Field {

        private final String typeName;
        private final String variableName;
        private final List<String> annotations;

        Field(String typeName, String variableName, List<String> annotations) {
            this.typeName = typeName;
            this.variableName = variableName;
            this.annotations = annotations;
        }

        void write(PrintWriter out, int indent) {
            String prefix = Strings.repeat("\t", indent);
            for (String annotation : annotations) {
                out.println(prefix + annotation);
            }
            out.println(prefix + "private " + typeName + " " + variableName + ";");
        }

    }

    private static class Method {

        private final List<String> lines;

        Method(List<String> lines) {
            this.lines = lines;
        }

        void write(PrintWriter out, int indent) {
            String prefix = Strings.repeat("\t", indent);
            for (String line : lines) {
                if (line.isEmpty()) {
                    out.println();
                } else {
                    out.println(prefix + line);
                }
            }
        }

    }

}
