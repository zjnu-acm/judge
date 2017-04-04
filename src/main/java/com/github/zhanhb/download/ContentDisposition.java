/*
 * Copyright 2015 zhanhb.
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

import java.nio.file.Path;
import java.util.function.Function;
import javax.annotation.Nullable;

import static com.github.zhanhb.download.ContentDispositionEncoder.wrapper;

/**
 *
 * @author zhanhb
 */
public interface ContentDisposition {

    public static final Function<Path, String> DEFAULT_NAME_MAPPER = path -> path.getFileName().toString();

    public static ContentDisposition attachment() {
        return attachment(DEFAULT_NAME_MAPPER);
    }

    public static ContentDisposition inline() {
        return inline(DEFAULT_NAME_MAPPER);
    }

    public static ContentDisposition attachment(Function<Path, String> nameMapper) {
        return wrapper("attachment", nameMapper);
    }

    public static ContentDisposition inline(Function<Path, String> nameMapper) {
        return wrapper("inline", nameMapper);
    }

    public static ContentDisposition none() {
        return __ -> null;
    }

    // https://tools.ietf.org/html/rfc6266
    @Nullable
    String getValue(ActionContext context);

}
