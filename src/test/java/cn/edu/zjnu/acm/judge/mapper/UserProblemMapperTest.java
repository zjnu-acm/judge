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
import cn.edu.zjnu.acm.judge.domain.UserProblem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
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
        UserProblem expResult = null;
        UserProblem result = instance.findOne(userId, problemId);
    }

    /**
     * Test of update method, of class UserProblemMapper.
     */
    @Test
    public void testUpdate() {
        log.info("update");
        String userId = "coach";
        long problemId = 1000L;
        long expResult = 0L;
        long result = instance.update(userId, problemId);
    }

    /**
     * Test of updateUser method, of class UserProblemMapper.
     */
    @Test
    public void testUpdateUser() {
        log.info("updateUser");
        String userId = "coach";
        long expResult = 0L;
        long result = instance.updateUser(userId);
    }

    /**
     * Test of updateProblem method, of class UserProblemMapper.
     */
    @Test
    public void testUpdateProblem() {
        log.info("updateProblem");
        long problemId = 1000;
        long expResult = 0L;
        long result = instance.updateProblem(problemId);
    }

    /**
     * Test of updateUsers method, of class UserProblemMapper.
     */
    @Test
    public void testUpdateUsers() {
        log.info("updateUsers");
        instance.updateUsers();
    }

}
