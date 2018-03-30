package com.taoze.basic.app.module1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewide.core.util.T;
import com.taoze.basic.R;
import com.taoze.basic.app.bean.ChatInfo;
import com.taoze.basic.common.base.BaseFragment;

/**
 * Module 1-1
 * Created by Taoze on 2018/3/28.
 */

public class Module11Fragment extends BaseFragment{

    private ChatInfo mInfo;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module11,null);
    }

    @Override
    protected void onInitData() {
        setTitleText("Module 1-1");
        setSubmitBtnVisibility(false);
        Bundle bundle = getArguments();
        if(bundle != null&&bundle.containsKey("info")){
            mInfo = (ChatInfo) bundle.get("info");
            T.showShort(getActivity(),"info: "+mInfo.toString());
            bundle.putString("tip","This is Module1 1-1 's Bundle");
            invlidateBackRefresh(bundle);
        }
    }
}
