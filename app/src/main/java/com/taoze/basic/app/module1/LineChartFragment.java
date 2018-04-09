package com.taoze.basic.app.module1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ewide.core.util.T;
import com.taoze.basic.R;
import com.taoze.basic.app.bean.ChatInfo;
import com.taoze.basic.common.base.BaseFragment;
import com.taoze.basic.common.view.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Module 1-1
 * Created by Taoze on 2018/3/28.
 */

public class LineChartFragment extends BaseFragment{

    @BindView(R.id.mLineChartView)
    public LineChartView mLineChartView;

    private ChatInfo mInfo;

    /*=========== 数据相关 ==========*/
    private LineChartData mLineData;                    //图表数据
    private int numberOfLines = 1;                      //图上折线/曲线的显示条数
    private int maxNumberOfLines = 4;                   //图上折线/曲线的最多条数
    private int numberOfPoints = 12;                    //图上的节点数

    /*=========== 状态相关 ==========*/
    private boolean isHasAxes = true;                   //是否显示坐标轴
    private boolean isHasAxesNames = true;              //是否显示坐标轴名称
    private boolean isHasLines = true;                  //是否显示折线/曲线
    private boolean isHasPoints = true;                 //是否显示线上的节点
    private boolean isFilled = false;                   //是否填充线下方区域
    private boolean isHasPointsLabels = false;          //是否显示节点上的标签信息
    private boolean isCubic = true;                    //是否是立体的
    private boolean isPointsHasSelected = false;        //设置节点点击后效果(消失/显示标签)
    private boolean isPointsHaveDifferentColor = true;         //节点是否有不同的颜色

    /*=========== 其他相关 ==========*/
    private ValueShape pointsShape = ValueShape.CIRCLE; //点的形状(圆/方/菱形)
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints]; //将线上的点放在一个数组中

    private Timer timer = new Timer();
    private boolean isFinish = false;
    private int position = 0;
    private List<PointValue> pointValueList = new ArrayList<>();
    private Random random = new Random();
    private List<Line> linesList = new ArrayList<>();
    private LineChartData lineChartData;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_line_chart,null);
    }

    @Override
    protected void onBindView(View contentView) {
        super.onBindView(contentView);
    }

    @Override
    protected void onInitData() {
        setTitleText("Module 1-1");
        setSubmitBtnVisibility(false);
        titleBar.inflateMenu(R.menu.menu_line_chart);
        titleBar.setMenuVisibility(true);
        titleBar.setOnMenuItemClickListener(new TitleBar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.reset:
                        resetLines();
                        break;
                    case R.id.addLne:
                        addLineToData();
                        break;
                    case R.id.showOrHideNode:
                        showOrHidePoints();
                        break;
                    case R.id.showOrHideNodeTarget:
                        showOrHidePointsLabels();
                        break;
                    case R.id.curveOrBrokenLine:
                        changeCubicLines();
                        break;
                    case R.id.fullAreaColor:
                        fillLinesArea();
                        break;
                    case R.id.switchAnima:
                        changeLinesAnimate();
                        break;
                    case R.id.dynamicDataDisplay:
                        dynamicDataDisplay();
                        break;
                }
                return true;
            }
        });
        getBundleData();

        //节点点击事件监听
//        mLineChartView.setOnValueTouchListener(new ValueTouchListener());

        /**
         * 禁用视图重新计算 主要用于图表在变化时动态更改，不是重新计算
         * 类似于ListView中数据变化时，只需notifyDataSetChanged()，而不用重新setAdapter()
         */
        mLineChartView.setViewportCalculationEnabled(false);

        setPointsValues();          //设置每条线的节点值
        setLinesDatas();            //设置每条线的一些属性
        resetViewport();            //计算并绘图
    }

    private void getBundleData(){
        Bundle bundle = getArguments();
        if(bundle != null&&bundle.containsKey("info")){
            mInfo = (ChatInfo) bundle.get("info");
            T.showShort(getActivity(),"info: "+mInfo.toString());
            bundle.putString("tip","This is Module1 1-1 's Bundle");
            invlidateBackRefresh(bundle);
        }
    }

    /**
     * 利用随机数设置每条线对应节点的值
     */
    private void setPointsValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
    }

    /**
     * 设置线的相关数据
     */
    private void setLinesDatas() {
        List<Line> lines = new ArrayList<>();
        //循环将每条线都设置成对应的属性
        for (int i = 0; i < numberOfLines; ++i) {
            //节点的值
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            /*========== 设置线的一些属性 ==========*/
            Line line = new Line(values);               //根据值来创建一条线
            line.setColor(ChartUtils.COLORS[i]);        //设置线的颜色
            line.setShape(pointsShape);                 //设置点的形状
            line.setHasLines(isHasLines);               //设置是否显示线
            line.setHasPoints(isHasPoints);             //设置是否显示节点
            line.setCubic(isCubic);                     //设置线是否立体或其他效果
            line.setFilled(isFilled);                   //设置是否填充线下方区域
            line.setHasLabels(isHasPointsLabels);       //设置是否显示节点标签
            //设置节点点击的效果
            line.setHasLabelsOnlyForSelected(isPointsHasSelected);
            //如果节点与线有不同颜色 则设置不同颜色
            if (isPointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        mLineData = new LineChartData(lines);                      //将所有的线加入线数据类中
        mLineData.setBaseValue(Float.NEGATIVE_INFINITY);           //设置基准数(大概是数据范围)
        /* 其他的一些属性方法 可自行查看效果
         * mLineData.setValueLabelBackgroundAuto(true);            //设置数据背景是否跟随节点颜色
         * mLineData.setValueLabelBackgroundColor(Color.BLUE);     //设置数据背景颜色
         * mLineData.setValueLabelBackgroundEnabled(true);         //设置是否有数据背景
         * mLineData.setValueLabelsTextColor(Color.RED);           //设置数据文字颜色
         * mLineData.setValueLabelTextSize(15);                    //设置数据文字大小
         * mLineData.setValueLabelTypeface(Typeface.MONOSPACE);    //设置数据文字样式
        */

        //如果显示坐标轴
        if (isHasAxes) {
            Axis axisX = new Axis();                    //X轴
            Axis axisY = new Axis().setHasLines(true);  //Y轴
            axisX.setTextColor(Color.GRAY);             //X轴灰色
            axisY.setTextColor(Color.GRAY);             //Y轴灰色
            //setLineColor()：此方法是设置图表的网格线颜色 并不是轴本身颜色
            //如果显示名称
            if (isHasAxesNames) {
                axisX.setName("周期");                //设置名称
                axisY.setName("销售量:吨");
            }
            mLineData.setAxisXBottom(axisX);            //设置X轴位置 下方
            mLineData.setAxisYLeft(axisY);              //设置Y轴位置 左边
        } else {
            mLineData.setAxisXBottom(null);
            mLineData.setAxisYLeft(null);
        }

        mLineChartView.setLineChartData(mLineData);    //设置图表控件
    }

    /**
     * 重点方法，计算绘制图表
     */
    private void resetViewport() {
        //创建一个图标视图 大小为控件的最大大小
        final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
        v.left = 0;                             //坐标原点在左下
        v.bottom = 0;
        v.top = 100;                            //最高点为100
        v.right = numberOfPoints - 1;           //右边为点 坐标从0开始 点号从1 需要 -1
        mLineChartView.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        mLineChartView.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }
    /**
     * 重置线的格式 恢复初始化
     */
    private void resetLines() {
        numberOfLines = 1;
        /*===== 恢复初始化时相关属性 =====*/
        isHasAxesNames = true;
        isHasLines = true;
        isHasPoints = true;
        pointsShape = ValueShape.CIRCLE;
        isFilled = false;
        isHasPointsLabels = false;
        isCubic = true;
        isPointsHasSelected = false;
        isPointsHaveDifferentColor = true;

        mLineChartView.setZoomEnabled(false);

        mLineChartView.setInteractive(true);
        position = 0;
        pointValueList.clear();
        linesList.clear();
        lineChartData = null;
        if (timer != null) {
            timer.cancel();
            timer = new Timer();
        }

        mLineChartView.setValueSelectionEnabled(isPointsHasSelected);
        resetViewport();        //重新计算
        setLinesDatas();        //再设置一次
    }

    /**
     * 增加线的条数
     */
    private void addLineToData() {
        if (mLineData.getLines().size() >= maxNumberOfLines) {
            T.showShort(getActivity(),"最多只能有4条线");
            return;
        } else {
            ++numberOfLines;
        }
        setLinesDatas();        //再设置一次
    }
    /**
     * 显示或者隐藏节点
     */
    private void showOrHidePoints() {
        isHasPoints = !isHasPoints;   //取反即可
        setLinesDatas();              //重新设置
    }

    /**
     * 显示或者隐藏节点标签
     */
    private void showOrHidePointsLabels() {
        isHasPointsLabels = !isHasPointsLabels;   //取反即可
        setLinesDatas();                          //重新设置
    }
    /**
     * 将折线转为曲线
     */
    private void changeCubicLines() {
        isCubic = !isCubic;         //取反即可
        setLinesDatas();            //重新设置
        if (isCubic) {
            final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
            v.bottom = -5;          //防止曲线超过范围做边界保护
            v.top = 105;            //根据具体需求设置 建议设置一下
            mLineChartView.setMaximumViewport(v);                   //设置最大视图
            mLineChartView.setCurrentViewportWithAnimation(v);      //有动画的增加当前视图
        } else {
            final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
            v.bottom = 0;           //如果上面没有设置 那么这里也不用设置
            v.top = 100;            //同样的 建议设置一下
            //动画监听 在动画完成后 设置最大的视图 直接设置也可 但效果要好一点
            mLineChartView.setViewportAnimationListener(new ChangeLinesAnimListener(v));
            mLineChartView.setCurrentViewportWithAnimation(v);      //有动画的增加当前视图
        }
    }

    /**
     * 填充线条下方区域
     */
    private void fillLinesArea() {
        isFilled = !isFilled;       //取反即可
        setLinesDatas();            //重新设置
    }

    /**
     * 线条改变时的动画
     */
    private void changeLinesAnimate() {
        //增强for循环改变线条数据
        for (Line line : mLineData.getLines()) {
            for (PointValue value : line.getValues()) {
                value.setTarget(value.getX(), (float) Math.random() * 100);         //更改X坐标
                //value.setTarget(value.getY(), (float) Math.random() * 100);       //或者更改Y坐标
            }
        }
        mLineChartView.startDataAnimation(); //开始动画
    }

    /**
     * 点击显示节点标签
     */
    private void showOrHideLablesByPointsSelected() {
        isPointsHasSelected = !isPointsHasSelected;                     //取反即可
        mLineChartView.setValueSelectionEnabled(isPointsHasSelected);   //设置选中状态
        if (isPointsHasSelected) {
            isHasPointsLabels = false;                                  //如果点击才显示标签 则标签开始不可见
        }
        setLinesDatas();                                                //重新设置
    }


    /**
     * 节点触摸监听
     */
    private class ValueTouchListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            T.showShort(getActivity(),"选中第 " + ((int) value.getX() + 1) + " 个节点");
        }

        @Override
        public void onValueDeselected() {
            T.showShort(getActivity(),"onValueDeselected()");
        }
    }

    /**
     * 线条改变动画监听
     */
    private class ChangeLinesAnimListener implements ChartAnimationListener {

        private Viewport v;

        public ChangeLinesAnimListener(Viewport v) {
            this.v = v;
        }

        @Override
        public void onAnimationStarted() {
        }

        @Override
        public void onAnimationFinished() {
            mLineChartView.setMaximumViewport(v);                   //设置最大视图
            mLineChartView.setViewportAnimationListener(null);      //取消监听
        }
    }

    /**
     * 视差使数据看起来为动态加载（心电图效果）
     */
    private void dynamicDataDisplay() {
        mLineChartView.setInteractive(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isFinish) {
                    //实时添加新的点
                    PointValue value1 = new PointValue(position * 5, 40 + random.nextInt(20));
                    value1.setLabel("00:00");
                    pointValueList.add(value1);
                    float x = value1.getX();
                    //根据新的点的集合画出新的线
                    Line line = new Line(pointValueList);
                    line.setColor(Color.RED);
                    line.setShape(ValueShape.CIRCLE);
                    line.setCubic(true);//曲线是否平滑，即是曲线还是折线

                    linesList.clear();
                    linesList.add(line);
                    lineChartData = initDatas(linesList);
                    mLineChartView.setLineChartData(lineChartData);
                    //根据点的横坐标实时变幻坐标的视图范围
                    Viewport port;
                    if (x > 50) {
                        port = initViewPort(x - 50, x);
                    } else {
                        port = initViewPort(0, 50);
                    }
                    mLineChartView.setCurrentViewport(port);//当前窗口

                    Viewport maPort = initMaxViewPort(x);
                    mLineChartView.setMaximumViewport(maPort);//最大窗口

                    position++;
                    if (position > 100 - 1) {
                        isFinish = true;
                        mLineChartView.setInteractive(true);
                    }
                }
            }
        }, 300, 300);
    }
    private LineChartData initDatas(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setTextColor(Color.GRAY);
        axisY.setTextColor(Color.GRAY);
        axisX.setName("Axis X");
        axisY.setName("Axis Y");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        return data;
    }

    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 100;
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }

    private Viewport initMaxViewPort(float right) {
        Viewport port = new Viewport();
        port.top = 100;
        port.bottom = 0;
        port.left = 0;
        port.right = right + 50;
        return port;
    }
}
