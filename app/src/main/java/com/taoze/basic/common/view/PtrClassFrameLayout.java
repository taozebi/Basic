package com.taoze.basic.common.view;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * 二次封裝的PriFrameLayout
 * Created by Taoze on 2016/12/1.
 */
public class PtrClassFrameLayout extends PtrFrameLayout {

    private MaterialHeader mPtrClassHeader;
    private MaterialHeader mPtrClassFooter;

    public PtrClassFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrClassFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }
    private void initViews() {
        mPtrClassHeader = new MaterialHeader(getContext());
        mPtrClassHeader.setPadding(4,4, 4, 4);
        setHeaderView(mPtrClassHeader);
        addPtrUIHandler(mPtrClassHeader);
        mPtrClassHeader.setPtrFrameLayout(this);
        mPtrClassFooter = new MaterialHeader(getContext());
        mPtrClassFooter.setPadding(4,4, 4, 4);
        setFooterView(mPtrClassFooter);
        addPtrUIHandler(mPtrClassFooter);
        mPtrClassFooter.setPtrFrameLayout(this);

        setLoadingMinTime(1000);
        setDurationToClose(200);
        setDurationToCloseHeader(500);
    }
}

