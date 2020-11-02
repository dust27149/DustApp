package com.dust.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dust.app.databinding.FragmentTaskBinding;
import com.dust.app.fragment.contract.TaskContract;
import com.dust.app.fragment.presenter.TaskPresenter;

public class TaskFragment extends Fragment implements TaskContract.View {

    private TaskContract.Presenter presenter;
    private FragmentTaskBinding binding;
    private Activity activity;

    public TaskFragment() {

    }

    public static TaskFragment newInstance() {
        return new TaskFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        new TaskPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void setPresenter(TaskContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }
}
