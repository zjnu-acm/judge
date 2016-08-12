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
package com.github.zhanhb.ckfinder.connector.handlers.command;

import com.github.zhanhb.ckfinder.connector.configuration.Constants;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

/**
 * Class to handle <code>DeleteFolder</code> command.
 */
@Slf4j
public class DeleteFolderCommand extends XMLCommand implements IPostCommand {

    @Override
    protected void createXMLChildNodes(int errorNum, Element rootElement) {
    }

    /**
     * @return error code or 0 if ok. Deletes folder and thumb folder.
     */
    @Override
    protected int getDataForXml() {
        if (!isTypeExists(getType())) {
            this.setType(null);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!getConfiguration().getAccessControl().checkFolderACL(getType(),
                getCurrentFolder(),
                getUserRole(),
                AccessControl.CKFINDER_CONNECTOR_ACL_FOLDER_DELETE)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }
        if (this.getCurrentFolder().equals("/")) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        if (FileUtils.isDirectoryHidden(this.getCurrentFolder(), getConfiguration())) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        Path dir = Paths.get(getConfiguration().getTypes().get(this.getType()).getPath()
                + this.getCurrentFolder());

        try {
            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND;
            }

            if (FileUtils.delete(dir)) {
                Path thumbDir = Paths.get(getConfiguration().getThumbsPath(),
                        this.getType()
                        + this.getCurrentFolder());
                FileUtils.delete(thumbDir);
            } else {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            }
        } catch (SecurityException e) {
            log.error("", e);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
        }

        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

}
