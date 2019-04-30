/*
 * Copyright 2019 ZJNU ACM.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author zhanhb
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class KaptchaServletTest {

	@Autowired
	private TestRestTemplate restTemplate;

    /**
     * Test of doGet method, of class KaptchaServlet.
     *
     * @see KaptchaServlet#doGet(HttpServletRequest, HttpServletResponse)
     * @see RegistrationBeanConfiguration#kaptcha()
     */
    @Test
    public void testDoGet() throws Exception {
        log.info("doGet");
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                "/images/rand.jpg",
                HttpMethod.GET,
                new HttpEntity<Void>(new HttpHeaders()),
                byte[].class);
        assertTrue(responseEntity.getHeaders().getContentType()
                .isCompatibleWith(MediaType.IMAGE_JPEG));
        assertNotEquals(0,responseEntity.getBody().length);
        assertFalse(responseEntity.getHeaders().get("Set-Cookie").isEmpty());
    }

}
