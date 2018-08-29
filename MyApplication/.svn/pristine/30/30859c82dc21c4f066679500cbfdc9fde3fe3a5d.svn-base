package com.younle.younle624.myapplication.activity.pos;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter.print.PrintMsgEvent;
import com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter.print.PrinterMsgType;
import com.younle.younle624.myapplication.application.MyApplication;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.PayCardinfoBean;
import com.younle.younle624.myapplication.domain.SavedFailOrder;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.domain.paybean.DiscountInfo;
import com.younle.younle624.myapplication.myservice.BluetoothService;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.printmanager.BTPrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.PrintDia;
import com.younle.younle624.myapplication.utils.printmanager.PrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.YunPrintUtils;
import com.younle.younle624.myapplication.utils.scanbar.ScanGunKeyEventHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * Created by Administrator on 2016/5/24.
 */
public class CollectionSuccessActivity extends Activity {
    private static final String TAG = "CollectionSuccessActivity";
    private Button mBtDetermine;
    private TextView tv_account_money;
    private TextView tv_pay_way;
    private TextView tv_merchant_num;
    private LinearLayout all_trade_num;
    private TextView tv_trade_num;
    private TextView tv_trade_time;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private TextView tv_left_printtime;
    //intent得到的显示数据
    private String tradenum = "0";
    private String payment = "0";
    private String total_fee = "0";
    private String freemoney = "0";
    private String tradetime = "0";
    private  final int UPDATE_UI = 2;
    private  final int WAIT_BLUETOOTH_OPEN = 1;
    private final int UPDATE_PRINT_TIME=3;
    private int leftTime;
    private AlertDialog waitPrintSecDia;
    private UnPayDetailsBean orderBean;
    private String printJZTime;
    /**
     * 备注
     */
    private EditText et_remak_content;
    private LinearLayout all_remark;
    private View v_line1;
    private View v_line2;
    /**
     * 会员优惠信息
     */
    private DiscountInfo discountBean;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            LogUtils.Log("CollectionSuccessActivity中的handler:"+handler);
            switch (msg.what) {
                case UPDATE_UI:
                    Toast.makeText(CollectionSuccessActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case Constant.WAIT_BLUETOOTH_OPEN://等待蓝牙打开
                    BTPrintUtils.getInstance().connectBtPrinterTest(mService,CollectionSuccessActivity.this,handler);
                    break;
                case UPDATE_PRINT_TIME://等待打印下联
                    leftTime--;
                    LogUtils.Log("leftTime=="+leftTime);
                    tv_left_printtime.setText(leftTime+"秒后自动打印第二联");
                    if(leftTime<=0) {
                        handler.removeMessages(UPDATE_PRINT_TIME);
                        if(waitPrintSecDia!=null&&waitPrintSecDia.isShowing()) {
                            waitPrintSecDia.dismiss();
                        }
                        printSecond();
                    }else {
                        handler.sendEmptyMessageDelayed(UPDATE_PRINT_TIME,1000);
                    }
                    break;
                case 10:
                    int progress= (int) msg.obj;
                    LogUtils.Log("打印进度:"+progress);
                    break;
                case Constant.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(CollectionSuccessActivity.this, "已连接", Toast.LENGTH_SHORT).show();
                            LogUtils.e(TAG, "已连接");
                            PrintByBluetooth();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(CollectionSuccessActivity.this, "正在连接", Toast.LENGTH_SHORT).show();
                            LogUtils.e(TAG, "正在连接");

                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(CollectionSuccessActivity.this, "无连接", Toast.LENGTH_SHORT).show();
                            LogUtils.e(TAG, "无连接");
                            break;
                    }
                    break;
                case Constant.FINISH_PRINT:
                    LogUtils.Log("蓝牙打印完成");
                    break;
            }
        }
    };
    private IWoyouService iWoyouService;
    public ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWoyouService = IWoyouService.Stub.asInterface(service);
            String printSettings = SpUtils.getInstance(CollectionSuccessActivity.this).getString(Constant.print_permission, "");
            LogUtils.Log("CollectionSuccessActivity  printSetting=="+printSettings);
            if(printSettings.contains("5")) {//打顾客联
                showPrintingDia();
                if(printSettings.contains("4")) {//同时打商家联
                    LogUtils.e(TAG,"直接收银商家联：");
                    PrintUtils.getInstance().zsmcPrint(iWoyouService, headerData, true, memberData, 28, printCallBack);
//                    PrintUtils.getInstance().newPosPrint(iWoyouService,headerData,printCallBack);
                }else {//不打商家联
                    PrintUtils.getInstance().zsmcPrint(iWoyouService, headerData, true, memberData, 28, printCallBack2);
                }
            }else {//不打顾客联
                if(printSettings.contains("4")) {//打商家联
                    showPrintingDia();
                    printSecond();
                }
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    private String memberFreeMoney;
    private BluetoothService mService;
    private String btPrintItems;

    private void dismisPrintingDia() {
        if(printIngDia !=null&& printIngDia.isShowing()) {
            printIngDia.dismiss();
        }
    }

    private List<String> headerData;
    private List<String> memberData;

    private List<String> btmemberData;
    private List<String> btheaderData;
    private String query_num = "";//打印的五位订单号
    private String transaction_id = "";//打印的五位订单号
    private PayCardinfoBean cardinfoPayBean;
    private AlertDialog openBluetoothDia;
    private LinearLayout all_pay_success;
    //分线程
    private ICallback printCallBack=new ICallback.Stub() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {
            LogUtils.Log("onRunResult");
            if(isSuccess) {//第一联打印成功
                leftTime = 5;
                tv_left_printtime.setText(leftTime + "秒后自动打印第二联");
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
    private ICallback printCallBack2=new ICallback.Stub() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {
            if(isSuccess) {//第二联打印成功
                dismisPrintingDia();
            }
        }
        @Override
        public void onReturnString(String result) throws RemoteException {
            LogUtils.Log("onReturnString:"+result);
        }
        @Override
        public void onRaiseException(int code, String msg) throws RemoteException {
            LogUtils.Log("onRaiseException:"+msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.pay_success);
        LogUtils.e(TAG,"onCreate()");
        btPrintItems = SpUtils.getInstance(this).getString(Constant.bt_print_permission, "");
        mService=BluetoothService.getInstance();
        mService.setHandler(handler);
//        mService=  BTPrintUtils.getInstance().connectBtPrinter(this, handler);
        Utils.initToolBarState(this);
        initView();
        initData();
        setListener();
    }

    /**
     * 打印商家联
     */
    private void printSecond() {
        headerData.remove(1);
        PrintUtils.getInstance().zsmcPrint(iWoyouService, headerData, false, null, 22, printCallBack2);
    }
    /**
     * handle printer message
     * @param event print msg event
     */
    @Subscribe
    public void onEventMainThread(PrintMsgEvent event) {
        if (event.type == PrinterMsgType.MESSAGE_TOAST) {
            Message msg=new Message();
            msg.obj=event.msg;
            msg.what=UPDATE_UI;
            handler.sendMessage(msg);
        }
    }
    @Subscribe
    public void onEventMainThread(Boolean btFinish) {
        if(btFinish) {
            LogUtils.Log("蓝牙打印第一联完成");
        }
        EventBus.getDefault().cancelEventDelivery(btFinish);
    }
    //初始化视图
    private void initView() {
        all_pay_success = (LinearLayout)findViewById(R.id.all_pay_success);
        tv_left_printtime = new TextView(CollectionSuccessActivity.this);
        tv_left_printtime.setTextColor(Color.BLACK);
        tv_left_printtime.setTextSize(Utils.dip2px(CollectionSuccessActivity.this, 12));
        tv_left_printtime.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        tv_left_printtime.setHeight(Utils.dip2px(this, 80));
        tv_left_printtime.setGravity(Gravity.BOTTOM | Gravity.CENTER);

        mBtDetermine = (Button) findViewById(R.id.bt_determine);
        tv_account_money = (TextView) findViewById(R.id.tv_account_money);
        tv_pay_way = (TextView) findViewById(R.id.tv_pay_way);
        tv_merchant_num = (TextView) findViewById(R.id.tv_merchant_num);
        all_trade_num = (LinearLayout) findViewById(R.id.all_trade_num);
        tv_trade_num = (TextView) findViewById(R.id.tv_trade_num);
        tv_trade_time = (TextView) findViewById(R.id.tv_trade_time);

        et_remak_content = (EditText)findViewById(R.id.et_remak_content);
        all_remark = (LinearLayout) findViewById(R.id.all_remark);
        v_line1 =  findViewById(R.id.v_line1);
        v_line2 =  findViewById(R.id.v_line2);
    }

    //初始化数据
    private void initData() {
        Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
        //获取Intent数据进行显示
        String tradenumExtra = getIntent().getStringExtra("tradenum");
        String paymentExtra = getIntent().getStringExtra("payment");
        String tradetimeExtra = getIntent().getStringExtra("tradetime");
        String queryNumExtra = getIntent().getStringExtra("querynum");
        String transactionidExtra = getIntent().getStringExtra("transaction_id");
        String total_feeExtra = getIntent().getStringExtra("total_fee");
        String freemoneyExtra = getIntent().getStringExtra("freemoney");
        String cardinfo_payExtra = getIntent().getStringExtra("cardinfo_pay");
        orderBean = (UnPayDetailsBean) getIntent().getSerializableExtra(Constant.ORDER_BEAN);
        discountBean = (DiscountInfo) getIntent().getSerializableExtra(Constant.MEMBER_DISCOUNT_BEAN);
        memberFreeMoney = getIntent().getStringExtra(Constant.MEMBER_FREE_MONEY);

        if(orderBean!=null&&!"".equals(orderBean.getMsg().getRemark())){
            et_remak_content.setText(orderBean.getMsg().getRemark());
        }

        if (total_feeExtra != null){total_fee = total_feeExtra;}
        if (freemoneyExtra != null){freemoney = Utils.keepTwoDecimal(freemoneyExtra);}
        if (tradenumExtra != null){tradenum = tradenumExtra;}
        if (paymentExtra != null){payment = paymentExtra;}
        if (queryNumExtra != null){query_num = queryNumExtra;}
        if (transactionidExtra != null){transaction_id = transactionidExtra;}

        if (tradetimeExtra != null&&!"".equals(tradetimeExtra)){
            tradetime = tradetimeExtra;
            //结账时间
            printJZTime = Utils.getCurrentM(Long.valueOf(tradetime) * 1000);
        }
        if (cardinfo_payExtra != null&&!"".equals(cardinfo_payExtra)){
            cardinfoPayBean = new Gson().fromJson(cardinfo_payExtra,PayCardinfoBean.class);
        }
        String way="未知";
        switch (Constant.payway) {
            case "0" :
                all_trade_num.setVisibility(View.VISIBLE);
                way="微信收款";
                break;
            case "1" :
                all_trade_num.setVisibility(View.VISIBLE);
                way="支付宝收款";
                break;
            case "2" :
                way="刷卡收款";
                break;
            case "3" :
                way="现金收款(记账)";
                break;
            case "4" :
                way="会员卡余额收款";
                break;
            case "6":
                way="刷卡收款(记账)";
                break;
            case "8":
                way="微信收款(记账)";
                break;
            case "9":
                way="支付宝收款(记账)";
                break;
        }

        //1.展示数据
        showMsg(way);
        String vip_discount = "";
        if(cardinfoPayBean!=null){
            if("CASH".equals(cardinfoPayBean.getCardtype())){
                vip_discount = "优惠券："+cardinfoPayBean.getInfo();
                initPrinterData(Double.parseDouble(total_fee),payment,vip_discount, way);
            }else if("DISCOUNT".equals(cardinfoPayBean.getCardtype())){
                vip_discount = "优惠券:"+cardinfoPayBean.getInfo();
                initPrinterData(Double.parseDouble(total_fee),payment,vip_discount, way);
            }
        }else{
            if(payment!=null&&!"0".equals(payment)&& total_fee !=null&&!"0".equals(total_fee)){
                double dis = Double.valueOf(payment)/Double.valueOf(total_fee);
                if(dis>0 && dis<1){
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");//如果小数不足2位,会以0补足.
                    vip_discount = "会员折扣："+decimalFormat.format(dis*10)+"折";
                }
                initPrinterData(Double.parseDouble(total_fee),payment,vip_discount, way);
            }else{
                initPrinterData(Double.parseDouble(total_fee),payment,vip_discount, way);
            }
        }

        //2.打印数据
        //initPrinterData(Double.parseDouble(payment), total_fee, vip_discount, way);
        PrintUtils.getInstance().initService(this, connService);

        if(btPrintItems.contains("4")||btPrintItems.contains("5")) {
            if(mService.getState()==BluetoothService.STATE_CONNECTED) {
                PrintByBluetooth();
            }else {
                BTPrintUtils.getInstance().connectBtPrinterTest(mService,this,handler);
            }
        }
    }
    private void initPrinterData(double orderAmount,String total_fee,String vip_discount,String payWay) {
        headerData =new ArrayList<>();
        memberData=new ArrayList<>();

        btheaderData =new ArrayList<>();
        btmemberData=new ArrayList<>();

        //1.公共头
        /*headerData.add(new WmPintData(Constant.STORE_P + Constant.STORE_M, 35, WmPintData.CENTER));
        headerData.add(new WmPintData("电话：" + Constant.STORE_TEL, 27, WmPintData.CENTER));*/
        headerData.add(Constant.STORE_P + Constant.STORE_M);
        headerData.add("电话：" + Constant.STORE_TEL);

        btheaderData.add(Constant.STORE_P + Constant.STORE_M);
        btheaderData.add("电话：" + Constant.STORE_TEL);
        //2.不同部分
        if(Constant.UNKNOWN_COMMODITY==null) {
            Utils.showToast(this,"数据异常",1000);
            return;
        }

        switch (Constant.UNKNOWN_COMMODITY) {
            case "0" ://下单支付'
                PrintUtils.getInstance().initFOPrintData(orderBean,discountBean,headerData,memberData,printJZTime,tradenum,
                        payWay,transaction_id,memberFreeMoney,false);
                PrintUtils.getInstance().initFOPrintData(orderBean,discountBean,btheaderData,btmemberData,printJZTime,tradenum,
                        payWay,transaction_id,memberFreeMoney,true);
                break;
            case "1" ://直接收款
                PrintUtils.getInstance().initZSPrintData(query_num, tradenum, tradetime, discountBean,
                        orderAmount,total_fee,vip_discount, payWay, headerData, memberData,transaction_id,false,freemoney);
                PrintUtils.getInstance().initZSPrintData(query_num, tradenum, tradetime, discountBean,
                        orderAmount,total_fee,vip_discount, payWay, btheaderData, btmemberData,transaction_id,true,freemoney);
                break;
            case "2"://会员充值
                LogUtils.e(TAG,"会员充值 Constant.MEMBER_ID="+Constant.MEMBER_ID);
                Constant.CHARGE_SUCCESS=true;
                all_remark.setVisibility(View.GONE);
                v_line1.setVisibility(View.GONE);
                v_line2.setVisibility(View.GONE);

                PrintUtils.getInstance().initMCPrintData(tradenum,tradetime,orderAmount,payWay,headerData,memberData,
                        transaction_id,false);
                PrintUtils.getInstance().initMCPrintData(tradenum,tradetime,orderAmount,payWay,btheaderData,btmemberData,
                        transaction_id,true);
                break;
        }
        //云打印数据格式化
        YunPrintUtils.formateDirPay(this, btheaderData, btmemberData);
    }
    //显示intent的数据
    private void showMsg(String pay_type) {
        if(orderBean!=null) {
            payment =orderBean.getMsg().getPayment()+"";
        }
        tv_account_money.setText(Utils.keepTwoDecimal(payment) + "元");
        tv_merchant_num.setText(tradenum);
        tv_trade_time.setText(Utils.getCurrentM(Long.valueOf(tradetime) * 1000));
        tv_pay_way.setText(pay_type);
        if(transaction_id!=null&&!"".equals(transaction_id)&&!"0".equals(transaction_id)){
            tv_trade_num.setText(transaction_id);
        }
    }
    /**
     * 提示打印第二联的dialog
     */
    private void showPrintSecondDia() {
        waitPrintSecDia=PrintDia.getInstance().showPrintSecondDia(this, tv_left_printtime, "立即打印", "无需打印", new PrintDia.OnHandleListener() {
            @Override
            public void onPositveButton() {
                handler.removeMessages(UPDATE_PRINT_TIME);
                printSecond();
                leftTime=-1;
            }
            @Override
            public void onNegativeButton() {
                handler.removeMessages(UPDATE_PRINT_TIME);
                dismisPrintingDia();
                leftTime=-1;
            }
        });
    }
    /**
     * 蓝牙打印（输入输出流）
     */
    private void PrintByBluetooth() {

        if(btPrintItems.contains("5")) {//打印顾客联
            if(btPrintItems.contains("4")) {//同时打印商家联
                startBtPrint(true, new BluetoothService.BtPrintFinshCallBack() {
                    @Override
                    public void onError() {
                    }
                    @Override
                    public void onFinish() {
                        startBtPrint(false,null);
                    }
                });
            }else {
                startBtPrint(true,null);
            }
        }else {//不打顾客联
            if(btPrintItems.contains("4")) {//打印商家联
                startBtPrint(false,null);
            }
        }
    }

    public void startBtPrint( boolean forCustomer, BluetoothService.BtPrintFinshCallBack callBack) {
        if(!forCustomer) {
            btheaderData.remove(1);
        }
        BTPrintUtils.getInstance().csDetailBtPrint(mService, btheaderData, forCustomer, btmemberData,
                CollectionSuccessActivity.this, callBack);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK :
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 打印中的dialog
     */
    private AlertDialog printIngDia;
    private void showPrintingDia() {
        printIngDia=PrintDia.getInstance().showPrintingDia(this, new PrintDia.PrintingCallBacK() {
            @Override
            public void close() {
                handler.removeMessages(UPDATE_PRINT_TIME);
                dismisPrintingDia();
            }
        });
    }

    /**
     * 点击确定的监听
     * 1.判断备注状态
     * 2.提交至服务器
     * 3.失败的处理
     */
    private void setListener() {
        mBtDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_remak_content.getText().toString();
                LogUtils.e(TAG,"msg="+msg);
                if("".equals(msg)){
                    finish();
                }else{
                    if(orderBean != null && msg.equals(orderBean.getMsg().getRemark())){
                        finish();
                        return;
                    }
                    //1.输入信息和进页面时是否相同，判断是否是本地订单
                    String firstLetter = query_num.substring(0, 1);
                    if("A".equals(firstLetter)) {
                        //3.非本地订单直接提交到服务器
                        if(TextUtils.isEmpty(msg)) {
                            Utils.showToast(CollectionSuccessActivity.this,"未检测到输入信息");
                            return;
                        }
                        if(msg.length()>100) {
                            Utils.showToast(CollectionSuccessActivity.this,"备注信息不得超过100个字符");
                            return;
                        }
                        if(orderBean!=null){
                            if(orderBean.getMsg().getRemark()!=null){
                                Utils.showWaittingDialog(CollectionSuccessActivity.this,"备注提交中...");
                                updateRemak(0,msg);
                            }
                        }else{
                            //直接收款成功页面
                            updateRemak(1,msg);
                        }
                        Constant.query_member_left = "";
                    }else {
                        //2.本地订单保存备注到数据库:匹配本地订单中与当前订单号一直的订单加上备注信息
                        Utils.showWaittingDialog(CollectionSuccessActivity.this,"备注提交中...");
                        MyApplication myAppinstance = MyApplication.getInstance();
                        DbManager.DaoConfig daoConfig = myAppinstance.getDaoConfig();
                        DbManager db = x.getDb(daoConfig);
                        try {
                            List<SavedFailOrder> savedLists = db.findAll(SavedFailOrder.class);
                            if(savedLists!=null){
                                for(int i=0;i<savedLists.size();i++){
                                    if(query_num.equals(savedLists.get(i).getQuery_num())){
                                        savedLists.get(i).setRemarsinfo(msg);
                                        break;
                                    }
                                }
                                //存储本地：
                                db.dropTable(SavedFailOrder.class);
                                for(int m=0;m<savedLists.size();m++){
                                    db.save(savedLists.get(m));
                                }
                                Utils.dismissWaittingDialog();
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                            Utils.dismissWaittingDialog();
                        }
                        //4.结束当前界面
                        finish();
                    }
                }
            }
        });
        et_remak_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }
    /**
     * 上传备注至服务器
     * @param tag
     * @param msg
     */
    private void updateRemak(int tag, final String msg) {
        String query_num = "0";
        switch (tag){
            case 0://0：下单支付
                query_num = orderBean.getMsg().getOrder_no();
                break;
            case 1://1：直接收银支付
                query_num = this.tradenum;
                break;
        }
        NetWorks netWorks=new NetWorks(this);
        netWorks.upDataRemak("0",query_num, msg, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("提交备注e=="+e.toString());
                finish();
                Utils.dismissWaittingDialog();
                Utils.showToast(CollectionSuccessActivity.this,"网络异常，提交备注失败！",1000);
            }
            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("提交备注response=="+response.toString());
                Utils.dismissWaittingDialog();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200) {
                        finish();
                    }else {
                        String notice = jsonObject.getString("err");
                        Utils.showToast(CollectionSuccessActivity.this,notice,1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },12);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacksAndMessages(null);
        LogUtils.Log("onPause()");
        super.onPause();
    }
    @Override
    protected void onResume() {
        LogUtils.Log("onResume:eftTime=="+leftTime);
        if(leftTime>0) {
            handler.sendEmptyMessage(UPDATE_PRINT_TIME);
        }
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        unbindService(connService);
        EventBus.getDefault().unregister(this);
        handler.removeCallbacksAndMessages(null);
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
        return name.equals(CollectionSuccessActivity.class.getName());
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(ScanGunKeyEventHelper.isScanGunEvent(event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
