package net.mindengine.rainbow4j;

public class ImageCompareResult {

    private double percentage;
    private long totalPixels;

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
}
