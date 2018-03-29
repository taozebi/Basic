package com.taoze.basic.app.module1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ewide.core.util.T;
import com.taoze.basic.R;
import com.taoze.basic.app.bean.ChatInfo;
import com.taoze.basic.common.base.BaseFragment;
import com.taoze.basic.common.view.PtrClassFrameLayout;

import java.util.List;

import butterknife.BindView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Headers;

/**
 * Module1
 * Created by Taoze on 2018/3/26.
 */
public class Module1Fragment extends BaseFragment{

    @BindView(R.id.mPtrCFrameLayout)
    public PtrClassFrameLayout mPtrCFrameLayout;

    @BindView(R.id.mChatLv)
    public ListView mChatLv;

    private List<ChatInfo> chatInfos;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module1,null);
    }

    @Override
    protected void onInitData() {
        setTitleText("Module 1");
        setSubmitBtnVisibility(false);

        mPtrCFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrCFrameLayout.autoRefresh(false);
            }
        }, 100);
        mPtrCFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH );

        mPtrCFrameLayout.setPtrHandler(new PtrDefaultHandler2(){
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPtrCFrameLayout.refreshComplete();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadFromServer();
            }
        });

        loadData();

    }

    /**
     * 加载数据
     */
    private void loadData(){

    }

    /**
     * 加载服务端数据
     */
    private void loadFromServer(){
        T.showShort(getActivity(),"loadXcjhFromServer()");
        HttpRequest.get("http://www.baidu.com", new BaseHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, Object o) {
                super.onSuccess(headers, o);
                T.showShort(getActivity(),"onSuccess()");
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                T.showShort(getActivity(),"onFailure()");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                T.showShort(getActivity(),"onFinish()");
            }
        });
        mPtrCFrameLayout.refreshComplete();
    }
}
