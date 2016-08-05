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
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Interface for configuration.
 */
public interface IConfiguration {

    int DEFAULT_IMG_WIDTH = 500;
    int DEFAULT_IMG_HEIGHT = 400;
    int DEFAULT_THUMB_MAX_WIDTH = 100;
    int DEFAULT_THUMB_MAX_HEIGHT = 100;
    float DEFAULT_IMG_QUALITY = 0.8f;
    String DEFAULT_THUMBS_URL = "_thumbs/";
    String DEFAULT_THUMBS_DIR = "%BASE_DIR%_thumbs/";
    boolean DEFAULT_CHECKAUTHENTICATION = true;
    String DEFAULT_BASE_URL = "/userfiles";

    /**
     * creates new instance of configuration for request.
     *
     * @return new configuration instance
     * @throws java.lang.Exception
     */
    public IConfiguration cloneConfiguration() throws Exception;

    /**
     * method to check if user is authenticated.
     *
     * @param request current request
     * @return true if is
     */
    public boolean checkAuthentication(HttpServletRequest request);

    /**
     * gets user role name sets in config.
     *
     * @return role name
     */
    public String getUserRoleName();

    /**
     * gets resources map types with names as map keys.
     *
     * @return resources map
     */
    public Map<String, ResourceType> getTypes();

    /**
     * gets location of ckfinder in app. For ex. /ckfinder/.
     *
     * @return base directory
     */
    public String getBaseDir();

    /**
     * returns path to ckfinder with app name for ex. /webapp/ckfinder/.
     *
     * @return base url
     */
    public String getBaseURL();

    /**
     * returns license key.
     *
     * @return license key
     */
    public String getLicenseKey();

    /**
     * returns license name.
     *
     * @return license name.
     */
    public String getLicenseName();

    /**
     * gets image max width.
     *
     * @return max image height
     */
    public Integer getImgWidth();

    /**
     * get image max height.
     *
     * @return max image height
     */
    public Integer getImgHeight();

    /**
     * get image quality.
     *
     * @return image quality
     */
    public float getImgQuality();

    /**
     * check if connector is enabled.
     *
     * @return if connector is enabled
     */
    public boolean enabled();

    /**
     * check if thums are enabled.
     *
     * @return true if thums are enabled
     */
    public boolean getThumbsEnabled();

    /**
     * gets url to thumbs dir(path from baseUrl).
     *
     * @return thumbs url
     */
    public String getThumbsURL();

    /**
     * gets path to thumbs directory.
     *
     * @return thumbs directory
     */
    public String getThumbsDir();

    /**
     * gets path to thumbs directory.
     *
     * @return thumbs directory
     */
    public String getThumbsPath();

    /**
     * gets thumbs quality.
     *
     * @return thumbs quality
     */
    public float getThumbsQuality();

    /**
     * Sets path to thumbs directory.
     *
     * @param directory thumbs directory
     */
    public void setThumbsPath(String directory);

    /**
     * checks if thumbs are accessed direct.
     *
     * @return true if thumbs can be accessed directly
     */
    public boolean getThumbsDirectAccess();

    /**
     * gets max width of thumb.
     *
     * @return max width of thumb
     */
    public int getMaxThumbWidth();

    /**
     * gets max height of thumb.
     *
     * @return max height of thumb
     */
    public int getMaxThumbHeight();

    /**
     * get list of access control levels.
     *
     * @return list of access control levels
     */
    public List<AccessControlLevel> getAccessConrolLevels();

    /**
     * get regex for hidden folders.
     *
     * @return regex for hidden folders
     */
    public List<String> getHiddenFolders();

    /**
     * get regex for hidden files.
     *
     * @return regex for hidden files
     */
    public List<String> getHiddenFiles();

    /**
     * get double extensions configuration.
     *
     * @return configuration value.
     */
    public boolean ckeckDoubleFileExtensions();

    /**
     * flag to check if force ASCII.
     *
     * @return true if force ASCII.
     */
    public boolean forceASCII();

    /**
     * Checks if disallowed characters in file and folder names are turned on.
     *
     * @return disallowUnsafeCharacters
     */
    public boolean isDisallowUnsafeCharacters();

    /**
     * flag if check image size after resizing image.
     *
     * @return true if check.
     */
    public boolean checkSizeAfterScaling();

    /**
     * Prepares configuration for single request. Empty method. It should be
     * overridden if needed.
     *
     * @param request request
     */
    default public void prepareConfigurationForRequest(HttpServletRequest request) {
    }

    /**
     * Checks if CKFinder configuration should be reloaded.
     *
     * @return true if reloading CKFinder configuration is necessary, false
     * otherwise.
     * @throws ConnectorException when error occurs while reloading
     * configuration file.
     */
    public boolean checkIfReloadConfig() throws ConnectorException;

    /**
     * gets a list of plugins.
     *
     * @return list of plugins.
     */
    public List<PluginInfo> getPlugins();

    /**
     * gets events.
     *
     * @return events.
     */
    public Events getEvents();

    /**
     * gets param SecureImageUploads.
     *
     * @return true if is set
     */
    public boolean getSecureImageUploads();

    /**
     * gets html extensions.
     *
     * @return list of html extensions.
     */
    public List<String> getHTMLExtensions();

    /**
     * gets a list of default resource types.
     *
     * @return list of default resource types
     */
    public Set<String> getDefaultResourceTypes();

    /**
     * sets new value of thumbs URL.
     *
     * @param url new url
     */
    public void setThumbsURL(String url);

    /**
     * sets new value of thumbs folder.
     *
     * @param dir new thumbs folder.
     */
    public void setThumbsDir(String dir);

    /**
     * gets UserFilePathBuilder implementation from configuration.
     *
     * @return IUserFilePathBuilder implementation
     */
    public IBasePathBuilder getBasePathBuilder();

    /**
     *
     * @return the servlet context
     */
    public ServletContext getServletContext();

    /**
     *
     * @return the configuration
     */
    public AccessControl getAccessControl();

}
