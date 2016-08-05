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
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.utils.XMLCreator;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Element;

/**
 * Base class to handle XML commands.
 */
public abstract class XMLCommand extends Command {

    /**
     * util to create XML document.
     */
    @SuppressWarnings("ProtectedField")
    protected XMLCreator creator;

    /**
     * sets response headers for XML response.
     *
     * @param response response
     * @param sc servlet context
     */
    @Override
    public void setResponseHeader(HttpServletResponse response,
            ServletContext sc) {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("utf-8");
    }

    /**
     * executes XML command. Creates XML response and writes it to response
     * output stream.
     *
     * @param out response output stream
     * @throws ConnectorException to handle in error handler.
     */
    @Override
    public void execute(OutputStream out) throws ConnectorException {
        try {
            createXMLResponse(getDataForXml());
            out.write(creator.getDocumentAsText().getBytes("UTF-8"));
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
        Element rootElement = creator.getDocument().createElement("Connector");
        if (this.type != null && !type.isEmpty()) {
            rootElement.setAttribute("resourceType", this.type);
        }
        if (mustAddCurrentFolderNode()) {
            createCurrentFolderNode(rootElement);
        }
        creator.addErrorCommandToRoot(rootElement, errorNum, getErrorMsg(errorNum));
        createXMLChildNodes(errorNum, rootElement);
        creator.getDocument().appendChild(rootElement);
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
     * @throws ConnectorException to handle in error handler.
     * @throws java.io.IOException
     */
    protected abstract void createXMLChildNodes(int errorNum,
            Element rootElement)
            throws ConnectorException, IOException;

    /**
     * gets all necessary data to create XML response.
     *
     * @return error code
     * {@link com.ckfinder.connector.configuration.Constants.Errors} or
     * {@link com.ckfinder.connector.configuration.Constants.Errors#CKFINDER_CONNECTOR_ERROR_NONE}
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
        Element element = creator.getDocument().createElement("CurrentFolder");
        element.setAttribute("path", this.currentFolder);
        element.setAttribute("url", configuration.getTypes().get(this.type).getUrl()
                + this.currentFolder);
        element.setAttribute("acl", String.valueOf(configuration.getAccessControl().checkACLForRole(this.type,
                this.currentFolder, this.userRole)));
        rootElement.appendChild(element);
    }

    @Override
    protected void initParams(HttpServletRequest request, IConfiguration configuration)
            throws ConnectorException {
        super.initParams(request, configuration);
        creator = new XMLCreator();
        creator.createDocument();
    }

    /**
     * whether <code>CurrentFolder</code> element should be added to the XML
     * response.
     *
     * @return true if must.
     */
    protected boolean mustAddCurrentFolderNode() {
        return this.type != null && this.currentFolder != null;
    }

}
