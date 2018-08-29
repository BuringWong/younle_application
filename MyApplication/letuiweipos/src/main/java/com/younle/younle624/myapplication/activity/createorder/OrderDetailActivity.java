package com.younle.younle624.myapplication.activity.createorder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PayQRCodeActivity;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PosOrderActivity;
import com.younle.younle624.myapplication.activity.pos.CollectionFailActivity;
import com.younle.younle624.myapplication.activity.pos.CollectionSuccessActivity;
import com.younle.younle624.myapplication.adapter.NumericWheelAdapter;
import com.younle.younle624.myapplication.adapter.OnWheelChangedListener;
import com.younle.younle624.myapplication.adapter.ReservationAdapter;
import com.younle.younle624.myapplication.adapter.ResumeDetailAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.ModifyTimeBean;
import com.younle.younle624.myapplication.domain.PayCardinfoBean;
import com.younle.younle624.myapplication.domain.ResultQueryBean;
import com.younle.younle624.myapplication.domain.SavedFailOrder;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.domain.orderbean.DetailMemberBean;
import com.younle.younle624.myapplication.domain.orderbean.DetailPosCashVoucherBean;
import com.younle.younle624.myapplication.domain.orderbean.DetailPosDiscountVoucherBean;
import com.younle.younle624.myapplication.domain.paybean.PayParams;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.domain.printsetting.YunPrintGroupBean;
import com.younle.younle624.myapplication.domain.waimai.WmPintData;
import com.younle.younle624.myapplication.myservice.BluetoothService;
import com.younle.younle624.myapplication.utils.CashCardPayUtils;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.GetTimeUtil;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.format.FormatUtils;
import com.younle.younle624.myapplication.utils.notice.NoticePopuUtils;
import com.younle.younle624.myapplication.utils.printmanager.BTPrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.PrintDia;
import com.younle.younle624.myapplication.utils.printmanager.PrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.YunPrintUtils;
import com.younle.younle624.myapplication.view.ArrayWheelAdapter;
import com.younle.younle624.myapplication.view.SelfImageView;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.younle.younle624.myapplication.view.WheelView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;
import zxing.activity.CaptureActivity;

/**
 * 订单提交成功，展示本次订单明细
 */
public class OrderDetailActivity extends Activity implements View.OnClickListener, SelfLinearLayout.ClickToReload {
    private String TAG = "OrderDetailActivity";
    private final int UPDATE_PRINT_TIME = 2;
    private final int MEMBER_PAY = 3;
    private final int CLICK_VIEW_START = 5;
    private final int CLICK_VIEW_END = 6;
    private static final int CUT_DOWN_TIMMER = 7;
    private static final int QUERY_ORDER_STATE = 10;
    private final int PAY_FLAG_QERY_PAY_STATE = 14;
    private final int CLOSE_THIS_ACTIVITY = 101;//不可更改
    private  final int WAIT_BLUETOOTH_OPEN = 15;
    private final int REFRESH_PRICE = 11;
    private static final int UNPAY_ORDER_DETAIL = 13;
    private int refresh_price_timmer = 60;
    private int yun_print_index;
    /**
     * true时不需要刷新，false需要刷新
     */
    private boolean price_timmer_isend = false;
    private ListView lv_order_details;
    private UnPayDetailsBean orderBean;
    private TextView tv_total_money;
    private TextView member_discount;
    private TextView tv_member_discount;
    private TextView tv_discount_total_money;
    private TextView tv_title;
    private ImageView iv_title;
    private ResumeDetailAdapter adapter;
    private AlertDialog alertDialog;
    private View footView;
    private View headView;
    private TextView change_order_money;
    /**
     * 备注填写入口
     */
    private TextView tv_mark_state;
    private LinearLayout ll_remark;
    private TextView tv_mark_msg;
    private TextView tv_mark_state_no_goods;
    private LinearLayout ll_remark_no_goods;
    private TextView tv_mark_msg_no_goods;

    private EditText et_new_price;
    private TextView btn_finish_change;
    private TextView btn_cancel_change;
    private AlertDialog changePriceDialog;
    private TextView tv_change_room;
    private TextView tv_add_entity_service;
    private TextView tv_minus_entity_service;
    private TextView tv_confirm_modify;
    private TextView tv_cancel_radius_goods;
    private View order_dotted_line1;
    private View order_dotted_line2;
    private LinearLayout all_goods_desc_head;
    private LinearLayout rl_current_room_resume;
    private TextView tv_current_room;
    private TextView tv_order_detail_name;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private RelativeLayout all_room_info;
    private LinearLayout rl_start;
    //private RelativeLayout rl_end;
    private TextView tv_time_choose_title;
    private int clickView;
    private String ymdData[] = new String[7200];
    private WheelView wl_ymd;
    private WheelView wl_week;
    private WheelView wl_hour;
    private WheelView wl_min;
    private WheelView wl_ymd_end;
    private WheelView wl_week_end;
    private WheelView wl_hour_end;
    private WheelView wl_min_end;
    private AlertDialog wheelChooseDialog;
    private TextView tv_left_printtime;
    private int recLen = Constant.RECLEN_TIME;
    private List<WmPintData> oneByOneList;
    private List<WmPintData> oneByOneBtList;

    String[] week_str =
            {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    String[] xiaoshi_start =
            {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                    "19", "20", "21", "22", "23"};

    String[] fenzhong_start =
            {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                    "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
                    "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
                    "55", "56", "57", "58", "59"};
    String lastweek = "周一";
    //最新的小时和分钟
    private String currentHour_start = "00";
    private String currentMin_start ="00";
    private String currrentYMD_start;//2016年11月23日
    private String currentHour_end = "00";
    private String currentMin_end = "00";
    private String currrentYMD_end;//2016年11月23日
    private LinearLayout ll_used_room;
    private TextView tv_useing_time;
    private TextView tv_current_resume;
    private View line2;
    private View line4;
    private View line5;
    private SelfImageView iv_transfer;
    private Button btn_cancel_order;
    private Button btn_print_order;
    private Button btn_finish_order;
    private TextView tv_current_money;
    private EditText et_modify_price_reason;
    private PopupWindow popupWindow;
    private String unpay_order_no = "";
    private String room_id_room = "";
    public ImageView iv_jiazai_filure;
    public SelfLinearLayout ll_loading;
    public RelativeLayout rl_no_room_goods;
    public LinearLayout ll_no_goods_has_rooms;
    public LinearLayout ll_discount_member_no_goods;
    public RelativeLayout rl_member_discount_no_goods;
    public ImageView iv_pic_card_coupons_no_goods;
    public TextView tv_member_discount_no_goods;
    public TextView tv_reduce_money_no_goods;
    public TextView tv_order_detail_name_no_goods;
    public View order_line1_no_goods;
    public RelativeLayout all_room_info_no_goods;
    public TextView tv_current_room_no_goods;
    public TextView tv_change_room_no_goods;
    public View order_line2_no_goods;
    public LinearLayout rl_start_no_goods;
    //public TextView tv_start_no_goods;
    public TextView tv_start_time_no_goods;
    public View order_line3_no_goods;
    //public RelativeLayout rl_end_no_goods;
    //public TextView tv_end_no_goods;
    public TextView tv_end_time_no_goods;
    //public View order_line4_no_goods;
    public TextView tv_useing_time_no_goods;
    public TextView tv_current_resume_no_goods;
    public TextView tv_add_entity_service_no_goods;
    public LinearLayout ll_used_room_no_goods;
    public TextView tv_total_money_no_goods;
    public TextView member_discount_no_goods;
    public TextView tv_discount_total_money_no_goods;
    public TextView change_order_money_no_goods;
    public TextView tv_order_no_show_goods_no_goods;
    public TextView tv_tv_order_time_show_goods_no_goods;
    public TextView tv_order_no_show_goods;
    public TextView tv_tv_order_time_show_goods;
    private TextView tv_reduce_money;
    public ImageView iv_pic_card_coupons;
    public RelativeLayout rl_member_discount;
    public LinearLayout ll_consume_explain;
    public TextView tv_consume_explain;
    public TextView tv_loading;
    public ProgressBar pb_loading;
    public String querryStartTime = "0";
    public String querryEndTime = "0";
    private long now_time = Utils.getCurrentTimeMill();
    public boolean querryTimeReturn;
    private final int NO_PAY_RESULT = 0;
    private final int MAY_PAY_SUCCESS = 1;
    private String payment = "0";//最终使用这个钱数支付
    private String order_total_fee;//订单总金额
    private String trade_num;
    private String query_num = "0";
    private String trade_time;
    private boolean isModifyGoods = false;
    private String room_name = "无";
    private float top_distance = 0;
    private float show_pos = 0;
    private int charge_model = 0;
    /**
     * 顶部会员信息信息
     */
    private TextView tv_mark_reback;
    private LinearLayout ll_member_info, al_member_left;
    private LinearLayout ll_consume_explain_no_goods;
    private TextView tv_consume_explain_no_goods;
    private TextView member_name;
    //private ImageView member_icon;
    //会员账户余额
    private TextView tv_member_left;
    //上月到店次数
    private TextView tv_come_times;
    //会员平均消费
    private TextView member_average_consume,member_fav;
    private NetWorks netWorks;
    /**
     * true:第一次打印完成的callback
     * false:不是第一次打印完成的callback
     */
//    private boolean contains;
    private TextView tv_cout_timmer;
    private PopupWindow waittingPop;
    private boolean isTimeGone;
    private boolean isClickCancel;
    private List<String> GroupBtheadData = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.ORDER_DETAIL_CHANGE_TOTALFEE:
                    //重新计算总价格
                    double total_fee = 0;
                    List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_entity = orderBean.getMsg().getOrder_goods();
                    if (order_entity != null && order_entity.size() > 0) {
                        for (int i = 0; i < order_entity.size(); i++) {
                            double v = order_entity.get(i).getGoods_num() * order_entity.get(i).getGoods_price();
                            total_fee += v;
                        }
                    }
                    List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms = orderBean.getMsg().getOrder_rooms();
                    if(order_rooms!=null && order_rooms.size()>0){
                        for (int i = 0; i < order_rooms.size(); i++) {
                            double price = Double.valueOf(order_rooms.get(i).getTrue_income());
                            total_fee += price;
                        }
                    }
                    payment = "" + total_fee;
                    order_total_fee = "" + total_fee;
                    tv_total_money.setText("总金额：￥" + Utils.keepTwoDecimal("" + total_fee));
                    //获取会员折扣:设置折扣显示和折扣后金额
                    tv_discount_total_money.setText("￥" + Utils.keepTwoDecimal("" + total_fee));
                    break;
                case UPDATE_PRINT_TIME://等待打印下联
                    leftTime--;
                    if(tv_left_printtime!=null){
                        tv_left_printtime.setText(leftTime + "秒后自动打印第二联");
                    }else{
                        LogUtils.e(TAG,"tv_left_printtime==null...");
                    }
                    if (leftTime <= 0) {
                        handler.removeMessages(UPDATE_PRINT_TIME);
                        if (waitPrintSecDia != null && waitPrintSecDia.isShowing()) {
                            waitPrintSecDia.dismiss();
                        }
                        printSecond();
                    } else {
                        handler.sendEmptyMessageDelayed(UPDATE_PRINT_TIME, 1000);
                    }
                    break;
                case CUT_DOWN_TIMMER:
                    cutDownTimer();
                    break;
                case QUERY_ORDER_STATE:
                    queryOrderState();
                    break;
                case CLOSE_THIS_ACTIVITY:
                    finish();
                    break;
                case REFRESH_PRICE:
                    refresh_price_timmer--;
                    if(refresh_price_timmer<=0){//60秒结束，需要刷新
                        price_timmer_isend = false;
                        return;
                    }
                    handler.sendEmptyMessageDelayed(REFRESH_PRICE,1000);
                    break;
                case Constant.WAIT_BLUETOOTH_OPEN://等待蓝牙打开
                    BTPrintUtils.getInstance().connectBtPrinterTest(mService,OrderDetailActivity.this, handler);
                    break;
                case Constant.MESSAGE_STATE_CHANGE:
                    LogUtils.e(TAG, "收到了连接状态变更的消息");
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(OrderDetailActivity.this, "已连接", Toast.LENGTH_SHORT).show();
                            LogUtils.e(TAG,"已连接");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(OrderDetailActivity.this, "正在连接", Toast.LENGTH_SHORT).show();
                            LogUtils.e(TAG, "正在连接");

                            break;
                        case BluetoothService.STATE_LISTEN:
                            LogUtils.e(TAG, "监听中");
                            break;
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(OrderDetailActivity.this, "无连接", Toast.LENGTH_SHORT).show();
                            LogUtils.e(TAG, "无连接");

                            break;
                    }
                    break;
               /* case Constant.ONE_BY_ONE_YUN_PRINT:
                    yun_print_index++;
                    if(yun_print_index< oneByOneYunList.size()) {
                        startYunPrint();
                    }else {
                        yun_print_index=0;
                    }
                    break;*/
            }
        }
    };
    private String memberFreeMoney;
    private String btPrintItems;
    private boolean isModifyEndTime;
    private TextView tv_countway_notice;
    private TextView tv_change_countway;
    private int accuracy;
    private TextView tv_unit;
    private TextView tv_after_discount;
    private String afterGj="";
    private String cardinfo_pay = "";
    /**
     * 会员价所产生的优惠
     */
    private LinearLayout footer_orgin_member_price,orgin_member_price;
    private TextView footer_member_discount_acc,member_discount_acc;
    private TextView footer_orign_price,orign_price;
    private boolean orderContainsMember;
    private ImageView member_price_logo,member_price_logo_no_goods;
    private boolean containsVoucher;
    private List<YunPrintGroupBean> oneByOneYunList;
    private String printItems;
    private List<WmPintData> btPrintData;
    private List<WmPintData> printData;
    private List<YunPrintGroupBean> yunPrintGroupBeans;
    private List<YunPrintGroupBean> yunNormalGroupBeans;
    private List<SavedPrinter> savedPrinterses;

    private void queryOrderState() {
        LogUtils.e(TAG, "轮询：recLen:" + recLen);
        LogUtils.e(TAG, "轮询：trade_num:" + trade_num);
        if (!isClickCancel) {
            if (recLen < Constant.RECLEN_TIME&&1 < recLen) {
                netWorks.queryPayState("0",trade_num, netCallBack, PAY_FLAG_QERY_PAY_STATE);
            }
        }
    }

    private void toFailActivity() {
        Intent intent;
        handler.removeCallbacksAndMessages(null);
        intent = new Intent(OrderDetailActivity.this, CollectionFailActivity.class);
        intent.putExtra("tradenum", trade_num);
        intent.putExtra("failreason", failreason);
        intent.putExtra("solveway", solveway);
        intent.putExtra("totalfee", orderBean.getMsg().getTotal_fee() + "");
        intent.putExtra("payment", payment);
        intent.putExtra("tradetime", trade_time);
        intent.putExtra(Constant.UNPAY_ORDER_ID, unpay_order_no);
        //intent.putExtra("querynum", Utils.getBQueryNum(this));//每一个本地querynum都只能在即将存储的时候获取 不可提前获取
        intent.putExtra("vip_card", memberBean.getMsg().getVipcardid());
        intent.putExtra(Constant.FROME_WHERE, Constant.ORDER_PAGER_DETAIL);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        intent.putExtra("cardinfo_pay",cardinfo_pay);
        startActivity(intent);
    }

    private void toSuccessActivity() {
        Intent intent;
        handler.removeCallbacksAndMessages(null);
        intent = new Intent(OrderDetailActivity.this, CollectionSuccessActivity.class);
        if ("".equals(Constant.LAST_ORDER_NO)) {
            intent.putExtra("tradenum", trade_num);
        } else {
            intent.putExtra("tradenum", Constant.LAST_ORDER_NO);
        }
        if(memberFreeMoney!=null) {
            intent.putExtra(Constant.MEMBER_FREE_MONEY,memberFreeMoney);
        }
        intent.putExtra("totalfee", orderBean.getMsg().getTotal_fee());
        intent.putExtra("payment", orderBean.getMsg().getPayment());
        intent.putExtra("tradetime", trade_time);
        intent.putExtra("querynum", query_num);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        intent.putExtra("cardinfo_pay",cardinfo_pay);
        startActivity(intent);
        finish();
    }

    private int leftTime;
    private IWoyouService iWoyouService;
    private ICallback callBack1 = new ICallback.Stub() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {
            if (isSuccess) {//第一联打印成功
               /* if(fromFirstComit) {
                    fromFirstComit=false;
                    printSecond();
                }else {*/
                    leftTime = 5;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isCurrentActivityTop()) {
                                showPrintSecondDia();
                                handler.sendEmptyMessageDelayed(UPDATE_PRINT_TIME, 1000);
                            } else {
                                printSecond();
                            }
                        }
                    });
//                }
            }
        }

        @Override
        public void onReturnString(String result) throws RemoteException {

        }

        @Override
        public void onRaiseException(int code, String msg) throws RemoteException {

        }
    };

    private ICallback printCallBack2 = new ICallback.Stub() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {
            if (isSuccess) {//第二联打印成功
               dismisPrintingDia();
            }
        }

        @Override
        public void onReturnString(String result) throws RemoteException {
            LogUtils.Log("onReturnString:" + result);
        }

        @Override
        public void onRaiseException(int code, String msg) throws RemoteException {
            LogUtils.Log("onRaiseException:" + msg);
        }
    };
    public ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWoyouService = IWoyouService.Stub.asInterface(service);//拿到打印服务的对象
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    private boolean show_dialog = false;
    private AlertDialog waitPrintSecDia;
    private DetailMemberBean memberBean;
    private DetailPosDiscountVoucherBean posDiscountVoucherBean;
    private DetailPosCashVoucherBean posCashVoucherBean;
    /**
     * 蓝牙打印
     */
    private BluetoothService mService;

    /**
     * 用于获取绑有会员的订单的未结账订单信息所包含的会员折扣信息
     */
    private String vipcreate_id = "0";
    private NetWorks.OnNetCallBack netCallBack = new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            switch (flag) {
                case MEMBER_PAY://会员支付
                    dealpayError(e);
                    break;
                case PAY_FLAG_QERY_PAY_STATE://查询订单状态
                    dealQueryError(e);
                    break;
                case UNPAY_ORDER_DETAIL://未支付订单详情
                    LogUtils.e(TAG,"Exception e="+e);
                    netError();
                    break;
            }
        }

        @Override
        public void onResonse(String response, int flag) {
            switch (flag) {
                case MEMBER_PAY://会员支付
                    dealpayRes(response);
                    break;
                case PAY_FLAG_QERY_PAY_STATE://查询订单状态
                    dealQueryRes(response);
                    break;
                case UNPAY_ORDER_DETAIL://未支付订单详情
                    LogUtils.Log("未支付订单详情response=" + response);
                    parseJson(response);
                    //LogUtils.e(TAG, "test response=" + Constant.res);
                    //parseJson(Constant.DETAIL_GOODS_DATA);
                    break;
            }
        }
    };

    private void dealQueryRes(String response) {
        LogUtils.e(TAG, "轮询：response" + response);
        if (recLen > 1&&recLen<Constant.RECLEN_TIME) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 200) {//弹出成功页面
                    ResultQueryBean resultQueryBean = new Gson().fromJson(response, ResultQueryBean.class);
                    if ("0".equals(resultQueryBean.getMsg().getSuccess())) {//等待支付
                        handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
                    } else if ("1".equals(resultQueryBean.getMsg().getSuccess())) {//确定成功
                        dismissWaittingPopWindow();
                        toSuccessActivity();
                    } else if ("2".equals(resultQueryBean.getMsg().getSuccess())) {//确认失败
                        //弹出失败页面
                        dismissWaittingPopWindow();
                        toFailActivity();
                    }
                } else {
                    //轮训查直到到成功为止
                    handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String failreason = "收款失败";
    private String solveway = "请重新发起收款";
    private void dealpayRes(String response) {
        LogUtils.e(TAG, "会员支付：" + response);
        if (!isClickCancel && !isTimeGone) {//未撤销并且，倒计时并未到时间
            dismissWaittingPopWindow();
            try {
                JSONObject payResponse = new JSONObject(response);
                int mCode = payResponse.getInt("code");
                if (30000 == mCode) {//支付失败
                    LogUtils.e(TAG, "支付失败mCode.equals(\"FAIL\")进入收款失败界面");
                    failreason = payResponse.getString("code_des");
                    solveway = payResponse.getString("solution");
                    toFailActivity();
                } else if (200 == mCode) {
                    LogUtils.e(TAG, "支付成功mCode.equals(\"SUCCESS\")");
                    if("4".equals(Constant.payway)) {
                        memberFreeMoney = payResponse.getString("freemoney");
                        if(payResponse.getString("query_num")!=null){
                            query_num = payResponse.getString("query_num");
                        }
                    }
                    toSuccessActivity();
                }
            } catch (JSONException e) {
                LogUtils.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
    }

    /**
     * 支付异常
     * @param e
     */
    private void dealpayError(Exception e) {
        LogUtils.e(TAG, "dealpayError()-onError()===" + e + "----e.getMessage()===" + e.getMessage());
        if(!isClickCancel&&!isTimeGone&&recLen<Constant.RECLEN_TIME){
            if (1 < recLen) {
                handler.sendEmptyMessage(QUERY_ORDER_STATE);
            } else {
                showSeeResultPopWindow(NO_PAY_RESULT);
            }
        }
    }

    /**
     * 查询订单返回error
     * @param e
     */
    private void dealQueryError(Exception e) {
        //轮训查出来成功为止
        LogUtils.e(TAG, "轮询：Exception e=" + e);
        if (recLen < Constant.RECLEN_TIME&&1 < recLen) {
            handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.this_order_detail_for_card_coupons);
        EventBus.getDefault().register(this);
        Constant.IS_CLOSE_DETAIL_ACTIVITY = false;
        memberBean = (DetailMemberBean) getIntent().getSerializableExtra(Constant.MEMBER_BEAN);
        if(getIntent().getLongExtra("first_come_time",0)>0){
            now_time = getIntent().getLongExtra("first_come_time",0);
        }
        btPrintItems = SpUtils.getInstance(this).getString(Constant.bt_print_permission, "");
        printItems = SpUtils.getInstance(this).getString(Constant.print_permission, "");

        mService=BluetoothService.getInstance();
        mService.setHandler(handler);

        netWorks = new NetWorks(this);
        initView();
        initData();
//        mService=  BTPrintUtils.getInstance().connectBtPrinter(this, handler);
        PrintUtils.getInstance().initService(this, connService);
        setListener();
        initChooseTime();
    }

    @Override
    public void ClickToReload() {
        initData();
        reLoad();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        show_dialog = getIntent().getBooleanExtra("show_dialog", false);
        if(show_dialog){
            show_dialog = false;
            Utils.showWaittingDialog(this,"正在修改中...");
        }
        LogUtils.e(TAG, "onNewIntent()执行");
        //initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        handler.removeMessages(REFRESH_PRICE);
        refresh_price_timmer = 60;
        price_timmer_isend = true;
        handler.sendEmptyMessageDelayed(REFRESH_PRICE, 1000);
        unpay_order_no = getIntent().getStringExtra("unpay_order_id");
        if(getIntent().getStringExtra("room_id_room")!=null){
            room_id_room = getIntent().getStringExtra("room_id_room");
        }
        Constant.localOrderBean.setPrimaryKeyId(unpay_order_no);
        getUnpayOrderDetails();
    }

    private void initChooseTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long currentTime1 = System.currentTimeMillis();
                long day = 24 * 60 * 60 * 1000;
                long lastYear = currentTime1 - day * 360;
                for (int i = 0; i < 7200; i++) {
                    long time = lastYear + day * i;
                    ymdData[i] = GetTimeUtil.getYMDTime(time);
                }
            }
        }).start();
    }


    /**
     * 提示打印第二联的dialog
     */
    private void showPrintSecondDia() {
        tv_left_printtime = new TextView(this);
        tv_left_printtime.setTextColor(Color.BLACK);
        tv_left_printtime.setTextSize(Utils.dip2px(this, 12));
        tv_left_printtime.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        tv_left_printtime.setHeight(Utils.dip2px(this, 80));
        tv_left_printtime.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        tv_left_printtime.setText(leftTime + "秒后自动打印第二联");
        waitPrintSecDia = PrintDia.getInstance().showPrintSecondDia(this, tv_left_printtime, "立即打印", "无需打印", new PrintDia.OnHandleListener() {
            @Override
            public void onPositveButton() {
                handler.removeMessages(UPDATE_PRINT_TIME);
                leftTime = -1;
                printSecond();
            }

            @Override
            public void onNegativeButton() {
                handler.removeMessages(UPDATE_PRINT_TIME);
                leftTime = -1;
                dismisPrintingDia();
            }
        });
    }

    private void printSecond() {
        PrintUtils.getInstance().printWm(iWoyouService, oneByOneList, printCallBack2);
    }

    private void dismisPrintingDia() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    /**
     * 获取订单详情
     */
    private void getUnpayOrderDetails() {
        if (memberBean != null) {
            vipcreate_id = memberBean.getMsg().getVipcreate_id();
            if(vipcreate_id==null) {
                vipcreate_id="0";
            }
        }else {
            vipcreate_id="0";
        }
        netWorks.getUnpayOrderDetails(unpay_order_no, vipcreate_id, netCallBack, UNPAY_ORDER_DETAIL);
    }

    /**
     * 解析返回的数据
     * @param response
     */
    private void parseJson(String response) {
        Utils.dismissWaittingDialog();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                praseMemberInfo(response);
                Gson gson = new Gson();
                orderBean = gson.fromJson(response, UnPayDetailsBean.class);
                cardinfo_pay = getCardinfoJson();
                SpUtils.getInstance(this).save("order_details_res",response);
                //用于分订单的展示
                if (orderBean.getMsg().getOrder_rooms() != null && orderBean.getMsg().getOrder_rooms().size() > 0) {
                    Constant.CURRENT_ORDER_ROOM = orderBean.getMsg().getOrder_rooms().get(0).getRoomname();
                }else{
                    Constant.CURRENT_ORDER_ROOM = "";
                }
                Constant.CURRENT_ORDER_NUM = String.valueOf(orderBean.getMsg().getQuery_num());
                initPrintData();
                showData();
                loadingDismiss();
            } else {
                noData();
                Utils.showToast(this, "未支付订单详情数据请求失败，请检查网络后再试");
            }
        } catch (JSONException e) {
            LogUtils.e(TAG, "JSONException e=" + e);
        }
    }

    /**
     * 判断是否包含会员价相关的商品或房间
     * @param orderBean
     * @return
     */
    private boolean containsMember(UnPayDetailsBean orderBean) {
        List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms = orderBean.getMsg().getOrder_rooms();
        List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_goods = orderBean.getMsg().getOrder_goods();
        if(order_goods!=null&&order_goods.size()>0) {
            for (int i = 0; i < order_goods.size(); i++) {
                if(1==order_goods.get(i).getIs_vip()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 解析当期订单的会员信息
     * @param json
     */
    private void praseMemberInfo(String json) {
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
                showMemberInfo();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化打印数据
     */
    private void initPrintData() {
        btPrintData = PrintUtils.getInstance().formatPosOrder(this, orderBean, true);
        printData = PrintUtils.getInstance().formatPosOrder(this, orderBean, false);

        yunPrintGroupBeans = YunPrintUtils.formatPrePrintData(orderBean, false);
        yunNormalGroupBeans = YunPrintUtils.formatPrePrintData(orderBean,true);

//        PrintUtils.getInstance().firstComitFormatData(orderBean, headData, false, false);
//        PrintUtils.getInstance().firstComitFormatData(orderBean, btheadData, true, false);

//        oneByOneList=PrintUtils.getInstance().oneByOneData(orderBean,this,false);//一菜一单(蓝牙)
//        oneByOneBtList=PrintUtils.getInstance().oneByOneData(orderBean,this,true);//一菜一单(蓝牙)

        if (Constant.FIRST_COMMIT) {
            Constant.FIRST_COMMIT=false;
            //1.云打印
            startYunPrint(true);
            //2.第1次提交打印,本地打印机
            if(printItems.contains("1")) {
                localPrint();
            }
            if(btPrintItems.contains("1")) {
                BTPrintUtils.getInstance().wmBtPrint(mService, btPrintData,this,null);
            }
        }
    }

    private void localPrint() {
        if (iWoyouService != null) {
            if(printData !=null&&printData.size()>0) {
                PrintUtils.getInstance().newPosPrint(iWoyouService,printData,null);
            }
        }
    }

    /**
     * 开启云打印
     */
    private void startYunPrint(boolean printAuto) {
//        oneByOneYunList = YunPrintUtils.formateSplitOrderDetailData( headData, orderBean);
//        if(oneByOneYunList !=null&& oneByOneYunList.size()>0) {
//            YunPrintUtils.yunPrintOneByOneNew(this, oneByOneYunList,null,"3","2",printAuto,null);
//        }
        LogUtils.e(TAG,"yunPrint()");
        if(yunNormalGroupBeans.size()>0||yunPrintGroupBeans.size()>0) {
            savedPrinterses = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
            if(savedPrinterses!=null&&savedPrinterses.size()>0) {
                YunPrintUtils.yunPrintOneByOneNew(this,savedPrinterses,yunPrintGroupBeans,yunNormalGroupBeans,"3","2",printAuto,null);

            }
        }
    }

    private void initView() {
        ll_loading = (SelfLinearLayout) findViewById(R.id.ll_loading);
        iv_jiazai_filure = (ImageView) findViewById(R.id.iv_jiazai_filure);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        rl_no_room_goods = (RelativeLayout) findViewById(R.id.rl_no_room_goods);

        //会员价相关
        orign_price = (TextView)findViewById(R.id.orign_price);
        member_discount_acc = (TextView)findViewById(R.id.member_discount_acc);
        orgin_member_price = (LinearLayout) findViewById(R.id.orgin_member_price);
        member_price_logo_no_goods = (ImageView)findViewById(R.id.member_price_logo_no_goods);

        ll_discount_member_no_goods = (LinearLayout) findViewById(R.id.ll_discount_member_no_goods);
        rl_member_discount_no_goods = (RelativeLayout) findViewById(R.id.rl_member_discount_no_goods);
        iv_pic_card_coupons_no_goods = (ImageView) findViewById(R.id.iv_pic_card_coupons_no_goods);
        tv_member_discount_no_goods = (TextView) findViewById(R.id.tv_member_discount_no_goods);
        tv_reduce_money_no_goods = (TextView) findViewById(R.id.tv_reduce_money_no_goods);
        ll_no_goods_has_rooms = (LinearLayout) findViewById(R.id.ll_no_goods_has_rooms);
        ll_no_goods_has_rooms.setVisibility(View.GONE);
        //rl_current_room_resume_no_goods = (LinearLayout) findViewById(R.id.rl_current_room_resume_no_goods);
        tv_order_detail_name_no_goods = (TextView) findViewById(R.id.tv_order_detail_name_no_goods);
        all_room_info_no_goods = (RelativeLayout) findViewById(R.id.all_room_info_no_goods);
        tv_current_room_no_goods = (TextView) findViewById(R.id.tv_current_room_no_goods);
        tv_change_room_no_goods = (TextView) findViewById(R.id.tv_change_room_no_goods);
        rl_start_no_goods = (LinearLayout) findViewById(R.id.rl_start_no_goods);
        //tv_start_no_goods = (TextView) findViewById(R.id.tv_start_no_goods);
        tv_start_time_no_goods = (TextView) findViewById(R.id.tv_start_time_no_goods);
        //rl_end_no_goods = (RelativeLayout) findViewById(R.id.rl_end_no_goods);
        //tv_end_no_goods = (TextView) findViewById(R.id.tv_end_no_goods);
        tv_end_time_no_goods = (TextView) findViewById(R.id.tv_end_time_no_goods);
        //order_line4_no_goods = findViewById(R.id.order_line4_no_goods);
        tv_useing_time_no_goods = (TextView) findViewById(R.id.tv_useing_time_no_goods);
        tv_current_resume_no_goods = (TextView) findViewById(R.id.tv_current_resume_no_goods);

        ll_used_room_no_goods = (LinearLayout) findViewById(R.id.ll_used_room_no_goods);
        tv_add_entity_service_no_goods = (TextView) findViewById(R.id.tv_add_entity_service_no_goods);
        //et_please_enter_remarks_no_goods = (EditText) findViewById(R.id.et_please_enter_remarks_no_goods);
        tv_order_no_show_goods_no_goods = (TextView) findViewById(R.id.tv_order_no_show_goods_no_goods);
        tv_tv_order_time_show_goods_no_goods = (TextView) findViewById(R.id.tv_tv_order_time_show_goods_no_goods);
        order_line1_no_goods = findViewById(R.id.order_line1_no_goods);
        order_line2_no_goods = findViewById(R.id.order_line2_no_goods);
        order_line3_no_goods = findViewById(R.id.order_line3_no_goods);
        //order_line4_no_goods = findViewById(R.id.order_line4_no_goods);
        tv_total_money_no_goods = (TextView) findViewById(R.id.tv_total_money_no_goods);
        member_discount_no_goods = (TextView) findViewById(R.id.member_discount_no_goods);
        tv_discount_total_money_no_goods = (TextView) findViewById(R.id.tv_discount_total_money_no_goods);
        change_order_money_no_goods = (TextView) findViewById(R.id.change_order_money_no_goods);
        iv_transfer = (SelfImageView) findViewById(R.id.iv_transfer);
        iv_transfer.setAlpha(0.4f);
        lv_order_details = (ListView) findViewById(R.id.lv_order_details);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("订单详情");
        btn_cancel_order = (Button) findViewById(R.id.btn_cancel_order);
        btn_print_order = (Button) findViewById(R.id.btn_print_order);
        btn_finish_order = (Button) findViewById(R.id.btn_finish_order);
        initFootHeadView();
        lv_order_details.addFooterView(footView, null, false);
        lv_order_details.addHeaderView(headView, null, false);
        //会员信息控件
        ll_member_info = (LinearLayout) findViewById(R.id.ll_member_info);
        al_member_left = (LinearLayout)findViewById(R.id.al_member_left);
        tv_mark_reback = (TextView) findViewById(R.id.tv_mark_reback);
        tv_mark_reback.setVisibility(View.VISIBLE);
        tv_mark_reback.setText("识别会员/券");

        member_name = (TextView) findViewById(R.id.member_name);
        tv_member_left = (TextView) findViewById(R.id.tv_member_left);
        tv_come_times = (TextView)findViewById(R.id.tv_come_times);
        member_average_consume = (TextView) findViewById(R.id.member_average_consume);
        member_fav = (TextView)findViewById(R.id.member_fav);
        findViewById(R.id.tv_show_membertags).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> tags = memberBean.getMsg().getTags();
                if(tags!=null&&tags.size()>0) {
                    NoticePopuUtils.showMemberTagsPop(OrderDetailActivity.this,findViewById(R.id.arl_this_order_detail),tags);
                }else {
                    Utils.showToast(OrderDetailActivity.this,"该会员暂无标签",2000);
                }
            }
        });

        ll_consume_explain_no_goods = (LinearLayout) findViewById(R.id.ll_consume_explain_no_goods);
        tv_consume_explain_no_goods = (TextView) findViewById(R.id.tv_consume_explain_no_goods);
        tv_mark_state_no_goods = (TextView) findViewById(R.id.tv_mark_state_no_goods);
        ll_remark_no_goods = (LinearLayout) findViewById(R.id.ll_remark_no_goods);
        tv_mark_msg_no_goods = (TextView) findViewById(R.id.tv_mark_msg_no_goods);
    }

    /**
     * 展示会员信息
     */
    private void showMemberInfo() {
        ll_member_info.setVisibility(View.VISIBLE);
        member_name.setText(memberBean.getMsg().getName());
        member_average_consume.setText(memberBean.getMsg().getAverage() + "");
        tv_come_times.setText(memberBean.getMsg().getMonthnum());
        member_fav.setText(memberBean.getMsg().getTop5());
        if(memberBean.getMsg().isSupply()) {
            al_member_left.setVisibility(View.VISIBLE);
            tv_member_left.setText("￥"+memberBean.getMsg().getMoney());
        }else {
            al_member_left.setVisibility(View.GONE);
        }


        //x.image().bind(member_icon, memberBean.getMsg().getHeadimgurl());
        ll_discount_member_no_goods.setVisibility(View.VISIBLE);
        rl_member_discount_no_goods.setVisibility(View.VISIBLE);
        rl_member_discount.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化脚布局和头布局，包括总金额及四个button
     */
    private void initFootHeadView() {

        //1.脚布局
        footView = View.inflate(this, R.layout.order_detail_footview_for_card_coupons, null);
        footer_orgin_member_price = (LinearLayout) footView.findViewById(R.id.orgin_member_price);
        footer_member_discount_acc = (TextView) footView.findViewById(R.id.member_discount_acc);
        footer_orign_price = (TextView) footView.findViewById(R.id.orign_price);

        change_order_money = (TextView) footView.findViewById(R.id.change_order_money);
        tv_total_money = (TextView) footView.findViewById(R.id.tv_total_money);
        member_discount = (TextView) footView.findViewById(R.id.member_discount);
        tv_member_discount = (TextView) footView.findViewById(R.id.tv_member_discount);
        tv_discount_total_money = (TextView) footView.findViewById(R.id.tv_discount_total_money);
        //2.备注
        tv_mark_state = (TextView)footView.findViewById(R.id.tv_mark_state);
        tv_mark_msg = (TextView)footView.findViewById(R.id.tv_mark_msg);
        ll_remark = (LinearLayout)footView.findViewById(R.id.ll_remark);

        tv_reduce_money = (TextView) footView.findViewById(R.id.tv_reduce_money);
        tv_order_no_show_goods = (TextView) footView.findViewById(R.id.tv_order_no_show_goods);
        tv_tv_order_time_show_goods = (TextView) footView.findViewById(R.id.tv_tv_order_time_show_goods);
        iv_pic_card_coupons = (ImageView) footView.findViewById(R.id.iv_pic_card_coupons);
        rl_member_discount = (RelativeLayout) footView.findViewById(R.id.rl_member_discount);
        ll_consume_explain = (LinearLayout) footView.findViewById(R.id.ll_consume_explain);
        tv_consume_explain = (TextView) footView.findViewById(R.id.tv_consume_explain);

        //2.头布局
        headView = View.inflate(this, R.layout.this_order_detail_header_new, null);
        //add_good_service = (TextView) headView.findViewById(R.id.add_good_service);
        rl_current_room_resume = (LinearLayout) headView.findViewById(R.id.rl_current_room_resume);
        tv_change_room = (TextView) headView.findViewById(R.id.tv_change_room);
        tv_add_entity_service = (TextView) headView.findViewById(R.id.tv_add_entity_service);
        tv_minus_entity_service = (TextView) headView.findViewById(R.id.tv_minus_entity_service);
        tv_confirm_modify = (TextView) headView.findViewById(R.id.tv_confirm_modify);
        tv_cancel_radius_goods = (TextView) headView.findViewById(R.id.tv_cancel_radius_goods);
        order_dotted_line1 = headView.findViewById(R.id.order_dotted_line1);
        all_goods_desc_head = (LinearLayout) headView.findViewById(R.id.all_goods_desc_head);
        order_dotted_line2 = headView.findViewById(R.id.order_dotted_line2);
        member_price_logo = (ImageView) headView.findViewById(R.id.member_price_logo);

        all_room_info = (RelativeLayout) headView.findViewById(R.id.all_room_info);
        rl_start = (LinearLayout) headView.findViewById(R.id.rl_start);
        //rl_end = (RelativeLayout) headView.findViewById(R.id.rl_end);
        tv_order_detail_name = (TextView) headView.findViewById(R.id.tv_order_detail_name);//当前房间
        tv_current_room = (TextView) headView.findViewById(R.id.tv_current_room);//当前房间
        tv_start_time = (TextView) headView.findViewById(R.id.tv_start_time);//开始使用时间
        tv_end_time = (TextView) headView.findViewById(R.id.tv_end_time);//使用结束时间
        ll_used_room = (LinearLayout) headView.findViewById(R.id.ll_used_room);
        tv_useing_time = (TextView) headView.findViewById(R.id.tv_useing_time);//使用时长
        tv_current_resume = (TextView) headView.findViewById(R.id.tv_current_resume);//已经消费金额

        line2 = headView.findViewById(R.id.order_line2);
        //line3 = headView.findViewById(R.id.order_line3);
        line4 = headView.findViewById(R.id.order_line4);
        line5 = headView.findViewById(R.id.order_line5);
    }

    private void setListener() {
        change_order_money.setOnClickListener(this);
        iv_title.setOnClickListener(this);
        tv_change_room.setOnClickListener(this);
        tv_add_entity_service.setOnClickListener(this);
        tv_minus_entity_service.setOnClickListener(this);
        tv_confirm_modify.setOnClickListener(this);
        tv_add_entity_service_no_goods.setOnClickListener(this);
        tv_cancel_radius_goods.setOnClickListener(this);
        rl_start.setOnClickListener(this);
        //rl_end.setOnClickListener(this);
        btn_print_order.setOnClickListener(this);
        btn_cancel_order.setOnClickListener(this);
        btn_finish_order.setOnClickListener(this);
        ll_loading.setClickToReload(this);
        tv_mark_reback.setOnClickListener(this);
        tv_change_room_no_goods.setOnClickListener(this);
        change_order_money_no_goods.setOnClickListener(this);
        rl_start_no_goods.setOnClickListener(this);
        //rl_end_no_goods.setOnClickListener(this);
        tv_mark_state.setOnClickListener(this);
        tv_mark_state_no_goods.setOnClickListener(this);
        lv_order_details.setOnScrollListener(new AbsListView.OnScrollListener() {

            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                try {
                    mCurrentfirstVisibleItem = firstVisibleItem;
                    View firstView = view.getChildAt(0);
                    if (null != firstView) {
                        ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                        if (null == itemRecord) {
                            itemRecord = new ItemRecod();
                        }
                        itemRecord.height = firstView.getHeight();
                        itemRecord.top = firstView.getTop();
                        recordSp.append(firstVisibleItem, itemRecord);
                    }
                    show_pos = getScrollY();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private int getScrollY() {
                int height = 0;
                for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                    ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
                    height += itemRecod.height;
                }
                ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
                if (null == itemRecod) {
                    itemRecod = new ItemRecod();
                }
                return height - itemRecod.top;
            }

            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });
    }

    public void reLoad() {
        tv_loading.setText("拼命加载中...");
        pb_loading.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setVisibility(View.GONE);
        rl_no_room_goods.setVisibility(View.GONE);
    }

    public void noData() {
        rl_no_room_goods.setVisibility(View.VISIBLE);
    }

    public void netError() {
        //分订单提交以后dialog消失
        Utils.dismissWaittingDialog();
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.net_error);
        pb_loading.setVisibility(View.GONE);
        tv_loading.setText("请检查网络后，点击屏幕重新加载！");
        rl_no_room_goods.setVisibility(View.GONE);
    }

    public void loadingDismiss() {
        ll_loading.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void showData() {
        payment = "" + orderBean.getMsg().getPayment();
        order_total_fee = String.valueOf(orderBean.getMsg().getTotal_fee());
        //会员价减免的金额
        double memberPriceDiscount = orderBean.getMsg().getPrice() - orderBean.getMsg().getTotal_fee();
        LogUtils.Log("memberPriceDiscount:"+memberPriceDiscount);
        if(memberPriceDiscount>0) {//有会员价商品
            orderContainsMember=true;
            footer_orgin_member_price.setVisibility(View.VISIBLE);
            footer_member_discount_acc.setText("-￥"+Utils.keepTwoDecimal(memberPriceDiscount+""));
            footer_orign_price.setText("原价：￥"+orderBean.getMsg().getPrice()+"，会员优惠：");

            orgin_member_price.setVisibility(View.VISIBLE);
            member_discount_acc.setText("-￥"+Utils.keepTwoDecimal(memberPriceDiscount+""));
            orign_price.setText("原价：￥"+orderBean.getMsg().getPrice()+"，会员优惠：");
        }else {
            orderContainsMember=false;
            footer_orgin_member_price.setVisibility(View.GONE);
            orgin_member_price.setVisibility(View.GONE);
        }
        LogUtils.e(TAG, "payment=" + orderBean.getMsg().getPayment());
        LogUtils.e(TAG, "total_fee=" + orderBean.getMsg().getTotal_fee());
        LogUtils.e(TAG, "vip=" + orderBean.getMsg().getVip());
        LogUtils.e(TAG, "vip_discount=" + orderBean.getMsg().getVip_discount());

        //VIP折扣信息
        String vip_discount = orderBean.getMsg().getVip_discount();
        if (orderBean.getMsg().getOrder_rooms() != null || orderBean.getMsg().getOrder_goods() != null) {
            LogUtils.e(TAG, "设置adapter数据:布局头数据适配 " + orderBean.getMsg().getQuery_num() + "号订单");
            //1.订单编号
            tv_order_detail_name.setText(orderBean.getMsg().getQuery_num() + "号订单明细");
            //2.房间
            setRoomInfo();
            //3.底部总金额，订单id，折扣，
            setFootZkRemark(vip_discount);
            //4. 备注相关信息
            showOrderMarkInfo();
            //5.商品服务的处理
            showGoodServices(vip_discount);
        } else {
            noData();
        }
    }

    private void showGoodServices(String vip_discount) {
        if((orderBean.getMsg().getOrder_goods() != null && orderBean.getMsg().getOrder_goods().size()>0)){
            LogUtils.e(TAG, "setShowAdapter(false)");
            lv_order_details.setVisibility(View.VISIBLE);
            btn_print_order.setVisibility(View.VISIBLE);
            ll_no_goods_has_rooms.setVisibility(View.GONE);
            tv_add_entity_service.setVisibility(View.VISIBLE);
            tv_minus_entity_service.setVisibility(View.VISIBLE);
            order_dotted_line1.setVisibility(View.VISIBLE);
            all_goods_desc_head.setVisibility(View.VISIBLE);
            order_dotted_line2.setVisibility(View.VISIBLE);
            setShowAdapter(false);
        } else {
            lv_order_details.setVisibility(View.GONE);
            btn_print_order.setVisibility(View.GONE);
            tv_add_entity_service.setVisibility(View.GONE);
            tv_minus_entity_service.setVisibility(View.GONE);
            tv_confirm_modify.setVisibility(View.GONE);
            order_dotted_line1.setVisibility(View.GONE);
            all_goods_desc_head.setVisibility(View.GONE);
            order_dotted_line2.setVisibility(View.GONE);
            LogUtils.e(TAG, "没有商品有房间：处理listview的header不显示,并且显示预先写的有房间的布局");
            //没有商品有房间：处理listview的header不显示,并且显示预先写的有房间的布局。
            if (orderBean.getMsg().getOrder_rooms() != null && orderBean.getMsg().getOrder_rooms().size() > 0) {
                ll_no_goods_has_rooms.setVisibility(View.VISIBLE);
                tv_order_detail_name_no_goods.setText(orderBean.getMsg().getQuery_num() + "号订单明细");
                if("1".equals(orderBean.getMsg().getOrder_rooms().get(0).getRoom_type())){
                    tv_current_room_no_goods.setText("桌台： " + orderBean.getMsg().getOrder_rooms().get(0).getRoomname());
                }else{
                    tv_current_room_no_goods.setText("房间： " + orderBean.getMsg().getOrder_rooms().get(0).getRoomname());
                }
                if(charge_model== Constant.ORDER_ROOM_FEE_DAY){
                    tv_start_time_no_goods.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getStart_time())));
                }else{
                    tv_start_time_no_goods.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getStart_time())));
                }
                if ((orderBean.getMsg().getOrder_rooms().get(0).getEnd_time()!=null)&&!"0".equals(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())) {
                    if(charge_model==Constant.ORDER_ROOM_FEE_DAY){
                        tv_end_time_no_goods.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())));
                    }else{
                        tv_end_time_no_goods.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())));
                    }
                }
                //当前房间
                tv_useing_time_no_goods.setText(orderBean.getMsg().getOrder_rooms().get(0).getUse_timelength());
                if ((orderBean.getMsg().getOrder_rooms().get(0).getIs_billing()!=null)&&!"0".equals(orderBean.getMsg().getOrder_rooms().get(0).getIs_billing())) {
                    tv_current_resume_no_goods.setText("￥"+Utils.keepTwoDecimal("" + orderBean.getMsg().getOrder_rooms().get(0).getTrue_income()));
                } else {
                    tv_current_resume_no_goods.setText("无使用费");
                }

                handleRoomVipPriceLoglo();
                LogUtils.e(TAG, "没有商品时候的历史房间：根据返回的数据确定添加历史房间信息");
                //根据返回的数据确定添加历史房间信息
                if (orderBean.getMsg().getOrder_rooms().size() > 1) {
                    LogUtils.e(TAG, "没有商品时候的显示历史房间信息...");
                    addHistoryRoomNoGoods();
                } else {
                    LogUtils.e(TAG, "没有商品时候的没有历史房间数据...");
                }
                //计费的显示
                tv_total_money_no_goods.setText("总金额：￥" + Utils.keepTwoDecimal("" + orderBean.getMsg().getTotal_fee()));

                //房间的打折信息
                /*if (vip_discount != null) {
                    member_discount_no_goods.setText(vip_discount+"折");//需要获取打折标识
                } else {
                    member_discount_no_goods.setText("无");//需要获取打折标识
                }*/
                ShowCardVoucherInfo(vip_discount);
                tv_discount_total_money_no_goods.setText("￥" + Utils.keepTwoDecimal("" + orderBean.getMsg().getPayment()));
                tv_order_no_show_goods_no_goods.setText(String.valueOf(String.valueOf(orderBean.getMsg().getOrderid())));
                tv_tv_order_time_show_goods_no_goods.setText(Utils.getPtintTime(orderBean.getMsg().getAddtime() + "000"));
            } else {
                ll_no_goods_has_rooms.setVisibility(View.GONE);
                noData();
            }
        }
    }

    //优惠信息的显示：包含会员折扣、优惠券信息、优惠钱数
    private void ShowCardVoucherInfo(String vip_discount) {
        if(orderBean.getMsg().getVoucherinfo()!=null){
            if(orderBean.getMsg().getVoucherinfo().getCard_type()!=null){
                if("CASH".equals(orderBean.getMsg().getVoucherinfo().getCard_type())){
                    rl_member_discount_no_goods.setVisibility(View.VISIBLE);
                    iv_pic_card_coupons_no_goods.setVisibility(View.VISIBLE);
                    member_discount_no_goods.setVisibility(View.GONE);
                    iv_pic_card_coupons_no_goods.setImageResource(R.drawable.coupons_pic);
                    tv_member_discount_no_goods.setText(orderBean.getMsg().getVoucherinfo().getInfo());
                    tv_reduce_money_no_goods.setText(String.valueOf(orderBean.getMsg().getVoucherinfo().getReduce_cost()));

                    if(orderBean.getMsg().getVoucherinfo().getVipcardid()!=null&&!"".equals(orderBean.getMsg().getVoucherinfo().getVipcardid())&&!"0".equals(orderBean.getMsg().getVoucherinfo().getVipcardid())){
                        ll_consume_explain_no_goods.setVisibility(View.VISIBLE);
                        tv_consume_explain_no_goods.setText("该顾客为会员："+ orderBean.getMsg().getVoucherinfo().getVipinfo_name() +"，本单记录为会员消费");
                    }else{
                        ll_consume_explain_no_goods.setVisibility(View.GONE);
                    }
                }else if("DISCOUNT".equals(orderBean.getMsg().getVoucherinfo().getCard_type())){
                    rl_member_discount_no_goods.setVisibility(View.VISIBLE);
                    iv_pic_card_coupons_no_goods.setVisibility(View.VISIBLE);
                    member_discount_no_goods.setVisibility(View.GONE);
                    iv_pic_card_coupons_no_goods.setImageResource(R.drawable.discount_pic);

                    tv_member_discount_no_goods.setText(orderBean.getMsg().getVoucherinfo().getInfo());
                    double total_fee = orderBean.getMsg().getTotal_fee();
                    double reduce_dis = (10 - orderBean.getMsg().getVoucherinfo().getDiscount())/10;
                    if(reduce_dis>0&&reduce_dis<1){
                        LogUtils.e(TAG,"total_fee="+total_fee+",reduce_dis="+reduce_dis);
                        tv_reduce_money_no_goods.setText(Utils.keepTwoDecimal(String.valueOf(total_fee*reduce_dis)));
                    }else{
                        tv_reduce_money_no_goods.setText("0.00");
                    }

                    if(orderBean.getMsg().getVoucherinfo().getVipcardid()!=null&&!"".equals(orderBean.getMsg().getVoucherinfo().getVipcardid())&&!"0".equals(orderBean.getMsg().getVoucherinfo().getVipcardid())){
                        ll_consume_explain_no_goods.setVisibility(View.VISIBLE);
                        tv_consume_explain_no_goods.setText("该顾客为会员："+ orderBean.getMsg().getVoucherinfo().getVipinfo_name() +"，本单记录为会员消费");
                    }else{
                        ll_consume_explain_no_goods.setVisibility(View.GONE);
                    }
                }else {
                    rl_member_discount_no_goods.setVisibility(View.GONE);
                }
            }else{
                rl_member_discount_no_goods.setVisibility(View.GONE);
            }
        }else{
            if (vip_discount != null) {
                rl_member_discount_no_goods.setVisibility(View.VISIBLE);
                iv_pic_card_coupons_no_goods.setVisibility(View.GONE);
                tv_member_discount_no_goods.setVisibility(View.VISIBLE);
                member_discount_no_goods.setVisibility(View.VISIBLE);
                tv_member_discount_no_goods.setText("会员折扣");
                member_discount_no_goods.setText(vip_discount+"折");//需要获取打折标识

                double total_fee = orderBean.getMsg().getTotal_fee();
                double reduce_dis = (10 - Double.valueOf(vip_discount))/10;
                if(reduce_dis>0&&reduce_dis<1){
                    tv_reduce_money_no_goods.setText(Utils.keepTwoDecimal(String.valueOf(total_fee*reduce_dis)));
                }else{
                    tv_reduce_money_no_goods.setText("0.00");
                }
            } else {
                rl_member_discount_no_goods.setVisibility(View.GONE);
            }
        }
    }

    private void setFootZkRemark(String vip_discount) {
        tv_total_money.setText("总金额：￥" + Utils.keepTwoDecimal("" + orderBean.getMsg().getTotal_fee()));
        tv_order_no_show_goods.setText(String.valueOf(orderBean.getMsg().getOrderid()));
        tv_tv_order_time_show_goods.setText(Utils.getPtintTime(orderBean.getMsg().getAddtime() + "000"));

        /*if (vip_discount != null) {
            rl_member_discount.setVisibility(View.VISIBLE);
            member_discount.setText(vip_discount+"折");//需要获取打折标识
        } else {
            rl_member_discount.setVisibility(View.GONE);
            member_discount.setText("无");//需要获取打折标识
        }*/
        hasGoodsCardVoucher(vip_discount);
        tv_discount_total_money.setText("￥" + Utils.keepTwoDecimal("" + orderBean.getMsg().getPayment()));
    }

    private void showOrderMarkInfo() {
        if (orderBean.getMsg().getRemark() == null || TextUtils.isEmpty(orderBean.getMsg().getRemark())) {
            //没有备注记录
            tv_mark_state.setText("未填写");
            tv_mark_state_no_goods.setText("未填写");
            ll_remark.setVisibility(View.GONE);
            ll_remark_no_goods.setVisibility(View.GONE);
        }else {
            tv_mark_state.setText("修改");
            tv_mark_state_no_goods.setText("修改");
            ll_remark.setVisibility(View.VISIBLE);
            ll_remark_no_goods.setVisibility(View.VISIBLE);
            tv_mark_msg.setText(orderBean.getMsg().getRemark());
            tv_mark_msg_no_goods.setText(orderBean.getMsg().getRemark());
        }
    }

    private void setRoomInfo() {
        //有房间的处理:
        if (orderBean.getMsg().getOrder_rooms() != null && orderBean.getMsg().getOrder_rooms().size() > 0) {
            LogUtils.e(TAG, "有房间...");
            LogUtils.e(TAG, "初始化开始选择的时间");

            //初始化开始选择的时间
            room_id_room = orderBean.getMsg().getOrder_rooms().get(0).getRoom_id_room();
            querryStartTime = orderBean.getMsg().getOrder_rooms().get(0).getStart_time();
            querryEndTime = orderBean.getMsg().getOrder_rooms().get(0).getEnd_time();
            if(orderBean.getMsg().getOrder_rooms().get(0).getIs_billing()!=null&&!"".equals(orderBean.getMsg().getOrder_rooms().get(0).getIs_billing())){
                charge_model = Integer.valueOf(orderBean.getMsg().getOrder_rooms().get(0).getIs_billing());
            }
            DateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日");
            currrentYMD_start = df2.format(Long.valueOf(querryStartTime + "000"));
            if (!"".equals(querryEndTime) && !"0".equals(querryEndTime)) {
                isModifyEndTime = true;
                currrentYMD_end = df2.format(Long.valueOf(querryEndTime + "000"));
            } else {
                currrentYMD_end = df2.format(Long.valueOf(querryStartTime + "000"));
            }
            room_name = orderBean.getMsg().getOrder_rooms().get(0).getRoomname();
            if(orderBean.getMsg().getOrder_rooms().get(0).getRoom_type()!=null) {
                if("1".equals(orderBean.getMsg().getOrder_rooms().get(0).getRoom_type())) {
                    tv_current_room.setText("桌台：" + orderBean.getMsg().getOrder_rooms().get(0).getRoomname());
                }else {
                    tv_current_room.setText("房间：" + orderBean.getMsg().getOrder_rooms().get(0).getRoomname());
                }
            }else {
                tv_current_room.setText("房间：" + orderBean.getMsg().getOrder_rooms().get(0).getRoomname());
            }

            if(charge_model== Constant.ORDER_ROOM_FEE_DAY){
                tv_start_time.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getStart_time())));
                tv_start_time_no_goods.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getStart_time())));
            }else{
                tv_start_time.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getStart_time())));
                tv_start_time_no_goods.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getStart_time())));
            }
            if (!"0".equals(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())) {
                if(charge_model==Constant.ORDER_ROOM_FEE_DAY){
                    tv_end_time.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())));
                    tv_end_time_no_goods.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())));
                }else{
                    tv_end_time.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())));
                    tv_end_time_no_goods.setText(Utils.getTimeFromLongString(Utils.PHPTransforTimeStampToJava(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())));
                }
            }else{
                tv_end_time.setText("结束时间未选择");
                tv_end_time_no_goods.setText("结束时间未选择");
            }
            //当前房间
            tv_useing_time.setText("使用时长：" + orderBean.getMsg().getOrder_rooms().get(0).getUse_timelength());
            if (!"0".equals(orderBean.getMsg().getOrder_rooms().get(0).getIs_billing())) {
                tv_current_resume.setText("房间消费：" + orderBean.getMsg().getOrder_rooms().get(0).getTrue_income());
            } else {
                tv_current_resume.setText("无使用费");
            }
            handleRoomVipPriceLoglo();

            top_distance = Utils.dip2px(this, 287)+2;
            //历史房间：根据返回的数据确定添加历史房间信息
            if (orderBean.getMsg().getOrder_rooms().size() > 1&&orderBean.getMsg().getOrder_goods()!=null) {
                LogUtils.e(TAG, "显示历史房间信息...");
                addHistoryRoom();
            } else {
                LogUtils.e(TAG, "房间数据orderBean.getMsg().getOrder_rooms().size()="+orderBean.getMsg().getOrder_rooms().size()+",orderBean.getMsg().getOrder_goods()==null:"+(orderBean.getMsg().getOrder_goods()==null));
            }
        } else {
            line2.setVisibility(View.GONE);
            //line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
            line5.setVisibility(View.GONE);
            all_room_info.setVisibility(View.GONE);
            rl_start.setVisibility(View.GONE);
            //rl_end.setVisibility(View.GONE);
            ll_used_room.setVisibility(View.GONE);
            rl_current_room_resume.setVisibility(View.GONE);
            top_distance = Utils.dip2px(this, 125);
        }
    }

    private void handleRoomVipPriceLoglo() {
        //会员价logo
        int is_vip = orderBean.getMsg().getOrder_rooms().get(0).getIs_vip();
        LogUtils.Log("当前房间is_vip=="+is_vip);
        if(is_vip==0) {
            member_price_logo_no_goods.setVisibility(View.GONE);
            member_price_logo.setVisibility(View.GONE);
        }else {
            member_price_logo_no_goods.setVisibility(View.VISIBLE);
            member_price_logo.setVisibility(View.VISIBLE);
        }
    }

    //有商品时候的会员折扣、券信息的显示
    private void hasGoodsCardVoucher(String vip_discount) {
        if(orderBean.getMsg().getVoucherinfo()!=null){
            if(orderBean.getMsg().getVoucherinfo().getCard_type()!=null){
                //现金券
                if("CASH".equals(orderBean.getMsg().getVoucherinfo().getCard_type())){
                    rl_member_discount.setVisibility(View.VISIBLE);
                    iv_pic_card_coupons.setVisibility(View.VISIBLE);
                    member_discount.setVisibility(View.GONE);
                    iv_pic_card_coupons.setImageResource(R.drawable.coupons_pic);

                    tv_member_discount.setText(orderBean.getMsg().getVoucherinfo().getInfo());
                    tv_reduce_money.setText(String.valueOf(orderBean.getMsg().getVoucherinfo().getReduce_cost()));

                    if(orderBean.getMsg().getVoucherinfo().getVipcardid()!=null&&!"".equals(orderBean.getMsg().getVoucherinfo().getVipcardid())&&!"0".equals(orderBean.getMsg().getVoucherinfo().getVipcardid())){
                        ll_consume_explain.setVisibility(View.VISIBLE);
                        tv_consume_explain.setText("该顾客为会员："+ orderBean.getMsg().getVoucherinfo().getVipinfo_name() +"，本单记录为会员消费");
                    }else{
                        ll_consume_explain.setVisibility(View.GONE);
                        ll_member_info.setVisibility(View.GONE);
                    }
                    //折扣券
                }else if("DISCOUNT".equals(orderBean.getMsg().getVoucherinfo().getCard_type())){
                    rl_member_discount.setVisibility(View.VISIBLE);
                    iv_pic_card_coupons.setVisibility(View.VISIBLE);
                    member_discount.setVisibility(View.GONE);
                    iv_pic_card_coupons.setImageResource(R.drawable.discount_pic);

                    tv_member_discount.setText(orderBean.getMsg().getVoucherinfo().getInfo());
                    double total_fee = orderBean.getMsg().getTotal_fee();
                    double reduce_dis = (10 - orderBean.getMsg().getVoucherinfo().getDiscount())/10;
                    if(reduce_dis>0&&reduce_dis<1){
                        LogUtils.e(TAG,"total_fee="+total_fee+",reduce_dis="+reduce_dis);
                        tv_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(total_fee*reduce_dis)));
                    }else{
                        tv_reduce_money.setText("0.00");
                    }
                    if(orderBean.getMsg().getVoucherinfo().getVipcardid()!=null&&
                            !"".equals(orderBean.getMsg().getVoucherinfo().getVipcardid())&&
                            !"0".equals(orderBean.getMsg().getVoucherinfo().getVipcardid())){
                        ll_consume_explain.setVisibility(View.VISIBLE);
                        tv_consume_explain.setText("该顾客为会员："+ orderBean.getMsg().getVoucherinfo().getVipinfo_name() +"，本单记录为会员消费");
                    }else{
                        ll_consume_explain.setVisibility(View.GONE);
                        ll_member_info.setVisibility(View.GONE);
                    }
                }else {
                    rl_member_discount.setVisibility(View.GONE);
                }
            }else{
                rl_member_discount.setVisibility(View.GONE);
            }
        }else{
            if (vip_discount != null) {
                rl_member_discount.setVisibility(View.VISIBLE);
                iv_pic_card_coupons.setVisibility(View.GONE);
                tv_member_discount.setVisibility(View.VISIBLE);
                member_discount.setVisibility(View.VISIBLE);
                tv_member_discount.setText("会员折扣");
                member_discount.setText(vip_discount+"折");//需要获取打折标识

                double total_fee = orderBean.getMsg().getTotal_fee();
                double reduce_dis = (10 - Double.valueOf(vip_discount))/10;
                if(reduce_dis>0&&reduce_dis<1){
                    LogUtils.e(TAG,"total_fee="+total_fee+",reduce_dis="+reduce_dis);
                    tv_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(total_fee*reduce_dis)));
                }else{
                    tv_reduce_money.setText("0.00");
                }
            } else {
                rl_member_discount.setVisibility(View.GONE);
            }
        }
    }

    private void addHistoryRoom() {
        int add_distance = 0;
        ll_used_room.removeAllViews();
        for (int i = 1; i < orderBean.getMsg().getOrder_rooms().size(); i++) {
            View history_room_view = View.inflate(this, R.layout.history_room_resume, null);
            ImageView historyroom_member_price_logo= (ImageView) history_room_view.findViewById(R.id.historyroom_member_price_logo);
            TextView tv_history_room_desc= (TextView) history_room_view.findViewById(R.id.tv_history_room_desc);
            TextView tv_history_room_name = (TextView) history_room_view.findViewById(R.id.tv_history_room_name);
            TextView tv_history_starttime = (TextView) history_room_view.findViewById(R.id.tv_history_starttime);
            TextView tv_history_endtime = (TextView) history_room_view.findViewById(R.id.tv_history_endtime);
            TextView tv_useing_time = (TextView) history_room_view.findViewById(R.id.tv_useing_time);
            TextView tv_current_resume = (TextView) history_room_view.findViewById(R.id.tv_current_resume);
            TextView tv_history_room_fee_rule = (TextView) history_room_view.findViewById(R.id.tv_history_room_fee_rule);

            tv_history_room_name.setText(orderBean.getMsg().getOrder_rooms().get(i).getRoomname());
            tv_history_starttime.setText("开始：" + Utils.getDatetimeStampToString(orderBean.getMsg().getOrder_rooms().get(i).getStart_time() + "000"));
            String end_time = orderBean.getMsg().getOrder_rooms().get(i).getEnd_time();
            String room_type = orderBean.getMsg().getOrder_rooms().get(i).getRoom_type();
            if("1".endsWith(room_type)) {
                tv_history_room_desc.setText(":   [原桌台]");
            }else if("0".endsWith(room_type)) {
                tv_history_room_desc.setText(":   [原房间]");
            }else {
                tv_history_room_desc.setText(":   [原房间/桌台]");
            }


            if (!"0".equals(end_time)) {
                tv_history_endtime.setText("结束：" + Utils.getDatetimeStampToString(orderBean.getMsg().getOrder_rooms().get(i).getEnd_time() + "000"));
            }
            tv_useing_time.setText("使用时长：" + orderBean.getMsg().getOrder_rooms().get(i).getUse_timelength());
            if (!"0".equals(orderBean.getMsg().getOrder_rooms().get(i).getIs_billing())) {
                tv_current_resume.setText("房间消费：￥" + orderBean.getMsg().getOrder_rooms().get(i).getTrue_income());
            }
            int is_vip = orderBean.getMsg().getOrder_rooms().get(i).getIs_vip();
            if(0==is_vip) {
                historyroom_member_price_logo.setVisibility(View.GONE);
            }else {
                historyroom_member_price_logo.setVisibility(View.VISIBLE);
            }

            if("0".equals(orderBean.getMsg().getOrder_rooms().get(i).getIs_billing())){
                tv_history_room_fee_rule.setText("[不计费]");
            }else if("1".equals(orderBean.getMsg().getOrder_rooms().get(i).getIs_billing())){
                tv_history_room_fee_rule.setText("[按小时计费]");
            }else if("2".equals(orderBean.getMsg().getOrder_rooms().get(i).getIs_billing())){
                tv_history_room_fee_rule.setText("[按天计费]");
            }else{
                tv_history_room_fee_rule.setText("");
            }
            add_distance += 106;
            ll_used_room.addView(history_room_view);
        }
        top_distance = top_distance + Utils.dip2px(this, add_distance);
    }

    private void addHistoryRoomNoGoods() {

        ll_used_room_no_goods.removeAllViews();
        for (int i = 1; i < orderBean.getMsg().getOrder_rooms().size(); i++) {
            View history_room_view = View.inflate(this, R.layout.history_room_resume, null);
            ImageView historyroom_member_price_logo= (ImageView) history_room_view.findViewById(R.id.historyroom_member_price_logo);
            TextView tv_history_room_name = (TextView) history_room_view.findViewById(R.id.tv_history_room_name);
            TextView tv_history_room_desc= (TextView) history_room_view.findViewById(R.id.tv_history_room_desc);

            TextView tv_history_starttime = (TextView) history_room_view.findViewById(R.id.tv_history_starttime);
            TextView tv_history_endtime = (TextView) history_room_view.findViewById(R.id.tv_history_endtime);
            TextView tv_useing_time = (TextView) history_room_view.findViewById(R.id.tv_useing_time);
            TextView tv_current_resume = (TextView) history_room_view.findViewById(R.id.tv_current_resume);
            TextView tv_history_room_fee_rule = (TextView) history_room_view.findViewById(R.id.tv_history_room_fee_rule);

            tv_history_room_name.setText(orderBean.getMsg().getOrder_rooms().get(i).getRoomname());
            tv_history_starttime.setText("开始：" + Utils.getDatetimeStampToString(orderBean.getMsg().getOrder_rooms().get(i).getStart_time() + "000"));

            String room_type = orderBean.getMsg().getOrder_rooms().get(i).getRoom_type();
            if("1".endsWith(room_type)) {
                tv_history_room_desc.setText(":   [原桌台]");
            }else if("0".endsWith(room_type)) {
                tv_history_room_desc.setText(":   [原房间]");
            }else {
                tv_history_room_desc.setText(":   [原房间/桌台]");
            }

            String end_time = orderBean.getMsg().getOrder_rooms().get(i).getEnd_time();
            if (!"0".equals(end_time)) {
                tv_history_endtime.setText("结束：" + Utils.getDatetimeStampToString(orderBean.getMsg().getOrder_rooms().get(i).getEnd_time() + "000"));
            }
            tv_useing_time.setText("使用时长：" + orderBean.getMsg().getOrder_rooms().get(i).getUse_timelength());
            if (!"0".equals(orderBean.getMsg().getOrder_rooms().get(i).getIs_billing())) {
                tv_current_resume.setText("房间消费：￥" + orderBean.getMsg().getOrder_rooms().get(i).getTrue_income());
            }

            int is_vip = orderBean.getMsg().getOrder_rooms().get(i).getIs_vip();
            if(0==is_vip) {
                historyroom_member_price_logo.setVisibility(View.GONE);
            }else {
                historyroom_member_price_logo.setVisibility(View.VISIBLE);
            }
            if("0".equals(orderBean.getMsg().getOrder_rooms().get(i).getIs_billing())){
                tv_history_room_fee_rule.setText("[不计费]");
            }else if("1".equals(orderBean.getMsg().getOrder_rooms().get(i).getIs_billing())){
                tv_history_room_fee_rule.setText("[按小时计费]");
            }else if("2".equals(orderBean.getMsg().getOrder_rooms().get(i).getIs_billing())){
                tv_history_room_fee_rule.setText("[按天计费]");
            }
            ll_used_room_no_goods.addView(history_room_view);
        }
    }

    private void setShowAdapter(boolean isShowMinusButton) {
        LogUtils.e(TAG, "orderBean.getMsg().getGoods_num()=" + orderBean.getMsg().getGoods_num());
        adapter = new ResumeDetailAdapter(this, tv_total_money, iv_transfer, handler, orderBean.getMsg().getGoods_num());
        adapter.setData(orderBean);
        adapter.setIsShowMAbtn(isShowMinusButton);
        adapter.setDataFrom(Constant.ORDER_DETAIL_ALL);
        adapter.setContainMember(orderContainsMember);
        lv_order_details.setAdapter(adapter);
        //处理再次加载页面闪动:需显示到上次滑动的位置
        if (show_pos != 0) {
            int distance = (int) (top_distance - show_pos + Utils.dip2px(this,1));//不知为什么总有3px差值
            lv_order_details.setSelectionFromTop(1, distance);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_title://返回
                finish();
                break;
            case R.id.btn_cancel_order://取消订单
                showCancelDialog();
                break;
            case R.id.btn_print_order://打印订单
                if (ClicKUtils.isFastDoubleClick()) {
                    return;
                } else {
                    /*printOrder();
                    judgeBtPrint();*/
                    localPrint();
                    startYunPrint(false);
                    if(btPrintData!=null&&btPrintData.size()>0) {
                        BTPrintUtils.getInstance().wmBtPrint(mService, btPrintData,this,null);
                    }
                }
                break;
            case R.id.btn_finish_order://结账:并且判断是否已经有结束时间了
                judgeRefreshOrToPay();
                break;
            case R.id.change_order_money_no_goods://没有商品和服务的改价
            case R.id.change_order_money://改价
                if (Utils.isNetworkAvailable(this)) {
                    changeMoney();
                } else {
                    Utils.showToast(this, "网络异常，请检查网络后再试");
                }
                break;
            case R.id.btn_finish_change://改价完毕
                if (Utils.isNetworkAvailable(this)) {
                    changePriceDone();
                } else {
                    Utils.showToast(this, "网络异常，请检查网络后再试");
                }
                break;
            case R.id.btn_cancel_change://取消改价
                if (changePriceDialog != null) {
                    changePriceDialog.dismiss();
                }
                break;
            case R.id.tv_change_room_no_goods://没有商品和服务的更换房间
            case R.id.tv_change_room://更换房间
                changeRoom();
                break;
            case R.id.tv_cancel_radius_goods://取消
                clearLocalData();
                tv_minus_entity_service.setBackgroundResource(R.drawable.add_entity_service_shape);
                tv_minus_entity_service.setTextColor(Color.parseColor("#48ab6f"));
                tv_minus_entity_service.setText(" - 减少商品 ");
                tv_cancel_radius_goods.setVisibility(View.GONE);
                tv_add_entity_service.setVisibility(View.VISIBLE);
                isModifyGoods = false;
                if(recoverShowData()){
                    if(Utils.isNetworkAvailable(this)){
                        Utils.showWaittingDialog(this,"正在恢复商品...");
                        initData();
                    }else{
                        Utils.showToast(this,"没有网络连接暂无法操作商品");
                    }
                }
                /*String order_details_res = SpUtils.getInstance(this).getString("order_details_res", "");
                if(!"".equals(order_details_res)){
                    parseJson(order_details_res);
                }else{
                    if(Utils.isNetworkAvailable(this)){
                        Utils.showWaittingDialog(this,"正在恢复商品...");
                        initData();
                    }else{
                        Utils.showToast(this,"没有网络连接暂无法操作商品");
                    }
                }*/
                break;
            case R.id.tv_minus_entity_service://减少实物/服务
                if (Utils.isNetworkAvailable(this)) {
                    if (!isModifyGoods) {//点击减少商品以后的操作：
                        isModifyGoods = true;
                        tv_minus_entity_service.setBackgroundResource(R.drawable.add_no_side_shape);
                        tv_minus_entity_service.setTextColor(Color.parseColor("#ffffff"));
                        tv_minus_entity_service.setText(" 确认修改 ");
                        tv_cancel_radius_goods.setVisibility(View.VISIBLE);
                        tv_add_entity_service.setVisibility(View.GONE);
                        //发通知给adapter让其显示加减号
                        adapter.setIsShowMAbtn(true);
                        adapter.notifyDataSetChanged();
                    } else {//点击确认修改以后的操作：
                        isModifyGoods = false;
                        tv_minus_entity_service.setBackgroundResource(R.drawable.add_entity_service_shape);
                        tv_minus_entity_service.setTextColor(Color.parseColor("#48ab6f"));
                        tv_minus_entity_service.setText(" - 减少商品 ");
                        tv_cancel_radius_goods.setVisibility(View.GONE);
                        tv_add_entity_service.setVisibility(View.VISIBLE);
                        adapter.setIsShowMAbtn(false);
                        adapter.notifyDataSetChanged();
                        minusEntityService();
                    }
                } else {
                    Utils.showToast(this, "网络异常，请检查网络后再试");
                }
                break;
            case R.id.tv_add_entity_service_no_goods:
            case R.id.tv_add_entity_service://添加实物/服务
                if (Utils.isNetworkAvailable(this)) {
                    Constant.order_price = 0.00;
                    Constant.order_goods_num = 0.00;
                    intent = new Intent(this, AddServiceGoodActivity.class);
                    intent.putExtra("unpay_order_id", unpay_order_no);//主键id
                    intent.putExtra("query_num", orderBean.getMsg().getQuery_num());
                    intent.putExtra("from_where", Constant.ADD_FROM_DETAILS);
                    intent.putExtra("room_name", room_name);//房间名字
                    intent.putExtra("order_bean", orderBean);
                    intent.putExtra(Constant.MEMBER_BEAN, memberBean);
                    if(containsVoucher) {
                        intent.putExtra("needVipPrice",0);
                    }else {
                        intent.putExtra("needVipPrice",1);
                    }
                    startActivity(intent);
                    //finish();
                } else {
                    Utils.showToast(this, "网络异常，请检查网络后再试");
                }
                break;
            /*case R.id.rl_end_no_goods://没有商品和服务的选择结束时间
            //case R.id.rl_end://选择结束时间
                clickView = CLICK_VIEW_END;
                //querryEndTime = orderBean.getMsg().getOrder_rooms().get(0).getEnd_time();
                querryTimeReturn = false;
                //QueryTimeRange();
                //showTimePickerPup();
                break;*/
            case R.id.rl_start_no_goods://没有商品和服务的选择开始时间
                LogUtils.e(TAG,"-------------------R.id.rl_start_no_goods");
            case R.id.rl_start://选择开始时间
                LogUtils.e(TAG,"-------------------R.id.rl_start");
                //querryStartTime = orderBean.getMsg().getOrder_rooms().get(0).getStart_time();
                /*clickView = CLICK_VIEW_START;
                querryTimeReturn = false;
                QueryTimeRange();
                showTimePickerPup();*/
                //QueryTimeRange();
                if(ClicKUtils.isFastDoubleClick()){
                    return;
                }else{
                    showTimePicker();
                }
                break;
            case R.id.tv_finish_choose://wheelView选择时间结束
                //finishTimeChoose();
                //finishTimeChooseNew();
                if(isModifyEndTime){
                    LogUtils.e(TAG,"R.id.tv_finish_choose 开始和结束时间");
                    modifyStartEndTime();
                }else{
                    LogUtils.e(TAG,"R.id.tv_finish_choose 只提交开始时间");
                    justModifyStartTime();
                }
                break;
            case R.id.tv_time_choose_cancel://取消wheelView选择时间：需要设置结束时间
                if(wheelChooseDialog!=null){
                    wheelChooseDialog.dismiss();
                }

                //设置结束时间
                DateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日");
                if(!"0".equals(querryEndTime)){
                    currrentYMD_end = df2.format(Long.valueOf(querryEndTime + "000"));
                }
                currentHour_end = "00";
                currentMin_end = "00";

                currrentYMD_start = df2.format(Long.valueOf(querryStartTime + "000"));
                currentHour_start = "00";
                currentMin_start ="00";
                break;
            case R.id.tv_mark_reback://会员识别
                Constant.MEMBER_ID="";
                //引导开通:跳转到充值
                netWorks.UpDateAuth(new NetWorks.OnGetQueryResult() {
                    @Override
                    public void stateOpen() {
                        toCaptureActivity();
                    }

                    @Override
                    public void stateClose() {

                    }
                }, "4");
                break;
            /*case R.id.tv_temp_no_choose://暂时不选择结束时间
                if(wheelChooseDialog!=null){
                    wheelChooseDialog.dismiss();
                }

                //结束时间不选择
                DateFormat df3 = new SimpleDateFormat("yyyy年MM月dd日");
                if(!"0".equals(querryEndTime)){
                    currrentYMD_end = df3.format(Long.valueOf(querryEndTime + "000"));
                }
                currentHour_end = "00";
                currentMin_end = "00";

                //只提交开始时间结束时间设置为0
                justModifyStartTime();
                break;*/
            case R.id.tv_mark_state_no_goods:
            case R.id.tv_mark_state://填写或更改备注
                inputOrChangeRemak();
                break;
        }
    }
    private void toCaptureActivity() {
        Constant.payway = "-1";
        Intent intent = new Intent(this, CaptureActivity.class);
        intent.putExtra(Constant.FROME_WHERE, Constant.MEMBER_CARDS_VOUCHER);
        orderBean.getMsg().getOrderid();
        intent.putExtra(Constant.UNPAY_ORDER_ID, unpay_order_no);
        intent.putExtra("total_fee", order_total_fee);
        intent.putExtra("commit", "1");
        startActivity(intent);
    }
    /**
     * 填写备注或者更改备注
     */
    private void inputOrChangeRemak() {
        NoticePopuUtils.setRemakDia(this, null, R.id.arl_this_order_detail, new NoticePopuUtils.SetRemakCallBack() {
            @Override
            public void onClickYes(String msg) {
                if(msg==null||TextUtils.isEmpty(msg)) {
                    Utils.showToast(OrderDetailActivity.this,"未检测到输入信息");
                    return;
                }
                if(msg.length()>100) {
                    Utils.showToast(OrderDetailActivity.this,"备注信息不得超过100个字符");
                    return;
                }
                if(msg.equals(tv_mark_msg.getText().toString())||msg.equals(tv_mark_msg_no_goods.getText().toString())) {
                    Utils.showToast(OrderDetailActivity.this,"新的备注与之前相同！");
                    return;
                }
                Utils.showWaittingDialog(OrderDetailActivity.this,"备注提交中");
                updateRemak(msg);
                LogUtils.Log("传递备注信息至服务器");
            }
            @Override
            public void onClickNo() {

            }
        });
    }

    /**
     * 上传备注至服务器及结果处理
     * @param msg
     */
    private void updateRemak(final String msg) {

        netWorks.upDataRemak("0",orderBean.getMsg().getOrder_no(), msg, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("提交备注e=="+e.toString());
                upDateMarkFailure(msg);
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("提交备注response=="+response.toString());
                Utils.dismissWaittingDialog();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200) {
                        tv_mark_state.setText("修改");
                        tv_mark_state_no_goods.setText("修改");
                        tv_mark_msg.setText(msg);
                        tv_mark_msg_no_goods.setText(msg);
                        ll_remark.setVisibility(View.VISIBLE);
                        ll_remark_no_goods.setVisibility(View.VISIBLE);
                        orderBean.getMsg().setRemark(msg);
                        //备注信息的打印数据
                        initPrintData();
                    }else {
                        String notice = jsonObject.getString("err");
                        Utils.showToast(OrderDetailActivity.this,notice,1000);
                        upDateMarkFailure(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },12);
    }

    /**
     * 提交失败
     * @param msg
     */
    private void upDateMarkFailure(final String msg) {

        Utils.dismissWaittingDialog();
        Utils.showToast(OrderDetailActivity.this,"网络异常，提交备注失败！",1000);
        NoticePopuUtils.refundErrorDia(OrderDetailActivity.this, "稍后您可以到管理-收银机订单管理中,为本订单添加备注！",
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
                        updateRemak(msg);
                    }
                });
    }

    /**
     * 判断是否刷新价格或者直接支付
     */
    private void judgeRefreshOrToPay() {
        if(!tv_end_time.getText().toString().trim().contains("未选择")||!tv_end_time_no_goods.getText().toString().trim().contains("未选择")){
            if (judgeRangeNet(true)) {
                showPayPopWindow();
            }
        }else{
            if(price_timmer_isend||charge_model==Constant.ORDER_ROOM_NO_FEE){
                if (judgeRangeNet(true)) {
                    showPayPopWindow();
                }
            }else{
                showReFreshPrice();
            }
        }
    }


    private void showReFreshPrice() {

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_change_price, null);
        TextView tv_return = (TextView) view.findViewById(R.id.tv_return);
        TextView tv_see_orders = (TextView) view.findViewById(R.id.tv_see_orders);
        TextView tv_explain = (TextView) view.findViewById(R.id.tv_explain);
        tv_explain.setText(OrderDetailActivity.this.getResources().getString(R.string.may_price_change));
        final PopupWindow pricePop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(0.6f);
        pricePop.setOutsideTouchable(false);
        pricePop.setFocusable(true);
        pricePop.showAtLocation(findViewById(R.id.arl_this_order_detail), Gravity.CENTER_HORIZONTAL, 0, Utils.dip2px(this, -100));//设置为取景框中间

        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlpha(1);
                pricePop.dismiss();
            }
        });
        tv_see_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlpha(1);
                pricePop.dismiss();
                initData();
                reLoad();
            }
        });
    }

    /**
     * 会员识别的回调，拿到会员号，联网请求会员信息
     * @param memberBean
     */
    @Subscribe
    public void onEventMainThread(DetailMemberBean memberBean) {
        LogUtils.Log("添加商品的会员识别");
        if (memberBean != null) {
            this.memberBean = memberBean;
            showMemberInfo();
            vipcreate_id = memberBean.getMsg().getVipcreate_id();
            getUnpayOrderDetails();
        } else {
            Utils.showToast(this, "未检索到该会员信息！");
        }
        //FIXME：这个可以保证只调用一次。
        EventBus.getDefault().cancelEventDelivery(memberBean);
    }

    /**
     * 会员识别的回调，拿到会员号，联网请求会员信息
     * @param posDiscountVoucherBean
     */
    @Subscribe
    public void onEventMainThread(DetailPosDiscountVoucherBean posDiscountVoucherBean) {
        LogUtils.e(TAG,"折扣券识别 onEventMainThread()");
        if (posDiscountVoucherBean != null) {
            this.posDiscountVoucherBean = posDiscountVoucherBean;
        } else {
            Toast.makeText(this, "未检索到该折扣券信息！", Toast.LENGTH_SHORT).show();
        }
        //FIXME：这个可以保证只调用一次。
        EventBus.getDefault().cancelEventDelivery(posDiscountVoucherBean);
    }

    /**
     * 会员识别的回调，拿到会员号，联网请求会员信息
     * @param posCashVoucherBean
     */
    @Subscribe
    public void onEventMainThread(DetailPosCashVoucherBean posCashVoucherBean) {
        LogUtils.e(TAG,"抵扣券识别 onEventMainThread()");
        if (posCashVoucherBean != null) {
            this.posCashVoucherBean = posCashVoucherBean;
        } else {
            Toast.makeText(this, "未检索到该抵扣券信息！", Toast.LENGTH_SHORT).show();
        }
        //FIXME：这个可以保证只调用一次。
        EventBus.getDefault().cancelEventDelivery(posCashVoucherBean);
    }


    /**
     * 减商品或者服务数量
     */
    private void minusEntityService() {
        Utils.showWaittingDialog(this, "正在修改中...");
        String commitOrderJson = getCommitOrderJson();
        LogUtils.e(TAG, "详情页面提交的商品数量：commitOrderJson=" + commitOrderJson);
        String currentTime_add_service = Utils.getCurrentTime();
        String token_add_service = Utils.getToken(currentTime_add_service, this);
        OkHttpUtils.post()
                .url(UrlConstance.MODIFY_GOODS_NUM)//返回:成功200 失败10122
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token_add_service)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime_add_service)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("order", commitOrderJson)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.dismissWaittingDialog();
                        Utils.showToast(OrderDetailActivity.this, "提交商品数量失败");
                        clearLocalData();
                        recoverShowData();
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "提交的商品数量：response=" + response);
                        boolean b = Utils.checkSaveToken(OrderDetailActivity.this, response);
                        if (b) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    clearLocalData();
                                    Utils.showToast(OrderDetailActivity.this, "修改商品数量成功！");
                                    initData();
                                } else {
                                    Utils.dismissWaittingDialog();
                                    Utils.showToast(OrderDetailActivity.this, "修改商品数量失败");
                                    clearLocalData();
                                    recoverShowData();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private boolean recoverShowData() {
        String order_details_res = SpUtils.getInstance(OrderDetailActivity.this).getString("order_details_res", "");
        if(!"".equals(order_details_res)){
            parseJson(order_details_res);
            return false;
        }
        return true;
    }

    private void changeRoom() {
        long change_time = System.currentTimeMillis();
        Intent intent;
        intent = new Intent(this, NewOrderActivity.class);
        intent.putExtra(Constant.FROME_WHERE, Constant.CHANGE_ROOM_TO_DETAIL);
        intent.putExtra("unpay_order_id", unpay_order_no);//主键id
        intent.putExtra("oldcosttype", orderBean.getMsg().getOrder_rooms().get(0).getIs_billing());//主键id
        intent.putExtra("room_id", orderBean.getMsg().getOrder_rooms().get(0).getId());//暂时写返回房间的第一个房间数据：可返回默认第一个为当前使用房间
        intent.putExtra("room_id_room", room_id_room);//更换房间需要的
        intent.putExtra("room_name", orderBean.getMsg().getOrder_rooms().get(0).getRoomname());
        intent.putExtra("room_pay_way", Integer.valueOf(orderBean.getMsg().getOrder_rooms().get(0).getIs_billing()));
        intent.putExtra("oldendtime", change_time);
        intent.putExtra(Constant.MEMBER_BEAN, memberBean);
        startActivity(intent);
    }

    private void changePriceDone() {
        final String newPrice = et_new_price.getText().toString();
        if (newPrice != null && !TextUtils.isEmpty(newPrice)) {
            if ("请点此返回".equals(tv_change_countway.getText().toString())) {
                Double discount = Double.valueOf(newPrice);
                if(discount<=0||discount>=10) {
                    Utils.showToast(this,"折扣必须在0-10之间",1500);
                    Utils.dismissWaittingDialog();
                    return;
                }
                Double doublePayment = Double.valueOf(payment);
                afterGj=Utils.keepTwoDecimal((discount/10)*doublePayment+"");
            }else {
                afterGj = Utils.keepTwoDecimal(newPrice);
            }
            String remark = et_modify_price_reason.getText().toString();
            LogUtils.e(TAG, "order_id=" + orderBean.getMsg().getOrderid());
            LogUtils.e(TAG, "price=" + afterGj);
            LogUtils.e(TAG, "remark=" + remark);
            String currentTime_add_entity = Utils.getCurrentTime();
            String token_add_entity = Utils.getToken(currentTime_add_entity, this);
            final String finalRemark = remark;
            OkHttpUtils.post()
                    .url(UrlConstance.MODIFY_ORDER_PRICE)//返回：成功200 失败10122
                    .addParams(Constant.PARAMS_NAME_POSTOKEN, token_add_entity)
                    .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime_add_entity)
                    .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                    .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                    .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                    .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                    .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                    .addParams("order_id", "" + orderBean.getMsg().getOrderid())
                    .addParams("price", afterGj)
                    .addParams("remark", finalRemark)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            LogUtils.e(TAG, "改价Exception e=" + e);
                            Utils.dismissWaittingDialog();
                            Utils.showToast(OrderDetailActivity.this, "改价同步服务器失败，请检查网络后再试！");
                        }

                        @Override
                        public void onResponse(String response) {
                            LogUtils.e(TAG, "改价response=" + response);
                            Utils.dismissWaittingDialog();
                            boolean b = Utils.checkSaveToken(OrderDetailActivity.this, response);
                            if (b) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int code = jsonObject.getInt("code");
                                    if (code == 200) {
                                        tv_discount_total_money.setText("￥" + Utils.keepTwoDecimal(afterGj));
                                        tv_discount_total_money_no_goods.setText("￥" + Utils.keepTwoDecimal(afterGj));
                                        orderBean.getMsg().setRemark(finalRemark);
                                        orderBean.getMsg().setPayment(Double.valueOf(afterGj));
                                        payment=afterGj;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            changePriceDialog.dismiss();
        } else {
            Utils.dismissWaittingDialog();
            Utils.showToast(this, "请输入修改后金额");
        }
    }

    /**
     * 查询时间范围
     */
    private void QueryTimeRange() {

        LogUtils.e(TAG, "orderid=" + orderBean.getMsg().getOrderid());
        LogUtils.e(TAG, "room_id=" + orderBean.getMsg().getOrder_rooms().get(0).getId());
        LogUtils.e(TAG, "start_time=" + orderBean.getMsg().getOrder_rooms().get(0).getStart_time());
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        OkHttpUtils.post()
                .url(UrlConstance.QUERRY_START_END_TIME)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("orderid", "" + orderBean.getMsg().getOrderid())
                .addParams("room_id", orderBean.getMsg().getOrder_rooms().get(0).getId())
                .addParams("start_time", orderBean.getMsg().getOrder_rooms().get(0).getStart_time())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.showToast(OrderDetailActivity.this, "修改时间失败，请检查网络后再试");
                        LogUtils.e(TAG, "查询使用时间范围：Exception e=" + e);
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "可用时间范围：response=" + response);
                        boolean b = Utils.checkSaveToken(OrderDetailActivity.this, response);
                        if (b) {
                            parseQuerryTimeJson(response);
                        }
                    }
                });
    }

    private void clearLocalData() {
        Constant.localOrderBean.setGoodList(null);
    }

    /**
     * 解析修改订单相关
     * @param response
     */
    private void parseQuerryTimeJson(String response) {//{"code":200,"msg":{"start_time":"1479953220","end_time":"0"}}
        //如果选择的时候在返回的时间中间，则不提示，如果不在则提示
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                querryTimeReturn = true;
                querryStartTime = jsonObject.getJSONObject("msg").getString("start_time");
                querryEndTime = jsonObject.getJSONObject("msg").getString("end_time");
            } else {
                Utils.showToast(this, "获取房间可选时间范围失败，请重试！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改订单开始结束时间
     */
    private void modifyStartEndTime() {
        LogUtils.e(TAG,"modifyStartEndTime()。。。开始："+currrentYMD_start + " " + currentHour_start + ":" + currentMin_start);
        LogUtils.e(TAG,"modifyStartEndTime()。。。结束："+currrentYMD_end + " " + currentHour_end + ":" + currentMin_end);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        long time_start = 0;
        long time_end = 0;
        Date date_start = new Date();//选完的开始时间
        Date date_end = new Date();//选完的结束时间
        try {
            date_start = format.parse(currrentYMD_start + " " + currentHour_start + ":" + currentMin_start);
            date_end = format.parse(currrentYMD_end + " " + currentHour_end + ":" + currentMin_end);
            LogUtils.e(TAG, "date_start = " + date_start.getTime() + ",date_end = " + date_end.getTime());

            time_start = date_start.getTime();
            time_end = date_end.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtils.e(TAG,"ParseException e=-="+e);
        }

        //判断开始结束时间大小
        LogUtils.e(TAG,"date_end.getTime()="+date_end.getTime()+",date_start.getTime()="+date_start.getTime());
        if(charge_model==Constant.ORDER_ROOM_FEE_DAY){

            //判断是否是大于一天 "yyyy-MM-dd HH:mm"
            long dxMill = date_end.getTime() - date_start.getTime();
            LogUtils.e(TAG,"charge_model==Constant.ORDER_ROOM_FEE_DAY:");
            LogUtils.e(TAG,"dxMill="+dxMill);

            if (dxMill>0) {
                //判断是否超出了已选的开始时间
                Long order_last_time_start = Long.valueOf(querryStartTime + "000");
                //Long order_last_time_start = date_start.getTime();

                if(order_last_time_start>Utils.getTimeNoSec(now_time)){//预订单验证
                    if(time_start<Utils.getTimeNoSec(now_time)){
                        Utils.showToast(this,"开始时间不可早于当前时间！");
                        return;
                    }
                }else{//当前正在使用的订单验证
                    if(order_last_time_start>time_start){
                        Utils.showToast(this,"开始时间不可早于当前订单的开始时间！");
                        return;
                    }
                }
                /*if(order_last_time_start==time_end){
                    Utils.showToast(this,"结束时间不可等于当前订单时间！");
                    return;
                }*/
                if(wheelChooseDialog!=null){
                    wheelChooseDialog.dismiss();
                }
                Utils.showWaittingDialog(this, "正在修改时间...");
                LogUtils.e(TAG, "orderid=" + orderBean.getMsg().getOrderid());
                LogUtils.e(TAG, "roomid=" + orderBean.getMsg().getOrder_rooms().get(0).getId());
                LogUtils.e(TAG, "costtype=" + orderBean.getMsg().getOrder_rooms().get(0).getIs_billing());
                LogUtils.e(TAG, "start_time=" + Utils.javaTransforTimeToPHP("" + time_start));
                LogUtils.e(TAG, "end_time=" + Utils.javaTransforTimeToPHP("" + time_end));
                LogUtils.e(TAG,"vipcreate_id="+vipcreate_id);
                if(vipcreate_id==null) {
                    vipcreate_id="0";
                }
                //如果时间已经返回比较返回时间再设置显示；如果没有返回就在返回以后设置时间显示
                String currentTime = Utils.getCurrentTime();
                String token = Utils.getToken(currentTime, this);
                OkHttpUtils.post()
                        .url(UrlConstance.MODIFY_START_END_TIME)
                        .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                        .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                        .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                        .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                        .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                        .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                        .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                        .addParams("id", "" + room_id_room)
                        .addParams("roomid", orderBean.getMsg().getOrder_rooms().get(0).getId())
                        .addParams("costtype", orderBean.getMsg().getOrder_rooms().get(0).getIs_billing())
                        .addParams("start_time", Utils.javaTransforTimeToPHP("" + time_start))
                        .addParams("end_time", Utils.javaTransforTimeToPHP("" + time_end))
                        .addParams("vipcreate_id",vipcreate_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                Utils.dismissWaittingDialog();
                                LogUtils.e(TAG, "提交修改过的时间onError():Exception e=" + e);
                                Utils.showToast(OrderDetailActivity.this, "同步订单时间失败，请检查网络后再试！");
                            }

                            @Override
                            public void onResponse(String response) {
                                LogUtils.e(TAG, "提交修改过的时间=====response=" + response);
                                Utils.dismissWaittingDialog();
                                boolean b = Utils.checkSaveToken(OrderDetailActivity.this, response);
                                if (b) {
                                    parseModifyJson(response);
                                }
                            }
                        });
            }else if(dxMill==0){
                Utils.showToast(this,"结束时间不可等于开始时间！");
            }else{
                Utils.showToast(this,"结束时间不可早于开始时间！");
            }
        }else{
            if (date_end.getTime()>date_start.getTime()) {
                //判断是否超出了已选的开始时间
                //long now_time = Utils.getCurrentTimeMill();
                Long order_last_time_start = Long.valueOf(querryStartTime + "000");
                //Long order_last_time_start = date_start.getTime();

                if(order_last_time_start>now_time){//预订单验证
                    if(time_start<now_time){
                        Utils.showToast(this,"开始时间不可早于当前时间！");
                        return;
                    }
                }else{//当前正在使用的订单验证
                    if(order_last_time_start>time_start){
                        Utils.showToast(this,"开始时间不可早于当前订单的开始时间！");
                        return;
                    }
                }
                /*if(order_last_time_start==time_end){
                    Utils.showToast(this,"结束时间不可等于当前订单时间！");
                    return;
                }*/
                /*if(order_last_time_start>time_end){
                    Utils.showToast(this,"结束时间不可早于当前订单时间！");
                    return;
                }*/
                if(wheelChooseDialog!=null){
                    wheelChooseDialog.dismiss();
                }
                Utils.showWaittingDialog(this, "正在修改时间...");
                LogUtils.e(TAG, "id=" + orderBean.getMsg().getOrderid());
                LogUtils.e(TAG, "roomid=" + orderBean.getMsg().getOrder_rooms().get(0).getId());
                LogUtils.e(TAG, "costtype=" + orderBean.getMsg().getOrder_rooms().get(0).getIs_billing());
                LogUtils.e(TAG, "start_time=" + Utils.javaTransforTimeToPHP("" + time_start));
                LogUtils.e(TAG, "end_time=" + Utils.javaTransforTimeToPHP("" + time_end));
                LogUtils.e(TAG,"vipcreate_id="+vipcreate_id);
                if(vipcreate_id==null) {
                    vipcreate_id="0";
                }
                //如果时间已经返回比较返回时间再设置显示；如果没有返回就在返回以后设置时间显示
                String currentTime = Utils.getCurrentTime();
                String token = Utils.getToken(currentTime, this);
                OkHttpUtils.post()
                        .url(UrlConstance.MODIFY_START_END_TIME)
                        .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                        .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                        .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                        .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                        .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                        .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                        .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                        .addParams("id", "" + room_id_room)
                        .addParams("roomid", orderBean.getMsg().getOrder_rooms().get(0).getId())
                        .addParams("costtype", orderBean.getMsg().getOrder_rooms().get(0).getIs_billing())
                        .addParams("start_time", Utils.javaTransforTimeToPHP("" + time_start))
                        .addParams("end_time", Utils.javaTransforTimeToPHP("" + time_end))
                        .addParams("vipcreate_id",vipcreate_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                Utils.dismissWaittingDialog();
                                LogUtils.e(TAG, "提交修改过的时间onError():Exception e=" + e);
                                Utils.showToast(OrderDetailActivity.this, "同步订单时间失败，请检查网络后再试！");
                            }

                            @Override
                            public void onResponse(String response) {
                                LogUtils.e(TAG, "提交修改过的时间=====response=" + response);
                                Utils.dismissWaittingDialog();
                                boolean b = Utils.checkSaveToken(OrderDetailActivity.this, response);
                                if (b) {
                                    parseModifyJson(response);
                                }
                            }
                        });
            }else if(date_end.getTime()==date_start.getTime()){
                Utils.showToast(this,"结束时间不可等于开始时间！");
            }else{
                Utils.showToast(this,"结束时间不可早于开始时间！");
            }
        }
    }

    private void justModifyStartTime() {
        LogUtils.e(TAG,"modifyStartEndTime()。。。开始："+currrentYMD_start + " " + currentHour_start + ":" + currentMin_start);
        LogUtils.e(TAG,"modifyStartEndTime()。。。结束："+currrentYMD_end + " " + currentHour_end + ":" + currentMin_end);

        long time_start;
        Date date_start = new Date();//选完的开始时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        try {
            date_start = format.parse(currrentYMD_start + " " + currentHour_start + ":" + currentMin_start);
            LogUtils.e(TAG, "date_start = " + date_start.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtils.e(TAG,"ParseException e=-="+e);
        }
        time_start = date_start.getTime();
        LogUtils.e(TAG,"justModifyStartTime time_start="+time_start);
        LogUtils.e(TAG,"justModifyStartTime Utils.getTimeNoSec(now_time)="+Utils.getTimeNoSec(now_time));

        //判断是否超出了已选的开始时间
        Long order_last_time_start = Long.valueOf(querryStartTime + "000");
        //Long order_last_time_start = date_start.getTime();

        if(order_last_time_start>Utils.getTimeNoSec(now_time)){//预订单验证
            if(time_start<Utils.getTimeNoSec(now_time)){
                Utils.showToast(this,"开始时间不可早于当前时间！");
                return;
            }
        }else{//当前正在使用的订单验证
            if(order_last_time_start>time_start){
                Utils.showToast(this,"开始时间不可早于当前订单的开始时间！");
                return;
            }
        }
        //判断开始时间
        /*if(time_start<Utils.getTimeNoSec(now_time)){
            Utils.showToast(this,"开始时间不可早于当前时间");
            return;
        }*/

        if(wheelChooseDialog!=null){
            wheelChooseDialog.dismiss();
        }

        Utils.showWaittingDialog(this, "正在修改时间...");
        LogUtils.e(TAG, "id=" + room_id_room);
        LogUtils.e(TAG, "roomid=" + orderBean.getMsg().getOrder_rooms().get(0).getId());
        LogUtils.e(TAG, "costtype=" + orderBean.getMsg().getOrder_rooms().get(0).getIs_billing());
        LogUtils.e(TAG, "start_time=" + Utils.javaTransforTimeToPHP("" + time_start));
        LogUtils.e(TAG, "end_time=0");
        LogUtils.e(TAG,"vipcreate_id="+vipcreate_id);
        if(vipcreate_id==null) {
            vipcreate_id="0";
        }
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        OkHttpUtils.post()
                .url(UrlConstance.MODIFY_START_END_TIME)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("id", "" + room_id_room)
                .addParams("roomid", orderBean.getMsg().getOrder_rooms().get(0).getId())
                .addParams("costtype", orderBean.getMsg().getOrder_rooms().get(0).getIs_billing())
                .addParams("start_time", Utils.javaTransforTimeToPHP("" + time_start))
                .addParams("end_time", "0")
                .addParams("vipcreate_id",vipcreate_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Utils.dismissWaittingDialog();
                        LogUtils.e(TAG, "提交修改过的时间onError():Exception e=" + e);
                        Utils.showToast(OrderDetailActivity.this, "同步订单时间失败，请检查网络后再试！");
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "提交修改过的时间=====response=" + response);
                        Utils.dismissWaittingDialog();
                        boolean b = Utils.checkSaveToken(OrderDetailActivity.this, response);
                        if (b) {
                            parseModifyJson(response);
                        }
                    }
                });

    }

    /**
     * 解析提交数据
     * @param response
     */
    private void parseModifyJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {//同步时间成功：设置显示
                LogUtils.e(TAG, "修改时间成功！");
                initData();
            } else if (code == 10125) {//同步时房间使用范围发生变化
                //无效的开始时间
                ModifyTimeBean modifyTimeBean = new Gson().fromJson(response, ModifyTimeBean.class);
                showReservationPopWindow(modifyTimeBean.getMsg());
            } else {
                Utils.showToast(this, "修改房间订单时间失败!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示取消订单Dialog
     */
    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("取消订单")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //撤销订单结果已经在network进行处理
                        netWorks.cancelRoomOrder(orderBean, OrderDetailActivity.this);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 打印中的dialog
     */
    private void showPrintingDia() {
        View printingDiaView = View.inflate(this, R.layout.printing_dialog, null);
        ImageView iv_close = (ImageView) printingDiaView.findViewById(R.id.iv_close);
        alertDialog = new AlertDialog.Builder(this)
                .setView(printingDiaView)
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = 0.9f;
        window.setAttributes(attributes);
        window.setLayout(Utils.dip2px(this, 212), LinearLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK;
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeMessages(UPDATE_PRINT_TIME);
                dismisPrintingDia();
            }
        });
    }

    /**
     * wheelView选择时间
     */
    private void showTimePicker() {
        View pupView = View.inflate(this, R.layout.pop_item_new, null);
        TextView tv_finish_choose = (TextView) pupView.findViewById(R.id.tv_finish_choose);
        TextView tv_time_choose_cancel = (TextView) pupView.findViewById(R.id.tv_time_choose_cancel);
        final LinearLayout ll_end_time = (LinearLayout) pupView.findViewById(R.id.ll_end_time);
        final LinearLayout ll_left_end = (LinearLayout) pupView.findViewById(R.id.ll_left_end);
        TextView tv_temp_no_choose = (TextView) pupView.findViewById(R.id.tv_temp_no_choose);
        final LinearLayout ll_set_end_time = (LinearLayout) pupView.findViewById(R.id.ll_set_end_time);
        tv_time_choose_title = (TextView) pupView.findViewById(R.id.tv_time_choose_title);

        tv_finish_choose.setOnClickListener(this);
        tv_time_choose_cancel.setOnClickListener(this);
        tv_temp_no_choose.setOnClickListener(this);

        if(orderBean.getMsg().getOrder_rooms()!=null&&orderBean.getMsg().getOrder_rooms().size()>0){
            if("0".equals(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())){
                isModifyEndTime = false;
                ll_set_end_time.setVisibility(View.VISIBLE);
                ll_end_time.setVisibility(View.GONE);
                ll_left_end.setVisibility(View.GONE);
            }else{
                isModifyEndTime = true;
                ll_set_end_time.setVisibility(View.GONE);
                ll_end_time.setVisibility(View.VISIBLE);
                ll_left_end.setVisibility(View.VISIBLE);
            }
        }

        initWheelViewNew(pupView);
        wheelChooseDialog = new AlertDialog.Builder(this)
                .setView(pupView)
                .show();
        Window window = wheelChooseDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //设置结束时间
        ll_set_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_set_end_time.setVisibility(View.GONE);
                ll_end_time.setVisibility(View.VISIBLE);
                ll_left_end.setVisibility(View.VISIBLE);
                isModifyEndTime = true;
            }
        });
        tv_temp_no_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_set_end_time.setVisibility(View.VISIBLE);
                ll_end_time.setVisibility(View.GONE);
                ll_left_end.setVisibility(View.GONE);
                isModifyEndTime = false;
            }
        });
    }

    private void initWheelViewNew(View view) {
        Calendar c = Calendar.getInstance();
        int curYear_start = c.get(Calendar.YEAR);//年
        int curMonth_start = c.get(Calendar.MONTH) + 1;//月
        int curDate_start = c.get(Calendar.DATE);//日
        String currenthh_start = new SimpleDateFormat("HH").format(c.getTime());//时
        String currentmm_start = new SimpleDateFormat("mm").format(c.getTime());//分
        long currnetYMD_start = System.currentTimeMillis();

        int curYear_end = c.get(Calendar.YEAR);//年
        int curMonth_end = c.get(Calendar.MONTH) + 1;//月
        int curDate_end = c.get(Calendar.DATE);//日
        String currenthh_end = new SimpleDateFormat("HH").format(c.getTime());//时
        String currentmm_end = new SimpleDateFormat("mm").format(c.getTime());//分
        long currnetYMD_end = System.currentTimeMillis();
        if(!"".equals(querryStartTime)&&!"0".equals(querryStartTime)){
            //开始时间的初始化:如果选择了就用选择的 未选择就直接用当前
            String datetimeStampToString = Utils.getDatetimeStampToString(querryStartTime + "000");//yyyy-MM-dd HH:mm:ss
            curYear_start = Integer.valueOf(datetimeStampToString.substring(0, 4));
            curMonth_start = Integer.valueOf(datetimeStampToString.substring(5, 7));
            curDate_start = Integer.valueOf(datetimeStampToString.substring(8, 10));
            currenthh_start = datetimeStampToString.substring(11, 13);
            currentmm_start = datetimeStampToString.substring(14, 16);
            currnetYMD_start = Long.valueOf(querryStartTime + "000");
        }
        LogUtils.e(TAG,"test121 currrentYMD_end="+currrentYMD_end+",currentHour_end="+currentHour_end+",currentMin_end="+currentMin_end);
        LogUtils.e(TAG,"test121");
        //if(currrentYMD_end!=null&&!"00".equals(currentHour_end)&&!"00".equals(currentMin_end)){
        if(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time()==null||"0".equals(orderBean.getMsg().getOrder_rooms().get(0).getEnd_time())){
            LogUtils.e(TAG,"test121");
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            Date parse = new Date();
            try {
                parse = format.parse(currrentYMD_end + " " + currentHour_end + ":" + currentMin_end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String datetimeStampToString = Utils.getDatetimeStampToString(System.currentTimeMillis());
            curYear_end = Integer.valueOf(datetimeStampToString.substring(0, 4));
            curMonth_end = Integer.valueOf(datetimeStampToString.substring(5, 7));
            curDate_end = Integer.valueOf(datetimeStampToString.substring(8, 10));
            currenthh_end = datetimeStampToString.substring(11, 13);
            currentmm_end = datetimeStampToString.substring(14, 16);
            currnetYMD_end = parse.getTime();
        }else{
            LogUtils.e(TAG,"test121");
            if(!"".equals(querryEndTime)&&!"0".equals(querryEndTime)){
                //结束时间的初始化：如果选择了就用选择的 未选择就直接用当前
                String datetimeStampToString2 = Utils.getDatetimeStampToString(querryEndTime + "000");//yyyy-MM-dd HH:mm:ss
                curYear_end = Integer.valueOf(datetimeStampToString2.substring(0, 4));
                curMonth_end = Integer.valueOf(datetimeStampToString2.substring(5, 7));
                curDate_end = Integer.valueOf(datetimeStampToString2.substring(8, 10));
                currenthh_end = datetimeStampToString2.substring(11, 13);
                currentmm_end = datetimeStampToString2.substring(14, 16);
                currnetYMD_end = Long.valueOf(querryEndTime + "000");
            }else{//04221427
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                Date parse = new Date();
                LogUtils.Log("prase=="+parse);
                String datetimeStampToString = Utils.getDatetimeStampToString(parse.getTime());
                curYear_end = Integer.valueOf(datetimeStampToString.substring(0, 4));
                curMonth_end = Integer.valueOf(datetimeStampToString.substring(5, 7));
                curDate_end = Integer.valueOf(datetimeStampToString.substring(8, 10));
                currenthh_end = datetimeStampToString.substring(11, 13);
                currentmm_end = datetimeStampToString.substring(14, 16);
                currnetYMD_end = parse.getTime();
            }
        }

        wl_ymd = (WheelView) view.findViewById(R.id.wl_ymd);
        wl_week = (WheelView) view.findViewById(R.id.wl_week);
        wl_hour = (WheelView) view.findViewById(R.id.wl_hour);
        wl_min = (WheelView) view.findViewById(R.id.wl_min);

        wl_ymd_end = (WheelView) view.findViewById(R.id.wl_ymd_end);
        wl_week_end = (WheelView) view.findViewById(R.id.wl_week_end);
        wl_hour_end = (WheelView) view.findViewById(R.id.wl_hour_end);
        wl_min_end = (WheelView) view.findViewById(R.id.wl_min_end);


        //开始时间wheel
        ArrayWheelAdapter<String> weekAdapter = new ArrayWheelAdapter<>(this, ymdData);
        List<String> ymdList = Arrays.asList(ymdData);
        wl_ymd.setViewAdapter(weekAdapter);
        weekAdapter.setTextSize(18);
        wl_ymd.setCyclic(true);
        wl_week.setVisibility(View.GONE);
        wl_ymd.setCurrentItem(ymdList.indexOf(GetTimeUtil.getYMDTime(currnetYMD_start)));
        wl_ymd.addChangingListener(new MyStartOnWheelChangeListener());

        ArrayWheelAdapter<String> weekAdapter2 = new ArrayWheelAdapter<String>(this, week_str);
        wl_week.setViewAdapter(weekAdapter2);
        weekAdapter2.setTextSize(18);
        wl_week.setEnabled(false);
        wl_week.setCyclic(true);
        changeWheelWeek(curYear_start, curMonth_start, curDate_start);
        NumericWheelAdapter numericAdapter1 = new NumericWheelAdapter(this, 0, 23);
        numericAdapter1.setLabel("时");
        numericAdapter1.setTextSize(18);
        wl_hour.setViewAdapter(numericAdapter1);
        wl_hour.setCyclic(true);// 可循环滚动
        wl_hour.addChangingListener(new MyStartOnWheelChangeListener());

        NumericWheelAdapter numericAdapter2 = new NumericWheelAdapter(this, 0, 59);
        numericAdapter2.setLabel("分");
        numericAdapter2.setTextSize(18);
        wl_min.setViewAdapter(numericAdapter2);
        wl_min.setCyclic(true);// 可循环滚动
        wl_min.addChangingListener(new MyStartOnWheelChangeListener());

        List<String> asList = Arrays.asList(xiaoshi_start);
        int hour_index = asList.indexOf(currenthh_start);
        wl_hour.setCurrentItem(hour_index);

        List<String> asList2 = Arrays.asList(fenzhong_start);
        int min_index = asList2.indexOf(currentmm_start);
        wl_min.setCurrentItem(min_index);

        //结束时间wheel
        ArrayWheelAdapter<String> weekAdapter_end = new ArrayWheelAdapter<>(this, ymdData);
        List<String> ymdList_end = Arrays.asList(ymdData);
        wl_ymd_end.setViewAdapter(weekAdapter_end);
        weekAdapter.setTextSize(18);
        wl_ymd_end.setCyclic(true);
        wl_week_end.setVisibility(View.GONE);
        wl_ymd_end.setCurrentItem(ymdList_end.indexOf(GetTimeUtil.getYMDTime(currnetYMD_end)));
        wl_ymd_end.addChangingListener(new MyEndOnWheelChangeListener());

        ArrayWheelAdapter<String> weekAdapter2_end = new ArrayWheelAdapter<String>(this, week_str);
        wl_week_end.setViewAdapter(weekAdapter2_end);
        weekAdapter2.setTextSize(18);
        wl_week_end.setEnabled(false);
        wl_week_end.setCyclic(true);
        changeWheelWeek(curYear_end, curMonth_end, curDate_end);
        NumericWheelAdapter numericAdapter1_end = new NumericWheelAdapter(this, 0, 23);
        numericAdapter1_end.setLabel("时");
        numericAdapter1_end.setTextSize(18);
        wl_hour_end.setViewAdapter(numericAdapter1_end);
        wl_hour_end.setCyclic(true);// 可循环滚动
        wl_hour_end.addChangingListener(new MyEndOnWheelChangeListener());

        NumericWheelAdapter numericAdapter2_end = new NumericWheelAdapter(this, 0, 59);
        numericAdapter2_end.setLabel("分");
        numericAdapter2_end.setTextSize(18);
        wl_min_end.setViewAdapter(numericAdapter2_end);
        wl_min_end.setCyclic(true);// 可循环滚动
        wl_min_end.addChangingListener(new MyEndOnWheelChangeListener());

        List<String> asList_end = Arrays.asList(xiaoshi_start);
        int hour_index_end = asList_end.indexOf(currenthh_end);
        wl_hour_end.setCurrentItem(hour_index_end);

        List<String> asList2_end = Arrays.asList(fenzhong_start);
        int min_index_end = asList2_end.indexOf(currentmm_end);
        wl_min_end.setCurrentItem(min_index_end);
    }
    /**
     * 修改价格
     */
    private void changeMoney() {
        View restDialog = View.inflate(this, R.layout.reset_count_dialog, null);
        et_new_price = (EditText) restDialog.findViewById(R.id.et_new_price);
        tv_current_money = (TextView) restDialog.findViewById(R.id.tv_current_money);
        btn_finish_change = (TextView) restDialog.findViewById(R.id.btn_finish_change);
        btn_cancel_change = (TextView) restDialog.findViewById(R.id.btn_cancel_change);
        et_modify_price_reason = (EditText) restDialog.findViewById(R.id.et_modify_price_reason);
        tv_unit = (TextView) restDialog.findViewById(R.id.tv_unit);
        //折扣
        tv_countway_notice = (TextView) restDialog.findViewById(R.id.tv_countway_notice);
        tv_change_countway = (TextView) restDialog.findViewById(R.id.tv_change_countway);
        tv_after_discount = (TextView) restDialog.findViewById(R.id.tv_after_discount);

        tv_change_countway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCoutWayNotice(tv_change_countway.getText().toString());
            }
        });

        tv_current_money.setText("当前金额：" + Utils.keepTwoDecimal(payment) + "元");
        btn_finish_change.setOnClickListener(this);
        btn_cancel_change.setOnClickListener(this);
        FormatUtils.accuracy=2;
        FormatUtils.setPricePoint(et_new_price,null);
        changePriceDialog = new AlertDialog.Builder(this)
                .setView(restDialog)
                .show();
    }
    private void setDiscountAccount(String text){
        SpannableStringBuilder builder=new SpannableStringBuilder(text);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.rgb(233,160,60));
        builder.setSpan(span,5,text.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_after_discount.setText(builder);
    }
    private void changeCoutWayNotice(String currentText) {
        if("按折扣改价".equals(currentText)) {
            et_new_price.setText(null);
            setDiscountAccount("折后金额为"+(payment)+"元");
            tv_countway_notice.setText("如需直接输入金额，");
            tv_change_countway.setText("请点此返回");
            et_new_price.setHint("请输入折扣(1~9.9),如8折输入8");
            FormatUtils.accuracy=1;
            FormatUtils.setPricePoint(et_new_price, new FormatUtils.OnInputChangeListener() {
                @Override
                public void inputChange() {
                    String currentInput = et_new_price.getText().toString();
                    if(!TextUtils.isEmpty(currentInput)) {
                        Double total = Double.valueOf(payment);
                        if("折".equals(tv_unit.getText().toString())) {
                           tv_after_discount.setVisibility(View.VISIBLE);
                           Double discount = Double.valueOf(currentInput);
                           setDiscountAccount("折后金额为"+Utils.keepTwoDecimal(total*discount/10+"")+"元");
                        }
                    }else {
                        tv_after_discount.setVisibility(View.GONE);
                    }
                }
            });
            tv_unit.setText("折");
        }else {
            et_new_price.setText(null);
            tv_after_discount.setVisibility(View.GONE);
            tv_countway_notice.setText("如需打折，请选择");
            tv_change_countway.setText("按折扣改价");
            et_new_price.setHint("请输入修改后的金额");
            FormatUtils.accuracy=2;
            FormatUtils.setPricePoint(et_new_price,null);
            tv_unit.setText("元");
        }
    }

    private void showPayPopWindow() {

        Constant.UNKNOWN_COMMODITY = "0";
        //显示popWindow
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popView = inflater.inflate(R.layout.pop_order_pay_way, null);
        TextView tv_total_pay_money = (TextView) popView.findViewById(R.id.tv_total_pay_money);
        TextView tv_micro_pay = (TextView) popView.findViewById(R.id.tv_micro_pay);
        TextView tv_ali_pay = (TextView) popView.findViewById(R.id.tv_ali_pay);
        tv_total_pay_money.setText(tv_discount_total_money.getText().toString());
        RelativeLayout rl_member_pay_money = (RelativeLayout) popView.findViewById(R.id.rl_member_pay_money);
        if(orderBean.getMsg().getVoucherinfo() != null){
            rl_member_pay_money.setVisibility(View.GONE);
        }else{
            if(memberBean==null) {
                rl_member_pay_money.setVisibility(View.GONE);
            }else {
                rl_member_pay_money.setVisibility(View.VISIBLE);
            }
        }
        if(Constant.OPEN_ALIPAY != 1){
            tv_ali_pay.setText("支付宝收款(记账)");
        }
        if(Constant.OPEN_WXPAY != 1){
            tv_micro_pay.setText("微信收款(记账)");
        }
        popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setAlpha(0.6f);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        popupWindow.showAtLocation(OrderDetailActivity.this.findViewById(R.id.arl_this_order_detail), Gravity.BOTTOM, 0, 0);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1);
            }
        });
        //取消付款
        popView.findViewById(R.id.tv_cancel_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlpha(1);
                popupWindow.dismiss();
            }
        });
        //微信付款
        popView.findViewById(R.id.rl_wx_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.payway = "0";
                payMoney();
            }
        });
        //支付宝付款
        popView.findViewById(R.id.rl_zfb_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.payway = "1";
                payMoney();
            }
        });
        //刷卡记账
        popView.findViewById(R.id.rl_card_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Constant.payway = "2";
                Constant.payway = "6";
                payMoney();
            }
        });
        //现金记账
        popView.findViewById(R.id.rl_cash_pay_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.payway = "3";
                payMoney();
            }
        });
        //会员卡余额支付
        rl_member_pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.payway = "4";
                payMoney();
            }
        });
    }

    /**
     * 不同结账方式
     */
    private void payMoney() {
        Constant.LAST_ORDER_NO = "";
        payment = tv_discount_total_money.getText().toString();
        int length = payment.length();
        payment = payment.substring(1, length);
        orderBean.getMsg().setPayment(Double.valueOf(payment));
        switch (Constant.payway) {
            case "0":
                if(Constant.OPEN_WXPAY == 1){
                    if (!judgeNet()) {
                        return;
                    }
                    if(Constant.OPEN_WXPAY_MICRO==3){
                        toPayQRCodeActivity();
                    }else{
                        captureActivitytoPay();
                    }
                }else{
                    //没开通微信：点击标记为微信记账
                    /*if(Utils.judgeIsFirstRemarkPay(OrderDetailActivity.this,new )){
                        Constant.payway = "8";
                        cashCardPay();
                    }*/
                    Utils.judgeIsFirstRemarkPay(OrderDetailActivity.this, new Utils.OnSureToNext() {
                        @Override
                        public void onSure() {
                            Constant.payway = "8";
                            cashCardPay();
                        }
                    });
                }
                break;
            case "1":
                if(Constant.OPEN_ALIPAY == 1){
                    if (!judgeNet()) {
                        return;
                    }
                    captureActivitytoPay();
                }else{
                    //没开通支付宝：点击标记为支付宝记账
                    /*if(Utils.judgeIsFirstRemarkPay(OrderDetailActivity.this)){
                        Constant.payway = "9";
                        cashCardPay();
                    }*/
                    Utils.judgeIsFirstRemarkPay(OrderDetailActivity.this, new Utils.OnSureToNext() {
                        @Override
                        public void onSure() {
                            Constant.payway = "9";
                            cashCardPay();
                        }
                    });
                }
                break;
            case "6"://2->6
            case "3":
                //现金刷卡
                /*if(Utils.judgeIsFirstRemarkPay(OrderDetailActivity.this)){
                    if (judgeRangeNet(true)) {
                        cashCardPay();
                    }
                }*/
                Utils.judgeIsFirstRemarkPay(OrderDetailActivity.this, new Utils.OnSureToNext() {
                    @Override
                    public void onSure() {
                        cashCardPay();
                    }
                });
                break;
            case "4":
                //会员卡余额支付
                if (!judgeRangeNet(false)) {
                    return;
                }
                startMemberPay();
                break;
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 去被扫码页面
     */
    private void toPayQRCodeActivity() {
        if (!judgeNet()) {
            return;
        }
        Constant.LAST_ORDER_NO = "";
        Intent intent = new Intent(OrderDetailActivity.this, PayQRCodeActivity.class);
        /*intent.putExtra("total_fee", order_total_fee);
        intent.putExtra("payment", payment);
        intent.putExtra(Constant.FROME_WHERE, Constant.ORDER_PAGER_DETAIL);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        if(memberBean!=null){//加上会员卡号：
            intent.putExtra("vip_card", memberBean.getMsg().getVipcardid());
            intent.putExtra("vipcreate_id", memberBean.getMsg().getVipcreate_id());
        }*/

        intent.putExtra(Constant.UNPAY_ORDER_ID, unpay_order_no);
        intent.putExtra("order_no_for_order", orderBean.getMsg().getOrder_no());
        if (memberBean != null) {
            intent.putExtra("vip_card", memberBean.getMsg().getVipcardid());
            intent.putExtra("vipcreate_id", memberBean.getMsg().getVipcreate_id());
        } else {
            intent.putExtra("vip_card", "0");
            intent.putExtra("vipcreate_id", "0");
        }
        //intent.putExtra("tradetime", Utils.getTradeTime());
        intent.putExtra("payment", payment);//intent.putExtra(Constant.MONEY, payment);
        intent.putExtra("total_fee", order_total_fee);
        intent.putExtra("vip_discount", orderBean.getMsg().getVip_discount());//折扣
        intent.putExtra(Constant.FROME_WHERE, Constant.ORDER_PAGER_DETAIL);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);//将当前订单携带至captureActivity，支付成功后再携带至收款成功界面
        intent.putExtra("cardinfo_pay", cardinfo_pay);

        startActivity(intent);
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    /**
     * 判断所有的支付条件
     * @return
     */
    private boolean judgeNet() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Utils.showToast(this, this.getResources().getString(R.string.no_network_please_cash_card));
            return false;
        } else {
            return true;
        }
    }

    /**
     * 会员余额支付
     */
    private void startMemberPay() {
        trade_num = Utils.getTradeNum();
        //query_num = Utils.getBQueryNum(this);
        trade_time = Utils.getTradeTime();//秒
        showWaittingPopWindow();
        netWorks.PayCard(getParams(), netCallBack, MEMBER_PAY);
    }

    /**
     * 显示等待的PopWindow
     */
    private void showWaittingPopWindow() {

        recLen = Constant.RECLEN_TIME;
        //增加PopWindow
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_waitting_layout, null);
        view.setBackgroundColor(Color.argb(100,0, 0, 0));
        tv_cout_timmer = (TextView) view.findViewById(R.id.tv_cout_timmer);
        LinearLayout ll_return_pre = (LinearLayout) view.findViewById(R.id.ll_return_pre);
        waittingPop = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        waittingPop.setOutsideTouchable(false);
        waittingPop.setFocusable(true);
        waittingPop.showAtLocation(findViewById(R.id.arl_this_order_detail), Gravity.CENTER_HORIZONTAL, 0, 0);//设置为取景框中间

        //设置倒计时
        handler.sendEmptyMessage(CUT_DOWN_TIMMER);

        //设置新的弹出框提示:返回收款界面或者订单列
        ll_return_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClickCancel = true;
                dismissWaittingPopWindow();
                showSeeResultPopWindow(MAY_PAY_SUCCESS);
            }
        });
    }

    /**
     * 显示支付结果说明 PopWindow
     */
    private void showSeeResultPopWindow(int tag) {

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.unknow_pay_result, null);
        TextView tv_return = (TextView) view.findViewById(R.id.tv_return);
        TextView tv_see_orders = (TextView) view.findViewById(R.id.tv_see_orders);
        TextView tv_title_pay_result = (TextView) view.findViewById(R.id.tv_title_pay_result);
        switch (tag){
            case NO_PAY_RESULT:
                tv_title_pay_result.setText(OrderDetailActivity.this.getResources().getString(R.string.no_pay_result));
                break;
            case MAY_PAY_SUCCESS:
                tv_title_pay_result.setText(OrderDetailActivity.this.getResources().getString(R.string.may_pay_success));
                break;
        }
        PopupWindow resultPop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        resultPop.setOutsideTouchable(false);
        resultPop.setFocusable(true);
        resultPop.showAtLocation(findViewById(R.id.arl_this_order_detail), Gravity.CENTER_HORIZONTAL, 0, 0);//设置为取景框中间

        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
                OrderDetailActivity.this.finish();
            }
        });
        tv_see_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
                OrderDetailActivity.this.finish();
                startActivity(new Intent(OrderDetailActivity.this, PosOrderActivity.class));
            }
        });
    }

    /**
     * 显示预定时间信息 PopWindow
     */
    private void showReservationPopWindow(List<ModifyTimeBean.MsgBean> infoBeans) {

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.reservation_pop, null);

        ListView lv_reservation_info = (ListView) view.findViewById(R.id.lv_reservation_info);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);

        final PopupWindow reservationPop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        reservationPop.setOutsideTouchable(false);
        reservationPop.setFocusable(true);
        reservationPop.showAtLocation(findViewById(R.id.arl_this_order_detail), Gravity.CENTER_HORIZONTAL, 0, 0);//设置为取景框中间

        //设置adapter
        lv_reservation_info.setAdapter(new ReservationAdapter(this,infoBeans));
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reservationPop!=null){
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 1;
                    getWindow().setAttributes(params);
                    reservationPop.dismiss();
                }
            }
        });
    }

    private void cutDownTimer() {
        recLen--;
        tv_cout_timmer.setText("" + recLen);
        if (recLen < Constant.RECLEN_TIME&&0 < recLen) {
            Message message = handler.obtainMessage(CUT_DOWN_TIMMER);
            handler.sendMessageDelayed(message, 1000);      // send message
        } else {
            isTimeGone = true;
            dismissWaittingPopWindow();
            showSeeResultPopWindow(NO_PAY_RESULT);
        }
    }

    private void dismissWaittingPopWindow() {
        if (waittingPop != null) {
            handler.removeMessages(CUT_DOWN_TIMMER);
            waittingPop.dismiss();
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);
        }
    }
    /**
     * vipcreate_id 获取折扣信息
     *
     * @return1992
     */
    private PayParams getParams() {
        PayParams paramsBean = new PayParams();
        paramsBean.setType(Constant.UNKNOWN_COMMODITY);
        paramsBean.setPayWay(Constant.payway);
        paramsBean.setOrderNo(trade_num);
        paramsBean.setOrderId(orderBean.getMsg().getOrderid() + "");
        paramsBean.setPayMent(payment);
        paramsBean.setTotal_fee(payment + "");
        if(memberBean!=null&&memberBean.getMsg()!=null){
            paramsBean.setVipId(memberBean.getMsg().getVipcardid());
            paramsBean.setVipcreate_id(memberBean.getMsg().getVipcreate_id());
        }
        paramsBean.setCardId("0");
        paramsBean.setLast_order_no(Constant.LAST_ORDER_NO);
        paramsBean.setCardinfo(cardinfo_pay);
        if(orderBean!=null){
            if(orderBean.getMsg()!=null){
                if(orderBean.getMsg().getRoom()!=null){
                    paramsBean.setRoom(new Gson().toJson(orderBean.getMsg().getRoom()));
                }
            }
        }
        return paramsBean;
    }

    /**
     * 现金或者刷卡支付
     */
    private void cashCardPay() {
        trade_num = Utils.getTradeNum();
        //query_num = Utils.getBQueryNum(this);
        trade_time = Utils.getTradeTime();//秒
        if(orderBean!=null){
            if(orderBean.getMsg()!=null){
                if(orderBean.getMsg().getRoom()!=null&&!"".equals(orderBean.getMsg().getRoom().getEnd_time())){
                    trade_time = orderBean.getMsg().getRoom().getEnd_time();
                }
            }
        }
        SavedFailOrder cardOrder;
        if(memberBean!=null) {
            String vipcardid = memberBean.getMsg().getVipcardid();
            cardOrder = Utils.getSaveOrder(String.valueOf(orderBean.getMsg().getOrder_no()), trade_num,
                    String.valueOf(orderBean.getMsg().getOrderid()), order_total_fee, query_num, trade_time,
                    "1", "0", Utils.keepTwoDecimal(payment), vipcardid, "0", "0",vipcreate_id,"0",cardinfo_pay);//type="0" 代表订单收款
        }else {
             cardOrder = Utils.getSaveOrder(String.valueOf(orderBean.getMsg().getOrder_no()), trade_num,
                     String.valueOf(orderBean.getMsg().getOrderid()), order_total_fee, query_num, trade_time,
                     "1", "0", Utils.keepTwoDecimal(payment), "0", "0", "0",vipcreate_id,"0",cardinfo_pay);//type="0" 代表订单收款
        }
        CashCardPayUtils.getInstance().cashCardPay(this, cardOrder, handler, orderBean);
    }

    /**
     * 跳到captureactivity去支付
     */
    private void captureActivitytoPay() {
        Intent intent = new Intent(OrderDetailActivity.this, CaptureActivity.class);
        intent.putExtra(Constant.UNPAY_ORDER_ID, unpay_order_no);
        intent.putExtra("order_no_for_order", orderBean.getMsg().getOrder_no());
        if (memberBean != null) {
            intent.putExtra("vip_card", memberBean.getMsg().getVipcardid());
        } else {
            intent.putExtra("vip_card", "0");
        }
        intent.putExtra("tradetime", Utils.getTradeTime());
        intent.putExtra("payment", payment);
        intent.putExtra("total_fee", order_total_fee);
        intent.putExtra(Constant.FROME_WHERE, Constant.ORDER_PAGER_DETAIL);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);//将当前订单携带至captureActivity，支付成功后再携带至收款成功界面
        intent.putExtra("cardinfo_pay",cardinfo_pay);

        startActivity(intent);
    }

    /**
     * 返回false不可玩下进行
     * @param isCrash
     * @return
     */
    private boolean judgeRangeNet(boolean isCrash) {
        Double money = Double.valueOf(payment);
        LogUtils.e(TAG, "payment====" + payment);
        if (money > 999999.99) {
            Utils.showToast(this, this.getResources().getString(R.string.pay_money_max));
            return false;
        }
        if (money < 0.01) {
            Utils.showToast(this, this.getResources().getString(R.string.pay_money_min));
            return false;
        }
        if (isCrash) {//现金收款不受网络限制
            return true;
        } else {//微信支付宝判断网络状态
            if (!NetworkUtils.isNetworkAvailable(this)) {
                Utils.showToast(this, this.getResources().getString(R.string.no_network_please_cash_card));
                return false;
            }
        }

        return true;
    }

    /**
     * 请求支付宝或者微信是否开通支付
     */
    private void requireForWXZFB(final int tag) {
        LogUtils.e(TAG, "请求支付宝(1)或者微信(0)是否开通支付：" + tag);
        LogUtils.e(TAG, "Constant.ADV_ID:" + Constant.ADV_ID);
        String currentTime = Utils.getCurrentTime();
        String token = Utils.getToken(currentTime, this);
        OkHttpUtils.post()
                .url(UrlConstance.QUERY_PAY_STATUS)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams("adv_id", Constant.ADV_ID)
                .build()
                .writeTimeOut(5000)
                .readTimeOut(5000)
                .connTimeOut(5000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        switch (tag) {
                            case 0:
                                Utils.showToast(OrderDetailActivity.this, "获取微信开通状态失败，请检查网络后再试");
                                break;
                            case 1:
                                Utils.showToast(OrderDetailActivity.this, "获取支付宝开通状态失败，请检查网络后再试");
                                break;
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG, "response==" + response);
                        boolean success = Utils.checkSaveToken(OrderDetailActivity.this, response);
                        if (success) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 200) {//返回成功
                                    int certifite = jsonObject.getJSONObject("msg").getInt("certifite");
                                    int alipay = jsonObject.getJSONObject("msg").getInt("alipay");
                                    int micro = jsonObject.getJSONObject("msg").getInt("micro");
                                    Constant.OPEN_ALIPAY = alipay;
                                    Constant.OPEN_WXPAY = certifite;
                                    Constant.OPEN_WXPAY_MICRO = micro;
                                    if (tag == 1 && Constant.OPEN_ALIPAY == 1) {//支付宝
                                        captureActivitytoPay();
                                    } else if (tag == 0 && Constant.OPEN_WXPAY == 1) {//微信
                                        if (Constant.OPEN_WXPAY_MICRO == 3) {
                                            toPayQRCodeActivity();
                                        } else {
                                            captureActivitytoPay();
                                        }
                                    } else {
                                        showNoBandPay(tag);
                                    }
                                } else {
                                    showNoBandPay(tag);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 获取订单json字符串
     * @return
     */
    private String getCommitOrderJson() {
        Constant.localOrderBean.setTradeNum("" + orderBean.getMsg().getOrderid());
        return new Gson().toJson(Constant.localOrderBean);
    }

    /**
     * 展示没有绑定提示的Dialog
     * @param tag
     */
    private void showNoBandPay(int tag) {
        String dialogMessage = "";
        if (tag == 1) {//支付宝
            dialogMessage = OrderDetailActivity.this.getResources().getString(R.string.click_zfb_no_band);
        } else if (tag == 0) {//微信
            dialogMessage = OrderDetailActivity.this.getResources().getString(R.string.click_wx_no_band);
        }
        AlertDialog errTokenDia;
        errTokenDia = new AlertDialog.Builder(OrderDetailActivity.this)
                .setMessage(dialogMessage)
                .setPositiveButton(OrderDetailActivity.this.getResources().getString(R.string.determine), null)
                .show();
        errTokenDia.setCancelable(false);
        errTokenDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }

    private void setAlpha(float alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        getWindow().setAttributes(params);
    }

    /**
     * 获取券信息
     */
    private String getCardinfoJson() {
        if(orderBean.getMsg().getVoucherinfo()!=null){
            containsVoucher=true;
            PayCardinfoBean payCardinfoBean = new PayCardinfoBean();
            if(orderBean.getMsg().getVoucherinfo().getCard_type()!=null){
                switch (orderBean.getMsg().getVoucherinfo().getCard_type()){
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
        }else {
            containsVoucher=false;
        }
        return "";
    }

    /**
     * 滚动的监听
     */
    class MyStartOnWheelChangeListener implements OnWheelChangedListener {

        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            switch (wheel.getId()) {
                case R.id.wl_ymd:
                    String value = ymdData[newValue];
                    int year = Integer.parseInt(value.substring(0, value.indexOf("-")));
                    int month = Integer.parseInt(value.substring(value.indexOf("-") + 1, value.lastIndexOf("-")));
                    int day = Integer.parseInt(value.substring(value.lastIndexOf("-") + 1, value.length()));
                    changeWheelWeek(year, month, day);
                    String month_str;
                    String day_str;
                    if (month < 10) {
                        month_str = "0" + month;
                    } else {
                        month_str = "" + month;
                    }
                    if (day < 10) {
                        day_str = "0" + day;
                    } else {
                        day_str = "" + day;
                    }
                    currrentYMD_start = year + "年" + month_str + "月" + day_str + "日";
                    break;
                case R.id.wl_hour:
                    if (newValue < 10) {
                        currentHour_start = "0" + newValue;
                    } else {
                        currentHour_start = newValue + "";
                    }
                    break;
                case R.id.wl_min:
                    if (newValue < 10) {
                        currentMin_start = "0" + newValue;
                    } else {
                        currentMin_start = newValue + "";
                    }
                    break;
            }
        }
    }
    /**
     * 滚动的监听
     */
    class MyEndOnWheelChangeListener implements OnWheelChangedListener {

        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            switch (wheel.getId()) {
                case R.id.wl_ymd_end:
                    String value = ymdData[newValue];
                    int year = Integer.parseInt(value.substring(0, value.indexOf("-")));
                    int month = Integer.parseInt(value.substring(value.indexOf("-") + 1, value.lastIndexOf("-")));
                    int day = Integer.parseInt(value.substring(value.lastIndexOf("-") + 1, value.length()));
                    changeWheelWeek(year, month, day);
                    String month_str;
                    String day_str;
                    if (month < 10) {
                        month_str = "0" + month;
                    } else {
                        month_str = "" + month;
                    }
                    if (day < 10) {
                        day_str = "0" + day;
                    } else {
                        day_str = "" + day;
                    }
                    currrentYMD_end = year + "年" + month_str + "月" + day_str + "日";
                    break;
                case R.id.wl_hour_end:
                    if (newValue < 10) {
                        currentHour_end = "0" + newValue;
                    } else {
                        currentHour_end = newValue + "";
                    }
                    break;
                case R.id.wl_min_end:
                    if (newValue < 10) {
                        currentMin_end = "0" + newValue;
                    } else {
                        currentMin_end = newValue + "";
                    }
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        handler.removeCallbacksAndMessages(null);
        LogUtils.Log("onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        LogUtils.e(TAG,"onRestart()");
        if (leftTime > 0) {
            handler.sendEmptyMessage(UPDATE_PRINT_TIME);
        }
        if (Constant.IS_CLOSE_DETAIL_ACTIVITY) {//支付成功
            finish();
        } else {
            initData();
        }
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        unbindService(connService);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void changeWheelWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        wl_week.setCurrentItem(i - 1);
        lastweek = week_str[i - 1];
    }

    private boolean isCurrentActivityTop() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(OrderDetailActivity.class.getName());
    }
}
