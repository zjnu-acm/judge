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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

/**
 * Class to handle <code>RenameFile</code> command.
 */
@Slf4j
public class RenameFileCommand extends XMLCommand implements IPostCommand {

    private String fileName;
    private String newFileName;
    private boolean renamed;
    private boolean addRenameNode;

    @Override
    protected void createXMLChildNodes(int errorNum, Element rootElement) {
        if (this.addRenameNode) {
            createRenamedFileNode(rootElement);
        }
    }

    /**
     * create rename file XML node.
     *
     * @param rootElement XML root node
     */
    private void createRenamedFileNode(Element rootElement) {
        Element element = getCreator().getDocument().createElement("RenamedFile");
        element.setAttribute("name", this.fileName);
        if (renamed) {
            element.setAttribute("newName", this.newFileName);
        }
        rootElement.appendChild(element);
    }

    /**
     * gets data for XML and checks all validation.
     *
     * @return error code or 0 if it's correct.
     * @throws java.io.IOException
     */
    @Override
    protected int getDataForXml() throws IOException {

        if (!isTypeExists(getType())) {
            this.setType(null);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!getConfiguration().getAccessControl().checkFolderACL(getType(), getCurrentFolder(), getUserRole(),
                AccessControl.CKFINDER_CONNECTOR_ACL_FILE_RENAME)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        if (getConfiguration().isForceAscii()) {
            this.newFileName = FileUtils.convertToASCII(this.newFileName);
        }

        if (this.fileName != null && !this.fileName.isEmpty()
                && this.newFileName != null && !this.newFileName.isEmpty()) {
            this.addRenameNode = true;
        }

        int checkFileExt = FileUtils.checkFileExtension(this.newFileName,
                this.getConfiguration().getTypes().get(this.getType()));
        if (checkFileExt == 1) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION;
        }
        if (getConfiguration().isCheckDoubleFileExtensions()) {
            this.newFileName = FileUtils.renameFileWithBadExt(this.getConfiguration().getTypes().get(this.getType()), this.newFileName);
        }

        if (!FileUtils.isFileNameInvalid(this.fileName)
                || FileUtils.isFileHidden(this.fileName, getConfiguration())) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        if (!FileUtils.isFileNameInvalid(this.newFileName, getConfiguration())
                || FileUtils.isFileHidden(this.newFileName, getConfiguration())) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
        }

        if (FileUtils.checkFileExtension(this.fileName,
                this.getConfiguration().getTypes().get(this.getType())) == 1) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        String dirPath = getConfiguration().getTypes().get(this.getType()).getPath()
                + this.getCurrentFolder();

        Path file = Paths.get(dirPath, this.fileName);
        Path newFile = Paths.get(dirPath, this.newFileName);
        Path dir = Paths.get(dirPath);

        try {
            if (!Files.exists(file)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND;
            }

            if (Files.exists(newFile)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST;
            }

            if (!Files.isWritable(dir) || !Files.isWritable(file)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            }
            try {
                Files.move(file, newFile);
                renamed = true;
                renameThumb();
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
            } catch (IOException ex) {
                renamed = false;
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            }
        } catch (SecurityException e) {
            log.error("", e);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
        }

    }

    /**
     * rename thumb file.
     */
    private void renameThumb() throws IOException {
        Path thumbFile = Paths.get(getConfiguration().getThumbsPath(),
                this.getType() + this.getCurrentFolder(),
                this.fileName);
        Path newThumbFile = Paths.get(getConfiguration().getThumbsPath(),
                this.getType() + this.getCurrentFolder(),
                this.newFileName);

        Files.move(thumbFile, newThumbFile);

    }

    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);
        this.fileName = request.getParameter("fileName");
        this.newFileName = request.getParameter("newFileName");
    }

}
