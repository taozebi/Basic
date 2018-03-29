package com.taoze.basic.app;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.taoze.basic.common.base.BaseApplication;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * 示例Application
 * Created by Taoze on 2018/3/26.
 */

public class DemoApplication extends BaseApplication{

    /** 是否有地图，地图需初始化 **/
    public static final boolean hasMapView = true;
    @Override
    public void onCreate() {
        super.onCreate();
        initializeOkHttp();
        if(hasMapView){
            initializeMap();
        }
    }

    private void initializeOkHttp(){
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }

    private void initializeMap(){
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
