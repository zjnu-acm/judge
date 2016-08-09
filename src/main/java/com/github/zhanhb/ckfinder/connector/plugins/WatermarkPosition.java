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

import java.awt.Point;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.geometry.Position;

/**
 * Class that calculates Watermark position from bottom right side of image.
 */
@RequiredArgsConstructor
class WatermarkPosition implements Position {

    private final int insetBottom;
    private final int insetRight;

    @Override
    public Point calculate(
            int enclosingWidth, int enclosingHeight, int width, int height,
            int insetLeft, int insetRight, int insetTop, int insetBottom) {
        int x = enclosingWidth - width - this.insetRight;
        int y = enclosingHeight - height - this.insetBottom;
        return new Point(x, y);
    }

}
