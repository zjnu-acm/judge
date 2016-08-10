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
import com.github.zhanhb.ckfinder.connector.data.FilePostParam;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
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
 * Class to handle <code>CopyFiles</code> command.
 */
@Slf4j
public class CopyFilesCommand extends XMLCommand implements IPostCommand {

    private List<FilePostParam> files;
    private int filesCopied;
    private int copiedAll;
    private boolean addCopyNode;

    @Override
    protected void createXMLChildNodes(int errorNum, Element rootElement) {
        if (getCreator().hasErrors()) {
            Element errorsNode = getCreator().getDocument().createElement("Errors");
            getCreator().addErrors(errorsNode);
            rootElement.appendChild(errorsNode);
        }

        if (addCopyNode) {
            createCopyFielsNode(rootElement);
        }
    }

    /**
     * creates copy file XML node.
     *
     * @param rootElement XML root node.
     */
    private void createCopyFielsNode(Element rootElement) {
        Element element = getCreator().getDocument().createElement("CopyFiles");
        element.setAttribute("copied", String.valueOf(this.filesCopied));
        element.setAttribute("copiedTotal", String.valueOf(this.copiedAll
                + this.filesCopied));
        rootElement.appendChild(element);
    }

    @Override
    protected int getDataForXml() {
        if (!checkIfTypeExists(getType())) {
            this.setType(null);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!getConfiguration().getAccessControl().checkFolderACL(getType(),
                getCurrentFolder(),
                getUserRole(),
                AccessControl.CKFINDER_CONNECTOR_ACL_FILE_RENAME
                | AccessControl.CKFINDER_CONNECTOR_ACL_FILE_DELETE
                | AccessControl.CKFINDER_CONNECTOR_ACL_FILE_UPLOAD)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        try {
            return copyFiles();
        } catch (Exception e) {
            log.error("", e);
        }
        //this code should never be reached
        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNKNOWN;
    }

    /**
     * copy files from request.
     *
     * @return error code
     */
    private int copyFiles() {
        this.filesCopied = 0;
        this.addCopyNode = false;
        for (FilePostParam file : files) {

            if (!FileUtils.checkFileName(file.getName())) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(
                    file.getFolder()).find()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }
            if (getConfiguration().getTypes().get(file.getType()) == null) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }
            if (file.getFolder() == null || file.getFolder().isEmpty()) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }
            if (FileUtils.checkFileExtension(file.getName(),
                    this.getConfiguration().getTypes().get(this.getType())) == 1) {
                getCreator().appendErrorNodeChild(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION,
                        file.getName(), file.getFolder(), file.getType());
                continue;
            }
            // check #4 (extension) - when moving to another resource type,
            //double check extension
            if (!getType().equals(file.getType())) {
                if (FileUtils.checkFileExtension(file.getName(),
                        this.getConfiguration().getTypes().get(file.getType())) == 1) {
                    getCreator().appendErrorNodeChild(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION,
                            file.getName(), file.getFolder(), file.getType());
                    continue;

                }
            }
            if (FileUtils.checkIfDirIsHidden(file.getFolder(), this.getConfiguration())) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (FileUtils.checkIfFileIsHidden(file.getName(), this.getConfiguration())) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (!getConfiguration().getAccessControl().checkFolderACL(file.getType(), file.getFolder(), getUserRole(),
                    AccessControl.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
            }

            Path sourceFile = Paths.get(getConfiguration().getTypes().get(file.getType()).getPath()
                    + file.getFolder(), file.getName());
            Path destFile = Paths.get(getConfiguration().getTypes().get(this.getType()).getPath()
                    + this.getCurrentFolder(), file.getName());

            try {
                if (!Files.exists(sourceFile) || !Files.isRegularFile(sourceFile)) {
                    getCreator().appendErrorNodeChild(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND,
                            file.getName(), file.getFolder(), file.getType());
                    continue;
                }
                if (!getType().equals(file.getType())) {
                    long maxSize = getConfiguration().getTypes().get(this.getType()).getMaxSize();
                    if (maxSize != 0 && maxSize < Files.size(sourceFile)) {
                        getCreator().appendErrorNodeChild(
                                Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG,
                                file.getName(), file.getFolder(), file.getType());
                        continue;
                    }
                }
                if (sourceFile.equals(destFile)) {
                    getCreator().appendErrorNodeChild(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_SOURCE_AND_TARGET_PATH_EQUAL,
                            file.getName(), file.getFolder(), file.getType());
                } else if (Files.exists(destFile)) {
                    if (file.getOptions() != null
                            && file.getOptions().contains("overwrite")) {
                        if (!handleOverwrite(sourceFile, destFile)) {
                            getCreator().appendErrorNodeChild(
                                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
                                    file.getName(), file.getFolder(), file.getType());
                        } else {
                            this.filesCopied++;
                        }
                    } else if (file.getOptions() != null
                            && file.getOptions().contains("autorename")) {
                        if (!handleAutoRename(sourceFile, destFile)) {
                            getCreator().appendErrorNodeChild(
                                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
                                    file.getName(), file.getFolder(), file.getType());
                        } else {
                            this.filesCopied++;
                        }
                    } else {
                        getCreator().appendErrorNodeChild(
                                Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST,
                                file.getName(), file.getFolder(), file.getType());
                    }
                } else if (FileUtils.copyFromSourceToDestFile(sourceFile, destFile,
                        false, getConfiguration())) {
                    this.filesCopied++;
                    copyThumb(file);
                }
            } catch (SecurityException | IOException e) {
                log.error("", e);
                getCreator().appendErrorNodeChild(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
                        file.getName(), file.getFolder(), file.getType());
            }
        }
        this.addCopyNode = true;
        if (getCreator().hasErrors()) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_COPY_FAILED;
        } else {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
        }
    }

    /**
     * Handles autorename option.
     *
     * @param sourceFile source file to copy from.
     * @param destFile destination file to copy to.
     * @return true if copied correctly
     * @throws IOException when ioerror occurs
     */
    private boolean handleAutoRename(Path sourceFile, Path destFile)
            throws IOException {
        int counter = 1;
        Path newDestFile;
        String fileName = destFile.getFileName().toString();
        String fileNameWithoutExtension = FileUtils.getFileNameWithoutExtension(fileName, false);
        String fileExtension = FileUtils.getFileExtension(fileName, false);
        Path parent = destFile.getParent();
        while (true) {
            String newFileName = fileNameWithoutExtension
                    + "(" + counter + ")."
                    + fileExtension;
            newDestFile = parent.resolve(newFileName);
            if (!Files.exists(newDestFile)) {
                // can't be in one if=, because when error in
                // copy file occurs then it will be infinity loop
                return (FileUtils.copyFromSourceToDestFile(sourceFile,
                        newDestFile,
                        false, getConfiguration()));
            } else {
                counter++;
            }
        }
    }

    /**
     * Handles overwrite option.
     *
     * @param sourceFile source file to copy from.
     * @param destFile destination file to copy to.
     * @return true if copied correctly
     * @throws IOException when ioerror occurs
     */
    private boolean handleOverwrite(Path sourceFile, Path destFile)
            throws IOException {
        return FileUtils.delete(destFile)
                && FileUtils.copyFromSourceToDestFile(sourceFile, destFile,
                        false, getConfiguration());
    }

    /**
     * copy thumb file.
     *
     * @param file file to copy.
     * @throws IOException when ioerror occurs
     */
    private void copyThumb(FilePostParam file) throws IOException {
        Path sourceThumbFile = Paths.get(getConfiguration().getThumbsPath(),
                file.getType()
                + file.getFolder(), file.getName());
        Path destThumbFile = Paths.get(getConfiguration().getThumbsPath(),
                this.getType()
                + this.getCurrentFolder(), file.getName());

        if (Files.isRegularFile(sourceThumbFile) && Files.exists(sourceThumbFile)) {
            FileUtils.copyFromSourceToDestFile(sourceThumbFile, destThumbFile,
                    false, getConfiguration());
        }

    }

    @Override
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    protected void initParams(HttpServletRequest request, IConfiguration configuration) throws ConnectorException {
        super.initParams(request, configuration);
        this.files = new ArrayList<>();
        this.copiedAll = (request.getParameter("copied") != null) ? Integer.parseInt(request.getParameter("copied")) : 0;

        getFilesListFromRequest(request);
    }

    /**
     * Get list of files from request.
     *
     * @param request - request object.
     */
    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
    private void getFilesListFromRequest(HttpServletRequest request) {
        int i = 0;
        String paramName = "files[" + i + "][name]";
        while (request.getParameter(paramName) != null) {
            String name = request.getParameter(paramName);
            String folder = request.getParameter("files[" + i + "][folder]");
            String options = request.getParameter("files[" + i + "][options]");
            String type = request.getParameter("files[" + i + "][type]");
            this.files.add(new FilePostParam(name, folder, options, type));
            paramName = "files[" + (++i) + "][name]";
        }
    }

}
