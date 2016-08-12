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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class to handle <code>DownloadFile</code> command.
 */
public class DownloadFileCommand extends Command {

    /**
     * File to download.
     */
    private Path file;
    /**
     * filename request param.
     */
    private String fileName;
    private String newFileName;

    /**
     * executes the download file command. Writes file to response.
     *
     * @param out output stream
     * @throws ConnectorException when something went wrong during reading file.
     */
    @Override
    protected void execute(OutputStream out) throws ConnectorException {
        if (!isTypeExists(getType())) {
            this.setType(null);
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE, false);
        }

        this.file = Paths.get(getConfiguration().getTypes().get(this.getType()).getPath()
                + getCurrentFolder(), fileName);

        if (!getConfiguration().getAccessControl().checkFolderACL(getType(), getCurrentFolder(), getUserRole(),
                AccessControl.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED);
        }

        if (!FileUtils.isFileNameInvalid(this.fileName)
                || FileUtils.checkFileExtension(this.fileName,
                        this.getConfiguration().getTypes().get(this.getType())) == 1) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST);
        }

        if (FileUtils.isDirectoryHidden(this.getCurrentFolder(), getConfiguration())) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST);
        }
        try {
            if (!Files.exists(file)
                    || !Files.isRegularFile(file)
                    || FileUtils.isFileHidden(this.fileName, this.getConfiguration())) {
                throw new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND);
            }

            FileUtils.printFileContentToResponse(file, out);
        } catch (IOException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    /**
     * inits params for download file command.
     *
     * @param request request
     * @param configuration connector configuration
     * @throws ConnectorException when error occurs.
     */
    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {

        super.initParams(request, configuration);
        // problem with showing filename when dialog window appear
        this.newFileName = request.getParameter("FileName").replace("\"", "\\\"");
        this.fileName = request.getParameter("FileName");
        try {
            if (request.getHeader("User-Agent").contains("MSIE")) {
                this.newFileName = URLEncoder.encode(this.newFileName, "UTF-8");
                this.newFileName = this.newFileName.replace("+", " ").replace("%2E", ".");
            } else {
                this.newFileName = MimeUtility.encodeWord(this.newFileName, "utf-8", "Q");
            }
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }

    }

    /**
     * Sets response headers.
     *
     * @param response response
     * @param sc servlet context
     */
    @Override
    public void setResponseHeader(HttpServletResponse response,
            ServletContext sc) {
        String mimetype = sc.getMimeType(fileName);
        response.setCharacterEncoding("utf-8");

        if (mimetype != null) {
            response.setContentType(mimetype);
        } else {
            response.setContentType("application/octet-stream");
        }
        if (file != null) {
            try {
                response.setContentLengthLong(Files.size(file));
            } catch (IOException ex) {
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=\""
                + this.newFileName + "\"");

        response.setHeader("Cache-Control", "cache, must-revalidate");
        response.setHeader("Pragma", "public");
        response.setHeader("Expires", "0");
    }

}
