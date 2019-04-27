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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.domain.DomainLocale;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author zhanhb
 */
public interface LocaleService {

    List<DomainLocale> findAll();

    @Nullable
    DomainLocale findOne(String id);

    @Nullable
    String resolve(Locale locale);

    List<DomainLocale> support(boolean all);

    DomainLocale toDomainLocale(Locale locale, Locale inLocale);

    DomainLocale toDomainLocale(String localeName, boolean supportOnly);

    @Nonnull
    Locale toSupported(Locale locale);

}
