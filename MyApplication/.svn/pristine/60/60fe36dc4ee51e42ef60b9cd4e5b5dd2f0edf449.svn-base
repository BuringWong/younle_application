package com.yongle.letuiweipad.pagers;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.MainActivity;
import com.yongle.letuiweipad.activity.manager.weigher.WeighManager;
import com.yongle.letuiweipad.adapter.GoodServiceAdapter;
import com.yongle.letuiweipad.adapter.ItemContentAdapter;
import com.yongle.letuiweipad.adapter.NewOrderRightAdapter;
import com.yongle.letuiweipad.adapter.RecyclerAdapter;
import com.yongle.letuiweipad.application.MyApplication;
import com.yongle.letuiweipad.basepager.BasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.dao.DaoSession;
import com.yongle.letuiweipad.dao.GoodBeanDao;
import com.yongle.letuiweipad.dao.GoodsTypeInfoDao;
import com.yongle.letuiweipad.domain.PrintDevices;
import com.yongle.letuiweipad.domain.SmScannerBean;
import com.yongle.letuiweipad.domain.WmPintData;
import com.yongle.letuiweipad.domain.createorder.AllGoodsInfoBean;
import com.yongle.letuiweipad.domain.createorder.CommitOrderBean;
import com.yongle.letuiweipad.domain.createorder.DetailMemberBean;
import com.yongle.letuiweipad.domain.createorder.DiscountInfo;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.domain.createorder.OrderBean;
import com.yongle.letuiweipad.domain.createorder.PayCardinfoBean;
import com.yongle.letuiweipad.domain.createorder.RulesBean;
import com.yongle.letuiweipad.domain.createorder.SavedFailOrder;
import com.yongle.letuiweipad.domain.createorder.UnPayDetailsBean;
import com.yongle.letuiweipad.domain.goodinfo.GoodsTypeInfo;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.domain.printer.YunPrintGroupBean;
import com.yongle.letuiweipad.pagers.unpayorder.UnPayOrderListFragment;
import com.yongle.letuiweipad.selfinterface.BleWeighListener;
import com.yongle.letuiweipad.selfinterface.DetailOnDelListener;
import com.yongle.letuiweipad.selfinterface.GoodKindsOnClickListener;
import com.yongle.letuiweipad.selfinterface.LocalWeighListener;
import com.yongle.letuiweipad.selfinterface.OnPayFinish;
import com.yongle.letuiweipad.selfinterface.OnWeighGoodChangeListener;
import com.yongle.letuiweipad.selfinterface.PrintSecond;
import com.yongle.letuiweipad.selfinterface.RecyclerItemClickListener;
import com.yongle.letuiweipad.utils.CashCardPayUtils;
import com.yongle.letuiweipad.utils.FpUtils;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.NetworkUtils;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.PayTranstions;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.SearchUtils;
import com.yongle.letuiweipad.utils.SpUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.yongle.letuiweipad.utils.pay.PayUtils;
import com.yongle.letuiweipad.utils.printmanager.BTPrintUtils;
import com.yongle.letuiweipad.utils.printmanager.BluetoothService;
import com.yongle.letuiweipad.utils.printmanager.PrintUtils;
import com.yongle.letuiweipad.utils.printmanager.YunPrintUtils;
import com.yongle.letuiweipad.utils.scanbar.HidConncetUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import static com.yongle.letuiweipad.utils.SaveUtils.getObject;

/**
 * Created by Administrator on 2017/9/10.
 * 打印模块：1.手动打印分订单 2. 提交订单自动打印 3.结账打印订单信息 4.历史订单的详情打印
 */

public class OrderPgerFragment extends BasePager {
    @BindView(R.id.tv_nogs)
    TextView tv_nogs;
    @BindView(R.id.tv_right_title) TextView tv_right_title;
    @BindView(R.id.gv_left)GridView gv_left;
    @BindView(R.id.gv_search)GridView gv_search;
    @BindView(R.id.et_search)EditText et_search;
    @BindView(R.id.tv_cancel_search) TextView tv_cancel_search;
    @BindView(R.id.lv_right)ListView lv_right;
    @BindView(R.id.lv_selected_gs)ListView lv_selected_gs;
    @BindView(R.id.rl_nomember_header)AutoRelativeLayout rlNomemberHeader;
    @BindView(R.id.member_name)TextView memberName;
    @BindView(R.id.tv_member_left)TextView tvMemberLeft;
    @BindView(R.id.al_member_left)AutoLinearLayout alMemberLeft;
    @BindView(R.id.member_average_consume) TextView memberAverageConsume;
    @BindView(R.id.tv_come_times)TextView tvComeTimes;
    @BindView(R.id.member_fav) TextView memberFav;
    @BindView(R.id.ll_member_info) AutoLinearLayout llMemberInfo;
    @BindView(R.id.order_gs_num)TextView orderGsNum;
    @BindView(R.id.order_gs_acc) TextView orderGsAcc;
    @BindView(R.id.total_num_acc)AutoRelativeLayout totalNumAcc;
    @BindView(R.id.tv_totalfee) TextView tvTotalfee;
    @BindView(R.id.tv_youhui_way) TextView tvYouhuiWay;
    @BindView(R.id.tv_youhuifee) TextView tvYouhuifee;
    @BindView(R.id.tv_should_pay)  TextView tvShouldPay;
    @BindView(R.id.tv_ordertime)  TextView tvOrdertime;
    @BindView(R.id.tv_order_num) TextView tvOrderNum;
    @BindView(R.id.member_pay)
    RadioButton memberPay;
    @BindView(R.id.member_pay_line)
    View memberPayLine;
    @BindView(R.id.tv_cashpay_shishou)  TextView tvCashpayShishou;
    @BindView(R.id.tv_cashpay_return) TextView tvCashpayReturn;
    @BindView(R.id.ll_acc) AutoLinearLayout llAcc;
    @BindView(R.id.btn7)
    Button btn7;
    @BindView(R.id.btn8) Button btn8;
    @BindView(R.id.btn9) Button btn9;
    @BindView(R.id.btn4) Button btn4;
    @BindView(R.id.btn5) Button btn5;
    @BindView(R.id.btn6) Button btn6;
    @BindView(R.id.btn1) Button btn1;
    @BindView(R.id.btn2)  Button btn2;
    @BindView(R.id.btn3) Button btn3;
    @BindView(R.id.btn0) Button btn0;
    @BindView(R.id.btn00) Button btn00;
    @BindView(R.id.btn_spot) Button btnSpot;
    @BindView(R.id.btn_del) Button btnDel;
    @BindView(R.id.btn_clear) Button btnClear;
    @BindView(R.id.tv_selected_nogs)  TextView tv_no_gs;
    @BindView(R.id.order_right_pay)
    LinearLayout order_right_pay;
    @BindView(R.id.order_right) LinearLayout order_right;
    @BindView(R.id.change_price) TextView change_price;
    @BindView(R.id.rl_wx_zhf)RelativeLayout rl_wx_zhf;
    @BindView(R.id.ll_pay_card)RelativeLayout ll_pay_card;
    @BindView(R.id.ll_pay_member)LinearLayout ll_pay_member;
    @BindView(R.id.ll_pay_cash)LinearLayout ll_pay_cash;
    @BindView(R.id.iv_find_code1) TextView iv_find_code1;
    @BindView(R.id.iv_find_code2) TextView iv_find_code2;
    @BindView(R.id.order_left) RelativeLayout order_left;

    @BindView(R.id.uncommit_footer)LinearLayout uncommit_footer;
    @BindView(R.id.comitted_footer)LinearLayout comitted_footer;
    @BindView(R.id.rg_payways)
    RadioGroup rg_payways;
    @BindView(R.id.al_payway_error) RelativeLayout payWayError;
    @BindView(R.id.tv_payway_error_notice) TextView tvPaywayErrorNotice;
    @BindView(R.id.tv_memberpay_memberleft) TextView tvMemberpayMemberLeft;
    @BindView(R.id.tv_memberpay_reduce) TextView tvMemberpayReduce;
    @BindView(R.id.tv_member_pay) TextView tv_member_pay;
    @BindView(R.id.btn_uncommit_cancel) Button btn_uncommit_cancel;
    @BindView(R.id.ll_scanner_state) LinearLayout ll_scanner_state;
    @BindView(R.id.iv_scanner_state)
    ImageView iv_scanner_state;
    @BindView(R.id.textView5) TextView tvMemberPriceTitle;
    @BindView(R.id.textView4) TextView textView4;
    @BindView(R.id.totalfee_name) TextView totalfee_name;
    @BindView(R.id.tv_memmber_reduce) TextView tv_memmber_reduce;
    @BindView(R.id.tv_reducefee_name) TextView tv_reducefee_name;
    @BindView(R.id.ll_weigher_state) LinearLayout ll_weigher_state;
    @BindView(R.id.iv_weigher_state) ImageView ivWeigherState;
    @BindView(R.id.pay_scaner_off) TextView payScannerOff;
    @BindView(R.id.pay_scanner_normal) RelativeLayout payScannerNormal;
    @BindView(R.id.fl_order_list)
    FrameLayout fl_order_list;
    @BindView(R.id.commited_order_up) Button commited_order_up;
    @BindView(R.id.no_member_notice) TextView noMemberNotice;
    @BindView(R.id.tv_inditify_member) TextView tv_inditify_member;
    @BindView(R.id.commit_order) Button commit_order;
    @BindView(R.id.net_error) RelativeLayout netError;
    @BindView(R.id.rl_order_remark) RelativeLayout rlOrderRemark;
    @BindView(R.id.tv_order_remark) TextView tvOrderRemark;
    @BindView(R.id.tv_current_weigh) TextView tv_current_weigh;
    private GoodServiceAdapter gs_detail_adapter;
    private ItemContentAdapter selectedAdapter;
    private NewOrderRightAdapter right_adapter;
    private String transaction_id = "";
    private HashMap<View, String> map;
    private String payment = "0";//最终使用这个钱数支付
    private String order_total_fee;//订单总金额
    private static final int UPDATE_UI = 1;
    private String primaryKeyId = "0";
    private String query_num = "0";
    private List<String> unPayCustomerPrintData = new ArrayList<>();
    private List<String> unPayBTCustomerPrintData = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.TOTAL_ACCOUNT:
                    handler.removeMessages(UPDATE_UI);
                    Bundle data = msg.getData();
                    int isEditClick = data.getInt("e_c");//是不是编辑(1)还是点击(0)或者清空数据(2)
                    double goodPice = data.getDouble("g_p");//传递的数量都是变化量 不是总量
                    double goodNum = data.getDouble("g_n");
                    target_poisition = data.getInt("target_poisition");
                    int gsAdapterPosition = data.getInt("adapter_position");
                    switch (isEditClick) {
                        case 0://点击
                            Constant.order_price += goodPice;
                            Constant.order_goods_num += goodNum;
                            break;
                        case 1://编辑
                            Constant.order_price += goodPice * goodNum;
                            Constant.order_goods_num += goodNum;
                            break;
                        case 2://清空数据
                            Constant.order_price = 0.00;
                            Constant.order_goods_num = 0.00;
                            break;
                    }
//                    upDataGs(gsAdapterPosition);
                    updateUI(Constant.order_price, Constant.order_goods_num,gsAdapterPosition!=-2);
                    break;
                case Constant.UNPAYITEM_TO_UNPAYDETAIL:
                    data=msg.getData();
                    String orderId = data.getString("orderId");
                    LogUtils.Log("收到了订单列表的信息id=="+orderId);
                    if(TextUtils.equals("-1",orderId)) {
                        clearLocalData(true,false);
                    }else {
                        primaryKeyId= orderId;
                        getUnpayOrderDetails(false);
                        changeUnpayDetailFoot();
                        whetherCanNotifyMember(true);
                    }
                    break;
                case Constant.MESSAGE_STATE_CHANGE:
                    LogUtils.e(TAG, "收到了连接状态变更的消息");
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(mActivity, "已连接", Toast.LENGTH_SHORT).show();
                            if(payed) {
                                printPayedByBluetooth();
                            }else {
                                printByBluetooth();
                            }
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(mActivity, "正在连接", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(mActivity, "无连接", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };

    private AlertDialog changePriceDialog;

    private PrintDevices savedScanner;
    private boolean showMemberPrice=false;
    private ArrayList<GoodBean> filterData=new ArrayList<GoodBean>();
    private GoodServiceAdapter filterAdapter;
    private boolean everRegistWeighReceiver;
    private boolean everRegistScannerReceiver;
    private boolean weigher_connected;
    /**
     * 是否是分订单
     */
    private boolean branachOrder=false;
    private int mainIndex;
    private UnPayOrderListFragment unPayOrderListFragment;
    private PrintDevices savedWeigher;
    private boolean unpayOrderPayLayoutVisiable;
    private List<List<WmPintData>> unpayMerchantData=new ArrayList<>();
    private List<List<WmPintData>> unpayBTMerchantData=new ArrayList<>();
    private OrderBean tempOrder;
    private BluetoothService mService;
    private SavedPrinter btPrinter;
    private SavedPrinter loaclPrinter;
    private String btPrinterPermission;
    private String localPrinterPermission;
    private List<YunPrintGroupBean> oneByOneYunList;
    private boolean payed=false;
    private boolean everShowWeighErr=false;
    private TextView tv_weigh_mode;
    private TextView tv_center;
    private String weigh_unit;
    private String weigh_mode;
    private String weigh_zf;
    private Double weigh_acc;
    /**
     * 电子秤版本：1，新秤 0，旧秤
     */
    private int weigh_version=0;
    private AlertDialog setZeroDia;
    private boolean everSet=false;
    private LinearLayoutManager llManager;
    private RecyclerAdapter chargeAdapter;
    private int usePackage =0;
    private AlertDialog scanMemberDia;
    private String song;
    private boolean hidden;
    private List<GoodBean> goodBeanList;

    /**
     * @param adapterPosition 商品列表总需要更新的item的位置，当从已选列表添加时，该值为-1，不可局部刷新
     */
    private void upDataGs(int adapterPosition) {
//        gs_detail_adapter.notifyDataSetChanged();
        try{
            if(-1==adapterPosition) {
                gs_detail_adapter.notifyDataSetChanged();
                return;
            }
            int firstVisiblePosition = gv_left.getFirstVisiblePosition();
            int lastVisiblePosition = gv_left.getLastVisiblePosition();
            if (adapterPosition >= firstVisiblePosition && adapterPosition <= lastVisiblePosition) {
                View childAt = gv_left.getChildAt(adapterPosition - firstVisiblePosition);
                gs_detail_adapter.getView(adapterPosition, childAt, gv_left);
            }
        }catch (Exception e){
            gs_detail_adapter.notifyDataSetChanged();
        }

    }

    private int target_poisition;
    private PopupWindow scanMemberPup;
    private DetailMemberBean memberBean;
    private View totalView;
    /**
     * 支付和选商品页面
     */
    private String trade_num;
    private String trade_time;
    private String vipcreate_id = "0";
    private String vip_discount = "0";
    private UnPayDetailsBean orderBean;
    private String cardinfo_json;
    private NetWorks netWorks;
    private String memberFreeMoney;
    private IWoyouService iWoyouService;
    private String total_fee = "0";
    private DiscountInfo discountInfo;
    private String tradetime;
    private String printJZTime;
    private String way = "未知";
    public OrderPgerFragment() {
        EventBus.getDefault().register(this);
        LogUtils.Log("OrderPgerLeftFragment的构造");
    }
    @Override
    public View initView() {
        LogUtils.e("temp","OrderPgerFragment  initView()");
        totalView = View.inflate(mActivity, R.layout.order_base_layout, null);
        return totalView;
    }

    @SuppressLint("StaticFieldLeak")
    @OnTextChanged(value = R.id.et_search,callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void searchGs(Editable editable){
        final String inpuStr = editable.toString();

        if(inpuStr.length()<=0) {
            filterData.clear();
            filterAdapter.notifyDataSetChanged();
            return;
        }

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                Utils.showWaittingDialogNoPb(mActivity,"检索中...");
            }

            @Override
            protected Void doInBackground(Void... params) {
                SearchUtils.getInstance().search(inpuStr,filterData,goodBeanList);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                if(!everSet) {
                    everSet=true;
                    filterAdapter.setData(filterData);
                    gv_search.setAdapter(filterAdapter);
                    filterAdapter.setWigherState(weigher_connected);
                }else {
                    filterAdapter.notifyDataSetChanged();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       Utils.dismissWaittingDialog();
                    }
                },1);
            }
        }.execute();
    }

    @OnFocusChange(R.id.et_search)
    void focusChange(View view, boolean b){
        if(b) {
            if(goodBeanList==null) {
                MyApplication application=(MyApplication)mActivity.getApplication();
                DaoSession daoSession = application.getDaoSession();
                GoodBeanDao goodBeanDao = daoSession.getGoodBeanDao();
                goodBeanList = goodBeanDao.loadAll();
                SearchUtils.getInstance().parseData(goodBeanList);
            }

            lv_right.setVisibility(View.INVISIBLE);
            gv_left.setVisibility(View.GONE);
            gv_search.setVisibility(View.VISIBLE);
            tv_cancel_search.setVisibility(View.VISIBLE);
//            ((MainActivity)mActivity).hideLogo(false);

        }else {
            lv_right.setVisibility(View.VISIBLE);
            gv_left.setVisibility(View.VISIBLE);
            gv_search.setVisibility(View.GONE);
            tv_cancel_search.setVisibility(View.GONE);
            et_search.setText(null);
            Utils.hideKeyboard(totalView);
//            ((MainActivity)mActivity).hideLogo(true);
        }
    }

    private void initState() {
        setPayWayListener();
        tvMemberPriceTitle.setVisibility(View.GONE);
        textView4.setText("金额");
        savedScanner = (PrintDevices) getObject(mActivity, Constant.SAVED_BLUETOOTH_SCANNER);
        savedWeigher = (PrintDevices) getObject(mActivity, Constant.SAVED_BLUETOOTH_WEIGHER);
        if(savedScanner==null&&savedWeigher==null) {
            tv_right_title.setText("选择商品");
        }else {
            tv_right_title.setText("外接设备：");
        }
        initBarScanner();
        if(Build.MODEL.contains("S2")) {
            ll_weigher_state.setVisibility(View.GONE);
            weigher_connected=true;
            initLocalWeigher();
        }else {
//            initWeigher();
            initBleWeigher();
        }
        if (map == null) {
            map = new HashMap<>();
            map.put(btn0, mActivity.getResources().getString(R.string.zero));
            map.put(btn1, mActivity.getResources().getString(R.string.one));
            map.put(btn2, mActivity.getResources().getString(R.string.two));
            map.put(btn3, mActivity.getResources().getString(R.string.three));
            map.put(btn4, mActivity.getResources().getString(R.string.four));
            map.put(btn5, mActivity.getResources().getString(R.string.five));
            map.put(btn6, mActivity.getResources().getString(R.string.six));
            map.put(btn7, mActivity.getResources().getString(R.string.seven));
            map.put(btn8, mActivity.getResources().getString(R.string.eight));
            map.put(btn9, mActivity.getResources().getString(R.string.nine));
            map.put(btn00, mActivity.getResources().getString(R.string.double_zero));
            map.put(btnSpot, mActivity.getResources().getString(R.string.point));
            tvCashpayShishou.setText("00.00");

        }
    }

    /**
     * 商米s2的秤的初始化操作
     */
    private void initLocalWeigher() {
        ((MainActivity)mActivity).setLocalWeighListener(new LocalWeighListener() {
            @Override
            public void onWeighConnect() {

            }

            @Override
            public void onWeighDisconnect() {

            }

            @Override
            public void onWeighDataChange(int net, int tare, int status) {
                if(weighPop!=null&&weighPop.isShowing()) {
                    updataWeighPup(null,net,tare,status);
                }

            }
        });
    }

    private void initBleWeigher() {
        if(((MainActivity)mActivity).weigher==null) {
            ll_weigher_state.setVisibility(View.GONE);
            return;
        }
        ll_weigher_state.setVisibility(View.VISIBLE);
        int weighState = WeighManager.getInstance().getWeighState();
        weigher_connected=weighState==2;
        WeighManager.getInstance().initState(mActivity,ll_weigher_state,ivWeigherState,weighState);
        if(Constant.SAVED_WEIGH_NAME.contains(Constant.WEIGHER_NAME)) {
            tv_current_weigh.setVisibility(View.VISIBLE);
            tv_current_weigh.setText("");
        }else {
            tv_current_weigh.setVisibility(View.GONE);
        }

        ((MainActivity)mActivity).setBleWeighListener(new BleWeighListener() {
            @Override
            public void onWeighDel() {
                ll_weigher_state.setVisibility(View.GONE);
            }
            @Override
            public void onWeighConnect() {
                if(Constant.SAVED_WEIGH_NAME==null) {
                    return;
                }
                if(Constant.SAVED_WEIGH_NAME.contains(Constant.WEIGHER_NAME)) {
                    tv_current_weigh.setVisibility(View.VISIBLE);
                }else {
                    tv_current_weigh.setVisibility(View.GONE);
                }
                weigher_connected=true;
                updateAdapter();
                WeighManager.getInstance().initState(mActivity,ll_weigher_state,ivWeigherState,2);
            }

            @Override
            public void onWeighDisconnect() {
                tv_current_weigh.setVisibility(View.GONE);
                weigher_connected=false;
                updateAdapter();
                WeighManager.getInstance().initState(mActivity,ll_weigher_state,ivWeigherState,0);
//                WeighManager.getInstance().connectWeigh(savedWeigher.getBlueToothAdd());
            }

            @Override
            public void onWeighConnecting() {
                tv_current_weigh.setVisibility(View.GONE);
                weigher_connected=false;
                WeighManager.getInstance().initState(mActivity,ll_weigher_state,ivWeigherState,1);
            }

            @Override
            public void onWeighDataChange(int version, double weighAcc, String weighMode, String zf, int status, String weighUnite) {
                weigh_version=version;
                if(weigh_version==1) {
                    tv_current_weigh.setVisibility(View.VISIBLE);
                    OrderPgerFragment.this.weigh_unit=weighUnite;
                    OrderPgerFragment.this.weigh_zf=zf;
                    OrderPgerFragment.this.weigh_mode=weighMode;
                    OrderPgerFragment.this.weigh_acc=weighAcc;
                    if(TextUtils.equals("g", weigh_unit)) {
                        tv_current_weigh.setText(Html.fromHtml("<u>"+weigh_zf + weigh_acc /1000+"kg"+"</u>"));
                    }else if (TextUtils.equals("kg", weigh_unit)){
                        tv_current_weigh.setText(Html.fromHtml("<u>"+weigh_zf + weigh_acc +"kg"+"</u>"));
                    }
                }else {
                    tv_current_weigh.setVisibility(View.GONE);
                    weigh_unit="kg";
                }

                upDateZeroDia(null);
                if(weighPop!=null&&weighPop.isShowing()) {
                    updataWeighPup(weighAcc+"",0,0,0);
                }

            }
        });
    }

    private boolean handleNewWeighData(String data) {
        if(data.contains("E 2")||data.contains("E 1")) {
            if(everShowWeighErr) {
                return true;
            }
            everShowWeighErr=true;
            NoticePopuUtils.showOfflinDia(mActivity, "电子秤异常，错误码："+data+"请联系乐推微解决！",null);
            return true;
        }

        try{
            weigh_unit = data.substring(14);
            //数量
            weigh_acc = Double.valueOf(data.substring(7,14));
            weigh_mode = data.substring(3, 5);
            //正负
            weigh_zf = data.substring(6, 7);
            if(TextUtils.equals("g", weigh_unit)) {
                tv_current_weigh.setText(Html.fromHtml("<u>"+weigh_zf + weigh_acc /1000+"kg"+"</u>"));
            }else if (TextUtils.equals("kg", weigh_unit)){
                tv_current_weigh.setText(Html.fromHtml("<u>"+weigh_zf + weigh_acc +"kg"+"</u>"));
            }
        }catch (Exception e){
            weigh_mode="";
            weigh_zf="";
            if(weighPop!=null&&weighPop.isShowing()) {
                tv_weigh.setText("------");
            }
            upDateZeroDia("------");
            return true;
        }
        return false;
    }

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if(action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
                int conectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
                if(conectState==0) {
                    setPayScannerState(false);
                }else if(conectState==1) {
                    setPayScannerState(false);
                }else if(conectState==2) {
                    setPayScannerState(true);
                }
            }else if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if(state==BluetoothAdapter.STATE_OFF) {
                    BluetoothAdapter.getDefaultAdapter().enable();
                    setPayScannerState(false);
                }
            }
        }
    };
    /**
     * 扫码枪
     * sdfsdfsdf
     */
    private void initBarScanner() {
        LogUtils.e(TAG,"小闪："+Constant.XS_IN);
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            BluetoothAdapter.getDefaultAdapter().enable();
        }
        if(savedScanner ==null) {
            if(!Constant.XS_IN) {//没有外接扫码枪
                ll_scanner_state.setVisibility(View.GONE);
                setPayScannerState(false);
                payScannerOff.setText(R.string.need_add_scanner);
            }else {//有外接小闪
                setPayScannerState(false);
            }
        }else {//有外接蓝牙设备
            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(savedScanner.getBlueToothAdd());
            boolean scannerConnected = HidConncetUtil.getScannerConnectState(device);
            LogUtils.saveLog(TAG,"蓝牙连接状态："+scannerConnected);
            setPayScannerState(scannerConnected);
            listenScannerState();
            everRegistScannerReceiver=true;
            ll_scanner_state.setVisibility(View.VISIBLE);
            if(!scannerConnected) {
                HidConncetUtil.connectScanner(mActivity, savedScanner.getBlueToothAdd());
            }
        }

    }
    private void listenScannerState() {
        ll_scanner_state.setVisibility(View.VISIBLE);
        HidConncetUtil.registBarScanReceiver(mActivity, receiver);
    }

    private void setPayScannerState(boolean scannerConnected) {
        LogUtils.e(TAG,"setPayScannerState（） state="+scannerConnected+"  小闪："+Constant.XS_IN);
        if(scannerConnected) {
            payScannerNormal.setVisibility(View.VISIBLE);
            payScannerOff.setVisibility(View.GONE);
            ll_scanner_state.setVisibility(View.VISIBLE);
            ll_scanner_state.setBackgroundResource(R.drawable.scanner_state_connect);
            iv_scanner_state.setBackgroundResource(R.drawable.connected);
            iv_scanner_state.clearAnimation();
            if((scanMemberPup!=null&&scanMemberPup.isShowing())||(scanMemberDia!=null&&scanMemberDia.isShowing())) {
                NoticePopuUtils.scanner_error.setVisibility(View.GONE);
            }
        }else {
            if(Constant.XS_IN) {
                payScannerNormal.setVisibility(View.VISIBLE);
                payScannerOff.setVisibility(View.GONE);
                if(savedScanner==null) {
                    ll_scanner_state.setVisibility(View.GONE);
                }else {
                    ll_scanner_state.setVisibility(View.VISIBLE);
                    ll_scanner_state.setBackgroundResource(R.drawable.scanner_state_disconnect);
                    iv_scanner_state.setBackgroundResource(R.drawable.dicconnected);
                }
                iv_scanner_state.clearAnimation();
                if((scanMemberPup!=null&&scanMemberPup.isShowing())||(scanMemberDia!=null&&scanMemberDia.isShowing())) {
                    NoticePopuUtils.scanner_error.setVisibility(View.GONE);
                }
            }else {//蓝牙未连接，小闪也未连接
                payScannerNormal.setVisibility(View.GONE);
                payScannerOff.setVisibility(View.VISIBLE);
                payScannerOff.setText(R.string.scanner_disconnect);
                if(savedScanner!=null) {
                    ll_scanner_state.setVisibility(View.VISIBLE);
                    ll_scanner_state.setBackgroundResource(R.drawable.scanner_state_disconnect);
                    iv_scanner_state.setBackgroundResource(R.drawable.dicconnected);
                    iv_scanner_state.clearAnimation();
                }
                if((scanMemberPup!=null&&scanMemberPup.isShowing())||(scanMemberDia!=null&&scanMemberDia.isShowing())) {
                    NoticePopuUtils.scanner_error.setVisibility(View.VISIBLE);
                    NoticePopuUtils.scanner_error_msg.setText(R.string.scanner_disconnect);
                }
            }
        }
    }
    private void inputDel() {
        String current = tvCashpayShishou.getText().toString();
        if ("00.00".equals(current) || "10.00".equals(current) || "20.00".equals(current) || "50.00".equals(current) || "100.00".equals(current) || "".equals(current)) {
            tvCashpayShishou.setText("");
            tvCashpayReturn.setText("00.00");
        } else {
            tvCashpayShishou.setText(current.substring(0, current.length() - 1));
        }
        current = tvCashpayShishou.getText().toString();
        if ("".equals(current)) {
            tvCashpayReturn.setText("00.00");
        } else {
            Double d_shi_shou = Double.valueOf(current);
            tvCashpayReturn.setText(Utils.dropZero(Utils.keepTwoDecimal(d_shi_shou - orderBean.getMsg().getPayment() + "")));
        }
    }
    //输入数字
    @OnClick({R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9,R.id.btn00})
    void inputNumber(View view) {
        String key = map.get(view);
        String current = tvCashpayShishou.getText().toString();
        if (current.contains(".") && (current.length() - 1 - current.indexOf(".")) >= 2 && !"00.00".equals(current) && !"10.00".equals(current) && !"20.00".equals(current) && !"50.00".equals(current) && !"100.00".equals(current)) {
            return;
        }
        if ("0".equals(current) || "00".equals(current) || "00.00".equals(current) || "10.00".equals(current) || "20.00".equals(current) || "50.00".equals(current) || "100.00".equals(current) || "".equals(current)) {//第一次输入数字的时候
            tvCashpayShishou.setText(key);
        } else {
            if (Double.valueOf(tvCashpayShishou.getText() + key) > 999999.99) {
                return;
            }
            tvCashpayShishou.setText(tvCashpayShishou.getText() + key);
        }
        current = tvCashpayShishou.getText().toString();
        Double d_shi_shou = Double.valueOf(current);
        if(orderBean!=null) {
            tvCashpayReturn.setText(Utils.dropZero(Utils.keepTwoDecimal(d_shi_shou - orderBean.getMsg().getPayment() + "")));
        }
    }

    public void payWayCheck(int checkedId){
        switch (checkedId) {
            case R.id.wx_pay:
                rl_wx_zhf.setVisibility(View.VISIBLE);
                ll_pay_card.setVisibility(View.GONE);
                ll_pay_cash.setVisibility(View.GONE);
                ll_pay_member.setVisibility(View.GONE);
                iv_find_code1.setText(R.string.wx_code1);
                iv_find_code2.setText(R.string.wx_code2);
                Drawable wx1=mActivity.getResources().getDrawable(R.drawable.find_wxcode1);
                Drawable wx2=mActivity.getResources().getDrawable(R.drawable.find_wxcode2);
                wx1.setBounds(0,0,wx1.getMinimumWidth(),wx1.getMinimumHeight());
                wx2.setBounds(0,0,wx2.getMinimumWidth(),wx2.getMinimumHeight());
                iv_find_code1.setCompoundDrawables(null,wx1,null,null);
                iv_find_code2.setCompoundDrawables(null,wx2,null,null);
                Constant.payway = "0";
                way = "微信支付";
                initPayWayState(Constant.OPEN_WXPAY);
                break;
            case R.id.zhf_pay:
                rl_wx_zhf.setVisibility(View.VISIBLE);
                ll_pay_card.setVisibility(View.GONE);
                ll_pay_cash.setVisibility(View.GONE);
                ll_pay_member.setVisibility(View.GONE);
                iv_find_code1.setText(R.string.zfb_code1);
                iv_find_code2.setText(R.string.zfb_code2);

                Drawable zfb1=mActivity.getResources().getDrawable(R.drawable.find_zfb_code1);
                Drawable zfb2=mActivity.getResources().getDrawable(R.drawable.find_zfb_code2);
                zfb1.setBounds(0,0,zfb1.getMinimumWidth(),zfb1.getMinimumHeight());
                zfb2.setBounds(0,0,zfb2.getMinimumWidth(),zfb2.getMinimumHeight());
                iv_find_code1.setCompoundDrawables(null,zfb1,null,null);
                iv_find_code2.setCompoundDrawables(null,zfb2,null,null);
                Constant.payway = "1";
                way = "支付宝支付";
                initPayWayState(Constant.OPEN_ALIPAY);
                break;
            case R.id.cash_pay:
                rl_wx_zhf.setVisibility(View.GONE);
                ll_pay_card.setVisibility(View.GONE);
                ll_pay_cash.setVisibility(View.VISIBLE);
                ll_pay_member.setVisibility(View.GONE);
                Constant.payway = "3";
                way = "现金收款(记账)";
                initPayWayState(1);
                break;
            case R.id.card_pay:
                rl_wx_zhf.setVisibility(View.GONE);
                ll_pay_card.setVisibility(View.VISIBLE);
                ll_pay_cash.setVisibility(View.GONE);
                ll_pay_member.setVisibility(View.GONE);
                Constant.payway = "2";
                way = "刷卡记账";
                initPayWayState(Constant.OPEN_YL);

                break;
            case R.id.member_pay:
                rl_wx_zhf.setVisibility(View.GONE);
                ll_pay_card.setVisibility(View.GONE);
                ll_pay_cash.setVisibility(View.GONE);
                ll_pay_member.setVisibility(View.VISIBLE);
                Constant.payway = "4";
                way = "会员卡余额支付";
                initPayWayState(1);
                break;
        }
    }

    private void initPayWayState(int whichWay) {
        if(!NetworkUtils.isAvailable(mActivity)) {//网络不可用
            if(TextUtils.equals(Constant.payway,"3")) {//现金可用，其他不可用
                payWayError.setVisibility(View.GONE);
            }else {
                payWayError.setVisibility(View.VISIBLE);
                tvPaywayErrorNotice.setText(R.string.net_error);
            }
        }else {//网络可用，再判断支付通道是否可用
            if(whichWay==0) {//不可用
                payWayError.setVisibility(View.VISIBLE);
            }else {
                payWayError.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 取消订单
     */
    void uncommitedOrderCancel() {
        ((MainActivity)mActivity).startFpLb();
        NoticePopuUtils.showBindPup(mActivity, "您确定要取消订单吗？", R.id.order_pager_layout, new NoticePopuUtils.OnClickCallBack() {
            @Override
            public void onClickYes() {
                hideRemark();
                afterCancelOrder();
            }
            @Override
            public void onClickNo() {
            }
        });
    }
    /**
     *
     */
    private void afterCancelOrder() {
        order_right_pay.setVisibility(View.GONE);
        tvCashpayShishou.setText("00.00");
        tvCashpayReturn.setText("00.00");
        showMemberPrice=false;
        branachOrder=false;
        primaryKeyId = "0";
        clearLocalData(true,mainIndex==0);
        refreshGoods(-1);
        upDateUnPayList();
    }

    private void commitOrderCancel() {
        ((MainActivity)mActivity).startFpLb();
        NoticePopuUtils.showBindPup(mActivity, "您确定要取消订单吗？", R.id.order_pager_layout, new NoticePopuUtils.OnClickCallBack() {
            @Override
            public void onClickYes() {
                Map<String, String> params = netWorks.getPublicParams();
                params.put(Constant.PARAMS_ADV_ID,Constant.ADV_ID);
                params.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID);
                params.put("roomid", "0");
                if(orderBean!=null) {
                    params.put("id", "" + orderBean.getMsg().getOrderid());
                }else {
                    Utils.showToast(mActivity,"取消失败，订单状态异常!");
                    return;
                }
                params.put("exist_goods", "1");
                netWorks.Request(UrlConstance.ROOM_ORDER_CANCEL, true, "订单取消中...", params, 5000, 0, new NetWorks.OnNetCallBack() {
                    @Override
                    public void onError(Exception e, int flag) {
                        Utils.showToast(mActivity, "网络异常，取消订单失败");

                    }

                    @Override
                    public void onResonse(String response, int flag) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                Utils.showToast(mActivity, "订单已取消");
                                afterCancelOrder();
                            } else {
                                Utils.showToast(mActivity, "网络异常，取消订单失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void onClickNo() {
            }
        });
    }

    /**
     * 1.取消订单 2.完成订单 3.提交订单
     */
    private void upDateUnPayList() {
        if(mainIndex==1) {
            fl_order_list.setVisibility(View.VISIBLE);
            unPayOrderListFragment.loadData("",false,false);
            unPayOrderListFragment.cancelSearch();
        }
    }

    public void showMemberTags() {
        String[] tags = memberBean.getMsg().getTags();
        if (tags != null && tags.length > 0) {
            NoticePopuUtils.showMemberTagsPop(mActivity, totalView, tags);
        } else {
            Utils.showToast(mActivity, "该会员暂无标签", 2000);
        }
    }

    /**
     * 现金，点，del，清除的监听
     * @param view    @OnClick(R.id.commited_order_print)

     */
    @OnClick({R.id.ll_reconnect,R.id.tv_update_goods,R.id.commited_order_print,R.id.btn_spot, R.id.btn_del, R.id.btn_clear,R.id.btn_ten,R.id.btn_twenty,R.id.btn_fifty,R.id.btn_hundred,R.id.cash_pay_sure,R.id.card_pay_sure,R.id.tv_member_pay,R.id.change_price,R.id.btn_uncommit_cancel,
            R.id.ll_weigher_state,R.id.tv_inditify_member,R.id.rl_pay_inditifymember,R.id.tv_show_membertags,R.id.commited_order_cancel,R.id.commited_order_add,R.id.change_member,R.id.tv_cancel_search,R.id.commited_order_up,R.id.net_error_refresh,R.id.rl_order_remark})
    void onClick(View view){
        switch (view.getId()) {
            case R.id.cash_pay_sure://现金刷卡支付
            case R.id.card_pay_sure:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                cashCardPay();
                break;
            case R.id.tv_member_pay:
                if(NetworkUtils.isAvailable(mActivity)) {
                    startMemberPay();
                }
                break;
            case R.id.tv_inditify_member://识别会员
            case R.id.change_member:
            case R.id.rl_pay_inditifymember:
                scanMemberPup = NoticePopuUtils.showInditifyMemberPup(mActivity, R.id.order_pager_layout,savedScanner, NoticePopuUtils.MEMBER_CODE,new NoticePopuUtils.OnFinishpswCallBack() {
                    @Override
                    public void onFinishInput(String msg) {
                        LogUtils.Log("输入会员卡号的回调：" + msg);
                        ((MainActivity)mActivity).startFpLb();
                        getMemberInfo(msg);
                    }
                });
                break;
            case R.id.tv_show_membertags://展示会员标签
                showMemberTags();
                break;
            case R.id.btn_uncommit_cancel://返回
                if(mainIndex==0){
                    if(TextUtils.equals("0",primaryKeyId)) {
                        uncommitedOrderCancel();
                        return;
                    }
                    reInitData(false);
                    getUnpayOrderDetails(false);
                    changeOrderRight(true);
                }else {
                    fl_order_list.setVisibility(View.VISIBLE);
                    getUnpayOrderDetails(false);
                    changeUnpayDetailFoot();
                    changeOrderRight(unpayOrderPayLayoutVisiable);
                    comitted_footer.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.commited_order_cancel://取消订单
                commitOrderCancel();
                break;
            case R.id.commited_order_add://添加商品
                ((MainActivity)mActivity).startFpLb();
                if(mainIndex==1) {
                    fl_order_list.setVisibility(View.GONE);
                    unpayOrderPayLayoutVisiable = order_right_pay.getVisibility()== View.VISIBLE;
                }
                clearLocalData(false,true);
                refreshGoods(-1);

                changeOrderRight(false);
                uncommit_footer.setVisibility(View.VISIBLE);
                rg_payways.check(R.id.wx_pay);
                btn_uncommit_cancel.setVisibility(View.VISIBLE);
                btn_uncommit_cancel.setText("返回");
                break;
            case R.id.btn_spot:
                String current = tvCashpayShishou.getText().toString();
                if (current.contains(".")) {
                    return;
                } else if ("00.00".equals(current) || "10.00".equals(current) || "20.00".equals(current) || "50.00".equals(current) || "100.00".equals(current) || "".equals(current)) {
                    tvCashpayShishou.setText("0.");
                } else {
                    tvCashpayShishou.setText(current + ".");
                }
                current = tvCashpayShishou.getText().toString();
                Double d_shi_shou = Double.valueOf(current);
                tvCashpayReturn.setText(Utils.dropZero(Utils.keepTwoDecimal(d_shi_shou - orderBean.getMsg().getPayment() + "")));
                break;
            case R.id.btn_del:
                inputDel();
                break;
            case R.id.btn_clear:
                tvCashpayShishou.setText("00.00");
                tvCashpayReturn.setText("00.00");
                break;
            case R.id.btn_ten:
                tvCashpayShishou.setText("10.00");
                tvCashpayReturn.setText(10 - orderBean.getMsg().getPayment() + "");
                break;
            case R.id.btn_twenty:
                tvCashpayShishou.setText("20.00");
                tvCashpayReturn.setText(20 - orderBean.getMsg().getPayment() + "");
                break;
            case R.id.btn_fifty:
                tvCashpayShishou.setText("50.00");
                tvCashpayReturn.setText(50 - orderBean.getMsg().getPayment() + "");
                break;
            case R.id.btn_hundred:
                tvCashpayShishou.setText("100.00");
                tvCashpayReturn.setText(100 - orderBean.getMsg().getPayment() + "");
                break;
            case R.id.change_price://改价
                if (Utils.isNetworkAvailable(mActivity)) {
                    changePriceDialog = NoticePopuUtils.changeMoney(mActivity, tvShouldPay.getText().toString(), new NoticePopuUtils.OnFinishChangePrice() {
                        @Override
                        public void onFinishInput(String afterGj,String gjRemark) {
                         changePriceDone(afterGj,gjRemark);
                        }
                    });
                } else {
                    Utils.showToast(mActivity, "网络异常，请检查网络后再试");
                }
                break;
            case R.id.tv_cancel_search:
                et_search.clearFocus();
                break;
            case R.id.commited_order_up:
                String btnStr = commited_order_up.getText().toString();
                LogUtils.Log("btnStr=="+btnStr);
                if(TextUtils.equals("挂单", btnStr)) {//挂单
                    hideRemark();
                    ((MainActivity)mActivity).startFpLb();
                    Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.guadan_animate);
                    animation.setInterpolator(new LinearInterpolator());
                    order_left.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if(mainIndex==1) {
                                upDateUnPayList();
                                whetherCanNotifyMember(false);
                            }else {
                                primaryKeyId="0";
                                branachOrder=false;
                                clearLocalData(true,true);
                                refreshGoods(-1);
                            }
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }else {//结算
                    changeOrderRight(true);
                    handler.removeMessages(Constant.UNPAYITEM_TO_UNPAYDETAIL);
                }
                break;
            case R.id.commited_order_print:
                initCurrentUnPayTotalData();
                 break;
            case R.id.net_error_refresh:
                getGsInfo();
                break;
            case R.id.rl_order_remark:
                NoticePopuUtils.showRemarkInPutDia(mActivity, "修改备注",tvOrderRemark.getText().toString(), true, new NoticePopuUtils.OnFinishInput() {
                    @Override
                    public void onFinishInput(String msg) {
                        unDateRemark(msg);
                    }

                    @Override
                    public void onCancelInput() {

                    }
                });
                break;
            case R.id.ll_weigher_state:
                if(tv_current_weigh.getVisibility()==View.VISIBLE) {
                    onClickWeighTitle();
                }
                break;
            case R.id.tv_update_goods:
                getGsInfo();
                break;
            case R.id.ll_reconnect:
                if(WeighManager.getInstance().getWeighState()==0) {
                    ((MainActivity)mActivity).connectWeigher();
                    WeighManager.getInstance().initState(mActivity,ll_weigher_state,ivWeigherState,1);
                }

                break;
        }
    }
    /**
     * 未支付订单的总数据
     */
    private void initCurrentUnPayTotalData() {
        PrintUtils.getInstance().formatUnPayPrintData(tempOrder,orderBean, unPayCustomerPrintData,false,true);
        PrintUtils.getInstance().formatUnPayPrintData(tempOrder,orderBean, unPayBTCustomerPrintData,true,true);
        PrintUtils.getInstance().formatUnPayPrintMerChantData(mActivity,tempOrder,orderBean,unpayMerchantData,false,true);
        PrintUtils.getInstance().formatUnPayPrintMerChantData(mActivity,tempOrder,orderBean,unpayBTMerchantData,true,true);
        oneByOneYunList = YunPrintUtils.formateSplitOrderDetailData(unPayBTCustomerPrintData, orderBean,tempOrder,true);
        unpayOrderPrintTransition(true);

    }

    /**
     * 提交订单备注
     * @param msg
     */
    private void unDateRemark(final String msg) {
        Map<String, String> publicParams = netWorks.getPublicParams();
        publicParams.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID);
        publicParams.put(Constant.PARAMS_ADV_ID,Constant.ADV_ID);
        publicParams.put("order_no",orderBean.getMsg().getOrder_no());
        publicParams.put("msginfo",msg);
        publicParams.put("from","1");
        netWorks.Request(UrlConstance.UPDATE_ORDER_MARK, true, "正在提交备注...", publicParams, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                upDateMarkFailure("网络异常，备注提交失败！",msg);

            }
            @Override
            public void onResonse(String response, int flag) {
                LogUtils.e(TAG,"提交订单备注："+response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200) {
//                        orderBean.getMsg().setRemark(msg);
//                        tvOrderRemark.setText(msg);
                        //备注信息的打印数据
                        getUnpayOrderDetails(false);
                    }else {
                        String notice = jsonObject.getString("err");
                        Utils.showToast(mActivity,notice,1000);
                        upDateMarkFailure(notice,msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 提交失败
     * @param msg
     */
    private void upDateMarkFailure(String errorMsg,final String msg) {

        Utils.dismissWaittingDialog();
        NoticePopuUtils.refundErrorDia(mActivity, errorMsg+",稍后您可以到已结中,为本订单添加备注！",
                "再试一次", "稍后添加", "备注内容保存失败！", new NoticePopuUtils.OnClickCallBack() {
                    @Override
                    public void onClickYes() {
                        //稍后添加
                        LogUtils.Log("稍后添加");
                    }

                    @Override
                    public void onClickNo() {
                        //再试一次
                        LogUtils.Log("再试一次");
                        unDateRemark(msg);
                    }
                });
    }

    private void changePriceDone(final String afterGj, final String gjRemark) {
        Map<String, String> params = netWorks.getPublicParams();
        params.put("order_id", "" + orderBean.getMsg().getOrderid());
        params.put("price", afterGj);
        params.put("remark", gjRemark);
        netWorks.Request(UrlConstance.MODIFY_ORDER_PRICE,true,"正在修改价格...", params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.showToast(mActivity, "改价同步服务器失败，请检查网络后再试！");
            }
            @Override
            public void onResonse(String response, int flag) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        tvShouldPay.setText( Utils.keepTwoDecimal(afterGj));
//                        orderBean.getMsg().setRemark(gjRemark);
                        orderBean.getMsg().setPayment(Double.valueOf(afterGj));
                        payment=afterGj;
                        if(changePriceDialog!=null&&changePriceDialog.isShowing()) {
                            changePriceDialog.dismiss();
                        }
                        showPaymentInFp();
                        initCardPayState();
                    }else {
                        Utils.showToast(mActivity, "改价失败，请重试！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void parsePayResultJson(String json) {
        discountInfo = null;
        if (!Constant.UNKNOWN_COMMODITY.equals("2")) {
            try{
                JSONObject payResponse=new JSONObject(json);
                query_num = payResponse.getString("query_num");
                transaction_id = payResponse.getString("transaction_id");
                String ticketInfo = payResponse.getString("ticketInfo");
                if (!TextUtils.isEmpty(ticketInfo)) {
                    Gson gson = new Gson();
                    discountInfo = gson.fromJson(ticketInfo, DiscountInfo.class);
                }
            }catch (Exception e){
            }

        }
    }

    /**
     * 支付成功
     */
    private void paySuccess(String showTradeNum) {
        LogUtils.saveLog(TAG,"paySuccess()");
        if(TextUtils.equals("3",Constant.payway)) {//打开钱箱
            try {
                iWoyouService.openDrawer(new ICallback.Stub() {
                    @Override
                    public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {
                        LogUtils.e(TAG,"isSuccess="+isSuccess+" code="+code+ " msg="+msg);
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


        tvCashpayShishou.setText("00.00");
        tvCashpayReturn.setText("00.00");
        branachOrder=false;
        primaryKeyId = "0";
        order_right_pay.setVisibility(View.GONE);
        order_right.setVisibility(View.VISIBLE);
        showMemberPrice=false;
//        SelfToast.showToast(mActivity, "收款成功", 2000, true);
        tradetime = Utils.getTradeTime();
        printJZTime = Utils.getCurrentM(Long.valueOf(tradetime) * 1000);
        PayCardinfoBean cardinfoPayBean = null;
        if (cardinfo_json != null && !"".equals(cardinfo_json)) {
            cardinfoPayBean = new Gson().fromJson(cardinfo_json, PayCardinfoBean.class);
        }
        String vip_discount = "";
        if (cardinfoPayBean != null) {
            if ("CASH".equals(cardinfoPayBean.getCardtype())) {
                vip_discount = "优惠券：" + cardinfoPayBean.getInfo();
            } else if ("DISCOUNT".equals(cardinfoPayBean.getCardtype())) {
                vip_discount = "优惠券:" + cardinfoPayBean.getInfo();
            }
        } else {
            if (payment != null && !"0".equals(payment) && total_fee != null && !"0".equals(total_fee)) {
                double dis = Double.valueOf(payment) / Double.valueOf(total_fee);
                if (dis > 0 && dis < 1) {
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");//如果小数不足2位,会以0补足.
                    vip_discount = "会员折扣：" + decimalFormat.format(dis * 10) + "折";
                }
            }
        }

        initPrinterData();

        NoticePopuUtils.showPayRsesultPop(mActivity, totalView,payment, way, trade_num,
                printJZTime,orderBean.getMsg().getRemark(), new NoticePopuUtils.OnFinishChoose() {
            @Override
            public void onClickYes() {
                hideRemark();
                ((MainActivity)mActivity).startFpLb();
                clearLocalData(true,mainIndex==0);
                refreshGoods(-1);
                upDateUnPayList();
            }
        });
    }

    /**
     * 格式化已结账订单打印数据
     */
    private void initPrinterData() {
        payedPrintData = new ArrayList<>();
        memberData = new ArrayList<>();

        payedBtPrintData=new ArrayList<>();
        memberBtData=new ArrayList<>();
        
        if(orderBean==null) {
            Utils.showToast(mActivity,"数据异常，无法打印!请到“已结”打印小票",2000);
            return;
        }

        //2.不同部分
        switch (Constant.UNKNOWN_COMMODITY) {
            case "0"://下单支付'
                PrintUtils.getInstance().initFOPrintData(orderBean, discountInfo, payedPrintData, memberData, printJZTime, trade_num,
                        way, transaction_id, memberFreeMoney, false);

                PrintUtils.getInstance().initFOPrintData(orderBean,discountInfo,payedBtPrintData,memberBtData,printJZTime,trade_num,
                        way,transaction_id,memberFreeMoney,true);
                break;
        }
        LogUtils.e(TAG,"localPermission="+localPrinterPermission);
        PrintUtils.getInstance().startLoclPrint(mActivity, iWoyouService, localPrinterPermission, payedPrintData, memberData, new PrintSecond() {
            @Override
            public void doPrintSecond() {
                PrintUtils.getInstance().printSecond(iWoyouService,payedPrintData);
            }
        });

        if(btPrinter!=null) {
            if(btPrinterPermission.contains("4")||btPrinterPermission.contains("5")) {
                if(mService.getState()==BluetoothService.STATE_CONNECTED) {
                    printPayedByBluetooth();
                }else {
                    payed=true;
                    BTPrintUtils.getInstance().connectBtPrinterTest(mService,mActivity,handler);
                }
            }
        }

        //云打印数据格式化
        YunPrintUtils.formateDirPay(mActivity, payedBtPrintData, memberBtData);

    }

    private void printPayedByBluetooth() {
        if(btPrinterPermission.contains("5")) {
            if(btPrinterPermission.contains("4")) {
                startBtPrint(true, new BluetoothService.BtPrintFinshCallBack() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {
                        startBtPrint(false,null);
                    }
                });
            } else {
                startBtPrint(true,null);
            }
        }else {
            if(btPrinterPermission.contains("4")) {
                startBtPrint(false,null);
            }
        }
    }
    public void startBtPrint( boolean forCustomer, BluetoothService.BtPrintFinshCallBack callBack) {
        if(!forCustomer) {
            payedBtPrintData.remove(1);
        }
        BTPrintUtils.getInstance().csDetailBtPrint(mService, payedBtPrintData, forCustomer, memberBtData,
                mActivity, callBack);
    }

    /**
     * 支付失败
     */
    private void payFail(String reason,String solution) {
        LogUtils.saveLog(TAG,"payFail()");
        tvCashpayShishou.setText("00.00");
        tvCashpayReturn.setText("00.00");
        printJZTime = Utils.getCurrentM(Long.valueOf(Utils.getTradeTime()) * 1000);
        NoticePopuUtils.showPayFailPop(mActivity, totalView,reason,solution, way,trade_num, printJZTime,"暂无", null);

    }

    private List<String> payedPrintData;
    private List<String> payedBtPrintData;
    private List<String> memberData;
    private List<String> memberBtData;

    /**
     * 支付
     */
    private void toPay(String barcode) {

        Constant.UNKNOWN_COMMODITY="0";
        String total_fee = orderBean.getMsg().getTotal_fee() + "";
        String vipcard_id = memberBean == null ? "0" : memberBean.getMsg().getVipcardid();
        String vipcreate_id = memberBean == null ? "0" : memberBean.getMsg().getVipcreate_id();
        String orderRoom=null;
        if (orderBean.getMsg().getRoom() != null) {
            orderRoom = new Gson().toJson(orderBean.getMsg().getRoom());
        }
        trade_num = Utils.getTradeNum();
        PayUtils.toPay(mActivity, totalView, trade_num,payment, total_fee, barcode, primaryKeyId, vipcard_id, vipcreate_id, null,
                vip_discount, cardinfo_json, orderRoom, 1,new OnPayFinish() {
                    @Override
                    public void onPaySuccess(String payResponse, String transtion_id, String payment) {
                        LogUtils.saveLog(TAG,"onPaySuccess()");
                        if(!judgeOrderFinished(payResponse)) {
                            parsePayResultJson(payResponse);
                            OrderPgerFragment.this.paySuccess(transtion_id);
                        }
                    }

                    @Override
                    public void onPayFailure(String response,String reason,String solution) {
                        OrderPgerFragment.this.payFail(reason,solution);
                    }
                });
    }

    private boolean judgeOrderFinished(String json) {
        //如果能拿到orderid,代表改订单已结账，不在往下进行
        LogUtils.saveLog(TAG,"judgeOrderFinished() json=="+json);
        try{
            JSONObject jsonObject=new JSONObject(json);
            String orderId = jsonObject.getString("orderId");
            if(orderId!=null&&!TextUtils.isEmpty(orderId)) {
                NoticePopuUtils.showPayedDia(mActivity, "已通过其他终端完成收款",R.id.order_pager_layout, new NoticePopuUtils.OnFinishChoose() {
                    @Override
                    public void onClickYes() {
                        tvCashpayShishou.setText("00.00");
                        tvCashpayReturn.setText("00.00");
                        branachOrder=false;
                        primaryKeyId = "0";
//                        order_right_pay.setVisibility(View.GONE);
//                        order_right.setVisibility(View.VISIBLE);
                        showMemberPrice=false;

                        ((MainActivity)mActivity).startFpLb();
                        clearLocalData(true,mainIndex==0);
                        refreshGoods(-1);
                        upDateUnPayList();
                    }
                });
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    /**
     * 会员余额支付
     */
    private void startMemberPay() {
        Constant.UNKNOWN_COMMODITY="0";
        Constant.payway="4";

        trade_time = Utils.getTradeTime();//秒
        String vipcard_id=null;
        String vipcreate_id=null;
        if (memberBean != null && memberBean.getMsg() != null) {
            vipcard_id = memberBean.getMsg().getVipcardid();
            vipcreate_id = memberBean.getMsg().getVipcreate_id();
        }
        String orderRoom=null;
        if (orderBean != null) {
            if (orderBean.getMsg() != null) {
                if (orderBean.getMsg().getRoom() != null) {
                    orderRoom=new Gson().toJson(orderBean.getMsg().getRoom());
                }
            }
        }
        trade_num = Utils.getTradeNum();
        PayUtils.toPay(mActivity, totalView, trade_num,payment, total_fee, null, primaryKeyId, vipcard_id, vipcreate_id, null,
                null, cardinfo_json, orderRoom, 1,new OnPayFinish() {
                    @Override
                    public void onPaySuccess(String payResponse, String transtion_id, String payment) {
                        if(!judgeOrderFinished(payResponse)) {
                            parsePayResultJson(payResponse);
                            OrderPgerFragment.this.paySuccess(trade_num);
                        }
                    }

                    @Override
                    public void onPayFailure(String response,String reason,String solution) {
                        OrderPgerFragment.this.payFail(reason,solution);
                    }
                });
    }


    /**
     * 现金或者刷卡支付
     */
    private void cashCardPay() {
        if(orderBean==null) {
            Utils.showToast(mActivity,"无法操作，订单为空！");
            return;
        }
        Constant.UNKNOWN_COMMODITY="0";
        payment = tvShouldPay.getText().toString();
        trade_num = Utils.getTradeNum();
        //query_num = Utils.getBQueryNum(this);
        trade_time = Utils.getTradeTime();//秒
        if (orderBean.getMsg() != null) {
            if (orderBean.getMsg().getRoom() != null && !"".equals(orderBean.getMsg().getRoom().getEnd_time())) {
                trade_time = orderBean.getMsg().getRoom().getEnd_time();
            }
        }


        SavedFailOrder cardOrder;
        if (memberBean != null) {
            String vipcardid = memberBean.getMsg().getVipcardid();
            cardOrder = PayTranstions.getSaveOrder(String.valueOf(orderBean.getMsg().getOrder_no()), trade_num,
                    String.valueOf(orderBean.getMsg().getOrderid()), order_total_fee, query_num, trade_time,
                    "1", "0", Utils.keepTwoDecimal(payment), vipcardid, "0", "0", vipcreate_id, "0", cardinfo_json);//type="0" 代表订单收款
        } else {
            cardOrder = PayTranstions.getSaveOrder(String.valueOf(orderBean.getMsg().getOrder_no()), trade_num,
                    String.valueOf(orderBean.getMsg().getOrderid()), order_total_fee, query_num, trade_time,
                    "1", "0", Utils.keepTwoDecimal(payment), "0", "0", "0", vipcreate_id, "0", cardinfo_json);//type="0" 代表订单收款
        }
        CashCardPayUtils.getInstance().cashCardPay(mActivity, cardOrder, null, orderBean, new OnPayFinish() {
            @Override
            public void onPaySuccess(String response,String transitonId,String payment) {
                LogUtils.e(TAG,"onPaySuccess() response=="+response);
                if(!judgeOrderFinished(response)) {
                    parsePayResultJson(response);
                    paySuccess(trade_num);
                }
            }

            @Override
            public void onPayFailure(String response,String reason,String solution) {
                LogUtils.e(TAG,"onPayFailure() response=="+response);
                payFail(reason,solution);
            }
        });
    }

    @Override
    public void initData(int index) {
        LogUtils.e("temp","下单页面 initData() index=="+index);
        netWorks = new NetWorks(mActivity);
        mainIndex=index;
        if(btPrinter==null){
            btPrinter = (SavedPrinter) getObject(mActivity, Constant.BT_PRINTER);
        }
        if(btPrinter!=null) {
            btPrinterPermission = btPrinter.getPrintPermission();
        }
        if(loaclPrinter==null) {
            loaclPrinter = (SavedPrinter) getObject(mActivity, Constant.LOCAL_PRINTER);
        }
        if(loaclPrinter!=null) {
            localPrinterPermission = loaclPrinter.getPrintPermission();
        }
        if(mService==null){
            mService= BluetoothService.getInstance();
            mService.setHandler(handler);
        }


        initState();
        clearLocalData(true,mainIndex==0);
        if(index==0) {
            showMemberPrice=false;
            primaryKeyId="0";
            branachOrder=false;
            fl_order_list.setVisibility(View.GONE);
            hideRemark();
            boolean firstUse=SpUtils.getInstance(mActivity).getBoolean(Constant.FIRST_USE,true);
            if(firstUse) {
                getGsInfo();
            }else {
                refreshGoods(-1);
            }
        }else if(index==1) {
            branachOrder=true;
            fl_order_list.setVisibility(View.VISIBLE);
            hideRemark();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            unPayOrderListFragment = new UnPayOrderListFragment();
            unPayOrderListFragment.setHandler(handler);
            if(!unPayOrderListFragment.isAdded()) {
                ft.add(R.id.fl_order_list, unPayOrderListFragment);
            }
            ft.commit();
        }
    }

    private void hideRemark() {
        tvOrderRemark.setText("");
        rlOrderRemark.setVisibility(View.GONE);
    }

    /**
     * 获取商品信息,1.初始化进来 2.添加商品 3.取消订单 4.完成结账
     * 已选择列表数据来源更新为orderbean
     */
    private void getGsInfo() {
        Map<String, String> params = netWorks.getPublicParams();
        params.put("storeid", Constant.STORE_ID);
        params.put("advid", Constant.ADV_ID);
        params.put("versionCode", "42");
        params.put("needVipPrice", "1");
        netWorks.Request(UrlConstance.GOODS_INFO,true,"正在获取商品信息...", params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                netError.setVisibility(View.VISIBLE);
                LogUtils.Log("请求商品error:" + e.toString());
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.e(TAG,"获取商品："+response);
                netError.setVisibility(View.GONE);
                parseJson(response);
            }
        });
    }

    private void setPayWayListener() {
        rg_payways.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                payWayCheck(checkedId);
            }
        });
    }

    private void parseJson(String response) {
        Gson gson = new Gson();
        AllGoodsInfoBean gotAllGoodsInfoBean;
        gotAllGoodsInfoBean = gson.fromJson(response, AllGoodsInfoBean.class);
        if(gotAllGoodsInfoBean!=null||gotAllGoodsInfoBean.getMsg()!=null&&gotAllGoodsInfoBean.getMsg().getGoodsInfo()!=null&&gotAllGoodsInfoBean.getMsg().getGoodsInfo().size()>0) {
            MyApplication application=(MyApplication)mActivity.getApplication();
            DaoSession daoSession = application.getDaoSession();
            GoodBeanDao goodBeanDao = daoSession.getGoodBeanDao();
            GoodsTypeInfoDao typeInfoDao = daoSession.getGoodsTypeInfoDao();
            try{
                typeInfoDao.deleteAll();
                goodBeanDao.deleteAll();
            }catch (Exception e){
                LogUtils.e(TAG,"删除表数据异常");
            }
            List<GoodsTypeInfo> typeList=new ArrayList<>();
            for (int i = 0; i < gotAllGoodsInfoBean.getMsg().getGoodsInfo().size(); i++) {
                AllGoodsInfoBean.MsgBean.GoodsInfoBean goodsInfoBean = gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(i);
                try{
                    GoodsTypeInfo goodsTypeInfo=new GoodsTypeInfo(null, goodsInfoBean.getTypeId(),goodsInfoBean.getTypeName());
                    typeList.add(goodsTypeInfo);
                    for (int j = 0; j <goodsInfoBean.getGoodsList().size(); j++) {
                        goodsInfoBean.getGoodsList().get(j).setTypeId(goodsInfoBean.getTypeId());
                    }
                    typeInfoDao.insertOrReplaceInTx(typeList);
                    goodBeanDao.insertOrReplaceInTx(goodsInfoBean.getGoodsList());
                    SpUtils.getInstance(mActivity).save(Constant.FIRST_USE,false);
                }catch (Exception e){
                    LogUtils.e(TAG,"写入数据库异常  e="+e.toString());
                }
            }
            goodBeanList=null;
            if(typeList!=null&&typeList.size()>0) {
                typeList.get(0).setChecked(true);
            }else {
                Utils.showToast(mActivity,"请至后台添加商品");
                return;
            }
            setAdapter(gotAllGoodsInfoBean.getMsg().getGoodsInfo().get(0).getGoodsList(),typeList,true);
        }
    }
    private void refreshGoods(int typeId){
        MyApplication application=(MyApplication)mActivity.getApplication();
        DaoSession daoSession = application.getDaoSession();
        GoodBeanDao goodBeanDao = daoSession.getGoodBeanDao();
        GoodsTypeInfoDao typeInfoDao = daoSession.getGoodsTypeInfoDao();

        List<GoodsTypeInfo> typeList = typeInfoDao.loadAll();
        boolean moveTop=false;
        if(typeList!=null&&typeList.size()>0) {
            List<GoodBean> goodsData;
            if(typeId>0) {//切换类目
                goodsData = goodBeanDao.queryBuilder()
                        .where(GoodBeanDao.Properties.TypeId.eq(typeId))
                        .orderAsc(GoodBeanDao.Properties.Id)
                        .list();
                for (int i = 0; i < typeList.size(); i++) {
                    if(typeList.get(i).getTypeId()==typeId) {
                        typeList.get(i).setChecked(true);
                    }else {
                        typeList.get(i).setChecked(false);
                    }
                }
            }else {//初始化
                goodsData=goodBeanDao.queryBuilder()
                        .where(GoodBeanDao.Properties.TypeId.eq(typeList.get(0).getTypeId()))
                        .orderAsc(GoodBeanDao.Properties.Id)
                        .list();
                for (int i = 0; i < typeList.size(); i++) {
                    typeList.get(i).setChecked(false);
                }
                typeList.get(0).setChecked(true);
                moveTop=true;
            }
            LogUtils.e(TAG,"分类数据："+typeList.toString());
            setAdapter(goodsData,typeList,moveTop);
        }
    }

    private void setAdapter(List<GoodBean> leftData,List<GoodsTypeInfo> typeInfoList,boolean moveTop) {
        //            SearchUtils.getInstance().parseData(gotAllGoodsInfoBean);
        if(right_adapter==null) {
            right_adapter = new NewOrderRightAdapter(mActivity);
            right_adapter.setData(typeInfoList);
            right_adapter.setKindsOnClickListener(goodKindsOnClickListener);
            lv_right.setAdapter(right_adapter);

        }else {
            right_adapter.setData(typeInfoList);
            right_adapter.notifyDataSetChanged();
            if(moveTop) {
                lv_right.setSelection(0);
            }
        }
        //左侧具体商品

        if(leftData!=null&&leftData.size()>0) {
            tv_nogs.setVisibility(View.GONE);
        }else {
            tv_nogs.setVisibility(View.VISIBLE);
        }
        if(gs_detail_adapter==null) {
            gs_detail_adapter = new GoodServiceAdapter(mActivity, handler);
            gs_detail_adapter.setWeighListener(onWeighGoodChangeListener);
            gs_detail_adapter.setData(leftData);
            gs_detail_adapter.setShowMemberPrice(showMemberPrice);
            gs_detail_adapter.setWigherState(weigher_connected);
            gv_left.setAdapter(gs_detail_adapter);
        }else {
            gs_detail_adapter.setData(leftData);
            gs_detail_adapter.setShowMemberPrice(showMemberPrice);
            gs_detail_adapter.setWigherState(weigher_connected);
            gs_detail_adapter.notifyDataSetChanged();
        }
        //已选商品
        if (Constant.localOrderBean.getGoodList() != null && Constant.localOrderBean.getGoodList().size() > 0) {
            if(selectedAdapter==null) {
                selectedAdapter = new ItemContentAdapter(mActivity, handler);
                selectedAdapter.setDetailOnDelListener(detailOnDelListener);
                selectedAdapter.setData(Constant.localOrderBean);
                selectedAdapter.setShowMemberPrice(showMemberPrice);
                selectedAdapter.setOnWeighChangeListener(onWeighGoodChangeListener);
                lv_selected_gs.setAdapter(selectedAdapter);
            }else {
                selectedAdapter.setData(Constant.localOrderBean);
                selectedAdapter.setShowMemberPrice(showMemberPrice);
                selectedAdapter.setOnWeighChangeListener(onWeighGoodChangeListener);
                selectedAdapter.notifyDataSetChanged();
            }

        }
        //搜索的adapter
        if(filterAdapter==null) {
            filterAdapter = new GoodServiceAdapter(mActivity,handler);
            filterAdapter.setWigherState(weigher_connected);
            filterAdapter.setShowMemberPrice(showMemberPrice);
            filterAdapter.setWeighListener(onWeighGoodChangeListener);
        }else {
            filterAdapter.setShowMemberPrice(showMemberPrice);
            filterAdapter.setWigherState(weigher_connected);
            filterAdapter.notifyDataSetChanged();
        }
    }

    private GoodKindsOnClickListener goodKindsOnClickListener=new GoodKindsOnClickListener() {
        @Override
        public void onGoodKindsClick(int typeId) {
            refreshGoods(typeId);
        }
    };
    private boolean isdeal=false;
    /**
     * 未结账订单详情删减的回调
     */
    private DetailOnDelListener detailOnDelListener=new DetailOnDelListener() {
        @Override
        public void onMinus(String json) {
            if(isdeal) {
                return;
            }
            isdeal=true;
            Map<String, String> params = netWorks.getPublicParams();
            params.put("order",json);
            netWorks.Request(UrlConstance.MODIFY_GOODS_NUM,false, "", params, 5000, 0, new NetWorks.OnNetCallBack(){
                @Override
                public void onError(Exception e, int flag) {
                    isdeal=false;
                    LogUtils.e(TAG,"更新订单：onError"+e.toString());
                    Utils.showToast(mActivity, "网络异常");
                }

                @Override
                public void onResonse(String response, int flag) {
                    isdeal=false;
                    LogUtils.e(TAG,"更新订单：response"+response.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            getUnpayOrderDetails(false);
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    /**
     * 清除本地信息1.取消订单(隐藏支付) 2.添加商品 (隐藏支付)  3.结账 (隐藏支付)  4.重新进入导航(隐藏支付) 页 5.提交订单(xianshi 支付)
     */
    private void clearLocalData(boolean clearMemInfo,boolean canInditifyMember) {
        if(clearMemInfo) {
            tvMemberPriceTitle.setVisibility(View.GONE);
        }
        reInitData(clearMemInfo);
        changeOrderRight(false);
        updateUI(0, 0,true);
        whetherCanNotifyMember(canInditifyMember);
    }

    /**
     * 抽取出来当度供提交订单后初始化数据，然后展示未结账订单详情数据使用
     * @param clearMemInfo
     */
    private void reInitData(boolean clearMemInfo) {
        Constant.order_goods_num = 0.00;
        Constant.localOrderBean.setGoodList(null);
        Constant.order_price = 0.00;
        Constant.order_goods_num = 0.00;
        if(clearMemInfo) {
            memberBean = null;
            orderBean = null;
            rlNomemberHeader.setVisibility(View.VISIBLE);
            llMemberInfo.setVisibility(View.GONE);
        }
        rg_payways.check(R.id.wx_pay);
    }

    private void whetherCanNotifyMember(boolean can) {
        if(!can) {
            noMemberNotice.setText("暂无订单详情");
            tv_inditify_member.setVisibility(View.GONE);
        }else {
            noMemberNotice.setText("暂无会员信息");
            tv_inditify_member.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        this.hidden=hidden;
        if(hidden) {
            ((MainActivity)mActivity).setLocalWeighListener(null);
            ((MainActivity)mActivity).setBleWeighListener(null);

        }else {
            initLocalWeigher();
        }
    }

    /**
     * 扫描结束后的回调
     */
    @Subscribe
    public void onEventMainThread(String barcode) {
        if (scanMemberPup != null && scanMemberPup.isShowing()) {//穿过来的是识别会员的码
            LogUtils.saveLog(TAG,"接收到会员条码：" + barcode);
            getMemberInfo(barcode);
        } else {
            if(barcode.length()>=20) {
                Utils.showToast(mActivity,"扫描太频繁，每次扫码后请暂停1秒以上");
                return;
            }
            if (order_right_pay.getVisibility() != View.GONE) {//支付码
                //扫到支付码
                LogUtils.saveLog(TAG,"扫描到支付条码：" + barcode + "   payWay==" + Constant.payway+"  wxstate="+Constant.OPEN_WXPAY+"  zfbstate="+Constant.OPEN_ALIPAY);
                if(TextUtils.equals("0",Constant.payway)&&Constant.OPEN_WXPAY==1&& NetworkUtils.isAvailable(mActivity)) {
                    toPay(barcode);
                }else if(TextUtils.equals("1",Constant.payway)&&Constant.OPEN_ALIPAY==1&&NetworkUtils.isAvailable(mActivity)) {
                    toPay(barcode);
                }
            } else {//商品码
                LogUtils.saveLog(TAG,"扫描到商品条码：" + barcode);
                findScanGood(barcode);
            }
        }
        EventBus.getDefault().cancelEventDelivery(barcode);
    }
    /**
     * 扫描结束后的回调
     */
    @Subscribe
    public void onEventMainThread(SmScannerBean barcode) {
        LogUtils.e(TAG,"  点单页面："+hidden);
        if(payScannerNormal==null) {
            return;
        }
        if(TextUtils.equals(barcode.getId(),"sm_scanner_oerder")) {
            if(savedScanner!=null) {
                BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(savedScanner.getBlueToothAdd());
                boolean scannerConnected = HidConncetUtil.getScannerConnectState(device);
                LogUtils.saveLog(TAG,"蓝牙连接状态："+scannerConnected);
                setPayScannerState(scannerConnected);
            }else {
                setPayScannerState(false);
            }
        }
        EventBus.getDefault().cancelEventDelivery(barcode);
    }

    /**
     * 读取到的码以99开头，那么就以自打印标签处理，否则以商品条码处理
     * @param barcode
     */
    synchronized private void findScanGood(String barcode) {

        LogUtils.e(TAG,"barcode="+barcode);
        if(goodBeanList==null) {
            MyApplication application=(MyApplication)mActivity.getApplication();
            DaoSession daoSession = application.getDaoSession();
            GoodBeanDao goodBeanDao = daoSession.getGoodBeanDao();
            goodBeanList = goodBeanDao.loadAll();
        }


        if(barcode.startsWith("99")||barcode.startsWith("98")) {
            String goodId = barcode.substring(2, 7);
            String totalfee = barcode.substring(7, barcode.length() - 1);
            Double aDouble = Double.valueOf(totalfee);
            double money = aDouble / 100;
            if(barcode.startsWith("98")) {
                money = aDouble / 10;
            }
            if (goodBeanList != null && goodBeanList.size() > 0) {
                for (int k = 0; k < goodBeanList.size(); k++) {
                    GoodBean goodTotalBean = goodBeanList.get(k);
                    if (goodId.equals(goodTotalBean.getGoodsId())) {//非多规格
                        GoodBean goodBean = goodTotalBean;
                        if (showMemberPrice && goodBean.getVipPrice()>=0) {
                            simpePrice = goodBean.getVipPrice();
                        }else {
                            simpePrice=goodBean.getGoodsPrice();
                        }
                        List<GoodBean> goodList = Constant.localOrderBean.getGoodList();
                        if (goodList != null) {
                            if (goodList.contains(goodBean)) {
                                goodBean.setGoodsNum(goodBean.getGoodsNum() + money/goodBean.getGoodsPrice());
                            } else {
                                goodBean.setGoodsNum(money/goodBean.getGoodsPrice());
                                goodList.add(0, goodBean);
                            }
                        } else {
                            goodBean.setGoodsNum(money/goodBean.getGoodsPrice());
                            List<GoodBean> list = new ArrayList<>();
                            list.add(0, goodBean);
                            Constant.localOrderBean.setGoodList(list);
                        }
                        UpdatePriceNum(money/goodBean.getGoodsPrice(), Double.valueOf(Utils.numdf.format(money)), 0,position);
                        return;
                    }
                }
            }
        }else {
            if (goodBeanList != null && goodBeanList.size() > 0) {
                for (int k = 0; k < goodBeanList.size(); k++) {
                    GoodBean goodTotalBean = goodBeanList.get(k);
                    if (barcode.equals(goodTotalBean.getGoodsCode())) {
                        GoodBean goodBean = goodTotalBean;
                        if (showMemberPrice && goodBean.getVipPrice()>=0) {
                            simpePrice = goodBean.getVipPrice();
                        }else {
                            simpePrice=goodBean.getGoodsPrice();
                        }
                        if (weigher_connected && TextUtils.equals("1", goodBean.getIs_weigh())) {
                            if(Constant.localOrderBean.getGoodList()!=null&&Constant.localOrderBean.getGoodList().size()>0) {
                                int poiInSecleted = Constant.localOrderBean.getGoodList().indexOf(goodBean);
                                showWeighPop(goodBean,goodBean.getGoodsName(),false,poiInSecleted,-1);
                            }else {
                                showWeighPop(goodBean,goodBean.getGoodsName(),false,0,-1);
                            }
                        }else{
                            scanAdd(goodBean, 1,k);
                        }
                        return;
                    }

                }
            }
        }
        Utils.showToast(mActivity, "未检索到该商品", 800);
    }

    /**
     * 添加实物或者服务
     */
    public void scanAdd(GoodBean goodBean, double weiAcc,int position) {
        List<GoodBean> goodList = Constant.localOrderBean.getGoodList();

        if (goodList != null) {
            if (goodList.contains(goodBean)) {
                goodBean.setGoodsNum(goodBean.getGoodsNum() + weiAcc);
            } else {
                goodBean.setGoodsNum(weiAcc);
                goodList.add(0, goodBean);
            }
        } else {
            goodBean.setGoodsNum(weiAcc);
            List<GoodBean> list = new ArrayList<>();
            list.add(0, goodBean);
            Constant.localOrderBean.setGoodList(list);
        }
        UpdatePriceNum(weiAcc, goodBean.getGoodsPrice() * weiAcc, 0,position);
    }

    //更新价格和商品数量
    private void UpdatePriceNum(double num, double price, int isClearData,int gvPosition) {
        Message mes = Message.obtain();
        mes.what = Constant.TOTAL_ACCOUNT;
        Bundle bundle = new Bundle();
        bundle.putInt("e_c", isClearData);
        bundle.putDouble("g_p", price);
        bundle.putDouble("g_n", num);
        bundle.putInt("adapter_position", gvPosition);
        mes.setData(bundle);
        handler.sendMessage(mes);
    }

    private void getMemberInfo(final String barcode) {
        int commited=order_right_pay.getVisibility()==View.VISIBLE?1:0;
        if(scanMemberPup!=null&&scanMemberPup.isShowing()) {
            scanMemberPup.dismiss();
        }
        NetWorks netWorks = new NetWorks(mActivity);
        Map<String, String> params = netWorks.getPublicParams();
        params.put("account_id", Constant.ACCOUNT_ID);
        params.put("adv_id", Constant.ADV_ID);
        params.put("storeid", Constant.STORE_ID);
        params.put("cardid", barcode);//455589890007
        params.put("flag", "0");
        params.put("order_id", primaryKeyId);
        params.put("total_fee", total_fee);
        params.put("commit", commited+"");//0:未提交订单 1：已提交订单
        params.put("type", "0");
        netWorks.Request(UrlConstance.NEW_QUERY_MEMBERINFO,true, "会员检索中...",params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("会员查询返回error：" + e.toString());
                Toast.makeText(mActivity, "网络不佳，请检查网络后重试！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("识别会员返回：" + response.toString());
                praseMemberInfoJson(response, barcode);
            }
        });
    }

    /**
     * @param json
     */
    private void praseMemberInfoJson(String json, String barcode) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            int code = jsonObj.getInt("code");
            if (code == 3005) {
                Utils.showToast(mActivity, "未检索到该会员信息！", 3000);
            } else if (code == 200) {
                Gson gson = new Gson();
                String card_type = jsonObj.getJSONObject("msg").getString("card_type");
                if (card_type != null) {
                    if ("MEMBER".equals(card_type)) {
                        memberBean = gson.fromJson(json, DetailMemberBean.class);
                        memberBean.getMsg().setAuth_code(barcode);
                        if(memberBean.getMsg().isIs_set_vip_price()) {
                            showMemberPrice=true;
                        }else {
                            showMemberPrice=false;
                        }
                        if(mainIndex==1) {
                            getUnpayOrderDetails(false);
                        }else {
                             countTotal();
                             updateAdapter();
                             showMemberInfo();
                        }

                    }
                }
                if (order_right_pay.getVisibility() == View.VISIBLE) {
                    getUnpayOrderDetails(false);
                }
            } else {
                String msg = jsonObj.getString("msg");
                Utils.showToast(mActivity, msg, 3000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 识别会员后重新计算价格
     */
    private void countTotal() {
        double total=0;
        if(mainIndex==0){
            List<GoodBean> goodList = Constant.localOrderBean.getGoodList();
            if(goodList!=null&&goodList.size()>0) {
                for (int i = 0; i < goodList.size(); i++) {
                    GoodBean goodBean = goodList.get(i);
                    if(goodBean.getVipPrice()<0) {//非会员价商品
                        total+=goodBean.getGoodsNum()*goodBean.getGoodsPrice();
                    }else {//会员价商品
                        total+=goodBean.getGoodsNum()*goodBean.getVipPrice();
                    }
                }
            }

        }else {
            List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_goods = orderBean.getMsg().getOrder_goods();
            int size = order_goods.size();
            for (int i = 0; i < size; i++) {
                UnPayDetailsBean.MsgBean.OrderGoodsBean orderGoodsBean = order_goods.get(i);
                if(orderGoodsBean.getIs_vip()==0) {
                    total+= orderGoodsBean.getGoods_num()* orderGoodsBean.getGoods_ori_price();
                }else {
                    total+= orderGoodsBean.getGoods_num()* orderGoodsBean.getGoods_price();
                }

            }
        }
        Constant.order_price=total;
        setLeftTotal(total);
    }

    private void updateAdapter() {
        LogUtils.Log("更新adapter");
       //已选
        if(selectedAdapter!=null) {
            selectedAdapter.setEverWeigher(weigher_connected);
            selectedAdapter.setShowMemberPrice(showMemberPrice);
            selectedAdapter.notifyDataSetChanged();
        }
        //待选
        if(gs_detail_adapter!=null){
            gs_detail_adapter.setWigherState(weigher_connected);
            gs_detail_adapter.setShowMemberPrice(showMemberPrice);
            gs_detail_adapter.notifyDataSetChanged();
        }
        //搜索
        if(filterAdapter!=null&&gv_search.getVisibility()==View.VISIBLE) {
            filterAdapter.setWigherState(weigher_connected);
            filterAdapter.setShowMemberPrice(showMemberPrice);
            filterAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 展示会员信息 途径：1.识别会员 2.未记账订单详情获取
     */
    private void showMemberInfo() {
        if(showMemberPrice) {
            tvMemberPriceTitle.setVisibility(View.VISIBLE);
        }else {
            tvMemberPriceTitle.setVisibility(View.GONE);
        }
        textView4.setText("原价");
        memberPay.setVisibility(View.VISIBLE);
        memberPayLine.setVisibility(View.VISIBLE);

        rlNomemberHeader.setVisibility(View.GONE);
        llMemberInfo.setVisibility(View.VISIBLE);
        tvMemberLeft.setText("￥" + memberBean.getMsg().getMoney());
        memberAverageConsume.setText("￥" +Utils.keepTwoDecimal( memberBean.getMsg().getAverage()+""));
        tvComeTimes.setText(memberBean.getMsg().getMonthnum() + "次");
        memberName.setText("会员："+memberBean.getMsg().getName());
        memberFav.setText("最爱商品："+memberBean.getMsg().getTop5());
        if (memberBean.getMsg().isSupply()) {
            alMemberLeft.setVisibility(View.VISIBLE);
            tvMemberLeft.setText("￥" + memberBean.getMsg().getMoney());
            tvMemberpayMemberLeft.setText("会员卡余额：￥"+Utils.keepTwoDecimal(memberBean.getMsg().getMoney()+""));
        } else {
            alMemberLeft.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        LogUtils.e(TAG,"onDestory()");
        EventBus.getDefault().unregister(this);
        try{
            if(everRegistWeighReceiver) {
                WeighManager.getInstance().unRegistReceiver(mActivity);
            }
            if(everRegistScannerReceiver) {
                mActivity.unregisterReceiver(receiver);
            }
        }catch (Exception e){
            LogUtils.Log("e=="+e);
        }
        super.onDestroy();
    }

    public void setPrintService(IWoyouService printService) {
        this.iWoyouService = printService;
    }

    /**
     * 更新视图
     * @param total_money
     * @param goods_num
     */
    private void updateUI(double total_money, double goods_num,boolean refreshSelected) {

        if(refreshSelected) {
            if (selectedAdapter != null) {
                selectedAdapter.setData(Constant.localOrderBean);
                selectedAdapter.setShowMemberPrice(showMemberPrice);
                selectedAdapter.notifyDataSetChanged();
                selectedAdapter.setEverWeigher(weigher_connected);

                lv_selected_gs.setSelection(target_poisition);
            } else {
                selectedAdapter = new ItemContentAdapter(mActivity, handler);
                selectedAdapter.setDetailOnDelListener(detailOnDelListener);
                selectedAdapter.setData(Constant.localOrderBean);
                selectedAdapter.setOnWeighChangeListener(onWeighGoodChangeListener);
                selectedAdapter.setShowMemberPrice(showMemberPrice);
                selectedAdapter.setEverWeigher(weigher_connected);
                lv_selected_gs.setAdapter(selectedAdapter);
                lv_selected_gs.setSelection(target_poisition);
            }
        }
        if(total_money<=0&&goods_num<=0) {
            commit_order.setVisibility(View.GONE);
            btn_uncommit_cancel.setText("返回");
        }else {
            if(TextUtils.equals("0",primaryKeyId)){
                btn_uncommit_cancel.setText("取消订单");
            }else {
                btn_uncommit_cancel.setText("返回");
            }
            commit_order.setVisibility(View.VISIBLE);
        }
        if(filterAdapter!=null&&gv_search.getVisibility()==View.VISIBLE) {
            filterAdapter.notifyDataSetChanged();
        }
        upDateOrder(total_money, goods_num);
    }

    /**
     * 更新出adapter外的东西
     * @param total_money
     * @param goods_num
     */
    private void upDateOrder(double total_money, double goods_num) {
        setLeftTotal(total_money);
        if (goods_num > 0) {
            tv_no_gs.setVisibility(View.GONE);
            totalNumAcc.setVisibility(View.VISIBLE);
            lv_selected_gs.setVisibility(View.VISIBLE);
            btn_uncommit_cancel.setVisibility(View.VISIBLE);
        } else {
            if(mainIndex==1) {
                tv_no_gs.setText("请选择目标订单");
            }else {
                tv_no_gs.setText("请添加商品");
            }
            tv_no_gs.setVisibility(View.VISIBLE);
            totalNumAcc.setVisibility(View.GONE);
            lv_selected_gs.setVisibility(View.GONE);
            btn_uncommit_cancel.setVisibility(View.GONE);
        }
        if (goods_num <= 0) {
            orderGsNum.setVisibility(View.GONE);
        } else {
            orderGsNum.setVisibility(View.VISIBLE);
            orderGsNum.setText(Utils.formatPrice(goods_num));
        }
    }

    /**
     * 已选商品的总价，优惠金额
     * @param total_money,实际金额
     */
    private void setLeftTotal(double total_money) {
        orderGsAcc.setText(Utils.keepTwoDecimal("" + total_money));
        if(showMemberPrice) {
            double oriTotal=0;
            int dataType = selectedAdapter.getType();
            if(dataType==0) {//计算localbean
                oriTotal = countOrifee();
            }else if(dataType==1) {//计算unpaybean
                oriTotal=orderBean.getMsg().getPrice();
            }

            String oriTotalStr = Utils.keepTwoDecimal(oriTotal + "");
            tv_reducefee_name.setVisibility(View.VISIBLE);
            tv_memmber_reduce.setVisibility(View.VISIBLE);
            totalfee_name.setText("原价："+oriTotalStr+"，会员价：");
            tv_memmber_reduce.setText("-"+Utils.keepTwoDecimal(Utils.keepTwoDecimal(oriTotal - total_money + "")));
        }else {
            tv_reducefee_name.setVisibility(View.GONE);
            tv_memmber_reduce.setVisibility(View.GONE);
            totalfee_name.setText("合计：");
        }
    }

    /**
     * 计算会员产生的优惠金额
     */
    private double countOrifee() {
        List<GoodBean> goodList = Constant.localOrderBean.getGoodList();
        double oriTotal=0.00;
        if(goodList!=null&&goodList.size()>0) {
            int size = goodList.size();
            for (int i = 0; i < size; i++) {
                oriTotal+=(goodList.get(i).getGoodsPrice()*goodList.get(i).getGoodsNum());
            }
        }
        return oriTotal;
    }

    /**
     * 提交订单
     */
    @OnClick(R.id.commit_order)
    public void commitOrder() {
        LogUtils.Log("3333333333");
        String action=branachOrder?"1":"0";
        String trade_num = Utils.getTradeNum();
        //存储交易编号 还要记录主键id
        /*Constant.localOrderBean.setTradeNum(trade_num);
        Constant.localOrderBean.setPrimaryKeyId(primaryKeyId);*/
        String commitOrderJson = getCommitOrderJsonForMulti();
        String vipcard_id = "";
        if (memberBean != null) {
            vipcard_id = memberBean.getMsg().getVipcardid();
        }
        int versionCode = Utils.getVersionCode(mActivity);
        NetWorks netWorks = new NetWorks(mActivity);
        Map<String, String> params = netWorks.getPublicParams();
        params.put("advid", Constant.ADV_ID);
        params.put("storeid", Constant.STORE_ID);
        params.put("account_id", Constant.ACCOUNT_ID);
        params.put("orderid", primaryKeyId);
        params.put("orderNo", trade_num);
        params.put("goodsNum", commitOrderJson);
        params.put("vipcard_id", vipcard_id);
        params.put("action", action);//action=0代表新建 1代表增加
        params.put("versionCode", String.valueOf(versionCode));
        netWorks.Request(UrlConstance.COMMIT_ORDER,true,"正在提交订单...", params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.e(TAG, "Exception e=" + e);
                Utils.showToast(mActivity, "提交订单失败，请检查网络后再试");
            }

            @Override
            public void onResonse(String response, int flag) {
                branachOrder=true;
                LogUtils.e(TAG, "新订单onResponse():response=" + response);
                parseCommitJson(response);
            }
        });
    }
    /**
     * 解析提交订单返回数据
     * @param json
     */
    private void parseCommitJson(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            JSONObject msg = jsonObject.getJSONObject("msg");
            if (code == 200) {//提交订单成功,清楚本地数据，展示未结账订单详情数据,包括会员信息
                SaveUtils.saveObject(mActivity,Constant.TEMP_ORDER,Constant.localOrderBean);
                reInitData(true);
                changeOrderRight(true);
                Constant.FIRST_COMMIT = true;
                int orderId = msg.getInt("orderId");
                primaryKeyId = "" + orderId;
                getUnpayOrderDetails(true);
            } else {
                Utils.showToast(mActivity, "提交订单失败，"+msg.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1.添加商品 2.清楚数据 3.提交订单后 4.需要展示未结账订单详情
     * @param showPay
     */
    private void changeOrderRight(boolean showPay) {
        if (showPay) {//左侧底部显示添加商品，右侧显示支付
            uncommit_footer.setVisibility(View.GONE);
            comitted_footer.setVisibility(View.VISIBLE);
            order_right_pay.setVisibility(View.VISIBLE);
            order_right.setVisibility(View.GONE);
            ((RadioButton) totalView.findViewById(R.id.wx_pay)).setChecked(true);
            commited_order_up.setText("挂单");
            fl_order_list.setVisibility(View.GONE);

        } else {
            if(mainIndex==0) {
                uncommit_footer.setVisibility(View.VISIBLE);

            }else {
                uncommit_footer.setVisibility(View.GONE);
            }
            comitted_footer.setVisibility(View.GONE);
            order_right_pay.setVisibility(View.GONE);
            order_right.setVisibility(View.VISIBLE);
        }
    }
    private void changeUnpayDetailFoot() {
        uncommit_footer.setVisibility(View.GONE);
        comitted_footer.setVisibility(View.VISIBLE);
        commited_order_up.setText("结算");
    }
    /**
     * 获取订单详情1.未结账页面识别会员 2.提交订单 3.未结账订单列表获取详情
     */
    private void getUnpayOrderDetails(final boolean printBanch) {
        LogUtils.Log("获取未结账订单详情");
        if (memberBean != null) {
            vipcreate_id = memberBean.getMsg().getVipcreate_id();
        }
        if (vipcreate_id == null) {
            vipcreate_id = "0";
        }
        final NetWorks netWorks = new NetWorks(mActivity);
        Map<String, String> publicParams = netWorks.getPublicParams();
        publicParams.put("order_id", primaryKeyId);
        publicParams.put("vipcreate_id", vipcreate_id);
        netWorks.Request(UrlConstance.UNPAY_ORDER_DETAIL,true, "正在获取订单详情...",publicParams, 5000, 2, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                if(unPayOrderListFragment!=null) {
                    unPayOrderListFragment.OnNetError();
                }
                netError.setVisibility(View.VISIBLE);
                LogUtils.Log("获取未结账订单详情onError() e==" + e.toString());
            }
            @Override
            public void onResonse(String response, int flag) {
                if(unPayOrderListFragment!=null) {
                    unPayOrderListFragment.OnNetOk();
                }
                netError.setVisibility(View.GONE);

                LogUtils.Log("未支付订单详情response=" + response);
                parseUnPayOrderJson(response,printBanch);
            }
        });
    }
    /**
     * 解析返回的数据
     * @param response
     */
    private void parseUnPayOrderJson(String response,boolean printBanch) {
        Utils.dismissWaittingDialog();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                Gson gson = new Gson();
                orderBean = gson.fromJson(response, UnPayDetailsBean.class);
                order_total_fee = orderBean.getMsg().getTotal_fee() + "";
                praseUnpayOrderMember(response);
                total_fee=orderBean.getMsg().getTotal_fee()+"";
                cardinfo_json = getCardinfoJson();
//                SpUtils.getInstance(mActivity).save("order_details_res", response);
                //用于分订单的展示
                if (orderBean.getMsg().getOrder_rooms() != null && orderBean.getMsg().getOrder_rooms().size() > 0) {
                    Constant.CURRENT_ORDER_ROOM = orderBean.getMsg().getOrder_rooms().get(0).getRoomname();
                } else {
                    Constant.CURRENT_ORDER_ROOM = "";
                }
                Constant.CURRENT_ORDER_NUM = String.valueOf(orderBean.getMsg().getQuery_num());
                showUnPayOrderData();
                showYouHui();
                OrderBean tempOrder= (OrderBean) getObject(mActivity,Constant.TEMP_ORDER);
                PrintUtils.getInstance().formatUnPayPrintData(tempOrder,orderBean, unPayCustomerPrintData,false,false);
                PrintUtils.getInstance().formatUnPayPrintData(tempOrder,orderBean, unPayBTCustomerPrintData,true,false);
                PrintUtils.getInstance().formatUnPayPrintMerChantData(mActivity,tempOrder,orderBean,unpayMerchantData,false,false);
                PrintUtils.getInstance().formatUnPayPrintMerChantData(mActivity,tempOrder,orderBean,unpayBTMerchantData,true,false);
                oneByOneYunList = YunPrintUtils.formateSplitOrderDetailData(unPayBTCustomerPrintData, orderBean,tempOrder,false);
                if(printBanch) {
                    unpayOrderPrintTransition(false);
                }
            } else {
                Utils.showToast(mActivity, "未支付订单详情数据请求失败，请检查网络后再试");
            }
        } catch (JSONException e) {
            LogUtils.e(TAG, "JSONException e=" + e);
        }
    }

    @BindView(R.id.rl_pay_inditifymember) RelativeLayout rl_pay_inditifymember;
    @BindView(R.id.ll_vocher_total) LinearLayout ll_vocher_total;
//    @BindView(R.id.ll_voucher_right) LinearLayout ll_voucher_right;
    @BindView(R.id.cb_left)TextView cb_left;
    @BindView(R.id.cb_right) TextView cb_right;
    @BindView(R.id.youhui_discount) TextView youhui_discount;
    @BindView(R.id.tv_huiyuan_youhuijia) TextView tv_huiyuan_youhuijia;
    private final String TAG="orderpagerFragment";
    private void showYouHui() {
        double youhui = orderBean.getMsg().getPrice() - orderBean.getMsg().getPayment();
        String youhuiStr=Utils.keepTwoDecimal(youhui+"");

        LogUtils.e(TAG,"showYouHui()--yohuiStr="+youhuiStr);
        vip_discount = orderBean.getMsg().getVip_discount();
        //3.代金券
        UnPayDetailsBean.MsgBean.voucherinfoBean voucherinfo = orderBean.getMsg().getVoucherinfo();
        if(voucherinfo !=null){
            if(voucherinfo.getCard_type()!=null){
                if("CASH".equals(voucherinfo.getCard_type())){
                    rl_pay_inditifymember.setVisibility(View.GONE);
                    ll_vocher_total.setVisibility(View.VISIBLE);
                    tv_huiyuan_youhuijia.setVisibility(View.GONE);
                    cb_right.setVisibility(View.VISIBLE);
                    youhui_discount.setVisibility(View.GONE);
                    cb_left.setVisibility(View.VISIBLE);
                    cb_left.setText("代金券："+ voucherinfo.getInfo());
                    tvYouhuifee.setText(String.valueOf(voucherinfo.getReduce_cost()));
                    return;
                }
                if("DISCOUNT".equals(voucherinfo.getCard_type())){
                    rl_pay_inditifymember.setVisibility(View.GONE);
                    ll_vocher_total.setVisibility(View.VISIBLE);
                    tv_huiyuan_youhuijia.setVisibility(View.GONE);
                    cb_right.setVisibility(View.VISIBLE);
                    youhui_discount.setVisibility(View.GONE);
                    cb_left.setVisibility(View.VISIBLE);
                    cb_left.setText("折扣券："+ voucherinfo.getInfo());
                    tvYouhuifee.setText("-"+youhuiStr);
                    return;
                }
            }

        }
        //1.会员折扣(全场折)
        if (vip_discount != null && Double.valueOf(vip_discount) > 0) {
            rl_pay_inditifymember.setVisibility(View.GONE);
            ll_vocher_total.setVisibility(View.VISIBLE);
            cb_right.setVisibility(View.GONE);
            tv_huiyuan_youhuijia.setVisibility(View.GONE);
            youhui_discount.setVisibility(View.VISIBLE);
            cb_left.setVisibility(View.VISIBLE);
            cb_left.setText("会员折扣");
            youhui_discount.setText(vip_discount+"折");
            tvYouhuifee.setText("-"+youhuiStr);
            return;
        }
        //2.会员价
        if(orderBean.getMsg().getVipinfo()!=null&&orderBean.getMsg().getVipinfo().isIs_set_vip_price()) {
            rl_pay_inditifymember.setVisibility(View.GONE);
            ll_vocher_total.setVisibility(View.VISIBLE);
            cb_left.setVisibility(View.GONE);
            cb_right.setVisibility(View.GONE);
            youhui_discount.setVisibility(View.GONE);
            tv_huiyuan_youhuijia.setVisibility(View.VISIBLE);
            tvYouhuifee.setText("原价："+Utils.keepTwoDecimal(orderBean.getMsg().getPrice()+"")+", 会员优惠：-"+youhuiStr);
            return;
        }
        ll_vocher_total.setVisibility(View.GONE);
        rl_pay_inditifymember.setVisibility(View.VISIBLE);
    }

    /**
     * 放弃使用优惠券
     * @param view
     */
    @OnClick({R.id.cb_right})
    void onVoucherEnableDisable(View view){
        Map<String, String> params = netWorks.getPublicParams();
        params.put("orderid",primaryKeyId);
        netWorks.Request(UrlConstance.DEL_ORDER_VOUCHER,true, "正在移除优惠券", params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.showToast(mActivity,"网络异常，优惠券移除失败！");

                LogUtils.e(TAG,"移除优惠券onerror() e=="+e.toString());
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.e(TAG,"移除优惠券onresponse() response=="+response.toString());
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200) {
                        Utils.showToast(mActivity,"优惠券已移除！");
                        getUnpayOrderDetails(false);
                    }else {
                        Utils.showToast(mActivity,"优惠券移除失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    /**
     * 待支付的信息
     */
    private void showUnPayOrderData() {
        selectedAdapter.setData(orderBean);
        selectedAdapter.setShowMemberPrice(showMemberPrice);
        selectedAdapter.setOnWeighChangeListener(onWeighGoodChangeListener);
        selectedAdapter.setEverWeigher(weigher_connected);
        selectedAdapter.notifyDataSetChanged();
        //备注
        rlOrderRemark.setVisibility(View.VISIBLE);
        String remark = orderBean.getMsg().getRemark();
        if(remark !=null&&!TextUtils.isEmpty(remark)) {
            tvOrderRemark.setText(remark);
        }

        upDateOrder(orderBean.getMsg().getTotal_fee(),orderBean.getMsg().getGoods_num());
        payment = "" + orderBean.getMsg().getPayment();
        order_total_fee = String.valueOf(orderBean.getMsg().getPrice());
        tvTotalfee.setText(Utils.keepTwoDecimal(order_total_fee));
        tvYouhuifee.setText(Utils.keepTwoDecimal(orderBean.getMsg().getPrice() - orderBean.getMsg().getPayment() + ""));
        tvShouldPay.setText(Utils.keepTwoDecimal(payment));
        showPaymentInFp();

        tvOrderNum.setText(orderBean.getMsg().getQuery_num());
        initCardPayState();
        String nowTime = Utils.df3.format(System.currentTimeMillis());
        String addime = Utils.df3.format(Long.valueOf(orderBean.getMsg().getAddtime()) * 1000);
        LogUtils.Log("addTime==" + addime);
        tvOrdertime.setText("下单时间：" + nowTime);
    }

    /**
     *副屏展示应付金额
     */
    private void showPaymentInFp() {
        if(order_right_pay.getVisibility()== View.VISIBLE) {
            ((MainActivity)mActivity).stopFpLb();
            FpUtils.showTp(((MainActivity)mActivity).dsKernel,payment);
        }
    }
    @BindView(R.id.ll_memberpay_lack)LinearLayout ll_memberpay_lack;
    @BindView(R.id.ll_memberpay_enough)LinearLayout ll_memberpay_enough;
    @BindView(R.id.tv_current_left)TextView tv_current_left;
    @BindView(R.id.tv_member_needmore) TextView tv_member_needmore;
    @BindView(R.id.rcy) RecyclerView rcy;
    @BindView(R.id.choose_pay_way) LinearLayout choose_pay_way;
    @BindView(R.id.tv_back) TextView tv_back;
    @BindView(R.id.lack_title) TextView lack_title;
    @BindView(R.id.ll_needmore) LinearLayout ll_needmore;
    DecimalFormat numFormate = new DecimalFormat("0.00");
    /**
     * 改价完毕调用，获取未结账详情调用
     */
    private void initCardPayState() {

        if(memberBean!=null&&memberBean.getMsg()!=null) {
            if(memberBean.getMsg().getMoney()<orderBean.getMsg().getPayment()) {//会员卡余额不足，走先充值后扣款流程
                double needMore = orderBean.getMsg().getPayment() - memberBean.getMsg().getMoney();

                showChargeItem(Double.valueOf(numFormate.format(needMore)));
            }else {
                ll_memberpay_lack.setVisibility(View.GONE);
                ll_memberpay_enough.setVisibility(View.VISIBLE);
                tv_member_pay.setVisibility(View.VISIBLE);
                tvMemberpayReduce.setText("（从余额中直接抵扣）￥"+ Utils.keepTwoDecimal(orderBean.getMsg().getPayment()+""));
            }
        }else {
            memberPay.setVisibility(View.GONE);
        }
    }
    private String chargeMoney;

    /**
     * 展示充值选项
     */
    private void showChargeItem(double needMore) {
        ll_memberpay_lack.setVisibility(View.VISIBLE);
        ll_memberpay_enough.setVisibility(View.GONE);
        tv_current_left.setText("扣除会员卡余额"+Utils.keepTwoDecimal(memberBean.getMsg().getMoney()+"")+"元后，还需收款");
        tv_member_needmore.setText(Utils.keepTwoDecimal(needMore+"")+"元");
        tv_back.setVisibility(View.GONE);
        lack_title.setText("会员卡余额不足抵扣");
        lack_title.setTextColor(Color.parseColor("#EA631B"));
        ll_needmore.setVisibility(View.VISIBLE);
        choose_pay_way.setVisibility(View.GONE);
        rcy.setVisibility(View.VISIBLE);
        llManager = new LinearLayoutManager(mActivity);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcy.setLayoutManager(llManager);
        rcy.setHasFixedSize(true);
//        rcy.addItemDecoration(new DividerItemDecoration(mActivity,LinearLayoutManager.VERTICAL));
        chargeAdapter = new RecyclerAdapter(mActivity);
         List<RulesBean> rules = memberBean.getMsg().getRules();
        RulesBean rulesBean=new RulesBean();
        rulesBean.setMoney(Utils.keepTwoDecimal(needMore+""));
        rulesBean.setSong("0");
        rules.add(0,rulesBean);
        LogUtils.e(TAG,"筛选前："+rules.toString());
        final List<RulesBean> filterRules = new ArrayList<>();
        //移除充值后扔不足以抵扣的选项
        for (int i = 0; i < rules.size(); i++) {
            String chong = rules.get(i).getMoney();
            String song = rules.get(i).getSong();
            double zong = Double.valueOf(chong) + Double.valueOf(song);
            Double aDouble = Double.valueOf(needMore);
            LogUtils.e(TAG,"zong="+zong+" more="+aDouble);
            if(zong>= aDouble) {
                filterRules.add(rules.get(i));
            }
        }
        LogUtils.e(TAG,"筛选后："+filterRules.toString());
        chargeAdapter.setData(filterRules,RecyclerAdapter.PAY_MEMBER_CHARGE_ITEM);

        rcy.setAdapter(chargeAdapter);
        chargeAdapter.setItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RulesBean chooseBean = filterRules.get(position);
                chargeMoney = chooseBean.getMoney();
                LogUtils.e(TAG,"chargeMoney="+chargeMoney);
                song = chooseBean.getSong();
                if(position==0) {
                    usePackage =0;
                }else {
                    usePackage =1;
                }
                //选择充值方式
                choose_pay_way.setVisibility(View.VISIBLE);
                rcy.setVisibility(View.GONE);
                tv_back.setVisibility(View.VISIBLE);
                lack_title.setText("充值"+chargeMoney+"元，请选择收款方式");
                lack_title.setTextColor(Color.parseColor("#333333"));
                ll_needmore.setVisibility(View.GONE);
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_pay_way.setVisibility(View.GONE);
                rcy.setVisibility(View.VISIBLE);
                tv_back.setVisibility(View.GONE);
                lack_title.setText("会员卡余额不足抵扣");
                lack_title.setTextColor(Color.parseColor("#EA631B"));
                ll_needmore.setVisibility(View.VISIBLE);

            }
        });
    }
    @OnClick({R.id.rl_chargepay_card,R.id.rl_chargepay_cash,R.id.rl_chargepay_wx,R.id.rl_chargepay_zfb})
    void chargePayOnClick(View view){
        switch (view.getId()) {
            case R.id.rl_chargepay_wx :
                 scanMemberDia = NoticePopuUtils.showInditifyMemberDia(mActivity, R.id.order_pager_layout,savedScanner, NoticePopuUtils.WX_CODE,new NoticePopuUtils.OnFinishpswCallBack() {
                    @Override
                    public void onFinishInput(String msg) {
                        scanMemberDia.dismiss();
                        Constant.payway="0";
                        toCharge(msg);
                    }
                });
                break;
            case R.id.rl_chargepay_zfb:
                Utils.showToast(mActivity,"支付宝");
                scanMemberDia = NoticePopuUtils.showInditifyMemberDia(mActivity, R.id.order_pager_layout,savedScanner, NoticePopuUtils.ZFB_CODE,new NoticePopuUtils.OnFinishpswCallBack() {
                    @Override
                    public void onFinishInput(String msg) {
                        scanMemberDia.dismiss();
                        Constant.payway="1";
                        Utils.showToast(mActivity,"支付宝:"+msg);
                        toCharge(msg);
                    }
                });
                break;
            case R.id.rl_chargepay_cash:
                Utils.showToast(mActivity,"现金");
                NoticePopuUtils.showCashChargeDia(mActivity, chargeMoney, new NoticePopuUtils.OnFinishChoose() {
                    @Override
                    public void onClickYes() {
                        Constant.payway="3";
                        toChargeCash();
                    }
                });
                break;
            case R.id.rl_chargepay_card:
                NoticePopuUtils.showCardChargeDia(mActivity, new NoticePopuUtils.OnFinishChoose() {
                    @Override
                    public void onClickYes() {
                        Constant.payway="2";
                        cashCardPay();
                        Utils.showToast(mActivity,"发起刷卡记账请求");
                    }
                });
                Utils.showToast(mActivity,"刷卡");
                break;
        }
    }

    private void toChargeCash() {
        LogUtils.e(TAG,"payment="+payment);
        Constant.UNKNOWN_COMMODITY="2";
        //秒
        trade_time = Utils.getTradeTime();
        String tradeNum= Utils.getTradeNum();//"order_no"
        SavedFailOrder cashOrder=PayTranstions.getSaveOrder("0",tradeNum,"0",chargeMoney+"","0", trade_time,
                "1","0",chargeMoney+"",
                memberBean.getMsg().getVipcardid(),chargeMoney+"",song+"","0","0","");
        cashOrder.setUse_package(usePackage);
        CashCardPayUtils.getInstance().cashCardPay(mActivity, cashOrder, null, null, new OnPayFinish() {
            @Override
            public void onPaySuccess(String response,String transtionid,String payment) {
                startMemberPay();
            }

            @Override
            public void onPayFailure(String errorMsg,String reason,String solution) {
                payFail(reason,solution);
            }
        });
    }

    private void toCharge(String barcode) {
        Constant.UNKNOWN_COMMODITY="2";
        String tradeNum = Utils.getTradeNum();
        trade_time = Utils.getTradeTime();
        PayUtils.toPay(mActivity, totalView, tradeNum,chargeMoney + "", chargeMoney + "", barcode, null, memberBean.getMsg().getVipcardid(), null, null,
                null, null, null,usePackage, new OnPayFinish() {
                    @Override
                    public void onPaySuccess(String response, String transtion_id, String payment) {
//                        paySccess(response, transtion_id, payment);
                        startMemberPay();

                    }
                    @Override
                    public void onPayFailure(String response,String reaseon,String solution) {
                        payFail(reaseon,solution);
                    }
                });
    }


    private static final String TAG_P = "upPayPrintData";

    /**
     * 打印未结账订单
     */
    void unpayOrderPrintTransition(boolean click) {
        boolean btJudegOne=true;
        boolean localJudegOne=true;
        if(!click) {
            if(btPrinterPermission!=null) {
                btJudegOne=btPrinterPermission.contains("1");
            }
            if(localPrinterPermission!=null) {
                localJudegOne=localPrinterPermission.contains("1");
            }
        }
        if(btPrinterPermission!=null&&btJudegOne) {//自动打印
            judgeBtPrint();
        }
        if(localPrinterPermission!=null&&localJudegOne) {
            if(localPrinterPermission.contains("3")) {
                if(localPrinterPermission.contains("2")) {//同时打印商家和顾客
                    PrintUtils.getInstance().zsmcPrint(iWoyouService, unPayCustomerPrintData, true, null, 22, new ICallback.Stub() {
                        @Override
                        public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {
                            if(isSuccess) {
                                PrintUtils.getInstance().printWmOneByOne(iWoyouService,unpayMerchantData,null);
                            }
                        }
                    });
                }else {//只打印顾客
                    PrintUtils.getInstance().zsmcPrint(iWoyouService, unPayCustomerPrintData, true, null, 22, null);
                }
            }else if(localPrinterPermission.contains("2")) {//只打印商家
                PrintUtils.getInstance().printWmOneByOne(iWoyouService,unpayMerchantData,null);
            }
        }
        startYunPrint(!click);
    }
    /**
     * 开启云打印
     */
    private void startYunPrint(boolean printAuto) {
        if(oneByOneYunList !=null&& oneByOneYunList.size()>0) {
            YunPrintUtils.yunPrintOneByOneNew(mActivity, oneByOneYunList,"3","2",printAuto,null);
        }
    }
    /**
     * 判断是否蓝牙打印
     */
    private void judgeBtPrint() {
        if(btPrinterPermission.contains("2")||btPrinterPermission.contains("3")) {
            if(mService.getState()== BluetoothService.STATE_CONNECTED) {
                printByBluetooth();
            }else {
                payed=false;
                BTPrintUtils.getInstance().connectBtPrinterTest(mService,mActivity,handler);
            }
        }
    }
    /**
     * 蓝牙打印（输入输出流）
     */
    private void printByBluetooth() {
        if(btPrinterPermission.contains("3")) {//打印顾客联
            if(btPrinterPermission.contains("2")) {//同时打印商家联
                printBtCustomer(true, new BluetoothService.BtPrintFinshCallBack() {//打印顾客联
                    @Override
                    public void onError() {
                    }
                    @Override
                    public void onFinish() {
                        LogUtils.e(TAG,"蓝牙打印完成了");
                        printBtMerchant(0);//打印商家联
                    }
                });
            }else {
                printBtCustomer(true,null);//打印顾客联
            }
        }else {//不打顾客联
            if(btPrinterPermission.contains("2")) {//打印商家联
                printBtMerchant(0);
            }
        }
    }
    private void printBtCustomer(boolean forCustomer,BluetoothService.BtPrintFinshCallBack callBack) {
        if(!forCustomer) {
            unPayBTCustomerPrintData.remove(1);
        }
        if(mService!=null&&mService.getState()== BluetoothService.STATE_CONNECTED) {
            if(forCustomer){
                BTPrintUtils.getInstance().csDetailBtPrint(mService, unPayBTCustomerPrintData, forCustomer, null, mActivity,callBack);
            }else{
                BTPrintUtils.getInstance().csDetailBtPrint(mService, unPayCustomerPrintData, forCustomer, null, mActivity, callBack);
            }
        }else {
            BTPrintUtils.getInstance().connectBtPrinterTest(mService, mActivity, handler);
        }
    }
    private void printBtMerchant(final int index) {
        if(unpayBTMerchantData!=null&&unpayBTMerchantData.size()>0) {
            if(index>unpayBTMerchantData.size()-1) {
                return;
            }
            BTPrintUtils.getInstance().oneByOnePrint(mService, unpayBTMerchantData.get(index), mActivity, new BluetoothService.BtPrintFinshCallBack() {
                @Override
                public void onError() {
                }
                @Override
                public void onFinish() {
                    printBtMerchant(index+1);
                }
            });
        }
    }

    /**
     * 解析当期订单的会员信息
     * @param json
     */
    private void praseUnpayOrderMember(String json) {
        try {
            JSONObject obj1 = new JSONObject(json);
            JSONObject msg = obj1.getJSONObject("msg");
            JSONObject vipinfo = msg.getJSONObject("vipinfo");

            if (vipinfo != null) {
                LogUtils.Log("vipinfo==" + vipinfo.toString());
                Gson gson = new Gson();
                DetailMemberBean.MsgBean msgBean = gson.fromJson(vipinfo.toString(), DetailMemberBean.MsgBean.class);
                memberBean = new DetailMemberBean();
                memberBean.setMsg(msgBean);
                if(memberBean.getMsg().isIs_set_vip_price()&&orderBean.getMsg().getVoucherinfo()==null) {
                    showMemberPrice=true;
                }else {
                    showMemberPrice=false;
                }
                showMemberInfo();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取券信息,用于支付提交
     */
    private String getCardinfoJson() {
        if (orderBean.getMsg().getVoucherinfo() != null) {
            PayCardinfoBean payCardinfoBean = new PayCardinfoBean();
            if (orderBean.getMsg().getVoucherinfo().getCard_type() != null) {
                switch (orderBean.getMsg().getVoucherinfo().getCard_type()) {
                    case "DISCOUNT"://折扣券
                        payCardinfoBean.setCode(orderBean.getMsg().getVoucherinfo().getCode());
                        payCardinfoBean.setCardid(orderBean.getMsg().getVoucherinfo().getCardid());
                        payCardinfoBean.setCardtype(orderBean.getMsg().getVoucherinfo().getCard_type());
                        payCardinfoBean.setDiscount(String.valueOf(orderBean.getMsg().getVoucherinfo().getDiscount()));
                        payCardinfoBean.setVipcardid(orderBean.getMsg().getVoucherinfo().getVipcardid());
                        payCardinfoBean.setInfo(orderBean.getMsg().getVoucherinfo().getInfo());
                        break;
                    case "CASH"://抵扣券
                        payCardinfoBean.setCode(orderBean.getMsg().getVoucherinfo().getCode());
                        payCardinfoBean.setCardid(orderBean.getMsg().getVoucherinfo().getCardid());
                        payCardinfoBean.setCardtype(orderBean.getMsg().getVoucherinfo().getCard_type());
                        payCardinfoBean.setLeast_cost(String.valueOf(orderBean.getMsg().getVoucherinfo().getLeast_cost()));
                        payCardinfoBean.setReduce_cost(String.valueOf(orderBean.getMsg().getVoucherinfo().getReduce_cost()));
                        payCardinfoBean.setVipcardid(String.valueOf(orderBean.getMsg().getVoucherinfo().getVipcardid()));
                        payCardinfoBean.setInfo(orderBean.getMsg().getVoucherinfo().getInfo());
                        break;
                }
                return new Gson().toJson(payCardinfoBean);
            }
        }
        return "";
    }

    /**
     * 得到提交的json数据
     * @return
     */
    private String getCommitOrderJsonForMulti() {
        List<CommitOrderBean> commitOrderBeanList = new ArrayList<>();
        List<GoodBean> goodList = Constant.localOrderBean.getGoodList();
        if (goodList != null) {
            for (int i = 0; i < goodList.size(); i++) {
                if (goodList.get(i).getGoodsNum() > 0) {
                    CommitOrderBean commitOrderBean = new CommitOrderBean();
                    commitOrderBean.setId(goodList.get(i).getGoodsId());
                    commitOrderBean.setNum(goodList.get(i).getGoodsNum());
                    commitOrderBean.setSize_id(goodList.get(i).getSizeId());
                    commitOrderBeanList.add(commitOrderBean);
                }
            }
        }
        return new Gson().toJson(commitOrderBeanList);
    }


    //----------------------------------------------------------称重相关----------------------------------------------------------

    RelativeLayout rl_weigh_price;
    LinearLayout ll_count_result,ll_no_weigh,ll_bottom_btn;
    TextView tv_single_price,tv_weigh,tv_good_total,tv_no,tv_yes,tv_weighgood_name;
    ImageView iv_close;
    DecimalFormat priceFormat = new DecimalFormat("0.00");
    PopupWindow weighPop;
    String weighUnit;
    double simpePrice;
    private OnWeighGoodChangeListener onWeighGoodChangeListener=new OnWeighGoodChangeListener() {
        @Override
        public void weigh(GoodBean goodBean, int position,int gsPoi) {
            LogUtils.Log("position=="+position);
            weighUnit=goodBean.getGoodsUnit();
            String weighGoodName;
            if (showMemberPrice && goodBean.getVipPrice()>=0) {
                simpePrice = goodBean.getVipPrice();
            }else {
                simpePrice=goodBean.getGoodsPrice();
            }
            weighGoodName=goodBean.getGoodsName();
            showWeighPop(goodBean,weighGoodName,false,position,gsPoi);
        }
    };

    public void showWeighPop(final GoodBean goodBean, String goodsName, final boolean isDgg, final int position,final int gsPoi){
        if(Build.MODEL.contains("S2")) {

        }else {
            int weighState = WeighManager.getInstance().getWeighState();
            if(weighState!=2) {
                Utils.showToast(mActivity,"电子秤未连接",1500);
                return;
            }
        }
        View weighView= View.inflate(mActivity,R.layout.weigh_popview,null);
        rl_weigh_price= (RelativeLayout) weighView.findViewById(R.id.rl_weigh_price);//单价，数量
        ll_count_result= (LinearLayout) weighView.findViewById(R.id.ll_count_result);//总计金额
        ll_no_weigh= (LinearLayout) weighView.findViewById(R.id.ll_no_weigh);//未放置
        ll_bottom_btn= (LinearLayout) weighView.findViewById(R.id.ll_bottom_btn);//底部按钮
        tv_weighgood_name = (TextView) weighView.findViewById(R.id.tv_weighgood_name);
        tv_weigh_mode = (TextView) weighView.findViewById(R.id.tv_weigh_mode);

        tv_single_price =(TextView)weighView.findViewById(R.id.tv_single_price);
        tv_weigh =(TextView)weighView.findViewById(R.id.tv_weigh);
        tv_good_total =(TextView)weighView.findViewById(R.id.tv_good_total);

        tv_yes= (TextView) weighView.findViewById(R.id.tv_right);
        tv_center= (TextView) weighView.findViewById(R.id.tv_center);
        tv_no= (TextView) weighView.findViewById(R.id.tv_left);

        iv_close= (ImageView) weighView.findViewById(R.id.iv_close);
        tv_single_price.setText("￥"+simpePrice+"/"+goodBean.getGoodsUnit());
        tv_weighgood_name.setText("称重商品："+goodsName);

        if(Build.MODEL.contains("S2")) {
            iv_close.setVisibility(View.VISIBLE);
            tv_center.setVisibility(View.VISIBLE);
            tv_no.setText("置零");
        }else {
            if(!Constant.SAVED_WEIGH_NAME.contains(Constant.WEIGHER_NAME)) {
                tv_center.setVisibility(View.GONE);
                tv_no.setText("取消");
            }else {
                iv_close.setVisibility(View.VISIBLE);
                tv_center.setVisibility(View.VISIBLE);
                tv_no.setText("置零");
            }
        }


        weighPop = new PopupWindow(weighView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        weighPop.setBackgroundDrawable(new BitmapDrawable());
        weighPop.setTouchable(true);
        weighPop.setOutsideTouchable(true);
       /* int[] wh=new int[2];
        Utils.getRelativeWH(mActivity,1606,1588,wh);
        weighPop.setWidth(wh[0]);
        weighPop.setHeight(wh[1]);*/

        Utils.setAlpha(mActivity,0.4f);
        weighPop.showAtLocation(totalView, Gravity.CENTER, 0, 0);
        weighPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                LogUtils.e(TAG,"onDismiss()");
                Utils.setAlpha(mActivity,1);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(TAG,"Click Close icon");
                weighPop.dismiss();
            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.MODEL.contains("S2")) {
                    NoticePopuUtils.showBindDia(mActivity, "为保证称重准确，请保证称台上没有物品。", new NoticePopuUtils.OnClickCallBack() {
                        @Override
                        public void onClickYes() {
                            setZero();
                        }

                        @Override
                        public void onClickNo() {

                        }
                    });
                }else {
                    if(!Constant.SAVED_WEIGH_NAME.contains(Constant.WEIGHER_NAME)) {
                        weighPop.dismiss();
                    }else {//置零
                        NoticePopuUtils.showBindDia(mActivity, "为保证称重准确，请保证称台上没有物品。", new NoticePopuUtils.OnClickCallBack() {
                            @Override
                            public void onClickYes() {
                                setZero();
                            }

                            @Override
                            public void onClickNo() {

                            }
                        });
                    }
                }
            }
        });
        tv_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tare();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weigh = tv_weigh.getText().toString();
                double weiAcc=0;
                try{
                    weiAcc= "g".equals(weighUnit) ? Double.valueOf(weigh.substring(0, weigh.indexOf("g"))) : Double.valueOf(weigh.substring(0, weigh.indexOf("kg")));
                }catch (Exception e){
                    Utils.showToast(mActivity,"数据异常！");
                    return;
                }
                scanAdd(goodBean,weiAcc,gsPoi);
                weighPop.dismiss();
            }
        });
    }

    private void onClickWeighTitle() {

        setZeroDia = NoticePopuUtils.showSetZero(mActivity, "如需置零，请移开电子秤上的物品，并点下方“置零”按钮。", new NoticePopuUtils.ThreeOnClickCallBack() {
            @Override
            public void onClickLeft() {

            }
            @Override
            public void onClickCenter() {//去皮
                tare();
            }

            @Override
            public void onClickRight() {//置零
                setZero();
            }
        });
        upDateZeroDia(null);
    }

    private void tare() {
        if(Build.MODEL.contains("S2")) {
            try {
                ((MainActivity)mActivity).scaleManager.tare();
            }catch (Exception e){
                LogUtils.d(TAG,"s2电子秤tare异常 e="+e.toString());
            }
        }else {
            if(TextUtils.equals("-",weigh_zf)&&TextUtils.equals("GS",weigh_mode)) {
                Utils.showToast(mActivity,mActivity.getResources().getString(R.string.tare_weigh_error),4000);
            }else if(TextUtils.isEmpty(weigh_zf)) {
            }else {
                WeighManager.getInstance().resetWeigher(WeighManager.TARE);
            }
        }

    }
    private void setZero() {
        if(Build.MODEL.contains("S2")) {
            try {
                ((MainActivity)mActivity).scaleManager.zero();
            }catch (Exception e){
                LogUtils.d(TAG,"s2电子秤zero异常 e="+e.toString());
            }
        }else {
            double currentWeigh=0;
            if(TextUtils.equals("g",weigh_unit)) {
                currentWeigh=weigh_acc/1000;
            }else if(TextUtils.equals("kg",weigh_unit)) {
                currentWeigh=weigh_acc;
            }
            if(TextUtils.equals("NT",weigh_mode)) {
                Utils.showToast(mActivity,mActivity.getResources().getString(R.string.zero_weigh_mode_error),5000);
            }else if(currentWeigh>=0.6) {
                Utils.showToast(mActivity,mActivity.getResources().getString(R.string.zero_weigh_outof_range),5000);
            }else if(TextUtils.isEmpty(weigh_mode)) {
            }else {
                WeighManager.getInstance().resetWeigher(WeighManager.ZERO);
            }
        }

    }

    private void upDateZeroDia(String str) {
        if(setZeroDia!=null&&setZeroDia.isShowing()) {
            if(str!=null) {
                NoticePopuUtils.upDateWeigh(str);
            }else {
                double tempWeigh=weigh_acc;
                if(TextUtils.equals("g",weigh_unit)) {
                    tempWeigh=weigh_acc/1000;
                }
                if(TextUtils.equals(weigh_zf,"+")) {
                    NoticePopuUtils.upDateWeigh(tempWeigh+"kg");
                }else {
                    NoticePopuUtils.upDateWeigh(weigh_zf+tempWeigh+"kg");
                }
            }
        }
    }

    /**
     *
     * @param weigh
     * @param net 净重
     * @param tare 皮重
     * @param status 0：动态  1：稳定
     */
    private void updataWeighPup(String weigh,double net,double tare,int status) {
        //ST NT 0000.204kg
        //ST NT 00000204 g
        //ragment: net=1398  tare=0  status=1

        try{
            if(Build.MODEL.contains("S2")) {
                rl_weigh_price.setVisibility(View.VISIBLE);
                ll_count_result.setVisibility(View.VISIBLE);
                ll_bottom_btn.setVisibility(View.VISIBLE);
                if(tare>0) {
                    tv_weigh_mode.setText("重量(净重)");
                }else {
                    tv_weigh_mode.setText("重量(毛重)");
                }
                if("g".equals(weighUnit)) {
                    tv_weigh.setText(net+"g");
                    if(net>0) {
                        tv_good_total.setText(getString(R.string.money_lgo)+priceFormat.format(net * simpePrice));
                    }else {
                        tv_good_total.setText(getString(R.string.money_lgo)+"0.00");
                    }
                }else {
                    tv_weigh.setText(net/1000+"kg");
                    if(net>0) {
                        tv_good_total.setText(getString(R.string.money_lgo)+priceFormat.format((net/1000) * simpePrice));
                    }else {
                        tv_good_total.setText(getString(R.string.money_lgo)+"0.00");
                    }
                }

                if(status==0) {
                    tv_good_total.setTextColor(Color.parseColor("#33ff5500"));
                    tv_weigh.setTextColor(Color.parseColor("#33333333"));
                    tv_yes.setEnabled(false);
                }else {
                    tv_good_total.setTextColor(Color.parseColor("#ff5500"));
                    tv_weigh.setTextColor(Color.parseColor("#333333"));
                    tv_yes.setEnabled(true);
                }

            }else if(weigh_version==1) {
                if(TextUtils.equals("NT",weigh_mode)) {
                    tv_weigh_mode.setText("重量(净重)");
                }else if(TextUtils.equals("GS",weigh_mode)) {
                    tv_weigh_mode.setText("重量(毛重)");
                }else {
                    tv_weigh_mode.setText("重量");
                }
                rl_weigh_price.setVisibility(View.VISIBLE);
                ll_count_result.setVisibility(View.VISIBLE);
                ll_bottom_btn.setVisibility(View.VISIBLE);
                Double tempWeigh=weigh_acc;
                if("g".equals(weighUnit)) {
                    tv_weigh.setText(weigh_zf+weigh_acc+"g");
                }else {
                    tv_weigh.setText(weigh_zf+weigh_acc/1000+"kg");
                    tempWeigh=weigh_acc/1000;
                }
                if(TextUtils.equals("+",weigh_zf)) {
                    tv_good_total.setText(getString(R.string.money_lgo)+priceFormat.format(tempWeigh * simpePrice));
                }else {
                    tv_good_total.setText(getString(R.string.money_lgo)+weigh_zf+priceFormat.format(tempWeigh * simpePrice));
                }
            }else {
                Double doubleWeigh = Double.valueOf(weigh);
                if(doubleWeigh<=0) {
                    ll_no_weigh.setVisibility(View.VISIBLE);
                    rl_weigh_price.setVisibility(View.GONE);
                    ll_count_result.setVisibility(View.GONE);
                    ll_bottom_btn.setVisibility(View.GONE);
                    iv_close.setVisibility(View.VISIBLE);
                }else {
                    ll_no_weigh.setVisibility(View.GONE);
                    rl_weigh_price.setVisibility(View.VISIBLE);
                    ll_count_result.setVisibility(View.VISIBLE);
                    ll_bottom_btn.setVisibility(View.VISIBLE);
                    iv_close.setVisibility(View.GONE);
                    if("g".equals(weighUnit)) {
                        tv_weigh.setText(doubleWeigh*1000+"g");
                        doubleWeigh=doubleWeigh*1000;
                    }else {
                        tv_weigh.setText(doubleWeigh+"kg");
                    }
                    tv_good_total.setText(getString(R.string.money_lgo)+priceFormat.format(doubleWeigh * simpePrice));
                }
            }
        }catch (Exception e){
            tv_weigh.setText(weigh);
            return;
        }
    }
}
