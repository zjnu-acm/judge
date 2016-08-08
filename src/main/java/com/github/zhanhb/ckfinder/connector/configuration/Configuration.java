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
import com.github.zhanhb.ckfinder.connector.data.PluginParam;
import com.github.zhanhb.ckfinder.connector.data.ResourceType;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import com.github.zhanhb.ckfinder.connector.utils.PathUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class loads configuration from XML file.
 */
@SuppressWarnings({"CollectionWithoutInitialCapacity", "ReturnOfCollectionOrArrayField", "FinalMethod"})
public class Configuration implements IConfiguration {

    private static final int MAX_QUALITY = 100;
    private static final float MAX_QUALITY_FLOAT = 100f;
    private long lastCfgModificationDate;
    private boolean enabled;
    private String xmlFilePath;
    private String baseDir;
    private String baseURL;
    private String licenseName;
    private String licenseKey;
    private Integer imgWidth;
    private Integer imgHeight;
    private float imgQuality;
    private Map<String, ResourceType> types;
    private boolean thumbsEnabled;
    private String thumbsURL;
    private String thumbsDir;
    private String thumbsPath;
    private boolean thumbsDirectAccess;
    private Integer thumbsMaxHeight;
    private Integer thumbsMaxWidth;
    private float thumbsQuality;
    private List<AccessControlLevel> accessControlLevels;
    private List<String> hiddenFolders;
    private List<String> hiddenFiles;
    private boolean doubleExtensions;
    private boolean forceASCII;
    private boolean checkSizeAfterScaling;
    private String userRoleSessionVar;
    private List<PluginInfo> plugins;
    private boolean secureImageUploads;
    private List<String> htmlExtensions;
    private Set<String> defaultResourceTypes;
    private IBasePathBuilder basePathBuilder;
    private boolean disallowUnsafeCharacters;
    private boolean loading;
    private Events events;
    private final ApplicationContext applicationContext;

    /**
     * Constructor.
     *
     * @param applicationContext
     * @param xmlFilePath
     * @throws java.lang.Exception
     */
    public Configuration(ApplicationContext applicationContext, String xmlFilePath) throws Exception {
        this.applicationContext = applicationContext;
        this.xmlFilePath = xmlFilePath;
        this.plugins = new ArrayList<>();
        this.htmlExtensions = new ArrayList<>();
        this.hiddenFolders = new ArrayList<>();
        this.hiddenFiles = new ArrayList<>();
        this.defaultResourceTypes = new LinkedHashSet<>();
        init();
    }

    /**
     * Resets all configuration values.
     */
    private void clearConfiguration() {
        this.enabled = false;
        this.baseDir = "";
        this.baseURL = "";
        this.licenseName = "";
        this.licenseKey = "";
        this.imgWidth = DEFAULT_IMG_WIDTH;
        this.imgHeight = DEFAULT_IMG_HEIGHT;
        this.imgQuality = DEFAULT_IMG_QUALITY;
        this.types = new LinkedHashMap<>();
        this.thumbsEnabled = false;
        this.thumbsURL = "";
        this.thumbsDir = "";
        this.thumbsPath = "";
        this.thumbsQuality = DEFAULT_IMG_QUALITY;
        this.thumbsDirectAccess = false;
        this.thumbsMaxHeight = DEFAULT_THUMB_MAX_HEIGHT;
        this.thumbsMaxWidth = DEFAULT_THUMB_MAX_WIDTH;
        this.accessControlLevels = new ArrayList<>();
        this.hiddenFolders = new ArrayList<>();
        this.hiddenFiles = new ArrayList<>();
        this.doubleExtensions = false;
        this.forceASCII = false;
        this.checkSizeAfterScaling = false;
        this.userRoleSessionVar = "";
        this.plugins = new ArrayList<>();
        this.secureImageUploads = false;
        this.htmlExtensions = new ArrayList<>();
        this.defaultResourceTypes = new LinkedHashSet<>();
        this.events = new Events();
        this.basePathBuilder = null;
        this.disallowUnsafeCharacters = false;
    }

    /**
     * Initializes configuration from XML file.
     *
     * @throws Exception when error occurs.
     */
    private void init() throws Exception {
        clearConfiguration();
        this.loading = true;
        Resource resource = getFullConfigPath();
        this.lastCfgModificationDate = resource.lastModified();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc;
        try (InputStream stream = resource.getInputStream()) {
            doc = db.parse(stream);
        }
        doc.normalize();
        Node node = doc.getFirstChild();
        if (node != null) {
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node childNode = nodeList.item(i);
                switch (childNode.getNodeName()) {
                    case "enabled":
                        this.enabled = Boolean.valueOf(nullNodeToString(childNode));
                        break;
                    case "baseDir":
                        this.baseDir = nullNodeToString(childNode);
                        this.baseDir = PathUtils.escape(this.baseDir);
                        this.baseDir = PathUtils.addSlashToEnd(this.baseDir);
                        break;
                    case "baseURL":
                        this.baseURL = nullNodeToString(childNode);
                        this.baseURL = PathUtils.escape(baseURL);
                        this.baseURL = PathUtils.addSlashToEnd(this.baseURL);
                        break;
                    case "licenseName":
                        this.licenseName = nullNodeToString(childNode);
                        break;
                    case "licenseKey":
                        this.licenseKey = nullNodeToString(childNode);
                        break;
                    case "imgWidth":
                        String width = nullNodeToString(childNode);
                        width = width.replaceAll("\\D", "");
                        try {
                            this.imgWidth = Integer.valueOf(width);
                        } catch (NumberFormatException e) {
                            this.imgWidth = null;
                        }
                        break;
                    case "imgQuality":
                        String quality = nullNodeToString(childNode);
                        quality = quality.replaceAll("\\D", "");
                        this.imgQuality = adjustQuality(quality);
                        break;
                    case "imgHeight":
                        String height = nullNodeToString(childNode);
                        height = height.replaceAll("\\D", "");
                        try {
                            this.imgHeight = Integer.valueOf(height);
                        } catch (NumberFormatException e) {
                            this.imgHeight = null;
                        }
                        break;
                    case "thumbs":
                        setThumbs(childNode.getChildNodes());
                        break;
                    case "accessControls":
                        setACLs(childNode.getChildNodes());
                        break;
                    case "hideFolders":
                        setHiddenFolders(childNode.getChildNodes());
                        break;
                    case "hideFiles":
                        setHiddenFiles(childNode.getChildNodes());
                        break;
                    case "checkDoubleExtension":
                        this.doubleExtensions = Boolean.valueOf(nullNodeToString(childNode));
                        break;
                    case "disallowUnsafeCharacters":
                        this.disallowUnsafeCharacters = Boolean.valueOf(nullNodeToString(childNode));
                        break;
                    case "forceASCII":
                        this.forceASCII = Boolean.valueOf(nullNodeToString(childNode));
                        break;
                    case "checkSizeAfterScaling":
                        this.checkSizeAfterScaling = Boolean.valueOf(nullNodeToString(childNode));
                        break;
                    case "htmlExtensions":
                        String htmlExt = nullNodeToString(childNode);
                        StringTokenizer scanner = new StringTokenizer(htmlExt, ",");
                        while (scanner.hasMoreTokens()) {
                            String val = scanner.nextToken();
                            if (val != null && !val.isEmpty()) {
                                htmlExtensions.add(val.trim().toLowerCase());
                            }
                        }
                        break;
                    case "secureImageUploads":
                        this.secureImageUploads = Boolean.valueOf(nullNodeToString(childNode));
                        break;
                    case "uriEncoding":
                        break;
                    case "userRoleSessionVar":
                        this.userRoleSessionVar = nullNodeToString(childNode);
                        break;
                    case "defaultResourceTypes":
                        String value = nullNodeToString(childNode);
                        StringTokenizer sc = new StringTokenizer(value, ",");
                        while (sc.hasMoreTokens()) {
                            this.defaultResourceTypes.add(sc.nextToken());
                        }
                        break;
                    case "plugins":
                        setPlugins(childNode);
                        break;
                    case "basePathBuilderImpl":
                        setBasePathImpl();
                        break;
                }
            }
        }
        setTypes(doc);
        this.events = new Events();
        registerEventHandlers();
        this.loading = false;
    }

    /**
     * Returns XML node contents or empty String instead of null if XML node is
     * empty.
     */
    private String nullNodeToString(Node childNode) {
        return childNode.getTextContent() == null ? "" : childNode.getTextContent().trim();
    }

    /**
     * Gets absolute path to XML configuration file.
     *
     * @return absolute path to XML configuration file
     * @throws ConnectorException when absolute path cannot be obtained.
     */
    private Resource getFullConfigPath() throws ConnectorException {
        Resource resource = applicationContext.getResource(xmlFilePath);
        if (!resource.exists()) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND,
                    "Configuration file could not be found under specified location.");
        }
        return resource;
    }

    /**
     * Sets user defined ConfigurationPathBuilder.
     *
     * @param value userPathBuilderImpl configuration value
     */
    private void setBasePathImpl() {
        basePathBuilder = applicationContext.getBean(IBasePathBuilder.class);
    }

    /**
     * Adjusts image quality.
     *
     * @param imgQuality Image quality
     * @return Adjusted image quality
     */
    private float adjustQuality(String imgQuality) {
        float helper;
        try {
            helper = Math.abs(Float.parseFloat(imgQuality));
        } catch (NumberFormatException e) {
            return DEFAULT_IMG_QUALITY;
        }
        if (helper == 0 || helper == 1) {
            return helper;
        } else if (helper > 0 && helper < 1) {
            helper = (Math.round(helper * MAX_QUALITY_FLOAT) / MAX_QUALITY_FLOAT);
        } else if (helper > 1 && helper <= MAX_QUALITY) {
            helper = (Math.round(helper) / MAX_QUALITY_FLOAT);
        } else {
            helper = DEFAULT_IMG_QUALITY;
        }
        return helper;
    }

    /**
     * Registers event handlers from all plugins.
     */
    private void registerEventHandlers() {
        for (PluginInfo item : this.plugins) {
            try {
                Class<? extends Plugin> clazz = Class.forName(item.getClassName()).asSubclass(Plugin.class);
                Plugin plugin = clazz.newInstance();
                plugin.setPluginInfo(item);
                plugin.registerEventHandlers(this.events);
                item.setEnabled(true);
            } catch (ClassCastException | ClassNotFoundException |
                    IllegalAccessException | InstantiationException e) {
                item.setEnabled(false);
            }
        }
    }

    /**
     * Sets hidden files list defined in XML configuration.
     *
     * @param childNodes list of files nodes.
     */
    private void setHiddenFiles(NodeList childNodes) {
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node node = childNodes.item(i);
            if (node.getNodeName().equals("file")) {
                String val = nullNodeToString(node);
                if (!val.isEmpty()) {
                    this.hiddenFiles.add(val.trim());
                }
            }
        }
    }

    /**
     * Sets hidden folders list defined in XML configuration.
     *
     * @param childNodes list of folder nodes.
     */
    private void setHiddenFolders(NodeList childNodes) {
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node node = childNodes.item(i);
            if (node.getNodeName().equals("folder")) {
                String val = nullNodeToString(node);
                if (!val.isEmpty()) {
                    this.hiddenFolders.add(val.trim());
                }
            }
        }
    }

    /**
     * Sets ACL configuration as a list of access control levels.
     *
     * @param childNodes nodes with ACL configuration.
     */
    private void setACLs(NodeList childNodes) {
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeName().equals("accessControl")) {
                AccessControlLevel acl = getACLFromNode(childNode);
                if (acl != null) {
                    this.accessControlLevels.add(acl);
                }
            }
        }
    }

    /**
     * Gets single ACL configuration from XML node.
     *
     * @param childNode XML accessControl node.
     * @return access control level object.
     */
    private AccessControlLevel getACLFromNode(Node childNode) {
        AccessControlLevel acl = new AccessControlLevel();
        for (int i = 0, j = childNode.getChildNodes().getLength(); i < j; i++) {
            Node childChildNode = childNode.getChildNodes().item(i);
            String nodeName = childChildNode.getNodeName();
            switch (nodeName) {
                case "role":
                    acl.setRole(nullNodeToString(childChildNode));
                    break;
                case "resourceType":
                    acl.setResourceType(nullNodeToString(childChildNode));
                    break;
                case "folder":
                    acl.setFolder(nullNodeToString(childChildNode));
                    break;
                case "folderView":
                    acl.setFolderView(Boolean.valueOf(nullNodeToString(childChildNode)));
                    break;
                case "folderCreate":
                    acl.setFolderCreate(Boolean.valueOf(nullNodeToString(childChildNode)));
                    break;
                case "folderRename":
                    acl.setFolderRename(Boolean.valueOf(nullNodeToString(childChildNode)));
                    break;
                case "folderDelete":
                    acl.setFolderDelete(Boolean.valueOf(nullNodeToString(childChildNode)));
                    break;
                case "fileView":
                    acl.setFileView(Boolean.valueOf(nullNodeToString(childChildNode)));
                    break;
                case "fileUpload":
                    acl.setFileUpload(Boolean.valueOf(nullNodeToString(childChildNode)));
                    break;
                case "fileRename":
                    acl.setFileRename(Boolean.valueOf(nullNodeToString(childChildNode)));
                    break;
                case "fileDelete":
                    acl.setFileDelete(Boolean.valueOf(nullNodeToString(childChildNode)));
                    break;
            }
        }

        if (acl.getResourceType() == null || acl.getRole() == null) {
            return null;
        }

        if (acl.getFolder() == null || acl.getFolder().isEmpty()) {
            acl.setFolder("/");
        }

        return acl;
    }

    /**
     * creates thumb configuration from XML.
     *
     * @param childNodes list of thumb XML nodes
     */
    private void setThumbs(NodeList childNodes) {
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node childNode = childNodes.item(i);
            switch (childNode.getNodeName()) {
                case "enabled":
                    this.thumbsEnabled = Boolean.valueOf(nullNodeToString(childNode));
                    break;
                case "url":
                    this.thumbsURL = nullNodeToString(childNode);
                    break;
                case "directory":
                    this.thumbsDir = nullNodeToString(childNode);
                    break;
                case "directAccess":
                    this.thumbsDirectAccess = Boolean.valueOf(nullNodeToString(childNode));
                    break;
                case "maxHeight":
                    String width = nullNodeToString(childNode);
                    width = width.replaceAll("\\D", "");
                    try {
                        this.thumbsMaxHeight = Integer.valueOf(width);
                    } catch (NumberFormatException e) {
                        this.thumbsMaxHeight = null;
                    }
                    break;
                case "maxWidth":
                    width = nullNodeToString(childNode);
                    width = width.replaceAll("\\D", "");
                    try {
                        this.thumbsMaxWidth = Integer.valueOf(width);
                    } catch (NumberFormatException e) {
                        this.thumbsMaxWidth = null;
                    }
                    break;
                case "quality":
                    String quality = nullNodeToString(childNode);
                    quality = quality.replaceAll("\\D", "");
                    this.thumbsQuality = adjustQuality(quality);
            }
        }

    }

    /**
     * Creates resource types configuration from XML configuration file (from
     * XML element 'types').
     *
     * @param doc XML document.
     */
    private void setTypes(Document doc) {
        types = new LinkedHashMap<>();
        NodeList list = doc.getElementsByTagName("type");

        for (int i = 0, j = list.getLength(); i < j; i++) {
            Element element = (Element) list.item(i);
            String name = element.getAttribute("name");
            if (name != null && !name.isEmpty()) {
                ResourceType resourceType = createTypeFromXml(name, element.getChildNodes());
                types.put(name, resourceType);
            }
        }
    }

    /**
     * Creates single resource type configuration from XML configuration file
     * (from XML element 'type').
     *
     * @param typeName name of type.
     * @param childNodes type XML child nodes.
     * @return resource type
     */
    private ResourceType createTypeFromXml(String typeName,
            NodeList childNodes) {
        ResourceType resourceType = new ResourceType(typeName);
        for (int i = 0, j = childNodes.getLength(); i < j; i++) {
            Node childNode = childNodes.item(i);
            switch (childNode.getNodeName()) {
                case "url":
                    String url = nullNodeToString(childNode);
                    resourceType.setUrl(url);
                    break;
                case "directory":
                    url = nullNodeToString(childNode);
                    resourceType.setPath(url);
                    break;
                case "maxSize":
                    resourceType.setMaxSize(nullNodeToString(childNode));
                    break;
                case "allowedExtensions":
                    resourceType.setAllowedExtensions(nullNodeToString(childNode));
                    break;
                case "deniedExtensions":
                    resourceType.setDeniedExtensions(nullNodeToString(childNode));
            }
        }
        return resourceType;
    }

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

    /**
     * Checks if connector is enabled.
     *
     * @return if connector is enabled.
     */
    @Override
    public boolean enabled() {
        return this.enabled && !this.loading;
    }

    /**
     * Checks if disallowed characters in file and folder names are turned on.
     *
     * @return disallowUnsafeCharacters
     */
    @Override
    public boolean isDisallowUnsafeCharacters() {
        return this.disallowUnsafeCharacters;
    }

    /**
     * Gets location of ckfinder in application e.g. /ckfinder/.
     *
     * @return base directory.
     */
    @Override
    public String getBaseDir() {
        return this.baseDir;
    }

    /**
     * Returns path to ckfinder with application name e.g. /webapp/ckfinder/.
     *
     * @return base url.
     */
    @Override
    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * Gets image max height.
     *
     * @return max image height.
     */
    @Override
    public Integer getImgHeight() {
        if (this.imgHeight != null) {
            return this.imgHeight;
        } else {
            return DEFAULT_IMG_HEIGHT;
        }
    }

    /**
     * Gets image max width.
     *
     * @return max image width.
     */
    @Override
    public Integer getImgWidth() {
        if (this.imgWidth != null) {
            return this.imgWidth;
        } else {
            return DEFAULT_IMG_WIDTH;
        }
    }

    /**
     * Gets image quality.
     *
     * @return image quality.
     */
    @Override
    public float getImgQuality() {
        return this.imgQuality;
    }

    /**
     * Returns license key.
     *
     * @return license key.
     */
    @Override
    public String getLicenseKey() {
        return this.licenseKey;
    }

    /**
     * Returns license name.
     *
     * @return license name.
     */
    @Override
    public String getLicenseName() {
        return this.licenseName;
    }

    /**
     * Gets resource types map with resources names as map keys.
     *
     * @return resources map
     */
    @Override
    public Map<String, ResourceType> getTypes() {
        return this.types;
    }

    /**
     * Checks if thumbs are accessed directly.
     *
     * @return true if thumbs can be accessed directly.
     */
    @Override
    public boolean getThumbsDirectAccess() {
        return this.thumbsDirectAccess;
    }

    /**
     * Gets maximum height of thumb.
     *
     * @return maximum height of thumb.
     */
    @Override
    public int getMaxThumbHeight() {
        if (this.thumbsMaxHeight != null) {
            return this.thumbsMaxHeight;
        } else {
            return DEFAULT_THUMB_MAX_HEIGHT;
        }
    }

    /**
     * Gets maximum width of thumb.
     *
     * @return maximum width of thumb.
     */
    @Override
    public int getMaxThumbWidth() {
        if (this.thumbsMaxWidth != null) {
            return this.thumbsMaxWidth;
        } else {
            return DEFAULT_THUMB_MAX_WIDTH;
        }
    }

    /**
     * Check if thumbs are enabled.
     *
     * @return true if thumbs are enabled.
     */
    @Override
    public boolean getThumbsEnabled() {
        return this.thumbsEnabled;
    }

    /**
     * Gets url to thumbs directory (path from baseUrl).
     *
     * @return thumbs url.
     */
    @Override
    public String getThumbsURL() {
        return this.thumbsURL;
    }

    /**
     * Gets path to thumbs directory.
     *
     * @return thumbs directory.
     */
    @Override
    public String getThumbsDir() {
        return this.thumbsDir;
    }

    /**
     * Gets path to thumbs directory.
     *
     * @return thumbs directory.
     */
    @Override
    public String getThumbsPath() {
        return this.thumbsPath;
    }

    /**
     * gets thumbs quality.
     *
     * @return thumbs quality
     */
    @Override
    public float getThumbsQuality() {
        return this.thumbsQuality;
    }

    /**
     * Sets directory name for thumbnails.
     *
     * @param directory directory name for thumbnails.
     */
    @Override
    public void setThumbsPath(String directory) {
        this.thumbsPath = directory;
    }

    /**
     * Returns list of access control levels.
     *
     * @return list of access control levels.
     */
    @Override
    public List<AccessControlLevel> getAccessConrolLevels() {
        return this.accessControlLevels;
    }

    /**
     * Returns regex for hidden folders.
     *
     * @return regex for hidden folders
     */
    @Override
    public List<String> getHiddenFolders() {
        return this.hiddenFolders;
    }

    /**
     * Gets regex for hidden files.
     *
     * @return regex for hidden files
     */
    @Override
    public List<String> getHiddenFiles() {
        return this.hiddenFiles;
    }

    /**
     * Returns flag that determines whether double extensions should be checked.
     *
     * @return flag that determines whether double extensions should be checked.
     */
    @Override
    public boolean ckeckDoubleFileExtensions() {
        return this.doubleExtensions;
    }

    /**
     * Returns flag that determines whether ASCII should be forced.
     *
     * @return true ASCII should be forced.
     */
    @Override
    public boolean forceASCII() {
        return this.forceASCII;
    }

    /**
     * Returns flag that determines whether image size after resizing image
     * should be checked.
     *
     * @return true if image size after resizing image should be checked.
     */
    @Override
    public boolean checkSizeAfterScaling() {
        return this.checkSizeAfterScaling;
    }

    /**
     * Gets user role name set in configuration.
     *
     * @return role name
     */
    @Override
    public String getUserRoleName() {
        return this.userRoleSessionVar;
    }

    /**
     * Gets list of available plugins.
     *
     * @return list of plugins.
     */
    @Override
    public List<PluginInfo> getPlugins() {
        return this.plugins;
    }

    /**
     * Returns flag that determines whether secure image uploads should be
     * performed.
     *
     * @return true if secure image uploads should be performed.
     */
    @Override
    public boolean getSecureImageUploads() {
        return this.secureImageUploads;
    }

    /**
     * Returns HTML extensions list.
     *
     * @return HTML extensions list.
     */
    @Override
    public List<String> getHTMLExtensions() {
        return this.htmlExtensions;
    }

    /**
     * Returns events.
     *
     * @return Events object
     */
    @Override
    public Events getEvents() {
        return this.events;
    }

    /**
     * Gets default resource types list from configuration.
     *
     * @return default resource types list from configuration.
     */
    @Override
    public Set<String> getDefaultResourceTypes() {
        return this.defaultResourceTypes;
    }

    /**
     * Gets path builder for baseDir and baseURL.
     *
     * @return path builder.
     */
    @Override
    public IBasePathBuilder getBasePathBuilder() {
        return this.basePathBuilder;
    }

    /**
     * Checks if CKFinder configuration should be reloaded. It is reloaded when
     * modification date is greater than the date of last configuration
     * initialization.
     *
     * @return true if reloading configuration is necessary.
     * @throws ConnectorException when configuration file cannot be reloaded.
     */
    @Override
    public boolean checkIfReloadConfig() throws ConnectorException {
        Resource resource = applicationContext.getResource(xmlFilePath);

        try {
            return resource.lastModified() > lastCfgModificationDate;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Sets plugins list from XML configuration file.
     *
     * @param childNode child of XML node 'plugins'.
     */
    private void setPlugins(Node childNode) {
        NodeList nodeList = childNode.getChildNodes();
        for (int i = 0, j = nodeList.getLength(); i < j; i++) {
            Node childChildNode = nodeList.item(i);
            if (childChildNode.getNodeName().equals("plugin")) {
                this.plugins.add(createPluginFromNode(childChildNode));
            }
        }
    }

    /**
     * Creates plugin data from configuration file.
     *
     * @param element XML plugin node.
     * @return PluginInfo data
     */
    private PluginInfo createPluginFromNode(Node element) {
        PluginInfo info = new PluginInfo();
        NodeList list = element.getChildNodes();
        for (int i = 0, l = list.getLength(); i < l; i++) {
            Node childElem = list.item(i);
            String nodeName = childElem.getNodeName();
            String textContent = nullNodeToString(childElem);
            switch (nodeName) {
                case "name":
                    info.setName(textContent);
                    break;
                case "class":
                    info.setClassName(textContent);
                    break;
                case "internal":
                    info.setInternal(Boolean.parseBoolean(textContent));
                    break;
                case "params":
                    NodeList paramLlist = childElem.getChildNodes();
                    if (list.getLength() > 0) {
                        info.setParams(new ArrayList<>());
                    }
                    for (int j = 0, m = paramLlist.getLength(); j < m; j++) {
                        Node node = paramLlist.item(j);
                        if ("param".equals(node.getNodeName())) {
                            NamedNodeMap map = node.getAttributes();
                            PluginParam pp = new PluginParam();
                            for (int k = 0, o = map.getLength(); k < o; k++) {
                                if ("name".equals(map.item(k).getNodeName())) {
                                    pp.setName(nullNodeToString(map.item(k)));
                                } else if ("value".equals(map.item(k).getNodeName())) {
                                    pp.setValue(nullNodeToString(map.item(k)));
                                }
                            }
                            info.getParams().add(pp);
                        }
                    }
            }
        }
        return info;
    }

    /**
     * Sets thumbs URL used by ConfigurationFacotry.
     *
     * @param url current thumbs url
     */
    @Override
    public void setThumbsURL(String url) {
        this.thumbsURL = url;
    }

    /**
     * Sets thumbnails directory used by ConfigurationFacotry.
     *
     * @param dir current thumbs directory.
     */
    @Override
    public void setThumbsDir(String dir) {
        this.thumbsDir = dir;
    }

    /**
     * Clones current configuration instance and copies it's all fields.
     *
     * @return cloned configuration
     * @throws java.lang.Exception
     */
    @Override
    public final IConfiguration cloneConfiguration() throws Exception {
        Configuration configuration = createConfigurationInstance();
        copyConfFields(configuration);
        return configuration;
    }

    /**
     * Creates current configuration class instance. In every subclass this
     * method should be overridden and return new configuration instance.
     *
     * @return new configuration instance
     * @throws java.lang.Exception
     */
    private Configuration createConfigurationInstance() throws Exception {
        return new Configuration(applicationContext, xmlFilePath);
    }

    /**
     * Copies configuration fields.
     *
     * @param configuration destination configuration
     */
    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    private void copyConfFields(Configuration configuration) {
        configuration.loading = this.loading;
        configuration.xmlFilePath = this.xmlFilePath;
        configuration.lastCfgModificationDate = this.lastCfgModificationDate;
        configuration.enabled = this.enabled;
        configuration.baseDir = this.baseDir;
        configuration.baseURL = this.baseURL;
        configuration.licenseName = this.licenseName;
        configuration.licenseKey = this.licenseKey;
        configuration.imgWidth = this.imgWidth;
        configuration.imgHeight = this.imgHeight;
        configuration.imgQuality = this.imgQuality;
        configuration.thumbsEnabled = this.thumbsEnabled;
        configuration.thumbsURL = this.thumbsURL;
        configuration.thumbsDir = this.thumbsDir;
        configuration.thumbsDirectAccess = this.thumbsDirectAccess;
        configuration.thumbsMaxHeight = this.thumbsMaxHeight;
        configuration.thumbsMaxWidth = this.thumbsMaxWidth;
        configuration.thumbsQuality = this.thumbsQuality;
        configuration.doubleExtensions = this.doubleExtensions;
        configuration.forceASCII = this.forceASCII;
        configuration.disallowUnsafeCharacters = this.disallowUnsafeCharacters;
        configuration.checkSizeAfterScaling = this.checkSizeAfterScaling;
        configuration.secureImageUploads = this.secureImageUploads;
        configuration.userRoleSessionVar = this.userRoleSessionVar;
        configuration.events = this.events;
        configuration.basePathBuilder = this.basePathBuilder;

        configuration.htmlExtensions = new ArrayList<>();
        configuration.htmlExtensions.addAll(this.htmlExtensions);
        configuration.hiddenFolders = new ArrayList<>();
        configuration.hiddenFiles = new ArrayList<>();
        configuration.hiddenFiles.addAll(this.hiddenFiles);
        configuration.hiddenFolders.addAll(this.hiddenFolders);
        configuration.defaultResourceTypes = new LinkedHashSet<>();
        configuration.defaultResourceTypes.addAll(this.defaultResourceTypes);
        configuration.types = new LinkedHashMap<>();
        configuration.accessControlLevels = new ArrayList<>();
        configuration.plugins = new ArrayList<>();
        copyTypes(configuration.types);
        copyACls(configuration.accessControlLevels);
        copyPlugins(configuration.plugins);
    }

    /**
     * Copies plugins for new configuration.
     *
     * @param newPlugins new configuration plugins list.
     */
    private void copyPlugins(List<PluginInfo> newPlugins) {
        for (PluginInfo pluginInfo : this.plugins) {
            newPlugins.add(new PluginInfo(pluginInfo));
        }
    }

    /**
     * Copies ACL for new configuration.
     *
     * @param newAccessControlLevels new configuration ACL list.
     */
    private void copyACls(List<AccessControlLevel> newAccessControlLevels) {
        for (AccessControlLevel acl : this.accessControlLevels) {
            newAccessControlLevels.add(new AccessControlLevel(acl));
        }
    }

    /**
     * Copies resource types for new configuration.
     *
     * @param newTypes new configuration resource types list.
     */
    private void copyTypes(Map<String, ResourceType> newTypes) {
        for (String name : this.types.keySet()) {
            newTypes.put(name, new ResourceType(this.types.get(name)));
        }
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public AccessControl getAccessControl() {
        return applicationContext.getBean(AccessControl.class);
    }

}
