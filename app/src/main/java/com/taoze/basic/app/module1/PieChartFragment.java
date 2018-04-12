package com.taoze.basic.app.module1;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ewide.core.util.T;
import com.taoze.basic.R;
import com.taoze.basic.common.base.BaseFragment;
import com.taoze.basic.common.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Taoze on 2018/4/9.
 */

public class PieChartFragment extends BaseFragment {

    @BindView(R.id.mPieChartView)
    public PieChartView mPieChartView;

    /*========= 状态相关 =========*/
    private boolean isExploded = false;                 //每块之间是否分离
    private boolean isHasLabelsInside = false;          //标签在内部
    private boolean isHasLabelsOutside = false;         //标签在外部
    private boolean isHasCenterCircle = false;          //空心圆环
    private boolean isPiesHasSelected = false;          //块选中标签样式
    private boolean isHasCenterSingleText = false;      //圆环中心单行文字
    private boolean isHasCenterDoubleText = false;      //圆环中心双行文字

    /*========= 数据相关 =========*/
    private PieChartData mPieChartData;                 //饼状图数据
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pie_chart,null);
    }

    @Override
    protected void onInitData() {

        titleBar.setTitleText("PieChart");
        titleBar.setSubmitBtnVisibility(false);
        titleBar.inflateMenu(R.menu.menu_pie_chart);
        titleBar.setOnMenuItemClickListener(new TitleBar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_pie_reset:
                        resetPieDatas();                            //重置数据
                        return true;
                    case R.id.menu_pie_explode_implode:
                        explodeChart();                             //分离每部分
                        return true;
                    case R.id.menu_pie_center_circle:
                        setCenterCircle();                          //设置空心圆环
                        return true;
                    case R.id.menu_pie_center_text_single:
                        setSingleTextInCenter();                    //设置环心单行文本
                        return true;
                    case R.id.menu_pie_center_text_double:
                        setDoubleTextInCenter();                    //设置环心双行文本
                        return true;
                    case R.id.menu_pie_show_hide_labels_inside:
                        showOrHideLabelsInside();                   //在内部设置标签
                        return true;
                    case R.id.menu_pie_show_hide_labels_outside:
                        showOrHideLabelsOutside();                  //在外部设置标签
                        return true;
                    case R.id.menu_pie_animate:
                        changePiesAnimate();                        //改变数据时动画
                        return true;
                    case R.id.menu_pie_pies_select_mode:
                        showOrHideLablesByPiesSelected();           //点击显示/不显示标签
                        return true;
                }
                return true;
            }
        });
        setPieDatas();
    }
    /**
     * 设置相关数据
     */
    private void setPieDatas() {
        int numValues = 6;                //把一张饼切成6块

        /*===== 随机设置每块的颜色和数据 =====*/
        List<SliceValue> values = new ArrayList<>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtils.pickColor());
            values.add(sliceValue);
        }

        /*===== 设置相关属性 类似Line Chart =====*/
        mPieChartData = new PieChartData(values);
        mPieChartData.setHasLabels(isHasLabelsInside);
        mPieChartData.setHasLabelsOnlyForSelected(isPiesHasSelected);
        mPieChartData.setHasLabelsOutside(isHasLabelsOutside);
        mPieChartData.setHasCenterCircle(isHasCenterCircle);

        //是否分离
        if (isExploded) {
            mPieChartData.setSlicesSpacing(10);                 //分离间距为18
        }

        //是否显示单行文本
        if (isHasCenterSingleText) {
            mPieChartData.setCenterText1("Hello");             //文本内容
        }

        //是否显示双行文本
        if (isHasCenterDoubleText) {
            mPieChartData.setCenterText2("World");             //文本内容

            /*===== 设置内置字体 不建议设置 除非有特殊需求 =====*/
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            mPieChartData.setCenterText2Typeface(tf);
            mPieChartData.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_double_text_size)));
        }
        mPieChartView.setPieChartData(mPieChartData);         //设置控件
    }
    /**
     * 重置饼状图
     */
    private void resetPieDatas() {
        /*========== 恢复相关属性 ==========*/
        mPieChartView.setCircleFillRatio(1.0f);     //充满整张饼
        isHasLabelsInside = false;
        isHasLabelsOutside = false;
        isHasCenterCircle = false;
        isHasCenterSingleText = false;
        isHasCenterDoubleText = false;
        isExploded = false;
        isPiesHasSelected = false;

        setPieDatas();                              //重新设置
    }

    /**
     * 块之间有空隙
     */
    private void explodeChart() {
        isExploded = !isExploded;           //取反即可
        setPieDatas();                      //重新设置
    }

    /**
     * 设置空心圆环
     */
    private void setCenterCircle() {
        isHasCenterCircle = !isHasCenterCircle;         //取反即可
        if (!isHasCenterCircle) {
            isHasCenterSingleText = false;              //无圆环则不显示字
            isHasCenterDoubleText = false;
        }
        setPieDatas();                                  //重新设置
    }

    /**
     * 圆环中心设置单行文字
     */
    private void setSingleTextInCenter() {
        isHasCenterSingleText = !isHasCenterSingleText;     //取反即可
        if (isHasCenterSingleText) {
            isHasCenterCircle = true;                       //必须是空心圆环
        }
        isHasCenterDoubleText = false;                      //没有两行
        setPieDatas();                                      //重新设置
    }

    /**
     * 圆环中心设置双行文字
     */
    private void setDoubleTextInCenter() {
        isHasCenterDoubleText = !isHasCenterDoubleText;     //取反即可
        if (isHasCenterDoubleText) {
            isHasCenterSingleText = true;                   //如果设置双行 则单行必须显示
            isHasCenterCircle = true;                       //必须是空心圆环
        }
        setPieDatas();                                      //重新设置
    }

    /**
     * 在内部显示标签
     */
    private void showOrHideLabelsInside() {
        isHasLabelsInside = !isHasLabelsInside;         //取反即可
        if (isHasLabelsInside) {
            isPiesHasSelected = false;                  //点击不显示标签
            //设置点击不显示标签
            mPieChartView.setValueSelectionEnabled(isPiesHasSelected);
            //已经在外部的话 适当变化形状
            if (isHasLabelsOutside) {
                mPieChartView.setCircleFillRatio(0.7f);
            } else {
                mPieChartView.setCircleFillRatio(1.0f);
            }
        }
        setPieDatas();                                  //重新设置
    }

    /**
     * 在外部显示标签
     */
    private void showOrHideLabelsOutside() {
        isHasLabelsOutside = !isHasLabelsOutside;       //取反即可
        if (isHasLabelsOutside) {
            isHasLabelsInside = true;                   //如果外面不显示 就在内部显示
            isPiesHasSelected = false;                  //点击不显示标签
            //设置点击不显示标签
            mPieChartView.setValueSelectionEnabled(isPiesHasSelected);
        }
        //已经在外部的话 适当变化形状
        if (isHasLabelsOutside) {
            mPieChartView.setCircleFillRatio(0.7f);
        } else {
            mPieChartView.setCircleFillRatio(1.0f);
        }
        setPieDatas();                                  //重新设置
    }

    /**
     * 改变数据时的动画
     */
    private void changePiesAnimate() {
        //随机设置值
        for (SliceValue value : mPieChartData.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
        mPieChartView.startDataAnimation();         //设置动画
    }

    /**
     * 点击每部分是否显示标签信息
     */
    private void showOrHideLablesByPiesSelected() {
        isPiesHasSelected = !isPiesHasSelected;             //取反即可
        //点击是否显示标签
        mPieChartView.setValueSelectionEnabled(isPiesHasSelected);
        if (isPiesHasSelected) {
            isHasLabelsInside = false;                      //内外都不显示标签
            isHasLabelsOutside = false;
            //如果已经在外部 适当变形
            if (isHasLabelsOutside) {
                mPieChartView.setCircleFillRatio(0.7f);
            } else {
                mPieChartView.setCircleFillRatio(1.0f);
            }
        }
        setPieDatas();                                      //重新设置
    }

    /**
     * 每部分点击监听
     */
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            T.showShort(getActivity(),"当前选中块约占: " + (int) value.getValue() + " %");
        }

        @Override
        public void onValueDeselected() {
        }
    }
}
