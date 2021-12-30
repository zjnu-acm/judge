/*
 * Copyright 2014-2019 zhanhb.
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
package cn.edu.zjnu.acm.judge.controller;

import com.github.zhanhb.ckfinder.connector.api.BasePathBuilder;
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import com.github.zhanhb.ckfinder.download.PathPartial;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

/**
 * @author zhanhb
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class CKFinderController {

    private static final MediaType APPLICATION_JAVASCRIPT = MediaType.parseMediaType("application/javascript");
    private final AntPathMatcher matcher = new AntPathMatcher();
    private final PathPartial pathPartial = PathPartial.builder().build();

    private final BasePathBuilder basePathBuilder;
    private final MessageSource messageSource;

    private Path toPath(String path) {
        log.info(path);
        // ignore all dot directories, such as a/./a, a/../b, .git/info
        StringTokenizer st = new StringTokenizer(path, "/\\");
        while (st.hasMoreTokens()) {
            if (st.nextToken().startsWith(".")) {
                return null;
            }
        }
        try {
            return FileUtils.resolve(basePathBuilder.getBasePath(), path);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    @GetMapping("/userfiles/{first}/**")
    public void attachment(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("first") String first) throws IOException, ServletException {
        final String uri = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String pattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        String rest = matcher.extractPathWithinPattern(pattern, uri);
        String path = StringUtils.isEmpty(rest) ? first : first + "/" + rest;
        pathPartial.service(request, response, toPath(path));
    }

    // Can't add produces, for the request head Accept is */*
    @ResponseBody
    @GetMapping("webjars/ckfinder/2.6.2.1/config.js")
    public ResponseEntity<String> configJs(Locale locale) {
        final String indent = "    ";
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            pw.println("CKFinder.customConfig = function(config) {");
            pw.println(indent + "// Define changes to default configuration here.");
            pw.println(indent + "// For the list of available options, check:");
            pw.println(indent + "// http://docs.cksource.com/ckfinder_2.x_api/symbols/CKFinder.config.html");
            pw.println();
            pw.println(indent + "// Sample configuration options:");
            pw.println(indent + "// config.uiColor = '#BDE31E';");
            if (locale != null) {
                String fallback = locale.toLanguageTag().toLowerCase(Locale.US);
                String lang = messageSource.getMessage("ckfinder.lang", new Object[0], fallback, locale);
                pw.println(indent + "config.language = '" + lang + "';");
            }
            pw.println(indent + "config.removePlugins = 'help';");
            pw.println("};");
        }
        return ResponseEntity.ok().contentType(APPLICATION_JAVASCRIPT).body(sw.toString());
    }

}
