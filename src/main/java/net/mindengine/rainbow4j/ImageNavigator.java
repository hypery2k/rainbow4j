/*******************************************************************************
* Copyright 2014 Ivan Shubin http://mindengine.net
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package net.mindengine.rainbow4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageNavigator {
    private final BufferedImage image;
    private final byte[] bytes;
    private int w;
    private int h;
    private int blockSize = 3;

    public ImageNavigator(BufferedImage image) {
        this.image = image;
        this.bytes = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        boolean hasAlphaChannel = image.getColorModel().hasAlpha();
        this.blockSize = 3;
        if (hasAlphaChannel) {
            this.blockSize = 4;
        }

        this.w = image.getWidth();
        this.h = image.getHeight();
    }

    public Color getSmoothedColor(int x, int y, int x2, int y2) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;

        if (x >= w || y >= h) {
            throw new RuntimeException("pixel is out of range");
        }

        if (x2 >= w) x2 = w - 1;
        if (y2 >= h) y2 = h - 1;

        int i, j;

        long r = 0, g = 0, b = 0, t = 0;

        for (i = x; i <= x2; i++) {
            for (j = y; j <= y2; j++) {
                t += 1;
                Color c = pickColor(i, j);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }

        if (t > 0) {
            return new Color((int)(r/t), (int)(g/t), (int)(b/t));
        }
        else return new Color(0, 0, 0);
    }

    private Color pickColor(int x, int y) {
        int k = y * w * blockSize + x * blockSize;

        return new Color(bytes[k] & 0xff,
                bytes[k + 1] & 0xff,
                bytes[k + 2] & 0xff
        );
    }

    public static long colorDiff(Color cA, Color cB) {
        return Math.abs(cA.getRed() - cB.getRed())
               + Math.abs(cA.getGreen() - cB.getGreen())
               + Math.abs(cB.getBlue() - cB.getBlue());
    }
}
