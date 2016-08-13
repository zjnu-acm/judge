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
public enum XMLCreator {
    INSTANCE;

    /**
     * Creates document.
     *
     * @return
     * @throws ConnectorException if a DocumentBuilder cannot be created which
     * satisfies the configuration requested.
     */
    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    public Document createDocument() throws ConnectorException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            document.setXmlStandalone(true);
            return document;
        } catch (Exception e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    public void writeTo(Document document, Writer writer) throws ConnectorException {
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
     * @param document
     * @param rootElement XML root node.
     * @param errorNum error code number.
     * @param errorText error text.
     */
    public void addErrorCommandToRoot(Document document, Element rootElement, int errorNum, String errorText) {
        // errors
        Element element = document.createElement("Error");
        element.setAttribute("number", String.valueOf(errorNum));
        if (errorText != null) {
            element.setTextContent(errorText);
        }
        rootElement.appendChild(element);
    }

}
