package com.taoze.basic.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

public class FManager {
	
	private BaseFragment currentFragment;

	private FragmentManager mFragmentManager;
	
	private int inAnimation;
	
	private int exitAnimation;
	
	private int mRootViewID;
	
	private List<BaseFragment> fragments;
	
	private CommonActivity mActivity;
	
	private int mRequestCode;

	private FragmentManageListener fragmentManageListener;

	private BaseFragment rootFragment;
	
	public FManager(int rootViewID,CommonActivity fragmentActivity,int requestCode) {
		this.mRootViewID = rootViewID;
		this.mActivity = fragmentActivity;
		this.mFragmentManager = fragmentActivity.getSupportFragmentManager();
		this.fragments = new ArrayList<BaseFragment>();
		this.mRequestCode = requestCode;
	}

	public FManager(int rootViewID, CommonActivity fragmentActivity, int requestCode, Class<? extends BaseFragment> rootFragment, Bundle bundle) {
		this.mRootViewID = rootViewID;
		this.mActivity = fragmentActivity;
		this.mFragmentManager = fragmentActivity.getSupportFragmentManager();
		this.fragments = new ArrayList<BaseFragment>();
		this.mRequestCode = requestCode;
		initRootFragment(rootFragment,bundle);
	}

	private void initRootFragment(Class<? extends BaseFragment> fragment, Bundle bundle){
		BaseFragment baseFragment = null;
		try {
			baseFragment = fragment.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		this.rootFragment = baseFragment;
		this.rootFragment.setArguments(bundle);
		String tag = baseFragment.getClass().getName();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.replace(mRootViewID,rootFragment,tag);
		transaction.commit();
		this.currentFragment = rootFragment;
		this.rootFragment.moduleManager = this;
	}

	public void replace(Class<? extends BaseFragment> clazz, Bundle bundle){
		replace(clazz,bundle,true);
	}
	
	private void replace(Class<? extends BaseFragment> clazz, Bundle bundle, boolean addToBack){
		//KKLog.d("replaceFragment "+clazz.toString());
		hideSoftInput();
		if(clazz == null){
			return;
		}
		BaseFragment baseFragment = null;
		try {
			baseFragment = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		if(currentFragment != null){
			if(currentFragment.getClass().toString().equals(clazz.toString())){
				return;
			}
			if(currentFragment == baseFragment){
				return;
			}
		}
		String tag = baseFragment.getClass().getName();
		mFragmentManager.popBackStackImmediate();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		if(inAnimation != 0 && exitAnimation != 0){
			transaction.setCustomAnimations(inAnimation,exitAnimation);
		}
		BaseFragment bf = (BaseFragment) mFragmentManager.findFragmentByTag(tag);
		if(bf == null){
//			baseFragment.mIsReplace = true;
			baseFragment.setArguments(bundle);
			transaction.add(mRootViewID,baseFragment,tag);
			if(fragments.size()>0){
				fragments.set(fragments.size()-1,baseFragment);
			}else{
				fragments.add(baseFragment);
			}
		}else{
//			bf.mIsReplace = true;
//			bf.setArguments(bundle);
			baseFragment = bf;
			transaction.show(baseFragment);
		}
		if(currentFragment != null){
			transaction.hide(currentFragment);
			currentFragment.onDestroyUI();
		}
		transaction.addToBackStack(tag);
		transaction.commit();
		baseFragment.moduleManager = this;
		currentFragment = baseFragment;
		if(fragmentManageListener != null){
			fragmentManageListener.onReplace(baseFragment);
		}
	}
	
	public void add(Class<? extends BaseFragment> clazz, Bundle bundle){
		add(clazz, bundle,true);
	}
	
	private void add(Class<? extends BaseFragment> clazz, Bundle bundle, boolean addToBack){
		//KKLog.d("addFragment "+clazz.toString());
		hideSoftInput();
		if(clazz == null){
			return;
		}
		BaseFragment baseFragment = null;
		try {
			baseFragment = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if(currentFragment != null){
			if(currentFragment.getClass().toString().equals(clazz.toString())){
				return;
			}
			if(currentFragment == baseFragment){
				return;
			}
		}
		String tag = baseFragment.getClass().getName();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		if(inAnimation != 0 && exitAnimation != 0){
			transaction.setCustomAnimations(inAnimation,exitAnimation);
		}
		BaseFragment bf = (BaseFragment) mFragmentManager.findFragmentByTag(tag);
		if(bf == null){
			baseFragment.setArguments(bundle);
			transaction.add(mRootViewID,baseFragment,tag);
		}else{
			baseFragment = bf;
			transaction.show(baseFragment);
		}
		if(currentFragment != null){
			transaction.hide(currentFragment);
		}
		fragments.add(baseFragment);
		transaction.addToBackStack(tag);
		transaction.commit();
		baseFragment.moduleManager = this;
		currentFragment = baseFragment;
		if(fragmentManageListener != null){
			fragmentManageListener.onAdded(baseFragment);
		}
	}
	
	public void clearBackStack(){
		while (fragments.size() > 0){
			back(false);
		}
	}

	public void back(){
		if(currentFragment != null){
			currentFragment.back();
		}
	}

	/**
	 * 返回前一个界面
	 * @param refresh 前一界面是否调用刷新方法
	 */
	public void back(boolean refresh){
		back(refresh,null);
	}
	
	public void back(boolean refresh,Bundle bundle){
		hideSoftInput();
		if(fragments.size() > 0){
			if(currentFragment != null){
				currentFragment.onDestroyUI();
//				FragmentTransaction transaction = mFragmentManager.beginTransaction();
//				if(isReplace){
//
//				}else {
					mFragmentManager.popBackStackImmediate();
//				}
				fragments.remove(currentFragment);
				if(fragmentManageListener != null){
					fragmentManageListener.onRemoved(currentFragment);
				}
				if(fragments.size() > 0){
					currentFragment = fragments.get(fragments.size()-1);
//					transaction.show(currentFragment);
					if(refresh){
						currentFragment.onRefresh(bundle);
					}
				}else{
					if(rootFragment != null){
						currentFragment = rootFragment;
//						transaction.show(rootFragment);
					}else{
						currentFragment = null;
					}
				}
//				transaction.commit();
			}
		}
	}

	public boolean onKeyUp(int keyCode, KeyEvent event){
		if(currentFragment != null){
			return currentFragment.onKeyUp(keyCode,event);
		}
		return false;
	}

	/**
	 * 关闭软键盘
	 * @return 是否关闭成功
	 */
	private boolean hideSoftInput() {
		InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if(isOpen){
			if(mActivity.getCurrentFocus() != null){
				imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				return true;
			}
		}
		return false;
	}

	public void showContent(Class<? extends BaseFragment> clazz){
		mActivity.showContent(clazz);
	}
	
	public BaseFragment getCurrentFragment() {
		return currentFragment;
	}
	
	public int getRequestCode(){
		return mRequestCode;
	}
	
	public void setAnimations(int in,int exit){
		this.inAnimation = in;
		this.exitAnimation = exit;
	}

	public int getFragmentCount(){
		return fragments.size();
	}

	public void setFragmentManageListener(FragmentManageListener fragmentManageListener) {
		this.fragmentManageListener = fragmentManageListener;
	}

	public interface FragmentManageListener{
		void onAdded(BaseFragment fragment);
		void onRemoved(BaseFragment fragment);
		void onReplace(BaseFragment fragment);
	}
}
