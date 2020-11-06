package com.dust.app.activity.fragment;

import android.app.Activity;
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
import com.dust.app.activity.contract.RegisterContract;
import com.dust.app.activity.presenter.RegisterPresenter;
import com.dust.app.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment implements RegisterContract.View {

    private RegisterContract.Presenter presenter;
    private FragmentRegisterBinding binding;
    private Activity activity;
    private CountDownTimer timer;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        new RegisterPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
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
                binding.tvAccountTips.setText("长度应大于0");
                binding.tvAccountTips.setVisibility(s.length() > 0 ? View.INVISIBLE : View.VISIBLE);
                binding.btnRegister.setEnabled(checkParams());
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
                binding.tvPasswordTips.setText("长度应大于6");
                binding.tvPasswordTips.setVisibility(s.length() >= 6 ? View.INVISIBLE : View.VISIBLE);
                binding.btnRegister.setEnabled(checkParams());
            }
        });
        binding.edtPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tvPasswordConfirmTips.setText("密码不一致");
                binding.tvPasswordConfirmTips.setVisibility(s.toString().equals(binding.edtPassword.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
                binding.btnRegister.setEnabled(checkParams());
            }
        });
        binding.edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tvPhoneNumberTips.setText("手机号有误");
                binding.tvPhoneNumberTips.setVisibility(s.length() == 11 ? View.INVISIBLE : View.VISIBLE);
                binding.btnGetVerification.setEnabled(s.length() == 11);
                binding.btnRegister.setEnabled(checkParams());
            }
        });
        binding.btnGetVerification.setOnClickListener(l -> {
            String phoneNumber = binding.edtPhoneNumber.getText().toString();
            if (phoneNumber.length() == 11) {
                presenter.getVerification(binding.edtPhoneNumber.getText().toString());
            }
            binding.btnRegister.setEnabled(checkParams());
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
                binding.tvVerificationTips.setText("验证码应为6位");
                binding.tvVerificationTips.setVisibility(s.length() == 6 ? View.INVISIBLE : View.VISIBLE);
                binding.btnRegister.setEnabled(checkParams());
            }
        });
        binding.btnRegister.setOnClickListener(l -> {
            if (checkParams()) {
                presenter.register(binding.edtAccount.getText().toString(), binding.edtPasswordConfirm.getText().toString(), binding.edtPhoneNumber.getText().toString(), binding.edtVerification.getText().toString());
            }
        });
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
    public void showToast(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getVerificationSuccess() {

        binding.btnGetVerification.setEnabled(false);
        timer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.btnGetVerification.setText(String.format(getString(R.string.cut_down_timer), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                binding.btnGetVerification.setText("获取");
                binding.btnGetVerification.setEnabled(true);
            }
        }.start();

    }

    @Override
    public void registerSuccess() {

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                activity.finish();
            }
        }.start();

    }

    private boolean checkParams() {
        String account = binding.edtAccount.getText().toString();
        String password1 = binding.edtPassword.getText().toString();
        String password2 = binding.edtPasswordConfirm.getText().toString();
        String phoneNumber = binding.edtPhoneNumber.getText().toString();
        String verification = binding.edtVerification.getText().toString();
        return account.length() != 0 && password2.equals(password1) && phoneNumber.length() == 11 && verification.length() == 6;
    }
}
