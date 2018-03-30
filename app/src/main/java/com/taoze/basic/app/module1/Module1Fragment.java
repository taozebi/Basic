package com.taoze.basic.app.module1;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ewide.core.util.T;
import com.taoze.basic.R;
import com.taoze.basic.app.adapter.ModuleAdapter;
import com.taoze.basic.app.bean.ChatInfo;
import com.taoze.basic.common.base.BaseFragment;
import com.taoze.basic.common.view.PtrClassFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
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
    private ModuleAdapter mModuleAdapter;

    private int count = 0;

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
        chatInfos = new ArrayList<ChatInfo>();
        ChatInfo info = new ChatInfo();
        info.picture = R.drawable.pic_man;
        info.nickname = "Jack";
        info.lastMsg = "肚子好饿啊,几点下班的?";
        info.lastTime = "11:34";
        chatInfos.add(info);

        info = new ChatInfo();
        info.picture = R.drawable.pic_boy;
        info.nickname = "Tony";
        info.lastMsg = "How are you?";
        info.lastTime = "17:32";
        chatInfos.add(info);

        info = new ChatInfo();
        info.picture = R.drawable.pic_girl;
        info.nickname = "Marry";
        info.lastMsg = "I'm fine, and you?";
        info.lastTime = "17:32";
        chatInfos.add(info);

        info = new ChatInfo();
        info.picture = R.drawable.pic_mm;
        info.nickname = "Linda";
        info.lastMsg = "呜啦啦呜啦啦 德玛西亚";
        info.lastTime = "20:34";
        chatInfos.add(info);

        mModuleAdapter = new ModuleAdapter(getActivity(),chatInfos,R.layout.adapter_module_item);
        mChatLv.setAdapter(mModuleAdapter);

    }

    /**
     * 加载服务端数据
     */
    private void loadFromServer(){
        HttpRequest.get("http://www.baidu.com", new BaseHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, Object o) {
                super.onSuccess(headers, o);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if(count < 2){
                    ChatInfo info = new ChatInfo();
                    info.picture = R.drawable.pic_man;
                    info.nickname = "Jack";
                    info.lastMsg = "肚子好饿啊,几点下班的?";
                    info.lastTime = "11:34";
                    chatInfos.add(info);

                    info = new ChatInfo();
                    info.picture = R.drawable.pic_boy;
                    info.nickname = "Tony";
                    info.lastMsg = "How are you?";
                    info.lastTime = "17:32";
                    chatInfos.add(info);

                    info = new ChatInfo();
                    info.picture = R.drawable.pic_girl;
                    info.nickname = "Marry";
                    info.lastMsg = "I'm fine, and you?";
                    info.lastTime = "17:32";
                    chatInfos.add(info);

                    info = new ChatInfo();
                    info.picture = R.drawable.pic_mm;
                    info.nickname = "Linda";
                    info.lastMsg = "呜啦啦呜啦啦 德玛西亚";
                    info.lastTime = "20:34";
                    chatInfos.add(info);

                    mModuleAdapter.notifyDataSetChanged();
                    count ++;
                }else{
                    T.showShort(getActivity(),"已经是最新的数据了");
                }
            }
        });
        mPtrCFrameLayout.refreshComplete();
    }

    @OnItemClick(R.id.mChatLv)
    public void OnItemClick(int positon){
        T.showShort(getActivity(),"Click "+(positon+1));
        Bundle bundle = new Bundle();
        bundle.putSerializable("info",(ChatInfo)mModuleAdapter.getItem(positon));
        getModuleManager().add(Module11Fragment.class,bundle);
    }

    @Override
    protected void onRefresh(Bundle bundle) {
        super.onRefresh(bundle);
        if(bundle != null && bundle.containsKey("tip")){
            T.showShort(getActivity(),bundle.getString("tip"));
        }
    }
}
