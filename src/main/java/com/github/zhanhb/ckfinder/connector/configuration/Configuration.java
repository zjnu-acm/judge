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
import com.github.zhanhb.ckfinder.connector.data.ResourceType;
import com.github.zhanhb.ckfinder.connector.plugins.WatermarkSettings;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder(builderClassName = "Builder")
@Getter
@SuppressWarnings({"CollectionWithoutInitialCapacity", "ReturnOfCollectionOrArrayField", "FinalMethod"})
public class Configuration implements IConfiguration {

    private final boolean enabled;
    private final String baseDir;
    private final String baseURL;
    private final String licenseName;
    private final String licenseKey;
    private final int imgWidth;
    private final int imgHeight;
    private final float imgQuality;
    @Singular
    private final Map<String, ResourceType> types;
    private final boolean thumbsEnabled;
    private final String thumbsURL;
    private final String thumbsDir;
    private final String thumbsPath;
    private final boolean thumbsDirectAccess;
    private final int maxThumbHeight;
    private final int maxThumbWidth;
    private final float thumbsQuality;
    @Singular
    private final List<String> hiddenFolders;
    @Singular
    private final List<String> hiddenFiles;
    private final boolean checkDoubleFileExtensions;
    private final boolean forceAscii;
    private final boolean checkSizeAfterScaling;
    private final String userRoleName;
    @Singular
    private final List<PluginInfo> plugins;
    private final boolean secureImageUploads;
    @Singular
    private final List<String> htmlExtensions;
    @Singular
    private final Set<String> defaultResourceTypes;
    private final boolean disallowUnsafeCharacters;
    private final Events events;
    private final WatermarkSettings watermarkSettings;
    private final AccessControl accessControl;

    @SuppressWarnings("PublicInnerClass")
    public static class Builder {

        Builder() {
            baseDir = "";
            baseURL = "";
            licenseName = "";
            licenseKey = "";
            imgWidth = DEFAULT_IMG_WIDTH;
            imgHeight = DEFAULT_IMG_HEIGHT;
            imgQuality = DEFAULT_IMG_QUALITY;
            thumbsURL = "";
            thumbsDir = "";
            thumbsPath = "";
            thumbsQuality = DEFAULT_IMG_QUALITY;
            maxThumbHeight = DEFAULT_THUMB_MAX_HEIGHT;
            maxThumbWidth = DEFAULT_THUMB_MAX_WIDTH;
            userRoleName = "";
        }
    }

}
