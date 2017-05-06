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
package cn.edu.zjnu.acm.judge.config;

import cn.edu.zjnu.acm.judge.Application;
import com.github.zhanhb.ckfinder.connector.autoconfigure.CKFinderProperties;
import javax.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CKFinderTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private CKFinderProperties properties;
    @Autowired
    private Filter[] filters;

    @Test
    public void test() throws Exception {
        log.info("filters: {}", (Object) filters);
        MockMvc mvc = webAppContextSetup(context).addFilters(filters).build();
        String[] path = properties.getServlet().getPath();
        for (String string : path) {
            mvc.perform(get(string))
                    .andDo(print())
                    .andExpect(forwardedUrl("/unauthorized"));
        }

    }

}
