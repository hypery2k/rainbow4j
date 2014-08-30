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


import com.sun.media.jai.codec.SeekableStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.media.jai.JAI;
public class Rainbow4J {

    public static Spectrum readSpectrum(BufferedImage image) throws IOException {
        return readSpectrum(image, null, 256);
    }
    
    public static Spectrum readSpectrum(BufferedImage image, Rectangle rectangle) throws IOException {
        return readSpectrum(image, rectangle, 256);
    }
    
    public static Spectrum readSpectrum(BufferedImage image, int precision) throws IOException {
        return readSpectrum(image, null, precision);
    }



    public static ImageCompareResult compare(BufferedImage imageA, BufferedImage imageB, int pixelSmooth, int tolerance) {

        if (tolerance < 0 ) {
            tolerance = 0;
        }


        int width = imageA.getWidth();
        int height = imageA.getHeight();

        if (width != imageB.getWidth() || height != imageB.getHeight()) {
            throw new RuntimeException("Cannot compare images with different sizes");
        }

        ImageNavigator navA = new ImageNavigator(imageA);
        ImageNavigator navB = new ImageNavigator(imageB);

        int y = 0, x = 0;

        int step = 1 + pixelSmooth * 2;

        double totalMismatchingPixels = 0;

        while(y < height) {
            while (x < width) {
                Color cA = navA.getSmoothedColor(x, y, x + step, y + step);
                Color cB = navB.getSmoothedColor(x, y, x + step, y + step);

                double ratio = edgeCorrectionRatio(x, y, step, width, height);

                long colorError = ImageNavigator.colorDiff(cA, cB);
                if (colorError > tolerance) {
                    totalMismatchingPixels += ratio * step * step;
                }

                x += step;
            }
            y += step;
            x = 0;
        }

        ImageCompareResult result = new ImageCompareResult();
        result.setPercentage(100.0 * totalMismatchingPixels / (width * height));


        result.setTotalPixels((long)totalMismatchingPixels);
        return result;
    }

    private static double edgeCorrectionRatio(int x, int y, int step, int width, int height) {
        int dw = width - x;
        int dh = height - y;

        if (dw < step || dh < step) {
            return Math.min(dw, step) * Math.min(dh, step) / (step * step);
        }
        return 1.0;
    }

    /**
     * 
     * @param image an image for calculating the color spectrum
     * @param precision 8 to 256 value for spectrum accuracy. The bigger value - the better precision, but the more memory it takes
     * @return
     * @throws IOException
     */
    public static Spectrum readSpectrum(BufferedImage image, Rectangle area, int precision) throws IOException {
        
        if (precision < 8) throw new IllegalArgumentException("Color size should not be less then 8");
        if (precision > 256) throw new IllegalArgumentException("Color size should not be bigger then 256");
        
        int spectrum[][][] = new int[precision][precision][precision];

        int width = image.getWidth();
        int height = image.getHeight();
        
        byte[] a = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        boolean hasAlphaChannel = image.getColorModel().hasAlpha();
        int blockSize = 3;
        if (hasAlphaChannel) {
            blockSize = 4;
        }
        
        int spectrumWidth = width;
        int spectrumHeight = height;
        
        if (area == null) {
            area = new Rectangle(0, 0, width, height);
        }
        else {
            spectrumWidth = area.width;
            spectrumHeight = area.height;
        }
        
        int k = 0;
        int r,g,b;
        
        for (int y = area.y; y < area.y + area.height; y++) {
            for (int x = area.x; x < area.x + area.width; x++) {
                k = y * width * blockSize + x * blockSize;
                
                r = (int)(a[k] & 0xff) * precision / 256;
                g = (int)(a[k+1] & 0xff) * precision / 256;
                b = (int)(a[k+2] & 0xff) * precision / 256;
                
                spectrum[Math.min(r, precision - 1)][Math.min(g, precision - 1)][Math.min(b, precision - 1)] += 1;
            }
        }
        
        return new Spectrum(spectrum, spectrumWidth, spectrumHeight);
    }



    public static BufferedImage loadImage(String filePath) throws IOException{
        RenderedImage image = JAI.create("fileload", filePath);
        
        return convertRenderedImage(image);
    }

    public static BufferedImage loadImage(InputStream stream) {
        SeekableStream sStream = SeekableStream.wrapInputStream(stream, true);
        RenderedImage image = JAI.create("stream", sStream);
        return convertRenderedImage(image);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static BufferedImage convertRenderedImage(RenderedImage img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage)img;  
        }   
        ColorModel cm = img.getColorModel();
        int width = img.getWidth();
        int height = img.getHeight();
        WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        Hashtable properties = new Hashtable();
        String[] keys = img.getPropertyNames();
        if (keys!=null) {
            for (int i = 0; i < keys.length; i++) {
                properties.put(keys[i], img.getProperty(keys[i]));
            }
        }
        BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
        img.copyData(raster);
        return result;
    }


}
