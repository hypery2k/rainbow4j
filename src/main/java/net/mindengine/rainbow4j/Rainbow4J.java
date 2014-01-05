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
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Hashtable;

import javax.media.jai.JAI;
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
     * @throws MagickException 
     */
    public static Spectrum readSpectrum(BufferedImage image, int precision) throws IOException {
        
        if (precision < 8) throw new IllegalArgumentException("Color size should not be less then 8");
        if (precision > 256) throw new IllegalArgumentException("Color size should not be bigger then 256");
        
        int spectrum[][][] = new int[precision][precision][precision];

        int width = image.getWidth();
        int height = image.getHeight();
        
        byte[] a = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        boolean hasAlphaChannel = image.getColorModel().hasAlpha();
        int k = 0;
        int r,g,b;
        
        
        for (int i = width*height - 1; i >= 0; i--) {
            if (hasAlphaChannel) {
                k += 1; //skipping alpha channel
            }
           
            r = (int)(a[k] & 0xff) * precision / 256;
            g = (int)(a[k+1] & 0xff) * precision / 256;
            b = (int)(a[k+2] & 0xff) * precision / 256;
            
            spectrum[Math.min(r, precision - 1)][Math.min(g, precision - 1)][Math.min(b, precision - 1)] += 1;
            
            k+=3;
        }
        
        
        int pointsnum = 0;
        for (int i=0;i<precision; i++) {
            for (int j=0; j<precision; j++) {
                for(int z=0; z<precision; z++) {
                    pointsnum += spectrum[i][j][z];
                    if (spectrum[i][j][z] > 300) {
                        System.out.println(String.format("(%d, %d, %d) = %d", i,j,z,spectrum[i][j][z]));
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Total = " + pointsnum);
        System.out.println("width*height = " + width*height);
        return new Spectrum(spectrum, width, height);
    }

    public static BufferedImage loadImage(String filePath) throws IOException{
        RenderedImage image = JAI.create("fileload", filePath);
        
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


    public static BufferedImage crop(BufferedImage image, int x, int y, int w, int h) {
        return image.getSubimage(x, y, w, h);
    }
        
}
