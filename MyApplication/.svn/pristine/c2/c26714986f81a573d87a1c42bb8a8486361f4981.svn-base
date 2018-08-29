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
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.adapter.ResumeDetailAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.GoodBean;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.domain.orderbean.DetailMemberBean;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.domain.printsetting.YunPrintGroupBean;
import com.younle.younle624.myapplication.domain.waimai.WmPintData;
import com.younle.younle624.myapplication.myservice.BluetoothService;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.printmanager.BTPrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.PrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.YunPrintUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class AddOrderDetailActivity extends Activity implements View.OnClickListener {
    private static final int UPDATE_PRINT_TIME = 1;
    private static final int WAIT_BLUETOOTH_OPEN = 2;
    private ListView lv_order_details;
    private TextView tv_total_money;
    private View view_line_fen_order;
    private TextView tv_total_money_this_order;
    private TextView tv_title;
    private ImageView iv_title;
    private View footView;
    private View view_line_this_order;
    private RelativeLayout rl_member_discount;
    private RelativeLayout rl_order_remark;
    private LinearLayout ll_should_pay;
    private LinearLayout ll_order_number;
    private LinearLayout ll_order_addtime;
    private View line_de_order_time_num;
    private TextView change_order_money;
    private View headView;
    private TextView tv_current_room;
    private ResumeDetailAdapter adapter;
    private AlertDialog alertDialog;
    /**
     * 打印
     */
    private Button btn_print;
    /**
     * 查看总订单
     */
    private Button btn_see_all;
    private String TAG = "AddOrderDetailActivity";
    private static final int UPDATE_MONEY = 0;
    private String unpay_order_id;
    private UnPayDetailsBean order_bean;
    private String query_num;
    private DetailMemberBean memberBean;
    private UnPayDetailsBean showSubOrderBean;
    private IWoyouService iWoyouService;
    private ICallback callBack1 = new ICallback.Stub() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {
            if (isSuccess) {//第一联打印成功
                leftTime = 5;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isMainActivityTop()) {
                            showPrintSecondDia();
                            handler.sendEmptyMessageDelayed(UPDATE_PRINT_TIME, 1000);
                        } else {
                            printSecond();
                        }
                    }
                });

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
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
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
            String printItems = SpUtils.getInstance(AddOrderDetailActivity.this).getString(Constant.print_permission, "");
            if(printItems.contains("1")) {
                localPrint();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    private int leftTime;
    private int yun_print_index;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_PRINT_TIME://等待打印下联
                    leftTime--;
                    LogUtils.Log("handler...leftTime==" + leftTime);
                    tv_left_printtime.setText(leftTime + "秒后自动打印第二联");
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
                case Constant.WAIT_BLUETOOTH_OPEN://等待蓝牙打开
                    BTPrintUtils.getInstance().connectBtPrinterTest(mService,AddOrderDetailActivity.this, handler);
                    break;
                case Constant.MESSAGE_STATE_CHANGE:
                    LogUtils.e(TAG, "收到了连接状态变更的消息");
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(AddOrderDetailActivity.this, "已连接", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(AddOrderDetailActivity.this, "正在连接", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(AddOrderDetailActivity.this, "无连接", Toast.LENGTH_SHORT).show();
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
    private TextView tv_left_printtime;
    private AlertDialog waitPrintSecDia;
    /**
     * 蓝牙打印
     */
    private BluetoothService mService;
    private String btPrintItems;
    private boolean containMember;
    private List<WmPintData> oneByOneList;
    private List<WmPintData> oneByOneBtList;
    private List<YunPrintGroupBean> oneByOneYunList;
    private String printItems;
    private List btPrintData;
    private List printData;
    private List<YunPrintGroupBean> yunPrintGroupBeans;
    private List<YunPrintGroupBean> yunNormalGroupBeans;
    private List<SavedPrinter> savedPrinterses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_detail);
        this.memberBean = (DetailMemberBean) getIntent().getSerializableExtra(Constant.MEMBER_BEAN);
        btPrintItems = SpUtils.getInstance(this).getString(Constant.bt_print_permission, "");

        printItems = SpUtils.getInstance(this).getString(Constant.print_permission, "");

        mService=BluetoothService.getInstance();
        mService.setHandler(handler);
        initView();
        initShowData();
        initPrintData();
        PrintUtils.getInstance().initService(this, connService);

        //1.云打印
        startYunPrint(true);
        //2.第1次提交打印,本地打印机
        if(btPrintItems.contains("1")) {
            BTPrintUtils.getInstance().wmBtPrint(mService, btPrintData,this,null);
        }
        setListener();
    }

    /**
     * 开启云打印
     */
    private void startYunPrint(boolean printAuto) {
        LogUtils.e(TAG,"yunPrint()");
        if(yunNormalGroupBeans.size()>0||yunPrintGroupBeans.size()>0) {
            savedPrinterses = (List<SavedPrinter>) SaveUtils.getObject(this, Constant.YUN_PRINTERS);
            if(savedPrinterses!=null&&savedPrinterses.size()>0) {
                YunPrintUtils.yunPrintOneByOneNew(this,savedPrinterses,yunPrintGroupBeans,yunNormalGroupBeans,"3","2",printAuto,null);
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
     * 初始化View
     */
    private void initView() {
        btn_print = (Button) findViewById(R.id.btn_print);
        btn_see_all = (Button) findViewById(R.id.btn_see_all);

        lv_order_details = (ListView) findViewById(R.id.lv_order_details);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("分订单明细");
        initFootHeadView();
        lv_order_details.addFooterView(footView);
        lv_order_details.addHeaderView(headView);
    }

    /**
     * 初始化脚布局和头布局
     */
    private void initFootHeadView() {

        //1.头布局
        headView = View.inflate(this, R.layout.add_order_header, null);
        tv_current_room = (TextView) headView.findViewById(R.id.tv_current_room);

        //2.脚布局
        footView = View.inflate(this, R.layout.order_detail_footview, null);
        rl_member_discount = (RelativeLayout) footView.findViewById(R.id.rl_member_discount);
        rl_member_discount.setVisibility(View.GONE);
        rl_order_remark = (RelativeLayout) footView.findViewById(R.id.rl_order_remark);
        rl_order_remark.setVisibility(View.GONE);
        ll_should_pay = (LinearLayout) footView.findViewById(R.id.ll_bottom);
        ll_should_pay.setVisibility(View.GONE);
        ll_order_number = (LinearLayout) footView.findViewById(R.id.ll_order_number);
        ll_order_number.setVisibility(View.GONE);
        ll_order_addtime = (LinearLayout) footView.findViewById(R.id.ll_order_addtime);
        ll_order_addtime.setVisibility(View.GONE);
        line_de_order_time_num = footView.findViewById(R.id.line_de_order_time_num);
        line_de_order_time_num.setVisibility(View.GONE);
        change_order_money = (TextView) footView.findViewById(R.id.change_order_money);
        change_order_money.setVisibility(View.GONE);
        tv_total_money = (TextView) footView.findViewById(R.id.tv_total_money);
        tv_total_money.setVisibility(View.GONE);
        view_line_fen_order = footView.findViewById(R.id.view_line_fen_order);
        view_line_fen_order.setVisibility(View.GONE);

        view_line_this_order = footView.findViewById(R.id.view_line_this_order);
        view_line_this_order.setVisibility(View.VISIBLE);
        tv_total_money_this_order = (TextView) footView.findViewById(R.id.tv_total_money_this_order);
        tv_total_money_this_order.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化数据
     */
    private void initShowData() {
        //获取主键id即为OrderId
        unpay_order_id = getIntent().getStringExtra("unpay_order_id");
        query_num = getIntent().getStringExtra("query_num");
        order_bean = (UnPayDetailsBean) getIntent().getSerializableExtra("order_bean");
        LogUtils.e(TAG, "Constant.localOrderBean=" + (new Gson().toJson(Constant.localOrderBean)));

        //本地存储的订单进行显示
        List<GoodBean> goodList = Constant.localOrderBean.getGoodList();

        //数据转换转换为OrderBean进行显示
        showSubOrderBean = new UnPayDetailsBean();

        UnPayDetailsBean.MsgBean msgBean = new UnPayDetailsBean.MsgBean();
        if(order_bean!=null){
            List<UnPayDetailsBean.MsgBean.GroupInfoBean> group_info = order_bean.getMsg().getGroup_info();
            List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms = order_bean.getMsg().getOrder_rooms();
            msgBean.setOrder_rooms(order_rooms);
            msgBean.setGroup_info(group_info);
        }
        double this_time_money = 0;
        if (goodList != null && goodList.size() > 0) {
            List<UnPayDetailsBean.MsgBean.OrderGoodsBean> orderEntityBeanList = new ArrayList<>();
            for (int i = 0; i < goodList.size(); i++) {
                if(goodList.get(i).getGoodsList()!=null && goodList.get(i).getGoodsList().size() > 0){
                    for(int k=0;k<goodList.get(i).getGoodsList().size();k++){
                        if(goodList.get(i).getGoodsList().get(k).getSizeNum()>0){
                            UnPayDetailsBean.MsgBean.OrderGoodsBean orderEntityBean = new UnPayDetailsBean.MsgBean.OrderGoodsBean();
                            orderEntityBean.setId(goodList.get(i).getGoodsId());
                            orderEntityBean.setGoods_name(goodList.get(i).getGoodsName()+"-"+goodList.get(i).getGoodsList().get(k).getSizeName());
                            if(memberBean!=null&&goodList.get(i).getGoodsList().get(k).getVipPrice()>=0) {
                                orderEntityBean.setGoods_price(goodList.get(i).getGoodsList().get(k).getVipPrice());
                                orderEntityBean.setGoods_ori_price(goodList.get(i).getGoodsList().get(k).getSizePrice());
                                orderEntityBean.setTotal_fee(goodList.get(i).getGoodsList().get(k).getSizeNum() * goodList.get(i).getGoodsList().get(k).getVipPrice());
                                this_time_money += goodList.get(i).getGoodsList().get(k).getSizeNum() * goodList.get(i).getGoodsList().get(k).getVipPrice();
                            }else {
                                orderEntityBean.setGoods_price(Double.valueOf(goodList.get(i).getGoodsList().get(k).getSizePrice()));
                                orderEntityBean.setTotal_fee(goodList.get(i).getGoodsList().get(k).getSizeNum() * goodList.get(i).getGoodsList().get(k).getSizePrice());
                                this_time_money += goodList.get(i).getGoodsList().get(k).getSizeNum() * goodList.get(i).getGoodsList().get(k).getSizePrice();
                            }
                            int is_vip=goodList.get(i).getGoodsList().get(k).getVipPrice()<0||memberBean==null?0:1;
                            if(!containMember) {
                                containMember=(is_vip==1);
                            }
                            orderEntityBean.setIs_vip(is_vip);
                            orderEntityBean.setGroup_id(goodList.get(i).getGroupId());
                            orderEntityBean.setGroup_name(goodList.get(i).getGroupName());
                            orderEntityBean.setSize_id(goodList.get(i).getGoodsList().get(k).getSizeId());
                            orderEntityBean.setSize_name(goodList.get(i).getGoodsList().get(k).getSizeName());
                            orderEntityBean.setGoods_num(goodList.get(i).getGoodsList().get(k).getSizeNum());
                            orderEntityBeanList.add(orderEntityBean);
                        }
                    }
                }else{
                    UnPayDetailsBean.MsgBean.OrderGoodsBean orderEntityBean = new UnPayDetailsBean.MsgBean.OrderGoodsBean();
                    orderEntityBean.setGoods_num(goodList.get(i).getGoodsNum());
                    orderEntityBean.setId(goodList.get(i).getGoodsId());
                    orderEntityBean.setGoods_name(goodList.get(i).getGoodsName());
                    if(memberBean!=null&&goodList.get(i).getVipPrice()>=0) {
                        orderEntityBean.setGoods_price(goodList.get(i).getVipPrice());
                        orderEntityBean.setGoods_ori_price(goodList.get(i).getGoodsPrice());
                        orderEntityBean.setTotal_fee(goodList.get(i).getGoodsNum() * goodList.get(i).getVipPrice());
                        this_time_money += goodList.get(i).getVipPrice() * goodList.get(i).getGoodsNum();
                    }else {
                        orderEntityBean.setGoods_price(goodList.get(i).getGoodsPrice());
                        orderEntityBean.setTotal_fee(goodList.get(i).getGoodsNum() * goodList.get(i).getGoodsPrice());
                        this_time_money += goodList.get(i).getGoodsPrice() * goodList.get(i).getGoodsNum();
                    }
                    int is_vip=goodList.get(i).getVipPrice()<0||memberBean==null?0:1;
                    if(!containMember) {
                        containMember=(is_vip==1);
                    }
                    orderEntityBean.setIs_vip(is_vip);
                    orderEntityBean.setGroup_id(goodList.get(i).getGroupId());
                    orderEntityBean.setGroup_name(goodList.get(i).getGroupName());
                    orderEntityBean.setSize_id("0");
                    orderEntityBeanList.add(orderEntityBean);
                }
            }
            msgBean.setOrder_goods(orderEntityBeanList);
        }
        //设置订单id
        msgBean.setOrderid(Integer.valueOf(unpay_order_id));
        showSubOrderBean.setMsg(msgBean);

        LogUtils.e(TAG,"showSubOrderBean.json="+new Gson().toJson(showSubOrderBean));

        //头布局的数据显示
        if (Constant.localOrderBean.getCurrentRoomName() != null) {
            if("".equals(Constant.localOrderBean.getCurrentRoomName())||"无".equals(Constant.localOrderBean.getCurrentRoomName())){
                tv_current_room.setText("订单序号：" + query_num +" 新增商品");
            }else{
                tv_current_room.setText("房间：" + Constant.localOrderBean.getCurrentRoomName() +" 新增商品");
            }
        } else {
            tv_current_room.setText("订单序号：" + query_num +" 新增商品");
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String format = decimalFormat.format(this_time_money);
        tv_total_money_this_order.setText("总金额：" + format);

        //商品的显示
        adapter = new ResumeDetailAdapter(this, tv_total_money_this_order);
        adapter.setData(showSubOrderBean);
        adapter.setContainMember(containMember);
        adapter.setIsShowMAbtn(false);
        adapter.setDataFrom(Constant.ORDER_DETAIL);
        lv_order_details.setAdapter(adapter);

    }

    private void initPrintData() {
        btPrintData = PrintUtils.getInstance().formatPosOrder(this, showSubOrderBean, true);
        printData = PrintUtils.getInstance().formatPosOrder(this, showSubOrderBean, false);

        yunPrintGroupBeans = YunPrintUtils.formatPrePrintData(showSubOrderBean, false);
        yunNormalGroupBeans = YunPrintUtils.formatPrePrintData(showSubOrderBean,true);
//        headData = new ArrayList<>();
//        btheadData = new ArrayList<>();
//        showSubOrderBean.getMsg().setAddtime(String.valueOf(Utils.getCurrentTimeMill()/1000));
//        PrintUtils.getInstance().firstComitFormatData(showSubOrderBean, headData, false, false);//本地打印顾客联
//        PrintUtils.getInstance().firstComitFormatData(showSubOrderBean, btheadData, true, false);//蓝牙打印顾客联
//        PrintUtils.getInstance().firstComitFormatData(showSubOrderBean, groupHeadData, false, true);
//        oneByOneList=PrintUtils.getInstance().oneByOneData(showSubOrderBean,this,false);//一菜一单
//        oneByOneBtList=PrintUtils.getInstance().oneByOneData(showSubOrderBean,this,true);//一菜一单

    }


    /**
     * 设置监听
     */
    private void setListener() {
        iv_title.setOnClickListener(this);
        btn_print.setOnClickListener(this);
        btn_see_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title:
                clearLocalOrderBean();
                finish();
                break;
            case R.id.btn_print:
                if (ClicKUtils.isFastDoubleClick()) {
                    return;
                } else {
                    localPrint();
                    startYunPrint(false);
                    if(btPrintData!=null&&btPrintData.size()>0) {
                        BTPrintUtils.getInstance().wmBtPrint(mService, btPrintData,this,null);
                    }
                }
                break;
            case R.id.btn_see_all://查看总订单到OrderDetailActivity
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("unpay_order_id", unpay_order_id);//主键id
                intent.putExtra(Constant.MEMBER_BEAN, memberBean);
                intent.putExtra("show_dialog", true);
                startActivity(intent);
                clearLocalOrderBean();
                finish();
                break;
        }
    }
    /**
     * 清除本地存储的订单
     */
    private void clearLocalOrderBean() {
        if (Constant.localOrderBean.getGoodList() != null) {
            Constant.localOrderBean.setGoodList(null);
        }
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

    private void dismisPrintingDia() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
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
        LogUtils.Log("showPrintSecondDia()");
        waitPrintSecDia = new AlertDialog.Builder(this)
                .setView(tv_left_printtime)
                .setPositiveButton("立即打印", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.removeMessages(UPDATE_PRINT_TIME);
                        leftTime = -1;
                        printSecond();
                    }
                })
                .setNegativeButton("无需打印", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.removeMessages(UPDATE_PRINT_TIME);
                        leftTime = -1;
                        dismisPrintingDia();
                    }
                })
                .show();
        waitPrintSecDia.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(this, 140));
        waitPrintSecDia.setCanceledOnTouchOutside(false);
        waitPrintSecDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK;
            }
        });
    }

    private void printSecond() {
        if(oneByOneList!=null&&oneByOneList.size()>0) {
            PrintUtils.getInstance().printWm(iWoyouService, oneByOneList, printCallBack2);
        }
    }

    @Override
    protected void onPause() {
        handler.removeCallbacksAndMessages(null);
        LogUtils.Log("onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        LogUtils.Log("onResume");
        LogUtils.Log("lefttime==" + leftTime);
        if (leftTime > 0) {
            handler.sendEmptyMessage(UPDATE_PRINT_TIME);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unbindService(connService);
        super.onDestroy();
    }
    /**
     *
     * 判断mainactivity是否处于栈顶
     * @return  true在栈顶false不在栈顶
     */
    private boolean isMainActivityTop(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(AddOrderDetailActivity.class.getName());
    }
}

