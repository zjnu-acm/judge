/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */
package com.ckfinder.connector.configuration;

import com.ckfinder.connector.utils.FileUtils;
import com.ckfinder.connector.utils.PathUtils;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Path builder that creates default values of baseDir and baseURL.
 */
@Slf4j
public class DefaultPathBuilder implements IBasePathBuilder {

    /**
     * Gets default value for baseDir (based on baseURL).
     *
     * @param request the {@code HttpServletRequest} object
     * @return default baseDir value.
     */
    @Override
    public String getBaseDir(final HttpServletRequest request) {
        String newBaseUrl = getBaseUrl(request);

        if (Pattern.matches(Constants.URL_REGEX, newBaseUrl)) {
            if (newBaseUrl.contains(request.getContextPath())) {
                newBaseUrl = newBaseUrl.substring(newBaseUrl.indexOf(
                        request.getContextPath()));
            } else if (newBaseUrl.indexOf('/') >= 0) {
                newBaseUrl = PathUtils.removeSlashFromEnd(newBaseUrl);
                newBaseUrl = newBaseUrl.substring(newBaseUrl.lastIndexOf('/'));
            } else {
                return "/";
            }
        }

        try {
            return FileUtils.calculatePathFromBaseUrl(request.getServletContext(), newBaseUrl);
        } catch (Exception e) {
            log.error("Could not create path for: " + newBaseUrl, e);
            return newBaseUrl;
        }
    }

    /**
     * Gets default value for baseURL.
     *
     * @param request current{@code HttpServletRequest} object
     * @return default baseURL value.
     */
    @Override
    public String getBaseUrl(final HttpServletRequest request) {
        return request.getContextPath().concat(IConfiguration.DEFAULT_BASE_URL);
    }
}
