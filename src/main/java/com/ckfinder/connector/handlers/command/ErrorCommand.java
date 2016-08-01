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
import com.ckfinder.connector.data.ResourceType;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.utils.FileUtils;
import com.ckfinder.connector.utils.PathUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class to handle errors via HTTP headers (for non-XML commands).
 */
public class ErrorCommand extends Command {

    private ConnectorException e;
    private HttpServletResponse response;

    @Override
    public void execute(final OutputStream out) throws ConnectorException {
        try {
            response.setHeader("X-CKFinder-Error", String.valueOf(e.getErrorCode()));
            switch (e.getErrorCode()) {
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
    public void setResponseHeader(final HttpServletResponse response,
            final ServletContext sc) {
        response.reset();
        this.response = response;

    }

    @Override
    protected void initParams(final HttpServletRequest request,
            final IConfiguration configuration, final Object... params)
            throws ConnectorException {
        super.initParams(request, configuration, params);
        e = (ConnectorException) params[0];
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
    protected boolean checkParam(final String reqParam) throws ConnectorException {
        return reqParam == null || reqParam.isEmpty()
                || !Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(reqParam).find();
    }

    @Override
    protected boolean checkHidden()
            throws ConnectorException {
        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            this.e = new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED);
            return true;
        }
        return false;
    }

    @Override
    protected boolean checkConnector(final HttpServletRequest request)
            throws ConnectorException {
        if (!configuration.enabled() || !configuration.checkAuthentication(request)) {
            this.e = new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED);
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkIfCurrFolderExists(final HttpServletRequest request)
            throws ConnectorException {
        String tmpType = getParameter(request, "type");
        if (checkIfTypeExists(tmpType)) {
            Path currDir = Paths.get(configuration.getTypes().get(tmpType).getPath()
                    + this.currentFolder);
            if (Files.exists(currDir) && Files.isDirectory(currDir)) {
                return true;
            } else {
                this.e = new ConnectorException(
                        Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND);
                return false;
            }
        }
        return false;
    }

    @Override
    protected boolean checkIfTypeExists(final String type) {
        ResourceType testType = configuration.getTypes().get(type);
        if (testType == null) {
            this.e = new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE, false);
            return false;
        }
        return true;
    }

    @Override
    protected void getCurrentFolderParam(final HttpServletRequest request) {
        String currFolder = getParameter(request, "currentFolder");
        if (!(currFolder == null || currFolder.isEmpty())) {
            this.currentFolder = PathUtils.addSlashToBeginning(PathUtils.addSlashToEnd(currFolder));
        }
    }
}
