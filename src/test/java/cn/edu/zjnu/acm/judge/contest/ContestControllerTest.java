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
package cn.edu.zjnu.acm.judge.contest;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 * @author zhanhb
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ContestControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ContestController instance;
    private final Locale locale = Locale.SIMPLIFIED_CHINESE;

    /**
     * Test of standingHtml method, of class ContestController.
     */
    @Test
    public void testStandingHtml() {
        log.info("standingHtml");
        long cid = 1058;
        ExtendedModelMap map = new ExtendedModelMap();
        String result = instance.standingHtml(cid, map, locale);
        assertEquals("contests/standing", result);
        assertNotNull(map.get("problems"));
    }

    @Test
    public void testMock() throws Exception {
        MockMvc mvc = webAppContextSetup(context).build();
        mvc.perform(get("/contests/{id}/standing", 1058))
                .andExpect(status().isOk());
    }

}
