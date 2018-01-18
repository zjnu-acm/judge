/*
 * Copyright 2017 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.data.form.SubmissionQueryForm;
import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.mapper.LanguageMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.util.CopyHelper;
import cn.edu.zjnu.acm.judge.util.DeleteHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

/**
 *
 * @author zhanhb
 */
@RunWith(Parameterized.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Import(JudgeServiceTest.Initializer.class)
public class JudgeServiceTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    private static final ImmutableMap<String, String> EXTENSION_MAP
            = ImmutableMap.of("cpp", "cc", "groovy", "groovy", "c", "c");
    private static final String IP = "::1";

    private static String build(String... args) {
        return Arrays.stream(args)
                .map(s -> new StringTokenizer(s).countTokens() > 1 ? '"' + s + '"' : s)
                .collect(Collectors.joining(" "));
    }

    private static String getExtension(Path path) {
        String name = path.getFileName().toString();
        return name.lastIndexOf('.') > 0 ? name.substring(name.lastIndexOf('.') + 1) : "";
    }

    @BeforeClass
    public static void setUpClass() {
        assumeTrue("not windows", Platform.isWindows());
    }

    @Parameterized.Parameters(name = "{index}: {0} {1}")
    public static List<Object[]> data() throws Exception {
        ArrayList<Object[]> list = new ArrayList<>(20);
        Path program = Paths.get(Initializer.class.getResource("/sample/program").toURI());
        for (Checker c : Checker.values()) {
            Path dir = program.resolve(c.name());

            try (Stream<Path> stream = Files.list(dir)) {
                stream.forEach(path -> list.add(new Object[]{c, path}));
            }
        }
        return list;
    }

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    @Autowired
    private LanguageMapper languageMapper;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private Initializer initializer;
    private final String specialKey = "wa/less.groovy";
    private final int specialScore = 50;
    private final Checker checker;
    private final Path path;

    public JudgeServiceTest(Checker checker, Path path) {
        this.checker = checker;
        this.path = path;
    }

    private int findFirstLanguageByExtension(String extension) {
        return languageMapper.findAll().stream()
                .filter(language -> language.getSourceExtension().toLowerCase().equals(extension.toLowerCase()))
                .map(Language::getId).findFirst().orElseThrow(RuntimeException::new);
    }

    @Test
    public void test() throws Exception {
        check(checker);
    }

    private Submission findOneByUserId(String userId) {
        return submissionMapper.findAllByCriteria(SubmissionQueryForm.builder().user(userId).size(1).build()).iterator().next();
    }

    private void check(Checker c) throws IOException {
        String userId = initializer.userId;
        CompletableFuture<?> future;
        long id;
        try {
            String extension = getExtension(path);
            int language = findFirstLanguageByExtension(EXTENSION_MAP.get(extension));
            String source = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            submissionService.remove(userId);
            future = submissionService.submit(language, source, userId, IP, initializer.problem);
            id = findOneByUserId(userId).getId();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        initializer.submissions.add(id);

        Submission submission;
        try {
            submission = future.thenApply(__ -> submissionMapper.findOne(id)).get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new AssertionError(ex);
        }
        assertNotNull(submission);
        int expectScore = c.getScore();
        String key = path.getParent().getFileName() + "/" + path.getFileName();
        if (specialKey.equals(key)) {
            expectScore = specialScore;
        }
        assertEquals(id + " " + path, expectScore, submission.getScore());
        String detail = submissionMapper.getSubmissionDetail(id);
        String[] details = detail.split(",");
        ArrayList<String> list = Lists.newArrayList();
        for (int i = 0; i < details.length; i += 4) {
            list.add(details[i]);
        }
        assertTrue(id + " " + path + " " + Arrays.toString(details), list.contains(Integer.toString(c.getStatus())));
    }

    @Service
    static class Initializer {

        @Autowired
        private LanguageMapper languageMapper;
        @Autowired
        private MockDataService mockDataService;
        @Autowired
        private JudgeConfiguration judgeConfiguration;
        @Autowired
        private SubmissionService submissionService;
        @Autowired
        private ProblemService problemService;
        @Autowired
        private AccountService accountService;

        private Path groovyPath;

        long problem;
        Path dataDir;
        int groovyLanguage;
        List<Long> submissions = Lists.newArrayList();
        String userId;

        private String getGroovy(String property) {
            return Arrays.stream(property.split(File.pathSeparator))
                    .filter(s -> s.contains("groovy-"))
                    .collect(Collectors.joining(File.pathSeparator));
        }

        @PostConstruct
        public void init() throws Exception {
            userId = mockDataService.user().getId();
            problem = mockDataService.problem(builder -> builder.timeLimit(6000L).memoryLimit(256 * 1024L)).getId();
            dataDir = CopyHelper.copy(Paths.get(Initializer.class.getResource("/sample/data").toURI()), judgeConfiguration.getDataDirectory(problem), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            Path mavenGroovyPath = Paths.get(getGroovy(System.getProperty("java.class.path")));
            groovyPath = Files.copy(mavenGroovyPath, dataDir.resolve(mavenGroovyPath.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            String executeCommand = build("java", "-cp", groovyPath.toString(), groovy.ui.GroovyMain.class.getName(), "Main.groovy");
            Language groovy = Language.builder().name("groovy")
                    .sourceExtension("groovy")
                    .executeCommand(executeCommand)
                    .executableExtension("groovy")
                    .description("")
                    .timeFactor(2)
                    .build();
            languageMapper.save(groovy);
            groovyLanguage = groovy.getId();
        }

        @PreDestroy
        public void destory() throws IOException {
            languageMapper.deleteById(groovyLanguage);
            submissions.forEach(submissionService::delete);
            DeleteHelper.delete(dataDir);
            problemService.delete(problem);
            accountService.delete(userId);
        }
    }

}
