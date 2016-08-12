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
package com.github.zhanhb.ckfinder.connector.data;

import com.github.zhanhb.ckfinder.connector.utils.XMLCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.w3c.dom.Element;

/**
 * Event data for
 * {@link com.github.zhanhb.ckfinder.connector.configuration.Events#addInitCommandEventHandler(java.util.function.Supplier) }
 * event.
 */
@Getter
@RequiredArgsConstructor
@ToString
public class InitCommandEventArgs extends EventArgs {

    private final XMLCreator xml;
    private final Element rootElement;

}
