package com.yongle.letuiweipad.pagers.scalepricetag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.UsbDeviceList;
import com.yongle.letuiweipad.application.MyApplication;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.selfinterface.ConfirmPwdListener;
import com.yongle.letuiweipad.selfinterface.TickePrinterStateListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.NoticePopuUtils;
import com.yongle.letuiweipad.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by bert_dong on 2018/7/5 0005.
 * 邮箱：18701038771@163.com
 */

public class BpFragment extends Fragment {
    private  Activity mActivity;
    private BroadcastReceiver receiver;
    private UsbDeviceList usbDeviceList;
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.tv_price) TextView tv_price;
    @BindView(R.id.ticket_print) TextView ticket_print;
    @BindView(R.id.ticket_state) TextView state;
    @BindView(R.id.printNum) TextView printNum;

    private String weighUnit="";
    private double simpePrice;
    private static final String TAG = "BpFragment";
    private Unbinder bind;
    private NetWorks netWorks;
    private Double currentPrice;
    private AlertDialog changePriceDialog;
    private View totalView;

    public BpFragment() {
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
        totalView = inflater.inflate(R.layout.bp_print_layout,null);
        bind = ButterKnife.bind(this, totalView);
        return totalView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e(TAG,"onstart()");
        usbDeviceList = new UsbDeviceList();
        usbDeviceList.getUsbDeviceList(MyApplication.getContext());
        receiver = usbDeviceList.registPrintReceiver(mActivity,new TickePrinterStateListener(){

            @Override
            public void onConnectIng() {
                if(state!=null) {
                    state.setText("标签打印机连接中");
                }
            }

            @Override
            public void onDisconnect() {
                if(state!=null) {
                    state.setText("标签打印机未连接");
                }
            }

            @Override
            public void onConnected() {
                if(state!=null) {
                    state.setText("标签打印机已连接");
                }
            }

            @Override
            public void onFailed(){
                if(state!=null) {
                    state.setText("标签打印机连接失败");
                }
            }
        });

        IntentFilter filter=new IntentFilter();
        filter.addAction(Constant.GOOD_SELECTED_RECEIVER);
        mActivity.registerReceiver(goodChangeReceiver,filter);
        updatePrintBtnStatus();
    }
    private GoodBean goodBean;
    BroadcastReceiver goodChangeReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            goodBean = (GoodBean) intent.getSerializableExtra(Constant.GOOD_EXTRA_NAME);
            if(goodBean !=null) {
                LogUtils.e(TAG, goodBean.toString());
                if(goodBean.getGoodsPrice()>=10000) {
                    Utils.showToast(mActivity,"打印条码仅支持总价1万元以内的商品");
                    goodBean=null;
                    return;
                }
                simpePrice = goodBean.getGoodsPrice();
                tv_name.setText(goodBean.getGoodsName());
                currentPrice = Double.valueOf(Utils.numdf.format(simpePrice));
                weighUnit= goodBean.getGoodsUnit();
                if(weighUnit!=null&&!TextUtils.isEmpty(weighUnit)){
                    tv_price.setText("￥"+Utils.numdf.format(currentPrice)+"元/"+weighUnit);
                }else {
                    tv_price.setText("￥"+Utils.numdf.format(currentPrice)+"元");
                }
                updatePrintBtnStatus();
            }
        }
    };

    @OnClick({R.id.ticket_print,R.id.change_price,R.id.btn0,R.id.btn00,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9,R.id.btnclear})
    void print(View view){
        switch (view.getId()) {
            case R.id.ticket_print :
                usbDeviceList.startPrint(goodBean,null,"￥"+Utils.numdf.format(currentPrice),Integer.valueOf(printNum.getText().toString()));
                break;
            case R.id.btn0:
            case R.id.btn00:
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
                String inputNum= ((TextView) view).getText().toString();
                String before = printNum.getText().toString();
                Integer num = Integer.valueOf(before+inputNum);
                 if(num>1000) {
                     Utils.showToast(mActivity,"单次打印数量不得超过1000");
                     return;
                 }else {
                     printNum.setText(num+"");
                 }
                updatePrintBtnStatus();
                break;
            case R.id.btnclear:
                printNum.setText("");
                updatePrintBtnStatus();
                break;
            case R.id.change_price://改价
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
        }
        
    }


    private void setNewFee() {
        changePriceDialog = NoticePopuUtils.changeMoney(mActivity, Utils.numdf.format(goodBean.getGoodsPrice()), new NoticePopuUtils.OnFinishChangePrice() {
              @Override
              public void onFinishInput(String afterGj,String gjRemark) {
                  currentPrice = Double.valueOf(Utils.numdf.format(Double.valueOf(afterGj)));

                  if(weighUnit!=null&&!TextUtils.isEmpty(weighUnit)){
                      tv_price.setText("￥"+Utils.numdf.format(currentPrice)+"元/"+weighUnit);
                  }else {
                      tv_price.setText("￥"+Utils.numdf.format(currentPrice)+"元");
                  }
                  changePriceDialog.dismiss();
              }
          });
    }

    private void updatePrintBtnStatus() {
        String str = printNum.getText().toString();
        if(TextUtils.isEmpty(str)) {
            ticket_print.setBackgroundResource(R.drawable.btn_unable_selector);
            ticket_print.setTextColor(Color.parseColor("#99999999"));
            ticket_print.setEnabled(false);
        }else {
            if(Integer.valueOf(str)>0&&goodBean!=null) {
                ticket_print.setBackgroundResource(R.drawable.positive_btn_selector);
                ticket_print.setTextColor(Color.parseColor("#ffffff"));
                ticket_print.setEnabled(true);
            }else {
                ticket_print.setBackgroundResource(R.drawable.btn_unable_selector);
                ticket_print.setTextColor(Color.parseColor("#99999999"));
                ticket_print.setEnabled(false);
            }
        }

    }

    @Override
    public void onDestroyView() {
        LogUtils.e(TAG,"ondestory()");
        bind.unbind();
        if(receiver!=null) {
            LogUtils.e(TAG,"unregistreceiver()"+receiver);
            mActivity.unregisterReceiver(receiver);
        }
        if(goodChangeReceiver!=null) {
            mActivity.unregisterReceiver(goodChangeReceiver);
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
