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
package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class UserProblemMapperTest {

    @Autowired
    private UserProblemMapper instance;

    /**
     * Test of init method, of class UserProblemMapper.
     */
    @Test
    public void testInit() {
        log.info("init");
        instance.init();
    }

    /**
     * Test of findOne method, of class UserProblemMapper.
     */
    @Test
    public void testFindOne() {
        log.info("findOne");
        String userId = "coach";
        long problemId = 1000;
        instance.findOne(userId, problemId);
    }

    /**
     * Test of update method, of class UserProblemMapper.
     */
    @Test
    public void testUpdate() {
        log.info("update");
        String userId = "coach";
        long problemId = 1000L;
        instance.update(userId, problemId);
    }

    /**
     * Test of updateUser method, of class UserProblemMapper.
     */
    @Test
    public void testUpdateUser() {
        log.info("updateUser");
        String userId = "coach";
        instance.updateUser(userId);
    }

    /**
     * Test of updateProblem method, of class UserProblemMapper.
     */
    @Test
    public void testUpdateProblem() {
        log.info("updateProblem");
        long problemId = 1000;
        instance.updateProblem(problemId);
    }

    /**
     * Test of updateUsers method, of class UserProblemMapper.
     */
    @Test
    public void testUpdateUsers() {
        log.info("updateUsers");
        instance.updateUsers();
    }

    /**
     * Test of updateProblems method, of class UserProblemMapper.
     */
    @Test
    public void testUpdateProblems() {
        log.info("updateProblems");
        instance.updateProblems();
    }

    /**
     * Test of findAllSolvedByUserId method, of class
 UserProblemMapper.
     */
    @Test
    public void testFindAllByUserIdAndAcceptedNot0() {
        log.info("findAllByUserIdAndAcceptedNot0");
        String userId = "";
        instance.findAllSolvedByUserId(userId);
    }

    /**
     * Test of findAllByUserId method, of class UserProblemMapper.
     */
    @Test
    public void testFindAllByUserId() {
        log.info("findAllByUserId");
        String userId = "";
        instance.findAllByUserId(userId);
    }

}
