/*
 * Copyright 2017 zhanhb.
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
package com.github.zhanhb.download;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @author zhanhb
 */
public class ETag {

    public static final Function<ActionContext, String> DEFAULT_ETAG_MAPPER
            = context -> {
                BasicFileAttributes attributes = context.get(BasicFileAttributes.class);
                return attributes.size() + "-" + attributes.lastModifiedTime().toMillis();
            };

    public static ETag weak() {
        return weak(DEFAULT_ETAG_MAPPER);
    }

    public static ETag strong() {
        return strong(DEFAULT_ETAG_MAPPER);
    }

    public static ETag weak(Function<ActionContext, String> eTagMapper) {
        return new ETag(true, eTagMapper);
    }

    public static ETag strong(Function<ActionContext, String> eTagMapper) {
        return new ETag(false, eTagMapper);
    }

    private final String prefix;
    private final Function<ActionContext, String> eTagMapper;

    private ETag(boolean weak, Function<ActionContext, String> eTagMapper) {
        this.prefix = weak ? "W/" : "";
        this.eTagMapper = Objects.requireNonNull(eTagMapper, "eTagMapper");
    }

    public String getValue(ActionContext context) {
        String result = Objects.requireNonNull(eTagMapper.apply(context));
        return prefix + "\"" + result + "\"";
    }

}
