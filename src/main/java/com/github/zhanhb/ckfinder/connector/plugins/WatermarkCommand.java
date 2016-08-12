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

import com.github.zhanhb.ckfinder.connector.configuration.IConfiguration;
import com.github.zhanhb.ckfinder.connector.data.AfterFileUploadEventArgs;
import com.github.zhanhb.ckfinder.connector.data.AfterFileUploadEventHandler;
import com.github.zhanhb.ckfinder.connector.errors.ConnectorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.springframework.core.io.Resource;

@Slf4j
public class WatermarkCommand implements AfterFileUploadEventHandler {

    private static final String DEFAULT_WATERMARK = "/logo.gif";

    @Override
    public boolean runEventHandler(AfterFileUploadEventArgs args, IConfiguration configuration) throws ConnectorException {
        try {
            WatermarkSettings settings = configuration.getWatermarkSettings();
            final Path originalFile = args.getFile();
            final WatermarkPosition position = new WatermarkPosition(settings.getMarginBottom(), settings.getMarginRight());

            Thumbnails.of(originalFile.toFile())
                    .watermark(position, getWatermakImage(settings), settings.getTransparency())
                    .scale(1)
                    .outputQuality(settings.getQuality())
                    .toFiles(Rename.NO_CHANGE);
        } catch (Exception ex) {
            // only log error if watermark is not created
            log.error("", ex);
        }
        return true;
    }

    /**
     * Extracts image location from settings or uses default image if none is
     * provided.
     *
     * @param settings
     * @return
     * @throws IOException
     */
    private BufferedImage getWatermakImage(WatermarkSettings settings) throws IOException {
        final Resource source = settings.getSource();
        final BufferedImage watermark;
        if (source == null) {
            watermark = ImageIO.read(getClass().getResourceAsStream(DEFAULT_WATERMARK));
        } else {
            try (InputStream is = source.getInputStream()) {
                watermark = ImageIO.read(is);
            }
        }
        return watermark;
    }

}
