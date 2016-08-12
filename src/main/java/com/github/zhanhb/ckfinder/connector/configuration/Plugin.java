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
package com.github.zhanhb.ckfinder.connector.configuration;

import com.github.zhanhb.ckfinder.connector.data.PluginInfo;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base class for plugins.
 */
public abstract class Plugin {

    @Getter(AccessLevel.PROTECTED)
    private PluginInfo pluginInfo;

    /**
     * register event handlers for plugin.
     *
     * @param eventHandler available event handlers.
     */
    protected abstract void registerEventHandlers(Events.Builder eventHandler);

    /**
     * @param pluginInfo the pluginInfo to set
     */
    @SuppressWarnings("FinalMethod")
    public final void setPluginInfo(PluginInfo pluginInfo) {
        this.pluginInfo = pluginInfo;
    }

}
