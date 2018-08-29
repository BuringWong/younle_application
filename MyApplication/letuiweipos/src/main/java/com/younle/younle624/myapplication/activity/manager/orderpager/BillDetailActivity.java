package com.younle.younle624.myapplication.activity.manager.orderpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter.print.PrintMsgEvent;
import com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter.print.PrinterMsgType;
import com.younle.younle624.myapplication.adapter.PrintInfoAdapter;
import com.younle.younle624.myapplication.adapter.ordermanager.PosDetailAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.EntityPrintBean;
import com.younle.younle624.myapplication.domain.Sender;
import com.younle.younle624.myapplication.domain.VoucherInfo;
import com.younle.younle624.myapplication.domain.orderbean.PosPrintBean;
import com.younle.younle624.myapplication.domain.orderbean.RefundInfo;
import com.younle.younle624.myapplication.domain.paybean.DiscountInfo;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.domain.printsetting.YunPrintGroupBean;
import com.younle.younle624.myapplication.domain.waimai.WmPintData;
import com.younle.younle624.myapplication.myservice.BluetoothService;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.NetworkUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.notice.NoticePopuUtils;
import com.younle.younle624.myapplication.utils.printmanager.BTPrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.PrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.YunPrintUtils;
import com.younle.younle624.myapplication.utils.refund.RefundTranstation;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import driver.HsBluetoothPrintDriver;
import okhttp3.Call;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * 订单详情页
 */
public class BillDetailActivity extends Activity implements View.OnClickListener, SelfLinearLayout.ClickToReload {
    private  final int UPDATE_UI = 2;
    private TextView tv_title;
    private ImageView iv_title;
    private HsBluetoothPrintDriver hs = HsBluetoothPrintDriver.getInstance();

    /**
     * 收入总额
     */
//    private TextView tv_income;
    private TextView voucher_name;
    private TextView tv_room_consume_des;
    /**
     * 打印button
     */
    private Button btn_print;
    private IWoyouService iWoyouService;
    /**
     * 账单数据
     */
    private ListView lv_detail;
    /**
     * 整个页面的跟布局
     */
    private LinearLayout ll_details;
    /**
     * 加载中de布局
     */
    private SelfLinearLayout ll_loading;
    /**
     * progressbar
     */
    private ProgressBar pb_loading;
    /**
     * 加载中。。。
     */
    private TextView tv_loading;
    private ImageView iv_jiazai_filure;
    /**
     * 蓝牙设备
     */
    private PrintInfoAdapter adapter;
    private PosDetailAdapter posAdapter;
    private List<WmPintData> data=new ArrayList<>();
    private List<WmPintData> btdata=new ArrayList<>();
    private List<String> showdata;
    private TextView tv_mark_reback;
    private int fromWhere;
    private String orderId = "";
    private String goodId = "";
    private String sizeId = "";
    private String goodstype = "";
    private String isRechargeLog = "";//0:pos机商品订单，1：会员充值订单 2：小程序商品订单
    /**
     * 用于区分订单类型：1：新订单，小程序下完单直接结账；2：分订单，在小程序端添加商品
     */
    private String type = "";
    //private int isSuccess = 1;
    private String TAG = "BillDetailActivity";
    /**
     * 备注填写入口
     */
    private TextView tv_mark_state;
    private LinearLayout ll_remark;
    private AutoRelativeLayout arl_order_mark_layout;
    private TextView tv_mark_msg;

    //本地打印服务
    public ServiceConnection connService = new ServiceConnection() {
        //连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWoyouService = IWoyouService.Stub.asInterface(service);//拿到打印服务的对象
        }

        //断开服务
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    private AlertDialog afterOpenConnect;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case UPDATE_UI:
                    Toast.makeText(BillDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case Constant.WAIT_BLUETOOTH_OPEN://等待蓝牙打开
                    BTPrintUtils.getInstance().connectBtPrinterTest(mService,BillDetailActivity.this,handler);
                    break;
                case Constant.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(BillDetailActivity.this, "已连接", Toast.LENGTH_SHORT).show();
                            startBtPrint();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(BillDetailActivity.this, "正在连接", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(BillDetailActivity.this, "无连接", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case Constant.ONE_BY_ONE_YUN_PRINT:
                    yun_print_index++;
                    if(yun_print_index< oneByOneYunList.size()) {
                        yunprintApplet();
                    }else {
                        yun_print_index=0;
                    }
                    break;
            }
        }
    };
    private boolean evetRegist = false;
    private View posFooter;
    private List<PosPrintBean.MsgBean.GoodsInfoBean> posData;
    private View header;

    private RelativeLayout rl_discount_coupons_info;
    private ImageView iv_voucher_card;
    private TextView tv_discount_dec;
    private TextView tv_discount_reduce_money;

    /**
     * 退款信息组建
     */
    private LinearLayout ll_refund_msg;
//    private TextView refund_time;
//    private TextView refund_money;
    private TextView tv_out_no;
//    private TextView refund_no;

    private TextView refund_account;
    private View second_depart;
    /**
     * pos的headview 和footView
     */
    private RelativeLayout rl_discount;
    private TextView tv_should_pay;
    private TextView tv_actual_pay;
    private TextView tv_notices;
    private TextView tv_deal_time;//支付时间
    private TextView tv_add_time;//下单时间
    private TextView tv_order_id;
    private TextView tv_trade_num;
    private LinearLayout all_trade_num;
    private TextView tv_pay_way;
    private TextView tv_member_balance;
    private TextView tv_member_number;
    //private ImageView iv_qr_code;
    private LinearLayout all_member_balance;
    private LinearLayout all_member_number;
    private PosPrintBean posBean;
    private DiscountInfo discountInfo;
    private boolean everCoon=false;
    private ImageView iv_callbacking;
    private LinearLayout ll_callback;
    private TextView btn_callback;//分情况显示标记退款还是原路退款
    private List<YunPrintGroupBean> oneByOneYunList;
    private int yun_print_index;

    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
               LogUtils.Log("onserviceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private BluetoothService mService;

    private List<Integer> toastCode= Arrays.asList(20103,20107,20108,20117,20119,20121,20133);
    private int isSetPwd;
    private Map<String, List<WmPintData>> yunPrintMap;
    private String logid="";
    private Sender sender;
    private LinearLayout ll_deliver_msg;
    private TextView tv_peisong_state;
    private TextView tv_deliver;
    private TextView tv_arrive_time;
    private TextView tv_customer_name_tel;
    private TextView tv_customer_add;
    private int deliveryStatus=-1;
    private Button btn_start_send;
    private RelativeLayout rl_deliver_fee;
    private TextView tv_peisong_fee;
    private TextView tv_total_fee_text;
    private List<Sender.DataBean> senderData;
    private List<YunPrintGroupBean> yunPrintGroupBeans;
    private List<YunPrintGroupBean> yunNormalGroupBeans;
    private List<SavedPrinter> savedPrinterses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        Utils.initToolBarState(this);
        EventBus.getDefault().register(this);
        mService=BluetoothService.getInstance();
        mService.setHandler(handler);
//        BTPrintUtils.getInstance().connectBtPrinterTest(mService,this,handler);
//        mService=  BTPrintUtils.getInstance().connectBtPrinter(this, handler);
        getDataTypeFromIntent();
        initView();
        initData();
        setListener();
        initService();
    }
    private void initService() {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private void getDataTypeFromIntent() {
        fromWhere = getIntent().getIntExtra(Constant.FROME_WHERE, -1);
        //isSuccess = getIntent().getIntExtra("isSuccess",1);
        if(getIntent().getStringExtra(Constant.ORDER_ID)!=null){
            orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        }

        if(getIntent().getSerializableExtra("sender")!=null) {
            sender = (Sender) getIntent().getSerializableExtra("sender");
        }

        if(getIntent().getStringExtra(Constant.GOOD_ID)!=null){
            goodId = getIntent().getStringExtra(Constant.GOOD_ID);
        }
        if(getIntent().getStringExtra("goodstype")!=null){
            goodstype = getIntent().getStringExtra("goodstype");
        }
        if(getIntent().getStringExtra("sizeid")!=null){
            sizeId = getIntent().getStringExtra("sizeid");
        }
        if(getIntent().getStringExtra("logid")!=null) {
            logid=getIntent().getStringExtra("logid");
        }
        if(getIntent().getSerializableExtra("sender")!=null) {
            sender = (Sender) getIntent().getSerializableExtra("sender");
        }

        if(getIntent().getStringExtra("isRechargeLog")!=null){
            isRechargeLog = getIntent().getStringExtra("isRechargeLog");
            switch (isRechargeLog){
                case "0":
                    type = "0";
                    break;
                case "1":
                    type = "1";
                    break;
                case "2":
                    type = "2";
                    break;
                case "3":
                    type = "3";
                    break;
            }
        }
    }

    /**
     * 联网获取数据
     */
    private void initData() {
        reLoad();
        boolean available = NetworkUtils.isAvailable(this);
        if(!available) {
            netError();
        }else {
            if(!everCoon) {
                everCoon=true;
                initParams();
            }
        }
    }
    private void initParams() {
        String url;
        switch (fromWhere) {
            case Constant.SERVICE_DATA:
                url = UrlConstance.SERVER_DETAIL;
                getDataFromNet(url);
                break;
            case Constant.ENTITY_DATA:
                url = UrlConstance.ENTITY_DETAIL;
                getDataFromNet(url);
                break;
            case Constant.APPLET_INFO_DATA:
            case Constant.POS_DATA:
            case Constant.COMMUNITY_INFO_DATA:
                url = UrlConstance.POS_DETAIL;
                getDataFromNetPos(url);
                break;
        }
    }

    /**
     * pos显示数据,两个参数
     *
     * @param url
     */
    private void getDataFromNetPos(String url) {

        String currentTime = Utils.getCurrentTime();
        LogUtils.e(TAG, "currentTime=" + currentTime);
        String token = Utils.getToken(currentTime, this);
        LogUtils.e(TAG, "orderId==" + orderId);
        LogUtils.e(TAG, "goodsid==" + goodId);
        LogUtils.e(TAG, "goodstype==" + goodstype);
        LogUtils.e(TAG, "isRechargeLog==" + type);
        LogUtils.e(TAG, "version==new");
        LogUtils.e(TAG, "versionCode=="+Constant.VERSION_CODE);
        LogUtils.e(TAG, "logid==" + logid);
        OkHttpUtils.post()
                .url(url)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams(Constant.PARAMS_NEME_ACCOUNT_ID, Constant.ACCOUNT_ID)
                .addParams("id", orderId)
                .addParams("goodsid", goodId)
                .addParams("sizeid",sizeId)
                .addParams("goodstype", goodstype)
                .addParams("isRechargeLog", type)
                .addParams("version", "new")
                .addParams(Constant.PARAMS_NAME_VERSIONCODE,Constant.VERSION_CODE+"")
                .addParams("logid",logid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        everCoon=false;
                        loadFailure(R.string.try_later);
                    }
                    @Override
                    public void onResponse(String response) {
                        everCoon=false;
                        LogUtils.Log("订单详情response=="+response);
                        boolean toNextStep = Utils.checkSaveToken(BillDetailActivity.this, response);
                        if(toNextStep){
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if(code==200) {
                                    praseJson(response);
                                }else {
                                    String msg = jsonObject.getString("msg");
                                    Utils.showToast(BillDetailActivity.this,msg);
                                    loadFailure(R.string.order_unnormal);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void getDataFromNet(String url) {
        //获取时间戳 1473410972494并截取前10位
        String currentTime = Utils.getCurrentTime();
        //获取token
        String token = Utils.getToken(currentTime, this);
        //获取成功后
        LogUtils.Log("currentTime==" + currentTime);
        LogUtils.Log("USER_ACCOUNT==" + Constant.USER_ACCOUNT);
        LogUtils.Log("DEVICE_NAME==" + Constant.DEVICE_NAME);
        LogUtils.Log("DEVICE_MODEL==" + Constant.DEVICE_MODEL);
        LogUtils.Log("orderId==" + orderId);
        LogUtils.Log("account_id==" + Constant.ACCOUNT_ID);
        OkHttpUtils.post()
                .url(url)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT,Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams("id", orderId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        everCoon = false;
                        loadFailure(R.string.try_later);
                    }

                    @Override
                    public void onResponse(String response) {
                        everCoon = false;
                        boolean toNextStep = Utils.checkSaveToken(BillDetailActivity.this, response);
                        LogUtils.Log("response==" + response);
                        if (toNextStep) {
                            praseJson(response);
                        }
                    }
                });
    }

    /**
     * 解析json数据
     * @param json
     */
    private void praseJson(String json) {
        Gson gson=new Gson();
        switch (fromWhere) {
            case Constant.SERVICE_DATA:
                VoucherInfo voucherInfo = gson.fromJson(json, VoucherInfo.class);
                showData(voucherInfo);
                break;
            case Constant.ENTITY_DATA:
                EntityPrintBean entityPrintBean = gson.fromJson(json, EntityPrintBean.class);
                showData(entityPrintBean);
                break;
            case Constant.APPLET_INFO_DATA:
            case Constant.COMMUNITY_INFO_DATA:
            case Constant.POS_DATA:
                LogUtils.e(TAG,"Constant.POS_DATA:"+json);
                PosPrintBean posPrintBean = gson.fromJson(json, PosPrintBean.class);
                isSetPwd=posPrintBean.getMsg().getIsSetPwd();
                if(posPrintBean.getMsg().getOrderType()==3&&sender==null) {
                    getSender();
                }
                try {
                    JSONObject detailResponse=new JSONObject(json);
                    String ticketInfo = detailResponse.getJSONObject("msg").getString("ticketInfo");
                    if (!TextUtils.isEmpty(ticketInfo)) {
                        discountInfo = gson.fromJson(ticketInfo, DiscountInfo.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showData(posPrintBean);
                break;
        }
    }

    /**
     * 获取配送员列表
     */
    private void getSender() {
        Map<String, String> params = netWorks.getPublicParams();
        params.put("storeid",Constant.STORE_ID);
        netWorks.Request(UrlConstance.GET_STORE_SENDER, params, 5000, 0, new NetWorks.OnNetCallBack() {

            @Override
            public void onError(Exception e, int flag) {
                Utils.showToast(BillDetailActivity.this,"网络异常，获取配送员信息失败",1000);

            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.e(TAG,"获取配送员："+response.toString());
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200) {
                        Gson gson=new Gson();
                        sender = gson.fromJson(response, Sender.class);
                    }else {
                        String msg = jsonObject.getString("msg");
                        Utils.showToast(BillDetailActivity.this,msg,1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showData(Object object) {
        showdata = new ArrayList<>();
        switch (fromWhere) {
            case Constant.SERVICE_DATA:
                VoucherInfo vi = (VoucherInfo) object;
                List<VoucherInfo.MsgBean.DetailBean> detailBeanList = vi.getMsg().getDetail();
                voucher_name.setText(vi.getMsg().getName());
                List<VoucherInfo.MsgBean.DetailBean> detailBeans = detailBeanList;
                for (int i = 0; i < detailBeans.size(); i++) {
                    if (i == detailBeans.size() - 2) {
                        Spanned detail = Html.fromHtml(Html.fromHtml(detailBeans.get(i).getVal()).toString());
                        showdata.add(detail.toString());
                    } else {
                        showdata.add(detailBeans.get(i).getVal());
                    }
                }
                break;
            case Constant.ENTITY_DATA:
                EntityPrintBean epb = (EntityPrintBean) object;
                List<EntityPrintBean.MsgBean.DetailBean> detailBeanListEc = epb.getMsg().getDetail();
                voucher_name.setText(epb.getMsg().getGoodsName());
                List<EntityPrintBean.MsgBean.DetailBean> epbDetails = detailBeanListEc;
                for (int i = 0; i < epbDetails.size(); i++) {
                    if (i == epbDetails.size() - 2) {
                        Spanned detail = Html.fromHtml(Html.fromHtml(epbDetails.get(i).getVal()).toString());
                        showdata.add(detail.toString());
                    } else {
                        showdata.add(epbDetails.get(i).getVal());
                    }
                }
                break;
            case Constant.APPLET_INFO_DATA://自组点单d
                posBean = (PosPrintBean) object;
                String dayOrderNum = posBean.getMsg().getDayOrderNum();
                String orderNo="订单：" + dayOrderNum;
                if(posData==null){
                    posData = posBean.getMsg().getGoodsInfo();
                }else{
                    posData.clear();
                    posData.addAll(posBean.getMsg().getGoodsInfo());
                }
                tv_add_time.setText(posBean.getMsg().getAddTime());//下单时间
                tv_out_no.setText(posBean.getMsg().getOutOrderNo());
                String notice=posBean.getMsg().getOrderNote();
                if(notice!=null&&!TextUtils.isEmpty(notice)&&!"无".equals(notice)){
                    ll_remark.setVisibility(View.VISIBLE);
                    tv_mark_msg.setText("订单备注："+posBean.getMsg().getOrderNote());
                }
                voucher_name.setText(orderNo);
                initPrintData();
                if(TextUtils.equals("1",posBean.getMsg().getIsRefund())&&posBean.getMsg().getFactPayMoney()<=posBean.getMsg().getRefundMoney()) {
                    ll_callback.setVisibility(View.GONE);
                }else {
                    ll_callback.setVisibility(View.VISIBLE);
                }
//                reBackInfo(posBean.getMsg().getOrderNo());
                break;
            case Constant.COMMUNITY_INFO_DATA://社区小程序订单c
                showCommunityData((PosPrintBean) object);
                break;
            case Constant.POS_DATA://pos机订单
                posBean = (PosPrintBean) object;
                if(posBean.getMsg().getOrderType()==3) {
                    showCommunityData(posBean);
                }else {
                    rl_deliver_fee.setVisibility(View.GONE);
                    if(posBean.getMsg().getPayType().contains("记账")||posBean.getMsg().getPayType().contains("标记")){
                        btn_callback.setText("标记退款");
                    }
                    if(posData==null){
                        posData = posBean.getMsg().getGoodsInfo();
                    }else{
                        posData.clear();
                        posData.addAll(posBean.getMsg().getGoodsInfo());
                    }
                    dayOrderNum = posBean.getMsg().getDayOrderNum();
                    orderNo="订单：" + dayOrderNum;
                    //1.折扣，会员减免信息
                    discountInfo();
                    //2.退款信息
                    reBackInfo(orderNo);
                    //3.支付信息
                    payInfo();
                    //4.备注信息的显示
                    orderMarkInfo();
                    //5.初始化打印数据
                    initPrintData();
                }
                break;
        }
        ll_details.setVisibility(View.VISIBLE);
        ll_loading.setVisibility(View.GONE);

        if(fromWhere==Constant.POS_DATA||fromWhere==Constant.APPLET_INFO_DATA||fromWhere==Constant.COMMUNITY_INFO_DATA) {//pos的数据填充
            if(posAdapter==null) {
                posAdapter=new PosDetailAdapter(BillDetailActivity.this);
                boolean cotainsVipGoods=contaiVip(posData);
                posAdapter.setData(posData);
                posAdapter.setContainsVipGoods(cotainsVipGoods);
                lv_detail.setAdapter(posAdapter);
                LogUtils.e(TAG,"lv_detail设置adapter");
            }else {
                LogUtils.e(TAG,"posAdapter.notifyDataSetChanged()");
                posAdapter.notifyDataSetChanged();
            }
        }else {//实物和服务的数据填充
            if(adapter==null) {
                adapter = new PrintInfoAdapter(BillDetailActivity.this);
                adapter.setData(showdata);
                lv_detail.setAdapter(adapter);
            }else {
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void showCommunityData(PosPrintBean object) {
        ll_deliver_msg.setVisibility(View.VISIBLE);
        tv_peisong_state.setVisibility(View.VISIBLE);
        btn_start_send.setVisibility(View.VISIBLE);

        String dayOrderNum;
        String orderNo;
        String notice;
        posBean = object;
        dayOrderNum = posBean.getMsg().getDayOrderNum();
        orderNo="订单：" + dayOrderNum;
        if(posData==null){
            posData = posBean.getMsg().getGoodsInfo();
        }else{
            posData.clear();
            posData.addAll(posBean.getMsg().getGoodsInfo());
        }
        tv_add_time.setText(posBean.getMsg().getAddTime());//下单时间
        notice=posBean.getMsg().getOrderNote();
        if(notice!=null&&!TextUtils.isEmpty(notice)&&!"无".equals(notice)){
            ll_remark.setVisibility(View.VISIBLE);
            tv_mark_msg.setText("订单备注："+posBean.getMsg().getOrderNote());
        }
        voucher_name.setText(orderNo);
//                deliveryStatus = "0";
        deliveryStatus = posBean.getMsg().getDeliveryStatus();
        if(3==posBean.getMsg().getOrderType()) {
            ll_deliver_msg.setVisibility(View.VISIBLE);
            tv_peisong_state.setVisibility(View.VISIBLE);
        }else {
            ll_deliver_msg.setVisibility(View.GONE);
            tv_peisong_state.setVisibility(View.GONE);
        }

        if(deliveryStatus==0) {
            tv_deliver.setVisibility(View.GONE);
            tv_peisong_state.setText("待配送");
            tv_peisong_state.setTextColor(Color.parseColor("#2A9804"));
        }else if(deliveryStatus==1) {
            tv_deliver.setVisibility(View.VISIBLE);
            tv_peisong_state.setText("已配送");
            tv_peisong_state.setTextColor(Color.parseColor("#333333"));
            tv_deliver.setText("配送员："+posBean.getMsg().getDeliveryName()+" "+posBean.getMsg().getDeliveryPhone());
        }else if(deliveryStatus==3){//已自提
            tv_deliver.setVisibility(View.GONE);
            tv_peisong_state.setText("已自提");
            tv_peisong_state.setTextColor(Color.parseColor("#333333"));
        }else if(deliveryStatus==2) {//待自提
            tv_deliver.setVisibility(View.GONE);
            tv_peisong_state.setText("待自提");
            tv_peisong_state.setTextColor(Color.parseColor("#2A9804"));
        }

        if(deliveryStatus==0||deliveryStatus==1) {
            tv_arrive_time.setText("送达时间："+posBean.getMsg().getDeliveryTime());
            tv_customer_name_tel.setText("收货人："+posBean.getMsg().getCneeName()+" "+posBean.getMsg().getCneePhone());
            tv_customer_add.setText(posBean.getMsg().getCneeAddress());
        }else {
            tv_arrive_time.setVisibility(View.GONE);
            tv_customer_name_tel.setText("联系电话："+posBean.getMsg().getCneePhone());
            tv_customer_add.setText("自提地址："+posBean.getMsg().getZt_add());
        }

        if(deliveryStatus==0) {//未配送
            btn_print.setBackgroundResource(R.drawable.call_back_shape);
            btn_print.setTextColor(Color.parseColor("#333333"));
            btn_start_send.setVisibility(View.VISIBLE);
        }else {//已配送
            btn_print.setBackgroundResource(R.drawable.print_button_selector);
            btn_print.setTextColor(Color.parseColor("#ffffff"));
            btn_start_send.setVisibility(View.GONE);
        }
        if(deliveryStatus==0||deliveryStatus==1) {
            rl_deliver_fee.setVisibility(View.VISIBLE);
            tv_peisong_fee.setText(posBean.getMsg().getDeliveryCost());
        }else {
            rl_deliver_fee.setVisibility(View.GONE);
        }
        //1.折扣，会员减免信息
        discountInfo();
        //2.退款信息
        reBackInfo(orderNo);
        //3.支付信息
        payInfo();
        //4.备注信息的显示
        orderMarkInfo();
        initPrintData();
    }

    private void discountInfo() {
        if(posBean.getMsg().getVoucherInfo()!=null){
            if(posBean.getMsg().getVoucherInfo().getType()!=null&&!"".equals(posBean.getMsg().getVoucherInfo().getType())){
                rl_discount_coupons_info.setVisibility(View.VISIBLE);
                tv_discount_dec.setText(posBean.getMsg().getVoucherInfo().getTitle()+": ");
                tv_discount_reduce_money.setText(posBean.getMsg().getVoucherInfo().getMoney());
                //会员价tv_discount_dec显示原价，tv_discount_reduce_money显示优惠金额

                switch (posBean.getMsg().getVoucherInfo().getType()){
                    case "CASH":
                        iv_voucher_card.setImageResource(R.drawable.coupons_pic);
                        break;
                    case "DISCOUNT":
                        iv_voucher_card.setImageResource(R.drawable.discount_pic);
                        break;
                    case "MEMBER_CARD":
                        iv_voucher_card.setImageResource(R.drawable.member_pay_pic);
                        break;
                    case "MEMBER_CASH":
                        iv_voucher_card.setVisibility(View.GONE);
                        break;
                }
            }else{
                if(posBean.getMsg().getDiscount() > 0){
                    rl_discount_coupons_info.setVisibility(View.VISIBLE);
                    double discount = posBean.getMsg().getDiscount();
                    tv_discount_dec.setText("会员折扣" + discount*10 + "折");
                    double shouldPayMoney = posBean.getMsg().getShouldPayMoney();
                    tv_discount_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(shouldPayMoney*discount)));
                    iv_voucher_card.setImageResource(R.drawable.member_pay_pic);
                }else{
                    rl_discount_coupons_info.setVisibility(View.GONE);
                }
            }
        }else{
            if(posBean.getMsg().getDiscount() > 0){
                rl_discount_coupons_info.setVisibility(View.VISIBLE);
                double discount = posBean.getMsg().getDiscount();
                tv_discount_dec.setText("会员折扣" + discount*10 + "折");
                double shouldPayMoney = posBean.getMsg().getShouldPayMoney();
                tv_discount_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(shouldPayMoney*discount)));
                iv_voucher_card.setImageResource(R.drawable.member_pay_pic);
            }else{
                rl_discount_coupons_info.setVisibility(View.GONE);
            }
        }
    }

    private void reBackInfo(String orderNo) {
        //退款信息
        if("1".equals(posBean.getMsg().getIsRefund())) {
            second_depart.setVisibility(View.VISIBLE);
            if(posBean.getMsg().getFactPayMoney()<=posBean.getMsg().getRefundMoney()) {
                ll_callback.setVisibility(View.GONE);
            }else {
                ll_callback.setVisibility(View.VISIBLE);
            }
            ll_refund_msg.setVisibility(View.VISIBLE);
            if(posBean.getMsg().getRefundData()!=null&&posBean.getMsg().getRefundData().size()>0) {
                ll_refund_msg.removeAllViews();
                if(posBean.getMsg().getResouceType()==1||posBean.getMsg().getResouceType()==2) {
                    LinearLayout refundItem= (LinearLayout) View.inflate(this, R.layout.billdetail_refund_layout,null);
                    TextView refund_time= (TextView) refundItem.findViewById(R.id.refund_time);
                    TextView refundNo= (TextView) refundItem.findViewById(R.id.refund_no);
                    TextView refundContent= (TextView) refundItem.findViewById(R.id.refund_content);
                    TextView handlePerson= (TextView) refundItem.findViewById(R.id.handler_person);
                    refundNo.setVisibility(View.GONE);
                    handlePerson.setText("操作账号："+posBean.getMsg().getRefundOperator());
                    refund_time.setText("退款时间："+ posBean.getMsg().getRefundTime());
                    refundContent.setText("未知商品 x 1: ￥"+Utils.numf.format(posBean.getMsg().getRefundMoney()));
                    ll_refund_msg.addView(refundItem);
                    return;
                }

                for (int i = 0; i < posBean.getMsg().getRefundData().size(); i++) {
                    LinearLayout refundItem= (LinearLayout) View.inflate(this,R.layout.billdetail_refund_layout,null);
                    TextView refund_time= (TextView) refundItem.findViewById(R.id.refund_time);
                    TextView refundNo= (TextView) refundItem.findViewById(R.id.refund_no);
                    TextView refundContent= (TextView) refundItem.findViewById(R.id.refund_content);
                    TextView handlePerson= (TextView) refundItem.findViewById(R.id.handler_person);

                    ArrayList<RefundInfo> refundGoods = posBean.getMsg().getRefundData().get(i).getRefundGoods();
                    String content="";
                    Long addTime=0L;
                    String acc="";
                    if(refundGoods!=null&&refundGoods.size()>0) {
                        for (int j = 0; j <refundGoods.size(); j++) {
                            if(j==refundGoods.size()-1) {
                                content+= refundGoods.get(j).getGoods_name()+" x "+refundGoods.get(j).getRefund_num()+": ￥ "+refundGoods.get(j).getRefund_money();
                            }else {
                                content+= refundGoods.get(j).getGoods_name()+" x "+refundGoods.get(j).getRefund_num()+": ￥ "+refundGoods.get(j).getRefund_money()+"\n";
                            }
                           if(addTime<=0L) {
                               addTime=refundGoods.get(j).getAddtime();
                           }
                           if(TextUtils.isEmpty(acc)) {
                               acc=refundGoods.get(j).getHandle_person();
                           }
                        }
                    }
                    refundNo.setText("退货单号："+posBean.getMsg().getRefundData().get(i).getRefund_no());
                    handlePerson.setText("操作账号："+acc);
                    LogUtils.e(TAG,"addTime=="+addTime);
                    refund_time.setText("退款时间："+Utils.df3.format(new Date(addTime*1000)));
                    refundContent.setText(content);
                    ll_refund_msg.addView(refundItem);

                }
            }

            SpannableStringBuilder builder=new SpannableStringBuilder(orderNo);
            ForegroundColorSpan span = new ForegroundColorSpan(Color.rgb(233,160,60));
            if(orderNo.contains("已标记退款")){
                LogUtils.e(TAG,"orderNo.contains(\"(\") && orderNo.contains(\")\")");
                int i1 = orderNo.indexOf("已");
                builder.setSpan(span,i1-1,orderNo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                LogUtils.e(TAG,"orderNo.length()-5");
                builder.setSpan(span,orderNo.length()-5,orderNo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //builder.setSpan(span,orderNo.length()-5,orderNo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            voucher_name.setText(builder);
        }else {
            second_depart.setVisibility(View.GONE);
            ll_refund_msg.setVisibility(View.GONE);
            if("会员充值".equals(posBean.getMsg().getDayOrderNum())) {
                ll_callback.setVisibility(View.GONE);
            }else {
                ll_callback.setVisibility(View.VISIBLE);
            }
            voucher_name.setText(orderNo);
        }
    }

    private void payInfo() {
        if(fromWhere==Constant.COMMUNITY_INFO_DATA||posBean.getMsg().getOrderType()==3) {
            if(tv_total_fee_text!=null) {
                tv_total_fee_text.setText("商品金额:");
            }
            tv_should_pay.setText(" ￥" + Constant.numDf.format(posBean.getMsg().getShouldPayMoney()-Double.valueOf(posBean.getMsg().getDeliveryCost())));
            tv_actual_pay.setText(Constant.numDf.format(posBean.getMsg().getFactPayMoney()));
            tv_notices.setText(posBean.getMsg().getRemark());
            tv_add_time.setText(posBean.getMsg().getAddTime());//下单时间
            tv_deal_time.setText(posBean.getMsg().getPayTime());//结账时间
            tv_order_id.setText(posBean.getMsg().getOrderNo());
            tv_out_no.setText(posBean.getMsg().getOutOrderNo());
            if(posBean.getMsg().getTransaction_id()!=null&&!"".equals(posBean.getMsg().getTransaction_id())){
                all_trade_num.setVisibility(View.VISIBLE);
                tv_trade_num.setText(posBean.getMsg().getTransaction_id());
            }
            tv_pay_way.setText(posBean.getMsg().getPayType());
            tv_room_consume_des.setVisibility(View.GONE);
        }else {
            if(tv_total_fee_text!=null) {
                tv_total_fee_text.setText("总金额:");
            }
            tv_should_pay.setText(" ￥" + Constant.numDf.format(posBean.getMsg().getShouldPayMoney()));
            tv_actual_pay.setText(Constant.numDf.format(posBean.getMsg().getFactPayMoney()));
            tv_notices.setText(posBean.getMsg().getRemark());
            tv_add_time.setText(posBean.getMsg().getAddTime());//下单时间
            tv_out_no.setText(posBean.getMsg().getOutOrderNo());

            tv_deal_time.setText(posBean.getMsg().getPayTime());//结账时间
            tv_order_id.setText(posBean.getMsg().getOrderNo());
            if(posBean.getMsg().getTransaction_id()!=null&&!"".equals(posBean.getMsg().getTransaction_id())){
                all_trade_num.setVisibility(View.VISIBLE);
                tv_trade_num.setText(posBean.getMsg().getTransaction_id());
            }
            tv_pay_way.setText(posBean.getMsg().getPayType());
            String consume_room = posBean.getMsg().getRoomName();

            if(consume_room!=null&&!"".equals(consume_room)){
                tv_room_consume_des.setVisibility(View.VISIBLE);
                tv_room_consume_des.setText(consume_room);
            }else{
                tv_room_consume_des.setVisibility(View.GONE);
            }


            //附加的信息显示 tv_member_balance tv_member_number iv_qr_code
            if("1".equals(isRechargeLog)){
                tv_member_balance.setText(String.valueOf(discountInfo.getRechargeMoney()));
                tv_member_number.setText(discountInfo.getMembershipNum());
                all_member_balance.setVisibility(View.VISIBLE);
                all_member_number.setVisibility(View.VISIBLE);
            }
        }

    }

    private void orderMarkInfo() {
        if("1".equals(posBean.getMsg().getIsWxapp())||fromWhere== Constant.APPLET_INFO_DATA||"会员充值".equals(posBean.getMsg().getDayOrderNum())){
            arl_order_mark_layout.setVisibility(View.GONE);
        }
        if(posBean.getMsg().getOrderNote()!=null&&!"".equals(posBean.getMsg().getOrderNote())&&!"无".equals(posBean.getMsg().getOrderNote())){
            tv_mark_state.setText("修改");
            ll_remark.setVisibility(View.VISIBLE);
            tv_mark_msg.setText("订单备注："+posBean.getMsg().getOrderNote());
        }
    }

    private boolean contaiVip(List<PosPrintBean.MsgBean.GoodsInfoBean> posData) {
        if(posData!=null&&posData.size()>0) {
            for (int i = 0; i < posData.size(); i++) {
                if(1==posData.get(i).getIs_vip()) {
                    return true;
                }
            }
        }
        return false;
    }


    private void setListener() {
        iv_title.setOnClickListener(this);
        ll_loading.setClickToReload(this);
        btn_print.setOnClickListener(this);
        if(ll_callback!=null) {
            ll_callback.setOnClickListener(this);
        }
        btn_start_send.setOnClickListener(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1) {
            initData();
        }
    }

    private void initView() {
        lv_detail = (ListView) findViewById(R.id.lv_detail);
        ll_details = (LinearLayout) findViewById(R.id.ll_details);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        ll_loading = (SelfLinearLayout) findViewById(R.id.ll_loading);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        iv_jiazai_filure = (ImageView)findViewById(R.id.iv_jiazai_filure);

        btn_start_send = (Button) findViewById(R.id.btn_start_send);
        //头部局
        header = View.inflate(this, R.layout.pos_detail_header, null);
        voucher_name = (TextView) header.findViewById(R.id.voucher_name);
        tv_room_consume_des = (TextView) header.findViewById(R.id.tv_room_consume_des);
        ll_deliver_msg = (LinearLayout)header.findViewById(R.id.ll_deliver_msg);
        tv_peisong_state = (TextView)header.findViewById(R.id.tv_peisong_state);
        tv_deliver = (TextView)header.findViewById(R.id.tv_deliver);
        tv_arrive_time = (TextView)header.findViewById(R.id.tv_arrive_time);
        tv_customer_name_tel = (TextView)header.findViewById(R.id.tv_customer_name_tel);
        tv_customer_add = (TextView)header.findViewById(R.id.tv_customer_add);


        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("详情");
        iv_title = (ImageView) findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        tv_mark_reback = (TextView) findViewById(R.id.tv_mark_reback);
        btn_print = (Button) findViewById(R.id.btn_print);
        //退款
        ll_callback = (LinearLayout)findViewById(R.id.ll_callback);
        btn_callback = (TextView) findViewById(R.id.btn_callback);
        iv_callbacking = (ImageView)findViewById(R.id.iv_callbacking);
        //基础收银版，服务和实物订单不可打印
        switch (fromWhere) {
            case Constant.SERVICE_DATA:
            case Constant.ENTITY_DATA:
                btn_print.setVisibility(View.GONE);
                ll_callback.setVisibility(View.GONE);
                break;
            case Constant.APPLET_INFO_DATA:
                //脚布局
                posFooter = View.inflate(this, R.layout.pos_detail_footer_for_coupons, null);
                //总金额，优惠信息
                LinearLayout rl_foot_part1= (LinearLayout) posFooter.findViewById(R.id.rl_foot_part1);
                rl_foot_part1.setVisibility(View.GONE);
                //结账时间
                LinearLayout all_checkout_time= (LinearLayout) posFooter.findViewById(R.id.all_checkout_time);
                all_checkout_time.setVisibility(View.GONE);
                //商户单号
                LinearLayout all_merchant_odd_numbers= (LinearLayout) posFooter.findViewById(R.id.all_merchant_odd_numbers);
                all_merchant_odd_numbers.setVisibility(View.GONE);
                //付款方式
                LinearLayout all_pay_way= (LinearLayout) posFooter.findViewById(R.id.all_pay_way);
                all_pay_way.setVisibility(View.GONE);
                //退款信息
                ll_refund_msg= (LinearLayout) posFooter.findViewById(R.id.ll_refund_msg);
                ll_refund_msg.setVisibility(View.GONE);
                //退款信息
                arl_order_mark_layout= (AutoRelativeLayout) posFooter.findViewById(R.id.arl_order_mark_layout);
                arl_order_mark_layout.setVisibility(View.GONE);
                posFooter.findViewById(R.id.line_mark_head).setVisibility(View.GONE);
                posFooter.findViewById(R.id.second_depart).setVisibility(View.GONE);
                tv_mark_msg = (TextView)posFooter.findViewById(R.id.tv_mark_msg);
                ll_remark = (LinearLayout)posFooter.findViewById(R.id.ll_remark);

                tv_add_time= (TextView) posFooter.findViewById(R.id.tv_add_time);
                tv_total_fee_text = (TextView) posFooter.findViewById(R.id.tv_total_fee_text);
                second_depart =posFooter. findViewById(R.id.second_depart);
                tv_out_no = (TextView)posFooter.findViewById(R.id.tv_out_no);

                lv_detail.addFooterView(posFooter);
                break;
            case Constant.COMMUNITY_INFO_DATA:
            case Constant.POS_DATA:
                //脚布局
                posFooter = View.inflate(this, R.layout.pos_detail_footer_for_coupons, null);
                rl_deliver_fee = (RelativeLayout) posFooter.findViewById(R.id.rl_deliver_fee);
                tv_peisong_fee = (TextView) posFooter.findViewById(R.id.tv_peisong_fee);

                tv_should_pay= (TextView) posFooter.findViewById(R.id.tv_should_pay);
                tv_actual_pay= (TextView) posFooter.findViewById(R.id.tv_actual_pay);
                tv_deal_time= (TextView) posFooter.findViewById(R.id.tv_deal_time);
                tv_add_time= (TextView) posFooter.findViewById(R.id.tv_add_time);
                tv_order_id= (TextView) posFooter.findViewById(R.id.tv_order_id);
                tv_trade_num= (TextView) posFooter.findViewById(R.id.tv_trade_num);
                tv_pay_way= (TextView) posFooter.findViewById(R.id.tv_pay_way);
                all_trade_num= (LinearLayout) posFooter.findViewById(R.id.all_trade_num);

                tv_notices= (TextView) posFooter.findViewById(R.id.tv_notices);
                tv_member_balance= (TextView) posFooter.findViewById(R.id.tv_member_balance);
                tv_member_number= (TextView) posFooter.findViewById(R.id.tv_member_number);
                all_member_balance= (LinearLayout) posFooter.findViewById(R.id.all_member_balance);
                all_member_number= (LinearLayout) posFooter.findViewById(R.id.all_member_number);

                //2.备注
                tv_mark_state = (TextView)posFooter.findViewById(R.id.tv_mark_state);
                tv_mark_msg = (TextView)posFooter.findViewById(R.id.tv_mark_msg);
                ll_remark = (LinearLayout)posFooter.findViewById(R.id.ll_remark);
                arl_order_mark_layout = (AutoRelativeLayout)posFooter.findViewById(R.id.arl_order_mark_layout);
                btn_print.setVisibility(View.VISIBLE);

                tv_mark_state.setOnClickListener(this);

                rl_discount_coupons_info = (RelativeLayout) posFooter.findViewById(R.id.rl_discount_coupons_info);
                iv_voucher_card = (ImageView) posFooter.findViewById(R.id.iv_voucher_card);
                tv_discount_dec = (TextView) posFooter.findViewById(R.id.tv_discount_dec);
                tv_discount_reduce_money = (TextView) posFooter.findViewById(R.id.tv_discount_reduce_money);
                tv_total_fee_text = (TextView) posFooter.findViewById(R.id.tv_total_fee_text);

                lv_detail.addFooterView(posFooter);

                ll_refund_msg = (LinearLayout)posFooter.findViewById(R.id.ll_refund_msg);
                tv_out_no = (TextView)posFooter.findViewById(R.id.tv_out_no);
                second_depart =posFooter. findViewById(R.id.second_depart);
                ll_callback.setVisibility(View.VISIBLE);
                break;
        }
        lv_detail.addHeaderView(header);
        if(fromWhere==Constant.POS_DATA||fromWhere==Constant.APPLET_INFO_DATA||fromWhere==Constant.COMMUNITY_INFO_DATA){
            btn_print.setVisibility(View.VISIBLE);
        }else{
            btn_print.setVisibility(View.GONE);
        }
    }
    private void initPrintData() {
        btdata.clear();
        data.clear();
        //0.pos机商品且已经支付；1代表会员充值订单；2代表分订单3.社区门店订单 4.自助点单已结账总订单
        switch (posBean.getMsg().getOrderType()) {
            case 0:
            case 1:
                String merchantPermission="4";
                String customerPermission="5";
                PrintUtils.getInstance().formatPosDetailData(this, btdata, posBean, discountInfo,true,customerPermission,merchantPermission);//普通订单的数据格式化（蓝牙打印机）
                PrintUtils.getInstance().formatPosDetailData(this, data, posBean, discountInfo,false,customerPermission,merchantPermission);//普通订单的数据格式化（本地打印机）
                yunPrintMap = YunPrintUtils.formatPosDetailData(posBean,discountInfo);
                break;
            case 2:
            case 3:
            case 4:
                PrintUtils.getInstance().formatAppletDetailData(this, btdata, posBean, discountInfo,true);//一菜一单，来自小程序推单的数据格式化(蓝牙)
                PrintUtils.getInstance().formatAppletDetailData(this, data, posBean, discountInfo,false);//一菜一单，来自小程序推单的数据格式化(本地)

                yunPrintGroupBeans = YunPrintUtils.formatFinishedAppletYunPrintData(posBean, discountInfo,this,false);
                yunNormalGroupBeans= YunPrintUtils.formatFinishedAppletYunPrintData(posBean, discountInfo,this,true);
                break;
        }
    }

    @Override
    public void
    onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title://回退
                finish();
                break;
            case R.id.btn_print://打印
                if(data.size()>0) {
                    startPrint();
                }
                if(btdata.size()>0) {
                    startBtPrint();
                }
                //0.pos机商品且已经支付；1代表会员充值订单；2代表分订单3.社区门店订单 4.自助点单已结账总订单
                switch (posBean.getMsg().getOrderType()) {
                    case 0 :
                    case 1:
                        YunPrintUtils.printBillDetailNormalOrder(BillDetailActivity.this,yunPrintMap);
                        break;
                    case 2:
                    case 3:
                    case 4:
                        yunPrint();
                        break;
                }
                Utils.showToast(BillDetailActivity.this,"打印指令以发出，请勿重复点击，如未打印请到“打印机设置”中设置打印内容");
                break;
            case R.id.ll_callback:
                LogUtils.e(TAG,new Gson().toJson(posBean));
                /*Intent intent=new Intent(this, RefundActivity.class);
                intent.putExtra("goods_data", posBean);
                startActivityForResult(intent,1);*/
                RefundTranstation refundTranstation=new RefundTranstation(this);
                refundTranstation.beginTranstration(isSetPwd, posBean, iv_callbacking, new RefundTranstation.OnRefundListener() {
                    @Override
                    public void onRefundSuccess() {
                        String url = UrlConstance.POS_DETAIL;
                        getDataFromNetPos(url);
                    }
                });
                break;
            case R.id.tv_mark_state:
                inputOrChangeRemak();
                break;
            case R.id.btn_start_send:
                startSend();
                break;
        }
    }

    /**
     * 开始云打印
     */
    private void yunPrint( ) {
        LogUtils.e(TAG,"yunPrint()");
        if(yunNormalGroupBeans.size()>0||yunPrintGroupBeans.size()>0) {
            savedPrinterses = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
            if(savedPrinterses!=null&&savedPrinterses.size()>0) {
                if(posBean.getMsg().getOrderType()==3) {
                    YunPrintUtils.yunPrintOneByOneNew(this,savedPrinterses,yunPrintGroupBeans,yunNormalGroupBeans,"a","b",false,null);
                }else {
                    YunPrintUtils.yunPrintOneByOneNew(this,savedPrinterses,yunPrintGroupBeans,yunNormalGroupBeans,"9","8",false,null);
                }
            }

        }
    }

    private void startSend() {
        if(sender==null||sender.getData()==null||sender.getData().size()<=0) {
            //直接提交订单配送状态
            Utils.showToast(BillDetailActivity.this,"配送员数据异常，请添加配送员！");
        }else {
            senderData = sender.getData();
            if(senderData.size()==1) {
                upDateSender(senderData.get(0).getId());
            }else {
                for (int i = 0; i < senderData.size(); i++) {
                    senderData.get(i).setSelected(false);
                }
                NoticePopuUtils.chooseSender(this,R.id.al_bill_detail, senderData, new NoticePopuUtils.OnFinishpswCallBack() {
                    @Override
                    public void onFinishInput(String sendId) {
                        upDateSender(sendId);
                    }
                });
            }

        }
    }
    private void upDateSender(String sendId) {
        Utils.showWaittingDialog(this,"正在提交配送员...");
        Map<String, String> params = netWorks.getPublicParams();
        params.put("storeid",Constant.STORE_ID);
        params.put("orderid",posBean.getMsg().getOrderid());
        params.put("manid",sendId);
        netWorks.Request(UrlConstance.SET_DEVLIVERMAN, params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.dismissWaittingDialog();
                Utils.showToast(BillDetailActivity.this,"网络异常，配送员分配失败，请检查网络！");
            }

            @Override
            public void onResonse(String response, int flag) {
                Utils.dismissWaittingDialog();
                LogUtils.e(TAG,"提交配送员："+response.toString());
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if(code==200) {
                        initData();
                    }else {
                        Utils.showToast(BillDetailActivity.this,msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 云打印
     */
    private void yunprintApplet() {
        if(oneByOneYunList!=null&&oneByOneYunList.size()>0) {
            savedPrinterses = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
            if(savedPrinterses!=null&&savedPrinterses.size()>0) {
                YunPrintUtils.yunPrintOneByOneNew(this,savedPrinterses, oneByOneYunList,null,"9","8",false,null);
            }
        }
    }

    /**
     * 填写备注或者更改备注
     */
    private void inputOrChangeRemak() {

        NoticePopuUtils.setRemakDia(this, null, R.id.arl_this_order_detail, new NoticePopuUtils.SetRemakCallBack() {
            @Override
            public void onClickYes(String msg) {
                LogUtils.e(TAG,"onClickYes()");
                if(msg!=null&&!TextUtils.isEmpty(msg)) {
                    LogUtils.e(TAG,"onClickYes() 返回信息为："+msg);
                    setRemark(posBean.getMsg().getOrderNoOri(),msg);
                }else {
                    LogUtils.e(TAG,"onClickYes() 返回信息为空");
                    ll_remark.setVisibility(View.GONE);
                }
            }
            @Override
            public void onClickNo() {
                LogUtils.e(TAG,"onClickNo()");
            }
        });
    }

    /**
     * 修改备注
     * @param msg
     */
    private void setRemark(String order_no, final String msg) {
        NetWorks netWorks=new NetWorks(this);
        netWorks.upDataRemak("1",order_no, msg, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("提交备注e=="+e.toString());
                Utils.dismissWaittingDialog();
                Utils.showToast(BillDetailActivity.this,"网络异常，提交备注失败！",1000);
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
                        ll_remark.setVisibility(View.VISIBLE);
                        tv_mark_msg.setText(msg);
                        posBean.getMsg().setOrderNote(msg);
                        initPrintData();
                    }else {
                        String notice = jsonObject.getString("err");
                        Utils.showToast(BillDetailActivity.this,notice,1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },12);
    }

    private void startBtPrint() {
        if(mService.getState()==BluetoothService.STATE_CONNECTED) {
            BTPrintUtils.getInstance().btFormatDataPrint(mService, btdata, this);
//            BTPrintUtils.getInstance().wmBtPrint(mService, btdata, this,null);
        }else {
            BTPrintUtils.getInstance().connectBtPrinterTest(mService,this,handler);
        }
    }

    /**
     * 调用打印机打印小票
     */
    private void startPrint() {
        if (iWoyouService != null) {
            if(data!=null&&data.size()>0) {
                PrintUtils.getInstance().newPosPrint(iWoyouService,data,null);
            }
        }
    }
    /**
     * handle printer message
     * @param event print msg event
     */
    @Subscribe
    public void onEventMainThread(PrintMsgEvent event) {
        if (event.type == PrinterMsgType.MESSAGE_TOAST) {
            LogUtils.Log(event.msg);
//           Toast.makeText(BillDetailActivity.this, event.msg, Toast.LENGTH_SHORT).show();
           Message msg=new Message();
            msg.obj=event.msg;
            msg.what=UPDATE_UI;
            handler.sendMessage(msg);
        }
    }
    @Override
    protected void onDestroy() {
        unbindService(connService);
        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        if (evetRegist) {
//            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
    /**
     * 联网获取数据失败
     */
    public void loadFailure(int stringID){//服务器异常
        iv_jiazai_filure.setVisibility(View.VISIBLE);
        iv_jiazai_filure.setImageResource(R.drawable.couldnot_get_data);
        tv_loading.setText(stringID);
        pb_loading.setVisibility(View.GONE);
    }
    public void reLoad(){//加载视图显示
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
    @Override
    public void ClickToReload() {
        initData();
    }

    NetWorks netWorks=new NetWorks(this);

    private void stopCirle() {
        iv_callbacking.clearAnimation();
        iv_callbacking.setVisibility(View.GONE);
    }
}
