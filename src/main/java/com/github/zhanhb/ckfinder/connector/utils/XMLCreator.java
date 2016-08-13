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
package com.github.zhanhb.ckfinder.connector.utils;

import com.github.zhanhb.ckfinder.connector.configuration.Constants;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class to create XML document.
 */
public class XMLCreator {

    /**
     * dom4j document.
     */
    private Document document;
    /**
     *
     * errors list.
     */
    private final List<ErrorNode> errorList;

    /**
     * standard constructor.
     */
    public XMLCreator() {
        this.errorList = new ArrayList<>(4);
    }

    /**
     * Creates document.
     *
     * @throws ConnectorException if a DocumentBuilder cannot be created which
     * satisfies the configuration requested.
     */
    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    public void createDocument() throws ConnectorException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
            document.setXmlStandalone(true);
        } catch (Exception e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    /**
     * gets document.
     *
     * @return document
     */
    public Document getDocument() {
        return document;
    }

    public void writeTo(Writer writer) throws ConnectorException {
        try {
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.transform(new DOMSource(document), new StreamResult(writer));
        } catch (TransformerException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }
    }

    /**
     * adds error node to root element with error code.
     *
     * @param rootElement XML root node.
     * @param errorNum error code number.
     * @param errorText error text.
     */
    public void addErrorCommandToRoot(Element rootElement,
            int errorNum, String errorText) {
        // errors
        Element element = this.getDocument().createElement("Error");
        element.setAttribute("number", String.valueOf(errorNum));
        if (errorText != null) {
            element.setTextContent(errorText);
        }
        rootElement.appendChild(element);
    }

    /**
     * save errors node to list.
     *
     * @param errorCode error code
     * @param name file name
     * @param path current folder
     * @param type resource type
     */
    public void appendErrorNodeChild(int errorCode, String name,
            String path, String type) {
        errorList.add(ErrorNode.builder().type(type).name(name).folder(path).errorCode(errorCode).build());
    }

    /**
     * add all error nodes from saved list to xml.
     *
     * @param errorsNode XML errors node
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public void addErrors(Element errorsNode) {
        for (ErrorNode item : this.errorList) {
            Element childElem = this.getDocument().createElement("Error");
            childElem.setAttribute("code", String.valueOf(item.getErrorCode()));
            childElem.setAttribute("name", item.getName());
            childElem.setAttribute("type", item.getType());
            childElem.setAttribute("folder", item.getFolder());
            errorsNode.appendChild(childElem);
        }
    }

    /**
     * checks if error list contains errors.
     *
     * @return true if there are any errors.
     */
    public boolean hasErrors() {
        return !errorList.isEmpty();
    }

}
