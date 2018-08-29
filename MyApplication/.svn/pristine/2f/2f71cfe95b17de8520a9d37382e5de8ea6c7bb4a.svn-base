package com.younle.younle624.myapplication.activity.manager.orderpager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.adapter.ordermanager.FilterAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.EntityOrderBean;
import com.younle.younle624.myapplication.domain.PosOrderBean;
import com.younle.younle624.myapplication.domain.PosOrderKinds;
import com.younle.younle624.myapplication.domain.ServerOrderBean;
import com.younle.younle624.myapplication.utils.AlertUtils;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.DoubleDatePickerDialog;
import com.younle.younle624.myapplication.view.SelfImageView;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.younle.younle624.myapplication.view.XListView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.younle.younle624.myapplication.R.id.tv_create_new_order;

public abstract class BaseActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "BaseActivity";
    public ProgressBar pb_loading;
    public TextView tv_loading;
    public SelfLinearLayout ll_loading;
    public XListView xl_pos_order;
    public ImageView iv_title;
    public TextView tv_title;
    public RelativeLayout rl_date_choose;
    public LinearLayout all_order_two_header;
    public RadioButton rb_statistics_by_order;
    public RadioButton rb_statistics_by_income;
    public View line_which_display_order;
    public View line_which_display_income;
    public TextView tv_explain_statistics;
    public ImageView tv_filter;
    public TextView tv_title_date;
    public TextView tv_date_exception;
    public ListView lv_all_store;
    public LinearLayout ll_service_kinds;
    public LinearLayout ll_choose_store;
    /**
     * 下拉筛选的整体视图组件
     */
    public LinearLayout ll_selector_add;
    public TextView tv_choose_kinds_one;
    public TextView tv_choose_kinds_three;
    public TextView tv_choose_kinds_two;
    public TextView tv_choose_kinds_four;
    public TextView tv_choose_kinds_five;
    /**
     *  pos账单专属的listview
     */
    public ImageView iv_jiazai_filure;
    public ListView lv_pos_kinds;
    public SelfImageView tv_transfer;
    public View headView;
    public LinearLayout filter_layout;
    public DoubleDatePickerDialog doubleDatePickerDialog;
    public String beforeDate;
    public FilterAdapter filterAdapter=new FilterAdapter(this);
    public FilterAdapter goods_filterAdapter=new FilterAdapter(this);
    public List<String> filters = new ArrayList<>();
    /**
     *付款方式的筛选,右侧的筛选
     */
    public AutoRelativeLayout rl_select_right;//全部渠道 中间
    public AutoRelativeLayout rl_select_all_tools;//全部工具 右侧
    public TextView tv_all_tools;
    public ImageView iv_tools_icon;
    /**
     * 左侧的筛选
     */
    public AutoRelativeLayout rl_select_left;//全部订单 左侧
    public View payway_center_line;//全部订单 左侧
    /**
     * 筛选数据的adapter
     */
    public LinearLayout ll_nodata;
    public TextView tv_right_select;
    public TextView tv_select_left;
    public ImageView right_icon;
    public ImageView left_icon;
    /**
     * 顶部左侧筛选和右侧筛选的下拉listview
     */
    public ListView lv_for_filter;
    public LinearLayout ll_listviews_continer;
    public View view_line;
    public ListView lv_choosed_goods;
    public boolean close1 =true;
    public boolean close2 =true;
    public boolean close3 =true;
    public RotateAnimation ra;
    public List<PosOrderKinds> rightSelectData;
    public List<PosOrderKinds> payToolsSelectData;

    public static DecimalFormat tf=new DecimalFormat("00");

    /**
     * 日期选择的回调
     */
    private DoubleDatePickerDialog.OnDateSetListener callBack = new DoubleDatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                              DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth,
                              int sh,int sm,int eh,int em) {
            String textString = String.format("%d-%d-%d至%d-%d-%d", startYear,
                    startMonthOfYear , startDayOfMonth, endYear, endMonthOfYear , endDayOfMonth);
            Date startDate = new Date(startYear - 1900, startMonthOfYear, startDayOfMonth);
            Date endDate = new Date(endYear - 1900, endMonthOfYear, endDayOfMonth);

            /*startTime = Utils.format(startDate);
            endTime = Utils.format(endDate);*/
            startHour=sh;
            startMinute=sm;
            endHour=eh;
            endMinute=em;

            startTime =startYear+"-"+startMonthOfYear+"-"+startDayOfMonth;
            endTime =endYear+"-"+endMonthOfYear+"-"+endDayOfMonth;
            LogUtils.e(TAG,"start="+startTime);
            LogUtils.e(TAG,"end="+endTime);
            long empt = endDate.getTime() - startDate.getTime();

            if(judgeDatePicker(textString, startDate, endDate, empt)) {
                tv_title_date.setText(startTime+" "+ tf.format(startHour)+":"+ tf.format(startMinute)+"至"+endTime+" "+ tf.format(endHour)+":"+tf.format(endMinute));
                initData();
            }
        }

        @Override
        public void startPrint(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                               DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth,
                               int sh,int sm,int eh,int em) {
            String textString = String.format("%d-%d-%d至%d-%d-%d", startYear,
                    startMonthOfYear, startDayOfMonth, endYear, endMonthOfYear, endDayOfMonth);
            Date startDate = new Date(startYear - 1900, startMonthOfYear, startDayOfMonth);
            Date endDate = new Date(endYear - 1900, endMonthOfYear, endDayOfMonth);

            pstartHour=sh;
            pstartMinute=sm;
            pendHour=eh;
            pendMinute=em;

            String printStartTime = startYear+"-"+startMonthOfYear+"-"+startDayOfMonth+" "+ tf.format(pstartHour)+":"+tf.format(pstartMinute);
            String printEndTime = endYear+"-"+endMonthOfYear+"-"+endDayOfMonth+" "+tf.format(pendHour)+":"+tf.format(pendMinute);

            pstartTime=startYear+"-"+startMonthOfYear+"-"+startDayOfMonth;
            pendTime=endYear+"-"+endMonthOfYear+"-"+endDayOfMonth;

            LogUtils.e(TAG,"start="+pstartTime);
            LogUtils.e(TAG,"end="+pendTime);

            long empt = endDate.getTime() - startDate.getTime();
            if(judgeDatePicker(textString, startDate, endDate, empt)) {
                LogUtils.Log("开始打印 :"+printStartTime+"---"+printEndTime);
                getPrintData(printStartTime,printEndTime);
            }
        }

        @Override
        public void Cancel(DoubleDatePickerDialog doubleDatePickerDialog) {
            CanDismiss(doubleDatePickerDialog);
            tv_title_date.setText(beforeDate);
        }
    };

    public int startHour=0;
    public int startMinute=0;
    public int endHour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    public int endMinute=Calendar.getInstance().get(Calendar.MINUTE);
    private int pstartHour=0;
    private int pstartMinute=0;
    private int pendHour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private int pendMinute=Calendar.getInstance().get(Calendar.MINUTE);
    public String pstartTime=Utils.getToday();
    public String pendTime = Utils.getToday();



    public String startTime=Utils.getToday();
    public String endTime = Utils.getToday();

    public int LAST_CLICK=0;//上次点击的哪个，0是初始化，1是左侧，2是右侧
    public boolean isShow = false;//当前有没有下拉框显示
    private List leftData;
    private List rightData;
    private List payToolsData;
    private List<PosOrderBean.MsgBean.GoodsNameListBean> showHor;
    private int dataType;
    public int[] three_layer_pos = new int[]{-1,-1,-1};
    private int isClickLeft;
    private String payTool = "0";
    public String goodstype = "0";
    /**
     * 打印汇总
     */
    public TextView tv_mark_reback;

    /**
     * 订单搜索
     */
    public EditText et_order_search;
    public TextView tv_cancel;

    public abstract void getPrintData(String startTime,String endTime);
    private boolean judgeDatePicker(String textString, Date startDate, Date endDate, long empt) {
        long yearMill = 365 * 24 * 60 * 60 * 1000L;
        if (endDate.getTime() < startDate.getTime()) {//如果结束日期小于开始日期
            Toast.makeText(BaseActivity.this, "结束日期不得小于开始日期", Toast.LENGTH_SHORT).show();
            CannotDisMiss();
            return false;
        }else if(empt>yearMill) {
            Toast.makeText(BaseActivity.this, "时间跨度不得超过一年", Toast.LENGTH_SHORT).show();
            CannotDisMiss();
            return  false;
        }else {//反射的方式调用，屏蔽alertdialog的点击消失事件
            CanDismiss(doubleDatePickerDialog);
            return  true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        LogUtils.e(TAG,"重新初始化了");
        Utils.initToolBarState(this);
        initView();
        initSecondTitle();
        headView = initHeadView();
        initXlistView();
        initTitle();
        initData();
        setListener();
    }

    //初始化视图
    public void initView() {
        iv_jiazai_filure = (ImageView)findViewById(R.id.iv_jiazai_filure);
        ll_loading = (SelfLinearLayout)findViewById(R.id.ll_loading);
        pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        tv_loading = (TextView)findViewById(R.id.tv_loading);

        xl_pos_order = (XListView) findViewById(R.id.xl_pos_order);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title_date = (TextView) findViewById(R.id.tv_title_date);
        tv_filter = (ImageView) findViewById(R.id.tv_filter);
        ll_choose_store = (LinearLayout) findViewById(R.id.ll_choose_store);
        ll_service_kinds = (LinearLayout) findViewById(R.id.ll_service_kinds);
        ll_selector_add = (LinearLayout) findViewById(R.id.ll_selector_add);
        rl_date_choose = (RelativeLayout)findViewById(R.id.rl_date_choose);

        //订单统计页新加
        all_order_two_header = (LinearLayout)findViewById(R.id.all_order_two_header);
        rb_statistics_by_order = (RadioButton)findViewById(R.id.rb_statistics_by_order);
        rb_statistics_by_income = (RadioButton)findViewById(R.id.rb_statistics_by_income);
        line_which_display_order = findViewById(R.id.line_which_display_order);
        line_which_display_income = findViewById(R.id.line_which_display_income);
        tv_explain_statistics = (TextView) findViewById(R.id.tv_explain_statistics);

        tv_choose_kinds_one = (TextView) findViewById(R.id.tv_choose_kinds_one);
        tv_choose_kinds_three = (TextView) findViewById(R.id.tv_choose_kinds_three);
        tv_choose_kinds_two = (TextView) findViewById(R.id.tv_choose_kinds_two);
        tv_choose_kinds_four = (TextView) findViewById(R.id.tv_choose_kinds_four);
        tv_choose_kinds_five = (TextView) findViewById(R.id.tv_choose_kinds_five);

        lv_all_store = (ListView) findViewById(R.id.lv_all_store);
        lv_all_store.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        lv_pos_kinds = (ListView)findViewById(R.id.lv_pos_kinds);
        lv_pos_kinds.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        tv_transfer = (SelfImageView) findViewById(R.id.tv_transfer);
        tv_transfer.setAlpha(0.4f);
        filter_layout = (LinearLayout)findViewById(R.id.filter_layout);

        tv_date_exception = (TextView)findViewById(R.id.tv_date_exception);
        ll_nodata = (LinearLayout)findViewById(R.id.ll_nodata);

        //筛选付款方式的视图组件
        rl_select_right = (AutoRelativeLayout)findViewById(R.id.rl_select_right);
        tv_right_select = (TextView)findViewById(R.id.tv_select_right);
        right_icon = (ImageView)findViewById(R.id.iv_right_icon);
        lv_for_filter = (ListView)findViewById(R.id.lv_right_data);
        ll_listviews_continer = (LinearLayout)findViewById(R.id.ll_listviews_continer);
        view_line = findViewById(R.id.view_line);
        lv_choosed_goods = (ListView)findViewById(R.id.lv_choosed_goods);
        rl_select_left = (AutoRelativeLayout)findViewById(R.id.rl_select_left);
        payway_center_line = findViewById(R.id.payway_center_line);
        rl_select_left.setVisibility(View.GONE);
        payway_center_line.setVisibility(View.GONE);

        left_icon = (ImageView)findViewById(R.id.iv_left_icon);
        tv_select_left = (TextView)findViewById(R.id.tv_select_left);

        rl_select_all_tools = (AutoRelativeLayout)findViewById(R.id.rl_select_all_tools);
        tv_all_tools = (TextView)findViewById(R.id.tv_all_tools);
        iv_tools_icon = (ImageView)findViewById(R.id.iv_tools_icon);
        //打印汇总
        tv_mark_reback = (TextView)findViewById(R.id.tv_mark_reback);
        //订单搜索
        tv_cancel = (TextView)findViewById(tv_create_new_order);
        et_order_search = (EditText)findViewById(R.id.et_search);
        et_order_search.setHint("输入订单号或备注内容搜索");
        if(et_order_search.hasFocus()) {
            tv_cancel.setText("      取消     ");
        }else {
            tv_cancel.setVisibility(View.GONE);
        }
    }

    /**
     * xlistview的配置
     */
    public void initXlistView() {
        xl_pos_order.setPullRefreshEnable(false);
        xl_pos_order.setPullLoadEnable(true);
        xl_pos_order.addHeaderView(headView);
    }

    //初始化顶部标题栏,不同子类有不同的实现
    public abstract void initTitle();
    //初始化二级标题
    public  void initSecondTitle(){
        tv_title_date.setText(startTime+" "+tf.format(startHour)+":"+tf.format(startHour) + "至" + endTime+" "+tf.format(endHour)+":"+tf.format(endMinute));
    }
    //初始化头布局
    public abstract View initHeadView();
    //初始化数据.不同的子类联网请求不同的数据
    public abstract void initData();

    //公共的监听
    public void setListener() {
        iv_title.setOnClickListener(this);
        rl_date_choose.setOnClickListener(this);
        xl_pos_order.setOnItemClickListener(this);
        rl_select_left.setOnClickListener(this);
        rl_select_right.setOnClickListener(this);
        rl_select_all_tools.setOnClickListener(this);
        tv_choose_kinds_one.setOnClickListener(this);
        tv_choose_kinds_two.setOnClickListener(this);
        tv_choose_kinds_three.setOnClickListener(this);
        tv_choose_kinds_four.setOnClickListener(this);
        tv_choose_kinds_five.setOnClickListener(this);
        lv_choosed_goods.setOnItemClickListener(new GoodsItemOnClickListener());
        lv_for_filter.setOnItemClickListener(new KindsItemOnClickListener());
        lv_for_filter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        rb_statistics_by_income.setOnClickListener(this);
        rb_statistics_by_order.setOnClickListener(this);

        tv_mark_reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkUtils.isNetOK(BaseActivity.this)&& !ClicKUtils.isFastDoubleClick()) {
                    showDia(true);
                }
            }
        });
        findViewById(R.id.ll_show_wxapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.getInstance().showWxAppPup(BaseActivity.this,iv_title);
            }
        });

    }

    class KindsItemOnClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (dataType){
                case Constant.POS_ORDER_CHOOSE_GOODS:// POS列表实物商品种类
                    clickLeftPOS(position);
                    break;
                case Constant.POS_ORDER_CHOOSE_SERVICE:// H5服务商品种类
                case Constant.POS_ORDER_CHOOSE_ENTITY:// H5实物商品种类
                    clickLeftSerEnt(position);
                    break;
            }
        }
    }

    /**
     * 点击实物服务的筛选
     * @param position
     */
    private void clickLeftSerEnt(int position) {
        getChooseDataFromNet("0",String.valueOf(position), isClickLeft,goodstype,payTool);
        setShowdataBox(false);
    }

    /**
     * 点击POS的筛选
     * @param position
     */
    private void clickLeftPOS(int position) {

        if(isClickLeft==0){//左侧点击
            recoredPos(1, position);
            List<PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean> category = showHor.get(three_layer_pos[0]).getCategory();
            for(int i=0;i<category.size();i++){
                if(position==i){
                    category.get(i).setIsCheched(true);
                }else{
                    category.get(i).setIsCheched(false);
                }
            }
            filterAdapter.notifyDataSetChanged();
            List<PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean.GoodsListBean> goodsList = category.get(position).getGoodsList();
            if(goodsList!=null && goodsList.size()>0){
                goods_filterAdapter.setData(goodsList);
                //设置数据选择adapter
                switch (dataType){
                    case Constant.POS_ORDER_CHOOSE_GOODS:
                        goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_GOODS_GOODS);
                        break;
                    case Constant.POS_ORDER_CHOOSE_ENTITY:
                        goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_ENTITY_GOODS);
                        break;
                    case Constant.POS_ORDER_CHOOSE_SERVICE:
                        goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_SERVICE_GOODS);
                        break;
                }
                lv_choosed_goods.setAdapter(goods_filterAdapter);
            }else{
                hide(1);
                //下一个级别没有数据了
                getChooseDataFromNet("0",String.valueOf(position), isClickLeft,goodstype,payTool);
                setShowdataBox(false);
            }
        }else if(isClickLeft==1){//中间点击
            getChooseDataFromNet("0",String.valueOf(position),isClickLeft,goodstype,payTool);
            setShowdataBox(false);
        }else{
            List<PosOrderKinds> toolsData = (List<PosOrderKinds>)payToolsData;
            payTool = toolsData.get(position).getId();
            getChooseDataFromNet("0",String.valueOf(position),isClickLeft,goodstype,payTool);
            setShowdataBox(false);
        }
    }

    class GoodsItemOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //点击具体商品进行网络请求，显示选择数据
            if(three_layer_pos[0]==-1){
                three_layer_pos[0] = 0;
            }
            if(three_layer_pos[1]==-1){
                three_layer_pos[1] = 0;
            }
            goodstype = String.valueOf(showHor.get(three_layer_pos[0]).getGoodsType());
            recoredPos(2,position);
            List<PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean.GoodsListBean> goodsList;
            goodsList = showHor.get(three_layer_pos[0]).getCategory().get(three_layer_pos[1]).getGoodsList();
            for(int i=0;i<goodsList.size();i++){
                if(position==i){
                    goodsList.get(i).setIsCheck(true);
                }else{
                    goodsList.get(i).setIsCheck(false);
                }
            }
            hide(1);
            tv_select_left.setText(goodsList.get(position).getName());
            getChooseDataFromNet(goodsList.get(position).getSizeId(),goodsList.get(position).getId(),
                    isClickLeft,goodsList.get(position).getGoodsType(),payTool);
            setShowdataBox(false);
        }
    }

    /**
     * 此方法作用：通知子页面点击了哪里
     * @param id
     * @param isLeft
     */
    public void getChooseDataFromNet(String sizeId,String id,int isLeft, String GoodsType,String payTool){
    }

    /**
     * 记录选择的位置
     * @param pos 0水平点击位置的记录 1标识含商品订单下类目的位置 2代表点击的具体哪个商品
     * @param id
     */
    private void recoredPos(int pos,int id){
        three_layer_pos[pos] = id;
        switch (pos){
            case 0:
                clearNamedPos(1);
            case 1:
                clearNamedPos(2);
                break;
        }
    }

    /**
     * 清除选择的位置
     */
    private void clearNamedPos(int pos){
        three_layer_pos[pos] = -1;
    }

    /**
     * xlistview的item的点击监听
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title://back
                finish();
                break;
            case R.id.rl_date_choose://日期筛选
                showDia(false);
                break;
            case R.id.rl_select_left://点击左侧筛选
                isClickLeft = 0;
                if(leftData!=null&&leftData.size()>0) {
                    switch (dataType){
                        case Constant.POS_ORDER_CHOOSE_GOODS:
                            if(close1){
                                inflaterData(1);
                            }
                            changeCurrentState(1);
                            break;
                        case Constant.POS_ORDER_CHOOSE_SERVICE:
                        case Constant.POS_ORDER_CHOOSE_ENTITY:
                            if(close1){
                                inflaterData(1);
                            }
                            changeCurrentState(1);
                            break;
                    }
                }else {
                    Toast.makeText(BaseActivity.this, "当前条件下没有筛选数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_select_right://点击中间筛选
                isClickLeft = 1;
                if(rightData!=null&&rightData.size()>0) {
                    if(close2) {//如果右侧打开状态
                        inflaterData(2);
                    }
                    changeCurrentState(2);
                }else {
                    Toast.makeText(BaseActivity.this, "当前条件下没有筛选数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_select_all_tools://点击右侧筛选
                isClickLeft = 2;
                if(payToolsSelectData!=null&&payToolsSelectData.size()>0) {
                    if(close3) {//如果右侧打开状态
                        inflaterData(3);
                    }
                    changeCurrentState(3);
                }else {
                    Toast.makeText(BaseActivity.this, "当前条件下没有筛选数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_choose_kinds_one:
                recoredPos(0, 0);//记录水平位置点击
                chooseHorKinds(0,false);
                break;
            case R.id.tv_choose_kinds_two:
                recoredPos(0, 1);
                chooseHorKinds(1,false);
                break;
            case R.id.tv_choose_kinds_three:
                recoredPos(0, 2);
                chooseHorKinds(2,false);
                break;
            case R.id.tv_choose_kinds_four:
                recoredPos(0, 3);
                chooseHorKinds(3,false);
                break;
            case R.id.tv_choose_kinds_five:
                recoredPos(0, 4);
                chooseHorKinds(4,false);
                break;
            case R.id.rb_statistics_by_income:
//                rl_select_left.setVisibility(View.VISIBLE);
                rl_select_all_tools.setVisibility(View.VISIBLE);
                line_which_display_income.setVisibility(View.VISIBLE);
                line_which_display_order.setVisibility(View.GONE);
                hideShowDropBox();
                chooseStatistics(0);
                break;
            case R.id.rb_statistics_by_order:
                rl_select_all_tools.setVisibility(View.GONE);
                rl_select_left.setVisibility(View.GONE);
                line_which_display_income.setVisibility(View.GONE);
                line_which_display_order.setVisibility(View.VISIBLE);
                //如果有rl_select_all_tools下拉菜单在展示 就收上去
                LogUtils.e(TAG,"case R.id.rb_statistics_by_order:");
                LogUtils.e(TAG,"isClickLeft=="+isClickLeft);
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

    /**
     * 顶部选择状态发生改变
     * @param which
     */
    public void chooseStatistics(int which){
        LogUtils.e(TAG,"close1="+close1+",close2="+close2+",close3="+close3);
        //清除所有的记录信息
        three_layer_pos = new int[]{-1,-1,-1};
    }

    /**
     * 筛选的数据的填充
     * @param leftOrRight 1是左侧，2是右侧 3是最右側
     */
    private void inflaterData(int leftOrRight) {
        switch (dataType){
            case Constant.POS_ORDER_CHOOSE_GOODS://右侧 POS列表实物商品种类
                posChooseItemData(leftOrRight);
                break;
            case Constant.POS_ORDER_CHOOSE_SERVICE://右侧 H5服务商品种类
                serviceChooseItemData(leftOrRight);
                break;
            case Constant.POS_ORDER_CHOOSE_ENTITY://右侧 H5实物商品种类
                entityChooseItemData(leftOrRight);
                break;
        }
    }

    /**
     * H5实物列表数据的填充
     * @param leftOrRight
     */
    private void entityChooseItemData(int leftOrRight) {
        switch (leftOrRight){
            case 1://左侧:横行选项不出现 只有一级列表
                if(leftData.size()>0){
                    List<EntityOrderBean.GoodsNameBean> leftlist = leftData;
                    filterAdapter.setData(leftlist);
                    filterAdapter.setDataFrom(Constant.ORDER_ENTITY_LEFT);
                    lv_for_filter.setAdapter(filterAdapter);
                }
                break;
            case 2://右侧:
                if(rightData.size()>0){
                    List<PosOrderKinds> rightlist = (List<PosOrderKinds>)rightData;
                    filterAdapter.setData(rightlist);
                    filterAdapter.setDataFrom(Constant.KINDS_CHOOSE_RIGHT);
                    lv_for_filter.setAdapter(filterAdapter);
                }
                break;
        }
    }

    /**
     * H5服务列表数据的处理
     * @param leftOrRight
     */
    private void serviceChooseItemData(int leftOrRight) {
        switch (leftOrRight){
            case 1://左侧
                if(leftData.size()>0){
                    List<ServerOrderBean.MsgBean.GoodsNameBean> leftlist = (List<ServerOrderBean.MsgBean.GoodsNameBean>)leftData;
                    filterAdapter.setData(leftlist);
                    filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_SERVICE_GOODS);
                    lv_for_filter.setAdapter(filterAdapter);
                }
                break;
            case 2://右侧
                if(rightData.size()>0){
                    List<PosOrderKinds> rightlist = (List<PosOrderKinds>)rightData;
                    filterAdapter.setData(rightlist);
                    filterAdapter.setDataFrom(Constant.KINDS_CHOOSE_RIGHT);
                    lv_for_filter.setAdapter(filterAdapter);
                }
                break;
        }
    }

    /**
     * pos机收款列表数据填充
     * @param leftOrRight
     */
    private void posChooseItemData(int leftOrRight) {
        switch (leftOrRight) {
            case 1 ://左侧
                boolean b = showHorChooseItem();
                if(b){
                    if(three_layer_pos[0] ==-1){
                        chooseHorKinds(0,true);
                    }else{
                        if(three_layer_pos[1]==-1){
                            //2.设置水平选中，其他都不默认第一项选中
                            LogUtils.e(TAG,"test1 chooseHorKinds three_layer_pos[0]="+three_layer_pos[0]);
                            chooseHorKinds(three_layer_pos[0],true);
                            filterAdapter.setData(showHor.get(three_layer_pos[0]).getCategory());
                            filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_GOODS_KINDS);//左侧的筛选来根据不同界面展示不同
                            lv_for_filter.setAdapter(filterAdapter);
                            if(showHor.get(three_layer_pos[0]).getCategory().size()>0){
                                goods_filterAdapter.setData(showHor.get(three_layer_pos[0]).getCategory().get(0).getGoodsList());
                                goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_GOODS_GOODS);
                                lv_choosed_goods.setAdapter(goods_filterAdapter);
                            }
                        }else{
                            if(three_layer_pos[2]==-1){
                                //3.设置水平选中，种类选中，商品不选中
                                LogUtils.e(TAG,"test2 chooseHorKinds three_layer_pos[0]="+three_layer_pos[0]);
                                chooseHorKinds(three_layer_pos[0],true);
                                List<PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean> category = showHor.get(three_layer_pos[0]).getCategory();
                                category.get(three_layer_pos[1]).setIsCheched(true);
                                filterAdapter.setData(category);
                                filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_GOODS_KINDS);//左侧的筛选来根据不同界面展示不同
                                lv_for_filter.setAdapter(filterAdapter);

                                goods_filterAdapter.setData(showHor.get(three_layer_pos[0]).getCategory().get(three_layer_pos[1]).getGoodsList());
                                goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_GOODS_GOODS);
                                lv_choosed_goods.setAdapter(goods_filterAdapter);
                            }else{
                                //4.设置水平选中，种类选中，商品选中
                                LogUtils.e(TAG,"test3 chooseHorKinds three_layer_pos[0]="+three_layer_pos[0]);
                                chooseHorKinds(three_layer_pos[0],true);
                                List<PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean> category = showHor.get(three_layer_pos[0]).getCategory();
                                category.get(three_layer_pos[1]).setIsCheched(true);
                                filterAdapter.setData(category);
                                filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_GOODS_KINDS);//左侧的筛选来根据不同界面展示不同
                                lv_for_filter.setAdapter(filterAdapter);

                                List<PosOrderBean.MsgBean.GoodsNameListBean.CategoryBean.GoodsListBean> goodsList = showHor.get(three_layer_pos[0]).getCategory().get(three_layer_pos[1]).getGoodsList();
                                goodsList.get(three_layer_pos[2]).setIsCheck(true);
                                goods_filterAdapter.setData(goodsList);
                                goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_GOODS_GOODS);
                                lv_choosed_goods.setAdapter(goods_filterAdapter);
                            }
                        }
                    }
                    if(leftData.size()>7) {
                        ViewGroup.LayoutParams para=lv_for_filter.getLayoutParams();
                        para.height= Utils.dip2px(this, 300);
                        lv_for_filter.setLayoutParams(para);
                    }
                }
                break;
            case 2 ://右侧
                filterAdapter.setData(rightData);
                filterAdapter.setDataFrom(Constant.KINDS_CHOOSE_RIGHT);//右侧的筛选bean类型相同
                lv_for_filter.setAdapter(filterAdapter);
                break;
            case 3 ://最右侧
                filterAdapter.setData(payToolsData);
                filterAdapter.setDataFrom(Constant.KINDS_CHOOSE_RIGHT);//最右侧的筛选bean类型相同
                lv_for_filter.setAdapter(filterAdapter);
                break;
        }
    }

    /**
     * 设置水平选项点击切换
     * @param i
     */
    private void chooseHorKinds(int i,boolean isInit) {
        switch (i){//设置水平选择项第一个选中
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
        if(!isInit){
            for(int j=0;j<showHor.get(i).getCategory().size();j++){
                if(i==0){
                    showHor.get(i).getCategory().get(j).setIsCheched(true);
                }else{
                    showHor.get(i).getCategory().get(j).setIsCheched(false);
                }
            }
        }
        filterAdapter.setData(showHor.get(i).getCategory());
        switch (dataType){
            case Constant.POS_ORDER_CHOOSE_GOODS:
                filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_GOODS_KINDS);
                break;
            case Constant.POS_ORDER_CHOOSE_ENTITY:
                filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_ENTITY_KINDS);
                break;
            case Constant.POS_ORDER_CHOOSE_SERVICE:
                filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_SERVICE_KINDS);
                break;
        }
        lv_for_filter.setAdapter(filterAdapter);
        if(showHor.get(i).getCategory().size()>0){
            lv_for_filter.setVisibility(View.VISIBLE);
            lv_choosed_goods.setVisibility(View.VISIBLE);
            ll_listviews_continer.setVisibility(View.VISIBLE);
            if(!isInit){
                for(int k=0;k<showHor.get(i).getCategory().size();k++){
                    if(k==0){
                        showHor.get(i).getCategory().get(k).setIsCheched(true);
                    }else{
                        showHor.get(i).getCategory().get(k).setIsCheched(false);
                    }
                }
            }
            goods_filterAdapter.setData(showHor.get(i).getCategory().get(0).getGoodsList());
            switch (dataType){
                case Constant.POS_ORDER_CHOOSE_GOODS:
                    goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_GOODS_GOODS);
                    break;
                case Constant.POS_ORDER_CHOOSE_ENTITY:
                    goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_ENTITY_GOODS);
                    break;
                case Constant.POS_ORDER_CHOOSE_SERVICE:
                    goods_filterAdapter.setDataFrom(Constant.POS_ORDER_CHOOSE_SERVICE_GOODS);
                    break;
            }
            lv_choosed_goods.setAdapter(goods_filterAdapter);

        }else{
            lv_for_filter.setVisibility(View.GONE);
            lv_choosed_goods.setVisibility(View.GONE);
            ll_listviews_continer.setVisibility(View.GONE);
            if(!isInit){
                LogUtils.e(TAG, "点击选择 进行数据请求...");
                hide(1);
                tv_select_left.setText(showHor.get(i).getTypeName());
                if(three_layer_pos[0]==-1){
                    three_layer_pos[0] = 0;
                }
                goodstype = String.valueOf(showHor.get(three_layer_pos[0]).getGoodsType());
                getChooseDataFromNet("0","0",isClickLeft,String.valueOf(showHor.get(i).getGoodsType()),payTool);
                setShowdataBox(false);
            }
        }
    }

    private void setSelectedColor(String color_one,String color_two,String color_three,String color_four,String color_five) {
        tv_choose_kinds_one.setTextColor(Color.parseColor(color_one));
        tv_choose_kinds_two.setTextColor(Color.parseColor(color_two));
        tv_choose_kinds_three.setTextColor(Color.parseColor(color_three));
        tv_choose_kinds_four.setTextColor(Color.parseColor(color_four));
        tv_choose_kinds_five.setTextColor(Color.parseColor(color_five));
    }

    /**
     * 设置左侧横行显示数据
     */
    private boolean showHorChooseItem() {
        int size = leftData.size();
        showHor = (List<PosOrderBean.MsgBean.GoodsNameListBean>)leftData;
        switch (size){
            case 0:
                ll_listviews_continer.setVisibility(View.GONE);
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

    private void showDia(boolean showPrint) {
        if(showPrint) {
            LogUtils.e(TAG,"showDia start="+pstartTime+"  end="+pendTime);
            Calendar c = Calendar.getInstance();
            doubleDatePickerDialog = new DoubleDatePickerDialog(BaseActivity.this, 0, callBack,
                    pstartTime,pendTime,
                    pstartHour,pstartMinute,
                    pendHour,pendMinute,
                    true,showPrint);
            DatePicker datePickerStart = doubleDatePickerDialog.getDatePickerStart();
            DatePicker datePickerEnd = doubleDatePickerDialog.getDatePickerEnd();
            //设置开始时间最大为今日,结束日期最大为今日
            datePickerStart.setMaxDate(c.getTimeInMillis());
            datePickerEnd.setMaxDate(c.getTimeInMillis());
            //保存当前显示日期，取消或back后仍然显示之前的日期
            doubleDatePickerDialog.show();
        }else {
            Calendar c = Calendar.getInstance();
            doubleDatePickerDialog = new DoubleDatePickerDialog(BaseActivity.this, 0, callBack,
                    startTime,endTime,
                    startHour,startMinute,
                    endHour,endMinute,
                    true,showPrint);
            DatePicker datePickerStart = doubleDatePickerDialog.getDatePickerStart();
            DatePicker datePickerEnd = doubleDatePickerDialog.getDatePickerEnd();
            //设置开始时间最大为今日,结束日期最大为今日
            datePickerStart.setMaxDate(c.getTimeInMillis());
            datePickerEnd.setMaxDate(c.getTimeInMillis());
            //保存当前显示日期，取消或back后仍然显示之前的日期
            beforeDate = tv_title_date.getText().toString();
            doubleDatePickerDialog.show();
        }
    }


    /**
     * 改变当前的店铺信息展示状态
     * @param clickPosition 初次点击0，左侧为1，右侧为2，点击空白为3(del)
     * @param clickPosition 初次点击0，左侧为1，右侧为2，3点击增加工具页面
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

    public  void setLeftFilterData(List leftData,int dataType){//设置左侧和右侧的筛选数据，由每个子类实现
        this.leftData=leftData;
        this.dataType=dataType;
    }

    public  void setRightFilterData(List rightData,int dataType){
        this.rightData=rightData;
        this.dataType=dataType;
    }

    public  void setPayToolsFilterData(List payToolsData,int dataType){
        this.payToolsData=payToolsData;
        this.dataType=dataType;
    }

    private void hide(int pos) {
        switch (pos) {
            case 1://左侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_all_store.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
                break;
            case 2://右侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_for_filter.setVisibility(View.GONE);
                break;
            case 3://最右侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_for_filter.setVisibility(View.GONE);
                break;
        }
        tv_transfer.setVisibility(View.GONE);
        closeAnimation();
    }

    /**
     * 打开下拉
     */
    private void show(int pos) {
        switch (pos) {
            case 1://左侧
                if(dataType== Constant.POS_ORDER_CHOOSE_SERVICE||dataType== Constant.POS_ORDER_CHOOSE_ENTITY) {
                    ll_selector_add.setVisibility(View.VISIBLE);
                    ll_service_kinds.setVisibility(View.GONE);
                    lv_choosed_goods.setVisibility(View.GONE);
                    view_line.setVisibility(View.GONE);
                    ll_listviews_continer.setVisibility(View.VISIBLE);
                    lv_for_filter.setVisibility(View.VISIBLE);
                }else {
                    ll_service_kinds.setVisibility(View.VISIBLE);
                    lv_choosed_goods.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                    ll_selector_add.setVisibility(View.GONE);
                    ll_listviews_continer.setVisibility(View.GONE);
                    lv_for_filter.setVisibility(View.GONE);
                }
                break;
            case 2://右侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_choosed_goods.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
                ll_selector_add.setVisibility(View.VISIBLE);
                ll_listviews_continer.setVisibility(View.VISIBLE);
                lv_for_filter.setVisibility(View.VISIBLE);
                break;
            case 3://最右侧
                ll_service_kinds.setVisibility(View.GONE);
                lv_choosed_goods.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
                ll_selector_add.setVisibility(View.VISIBLE);
                ll_listviews_continer.setVisibility(View.VISIBLE);
                lv_for_filter.setVisibility(View.VISIBLE);
                break;
        }
        tv_transfer.setVisibility(View.VISIBLE);
        openAnimation();
    }

    /**
     * 列表拉出的动画
     */
    public void openAnimation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(500);
        ll_selector_add.startAnimation(ta);
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
        ta=null;
    }

    /**
     * 列表关闭的动画
     */
    public void closeAnimation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        ta.setDuration(200);
        ll_selector_add.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_selector_add.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ta=null;
    }

    private void CannotDisMiss() {
        try {
            Field field = doubleDatePickerDialog.getClass()
                    .getSuperclass().getSuperclass().getDeclaredField(
                            "mShowing");
            field.setAccessible(true);
            field.set(doubleDatePickerDialog, false);// 将mShowing变量设为false，表示对话框已关闭
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

    public void loadFailure(String e){
        tv_loading.setText(e);
        pb_loading.setVisibility(View.GONE);
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.couldnot_get_data);
    }

    public void reLoad(){
        ll_loading.setVisibility(View.VISIBLE);
        tv_loading.setText("拼命加载中...");
        pb_loading.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setVisibility(View.GONE);
    }

    public void netError() {
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.net_error);
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("请检查网络后，点击屏幕重新加载！");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(doubleDatePickerDialog!=null&&doubleDatePickerDialog.isShowing()) {
                    CanDismiss(doubleDatePickerDialog);
                    tv_title_date.setText(beforeDate);
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
