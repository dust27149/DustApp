package com.dust.app.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dust.app.activity.contract.MapContract;
import com.dust.app.activity.presenter.MapPresenter;
import com.dust.app.databinding.FragmentMapBinding;
import com.dust.app.map.CustomTileSource;
import com.dust.app.map.MapConfig;
import com.dust.app.map.MapEventListener;
import com.dust.app.map.OpenStreetMap;
import com.dust.app.utils.ReactUtils;

import java.io.File;

public class MapFragment extends Fragment implements MapContract.View {

    private final String TAG = MapFragment.class.getSimpleName();
    private MapContract.Presenter presenter;
    private FragmentMapBinding binding;
    private Activity activity;
    private OpenStreetMap map;

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
        binding.ibSatelliteMap.setOnClickListener(l -> {
            try {
                map.loadFile(new File(activity.getExternalFilesDir("/resources/map/mbtiles"), ""));
            } catch (Exception e) {
                Log.e(TAG, "加载失败：" + e.getLocalizedMessage());
                showToast("加载失败");
            }
        });
        binding.ibRoad.setOnClickListener(l -> {
            if (binding.ibRoad.isChecked()) {
                try {
                    String[] shapeFileName = {"boundaries.shp", "grass.shp", "water.shp", "roads.shp", "gates.shp"};
                    for (String s : shapeFileName) {
                        map.loadShapeFile(new File(activity.getExternalFilesDir("/resources/map/shape_files"), s));
                    }
                    map.loadArchives(new File(activity.getExternalFilesDir("/resources/map/sqlite"), "DOM.sqlite"));
                    map.loadArchives(new File(activity.getExternalFilesDir("/resources/map/sqlite"), "counters.sqlite"));
                } catch (Exception e) {
                    Log.e(TAG, "加载失败：" + e.getLocalizedMessage());
                    showToast("加载失败");
                }
            } else {
                binding.map.getOverlays().clear();
            }
        });
        binding.ibLocate.setOnClickListener(l -> map.zoomToMyLocation());
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
    public void getAddressSuccess(String address) {
        showToast(address);
    }

    @Override
    public void getAddressFailed() {
        showToast("获取地址失败");
    }

    @Override
    public void showToast(String s) {
        ReactUtils.showCustomToast(activity, s);
    }

    private void initMap() {
        map = new OpenStreetMap(activity, binding.map);
        MapConfig config = new MapConfig();
        config.tileBase = CustomTileSource.GoogleSatWGS84;
        config.tileOverlay = CustomTileSource.tianDiTuRoadTileSource;
        map.initMap(config);
        map.setOnMapClickListener(new MapEventListener() {
            @Override
            public void onTap() {
                if (binding.svSearch.getVisibility() == View.VISIBLE) {
                    binding.svSearch.setVisibility(View.INVISIBLE);
                    binding.ibSatelliteMap.setVisibility(View.INVISIBLE);
                    binding.ibRoad.setVisibility(View.INVISIBLE);
                    binding.ibLocate.setVisibility(View.INVISIBLE);
                } else {
                    binding.svSearch.setVisibility(View.VISIBLE);
                    binding.ibSatelliteMap.setVisibility(View.VISIBLE);
                    binding.ibRoad.setVisibility(View.VISIBLE);
                    binding.ibLocate.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLongPress(double longitude, double latitude) {
                presenter.getAddress(longitude, latitude);
            }
        });
    }

}
