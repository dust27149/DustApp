package com.dust.app.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dust.app.activity.contract.SettingContract;
import com.dust.app.activity.presenter.SettingPresenter;
import com.dust.app.bean.Constant;
import com.dust.app.databinding.FragmentSettingBinding;
import com.dust.app.utils.SharedPreferencesUtils;
import com.dust.app.utils.SystemUtils;

public class SettingFragment extends Fragment implements SettingContract.View {

    private SettingContract.Presenter presenter;
    private FragmentSettingBinding binding;
    private Activity activity;

    public SettingFragment() {

    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        new SettingPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String server = SharedPreferencesUtils.getString("hostname", null);
        if (server == null) {
            binding.edtServer.setText(Constant.DEFAULT_HOSTNAME);
        } else {
            binding.edtServer.setText(server);
        }
        binding.btnBatteryOptimization.setOnClickListener(l -> {
            if (SystemUtils.isIgnoringBatteryOptimizations(activity)) {
                Toast.makeText(getActivity(), "电池已优化", Toast.LENGTH_SHORT).show();
            } else {
                SystemUtils.requestIgnoreBatteryOptimizations(activity);
            }
        });
        binding.btnBackgroundAuthority.setOnClickListener(l -> SystemUtils.intentToSetting(activity));
        binding.btnReset.setOnClickListener(l -> presenter.saveSettings(Constant.DEFAULT_HOSTNAME));
        binding.btnSave.setOnClickListener(l -> presenter.saveSettings(binding.edtServer.getText().toString()));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveSettingsSuccess() {
        activity.finish();
    }

}
