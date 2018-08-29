package com.yongle.letuiweipad.pagers.manager;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.manager.VoucherDetailActivity;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.domain.ManagerScanBean;
import com.yongle.letuiweipad.domain.PrintDevices;
import com.yongle.letuiweipad.domain.SmScannerVoucherBean;
import com.yongle.letuiweipad.domain.ZtBean;
import com.yongle.letuiweipad.domain.createorder.PayCardinfoBean;
import com.yongle.letuiweipad.domain.voucherhx.VoucherCardInfo;
import com.yongle.letuiweipad.domain.voucherhx.VoucherRead;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.SelfToast;
import com.yongle.letuiweipad.utils.Utils;
import com.yongle.letuiweipad.utils.scanbar.HidConncetUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class VoucherHxPager extends ManagerBasePager {
    View  baseView;
    @BindView(R.id.tv_left)TextView tvLeft;
    @BindView(R.id.tv_right)TextView tvRight;
    @BindView(R.id.tv_payscan_notice)TextView tv_payscan_notice;
    @BindView(R.id.close)ImageView close;
    @BindView(R.id.ll_fp_notice)LinearLayout ll_fp_notice;
    @BindView(R.id.et_enter_num)EditText et_enter_num;
    @BindView(R.id.tv_request_memberinfo) TextView tv_request_memberinfo;
    @BindView(R.id.scanner_error)RelativeLayout scannerError;//读取会员信息时，扫码枪状态异常
    @BindView(R.id.scanner_error_msg)TextView scannerErrorMsg;
    @BindView(R.id.line_left) View line_left;
    @BindView(R.id.line_right) View line_right;
    @BindView(R.id.rg_actions) RadioGroup rg_actions;
    @BindView(R.id.target_des) TextView target_des;
    private static final String TAG = "VoucherHxPager";
    private NetWorks netWorks;
    private String card_type="";
    private TextView tv_package;
    private TextView tv_validity_period;
    private TextView tv_content;
    private TextView tv_status;
    private TextView tv_voucher_function;
    private LinearLayout ll_voucher_function;
    private TextView tv_yes;
    private TextView tv_no;
    private VoucherCardInfo voucherCardInfo;
    private VoucherRead voucherRead;
    private String voucherCode;
    private PrintDevices savedScannerDevice;
    private int action;

    public VoucherHxPager() {
        EventBus.getDefault().register(this);
    }

    @Override
    public View initView() {
        baseView = View.inflate(mActivity, R.layout.voucher_hx_layout, null);
        return baseView;
    }
    /**
     * 扫描结束后的回调
     */
    @Subscribe
    public void onEventMainThread(ManagerScanBean scanBean) {
        if(Constant.MANAGER_POSITION!=2) {
            return;
        }
        voucherCode = scanBean.getCode();
        checkVoucher();
        EventBus.getDefault().cancelEventDelivery(scanBean);
    }

    /**
     * 扫描结束后的回调
     */
    @Subscribe
    public void onEventMainThread(SmScannerVoucherBean xs_stateChange) {
        LogUtils.e(TAG,xs_stateChange.getId()+"  核销页面，位置："+Constant.MANAGER_POSITION);

        if(Constant.MANAGER_POSITION!=2) {
            return;
        }
        if(TextUtils.equals(xs_stateChange.getId(),"sm_scanner_voucher")) {
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

        EventBus.getDefault().cancelEventDelivery(xs_stateChange);
    }

    @Override
    public void initData(int position) {
        tvLeft.setText("核销");
        close.setVisibility(View.GONE);
        tv_payscan_notice.setText("请扫描顾客出示的核销二维码");
        ll_fp_notice.setVisibility(View.INVISIBLE);
        netWorks = new NetWorks(mActivity);
        setState();
        initBarScanner();
    }

    private void setState() {
        rg_actions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rb_left) {
                    line_left.setVisibility(View.VISIBLE);
                    line_right.setVisibility(View.INVISIBLE);
                    target_des.setText("您可以在此核销微信卡券、乐推微H5代金券");
                    et_enter_num.setHint("手动输入手机号或会员卡号");
                    tv_payscan_notice.setText("请扫描顾客的会员卡二维码");
//                    ll_fp_notice.setVisibility(View.VISIBLE);
                    action=0;
                }else if(checkedId==R.id.rb_right) {
                    line_left.setVisibility(View.INVISIBLE);
                    line_right.setVisibility(View.VISIBLE);
                    target_des.setText("您可以在此核销社区门店小程序的自提码");
                    et_enter_num.setHint("手动输入自提码");
                    tv_payscan_notice.setText("请扫描顾客的自提码");
//                    ll_fp_notice.setVisibility(View.GONE);
                    action=1;
                }
            }
        });
        rg_actions.check(R.id.rb_left);
    }

    private void initBarScanner() {
        savedScannerDevice = (PrintDevices) SaveUtils.getObject(mActivity, Constant.SAVED_BLUETOOTH_SCANNER);
        if(savedScannerDevice ==null) {
            if(!Constant.XS_IN) {
                scannerDisconnected();
            }else {
                scannerError.setVisibility(View.VISIBLE);
                scannerErrorMsg.setText(R.string.need_add_scanner);
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
    private void listenScannerState() {
        HidConncetUtil.registBarScanReceiver(mActivity, receiver);
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
    /**
     * 扫码枪连接正常
     */
    private void scannerConnected() {
        scannerError.setVisibility(View.GONE);
    }

    /**
     * 扫码枪连接异常
     */
    private void scannerDisconnected() {
        if(Constant.XS_IN) {
            scannerConnected();
        }else {
            scannerError.setVisibility(View.VISIBLE);
            scannerErrorMsg.setText(R.string.scanner_disconnect);
        }

    }
    @OnClick(R.id.tv_request_memberinfo)
    void finishInputCode(View view){
        et_enter_num.clearFocus();
        voucherCode= et_enter_num.getText().toString();
        checkVoucher();
    }
    private void checkVoucher() {
        if(voucherCode!=null&&!TextUtils.isEmpty(voucherCode)) {
            Map<String, String> params = netWorks.getPublicParams();
            params.put("account_id", Constant.ACCOUNT_ID);
            params.put("advid", Constant.ADV_ID);
            params.put("code", voucherCode);
            params.put("storeid",Constant.STORE_ID);
            params.put("action",action+"");
            netWorks.Request(UrlConstance.VOUCHER_READ_URL, true, "查询中...", params, 5000, 0, new NetWorks.OnNetCallBack() {
                @Override
                public void onError(Exception e, int flag) {
                    LogUtils.e(TAG,"onError()...");
                }

                @Override
                public void onResonse(String response, int flag) {
                    LogUtils.Log("onResponse："+response);
                    praseJson(response);
                }
            });
        }else {
            Toast.makeText(mActivity, "请输入兑换码", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析联网请求查询到的代金券信息
     * @param json
     */
    private void praseJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            String code = jsonObject.getString("code");
            if("200".equals(code)){
                Gson gson=new Gson();
                if(action==0) {
                    int card_type = jsonObject.getInt("card_type");
                    switch (card_type){
                        case 0:
                            voucherRead = gson.fromJson(json, VoucherRead.class);
                            if(voucherRead!=null&&!TextUtils.equals("核销",voucherRead.getMsg().getStatus())) {
                                handleUnusual(voucherRead.getMsg().getStatus()+" 无法使用！");
                            }else {
                                showVoucherInfoDia();
                            }
                            break;
                        case 1:
                            voucherCardInfo = gson.fromJson(json, VoucherCardInfo.class);
                            showVoucherInfoDia();
                            break;
                    }
                }else if(action==1) {
                    ZtBean ztBean = gson.fromJson(jsonObject.getString("msg"), ZtBean.class);
                    Intent intent=new Intent(mActivity, VoucherDetailActivity.class);
                    intent.putExtra("code",voucherCode);
                    intent.putExtra("ztbean",ztBean);
                    mActivity.startActivity(intent);
                    et_enter_num.setText("");
                }
            }else {
                String notice;
                if(jsonObject.getString("msg") != null){
                    notice = jsonObject.getString("msg");
                }else{
                    notice="未定义的核销码错误";
                }
                handleUnusual(notice);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示订单内容
     */
    private void showVoucherInfoDia() {
        final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.voucher_verification_package, null);
        tv_package = ButterKnife.findById(view, R.id.tv_package);
        tv_validity_period = ButterKnife.findById(view, R.id.tv_validity_period);
        tv_voucher_function = ButterKnife.findById(view, R.id.tv_voucher_function);
        ll_voucher_function = ButterKnife.findById(view, R.id.ll_voucher_function);
        tv_content = ButterKnife.findById(view, R.id.tv_content);
        tv_status = ButterKnife.findById(view, R.id.tv_status);


        tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        tv_no = (TextView) view.findViewById(R.id.tv_no);

        final AlertDialog alertDialog = new AlertDialog.Builder(mActivity)
                .setView(view)
                .show();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        int[] wh=new int[2];
        Utils.getRelativeWH(mActivity,690,720,wh);
        params.width=wh[0];
        params.height=wh[1];
        alertDialog.getWindow().setAttributes(params);
        inflateData();

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                hxVoucher();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void inflateData() {
        if(voucherRead != null) {
            card_type = "0";
            voucherCode = voucherRead.getMsg().getSecmark();//核销码
            tv_package.setText(voucherRead.getMsg().getServer_name());
            tv_validity_period.setText(voucherRead.getMsg().getVhs_start() +"至"+ voucherRead.getMsg().getVhs_end());
            String vhs_info = voucherRead.getMsg().getVhs_info();
            Spanned spanned = Html.fromHtml(Html.fromHtml(vhs_info).toString());
            LogUtils.Log("spanned" + spanned);
            tv_content.setText(spanned);
            switch (voucherRead.getMsg().getStatus()) {
                case "核销" :
                    tv_status.setVisibility(View.GONE);
                    tv_yes.setVisibility(View.VISIBLE);
                    break;
                default:
                    tv_status.setVisibility(View.VISIBLE);
                    tv_status.setText(voucherRead.getMsg().getStatus()+" 无法使用！");
                    tv_yes.setVisibility(View.GONE);
                    tv_no.setText("关闭");
                    break;
            }
        }else if(voucherCardInfo != null) {
            card_type = "1";
            tv_package.setText(voucherCardInfo.getMsg().getTitle());
            tv_validity_period.setText(voucherCardInfo.getMsg().getUsetime());
            tv_content.setText(voucherCardInfo.getMsg().getInfo());
            tv_yes.setVisibility(View.VISIBLE);
            if(voucherCardInfo.getMsg().getCardmsg()!=null&&!"".equals(voucherCardInfo.getMsg().getCardmsg())){
                ll_voucher_function.setVisibility(View.VISIBLE);
                tv_voucher_function.setText(voucherCardInfo.getMsg().getCardmsg());
            }
        }
    }
    private void hxVoucher() {
        NetWorks netWorks=new NetWorks(mActivity);
        Map<String, String> params = netWorks.getPublicParams();
        params.put("account_id", Constant.ACCOUNT_ID);
        params.put("storename", Constant.STORE_M);
        params.put("advid", Constant.ADV_ID);
        params.put("storeid",Constant.STORE_ID);
        params.put("code", voucherCode);
//        params.put("code", "-1");
        params.put("card_type", card_type);
        params.put("cardinfo", getCardinfoJson());

        netWorks.Request(UrlConstance.HX_VOUCHER_URL, true, "核销中...", params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                Toast.makeText(mActivity, "连接超时，请检查网络后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("核销返回："+response.toString());
                PraseJson(response);
            }
        });
    }
    private void PraseJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if(code==200) {
                SelfToast.showToast(mActivity,"核销成功",20000,true);
            }else {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                Utils.showToast(mActivity,"核销失败",1000);
                NoticePopuUtils.showCancelFail(mActivity, "核销失败", "返回重试",R.id.hx_layout, new NoticePopuUtils.OnFinishChoose() {
                    @Override
                    public void onClickYes() {
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String getCardinfoJson() {
        if(voucherCardInfo != null){
            PayCardinfoBean payCardinfoBean = new PayCardinfoBean();
            LogUtils.e(TAG,"getCardinfoJson Code="+voucherCardInfo.getMsg().getCode());
            payCardinfoBean.setCode(voucherCode);

            LogUtils.e(TAG,"getCardinfoJson Cardid="+voucherCardInfo.getMsg().getCardid());
            payCardinfoBean.setCardid(voucherCardInfo.getMsg().getCardid());

            LogUtils.e(TAG,"getCardinfoJson Cardtype="+voucherCardInfo.getMsg().getCard_type());
            payCardinfoBean.setCardtype(voucherCardInfo.getMsg().getCard_type());

            LogUtils.e(TAG,"getCardinfoJson Least_cost="+voucherCardInfo.getMsg().getLeast_cost());
            payCardinfoBean.setLeast_cost(String.valueOf(voucherCardInfo.getMsg().getLeast_cost()));

            LogUtils.e(TAG,"getCardinfoJson Reduce_cost="+voucherCardInfo.getMsg().getReduce_cost());
            payCardinfoBean.setReduce_cost(String.valueOf(voucherCardInfo.getMsg().getReduce_cost()));

            LogUtils.e(TAG,"getCardinfoJson Vipcardid="+voucherCardInfo.getMsg().getVipcardid());
            payCardinfoBean.setVipcardid(String.valueOf(voucherCardInfo.getMsg().getVipcardid()));

            LogUtils.e(TAG,"getCardinfoJson Info="+voucherCardInfo.getMsg().getInfo());
            payCardinfoBean.setInfo(voucherCardInfo.getMsg().getInfo());
            return new Gson().toJson(payCardinfoBean);
        }else{
            return "";
        }
    }
    /**
     * 扫描核销异常的处理
     * @param notice
     */
    private void handleUnusual(String notice) {
        NoticePopuUtils.showCancelFail(mActivity, notice, "确定",R.id.hx_layout, new NoticePopuUtils.OnFinishChoose() {
            @Override
            public void onClickYes() {
            }
        });
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
