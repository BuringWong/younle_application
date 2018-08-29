package com.younle.younle624.myapplication.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/23.
 * 微信/e-mail:tt090423@126.com
 * 生成柱状图的工具类
 */
public class MChatUtils {
    private Context context;
    private static LineData d;
    private static LineDataSet lineDataSet;
    private static int[] colors5=new int[]{Color.rgb(155,85,206),Color.rgb(255,198,64),Color.rgb(53,206,238),Color.rgb(52,143,245),Color.rgb(0,210,89)};
    private static int[] colors4=new int[]{Color.rgb(255,198,64),Color.rgb(53,206,238),Color.rgb(52,143,245),Color.rgb(0,210,89)};
    private static int[] colors3=new int[]{Color.rgb(53,206,238),Color.rgb(52,143,245),Color.rgb(0,210,89)};
    private static int[] colors2=new int[]{Color.rgb(52,143,245),Color.rgb(0,210,89)};
    private static int[] colors1=new int[]{Color.rgb(0,210,89)};
    public MChatUtils(Context context) {
        this.context=context;
    }

    /**
     * 填充数据
     * @param mChart  视图组件
     * @param xData   x轴数据
     * @param barEntries
     * @param entryList
     */
    public static void setData(CombinedChart mChart, List<String> xData, List<BarEntry> barEntries, List<Entry> entryList){
        mChart.clear();
        CombinedData data = new CombinedData(xData);//x坐标的坐标值
        data.setData(generateLineData(entryList));//折线的数据
        data.setData(generateBarData(barEntries));//柱状图的数据
        mChart.setData(data);
        mChart.getLegend().setForm(Legend.LegendForm.SQUARE);
        mChart.invalidate();
        mChart.animateXY(1000,1000);
    }
    /**
     * 初始化一些配置
     * @param mChart
     */
    public static void initMchat(Context context,CombinedChart mChart) {
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);//柱状图无阴影
        mChart.setDescription("");
        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });
        //右侧y轴
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisLineWidth(Utils.dip2px(context, 1));
        rightAxis.setAxisLineColor(Color.rgb(60,188,25));
//        rightAxis.setAxisMaxValue(5);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        rightAxis.setTextColor(Color.rgb(60,188,25));
//        rightAxis.setSpaceTop(5f);
        //左侧y坐标

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisLineColor(Color.rgb(83,174,242));
        leftAxis.setAxisLineWidth(Utils.dip2px(context, 1));
//        leftAxis.setAxisMaxValue(999999);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        leftAxis.setSpaceTop(0.1f);
        leftAxis.setTextColor(Color.rgb(83,174,242));
        //x轴相关的参数
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x周的位置
        xAxis.setDrawGridLines(false);

    }

    /**
     * 生成柱状图的数据
     * @return
     */
    public static BarData generateBarData(List<BarEntry> entries) {
        BarData d = new BarData();
        BarDataSet barDataSet = new BarDataSet(entries, "金额(元)");
        final DecimalFormat df = new DecimalFormat("0.00");
        //设置数据样式
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + df.format(value);
            }
        });
        barDataSet.setColor(Color.rgb(83, 174, 242));
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextColor(Color.rgb(0, 0, 0));
        barDataSet.setValueTextSize(10f);
//        barDataSet.setBarSpacePercent(1);
        barDataSet.setValueTextColor(Color.rgb(83, 174, 242));
        d.addDataSet(barDataSet);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        return d;
    }
    //来点随机数吧
    private static void setData(List<BarEntry> entries,HorizontalBarChart hbar) {
            BarData data = new BarData();
            IBarDataSet barDataSet=new BarDataSet(entries,"金额（元）");
            data.addDataSet(barDataSet);
            data.setValueTextSize(10f);
            hbar.setData(data);
    }

    public static LineData generateLineData(List<Entry> entries) {
        d = new LineData();
        lineDataSet = new LineDataSet(entries, "订单数量(个)");
        //设置数据样式
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int) value;
            }
        });
        lineDataSet.setColor(Color.rgb(60, 188, 25));//线的颜色
        lineDataSet.setLineWidth(1.5f);//线宽
        lineDataSet.setCircleColor(Color.rgb(60, 188, 25));//圆点的颜色
        lineDataSet.setCircleRadius(3f);//圆点的半径
        lineDataSet.setCircleColorHole(Color.rgb(60, 188, 25));
        lineDataSet.setFillColor(Color.rgb(60, 188, 25));
        lineDataSet.setDrawCubic(false);//是否自动计算函数，曲线或是直线
        lineDataSet.setDrawValues(true);//是否显示圆点对应的值
        lineDataSet.setValueTextSize(10f);//圆点对应值得字体大小
        lineDataSet.setValueTextColor(Color.rgb(60, 188, 25));//字体颜色
        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);//以哪个轴为准
        d.addDataSet(lineDataSet);

        return d;
    }
    //初始化饼状图
    public static void initPie(Context context,PieChart mChart){

        mChart.setDescription("");
//        mChart.setExtraOffsets(5, 10, 5, 5);//外边距
        mChart.setExtraOffsets(0,0,0,0);//外边距
        mChart.setDragDecelerationFrictionCoef(0.95f);//转动惯性
        mChart.setDrawHoleEnabled(true);//pie中央不绘制
        mChart.setHoleColor(Color.WHITE);//中央的颜色
        mChart.setTransparentCircleColor(Color.WHITE);//透明度
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(50f);//中心圆的半径
        mChart.setTransparentCircleRadius(56f);//半透明圈
        mChart.setDrawCenterText(true);//饼状图中间可以添加文字
        mChart.setDrawSliceText(false);
        mChart.setRotationAngle(0);//初始旋转角度
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setUsePercentValues(true);
        mChart.setBackgroundColor(Color.WHITE);
        Legend l = mChart.getLegend();
        l.setEnabled(false);
//        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);

    }
    public static void setPieData(PieChart pieChart, List<String> xValues, PieDataSet pieDataSet) {
        pieChart.clear();
        pieChart.animateY(2000, Easing.EasingOption.EaseInOutQuad);
        PieData pieData=new PieData(xValues,pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());//设置为百分比显示
        pieChart.setData(pieData);
    }

    public static void setCenterText(PieChart pieChart, String text) {
        pieChart.setCenterText(text);
        pieChart.setCenterTextSize(20);
        pieChart.setCenterTextColor(Color.rgb(61, 149, 25));
    }

    /**
     * 初始化横向柱状图
     * @param context
     * @param hbar
     */
    public static void initHBar(Context context, HorizontalBarChart hbar) {

        hbar.setDrawBarShadow(false);
        hbar.setDrawValueAboveBar(true);
        hbar.setMaxVisibleValueCount(10);
        hbar.setPinchZoom(false);
        hbar.setDrawGridBackground(false);
        hbar.setBackgroundColor(Color.WHITE);
        hbar.setDescription("");
        //y轴
        YAxis yl = hbar.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setAxisMinValue(0);
        yl.setEnabled(false);
        //y轴
        YAxis yr = hbar.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(true);
        yr.setGridColor(Color.parseColor("#f2f2f2"));
        yr.setStartAtZero(true);
        yr.setDrawZeroLine(true);
        yr.setAxisMinValue(0);


        XAxis xAxis=hbar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineWidth(1.0f);
        //设置数据
        hbar.animateY(1000);
        Legend l = hbar.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);
    }

    public static void setHBarData(HorizontalBarChart hbar, List<String> hXData,List<BarEntry> yValues,int which) {
        BarDataSet dataSet=new BarDataSet(yValues,"销量");
        switch (hXData.size()) {
            case 1 :
                dataSet.setColors(colors1);
                break;
            case 2 :
                dataSet.setColors(colors2);
                break;
            case 3 :
                dataSet.setColors(colors3);
                break;
            case 4 :
                dataSet.setColors(colors4);
                break;
            case 5 :
                dataSet.setColors(colors5);
                break;
        }
        dataSet.setDrawValues(true);
        dataSet.setBarSpacePercent(30f);
        if(which==0) {
            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return   Utils.dropZero(value+"");
                }
            });
        }else {
            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return   Utils.dropZero(value+"");
                }
            });
        }
        dataSet.setVisible(true);
        dataSet.setValueTextColor(Color.BLACK);
        BarData bData=new BarData(hXData,dataSet);
        hbar.setData(bData);
        hbar.invalidate();
    }
}
