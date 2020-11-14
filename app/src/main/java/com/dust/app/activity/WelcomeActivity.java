package com.dust.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dust.app.activity.contract.WelcomeContract;
import com.dust.app.activity.presenter.WelcomePresenter;
import com.dust.app.databinding.ActivityWelcomeBinding;
import com.dust.app.retrofit.RetrofitHelper;
import com.dust.app.service.UpdateService;
import com.dust.app.utils.SharedPreferencesUtils;

import static com.dust.app.utils.SystemUtils.setStatusBarFullTransparent;

public class WelcomeActivity extends AppCompatActivity implements WelcomeContract.View {

    private final String TAG = WelcomeActivity.class.getSimpleName();
    private ActivityWelcomeBinding binding;
    private WelcomeContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setStatusBarFullTransparent(this, true);
        new WelcomePresenter(this);
        initData();
        initView();
    }

    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        presenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        presenter.unSubscribe();
    }

    @Override
    public void refreshTokenSuccess() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void initData() {
        String server = SharedPreferencesUtils.getString("hostname", null);
        if (server != null) {
            RetrofitHelper.HOSTNAME = server;
        }
        Log.i(TAG, "SERVER:" + RetrofitHelper.getMainServer());
    }

    private void initView() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(3 * 1000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                String REFRESH_TOKEN = SharedPreferencesUtils.getString("REFRESH_TOKEN", null);
                Log.e(TAG, "refresh token");
                if (REFRESH_TOKEN != null) {
                    presenter.refreshToken(REFRESH_TOKEN);
                }
                Intent intent = new Intent(WelcomeActivity.this, UpdateService.class);
                startService(intent);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.getRoot().startAnimation(alphaAnimation);
    }

}
