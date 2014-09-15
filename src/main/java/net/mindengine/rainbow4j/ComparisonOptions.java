package net.mindengine.rainbow4j;

import java.util.LinkedList;
import java.util.List;

public class ComparisonOptions {
    private int tolerance;
    private boolean stretchToFit = false;

    private List<ImageFilter> filters = new LinkedList<ImageFilter>();
    private List<ImageFilter> mapFilters = new LinkedList<ImageFilter>();

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

    public int getTolerance() {
        return tolerance;
    }

    public boolean isStretchToFit() {
        return stretchToFit;
    }

    public void setStretchToFit(boolean stretchToFit) {
        this.stretchToFit = stretchToFit;
    }


    public List<ImageFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<ImageFilter> filters) {
        this.filters = filters;
    }

    public void addFilter(ImageFilter filter) {
        if (filters == null) {
            filters = new LinkedList<ImageFilter>();
        }
        filters.add(filter);
    }

    public List<ImageFilter> getMapFilters() {
        return mapFilters;
    }

    public void setMapFilters(List<ImageFilter> mapFilters) {
        this.mapFilters = mapFilters;
    }

    public void addMapFilter(ImageFilter imageFilter) {
        if (mapFilters == null) {
            mapFilters = new LinkedList<ImageFilter>();
        }

        mapFilters.add(imageFilter);
    }
}
