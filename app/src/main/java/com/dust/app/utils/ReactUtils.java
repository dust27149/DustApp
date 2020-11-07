package com.dust.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dust.app.widget.LoadingView;

public class ReactUtils {

    public static void showCustomToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        View toastView = toast.getView();
        toastView.setBackground(new ColorDrawable(0xC0000000));
        toastView.setForegroundGravity(View.TEXT_ALIGNMENT_GRAVITY);
        TextView tv = toastView.findViewById(android.R.id.message);
        tv.setMaxLines(2);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setMaxWidth(dpToPx(context, 130));
        toast.show();
    }

    public static void showCustomToast(Context context, String message, int imgResId) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        View toastView = toast.getView();
        toastView.setBackground(new ColorDrawable(0xC0000000));
        toastView.setForegroundGravity(View.TEXT_ALIGNMENT_GRAVITY);
        TextView tv = toastView.findViewById(android.R.id.message);
        tv.setMaxLines(2);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setMaxWidth(dpToPx(context, 130));
        tv.setCompoundDrawablesWithIntrinsicBounds(0, imgResId, 0, 0);
        tv.setCompoundDrawablePadding(20);
        toast.show();
    }

    public static Dialog createLoadingDialog(Context context, String message) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setBackground(new ColorDrawable(0xC0000000));
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        linearLayout.setPadding(dpToPx(context, 20), dpToPx(context, 10), dpToPx(context, 20), dpToPx(context, 10));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LoadingView loadingView = new LoadingView(context);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(100, 100);
        loadingView.setLayoutParams(viewParams);
        linearLayout.addView(loadingView);

        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setMaxLines(2);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setMaxWidth(dpToPx(context, 130));
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(tvParams);
        linearLayout.addView(textView);

        Dialog loadingDialog = new Dialog(context);
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(linearLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        loadingDialog.show();
        return loadingDialog;
    }

    public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
