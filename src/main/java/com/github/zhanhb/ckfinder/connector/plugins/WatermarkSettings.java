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
import javax.servlet.ServletContext;

class WatermarkSettings {

    public static final String WATERMARK = "watermark";
    public static final String SOURCE = "source";
    public static final String TRANSPARENCY = "transparency";
    public static final String QUALITY = "quality";
    public static final String MARGIN_BOTTOM = "marginBottom";
    public static final String MARGIN_RIGHT = "marginRight";
    public static final String DEFULT_WATERMARK = "";

    /**
     * @param configuration
     * @param servletContext
     * @return
     * @throws Exception
     */
    public static WatermarkSettings createFromConfiguration(IConfiguration configuration, ServletContext servletContext) throws Exception {
        WatermarkSettings settings = new WatermarkSettings();

        for (PluginInfo pluginInfo : configuration.getPlugins()) {
            if (WATERMARK.equals(pluginInfo.getName())) {
                for (PluginParam param : pluginInfo.getParams()) {
                    final String name = param.getName();
                    final String value = param.getValue();
                    switch (name) {
                        case SOURCE:
                            settings.setSource(servletContext.getRealPath(value));
                            break;
                        case TRANSPARENCY:
                            settings.setTransprancy(Float.parseFloat(value));
                            break;
                        case QUALITY:
                            final int parseInt = Integer.parseInt(value);
                            final int name1 = parseInt % 101;
                            final float name2 = name1 / 100f;
                            settings.setQuality(name2);
                            break;
                        case MARGIN_BOTTOM:
                            settings.setMarginBottom(Integer.parseInt(value));
                            break;
                        case MARGIN_RIGHT:
                            settings.setMarginRight(Integer.parseInt(value));
                    }
                }
            }
        }
        return settings;
    }
    private String source;
    private float transparency;
    private float quality;
    private int marginBottom;
    private int marginRight;

    public WatermarkSettings() {
        this.source = DEFULT_WATERMARK;
        this.marginRight = 0;
        this.marginBottom = 0;
        this.quality = 90;
        this.transparency = 1.0f;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the transparency
     */
    public float getTransparency() {
        return transparency;
    }

    /**
     * @param transparency the transparency to set
     */
    public void setTransprancy(float transparency) {
        this.transparency = transparency;
    }

    /**
     * @return the quality
     */
    public float getQuality() {
        return quality;
    }

    /**
     * @param quality the quality to set
     */
    public void setQuality(float quality) {
        this.quality = quality;
    }

    /**
     * @return the marginBottom
     */
    public int getMarginBottom() {
        return marginBottom;
    }

    /**
     * @param marginBottom the marginBottom to set
     */
    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    /**
     * @return the marginRight
     */
    public int getMarginRight() {
        return marginRight;
    }

    /**
     * @param marginRight the marginRight to set
     */
    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

}
