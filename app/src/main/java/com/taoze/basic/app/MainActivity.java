package com.taoze.basic.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
import com.baidu.mapapi.model.LatLng;
import com.taoze.basic.R;
import com.taoze.basic.app.module1.Module1Fragment;
import com.taoze.basic.app.module2.Module2Fragment;
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
        initData();
        setMapConfig();
    }

    private void initData(){
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务

        View uiView = findViewById(R.id.mapNavigationWidget);
        mMapNavigationWidget = new MapNavigationWidget(this, mMapView, uiView);

        titleBar.setTitleText(R.string.app_name);
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
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @OnClick({R.id.mModuleOneBtn,R.id.mModuleTwoBtn,R.id.mModuleThreeBtn,R.id.mModuleFourBtn})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.mModuleOneBtn:
                showShort(" Module one btn clicked;");
                mFManager.replace(Module1Fragment.class,null);
                break;
            case R.id.mModuleTwoBtn:
                showShort(" Module two btn clicked;");
                mFManager.replace(Module2Fragment.class,null);
                break;
            case R.id.mModuleThreeBtn:
                showShort(" Module three btn clicked;");
                break;
            case R.id.mModuleFourBtn:
                showShort(" Module four btn clicked;");
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
            mCurrentLat = bdLocation.getLatitude();
            mCurrentLon = bdLocation.getLongitude();
            mCurrentAccracy = bdLocation.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
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
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}