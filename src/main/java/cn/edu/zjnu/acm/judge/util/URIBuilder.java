/*
 * Copyright 2016-2019 ZJNU ACM.
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

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author zhanhb
 */
public class URIBuilder {

    public static URIBuilder fromRequest(HttpServletRequest request) {
        return new URIBuilder(request.getServletPath(), request.getParameterMap());
    }

    private String path;
    private final Map<String, String[]> query;

    private URIBuilder(String path, Map<String, String[]> query) {
        this.path = path;
        this.query = Maps.newLinkedHashMapWithExpectedSize(query.size());
        query.forEach(this::replaceQueryParam);
    }

    public URIBuilder replaceQueryParam(String name, String... values) {
        if (values == null || values.length == 0) {
            query.remove(name);
        } else {
            String[] clone = values.clone();
            for (String string : clone) {
                Objects.requireNonNull(string);
            }
            query.put(name, clone);
        }
        return this;
    }

    public URIBuilder replacePath(String path) {
        this.path = Objects.toString(path, "");
        return this;
    }

    @Override
    public String toString() {
        Iterator<Map.Entry<String, String[]>> it = query.entrySet().iterator();
        String value = URLEncoder.PATH.encode(path);
        if (!it.hasNext()) {
            return value;
        }
        URLEncoder qe = URLEncoder.QUERY;
        StringBuilder sb = new StringBuilder(value).append('?');
        while (true) {
            Map.Entry<String, String[]> entry = it.next();
            String key = qe.encode(entry.getKey());
            for (String val : entry.getValue()) {
                sb.append('&');
                sb.append(key);
                sb.append('=');
                sb.append(qe.encode(val));
            }
            if (!it.hasNext()) {
                return sb.toString();
            }
        }
    }

}
