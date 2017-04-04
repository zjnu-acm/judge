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
package cn.edu.zjnu.acm.judge.security.password;

import cn.edu.zjnu.acm.judge.Application;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class BcryptPasswordEncoderTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Ignore("not safe")
    @Test
    public void testEncode() throws SQLException {
        int c = 0;
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("select user_id,password from users where length(password)<31");
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String password = rs.getString("password");
                    String userId = rs.getString("user_id");
                    try (PreparedStatement ps1 = conn.prepareStatement("update users set password=? where user_id=? and password=?")) {
                        ps1.setString(1, passwordEncoder.encode(password));
                        ps1.setString(2, userId);
                        ps1.setString(3, password);
                        ps1.executeUpdate();
                    }
                    c++;
                    if (c % 50 == 0) {
                        log.info("{}", c);
                        conn.commit();
                    }
                }
            }
            if (c % 50 == 0) {
                log.info("{}", c);
                conn.commit();
            }
        }
    }

}
