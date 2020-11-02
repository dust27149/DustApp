package com.dust.app.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dust.app.R;
import com.dust.app.bean.Constant;
import com.dust.app.databinding.ActivityLoginBinding;
import com.dust.app.fragment.PwdLoginFragment;
import com.dust.app.fragment.SMSLoginFragment;
import com.dust.app.wxapi.WXEntryActivity;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        PwdLoginFragment pwdLoginFragment = PwdLoginFragment.newInstance();
        SMSLoginFragment smsLoginFragment = SMSLoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_login, pwdLoginFragment).commit();
        binding.ivLogo.setOnClickListener(l -> startActivity(new Intent(LoginActivity.this, SettingActivity.class)));
        binding.tvLoginType.setOnClickListener(l -> {
            String str = binding.tvLoginType.getText().toString();
            if (getResources().getString(R.string.sms_login).equals(str)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_login, smsLoginFragment).commit();
                binding.tvLoginType.setText(R.string.pwd_login);
            } else if (getResources().getString(R.string.pwd_login).equals(str)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_login, pwdLoginFragment).commit();
                binding.tvLoginType.setText(R.string.sms_login);
            }
        });
        binding.tvRegister.setOnClickListener(l -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        binding.ivWxLogin.setOnClickListener(l -> {
            if (isWxAvailable()) {
                startActivityForResult(new Intent(LoginActivity.this, WXEntryActivity.class), Constant.WX_REQ);
            } else {
                Toast.makeText(LoginActivity.this, "尚未安装微信", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isWxAvailable() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.tencent.mm", 0);
            return packageInfo != null;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

}


