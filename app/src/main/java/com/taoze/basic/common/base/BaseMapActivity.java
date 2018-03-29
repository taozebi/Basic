package com.taoze.basic.common.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.taoze.basic.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Map基类
 * Created by Taoze on 2018/3/26.
 */
public class BaseMapActivity extends CommonActivity{

    @BindView(R.id.mMapView)
    public MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_map);

        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(39.915071, 116.403907); // 默认 天安门
        float zoom = 11.0f; // 默认 11级
        builder.target(center).zoom(zoom);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
