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
package com.ckfinder.connector.errors;

import com.ckfinder.connector.ConnectorServlet;
import com.ckfinder.connector.configuration.Constants;
import com.ckfinder.connector.configuration.IConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Error utils.
 */
public final class ErrorUtils {

    private static final ErrorUtils errorUtils = new ErrorUtils();

    private static Map<String, Map<Integer, String>> langMap;

    /**
     * Returns or creates and returns {@code ErrorUtils} instance.
     *
     * @return {@code ErrorUtils} instance.
     */
    public static ErrorUtils getInstance() {
        return errorUtils;
    }

    /**
     * Default constructor which creates {@code ErrorUtils} instance and
     * initializes list of available languages supported by CKFinder.
     */
    private ErrorUtils() {
        langMap = getLangCodeFromJars().stream()
                .collect(Collectors.toMap(Function.identity(),
                        this::getMessagesByLangCode));
    }

    /**
     * Gets error message by locale code.
     *
     * @param errorCode error number
     * @param lang connector language code
     * @param conf connector configuration object
     * @return localized error message.
     */
    public String getErrorMsgByLangAndCode(final String lang,
            final int errorCode,
            final IConfiguration conf) {
        if (lang != null && langMap.get(lang) != null) {
            return langMap.get(lang).get(errorCode);
        } else if (langMap.get(Constants.DEFAULT_LANG_CODE) != null) {
            return langMap.get(Constants.DEFAULT_LANG_CODE).get(errorCode);
        } else if (conf.isDebugMode()) {
            return "Unable to load error message";
        } else {
            return "";
        }
    }

    /**
     * Gets language codes from jar file holding locale files.
     *
     * @return {@code List} with language codes.
     */
    private List<String> getLangCodeFromJars() {
        List<String> langFiles = new ArrayList<>();
        try {
            Path dir = Paths.get(ConnectorServlet.class.getResource("/lang/").toURI());
            if (Files.isDirectory(dir)) {
                for (Iterator<Path> it = Files.newDirectoryStream(dir).iterator(); it.hasNext();) {
                    langFiles.add(it.next().getFileName().toString().replaceAll(".xml", ""));
                }
            }
        } catch (URISyntaxException | IOException ex) {
            return null;
        }
        return langFiles;
    }

    /**
     * Gets localized messages from locale file by language code provided as
     * parameter.
     *
     * @param langCode language code used to get localized messages
     * @return {@code Map} of localized messages.
     */
    private Map<Integer, String> getMessagesByLangCode(final String langCode) {
        Map<Integer, String> langCodeMap = new HashMap<>();
        try {
            InputStream is = ConnectorServlet.class.getResourceAsStream("/lang/" + langCode + ".xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            NodeList unkonwErrornodeList = doc.getElementsByTagName("errorUnknown");
            NodeList errorNodeList = doc.getElementsByTagName("error");
            Element unkonwErrorElem = (Element) unkonwErrornodeList.item(0);
            langCodeMap.put(1, unkonwErrorElem.getTextContent());

            for (int i = 0, j = errorNodeList.getLength(); i < j; i++) {
                Element element = (Element) errorNodeList.item(i);
                langCodeMap.put(Integer.valueOf(element.getAttribute("number")),
                        element.getTextContent());
            }
        } catch (Exception e) {
            return null;
        }
        return langCodeMap;
    }

}
