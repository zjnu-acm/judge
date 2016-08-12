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
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import com.github.zhanhb.ckfinder.connector.utils.PathUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class loads configuration from XML file.
 */
@Slf4j
@SuppressWarnings({"CollectionWithoutInitialCapacity", "ReturnOfCollectionOrArrayField", "FinalMethod"})
public class Configuration implements IConfiguration {

    private static final int MAX_QUALITY = 100;
    private static final float MAX_QUALITY_FLOAT = 100f;

    @Getter
    private boolean enabled;
    @Getter
    private String baseDir;
    @Getter
    private String baseURL;
    @Getter
    private String licenseName;
    @Getter
    private String licenseKey;
    private Integer imgWidth;
    private Integer imgHeight;
    @Getter
    private float imgQuality;
    @Getter
    private Map<String, ResourceType> types;
    @Getter
    private boolean thumbsEnabled;
    @Getter
    private String thumbsURL;
    @Getter
    private String thumbsDir;
    @Getter
    private String thumbsPath;
    @Getter
    private boolean thumbsDirectAccess;
    private Integer thumbsMaxHeight;
    private Integer thumbsMaxWidth;
    @Getter
    private float thumbsQuality;
    @Getter
    private List<AccessControlLevel> accessControlLevels;
    @Getter
    private List<String> hiddenFolders;
    @Getter
    private List<String> hiddenFiles;
    @Getter
    private boolean checkDoubleFileExtensions;
    @Getter
    private boolean forceAscii;
    @Getter
    private boolean checkSizeAfterScaling;
    @Getter
    private String userRoleName;
    @Getter
    private List<PluginInfo> plugins;
    @Getter
    private boolean secureImageUploads;
    @Getter
    private List<String> htmlExtensions;
    @Getter
    private Set<String> defaultResourceTypes;
    @Getter
    private IBasePathBuilder basePathBuilder;
    @Getter
    private boolean disallowUnsafeCharacters;
    @Getter
    private Events events;
    @Getter
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
        this.plugins = new ArrayList<>();
        this.htmlExtensions = new ArrayList<>();
        this.hiddenFolders = new ArrayList<>();
        this.hiddenFiles = new ArrayList<>();
        this.defaultResourceTypes = new LinkedHashSet<>();
        init(xmlFilePath);
        updateResourceTypesPaths();
    }

    /**
     * Initializes configuration from XML file.
     *
     * @throws Exception when error occurs.
     */
    private void init(String xmlFilePath) throws Exception {
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
        this.checkDoubleFileExtensions = false;
        this.forceAscii = false;
        this.checkSizeAfterScaling = false;
        this.userRoleName = "";
        this.plugins = new ArrayList<>();
        this.secureImageUploads = false;
        this.htmlExtensions = new ArrayList<>();
        this.defaultResourceTypes = new LinkedHashSet<>();
        this.events = new Events();
        this.basePathBuilder = null;
        this.disallowUnsafeCharacters = false;

        Resource resource = getFullConfigPath(xmlFilePath);
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
                        this.enabled = Boolean.parseBoolean(nullNodeToString(childNode));
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
                        this.checkDoubleFileExtensions = Boolean.parseBoolean(nullNodeToString(childNode));
                        break;
                    case "disallowUnsafeCharacters":
                        this.disallowUnsafeCharacters = Boolean.parseBoolean(nullNodeToString(childNode));
                        break;
                    case "forceASCII":
                        this.forceAscii = Boolean.parseBoolean(nullNodeToString(childNode));
                        break;
                    case "checkSizeAfterScaling":
                        this.checkSizeAfterScaling = Boolean.parseBoolean(nullNodeToString(childNode));
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
                        this.secureImageUploads = Boolean.parseBoolean(nullNodeToString(childNode));
                        break;
                    case "uriEncoding":
                        break;
                    case "userRoleSessionVar":
                        this.userRoleName = nullNodeToString(childNode);
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
    }

    /**
     * Returns XML node contents or empty String instead of null if XML node is
     * empty.
     */
    private String nullNodeToString(Node childNode) {
        String textContent = childNode.getTextContent();
        return textContent == null ? "" : textContent.trim();
    }

    /**
     * Gets absolute path to XML configuration file.
     *
     * @return absolute path to XML configuration file
     * @throws ConnectorException when absolute path cannot be obtained.
     */
    private Resource getFullConfigPath(String xmlFilePath) throws ConnectorException {
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
                Plugin plugin = item.getClassName().newInstance();
                plugin.setPluginInfo(item);
                plugin.registerEventHandlers(this.events);
            } catch (InstantiationException | IllegalAccessException ex) {
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
        String role = null;
        String resourceType = null;
        String folder = null;
        int mask = 0;
        for (int i = 0, j = childNode.getChildNodes().getLength(); i < j; i++) {
            Node childChildNode = childNode.getChildNodes().item(i);
            String nodeName = childChildNode.getNodeName();
            int index = 0;
            boolean bool = false;
            switch (nodeName) {
                case "role":
                    role = nullNodeToString(childChildNode);
                    break;
                case "resourceType":
                    resourceType = nullNodeToString(childChildNode);
                    break;
                case "folder":
                    folder = nullNodeToString(childChildNode);
                    break;
                case "folderView":
                    bool = Boolean.parseBoolean(nullNodeToString(childChildNode));
                    index = AccessControl.CKFINDER_CONNECTOR_ACL_FOLDER_VIEW;
                    break;
                case "folderCreate":
                    bool = Boolean.parseBoolean(nullNodeToString(childChildNode));
                    index = AccessControl.CKFINDER_CONNECTOR_ACL_FOLDER_CREATE;
                    break;
                case "folderRename":
                    bool = Boolean.parseBoolean(nullNodeToString(childChildNode));
                    index = AccessControl.CKFINDER_CONNECTOR_ACL_FOLDER_RENAME;
                    break;
                case "folderDelete":
                    bool = Boolean.parseBoolean(nullNodeToString(childChildNode));
                    index = AccessControl.CKFINDER_CONNECTOR_ACL_FOLDER_DELETE;
                    break;
                case "fileView":
                    bool = Boolean.parseBoolean(nullNodeToString(childChildNode));
                    index = AccessControl.CKFINDER_CONNECTOR_ACL_FILE_VIEW;
                    break;
                case "fileUpload":
                    bool = Boolean.parseBoolean(nullNodeToString(childChildNode));
                    index = AccessControl.CKFINDER_CONNECTOR_ACL_FILE_UPLOAD;
                    break;
                case "fileRename":
                    bool = Boolean.parseBoolean(nullNodeToString(childChildNode));
                    index = AccessControl.CKFINDER_CONNECTOR_ACL_FILE_RENAME;
                    break;
                case "fileDelete":
                    bool = Boolean.parseBoolean(nullNodeToString(childChildNode));
                    index = AccessControl.CKFINDER_CONNECTOR_ACL_FILE_DELETE;
                    break;
            }
            if (index != 0) {
                if (bool) {
                    mask |= index;
                } else {
                    mask &= ~index;
                }
            }
        }

        if (resourceType == null || role == null) {
            return null;
        }

        if (folder == null || folder.isEmpty()) {
            folder = "/";
        }
        return AccessControlLevel.builder()
                .folder(folder)
                .resourceType(resourceType)
                .role(role)
                .mask(mask)
                .build();
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
                    this.thumbsEnabled = Boolean.parseBoolean(nullNodeToString(childNode));
                    break;
                case "url":
                    this.thumbsURL = nullNodeToString(childNode);
                    break;
                case "directory":
                    this.thumbsDir = nullNodeToString(childNode);
                    break;
                case "directAccess":
                    this.thumbsDirectAccess = Boolean.parseBoolean(nullNodeToString(childNode));
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
        PluginInfo.Builder info = PluginInfo.builder();
        NodeList list = element.getChildNodes();
        for (int i = 0, l = list.getLength(); i < l; i++) {
            Node childElem = list.item(i);
            String nodeName = childElem.getNodeName();
            String textContent = nullNodeToString(childElem);
            switch (nodeName) {
                case "name":
                    info.name(textContent);
                    break;
                case "class":
                    info.className(ClassUtils.resolveClassName(textContent, null).asSubclass(Plugin.class));
                    break;
                case "internal":
                    info.internal(Boolean.parseBoolean(textContent));
                    break;
                case "params":
                    NodeList paramLlist = childElem.getChildNodes();
                    for (int j = 0, m = paramLlist.getLength(); j < m; j++) {
                        Node node = paramLlist.item(j);
                        if ("param".equals(node.getNodeName())) {
                            NamedNodeMap map = node.getAttributes();
                            String name = null;
                            String value = null;
                            for (int k = 0, o = map.getLength(); k < o; k++) {
                                Node item = map.item(k);
                                String nodeName1 = item.getNodeName();
                                if ("name".equals(nodeName1)) {
                                    name = nullNodeToString(item);
                                } else if ("value".equals(nodeName1)) {
                                    value = nullNodeToString(item);
                                }
                            }
                            info.param(new PluginParam(name, value));
                        }
                    }
            }
        }
        return info.build();
    }

    @Override
    public AccessControl getAccessControl() {
        return applicationContext.getBean(AccessControl.class);
    }

    /**
     * Updates resources types paths by request.
     *
     * @param request request
     * @param conf connector configuration.
     * @throws Exception when error occurs
     */
    private void updateResourceTypesPaths() throws Exception {
        String baseFolder = getBaseFolder();
        baseFolder = this.getThumbsDir().replace(Constants.BASE_DIR_PLACEHOLDER, baseFolder);
        baseFolder = PathUtils.escape(baseFolder);
        baseFolder = PathUtils.removeSlashFromEnd(baseFolder);
        Path file = Paths.get(baseFolder);
        if (file == null) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND,
                    "Thumbs directory could not be created using specified path.");
        }

        log.debug("{}", file);
        Files.createDirectories(file);

        thumbsPath = file.toAbsolutePath().toString();

        String thumbUrl = this.getThumbsURL();
        thumbUrl = thumbUrl.replaceAll(Constants.BASE_URL_PLACEHOLDER,
                this.getBasePathBuilder().getBaseUrl());
        thumbsURL = PathUtils.escape(thumbUrl);

        for (ResourceType item : this.getTypes().values()) {
            String url = item.getUrl();
            url = url.replaceAll(Constants.BASE_URL_PLACEHOLDER,
                    this.getBasePathBuilder().getBaseUrl());
            url = PathUtils.escape(url);
            url = PathUtils.removeSlashFromEnd(url);
            item.setUrl(url);

            String s = getBaseFolder();
            s = item.getPath().replace(Constants.BASE_DIR_PLACEHOLDER, s);
            s = PathUtils.escape(s);
            s = PathUtils.removeSlashFromEnd(s);

            if (s == null || s.isEmpty()) {
                throw new IllegalStateException("baseFolder is empty");
            }
            Path p = Paths.get(s);
            if (p == null) {
                throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND,
                        "Resource directory could not be created using specified path.");
            }

            FileUtils.createPath(p, false);

            item.setPath(p.toAbsolutePath().toString());
        }
    }

    /**
     * Gets the path to base dir from configuration Crates the base dir folder
     * if it doesn't exists.
     *
     * @param this connector configuration
     * @param request request
     * @return path to base dir from conf
     * @throws IOException when error during creating folder occurs
     */
    private String getBaseFolder() throws IOException {
        String baseFolder = this.getBasePathBuilder().getBaseDir();
        Path baseDir = Paths.get(baseFolder);
        if (!Files.exists(baseDir)) {
            FileUtils.createPath(baseDir, false);
        }
        return PathUtils.addSlashToEnd(baseFolder);
    }

}
