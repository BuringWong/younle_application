package com.yongle.letuiweipad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.SmScannerBean;
import com.yongle.letuiweipad.domain.SmScannerMemberBean;
import com.yongle.letuiweipad.domain.SmScannerVoucherBean;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import static android.hardware.usb.UsbManager.ACTION_USB_ACCESSORY_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_ACCESSORY_DETACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;

public class ScannerReceiver extends BroadcastReceiver {
    public static final String TAGIN = ACTION_USB_DEVICE_ATTACHED;
    public static final String TAGOUT = ACTION_USB_DEVICE_DETACHED;

    public static final String ain = ACTION_USB_ACCESSORY_ATTACHED;
    public static final String aout = ACTION_USB_ACCESSORY_DETACHED;
    public static final String STATE_CHANGE = "android.hardware.usb.action.USB_STATE";
    private static final String TAG = "ScannerReceiver";


    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(TAGIN)) {
            boolean xsIn = Utils.containUsbScanner(context);
            if(xsIn) {
                Utils.showToast(context,"小闪插入");
                Constant.XS_IN=true;

                EventBus.getDefault().post(new SmScannerBean("sm_scanner_oerder"));
                EventBus.getDefault().post(new SmScannerVoucherBean("sm_scanner_voucher"));
                EventBus.getDefault().post(new SmScannerMemberBean("sm_scanner_member"));
            }
        }else if(action.equals(TAGOUT)) {
            LogUtils.saveLog(TAG,"-----------------------------------");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean xsIn = Utils.containUsbScanner(context);
//                    Utils.showToast(context,"小闪拔出 xsIn="+xsIn+"  Constant:"+Constant.XS_IN);
                    if(Constant.XS_IN&&!xsIn) {
                        Constant.XS_IN=false;
                        Utils.showToast(context,"小闪拔出");
                        EventBus.getDefault().post(new SmScannerBean("sm_scanner_oerder"));
                        EventBus.getDefault().post(new SmScannerVoucherBean("sm_scanner_voucher"));
                        EventBus.getDefault().post(new SmScannerMemberBean("sm_scanner_member"));
                    }
                }
            },1000);

        }
    }
}
