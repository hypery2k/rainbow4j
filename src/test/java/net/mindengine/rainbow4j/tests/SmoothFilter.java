package net.mindengine.rainbow4j.tests;

import net.mindengine.rainbow4j.ImageFilter;
import net.mindengine.rainbow4j.ImageHandler;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;

/**
 * Created by ishubin on 2014/09/14.
 */
public class SmoothFilter implements ImageFilter {
    private int radius;

    public SmoothFilter(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void apply(byte[] bytes, int width, int height, Rectangle area) {
        if (area.width + area.x > width || area.height + area.y > height) {
            throw new RuntimeException("Specified area is outside of image");
        }

        byte[] copyBytes = ArrayUtils.clone(bytes);

        ImageHandler filter = new ImageHandler(copyBytes, width, height);

        for (int y = area.y; y < area.height; y++) {
            for (int x = area.x; x < area.width; x++) {
                Color smoothedColor = filter.getSmoothedColor(Math.max(x - radius, area.x),
                        Math.max(y - radius, area.y),
                        Math.min(x + radius, area.x + area.width),
                        Math.min(y + radius, area.y + area.height));

                int k = y * width * 3 + x * 3;
                bytes[k] = (byte)(smoothedColor.getRed() & 0xff);
                bytes[k+1] = (byte)(smoothedColor.getGreen() & 0xff);
                bytes[k+2] = (byte)(smoothedColor.getBlue() & 0xff);
            }
        }
    }
}