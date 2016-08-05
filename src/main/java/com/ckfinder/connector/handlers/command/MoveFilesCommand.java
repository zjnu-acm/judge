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
package com.ckfinder.connector.handlers.command;

import com.ckfinder.connector.configuration.Constants;
import com.ckfinder.connector.configuration.IConfiguration;
import com.ckfinder.connector.data.FilePostParam;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.utils.AccessControl;
import com.ckfinder.connector.utils.FileUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

/**
 * Class to handle <code>MoveFiles</code> command.
 */
@Slf4j
public class MoveFilesCommand extends XMLCommand implements IPostCommand {

    private List<FilePostParam> files;
    private int filesMoved;
    private int movedAll;
    private boolean addMoveNode;

    @Override
    protected void createXMLChildNodes(int errorNum, Element rootElement)
            throws ConnectorException {
        if (creator.hasErrors()) {
            Element errorsNode = creator.getDocument().createElement("Errors");
            creator.addErrors(errorsNode);
            rootElement.appendChild(errorsNode);
        }

        if (addMoveNode) {
            createMoveFielsNode(rootElement);
        }
    }

    /**
     * creates move file XML node.
     *
     * @param rootElement XML root element.
     */
    private void createMoveFielsNode(Element rootElement) {
        Element element = creator.getDocument().createElement("MoveFiles");
        element.setAttribute("moved", String.valueOf(this.filesMoved));
        element.setAttribute("movedTotal",
                String.valueOf(this.movedAll + this.filesMoved));
        rootElement.appendChild(element);
    }

    @Override
    protected int getDataForXml() {
        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!getAccessControl().checkFolderACL(this.type,
                this.currentFolder,
                this.userRole,
                AccessControl.CKFINDER_CONNECTOR_ACL_FILE_RENAME
                | AccessControl.CKFINDER_CONNECTOR_ACL_FILE_DELETE
                | AccessControl.CKFINDER_CONNECTOR_ACL_FILE_UPLOAD)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        try {
            return moveFiles();
        } catch (Exception e) {
            log.error("", e);
        }
        //this code should never be reached
        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNKNOWN;

    }

    /**
     * move files.
     *
     * @return error code.
     */
    private int moveFiles() {
        this.filesMoved = 0;
        this.addMoveNode = false;
        for (FilePostParam file : files) {

            if (!FileUtils.checkFileName(file.getName())) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }
            if (Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(
                    file.getFolder()).find()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (configuration.getTypes().get(file.getType()) == null) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (file.getFolder() == null || file.getFolder().isEmpty()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }
            if (FileUtils.checkFileExtension(file.getName(),
                    this.configuration.getTypes().get(this.type)) == 1) {
                creator.appendErrorNodeChild(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION,
                        file.getName(), file.getFolder(), file.getType());
                continue;
            }

            if (!this.type.equals(file.getType())) {
                if (FileUtils.checkFileExtension(file.getName(),
                        this.configuration.getTypes().get(file.getType())) == 1) {
                    creator.appendErrorNodeChild(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION,
                            file.getName(), file.getFolder(), file.getType());
                    continue;
                }
            }

            if (FileUtils.checkIfFileIsHidden(file.getName(),
                    this.configuration)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (FileUtils.checkIfDirIsHidden(file.getFolder(), this.configuration)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (!getAccessControl().checkFolderACL(file.getType(), file.getFolder(), this.userRole,
                    AccessControl.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;

            }
            Path sourceFile = Paths.get(configuration.getTypes().get(file.getType()).getPath()
                    + file.getFolder(), file.getName());
            Path destFile = Paths.get(configuration.getTypes().get(this.type).getPath()
                    + this.currentFolder, file.getName());

            Path sourceThumb = Paths.get(configuration.getThumbsPath(), file.getType()
                    + file.getFolder() + file.getName());
            try {
                if (!Files.exists(sourceFile) || !Files.isRegularFile(sourceFile)) {
                    creator.appendErrorNodeChild(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND,
                            file.getName(), file.getFolder(), file.getType());
                    continue;
                }
                if (!this.type.equals(file.getType())) {
                    long maxSize = configuration.getTypes().get(this.type).getMaxSize();
                    if (maxSize != 0 && maxSize < Files.size(sourceFile)) {
                        creator.appendErrorNodeChild(
                                Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG,
                                file.getName(), file.getFolder(), file.getType());
                        continue;
                    }
                }
                if (sourceFile.equals(destFile)) {
                    creator.appendErrorNodeChild(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_SOURCE_AND_TARGET_PATH_EQUAL,
                            file.getName(), file.getFolder(), file.getType());
                } else if (Files.exists(destFile)) {
                    if (file.getOptions() != null
                            && file.getOptions().contains("overwrite")) {
                        if (!handleOverwrite(sourceFile, destFile)) {
                            creator.appendErrorNodeChild(
                                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
                                    file.getName(), file.getFolder(),
                                    file.getType());
                        } else {
                            this.filesMoved++;
                            FileUtils.delete(sourceThumb);
                        }
                    } else if (file.getOptions() != null
                            && file.getOptions().contains("autorename")) {
                        if (!handleAutoRename(sourceFile, destFile)) {
                            creator.appendErrorNodeChild(
                                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
                                    file.getName(), file.getFolder(),
                                    file.getType());
                        } else {
                            this.filesMoved++;
                            FileUtils.delete(sourceThumb);
                        }
                    } else {
                        creator.appendErrorNodeChild(
                                Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST,
                                file.getName(), file.getFolder(), file.getType());
                    }
                } else if (FileUtils.copyFromSourceToDestFile(sourceFile, destFile,
                        true, configuration)) {
                    this.filesMoved++;
                    moveThumb(file);
                }
            } catch (SecurityException | IOException e) {
                log.error("", e);
                creator.appendErrorNodeChild(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
                        file.getName(), file.getFolder(), file.getType());
            }

        }
        this.addMoveNode = true;
        if (creator.hasErrors()) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_MOVE_FAILED;
        } else {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
        }
    }

    /**
     * Handles autorename option.
     *
     * @param sourceFile source file to move from.
     * @param destFile destination file to move to.
     * @return true if moved correctly
     * @throws IOException when ioerror occurs
     */
    private boolean handleAutoRename(Path sourceFile, Path destFile)
            throws IOException {
        int counter = 1;
        Path newDestFile;
        String fileNameWithoutExtension = FileUtils.getFileNameWithoutExtension(destFile.getFileName().toString(), false);
        while (true) {
            String newFileName = fileNameWithoutExtension
                    + "(" + counter + ")."
                    + FileUtils.getFileExtension(destFile.getFileName().toString(), false);
            newDestFile = destFile.getParent().resolve(newFileName);
            if (!Files.exists(newDestFile)) {
                // can't be in one if=, because when error in
                // copy file occurs then it will be infinity loop
                return (FileUtils.copyFromSourceToDestFile(sourceFile,
                        newDestFile, true, configuration));
            } else {
                counter++;
            }
        }
    }

    /**
     * Handles overwrite option.
     *
     * @param sourceFile source file to move from.
     * @param destFile destination file to move to.
     * @return true if moved correctly
     * @throws IOException when ioerror occurs
     */
    private boolean handleOverwrite(Path sourceFile, Path destFile)
            throws IOException {
        return FileUtils.delete(destFile)
                && FileUtils.copyFromSourceToDestFile(sourceFile, destFile,
                        true, configuration);
    }

    /**
     * move thumb file.
     *
     * @param file file to move.
     * @throws IOException when ioerror occurs
     */
    private void moveThumb(FilePostParam file) throws IOException {
        Path sourceThumbFile = Paths.get(configuration.getThumbsPath(),
                file.getType()
                + file.getFolder() + file.getName());
        Path destThumbFile = Paths.get(configuration.getThumbsPath(),
                this.type
                + this.currentFolder
                + file.getName());

        FileUtils.copyFromSourceToDestFile(sourceThumbFile, destThumbFile,
                true, configuration);

    }

    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);
        this.files = new ArrayList<>();
        this.movedAll = (request.getParameter("moved") != null) ? Integer.valueOf(request.getParameter("moved")) : 0;
        getFilesListFromRequest(request);
    }

    /**
     * get file list to copy from request.
     *
     * @param request request
     */
    private void getFilesListFromRequest(HttpServletRequest request) {
        int i = 0;
        while (true) {
            String paramName = "files[" + i + "][name]";
            if (request.getParameter(paramName) != null) {
                FilePostParam file = new FilePostParam();
                file.setName(request.getParameter(paramName));
                file.setFolder(request.getParameter("files[" + i + "][folder]"));
                file.setOptions(request.getParameter("files[" + i
                        + "][options]"));
                file.setType(request.getParameter("files[" + i + "][type]"));
                files.add(file);
            } else {
                break;
            }
            i++;
        }
    }

}
