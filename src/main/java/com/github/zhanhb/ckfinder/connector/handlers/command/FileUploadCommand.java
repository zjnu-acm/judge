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
import com.github.zhanhb.ckfinder.connector.data.AfterFileUploadEventArgs;
import com.github.zhanhb.ckfinder.connector.data.ResourceType;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import com.github.zhanhb.ckfinder.connector.errors.ErrorUtils;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import com.github.zhanhb.ckfinder.connector.utils.ImageUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to handle <code>FileUpload</code> command.
 */
@Slf4j
@SuppressWarnings("ProtectedField")
public class FileUploadCommand extends Command implements IPostCommand {

    /**
     * Array containing unsafe characters which can't be used in file name.
     */
    private static final char[] UNSAFE_FILE_NAME_CHARS = {':', '*', '?', '|', '/'};

    /**
     * Uploading file name request.
     */
    protected String fileName;
    /**
     * File name after rename.
     */
    protected String newFileName;
    /**
     * Function number to call after file upload is completed.
     */
    protected String ckEditorFuncNum;
    /**
     * The selected response type to be used after file upload is completed.
     */
    protected String responseType;
    /**
     * Function number to call after file upload is completed.
     */
    protected String ckFinderFuncNum;
    /**
     * Language (locale) code.
     */
    private String langCode;
    /**
     * Flag informing if file was uploaded correctly.
     */
    protected boolean uploaded;
    /**
     * Error code number.
     */
    protected int errorCode;
    /**
     * Custom error message.
     */
    protected String customErrorMsg;

    /**
     * default constructor.
     */
    public FileUploadCommand() {
        this.errorCode = 0;
        this.fileName = "";
        this.newFileName = "";
        this.type = "";
        this.uploaded = false;
    }

    /**
     * Executes file upload command.
     *
     * @param out the response output stream
     * @throws ConnectorException when error occurs.
     */
    @Override
    public void execute(OutputStream out) throws ConnectorException {
        try {
            String errorMsg = this.errorCode == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE ? "" : (this.errorCode == Constants.Errors.CKFINDER_CONNECTOR_ERROR_CUSTOM_ERROR ? this.customErrorMsg
                    : ErrorUtils.INSTANCE.getErrorMsgByLangAndCode(this.langCode, this.errorCode, this.configuration));
            errorMsg = errorMsg.replace("%1", this.newFileName);
            String path = "";

            if (!uploaded) {
                this.newFileName = "";
                this.currentFolder = "";
            } else {
                path = configuration.getTypes().get(this.type).getUrl()
                        + this.currentFolder;
            }

            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            if (this.responseType != null && this.responseType.equals("txt")) {
                writer.write(this.newFileName + "|" + errorMsg);
            } else if (checkFuncNum()) {
                handleOnUploadCompleteCallFuncResponse(writer, errorMsg, path);
            } else {
                handleOnUploadCompleteResponse(writer, errorMsg);
            }
            out.flush();
        } catch (IOException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    /**
     * check if func num is set in request.
     *
     * @return true if is.
     */
    protected boolean checkFuncNum() {
        return this.ckFinderFuncNum != null;
    }

    /**
     * return response when func num is set.
     *
     * @param out response.
     * @param errorMsg error message
     * @param path path
     * @throws IOException when error occurs.
     */
    protected void handleOnUploadCompleteCallFuncResponse(Writer out,
            String errorMsg,
            String path)
            throws IOException {
        this.ckFinderFuncNum = this.ckFinderFuncNum.replaceAll(
                "[^\\d]", "");
        out.write("<script type=\"text/javascript\">");
        out.write("window.parent.CKFinder.tools.callFunction("
                + this.ckFinderFuncNum + ", '"
                + path
                + FileUtils.backupWithBackSlash(this.newFileName, "'")
                + "', '" + errorMsg + "');");
        out.write("</script>");
    }

    /**
     *
     * @param writer out put stream
     * @param errorMsg error message
     * @throws IOException when error occurs
     */
    protected void handleOnUploadCompleteResponse(Writer writer,
            String errorMsg) throws IOException {
        writer.write("<script type=\"text/javascript\">");
        writer.write("window.parent.OnUploadCompleted(");
        writer.write("'" + FileUtils.backupWithBackSlash(this.newFileName, "'") + "'");
        writer.write(", '"
                + (this.errorCode
                != Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE ? errorMsg
                        : "") + "'");
        writer.write(");");
        writer.write("</script>");
    }

    /**
     * initializing parametrs for command handler.
     *
     * @param request request
     * @param configuration connector configuration.
     * @throws ConnectorException when error occurs.
     */
    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);
        this.ckFinderFuncNum = request.getParameter("CKFinderFuncNum");
        this.ckEditorFuncNum = request.getParameter("CKEditorFuncNum");
        this.responseType = request.getParameter("response_type") != null
                ? request.getParameter("response_type") : request.getParameter("responseType");
        this.langCode = request.getParameter("langCode");

        if (this.errorCode == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            this.uploaded = uploadFile(request);
        }

    }

    /**
     * uploads file and saves to file.
     *
     * @param request request
     * @return true if uploaded correctly.
     */
    private boolean uploadFile(HttpServletRequest request) {
        if (!configuration.getAccessControl().checkFolderACL(this.type, this.currentFolder, this.userRole,
                AccessControl.CKFINDER_CONNECTOR_ACL_FILE_UPLOAD)) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
            return false;
        }
        return fileUpload(request);
    }

    /**
     *
     * @param request http request
     * @return true if uploaded correctly
     */
    private boolean fileUpload(HttpServletRequest request) {
        try {
            Collection<Part> parts = request.getParts();
            for (Part part : parts) {
                String path = configuration.getTypes().get(this.type).getPath() + this.currentFolder;
                this.fileName = getFileItemName(part);
                if (validateUploadItem(part, path)) {
                    return saveTemporaryFile(path, part);
                }
            }
            return false;
        } catch (ConnectorException e) {
            this.errorCode = e.getErrorCode();
            if (this.errorCode == Constants.Errors.CKFINDER_CONNECTOR_ERROR_CUSTOM_ERROR) {
                this.customErrorMsg = e.getMessage();
            }
            return false;
        } catch (Exception e) {
            String message = e.getMessage().toLowerCase();
            if (message.contains("sizelimit") || message.contains("size limit")) {
                log.info("", e);
                this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG;
                return false;
            }
            log.error("", e);
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            return false;
        }

    }

    /**
     * saves temporary file in the correct file path.
     *
     * @param path path to save file
     * @param item file upload item
     * @return result of saving, true if saved correctly
     * @throws Exception when error occurs.
     */
    private boolean saveTemporaryFile(String path, Part item)
            throws Exception {
        Path file = Paths.get(path, this.newFileName);

        if (!ImageUtils.isImage(file)) {
            item.write(file.toString());
            if (configuration.getEvents() != null) {
                AfterFileUploadEventArgs args = new AfterFileUploadEventArgs(this.currentFolder, file);
                configuration.getEvents().runAfterFileUpload(args, configuration);
            }
            return true;
        } else if (ImageUtils.checkImageSize(item.getInputStream(), this.configuration)
                || configuration.checkSizeAfterScaling()) {
            ImageUtils.createTmpThumb(item.getInputStream(), file, getFileItemName(item),
                    this.configuration);
            if (!configuration.checkSizeAfterScaling()
                    || FileUtils.checkFileSize(configuration.getTypes().get(this.type), Files.size(file))) {
                if (configuration.getEvents() != null) {
                    AfterFileUploadEventArgs args = new AfterFileUploadEventArgs(this.currentFolder, file);
                    configuration.getEvents().runAfterFileUpload(args, configuration);
                }
                return true;
            } else {
                Files.deleteIfExists(file);
                this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG;
                return false;
            }
        } else {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG;
            return false;
        }
    }

    /**
     * if file exists this method adds (number) to file.
     *
     * @param path folder
     * @param name file name
     * @return new file name.
     */
    private String getFinalFileName(String path, String name) {
        Path file = Paths.get(path, name);
        int number = 0;

        String nameWithoutExtension = FileUtils.getFileNameWithoutExtension(name, false);
        Pattern p = Pattern.compile("^(AUX|COM\\d|CLOCK\\$|CON|NUL|PRN|LPT\\d)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(nameWithoutExtension);
        boolean protectedName = m.find();

        while (true) {
            if (Files.exists(file) || protectedName) {
                number++;
                @SuppressWarnings("StringBufferWithoutInitialCapacity")
                StringBuilder sb = new StringBuilder();
                sb.append(FileUtils.getFileNameWithoutExtension(name, false));
                sb.append("(").append(number).append(").");
                sb.append(FileUtils.getFileExtension(name, false));
                this.newFileName = sb.toString();
                file = Paths.get(path, this.newFileName);
                this.errorCode
                        = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_FILE_RENAMED;
                protectedName = false;
            } else {
                return this.newFileName;
            }
        }
    }

    /**
     * validates uploaded file.
     *
     * @param item uploaded item.
     * @param path file path
     * @return true if validation
     */
    private boolean validateUploadItem(Part item, String path) {

        if (item.getSubmittedFileName() != null && item.getSubmittedFileName().length() > 0) {
            this.fileName = getFileItemName(item);
        } else {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_INVALID;
            return false;
        }
        this.newFileName = this.fileName;

        for (char c : UNSAFE_FILE_NAME_CHARS) {
            this.newFileName = this.newFileName.replace(c, '_');
        }

        if (configuration.isDisallowUnsafeCharacters()) {
            this.newFileName = this.newFileName.replace(';', '_');
        }
        if (configuration.forceASCII()) {
            this.newFileName = FileUtils.convertToASCII(this.newFileName);
        }
        if (!this.newFileName.equals(this.fileName)) {
            this.errorCode
                    = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_INVALID_NAME_RENAMED;
        }

        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            return false;
        }
        if (!FileUtils.checkFileName(this.newFileName)
                || FileUtils.checkIfFileIsHidden(this.newFileName,
                        configuration)) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
            return false;
        }
        final ResourceType resourceType = configuration.getTypes().get(this.type);
        int checkFileExt = FileUtils.checkFileExtension(this.newFileName, resourceType);
        if (checkFileExt == 1) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION;
            return false;
        }
        if (configuration.ckeckDoubleFileExtensions()) {
            this.newFileName = FileUtils.renameFileWithBadExt(resourceType, this.newFileName);
        }

        try {
            Path file = Paths.get(path, getFinalFileName(path, this.newFileName));
            if (!(ImageUtils.isImage(file) && configuration.checkSizeAfterScaling())
                    && !FileUtils.checkFileSize(resourceType, item.getSize())) {
                this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_TOO_BIG;
                return false;
            }

            if (configuration.getSecureImageUploads() && ImageUtils.isImage(file)
                    && !ImageUtils.checkImageFile(item)) {
                this.errorCode
                        = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_CORRUPT;
                return false;
            }

            if (!FileUtils.checkIfFileIsHtmlFile(file.getFileName().toString(), configuration)
                    && FileUtils.detectHtml(item)) {
                this.errorCode
                        = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UPLOADED_WRONG_HTML_FILE;
                return false;
            }
        } catch (SecurityException | IOException e) {
            log.error("", e);
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
            return false;
        }

        return true;
    }

    /**
     * set response headers. Not user in this command.
     *
     * @param response response
     * @param sc servlet context
     */
    @Override
    public void setResponseHeader(HttpServletResponse response,
            ServletContext sc) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
    }

    /**
     * save if uploaded file item name is full file path not only file name.
     *
     * @param item file upload item
     * @return file name of uploaded item
     */
    private String getFileItemName(Part item) {
        Pattern p = Pattern.compile("[^\\\\/]+$");
        Matcher m = p.matcher(item.getSubmittedFileName());

        return (m.find()) ? m.group(0) : "";
    }

    /**
     * check request for security issue.
     *
     * @param reqParam request param
     * @return true if validation passed
     * @throws ConnectorException if validation error occurs.
     */
    @Override
    protected boolean checkParam(String reqParam)
            throws ConnectorException {
        if (reqParam == null || reqParam.isEmpty()) {
            return true;
        }
        if (Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(reqParam).find()) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkHidden()
            throws ConnectorException {
        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            return true;
        }
        return false;
    }

    @Override
    protected boolean checkConnector(HttpServletRequest request)
            throws ConnectorException {
        if (!configuration.enabled() || !configuration.checkAuthentication(request)) {
            this.errorCode
                    = Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED;
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkIfCurrFolderExists(HttpServletRequest request)
            throws ConnectorException {
        String tmpType = request.getParameter("type");
        if (checkIfTypeExists(tmpType)) {
            Path currDir = Paths.get(configuration.getTypes().get(tmpType).getPath()
                    + this.currentFolder);
            if (Files.exists(currDir) && Files.isDirectory(currDir)) {
                return true;
            } else {
                this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND;
                return false;
            }
        }
        return false;
    }

    @Override
    protected boolean checkIfTypeExists(String type) {
        ResourceType testType = configuration.getTypes().get(type);
        if (testType == null) {
            this.errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
            return false;
        }
        return true;
    }

}
