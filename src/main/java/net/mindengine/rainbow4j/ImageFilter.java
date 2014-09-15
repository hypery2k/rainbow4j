package net.mindengine.rainbow4j;

import java.awt.*;

public interface ImageFilter {
    public void apply(byte[] bytes, int width, int height, Rectangle area);
}
