package com.taoze.basic.app.module3;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taoze.basic.R;
import com.taoze.basic.common.base.CommonActivity;

import butterknife.BindView;

/**
 * Created by Taoze on 2018/4/4.
 */

public class BlogActivity extends CommonActivity{

    @BindView(R.id.mWebView)
    public WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        titleBar.setTitleText("WebViewActivity");
        titleBar.setBackBtnVisibility(true);

        //这段代码的作用是让webview不要使用系统自带浏览器
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
        });
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onInitData() {

    }
}
