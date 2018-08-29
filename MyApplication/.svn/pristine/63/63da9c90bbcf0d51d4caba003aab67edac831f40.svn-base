package com.younle.younle624.myapplication.utils.printmanager;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.domain.waimai.WmPintData;
import com.younle.younle624.myapplication.myservice.BluetoothService;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import zxing.encoding.EncodingHandler;

/**
 * 作者：Create by 我是奋斗 on2017/3/15 13:28
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class BTPrintUtils {
    public static BTPrintUtils instance;
    private static int FONT_BIG=1;
    private static int FONT_NORMAL=0;
    public static BTPrintUtils getInstance(){
        if(instance==null) {
            instance=new BTPrintUtils();
        }
        return instance;
    }
    public  void connectBtPrinterTest(BluetoothService mService,Context context,Handler handler){
        SavedPrinter device= (SavedPrinter) SaveUtils.getObject(context, Constant.BT_PRINTER);
        if(device!=null) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
                handler.sendEmptyMessageDelayed(Constant.WAIT_BLUETOOTH_OPEN, 1000);
            }else {
                if(mService.getState()!=BluetoothService.STATE_CONNECTED&&mService.getState()!=BluetoothService.STATE_CONNECTING) {
                    mService.connect(bluetoothAdapter.getRemoteDevice(device.getBlueToothAdd()));
                }
            }
        }
    }

    public void btFormatDataPrint(final BluetoothService mService, final List<WmPintData> data, final Context context) {
        new Thread(){
            public void run(){
                LogUtils.Log("列表页打印："+Thread.currentThread().getName());
                for (int i = 0; i < data.size(); i++) {
                    WmPintData wmPintData = data.get(i);
                    int alin = wmPintData.getAlin();
                    String msg = wmPintData.getMsg();
                    if (msg.equals("----------------------------")) {
                        msg =      "--------------------------------";
                    }
                    int textSize = wmPintData.getTextSize();
                    int type = wmPintData.getType();
                    mService.printReset();

                    if(type==1) {
                        Bitmap bitmap = generateQRcode(msg);
                        sendMessage(mService,bitmap,context);
                        mService.printLF();
                    }else {
                        switch (alin) {
                            case 0 :
                                mService.printLeft();
                                break;
                            case 1:
                                mService.printCenter();
                                break;
                            case 2:
                                mService.printRight();
                                break;
                        }
                        switch (textSize) {
                            case  27:
                                mService.printSize(FONT_NORMAL);
                                break;
                            case 35:
                            case 43:
                                mService.printSize(FONT_BIG);
                                break;
                        }
                        if(msg.contains("原单价")) {
                            sendMessage(mService,msg,context);
                            mService.printLF();
                        }else {
                            sendMessage(mService,msg,context);
                        }
                        mService.printLF();
                    }
                }
            }
        }.start();
    }
    /**
     * 来自CollectionSuccessActivity的打印
     * @param mService
     * @param headData
     * @param forCustomer
     * @param memberData
     * @param context
     */
    public void csDetailBtPrint(final BluetoothService mService, final List<String> headData, final boolean forCustomer,
                                final List<String> memberData, final Context context, final BluetoothService.BtPrintFinshCallBack callBack) {
        new Thread(){
            public void run(){
                LogUtils.Log("直收页打印："+Thread.currentThread().getName());
                for (int i = 0; i < headData.size(); i++) {
                    String msg = headData.get(i);
                    if(forCustomer){
                        switch (msg){
                            case "支付方式:刷卡收款(记账)":
                                msg = "支付方式:刷卡收款";
                                break;
                            case "支付方式:现金收款(记账)":
                                msg = "支付方式:现金收款";
                                break;
                            case "支付方式:微信收款(记账)":
                                msg = "支付方式:微信收款";
                                break;
                            case "支付方式:支付宝收款(记账)":
                                msg = "支付方式:支付宝收款";
                                break;
                        }
                    }
                    if (msg.equals("----------------------------")) {
                        msg =      "--------------------------------";
                    }
                    mService.printReset();
                    if ((i == 0 || i == 1 || i == 2)&&forCustomer) {
                        mService.printSize(FONT_BIG);
                        mService.printCenter();
                        sendMessage(mService, msg, context);
                        mService.printLF();
                    }else if((i == 0 || i == 1)&&!forCustomer) {
                        mService.printSize(FONT_BIG);
                        mService.printCenter();
                        sendMessage(mService, msg, context);
                        mService.printLF();
                    }else {
                        if(msg.contains("原单价")) {
                            sendMessage(mService, msg, context);
                            mService.printLF();
                        }else {
                            sendMessage(mService, msg, context);
                        }
                        mService.printLF();
                    }
                }
                //2.再打会员部分内容
                if (forCustomer && memberData != null && memberData.size() > 0) {
                    for (int i = 0; i < memberData.size(); i++) {
                        String msg = memberData.get(i);
                        if (msg.equals("----------------------------------")) {
                                 msg = "----------------------------------";
                        }
                        mService.printReset();
                        if ((memberData.contains("扫描下方二维码成为我店会员") || memberData.contains("会员可使用微信扫描下方二维码")) && i == (memberData.size() - 1)) {//二维码
                            mService.printSize(FONT_NORMAL);
                            Bitmap bitmap = generateQRcode(msg);
                            if(bitmap!=null){
                                sendMessage(mService, bitmap, context);
                                mService.printLF();
                            }
                        } else if ("扫描下方二维码成为我店会员".equals(msg)) {
                            mService.printSize(FONT_NORMAL);
                            mService.printCenter();
                            sendMessage(mService, msg, context);
                            mService.printLF();
                        } else {
                            mService.printSize(FONT_NORMAL);
                            mService.printCenter();
                            sendMessage(mService, msg, context);
                            mService.printLF();
                        }
                    }
                }
                //3.最后打印底部信息联
                if (forCustomer) {
                    if(Constant.bottomData!=null&&Constant.bottomData.length>0) {
                        for (int i = 0; i < Constant.bottomData.length; i++) {
                            String msg = Constant.bottomData[i];
                            mService.printReset();
                            mService.printSize(FONT_NORMAL);
                            mService.printLeft();
                            sendMessage(mService, msg, context);
                            mService.printLF();
                        }
                    }
                    mService.printReset();
                    sendMessage(mService,"--------------------------------",context);
                    mService.printLF();
                    mService.printReset();
                    sendMessage(mService,"顾客联",context);
                    mService.printLF();
                } else {
                    mService.printReset();
                    mService.printSize(FONT_NORMAL);
                    mService.printLeft();
                    sendMessage(mService, "商家联", context);
                    mService.printLF();
                    mService.printLF();
                }
                mService.printLF(callBack);
            }
        }.start();
    }
    /**
     * 外卖蓝牙打印
     * @param mService
     * @param data
     * @param context
     */
    public void wmBtPrint(final BluetoothService mService, final List<WmPintData> data, final Context context, final BluetoothService.BtPrintFinshCallBack callBack) {
        new Thread(){
            public void run(){
                if(data!=null&&data.size()>0) {
                    for (int i = 0; i < data.size(); i++) {
                        mService.printReset();
                        if(data.get(i).getTextSize()==27) {
                            mService.printSize(FONT_NORMAL);
                        }else if(data.get(i).getTextSize()==35||data.get(i).getTextSize()==43) {
                            mService.printSize(FONT_BIG);
                        }
                        //二维码
                        if(data.get(i).getType()== WmPintData.QRCODE) {
                            Bitmap bitmap = generateQRcode(data.get(i).getMsg());
                            if(bitmap!=null){
                                sendMessage(mService, bitmap, context);
                                mService.printLF();
                            }
                        }else {
                            mService.printLeft();
                            sendMessage(mService, data.get(i).getMsg(), context);
                            mService.printLF();
                        }
                    }
                    mService.printLF();
                    mService.printLF(callBack);
                }
            }
        }.start();
    }

    /**
     * 打印文本
     * @param message
     */
    public void sendMessage(BluetoothService mService,String message,Context context) {
        LogUtils.Log("sendMessage()");
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
//            Toast.makeText(context, "蓝牙没有连接", Toast.LENGTH_SHORT).show();
            return;
        }
        if (message.length() > 0) {
            byte[] send;
            try {
                send = message.getBytes("GB2312");
            } catch (UnsupportedEncodingException e) {
                send = message.getBytes();
            }
            mService.write(send,null);
        }
    }
    /**
     * 打印图片
     * @param bitmap
     */
    public void sendMessage(BluetoothService mService,Bitmap bitmap,Context context) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            return;
        }
        // 发送打印图片前导指令
        byte[] start = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1B,
                0x40, 0x1B, 0x33, 0x00 };
        mService.write(start,null);
        byte[] draw2PxPoint = PicFromPrintUtils.draw2PxPoint(bitmap);
        mService.write(draw2PxPoint,null);
        // 发送结束指令
        byte[] end = { 0x1d, 0x4c, 0x1f, 0x00 };
        mService.write(end,null);
    }

    private Bitmap generateQRcode(String str) {
        try {
            Bitmap bitmap = EncodingHandler.createQRCode(str, 192);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
