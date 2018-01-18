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
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author zhanhb
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ContestOnlyServiceTest {

    @Autowired
    private ContestOnlyService contestOnlyService;
    @Autowired
    private MockMvc mvc;

    /**
     * Test of getContestOnly method, of class ContestOnlyService.
     */
    @Test
    public void testGetContestOnly() throws Exception {
        log.info("getContestOnly");
        Long old = contestOnlyService.getContestOnly();
        try {
            contestOnlyService.setContestOnly(null);
            request(HttpStatus.OK);

            contestOnlyService.setContestOnly(Long.MIN_VALUE);
            assertEquals(Long.valueOf(Long.MIN_VALUE), contestOnlyService.getContestOnly());
            request(HttpStatus.BAD_REQUEST);

            contestOnlyService.setContestOnly(Long.MIN_VALUE);
            request(HttpStatus.BAD_REQUEST);
            assertEquals(Long.valueOf(Long.MIN_VALUE), contestOnlyService.getContestOnly());

            contestOnlyService.setContestOnly(null);
            request(HttpStatus.OK);
            assertEquals(null, contestOnlyService.getContestOnly());
        } finally {
            contestOnlyService.setContestOnly(old);
        }
    }

    private void request(HttpStatus status) throws Exception {
        mvc.perform(get("/registerpage"))
                .andExpect(status().is(status.value()));
        mvc.perform(get("/register"))
                .andExpect(status().is(status.value()));
    }

}
