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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class PathPartial {

    private static final Logger log = LoggerFactory.getLogger(PathPartial.class);

    /**
     * Full range marker.
     */
    private static final Range[] FULL = {};

    // ----------------------------------------------------- Static Initializer
    /**
     * MIME multipart separation string
     */
    private static final String MIME_SEPARATION = "PATH_PARTIAL_MIME_BOUNDARY";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")
            .withZone(ZoneId.of("GMT")).withLocale(Locale.US);

    public static PathPartialBuilder builder() {
        return new PathPartialBuilder();
    }

    private final boolean useAcceptRanges;
    private final ContentDisposition contentDisposition;
    private final ETag eTag;
    private final ContentTypeResolver contentTypeResolver;
    private final NotFoundHandler notFound;

    PathPartial(boolean useAcceptRanges, @Nonnull ContentDisposition contentDisposition,
            @Nonnull ETag eTag, @Nonnull ContentTypeResolver contentType,
            @Nonnull NotFoundHandler notFound) {
        this.useAcceptRanges = useAcceptRanges;
        this.contentDisposition = Objects.requireNonNull(contentDisposition, "contentDisposition");
        this.eTag = Objects.requireNonNull(eTag, "eTag");
        this.contentTypeResolver = Objects.requireNonNull(contentType, "contentType");
        this.notFound = Objects.requireNonNull(notFound, "notFound");
    }

    public void service(HttpServletRequest request, HttpServletResponse response,
            Path path) throws IOException, ServletException {
        serveResource(request, response, !"HEAD".equals(request.getMethod()), path);
    }

    /**
     * Process a HEAD request for the specified resource.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param path path of the resource
     * @exception IOException if an input/output error occurs
     * @throws ServletException if servlet exception occurs
     */
    public void doHead(HttpServletRequest request, HttpServletResponse response,
            Path path) throws IOException, ServletException {
        // Serve the requested resource, without the data content
        serveResource(request, response, false, path);
    }

    /**
     * Process a GET request for the specified resource.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param path path of the resource
     * @exception IOException if an input/output error occurs
     * @throws ServletException if servlet exception occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response,
            Path path) throws IOException, ServletException {
        serveResource(request, response, true, path);
    }

    /**
     * Check if the conditions specified in the optional If headers are
     * satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param attr The resource information
     * @param etag ETag of the entity
     * @return boolean true if the resource meets all the specified conditions,
     * and false if any of the conditions is not satisfied, in which case
     * request processing is stopped
     */
    private boolean checkIfHeaders(HttpServletRequest request, HttpServletResponse response,
            BasicFileAttributes attr, String etag) throws IOException {
        try {
            checkIfMatch(request, etag);
            checkIfUnmodifiedSince(request, attr);
            checkIfNoneMatch(request, etag);
            checkIfModifiedSince(request, attr);
            return true;
        } catch (CheckException ex) {
            if (ex.isError()) {
                response.sendError(ex.getCode());
            } else {
                response.setStatus(ex.getCode());
                response.setHeader(HttpHeaders.ETAG, etag);
            }
            return false;
        }
    }

    /**
     * Serve the specified resource, optionally including the data content.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param content Should the content be included?
     * @param path the resource to serve
     *
     * @exception IOException if an input/output error occurs
     */
    private void serveResource(HttpServletRequest request, HttpServletResponse response,
            boolean content, Path path) throws IOException, ServletException {
        ActionContext context = new ActionContext()
                .put(HttpServletRequest.class, request)
                .put(HttpServletResponse.class, response)
                .put(ServletContext.class, request.getServletContext())
                .put(Path.class, path);
        if (path == null) {
            notFound.handle(context);
            return;
        }
        BasicFileAttributes attr;
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException ex) {
            notFound.handle(context);
            return;
        }
        context.put(BasicFileAttributes.class, attr);

        boolean isError = response.getStatus() >= HttpServletResponse.SC_BAD_REQUEST;
        // Check if the conditions specified in the optional If headers are
        // satisfied.
        // Checking If headers
        boolean included = (request.getAttribute(RequestDispatcher.INCLUDE_CONTEXT_PATH) != null);
        String etag = this.eTag.getValue(context);
        if (!included && !isError && !checkIfHeaders(request, response, attr, etag)) {
            return;
        }
        // Find content type.
        // TODO mime type
        String contentType = contentTypeResolver.getValue(context);
        Range[] ranges = null;
        if (!isError) {
            if (useAcceptRanges) {
                // Accept ranges header
                response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
            }
            // Parse range specifier
            ranges = parseRange(request, response, attr, etag);
            // ETag header
            response.setHeader(HttpHeaders.ETAG, etag);
            // Last-Modified header
            response.setHeader(HttpHeaders.LAST_MODIFIED, dtf.format(attr.lastModifiedTime().toInstant()));
        }
        // Get content length
        long contentLength = attr.size();
        // Special case for zero length files, which would cause a
        // (silent) ISE when setting the output buffer size
        boolean serveContent = content && contentLength != 0;
        ServletOutputStream ostream = null;
        if (serveContent) {
            ostream = response.getOutputStream();
        }

        String disposition = contentDisposition.getValue(context);
        if (disposition != null) {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition);
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
                log.trace("Serving bytes");
                Files.copy(path, ostream);
            }
        } else if (ranges != null && ranges.length != 0) {
            // Partial content response.
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            if (ranges.length == 1) {
                Range range = ranges[0];
                response.addHeader(HttpHeaders.CONTENT_RANGE, range.toString());
                long length = range.end - range.start + 1;
                response.setContentLengthLong(length);
                if (contentType != null) {
                    log.debug("serveFile: contentType='{}'", contentType);
                    response.setContentType(contentType);
                }
                if (serveContent) {
                    try (InputStream stream = Files.newInputStream(path)) {
                        copyRange(stream, ostream, range.start, range.end);
                    }
                }
            } else {
                response.setContentType("multipart/byteranges; boundary=" + MIME_SEPARATION);
                if (serveContent) {
                    copy(path, ostream, ranges, contentType);
                }
            }
        }
    }

    /**
     * Parse the range header.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param attr
     * @return Vector of ranges
     */
    @Nullable
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    private Range[] parseRange(HttpServletRequest request, HttpServletResponse response,
            BasicFileAttributes attr, String etag) throws IOException {
        // Checking If-Range
        String headerValue = request.getHeader(HttpHeaders.IF_RANGE);
        if (headerValue != null) {
            long headerValueTime = -1;
            try {
                headerValueTime = request.getDateHeader(HttpHeaders.IF_RANGE);
            } catch (IllegalArgumentException e) {
                // Ignore
            }
            // If the ETag the client gave does not match the entity
            // eTag, then the entire entity is returned.
            if (headerValueTime == -1 && !headerValue.trim().equals(etag)
                    || attr.lastModifiedTime().toMillis() > headerValueTime + 1000) {
                // If the timestamp of the entity the client got is older than
                // the last modification date of the entity, the entire entity
                // is returned.
                return FULL;
            }
        }
        long fileLength = attr.size();
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
        // List which will contain all the ranges which are successfully
        // parsed.
        List<Range> result = new ArrayList<>(4);
        // Parsing the range list
        // "bytes=".length() = 6
        for (int index, last = 6;; last = index + 1) {
            index = rangeHeader.indexOf(',', last);
            boolean isLast = index == -1;
            final String rangeDefinition = (isLast ? rangeHeader.substring(last) : rangeHeader.substring(last, index)).trim();
            final int dashPos = rangeDefinition.indexOf('-');
            if (dashPos == -1) {
                break;
            }
            final Range currentRange = new Range(fileLength);
            try {
                if (dashPos == 0) {
                    final long offset = Long.parseLong(rangeDefinition);
                    if (offset == 0) { // -0, --0
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
            if (!currentRange.validate()) {
                break;
            }
            result.add(currentRange);
            if (isLast) {
                int size = result.size();
                if (size == 0) {
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
     * @param etag ETag of the entity
     */
    private void checkIfMatch(HttpServletRequest request, String etag) {
        String headerValue = request.getHeader(HttpHeaders.IF_MATCH);
        if (headerValue != null && headerValue.indexOf('*') == -1
                && !anyMatches(headerValue, etag)) {
            // If none of the given ETags match, 412 Precodition failed is
            // sent back
            throw new CheckException(HttpServletResponse.SC_PRECONDITION_FAILED);
        }
    }

    /**
     * Check if the if-modified-since condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param attr File attributes
     */
    @SuppressWarnings("NestedAssignment")
    private void checkIfModifiedSince(HttpServletRequest request, BasicFileAttributes attr) {
        try {
            long headerValue;
            // If an If-None-Match header has been specified, if modified since
            // is ignored.
            if (request.getHeader(HttpHeaders.IF_NONE_MATCH) == null
                    && (headerValue = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE)) != -1
                    && attr.lastModifiedTime().toMillis() < headerValue + 1000) {
                // The entity has not been modified since the date
                // specified by the client. This is not an error case.
                throw new CheckException(HttpServletResponse.SC_NOT_MODIFIED);
            }
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Check if the if-none-match condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param etag ETag of the entity
     */
    private void checkIfNoneMatch(HttpServletRequest request, String etag) {
        String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (headerValue != null && (headerValue.equals("*") || anyMatches(headerValue, etag))) {
            // For GET and HEAD, we should respond with
            // 304 Not Modified.
            // For every other method, 412 Precondition Failed is sent
            // back.
            String method = request.getMethod();
            if ("GET".equals(method) || "HEAD".equals(method)) {
                throw new CheckException(HttpServletResponse.SC_NOT_MODIFIED);
            } else {
                throw new CheckException(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        }
    }

    /**
     * Check if the if-unmodified-since condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param attr File attributes
     */
    private void checkIfUnmodifiedSince(HttpServletRequest request, BasicFileAttributes attr) {
        if (request.getHeader(HttpHeaders.IF_MATCH) == null) {
            try {
                long lastModified = attr.lastModifiedTime().toMillis();
                long headerValue = request.getDateHeader(HttpHeaders.IF_UNMODIFIED_SINCE);
                if (headerValue != -1 && lastModified >= headerValue + 1000) {
                    // The entity has not been modified since the date
                    // specified by the client. This is not an error case.
                    throw new CheckException(HttpServletResponse.SC_PRECONDITION_FAILED);
                }
            } catch (IllegalArgumentException ex) {
                throw new CheckException(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
        }
    }

    /**
     * Copy the contents of the specified input stream to the specified output
     * stream, and ensure that both streams are closed before returning (even in
     * the face of an exception).
     *
     * @param path The cache entry for the source resource
     * @param ostream The output stream to write to
     * @param ranges Enumeration of the ranges the client wanted to retrieve
     * @param contentType Content type of the resource
     * @exception IOException if an input/output error occurs
     */
    private void copy(Path path, ServletOutputStream ostream, Range[] ranges, String contentType)
            throws IOException {
        IOException exception = null;
        for (Range currentRange : ranges) {
            try (InputStream stream = Files.newInputStream(path)) {
                // Writing MIME header.
                ostream.println();
                ostream.println("--" + MIME_SEPARATION);
                if (contentType != null) {
                    ostream.println(HttpHeaders.CONTENT_TYPE + ": " + contentType);
                }
                ostream.println(HttpHeaders.CONTENT_RANGE + ": " + currentRange);
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
     */
    private void copyRange(InputStream istream, OutputStream ostream, long start, long end)
            throws IOException {
        log.trace("Serving bytes: {}-{}", start, end);
        IOUtils.copy(istream, ostream, start, end + 1 - start, new byte[8192]);
    }

    private boolean anyMatches(String headerValue, String etag) {
        StringTokenizer tokenizer = new StringTokenizer(headerValue, ",");
        while (tokenizer.hasMoreTokens()) {
            if (tokenizer.nextToken().trim().equals(etag)) {
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

        @Override
        public String toString() {
            return "bytes " + start + "-" + end + "/" + total;
        }

    }

    private static class CheckException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private final int code;

        CheckException(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public boolean isError() {
            return code >= HttpServletResponse.SC_BAD_REQUEST;
        }

        @Override
        public Throwable fillInStackTrace() {
            return this;
        }

    }

}
