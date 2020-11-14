package com.dust.app.map;

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

public class MapConfig {
    public double zoomLevel = 14.0;
    public double maxZoomLevel = 20.0;
    public double minZoomLevel = 3.0;
    public double centerLongitude = 116.3912244342;
    public double centerLatitude = 39.9062486752;
    public boolean enableMultiTouchControls = true;
    public boolean showScaleBar = false;
    public boolean showMyLocation = true;
    public boolean showZoomController = false;
    public OnlineTileSourceBase tileBase = CustomTileSource.GoogleSatWGS84;
    public OnlineTileSourceBase tileOverlay;
}
