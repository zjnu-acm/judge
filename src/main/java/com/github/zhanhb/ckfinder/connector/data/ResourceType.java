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
package com.github.zhanhb.ckfinder.connector.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Resource type entity.
 */
@Builder(builderClassName = "Builder")
@Getter
public class ResourceType {

    /**
     * resource name.
     */
    @NonNull
    private final String name;
    /**
     * resource url.
     */
    private final String url;
    /**
     * resource directory.
     */
    private final String path;
    /**
     * max file size in resource.
     */
    private final long maxSize;
    /**
     * list of allowed extensions in resource (separated with comma).
     */
    @NonNull
    private final String allowedExtensions;
    /**
     * list of denied extensions in resource (separated with comma).
     */
    @NonNull
    private final String deniedExtensions;

}
