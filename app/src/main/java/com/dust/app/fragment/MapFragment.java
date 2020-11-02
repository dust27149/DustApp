package com.dust.app.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dust.app.bean.Constant;
import com.dust.app.databinding.FragmentMapBinding;
import com.dust.app.fragment.contract.MapContract;
import com.dust.app.fragment.presenter.MapPresenter;
import com.dust.app.map.CustomShapeFileConverter;
import com.dust.app.map.CustomTileSource;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.modules.ArchiveFileFactory;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.OfflineTileProvider;
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.List;
import java.util.Set;

import static com.dust.app.map.CustomTileSource.tianDiTuRoadTileSource;

public class MapFragment extends Fragment implements MapContract.View {

    private final String TAG = MapFragment.class.getSimpleName();
    private MapContract.Presenter presenter;
    private FragmentMapBinding binding;
    private Activity activity;

    private IMapController mapController;

    public MapFragment() {

    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        new MapPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentMapBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();
        binding.svSearch.setOnClickListener(l -> {

        });
        binding.ibSatelliteMap.setOnClickListener(l -> loadFile(new File(activity.getExternalFilesDir("/resources/map/mbtiles"), "")));
        binding.ibRoad.setOnClickListener(l -> {
            if (binding.ibRoad.isChecked()) {
                String[] shapeFileName = {"boundaries.shp", "grass.shp", "water.shp", "roads.shp", "gates.shp"};
                for (String s : shapeFileName) {
                    loadShapeFile(new File(activity.getExternalFilesDir("/resources/map/shape_files"), s));
                }
                loadArchives(new File(activity.getExternalFilesDir("/resources/map/sqlite"), "DOM.sqlite"));
                loadArchives(new File(activity.getExternalFilesDir("/resources/map/sqlite"), "counters.sqlite"));
            } else {
                binding.map.getOverlays().clear();
            }
        });
        binding.ibLocate.setOnClickListener(l -> {
            try {
                zoomToPosition(Constant.location.getLongitude(), Constant.location.getLatitude(), 18.0);
            } catch (Exception e) {
                showToast("无法获取当前位置！");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
        binding.map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
        binding.map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

    private void initMap() {

        initMapConfig();
        zoomToPosition(116.3912244342, 39.9062486752, 14.0);

        MapTileProviderBasic provider = new MapTileProviderBasic(activity, tianDiTuRoadTileSource);
        TilesOverlay layer = new TilesOverlay(provider, activity);
        layer.setLoadingBackgroundColor(Color.TRANSPARENT);
        layer.setLoadingLineColor(Color.TRANSPARENT);
        binding.map.getOverlayManager().add(layer);

        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(activity), binding.map);
        mLocationOverlay.enableMyLocation();
        binding.map.getOverlays().add(mLocationOverlay);

    }

    private void initMapConfig() {
        binding.map.setZoomRounding(false);
        binding.map.setMaxZoomLevel(20.0);
        binding.map.setMinZoomLevel(3.0);
        binding.map.setTileSource(CustomTileSource.GoogleSatWGS84);
        binding.map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        binding.map.setMultiTouchControls(true);
        mapController = binding.map.getController();

        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(binding.map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setAlignBottom(true);
        mScaleBarOverlay.setScaleBarOffset(10, 10);
        binding.map.getOverlays().add(mScaleBarOverlay);
    }

    private void addMarkers(GeoPoint geoPoint, Drawable drawable) {
        Marker marker = new Marker(binding.map);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setIcon(drawable);
        binding.map.getOverlays().add(marker);
    }

    private void zoomToPosition(double longitude, double latitude, double zoomLevel) {
        mapController.setZoom(zoomLevel);
        mapController.setCenter(new GeoPoint(latitude, longitude));
    }

    private void loadFile(File file) {

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
                            OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(activity), new File[]{f});
                            if (name.contains("mbtiles")) {
                                TilesOverlay layer = new TilesOverlay(tileProvider, activity);
                                layer.setLoadingBackgroundColor(Color.TRANSPARENT);
                                layer.setLoadingLineColor(Color.TRANSPARENT);
                                binding.map.getOverlayManager().add(layer);
                            } else {
                                String source;
                                IArchiveFile[] archives = tileProvider.getArchives();
                                if (archives.length > 0) {
                                    Set<String> tileSources = archives[0].getTileSources();
                                    if (!tileSources.isEmpty()) {
                                        source = tileSources.iterator().next();
                                        tileProvider.setTileSource(FileBasedTileSource.getSource(source));
                                        TilesOverlay layer = new TilesOverlay(tileProvider, activity);
                                        layer.setLoadingBackgroundColor(Color.TRANSPARENT);
                                        layer.setLoadingLineColor(Color.TRANSPARENT);
                                        binding.map.getOverlayManager().add(layer);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e(TAG, ex.toString());
                        }
                    }
                }
            } else {
                Log.e(TAG, file.getAbsolutePath() + " did not have any files I can open! Try using MOBAC");
                Toast.makeText(activity, file.getAbsolutePath() + " did not have any files I can open! Try using MOBAC", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, file.getAbsolutePath() + " dir not found!");
            Toast.makeText(activity, file.getAbsolutePath() + " dir not found!", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadMbTiles(File file) {
        try {
            OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(activity), new File[]{file});
            TilesOverlay layer = new TilesOverlay(tileProvider, activity);
            layer.setLoadingBackgroundColor(Color.TRANSPARENT);
            layer.setLoadingLineColor(Color.TRANSPARENT);
            binding.map.getOverlayManager().add(layer);
//            binding.map.invalidate();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void loadArchives(File file) {
        try {
            OfflineTileProvider tileProvider = new OfflineTileProvider(new SimpleRegisterReceiver(activity), new File[]{file});
            IArchiveFile[] archives = tileProvider.getArchives();
            if (archives.length > 0) {
                Set<String> tileSources = archives[0].getTileSources();
                if (!tileSources.isEmpty()) {
                    String source = tileSources.iterator().next();
                    tileProvider.setTileSource(FileBasedTileSource.getSource(source));
                    TilesOverlay layer = new TilesOverlay(tileProvider, activity);
                    layer.setLoadingBackgroundColor(Color.TRANSPARENT);
                    layer.setLoadingLineColor(Color.TRANSPARENT);
                    binding.map.getOverlayManager().add(layer);
                }
            }
//            binding.map.invalidate();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void loadShapeFile(File file) {
        try {
            List<Overlay> folder = CustomShapeFileConverter.convert(binding.map, file);
            binding.map.getOverlayManager().addAll(folder);
            binding.map.invalidate();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
