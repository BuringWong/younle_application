package zxing.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.membercenter.ChargeActivity;
import com.younle.younle624.myapplication.activity.manager.orderpager.AboutH5Activity;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.HowToUseZfbWxActivity;
import com.younle.younle624.myapplication.activity.manager.orderpager.pos.PosOrderActivity;
import com.younle.younle624.myapplication.activity.pos.CollectionFailActivity;
import com.younle.younle624.myapplication.activity.pos.CollectionSuccessActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.ResultQueryBean;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.domain.VoucherScanResult;
import com.younle.younle624.myapplication.domain.createorder.CreateOrderMemberBean;
import com.younle.younle624.myapplication.domain.orderbean.DetailMemberBean;
import com.younle.younle624.myapplication.domain.orderbean.DetailPosCashVoucherBean;
import com.younle.younle624.myapplication.domain.orderbean.DetailPosDiscountVoucherBean;
import com.younle.younle624.myapplication.domain.orderbean.PosCashVoucherBean;
import com.younle.younle624.myapplication.domain.orderbean.PosDiscountVoucherBean;
import com.younle.younle624.myapplication.domain.orderbean.PosMemberBean;
import com.younle.younle624.myapplication.domain.paybean.DiscountInfo;
import com.younle.younle624.myapplication.domain.paybean.PayParams;
import com.younle.younle624.myapplication.domain.printsetting.PrintDevices;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.scanbar.HidConncetUtil;
import com.younle.younle624.myapplication.utils.scanbar.ScanGunKeyEventHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.io.IOException;
import java.util.Vector;

import okhttp3.Call;
import zxing.camera.CameraManager;
import zxing.decoding.CaptureActivityHandler;
import zxing.decoding.DecodeHandlerInterface;
import zxing.decoding.InactivityTimer;
import zxing.view.ViewfinderView;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback, DecodeHandlerInterface, View.OnClickListener {

    private static final String TAG = "CaptureActivity";
    /**
     * 直接直接收款
     */
    private final int PAY_FLAG_DIR_COLL = 1;
    /**
     * 查询支付结果
     */
    private final int PAY_FLAG_QERY_PAY_STATE = 2;
    private CaptureActivityHandler capturehandler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.80f;
    private boolean vibrate;
    private TextView mTvCardCode;
    private TextView tv_gathering_account;
    private TextView tv_enter_explain;
    /**
     * 实付金额
     */
    private String payment;
    /**
     * 充值金额
     */
    private String fact_money="0";
    /**
     * 订单金额
     */
    private String total_fee = "0";
    private String commit = "0";
    private String type_for_mc = "0";
    private String last_order_no;
    private static final long VIBRATE_DURATION = 200L;
    private int fromeWhere;
    private TextView tv_scan_desc;
    private ImageView iv_back;
    private RelativeLayout ll_indentify;
    private FrameLayout fl_layout_scanning;
    private LinearLayout all_pay_money;
    private LinearLayout rl_enter_phone_num;//输入手机号码布局
    private LinearLayout ll_blank_layout;//空白占位布局
    private LinearLayout all_scan_code_explain;//
    private EditText et_enter_phone_num;
    private TextView tv_click_comfirm;
    private FrameLayout fl_surface_view;//扫码以及扫码说明布局
    private ImageView iv_confirm_loading;
    private ImageView iv_pb;
    private static final int FAIL = 3;
    private static final int CUT_DOWN_TIMMER = 5;
    private static final int QUERY_ORDER_STATE = 6;
    private static final int MAIN_SET_CAMERA_VISIBLE = 7;
    private static final int MAIN_SET_CAMERA_GONE = 8;
    private PopupWindow waittingPop;
    private int recLen = Constant.RECLEN_TIME;
    private TextView tv_cout_timmer;
    private boolean isClickCancel;
    private boolean isTimeGone;
    private String auth_code = "";  //传到服务器请求数据的订单号
    private String trade_num = "";  //订单号
    private String trade_time = Utils.getTradeTime(); //交易时间
    private String vip_card = "0"; //vip卡号
    private String cardId = ""; //扫码得到的卡号
    private String vipcreate_id = "0"; //扫码得到的卡号
    //private int order_id; //交易编号 从订单页面传来的是primarykeyid
    private String query_num = "";
    private String transaction_id = "";
    private String failreason = "收款失败";
    private String solveway = "请重新发起收款";
    private final int NO_PAY_RESULT = 0;
    private final int MAY_PAY_SUCCESS = 1;
    private ScanGunKeyEventHelper scanGunKeyEventHelper;
    private String cardinfo_pay ="";
    private boolean isManulEnter = false;
    /**
     * 外接扫码枪
     */
//    private LinearLayout part1_connecting;
//    private LinearLayout part2_normal;
//    private LinearLayout part3_disconnected;
//    private TextView tv_how_to_findcode;
//    private LinearLayout ll_cancel;
//    private TextView camer_tv_title;

    private TextView tv_title;
//    private TextView tv_use_pos;
//    private TextView should_pay_money;
//    private RelativeLayout rl_scan_layout;

    private RelativeLayout rl_barscanner;
    private ImageView iv_scanner_state_icon;
    private TextView tv_barscanner_state;
    private boolean keyBoardShow = false;
    /**
     * 赠送的钱
     */
    String give_money = "0";

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QUERY_ORDER_STATE:
                    queryOrderState();
                    break;
                case FAIL:
                    toFailActivity();
                    break;
                case CUT_DOWN_TIMMER:
                    cutDownTimer();
                    break;
                case MAIN_SET_CAMERA_VISIBLE:
                    cameraVisi();
                    break;
                case MAIN_SET_CAMERA_GONE:
                    cameraGone();
                    break;
            }
        }
    };

    private DiscountInfo discountInfo;
    private NetWorks netWorks;
    private NetWorks.OnNetCallBack callBack = new NetWorks.OnNetCallBack() {
        @Override
        public void onError(Exception e, int flag) {
            switch (flag) {
                case PAY_FLAG_DIR_COLL:
                    dealpayError();
                    break;
                case PAY_FLAG_QERY_PAY_STATE:
                    dealQueryError();
                    break;
            }
        }

        @Override
        public void onResonse(String response, int flag) {
            switch (flag) {
                case PAY_FLAG_DIR_COLL:
                    dealpayRes(response);
                    break;
                case PAY_FLAG_QERY_PAY_STATE:
                    dealQueryRes(response);
                    break;
            }
        }
    };
    /**
     * 用于绑定会员账号和订单
     */
    private String unpay_order_id ="0";
    private String order_no_for_order ="0";
    private String vip_discount ="0";
    private UnPayDetailsBean orderBean;
    private PosMemberBean memberBean;
    private PrintDevices savedDevice;
    /**
     * 是否已经扫描到条码
     */
    private boolean alreadySacan=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedDevice = (PrintDevices) SaveUtils.getObject(CaptureActivity.this, Constant.SAVED_BLUETOOTH_SCANNER);
        registBarScanReceiver();
        //setContentView(R.layout.camera_add_phone);
        setContentView(R.layout.camera_add_phone2);
        CameraManager.init(getApplication());
        initView();
        getDataFromIntent();
        initData();
        initSoft();
        setListener();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        netWorks = new NetWorks(this);
        if(savedDevice!=null) {
            rl_barscanner.setVisibility(View.VISIBLE);
            initBarScanner();
        }else {
            rl_barscanner.setVisibility(View.GONE);
        }
    }

    private void initBarScanner() {
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            BluetoothAdapter.getDefaultAdapter().enable();
        }
        scanGunKeyEventHelper=new ScanGunKeyEventHelper(new ScanGunKeyEventHelper.OnScanSuccessListener() {
            @Override
            public void onScanSuccess(String barcode) {
                LogUtils.Log("barCode=="+barcode);
                if(barcode!=null&& !TextUtils.isEmpty(barcode)) {
                    if(waittingPop!=null&&waittingPop.isShowing()) {
                        return;
                    }
                    auth_code=barcode;
                    if(!alreadySacan) {
                        alreadySacan=true;
                        handleScanCode();
                    }
                }
            }
        });
        HidConncetUtil.initState(this,tv_barscanner_state,iv_scanner_state_icon,"已连接扫码枪,您可以扫描顾客的付款码收款");
    }

    private void initView() {
        rl_barscanner = (RelativeLayout)findViewById(R.id.rl_barscanner);
        iv_scanner_state_icon = (ImageView)findViewById(R.id.iv_scanner_state_icon);
        tv_barscanner_state = (TextView)findViewById(R.id.tv_barscanner_state);
        tv_title = (TextView) findViewById(R.id.tv_title);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        mTvCardCode = (TextView) findViewById(R.id.tv_card_code);
        tv_scan_desc = (TextView) findViewById(R.id.tv_scan_desc);
        tv_gathering_account = (TextView) findViewById(R.id.tv_gathering_account);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //取景框中的识别中
        ll_indentify = (RelativeLayout) findViewById(R.id.ll_indentify);
        iv_pb = (ImageView) findViewById(R.id.iv_pb);

        fl_layout_scanning = (FrameLayout) findViewById(R.id.fl_layout_scanning);

        all_pay_money = (LinearLayout) findViewById(R.id.all_pay_money);
        rl_enter_phone_num = (LinearLayout) findViewById(R.id.rl_enter_phone_num);
        ll_blank_layout = (LinearLayout) findViewById(R.id.ll_blank_layout);
        all_scan_code_explain = (LinearLayout) findViewById(R.id.all_scan_code_explain);
        et_enter_phone_num = (EditText) findViewById(R.id.et_enter_phone_num);

        tv_click_comfirm = (TextView) findViewById(R.id.tv_click_comfirm);
        fl_surface_view = (FrameLayout) findViewById(R.id.fl_surface_view);
        iv_confirm_loading = (ImageView) findViewById(R.id.iv_confirm_loading);
        tv_enter_explain = (TextView) findViewById(R.id.tv_enter_explain);
    }

    private void setListener() {
        mTvCardCode.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_click_comfirm.setOnClickListener(this);
        et_enter_phone_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideSoftKeyboard();
                }
            }
        });
    }

    int currentKeyboardHeight = 0;
    private void initSoft() {
        //软键盘最小高度
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        //decor view
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;
            @Override
            public void onGlobalLayout() {
                //获取decorview的可见范围
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                // 通过decorview高度变化判断是否显示来软键盘
                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        // 计算当前软键盘高度(这个高度包含来全屏时navigation bar的高度).
                        currentKeyboardHeight = decorView.getHeight() - windowVisibleDisplayFrame.bottom;
                        //listener.onKeyboardShown(currentKeyboardHeight);
                        LogUtils.e(TAG,"ttt计算当前软键盘高度currentKeyboardHeight="+currentKeyboardHeight);
                        handler.sendEmptyMessage(MAIN_SET_CAMERA_GONE);
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        //listener.onKeyboardHidden();
                        LogUtils.e(TAG,"ttt隐藏软键盘");
                        handler.sendEmptyMessage(MAIN_SET_CAMERA_VISIBLE);
                    }
                }
                //保存decorview高度
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }

    private void getDataFromIntent() {

        String payment_extra = getIntent().getStringExtra("payment");

        if (payment_extra != null) {
            payment = Utils.keepTwoDecimal(payment_extra);
        }
        String total_fee_extra = getIntent().getStringExtra("total_fee");
        if (total_fee_extra != null) {
            total_fee = Utils.keepTwoDecimal(total_fee_extra);
        }
        String vip_card_intent_extra = getIntent().getStringExtra("vip_card");
        if (vip_card_intent_extra != null) {
            vip_card = vip_card_intent_extra;
            LogUtils.e(TAG,"getIntent vip_card="+vip_card);
        }
        if(getIntent().getStringExtra("cardinfo_pay")!=null){
            cardinfo_pay = getIntent().getStringExtra("cardinfo_pay");
        }
        if (getIntent().getStringExtra("vipcreate_id") != null) {
            vipcreate_id = getIntent().getStringExtra("vipcreate_id");
            LogUtils.e(TAG,"getIntent vip_card="+vip_card);
        }
        orderBean = (UnPayDetailsBean) getIntent().getSerializableExtra(Constant.ORDER_BEAN);
        memberBean = (PosMemberBean) getIntent().getSerializableExtra("member_bean");
        fromeWhere = getIntent().getIntExtra(Constant.FROME_WHERE, -1);
        if(getIntent().getStringExtra(Constant.UNPAY_ORDER_ID)!=null){
            unpay_order_id = getIntent().getStringExtra(Constant.UNPAY_ORDER_ID);
        }
        if(getIntent().getStringExtra("order_no_for_order")!=null){
            order_no_for_order = getIntent().getStringExtra("order_no_for_order");
        }
        if(getIntent().getStringExtra("vip_discount")!=null){
            vip_discount = getIntent().getStringExtra("vip_discount");
        }
        if(getIntent().getStringExtra("commit")!=null){
            commit = getIntent().getStringExtra("commit");
        }
        if(getIntent().getStringExtra("type_for_mc")!=null){
            type_for_mc = getIntent().getStringExtra("type_for_mc");
        }
        //2017.0410为显示房间开始结束时间一致加
        if(orderBean!=null){
            if(orderBean.getMsg()!=null){
                if(orderBean.getMsg().getRoom()!=null){
                   if(orderBean.getMsg().getRoom().getEnd_time()!=null&&!"".equals(orderBean.getMsg().getRoom().getEnd_time())){
                       trade_time = orderBean.getMsg().getRoom().getEnd_time();
                   }
                }
            }
        }

        LogUtils.e(TAG, "unpay_order_id=-=-=" + unpay_order_id);
    }

    private void initData() {
        tv_title.setText("扫描二维码");
        if (fromeWhere == Constant.MEMBER_CUSTOMER_CONFIRM) {//会员识别
            tv_scan_desc.setText("将会员的卡号二维码放入取景框内");
            tv_scan_desc.setTextColor(Color.WHITE);
            all_pay_money.setVisibility(View.GONE);
            rl_enter_phone_num.setVisibility(View.VISIBLE);
            mTvCardCode.setText("会员如何找到卡号二维码");
            tv_enter_explain.setText("会员卡：请输入手机号或卡号");
        } else if (fromeWhere == Constant.MEMBER_CARDS_VOUCHER) {//下单收银会员卡支付
            //tv_scan_desc.setText("将会员的卡号二维码放入取景框内");
            tv_scan_desc.setText("支持扫描会员卡、代金券、折扣券的二维码");
            tv_scan_desc.setTextColor(Color.WHITE);
            all_pay_money.setVisibility(View.GONE);
            rl_enter_phone_num.setVisibility(View.VISIBLE);
            mTvCardCode.setText("会员如何找到会员卡/券二维码?");
            rl_enter_phone_num.setVisibility(View.VISIBLE);
        } else if (fromeWhere == Constant.MEMBER_DIRECT_PAY) {//直接收银会员卡支付
            tv_scan_desc.setText("支持扫描会员卡、代金券、折扣券的二维码");
            tv_scan_desc.setTextColor(Color.WHITE);
            all_pay_money.setVisibility(View.GONE);
            rl_enter_phone_num.setVisibility(View.VISIBLE);
            mTvCardCode.setText("顾客如何找到会员卡/券的二维码？");
        }else if (fromeWhere == Constant.VOUCHER_PAGER) {//二维码核销
            all_pay_money.setVisibility(View.GONE);
            tv_scan_desc.setText("请将二维码置入上方取景框");
            tv_scan_desc.setTextColor(Color.WHITE);
            mTvCardCode.setText("关于H5电商系统");
            mTvCardCode.setTextColor(Color.rgb(63, 136, 206));
            all_pay_money.setVisibility(View.GONE);
            rl_enter_phone_num.setVisibility(View.GONE);
        } else if (fromeWhere == Constant.MEMBER_CHARGE) {//会员充值,识别会员
            tv_scan_desc.setText("将会员卡的二维码置入取景框内");
            tv_scan_desc.setTextColor(Color.WHITE);
            tv_scan_desc.setTextSize(16);
            all_pay_money.setVisibility(View.GONE);
            rl_enter_phone_num.setVisibility(View.VISIBLE);
            et_enter_phone_num.setHint("手动输入手机号或会员卡号");
            mTvCardCode.setText("顾客怎样找到会员卡的二维码？");
            mTvCardCode.setTextSize(16);
            tv_enter_explain.setVisibility(View.GONE);
        } else if (fromeWhere == Constant.MEMBER_CHARGE_PAY) {//会员充值，支付
            payView();
            all_pay_money.setVisibility(View.VISIBLE);
            rl_enter_phone_num.setVisibility(View.GONE);
        } else if (fromeWhere == Constant.ORDER_PAGER_DETAIL) {//下单结账
            LogUtils.e(TAG, "fromeWhere=订单结账扫码");
            payView();
            all_pay_money.setVisibility(View.VISIBLE);
            rl_enter_phone_num.setVisibility(View.GONE);
        } else if(fromeWhere==Constant.MEME_PAY_DIC) {//会员扫码收款
            tv_scan_desc.setText("将会员的卡号二维码放入取景框内");
            payView();
            all_pay_money.setVisibility(View.VISIBLE);
            rl_enter_phone_num.setVisibility(View.GONE);
        }else {
            LogUtils.e(TAG, "fromeWhere=直接收款扫码");
            payView();
            all_pay_money.setVisibility(View.VISIBLE);
            rl_enter_phone_num.setVisibility(View.GONE);
        }
    }

    private void payView() {
        tv_gathering_account.setText(CaptureActivity.this.getResources().getString(R.string.rmb_symbol) + payment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel:
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_card_code:
                //点击弹出提示怎么找到刷卡码
                if (fromeWhere != Constant.VOUCHER_PAGER) {
                    Intent intent = new Intent(CaptureActivity.this, HowToUseZfbWxActivity.class);
                    intent.putExtra("payway", Constant.payway);
                    intent.putExtra(Constant.FROME_WHERE, fromeWhere);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(CaptureActivity.this, AboutH5Activity.class));
                }
                break;
            case R.id.tv_click_comfirm:
                //点击确认查询券或者会员信息
                String enter_info = et_enter_phone_num.getText().toString();
                if("".equals(enter_info)){
                    if(keyBoardShow){//判断软键盘是否弹出：
                        Utils.showToastTop(this,"不能为空",2000);
                    }else{
                        Utils.showToast(this,"不能为空",2000);
                    }
                }else{
                    showAnima();
                    isManulEnter = true;
                    Constant.MEMBER_ID = enter_info;
                    if(fromeWhere==Constant.MEMBER_CHARGE){
                        auth_code = enter_info;
                        getMemberInfo("1");
                    }else{
                        getMemberInfo("0");
                    }
                }
                break;
        }
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        LogUtils.e(TAG, "Result result==" + result.toString());
        if(waittingPop!=null&&waittingPop.isShowing()) {
            return;
        }
        playBeepSoundAndVibrate();
        auth_code = result.getText();
        if(!alreadySacan) {
            alreadySacan=true;
            handleScanCode();
        }
    }

    private void handleScanCode() {
        LogUtils.e(TAG, "handleScanCode()");
        if (auth_code.equals("")) {
            Toast.makeText(CaptureActivity.this, CaptureActivity.this.getResources().getString(R.string.scan_failed), Toast.LENGTH_SHORT).show();
        } else {
            //1.下单，会员识别扫码
            if (fromeWhere == Constant.MEMBER_CUSTOMER_CONFIRM) {
                ll_indentify.setVisibility(View.VISIBLE);
                Constant.MEMBER_ID = auth_code;
                Utils.pbAnimation(this,iv_pb);
                stopScan();
                getMemberInfo("0");
                //下单会员、券的识别
            } else if (fromeWhere == Constant.MEMBER_CARDS_VOUCHER) {
                ll_indentify.setVisibility(View.VISIBLE);
                Constant.MEMBER_ID = auth_code;
                Utils.pbAnimation(this,iv_pb);
                stopScan();
                getMemberInfo("0");
                //会员充值
            } else if (fromeWhere == Constant.MEMBER_CHARGE) {
                ll_indentify.setVisibility(View.VISIBLE);
                Constant.MEMBER_ID = auth_code;
                Utils.pbAnimation(this, iv_pb);
                stopScan();
                getMemberInfo("1");
                //直接收银会员支付：识别会员信息
            }  else if (fromeWhere == Constant.MEMBER_DIRECT_PAY) {
                ll_indentify.setVisibility(View.VISIBLE);
                Constant.MEMBER_ID = auth_code;
                Utils.pbAnimation(this, iv_pb);
                stopScan();
                unpay_order_id = "0";
                getMemberInfo("0");
            } else if (fromeWhere == Constant.VOUCHER_PAGER) {
                //3.核销扫码
                VoucherScanResult vresult = new VoucherScanResult(auth_code, "扫描成功");
                EventBus.getDefault().post(vresult);
                finish();
                //4.会员充值，支付
            } else if (fromeWhere == Constant.MEMBER_CHARGE_PAY) {
                LogUtils.e(TAG, "会员充值支付扫码");
                give_money = "" + getIntent().getStringExtra("give_money");//赠送的钱
                toPay();
                //5.订单支付扫码
            } else if (fromeWhere == Constant.ORDER_PAGER_DETAIL) {
                LogUtils.e(TAG, "订单支付扫码");
                toPay();
               //6.会员卡余额直接扫码收款
            } else if(fromeWhere==Constant.MEME_PAY_DIC) {
                cardId = auth_code;
                toPay();
            } else {
                //7.直接收款扫码
                LogUtils.e(TAG, "直接收款扫码");
                //发起支付请求后的业务逻辑 支付宝需要：auth_code paytype  payment deviceid adv_id storeid order_id(会员时候传) type(0下单 1收银 2会员卡充值)
                toPay();
            }
        }
    }

    private void toPay() {
        showWaittingPopWindow();
        netWorks.PayCard(getPayParams(), callBack, PAY_FLAG_DIR_COLL);
    }

    private PayParams getPayParams() {
        PayParams payParams = new PayParams();
        trade_num = Utils.getTradeNum();
        payParams.setType(Constant.UNKNOWN_COMMODITY);
        payParams.setPayWay(Constant.payway);
        payParams.setAuthCode(auth_code);
        payParams.setOrderNo(trade_num);
        payParams.setOrderId(String.valueOf(unpay_order_id));
        payParams.setPayMent(payment);
        payParams.setTotal_fee(total_fee);
        payParams.setVipId(vip_card);
        payParams.setCardId(cardId);
        payParams.setVipcreate_id(vipcreate_id);
        payParams.setLast_order_no(Constant.LAST_ORDER_NO);
        payParams.setVip_discount(vip_discount);
        payParams.setCardinfo(cardinfo_pay);
        if(orderBean!=null){
            if(orderBean.getMsg()!=null){
                if(orderBean.getMsg().getRoom()!=null){
                    payParams.setRoom(new Gson().toJson(orderBean.getMsg().getRoom()));
                }
            }
        }
        return payParams;
    }

    private void parsePayResultJson(JSONObject payResponse) throws JSONException {
        discountInfo=null;
        if (!Constant.UNKNOWN_COMMODITY.equals("2")) {
            query_num = payResponse.getString("query_num");
            transaction_id = payResponse.getString("transaction_id");
            String ticketInfo = payResponse.getString("ticketInfo");
            if (!TextUtils.isEmpty(ticketInfo)) {
                Gson gson = new Gson();
                discountInfo = gson.fromJson(ticketInfo, DiscountInfo.class);
            }
        }
    }

    /**
     * 设置充值微信支付宝收款结果
     */
    private void setChargeResult(boolean result) {
        if (fromeWhere == Constant.MEMBER_CHARGE_PAY) {
//            Constant.CHARGE_SUCCESS = result;
        }
    }

    private void dismissWaittingPopWindow() {
        if (!isFinishing()&&waittingPop != null) {
            handler.removeMessages(CUT_DOWN_TIMMER);
            waittingPop.dismiss();
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);
        }
    }

    /**
     * 显示等待的PopWindow
     */
    private void showWaittingPopWindow() {

        recLen = Constant.RECLEN_TIME;
        //增加PopWindow
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_waitting_layout, null);
        tv_cout_timmer = (TextView) view.findViewById(R.id.tv_cout_timmer);
        LinearLayout ll_return_pre = (LinearLayout) view.findViewById(R.id.ll_return_pre);

        waittingPop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        waittingPop.setOutsideTouchable(false);
        waittingPop.setFocusable(true);
        waittingPop.showAtLocation(findViewById(R.id.root_camera_layout), Gravity.CENTER_HORIZONTAL, 0, Utils.dip2px(this, -100));//设置为取景框中间
        handler.sendEmptyMessage(CUT_DOWN_TIMMER);

        ll_return_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClickCancel = true;
                setChargeResult(false);
                dismissWaittingPopWindow();
                showSeeResultPopWindow(MAY_PAY_SUCCESS);
            }
        });
    }

    private void showSeeResultPopWindow(int tag) {

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.unknow_pay_result, null);
        TextView tv_return = (TextView) view.findViewById(R.id.tv_return);
        TextView tv_see_orders = (TextView) view.findViewById(R.id.tv_see_orders);
        TextView tv_title_pay_result = (TextView) view.findViewById(R.id.tv_title_pay_result);
        switch (tag){
            case NO_PAY_RESULT:
                tv_title_pay_result.setText(CaptureActivity.this.getResources().getString(R.string.no_pay_result));
                break;
            case MAY_PAY_SUCCESS:
                tv_title_pay_result.setText(CaptureActivity.this.getResources().getString(R.string.may_pay_success));
                break;
        }
        final PopupWindow resultPop = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        resultPop.setOutsideTouchable(false);
        resultPop.setFocusable(true);
        resultPop.showAtLocation(findViewById(R.id.root_camera_layout), Gravity.CENTER_HORIZONTAL, 0, Utils.dip2px(this, -100));//设置为取景框中间

        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resultPop!=null){
                    resultPop.dismiss();
                }
                Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
                finish();
            }
        });
        tv_see_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resultPop!=null){
                    resultPop.dismiss();
                }
                Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
                startActivity(new Intent(CaptureActivity.this, PosOrderActivity.class));
                finish();
            }
        });
    }

    private void stopScan() {
        CameraManager.get().stopPreview();
        viewfinderView.SPEEN_DISTANCE = 0;
    }

    /**
     * 联网获取会员信息
     * @param flag 0：代表来自下单会员识别。1：充值会员识别
     */
    private void getMemberInfo(final String flag) {
        String currentTime = Utils.getCurrentTime();
        final String token = Utils.getToken(currentTime,this);
        String url="0".equals(flag)?UrlConstance.NEW_QUERY_MEMBERINFO:UrlConstance.QUERY_MEMBERINFO;
        LogUtils.Log("会员识别url=="+url.toString());
        LogUtils.Log("account_id==" + Constant.ACCOUNT_ID);
        LogUtils.Log("adv_id==" + Constant.ADV_ID);
        LogUtils.Log("storeid==" + Constant.STORE_ID);
        LogUtils.Log("cardid==" + Constant.MEMBER_ID);
        LogUtils.Log("flag==" + flag);
        LogUtils.Log("order_id==" +unpay_order_id);
        LogUtils.Log("total_fee==" + total_fee);
        LogUtils.Log("commit==" + commit);
        LogUtils.Log("type==" + type_for_mc);

        OkHttpUtils.post()
                .url(url)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams(Constant.PARAMS_NEME_PASSDORD_MD5, MD5.md5(MD5.md5(Constant.PASSWORD)))
                .addParams("account_id", Constant.ACCOUNT_ID)
                .addParams("adv_id", Constant.ADV_ID)
                .addParams("storeid", Constant.STORE_ID)
                .addParams("cardid", Constant.MEMBER_ID)//455589890007
                .addParams("flag", flag)
                .addParams("order_id", unpay_order_id)
                .addParams("total_fee", total_fee)
                .addParams("commit", commit)
                .addParams("type", type_for_mc)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("会员查询返回error：" + e.toString());
                        Toast.makeText(CaptureActivity.this, "网络不佳，请检查网络后重试！", Toast.LENGTH_SHORT).show();
                        CaptureActivity.this.finish();
                        dismissAnima();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissAnima();
                        LogUtils.e(TAG, "会员查询返回response:" + response.toString());
                        //1.验证token
                        boolean toNext = Utils.checkSaveToken(CaptureActivity.this, response);
                        if (toNext) {
                            //2.处理数据,eventbus将response传递到对应的界面处理
                            //'3005'  => '暂无符合条件的数据',
                            praseChargeJson(response, flag);
                        }
                    }
                });
    }

    private void showAnima() {
        tv_click_comfirm.setVisibility(View.GONE);
        Utils.pbAnimationPro(this,iv_confirm_loading,true);
    }

    private void dismissAnima() {
        tv_click_comfirm.setVisibility(View.VISIBLE);
        Utils.pbAnimationStopPro(iv_confirm_loading,true);
    }

    /**
     * @param json
     * @param flag 0：代表来自下单会员识别。1：充值会员识别
     */
    private void praseChargeJson(String json, String flag) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            int code = jsonObj.getInt("code");
            if (code == 3005) {
                Toast.makeText(CaptureActivity.this, "未检索到该会员信息！", Toast.LENGTH_SHORT).show();
                finish();
            }else if(code == 200){
                Intent intent;
                if ("0".equals(flag)) {
                    Gson gson = new Gson();
                    if(fromeWhere == Constant.MEMBER_CUSTOMER_CONFIRM){//房间页面、添加商品页面会员识别
                        String card_type = jsonObj.getJSONObject("msg").getString("card_type");
                        if(card_type!=null){
                            if("MEMBER".equals(card_type)){
                                DetailMemberBean memberBean = gson.fromJson(json, DetailMemberBean.class);
                                memberBean.getMsg().setAuth_code(auth_code);
                                CreateOrderMemberBean createOrderMemberBean=new CreateOrderMemberBean(memberBean);
                                EventBus.getDefault().post(createOrderMemberBean);
                            }
                        }
                    }else if(fromeWhere == Constant.MEMBER_CARDS_VOUCHER){//下单会员券的识别
                        String card_type = jsonObj.getJSONObject("msg").getString("card_type");
                        if(card_type!=null){
                            if("MEMBER".equals(card_type)){
                                DetailMemberBean memberBean = gson.fromJson(json, DetailMemberBean.class);
                                memberBean.getMsg().setAuth_code(auth_code);
                                EventBus.getDefault().post(memberBean);
                            }else if("DISCOUNT".equals(card_type)){//折扣券
                                DetailPosDiscountVoucherBean detailPosDiscountVoucherBean = gson.fromJson(json, DetailPosDiscountVoucherBean.class);
                                detailPosDiscountVoucherBean.getMsg().setCode(auth_code);
                                EventBus.getDefault().post(detailPosDiscountVoucherBean);
                            }else if("CASH".equals(card_type)){//代金券
                                DetailPosCashVoucherBean detailPosCashVoucherBean = gson.fromJson(json, DetailPosCashVoucherBean.class);
                                detailPosCashVoucherBean.getMsg().setCode(auth_code);
                                EventBus.getDefault().post(detailPosCashVoucherBean);
                            }
                        }
                    }else{
                        String card_type = jsonObj.getJSONObject("msg").getString("card_type");
                        if(card_type!=null){
                            if("MEMBER".equals(card_type)){
                                PosMemberBean memberBean = gson.fromJson(json, PosMemberBean.class);
                                memberBean.getMsg().setAuth_code(auth_code);
                                EventBus.getDefault().post(memberBean);
                            }else if("DISCOUNT".equals(card_type)){//折扣券
                                PosDiscountVoucherBean posDiscountVoucherBean = gson.fromJson(json, PosDiscountVoucherBean.class);
                                posDiscountVoucherBean.getMsg().setCode(auth_code);
                                EventBus.getDefault().post(posDiscountVoucherBean);
                            }else if("CASH".equals(card_type)){//代金券
                                PosCashVoucherBean posCashVoucherBean = gson.fromJson(json, PosCashVoucherBean.class);
                                posCashVoucherBean.getMsg().setCode(auth_code);
                                EventBus.getDefault().post(posCashVoucherBean);
                            }
                        }
                    }
                } else {
                    intent = new Intent(this, ChargeActivity.class);
                    intent.putExtra("member_info", json);
                    try {
                        Constant.MEMBER_ID = jsonObj.getJSONObject("msg").getString("member_cardno");
                        LogUtils.e(TAG,"Constant.MEMBER_ID--="+Constant.MEMBER_ID);
                    } catch (JSONException e) {
                        LogUtils.e(TAG,"取不到会员充值会员卡号 e="+e.toString());
                        e.printStackTrace();
                    }
                    intent.putExtra("member_code", Constant.MEMBER_ID);
                    startActivity(intent);
                }
                hideSoftKeyboard();
                finish();
            }else{
                if(keyBoardShow){//判断软键盘是否弹出：
                    Utils.showToastTop(CaptureActivity.this,""+ jsonObj.getString("msg"),2000);
                }else{
                    Utils.showToast(CaptureActivity.this,""+ jsonObj.getString("msg"),2000);
                }
                if(!isManulEnter){
                    finish();
                }else{
                    isManulEnter = false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(findViewById(R.id.root_camera_layout),InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(findViewById(R.id.root_camera_layout).getWindowToken(), 0); //制隐藏键盘
    }

    private void cutDownTimer() {
        recLen--;
        tv_cout_timmer.setText("" + recLen);
        if (recLen < Constant.RECLEN_TIME&&0 < recLen) {
            Message message = handler.obtainMessage(CUT_DOWN_TIMMER);
            handler.sendMessageDelayed(message, 1000);      // send message
        } else {
            isTimeGone = true;
            goToUnKnownActivity();
        }
    }
    private void queryOrderState() {
        LogUtils.e(TAG, "轮询：recLen:" + recLen +",trade_num:" + trade_num);
        if (!isClickCancel) {
            if (recLen < Constant.RECLEN_TIME&&1 < recLen) {
                netWorks.queryPayState("0",trade_num, callBack, PAY_FLAG_QERY_PAY_STATE);
            }
        }
    }
    private void dealQueryError() {
        if (recLen < Constant.RECLEN_TIME&&1 < recLen) {
            handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
        }
    }

    private void cameraGone() {
        et_enter_phone_num.requestFocus();
        LinearLayout.LayoutParams layoutParams_rl2 = (LinearLayout.LayoutParams) rl_enter_phone_num.getLayoutParams();
        //LinearLayout.LayoutParams layoutParams_fl2 = (LinearLayout.LayoutParams) fl_surface_view.getLayoutParams();
        LinearLayout.LayoutParams layoutParams_fl2 = (LinearLayout.LayoutParams) ll_blank_layout.getLayoutParams();
        layoutParams_rl2.weight = 4;
        layoutParams_fl2.weight = 7;
        rl_enter_phone_num.setLayoutParams(layoutParams_rl2);
        //fl_surface_view.setLayoutParams(layoutParams_fl2);
        ll_blank_layout.setLayoutParams(layoutParams_fl2);
        fl_layout_scanning.setVisibility(View.GONE);
        //fl_surface_view.setVisibility(View.INVISIBLE);
        ll_blank_layout.setVisibility(View.INVISIBLE);
        all_scan_code_explain.setVisibility(View.GONE);
        keyBoardShow = true;
    }
    private void cameraVisi() {
        et_enter_phone_num.clearFocus();
        LinearLayout.LayoutParams layoutParams_rl = (LinearLayout.LayoutParams) rl_enter_phone_num.getLayoutParams();
        //LinearLayout.LayoutParams layoutParams_fl = (LinearLayout.LayoutParams) fl_surface_view.getLayoutParams();
        LinearLayout.LayoutParams layoutParams_fl = (LinearLayout.LayoutParams) ll_blank_layout.getLayoutParams();
        layoutParams_rl.weight = 4;
        layoutParams_fl.weight = 11;
        rl_enter_phone_num.setLayoutParams(layoutParams_rl);
        //fl_surface_view.setLayoutParams(layoutParams_fl);
        ll_blank_layout.setLayoutParams(layoutParams_fl);
        fl_layout_scanning.setVisibility(View.VISIBLE);
        //fl_surface_view.setVisibility(View.VISIBLE);
        ll_blank_layout.setVisibility(View.VISIBLE);
        all_scan_code_explain.setVisibility(View.VISIBLE);
        keyBoardShow = false;
        et_enter_phone_num.setText("");
    }

    /**
     * 支付返回error
     */
    private void dealpayError() {
        switch (fromeWhere){
            case Constant.MEMBER_CHARGE_PAY:
                goToUnKnownActivity();
                break;
            default:
                if(!isClickCancel&&!isTimeGone&&recLen<Constant.RECLEN_TIME){
                    if (1 < recLen) {
                        handler.sendEmptyMessage(QUERY_ORDER_STATE);
                    } else {
                        goToUnKnownActivity();
                    }
                }
        }
    }

    private void goToUnKnownActivity() {
        dismissWaittingPopWindow();
        //toUnKnowActivity();
        //弹出支付结果提示页面
        showSeeResultPopWindow(NO_PAY_RESULT);
    }

    private void dealpayRes(String response) {
        LogUtils.e(TAG, "dealpayRes()刷卡支付返回数据：" + response);
        if (!isClickCancel && !isTimeGone) {
            dismissWaittingPopWindow();
                try {
                    JSONObject payResponse = new JSONObject(response);
                    int mCode = payResponse.getInt("code");
                    if (30000 == mCode) {//支付失败
                        LogUtils.e(TAG, "支付失败mCode.equals(\"FAIL\")进入收款失败界面");
                        failreason = payResponse.getString("code_des");
                        solveway = payResponse.getString("solution");
                        setChargeResult(false);
                        toFailActivity();
                    } else if (200 == mCode) {
                        transaction_id = payResponse.getString("transaction_id");
                        try {
                            String QRCode_url =  payResponse.getJSONObject("ticketInfo").getString("url");
                            if(QRCode_url!=null&&!"".equals(QRCode_url)){
                                Constant.query_member_left = QRCode_url;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            payment = payResponse.getString("payment");
                        } catch (JSONException e) {
                            payment = total_fee;
                            LogUtils.e(TAG, e.toString());
                            e.printStackTrace();
                        }
                        Constant.IS_CLOSE_DETAIL_ACTIVITY = true;
                        //1.充值的话变更充值结果
                        setChargeResult(true);
                        //2.非会员充值的话，解析折扣信息
                        parsePayResultJson(payResponse);
                        //3.跳转至成功界面
                        toSuccessActivity();
                        //4.结束订单详情页
                        /*ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
                        manager.killBackgroundProcesses("com.younle.younle624.myapplication.activity.createorder.OrderDetailActivity");*/
                    }
                } catch (JSONException e) {
                    LogUtils.e(TAG, e.toString());
                    e.printStackTrace();
                }
        }
    }

    private void dealQueryRes(String response) {
        LogUtils.e(TAG, "轮询：response" + response);
        if (recLen < Constant.RECLEN_TIME&&1 < recLen&&!isClickCancel) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int code = jsonObject.getInt("code");
                if (code == 200) {//弹出成功页面
                    ResultQueryBean resultQueryBean = new Gson().fromJson(response, ResultQueryBean.class);
                    if ("0".equals(resultQueryBean.getMsg().getSuccess())) {//等待支付
                        handler.sendEmptyMessageDelayed(QUERY_ORDER_STATE, 2000);
                    } else if ("1".equals(resultQueryBean.getMsg().getSuccess())) {//确定成功
                        transaction_id = resultQueryBean.getMsg().getTransaction_id();
                        if(resultQueryBean.getMsg().getPayment()!=0){
                            payment = String.valueOf(resultQueryBean.getMsg().getPayment());
                        }
                        dismissWaittingPopWindow();
                        setChargeResult(true);
                        toSuccessActivity();
                    } else if ("2".equals(resultQueryBean.getMsg().getSuccess())) {//确认失败
                        //弹出失败页面
                        dismissWaittingPopWindow();
                        setChargeResult(false);
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

    private void toFailActivity() {
        Intent intent;
        handler.removeCallbacksAndMessages(null);
        intent = new Intent(CaptureActivity.this, CollectionFailActivity.class);
        intent.putExtra("tradenum", trade_num);
        intent.putExtra("failreason", failreason);
        intent.putExtra("solveway", solveway);
        intent.putExtra("totalfee", total_fee);//"totalfee"
        intent.putExtra("payment", payment);
        intent.putExtra("tradetime", trade_time);
        intent.putExtra("querynum", query_num);
        intent.putExtra("vip_card", vip_card);
        intent.putExtra("song_money",give_money);//赠送的钱
        intent.putExtra(Constant.UNPAY_ORDER_ID,unpay_order_id);
        intent.putExtra("order_no_for_order",order_no_for_order);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        intent.putExtra(Constant.MEMBER_DISCOUNT_BEAN, discountInfo);
        intent.putExtra("member_bean", memberBean);
        intent.putExtra(Constant.FROME_WHERE, fromeWhere);

        LogUtils.e(TAG,"cap 到失败页面的传值...");
        LogUtils.e(TAG,"total_fee="+total_fee+",payment="+payment);
        intent.putExtra("cardinfo_pay",cardinfo_pay);
        startActivity(intent);
        finish();
    }

    private void toSuccessActivity() {
        Intent intent;
        handler.removeCallbacksAndMessages(null);
        LogUtils.e(TAG,"toSuccessActivity()= total_fee="+total_fee+",payment="+payment);
        intent = new Intent(CaptureActivity.this, CollectionSuccessActivity.class);
        intent.putExtra("tradenum", trade_num);
        intent.putExtra("total_fee", total_fee);
        intent.putExtra("payment", payment);
        intent.putExtra("tradetime", trade_time);
        intent.putExtra("querynum", query_num);
        intent.putExtra("transaction_id", transaction_id);
        intent.putExtra(Constant.ORDER_BEAN, orderBean);
        intent.putExtra(Constant.MEMBER_DISCOUNT_BEAN, discountInfo);
        intent.putExtra("cardinfo_pay",cardinfo_pay);
        startActivity(intent);
        finish();
    }

    public void resturnScanResult(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void launchProductQuary(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (capturehandler == null) {
            capturehandler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return capturehandler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = CaptureActivity.this.getResources().openRawResourceFd(
                    R.raw.pay);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume()");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);}

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    private void registBarScanReceiver() {
        IntentFilter mFilter=new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, mFilter);
    }
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if(action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
                int conectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
                LogUtils.e(TAG, "connectState==" + conectState);
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(conectState==0) {
                    tv_barscanner_state.setText("连接失败,请按码枪上的扳机,启动扫码枪");
                    iv_scanner_state_icon.setBackgroundResource(R.drawable.scanner_disconnected);
                    iv_scanner_state_icon.clearAnimation();
                }else {
                    if (conectState == 1) {
                        tv_barscanner_state.setText("正在连接蓝牙扫码枪");
                        iv_scanner_state_icon.setBackgroundResource(R.drawable.scanner_connecting);
                        Utils.pbAnimation(CaptureActivity.this, iv_scanner_state_icon);
                    } else if (conectState == 2) {
                        if (device.getAddress().equals(savedDevice.getBlueToothAdd())) {
                            LogUtils.e(TAG, "device==" + device);
                            tv_barscanner_state.setText("已连接扫码枪,您可以扫描顾客的付款码收款");
                            iv_scanner_state_icon.setBackgroundResource(R.drawable.scanner_connected);
                            iv_scanner_state_icon.clearAnimation();
                        }
                    }
                }
            }else if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                LogUtils.e(TAG,"state=="+state);
                /*PrintDevices   device = (PrintDevices) SaveUtils.getObject(CaptureActivity.this, Constant.SAVED_BLUETOOTH_SCANNER);
                if(state==BluetoothAdapter.STATE_ON&&device!=null) {
                    BluetoothService.getInstance().connect(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(device.getBlueToothAdd()));
                }*/
            }
        }
    };
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.e(TAG,"dispatchKeyEvent()dispatchKeyEvent()dispatchKeyEvent()dispatchKeyEvent()");
        if(scanGunKeyEventHelper!=null&&scanGunKeyEventHelper.isScanGunEvent(event)) {
            scanGunKeyEventHelper.analysisKeyEvent(event);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.e(TAG,"onTouchEvent()onTouchEvent()onTouchEvent()onTouchEvent()");
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                hideSoftKeyboard();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (capturehandler != null) {
            capturehandler.quitSynchronously();
            capturehandler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        if(receiver!=null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}