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

import com.github.zhanhb.ckfinder.connector.configuration.Constants;
import com.github.zhanhb.ckfinder.connector.configuration.IConfiguration;
import com.github.zhanhb.ckfinder.connector.data.BeforeExecuteCommandEventArgs;
import com.github.zhanhb.ckfinder.connector.data.BeforeExecuteCommandEventHandler;
import com.github.zhanhb.ckfinder.connector.data.PluginInfo;
import com.github.zhanhb.ckfinder.connector.data.PluginParam;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import com.github.zhanhb.ckfinder.connector.handlers.command.XMLCommand;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import com.github.zhanhb.ckfinder.connector.utils.ImageUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

@RequiredArgsConstructor
@Slf4j
public class ImageResizeCommad extends XMLCommand implements BeforeExecuteCommandEventHandler {

    private static final String[] SIZES = {"small", "medium", "large"};

    private final PluginInfo pluginInfo;

    /**
     * file name
     */
    private String fileName;
    private String newFileName;
    private String overwrite;
    private Integer width;
    private Integer height;
    private boolean wrongReqSizesParams;
    private Map<String, String> sizesFromReq;

    @Override
    public boolean runEventHandler(BeforeExecuteCommandEventArgs args, IConfiguration configuration)
            throws ConnectorException {
        if ("ImageResize".equals(args.getCommand())) {
            this.runCommand(args.getRequest(), args.getResponse(), configuration);
            return false;
        }
        return true;
    }

    @Override
    protected void createXMLChildNodes(int arg0, Element arg1) {
    }

    @Override
    protected int getDataForXml() {
        if (!checkIfTypeExists(getType())) {
            this.setType(null);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE;
        }

        if (!getConfiguration().getAccessControl().checkFolderACL(getType(), getCurrentFolder(), getUserRole(),
                AccessControl.CKFINDER_CONNECTOR_ACL_FILE_DELETE
                | AccessControl.CKFINDER_CONNECTOR_ACL_FILE_UPLOAD)) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED;
        }

        if (this.fileName == null || this.fileName.isEmpty()) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
        }

        if (!FileUtils.checkFileName(fileName)
                || FileUtils.checkIfFileIsHidden(fileName, getConfiguration())) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        if (FileUtils.checkFileExtension(fileName, getConfiguration().getTypes().get(getType())) == 1) {
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
        }

        Path file = Paths.get(getConfiguration().getTypes().get(getType()).getPath() + this.getCurrentFolder(),
                fileName);
        try {
            if (!(Files.exists(file) && Files.isRegularFile(file))) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND;
            }

            if (this.wrongReqSizesParams) {
                return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
            }

            if (this.width != null && this.height != null) {

                if (!FileUtils.checkFileName(this.newFileName)
                        && FileUtils.checkIfFileIsHidden(this.newFileName, getConfiguration())) {
                    return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_NAME;
                }

                if (FileUtils.checkFileExtension(this.newFileName,
                        getConfiguration().getTypes().get(this.getType())) == 1) {
                    return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_EXTENSION;
                }

                Path thumbFile = Paths.get(getConfiguration().getTypes().get(getType()).getPath() + this.getCurrentFolder(),
                        this.newFileName);

                if (Files.exists(thumbFile) && !Files.isWritable(thumbFile)) {
                    return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
                }
                if (!"1".equals(this.overwrite) && Files.exists(thumbFile)) {
                    return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ALREADY_EXIST;
                }
                int maxImageHeight = getConfiguration().getImgHeight();
                int maxImageWidth = getConfiguration().getImgWidth();
                if ((maxImageWidth > 0 && this.width > maxImageWidth)
                        || (maxImageHeight > 0 && this.height > maxImageHeight)) {
                    return Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST;
                }

                try {
                    ImageUtils.createResizedImage(file, thumbFile,
                            this.width, this.height, getConfiguration().getImgQuality());

                } catch (IOException e) {
                    log.error("", e);
                    return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
                }
            }

            String fileNameWithoutExt = FileUtils.getFileNameWithoutExtension(fileName);
            String fileExt = FileUtils.getFileExtension(fileName);
            for (String size : SIZES) {
                if (sizesFromReq.get(size) != null
                        && sizesFromReq.get(size).equals("1")) {
                    String thumbName = fileNameWithoutExt.concat("_").concat(size).concat(".").concat(fileExt);
                    Path thumbFile = Paths.get(getConfiguration().getTypes().get(this.getType()).getPath().concat(this.getCurrentFolder()).concat(thumbName));
                    for (PluginParam param : pluginInfo.getParams()) {
                        if (size.concat("Thumb").equals(param.getName())) {
                            if (checkParamSize(param.getValue())) {
                                String[] params = parseValue(param.getValue());
                                try {
                                    ImageUtils.createResizedImage(file, thumbFile, Integer.parseInt(params[0]),
                                            Integer.parseInt(params[1]), getConfiguration().getImgQuality());
                                } catch (IOException e) {
                                    log.error("", e);
                                    return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
                                }
                            }
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            log.error("", e);
            return Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED;
        }

        return Constants.Errors.CKFINDER_CONNECTOR_ERROR_NONE;
    }

    private String[] parseValue(String value) {
        StringTokenizer st = new StringTokenizer(value, "x");
        return new String[]{st.nextToken(), st.nextToken()};
    }

    private boolean checkParamSize(String value) {
        return Pattern.matches("(\\d)+x(\\d)+", value);
    }

    @Override
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    protected void initParams(HttpServletRequest request, IConfiguration configuration1)
            throws ConnectorException {
        super.initParams(request, configuration1);

        this.sizesFromReq = new HashMap<>();
        this.fileName = request.getParameter("fileName");
        this.newFileName = request.getParameter("newFileName");
        this.overwrite = request.getParameter("overwrite");
        String reqWidth = request.getParameter("width");
        String reqHeight = request.getParameter("height");
        this.wrongReqSizesParams = false;
        try {
            if (reqWidth != null && !reqWidth.isEmpty()) {
                this.width = Integer.valueOf(reqWidth);
            } else {
                this.width = null;
            }
        } catch (NumberFormatException e) {
            this.width = null;
            this.wrongReqSizesParams = true;
        }
        try {
            if (reqHeight != null && !reqHeight.isEmpty()) {
                this.height = Integer.valueOf(reqHeight);
            } else {
                this.height = null;
            }
        } catch (NumberFormatException e) {
            this.height = null;
            this.wrongReqSizesParams = true;
        }
        for (String size : SIZES) {
            sizesFromReq.put(size, request.getParameter(size));
        }

    }

}
