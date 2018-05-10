package com.taoze.basic.common.listener;

import android.view.View;
import android.widget.AbsListView;

import com.taoze.basic.common.view.PtrClassFrameLayout;

/**
 * 解决ListView 下拉事件冲突
 * Created by Taoze on 2018/5/10.
 */

public class PtrListViewOnScrollListener implements AbsListView.OnScrollListener {

    public PtrClassFrameLayout mPtrLayout;

    private boolean pauseOnScroll;

    private boolean pauseOnFling;

    private AbsListView.OnScrollListener externalListener;

    public PtrListViewOnScrollListener(PtrClassFrameLayout mPtrLayout, boolean pauseOnScroll, boolean pauseOnFling) {
        this(mPtrLayout, pauseOnScroll, pauseOnFling, null);
    }

    public PtrListViewOnScrollListener(PtrClassFrameLayout mPtrLayout, boolean pauseOnScroll, boolean pauseOnFling, AbsListView.OnScrollListener customListener) {
        this.mPtrLayout = mPtrLayout;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        externalListener = customListener;
    }

    @Override

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                if (pauseOnScroll) {
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

                if (pauseOnFling) {
                }
                break;
        }
        if (externalListener != null) {
            externalListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        //解决listView滑动与下拉刷新冲突问题
        // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，
        // 需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
        View firstView = view.getChildAt(firstVisibleItem);

        if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
            mPtrLayout.setEnabled(true);
        } else {
            mPtrLayout.setEnabled(false);
        }

        if (externalListener != null) {
            externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}

