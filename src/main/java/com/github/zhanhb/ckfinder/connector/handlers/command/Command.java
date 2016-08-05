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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base class for all command handlers.
 */
@SuppressWarnings("ProtectedField")
public abstract class Command {

    /**
     * Connector configuration.
     */
    protected IConfiguration configuration;
    protected String userRole;
    protected String currentFolder;
    protected String type;

    /**
     * standard constructor.
     */
    public Command() {
        configuration = null;
        userRole = null;
        currentFolder = null;
        type = null;
    }

    /**
     * Runs command. Initialize, sets response and execute command.
     *
     * @param request request
     * @param response response
     * @param configuration connector configuration
     * @throws ConnectorException when error occurred.
     */
    public void runCommand(HttpServletRequest request,
            HttpServletResponse response,
            IConfiguration configuration) throws ConnectorException {
        this.initParams(request, configuration);
        try {
            setResponseHeader(response, request.getServletContext());
            try (ServletOutputStream out = response.getOutputStream()) {
                execute(out);
                out.flush();
            }
        } catch (IOException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }
    }

    /**
     * initialize params for command handler.
     *
     * @param request request
     * @param configuration connector configuration
     * @throws ConnectorException to handle in error handler.
     */
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {
        if (configuration != null) {
            this.configuration = configuration;
            this.userRole = (String) request.getSession().getAttribute(
                    this.configuration.getUserRoleName());

            getCurrentFolderParam(request);

            if (checkConnector(request) && checkParam(this.currentFolder)) {
                this.currentFolder = PathUtils.escape(this.currentFolder);
                if (!checkHidden()) {
                    if ((this.currentFolder == null || this.currentFolder.isEmpty())
                            || checkIfCurrFolderExists(request)) {
                        this.type = request.getParameter("type");
                    }
                }
            }
        }
    }

    /**
     * check if connector is enabled and checks authentication.
     *
     * @param request current request.
     * @return true if connector is enabled and user is authenticated
     * @throws ConnectorException when connector is disabled
     */
    protected boolean checkConnector(HttpServletRequest request)
            throws ConnectorException {
        if (!configuration.enabled() || !configuration.checkAuthentication(request)) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED, false);
        }
        return true;
    }

    /**
     * Checks if current folder exists.
     *
     * @param request current request object
     * @return {@code true} if current folder exists
     * @throws ConnectorException if current folder doesn't exist
     */
    protected boolean checkIfCurrFolderExists(HttpServletRequest request)
            throws ConnectorException {
        String tmpType = request.getParameter("type");
        if (tmpType != null) {
            if (checkIfTypeExists(tmpType)) {
                Path currDir = Paths.get(
                        configuration.getTypes().get(tmpType).getPath()
                        + this.currentFolder);
                if (!Files.exists(currDir) || !Files.isDirectory(currDir)) {
                    throw new ConnectorException(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND,
                            false);
                } else {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Checks if type of resource provided as parameter exists.
     *
     * @param type name of the resource type to check if it exists
     * @return {@code true} if provided type exists, {@code false} otherwise.
     */
    protected boolean checkIfTypeExists(String type) {
        ResourceType testType = configuration.getTypes().get(type);
        return testType != null;
    }

    /**
     * checks if current folder is hidden.
     *
     * @return false if isn't.
     * @throws ConnectorException when is hidden
     */
    protected boolean checkHidden() throws ConnectorException {
        if (FileUtils.checkIfDirIsHidden(this.currentFolder, configuration)) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST,
                    false);
        }
        return false;
    }

    /**
     * executes command and writes to response.
     *
     * @param out response output stream
     * @throws ConnectorException when error occurs
     * @throws java.io.IOException
     */
    public abstract void execute(OutputStream out)
            throws ConnectorException, IOException;

    /**
     * sets header in response.
     *
     * @param response servlet response
     * @param sc servlet context
     */
    public abstract void setResponseHeader(HttpServletResponse response,
            ServletContext sc);

    /**
     * check request for security issue.
     *
     * @param reqParam request param
     * @return true if validation passed
     * @throws ConnectorException if validation error occurs.
     */
    protected boolean checkParam(String reqParam)
            throws ConnectorException {
        if (reqParam == null || reqParam.isEmpty()) {
            return true;
        }
        if (Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(reqParam).find()) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME,
                    false);
        }

        return true;
    }

    /**
     * gets current folder request param or sets default value if it's not set.
     *
     * @param request request
     */
    protected void getCurrentFolderParam(HttpServletRequest request) {
        String currFolder = request.getParameter("currentFolder");
        if (currFolder == null || currFolder.isEmpty()) {
            this.currentFolder = "/";
        } else {
            this.currentFolder = PathUtils.addSlashToBeginning(PathUtils.addSlashToEnd(currFolder));
        }
    }

    /**
     * If string provided as parameter is null, this method converts it to empty
     * string.
     *
     * @param s string to check and convert if it is null
     * @return empty string if parameter was {@code null} or unchanged string if
     * parameter was nonempty string.
     */
    protected String nullToString(String s) {
        return s == null ? "" : s;
    }

}
