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
import cn.edu.zjnu.acm.judge.domain.Message;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@SuppressWarnings("deprecation")
@Transactional
@WebAppConfiguration
public class MessageMapperTest {

    @Autowired
    private MessageMapper instance;

    /**
     * Test of nextId method, of class MessageMapper.
     */
    @Test
    public void testNextId() {
        log.info("nextId");
        instance.nextId();
    }

    /**
     * Test of findOne method, of class MessageMapper.
     * @see MessageMapper#findOne(long)
     */
    @Test
    public void testFindOne() {
        log.info("findOne");
        long id = 0;
        Message expResult = null;
        Message result = instance.findOne(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum method, of
     * class MessageMapper.
     * @see MessageMapper#findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(long, long)
     */
    @Test
    public void testFindAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum() {
        log.info("findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum");
        long thread = -1;
        long orderNum = -1;
        instance.findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(thread, orderNum);
    }

    /**
     * Test of updateOrderNumByThreadIdAndOrderNumGreaterThan method, of class
     * MessageMapper.
     * @see MessageMapper#updateOrderNumByThreadIdAndOrderNumGreaterThan(long, long)
     */
    @Test
    public void testUpdateOrderNumByThreadIdAndOrderNumGreaterThan() {
        log.info("updateOrderNumByThreadIdAndOrderNumGreaterThan");
        long thread = -1L;
        long orderNum = -1L;
        instance.updateOrderNumByThreadIdAndOrderNumGreaterThan(thread, orderNum);
    }

    /**
     * Test of updateThreadIdByThreadId method, of class MessageMapper.
     * @see MessageMapper#updateThreadIdByThreadId(long, long)
     */
    @Test
    public void testUpdateThreadIdByThreadId() {
        log.info("updateThreadIdByThreadId");
        long nextId = -1;
        long original = -1;
        instance.updateThreadIdByThreadId(nextId, original);
    }

    /**
     * Test of findAllByThreadIdBetween method, of class MessageMapper.
     * @see MessageMapper#findAllByThreadIdBetween(Long, Long, Long, Integer)
     */
    @Test
    public void testFindAllByThreadIdBetween() {
        log.info("findAllByThreadIdBetween");
        Long[] arr = {-1L, 0L, 1L, null};
        Integer[] array = {0, 1, -1, null};
        for (Long min : arr) {
            for (Long max : arr) {
                for (Long problemId : arr) {
                    for (Integer limit : array) {
                        if (!Objects.equals(limit, -1)) {
                            instance.findAllByThreadIdBetween(min, max, problemId, limit);
                        }
                    }
                }
            }
        }
    }

    /**
     * Test of mint method, of class MessageMapper.
     * @see MessageMapper#mint(long, Long, int, long)
     */
    @Test
    public void testMint() {
        log.info("mint");
        long top = 0L;
        Long problemId = null;
        int limit = 0;
        long coalesce = 0L;
        long expResult = 0L;
        long result = instance.mint(top, problemId, limit, coalesce);
        assertEquals(expResult, result);
    }

    /**
     * Test of maxt method, of class MessageMapper.
     * @see MessageMapper#maxt(long, Long, int, long)
     */
    @Test
    public void testMaxt() {
        log.info("maxt");
        long top = 0L;
        Long problemId = null;
        int limit = 0;
        long coalesce = 0L;
        long expResult = 0L;
        long result = instance.maxt(top, problemId, limit, coalesce);
        assertEquals(expResult, result);
    }

}
