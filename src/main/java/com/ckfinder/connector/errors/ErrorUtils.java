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
package com.ckfinder.connector.errors;

import com.ckfinder.connector.configuration.IConfiguration;
import java.util.Locale;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;

/**
 * Error utils.
 */
@Slf4j
public enum ErrorUtils {

    INSTANCE;

    /**
     * Gets error message by locale code.
     *
     * @param errorCode error number
     * @param lang connector language code
     * @param conf connector configuration object
     * @return localized error message.
     */
    public String getErrorMsgByLangAndCode(String lang,
            int errorCode,
            IConfiguration conf) {
        try {
            return ResourceBundle.getBundle(ErrorUtils.class.getPackage()
                    .getName().concat(".LocalStrings"), new Locale(lang))
                    .getString(Integer.toString(errorCode));
        } catch (RuntimeException ex) {
            log.error("", ex);
            return "";
        }
    }

}
