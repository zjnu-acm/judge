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
import cn.edu.zjnu.acm.judge.domain.User;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class UserMapperTest {

    @Autowired
    private UserMapper instance;

    /**
     * Test of findOne method, of class UserMapper.
     */
    @Ignore
    @Test
    public void testFindOne() {
        log.info("findOne");
        String id = "coach";
        String expResult = id;
        String result = instance.findOne(id).getId();
        assertEquals(expResult, result);
    }

    @Test
    public void testNeighbours() {
        log.info("neighbours");
        String userId = "coach";
        List<User> result = instance.neighbours(userId, 4);
        log.info("{}", result.size());
        log.info("{}", result);
    }

    /**
     * Test of findAll method, of class UserMapper.
     */
    @Test
    public void testFindAll() {
        log.info("findAll");
        Pageable pageable = new PageRequest(0, 50);
        List<User> result = instance.findAll(pageable);
        log.debug("{}", result);

        pageable = new PageRequest(0, 50, new Sort(new Sort.Order(Sort.Direction.DESC, "solved"), new Sort.Order(Sort.Direction.ASC, "submit")));
        result = instance.findAll(pageable);
        log.debug("{}", result);

        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "solved");
        ArrayList<Sort.Order> list = new ArrayList<>(1);
        list.add(order);
        pageable = new PageRequest(0, 50, new Sort(list));
        list.clear();
        result = instance.findAll(pageable);
        log.debug("{}", result);
    }

}
