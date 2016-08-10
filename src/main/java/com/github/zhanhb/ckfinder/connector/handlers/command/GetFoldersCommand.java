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
import com.github.zhanhb.ckfinder.connector.data.XmlAttribute;
import com.github.zhanhb.ckfinder.connector.data.XmlElementData;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

/**
 * Class to handle <code>GetFolders</code> command.
 */
@Slf4j
public class GetFoldersCommand extends XMLCommand {

    /**
     * list of subdirectories in directory.
     */
    private List<String> directories;

    @Override
    protected void createXMLChildNodes(int errorNum, Element rootElement) throws IOException {
        if (errorNum == Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE) {
            createFoldersData(rootElement);
        }
    }

    /**
     * gets data for response.
     *
     * @return 0 if everything went ok or error code otherwise
     * @throws java.io.IOException
     */
    @Override
    protected int getDataForXml() throws IOException {
        if (!checkIfTypeExists(getType())) {
            this.setType(null);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!getConfiguration().getAccessControl().checkFolderACL(getType(),
                getCurrentFolder(),
                getUserRole(),
                AccessControl.CKFINDER_CONNECTOR_ACL_FOLDER_VIEW)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }
        if (FileUtils.checkIfDirIsHidden(this.getCurrentFolder(), getConfiguration())) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        Path dir = Paths.get(getConfiguration().getTypes().get(this.getType()).getPath()
                + this.getCurrentFolder());
        try {
            if (!Files.exists(dir)) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND;
            }

            directories = FileUtils.findChildrensList(dir, true);
        } catch (SecurityException e) {
            log.error("", e);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
        }
        filterListByHiddenAndNotAllowed();
        Collections.sort(directories);
        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

    /**
     * filters list and check if every element is not hidden and have correct
     * ACL.
     */
    private void filterListByHiddenAndNotAllowed() {
        List<String> tmpDirs = this.directories.stream()
                .filter(dir -> (getConfiguration().getAccessControl().checkFolderACL(this.getType(), this.getCurrentFolder() + dir, this.getUserRole(),
                        AccessControl.CKFINDER_CONNECTOR_ACL_FOLDER_VIEW)
                        && !FileUtils.checkIfDirIsHidden(dir, getConfiguration())))
                .collect(Collectors.toList());

        this.directories.clear();
        this.directories.addAll(tmpDirs);

    }

    /**
     * creates folder data node in XML document.
     *
     * @param rootElement root element in XML document
     */
    private void createFoldersData(Element rootElement) throws IOException {
        Element element = getCreator().getDocument().createElement("Folders");
        for (String dirPath : directories) {
            Path dir = Paths.get(this.getConfiguration().getTypes().get(this.getType()).getPath()
                    + this.getCurrentFolder()
                    + dirPath);
            if (Files.exists(dir)) {
                XmlElementData xmlElementData = new XmlElementData("Folder");
                xmlElementData.getAttributes().add(new XmlAttribute("name", dirPath));

                xmlElementData.getAttributes().add(new XmlAttribute("hasChildren",
                        FileUtils.hasChildren(getConfiguration().getAccessControl(), this.getCurrentFolder() + dirPath + "/", dir, getConfiguration(), this.getType(), this.getUserRole()).toString()));

                xmlElementData.getAttributes().add(new XmlAttribute("acl",
                        String.valueOf(getConfiguration().getAccessControl().checkACLForRole(this.getType(),
                                this.getCurrentFolder()
                                + dirPath, this.getUserRole()))));
                xmlElementData.addToDocument(getCreator().getDocument(), element);
            }

        }
        rootElement.appendChild(element);
    }

}
