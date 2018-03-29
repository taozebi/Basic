package com.taoze.basic.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ewide.core.util.KKLog;
import com.ewide.core.util.T;
import com.taoze.basic.common.view.TitleBar;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
	
	protected View rootView;
	
	private LayoutInflater mInflater;
	
	FManager moduleManager;
	
	protected TitleBar titleBar;
	
	private int tempRequestCode = -1;

	private boolean invlidate = false;

	private Bundle backBundle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		this.mInflater = inflater;
		LinearLayout baseLayout = new LinearLayout(getActivity());
		baseLayout.setOrientation(LinearLayout.VERTICAL);
		
		titleBar = new TitleBar(getActivity());
		
		titleBar.setOnBackBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!onBack()){
					back();
				}
			}
		});
		
		titleBar.setOnSubmitBtnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmit();
			}
		});

		baseLayout.addView(titleBar,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		titleBar.setBackBtnVisibility(true);
		titleBar.setSubmitBtnVisibility(true);
		
		View contentView = onCreateContentView(inflater,container,savedInstanceState);
		baseLayout.addView(contentView,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		rootView = baseLayout;
		ButterKnife.bind(this,rootView);
		refresh(true);
		return rootView;
	}
	
	/**
	 * 创建内容View
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	void back(){
		if(invlidate){
			moduleManager.back(invlidate,backBundle);
			invlidate = false;
			backBundle = null;
		}else{
			moduleManager.back(false,null);
		}
	}

//	/**
//	 * 关闭Slide页面
//	 */
//	protected void close(){
//		DrawerLayout drawer = (DrawerLayout)((MainActivity)getActivity()).findViewById(R.id.drawer);
//		if(drawer.isDrawerOpen(GravityCompat.END)){
//    		drawer.closeDrawer(GravityCompat.END);
//    	}
//	}
//	
	/**
	 * 描述：初始化数据。建议在此方法内填充视图的数据。<br/>
	 * <b>注意：此方法在onBindView后执行。</b>
	 */
	protected abstract void onInitData();
	
	
	
	/**
	 * 描述：用指定资源ID表示的View填充主界面，视图创建完毕后调用，建议在此方法内注册监听。<br/>
	 * <b>注意：此方法在onInitData前执行。</b>
	 * @param contentView 内容View
	 */
	protected void onBindView(View contentView) {
		
	}
	
	/**
	 * 设置后退按钮是否可见
	 * @param visible
	 */
	protected void setBackBtnVisible(boolean visible){
		if(titleBar != null){
			titleBar.setBackBtnVisibility(visible);
		}
	}

	public boolean onKeyUp(int keyCode, KeyEvent event){
		return false;
	}

	/**
	 * 视图销毁时回调
	 */
	protected void onDestroyUI(){}
	
	/**
	 * 返回键按下时回调，如返回false，则会自动调用FManager.back方法。
	 * @return
	 */
	protected boolean onBack(){
		return false;
	}
	
	protected void onSubmit(){
		invlidateBackRefresh(null);
	}
	
	protected void onRefresh(Bundle bundle){
		
	}

	/**
	 * 刷新当前界面
	 * @param resetBind 是否重新注册控件监听
	 */
	public void refresh(boolean resetBind){
		if(resetBind){
			onBindView(rootView);
		}
		onInitData();
	}

	/**
	 * 刷新当前界面和数据。不注册控件监听。
	 */
	public void refreshData(){
		onInitData();
	}

	/**
	 * 标记后退刷新，调用此方法后，如果按下后退键，则前一个界面会自动调用onRefresh进行刷新。
	 * @param bundle 返回前一个界面的Bundle
	 */
	protected void invlidateBackRefresh(Bundle bundle){
		this.invlidate = true;
		this.backBundle = bundle;
	}

	/**
	 * 标记后退刷新，调用此方法后，如果按下后退键，则前一个界面会自动调用onRefresh进行刷新。
	 */
	protected void invlidateBackRefresh(){
		this.invlidate = true;
	}

	/**
     * 描述：设置标题文本.
     * @param text 文本
     */
	public void setTitleText(String text) {
		if(titleBar != null){
			titleBar.setTitleText(text);
		}
	}
	
	/**
	 * 描述：设置标题文本.
	 * @param resId 资源ID
	 */
	public void setTitleText(int resId) {
		if(titleBar != null){
			titleBar.setTitleText(resId);
		}
	}
	
	/**
     * 描述：设置标题文本.
     * @param text 文本
     */
	public void setSubmitBtnText(String text) {
		if(titleBar != null){
			titleBar.setSubmitBtnText(text);
		}
	}
	
	/**
	 * 描述：设置标题文本.
	 * @param resId 资源ID
	 */
	public void setSubmitBtnText(int resId) {
		if(titleBar != null){
			titleBar.setSubmitBtnText(resId);
		}
	}
	
	/**
	 * 设置是否包含后退按钮
	 * @param visible 是否可见
	 * */
	public void setBackBtnVisibility(boolean visible) {
		if(titleBar != null){
			titleBar.setBackBtnVisibility(visible);
		}
	}
	
	/**
	 * 设置是否包含确认按钮
	 * @param visible  是否可见
	 */
	public void setSubmitBtnVisibility(boolean visible) {
		if(titleBar != null){
			titleBar.setSubmitBtnVisibility(visible);
		}
	}
	
	public FManager getModuleManager() {
		return moduleManager;
	}
	
	private void mStartActivityForResult(Intent intent){
		getActivity().startActivityForResult(intent, getModuleManager().getRequestCode());
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		this.tempRequestCode = requestCode;
		mStartActivityForResult(intent);
	}
	
	public void activityResult(int resultCode, Intent data) {
		onActivityResult(tempRequestCode, resultCode, data);
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
	
	/**
	 * 根据ID返回View
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	protected <T extends View> T findViewById(int id){
		if(rootView != null){
			return (T) rootView.findViewById(id);
		}
		return null;
	}
	
	protected void showShort(String message){
		T.showShort(getActivity(), message);
	}
	
	protected void showLong(String message){
		T.showLong(getActivity(), message);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		KKLog.d(getClass().getName()+" onActivityCreated");
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		KKLog.d(getClass().getName()+" onDestroyView");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		KKLog.d(getClass().getName()+" onAttach");
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		KKLog.d(getClass().getName()+" onDetach");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		KKLog.d(getClass().getName()+" onResume");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		KKLog.d(getClass().getName()+" onPause");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		KKLog.d(getClass().getName()+" onDestroy");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		KKLog.d(getClass().getName()+" onCreate");
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		KKLog.d(getClass().getName()+" onStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		KKLog.d(getClass().getName()+" onStop");
	}
}
