package com.taoze.basic.app;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.taoze.basic.app.global.AppData;
import com.taoze.basic.common.base.BaseActivity;
import com.taoze.basic.common.base.BaseApplication;
import com.taoze.basic.common.widget.AlertDialog;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.jpush.android.api.JPushInterface;

/**
 * 示例Application
 * Created by Taoze on 2018/3/26.
 */

public class DemoApplication extends BaseApplication {

    public static final String TAG = "zebit";

    /**
     * 是否有地图，地图需初始化
     **/
    public static final boolean hasMapView = true;

    private List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public Activity getTopActivity(){
        return activities.get(activities.size()-1);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (hasMapView) {
            initializeMap();
        }
        initializeJPush();
        initializeBugly();
        initializeOkHttp();
    }

    private void initializeJPush(){
        Log.d(TAG,"init Jpsh");
        JPushInterface.setDebugMode(true);//测试版为true
        JPushInterface.init(this);
    }

    private void initializeBugly(){
        Log.d(TAG,"init Bugly");
        CrashReport.initCrashReport(getApplicationContext(), AppData.BUGLY_APPID,false);
    }

    private void initializeOkHttp() {
        Log.d(TAG,"init OKHttp");
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }

    private void initializeMap() {
        Log.d(TAG,"init BaiduMap");
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    public boolean toExit(final Activity activity) {
        new AlertDialog(activity)
                .builder()
                .setTitle("退出程序")
                .setMsg("可能会使你现有记录归零，确定退出？")
                .setPositiveButton("确认退出", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 销毁Activity
                        for (Activity act : activities) {
                            act.finish();
                        }
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                }).show();
        return true;
    }
}
