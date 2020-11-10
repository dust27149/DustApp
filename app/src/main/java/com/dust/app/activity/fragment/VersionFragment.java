package com.dust.app.activity.fragment;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.dust.app.R;
import com.dust.app.activity.contract.VersionContract;
import com.dust.app.activity.presenter.VersionPresenter;
import com.dust.app.bean.AppInfo;
import com.dust.app.bean.Constant;
import com.dust.app.databinding.FragmentVersionBinding;
import com.dust.app.service.InstallService;
import com.dust.app.utils.NotificationUtils;
import com.dust.app.utils.PackageUtils;

public class VersionFragment extends Fragment implements VersionContract.View {

    private VersionContract.Presenter presenter;
    private FragmentVersionBinding binding;
    private Activity activity;

    public VersionFragment() {

    }

    public static VersionFragment newInstance() {
        return new VersionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        new VersionPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVersionBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvAppVersion.setText(PackageUtils.getVersionName(activity));
        binding.tvCheckUpdate.setOnClickListener(l -> presenter.checkUpdate(Constant.APP_ID, PackageUtils.getVersionName(activity)));
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
    public void setPresenter(VersionContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void hasUpdate(AppInfo appInfo) {
        binding.ivUpdate.setVisibility(View.VISIBLE);
        Intent intent = new Intent(activity, InstallService.class);
        intent.putExtra("download_url", appInfo.downloadUrl);
        intent.putExtra("app_name", appInfo.appName);
        intent.putExtra("notification_id", NotificationUtils.getUpdateNotificationId());
        PendingIntent pendingIntent = PendingIntent.getService(activity, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, NotificationUtils.getUpdateChannel(activity))
                .setContentTitle("发现新版本：" + Constant.updateInfo.appVersion)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Constant.updateInfo.updateInfo))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logo)
                .setOnlyAlertOnce(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo))
                .addAction(R.drawable.ic_logo, getString(R.string.update), pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
        notificationManager.notify(NotificationUtils.getUpdateNotificationId(), builder.build());
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

}
