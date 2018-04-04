package com.taoze.basic.app;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.model.LatLng;
import com.ewide.core.util.T;
import com.taoze.basic.R;
import com.taoze.basic.app.module1.Module1Fragment;
import com.taoze.basic.app.module2.Module2Fragment;
import com.taoze.basic.app.module3.Module3Fragment;
import com.taoze.basic.app.module4.OfflineMapActivity;
import com.taoze.basic.common.base.BaseFragment;
import com.taoze.basic.common.base.CommonActivity;
import com.taoze.basic.common.base.FManager;
import com.taoze.basic.common.widget.MapNavigationWidget;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends CommonActivity implements SensorEventListener {

    @BindView(R.id.mMapView)
    public MapView mMapView;

    @BindView(R.id.mModuleOneBtn)
    public Button mModuleOneBtn;
    @BindView(R.id.mModuleTwoBtn)
    public Button mModuleTwoBtn;
    @BindView(R.id.mModuleThreeBtn)
    public Button mModuleThreeBtn;
    @BindView(R.id.mModuleFourBtn)
    public Button mModuleFourBtn;
    @BindView(R.id.layout_content_toolbar)
    public LinearLayout mToolBar;

    private FManager mFManager;

    private BaiduMap mBaiduMap;
    // 定位相关
    LocationClient mLocationClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;

    private MapNavigationWidget mMapNavigationWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(DemoApplication.TAG,"MainActivity onCreate()");
        initData();
        setMapConfig();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(DemoApplication.TAG,"MainActivity onNewIntent()");
        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(39.915071, 116.403907); // 默认 天安门
        float zoom = 11.0f; // 默认 11级
        if (null != intent) {
            center = new LatLng(intent.getDoubleExtra("y", 30.594985),
                    intent.getDoubleExtra("x", 114.318481));
            zoom = intent.getFloatExtra("level", 11.0f);
            Log.e(DemoApplication.TAG,"MainActivity onNewIntent() ---> x:"+center.latitude+" ,y: "+center.longitude +" ,zoom :"+zoom);
        }else{
            Log.e(DemoApplication.TAG,"MainActivity getIntent() is null");
        }
        builder.target(center).zoom(zoom);
    }

    private void initData(){
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务

        View uiView = findViewById(R.id.mapNavigationWidget);
        mMapNavigationWidget = new MapNavigationWidget(this, mMapView, uiView);

//        titleBar.setTitleText(R.string.app_name);
        titleBar.setVisibility(View.GONE);

        mFManager = new FManager(R.id.layout_content,this,200);
        mFManager.setAnimations(R.anim.fade_in,R.anim.fade_exit);
        mFManager.setFragmentManageListener(new FManager.FragmentManageListener() {
            @Override
            public void onAdded(BaseFragment fragment) {
                mToolBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onRemoved(BaseFragment fragment) {
                if(mFManager.getFragmentCount() == 0){
                    showToolbar();
                }
            }

            @Override
            public void onReplace(BaseFragment fragment) {
                if(mFManager.getFragmentCount() == 0){
                    showToolbar();
                }
            }
        });
    }

    private void setMapConfig(){

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        //隐藏放大缩小按钮
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        mBaiduMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3*10000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        MapStatus.Builder builder = new MapStatus.Builder();
//        LatLng center = new LatLng(30.594985, 114.318481);  // 默认武汉114.318481,30.594985
        LatLng center = new LatLng(39.915071, 116.403907); // 默认 天安门
        float zoom = 5.0f; // 默认 11级
        builder.target(center).zoom(zoom);
        builder.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }

    @OnClick({R.id.mModuleOneBtn,R.id.mModuleTwoBtn,R.id.mModuleThreeBtn,R.id.mModuleFourBtn})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.mModuleOneBtn:
                mFManager.replace(Module1Fragment.class,null);
                break;
            case R.id.mModuleTwoBtn:
                mFManager.replace(Module2Fragment.class,null);
                break;
            case R.id.mModuleThreeBtn:
                mFManager.replace(Module3Fragment.class,null);
                break;
            case R.id.mModuleFourBtn:
                Intent intent = new Intent(MainActivity.this, OfflineMapActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mMapView == null) {
                return;
            }
            double lat = bdLocation.getLatitude();
            double lng = bdLocation.getLongitude();
            if (4.9E-324 == lat || 4.9E-324 == lng) {
                T.showShort(MainActivity.this,"定位失败，请查看手机是否开启了定位权限");
                //定位失败 默认定位到武汉
                mCurrentLat = 30.594985;
                mCurrentLon = 114.318481;
                mCurrentAccracy = 50;
            }else{
                mCurrentLat = bdLocation.getLatitude();
                mCurrentLon = bdLocation.getLongitude();
                mCurrentAccracy = bdLocation.getRadius();
            }

            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng center = new LatLng(mCurrentLat,
                        mCurrentLon);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(center).zoom(5.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }

    private void showToolbar(){
        if(mToolBar.getVisibility() != View.VISIBLE){
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideToolbar(){
        if(mToolBar.getVisibility() != View.INVISIBLE){
            mToolBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(DemoApplication.TAG,"MainActivity requestCode: "+requestCode+"  ,resultCode: "+resultCode);
    }

    @Override
    public void onBackPressed() {
        if(mFManager.getCurrentFragment() != null){
            mFManager.back();
        }else {
            ((DemoApplication)getApplication()).toExit(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}