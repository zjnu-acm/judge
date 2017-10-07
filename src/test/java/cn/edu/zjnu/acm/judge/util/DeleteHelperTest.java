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
package cn.edu.zjnu.acm.judge.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author zhanhb
 */
@Slf4j
@Transactional
public class DeleteHelperTest {

    /**
     * Test of delete method, of class DeleteHelper.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testDelete() throws IOException {
        log.info("delete");
        Path path = Paths.get("target/test1.txt");
        Files.createFile(path);
        assertTrue(!Files.notExists(path));
        DeleteHelper.delete(path);
        assertTrue(!Files.exists(path));
    }

    /**
     * Test of delete method, of class DeleteHelper.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testDeleteDirectory() throws IOException {
        log.info("delete");
        Path d = Paths.get("target/test1.txt");
        Path path = d.resolve("target/test1.txt");
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        DeleteHelper.delete(d);
        assertTrue(!Files.exists(d));
    }

    @Test
    public void testNotExists() throws IOException {
        DeleteHelper.delete(Paths.get("target/test1"));
    }

    @Test
    public void testDeleteEmptyDirectory() throws IOException {
        log.info("delete");
        Path d = Paths.get("target/test1.txt");
        Files.createDirectories(d);
        DeleteHelper.delete(d);
        assertTrue(!Files.exists(d));
    }

}
