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
package com.github.zhanhb.ckfinder.connector.configuration;

/**
 * Class holding constants used by the CKFinder connector.
 */
public interface Constants {

    /**
     * Regular expression to find images.
     */
    String CKFINDER_REGEX_IMAGES_EXT = "([^\\.]+(\\.(?i)(jpg|png|gif|bmp))$)";
    /**
     * Regular expression to identify invalid characters in file name.
     */
    String INVALID_FILE_NAME_REGEX = "\\p{Cntrl}|[/\\\\\\:\\*\\?\"\\<\\>\\|]";
    /**
     * Regular expression to identify invalid characters in path.
     */
    String INVALID_PATH_REGEX = "(/\\.|\\p{Cntrl}|//|\\\\|[:*?<>\"\\|])";
    /**
     * Regular expression to identify full URL.
     */
    String URL_REGEX = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    String CKFINDER_CHARS = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    /**
     * Regular expression to find default language code in CKFinder.
     */
    String DEFAULT_LANG_CODE = "en";
    /**
     * Regular expression to find baseDir configuration property placeholder.
     */
    String BASE_DIR_PLACEHOLDER = "%BASE_DIR%";
    /**
     * Regular expression to find baseUrl configuration property placeholder.
     */
    String BASE_URL_PLACEHOLDER = "%BASE_URL%";

    /**
     * Class holding CKFinder error codes.
     */
    @SuppressWarnings("PublicInnerClass")
    interface Errors {

        int CKFINDER_CONNECTOR_ERROR_NONE = 0;
        int CKFINDER_CONNECTOR_ERROR_CUSTOM_ERROR = 1;
        int CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND = 10;
        int CKFINDER_CONNECTOR_ERROR_TYPE_NOT_SPECIFIED = 11;
        int CKFINDER_CONNECTOR_ERROR_INVALID_TYPE = 12;
        int CKFINDER_CONNECTOR_ERROR_INVALID_NAME = 102;
        int CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED = 103;
        int CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED = 104;
        int CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION = 105;
        int CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST = 109;
        @Deprecated
        int CKFINDER_CONNECTOR_ERROR_UNKNOWN = 110;
        int CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST = 115;
        int CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND = 116;
        int CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND = 117;
        int CKFINDER_CONNECTOR_ERROR_SOURCE_AND_TARGET_PATH_EQUAL = 118;
        int CKFINDER_CONNECTOR_ERROR_UPLOADED_FILE_RENAMED = 201;
        int CKFINDER_CONNECTOR_ERROR_UPLOADED_INVALID = 202;
        int CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG = 203;
        int CKFINDER_CONNECTOR_ERROR_UPLOADED_CORRUPT = 204;
        int CKFINDER_CONNECTOR_ERROR_UPLOADED_NO_TMP_DIR = 205;
        int CKFINDER_CONNECTOR_ERROR_UPLOADED_WRONG_HTML_FILE = 206;
        int CKFINDER_CONNECTOR_ERROR_MOVE_FAILED = 300;
        int CKFINDER_CONNECTOR_ERROR_COPY_FAILED = 301;
        int CKFINDER_CONNECTOR_ERROR_DELETE_FAILED = 302;
        int CKFINDER_CONNECTOR_ERROR_UPLOADED_INVALID_NAME_RENAMED = 207;
        int CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED = 500;
        int CKFINDER_CONNECTOR_ERROR_THUMBNAILS_DISABLED = 501;
    }

}
