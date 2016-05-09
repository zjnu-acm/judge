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
package cn.edu.zjnu.acm.judge.util;

import com.github.zhanhb.download.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author zhanhb
 */
public class URLBuilder {

    private static final URLEncoder ENCODER = new URLEncoder("-_.*~-<>");

    public static URLBuilder fromRequest(HttpServletRequest request) {
        return new URLBuilder(request.getServletPath(), request.getParameterMap());
    }

    private final String path;
    private final Map<String, String[]> query;

    private URLBuilder(String path, Map<String, String[]> query) {
        this.path = path;
        this.query = new LinkedHashMap<>(query);
    }

    public URLBuilder replaceQueryParam(String name, String... values) {
        if (values == null || values.length == 0) {
            query.remove(name);
        } else {
            query.put(name, values.clone());
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(path);
        boolean first = true;
        for (Map.Entry<String, String[]> entry : query.entrySet()) {
            String key = encode(entry.getKey());
            String[] value = entry.getValue();
            if (value != null) {
                for (String string : value) {
                    if (first) {
                        first = false;
                        sb.append('?');
                    } else {
                        sb.append('&');
                    }
                    sb.append(key).append("=").append(encode(string));
                }
            }
        }
        return sb.toString();
    }

    private String encode(String string) {
        return ENCODER.encode(string);
    }

}
