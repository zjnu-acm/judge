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

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class PluginInfo {

    private String name;
    private String className;
    private List<PluginParam> params;
    private boolean enabled;
    private boolean internal;

    /**
     * copy constructor.
     *
     * @param info source plugin info
     */
    public PluginInfo(final PluginInfo info) {
        super();
        this.name = info.name;
        this.className = info.className;
        this.params = info.params;
        this.enabled = info.enabled;
        this.internal = info.internal;
    }

}
