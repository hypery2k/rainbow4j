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


import com.sun.media.jai.codec.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
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
        return compare(imageA, imageB, pixelSmooth, tolerance,
                new Rectangle(0, 0, imageA.getWidth(), imageA.getHeight()),
                new Rectangle(0, 0, imageB.getWidth(), imageB.getHeight()));
    }


    public static ImageCompareResult compare(BufferedImage imageA, BufferedImage imageB, int pixelSmooth, int tolerance, Rectangle areaA, Rectangle areaB) {
        if (tolerance < 0 ) {
            tolerance = 0;
        }

        if (areaA.width + areaA.x > imageA.getWidth() ||
            areaA.height + areaA.y > imageA.getHeight()) {
            throw new RuntimeException("Specified area is outside for original image");
        }
        if (areaB.width + areaB.x > imageB.getWidth() ||
                areaB.height + areaB.y > imageB.getHeight()) {
            throw new RuntimeException("Specified area is outside for secondary image");
        }

        BufferedImage comparisonMap = new BufferedImage(areaA.width, areaA.height, BufferedImage.TYPE_INT_RGB);

        int Cax = areaA.x;
        int Cay = areaA.y;

        int Cbx = areaB.x;
        int Cby = areaB.y;

        int Wa = areaA.width;
        int Ha = areaA.height;

        int Wb = areaB.width;
        int Hb = areaB.height;

        double Kx = ((double)Wb) / ((double)Wa);
        double Ky = ((double)Hb) / ((double)Ha);


        ImageNavigator navA = new ImageNavigator(imageA);
        ImageNavigator navB = new ImageNavigator(imageB);

        int x = 0, y = 0;

        double totalMismatchingPixels = 0;

        while(y < Ha) {
            while (x < Wa) {
                int xA = x + Cax;
                int yA = y + Cay;

                Color cA = navA.getSmoothedColor(Math.max(Cax, xA - pixelSmooth),
                                                Math.max(Cay, yA - pixelSmooth),
                                                Math.min(Cax + Wa, xA + pixelSmooth),
                                                Math.min(Cay + Ha, yA + pixelSmooth));

                int xB = (int)Math.round((((double)x) * Kx) + Cbx);
                int yB = (int)Math.round(((double)y) * Ky + Cby);

                xB = Math.min(xB, Cbx + Wb - 1);
                yB = Math.min(yB, Cby + Hb - 1);

                Color cB = navB.getSmoothedColor(Math.max(Cbx, xB - pixelSmooth),
                        Math.max(Cby, yB - pixelSmooth),
                        Math.min(Cbx + Wb, xB + pixelSmooth),
                        Math.min(Cby + Hb, yB + pixelSmooth));

                long colorError = ImageNavigator.colorDiff(cA, cB);
                if (colorError > tolerance) {
                    totalMismatchingPixels += 1;

                    int color = 0xff3333;
                    if (tolerance > 0) {
                        int level = (int) (colorError / tolerance);
                        if (level == 2) {
                            color = 0xFFEA00;
                        }
                        else if (level < 2) {
                            color = 0x00ff00;
                        }
                    }

                    comparisonMap.setRGB(x, y, color);
                }
                else {
                    comparisonMap.setRGB(x, y, 0x000000);
                }

                x += 1;
            }
            y += 1;
            x = 0;
        }

        ImageCompareResult result = new ImageCompareResult();

        double totalPixels = (Wa * Ha);
        result.setPercentage(100.0 * totalMismatchingPixels / totalPixels);

        result.setTotalPixels((long)totalMismatchingPixels);
        result.setComparisonMap(comparisonMap);
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

    public static void saveImage(BufferedImage image, File file) throws IOException {

        PNGEncodeParam pngEncode = new PNGEncodeParam() {
            @Override
            public void setBitDepth(int i) {

            }
            @Override
            public boolean isBackgroundSet() {
                return false;
            }
        };

        FileOutputStream out = new FileOutputStream(file);

        ImageEncoder encoder = ImageCodec.createImageEncoder("PNG", out, pngEncode);

        BufferedImage newImage = new BufferedImage( image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        newImage.createGraphics().drawImage( image, 0, 0, Color.BLACK, null);


        encoder.encode(newImage);
        out.close();
    }
}
