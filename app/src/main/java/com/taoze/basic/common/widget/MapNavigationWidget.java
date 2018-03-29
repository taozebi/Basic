package com.taoze.basic.common.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.ewide.core.util.T;
import com.taoze.basic.R;

/**
 * 地图导航组件
 * Created by Taoze on 2018/3/28.
 */
public class MapNavigationWidget implements OnClickListener {
	private Context mContext;
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private Button initialExtentBtn,zoomInBtn,zoomOutBtn;
	private Button gpsBtn;
	private boolean isLocation = false;
	private double count = 0;

	private boolean isFirstLocation = true;

	public MapNavigationWidget(Context context, MapView mapView, View uiView) {
		this.mContext = context;
		this.mMapView = mapView;
		mBaiduMap = mapView.getMap();
		createView(uiView);
	}

	protected void createView(View uiView) {
		initialExtentBtn = (Button)uiView.findViewById(R.id.mapInitialExtentBtn);
		initialExtentBtn.setOnClickListener(this);
		zoomInBtn = (Button)uiView.findViewById(R.id.mapZoomInBtn);
		zoomInBtn.setOnClickListener(this);
		zoomOutBtn = (Button)uiView.findViewById(R.id.mapZoomOutBtn);
		zoomOutBtn.setOnClickListener(this);

		gpsBtn = (Button) uiView.findViewById(R.id.GPSBtn);
		gpsBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.mapInitialExtentBtn:
				T.showShort(mContext,"test button");
				break;
			case R.id.mapZoomInBtn:
				zoomIn();
				break;
			case R.id.mapZoomOutBtn:
				zoomOut();
				break;
			case R.id.GPSBtn:
				T.showShort(mContext,"test gps button");
				break;
		}
	}

	private void zoomIn(){
		MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
		mBaiduMap.setMapStatus(zoomIn);
	}

	private void zoomOut(){
		MapStatusUpdate zoomOut = MapStatusUpdateFactory.zoomOut();
		mBaiduMap.setMapStatus(zoomOut);
	}

	private void center(LatLng centerPoint){
		//定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder()
				.target(centerPoint)
				.zoom(mBaiduMap.getMapStatus().zoom)
				.build();
		//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		//改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

}
