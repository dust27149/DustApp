package com.dust.app.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dust.app.activity.MainActivity;
import com.dust.app.activity.contract.PwdLoginContract;
import com.dust.app.activity.presenter.PwdLoginPresenter;
import com.dust.app.databinding.FragmentPwdLoginBinding;

public class PwdLoginFragment extends Fragment implements PwdLoginContract.View {

    private PwdLoginContract.Presenter presenter;
    private FragmentPwdLoginBinding binding;
    private Activity activity;

    public PwdLoginFragment() {

    }

    public static PwdLoginFragment newInstance() {
        return new PwdLoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        new PwdLoginPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPwdLoginBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.edtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.btnLogin.setEnabled(s.length() > 0 && binding.edtPassword.getText().length() >= 6);
            }
        });
        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.btnLogin.setEnabled(binding.edtAccount.getText().length() > 0 && s.length() >= 6);
            }
        });
        binding.btnLogin.setOnClickListener(l -> presenter.login(binding.edtAccount.getText().toString(), binding.edtPassword.getText().toString()));
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
    public void setPresenter(PwdLoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }
}
