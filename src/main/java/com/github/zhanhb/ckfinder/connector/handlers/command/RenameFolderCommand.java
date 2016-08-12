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
import com.github.zhanhb.ckfinder.connector.configuration.IConfiguration;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import com.github.zhanhb.ckfinder.connector.utils.PathUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

/**
 * Class to handle <code>RenameFolder</code> command.
 */
@Slf4j
public class RenameFolderCommand extends XMLCommand implements IPostCommand {

    private String newFolderName;
    private String newFolderPath;

    @Override
    protected void createXMLChildNodes(int errorNum, Element rootElement) {
        if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            createRenamedFolderNode(rootElement);
        }

    }

    /**
     * creates XML node for renamed folder.
     *
     * @param rootElement XML root element.
     */
    private void createRenamedFolderNode(Element rootElement) {
        Element element = getCreator().getDocument().createElement("RenamedFolder");
        element.setAttribute("newName", this.newFolderName);
        element.setAttribute("newPath", this.newFolderPath);
        element.setAttribute("newUrl", getConfiguration().getTypes().get(this.getType()).getUrl() + this.newFolderPath);
        rootElement.appendChild(element);

    }

    @Override
    protected int getDataForXml() throws IOException {

        try {
            checkParam(newFolderName);

        } catch (ConnectorException e) {
            return e.getErrorCode();
        }

        if (!checkIfTypeExists(getType())) {
            this.setType(null);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!getConfiguration().getAccessControl().checkFolderACL(getType(),
                getCurrentFolder(),
                getUserRole(),
                AccessControl.CKFINDER_CONNECTOR_ACL_FOLDER_RENAME)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        if (getConfiguration().isForceAscii()) {
            this.newFolderName = FileUtils.convertToASCII(this.newFolderName);
        }

        if (FileUtils.checkIfDirIsHidden(this.newFolderName, getConfiguration())
                || !FileUtils.checkFolderName(this.newFolderName, getConfiguration())) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
        }

        if (this.getCurrentFolder().equals("/")) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        Path dir = Paths.get(getConfiguration().getTypes().get(this.getType()).getPath()
                + this.getCurrentFolder());
        try {
            if (!Files.isDirectory(dir)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }
            setNewFolder();
            Path newDir = Paths.get(getConfiguration().getTypes().get(this.getType()).getPath()
                    + this.newFolderPath);
            if (Files.exists(newDir)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST;
            }
            try {
                Files.move(dir, newDir);
                renameThumb();
            } catch (IOException ex) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            }
        } catch (SecurityException e) {
            log.error("", e);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
        }

        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

    /**
     * renames thumb folder.
     */
    private void renameThumb() throws IOException {
        Path thumbDir = Paths.get(getConfiguration().getThumbsPath(),
                this.getType()
                + this.getCurrentFolder());
        Path newThumbDir = Paths.get(getConfiguration().getThumbsPath(),
                this.getType()
                + this.newFolderPath);
        Files.move(thumbDir, newThumbDir);
    }

    /**
     * sets new folder name.
     */
    private void setNewFolder() {
        String tmp1 = this.getCurrentFolder().substring(0,
                this.getCurrentFolder().lastIndexOf('/'));
        this.newFolderPath = tmp1.substring(0,
                tmp1.lastIndexOf('/') + 1).concat(this.newFolderName);
        this.newFolderPath = PathUtils.addSlashToEnd(this.newFolderPath);
    }

    /**
     * @param request request
     * @param configuration connector conf
     * @throws ConnectorException when error occurs.
     */
    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration) throws ConnectorException {
        super.initParams(request, configuration);
        this.newFolderName = request.getParameter("NewFolderName");
    }

}
