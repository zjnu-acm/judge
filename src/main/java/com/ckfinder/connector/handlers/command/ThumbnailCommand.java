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
package com.ckfinder.connector.handlers.command;

import com.ckfinder.connector.configuration.Constants;
import com.ckfinder.connector.configuration.IConfiguration;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.utils.AccessControlUtil;
import com.ckfinder.connector.utils.FileUtils;
import com.ckfinder.connector.utils.ImageUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class to handle <code>Thumbnail</code> command.
 */
public class ThumbnailCommand extends Command {

    /**
     * Backup map holding mime types for images just in case if they aren't set
     * in a request
     */
    private static final HashMap<String, String> imgMimeTypeMap = new HashMap<>(57);

    static {
        imgMimeTypeMap.put(".art", "image/x-jg");
        imgMimeTypeMap.put(".bm", "image/bmp");
        imgMimeTypeMap.put(".bmp", "image/bmp");
        imgMimeTypeMap.put(".dwg", "image/vnd.dwg");
        imgMimeTypeMap.put(".dxf", "image/vnd.dwg");
        imgMimeTypeMap.put(".fif", "image/fif");
        imgMimeTypeMap.put(".flo", "image/florian");
        imgMimeTypeMap.put(".fpx", "image/vnd.fpx");
        imgMimeTypeMap.put(".g3", "image/g3fax");
        imgMimeTypeMap.put(".gif", "image/gif");
        imgMimeTypeMap.put(".ico", "image/x-icon");
        imgMimeTypeMap.put(".ief", "image/ief");
        imgMimeTypeMap.put(".iefs", "image/ief");
        imgMimeTypeMap.put(".jut", "image/jutvision");
        imgMimeTypeMap.put(".mcf", "image/vasa");
        imgMimeTypeMap.put(".nap", "image/naplps");
        imgMimeTypeMap.put(".naplps", "image/naplps");
        imgMimeTypeMap.put(".nif", "image/x-niff");
        imgMimeTypeMap.put(".niff", "image/x-niff");
        imgMimeTypeMap.put(".pct", "image/x-pict");
        imgMimeTypeMap.put(".pcx", "image/x-pcx");
        imgMimeTypeMap.put(".pgm", "image/x-portable-graymap");
        imgMimeTypeMap.put(".pic", "image/pict");
        imgMimeTypeMap.put(".pict", "image/pict");
        imgMimeTypeMap.put(".pm", "image/x-xpixmap");
        imgMimeTypeMap.put(".png", "image/png");
        imgMimeTypeMap.put(".pnm", "image/x-portable-anymap");
        imgMimeTypeMap.put(".ppm", "image/x-portable-pixmap");
        imgMimeTypeMap.put(".ras", "image/x-cmu-raster");
        imgMimeTypeMap.put(".rast", "image/cmu-raster");
        imgMimeTypeMap.put(".rf", "image/vnd.rn-realflash");
        imgMimeTypeMap.put(".rgb", "image/x-rgb");
        imgMimeTypeMap.put(".rp", "  image/vnd.rn-realpix");
        imgMimeTypeMap.put(".svf", "image/vnd.dwg");
        imgMimeTypeMap.put(".svf", "image/x-dwg");
        imgMimeTypeMap.put(".tiff", "image/tiff");
        imgMimeTypeMap.put(".turbot", "image/florian");
        imgMimeTypeMap.put(".wbmp", "image/vnd.wap.wbmp");
        imgMimeTypeMap.put(".xif", "image/vnd.xiff");
        imgMimeTypeMap.put(".xpm", "image/x-xpixmap");
        imgMimeTypeMap.put(".x-png", "image/png");
        imgMimeTypeMap.put(".xwd", "image/x-xwindowdump");
    }

    /**
     * File name.
     */
    private String fileName;
    /**
     * Thumbnail file.
     */
    private Path thumbFile;
    /**
     * Field holding If-None-Match header value.
     */
    private String ifNoneMatch;
    /**
     * Field holding If-Modified-Since header value.
     */
    private long ifModifiedSince;
    /**
     * current {code HttpServletResponse} object.
     */
    private HttpServletResponse response;
    /**
     * Full path to the thumbnail.
     */
    private String fullCurrentPath;

    @Override
    public void setResponseHeader(final HttpServletResponse response, final ServletContext sc) {
        response.setHeader("Cache-Control", "public");
        String mimetype = getMimeTypeOfImage(sc, response);
        if (mimetype != null) {
            response.setContentType(mimetype);
        }
        response.addHeader("Content-Disposition", "attachment; filename=\"" + this.fileName + "\"");
        // set to fill some params later.
        this.response = response;

    }

    /**
     * Gets mime type of image.
     *
     * @param sc the {@code ServletContext} object.
     * @param response currect response object
     * @return mime type of the image.
     */
    private String getMimeTypeOfImage(final ServletContext sc, final HttpServletResponse response) {
        if (this.fileName == null || this.fileName.length() == 0) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
        String tempFileName = this.fileName.substring(0,
                this.fileName.lastIndexOf('.') + 1).concat(
                FileUtils.getFileExtension(this.fileName).toLowerCase());
        String mimeType = sc.getMimeType(tempFileName);
        if (mimeType == null || mimeType.length() == 0) {
            mimeType = ThumbnailCommand.imgMimeTypeMap.get(fileName.toLowerCase().substring(fileName.lastIndexOf('.')));
        }

        if (mimeType == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
        return mimeType;
    }

    @Override
    public void execute(final OutputStream out) throws ConnectorException, IOException {
        validate();
        createThumb();
        if (setResponseHeadersAfterCreatingFile()) {
            try {
                FileUtils.printFileContentToResponse(thumbFile, out);
            } catch (IOException e) {
                if (configuration.isDebugMode()) {
                    throw new ConnectorException(e);
                }
                try {
                    this.response.sendError(HttpServletResponse.SC_FORBIDDEN);
                } catch (IOException e1) {
                    throw new ConnectorException(e1);
                }
            }
        } else {
            try {
                this.response.reset();
                this.response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            } catch (IOException e1) {
                throw new ConnectorException(e1);
            }
        }
    }

    @Override
    public void initParams(final HttpServletRequest request,
            final IConfiguration configuration, final Object... params)
            throws ConnectorException {
        super.initParams(request, configuration, params);
        this.fileName = getParameter(request, "FileName");
        try {
            this.ifModifiedSince = request.getDateHeader("If-Modified-Since");
        } catch (IllegalArgumentException e) {
            this.ifModifiedSince = 0;
        }
        this.ifNoneMatch = request.getHeader("If-None-Match");
    }

    /**
     * Validates thumbnail file properties and current user access rights.
     *
     * @throws ConnectorException when validation fails.
     */
    private void validate() throws ConnectorException, IOException {
        if (!this.configuration.getThumbsEnabled()) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_THUMBNAILS_DISABLED);
        }
        if (!checkIfTypeExists(this.type)) {
            this.type = null;
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_TYPE, false);
        }

        if (!AccessControlUtil.getInstance().checkFolderACL(
                this.type, this.currentFolder, this.userRole,
                AccessControlUtil.CKFINDER_CONNECTOR_ACL_FILE_VIEW)) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_UNAUTHORIZED);
        }

        if (!FileUtils.checkFileName(this.fileName)) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_REQUEST);
        }

        if (FileUtils.checkIfFileIsHidden(this.fileName, this.configuration)) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND);
        }

        Path typeThumbDir = Paths.get(configuration.getThumbsPath(), this.type);

        try {
            this.fullCurrentPath = typeThumbDir.toAbsolutePath().toString()
                    + currentFolder;
            if (!Files.exists(typeThumbDir)) {
                Files.createDirectories(typeThumbDir);
            }
        } catch (SecurityException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    /**
     * Creates thumbnail file if thumbnails are enabled and thumb file doesn't
     * exists.
     *
     * @throws ConnectorException when thumbnail creation fails.
     */
    private void createThumb() throws ConnectorException {
        this.thumbFile = Paths.get(fullCurrentPath, this.fileName);
        try {
            if (!Files.exists(thumbFile)) {
                Path orginFile = Paths.get(configuration.getTypes().get(this.type).getPath()
                        + this.currentFolder, this.fileName);
                if (!Files.exists(orginFile)) {
                    throw new ConnectorException(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_FILE_NOT_FOUND);
                }
                try {
                    ImageUtils.createThumb(orginFile, thumbFile, configuration);
                } catch (Exception e) {
                    try {
                        Files.deleteIfExists(thumbFile);
                    } catch (IOException ex) {
                        e.addSuppressed(ex);
                    }
                    throw new ConnectorException(
                            Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED,
                            e);
                }
            }
        } catch (SecurityException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }

    }

    /**
     * Fills in response headers after creating file.
     *
     * @return true if continue returning thumb or false if break and send
     * response code.
     * @throws ConnectorException when access is denied.
     */
    private boolean setResponseHeadersAfterCreatingFile() throws ConnectorException, IOException {
        // Set content size
        Path file = Paths.get(this.fullCurrentPath, this.fileName);
        try {
            FileTime lastModifiedTime = Files.getLastModifiedTime(file);
            String etag = "W/\"" + Long.toHexString(lastModifiedTime.toMillis()) + "-" + Long.toHexString(Files.size(file)) + '"';
            if (etag.equals(this.ifNoneMatch)) {
                return false;
            } else {
                response.setHeader("Etag", etag);
            }

            if (lastModifiedTime.toMillis() <= this.ifModifiedSince) {
                return false;
            } else {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                response.setHeader("Last-Modified", df.format(date));
            }
            response.setContentLengthLong(Files.size(file));
        } catch (SecurityException e) {
            throw new ConnectorException(
                    Constants.Errors.CKFINDER_CONNECTOR_ERROR_ACCESS_DENIED, e);
        }
        return true;
    }
}
