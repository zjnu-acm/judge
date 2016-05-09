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
package com.ckfinder.connector.plugins;

import com.ckfinder.connector.ServletContextFactory;
import com.ckfinder.connector.configuration.IConfiguration;
import com.ckfinder.connector.data.AfterFileUploadEventArgs;
import com.ckfinder.connector.data.EventArgs;
import com.ckfinder.connector.data.IEventHandler;
import com.ckfinder.connector.errors.ConnectorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

@Slf4j
public class WatermarkCommand implements IEventHandler {

    private static final String DEFAULT_WATERMARK = "/logo.gif";

    @Override
    public boolean runEventHandler(final EventArgs args, final IConfiguration configuration) throws ConnectorException {
        try {
            final WatermarkSettings settings = WatermarkSettings.createFromConfiguration(configuration,
                    ServletContextFactory.getServletContext());
            final File originalFile = ((AfterFileUploadEventArgs) args).getFile();
            final WatermarkPosition position = new WatermarkPosition(settings.getMarginBottom(), settings.getMarginRight());

            try (InputStream stream = Files.newInputStream(originalFile.toPath())) {
                Thumbnails.of(stream)
                        .watermark(position, getWatermakImage(settings), settings.getTransparency())
                        .scale(1)
                        .outputQuality(settings.getQuality())
                        .toFiles(Rename.NO_CHANGE);
            }

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
    private BufferedImage getWatermakImage(final WatermarkSettings settings) throws IOException {
        final String source = settings.getSource();
        final BufferedImage watermark;
        if (source == null || source.isEmpty()) {
            watermark = ImageIO.read(getClass().getResourceAsStream(DEFAULT_WATERMARK));
        } else {
            watermark = ImageIO.read(new File(source));
        }
        return watermark;
    }
}
