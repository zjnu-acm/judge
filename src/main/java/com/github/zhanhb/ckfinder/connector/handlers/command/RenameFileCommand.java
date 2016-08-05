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
    protected void createXMLChildNodes(int errorNum,
            Element rootElement) throws ConnectorException {
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
        Element element = creator.getDocument().createElement("RenamedFile");
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

        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!configuration.getAccessControl().checkFolderACL(this.type, this.currentFolder, this.userRole,
                AccessControl.CKFINDER_CONNECTOR_ACL_FILE_RENAME)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        if (configuration.forceASCII()) {
            this.newFileName = FileUtils.convertToASCII(this.newFileName);
        }

        if (this.fileName != null && !this.fileName.isEmpty()
                && this.newFileName != null && !this.newFileName.isEmpty()) {
            this.addRenameNode = true;
        }

        int checkFileExt = FileUtils.checkFileExtension(this.newFileName,
                this.configuration.getTypes().get(this.type));
        if (checkFileExt == 1) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION;
        }
        if (configuration.ckeckDoubleFileExtensions()) {
            this.newFileName = FileUtils.renameFileWithBadExt(this.configuration.getTypes().get(this.type), this.newFileName);
        }

        if (!FileUtils.checkFileName(this.fileName)
                || FileUtils.checkIfFileIsHidden(this.fileName,
                        configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        if (!FileUtils.checkFileName(this.newFileName, configuration)
                || FileUtils.checkIfFileIsHidden(this.newFileName,
                        configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
        }

        if (FileUtils.checkFileExtension(this.fileName,
                this.configuration.getTypes().get(this.type)) == 1) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        String dirPath = configuration.getTypes().get(this.type).getPath()
                + this.currentFolder;

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
        Path thumbFile = Paths.get(configuration.getThumbsPath(),
                this.type + this.currentFolder,
                this.fileName);
        Path newThumbFile = Paths.get(configuration.getThumbsPath(),
                this.type + this.currentFolder,
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
