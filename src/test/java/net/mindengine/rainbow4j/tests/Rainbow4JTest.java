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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import net.mindengine.rainbow4j.ColorDistribution;
import net.mindengine.rainbow4j.ImageCompareResult;
import net.mindengine.rainbow4j.Rainbow4J;
import net.mindengine.rainbow4j.Spectrum;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class Rainbow4JTest {


    @Test
    public void shouldRead_imageSpectrum_withCustomPrecision() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/test-spectrum-black-white-1.png").getFile());

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
    
    @Test(enabled = false)
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
    public void shouldRead_image_fromStream() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResourceAsStream("/color-scheme-image-1.png"));

        Assert.assertEquals(image.getWidth(), 778);
        Assert.assertEquals(image.getHeight(), 392);
    }
    
    @Test
    public void shouldRead_imageSpectrum_fromPNG_2() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/color-scheme-image-1.png").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image);

        Assert.assertEquals((int)spectrum.getPercentage(58, 112, 208, 5), 8);
        Assert.assertEquals((int)spectrum.getPercentage(207, 71, 29, 5), 32);
    }
    
    @Test(enabled = false)
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

    @Test
    public void shouldReadSpectrum_fromPNG_3() throws IOException{
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/color-scheme-image-2.png").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image, new Rectangle(48, 217, 344, 407));

        Assert.assertEquals((int)spectrum.getPercentage(170, 170, 170, 5), 2);
        Assert.assertEquals((int)spectrum.getPercentage(119, 119, 119, 5), 1);
        Assert.assertEquals((int)spectrum.getPercentage(255, 255, 255, 5), 95);
    }
    
    @Test
    public void shouldGive_colorDistribution() throws IOException {
        BufferedImage image = Rainbow4J.loadImage(getClass().getResource("/color-scheme-image-1.png").getFile());

        Spectrum spectrum = Rainbow4J.readSpectrum(image);
        
        List<ColorDistribution> colors = spectrum.getColorDistribution(3);
        
        Assert.assertEquals(colors.size(), 4);
        
        Assert.assertEquals(colors.get(0).getColor(), new Color(0, 0, 0));
        Assert.assertEquals((int)colors.get(0).getPercentage(), 14);
        
        Assert.assertEquals(colors.get(1).getColor(), new Color(58, 112, 207));
        Assert.assertEquals((int)colors.get(1).getPercentage(), 8);
        
        Assert.assertEquals(colors.get(2).getColor(), new Color(207, 71, 29));
        Assert.assertEquals((int)colors.get(2).getPercentage(), 32);
        
        Assert.assertEquals(colors.get(3).getColor(), new Color(255, 255, 255));
        Assert.assertEquals((int)colors.get(3).getPercentage(), 44);
    }



    @Test(dataProvider = "imageCompareProvider")
    public void shouldCompare_images(int pixelSmooth, double approxPercentage, long expectedTotalPixels) throws IOException {
        BufferedImage imageA = Rainbow4J.loadImage(getClass().getResource("/comp-image-1.jpg").getFile());
        BufferedImage imageB = Rainbow4J.loadImage(getClass().getResource("/comp-image-2.jpg").getFile());

        ImageCompareResult diff = Rainbow4J.compare(imageA, imageB, pixelSmooth, 1);

        assertThat(diff.getPercentage(), is(greaterThan(approxPercentage - 0.02)));
        assertThat(diff.getPercentage(), is(lessThan(approxPercentage + 0.02)));

        assertThat(diff.getTotalPixels(), is(expectedTotalPixels));
    }

    @Test
    public void shouldCompare_sameImages_ofDifferentSizes() throws IOException {
        BufferedImage imageA = Rainbow4J.loadImage(getClass().getResource("/comp-image-1.jpg").getFile());
        BufferedImage imageB = Rainbow4J.loadImage(getClass().getResource("/comp-image-1-scaled-down.jpg").getFile());

        ImageCompareResult diff = Rainbow4J.compare(imageA, imageB, 0, 10);

        assertThat(diff.getTotalPixels(), is(lessThan(5700L)));
        assertThat(diff.getPercentage(), is(lessThan(2.26)));
    }

    @Test
    public void shouldCompare_differentImages_withDifferentSizes() throws IOException {
        BufferedImage imageA = Rainbow4J.loadImage(getClass().getResource("/comp-image-1.jpg").getFile());
        BufferedImage imageB = Rainbow4J.loadImage(getClass().getResource("/comp-image-3-scaled-down.jpg").getFile());

        ImageCompareResult diff = Rainbow4J.compare(imageA, imageB, 0, 10);

        assertThat(diff.getTotalPixels(), is(greaterThan(13800L)));
        assertThat(diff.getTotalPixels(), is(lessThan(14000L)));

        assertThat(diff.getPercentage(), is(greaterThan(5.4)));
        assertThat(diff.getPercentage(), is(lessThan(5.6)));
    }

    @Test
    public void shouldCompare_images_withOnlyPartialRegions() throws IOException {
        BufferedImage imageA = Rainbow4J.loadImage(getClass().getResource("/page-screenshot.png").getFile());
        BufferedImage imageB = Rainbow4J.loadImage(getClass().getResource("/page-sample-correct.png").getFile());


        ImageCompareResult diff = Rainbow4J.compare(imageA, imageB, 0, 2, new Rectangle(100, 90, 100, 40), new Rectangle(40, 40, 100, 40));

        assertThat(diff.getTotalPixels(), is(lessThan(2L)));
        assertThat(diff.getPercentage(), is(lessThan(0.01)));
    }


    @DataProvider
    public Object[][] imageCompareProvider() {
        return new Object[][] {
                //pixelsmooth,  approx percentage, total pixels
                {0, 0.57, 1436L},
                {1, 0.85, 2148},
                {2, 1.0116, 2529},
                {10, 1.95, 4896}
        };
    }


}
