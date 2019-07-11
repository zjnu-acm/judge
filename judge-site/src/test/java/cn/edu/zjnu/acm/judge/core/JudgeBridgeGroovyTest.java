/*
 * Copyright 2016 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.core;

import cn.edu.zjnu.acm.judge.util.DeleteHelper;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static cn.edu.zjnu.acm.judge.test.PlatformAssuming.assumingWindows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
@Slf4j
public class JudgeBridgeGroovyTest {

    private static Path work;
    private static Path input;
    private static Path output;
    private static Path groovy;
    private static Path program;

    private static String build(String... args) {
        return Arrays.stream(args)
                .map(s -> new StringTokenizer(s).countTokens() > 1 ? '"' + s + '"' : s)
                .collect(Collectors.joining(" "));
    }

    public static List<Arguments> data() throws Exception {
        Checker[] values = Checker.values();
        ArrayList<Arguments> list = new ArrayList<>(values.length);
        for (Checker checker : values) {
            Path path = program.resolve(checker.name());
            Files.list(path)
                    .filter(p -> p.getFileName().toString().endsWith(".groovy"))
                    .map(Object::toString)
                    .forEach(executable -> list.add(arguments(checker, executable)));
        }
        return list;
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
        assumingWindows();
        work = Files.createDirectories(Paths.get("target", "work", "judgeBridgeTest"));
        URI uri = JudgeBridgeGroovyTest.class.getResource("/sample/program").toURI();
        program = Paths.get(uri);
        Path data = program.resolve("../data").toRealPath();
        input = data.resolve("b.in");
        output = data.resolve("b.out");
        Path[] groovyJars = GroovyHolder.getPaths();
        assertThat(groovyJars).describedAs("groovyJars").hasSize(1);
        Path groovyPath = groovyJars[0];
        groovy = Files.copy(groovyPath, work.resolve(groovyPath.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
        if (work != null) {
            DeleteHelper.delete(work);
        }
    }

    private final Validator validator = SimpleValidator.NORMAL;

    private JudgeBridge judgeBridge;

    @BeforeEach
    public void setUp() {
        judgeBridge = new JudgeBridge();
    }

    @AfterEach
    public void tearDown() {
        judgeBridge.close();
    }

    @ParameterizedTest
    @MethodSource("data")
    public void test(Checker checker, String executable) throws IOException {
        Options options = Options.builder().command(build("java", "-cp", groovy.getFileName().toString(), groovy.ui.GroovyMain.class.getName(), executable))
                .inputFile(input)
                .outputFile(work.resolve(output.getFileName()))
                .standardOutput(output)
                .errFile(work.resolve("NUL"))
                .memoryLimit(256 * 1024 * 1024)
                .outputLimit(16 * 1024 * 1024)
                .workDirectory(work)
                .timeLimit(6000)
                .build();
        boolean stopOnError = false;
        ExecuteResult er = judgeBridge.judge(new Options[]{options}, stopOnError, validator)[0];
        Status result = er.getCode();
        Status expect = checker.getStatus();
        assertThat(result)
                .describedAs("executable: %s, exptect %s, got %s",
                        executable, expect, result)
                .isEqualTo(expect);
    }

}
