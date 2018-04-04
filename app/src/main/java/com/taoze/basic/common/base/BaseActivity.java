package com.taoze.basic.common.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.List;

import butterknife.ButterKnife;


/**
 * BaseActivity
 * @date 2016年9月22日
 * @version 1.0.0
 * 
 */
public abstract class BaseActivity extends FragmentActivity {

/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		FragmentManager fm = getSupportFragmentManager();
		int index = requestCode >> 16;
		if (index != 0) {
			index--;
			if (fm.getFragments() == null || index < 0
					|| index >= fm.getFragments().size()) {
				return;
			}
			Fragment frag = fm.getFragments().get(index);
			if (frag == null) {
			} else {
				handleResult(frag, requestCode, resultCode, data);
			}
			return;
		}
	}*/

/*	private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
		frag.onActivityResult(requestCode & 0xffff, resultCode, data);
		List<Fragment> frags = frag.getChildFragmentManager().getFragments();
		if (frags != null) {
			for (Fragment f : frags) {
				if (f != null)
					handleResult(f, requestCode, resultCode, data);
			}
		}
	}*/
	
	/**
	 * 描述：用指定资源ID表示的View填充主界面.可根据需要实现.
	 */
	public void bindViewListener() {
		
	}
	
	/**
	 * 描述：用指定的View填充主界面.
	 * @param contentView  指定的View
	 */
	@Override
	public void setContentView(View contentView) {
		super.setContentView(contentView);
		//注解控件
		ButterKnife.bind(this);
		//注册特殊监听
		bindViewListener();
	}
	
	/**
	 * 描述：用指定资源ID表示的View填充主界面.
	 * @param resId  指定的View的资源ID
	 */
	@Override
	public void setContentView(int resId) {
		super.setContentView(resId);
		//注解控件
		ButterKnife.bind(this);
		//注册特殊监听
		bindViewListener();
	}
}

