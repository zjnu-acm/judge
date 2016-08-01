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

import java.nio.file.Path;
import lombok.Getter;
import lombok.Setter;

/**
 * Event data for
 * {@link com.ckfinder.connector.configuration.Events.EventTypes#AfterFileUpload}
 * event.
 */
@Getter
@Setter
public class AfterFileUploadEventArgs extends EventArgs {

    private String currentFolder;
    private Path file;

}
