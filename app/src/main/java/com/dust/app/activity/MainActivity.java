package com.dust.app.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dust.app.R;
import com.dust.app.databinding.ActivityMainBinding;
import com.dust.app.fragment.InfoFragment;
import com.dust.app.fragment.MapFragment;
import com.dust.app.fragment.MineFragment;
import com.dust.app.fragment.TaskFragment;
import com.dust.app.service.GPSService;
import com.dust.app.service.MQTTService;
import com.tbruyelle.rxpermissions3.RxPermissions;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final RxPermissions rxPermissions = new RxPermissions(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        startService();
    }

    private void initView() {
        InfoFragment infoFragment = InfoFragment.newInstance();
        MapFragment mapFragment = MapFragment.newInstance();
        MineFragment mineFragment = MineFragment.newInstance();
        TaskFragment taskFragment = TaskFragment.newInstance();

        getSupportFragmentManager().beginTransaction().add(R.id.frame_main, mineFragment).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_main, taskFragment).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_main, infoFragment).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_main, mapFragment).addToBackStack(null).commit();
        binding.tvMap.setSelected(true);

        binding.tvInfo.setOnClickListener(l -> {
            resetSelect();
            binding.tvInfo.setSelected(true);
            getSupportFragmentManager().beginTransaction().show(infoFragment).hide(mapFragment).hide(mineFragment).hide(taskFragment).commit();
        });
        binding.tvMap.setOnClickListener(l -> {
            resetSelect();
            binding.tvMap.setSelected(true);
            getSupportFragmentManager().beginTransaction().show(mapFragment).hide(infoFragment).hide(mineFragment).hide(infoFragment).commit();
        });
        binding.tvMine.setOnClickListener(l -> {
            resetSelect();
            binding.tvMine.setSelected(true);
            getSupportFragmentManager().beginTransaction().show(mineFragment).hide(taskFragment).hide(mapFragment).hide(infoFragment).commit();
        });
        binding.tvTask.setOnClickListener(l -> {
            resetSelect();
            binding.tvTask.setSelected(true);
            getSupportFragmentManager().beginTransaction().show(taskFragment).hide(infoFragment).hide(mapFragment).hide(mineFragment).commit();
        });

    }

    private void startService() {
        String[] permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        } else {
            permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        }
        Disposable disposable = rxPermissions.request(permission)
                .subscribe(granted -> {
                    Log.e(TAG, granted ? "已授权" : "未授权");
                    if (granted) {
                        Intent gpsIntent = new Intent(getApplicationContext(), GPSService.class);
                        startService(gpsIntent);
                    } else {
                        new AlertDialog.Builder(this)
                                .setCancelable(true)
                                .setTitle("权限不足")
                                .setMessage("定位权限不足！\r\n请先设置定位权限为始终允许，再重启APP。")
                                .setNegativeButton("取消", (dialog, which) -> {

                                })
                                .setPositiveButton("去设置", (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                    finish();
                                })
                                .create()
                                .show();
                    }
                });
        compositeDisposable.add(disposable);
        Intent mqttIntent = new Intent(getApplicationContext(), MQTTService.class);
        startService(mqttIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void resetSelect() {
        binding.tvTask.setSelected(false);
        binding.tvMine.setSelected(false);
        binding.tvMap.setSelected(false);
        binding.tvInfo.setSelected(false);
    }
}
