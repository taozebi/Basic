package com.taoze.basic.common.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;


import com.ewide.core.util.T;
import com.taoze.basic.app.DemoApplication;
import com.taoze.basic.common.view.TitleBar;

import butterknife.ButterKnife;

/**
 * CommonActivity
 * @date 2016-3-2
 * @version 1.0.0
 * 
 */
public abstract class CommonActivity extends BaseActivity {

	/** 总布局. */
	protected RelativeLayout baseLayout = null;
	
	/** 标题栏布局. */
	protected TitleBar titleBar = null;
	
	/** 主内容布局. */
	protected RelativeLayout contentLayout = null;
	
	protected LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((DemoApplication)getApplication()).addActivity(this);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			// 透明状态栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			// 透明导航栏
////			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		}
		mInflater = LayoutInflater.from(this);
		
		baseLayout = new RelativeLayout(this);
		
		contentLayout = new RelativeLayout(this);
		
		titleBar = new TitleBar(this);

//		titleBar.setId(titleBarID);
//		titleBar.setFitsSystemWindows(true);
//		titleBar.setClipToPadding(false);
		titleBar.setOnBackBtnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(DemoApplication.TAG, "CommonActivity back clicked");
				if(!onBack()){
					((DemoApplication) getApplication()).getTopActivity().finish();
				}
			}
		});
		
		titleBar.setOnSubmitBtnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmit();
			}
		});
		
		RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		baseLayout.addView(titleBar,titleLayoutParams);
		
		RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		contentLayoutParams.addRule(RelativeLayout.BELOW,titleBar.getId());
//		contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
		baseLayout.addView(contentLayout,contentLayoutParams);
		
		RelativeLayout.LayoutParams baseParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		setContentView(baseLayout,baseParams);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		((DemoApplication)getApplication()).removeActivity(this);
	}

	protected void onSubmit() {
		
	}
	
	/**
	 * 返回键按下时回调，如返回false，则会自动调用finish方法。
	 * @return
	 */
	protected boolean onBack(){
		Log.d(DemoApplication.TAG,"CommonActivity --> onBack()");
		return false;
	}

	/**
	 * 描述：用指定的View填充主界面.
	 * @param contentView  指定的View
	 */
	@Override
	public void setContentView(View contentView) {
		contentLayout.removeAllViews();
		contentLayout.addView(contentView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//注解控件
		ButterKnife.bind(this);
	}
	
	/**
	 * 描述：用指定资源ID表示的View填充主界面.
	 * @param resId  指定的View的资源ID
	 */
	@Override
	public void setContentView(int resId) {
		setContentView(mInflater.inflate(resId, null));
	}

	/**
	 * 设置是否显示标题
	 * @param visible  是否可见
	 */
	protected void setTitleBarVisible(boolean visible) {
		if(titleBar != null){
			titleBar.setVisibility(visible? View.VISIBLE: View.GONE);
		}
	}
	
	protected void addActionView(int resId){
		if(titleBar != null){
			titleBar.addActionView(mInflater.inflate(resId, null));
		}
	}
	
	protected void addActionView(View view){
		if(titleBar != null){
			titleBar.addActionView(view);
		}
	}

	public void showContent(Class<? extends BaseFragment> clazz){}
	
	protected void showShort(String message){
		T.showShort(this, message);
	}
	
	protected void showLong(String message){
		T.showLong(this, message);
	}
}

