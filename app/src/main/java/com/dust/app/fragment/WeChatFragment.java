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
import com.dust.app.databinding.FragmentWeChatBindBinding;
import com.dust.app.fragment.contract.WeChatContract;
import com.dust.app.fragment.presenter.WeChatPresenter;

public class WeChatFragment extends Fragment implements WeChatContract.View {

    private WeChatContract.Presenter presenter;
    private Activity activity;
    private FragmentWeChatBindBinding binding;
    private CountDownTimer timer;
    private String code;

    public WeChatFragment() {

    }

    public static WeChatFragment newInstance() {
        return new WeChatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        new WeChatPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWeChatBindBinding.inflate(inflater);
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
                binding.btnBindWeChat.setEnabled(binding.edtVerification.getText().length() == 6 && binding.edtPhoneNumber.getText().length() == 11);
            }
        });
        binding.btnGetVerification.setOnClickListener(l -> presenter.getVerification(binding.edtPhoneNumber.getText().toString()));
        binding.btnBindWeChat.setOnClickListener(l -> presenter.bindWeChat(binding.edtPhoneNumber.getText().toString(), binding.edtVerification.getText().toString(), code));
        if (getArguments() != null) {
            code = getArguments().getString("code");
            presenter.loginByWeChat(code);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void setPresenter(WeChatContract.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.subscribe();
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginByWeChatSuccess() {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
    public void bindWeChatSuccess() {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
