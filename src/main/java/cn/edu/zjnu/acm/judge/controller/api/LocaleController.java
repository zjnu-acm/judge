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
package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.domain.DomainLocale;
import cn.edu.zjnu.acm.judge.service.LocaleService;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 *
 * @author zhanhb
 */
@RequestMapping(value = "/api/locales", produces = APPLICATION_JSON_VALUE)
@RestController
@Secured("ROLE_ADMIN")
public class LocaleController {

    @Autowired
    private LocaleService localeService;

    @GetMapping("current")
    public DomainLocale current(Locale locale) {
        return localeService.toDomainLocale(localeService.toSupported(locale), locale);
    }

    @GetMapping("{id}")
    public DomainLocale findOne(@PathVariable("id") String name,
            @RequestParam(value = "support", required = false, defaultValue = "false") boolean support) {
        return localeService.toDomainLocale(name, support);
    }

    @GetMapping
    public List<DomainLocale> findAll() {
        return localeService.findAll();
    }

    @GetMapping(params = "all")
    public List<DomainLocale> supported(@RequestParam("all") boolean all) {
        return localeService.support(all);
    }

}
