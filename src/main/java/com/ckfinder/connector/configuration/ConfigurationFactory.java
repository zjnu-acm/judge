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
package com.ckfinder.connector.configuration;

import com.ckfinder.connector.data.ResourceType;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.utils.FileUtils;
import com.ckfinder.connector.utils.PathUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory returning configuration instance.
 */
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"FinalClass", "FinalMethod", "FinalMethodInFinalClass"})
public final class ConfigurationFactory {

    @Getter
    private final IConfiguration configuration;

    /**
     * Gets and prepares configuration.
     *
     * @param request request
     * @return the configuration
     * @throws Exception when error occurs
     */
    public final IConfiguration getConfiguration(final HttpServletRequest request)
            throws Exception {
        IConfiguration baseConf = getConfiguration();
        return prepareConfiguration(request, baseConf);
    }

    /**
     * Prepares configuration using request.
     *
     * @param request request
     * @param baseConf base configuration initialized in IConfiguration.init()
     * @return prepared configuration
     * @throws Exception when error occurs
     */
    public IConfiguration prepareConfiguration(final HttpServletRequest request,
            final IConfiguration baseConf) throws Exception {
        if (baseConf != null) {
            IConfiguration conf = baseConf.cloneConfiguration();
            conf.prepareConfigurationForRequest(request);
            updateResourceTypesPaths(request, conf);
            return conf;
        }
        return null;
    }

    /**
     * Updates resources types paths by request.
     *
     * @param request request
     * @param conf connector configuration.
     * @throws Exception when error occurs
     */
    private void updateResourceTypesPaths(final HttpServletRequest request,
            final IConfiguration conf) throws Exception {

        String baseFolder = getBaseFolder(conf, request);
        baseFolder = conf.getThumbsDir().replace(Constants.BASE_DIR_PLACEHOLDER, baseFolder);
        baseFolder = PathUtils.escape(baseFolder);
        baseFolder = PathUtils.removeSlashFromEnd(baseFolder);
        Path file = Paths.get(baseFolder);
        if (file == null) {
            throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_FOLDER_NOT_FOUND,
                    "Thumbs directory could not be created using specified path.");
        }

        log.debug("{}", file);
        Files.createDirectories(file);

        conf.setThumbsPath(file.toAbsolutePath().toString());

        String thumbUrl = conf.getThumbsURL();
        thumbUrl = thumbUrl.replaceAll(
                Constants.BASE_URL_PLACEHOLDER,
                conf.getBasePathBuilder().getBaseUrl(request));
        conf.setThumbsURL(PathUtils.escape(thumbUrl));

        for (ResourceType item : conf.getTypes().values()) {
            String url = item.getUrl();
            url = url.replaceAll(Constants.BASE_URL_PLACEHOLDER,
                    conf.getBasePathBuilder().getBaseUrl(request));
            url = PathUtils.escape(url);
            url = PathUtils.removeSlashFromEnd(url);
            item.setUrl(url);

            String s = getBaseFolder(conf, request);
            s = item.getPath().replace(Constants.BASE_DIR_PLACEHOLDER, s);
            s = PathUtils.escape(s);
            s = PathUtils.removeSlashFromEnd(s);

            boolean isFromUrl = false;
            if (s == null || s.isEmpty()) {
                throw new IllegalStateException("baseFolder is empty");
            }
            log.debug("isFromUrl: {}", isFromUrl);
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
     * @param conf connector configuration
     * @param request request
     * @return path to base dir from conf
     * @throws ConnectorException when error during creating folder occurs
     */
    private String getBaseFolder(final IConfiguration conf,
            final HttpServletRequest request)
            throws ConnectorException {
        try {
            String baseFolder = conf.getBasePathBuilder().getBaseDir(request);
            Path baseDir = Paths.get(baseFolder);
            if (!Files.exists(baseDir)) {
                FileUtils.createPath(baseDir, false);
            }
            return PathUtils.addSlashToEnd(baseFolder);
        } catch (IOException e) {
            throw new ConnectorException(e);
        }
    }

}
