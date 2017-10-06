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
package cn.edu.zjnu.acm.judge.exception;

import java.util.Objects;

/**
 *
 * @author zhanhb
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final BusinessCode code;
    private final Object[] params;

    public BusinessException(BusinessCode errorCode, Object... params) {
        super(errorCode.name());
        this.code = errorCode;
        this.params = Objects.requireNonNull(params, "params").clone();
    }

    public BusinessCode getCode() {
        return code;
    }

    public Object[] getParams() {
        return params.clone();
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
