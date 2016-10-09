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
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Standing;
import cn.edu.zjnu.acm.judge.domain.User;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ContestMapperTest {

    @Autowired
    private ContestMapper instance;
    private Locale locale = Locale.SIMPLIFIED_CHINESE;

    /**
     * Test of standing method, of class ContestMapper.
     */
    @Test
    public void testStanding() {
        log.info("standing");
        long contest = 1058L;
        List<Standing> result = instance.standing(contest);
        log.info("{}", result);
    }

    /**
     * Test of getProblems method, of class ContestMapper.
     */
    @Test
    public void testGetProblems() {
        log.info("getProblems");
        long contestId = 0L;
        List<Problem> expResult = Arrays.asList();
        List<Problem> result = instance.getProblems(contestId, null, locale.getLanguage());
        assertEquals(expResult, result);
    }

    /**
     * Test of getProblems method, of class ContestMapper.
     */
    @Test
    public void testGetProblemsNullLocale() {
        log.info("getProblems");
        long contestId = 0L;
        List<Problem> expResult = Arrays.asList();
        List<Problem> result = instance.getProblems(contestId, null, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProblems method, of class ContestMapper.
     */
    @Test
    public void testGetUserProblems() {
        log.info("getProblems");
        long contestId = 0L;
        String userId = "'";
        List<Problem> expResult = Arrays.asList();
        List<Problem> result = instance.getProblems(contestId, userId, locale.getLanguage());
        assertEquals(expResult, result);
    }

    /**
     * Test of updateContestOrder method, of class ContestMapper.
     */
    @Test
    public void testUpdateContestOrder() {
        log.info("updateContestOrder");
        long contestId = 0L;
        instance.updateContestOrder(contestId, 0);
    }

    /**
     * Test of deleteContestProblem method, of class ContestMapper.
     */
    @Test
    public void testDeleteContestProblem() {
        log.info("deleteContestProblem");
        long contestId = 0L;
        long problemId = 0L;
        instance.deleteContestProblem(contestId, problemId);
    }

    /**
     * Test of findOne method, of class ContestMapper.
     */
    @Test
    public void testFindOne() {
        log.info("findOne");
        long contestId = 0L;
        Contest expResult = null;
        Contest result = instance.findOne(contestId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getProblem method, of class ContestMapper.
     */
    @Test
    public void testGetProblem() {
        log.info("getProblem");
        long contestId = 0L;
        long problemOrder = 0L;
        Problem expResult = null;
        Problem result = instance.getProblem(contestId, problemOrder);
        assertEquals(expResult, result);
    }

    /**
     * Test of findOneByIdAndDisabledFalse method, of class ContestMapper.
     */
    @Test
    public void testFindOneByIdAndDisabledFalse() {
        log.info("findOneByIdAndDisabledFalse");
        long contestId = 0L;
        Contest expResult = null;
        Contest result = instance.findOneByIdAndDisabledFalse(contestId);
        assertEquals(expResult, result);
    }

    /**
     * Test of attenders method, of class ContestMapper.
     */
    @Test
    public void testAttenders() {
        log.info("attenders");
        long contestId = 0L;
        List<User> expResult = Arrays.asList();
        List<User> result = instance.attenders(contestId);
        assertEquals(expResult, result);
    }

    /**
     * Test of enable method, of class ContestMapper.
     */
    @Test
    public void testEnable() {
        log.info("enable");
        long contestId = 0L;
        instance.enable(contestId);
    }

    /**
     * Test of disable method, of class ContestMapper.
     */
    @Test
    public void testDisable() {
        log.info("disable");
        long contestId = 0L;
        instance.disable(contestId);
    }

    /**
     * Test of getProblemIdInContest method, of class ContestMapper.
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testGetProblemIdInContest() {
        log.info("getProblemIdInContest");
        long contestId = 0L;
        long problemId = 0L;
        instance.getProblemIdInContest(contestId, problemId);
    }

    /**
     * Test of findAll method, of class ContestMapper.
     */
    @Test
    public void testFindAll() {
        log.info("findAll");
        instance.findAll();
    }

    /**
     * Test of pending method, of class ContestMapper.
     */
    @Test
    public void testPending() {
        log.info("pending");
        instance.pending();
    }

    /**
     * Test of past method, of class ContestMapper.
     */
    @Test
    public void testPast() {
        log.info("past");
        instance.past();
    }

    /**
     * Test of current method, of class ContestMapper.
     */
    @Test
    public void testCurrent() {
        log.info("current");
        instance.current();
    }

    /**
     * Test of contests method, of class ContestMapper.
     */
    @Test
    public void testContests() {
        log.info("contests");
        instance.contests();
    }

    /**
     * Test of runningAndScheduling method, of class ContestMapper.
     */
    @Test
    public void testRunningAndScheduling() {
        log.info("runningAndScheduling");
        instance.runningAndScheduling();
    }

    /**
     * Test of updateProblemTitle method, of class ContestMapper.
     */
    @Test
    public void testUpdateProblemTitle() {
        log.info("updateProblemTitle");
        long contestId = 0L;
        long problemId = 0L;
        // nothing happen for there will neither be a contest nor a problem id 0.
        instance.updateProblemTitle(contestId, problemId);
    }

}
