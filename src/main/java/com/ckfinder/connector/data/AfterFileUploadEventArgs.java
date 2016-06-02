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

import java.io.File;

/**
 * Event data for
 * {@link com.ckfinder.connector.configuration.Events.EventTypes#AfterFileUpload}
 * event.
 */
@SuppressWarnings("FinalMethod")
public class AfterFileUploadEventArgs extends EventArgs {

    private String currentFolder;
    private File file;

    /**
     * @return the currentFolder
     */
    public final String getCurrentFolder() {
        return currentFolder;
    }

    /**
     * @param currentFolder the currentFolder to set
     */
    public final void setCurrentFolder(final String currentFolder) {
        this.currentFolder = currentFolder;
    }

    /**
     * @return the fileContent
     */
    public final byte[] getFileContent() {
        throw new NoSuchMethodError();
    }

    /**
     * @param fileContent the fileContent to set
     */
    public final void setFileContent(final byte[] fileContent) {
        // noop
    }

    /**
     * @return the file
     */
    public final File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public final void setFile(final File file) {
        this.file = file;
    }

}
