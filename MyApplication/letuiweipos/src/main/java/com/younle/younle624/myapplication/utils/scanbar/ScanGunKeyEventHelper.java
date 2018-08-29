package com.younle.younle624.myapplication.utils.scanbar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.TextUtils;
import android.view.InputDevice;
import android.view.KeyEvent;

import com.younle.younle624.myapplication.utils.LogUtils;

import java.util.Iterator;
import java.util.Set;


/**
 * 扫码枪事件解析类 by chen
 */
public class ScanGunKeyEventHelper {

    private final static long MESSAGE_DELAY = 500;             //延迟500ms，判断扫码是否完成。
    private StringBuffer mStringBufferResult;                  //扫码内容
    private boolean mCaps;                                     //大小写区分
    private final Handler mHandler;
    private final BluetoothAdapter mBluetoothAdapter;
    private final Runnable mScanningFishedRunnable;
    private OnScanSuccessListener mOnScanSuccessListener;
    private String mDeviceName;
    private int deviceId=-1;

    public ScanGunKeyEventHelper(OnScanSuccessListener onScanSuccessListener) {
        mOnScanSuccessListener = onScanSuccessListener ;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mStringBufferResult = new StringBuffer();
        mHandler = new Handler();
        mScanningFishedRunnable = new Runnable() {
            @Override
            public void run() {
                performScanSuccess();
            }
        };
    }


    /**
     * 返回扫码成功后的结果
     */
    private void performScanSuccess() {
        String barcode = mStringBufferResult.toString();
        if (mOnScanSuccessListener != null)
            mOnScanSuccessListener.onScanSuccess(barcode);
            mStringBufferResult.setLength(0);
    }


    /**
     * 扫码枪事件解析
     * @param event
     */
    public void analysisKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        //字母大小写判断
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            checkLetterStatus(event);
            char aChar = getInputCode(event);
            if (aChar != 0) {
                mStringBufferResult.append(aChar);
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //若为回车键，直接返回
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.post(mScanningFishedRunnable);
            } else {
                //延迟post，若500ms内，有其他事件
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.postDelayed(mScanningFishedRunnable, MESSAGE_DELAY);
            }
        }
    }

    //检查shift键
    private void checkLetterStatus(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //按着shift键，表示大写
                mCaps = true;
            } else {
                //松开shift键，表示小写
                mCaps = false;
            }
        }
    }


    //获取扫描内容
    private char getInputCode(KeyEvent event) {

        int keyCode = event.getKeyCode();

        char aChar;

        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            //字母
            aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            //数字
            aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
        } else {
            //其他符号
            switch (keyCode) {
                case KeyEvent.KEYCODE_PERIOD:
                    aChar = '.';
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    aChar = mCaps ? '_' : '-';
                    break;
                case KeyEvent.KEYCODE_SLASH:
                    aChar = '/';
                    break;
                case KeyEvent.KEYCODE_BACKSLASH:
                    aChar = mCaps ? '|' : '\\';
                    break;
                default:
                    aChar = 0;
                    break;
            }
        }

        return aChar;

    }




    public interface OnScanSuccessListener {
        void onScanSuccess(String barcode);
    }


    public void onDestroy() {
        mHandler.removeCallbacks(mScanningFishedRunnable);
        mOnScanSuccessListener = null;
    }


    //部分手机如三星，无法使用该方法
//    private void hasScanGun() {
//        Configuration cfg = getResources().getConfiguration();
//        return cfg.keyboard != Configuration.KEYBOARD_NOKEYS;
//    }

    /**
     * 扫描枪是否连接
     * @return
     */
    public boolean hasScanGun(Context context) {

        isInputDeviceExist("");
        if (mBluetoothAdapter == null) {
            return false;
        }
        Set<BluetoothDevice> blueDevices = mBluetoothAdapter.getBondedDevices();
        if (blueDevices == null || blueDevices.size() <= 0) {
            return false;
        }
        for (Iterator<BluetoothDevice> iterator = blueDevices.iterator(); iterator.hasNext(); ) {
            BluetoothDevice bluetoothDevice = iterator.next();
            int majorDeviceClass = bluetoothDevice.getBluetoothClass().getMajorDeviceClass();
            if (majorDeviceClass == BluetoothClass.Device.Major.PERIPHERAL) {
                String name = bluetoothDevice.getName();
                LogUtils.Log("蓝牙设备名称："+name);
                deviceId=isInputDeviceExist(mDeviceName);
            }
        }
        Configuration cfg=context.getResources().getConfiguration();
        LogUtils.Log("keyboard=="+cfg.keyboard);//1.keyboard_nokeys 2.KEYBOARD_QWERTY
        return cfg.keyboard!=Configuration.KEYBOARD_NOKEYS;
    }


    /**
     * 输入设备是否存在
     * @param deviceName
     * @return
     */
    private int isInputDeviceExist(String deviceName) {
        int[] deviceIds = InputDevice.getDeviceIds();

        for (int id : deviceIds) {
            String inputDeviceName = InputDevice.getDevice(id).getName();
            LogUtils.Log("输入设备名称==" + inputDeviceName + " id==" + id);
            if (inputDeviceName.equals(deviceName)) {
                LogUtils.Log("id=="+id);
                return id;
            }
        }
        return -1;
    }

    private static final String TAG = "ScanGunKeyEventHelper";
    /**
     * 是否为扫码枪事件(部分机型KeyEvent获取的名字错误)
     * @param event
     * @return
     */
    @Deprecated
    public static  boolean isScanGunEvent(KeyEvent event) {
        String name = event.getDevice().getName();
        LogUtils.e(TAG,"name="+name);

        return TextUtils.equals(name,"General Bluetooth HID Barcode Scanner")
                || name.contains("INSPIRY")
                ||TextUtils.equals(name,"Broadcom Bluetooth HID")
                ||TextUtils.equals(name,"MTK BT HID")
                ||TextUtils.equals(name, "barcode scanner")
                ||name.contains("hid")
                ||name.contains("HID");
//        return name.equals("MTK BT HID"); ||TextUtils.equals(name, Constant.SM_XS)
    }
}





