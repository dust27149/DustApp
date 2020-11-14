package com.dust.app.map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.modules.ArchiveFileFactory;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.OfflineTileProvider;
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.List;
import java.util.Set;

public class OpenStreetMap {

    private static final String TAG = OpenStreetMap.class.getSimpleName();
    private Context context;
    private MapView map;
    public MapEventListener mapEventListener;
    public MyLocationNewOverlay mLocationOverlay;

    public OpenStreetMap(Context context, MapView map) {
        this.context = context;
        this.map = map;
    }

    public void setOnMapClickListener(MapEventListener mapEventListener) {
        this.mapEventListener = mapEventListener;
    }

    public void initMap() {
        initMap(new MapConfig());
    }

    public void initMap(MapConfig config) {
        map.setZoomRounding(false);
        map.setMaxZoomLevel(config.maxZoomLevel);
        map.setMinZoomLevel(config.minZoomLevel);
        map.setTileSource(config.tileBase);
        map.setMultiTouchControls(config.enableMultiTouchControls);
        if (config.tileOverlay != null) {
            addLayer(config.tileOverlay);
        }
        if (config.showScaleBar) {
            addScaleBar();
        }
        if (config.showMyLocation) {
            mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), map);
            showMyLocation();
        }
        if (config.showZoomController) {
            map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        } else {
            map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        }
        zoomToPosition(config.centerLongitude, config.centerLatitude, config.zoomLevel);
        map.getOverlayManager().add(new MapEventsOverlay());
    }

    public void addLayer(OnlineTileSourceBase sourceBase) {
        MapTileProviderBasic provider = new MapTileProviderBasic(context, sourceBase);
        TilesOverlay layer = new TilesOverlay(provider, context);
        layer.setLoadingBackgroundColor(Color.TRANSPARENT);
        layer.setLoadingLineColor(Color.TRANSPARENT);
        map.getOverlayManager().add(layer);
    }

    public void showMyLocation() {
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);
    }

    public void zoomToMyLocation() {
        GeoPoint location = mLocationOverlay.getMyLocation();
        zoomToPosition(location.getLongitude(), location.getLatitude(), 14);
    }

    public void addScaleBar() {
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setAlignBottom(true);
        mScaleBarOverlay.setScaleBarOffset(10, 10);
        map.getOverlays().add(mScaleBarOverlay);
    }

    public void addMarkers(GeoPoint geoPoint, Drawable drawable) {
        Marker marker = new Marker(map);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setIcon(drawable);
        map.getOverlays().add(marker);
    }

    public void zoomToPosition(double longitude, double latitude, double zoomLevel) {
        map.getController().setZoom(zoomLevel);
        map.getController().setCenter(new GeoPoint(latitude, longitude));
    }

    public void loadFile(File file) throws Exception {
        if (file.exists()) {
            File[] list = file.listFiles();
            if (list != null) {
                for (File f : list) {
                    if (f.isDirectory()) {
                        continue;
                    }
                    String name = f.getName().toLowerCase();
                    if (!name.contains(".")) {
                        continue; //skip files without an extension
                    }
                    name = name.substring(name.lastIndexOf(".") + 1);
                    if (name.length() == 0) {
                        continue;
                    }
                    if (ArchiveFileFactory.isFileExtensionRegistered(name)) {
                        try {
                            OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(context), new File[]{f});
                            if (name.contains("mbtiles")) {
                                TilesOverlay layer = new TilesOverlay(tileProvider, context);
                                layer.setLoadingBackgroundColor(Color.TRANSPARENT);
                                layer.setLoadingLineColor(Color.TRANSPARENT);
                                map.getOverlayManager().add(layer);
                            } else {
                                String source;
                                IArchiveFile[] archives = tileProvider.getArchives();
                                if (archives.length > 0) {
                                    Set<String> tileSources = archives[0].getTileSources();
                                    if (!tileSources.isEmpty()) {
                                        source = tileSources.iterator().next();
                                        tileProvider.setTileSource(FileBasedTileSource.getSource(source));
                                        TilesOverlay layer = new TilesOverlay(tileProvider, context);
                                        layer.setLoadingBackgroundColor(Color.TRANSPARENT);
                                        layer.setLoadingLineColor(Color.TRANSPARENT);
                                        map.getOverlayManager().add(layer);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            throw new Exception(file.getAbsolutePath() + " did not have any files I can open! Try using MOBAC");
                        }
                    }
                }
            } else {
                throw new Exception(file.getAbsolutePath() + " did not have any files I can open! Try using MOBAC");
            }
        } else {
            throw new Exception(file.getAbsolutePath() + " dir not found!");
        }

    }

    public void loadMbTiles(File file) throws Exception {
        OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(context), new File[]{file});
        TilesOverlay layer = new TilesOverlay(tileProvider, context);
        layer.setLoadingBackgroundColor(Color.TRANSPARENT);
        layer.setLoadingLineColor(Color.TRANSPARENT);
        map.getOverlayManager().add(layer);
    }

    public void loadArchives(File file) throws Exception {
        OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(context), new File[]{file});
        IArchiveFile[] archives = tileProvider.getArchives();
        if (archives.length > 0) {
            Set<String> tileSources = archives[0].getTileSources();
            if (!tileSources.isEmpty()) {
                String source = tileSources.iterator().next();
                tileProvider.setTileSource(FileBasedTileSource.getSource(source));
                TilesOverlay layer = new TilesOverlay(tileProvider, context);
                layer.setLoadingBackgroundColor(Color.TRANSPARENT);
                layer.setLoadingLineColor(Color.TRANSPARENT);
                map.getOverlayManager().add(layer);
            }
        }
    }

    public void loadShapeFile(File file) throws Exception {
        List<Overlay> folder = CustomShapeFileConverter.convert(map, file);
        map.getOverlayManager().addAll(folder);
        map.invalidate();
    }

    private class MapEventsOverlay extends Overlay {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
            if (mapEventListener != null) {
                mapEventListener.onTap();
            }
            return super.onSingleTapConfirmed(e, mapView);
        }

        @Override
        public boolean onLongPress(MotionEvent e, MapView mapView) {
            if (mapEventListener != null) {
                Projection projection = mapView.getProjection();
                GeoPoint geoPoint = (GeoPoint) projection.fromPixels((int) e.getX(), (int) e.getY());
                mapEventListener.onLongPress(geoPoint.getLongitude(), geoPoint.getLatitude());
            }
            return true;
        }

    }

}
