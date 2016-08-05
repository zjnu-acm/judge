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
package com.ckfinder.connector.data;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * XML element.
 */
@Getter
@Setter
public class XmlElementData {

    /**
     * node name.
     */
    private String name;
    /**
     * node text value.
     */
    private String value;
    /**
     * list of attributes of node.
     */
    private List<XmlAttribute> attributes;
    /**
     * list of child nodes.
     */
    private List<XmlElementData> childrens;

    /**
     * Constructor.
     *
     * @param name XML element name.
     */
    public XmlElementData(String name) {
        super();
        this.name = name;
        this.attributes = new ArrayList<>();
        this.childrens = new ArrayList<>();
    }

    /**
     * Adds element to the document.
     *
     * @param document XML document.
     * @param parent Parent node for element.
     */
    public void addToDocument(Document document,
            Element parent) {
        Element element = this.toElement(document);
        for (XmlElementData xmlElementData : this.childrens) {
            element.appendChild(xmlElementData.toElement(document));
        }
        for (XmlAttribute attribute : this.attributes) {
            element.setAttribute(attribute.getKey(), attribute.getValue());
        }
        if (parent != null) {
            parent.appendChild(element);
        } else {
            document.appendChild(element);
        }

    }

    /**
     * Creates this element in XML document.
     *
     * @param document XML document
     * @return creates a element in document
     */
    private Element toElement(Document document) {
        return document.createElement(this.name);
    }

}
