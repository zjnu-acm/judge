/*
 * Copyright 2014 zhanhb.
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
package cn.edu.zjnu.acm.judge.exception;

import java.util.Objects;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 *
 * @author zhanhb
 */
public class MessageException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    @Getter
    private final HttpStatus httpStatus;

    public MessageException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public MessageException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = Objects.requireNonNull(httpStatus);
    }

    // no stack trace
    @Override
    public MessageException fillInStackTrace() {
        return this;
    }

}
