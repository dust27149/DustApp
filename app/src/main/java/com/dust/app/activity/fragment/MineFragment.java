package com.dust.app.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dust.app.activity.contract.MineContract;
import com.dust.app.activity.presenter.MinePresenter;
import com.dust.app.bean.UserInfo;
import com.dust.app.databinding.FragmentMineBinding;
import com.dust.app.utils.ReactUtils;

import java.util.List;

public class MineFragment extends Fragment implements MineContract.View {

    private MineContract.Presenter presenter;
    private FragmentMineBinding binding;
    private Activity activity;

    public MineFragment() {

    }

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        new MinePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMineBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvMineTemp.setOnClickListener(l -> presenter.getUserInfoList());
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
    public void setPresenter(MineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getUserInfoListSuccess(List<UserInfo> userInfoList) {
        // todo
    }

    @Override
    public void showToast(String s) {
        ReactUtils.showCustomToast(activity, s);
    }
}
