package com.yongle.letuiweipad.pagers.scalepricetag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.MainActivity;
import com.yongle.letuiweipad.activity.UsbDeviceList;
import com.yongle.letuiweipad.activity.manager.weigher.WeighManager;
import com.yongle.letuiweipad.application.MyApplication;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.WmPintData;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.selfinterface.BleWeighListener;
import com.yongle.letuiweipad.selfinterface.ConfirmPwdListener;
import com.yongle.letuiweipad.selfinterface.LocalWeighListener;
import com.yongle.letuiweipad.selfinterface.TickePrinterStateListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by bert_dong on 2018/7/5 0005.
 * 邮箱：18701038771@163.com
 */

public class ScaleLeftFragment extends Fragment {
    private static final String TAG = "ScaleLeftFragment";
    private  Activity mActivity;
    private Unbinder bind;
    private List<WmPintData> printData;
    private UsbManager usbManager;
    private BroadcastReceiver receiver;
    private UsbDeviceList usbDeviceList;
    @BindView(R.id.ticket_state)
    TextView state;
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.tv_price) TextView tv_price;
    @BindView(R.id.tv_weigh) TextView tv_weigh;
    @BindView(R.id.ticket_print) TextView ticket_print;
    @BindView(R.id.total_acc) TextView total_acc;

    @BindView(R.id.tare) TextView tare;
    @BindView(R.id.zero) TextView zero;
    private String weighUnit="g";
    private double simpePrice;
    private GoodBean goodBean;
    private NetWorks netWorks;
    private AlertDialog changePriceDialog;
    private int ble_version;
    private String ble_zf;
    private double ble_weighAcc;
    private String ble_weighUnite;
    private String ble_mode;

    public ScaleLeftFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.e(TAG,"oncreate()");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LogUtils.e(TAG,"oncreateView()");
        View pricetagLeft=inflater.inflate(R.layout.pricetag_left_layout,null);
        bind = ButterKnife.bind(this, pricetagLeft);
        return pricetagLeft;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e(TAG,"onstart()");
        usbManager = (UsbManager)mActivity.getSystemService(Context.USB_SERVICE);
        usbDeviceList = new UsbDeviceList();
        usbDeviceList.getUsbDeviceList(MyApplication.getContext());

        receiver = usbDeviceList.registPrintReceiver(mActivity,new TickePrinterStateListener(){

            @Override
            public void onConnectIng() {
            }

            @Override
            public void onDisconnect() {
            }

            @Override
            public void onConnected() {
            }

            @Override
            public void onFailed(){
            }
        });

        IntentFilter filter=new IntentFilter();
        filter.addAction(Constant.GOOD_SELECTED_RECEIVER);
        mActivity.registerReceiver(goodChangeReceiver,filter);
        initLocalWeigher();
    }

    BroadcastReceiver goodChangeReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            goodBean = (GoodBean) intent.getSerializableExtra(Constant.GOOD_EXTRA_NAME);
            if(goodBean !=null) {
                LogUtils.e(TAG, goodBean.toString());
                tv_name.setText(goodBean.getGoodsName());
                simpePrice = Double.valueOf(Utils.numdf.format(goodBean.getGoodsPrice()));
                weighUnit= goodBean.getGoodsUnit();
                if(weighUnit!=null&&!TextUtils.isEmpty(weighUnit)){
                    tv_price.setText(Utils.numdf.format(simpePrice)+"元/"+weighUnit);
                }else {
                    tv_price.setText(Utils.numdf.format(simpePrice)+"元");
                }
                upDateBtnStatus();
            }
        }
    };
    /**
     * 商米s2的秤的初始化操作
     * public final  int DISCONNECTED=0;
       public final  int CONNECTING=1;
       public final  int CONNECTED=2;
     */
    private void initLocalWeigher() {
        //本地
        ((MainActivity)mActivity).setLocalWeighListener(new LocalWeighListener() {
            @Override
            public void onWeighConnect() {

            }

            @Override
            public void onWeighDisconnect() {

            }

            @Override
            public void onWeighDataChange(int net, int tare, int status) {
                upDateWeigh(net,tare,status);
            }
        });
        //蓝牙
        if(((MainActivity) mActivity).weigher==null) {
            state.setText("请到管理添加电子称");
            state.setTextColor(Color.parseColor("#999999"));
        }else {
            int weighState = WeighManager.getInstance().getWeighState();
            LogUtils.e(TAG,"weighState="+weighState);
            if(weighState==0) {
                state.setText("重新连接");
                state.setTextColor(Color.parseColor("#0000CC"));
            }else if(weighState==1) {
                state.setText("电子秤连接中");
                state.setTextColor(Color.parseColor("#999999"));
            }else {
                state.setText("电子秤已连接");
                state.setTextColor(Color.parseColor("#999999"));
            }
        }



        ((MainActivity)mActivity).setBleWeighListener(new BleWeighListener() {
            @Override
            public void onWeighDel() {
                state.setText("请到管理添加电子称");
                state.setTextColor(Color.parseColor("#999999"));
            }

            @Override
            public void onWeighConnect() {
                state.setText("电子秤已连接");
                state.setTextColor(Color.parseColor("#999999"));
            }

            @Override
            public void onWeighDisconnect() {
                state.setText("重新连接");
                state.setTextColor(Color.parseColor("#0000CC"));

            }

            @Override
            public void onWeighConnecting() {
                state.setText("电子秤连接中");
                state.setTextColor(Color.parseColor("#999999"));
            }

            @Override
            public void onWeighDataChange(int version, double weighAcc, String weighMode, String zf,int status,String weiUnite) {
                LogUtils.e(TAG,"version="+version+ "weighAcc="+weighAcc+"  weighMode="+weighMode+"  zf="+zf+"  status="+status);
                if(version==0) {
                    upDateWeigh(weighAcc*1000,0,status);
                    tare.setVisibility(View.GONE);
                    zero.setVisibility(View.GONE);

                }else {
                    ble_version=version;
                    ble_zf=zf;
                    ble_weighAcc=weighAcc;
                    ble_weighUnite=weiUnite;
                    ble_mode=weighMode;
                    try {
                        Double net = Double.valueOf(zf + weighAcc);
                        upDateWeigh(net,0,status);
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    private void upDateWeigh(double net, double tare, int status) {
        /*if(tare>0) {
            tv_weigh_mode.setText("重量(净重)");
        }else {
            tv_weigh_mode.setText("重量(毛重)");
        }*/
        if("g".equals(weighUnit)) {
            tv_weigh.setText(net+"g");
            if(net>0) {
                total_acc.setText(getString(R.string.money_lgo)+Utils.numdf.format(net * simpePrice));
            }else {
                total_acc.setText(getString(R.string.money_lgo)+"0.00");
            }
        }else {
            tv_weigh.setText(net/1000+"kg");
            if(net>0) {
                total_acc.setText(getString(R.string.money_lgo)+Utils.numdf.format((net/1000) * simpePrice));
            }else {
                total_acc.setText(getString(R.string.money_lgo)+"0.00");
            }
        }
        if(status==0) {//不稳定
            total_acc.setTextColor(Color.parseColor("#33ff5500"));
            tv_weigh.setTextColor(Color.parseColor("#33333333"));
            ticket_print.setEnabled(false);
        }else {
            total_acc.setTextColor(Color.parseColor("#ff5500"));
            tv_weigh.setTextColor(Color.parseColor("#333333"));
            ticket_print.setEnabled(true);
        }
        upDateBtnStatus();
    }

    private void upDateBtnStatus() {
        String str = tv_weigh.getText().toString();
        String total = total_acc.getText().toString();
        boolean overload=false;
        if(total!=null&&!TextUtils.isEmpty(total)&&!TextUtils.equals(total,"--")) {
            Double total_fee = Double.valueOf(total.substring(1));
            if(total_fee>=10000) {
                overload=true;
            }
        }
        if("--".equals(str)||goodBean==null||overload) {
            ticket_print.setBackgroundResource(R.drawable.btn_unable_selector);
            ticket_print.setTextColor(Color.parseColor("#99999999"));
            ticket_print.setEnabled(false);
            if(overload) {
                ticket_print.setText("总价不得超1万");
            }else {
                ticket_print.setText("打印标签");
            }
        }else {
            ticket_print.setBackgroundResource(R.drawable.positive_btn_selector);
            ticket_print.setTextColor(Color.parseColor("#ffffff"));
            ticket_print.setEnabled(true);
            ticket_print.setText("打印标签");

        }
    }

    @OnClick({R.id.ticket_print,R.id.change_price,R.id.zero,R.id.tare,R.id.ticket_state})
    void print(View view){
        switch (view.getId()) {
            case R.id.ticket_print :
                usbDeviceList.startPrint(goodBean,tv_weigh.getText().toString(),total_acc.getText().toString(),1);
                break;
            case R.id.change_price:
                if(goodBean==null) {
                    Utils.showToast(mActivity,"请先选择商品");
                    return;
                }
                if(netWorks==null) {
                    netWorks = new NetWorks(mActivity);
                }
                netWorks.confirmPassWord(new ConfirmPwdListener() {
                    @Override
                    public void onPwdPass() {
                        //展示改价的dia
                        setNewFee();
                    }
                });
                break;
            case R.id.tare:
                tare();
                break;
            case R.id.zero:
                setZero();
                break;
            case R.id.ticket_state:
                if(WeighManager.getInstance().getWeighState()==0) {
                    ((MainActivity)mActivity).connectWeigher();
                    state.setText("连接中.....");
                }
                break;

        }
    }

    private void tare() {
        if(Build.MODEL.contains("S2")) {
            try {
                ((MainActivity)mActivity).scaleManager.tare();
            }catch (Exception e){
                LogUtils.d(TAG,"s2电子秤tare异常 e="+e.toString());
            }
        }else {
            if(TextUtils.equals("-",ble_zf)&&TextUtils.equals("GS",ble_mode)) {
                Utils.showToast(mActivity,mActivity.getResources().getString(R.string.tare_weigh_error),4000);
            }else if(TextUtils.isEmpty(ble_zf)) {
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
            if(TextUtils.equals("g",ble_weighUnite)) {
                currentWeigh=ble_weighAcc/1000;
            }else if(TextUtils.equals("kg",ble_weighUnite)) {
                currentWeigh=ble_weighAcc;
            }
            if(TextUtils.equals("NT",ble_mode)) {
                Utils.showToast(mActivity,mActivity.getResources().getString(R.string.zero_weigh_mode_error),5000);
            }else if(currentWeigh>=0.6) {
                Utils.showToast(mActivity,mActivity.getResources().getString(R.string.zero_weigh_outof_range),5000);
            }else if(TextUtils.isEmpty(ble_mode)) {
            }else {
                WeighManager.getInstance().resetWeigher(WeighManager.ZERO);
            }
        }

    }
    private void setNewFee() {
        changePriceDialog = NoticePopuUtils.changeMoney(mActivity, total_acc.getText().toString().substring(1), new NoticePopuUtils.OnFinishChangePrice() {
            @Override
            public void onFinishInput(String afterGj,String gjRemark) {

                if(weighUnit!=null&&!TextUtils.isEmpty(weighUnit)){
                    tv_price.setText(afterGj+"元/"+weighUnit);
                }else {
                    tv_price.setText(afterGj+"元");
                }
                changePriceDialog.dismiss();
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.e(TAG,"onHiddenChanged()");
        if(hidden) {
            ((MainActivity)mActivity).setLocalWeighListener(null);
        }else {
            initLocalWeigher();
        }
    }
    @Override
    public void onDestroyView() {
        LogUtils.e(TAG,"onDestroyView()");
        bind.unbind();
        ((MainActivity)mActivity).setLocalWeighListener(null);
        ((MainActivity)mActivity).setBleWeighListener(null);

        if(receiver!=null) {
            LogUtils.e(TAG,"unregistreceiver()"+receiver);
            mActivity.unregisterReceiver(receiver);
        }
        if(goodChangeReceiver!=null) {
            mActivity.unregisterReceiver(goodChangeReceiver);
        }
//        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].removeReceiver(mActivity);
        super.onDestroyView();
    }
}
