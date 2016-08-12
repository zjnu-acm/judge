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
import com.github.zhanhb.ckfinder.connector.data.ResourceType;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import com.github.zhanhb.ckfinder.connector.utils.PathUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Setter;

/**
 * Class to handle errors via HTTP headers (for non-XML commands).
 */
public class ErrorCommand extends Command implements IErrorCommand {

    @Setter
    private ConnectorException connectorException;
    private HttpServletResponse response;

    @Override
    protected void execute(OutputStream out) throws ConnectorException {
        try {
            response.setHeader("X-CKFinder-Error", String.valueOf(connectorException.getErrorCode()));
            switch (connectorException.getErrorCode()) {
                case Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST:
                case Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME:
                case Constants.Errors.CKFINDER_CONNECTOR_ERROR_THUMBNAILS_DISABLED:
                case Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED:
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    break;
                case Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED:
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (IOException ioex) {
            throw new ConnectorException(ioex);
        }
    }

    @Override
    public void setResponseHeader(HttpServletResponse response, ServletContext sc) {
        response.reset();
        this.response = response;
    }

    @Override
    protected void initParams(HttpServletRequest request,
            IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);
    }

    /**
     * for error command there should be no exception thrown because there are
     * no more exception handlers.
     *
     * @param reqParam request param
     * @return true if validation passed
     * @throws ConnectorException it should never throw an exception
     */
    @Override
    protected boolean isRequestPathValid(String reqParam) throws ConnectorException {
        return reqParam == null || reqParam.isEmpty()
                || !Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(reqParam).find();
    }

    @Override
    protected boolean isHidden() throws ConnectorException {
        if (FileUtils.isDirectoryHidden(this.getCurrentFolder(), getConfiguration())) {
            this.connectorException = new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED);
            return true;
        }
        return false;
    }

    @Override
    protected boolean isConnectorEnabled()
            throws ConnectorException {
        if (!getConfiguration().isEnabled()) {
            this.connectorException = new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED);
            return false;
        }
        return true;
    }

    @Override
    protected boolean isCurrFolderExists(HttpServletRequest request)
            throws ConnectorException {
        String tmpType = request.getParameter("type");
        if (isTypeExists(tmpType)) {
            Path currDir = Paths.get(getConfiguration().getTypes().get(tmpType).getPath()
                    + this.getCurrentFolder());
            if (Files.exists(currDir) && Files.isDirectory(currDir)) {
                return true;
            } else {
                this.connectorException = new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND);
                return false;
            }
        }
        return false;
    }

    @Override
    protected boolean isTypeExists(String type) {
        ResourceType testType = getConfiguration().getTypes().get(type);
        if (testType == null) {
            this.connectorException = new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE, false);
            return false;
        }
        return true;
    }

    @Override
    protected void getCurrentFolderParam(HttpServletRequest request) {
        String currFolder = request.getParameter("currentFolder");
        if (!(currFolder == null || currFolder.isEmpty())) {
            this.setCurrentFolder(PathUtils.addSlashToBeginning(PathUtils.addSlashToEnd(currFolder)));
        }
    }

}
