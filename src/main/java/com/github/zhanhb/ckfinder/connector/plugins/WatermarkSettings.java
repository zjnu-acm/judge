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
import com.github.zhanhb.ckfinder.connector.data.PluginInfo;
import com.github.zhanhb.ckfinder.connector.data.PluginParam;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

@Builder(builderClassName = "Builder")
@Getter
public class WatermarkSettings {

    public static final String WATERMARK = "watermark";
    public static final String SOURCE = "source";
    public static final String TRANSPARENCY = "transparency";
    public static final String QUALITY = "quality";
    public static final String MARGIN_BOTTOM = "marginBottom";
    public static final String MARGIN_RIGHT = "marginRight";

    /**
     * @param configuration
     * @param applicationContext
     * @return
     * @throws Exception
     */
    public static WatermarkSettings createFromConfiguration(IConfiguration configuration, ApplicationContext applicationContext) throws Exception {
        Builder settings = WatermarkSettings.builder();

        for (PluginInfo pluginInfo : configuration.getPlugins()) {
            if (WATERMARK.equals(pluginInfo.getName())) {
                for (PluginParam param : pluginInfo.getParams()) {
                    final String name = param.getName();
                    final String value = param.getValue();
                    switch (name) {
                        case SOURCE:
                            settings.source(applicationContext.getResource(value));
                            break;
                        case TRANSPARENCY:
                            settings.transparency(Float.parseFloat(value));
                            break;
                        case QUALITY:
                            final int parseInt = Integer.parseInt(value);
                            final int name1 = parseInt % 101;
                            final float name2 = name1 / 100f;
                            settings.quality(name2);
                            break;
                        case MARGIN_BOTTOM:
                            settings.marginBottom(Integer.parseInt(value));
                            break;
                        case MARGIN_RIGHT:
                            settings.marginRight(Integer.parseInt(value));
                    }
                }
            }
        }
        return settings.build();
    }

    private final Resource source;
    private final float transparency;
    private final float quality;
    private final int marginBottom;
    private final int marginRight;

    @SuppressWarnings("PackageVisibleInnerClass")
    public static class Builder {

        Builder() {
            this.source = null;
            this.marginRight = 0;
            this.marginBottom = 0;
            this.quality = 90;
            this.transparency = 1.0f;
        }

    }

}
