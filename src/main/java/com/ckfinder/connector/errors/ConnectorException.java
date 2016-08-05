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
package com.ckfinder.connector.errors;

import com.ckfinder.connector.configuration.Constants;
import lombok.Getter;

/**
 * Connector Exception.
 */
@Getter
public class ConnectorException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -8643752550259111562L;
    private int errorCode;
    private boolean addCurrentFolder;

    /**
     * standard constructor.
     *
     * @param errorCode error code number
     */
    public ConnectorException(int errorCode) {
        super(null, null);
        addCurrentFolder = true;
        this.errorCode = errorCode;
    }

    /**
     * standard constructor.
     *
     * @param addCurrentFolder add current node flag
     * @param errorCode error code number
     */
    public ConnectorException(int errorCode, boolean addCurrentFolder) {
        this(errorCode);
        this.addCurrentFolder = addCurrentFolder;
    }

    /**
     * constructor with error code and error message parameters.
     *
     * @param errorCode error code number
     * @param errorMsg error text message
     */
    public ConnectorException(int errorCode, String errorMsg) {
        super(errorMsg, null);
    }

    /**
     * constructor with error code and error message parameters.
     *
     * @param errorCode error code number
     * @param e exception
     */
    public ConnectorException(int errorCode, Exception e) {
        super(e.getMessage(), e);
        addCurrentFolder = false;
    }

    /**
     * constructor with exception param.
     *
     * @param e Exception
     */
    public ConnectorException(Exception e) {
        super(e.getMessage(), e);
        if (e instanceof ConnectorException) {
            errorCode = ((ConnectorException) e).getErrorCode();
            addCurrentFolder = ((ConnectorException) e).isAddCurrentFolder();
        } else {
            addCurrentFolder = false;
            errorCode = Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNKNOWN;
        }
    }

}
