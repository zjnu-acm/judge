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
package com.github.zhanhb.ckfinder.connector.plugins;

import com.github.zhanhb.ckfinder.connector.configuration.IConfiguration;
import com.github.zhanhb.ckfinder.connector.data.InitCommandEventArgs;
import com.github.zhanhb.ckfinder.connector.data.InitCommandEventHandler;
import com.github.zhanhb.ckfinder.connector.data.PluginInfo;
import com.github.zhanhb.ckfinder.connector.data.PluginParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Slf4j
@RequiredArgsConstructor
public class ImageResizeInitCommandEventHandler implements InitCommandEventHandler {

    private final PluginInfo pluginInfo;

    @Override
    public boolean runEventHandler(InitCommandEventArgs args, IConfiguration arg1) {
        log.debug("runEventHandler: {} {}", args, arg1);
        NodeList list = args.getRootElement().getElementsByTagName("PluginsInfo");
        if (list.getLength() > 0) {
            Node node = list.item(0);
            Element pluginElem = args.getDocument().createElement(pluginInfo.getName());
            for (PluginParam param : pluginInfo.getParams()) {
                pluginElem.setAttribute(param.getName(), param.getValue());
            }
            node.appendChild(pluginElem);
        }
        return false;
    }

}
