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

import net.mindengine.rainbow4j.Rainbow4J;
import net.mindengine.rainbow4j.Spectrum;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;


public class Rainbow4JTests {

    @Test
    public void shouldRead_imageSpectrum_fromJPEG() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResourceAsStream("/test-spectrum-black-white-1.jpg"));

        Spectrum spectrum = Rainbow4J.readSpectrum(image);
        
        Assert.assertEquals(spectrum.getPrecision(), 256);

        Assert.assertEquals((int)spectrum.getPercentage(255,255,255, 0), 67);
        Assert.assertEquals((int) spectrum.getPercentage(0, 0, 0, 0), 30);
        Assert.assertEquals((int) spectrum.getPercentage(128, 128, 128, 0), 0);

        Assert.assertEquals((int)spectrum.getPercentage(254,254,254, 0), 0);
        Assert.assertEquals((int)spectrum.getPercentage(254,254,254, 1), 68);
        Assert.assertEquals((int)spectrum.getPercentage(254,250,254, 10), 68);
    }
    
    @Test
    public void shouldRead_imageSpectrum_withCustomPrecision() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResourceAsStream("/test-spectrum-black-white-1.jpg"));

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
    public void shouldCropImage_andReadSpectrum() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/test-spectrum-black-white-1.png").getFile());

        image = Rainbow4J.crop(image, 100, 100, 50, 50);

        Spectrum spectrum = Rainbow4J.readSpectrum(image);

        Assert.assertEquals((int)spectrum.getPercentage(255,255,255, 0), 100);
        Assert.assertEquals((int)spectrum.getPercentage(0, 0, 0, 0), 0);
        Assert.assertEquals((int)spectrum.getPercentage(128,128,128, 0), 0);
    }

}
