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

import com.github.zhanhb.ckfinder.connector.data.AccessControlLevel;
import com.github.zhanhb.ckfinder.connector.data.PluginInfo;
import com.github.zhanhb.ckfinder.connector.data.ResourceType;
import com.github.zhanhb.ckfinder.connector.plugins.WatermarkSettings;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.springframework.context.ApplicationContext;

/**
 * Class loads configuration from XML file.
 */
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
    private final List<AccessControlLevel> accessControlLevels;
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
    private final ApplicationContext applicationContext;
    private final WatermarkSettings watermarkSettings;

    /**
     * Checks if user is authenticated.
     *
     * @param request current request
     * @return true if user is authenticated and false otherwise.
     */
    @Override
    public boolean checkAuthentication(HttpServletRequest request) {
        return DEFAULT_CHECKAUTHENTICATION;
    }

    @Override
    public AccessControl getAccessControl() {
        return applicationContext.getBean(AccessControl.class);
    }

}
