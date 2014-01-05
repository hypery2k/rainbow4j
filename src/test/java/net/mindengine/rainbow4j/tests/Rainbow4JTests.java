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
package net.mindengine.rainbow4j.tests;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.mindengine.rainbow4j.Rainbow4J;
import net.mindengine.rainbow4j.Spectrum;

import org.testng.Assert;
import org.testng.annotations.Test;


public class Rainbow4JTests {

    @Test
    public void shouldRead_imageSpectrum_withCustomPrecision() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/test-spectrum-black-white-1.jpg").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image, 64);
        
        Assert.assertEquals(spectrum.getPrecision(), 64);

        Assert.assertEquals((int)spectrum.getPercentage(255,255,255, 0), 68);
        Assert.assertEquals((int) spectrum.getPercentage(0, 0, 0, 0), 31);
        Assert.assertEquals((int) spectrum.getPercentage(128, 128, 128, 0), 0);

        Assert.assertEquals((int)spectrum.getPercentage(254,254,254, 0), 68);
        Assert.assertEquals((int)spectrum.getPercentage(253,253,253, 0), 68);
        Assert.assertEquals((int)spectrum.getPercentage(254,254,254, 5), 68);
        Assert.assertEquals((int)spectrum.getPercentage(254,250,254, 10), 68);
    }
    

    @Test
    public void shouldRead_imageSpectrum_fromPNG() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/test-spectrum-black-white-1.png").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image);

        Assert.assertEquals((int)spectrum.getPercentage(255,255,255, 0), 67);
        Assert.assertEquals((int)spectrum.getPercentage(0, 0, 0, 0), 30);
        Assert.assertEquals((int)spectrum.getPercentage(128,128,128, 0), 0);

        Assert.assertEquals((int)spectrum.getPercentage(254,254,254, 0), 0);
        Assert.assertEquals((int)spectrum.getPercentage(254,254,254, 1), 68);
        Assert.assertEquals((int)spectrum.getPercentage(254,250,254, 10), 68);
    }
    
    @Test
    public void shouldRead_imageSpectrum_fromJPG() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/test-spectrum-black-white-1.jpg").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image);

        Assert.assertEquals((int)spectrum.getPercentage(255,255,255, 0), 67);
        Assert.assertEquals((int)spectrum.getPercentage(0, 0, 0, 0), 30);
        Assert.assertEquals((int)spectrum.getPercentage(128,128,128, 0), 0);

        Assert.assertEquals((int)spectrum.getPercentage(254,254,254, 0), 0);
        Assert.assertEquals((int)spectrum.getPercentage(254,254,254, 1), 68);
        Assert.assertEquals((int)spectrum.getPercentage(254,250,254, 10), 68);
    }
    
    
    @Test
    public void shouldRead_imageSpectrum_fromPNG_2() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/color-scheme-image-1.png").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image);

        Assert.assertEquals((int)spectrum.getPercentage(58, 112, 208, 5), 8);
        Assert.assertEquals((int)spectrum.getPercentage(207, 71, 29, 5), 32);
    }
    
    @Test
    public void shouldRead_imageSpectrum_fromJPG_2() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/color-scheme-image-1.jpg").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image);

        Assert.assertEquals((int)spectrum.getPercentage(58, 112, 208, 5), 8);
        Assert.assertEquals((int)spectrum.getPercentage(207, 71, 29, 5), 32);
    }
    
    
    
    @Test
    public void shouldReadSpectrum_fromSpecifiedRegion() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/test-spectrum-black-white-1.png").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image, new Rectangle(100, 200, 20, 20));

        Assert.assertEquals((int)spectrum.getPercentage(255 ,255, 255, 0), 0);
        Assert.assertEquals((int)spectrum.getPercentage(0, 0, 0, 0), 100);
        Assert.assertEquals((int)spectrum.getPercentage(128,128,128, 0), 0);
    }
    
    @Test
    public void shouldCropImage_andReadSpectrum_2() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/color-scheme-image-1.png").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image, new Rectangle(10, 10, 30, 20));
        
        Assert.assertEquals((int)spectrum.getPercentage(255 ,255, 255, 0), 100);
        Assert.assertEquals((int)spectrum.getPercentage(0, 0, 0, 0), 0);
        Assert.assertEquals((int)spectrum.getPercentage(128,128,128, 0), 0);
    }

}
