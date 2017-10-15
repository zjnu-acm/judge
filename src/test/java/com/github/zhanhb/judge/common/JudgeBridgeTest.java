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
package com.github.zhanhb.judge.common;

import cn.edu.zjnu.acm.judge.util.DeleteHelper;
import com.sun.jna.Platform;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class JudgeBridgeTest {

    @BeforeClass
    public static void setUpClass() {
        assumeTrue("not windows", Platform.isWindows());
    }

    private static String build(String... args) {
        return Arrays.stream(args)
                .map(s -> new StringTokenizer(s).countTokens() > 1 ? '"' + s + '"' : s)
                .collect(Collectors.joining(" "));
    }

    private final boolean stopOnError = false;
    private final Validator validator = SimpleValidator.NORMAL;
    private final String groovy = getGroovy(System.getProperty("java.class.path"));
    private Path program;
    private Path data;
    private Path input;
    private Path output;
    private Path tmp;

    @Before
    public void setUp() throws Exception {
        URI uri = JudgeBridgeTest.class.getClassLoader().getResource("sample/program").toURI();
        program = Paths.get(uri);
        data = program.resolve("../data").toRealPath();
        input = data.resolve("b.in");
        output = data.resolve("b.out");
        tmp = Files.createDirectories(Paths.get("target/tmp"));
    }

    @Test
    public void testAC() throws Exception {
        checkGroovy(Checker.ac);
    }

    @Test
    public void testMle() throws Exception {
        checkGroovy(Checker.mle);
    }

    @Test
    public void testOle() throws Exception {
        checkGroovy(Checker.ole);
    }

    @Test
    public void testRe() throws Exception {
        checkGroovy(Checker.re);
    }

    @Test
    public void testTle() throws Exception {
        checkGroovy(Checker.tle);
    }

    @Test
    public void testWa() throws Exception {
        checkGroovy(Checker.wa);
    }

    @Test
    public void testPE() throws Exception {
        checkGroovy(Checker.pe);
    }

    @After
    public void tearDown() throws IOException {
        DeleteHelper.delete(tmp);
    }

    @SneakyThrows
    private void test(String executable, Checker checker) {
        Options options = Options.builder().command(build("java", "-cp", groovy, groovy.ui.GroovyMain.class.getName(), executable))
                .inputFile(input)
                .outputFile(tmp.resolve(output.getFileName()))
                .standardOutput(output)
                .errFile(tmp.resolve("NUL"))
                .memoryLimit(256 * 1024 * 1024)
                .outputLimit(16 * 1024 * 1024)
                .workDirectory(tmp)
                .timeLimit(6000)
                .build();
        ExecuteResult er = JudgeBridge.INSTANCE.judge(new Options[]{options}, stopOnError, validator)[0];
        log.info("{}", er);
        assertEquals(executable, checker.getStatus(), er.getCode());
    }

    private void checkGroovy(Checker checker) throws IOException {
        Path path = program.resolve(checker.name());
        Files.list(path)
                .filter(p -> p.getFileName().toString().endsWith(".groovy"))
                .map(Object::toString)
                .forEach(
                        executable -> test(executable, checker)
                );
    }

    private String getGroovy(String property) {
        return Arrays.stream(property.split(File.pathSeparator))
                .filter(s -> s.contains("groovy-"))
                .collect(Collectors.joining(File.pathSeparator));
    }

}
