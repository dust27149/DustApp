package com.dust.app.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.dust.app.R;
import com.dust.app.activity.fragment.WeChatFragment;
import com.dust.app.bean.Constant;
import com.dust.app.databinding.ActivityWeChatEntryBinding;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.dust.app.bean.Constant.APP_ID;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private final String TAG = WXEntryActivity.class.getSimpleName();
    private IWXAPI api;
    private ActivityWeChatEntryBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("微信登录");
        }
        binding = ActivityWeChatEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        regToWx();
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int errorCode = baseResp.errCode;
        switch (errorCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.i(TAG, "用户同意");
                String code = ((SendAuth.Resp) baseResp).code;
                Bundle bundle = new Bundle();
                bundle.putString("code", code);
                WeChatFragment weChatFragment = WeChatFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_loin_we_chat, weChatFragment).commit();
                weChatFragment.setArguments(bundle);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.i(TAG, "用户拒绝");
                Toast.makeText(this, "您已拒绝使用微信登录", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.i(TAG, "用户取消");
                Toast.makeText(this, "您已取消使用微信登录", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleIntent(intent, this);
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        api.registerApp(APP_ID);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                api.registerApp(Constant.APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

}
