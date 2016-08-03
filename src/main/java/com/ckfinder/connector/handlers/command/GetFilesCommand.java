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
import com.ckfinder.connector.data.XmlAttribute;
import com.ckfinder.connector.data.XmlElementData;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.utils.AccessControl;
import com.ckfinder.connector.utils.FileUtils;
import com.ckfinder.connector.utils.ImageUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

/**
 * Class to handle <code>GetFiles</code> command.
 */
@Slf4j
public class GetFilesCommand extends XMLCommand {

    /**
     * number of bytes in kilobyte.
     */
    private static final float BYTES = 1024f;
    /**
     * list of files.
     */
    private List<String> files;
    /**
     * temporary field to keep full path.
     */
    private String fullCurrentPath;
    /**
     * show thumb post param.
     */
    private String showThumbs;

    /**
     * initializing parameters for command handler.
     *
     * @param request request
     * @param configuration connector configuration
     * @throws ConnectorException when error occurs
     */
    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);

        this.showThumbs = request.getParameter("showThumbs");
    }

    @Override
    protected void createXMLChildNodes(int errorNum, Element rootElement)
            throws ConnectorException, IOException {
        if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            createFilesData(rootElement);
        }
    }

    /**
     * gets data to XML response.
     *
     * @return 0 if ok, otherwise error code
     * @throws java.io.IOException
     */
    @Override
    protected int getDataForXml() throws IOException {

        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        this.fullCurrentPath = configuration.getTypes().get(this.type).getPath()
                + this.currentFolder;

        if (!getAccessControl().checkFolderACL(this.type, this.currentFolder, this.userRole,
                AccessControl.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        Path dir = Paths.get(this.fullCurrentPath);
        try {
            if (!Files.exists(dir)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND;
            }
            files = FileUtils.findChildrensList(dir, false);
        } catch (SecurityException e) {
            log.error("", e);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
        }
        filterListByHiddenAndNotAllowed();
        Collections.sort(files);
        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

    /**
     *
     *
     */
    private void filterListByHiddenAndNotAllowed() {
        List<String> tmpFiles = new ArrayList<>();
        for (String file : this.files) {
            if (FileUtils.checkFileExtension(file, this.configuration.getTypes().get(this.type)) == 0
                    && !FileUtils.checkIfFileIsHidden(file, this.configuration)) {
                tmpFiles.add(file);
            }
        }

        this.files.clear();
        this.files.addAll(tmpFiles);

    }

    /**
     * creates files data node in response XML.
     *
     * @param rootElement root element from XML.
     */
    private void createFilesData(Element rootElement) throws IOException {
        Element element = creator.getDocument().createElement("Files");
        for (String filePath : files) {
            Path file = Paths.get(this.fullCurrentPath, filePath);
            if (Files.exists(file)) {
                XmlElementData elementData = new XmlElementData("File");
                XmlAttribute attribute = new XmlAttribute("name", filePath);
                elementData.getAttributes().add(attribute);
                attribute = new XmlAttribute("date",
                        FileUtils.parseLastModifDate(file));
                elementData.getAttributes().add(attribute);
                attribute = new XmlAttribute("size", getSize(file));
                elementData.getAttributes().add(attribute);
                if (ImageUtils.isImage(file) && isAddThumbsAttr()) {
                    String attr = createThumbAttr(file);
                    if (!attr.isEmpty()) {
                        attribute = new XmlAttribute("thumb", attr);
                        elementData.getAttributes().add(attribute);
                    }
                }
                elementData.addToDocument(this.creator.getDocument(), element);
            }
        }
        rootElement.appendChild(element);
    }

    /**
     * gets thumb attribute value.
     *
     * @param file file to check if has thumb.
     * @return thumb attribute values
     */
    private String createThumbAttr(Path file) {
        Path thumbFile = Paths.get(configuration.getThumbsPath(),
                this.type + this.currentFolder,
                file.getFileName().toString());
        if (Files.exists(thumbFile)) {
            return file.getFileName().toString();
        } else if (isShowThumbs()) {
            return "?".concat(file.getFileName().toString());
        } else {
            return "";
        }
    }

    /**
     * get file size.
     *
     * @param file file
     * @return file size
     */
    private String getSize(Path file) throws IOException {
        long size = Files.size(file);
        if (size > 0 && size < BYTES) {
            return "1";
        } else {
            return String.valueOf(Math.round(size / BYTES));
        }
    }

    /**
     * Check if show thumbs or not (add attr to file node with thumb file name).
     *
     * @return true if show thumbs
     */
    private boolean isAddThumbsAttr() {
        return configuration.getThumbsEnabled()
                && (configuration.getThumbsDirectAccess()
                || isShowThumbs());
    }

    /**
     * checks show thumb request attribute.
     *
     * @return true if is set.
     */
    private boolean isShowThumbs() {
        return (this.showThumbs != null && this.showThumbs.equals("1"));
    }
}
