package com.taoze.basic.common.base;

import android.view.KeyEvent;

public interface IBackListener {

	/**
	 * 监听回退键
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event);
	
}
