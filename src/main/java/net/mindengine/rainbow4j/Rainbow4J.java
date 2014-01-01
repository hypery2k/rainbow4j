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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Rainbow4J {



    public static Spectrum readSpectrum(BufferedImage image) throws IOException {
        int spectrum[][][] = new int[256][256][256];

        int width = image.getWidth();
        int height = image.getHeight();

        final byte[] a = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int k =0;
        int r,g,b;
        for (int i = width*height - 1; i >= 0; i--) {
            if (hasAlphaChannel) {
                k += 1; //skipping alpha chanel
            }
            r = a[k] & 0xff;
            g = a[k+1] & 0xff;
            b = a[k+2] & 0xff;

            spectrum[r][g][b]+= 1;

            k+=3;
        }

        return new Spectrum(spectrum, width, height);
    }

    public static BufferedImage loadImage(String filePath) throws IOException{
        return loadImage(new FileInputStream(new File(filePath)));
    }

    public static BufferedImage loadImage(InputStream resourceAsStream) throws IOException {
        return ImageIO.read(resourceAsStream);
    }

    public static BufferedImage crop(BufferedImage image, int x, int y, int w, int h) {
        return image.getSubimage(x, y, w, h);
    }
}
