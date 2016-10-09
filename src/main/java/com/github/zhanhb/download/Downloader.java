/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zhanhb.download;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.annotation.Nullable;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@Slf4j
public class Downloader {

    /**
     * Full range marker.
     */
    private static final Range[] FULL = {};

    // ----------------------------------------------------- Static Initializer
    /**
     * MIME multipart separation string
     */
    private static final String MIME_SEPARATION = "DOWNLOADER_MIME_BOUNDARY";
    private static final ThreadLocal<byte[]> INPUT_BUFFER_POOL = ThreadLocal.withInitial(() -> new byte[8192]);
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")
            .withZone(ZoneId.of("GMT")).withLocale(Locale.US);

    public static Downloader newInstance() {
        return new Downloader();
    }

    private static String getETag(Resource resource) throws IOException {
        long contentLength = resource.contentLength();
        long lastModified = resource.lastModified();
        return "W/\"" + contentLength + "-" + lastModified + "\"";
    }

    // ----------------------------------------------------- Instance Variables
    /**
     * Should the Accept-Ranges: bytes header be send with static resources?
     */
    private boolean useAcceptRanges = true;

    /**
     * Should the Content-Disposition: attachment; filename=... header be sent
     * with static resources?
     */
    private ContentDisposition contentDisposition = SimpleContentDisposition.ATTACHMENT;

    private Downloader() {
    }

    // ------------------------------------------------------ public Methods
    public Downloader useAcceptRanges(boolean useAcceptRanges) {
        this.useAcceptRanges = useAcceptRanges;
        return this;
    }

    public Downloader contentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
        return this;
    }

    public void service(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        serveResource(request, response, !"HEAD".equals(request.getMethod()), resource);
    }

    /**
     * Process a HEAD request for the specified resource.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource
     *
     * @exception IOException if an input/output error occurs
     */
    public void doHead(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        // Serve the requested resource, without the data content
        serveResource(request, response, false, resource);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        serveResource(request, response, true, resource);
    }

    /**
     * Check if the conditions specified in the optional If headers are
     * satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource The resource information
     * @return boolean true if the resource meets all the specified conditions,
     * and false if any of the conditions is not satisfied, in which case
     * request processing is stopped
     */
    private boolean checkIfHeaders(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        return checkIfMatch(request, response, resource)
                && checkIfUnmodifiedSince(request, response, resource)
                && checkIfNoneMatch(request, response, resource)
                && checkIfModifiedSince(request, response, resource);
    }

    /**
     * Serve the specified resource, optionally including the data content.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param content Should the content be included?
     * @param resource the resource to serve
     *
     * @exception IOException if an input/output error occurs
     */
    private void serveResource(HttpServletRequest request, HttpServletResponse response,
            boolean content, Resource resource) throws IOException {
        boolean serveContent = content;
        if (resource == null || !resource.exists()) {
            // Check if we're included so we can return the appropriate
            // missing resource name in the error
            String requestUri = (String) request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI);
            if (requestUri == null) {
                requestUri = request.getRequestURI();
            } else {
                // We're included
                // SRV.9.3 says we must throw a FNFE
                throw new FileNotFoundException("The requested resource (" + requestUri + ") is not available");
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, requestUri);
            return;
        }

        boolean isError = response.getStatus() >= HttpServletResponse.SC_BAD_REQUEST;
        // Check if the conditions specified in the optional If headers are
        // satisfied.
        // Checking If headers
        boolean included = (request.getAttribute(RequestDispatcher.INCLUDE_CONTEXT_PATH) != null);
        if (!included && !isError && !checkIfHeaders(request, response, resource)) {
            return;
        }
        // Find content type.
        // TODO mime type
        String contentType = null;
        if (contentType == null) {
            contentType = request.getServletContext().getMimeType(resource.getFilename());
        }
        Range[] ranges = null;
        long contentLength;
        if (!isError) {
            if (useAcceptRanges) {
                // Accept ranges header
                response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
            }
            // Parse range specifier
            ranges = parseRange(request, response, resource);
            // ETag header
            response.setHeader(HttpHeaders.ETAG, getETag(resource));
            // Last-Modified header
            response.setHeader(HttpHeaders.LAST_MODIFIED, dtf.format(Instant.ofEpochMilli(resource.lastModified())));
        }
        // Get content length
        contentLength = resource.contentLength();
        // Special case for zero length files, which would cause a
        // (silent) ISE when setting the output buffer size
        if (contentLength == 0L) {
            serveContent = false;
        }
        ServletOutputStream ostream = null;
        if (serveContent) {
            ostream = response.getOutputStream();
        }

        ContentDisposition cd = this.contentDisposition;
        if (cd != null) {
            String disposition = cd.getContentDisposition(resource.getFilename());
            if (disposition != null) {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition);
            }
        }

        // Check to see if a Filter, Valve of wrapper has written some content.
        // If it has, disable range requests and setting of a content length
        // since neither can be done reliably.
        if (isError || ranges == FULL) {
            // Set the appropriate output headers
            if (contentType != null) {
                log.debug("serveFile: contentType='{}'", contentType);
                response.setContentType(contentType);
            }
            if (contentLength >= 0) {
                response.setContentLengthLong(contentLength);
            }
            // Copy the input stream to our output stream (if requested)
            if (serveContent) {
                try (InputStream stream = resource.getInputStream()) {
                    log.trace("Serving bytes");
                    IOUtils.copyLarge(stream, ostream, INPUT_BUFFER_POOL.get());
                }
            }
        } else if (ranges != null && ranges.length != 0) {
            // Partial content response.
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            if (ranges.length == 1) {
                Range range = ranges[0];
                response.addHeader(HttpHeaders.CONTENT_RANGE, "bytes " + range.start + "-" + range.end + "/" + range.total);
                long length = range.end - range.start + 1;
                response.setContentLengthLong(length);
                if (contentType != null) {
                    log.debug("serveFile: contentType='{}'", contentType);
                    response.setContentType(contentType);
                }
                if (serveContent) {
                    try (InputStream stream = resource.getInputStream()) {
                        copyRange(stream, ostream, range.start, range.end);
                    }
                }
            } else {
                response.setContentType("multipart/byteranges; boundary=" + MIME_SEPARATION);
                if (serveContent) {
                    copy(resource, ostream, ranges, contentType);
                }
            }
        }
    }

    /**
     * Parse the range header.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource
     * @return Vector of ranges
     */
    @Nullable
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    private Range[] parseRange(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        // Checking If-Range
        String headerValue = request.getHeader(HttpHeaders.IF_RANGE);
        if (headerValue != null) {
            long headerValueTime = -1;
            try {
                headerValueTime = request.getDateHeader(HttpHeaders.IF_RANGE);
            } catch (IllegalArgumentException e) {
                // Ignore
            }
            String eTag = getETag(resource);
            long lastModified = resource.lastModified();
            if (headerValueTime == -1) {
                // If the ETag the client gave does not match the entity
                // etag, then the entire entity is returned.
                if (!headerValue.trim().equals(eTag)) {
                    return FULL;
                }
            } else if (lastModified > headerValueTime + 1000) {
                // If the timestamp of the entity the client got is older than
                // the last modification date of the entity, the entire entity
                // is returned.
                return FULL;
            }
        }
        long fileLength = resource.contentLength();
        if (fileLength == 0) {
            return FULL;
        }
        // Retrieving the range header (if any is specified
        String rangeHeader = request.getHeader(HttpHeaders.RANGE);
        if (rangeHeader == null) {
            return FULL;
        }
        // bytes is the only range unit supported (and I don't see the point
        // of adding new ones).
        if (!rangeHeader.startsWith("bytes=")) {
            return FULL;
        }
        // Vector which will contain all the ranges which are successfully
        // parsed.
        List<Range> result = new ArrayList<>(4);
        // Parsing the range list
        // "bytes=".length() = 6
        for (int index, last = 6;; last = index + 1) {
            index = rangeHeader.indexOf(',', last);
            final String rangeDefinition = (index != -1 ? rangeHeader.substring(last, index) : rangeHeader.substring(last)).trim();
            final Range currentRange = new Range(fileLength);
            final int dashPos = rangeDefinition.indexOf('-');
            if (dashPos == -1) {
                break;
            }
            try {
                if (dashPos == 0) {
                    final long offset = Long.parseLong(rangeDefinition);
                    if (offset == 0) { // -0
                        break;
                    }
                    currentRange.start = Math.max(fileLength + offset, 0);
                } else {
                    currentRange.start = Long.parseLong(rangeDefinition.substring(0, dashPos));
                    if (dashPos < rangeDefinition.length() - 1) {
                        currentRange.end = Long.parseLong(rangeDefinition.substring(dashPos + 1, rangeDefinition.length()));
                    }
                }
            } catch (NumberFormatException e) {
                break;
            }
            if (currentRange.validate()) {
                result.add(currentRange);
            }
            if (index == -1) {
                int size = result.size();
                if (size == 0 || size > 1 && resource.isOpen()) {
                    break;
                }
                return result.toArray(new Range[size]);
            }
        }
        response.addHeader(HttpHeaders.CONTENT_RANGE, "bytes */" + fileLength);
        response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
        return null;
    }

    /**
     * Check if the if-match condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource File object
     * @return boolean true if the resource meets the specified condition, and
     * false if the condition is not satisfied, in which case request processing
     * is stopped
     */
    private boolean checkIfMatch(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        String eTag = getETag(resource);
        String headerValue = request.getHeader(HttpHeaders.IF_MATCH);
        if (headerValue != null && headerValue.indexOf('*') == -1
                && !anyMatches(headerValue, eTag)) {
            // If none of the given ETags match, 412 Precodition failed is
            // sent back
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return false;
        }
        return true;
    }

    /**
     * Check if the if-modified-since condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource File object
     * @return boolean true if the resource meets the specified condition, and
     * false if the condition is not satisfied, in which case request processing
     * is stopped
     */
    @SuppressWarnings("NestedAssignment")
    private boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        try {
            long headerValue;
            // If an If-None-Match header has been specified, if modified since
            // is ignored.
            if (request.getHeader(HttpHeaders.IF_NONE_MATCH) == null
                    && (headerValue = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE)) != -1
                    && resource.lastModified() < headerValue + 1000) {
                // The entity has not been modified since the date
                // specified by the client. This is not an error case.
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader(HttpHeaders.ETAG, getETag(resource));
                return false;
            }
        } catch (IllegalArgumentException ex) {
        }
        return true;
    }

    /**
     * Check if the if-none-match condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource File object
     * @return boolean true if the resource meets the specified condition, and
     * false if the condition is not satisfied, in which case request processing
     * is stopped
     */
    private boolean checkIfNoneMatch(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        String eTag = getETag(resource);
        String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (headerValue != null && (headerValue.equals("*") || anyMatches(headerValue, eTag))) {
            // For GET and HEAD, we should respond with
            // 304 Not Modified.
            // For every other method, 412 Precondition Failed is sent
            // back.
            String method = request.getMethod();
            if ("GET".equals(method) || "HEAD".equals(method)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader(HttpHeaders.ETAG, eTag);
            } else {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
            return false;
        }
        return true;
    }

    /**
     * Check if the if-unmodified-since condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resource File object
     * @return boolean true if the resource meets the specified condition, and
     * false if the condition is not satisfied, in which case request processing
     * is stopped
     */
    private boolean checkIfUnmodifiedSince(HttpServletRequest request, HttpServletResponse response,
            Resource resource) throws IOException {
        if (request.getHeader(HttpHeaders.IF_MATCH) == null) {
            try {
                long lastModified = resource.lastModified();
                long headerValue = request.getDateHeader(HttpHeaders.IF_UNMODIFIED_SINCE);
                if (headerValue != -1 && lastModified >= headerValue + 1000) {
                    // The entity has not been modified since the date
                    // specified by the client. This is not an error case.
                    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
                    return false;
                }
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
                return false;
            }
        }
        return true;
    }

    /**
     * Copy the contents of the specified input stream to the specified output
     * stream, and ensure that both streams are closed before returning (even in
     * the face of an exception).
     *
     * @param resource The cache entry for the source resource
     * @param ostream The output stream to write to
     * @param ranges Enumeration of the ranges the client wanted to retrieve
     * @param contentType Content type of the resource
     * @exception IOException if an input/output error occurs
     */
    private void copy(Resource resource, ServletOutputStream ostream, Range[] ranges, String contentType)
            throws IOException {
        IOException exception = null;
        for (Range currentRange : ranges) {
            try (InputStream stream = resource.getInputStream()) {
                // Writing MIME header.
                ostream.println();
                ostream.println("--" + MIME_SEPARATION);
                if (contentType != null) {
                    ostream.println(HttpHeaders.CONTENT_TYPE + ": " + contentType);
                }
                ostream.println(HttpHeaders.CONTENT_RANGE + ": bytes " + currentRange.start + "-" + currentRange.end + "/" + currentRange.total);
                ostream.println();
                // Printing content
                copyRange(stream, ostream, currentRange.start, currentRange.end);
            } catch (IOException ex) {
                exception = ex;
            }
        }
        ostream.println();
        ostream.print("--" + MIME_SEPARATION + "--");
        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Copy the contents of the specified input stream to the specified output
     * stream, and ensure that both streams are closed before returning (even in
     * the face of an exception).
     *
     * @param istream The input stream to read from
     * @param ostream The output stream to write to
     * @param start Start of the range which will be copied
     * @param end End of the range which will be copied
     * @return Exception which occurred during processing
     */
    private void copyRange(InputStream istream, OutputStream ostream, long start, long end)
            throws IOException {
        log.trace("Serving bytes: {}-{}", start, end);
        IOUtils.copyLarge(istream, ostream, start, end + 1 - start, INPUT_BUFFER_POOL.get());
    }

    private boolean anyMatches(String headerValue, String eTag) {
        StringTokenizer tokenizer = new StringTokenizer(headerValue, ",");
        while (tokenizer.hasMoreTokens()) {
            if (tokenizer.nextToken().trim().equals(eTag)) {
                return true;
            }
        }
        return false;
    }

    private static class Range {

        public long start;
        public long end;
        public long total;

        Range(long length) {
            this.end = length - 1;
            this.total = length;
        }

        /**
         * Validate range.
         */
        public boolean validate() {
            end = Math.min(end, total - 1);
            return start <= end;
        }

    }

}
