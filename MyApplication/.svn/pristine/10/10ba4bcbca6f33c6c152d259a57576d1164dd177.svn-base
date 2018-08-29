package com.younle.younle624.myapplication.activity.manager.orderpager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.adapter.ordermanager.FilterAdapter;
import com.younle.younle624.myapplication.adapter.ordermanager.Top5Adapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.ChartDataBean;
import com.younle.younle624.myapplication.domain.PosChartBean;
import com.younle.younle624.myapplication.domain.PosOrderKinds;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.MChatUtils;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.DoubleDatePickerDialog;
import com.younle.younle624.myapplication.view.NoScrollGirdView;
import com.younle.younle624.myapplication.view.SelfImageView;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 我是奋斗 on 2016/6/3.
 * 微信/e-mail:tt090423@126.com
 */
public class ShowChartActivity<T> extends Activity implements View.OnClickListener, SelfLinearLayout.ClickToReload {

    private static final int SHOW_SELL = 1;
    private static final int SHOW_USED = 2;
    /**
     * 来自哪里，pos，服务，实物
     */
    private int fromWhere;
    /**
     * 第一部分，公共头部
     */
    private ImageView iv_title;
    private TextView tv_title;
    private TextView tv_mark_reback;
    /**
     * 第二部分，更换条件
     */
    private TextView tv_select_left;
    //    private TextView tv_filter2;
    private LinearLayout filter_layout;
    /**
     * 第三部分，更换日期
     */
    private TextView tv_title_all;
    private ImageView tv_filter;
    /**
     * 第四部分，售出或核销代金卷
     */
    private LinearLayout ll_sell_used;
    private Button btn_sell;
    private Button btn_used;
    /**
     * 柱状图部分
     */
    private TextView tv_account_desc;
    private TextView tv_account_num;
    private CombinedChart chart1;
    /**
     * 第一个饼状图部分
     */
//    private PieChart first_pie;
//    private TextView tv_pie1_desc;
    /**
     * 第二个饼状图部分
     */
//    private PieChart second_pie;
//    private TextView tv_pie2_desc;
//    private TextView tv_pie2_title;
    /**
     * 透明图层
     */
    private SelfImageView tv_transfer;
    /**
     * 筛选的控件
     */
    private ListView lv_all_store;
    private LinearLayout ll_listviews_continer;

    private LinearLayout ll_service_kinds;
    private LinearLayout ll_selector_add;
    private ListView lv_for_filter;
    //private ListView lv_choosed_goods;
    private View view_line;
    public TextView tv_choose_kinds_one;
    public TextView tv_choose_kinds_two;
    public TextView tv_choose_kinds_three;
    public TextView tv_choose_kinds_four;
    public TextView tv_choose_kinds_five;

    private ArrayList colors;
    private View pupView;
    private PopupWindow popupWindow;
    private String beforeDate;
    private List<String> barXData;
    private List<String> pie1XData;
    private List<String> pie2XData;
    private List<BarEntry> barY1;
    private List<Entry> barY2;
    private ArrayList<Entry> pieY1;
    private ArrayList<Entry> pieY2;
    private String isUsed = "0";

    private DoubleDatePickerDialog doubleDatePickerDialog;
    private boolean everFilter = false;
    private String fAccount;
    private DecimalFormat df;
    /**
     * 从服务器拿到的图形列表的数据
     */
    private ChartDataBean chartDataBean;
    private PosChartBean posChartBean;
    private String startFormat;
    private String endFormat;
    private String goodId ="";
    private String sizeId ="";
    private TextView tv_current_nodata;

    /**
     * 加载中的视图
     */
    private SelfLinearLayout ll_loading;
    private ProgressBar pb_loading;
    private TextView tv_loading;
    private String TAG = "ShowChartActivity";

    /**
     * 成交总额
     */
    private double totalAccount;
    /**
     * 成交单数
     */
    private int totalNum;
    private boolean everGetdata = false;
    private boolean everGetCheckedData = false;

    /**
     * 请求url
     */
    private String url;

    private FilterAdapter kindAdapter;
    private List<PosOrderKinds> kinds = new ArrayList<>();
    private int firstPos;
    private int secPos = 0;
    List<PosChartBean.MsgBean.GoodsNameListBean> goods;
    private FilterAdapter filterDetailAdapter;
    public FilterAdapter filterAdapter = new FilterAdapter(this);
    public FilterAdapter goods_filterAdapter = new FilterAdapter(this);
    private TextView tv_date_exception;
    private ImageView iv_jiazai_filure;

    /**
     * 顶部右侧的筛选
     */
    public AutoRelativeLayout rl_chart_select_right;
    public TextView tv_select_right;
    public ImageView right_icon;
    /**
     * 顶部左侧的筛选
     */
    private AutoRelativeLayout rl_chart_select_left;
    public View payway_center_line;
    //public ListView lv_right;
    private int PAY_WAY = 1;
    private int SELECT_FROM = 0;
    private List<PosOrderKinds> rightSelectData;
    private ImageView left_icon;
    private boolean close1 = true;
    private boolean close2 = true;
    private boolean close3 = true;
    private int LAST_CLICK = 0;
    private List<ChartDataBean.MsgBean.GoodsNameBean> leftSelectData;
    private String right_selected = "-1";
    private int left_filter_datatype;
    private String intent_left_title;
    private RelativeLayout rl_date_choose;
    private int[] three_layer_pos = new int[]{-1,-1,-1};
    private boolean isShow = false;//当前有没有下拉框显示
    private int isClickLeft;
    private List<PosChartBean.MsgBean.GoodsNameListBean> showHor;
    private RotateAnimation ra;
    private String payTool = "0";
    private String goodstype = "0";
    private boolean hasMemberSys;
    private boolean isFirst = true;

    /**
     * top5横向柱状图
     */
    private HorizontalBarChart hbar;
    private RadioButton rb_by_num;
    private RadioButton rb_by_income;
    private final int SALE_NUM=0;
    private final int SALE_INCOME=1;
    private List<String> hXData;
    private List<PosChartBean.MsgBean.SaledTop5Bean> saledTop5=new ArrayList<>();
    private List<PosChartBean.MsgBean.IncomeTop5Bean> incomeTop5=new ArrayList<>();
    private NoScrollGirdView gv_top5;
    private TextView tv_top5_nodata;
    private RelativeLayout rl_top5;
    private ScrollView chart_scroll_view;

    private RelativeLayout rl_about_chart;
    private LinearLayout all_order_two_header;
    private RadioButton rb_statistics_by_order;
    private RadioButton rb_statistics_by_income;
    private View line_which_display_order;
    private View line_which_display_income;
    private TextView tv_explain_statistics;
    private int ordertype;
    private boolean resetLeftRight = false;
    public AutoRelativeLayout rl_select_all_tools;
    public TextView tv_all_tools;
    public ImageView iv_tools_icon;
    public List<PosOrderKinds> payToolsSelectData;
    private String sdate;
    private String stime;
    private String edate;
    private String etime;
    private Integer startHour;
    private Integer startMinute;
    private Integer endHour;
    private Integer endMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_base);
        Utils.initToolBarState(this);
        //1.获取intent数据
        showWhat();
        //2.初始化视图组件
        initView();
        //3.初始化数据
        initData();
        //4.设置监听
        setListener();
    }

    /**
     * 要展示那类数据
     */
    private void showWhat() {
        Intent intent = getIntent();
        fromWhere = intent.getIntExtra("from", -1);

        startFormat = intent.getStringExtra(Constant.START_DATE);
        endFormat = intent.getStringExtra(Constant.END_DATE);
        sdate = startFormat.substring(0, startFormat.indexOf(" "));
        stime = startFormat.substring(startFormat.indexOf(" ") + 1);
        String[] split = stime.split(":");
        startHour=Integer.valueOf(split[0]);
        startMinute=Integer.valueOf(split[1]);

        edate = endFormat.substring(0, endFormat.indexOf(" "));
        etime = endFormat.substring(endFormat.indexOf(" ") + 1);
        String[] ts = etime.split(":");
        endHour=Integer.valueOf(ts[0]);
        endMinute=Integer.valueOf(ts[1]);

        goodId = intent.getStringExtra(Constant.GOOD_ID);
        sizeId = intent.getStringExtra("sizeid");

        intent_left_title = intent.getStringExtra("left_title");//左侧筛选数据的初始化
        right_selected = intent.getStringExtra("right_selected");//右侧筛选数据的初始化
        three_layer_pos = intent.getIntArrayExtra("record_pos");//接收pos
        goodstype=intent.getStringExtra(Constant.GOOD_TYPE);
        isUsed = right_selected;
        LogUtils.e(TAG, "goodId == " + goodId);
        ordertype = intent.getIntExtra("ordertype", 0);
        payTool = intent.getStringExtra("payTool");
        hasMemberSys = intent.getBooleanExtra("hasMemberSys", false);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        iv_title.setOnClickListener(this);
//        first_pie.setOnChartValueSelectedListener(new MyOnChartValueSelectedListener());
//        second_pie.setOnChartValueSelectedListener(new MyOnChartValueSelectedListener2());
        tv_filter.setOnClickListener(this);
        lv_all_store.setOnItemClickListener(new FilterItemClickListener());
        lv_for_filter.setOnItemClickListener(new KindsItemOnClickListener());
        btn_sell.setOnClickListener(this);
        btn_used.setOnClickListener(this);
        ll_loading.setClickToReload(this);

        MyTextListener myTextListener = new MyTextListener();
        tv_choose_kinds_one.setOnClickListener(myTextListener);
        tv_choose_kinds_three.setOnClickListener(myTextListener);
        tv_choose_kinds_four.setOnClickListener(myTextListener);
        tv_choose_kinds_five.setOnClickListener(myTextListener);
        tv_choose_kinds_two.setOnClickListener(myTextListener);

        rl_date_choose.setOnClickListener(this);
        //左右侧筛选的监听
        rl_chart_select_right.setOnClickListener(this);
        rl_chart_select_left.setOnClickListener(this);
        rl_select_all_tools.setOnClickListener(this);
        //点击空白的监听
        ll_selector_add.setOnClickListener(new TransFerOnClickListener());

        //top5按销量和按销售额排序
        rb_by_income.setOnClickListener(this);
        rb_by_num.setOnClickListener(this);

        rb_statistics_by_income.setOnClickListener(this);
        rb_statistics_by_order.setOnClickListener(this);
    }

    /**
     * 点击空白处收起筛选栏
     */
    class TransFerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LogUtils.Log("点击空白");
            changeCurrentState(3);//传3代表
        }
    }

    /**
     * 1.联网请求新数据之-----根据商品筛选
     * 筛选item的点击监听
     */
    class FilterItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            changeCurrentState(1);
            ChartDataBean.MsgBean.GoodsNameBean goodsNameBean;
            switch (fromWhere) {
                case Constant.SERVICE_DATA:
                    goodsNameBean = leftSelectData.get(position);
                    tv_select_left.setText(goodsNameBean.getName());
                    goodId = goodsNameBean.getId();
                    getServiceDataFromIntent();
                    break;
                case Constant.ENTITY_DATA:
                    goodsNameBean = leftSelectData.get(position);
                    tv_select_left.setText(goodsNameBean.getName());
                    goodId = goodsNameBean.getId();
                    getEntityDataFromIntent();
                    break;
                case Constant.POS_DATA:
                    //增加情况判断：如果没有点击第二级，直接点击第三级：会造成空指针
                    if (three_layer_pos[0] == -1) {
                        three_layer_pos[0] = 0;
                    }
                    if (three_layer_pos[1] == -1) {
                        three_layer_pos[1] = 0;
                    }
                    goodstype = String.valueOf(showHor.get(three_layer_pos[0]).getGoodsType());
                    recoredPos(2, position);
                    goodId = String.valueOf(showHor.get(three_layer_pos[0]).getCategory().get(three_layer_pos[1]).getGoodsList().get(position).getId());
                    sizeId = String.valueOf(showHor.get(three_layer_pos[0]).getCategory().get(three_layer_pos[1]).getGoodsList().get(position).getSizeId());
                    goodstype = showHor.get(three_layer_pos[0]).getCategory().get(three_layer_pos[1]).getGoodsList().get(position).getGoodsType();
                    tv_transfer.setVisibility(View.GONE);
                    Utils.closeAnimation(ll_selector_add);
                    ll_selector_add.setVisibility(View.GONE);
                    getChooseDataFromNet(goodId, isClickLeft);
                    tv_select_left.setText(showHor.get(three_layer_pos[0]).getCategory().get(three_layer_pos[1]).getGoodsList().get(position).getName());
                    setShowdataBox(false);
                    break;
            }
        }
    }

    /**
     * 类品点击监听
     */
    class KindsItemOnClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (isClickLeft==0) {//点击的是左侧
                LogUtils.e(TAG, "点击的是左侧");
                recoredPos(1, position);
                List<PosChartBean.MsgBean.GoodsNameListBean.CategoryBean> category = showHor.get(three_layer_pos[0]).getCategory();
                for (int i = 0; i < category.size(); i++) {
                    if (position == i) {
                        category.get(i).setIsChecked(true);
                    } else {
                        category.get(i).setIsChecked(false);
                    }
                }
                filterAdapter.notifyDataSetChanged();
                List<PosChartBean.MsgBean.GoodsNameListBean.CategoryBean.GoodsListBean> goodsList = category.get(position).getGoodsList();
                if (goodsList != null && goodsList.size() > 0) {
                    LogUtils.e(TAG, "横行 -- 底下有数据...");
                    goods_filterAdapter.setData(goodsList);
                    goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_CHART_GOODS);
                    lv_all_store.setAdapter(goods_filterAdapter);
                } else {
                    hide(1);
                    getChooseDataFromNet(String.valueOf(position), isClickLeft);
                    setShowdataBox(false);
                }
            } else if(isClickLeft==1){
                LogUtils.e(TAG, "点击的是右侧");
                getChooseDataFromNet(String.valueOf(position), isClickLeft);
                setShowdataBox(false);
            }else{
                payTool = payToolsSelectData.get(position).getId();
                getChooseDataFromNet(String.valueOf(position),isClickLeft);
                setShowdataBox(false);
            }
        }
    }

    /**
     * 选择类别点击监听
     * 种类的筛选
     */
    class MyTextListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_choose_kinds_one:
                    recoredPos(0, 0);
                    chooseHorKinds(0, false);
                    break;
                case R.id.tv_choose_kinds_two:
                    recoredPos(0, 1);
                    chooseHorKinds(1, false);
                    break;
                case R.id.tv_choose_kinds_three:
                    recoredPos(0, 2);
                    chooseHorKinds(2, false);
                    break;
                case R.id.tv_choose_kinds_four:
                    recoredPos(0, 3);
                    chooseHorKinds(3, false);
                    break;
                case R.id.tv_choose_kinds_five://直接收银
                    recoredPos(0, 4);
                    chooseHorKinds(4, false);
                    break;
            }
        }
    }

    /**
     * 设置左侧横行显示数据
     */
    private boolean showHorChooseItem() {
        int size = showHor.size();
        switch (size) {
            case 0:
                Utils.showToast(this, "暂无品类选择数据");
                return false;
            case 1:
                tv_choose_kinds_one.setVisibility(View.VISIBLE);
                tv_choose_kinds_one.setText(showHor.get(0).getTypeName());
                tv_choose_kinds_two.setVisibility(View.GONE);
                tv_choose_kinds_three.setVisibility(View.GONE);
                tv_choose_kinds_four.setVisibility(View.GONE);
                tv_choose_kinds_five.setVisibility(View.GONE);
                return true;
            case 2:
                tv_choose_kinds_one.setVisibility(View.VISIBLE);
                tv_choose_kinds_one.setText(showHor.get(0).getTypeName());
                tv_choose_kinds_two.setVisibility(View.VISIBLE);
                tv_choose_kinds_two.setText(showHor.get(1).getTypeName());
                tv_choose_kinds_three.setVisibility(View.GONE);
                tv_choose_kinds_four.setVisibility(View.GONE);
                tv_choose_kinds_five.setVisibility(View.GONE);
                return true;
            case 3:
                tv_choose_kinds_one.setVisibility(View.VISIBLE);
                tv_choose_kinds_one.setText(showHor.get(0).getTypeName());
                tv_choose_kinds_two.setVisibility(View.VISIBLE);
                tv_choose_kinds_two.setText(showHor.get(1).getTypeName());
                tv_choose_kinds_three.setVisibility(View.VISIBLE);
                tv_choose_kinds_three.setText(showHor.get(2).getTypeName());
                tv_choose_kinds_four.setVisibility(View.GONE);
                tv_choose_kinds_five.setVisibility(View.GONE);
                return true;
            case 4:
                tv_choose_kinds_one.setVisibility(View.VISIBLE);
                tv_choose_kinds_one.setText(showHor.get(0).getTypeName());
                tv_choose_kinds_two.setVisibility(View.VISIBLE);
                tv_choose_kinds_two.setText(showHor.get(1).getTypeName());
                tv_choose_kinds_three.setVisibility(View.VISIBLE);
                tv_choose_kinds_three.setText(showHor.get(2).getTypeName());
                tv_choose_kinds_four.setVisibility(View.VISIBLE);
                tv_choose_kinds_four.setText(showHor.get(3).getTypeName());
                tv_choose_kinds_five.setVisibility(View.GONE);
                return true;
            case 5:
                tv_choose_kinds_one.setVisibility(View.VISIBLE);
                tv_choose_kinds_one.setText(showHor.get(0).getTypeName());
                tv_choose_kinds_two.setVisibility(View.VISIBLE);
                tv_choose_kinds_two.setText(showHor.get(1).getTypeName());
                tv_choose_kinds_three.setVisibility(View.VISIBLE);
                tv_choose_kinds_three.setText(showHor.get(2).getTypeName());
                tv_choose_kinds_four.setVisibility(View.VISIBLE);
                tv_choose_kinds_four.setText(showHor.get(3).getTypeName());
                tv_choose_kinds_five.setVisibility(View.VISIBLE);
                tv_choose_kinds_five.setText(showHor.get(4).getTypeName());
                return true;
        }
        return false;
    }

    /**
     * 记录选择的位置
     *
     * @param pos
     * @param id
     */
    private void recoredPos(int pos, int id) {
        three_layer_pos[pos] = id;
        switch (pos) {
            case 0:
                clearNamedPos(1);
            case 1:
                clearNamedPos(2);
                break;
        }
    }

    private void clearNamedPos(int pos) {
        three_layer_pos[pos] = -1;
    }

    /**
     * 点击屏幕重新加载的回调
     */
    @Override
    public void ClickToReload() {
        initData();
    }

    /**
     * 图例1被选中的监听
     */
    class MyOnChartValueSelectedListener implements OnChartValueSelectedListener {
        @Override
        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
            int xIndex = h.getXIndex();
            float vpercent = e.getVal();
            String percent = Constant.numDf.format(vpercent * 100) + "%";
            String account = df.format(vpercent * totalAccount);
//            tv_pie1_desc.setText("商品名称：" + pie1XData.get(xIndex) + '\n' + "占比：" + percent
//                    + '\t' + "收入金额：" + account + "元");
        }

        @Override
        public void onNothingSelected() {

        }
    }

    /**
     * 图例2被选中的监听
     */
    class MyOnChartValueSelectedListener2 implements OnChartValueSelectedListener {
        @Override
        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
            int xIndex = h.getXIndex();
            float vpercent = e.getVal();
            String percent = vpercent * 100 + "%";
            String account = df.format(vpercent * totalAccount);
//            tv_pie2_desc.setText("收入来源：" + pie2XData.get(xIndex) + '\n' + "占比：" + percent
//                    + '\t' + "收入金额：" + account + "元");
        }

        @Override
        public void onNothingSelected() {

        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        chart_scroll_view = (ScrollView)findViewById(R.id.chart_scroll_view);
        rl_top5 = (RelativeLayout)findViewById(R.id.rl_top5);
        rl_about_chart = (RelativeLayout)findViewById(R.id.rl_about_chart);
        df = new DecimalFormat("0.00");
        //第一部分，公共头部
        iv_title = (ImageView) findViewById(R.id.iv_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_mark_reback = (TextView) findViewById(R.id.tv_mark_reback);
        //第二部分，更换日期
        rl_date_choose = (RelativeLayout) findViewById(R.id.rl_date_choose);
        tv_filter = (ImageView) findViewById(R.id.tv_filter);
        tv_title_all = (TextView) findViewById(R.id.tv_title_date);
        //第四部分，售出或核销代金卷
        ll_sell_used = (LinearLayout) findViewById(R.id.ll_sell_used);
        btn_used = (Button) findViewById(R.id.btn_used);
        btn_sell = (Button) findViewById(R.id.btn_sell);
        //柱状图部分
        tv_account_desc = (TextView) findViewById(R.id.tv_account_desc);
        tv_account_num = (TextView) findViewById(R.id.tv_account_num);
        chart1 = (CombinedChart) findViewById(R.id.chart1);
        //第一个饼状图
//        first_pie = (PieChart) findViewById(R.id.first_pie);
//        tv_pie1_desc = (TextView) findViewById(R.id.tv_pie1_desc);
        //第二个饼状图部分
//        second_pie = (PieChart) findViewById(R.id.second_pie);
//        tv_pie2_desc = (TextView) findViewById(R.id.tv_pie2_desc);
//        tv_pie2_title = (TextView) findViewById(R.id.tv_pie2_title);
        //透明图层
        tv_transfer = (SelfImageView) findViewById(R.id.tv_transfer);
        tv_transfer.setAlpha(0.4f);
        //pup部分
        popupWindow = new PopupWindow(ShowChartActivity.this);
        pupView = View.inflate(this, R.layout.popuview, null);
        //筛选
//        ll_listviews_continer = (LinearLayout) findViewById(R.id.ll_listviews_continer);
        lv_all_store = (ListView) findViewById(R.id.lv_all_store);
        //当前筛选条件下没有数据
        tv_current_nodata = (TextView) findViewById(R.id.tv_current_nodata);
        //各种异常的图表
        iv_jiazai_filure = (ImageView) findViewById(R.id.iv_jiazai_filure);
        //加载中
        ll_loading = (SelfLinearLayout) findViewById(R.id.ll_loading);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        tv_loading = (TextView) findViewById(R.id.tv_loading);

        ll_service_kinds = (LinearLayout) findViewById(R.id.ll_service_kinds);
        ll_selector_add = (LinearLayout) findViewById(R.id.ll_selector_add);
        lv_all_store = (ListView) findViewById(R.id.lv_all_store);
        view_line = findViewById(R.id.view_line);
        lv_for_filter = (ListView) findViewById(R.id.lv_for_filter);
        lv_for_filter.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        tv_choose_kinds_one = (TextView) findViewById(R.id.tv_choose_kinds_one);
        tv_choose_kinds_two = (TextView) findViewById(R.id.tv_choose_kinds_two);
        tv_choose_kinds_three = (TextView) findViewById(R.id.tv_choose_kinds_three);
        tv_choose_kinds_four = (TextView) findViewById(R.id.tv_choose_kinds_four);
        tv_choose_kinds_five = (TextView) findViewById(R.id.tv_choose_kinds_five);
        ll_listviews_continer = (LinearLayout) findViewById(R.id.ll_listviews_continer);

        tv_date_exception = (TextView) findViewById(R.id.tv_date_exception);

        //右侧筛选的视图组件
        rl_chart_select_right = (AutoRelativeLayout) findViewById(R.id.rl_select_right);
        tv_select_right = (TextView) findViewById(R.id.tv_select_right);
        right_icon = (ImageView) findViewById(R.id.iv_right_icon);

        payway_center_line = findViewById(R.id.payway_center_line);

        //左侧筛选的视图组件
        rl_chart_select_left = (AutoRelativeLayout) findViewById(R.id.rl_select_left);
        tv_select_left = (TextView) findViewById(R.id.tv_select_left);
        left_icon = (ImageView) findViewById(R.id.iv_left_icon);
        filter_layout = (LinearLayout) findViewById(R.id.filter_layout);

        //横向柱状图
        hbar = (HorizontalBarChart)findViewById(R.id.hbar);
        rb_by_num = (RadioButton)findViewById(R.id.rb_by_num);
        rb_by_income = (RadioButton)findViewById(R.id.rb_by_income);
        gv_top5 = (NoScrollGirdView)findViewById(R.id.gv_top5);
        tv_top5_nodata = (TextView)findViewById(R.id.tv_top5_nodata);
        //屏蔽pos左侧的筛选
        /*switch (fromWhere) {
            case Constant.POS_DATA:
                //left_icon.setVisibility(View.GONE);
                //rl_chart_select_left.setEnabled(false);
                left_icon.setVisibility(View.VISIBLE);
                rl_chart_select_left.setEnabled(true);
                break;
        }*/

        //订单统计页新加
        all_order_two_header = (LinearLayout)findViewById(R.id.all_order_two_header);
        rb_statistics_by_order = (RadioButton)findViewById(R.id.rb_statistics_by_order);
        rb_statistics_by_income = (RadioButton)findViewById(R.id.rb_statistics_by_income);
        line_which_display_order = findViewById(R.id.line_which_display_order);
        line_which_display_income = findViewById(R.id.line_which_display_income);
        tv_explain_statistics = (TextView) findViewById(R.id.tv_explain_statistics);

        //工具收款
        rl_select_all_tools = (AutoRelativeLayout)findViewById(R.id.rl_select_all_tools);
        rl_select_all_tools.setVisibility(View.VISIBLE);
        tv_all_tools = (TextView)findViewById(R.id.tv_all_tools);
        iv_tools_icon = (ImageView)findViewById(R.id.iv_tools_icon);

        if(hasMemberSys&&isFirst){
            all_order_two_header.setVisibility(View.VISIBLE);
            rb_statistics_by_income.setChecked(true);
            isFirst = false;
        }

        //rg设置选中
        if(ordertype==1){
            rb_statistics_by_income.setChecked(false);
            rb_statistics_by_order.setChecked(true);
            line_which_display_income.setVisibility(View.GONE);
            line_which_display_order.setVisibility(View.VISIBLE);
            rl_select_all_tools.setVisibility(View.GONE);
            rl_chart_select_left.setVisibility(View.GONE);
            tv_explain_statistics.setText(getString(R.string.order_statistics));
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //公共头部
        tv_title.setText("数据图表");
        iv_title.setVisibility(View.VISIBLE);
        //加载视图显示
        reLoad();
        //顶部筛选，右侧筛选数据以及顶部文本
        initRightSelectData();
        //左侧筛选数据的初始化
        LogUtils.Log("左侧数据" + intent_left_title);
        tv_select_left.setText(intent_left_title);

        boolean available = NetworkUtils.isAvailable(this);
        if (!available) {
            netError();
        } else {
            initParams();
        }
    }

    private void initServiceEntityRightData() {
        if ("0".equals(right_selected)) {
            tv_select_right.setText(rightSelectData.get(0).getName());
        } else if ("1".equals(right_selected)) {
            tv_select_right.setText(rightSelectData.get(1).getName());
        }
    }

    /**
     * 初始化右侧筛选数据
     */
    private void initRightSelectData() {
        rightSelectData = new ArrayList<>();
        switch (fromWhere) {
            case Constant.POS_DATA:
                initPosRightSelectData();
                break;
            case Constant.SERVICE_DATA:
                rightSelectData.add(new PosOrderKinds("售出订单", "0"));
                rightSelectData.add(new PosOrderKinds("核销订单", "1"));
                initServiceEntityRightData();
                break;
            case Constant.ENTITY_DATA:
                rightSelectData.add(new PosOrderKinds("售出订单", "0"));
                rightSelectData.add(new PosOrderKinds("完成订单", "1"));
                initServiceEntityRightData();
                break;
        }
    }

    /**
     * pos图形列表右侧的筛选数据
     */
    private void initPosRightSelectData() {
        rightSelectData.add(new PosOrderKinds("全部渠道", "-1"));
        rightSelectData.add(new PosOrderKinds("微信收款(记账)","8"));
        if (Constant.OPEN_WXPAY == 1) {
            rightSelectData.add(new PosOrderKinds("微信收款", "0"));
        }
        rightSelectData.add(new PosOrderKinds("支付宝收款(记账)","9"));
        if (Constant.OPEN_ALIPAY == 1) {
            rightSelectData.add(new PosOrderKinds("支付宝收款", "1"));
        }
        rightSelectData.add(new PosOrderKinds("刷卡收款(记账)", "6"));
        rightSelectData.add(new PosOrderKinds("现金收款(记账)", "3"));
        if(ordertype!=1){
            rightSelectData.add(new PosOrderKinds("会员卡余额收款", "4"));
        }
        switch (right_selected) {
            case "-1":
                tv_select_right.setText("全部渠道");
                break;
            case "0":
                tv_select_right.setText("微信收款");
                break;
            case "1":
                tv_select_right.setText("支付宝收款");
                break;
            case "2":
                tv_select_right.setText("刷卡收款");
                break;
            case "3":
                tv_select_right.setText("现金收款(记账)");
                break;
            case "4":
                tv_select_right.setText("会员卡余额收款");
                break;
            case "6":
                tv_select_right.setText("刷卡收款(记账)");
                break;
            case "8":
                tv_select_right.setText("微信收款(记账)");
                break;
            case "9":
                tv_select_right.setText("支付宝收款(记账)");
                break;
        }

        //收款工具
        payToolsSelectData = new ArrayList<>();
        payToolsSelectData.add(new PosOrderKinds("全部工具", "0"));
        payToolsSelectData.add(new PosOrderKinds("收银机", "1"));
        payToolsSelectData.add(new PosOrderKinds("收款二维码", "2"));
        payToolsSelectData.add(new PosOrderKinds("自助点单小程序", "3"));
        LogUtils.e(TAG,"payTool="+payTool);
        switch (payTool) {
            case "0":
                tv_all_tools.setText("全部工具");
                break;
            case "1":
                tv_all_tools.setText("收银机");
                break;
            /*case "3":
                tv_all_tools.setText("收款码收款");
                break;*/
            case "2":
                tv_all_tools.setText("收款二维码");
                break;
            case "3":
                tv_all_tools.setText("自助点单小程序");
                break;
        }
    }

    private void initParams() {
        tv_title_all.setText(startFormat + "至" + endFormat);
        MChatUtils.initMchat(this, chart1);
        MChatUtils.initHBar(this, hbar);
//        MChatUtils.initPie(this, first_pie);
        LogUtils.e(TAG, "fromWhere = " + fromWhere);
        switch (fromWhere) {
            case Constant.POS_DATA:
                //初始化顶部栏选项栏
                tv_choose_kinds_three.setText("实物");
                tv_choose_kinds_four.setText("服务");
                tv_choose_kinds_five.setText("房间");
                tv_account_desc.setText("收款总额及订单数");
                /*MChatUtils.initMchat(this, chart1);
                MChatUtils.initPie(this, first_pie);*/
                url = UrlConstance.POS_CHART;
                getPosDataFromIntent();
                break;
            case Constant.SERVICE_DATA:
                tv_account_desc.setText("预期收入及交易金额");
                //显示第二个饼状图
//                second_pie.setVisibility(View.VISIBLE);
//                tv_pie2_desc.setVisibility(View.VISIBLE);
//                tv_pie2_title.setVisibility(View.VISIBLE);
                /*MChatUtils.initMchat(this, chart1);
                MChatUtils.initPie(this, first_pie);*/
//                MChatUtils.initPie(this, second_pie);
                url = UrlConstance.SERVICE_CHART;
                getServiceDataFromIntent();
                break;
            case Constant.ENTITY_DATA:
                tv_account_desc.setText("预期收入及售出订单数");
                //显示第二个饼状图
//                second_pie.setVisibility(View.VISIBLE);
//                tv_pie2_desc.setVisibility(View.VISIBLE);
//                tv_pie2_title.setVisibility(View.VISIBLE);
               /* MChatUtils.initMchat(this, chart1);
                MChatUtils.initPie(this, first_pie);*/
//                MChatUtils.initPie(this, second_pie);
                url = UrlConstance.ENTITY_CHART;
                getEntityDataFromIntent();
                break;
        }
    }

    /**
     * POS机订单的联网请求
     */
    private void getPosDataFromIntent() {
//        tv_pie1_desc.setText("点击图标的某一部分，查看详情");
        String currentTime = Utils.getCurrentTime();

        LogUtils.e(TAG, "currentTime1=" + currentTime);
        LogUtils.e(TAG, "userAccount==" + Constant.USER_ACCOUNT);
        LogUtils.e(TAG, "uid==" + Constant.ADV_ID);
        LogUtils.e(TAG, "storeid==" + Constant.STORE_ID);
        LogUtils.e(TAG, "start==" + startFormat);
        LogUtils.e(TAG, "end==" + endFormat);
        LogUtils.e(TAG, "goodsid==" + goodId);
        LogUtils.e(TAG, "goodstype==" + goodstype);
        LogUtils.e(TAG, "paytype==" + right_selected);
        LogUtils.e(TAG, "ordertype==" + String.valueOf(ordertype));
        LogUtils.e(TAG, "payTool==" + payTool);
        String token = Utils.getToken(currentTime, this);
        OkHttpUtils.post()
                .url(url)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_UID, Constant.ADV_ID)
                .addParams(Constant.PARAMS_NEME_STORE_ID, Constant.STORE_ID)
                .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams("start", startFormat)
                .addParams("end", endFormat)
                .addParams("goodsid", goodId)
                .addParams("sizeid", sizeId)
                .addParams("goodstype", goodstype)
                .addParams("payType", right_selected)
                .addParams("ordertype", String.valueOf(ordertype))
                .addParams("payTool", payTool)
                .addParams("version", "new")
                .addParams("versionCode",Constant.VERSION_CODE+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        loadFailure(R.string.try_later);
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "POS返回数据：" + response);
                        boolean toNextStep = Utils.checkSaveToken(ShowChartActivity.this, response);
                        if (toNextStep) {
                            praseJson(response);
                        }
                    }
                });
    }

    /**
     * 实物类的联网请求
     */
    private void getEntityDataFromIntent() {
//        tv_pie1_desc.setText("点击图标的某一部分，查看详情");
//        tv_pie2_desc.setText("点击图标的某一部分，查看详情");
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        LogUtils.e(TAG, "--------------------------------------------");
        LogUtils.e(TAG, "troken=" + token);
        LogUtils.e(TAG, "currentTime2=" + currentTime);
        LogUtils.e(TAG, "useraccount=" + Constant.USER_ACCOUNT);
        LogUtils.e(TAG, "numimei=" + Constant.DEVICE_IMEI);
        LogUtils.e(TAG, "devicenm=" + Constant.DEVICE_NAME);
        LogUtils.e(TAG, "posmod=" + Constant.DEVICE_MODEL);
        LogUtils.e(TAG, "uid=" + Constant.ADV_ID);
        LogUtils.e(TAG, "storeid=" + Constant.STORE_ID);
        LogUtils.e(TAG, "userkey=" + MD5.md5(MD5.md5(Constant.PASSWORD)));
        LogUtils.e(TAG, "start=" + startFormat);
        LogUtils.e(TAG, "end=" + endFormat);
        LogUtils.e(TAG, "goodsid=" + goodId);
        LogUtils.e(TAG, "orderType=" + right_selected);
        LogUtils.e(TAG, "--------------------------------------------");

        OkHttpUtils.post()
                .url(url)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_UID, Constant.ADV_ID)
                .addParams(Constant.PARAMS_NEME_STORE_ID, Constant.STORE_ID)
                .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("start", startFormat)
                .addParams("end", endFormat)
                .addParams("goodsid", goodId)
                .addParams("orderType", right_selected)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        loadFailure(R.string.click_to_reload);
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "实物图表返回response" + response);
                        boolean toNextStep = Utils.checkSaveToken(ShowChartActivity.this, response);
                        if (toNextStep) {
                            praseJson(response);
                        }
                    }
                });
    }

    /**
     * 服务类的联网请求
     * 核销的代金券数据
     */
    private void getServiceDataFromIntent() {
//        tv_pie1_desc.setText("点击图标的某一部分，查看详情");
//        tv_pie2_desc.setText("点击图标的某一部分，查看详情");
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        LogUtils.e(TAG, "token=" + token);
        LogUtils.e(TAG, "currentTime3=" + currentTime);
        LogUtils.e(TAG, "useraccount=" + Constant.USER_ACCOUNT);
        LogUtils.e(TAG, "numimei=" + Constant.DEVICE_IMEI);
        LogUtils.e(TAG, "devicenm=" + Constant.DEVICE_NAME);
        LogUtils.e(TAG, "posmod=" + Constant.DEVICE_MODEL);
        LogUtils.e(TAG, "uid=" + Constant.ADV_ID);
        LogUtils.e(TAG, "userkey=" + MD5.md5(MD5.md5(Constant.PASSWORD)));
        LogUtils.e(TAG, "start=" + startFormat);
        LogUtils.e(TAG, "end=" + endFormat);
        LogUtils.e(TAG, "isUsed=" + isUsed);
        LogUtils.e(TAG, "goodsid=" + goodId);

        OkHttpUtils.post()
                .url(url)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_UID, Constant.ADV_ID)
                .addParams(Constant.PARAMS_NEME_STORE_ID, Constant.STORE_ID)
                .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams("start", startFormat)
                .addParams("end", endFormat)
                .addParams("isUsed", isUsed + "")
                .addParams("goodsid", goodId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        loadFailure(R.string.click_to_reload);
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "服务返回数据：response = " + response);
                        boolean toNextStep = Utils.checkSaveToken(ShowChartActivity.this, response);
                        if (toNextStep) {
                            praseJson(response);
                        }
                    }
                });
    }

    /**
     * 解析json数据
     *
     * @param json
     */
    private void praseJson(String json) {
        ll_loading.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            JSONObject msgObject = jsonObject.getJSONObject("msg");
            JSONArray barArray = msgObject.getJSONArray("bar");
            if (code == 200) {
                Gson gson = new Gson();
                switch (fromWhere) {
                    case Constant.POS_DATA:
                        posChartBean = gson.fromJson(json, PosChartBean.class);
                        showHor = posChartBean.getMsg().getGoodsNameList();
                        //设置header数据
                        if (posChartBean.getMsg().getBar().size() <= 0) {
                            tv_date_exception.setText("没有当前条件下没有数据，请更换筛选条件！");
                            tv_date_exception.setVisibility(View.VISIBLE);
                        } else {
                            tv_date_exception.setVisibility(View.GONE);
                            devideData();
                        }
                        break;
                    case Constant.ENTITY_DATA:
                    case Constant.SERVICE_DATA:
                        if (barArray.length() > 0) {
                            tv_date_exception.setVisibility(View.GONE);
                            chartDataBean = gson.fromJson(json, ChartDataBean.class);
                            leftSelectData = chartDataBean.getMsg().getGoodsName();
                            left_filter_datatype = Constant.CHART_ENTITY_LEFT;
                            devideData();
                        } else {
                            tv_date_exception.setText("没有当前条件下没有数据，请更换筛选条件！");
                            tv_date_exception.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                tv_current_nodata.setVisibility(View.GONE);
            } else {
                tv_current_nodata.setVisibility(View.VISIBLE);
                tv_current_nodata.setText(R.string.nodata);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 联网获取数据失败
     */
    public void loadFailure(int stringID) {//服务器异常
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.couldnot_get_data);
        tv_loading.setText(stringID);
        pb_loading.setVisibility(View.GONE);
    }

    public void reLoad() {//加载视图显示
        ll_loading.setVisibility(View.VISIBLE);
        pb_loading.setVisibility(View.VISIBLE);
        tv_loading.setText(R.string.try_loading);
        iv_jiazai_filure.setVisibility(View.GONE);
    }

    public void netError() {
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.net_error);
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("请检查网络后，点击屏幕重新加载！");
    }

    /**
     * 解析数据
     */
    private void devideData() {
        totalNum = 0;
        totalAccount = 0;
        barXData = new ArrayList<>();
        barY1 = new ArrayList();
        barY2 = new ArrayList<>();
        pieY1 = new ArrayList<>();
        pieY2 = new ArrayList<>();
        pie1XData = new ArrayList<>();
        pie2XData = new ArrayList<>();
        //1.柱状图数据
        initBarData();
        //2.横向柱状图数据
        switch (fromWhere) {
            case Constant.SERVICE_DATA:
            case Constant.ENTITY_DATA:
                /*//收入，第一块饼状图
                List<ChartDataBean.MsgBean.SectorIncomeBean> sectorIncome = chartDataBean.getMsg().getSectorIncome();
                //渠道，第二块饼状图
                ChartDataBean.MsgBean.SectorChannelBean sectorChannel = chartDataBean.getMsg().getSectorChannel();
                for (int i = 0; i < sectorIncome.size(); i++) {
                    //4.饼状图的xvalue
                    ChartDataBean.MsgBean.SectorIncomeBean sectorIncomeBean = sectorIncome.get(i);
                    pie1XData.add(sectorIncomeBean.getName());
                    //5.饼状图的数据
                    Entry pieEntry = new Entry((float) sectorIncomeBean.getRate(), i);
                    pieY1.add(pieEntry);
                }
                pie2XData.add("自销");
                pie2XData.add("分销");
                Entry pieEntry1 = new Entry((float) sectorChannel.getSelfIncomeRate(), 0);
                pieY2.add(pieEntry1);
                Entry pieEntry2 = new Entry((float) sectorChannel.getOthersIncomeRate(), 1);
                pieY2.add(pieEntry2);
                fAccount = df.format(totalAccount);
                PieDataSet pieDataSet1 = generatePieDataSet(pieY1);
                PieDataSet pieDataSet2 = generatePieDataSet(pieY2);
                String centerText = "总收入" + '\n' + chartDataBean.getMsg().getIncome() + "元";*/
                //图表设置数据
                /*MChatUtils.setPieData(first_pie, pie1XData, pieDataSet1);
                MChatUtils.setPieData(second_pie, pie2XData, pieDataSet2);
                MChatUtils.setCenterText(first_pie, centerText);
                MChatUtils.setCenterText(second_pie, centerText);*/
                MChatUtils.setData(chart1, barXData, barY1, barY2);
                List<ChartDataBean.MsgBean.IncomeTop5Bean> esIncomeTop5 = chartDataBean.getMsg().getIncomeTop5();
                incomeTop5.clear();
                for (int i = 0; i < esIncomeTop5.size(); i++) {
                    PosChartBean.MsgBean.IncomeTop5Bean incomeTop5Bean=new PosChartBean.MsgBean.IncomeTop5Bean();
                    incomeTop5Bean.setGoodsName(esIncomeTop5.get(i).getGoodsName());
                    incomeTop5Bean.setGoodsId(esIncomeTop5.get(i).getGoodsId());
                    incomeTop5Bean.setIncome(esIncomeTop5.get(i).getIncome());
                    incomeTop5.add(incomeTop5Bean);
                }
                List<ChartDataBean.MsgBean.SaledTop5Bean> esSaledTop5 = chartDataBean.getMsg().getSaledTop5();
                saledTop5.clear();
                for (int i = 0; i < esSaledTop5.size(); i++) {
                    PosChartBean.MsgBean.SaledTop5Bean saledTop5Bean=new PosChartBean.MsgBean.SaledTop5Bean();
                    saledTop5Bean.setGoodsName(esSaledTop5.get(i).getGoodsName());
                    saledTop5Bean.setGoodsId(esSaledTop5.get(i).getGoodsId());
                    saledTop5Bean.setSaled(esSaledTop5.get(i).getSaled());
                    saledTop5.add(saledTop5Bean);
                }
                break;
            case Constant.POS_DATA:
                //判断显示筛选的选项
                List<PosChartBean.MsgBean.GoodsNameListBean> goodsNameList = posChartBean.getMsg().getGoodsNameList();
                if (goodsNameList != null) {
                    if (goodsNameList.size() <= 0) {
                        left_icon.setVisibility(View.GONE);
                    }
                } else {
                    left_icon.setVisibility(View.GONE);
                }
                MChatUtils.setData(chart1, barXData, barY1, barY2);
                this.saledTop5 = posChartBean.getMsg().getSaledTop5();
                this.incomeTop5 = posChartBean.getMsg().getIncomeTop5();
                 /* //收入，第一块饼状图
                List<PosChartBean.MsgBean.SectorBean> sectorBeans = posChartBean.getMsg().getSector();
                for (int i = 0; i < 5; i++) {
                    //4.饼状图的xvalue
                    PosChartBean.MsgBean.SectorBean sectorBean = sectorBeans.get(i);
                    pie1XData.add(sectorBean.getName());
                    //5.饼状图的数据
                    Entry pieEntry = new Entry((float) sectorBean.getRate(), i);
                    pieY1.add(pieEntry);
                }
                //图表设置数据
                fAccount = df.format(totalAccount);
                PieDataSet pieDataSet11 = generatePieDataSet(pieY1);
                PieDataSet pieDataSet22 = generatePieDataSet(pieY2);
                String centerText2 = "总收入" + '\n' + posChartBean.getMsg().getIncome() + "元";*/
                /*MChatUtils.setPieData(first_pie, pie1XData, pieDataSet11);
                MChatUtils.setPieData(second_pie, pie2XData, pieDataSet22);
                MChatUtils.setCenterText(first_pie, centerText2);
                MChatUtils.setCenterText(second_pie, centerText2);*/
                break;
        }
        if(rb_by_num.isChecked()) {
            showTop5(SALE_NUM,true);
        }else if(rb_by_income.isChecked()) {
            showTop5(SALE_INCOME,true);
        }
        textData();

        //是否重新设置左右品类显示
        if(resetLeftRight){
            resetLeftRight=false;
            //设置左右两个品类选择初始化显示：没有根据返回来动态设置
            tv_select_left.setText("全部");
            tv_select_right.setText("全部渠道");
            tv_all_tools.setText("全部工具");
        }
    }

    private Top5Adapter top5Adapter=new Top5Adapter(this);
    private void showTop5(int which,boolean needScroll) {
        if(!"0".equals(goodId)) {
            rl_top5.setVisibility(View.GONE);
            gv_top5.setVisibility(View.GONE);
            hbar.setVisibility(View.GONE);
            tv_top5_nodata.setVisibility(View.GONE);
        }else {
            rl_top5.setVisibility(View.VISIBLE);
            gv_top5.setVisibility(View.VISIBLE);
            hbar.setVisibility(View.VISIBLE);
            tv_top5_nodata.setVisibility(View.VISIBLE);

            if(saledTop5 !=null&& saledTop5.size()>0) {
                hbar.setVisibility(View.VISIBLE);
                gv_top5.setVisibility(View.VISIBLE);
                tv_top5_nodata.setVisibility(View.GONE);
                if(which==SALE_NUM) {
                    top5Adapter.setData(saledTop5);
                    top5Adapter.setDataType(SALE_NUM);
                }else {
                    top5Adapter.setData(incomeTop5);
                    top5Adapter.setDataType(SALE_INCOME);
                }
                gv_top5.setAdapter(top5Adapter);
                hXData = getHBXData();
                List<BarEntry> yValues= getHBYData(which);
                MChatUtils.setHBarData(hbar, hXData, yValues, which);
                hbar.animateY(1000);
            }else {
                hbar.setVisibility(View.GONE);
                gv_top5.setVisibility(View.GONE);
                if("4".equals(goodstype)||ordertype==1||"2".equals(payTool)){
                    rl_top5.setVisibility(View.GONE);//top5头布局不显示
                    tv_top5_nodata.setVisibility(View.GONE);
                }else{
                    tv_top5_nodata.setVisibility(View.VISIBLE);
                }
                noDataOrOpenMember();
            }
        }
        if(needScroll) {
            gv_top5.setFocusable(false);
            chart_scroll_view.smoothScrollTo(0, 0);
        }
    }

    /**
     * 引导开通会员营销或者显示没有数据
     */
    private void noDataOrOpenMember() {
/*        if (!Constant.OPENED_PERMISSIONS.contains("2")) {
            //图文混排，spanable
            String notice = getString(R.string.top5_guide_memberpay);
            SpannableString ssp = new SpannableString(notice);
            ssp.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(ShowChartActivity.this, UpgradeAccountActivity.class);
                    ChargeItem payItem = new ChargeItem("60", "160", "280", "480", "mdp");
                    intent.putExtra("pay_item", payItem);
                    ShowChartActivity.this.startActivity(intent);
                }
            }, notice.length()-8, notice.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            ssp.setSpan(new ForegroundColorSpan(Color.rgb(51, 102, 255)), notice.length()-8, notice.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_top5_nodata.setHighlightColor(Color.TRANSPARENT);
            tv_top5_nodata.setText(ssp);
            tv_top5_nodata.setMovementMethod(LinkMovementMethod.getInstance());
        }else {
            tv_top5_nodata.setText(R.string.top5_nodata);
        }*/
            tv_top5_nodata.setText(R.string.top5_nodata);
    }

    /**
     * top5y轴数据
     * @return
     */
    private List<BarEntry> getHBYData(int which) {

        List<BarEntry> yValues=new ArrayList<>();
        for (int i = 0; i < saledTop5.size(); i++) {
            double yVal;
            if(which==SALE_NUM) {
                PosChartBean.MsgBean.SaledTop5Bean t =  saledTop5.get(i);
                 yVal = Double.parseDouble(t.getSaled());
            }else {
                PosChartBean.MsgBean.IncomeTop5Bean t = incomeTop5.get(i);
                yVal=Double.parseDouble(t.getIncome());
            }
            BarEntry barEntry=new BarEntry((float)yVal,i);
            yValues.add(barEntry);
        }
        return yValues;
    }

    /**
     * top5x轴数据
     * @return
     */
    private List<String> getHBXData() {
        List<String> hXData=new ArrayList<>();
        for (int i = 0; i < saledTop5.size(); i++) {
            hXData.add(i, "TOP" + (saledTop5.size()-i));
        }
        return hXData;
    }

    /**
     * 柱状图的数据
     */
    private void initBarData() {
        switch (fromWhere) {
            case Constant.ENTITY_DATA://线上实物
            case Constant.SERVICE_DATA://服务
                List barBeans = chartDataBean.getMsg().getBar();
                for (int i = 0; i < barBeans.size(); i++) {
                    ChartDataBean.MsgBean.BarBean barBean = (ChartDataBean.MsgBean.BarBean) barBeans.get(i);
                    barXData.add(barBean.getX());
                    //2.柱状图数据
                    BarEntry barEntry = new BarEntry((float) barBean.getIncome(), i);
                    barY1.add(barEntry);
                    //3.折线图数据
                    Entry entry = new Entry((float) barBean.getSaled(), i);
                    barY2.add(entry);
                    //统计总数，totalaccount，totalnum
                    totalAccount += barBean.getIncome();
                    totalNum += barBean.getSaled();
                }
                break;
            case Constant.POS_DATA://POS机
                List<PosChartBean.MsgBean.BarBean> barBeanList = posChartBean.getMsg().getBar();
                for (int i = 0; i < barBeanList.size(); i++) {
                    PosChartBean.MsgBean.BarBean barBean = barBeanList.get(i);
                    barXData.add(barBean.getX());
                    //2.柱状图数据
                    BarEntry barEntry = new BarEntry((float) barBean.getIncome(), i);
                    barY1.add(barEntry);
                    //3.折线图数据
                    Entry entry = new Entry((int) barBean.getSaled(), i);
                    barY2.add(entry);
                    //统计总数，totalaccount，totalnum
                    totalAccount += barBean.getIncome();
                    totalNum += barBean.getSaled();
                }
                break;
        }
    }

    /**
     * 图形列表界面的文本信息
     */
    private void textData() {
        switch (fromWhere) {
            case Constant.POS_DATA:
                tv_account_num.setText("收款总额:" + posChartBean.getMsg().getIncome() + "元" + "    订单数:" + posChartBean.getMsg().getSaled() + "个");
                break;
            //btn_sell.setEnabled(false);
            // btn_used.setEnabled(true);
            case Constant.SERVICE_DATA:
                if ("1".equals(right_selected)) {//核销
                    tv_account_desc.setText("实际收入及核销订单数");
                    tv_account_num.setText("实际收入:" + chartDataBean.getMsg().getIncome() + "元" + "    核销订单数:" + chartDataBean.getMsg().getSaled() + "个");
                } else {//预期
                    tv_account_desc.setText("预期收入及售出订单数");
                    tv_account_num.setText("预期收入:" + chartDataBean.getMsg().getIncome() + "元" + "    售出订单数:" + chartDataBean.getMsg().getSaled() + "个");
                }
                break;
            case Constant.ENTITY_DATA:
                if ("1".equals(right_selected)) {//核销
                    tv_account_desc.setText("实际收入及完成订单数");
                    tv_account_num.setText("实际收入:" + chartDataBean.getMsg().getIncome() + "元" + "    完成订单数:" + chartDataBean.getMsg().getSaled() + "个");
                } else {//预期
                    tv_account_desc.setText("预期收入及售出订单数");
                    tv_account_num.setText("预期收入:" + chartDataBean.getMsg().getIncome() + "元" + "    售出订单数:" + chartDataBean.getMsg().getSaled() + "个");
                }
                break;
        }
    }

    private PieDataSet generatePieDataSet(ArrayList<Entry> yValues) {
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(1f); //设置个饼状图之间的距离

        generatePieColor();
        pieDataSet.setColors(colors);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float px = 3 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        pieDataSet.setValueTextColor(Color.WHITE);//设置value的字体颜色
        return pieDataSet;
    }

    private ArrayList<Integer> generatePieColor() {
        colors = new ArrayList<>();
        // 饼图颜色
        for (int i = 0; i < pieY1.size(); i++) {
            colors.add(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
//            colors.add(Color.rgb(0,0,0));
        }
        return colors;
    }

    /**
     * 各个组件的点解监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title://back键
                finish();
                break;
            case R.id.rl_date_choose://选择日期,显示日期选择dialog，得到日期，发送请求道服务器，请求新的数据
                showDia();
                break;
            case R.id.rb_by_num://按销量排
                showTop5(SALE_NUM, false);
                break;
            case R.id.rb_by_income://按销售额排
                showTop5(SALE_INCOME,false);
                break;
            case R.id.rl_select_left://左侧的筛选
                isClickLeft = 0;
                if (showHor != null && showHor.size() > 0) {
                    if (close1) {
                        inflaterData(1);
                    }
                    changeCurrentState(1);
                } else {
                    Toast.makeText(ShowChartActivity.this, "当前条件下没有筛选数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_select_right://右侧的筛选
                isClickLeft = 1;
                if (rightSelectData != null && rightSelectData.size() > 0) {
                    if (close2) {//如果右侧打开状态
                        inflaterData(2);
                    }
                    changeCurrentState(2);
                } else {
                    Toast.makeText(ShowChartActivity.this, "当前条件下没有筛选数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_select_all_tools://右侧的筛选
                isClickLeft = 2;
                if (payToolsSelectData != null && payToolsSelectData.size() > 0) {
                    if (close3) {//如果最右侧打开状态
                        inflaterData(3);
                    }
                    changeCurrentState(3);
                } else {
                    Toast.makeText(ShowChartActivity.this, "当前条件下没有筛选数据", Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * 3.请求新数据之-----售出代金券和核销代金券的切换
             */
            case R.id.btn_sell://售出代金券
                //tv_account_num.setText("预期收入:" + fAccount + "    售出订单数:" + totalNum + "个");
                btn_sell.setEnabled(false);
                btn_used.setEnabled(true);
                //btn_sell.setBackgroundColor(Color.rgb(255, 151, 0));
                //btn_used.setBackgroundColor(Color.WHITE);
                btn_sell.setBackgroundResource(R.drawable.ll_shape_orange);
                btn_sell.setTextColor(Color.parseColor("#FF9700"));
                btn_used.setBackgroundResource(R.drawable.ll_shape_black);
                btn_used.setTextColor(Color.parseColor("#5E5E5E"));
                getServiceDataFromIntent();
                break;
            case R.id.btn_used://核销代金券
                //tv_account_num.setText("实际收入:" + fAccount + "    核销订单数:" + totalNum + "个");
                btn_used.setEnabled(false);
                btn_sell.setEnabled(true);
                //btn_used.setBackgroundColor(Color.rgb(255, 151, 0));
                //btn_sell.setBackgroundColor(Color.WHITE);
                btn_used.setBackgroundResource(R.drawable.ll_shape_orange);
                btn_used.setTextColor(Color.parseColor("#FF9700"));
                btn_sell.setBackgroundResource(R.drawable.ll_shape_black);
                btn_sell.setTextColor(Color.parseColor("#5E5E5E"));
                getServiceDataFromIntent();
                break;
            case R.id.rb_statistics_by_income:
                rl_chart_select_left.setVisibility(View.VISIBLE);
                rl_select_all_tools.setVisibility(View.VISIBLE);
                line_which_display_income.setVisibility(View.VISIBLE);
                line_which_display_order.setVisibility(View.GONE);
                hideShowDropBox();
                chooseStatistics(0);
                break;
            case R.id.rb_statistics_by_order:
                rl_select_all_tools.setVisibility(View.GONE);
                rl_chart_select_left.setVisibility(View.GONE);
                line_which_display_income.setVisibility(View.GONE);
                line_which_display_order.setVisibility(View.VISIBLE);
                hideShowDropBox();
                chooseStatistics(1);
                break;
        }
    }

    //切换时隐藏显示的下拉框
    private void hideShowDropBox() {
        if(isShow){
            if(isClickLeft==0){
                changeCurrentState(1);
            }else if(isClickLeft==1){
                changeCurrentState(2);
            }else if(isClickLeft==2){
                changeCurrentState(3);
            }
        }
    }

    private void chooseStatistics(int which) {
        switch (which){
            case 0:
                ordertype=0;
                tv_explain_statistics.setText(getString(R.string.income_statistics));
                if(rightSelectData!=null){
                    int size = rightSelectData.size();
                    if(!"会员卡余额收款".equals(rightSelectData.get(size-1).getName())){
                        rightSelectData.add(new PosOrderKinds("会员卡余额收款","4"));
                    }
                }
                break;
            case 1:
                ordertype=1;
                tv_explain_statistics.setText(getString(R.string.order_statistics));
                if(rightSelectData!=null){
                    int size = rightSelectData.size();
                    if("会员卡余额收款".equals(rightSelectData.get(size-1).getName())){
                        rightSelectData.remove(size-1);
                    }
                }
                break;
        }
        three_layer_pos = new int[]{-1,-1,-1};
        resetLeftRight = true;
        resetParams();
        initParams();
    }

    /**
     * 切换的时候清除所有记录
     */
    private void resetParams() {
        goodId = "0";
        sizeId = "0";
        right_selected = "-1";
        goodstype = "0";
    }

    /**
     * 筛选的数据的填充
     *
     * @param leftOrRight 1是左侧，2是右侧
     */
    private void inflaterData(int leftOrRight) {
        switch (leftOrRight) {
            case 1://左侧
                //初始化水平选择项
                boolean b = showHorChooseItem();
                if (b) {
                    LogUtils.e(TAG, "inflaterData() filterAdapter.setData(leftData)=" + showHor.toString());
                    if (three_layer_pos[0] == -1) {
                        LogUtils.e(TAG, "three_layer_pos[0] == -1");
                        chooseHorKinds(0, true);
                    } else {
                        if (three_layer_pos[1] == -1) {
                            LogUtils.e(TAG, "three_layer_pos[1] == -1");
                            //2.设置水平选中，其他都不默认第一项选中
                            chooseHorKinds(three_layer_pos[0], true);
                            filterAdapter.setData(showHor.get(three_layer_pos[0]).getCategory());
                            filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_CHART_KINDS);//左侧的筛选来根据不同界面展示不同
                            lv_for_filter.setAdapter(filterAdapter);
                            if (showHor.get(three_layer_pos[0]).getCategory().size() > 0) {
                                goods_filterAdapter.setData(showHor.get(three_layer_pos[0]).getCategory().get(0).getGoodsList());
                                goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_CHART_GOODS);
                                lv_all_store.setAdapter(goods_filterAdapter);
                            }
                        } else {
                            LogUtils.e(TAG, "three_layer_pos[1] != -1");
                            if (three_layer_pos[2] == -1) {
                                LogUtils.e(TAG, "three_layer_pos[2] == -1");
                                //3.设置水平选中，种类选中，商品不选中
                                chooseHorKinds(three_layer_pos[0], true);
                                List<PosChartBean.MsgBean.GoodsNameListBean.CategoryBean> category = showHor.get(three_layer_pos[0]).getCategory();
                                category.get(three_layer_pos[1]).setIsChecked(true);
                                filterAdapter.setData(category);
                                filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_CHART_KINDS);//左侧的筛选来根据不同界面展示不同
                                lv_for_filter.setAdapter(filterAdapter);

                                goods_filterAdapter.setData(showHor.get(three_layer_pos[0]).getCategory().get(three_layer_pos[1]).getGoodsList());
                                goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_CHART_GOODS);
                                lv_all_store.setAdapter(goods_filterAdapter);
                            } else {
                                LogUtils.e(TAG, "three_layer_pos[2] != -1");
                                //4.设置水平选中，种类选中，商品选中
                                chooseHorKinds(three_layer_pos[0], true);
                                List<PosChartBean.MsgBean.GoodsNameListBean.CategoryBean> category = showHor.get(three_layer_pos[0]).getCategory();
                                category.get(three_layer_pos[1]).setIsChecked(true);
                                filterAdapter.setData(category);
                                filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_CHART_KINDS);//左侧的筛选来根据不同界面展示不同
                                lv_for_filter.setAdapter(filterAdapter);

                                List<PosChartBean.MsgBean.GoodsNameListBean.CategoryBean.GoodsListBean> goodsList = showHor.get(three_layer_pos[0]).getCategory().get(three_layer_pos[1]).getGoodsList();
                                goodsList.get(three_layer_pos[2]).setIsChecked(true);
                                goods_filterAdapter.setData(goodsList);
                                goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_CHART_GOODS);
                                lv_all_store.setAdapter(goods_filterAdapter);
                            }
                        }
                    }

                    if (showHor.size() > 7) {
                        ViewGroup.LayoutParams para = lv_for_filter.getLayoutParams();
                        para.height = Utils.dip2px(this, 300);
                        lv_for_filter.setLayoutParams(para);
                    }
                }
                break;
            case 2://右侧
                filterAdapter.setData(rightSelectData);
                filterAdapter.setDataFrom(Constant.KINDS_CHOOSE_RIGHT);
                lv_for_filter.setAdapter(filterAdapter);
                break;
            case 3://最右侧
                filterAdapter.setData(payToolsSelectData);
                filterAdapter.setDataFrom(Constant.KINDS_CHOOSE_RIGHT);
                lv_for_filter.setAdapter(filterAdapter);
                break;
        }
    }

    /**
     * ll_selector_add
     * 改变当前的店铺信息展示状态
     */
    public void changeCurrentState(int clickPosition) {
        if(LAST_CLICK==clickPosition) {//点击的同是左侧或者右侧，改变状态
            if(ll_selector_add.getVisibility()==View.VISIBLE) {
                hide(clickPosition);
                setShowdataBox(false);
            }else {
                show(clickPosition);
                setShowdataBox(true);
            }
        }else {
            inflaterData(clickPosition);
            show(clickPosition);
            setShowdataBox(true);
            LAST_CLICK=clickPosition;
        }
    }

    /**
     * 设置当前有没有下拉框的显示
     * @param b
     */
    private void setShowdataBox(boolean b) {
        showOrCloseIcon(LAST_CLICK,isClickLeft,b);
        isShow = b;
    }

    public void showOrCloseIcon(int last_pos,int now_pos,boolean b){
        if(isShow){//有正在显示的框
            ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(200);
            ra.setFillAfter(true);
            switch (last_pos){
                case 1:
                    left_icon.startAnimation(ra);
                    break;
                case 2:
                    right_icon.startAnimation(ra);
                    break;
                case 3:
                    iv_tools_icon.startAnimation(ra);
                    break;
            }
            if(b){//就把当前点击位置打开
                ra = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(200);
                ra.setFillAfter(true);
                switch (now_pos){
                    case 0:
                        left_icon.startAnimation(ra);
                        break;
                    case 1:
                        right_icon.startAnimation(ra);
                        break;
                    case 2:
                        iv_tools_icon.startAnimation(ra);
                        break;
                }
            }else{//就把当前点击位置关闭
                switch (now_pos){
                    case 0:
                        left_icon.startAnimation(ra);
                        break;
                    case 1:
                        right_icon.startAnimation(ra);
                        break;
                    case 2:
                        iv_tools_icon.startAnimation(ra);
                        break;
                }
            }
        }else{
            if(b){
                ra = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(200);
                ra.setFillAfter(true);
                switch (now_pos){
                    case 0:
                        left_icon.startAnimation(ra);
                        break;
                    case 1:
                        right_icon.startAnimation(ra);
                        break;
                    case 2:
                        iv_tools_icon.startAnimation(ra);
                        break;
                }
            }
        }
    }

    /**
     * 打开下拉
     */
    private void show(int pos) {
        switch (pos) {
            case 1://左侧
                ll_service_kinds.setVisibility(View.VISIBLE);
                lv_all_store.setVisibility(View.VISIBLE);
                view_line.setVisibility(View.VISIBLE);
                ll_selector_add.setVisibility(View.GONE);
                ll_listviews_continer.setVisibility(View.GONE);
                lv_for_filter.setVisibility(View.GONE);
                break;
            case 2://右侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_all_store.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
                ll_selector_add.setVisibility(View.VISIBLE);
                ll_listviews_continer.setVisibility(View.VISIBLE);
                lv_for_filter.setVisibility(View.VISIBLE);
                break;
            case 3://最右侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_all_store.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
                ll_selector_add.setVisibility(View.VISIBLE);
                ll_listviews_continer.setVisibility(View.VISIBLE);
                lv_for_filter.setVisibility(View.VISIBLE);
                break;
        }
        tv_transfer.setVisibility(View.VISIBLE);
        openAnimation(ll_selector_add);
    }

    private void hide(int pos) {
        switch (pos) {
            case 1://左侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_all_store.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
                break;
            case 2://左侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_for_filter.setVisibility(View.GONE);
                break;
            case 3://最右侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_for_filter.setVisibility(View.GONE);
                break;
        }
        tv_transfer.setVisibility(View.GONE);
        closeAnimation(ll_selector_add);
    }

    public void closeAnimation(final View view) {
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        ta.setDuration(200);
        view.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void openAnimation(final View view) {
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(200);
        view.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ll_selector_add.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 显示日期选择的dialog
     */
    private void showDia() {
        Calendar c = Calendar.getInstance();
        doubleDatePickerDialog = new DoubleDatePickerDialog(ShowChartActivity.this, 0, callBack,
                 sdate,edate,
                startHour,startMinute,
                endHour,endMinute,
                true,false);

        DatePicker datePickerStart = doubleDatePickerDialog.getDatePickerStart();
        DatePicker datePickerEnd = doubleDatePickerDialog.getDatePickerEnd();
        //设置开始时间最大为今日,结束日期最大为今日
        datePickerStart.setMaxDate(c.getTimeInMillis());
        datePickerEnd.setMaxDate(c.getTimeInMillis());
        //保存当前显示日期，取消或back后仍然显示之前的日期
        beforeDate = tv_title_all.getText().toString();
        doubleDatePickerDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (doubleDatePickerDialog != null && doubleDatePickerDialog.isShowing()) {
                    CanDismiss(doubleDatePickerDialog);
                    tv_title_all.setText(beforeDate);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 2.联网请求新数据之-----更换日期的筛选
     * 日期选择的回调
     */
    public static DecimalFormat tf=new DecimalFormat("00");

    private DoubleDatePickerDialog.OnDateSetListener callBack = new DoubleDatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth,
                              int sh,int sm,int eh,int em) {
            Date startDate = new Date(startYear - 1900, startMonthOfYear, startDayOfMonth);
            Date endDate = new Date(endYear - 1900, endMonthOfYear, endDayOfMonth);

            startHour=sh;
            startMinute=sm;
            endHour=eh;
            endMinute=em;
            startFormat = Utils.format(startDate)+" "+tf.format(startHour)+":"+tf.format(startMinute);
            endFormat = Utils.format(endDate)+" "+tf.format(endHour)+":"+tf.format(endMinute);

            sdate=Utils.format(startDate);
            edate=Utils.format(endDate);

            long empt = endDate.getTime() - startDate.getTime();
            long yearMill = 365 * 24 * 60 * 60 * 1000L;
            if (endDate.getTime() < startDate.getTime()) {//如果结束日期小于开始日期
                Toast.makeText(ShowChartActivity.this, "结束日期不得小于开始日期", Toast.LENGTH_SHORT).show();
                CannotDisMiss();
            } else if (empt > yearMill) {
                Utils.showToast(ShowChartActivity.this, "最大时间跨度为1年");
                CannotDisMiss();
            } else {//反射的方式调用，屏蔽alertdialog的点击消失事件
                everGetdata = false;
                everGetCheckedData = false;
                CanDismiss(doubleDatePickerDialog);
                tv_title_all.setText(startFormat + "至" + endFormat);
                //联网请求，获取新数据，更新视图
                requstNewData();
            }
        }

        @Override
        public void startPrint(DatePicker mDatePicker_start, int year, int month, int dayOfMonth, DatePicker mDatePicker_end, int year1, int month1, int dayOfMonth1,
                               int startHour,int startMinte,int endHour,int minute) {

        }

        @Override
        public void Cancel(DoubleDatePickerDialog doubleDatePickerDialog) {
            CanDismiss(doubleDatePickerDialog);
            tv_title_all.setText(beforeDate);
        }
    };

    /**
     * 日期区间变更后请求新的数据
     */
    private void requstNewData() {
        switch (fromWhere) {
            //线上服务的数据
            case Constant.SERVICE_DATA:
                if (btn_sell.isEnabled()) {//核销
                } else {//售出
                }
                getServiceDataFromIntent();
                break;
            //线上实物的数据, 线下pos数据
            case Constant.ENTITY_DATA:
                getEntityDataFromIntent();
                break;
            case Constant.POS_DATA:
                getPosDataFromIntent();
                break;
        }
    }

    private void CannotDisMiss() {
        try {
            Field field = doubleDatePickerDialog.getClass()
                    .getSuperclass().getSuperclass().getDeclaredField(
                            "mShowing");
            field.setAccessible(true);
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(doubleDatePickerDialog, false);
            doubleDatePickerDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CanDismiss(DoubleDatePickerDialog doubleDatePickerDialog) {
        try {
            Field field = doubleDatePickerDialog.getClass()
                    .getSuperclass().getSuperclass().getDeclaredField(
                            "mShowing");
            field.setAccessible(true);
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(doubleDatePickerDialog, true);
            doubleDatePickerDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置水平选项点击切换
     *
     * @param i
     */
    private void chooseHorKinds(int i, boolean isInit) {

        //设置水平选择项第一个选中
        switch (i) {
            case 0:
                setSelectedColor("#FF9700", "#666666", "#666666", "#666666", "#666666");
                break;
            case 1:
                setSelectedColor("#666666", "#FF9700", "#666666", "#666666", "#666666");
                break;
            case 2:
                setSelectedColor("#666666", "#666666", "#FF9700", "#666666", "#666666");
                break;
            case 3:
                setSelectedColor("#666666", "#666666", "#666666", "#FF9700", "#666666");
                break;
            case 4:
                setSelectedColor("#666666", "#666666", "#666666", "#666666", "#FF9700");
                break;
        }

        if (!isInit) {
            for (int j = 0; j < showHor.get(i).getCategory().size(); j++) {
                if (i == 0) {
                    showHor.get(i).getCategory().get(j).setIsChecked(true);
                } else {
                    showHor.get(i).getCategory().get(j).setIsChecked(false);
                }
            }
        }

        filterAdapter.setData(showHor.get(i).getCategory());
        filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_CHART_KINDS);
        lv_for_filter.setAdapter(filterAdapter);

        if (showHor.get(i).getCategory().size() > 0) {
            lv_for_filter.setVisibility(View.VISIBLE);
            lv_all_store.setVisibility(View.VISIBLE);
            ll_listviews_continer.setVisibility(View.VISIBLE);
            if (!isInit) {
                for (int k = 0; k < showHor.get(i).getCategory().size(); k++) {
                    if (k == 0) {
                        showHor.get(i).getCategory().get(k).setIsChecked(true);
                    } else {
                        showHor.get(i).getCategory().get(k).setIsChecked(false);
                    }
                }
            }
            goods_filterAdapter.setData(showHor.get(i).getCategory().get(0).getGoodsList());
            goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_CHART_GOODS);
            lv_all_store.setAdapter(goods_filterAdapter);
        } else {
            LogUtils.e(TAG, "横行类目下面没有下一层了，直接请求数据 isInit==" + isInit);
            lv_for_filter.setVisibility(View.GONE);
            lv_all_store.setVisibility(View.GONE);
            ll_listviews_continer.setVisibility(View.GONE);
            if (isInit) {
                LogUtils.e(TAG, "初始化显示...");
            } else {
                LogUtils.e(TAG, "点击选择 进行数据请求...");
                hide(1);
                tv_select_left.setText(showHor.get(i).getTypeName());
                if (three_layer_pos[0] == -1) {
                    three_layer_pos[0] = 0;
                }
                goodstype = String.valueOf(showHor.get(three_layer_pos[0]).getGoodsType());
                getChooseDataFromNet(String.valueOf(showHor.get(i).getGoodsType()), isClickLeft);
                setShowdataBox(false);
            }
        }
    }

    /**
     * 所有情况的数据选择的请求
     *
     * @param id
     * @param isLeft
     */
    public void getChooseDataFromNet(String id, int isLeft) {

        if (isLeft==0) {//左侧点击：id即为商品id
            LogUtils.e(TAG, "getChooseDataFromNet() 请求左侧筛选出的数据");
            goodId = id;
            if (fromWhere == Constant.POS_DATA) {
                getPosDataFromIntent();
            } else if (fromWhere == Constant.SERVICE_DATA) {
                getServiceDataFromIntent();
            } else {
                getEntityDataFromIntent();
            }
        } else if(isLeft==1){//右侧点击:筛选支付方式
            LogUtils.e(TAG, "getChooseDataFromNet() 请求右侧筛选出的数据");
            right_selected = rightSelectData.get(Integer.valueOf(id)).getId();
            tv_select_right.setText(rightSelectData.get(Integer.valueOf(id)).getName());
            Toast.makeText(ShowChartActivity.this, rightSelectData.get(Integer.valueOf(id)).getName(), Toast.LENGTH_SHORT).show();
            changeCurrentState(2);
            if (fromWhere == Constant.POS_DATA) {
                getPosDataFromIntent();
            } else if (fromWhere == Constant.SERVICE_DATA) {
                getServiceDataFromIntent();
            } else {
                getEntityDataFromIntent();
            }
        }else{
            tv_all_tools.setText(payToolsSelectData.get(Integer.valueOf(id)).getName());
            LogUtils.e(TAG, "getChooseDataFromNet 点击最最最右侧 payTool=" + payTool);
            changeCurrentState(3);
            getPosDataFromIntent();
        }
    }

    private void setSelectedColor(String color_one, String color_two, String color_three, String color_four, String color_five) {
        tv_choose_kinds_one.setTextColor(Color.parseColor(color_one));
        tv_choose_kinds_two.setTextColor(Color.parseColor(color_two));
        tv_choose_kinds_three.setTextColor(Color.parseColor(color_three));
        tv_choose_kinds_four.setTextColor(Color.parseColor(color_four));
        tv_choose_kinds_five.setTextColor(Color.parseColor(color_five));
    }
}

