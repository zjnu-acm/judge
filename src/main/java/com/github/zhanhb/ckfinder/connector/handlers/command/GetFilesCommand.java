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
import com.github.zhanhb.ckfinder.connector.data.XmlAttribute;
import com.github.zhanhb.ckfinder.connector.data.XmlElementData;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import com.github.zhanhb.ckfinder.connector.utils.ImageUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
    protected void createXMLChildNodes(int errorNum, Element rootElement) throws IOException {
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
        if (!checkIfTypeExists(getType())) {
            this.setType(null);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        this.fullCurrentPath = getConfiguration().getTypes().get(this.getType()).getPath()
                + this.getCurrentFolder();

        if (!getConfiguration().getAccessControl().checkFolderACL(getType(), getCurrentFolder(), getUserRole(),
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
        List<String> tmpFiles = this.files.stream()
                .filter(file -> (FileUtils.checkFileExtension(file, this.getConfiguration().getTypes().get(this.getType())) == 0
                        && !FileUtils.checkIfFileIsHidden(file, getConfiguration())))
                .collect(Collectors.toList());

        this.files.clear();
        this.files.addAll(tmpFiles);

    }

    /**
     * creates files data node in response XML.
     *
     * @param rootElement root element from XML.
     */
    private void createFilesData(Element rootElement) throws IOException {
        Element element = getCreator().getDocument().createElement("Files");
        for (String filePath : files) {
            Path file = Paths.get(this.fullCurrentPath, filePath);
            if (Files.exists(file)) {
                XmlElementData.Builder elementData = XmlElementData.builder().name("File");
                elementData.attribute(new XmlAttribute("name", filePath))
                        .attribute(new XmlAttribute("date", FileUtils.parseLastModifDate(file)))
                        .attribute(new XmlAttribute("size", getSize(file)));
                if (ImageUtils.isImageExtension(file) && isAddThumbsAttr()) {
                    String attr = createThumbAttr(file);
                    if (!attr.isEmpty()) {
                        elementData.attribute(new XmlAttribute("thumb", attr));
                    }
                }
                elementData.build().addToDocument(getCreator().getDocument(), element);
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
        Path thumbFile = Paths.get(getConfiguration().getThumbsPath(),
                this.getType() + this.getCurrentFolder(),
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
        return getConfiguration().getThumbsEnabled()
                && (getConfiguration().getThumbsDirectAccess()
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
