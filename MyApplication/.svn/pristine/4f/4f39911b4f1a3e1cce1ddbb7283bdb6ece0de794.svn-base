package com.yongle.letuiweipad.pagers.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.MainActivity;
import com.yongle.letuiweipad.adapter.RecyclerAdapter;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.domain.ManagerScanBean;
import com.yongle.letuiweipad.domain.PrintDevices;
import com.yongle.letuiweipad.domain.SmScannerMemberBean;
import com.yongle.letuiweipad.domain.createorder.RulesBean;
import com.yongle.letuiweipad.domain.createorder.SavedFailOrder;
import com.yongle.letuiweipad.domain.membercharge.MemberChargeInfoBean;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.selfinterface.OnPayFinish;
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
import com.yongle.letuiweipad.utils.Utils;
import com.yongle.letuiweipad.utils.pay.PayUtils;
import com.yongle.letuiweipad.utils.printmanager.BTPrintUtils;
import com.yongle.letuiweipad.utils.printmanager.BluetoothService;
import com.yongle.letuiweipad.utils.printmanager.PrintUtils;
import com.yongle.letuiweipad.utils.printmanager.YunPrintUtils;
import com.yongle.letuiweipad.utils.scanbar.HidConncetUtil;
import com.yongle.letuiweipad.view.ScanRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import static com.yongle.letuiweipad.utils.Utils.df3;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class MemberChargePager extends ManagerBasePager {
    @BindView(R.id.charge_list)RecyclerView chargeList;
    @BindView(R.id.gif_scan)GifImageView gifScan;
    @BindView(R.id.tv_payscan_notice) TextView tvPayscanNotice;
    @BindView(R.id.center_line) View centerLine;
    @BindView(R.id.input_notice) TextView inputNotice;
    @BindView(R.id.et_enter_num)EditText etEnterNum;
    @BindView(R.id.tv_request_memberinfo)TextView tvRequestMemberinfo;
    @BindView(R.id.close)ImageView close;
    @BindView(R.id.tv_left) TextView tvLeft;
    @BindView(R.id.inditify_member)RelativeLayout inditifyMember;
    @BindView(R.id.memcharge_chargeinfo)RelativeLayout memchargeChargeinfo;
    @BindView(R.id.scanner_error)RelativeLayout scannerError;//读取会员信息时，扫码枪状态异常
    @BindView(R.id.scanner_error_msg)TextView scannerErrorMsg;
    @BindView(R.id.pay_module)LinearLayout payModule;
    @BindView(R.id.rg_payways)RadioGroup rg_payways;
    @BindView(R.id.find_voucher)TextView findVoucher;
    @BindView(R.id.pay_scaner_off)TextView pay_scaner_off;//支付时，扫码枪状态异常
    @BindView(R.id.pay_scanner_normal)RelativeLayout pay_scanner_normal;//支付时，扫码枪状态正常

    @BindView(R.id.member_name)TextView member_name;
    @BindView(R.id.member_cardno)TextView member_cardno;
    @BindView(R.id.member_balance)TextView member_balance;

    @BindView(R.id.rl_wx_zhf)RelativeLayout rl_wx_zhf;
    @BindView(R.id.ll_pay_card)RelativeLayout ll_pay_card;
    @BindView(R.id.ll_pay_member)LinearLayout ll_pay_member;
    @BindView(R.id.ll_pay_cash)LinearLayout ll_pay_cash;
    @BindView(R.id.iv_find_code1) TextView iv_find_code1;
    @BindView(R.id.iv_find_code2) TextView iv_find_code2;
    @BindView(R.id.tv_cashpay_shishou)  TextView tvCashpayShishou;
    @BindView(R.id.tv_cashpay_return) TextView tvCashpayReturn;
    @BindView(R.id.btn7)Button btn7;
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
    @BindView(R.id.no_permission) RelativeLayout no_permission;
    @BindView(R.id.card_pay_notice) TextView card_pay_notice;
    @BindView(R.id.tv_nopermission_notice) TextView tv_nopermission_notice;
    @BindView(R.id.finsh_card_pay) TextView finsh_card_pay;
    @BindView(R.id.card_pay_sure) TextView cardPaySure;
    @BindView(R.id.al_payway_error) RelativeLayout payWayError;
    @BindView(R.id.tv_payway_error_notice) TextView tvPaywayErrorNotice;
    @BindView(R.id.net_error)RelativeLayout netError;
    @BindView(R.id.net_error_refresh)TextView netErrorRefresh;
    private HashMap<View, String> map;

    /**
     * 可以监听keyevent的控件
     */
    @BindView(R.id.scan_rl_layot)ScanRelativeLayout scanRllayot;

    private RecyclerAdapter adapter;
    private List<String> data;
    private View baseView;
    private PrintDevices savedScannerDevice;
    private MemberChargeInfoBean memberChargeInfoBean;
    private List<RulesBean> rulesBeanList;
    private String way;
    private Double chargeMoney=0.0;
    private Double songMoney=0.0;
    private String vipcard_id="0";
    private NetWorks netWorks;
    private String tradeNum;
    private String trade_time;

    private IWoyouService iWoyouService;
    private boolean serviceBind=false;
    public ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.e(TAG,"onSeviceConnected()");
            serviceBind=true;
            iWoyouService = IWoyouService.Stub.asInterface(service);//拿到打印服务的对象
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case Constant.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(mActivity, "已连接", Toast.LENGTH_SHORT).show();
                            printPayedByBluetooth();
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
    private List<String> headerData;
    private List<String> btHeaderData;
    private List<String> memberData;
    private List<String> btMemberData;
    private String memberCardNo;
    private Double currentLeft;
    private double beforeLeft;
    private SavedPrinter btPrinter;
    private String btPrinterPermission;
    private SavedPrinter loaclPrinter;
    private String localPrinterPermission;
    private BluetoothService mService;

    public MemberChargePager() {
        EventBus.getDefault().register(this);
    }

    @Override
    public View initView() {
        baseView = View.inflate(mActivity, R.layout.member_charge_layout, null);
        return baseView;
    }
    /**
     * 扫描结束后的回调
     */
    @Subscribe
    public void onEventMainThread(ManagerScanBean scanBean) {
        if(Constant.MANAGER_POSITION!=1) {
            return;
        }
        String barcode = scanBean.getCode();
        LogUtils.e(TAG,"scanbean =="+ barcode);
        if(inditifyMember.getVisibility()==View.VISIBLE) {//会员识别
            getMemberInfo(barcode);
        }else {//支付
            //扫到支付码
            LogUtils.Log("扫描到支付条码：" + barcode + "   payWay==" + Constant.payway);
            if(TextUtils.equals("0",Constant.payway)&&Constant.OPEN_WXPAY==1&& NetworkUtils.isAvailable(mActivity)) {
                toPay(barcode);
            }else if(TextUtils.equals("1",Constant.payway)&&Constant.OPEN_ALIPAY==1&&NetworkUtils.isAvailable(mActivity)) {
                toPay(barcode);
            }
        }
        EventBus.getDefault().cancelEventDelivery(scanBean);
    }
    /**
     * 扫描结束后的回调
     */
    @Subscribe
    public void onEventMainThread(SmScannerMemberBean smScannerBean) {
        LogUtils.e(TAG,smScannerBean.getId()+"  核销页面，位置："+Constant.MANAGER_POSITION);

        if(Constant.MANAGER_POSITION!=1) {
            return;
        }
        if(TextUtils.equals(smScannerBean.getId(),"sm_scanner_member")) {
            if(savedScannerDevice!=null) {
                BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(savedScannerDevice.getBlueToothAdd());
                boolean scannerConnected = HidConncetUtil.getScannerConnectState(device);
                if(scannerConnected) {
                    scannerConnected();
                }else {
                    scannerDisconnected();
                }
            }else {
                scannerDisconnected();
            }
        }

        EventBus.getDefault().cancelEventDelivery(smScannerBean);
    }

    /**
     * 支付失败
     */
    private void payFail(String reason,String solution) {
        String nowTime = df3.format(System.currentTimeMillis());
        NoticePopuUtils.showPayFailPop(mActivity, baseView,reason,solution, way,tradeNum, nowTime,"暂无", null);

    }

    @Override
    public void initData(int position) {
        baseView.setVisibility(View.GONE);
        findVoucher.setVisibility(View.GONE);
        LogUtils.e(TAG,"会员充值 initData="+position);

        mService= BluetoothService.getInstance();
        mService.setHandler(handler);
        btPrinter = (SavedPrinter) SaveUtils.getObject(mActivity, Constant.BT_PRINTER);
        if(btPrinter!=null) {
            btPrinterPermission = btPrinter.getPrintPermission();
        }
        loaclPrinter = (SavedPrinter) SaveUtils.getObject(mActivity, Constant.LOCAL_PRINTER);
        if(loaclPrinter!=null) {
            localPrinterPermission = loaclPrinter.getPrintPermission();
        }

        close.setVisibility(View.GONE);
        tvLeft.setText("会员充值");
        etEnterNum.setText(null);
        if(netWorks==null) {
            netWorks=new NetWorks(mActivity);
        }
        queryMuduleInfo();
    }

    private void queryMuduleInfo() {
        no_permission.setVisibility(View.VISIBLE);
        Map<String, String> params = netWorks.getPublicParams();
        params.put(Constant.PARAMS_NEME_ACCOUNT_ID,Constant.ACCOUNT_ID);
        params.put("advid",Constant.ADV_ID);
        netWorks.Request(UrlConstance.CHECK_RECHARGE,true,"正在查询模块状态...",params,5000,0 ,new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Utils.dismissWaittingDialog();
                baseView.setVisibility(View.VISIBLE);
                netError.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("查询储值功能状态："+response.toString());
                Utils.dismissWaittingDialog();
                netError.setVisibility(View.GONE);
                baseView.setVisibility(View.VISIBLE);
                parseRechargeJson(response);
            }
        });
    }

    /**
     * 检索是否开通了储值模块
     * @param json
     */
    private void parseRechargeJson(String json) {
        try {
            JSONObject object=new JSONObject(json);
            int code = object.getInt("code");
            if(code==200) {
                int status = object.getJSONObject("msg").getInt("status");
                if(status==0) {//没有开通智慧门店
                    tv_nopermission_notice.setText("账号尚未开通此模块");
                }else if(status==1){//没有开通储值功能
                    tv_nopermission_notice.setText("您尚未开通储值功能，开通储值功能后可为会员储值。");
                }else if(status==2) {//开通了储值功能
                    no_permission.setVisibility(View.GONE);
                    inditifyMember.setVisibility(View.VISIBLE);
                    memchargeChargeinfo.setVisibility(View.GONE);
                    payModule.setVisibility(View.GONE);
                    initBarScanner();
                    LinearLayoutManager llManager=new LinearLayoutManager(mActivity);
                    llManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    adapter = new RecyclerAdapter(mActivity);
                    chargeList.setHasFixedSize(true);
                    chargeList.setLayoutManager(llManager);
                    setListener();
                    initState();
                    PrintUtils.getInstance().initService(mActivity, connService);
                }
            }else {
                tv_nopermission_notice.setText("参数有误!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        LogUtils.e(TAG,"onstop()");
        if(serviceBind) {
            mActivity.unbindService(connService);
            serviceBind=false;
        }
        super.onStop();
    }

    private void initBarScanner() {
        savedScannerDevice = (PrintDevices) SaveUtils.getObject(mActivity, Constant.SAVED_BLUETOOTH_SCANNER);
        if(savedScannerDevice==null) {
            if(Constant.XS_IN) {
                scannerConnected();
            }else {
                scannerError.setVisibility(View.VISIBLE);
                scannerErrorMsg.setText(R.string.need_add_scanner);
                pay_scaner_off.setText(R.string.need_add_scanner);
                pay_scanner_normal.setVisibility(View.GONE);
            }
        }else {
            boolean connected = HidConncetUtil.getScannerConnectState(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(savedScannerDevice.getBlueToothAdd()));
            if(connected) {
                scannerConnected();
            }else {
                scannerDisconnected();
            }
            listenScannerState();
            HidConncetUtil.connectScanner(mActivity, savedScannerDevice.getBlueToothAdd());
        }

    }

    /**
     * 扫码枪连接正常
     */
    private void scannerConnected() {
        pay_scanner_normal.setVisibility(View.VISIBLE);
        pay_scaner_off.setVisibility(View.GONE);
        scannerError.setVisibility(View.GONE);
    }

    /**
     * 扫码枪连接异常
     */
    private void scannerDisconnected() {
        if(Constant.XS_IN) {
            scannerConnected();
        }else {
            pay_scanner_normal.setVisibility(View.GONE);
            pay_scaner_off.setVisibility(View.VISIBLE);
            pay_scaner_off.setText(R.string.scanner_disconnect);
            scannerError.setVisibility(View.VISIBLE);
            scannerErrorMsg.setText(R.string.scanner_disconnect);
        }

    }
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if(action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
                int conectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(conectState==0) {
                    scannerDisconnected();
                }else if(conectState==1) {
                    scannerDisconnected();
                }else if(conectState==2) {
                    scannerConnected();
                }
            }else if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            }
        }
    };
    private void listenScannerState() {
        HidConncetUtil.registBarScanReceiver(mActivity, receiver);
    }

    @OnClick({R.id.btn_spot, R.id.btn_del, R.id.btn_clear,R.id.btn_ten,R.id.btn_twenty,R.id.btn_fifty,R.id.btn_hundred,
            R.id.tv_request_memberinfo,R.id.cash_pay_sure,R.id.card_pay_sure,R.id.find_card,R.id.find_voucher,R.id.net_error_refresh})
    void OnClick(View v){
        switch (v.getId()) {
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
                tvCashpayReturn.setText(Utils.dropZero(Utils.keepTwoDecimal(d_shi_shou - chargeMoney + "")));
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
                tvCashpayReturn.setText(10 - chargeMoney + "");
                break;
            case R.id.btn_twenty:
                tvCashpayShishou.setText("20.00");
                tvCashpayReturn.setText(20 - chargeMoney + "");
                break;
            case R.id.btn_fifty:
                tvCashpayShishou.setText("50.00");
                tvCashpayReturn.setText(50 - chargeMoney + "");
                break;
            case R.id.btn_hundred:
                tvCashpayShishou.setText("100.00");
                tvCashpayReturn.setText(100 - chargeMoney + "");
                break;
            case R.id.tv_request_memberinfo:
                String memberStr = etEnterNum.getText().toString().trim();
                Utils.hideKeyboard(baseView);
                if(!TextUtils.isEmpty(memberStr)) {
                    getMemberInfo(memberStr);
                }else {
                    Utils.showToast(mActivity,"未检测到输入，请输入会员卡号或手机号！");
                }
                break;
            case R.id.cash_pay_sure:
                Constant.payway="3";
                cashCardCharge();
                break;
            case R.id.card_pay_sure:
                Constant.payway="6";
                cashCardCharge();
                break;
            case R.id.find_card:
                FpUtils.showMemberVocher(mActivity,FpUtils.MEMBER);
                break;
            case R.id.find_voucher:
                FpUtils.showMemberVocher(mActivity,FpUtils.VOUCHER);
                break;
            case R.id.net_error_refresh:
                queryMuduleInfo();
                break;
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
        tvCashpayReturn.setText(Utils.dropZero(Utils.keepTwoDecimal(d_shi_shou - chargeMoney + "")));
    }
    private void initState() {
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
            tvCashpayShishou.setText("00.00");
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
            tvCashpayReturn.setText(Utils.dropZero(Utils.keepTwoDecimal(d_shi_shou - chargeMoney + "")));
        }
    }

    private void setListener() {
        rg_payways.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                payWayCheck(checkedId);
            }
        });
        adapter.setItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int size = rulesBeanList.size();
                chargeMoney = Double.valueOf(rulesBeanList.get(position).getMoney());
                handleFp();
                songMoney= Double.valueOf(rulesBeanList.get(position).getSong());
                for (int i = 0; i < size; i++) {
                    if(i==position) {
                        rulesBeanList.get(i).setChecked(true);
                    }else {
                        rulesBeanList.get(i).setChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * 支付
     */
    private void toPay(String barcode) {
        Constant.UNKNOWN_COMMODITY="2";
        tradeNum = Utils.getTradeNum();
        trade_time = Utils.getTradeTime();
        PayUtils.toPay(mActivity, baseView, tradeNum,chargeMoney + "", chargeMoney + "", barcode, null, vipcard_id, null, null,
                null, null, null, 1,new OnPayFinish() {
                    @Override
                    public void onPaySuccess(String response, String transtion_id, String payment) {
                        paySccess(response, transtion_id, payment);

                    }
                    @Override
                    public void onPayFailure(String response,String reaseon,String solution) {
                        payFail(reaseon,solution);
                    }
                });

    }

    /**
     * 现金和刷卡充值（现金和刷卡无论是否成功，均需要改变当前余额）
     */
    private void cashCardCharge() {
        Constant.UNKNOWN_COMMODITY="2";
        //秒
        trade_time = Utils.getTradeTime();
        tradeNum= Utils.getTradeNum();//"order_no"
        SavedFailOrder cashOrder=PayTranstions.getSaveOrder("0",tradeNum,"0",chargeMoney+"","0", trade_time,
                "1","0",chargeMoney+"",
                vipcard_id,chargeMoney+"",songMoney+"","0","0","");
        CashCardPayUtils.getInstance().cashCardPay(mActivity, cashOrder, null, null, new OnPayFinish() {
            @Override
            public void onPaySuccess(String response,String transtionid,String payment) {
                paySccess(response, transtionid, chargeMoney+"");
            }

            @Override
            public void onPayFailure(String errorMsg,String reason,String solution) {
                payFail(reason,solution);
            }
        });
    }

    private static final String TAG = "MemberChargePager";
    private void paySccess(String response, String transtion_id, String payment) {
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

        if(response!=null) {
            try {
                currentLeft = new JSONObject(response).getDouble("freemoney");
                member_balance.setText(Utils.dropZero(currentLeft +""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String nowTime = df3.format(System.currentTimeMillis());
        initPrintData(transtion_id);
        NoticePopuUtils.showPayRsesultPop(mActivity,baseView,payment,way,tradeNum,nowTime,"暂无",null);
    }

    private void initPrintData(String transtion_id) {
        headerData = new ArrayList<>();
        memberData = new ArrayList<>();
        btHeaderData=new ArrayList<>();
        btMemberData=new ArrayList<>();

        PrintUtils.getInstance().initMCPrintData(tradeNum,trade_time,chargeMoney,way, memberCardNo,beforeLeft,currentLeft,headerData, memberData,
                transtion_id,false);
        PrintUtils.getInstance().initMCPrintData(tradeNum,trade_time,chargeMoney,way, memberCardNo,beforeLeft,currentLeft,btHeaderData, btMemberData,
                transtion_id,true);

        PrintUtils.getInstance().startLoclPrint(mActivity, iWoyouService, localPrinterPermission, headerData, memberData, new PrintSecond() {
            @Override
            public void doPrintSecond() {
                PrintUtils.getInstance().printSecond(iWoyouService,headerData);
            }
        });

        if(btPrinter!=null) {
            if(btPrinterPermission.contains("4")||btPrinterPermission.contains("5")) {
                if(mService.getState()== BluetoothService.STATE_CONNECTED) {
                    printPayedByBluetooth();
                }else {
                    BTPrintUtils.getInstance().connectBtPrinterTest(mService,mActivity,handler);
                }
            }
        }

        //云打印数据格式化
        YunPrintUtils.formateDirPay(mActivity, headerData, memberData);

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
            btHeaderData.remove(1);
        }
        BTPrintUtils.getInstance().csDetailBtPrint(mService, btHeaderData, forCustomer, btMemberData,
                mActivity, callBack);
    }




    private void getMemberInfo(final String barcode) {
        ((MainActivity)mActivity).startFpLb();
        Map<String, String> params = netWorks.getPublicParams();
        params.put("account_id", Constant.ACCOUNT_ID);
        params.put("adv_id", Constant.ADV_ID);
        params.put("storeid", Constant.STORE_ID);
        params.put("cardid", barcode);//455589890007
        params.put("flag", "1");
        params.put("order_id", "0");
        params.put("total_fee", "0");
        params.put("commit", "0");
        params.put("type", "0");
        netWorks.Request(UrlConstance.QUERY_MEMBERINFO, true,"会员检索中...",params, 5000, 0, new NetWorks.OnNetCallBack() {
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

    private void praseMemberInfoJson(String response, String barcode) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            int code = jsonObject.getInt("code");

            if(code==200) {
                Gson gson=new Gson();
                memberChargeInfoBean = gson.fromJson(response, MemberChargeInfoBean.class);
                if(memberChargeInfoBean!=null) {
                    vipcard_id = memberChargeInfoBean.getMsg().getId();
                    rulesBeanList = memberChargeInfoBean.getMsg().getRules();
                    showData();
                }
            }else {
                String msg = jsonObject.getString("msg");
                Utils.showToast(mActivity,msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showData() {
        inditifyMember.setVisibility(View.GONE);
        payModule.setVisibility(View.VISIBLE);
        memchargeChargeinfo.setVisibility(View.VISIBLE);

        member_name.setText(memberChargeInfoBean.getMsg().getName());
        memberCardNo = memberChargeInfoBean.getMsg().getMember_cardno();
        this.member_cardno.setText(memberCardNo);
        beforeLeft = memberChargeInfoBean.getMsg().getMoney();
        member_balance.setText(Utils.dropZero(beforeLeft +""));
        if(rulesBeanList!=null&&rulesBeanList.size()>0) {
            rulesBeanList.get(0).setChecked(true);
            chargeMoney=Double.valueOf(rulesBeanList.get(0).getMoney());
            songMoney=Double.valueOf(rulesBeanList.get(0).getSong());
            adapter.setData(rulesBeanList, RecyclerAdapter.MEMBER_CHARGE_ITEM);
            handleFp();
        }

        chargeList.setAdapter(adapter);
        rg_payways.check(R.id.wx_pay);
    }
    private void handleFp() {
        ((MainActivity)mActivity).stopFpLb();
        FpUtils.showTp(((MainActivity)mActivity).dsKernel,chargeMoney+"");
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
                way = "现金记账";
                initPayWayState(1);
                break;
            case R.id.card_pay:
                rl_wx_zhf.setVisibility(View.GONE);
                ll_pay_card.setVisibility(View.VISIBLE);
                ll_pay_cash.setVisibility(View.GONE);
                ll_pay_member.setVisibility(View.GONE);
                finsh_card_pay.setVisibility(View.GONE);
                card_pay_notice.setText(R.string.card_charge_notice);
                cardPaySure.setVisibility(View.GONE);
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
            if(TextUtils.equals(Constant.payway,"3")) {
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

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        try {
            if(receiver!=null) {
                mActivity.unregisterReceiver(receiver);
            }
            if(serviceBind) {
                mActivity.unbindService(connService);
                serviceBind=false;
            }
        }catch (Exception e){

        }

        super.onDestroy();
    }
}
