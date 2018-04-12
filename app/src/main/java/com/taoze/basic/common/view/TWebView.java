package com.taoze.basic.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 自定义WebView, 重写OnDraw方法, 监听WebView界面加载完成事件
 * Created by Taoze on 2018/4/11.
 */

public class TWebView extends WebView {

    private DrawFinishedListener mListener;

    public interface DrawFinishedListener{
        public void drawFinished();
    }

    public TWebView(Context context) {
        super(context);
    }

    public TWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getDrawFinishedListener().drawFinished();
    }

    public void setDrawFinishedListener(DrawFinishedListener listener){
        this.mListener = listener;
    }

    private DrawFinishedListener getDrawFinishedListener(){
        return this.mListener;
    }
}
