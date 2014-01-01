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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Rainbow4J {

    public static Spectrum readSpectrum(BufferedImage image) throws IOException {
        return readSpectrum(image, 256);
    }

    /**
     * 
     * @param image an image for calculating the color spectrum
     * @param precision 8 to 256 value for spectrum accuracy. The bigger value - the better precision, but the more memory it takes
     * @return
     * @throws IOException
     */
    public static Spectrum readSpectrum(BufferedImage image, int precision) throws IOException {
        
        if (precision < 8) throw new IllegalArgumentException("Color size should not be less then 8");
        if (precision > 256) throw new IllegalArgumentException("Color size should not be bigger then 256");
        
        int spectrum[][][] = new int[precision][precision][precision];

        int width = image.getWidth();
        int height = image.getHeight();

        final byte[] a = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int k = 0;
        int r,g,b;
        
        
        for (int i = width*height - 1; i >= 0; i--) {
            if (hasAlphaChannel) {
                k += 1; //skipping alpha channel
            }
            r = (a[k] & 0xff) * precision / 256;
            g = (a[k+1] & 0xff) * precision / 256;
            b = (a[k+2] & 0xff) * precision / 256;
            
            spectrum[Math.min(r, precision - 1)][Math.min(g, precision - 1)][Math.min(b, precision - 1)]+= 1;

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
