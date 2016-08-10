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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.w3c.dom.Element;

/**
 * Class to handle errors from commands returning XML response.
 */
public class XMLErrorCommand extends XMLCommand implements IErrorCommand {

    /**
     * exception to handle.
     */
    @Setter
    private ConnectorException connectorException;

    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);
        if (connectorException.isAddCurrentFolder()) {
            String tmpType = request.getParameter("type");
            if (checkIfTypeExists(tmpType)) {
                this.setType(tmpType);
            }
        }
    }

    @Override
    protected int getDataForXml() {
        return connectorException.getErrorCode();
    }

    @Override
    protected void createXMLChildNodes(int errorNum,
            Element rootElement) throws ConnectorException {
    }

    @Override
    protected String getErrorMsg(int errorNum) {
        return connectorException.getMessage();
    }

    /**
     * for error command there should be no exception throw because there is no
     * more exception handlers.
     *
     * @param reqParam request param
     * @return true if validation passed
     * @throws ConnectorException it should never throw an exception
     */
    @Override
    protected boolean checkParam(String reqParam)
            throws ConnectorException {
        return reqParam == null || reqParam.isEmpty()
                || !Pattern.compile(Constants.INVALID_PATH_REGEX).matcher(reqParam).find();
    }

    @Override
    protected boolean checkConnector(HttpServletRequest request)
            throws ConnectorException {
        if (!getConfiguration().enabled() || !getConfiguration().checkAuthentication(request)) {
            this.connectorException = new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED);
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkHidden() throws ConnectorException {
        if (FileUtils.checkIfDirIsHidden(this.getCurrentFolder(), getConfiguration())) {
            this.connectorException = new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED);
            return true;
        }
        return false;
    }

    @Override
    protected boolean checkIfCurrFolderExists(HttpServletRequest request)
            throws ConnectorException {
        String tmpType = request.getParameter("type");
        if (checkIfTypeExists(tmpType)) {
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
    protected boolean checkIfTypeExists(String type) {
        ResourceType testType = getConfiguration().getTypes().get(type);
        if (testType == null) {
            this.connectorException = new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE, false);
            return false;
        }
        return true;
    }

    @Override
    protected boolean mustAddCurrentFolderNode() {
        return connectorException.isAddCurrentFolder();
    }

    @Override
    protected void getCurrentFolderParam(HttpServletRequest request) {
        String currFolder = request.getParameter("currentFolder");
        if (!(currFolder == null || currFolder.isEmpty())) {
            this.setCurrentFolder(PathUtils.addSlashToBeginning(PathUtils.addSlashToEnd(currFolder)));
        }
    }

}
