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
package com.ckfinder.connector.data;

import com.ckfinder.connector.configuration.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Resource type entity.
 */
@AllArgsConstructor
@Getter
@Setter
public class ResourceType {

    /**
     * bytes in KB.
     */
    private static final int BYTES = 1024;
    /**
     * resource name.
     */
    private String name;
    /**
     * resource url.
     */
    private String url;
    /**
     * resource directory.
     */
    private String path;
    /**
     * max file size in resource.
     */
    private String maxSize;
    /**
     * list of allowed extensions in resource (separated with comma).
     */
    private String allowedExtensions;
    /**
     * list of denied extensions in resource (separated with comma).
     */
    private String deniedExtensions;

    /**
     * constructor.
     *
     * @param name type name
     */
    public ResourceType(String name) {
        this.name = name;
    }

    /**
     * clone constructor.
     *
     * @param type source type
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public ResourceType(ResourceType type) {
        this.name = type.name;
        this.url = type.url;
        this.path = type.path;
        this.maxSize = type.maxSize;
        this.allowedExtensions = type.allowedExtensions;
        this.deniedExtensions = type.deniedExtensions;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        if (url == null) {
            return Constants.BASE_URL_PLACEHOLDER.concat("/").concat(this.name.toLowerCase()).concat("/");
        }
        return url;
    }

    /**
     * @return the directory
     */
    public String getPath() {
        if (path == null) {
            return Constants.BASE_DIR_PLACEHOLDER.concat(this.name.toLowerCase()).concat("/");
        }
        return path;
    }

    /**
     * @return the maxSize
     */
    public long getMaxSize() {
        try {
            //No XML node, no value, value equals 0 = no resource type maxSize
            if (maxSize == null
                    || maxSize.isEmpty()
                    || maxSize.equals("0")) {
                return 0;
            }
            return parseMaxSize();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * parses max size value from config (ex. 16M to number of bytes).
     *
     * @return number of bytes in max size.
     */
    private long parseMaxSize() {

        char lastChar = maxSize.toLowerCase().charAt(maxSize.length() - 1);
        int a;
        switch (lastChar) {
            case 'k':
                a = BYTES;
                break;
            case 'm':
                a = BYTES * BYTES;
                break;
            case 'g':
                a = BYTES * BYTES * BYTES;
                break;
            default:
                return 0;
        }
        long value = Long.parseLong(maxSize.substring(0, maxSize.length() - 1));
        return value * a;

    }

    /**
     * @return the allowedExtensions
     */
    public String getAllowedExtensions() {
        if (allowedExtensions == null) {
            return "";
        }
        return allowedExtensions;
    }

    /**
     * @return the deniedExtensions
     */
    public String getDeniedExtensions() {
        if (deniedExtensions == null) {
            return "";
        }
        return deniedExtensions;
    }

}
