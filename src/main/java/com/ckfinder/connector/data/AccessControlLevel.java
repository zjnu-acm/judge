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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Access control level entity.
 */
@Getter
@NoArgsConstructor
@Setter
public class AccessControlLevel {

    /**
     *
     */
    private String role;
    /**
     *
     */
    private String resourceType;
    /**
     *
     */
    private String folder;
    /**
     *
     */
    private boolean folderView;
    /**
     *
     */
    private boolean folderCreate;
    /**
     *
     */
    private boolean folderRename;
    /**
     *
     */
    private boolean folderDelete;
    /**
     *
     */
    private boolean fileView;
    /**
     *
     */
    private boolean fileUpload;
    /**
     *
     */
    private boolean fileRename;
    /**
     *
     */
    private boolean fileDelete;

    /**
     * copy constructor.
     *
     * @param acl source acl
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public AccessControlLevel(AccessControlLevel acl) {
        this.role = acl.role;
        this.resourceType = acl.resourceType;
        this.folder = acl.folder;
        this.folderView = acl.folderView;
        this.folderCreate = acl.folderCreate;
        this.folderRename = acl.folderRename;
        this.folderDelete = acl.folderDelete;
        this.fileView = acl.fileView;
        this.fileUpload = acl.fileUpload;
        this.fileRename = acl.fileRename;
        this.fileDelete = acl.fileDelete;
    }

}
