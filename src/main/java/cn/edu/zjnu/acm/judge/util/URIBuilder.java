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

import com.github.zhanhb.ckfinder.download.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author zhanhb
 */
public class URIBuilder {

    /**
     * https://tools.ietf.org/html/rfc3986#section-3.4
     */
    private static final URLEncoder QUERY_ENCODER = new URLEncoder("-._~!$'()*,;:@/?");
    private static final URLEncoder PATH_ENCODER = new URLEncoder("-._~!$'()*,;:@/");

    public static URIBuilder fromRequest(HttpServletRequest request) {
        return new URIBuilder(request.getServletPath(), request.getParameterMap());
    }

    private String path;
    private final Map<String, String[]> query;

    private URIBuilder(String path, Map<String, String[]> query) {
        this.path = path;
        this.query = new LinkedHashMap<>(query.size());
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
        StringBuilder sb = new StringBuilder(PATH_ENCODER.encode(path));
        boolean first = true;
        for (Map.Entry<String, String[]> entry : query.entrySet()) {
            String key = QUERY_ENCODER.encode(entry.getKey());
            for (String string : entry.getValue()) {
                sb.append(first ? '?' : '&');
                sb.append(QUERY_ENCODER.encode(key));
                sb.append("=");
                sb.append(QUERY_ENCODER.encode(string));
                first = false;
            }
        }
        return sb.toString();
    }

}
