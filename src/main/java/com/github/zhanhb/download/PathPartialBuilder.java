/*
 * Copyright 2017 zhanhb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

/**
 *
 * @author zhanhb
 */
public class PathPartialBuilder {

    // ----------------------------------------------------- Instance Variables
    /**
     * Should the Accept-Ranges: bytes header be send with static resources?
     */
    private boolean useAcceptRanges = true;

    /**
     * Should the Content-Disposition: attachment; filename=... header be sent
     * with static resources?
     */
    private ContentDisposition contentDisposition = ContentDisposition.none();

    /**
     * content type resolver
     */
    private ContentTypeResolver contentType = ContentTypeResolver.getDefault();

    /**
     * ETag generator
     */
    private ETag eTag = ETag.weak();

    /**
     * not found handler
     */
    private NotFoundHandler notFound = NotFoundHandler.defaultHandler();

    // package private
    PathPartialBuilder() {
    }

    public PathPartialBuilder useAcceptRanges(boolean useAcceptRanges) {
        this.useAcceptRanges = useAcceptRanges;
        return this;
    }

    public PathPartialBuilder contentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
        return this;
    }

    public PathPartialBuilder eTag(ETag etag) {
        this.eTag = etag;
        return this;
    }

    public PathPartialBuilder contentType(ContentTypeResolver contentType) {
        this.contentType = contentType;
        return this;
    }

    public PathPartialBuilder notFound(NotFoundHandler notFound) {
        this.notFound = notFound;
        return this;
    }

    public PathPartial build() {
        return new PathPartial(useAcceptRanges, contentDisposition, eTag, contentType, notFound);
    }

}
