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
package cn.edu.zjnu.acm.judge.data.excel;

import cn.edu.zjnu.acm.judge.util.excel.Excel;
import lombok.Data;

/**
 *
 * @author zhanhb
 */
@Data
public class Account {

    @Excel(name = "id", order = 1)
    private String id;
    @Excel(name = "password", order = 2)
    private String password;
    @Excel(name = "nick", order = 3)
    private String nick;
    @Excel(name = "school", order = 4)
    private String school;
    @Excel(name = "email", order = 5)
    private String email;
    private Boolean exists;

}
