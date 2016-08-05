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
package com.github.zhanhb.ckfinder.connector.handlers.command;

import com.github.zhanhb.ckfinder.connector.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 * Class to handle <code>QuickUpload</code> command.
 */
public class QuickUploadCommand extends FileUploadCommand {

    @Override
    protected void handleOnUploadCompleteResponse(Writer writer,
            String errorMsg) throws IOException {
        if (this.responseType != null && this.responseType.equalsIgnoreCase("json")) {
            handleJSONResponse(writer, errorMsg, null);
        } else {
            writer.write("<script type=\"text/javascript\">");
            writer.write("window.parent.OnUploadCompleted(");
            writer.write("" + this.errorCode + ", ");
            if (uploaded) {
                writer.write("'" + configuration.getTypes().get(this.type).getUrl()
                        + this.currentFolder
                        + FileUtils.backupWithBackSlash(FileUtils.encodeURIComponent(this.newFileName), "'")
                        + "', ");
                writer.write("'" + FileUtils.backupWithBackSlash(this.newFileName, "'")
                        + "', ");
            } else {
                writer.write("'', '', ");
            }
            writer.write("''");
            writer.write(");");
            writer.write("</script>");
        }
    }

    @Override
    protected void handleOnUploadCompleteCallFuncResponse(Writer writer,
            String errorMsg, String path) throws IOException {
        if (this.responseType != null && this.responseType.equalsIgnoreCase("json")) {
            handleJSONResponse(writer, errorMsg, path);
        } else {
            writer.write("<script type=\"text/javascript\">");
            this.ckEditorFuncNum = this.ckEditorFuncNum.replaceAll(
                    "[^\\d]", "");
            writer.write(("window.parent.CKEDITOR.tools.callFunction("
                    + this.ckEditorFuncNum + ", '"
                    + path
                    + FileUtils.backupWithBackSlash(FileUtils.encodeURIComponent(this.newFileName), "'")
                    + "', '" + errorMsg + "');"));
            writer.write("</script>");
        }
    }

    @Override
    protected boolean checkFuncNum() {
        return this.ckEditorFuncNum != null;
    }

    @Override
    public void setResponseHeader(HttpServletResponse response,
            ServletContext sc) {
        response.setCharacterEncoding("utf-8");
        if (this.responseType != null && this.responseType.equalsIgnoreCase("json")) {
            response.setContentType("application/json");
        } else {
            response.setContentType("text/html");
        }
    }

    /**
     * Writes JSON object into response stream after uploading file which was
     * dragged and dropped in to CKEditor 4.5 or higher.
     *
     * @param writer the response stream
     * @param errorMsg string representing error message which indicates that
     * there was an error during upload or uploaded file was renamed
     * @param path path to uploaded file
     */
    private void handleJSONResponse(Writer writer,
            String errorMsg, String path) throws IOException {

        Gson gson = new GsonBuilder().serializeNulls().create();
        @SuppressWarnings("CollectionWithoutInitialCapacity")
        Map<String, Object> jsonObj = new HashMap<>();

        jsonObj.put("fileName", this.newFileName);
        jsonObj.put("uploaded", this.uploaded ? 1 : 0);

        if (uploaded) {
            if (path != null && !path.isEmpty()) {
                jsonObj.put(
                        "url",
                        path
                        + FileUtils.backupWithBackSlash(FileUtils
                                .encodeURIComponent(this.newFileName),
                                "'"));
            } else {
                jsonObj.put(
                        "url",
                        configuration.getTypes().get(this.type).getUrl()
                        + this.currentFolder
                        + FileUtils.backupWithBackSlash(FileUtils
                                .encodeURIComponent(this.newFileName),
                                "'"));
            }
        }

        if (errorMsg != null && !errorMsg.isEmpty()) {
            Map<String, Object> jsonErrObj = new HashMap<>(3);
            jsonErrObj.put("number", this.errorCode);
            jsonErrObj.put("message", errorMsg);
            jsonObj.put("error", jsonErrObj);
        }

        writer.write(gson.toJson(jsonObj));
    }

}
