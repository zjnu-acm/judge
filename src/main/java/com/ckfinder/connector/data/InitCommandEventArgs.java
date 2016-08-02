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

import com.ckfinder.connector.utils.XMLCreator;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Element;

/**
 * Event data for
 * {@link com.ckfinder.connector.configuration.Events#addInitCommandEventHandler(java.lang.Class) }
 * event.
 */
@Getter
@Setter
public class InitCommandEventArgs extends EventArgs {

    private XMLCreator xml;
    private Element rootElement;

}
