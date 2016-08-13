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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Event data for
 * {@link com.github.zhanhb.ckfinder.connector.configuration.Events.Builder#initCommandEventHandler(java.util.function.Supplier) }
 * event.
 */
@Getter
@RequiredArgsConstructor
@ToString
public class InitCommandEventArgs extends EventArgs {

    private final Document document;
    private final Element rootElement;

}
