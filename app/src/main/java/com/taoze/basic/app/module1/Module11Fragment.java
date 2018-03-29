package com.taoze.basic.app.module1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taoze.basic.R;
import com.taoze.basic.common.base.BaseFragment;

/**
 * Created by Taoze on 2018/3/28.
 */

public class Module11Fragment extends BaseFragment{
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module11,null);
    }

    @Override
    protected void onInitData() {

    }
}
