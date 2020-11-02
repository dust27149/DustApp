package com.dust.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dust.app.R;
import com.dust.app.activity.MainActivity;
import com.dust.app.databinding.FragmentSmsLoginBinding;
import com.dust.app.fragment.contract.SMSLoginContract;
import com.dust.app.fragment.presenter.SMSLoginPresenter;

public class SMSLoginFragment extends Fragment implements SMSLoginContract.View {

    private SMSLoginContract.Presenter presenter;
    private FragmentSmsLoginBinding binding;
    private Activity activity;
    private CountDownTimer timer;

    public SMSLoginFragment() {

    }

    public static SMSLoginFragment newInstance() {
        return new SMSLoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        new SMSLoginPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSmsLoginBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.btnGetVerification.setEnabled(s.length() == 11);
            }
        });
        binding.edtVerification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.btnLogin.setEnabled(binding.edtVerification.getText().length() == 6 && binding.edtPhoneNumber.getText().length() == 11);
            }
        });
        binding.btnGetVerification.setOnClickListener(l -> presenter.getVerification(binding.edtPhoneNumber.getText().toString()));
        binding.btnLogin.setOnClickListener(l -> presenter.login(binding.edtPhoneNumber.getText().toString(), binding.edtVerification.getText().toString()));
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
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void setPresenter(SMSLoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getVerificationSuccess() {
        timer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.btnGetVerification.setText(String.format(getString(R.string.cut_down_timer), millisUntilFinished / 1000));
                binding.btnGetVerification.setEnabled(false);
            }

            @Override
            public void onFinish() {
                binding.btnGetVerification.setText(getResources().getText(R.string.get));
                binding.btnGetVerification.setEnabled(true);
            }
        }.start();
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
