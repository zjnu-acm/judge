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
import com.github.zhanhb.ckfinder.connector.utils.XMLCreator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Base class to handle XML commands.
 */
public abstract class XMLCommand extends Command {

    @Getter(AccessLevel.PROTECTED)
    private Document document;

    /**
     *
     * errors list.
     */
    private final List<ErrorNode> errorList = new ArrayList<>(4);

    /**
     * sets response headers for XML response.
     *
     * @param request
     * @param response response
     */
    @Override
    public void setResponseHeader(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("utf-8");
    }

    /**
     * executes XML command. Creates XML response and writes it to response
     * output stream.
     *
     * @throws ConnectorException to handle in error handler.
     */
    @Override
    @SuppressWarnings("FinalMethod")
    protected final void execute(HttpServletResponse response) throws ConnectorException {
        try (PrintWriter out = response.getWriter()) {
            createXMLResponse(getDataForXml());
            XMLCreator.INSTANCE.writeTo(document, out);
        } catch (ConnectorException e) {
            throw e;
        } catch (IOException e) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }
    }

    /**
     * abstract method to create XML response in command.
     *
     * @param errorNum error code from method getDataForXml()
     * @throws ConnectorException to handle in error handler.
     */
    private void createXMLResponse(int errorNum) throws ConnectorException, IOException {
        Element rootElement = document.createElement("Connector");
        if (getType() != null && !getType().isEmpty()) {
            rootElement.setAttribute("resourceType", getType());
        }
        if (mustAddCurrentFolderNode()) {
            createCurrentFolderNode(rootElement);
        }
        XMLCreator.INSTANCE.addErrorCommandToRoot(document, rootElement, errorNum, getErrorMsg(errorNum));
        createXMLChildNodes(errorNum, rootElement);
        document.appendChild(rootElement);
    }

    /**
     * gets error message if needed.
     *
     * @param errorNum error code
     * @return error message
     */
    protected String getErrorMsg(int errorNum) {
        return null;
    }

    /**
     * abstract method to create XML nodes for commands.
     *
     * @param errorNum error code
     * @param rootElement XML root node
     * @throws java.io.IOException
     */
    protected abstract void createXMLChildNodes(int errorNum, Element rootElement) throws IOException;

    /**
     * gets all necessary data to create XML response.
     *
     * @return error code
     * {@link com.github.zhanhb.ckfinder.connector.configuration.Constants.Errors}
     * or
     * {@link com.github.zhanhb.ckfinder.connector.configuration.Constants.Errors#CKFINDER_CONNECTOR_ERROR_NONE}
     * if no error occurred.
     * @throws java.io.IOException
     */
    protected abstract int getDataForXml() throws IOException;

    /**
     * creates <code>CurrentFolder</code> element.
     *
     * @param rootElement XML root node.
     */
    protected final void createCurrentFolderNode(Element rootElement) {
        Element element = document.createElement("CurrentFolder");
        element.setAttribute("path", getCurrentFolder());
        element.setAttribute("url", getConfiguration().getTypes().get(getType()).getUrl()
                + getCurrentFolder());
        element.setAttribute("acl", String.valueOf(getConfiguration().getAccessControl().checkACLForRole(getType(), getCurrentFolder(), getUserRole())));
        rootElement.appendChild(element);
    }

    @Override
    protected void initParams(HttpServletRequest request,
            IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);
        document = XMLCreator.INSTANCE.createDocument();
    }

    /**
     * whether <code>CurrentFolder</code> element should be added to the XML
     * response.
     *
     * @return true if must.
     */
    protected boolean mustAddCurrentFolderNode() {
        return getType() != null && getCurrentFolder() != null;
    }

    /**
     * save errors node to list.
     *
     * @param errorCode error code
     * @param name file name
     * @param path current folder
     * @param type resource type
     */
    protected final void appendErrorNodeChild(int errorCode, String name,
            String path, String type) {
        errorList.add(ErrorNode.builder().type(type).name(name).folder(path).errorCode(errorCode).build());
    }

    /**
     * checks if error list contains errors.
     *
     * @return true if there are any errors.
     */
    protected final boolean hasErrors() {
        return !errorList.isEmpty();
    }

    /**
     * add all error nodes from saved list to xml.
     *
     * @param errorsNode XML errors node
     */
    protected final void addErrors(Element errorsNode) {
        for (ErrorNode item : errorList) {
            Element childElem = document.createElement("Error");
            childElem.setAttribute("code", String.valueOf(item.getErrorCode()));
            childElem.setAttribute("name", item.getName());
            childElem.setAttribute("type", item.getType());
            childElem.setAttribute("folder", item.getFolder());
            errorsNode.appendChild(childElem);
        }
    }

}
