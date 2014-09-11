package net.mindengine.rainbow4j;

import java.awt.image.BufferedImage;

public class ImageCompareResult {

    private double percentage;
    private long totalPixels;
    private BufferedImage comparisonMap;

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setTotalPixels(long totalPixels) {
        this.totalPixels = totalPixels;
    }

    public long getTotalPixels() {
        return totalPixels;
    }

    public BufferedImage getComparisonMap() {
        return comparisonMap;
    }

    public void setComparisonMap(BufferedImage comparisonMap) {
        this.comparisonMap = comparisonMap;
    }
}
