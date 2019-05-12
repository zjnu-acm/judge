/*
 * Copyright 2017-2019 ZJNU ACM.
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
import cn.edu.zjnu.acm.judge.data.dto.SubmissionDetailDTO;
import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.mapper.LanguageMapper;
import cn.edu.zjnu.acm.judge.support.JudgeData;
import cn.edu.zjnu.acm.judge.support.RunRecord;
import cn.edu.zjnu.acm.judge.support.RunResult;
import cn.edu.zjnu.acm.judge.util.Platform;
import cn.edu.zjnu.acm.judge.util.ResultType;
import com.github.zhanhb.judge.GroovyHolder;
import com.github.zhanhb.judge.common.SimpleValidator;
import com.github.zhanhb.judge.common.Validator;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

/**
 *
 * @author zhanhb
 */
@RunWith(Parameterized.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class JudgeRunnerTest {

    private static final ImmutableMap<String, String> EXTENSION_MAP
            = ImmutableMap.of("cpp", "cc", "groovy", "groovy", "c", "c");

    private static final Map<String, Integer> SPECIAL_SCORE = ImmutableMap.of("wa/less.groovy", 50);

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
        assumeTrue("Only platform windows is supported", Platform.isWindows());
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static List<Object[]> data() throws Exception {
        ArrayList<Object[]> list = new ArrayList<>(20);
        Path program = Paths.get(JudgeRunnerTest.class.getResource("/sample/program").toURI());
        for (Checker c : Checker.values()) {
            Path dir = program.resolve(c.name());

            try (Stream<Path> stream = Files.list(dir)) {
                stream.forEach(path -> list.add(new Object[]{c.name() + "/" + path.getFileName(), c, path}));
            }
        }
        return list;
    }

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    @Autowired
    private LanguageMapper languageMapper;
    @Autowired
    private JudgeRunner judgeRunner;
    @Autowired
    private SubmissionService submissionService;

    private final long timeLimit = 6000L;
    private final long memoryLimit = 256 * 1024L;
    private final Validator validator = SimpleValidator.PE_AS_ACCEPTED;

    private final String key;
    private final Checker checker;
    private final Path path;
    private final JudgeData judgeData;
    private Path work;

    public JudgeRunnerTest(String key, Checker checker, Path path)
            throws URISyntaxException, IOException {
        this.checker = checker;
        this.path = path;
        this.key = key;
        this.judgeData = new JudgeData(Paths.get(JudgeRunnerTest.class.getResource("/sample/data").toURI()));
    }

    private int findFirstLanguageByExtension(String extension) {
        return languageMapper.findAll().stream()
                .filter(language -> language.getSourceExtension().toLowerCase().equals(extension.toLowerCase()))
                .map(Language::getId).findFirst().orElseThrow(RuntimeException::new);
    }

    @Before
    public void init() throws Exception {
        work = Files.createDirectories(Paths.get("target/work/judgeRunnerTest").resolve(key));
        Path[] groovyJars = GroovyHolder.getPaths();
        assertThat("groovyJars", groovyJars, not(emptyArray()));
        for (Path groovyJar : groovyJars) {
            Files.copy(groovyJar, work.resolve(groovyJar.getFileName().toString()));
        }
        String cp = Arrays.stream(groovyJars).map(p -> p.getFileName().toString()).collect(Collectors.joining(File.pathSeparator));
        String executeCommand = build("java", "-cp", cp, groovy.ui.GroovyMain.class.getName(), "Main.groovy");
        Language groovy = Language.builder()
                .name("groovy")
                .sourceExtension("groovy")
                .executeCommand(executeCommand)
                .executableExtension("groovy")
                .description("")
                .timeFactor(2)
                .build();
        log.warn("Language groovy: {}", groovy);
        languageMapper.save(groovy);
    }

    @Test
    public void test() throws IOException {
        String extension = getExtension(path);
        int languageId = findFirstLanguageByExtension(EXTENSION_MAP.get(extension));
        Language language = languageMapper.findOne(languageId);
        Objects.requireNonNull(language, "language " + languageId + " not exists");
        String source = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        RunRecord runRecord = RunRecord.builder()
                .source(source)
                .timeLimit(timeLimit)
                .memoryLimit(memoryLimit)
                .language(language)
                .build();

        RunResult runResult = judgeRunner.run(runRecord, work, judgeData, validator, false);

        int expectScore = SPECIAL_SCORE.getOrDefault(key, checker.getScore());
        String extectedCaseResult = ResultType.getCaseScoreDescription(checker.getStatus());

        assertNull("type will either be null or COMPILATION_ERROR,"
                + " if got other result, please modify this file",
                runResult.getType());
        List<SubmissionDetailDTO> details = submissionService.parseSubmissionDetail(runResult.getMessage());
        String testMessage = key + " " + details + " " + extectedCaseResult;
        assertEquals(testMessage, expectScore, runResult.getScore());
        boolean matches = details.stream().anyMatch(detail -> extectedCaseResult.equals(detail.getResult()));
        assertTrue(testMessage, matches);
    }

}
