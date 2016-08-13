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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Element;

/**
 * Base class to handle XML commands.
 */
public abstract class XMLCommand extends Command {

    @Deprecated
    private final XMLCreator creator = new XMLCreator();

    /**
     * sets response headers for XML response.
     *
     * @param response response
     * @param sc servlet context
     */
    @Override
    public void setResponseHeader(HttpServletResponse response, ServletContext sc) {
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
            getCreator().writeTo(out);
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
        Element rootElement = getCreator().getDocument().createElement("Connector");
        if (getType() != null && !getType().isEmpty()) {
            rootElement.setAttribute("resourceType", getType());
        }
        if (mustAddCurrentFolderNode()) {
            createCurrentFolderNode(rootElement);
        }
        getCreator().addErrorCommandToRoot(rootElement, errorNum, getErrorMsg(errorNum));
        createXMLChildNodes(errorNum, rootElement);
        getCreator().getDocument().appendChild(rootElement);
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
    protected void createCurrentFolderNode(Element rootElement) {
        Element element = getCreator().getDocument().createElement("CurrentFolder");
        element.setAttribute("path", getCurrentFolder());
        element.setAttribute("url", getConfiguration().getTypes().get(getType()).getUrl()
                + getCurrentFolder());
        element.setAttribute("acl", String.valueOf(getConfiguration().getAccessControl().checkACLForRole(getType(), getCurrentFolder(), getUserRole())));
        rootElement.appendChild(element);
    }

    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);
        getCreator().createDocument();
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

    protected XMLCreator getCreator() {
        return creator;
    }

}
