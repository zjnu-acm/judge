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
package com.ckfinder.connector.plugins;

import com.ckfinder.connector.configuration.Constants;
import com.ckfinder.connector.configuration.IConfiguration;
import com.ckfinder.connector.data.BeforeExecuteCommandEventArgs;
import com.ckfinder.connector.data.IEventHandler;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.handlers.command.XMLCommand;
import com.ckfinder.connector.utils.AccessControl;
import com.ckfinder.connector.utils.FileUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

@Slf4j
public class ImageResizeInfoCommand extends XMLCommand implements IEventHandler<BeforeExecuteCommandEventArgs> {

    private int imageWidth;
    private int imageHeight;
    private String fileName;

    @Override
    public boolean runEventHandler(BeforeExecuteCommandEventArgs args, IConfiguration configuration1)
            throws ConnectorException {
        if ("ImageResizeInfo".equals(args.getCommand())) {
            this.runCommand(args.getRequest(), args.getResponse(), configuration1);
            return false;
        }
        return true;
    }

    @Override
    protected void createXMLChildNodes(int errorNum, Element rootElement)
            throws ConnectorException {
        if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            createImageInfoNode(rootElement);
        }

    }

    private void createImageInfoNode(Element rootElement) {
        Element element = creator.getDocument().createElement("ImageInfo");
        element.setAttribute("width", String.valueOf(imageWidth));
        element.setAttribute("height", String.valueOf(imageHeight));
        rootElement.appendChild(element);

    }

    @Override
    protected int getDataForXml() {
        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!configuration.getAccessControl().checkFolderACL(type, this.currentFolder,
                userRole, AccessControl.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        if (fileName == null || fileName.isEmpty()
                || !FileUtils.checkFileName(this.fileName)
                || FileUtils.checkIfFileIsHidden(this.fileName, this.configuration)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        if (FileUtils.checkFileExtension(fileName, configuration.getTypes().get(type)) == 1) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        Path imageFile = Paths.get(configuration.getTypes().get(this.type).getPath()
                + this.currentFolder,
                this.fileName);

        try {
            if (!(Files.exists(imageFile) && Files.isRegularFile(imageFile))) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND;
            }

            BufferedImage image = ImageIO.read(imageFile.toFile());
            this.imageWidth = image.getWidth();
            this.imageHeight = image.getHeight();
        } catch (SecurityException | IOException e) {
            log.error("", e);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
        }

        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);
        this.imageHeight = 0;
        this.imageWidth = 0;
        this.currentFolder = request.getParameter("currentFolder");
        this.type = request.getParameter("type");
        this.fileName = request.getParameter("fileName");
    }

}
