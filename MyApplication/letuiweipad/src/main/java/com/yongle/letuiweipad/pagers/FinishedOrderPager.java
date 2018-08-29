package com.yongle.letuiweipad.pagers;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.yongle.letuiweipad.adapter.PosDetailAdapter;
import com.yongle.letuiweipad.adapter.RecyclerAdapter;
import com.yongle.letuiweipad.basepager.BasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.domain.PosPrintBean;
import com.yongle.letuiweipad.domain.PrintTotalBean;
import com.yongle.letuiweipad.domain.RefundResult;
import com.yongle.letuiweipad.domain.WmPintData;
import com.yongle.letuiweipad.domain.createorder.DiscountInfo;
import com.yongle.letuiweipad.domain.finishedorder.PosOrderBean;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.selfinterface.RecyclerItemClickListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.yongle.letuiweipad.utils.printmanager.BTPrintUtils;
import com.yongle.letuiweipad.utils.printmanager.BluetoothService;
import com.yongle.letuiweipad.utils.printmanager.PrintUtils;
import com.yongle.letuiweipad.utils.printmanager.YunPrintUtils;
import com.yongle.letuiweipad.utils.refund.RefundTranstation;
import com.younle.younle624.myapplication.domain.orderbean.RefundInfo;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * Created by 我是奋斗 on 2016/5/9.
 * 微信/e-mail:tt090423@126.com
 */

public class FinishedOrderPager extends BasePager {
    @BindView(R.id.rlistView)RecyclerView rlistView;
    @BindView(R.id.print_total)TextView printTotal;
    @BindView(R.id.order_list)RadioButton posOrders;
    @BindView(R.id.membercharge_list)RadioButton memberchargeList;
    @BindView(R.id.tv_time_name)TextView tvTimeName;
    @BindView(R.id.start_time)TextView tvStartTime;
    @BindView(R.id.end_time)TextView tvEndTime;
    @BindView(R.id.tv_all)RadioButton tvPayTypeAll;
    @BindView(R.id.tv_wx) RadioButton tvWx;
    @BindView(R.id.tv_zfb)RadioButton tvZfb;
    @BindView(R.id.tv_cash)RadioButton tvCash;
    @BindView(R.id.tv_card)RadioButton tvCard;
    @BindView(R.id.tv_member)RadioButton tvMember;
    @BindView(R.id.tv_all_tool)RadioButton tvToolsAll;
    @BindView(R.id.tv_pos)RadioButton tvPos;
    @BindView(R.id.tv_wxapp)RadioButton tvWxapp;
    @BindView(R.id.tv_qrcode)RadioButton tvQrcode;
    @BindView(R.id.ll_total_num)AutoLinearLayout llTotalNum;
    @BindView(R.id.ll_total_acc)AutoLinearLayout llTotalAcc;
    @BindView(R.id.tv_cancel_search)TextView tvCancelSearch;
    @BindView(R.id.et_search)EditText etSearch;
    @BindView(R.id.net_error)RelativeLayout net_error;
    @BindView(R.id.net_error_refresh)TextView net_error_refresh;
    @BindView(R.id.total_num) TextView totalNum;
    @BindView(R.id.total_fee) TextView totalFee;
    @BindView(R.id.tv_date_exception) TextView tv_date_exception;
    @BindView(R.id.ll_nodata) LinearLayout ll_nodata;

    @BindView(R.id.rg_type)RadioGroup rgType;
    @BindView(R.id.rg_payway)RadioGroup rgPayWay;
    @BindView(R.id.rg_paytools)RadioGroup rgPayTools;
    @BindView(R.id.pay_tool) RelativeLayout pay_tool;
    @BindView(R.id.refresh_layout)SwipeRefreshLayout refreshLayout;
    @BindView(R.id.orderno_title)TextView ordernoTitle;
    @BindView(R.id.goodnum_title)TextView goodnumTitle;
    @BindView(R.id.tools_title)TextView toolsTitle;
    @BindView(R.id.remark_title)TextView remarkTitle;
    private IWoyouService iWoyouService;
    private String TAG = "FinishedOrderPager";
    private View baseView;
    private RecyclerAdapter adapter;
    private String selectTime = "";
    private NetWorks netWorks;
    private String id = "0";
    private String sizeId = "0";
    private String payTool = "0";
    private String payType = "-1";
    private int ordertype = 0;//0:商品列表 1：充值列表
    private String remark = "";//搜索字段
    private String goodId = "";

    public String goodstype = "0";
    private int pageNum = 1;

//    public String startTime = Utils.getFirstdayofThisMonth();

    private PosOrderBean posOrderBean;
    private List<PosOrderBean.MsgBean.OrderListBean> orderList;
    private LinearLayoutManager llManager;
    private int lastVisibleItemPosition;
    private boolean isLoading;
    private String logid="";
    private int isSetPwd;
    private DiscountInfo discountInfo;
    private PosPrintBean posPrintBean;
    private View posFooter;
    private TextView tv_should_pay;
    private TextView tv_actual_pay;
    private TextView tv_notices;
    private TextView tv_deal_time;
    private TextView tv_add_time;
    private TextView tv_order_id;
    private TextView tv_trade_num;
    private LinearLayout all_trade_num;
    private TextView tv_pay_way;
    private TextView tv_member_balance;
    private TextView tv_member_number;
    private LinearLayout all_member_balance;
    private LinearLayout all_member_number;
    private TextView tv_mark_state;
    private TextView tv_mark_msg;
    private LinearLayout ll_remark;
    private AutoRelativeLayout arl_order_mark_layout;
    private Button btn_print;
    private LinearLayout ll_callback;
    private ImageView iv_callbacking;
    private TextView btn_callback;
    private ListView lv_detail;
    private LinearLayout ll_details;
    private RelativeLayout rl_discount_coupons_info;
    private ImageView iv_voucher_card;
    private TextView tv_discount_dec;
    private TextView tv_discount_reduce_money;
    private LinearLayout ll_refund_msg;
    private TextView tv_out_no;
    private View second_depart;
    private View header;
    private TextView voucher_name;
    private TextView tv_room_consume_des;
    private List<PosPrintBean.MsgBean.GoodsInfoBean> posData;
    private PosDetailAdapter posAdapter;
    private String detailOrderId;
    private PopupWindow detailPopuWindow;
    DecimalFormat df=new DecimalFormat("00");

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
    private BluetoothService mService;
    private List<WmPintData> localTotalPrintData;
    private List<WmPintData> btTotalPrintData;
    private Map<String, String> yunPrintTotalMap;
    private int startHour=0;
    private int startMinute=0;
    private int endHour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private int endMinute=Calendar.getInstance().get(Calendar.MINUTE);
    public String startTime = Utils.getToday()+" "+df.format(startHour)+":"+df.format(startMinute);
    public String endTime = Utils.getToday()+" "+df.format(endHour)+":"+df.format(endMinute);

    public FinishedOrderPager() {
        EventBus.getDefault().register(this);

    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case Constant.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(mActivity, "已连接", Toast.LENGTH_SHORT).show();
                            startBtPrint();
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
    @Override
    public View initView() {
        baseView = View.inflate(mActivity, R.layout.finished_order_layout, null);
        return baseView;
    }
    private boolean everUpdate=false;

    @Override
    public void initData(int index) {
        initTime();

        etSearch.setHint(R.string.finished_order_serch_hint);
        mService=BluetoothService.getInstance();
        mService.setHandler(handler);
        if(!Constant.OPENED_PERMISSIONS.contains("4")) {
            memberchargeList.setVisibility(View.GONE);
        }else {
            memberchargeList.setVisibility(View.VISIBLE);
        }
        netWorks = new NetWorks(mActivity);
        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);

        llManager = new LinearLayoutManager(mActivity);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlistView.setLayoutManager(llManager);
        rlistView.setHasFixedSize(true);
        rlistView.addItemDecoration(new DividerItemDecoration(mActivity,LinearLayoutManager.VERTICAL));
        adapter = new RecyclerAdapter(mActivity);
        rlistView.setAdapter(adapter);


        getDataFromNet(false);
        setListener();
        initService();
    }

    private void initService() {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        mActivity.startService(intent);
        mActivity.bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }
    @OnTextChanged(R.id.et_search)
    void searchGs(Editable editable){
        remark = editable.toString();
        if(remark !=null&&! TextUtils.isEmpty(remark)) {
            pageNum=1;
            getDataFromNet(false);
        }
    }
    @OnFocusChange(R.id.et_search)
    void focusChange(View view, boolean b){
        if(b) {
            tvCancelSearch.setVisibility(View.VISIBLE);
        }else {
            tvCancelSearch.setVisibility(View.GONE);
            etSearch.setText(null);
            remark="";
            pageNum=1;
            getDataFromNet(false);
            Utils.hideKeyboard(baseView);
        }
    }

    private void setListener() {
        adapter.setItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                detailOrderId = orderList.get(index).getOrderId();
                orderList.get(index).getIsRechargeLog();
                getDataFromNetPos();
            }
        });
        ConditionCheckedListener conditionCheckedListener=new ConditionCheckedListener();
        rgType.check(R.id.order_list);
        rgPayWay.check(R.id.tv_all);
        rgPayTools.check(R.id.tv_all_tool);
        rgType.setOnCheckedChangeListener(conditionCheckedListener);
        rgPayWay.setOnCheckedChangeListener(conditionCheckedListener);
        rgPayTools.setOnCheckedChangeListener(conditionCheckedListener);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(false);
            }
        });
        rlistView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if(!isLoading) {
                    if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItemPosition+1==adapter.getItemCount()) {
                        LogUtils.Log("是时候加载更多了");
                        isLoading=true;
                        getDataFromNet(true);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = llManager.findLastVisibleItemPosition();
                LogUtils.Log("lastposi=="+ lastVisibleItemPosition);
            }
        });
    }

    /**
     * 展示订单详情
     */
    private void showDetail() {
        View popView = View.inflate(mActivity, R.layout.finished_order_detail, null);
        TextView title = ButterKnife.findById(popView, R.id.tv_left);
        title.setText("详情");
        btn_print = ButterKnife.findById(popView, R.id.btn_print);
        ll_callback = ButterKnife.findById(popView, R.id.ll_callback);
        iv_callbacking = ButterKnife.findById(popView, R.id.iv_callbacking);
        btn_callback = ButterKnife.findById(popView, R.id.btn_callback);
        lv_detail = ButterKnife.findById(popView, R.id.lv_detail);
        ll_details = ButterKnife.findById(popView, R.id.ll_details);
        initDetailHeaderFooter();
        inflateData();
        detailPopuWindow = new PopupWindow(popView);
        BitmapDrawable bitmapDrawable = new BitmapDrawable();
        detailPopuWindow.setBackgroundDrawable(bitmapDrawable);
        int width = mActivity.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        detailPopuWindow.setWidth((int) (width*0.34));
        detailPopuWindow.setFocusable(true);
        detailPopuWindow.setOutsideTouchable(true);
        setAlpha(0.4f);
        detailPopuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        detailPopuWindow.setAnimationStyle(R.style.detail_popwindow);
        detailPopuWindow.showAtLocation(baseView, Gravity.RIGHT | Gravity.TOP, 0, 0);
        detailPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1f);
            }
        });
        setDetailListener();
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void refundSuccess(RefundResult result){
        if(result.getResult()) {
            if(detailPopuWindow!=null&&detailPopuWindow.isShowing()) {
                detailPopuWindow.dismiss();
            }

            pageNum=1;
            getDataFromNet(false);
            getDataFromNetPos();
        }
    }

    private void setDetailListener() {
        //退款
        ll_callback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefundTranstation refundTranstation=new RefundTranstation(mActivity);
                refundTranstation.beginTranstration(R.id.history_order_root,isSetPwd, posPrintBean, iv_callbacking, new RefundTranstation.OnRefundListener() {
                    @Override
                    public void onRefundSuccess() {
                        if(detailPopuWindow!=null&&detailPopuWindow.isShowing()) {
                            detailPopuWindow.dismiss();
                        }
                        pageNum=1;
                        getDataFromNet(false);
                        getDataFromNetPos();
                    }
                });
            }
        });
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(printData.size()>0) {
                    startPrint(false);
                }
            }
        });
        arl_order_mark_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoticePopuUtils.showRemarkInPutDia(mActivity, "修改备注",tv_mark_msg.getText().toString(), true, new NoticePopuUtils.OnFinishInput() {
                    @Override
                    public void onFinishInput(String msg) {
                        unDateRemark(msg);
                    }

                    @Override
                    public void onCancelInput() {

                    }
                });
            }
        });

    }
    /**
     * 提交订单备注
     * @param msg
     */
    private void unDateRemark(final String msg) {
        Map<String, String> publicParams = netWorks.getPublicParams();
        publicParams.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID);
        publicParams.put(Constant.PARAMS_ADV_ID,Constant.ADV_ID);
        publicParams.put("order_no",posPrintBean.getMsg().getOrderNoOri());
        publicParams.put("msginfo",msg);
        publicParams.put("from","0");
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
                        posPrintBean.getMsg().setOrderNote(msg);
                        ll_remark.setVisibility(View.VISIBLE);
                        tv_mark_msg.setText(msg);
                        //备注信息的打印数据
//                        getUnpayOrderDetails(false);
                        initPrintData();
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

    /**
     * 调用打印机打印小票
     */
    private void startPrint(boolean total) {
        List<SavedPrinter> savedPrinters = (List<SavedPrinter>) SaveUtils.getObject(mActivity, Constant.YUN_PRINTERS);
        boolean containYun;
        if(total) {
            containYun=yunPrintTotalMap!=null&&yunPrintTotalMap.size()>0;
        }else {
            containYun=(yunPrintMap!=null&&yunPrintMap.size()>0);
        }
        if(printData.size()<=0&&btPrintData.size()<=0&&!containYun) {//无打印机
//            PrintUtils.getInstance().showCanNotPrintDia(mActivity);
            NoticePopuUtils.showOfflinDia(mActivity,"无法打印\n请您至“管理-打印设置-小票设置”中设置打印内容。",null);
        }else {
            if (iWoyouService != null) {
                if(printData!=null&&printData.size()>0) {
                    Utils.showWaittingDialog(mActivity,"小票打印中...");
                    PrintUtils.getInstance().newPosPrint(iWoyouService, printData, true,new ICallback.Stub() {
                        @Override
                        public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {
                            if(isSuccess) {
                                Utils.dismissWaittingDialog();
                            }
                        }
                    });
                }
            }
            if(btPrintData.size()>0) {
                startBtPrint();
            }
            if(total) {//汇总

            }else {//订单
                if(savedPrinters !=null && savedPrinters.size()>0){
                    if(yunPrintMap!=null&&yunPrintMap.size()>0) {
                        YunPrintUtils.printBillDetailNormalOrder(mActivity,yunPrintMap);
                    }
                }
            }
        }
    }

    private void startBtPrint() {
        if(mService.getState()== BluetoothService.STATE_CONNECTED) {
            BTPrintUtils.getInstance().btFormatDataPrint(mService, btPrintData, mActivity);
//            BTPrintUtils.getInstance().wmBtPrint(mService, btdata, this,null);
        }else {
            BTPrintUtils.getInstance().connectBtPrinterTest(mService,mActivity,handler);
        }
    }

    private void inflateData() {
        if(posPrintBean.getMsg().getPayType().contains("记账")||posPrintBean.getMsg().getPayType().contains("标记")){
            btn_callback.setText("标记退款");
        }
        if(posData==null){
            posData = posPrintBean.getMsg().getGoodsInfo();
        }else{
            posData.clear();
            posData.addAll(posPrintBean.getMsg().getGoodsInfo());
        }
        String dayOrderNum = posPrintBean.getMsg().getDayOrderNum();
        String orderNo = "订单：" + dayOrderNum;
        //int orderType = posBean.getMsg().getOrderType();//0代表有商品且已经支付；1代表会员充值订单；2代表分订单
        detailMemberInfo();
        //退款信息
        detailRefundInfo(orderNo);
        //支付信息
        detailPayInfo();

        //附加的信息显示 tv_member_balance tv_member_number iv_qr_code
        if(ordertype==1){
            all_member_balance.setVisibility(View.VISIBLE);
            all_member_number.setVisibility(View.VISIBLE);
            tv_member_balance.setText(String.valueOf(discountInfo.getRechargeMoney()));
            tv_member_number.setText(discountInfo.getMembershipMobile());
        }else {
            all_member_balance.setVisibility(View.GONE);
            all_member_number.setVisibility(View.GONE);
        }

        //备注信息的显示
        if("1".equals(posPrintBean.getMsg().getIsWxapp())/*||fromWhere==Constant.APPLET_INFO_DATA*/||"会员充值".equals(posPrintBean.getMsg().getDayOrderNum())){
            arl_order_mark_layout.setVisibility(View.GONE);
        }
        if(posPrintBean.getMsg().getOrderNote()!=null&&!"".equals(posPrintBean.getMsg().getOrderNote())&&!"无".equals(posPrintBean.getMsg().getOrderNote())){
            tv_mark_state.setText("修改");
            ll_remark.setVisibility(View.VISIBLE);
            tv_mark_msg.setText(posPrintBean.getMsg().getOrderNote());
        }

        ll_details.setVisibility(View.VISIBLE);

        posAdapter=new PosDetailAdapter(mActivity);
        boolean cotainsVipGoods=contaiVip(posData);
        posAdapter.setData(posData);
        posAdapter.setContainsVipGoods(cotainsVipGoods);
        lv_detail.setAdapter(posAdapter);
        initPrintData();
    }
    private List<WmPintData> printData=new ArrayList<>();
    private List<WmPintData> btPrintData=new ArrayList<>();
    private Map<String, List<WmPintData>> yunPrintMap;

    /**
     * 1.提交完订单备注
     * 2.请求订单详情
     */
    private void initPrintData() {
        PrintUtils.getInstance().formatPosDetailData(mActivity, printData, posPrintBean, discountInfo,false);//普通订单的数据格式化（本地打印机）
        PrintUtils.getInstance().formatPosDetailData(mActivity, btPrintData, posPrintBean, discountInfo,true);//普通订单的数据格式化（蓝牙打印机）
        yunPrintMap = YunPrintUtils.formatPosDetailData(posPrintBean,discountInfo);
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

    private void detailPayInfo() {
        tv_should_pay.setText(Utils.dropZero(posPrintBean.getMsg().getShouldPayMoney()+""));
        tv_actual_pay.setText(Utils.dropZero(posPrintBean.getMsg().getFactPayMoney()+""));
        tv_notices.setText(posPrintBean.getMsg().getRemark());
        tv_add_time.setText(posPrintBean.getMsg().getAddTime());//下单时间
        tv_deal_time.setText(posPrintBean.getMsg().getPayTime());//结账时间
        tv_order_id.setText(posPrintBean.getMsg().getOrderNo());
        if(posPrintBean.getMsg().getTransaction_id()!=null&&!"".equals(posPrintBean.getMsg().getTransaction_id())){
            all_trade_num.setVisibility(View.VISIBLE);
            tv_trade_num.setText(posPrintBean.getMsg().getTransaction_id());
        }
        tv_pay_way.setText(posPrintBean.getMsg().getPayType());
        String consume_room = posPrintBean.getMsg().getRoomName();
        if(consume_room !=null&&!"".equals(consume_room)){
            tv_room_consume_des.setVisibility(View.VISIBLE);
            tv_room_consume_des.setText(consume_room);
        }else{
            tv_room_consume_des.setVisibility(View.GONE);
        }
    }

    private void detailRefundInfo(String orderNo) {
        if("1".equals(posPrintBean.getMsg().getIsRefund())) {
            second_depart.setVisibility(View.VISIBLE);

            if(posPrintBean.getMsg().getFactPayMoney()<=posPrintBean.getMsg().getRefundMoney()) {
                ll_callback.setVisibility(View.GONE);
            }else {
                ll_callback.setVisibility(View.VISIBLE);
            }
            ll_refund_msg.setVisibility(View.VISIBLE);
            if(posPrintBean.getMsg().getResouceType()==1||posPrintBean.getMsg().getResouceType()==2) {
                LinearLayout refundItem= (LinearLayout) View.inflate(mActivity, R.layout.billdetail_refund_layout,null);
                TextView refund_time= (TextView) refundItem.findViewById(R.id.refund_time);
                TextView refundNo= (TextView) refundItem.findViewById(R.id.refund_no);
                TextView refundContent= (TextView) refundItem.findViewById(R.id.refund_content);
                TextView handlePerson= (TextView) refundItem.findViewById(R.id.handler_person);
                refundNo.setVisibility(View.GONE);
                handlePerson.setText("操作账号："+posPrintBean.getMsg().getRefundOperator());
                refund_time.setText("退款时间："+ posPrintBean.getMsg().getRefundTime());
                refundContent.setText("未知商品 x 1: ￥"+Utils.numdf.format(posPrintBean.getMsg().getRefundMoney()));
                ll_refund_msg.addView(refundItem);
                return;
            }

            if(posPrintBean.getMsg().getRefundData()!=null&&posPrintBean.getMsg().getRefundData().size()>0) {
                ll_refund_msg.removeAllViews();
                for (int i = 0; i < posPrintBean.getMsg().getRefundData().size(); i++) {
                    LinearLayout refundItem= (LinearLayout) View.inflate(mActivity,R.layout.billdetail_refund_layout,null);
                    TextView refund_time= (TextView) refundItem.findViewById(R.id.refund_time);
                    TextView refundNo= (TextView) refundItem.findViewById(R.id.refund_no);
                    TextView refundContent= (TextView) refundItem.findViewById(R.id.refund_content);
                    TextView handlePerson= (TextView) refundItem.findViewById(R.id.handler_person);

                    ArrayList<RefundInfo> refundGoods = posPrintBean.getMsg().getRefundData().get(i).getRefundGoods();
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
                    refundNo.setText("退货单号："+posPrintBean.getMsg().getRefundData().get(i).getRefund_no());
                    handlePerson.setText("操作账号："+acc);
                    LogUtils.e(TAG,"addTime=="+addTime);
                    refund_time.setText("退款时间："+Utils.df3.format(new Date(addTime*1000)));
                    refundContent.setText(content);
                    ll_refund_msg.addView(refundItem);

                }
            }
            tv_out_no.setText(posPrintBean.getMsg().getOutOrderNo());
            SpannableStringBuilder builder=new SpannableStringBuilder(orderNo);
            ForegroundColorSpan span = new ForegroundColorSpan(Color.rgb(233,160,60));
            LogUtils.e(TAG,"orderNo="+orderNo);
            if(orderNo.contains("已标记退款")){
                LogUtils.e(TAG,"orderNo.contains(\"(\") && orderNo.contains(\")\")");
                int i1 = orderNo.indexOf("已");
                builder.setSpan(span,i1-1,orderNo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                LogUtils.e(TAG,"orderNo.length()-5");
                builder.setSpan(span,orderNo.length()-5,orderNo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            voucher_name.setText(builder);
        }else {
            second_depart.setVisibility(View.GONE);
            ll_refund_msg.setVisibility(View.GONE);
            if("会员充值".equals(posPrintBean.getMsg().getDayOrderNum())) {
                ll_callback.setVisibility(View.GONE);
            }else {
                ll_callback.setVisibility(View.VISIBLE);
            }
            voucher_name.setText(orderNo);
        }
    }

    private void detailMemberInfo() {
        if(posPrintBean.getMsg().getVoucherInfo()!=null){
            if(posPrintBean.getMsg().getVoucherInfo().getType()!=null&&!"".equals(posPrintBean.getMsg().getVoucherInfo().getType())){
                rl_discount_coupons_info.setVisibility(View.VISIBLE);
                tv_discount_dec.setText(posPrintBean.getMsg().getVoucherInfo().getTitle()+": ");
                tv_discount_reduce_money.setText(posPrintBean.getMsg().getVoucherInfo().getMoney());
                switch (posPrintBean.getMsg().getVoucherInfo().getType()){
                    case "CASH":
                        iv_voucher_card.setVisibility(View.VISIBLE);
                        iv_voucher_card.setImageResource(R.drawable.coupons_pic);
                        break;
                    case "DISCOUNT":
                        iv_voucher_card.setVisibility(View.VISIBLE);
                        iv_voucher_card.setImageResource(R.drawable.discount_pic);
                        break;
                    case "MEMBER_CARD":
                        iv_voucher_card.setVisibility(View.VISIBLE);
                        iv_voucher_card.setImageResource(R.drawable.member_pay_pic);
                        break;
                    case "MEMBER_CASH":
                        iv_voucher_card.setVisibility(View.GONE);
                        break;
                }
            }else{
                if(posPrintBean.getMsg().getDiscount() > 0){
                    rl_discount_coupons_info.setVisibility(View.VISIBLE);
                    double discount = posPrintBean.getMsg().getDiscount();
                    tv_discount_dec.setText("会员折扣" + discount*10 + "折");
                    double shouldPayMoney = posPrintBean.getMsg().getShouldPayMoney();
                    tv_discount_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(shouldPayMoney*discount)));
                    iv_voucher_card.setImageResource(R.drawable.member_pay_pic);
                }else{
                    rl_discount_coupons_info.setVisibility(View.GONE);
                }
            }
        }else{
            if(posPrintBean.getMsg().getDiscount() > 0){
                rl_discount_coupons_info.setVisibility(View.VISIBLE);
                double discount = posPrintBean.getMsg().getDiscount();
                tv_discount_dec.setText("会员折扣" + discount*10 + "折");
                double shouldPayMoney = posPrintBean.getMsg().getShouldPayMoney();
                tv_discount_reduce_money.setText(Utils.keepTwoDecimal(String.valueOf(shouldPayMoney*discount)));
                iv_voucher_card.setImageResource(R.drawable.member_pay_pic);
            }else{
                rl_discount_coupons_info.setVisibility(View.GONE);
            }
        }
    }

    private void initDetailHeaderFooter() {
        //头部局
        header = View.inflate(mActivity, R.layout.pos_detail_header, null);
        voucher_name = (TextView) header.findViewById(R.id.voucher_name);
        tv_room_consume_des = (TextView) header.findViewById(R.id.tv_room_consume_des);
        //脚布局
        posFooter = View.inflate(mActivity, R.layout.pos_detail_footer_for_coupons, null);
        tv_should_pay= (TextView) posFooter.findViewById(R.id.tv_should_pay);
        tv_actual_pay= (TextView) posFooter.findViewById(R.id.tv_actual_pay);
        tv_notices= (TextView) posFooter.findViewById(R.id.tv_notices);
        tv_deal_time= (TextView) posFooter.findViewById(R.id.tv_deal_time);
        tv_add_time= (TextView) posFooter.findViewById(R.id.tv_add_time);
        tv_order_id= (TextView) posFooter.findViewById(R.id.tv_order_id);
        tv_trade_num= (TextView) posFooter.findViewById(R.id.tv_trade_num);
        all_trade_num= (LinearLayout) posFooter.findViewById(R.id.all_trade_num);
        tv_pay_way= (TextView) posFooter.findViewById(R.id.tv_pay_way);
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

//        tv_mark_state.setOnClickListener(this);

        rl_discount_coupons_info = (RelativeLayout) posFooter.findViewById(R.id.rl_discount_coupons_info);
        iv_voucher_card = (ImageView) posFooter.findViewById(R.id.iv_voucher_card);
        tv_discount_dec = (TextView) posFooter.findViewById(R.id.tv_discount_dec);
        tv_discount_reduce_money = (TextView) posFooter.findViewById(R.id.tv_discount_reduce_money);

        ll_refund_msg = (LinearLayout)posFooter.findViewById(R.id.ll_refund_msg);
        tv_out_no = (TextView) posFooter.findViewById(R.id.tv_out_no);

        second_depart =posFooter. findViewById(R.id.second_depart);
        ll_callback.setVisibility(View.VISIBLE);
        lv_detail.addFooterView(posFooter);
        lv_detail.addHeaderView(header);
    }
    /**
     * 获取订单详情
     */
    private void getDataFromNetPos() {
        Map<String, String> params = netWorks.getPublicParams();
        params.put(Constant.PARAMS_NEME_ACCOUNT_ID, Constant.ACCOUNT_ID);
        params.put("id", detailOrderId);
        params.put("goodsid", goodId);
        params.put("sizeid",sizeId);
        params.put("goodstype", goodstype);
        params.put("isRechargeLog", ordertype+"");
        params.put("version", "new");
        params.put("versionCode",Constant.VERSION_CODE+"");
        params.put("logid",logid);
        netWorks.Request(UrlConstance.POS_DETAIL,true, "正在查询订单详情", params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                net_error.setVisibility(View.VISIBLE);
                LogUtils.Log("请求历史订单详情：error=="+e.toString());

            }
            @Override
            public void onResonse(String response, int flag) {
                net_error.setVisibility(View.GONE);
                LogUtils.Log("请求历史订单详情：response=="+response);
                parseOrdetailJson(response);
            }
        });
    }

    private void parseOrdetailJson(String json) {
        Gson gson=new Gson();
        posPrintBean = gson.fromJson(json, PosPrintBean.class);
        isSetPwd= posPrintBean.getMsg().getIsSetPwd();
        try {
            JSONObject detailResponse=new JSONObject(json);
            String ticketInfo = detailResponse.getJSONObject("msg").getString("ticketInfo");
            if (!TextUtils.isEmpty(ticketInfo)) {
                discountInfo = gson.fromJson(ticketInfo, DiscountInfo.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDetail();
    }

    class ConditionCheckedListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            LogUtils.Log("onCheckedChanged");
            switch (i) {
                case R.id.order_list :
//                    initTime();
                    ordertype=0;
                    pay_tool.setVisibility(View.VISIBLE);
                    break;
                case R.id.membercharge_list :
//                    initTime();
                    pay_tool.setVisibility(View.GONE);
                    ordertype=1;
                    break;
                case R.id.tv_all :
                    payType="-1";
                    break;
                case R.id.tv_wx :
                    payType="0";
                    break;
                case R.id.tv_zfb :
                    payType="1";
                    break;
                case R.id.tv_card :
                    payType="2";
                    break;
                case R.id.tv_cash:
                    payType="3";
                    break;
                case R.id.tv_member :
                    payType="4";
                    break;
                case R.id.tv_all_tool :
                    payTool="0";
                    break;
                case R.id.tv_pos :
                    payTool="1";
                    break;
                case R.id.tv_wxapp :
                    payTool="3";
                    break;
                case R.id.tv_qrcode :
                    payTool="2";
                    break;
            }
            getDataFromNet(false);
        }
    }

    /**
     * 会员充值和商品订单的顶部不同，更换
     */
    private void changeOrderDetailtitle() {
        if(ordertype==0) {//商品订单
            ordernoTitle.setVisibility(View.VISIBLE);
            goodnumTitle.setVisibility(View.VISIBLE);
            toolsTitle.setVisibility(View.VISIBLE);
            remarkTitle.setVisibility(View.VISIBLE);
        }else if(ordertype==1) {//会员充值
            ordernoTitle.setVisibility(View.GONE);
            goodnumTitle.setVisibility(View.GONE);
            toolsTitle.setVisibility(View.GONE);
            remarkTitle.setVisibility(View.GONE);
        }
    }

    private void initTime() {
        endHour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        endMinute=Calendar.getInstance().get(Calendar.MINUTE);

        startHour=0;
        startMinute=0;
        startTime = Utils.getToday()+" "+df.format(startHour)+":"+df.format(startMinute);
        endTime = Utils.getToday()+" "+df.format(endHour)+":"+df.format(endMinute);

        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);
    }

    private void setAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        mActivity.getWindow().setAttributes(params);
    }

    private void getDataFromNet(final boolean addPager) {
        if(!addPager) {
            pageNum=1;
        }

        getOrderList(addPager);
    }

    private void getOrderList(final boolean addPager) {
        selectTime = "";
        Map<String, String> params = netWorks.getPublicParams();
        params.put(Constant.PARAMS_NEME_STORE_ID, Constant.STORE_ID);
        params.put("goodsid", id);//商品id,按商品名称筛选
        params.put("sizeid", sizeId);
        params.put("goodstype", goodstype);
        params.put("start", startTime);//查询的开始时间（默认当月1号） startTime
        params.put("end", endTime);//查询的结束时间（默认当月当天） endTime
        params.put("selectTime", selectTime);//第一页的查询时间戳（默认为当前系统时间）
        params.put("page", String.valueOf(pageNum));//页码
        params.put("payType", payType);
        params.put("ordertype", String.valueOf(ordertype));
        params.put("payTool", payTool);
        params.put("version", "new");
        params.put("versionCode", Constant.VERSION_CODE + "");
        params.put("remark", remark);
        netWorks.Request(UrlConstance.POS_LIST,true, "正在请求数据...", params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                isLoading=false;
                stopRefresh();
                LogUtils.e(TAG, "返回数据失败：" + e.toString());
                loadFailure("请稍后再试！");
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log( "pos机收款列表 response=" + response);
                isLoading=false;
                stopRefresh();
                parseJson(response,addPager);
            }
        });
    }

    private void stopRefresh() {
        if(refreshLayout!=null&&refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    private void loadFailure(String way) {
        net_error.setVisibility(View.VISIBLE);
        rlistView.setVisibility(View.GONE);
    }

    /**
     * 解析json
     *
     * @param json
     */
    private void parseJson(String json,boolean isLoadMore) {
        LogUtils.e(TAG, "parseJson");
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                reInitView();
                pageNum++;
                Gson gson = new Gson();
                posOrderBean = gson.fromJson(json, PosOrderBean.class);
                if(isLoadMore) {
                    List<PosOrderBean.MsgBean.OrderListBean> moreList = posOrderBean.getMsg().getOrderList();
                    if(moreList!=null&&moreList.size()>0) {
                        this.orderList.addAll(posOrderBean.getMsg().getOrderList());
                    }else {
                        Utils.showToast(mActivity,"没有更多的数据了",1000);
                        pageNum--;
                        return;
                    }
                }else {
                    if (!Constant.IS_FIRST_SELECT_TIME) {//只取第一次的值
                        Constant.IS_FIRST_SELECT_TIME = true;
                        selectTime = posOrderBean.getMsg().getSelectTime() + "";
                    }
                    orderList = posOrderBean.getMsg().getOrderList();
                }
                showData();
            } else {
                noData();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void noData() {
        ll_nodata.setVisibility(View.VISIBLE);
        rlistView.setVisibility(View.GONE);
        tv_date_exception.setText(getString(R.string.nodata));
    }

    private void showData() {
        changeOrderDetailtitle();
        if(posOrderBean!=null) {
            totalNum.setText(posOrderBean.getMsg().getSaled()+"");
            double income = posOrderBean.getMsg().getIncome();
            totalFee.setText("￥"+Utils.dropZero(income +""));
        }
        if (orderList != null && orderList.size() > 0) {
            adapter.setData(orderList, RecyclerAdapter.FINISH_TYPE);
            adapter.setFinishListType(ordertype);
            adapter.notifyDataSetChanged();
        }else {
            noData();
        }
    }

    @OnClick({R.id.print_total, R.id.al_type, R.id.start_time, R.id.end_time,R.id.net_error_refresh,R.id.tv_cancel_search,R.id.ll_showwxapp_dia})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.print_total://打印汇总
                NoticePopuUtils.chooseDoubleTime(mActivity, Utils.getToday(), Utils.getToday(), new NoticePopuUtils.DoubleTimeChooseCallBack() {
                    @Override
                    public void onFinishInput(String start, String end) {
                        LogUtils.e(TAG,"stat="+start+" end="+end);
                        printTotal(start,end);
                    }
                });
                break;
            case R.id.al_type:
                break;
            case R.id.start_time:
                NoticePopuUtils.chooseTime(mActivity, startTime,"请选择开始时间",endTime,null,startHour,startMinute, new NoticePopuUtils.OnFinishTimeChoose() {
                    @Override
                    public void onFinishChoose(String msg,int hour,int minute) {
                        startHour=hour;
                        startMinute=minute;
                        tvStartTime.setText(msg+" "+df.format(hour)+":"+df.format(minute));
                        startTime=tvStartTime.getText().toString();
                        getDataFromNet(false);
                    }
                });
                break;
            case R.id.end_time:
                NoticePopuUtils.chooseTime(mActivity, endTime,"请选择结束时间",null,startTime, endHour,endMinute,new NoticePopuUtils.OnFinishTimeChoose() {
                    @Override
                    public void onFinishChoose(String msg,int hour,int minute) {
                        endHour=hour;
                        endMinute=minute;
                        tvEndTime.setText(msg+" "+df.format(hour)+":"+df.format(minute));
                        endTime=tvEndTime.getText().toString();
                        getDataFromNet(false);

                    }
                });
                break;
            case R.id.net_error_refresh:
                getDataFromNet(false);
                break;
            case R.id.tv_cancel_search:
                etSearch.clearFocus();
                break;
            case R.id.ll_showwxapp_dia:
                NoticePopuUtils.showWxAppPup(mActivity,etSearch);
                break;
        }
    }

    public void printTotal(final String startTime, final String endTime) {
        Map<String, String> params = netWorks.getPublicParams();
        params.put(Constant.PARAMS_ADV_ID,Constant.ADV_ID);
        params.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID);
        params.put("start",startTime);
        params.put("end",endTime);
        params.put("versionCode",Constant.VERSION_CODE+"");
        netWorks.Request(UrlConstance.REQUEST_TOTAL_PRINT,true, "正在请求打印数据...",params, 10000,0,new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("获取打印汇总error() "+e.toString());
                Utils.showToast(mActivity,"未能取得数据，请重试");
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.e(TAG,"获取打印汇总数据："+response.toString());
                parsePrintDataJson(response,startTime,endTime);
            }
        });
    }
    /**
     * 解析打印汇总数据json
     * @param json
     */
    private void parsePrintDataJson(String json,String start,String end) {
        try {
            JSONObject jo=new JSONObject(json);
            int code=jo.getInt("code");
            String err = jo.getString("err");
            if(code==200) {
                Gson gson=new Gson();
                PrintTotalBean printTotalBean = gson.fromJson(jo.getString("data"), PrintTotalBean.class);
                if(iWoyouService!=null) {
                    printData = PrintUtils.getInstance().initTotalPrintData(printTotalBean, start, end,false);
                }
                SavedPrinter device = (SavedPrinter) SaveUtils.getObject(mActivity, Constant.BT_PRINTER);
                if(device!=null) {
                    btPrintData = PrintUtils.getInstance().initTotalPrintData(printTotalBean, start, end,true);
                }
                yunPrintTotalMap = YunPrintUtils.yunPrintTotal(printTotalBean, start, end);
                startPrint(true);
            }else {
                Utils.showToast(mActivity,err,1000);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void reInitView(){
        rlistView.setVisibility(View.VISIBLE);
        net_error.setVisibility(View.GONE);
        ll_nodata.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}