package com.taoze.basic.common.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 弹出进度组件
 */
public class ProgressDialogUtil {

    private static ProgressDialog progressDialog;

    private ProgressDialogUtil() {}

    /**
     * 显示一个进度弹出组件
     * @param context 上下文
     * @param title 标题
     * @param message 进度条描述
     * @param onCancelListener 取消回调接口
     */
    public static void show(Context context, String title, String message, final OnCancelListener onCancelListener){
        if(progressDialog != null){
            dismiss();
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onCancelListener.cancel();
            }
        });
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    /**
     * 显示一个进度弹出组件
     * @param context 上下文
     * @param title 标题
     * @param message 进度条描述
     */
    public static void show(Context context, String title, String message){
        if(progressDialog != null){
            dismiss();
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    /**
     * 进度组件是否显示
     * @return
     */
    public static boolean isShowing(){
        return progressDialog != null && progressDialog.isShowing();
    }

    /**
     * 隐藏进度组件
     */
    public static void dismiss(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public interface OnCancelListener{
        void cancel();
    }
}
