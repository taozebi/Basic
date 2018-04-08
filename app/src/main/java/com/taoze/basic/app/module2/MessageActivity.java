package com.taoze.basic.app.module2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.ewide.core.util.T;
import com.taoze.basic.R;
import com.taoze.basic.app.DemoApplication;
import com.taoze.basic.common.base.CommonActivity;
import com.taoze.basic.common.view.PtrClassFrameLayout;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Taoze on 2018/4/8.
 */

public class MessageActivity extends CommonActivity{

    @BindView(R.id.mPtcMessage)
    public PtrClassFrameLayout mPtcMessage;
    @BindView(R.id.mMessageLv)
    public ListView mMessageLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    @Override
    public void onInitData() {
        titleBar.setTitleText("Message");
        titleBar.setBackBtnVisibility(true);
        mPtcMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtcMessage.autoRefresh(true);
            }
        }, 100);
        mPtcMessage.setMode(PtrFrameLayout.Mode.REFRESH );

        mPtcMessage.setPtrHandler(new PtrDefaultHandler2(){
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPtcMessage.refreshComplete();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtcMessage.refreshComplete();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            Log.d(DemoApplication.TAG,bundle.toString());
            T.showShort(this,"推送消息包含的信息: taskId = " +bundle.getString("taskId"));
        }
    }
}
