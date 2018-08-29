package com.yongle.letuiweipad.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.selfinterface.TickePrinterStateListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.yongle.letuiweipad.utils.ticketprinter.DeviceConnFactoryManager;
import com.yongle.letuiweipad.utils.ticketprinter.PrinterCommand;
import com.yongle.letuiweipad.utils.ticketprinter.ThreadPool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import butterknife.BindView;
import butterknife.Unbinder;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static com.yongle.letuiweipad.constant.Constant.MESSAGE_UPDATE_PARAMETER;
import static com.yongle.letuiweipad.utils.ticketprinter.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;
import static com.yongle.letuiweipad.utils.ticketprinter.DeviceConnFactoryManager.CONN_STATE_FAILED;

/**
 * Created by Administrator
 *
 * @author 猿史森林
 *         Date: 2017/11/3
 *         Class description:
 */
public class UsbDeviceList {
    /**
     * Debugging
     */
    private static final String DEBUG_TAG = "DeviceListActivity";
    public static LinearLayout deviceNamelinearLayout;

    /**
     * Member fields
     */
    private ListView lvUsbDevice = null;
    private ArrayAdapter<String> mUsbDeviceArrayAdapter;
    public static final String USB_NAME = "usb_name";
    private UsbManager usbManager;
    private int id=0;
    private ThreadPool threadPool;
    /**
     * 连接状态断开
     */
    private static final int CONN_STATE_DISCONN = 0x007;
    /**
     * 使用打印机指令错误
     */
    private static final int PRINTER_COMMAND_ERROR = 0x008;
    private static final int CONN_MOST_DEVICES = 0x11;
    private static final int CONN_PRINTER = 0x12;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_USB_PERMISSION:
                    synchronized (this) {
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (device != null) {
                                System.out.println("permission ok for device " + device);
                                usbConn(device);
                            }
                        } else {
                            System.out.println("permission denied for device " + device);
                        }
                    }
                    break;
                //Usb连接断开、蓝牙连接断开广播
                case ACTION_USB_DEVICE_DETACHED:
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget();
                    break;
                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state) {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if (id == deviceId) {
                                LogUtils.e(TAG,"device disconnect");
                                if(tickePrinterStateListener!=null) {
                                    tickePrinterStateListener.onDisconnect();
                                }
//                                tv_state.setText(getString(R.string.str_conn_state_disconnect));
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            LogUtils.e(TAG,"device connecting");
                            if(tickePrinterStateListener!=null) {
                                tickePrinterStateListener.onConnectIng();
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            LogUtils.e(TAG,"device connected");
                            if(tickePrinterStateListener!=null) {
                                tickePrinterStateListener.onConnected();
                            }
                            break;
                        case CONN_STATE_FAILED:
                            LogUtils.e(TAG,"device connect fail");
                            Utils.toast(context, context.getString(R.string.str_conn_fail));
                            if(tickePrinterStateListener!=null) {
                                tickePrinterStateListener.onFailed();
                            }
                            break;
                        default:
                            break;
                    }
                    break;
//                case ACTION_QUERY_PRINTER_STATE:
//                    if(counts >0) {
//                        sendContinuityPrint();
//                    }
//                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                    }
                    break;
                case PRINTER_COMMAND_ERROR:
                    Utils.toast(context, context.getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:
                    Utils.toast(context, context.getString(R.string.str_cann_printer));
                    break;
                case MESSAGE_UPDATE_PARAMETER:
                    String strIp = msg.getData().getString("Ip");
                    String strPort = msg.getData().getString("Port");
                    //初始化端口信息
                    new DeviceConnFactoryManager.Build()
                            //设置端口连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
                            //设置端口IP地址
                            .setIp(strIp)
                            //设置端口ID（主要用于连接多设备）
                            .setId(id)
                            //设置连接的热点端口号
                            .setPort(Integer.parseInt(strPort))
                            .build();
                    threadPool = ThreadPool.getInstantiation();
                    threadPool.addTask(new Runnable() {
                        @Override
                        public void run() {
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    private Unbinder bind;
    @BindView(R.id.state) TextView tv_state;
    @BindView(R.id.startprint) TextView startPrint;
    private Context context;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_usb_list);
        bind = ButterKnife.bind(this);

        *//*usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        lvUsbDevice = (ListView) findViewById(R.id.lvUsbDevices);
        mUsbDeviceArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.usb_device_name_item);
        lvUsbDevice.setOnItemClickListener(mDeviceClickListener);
        lvUsbDevice.setAdapter(mUsbDeviceArrayAdapter);*//*
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        getUsbDeviceList(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_QUERY_PRINTER_STATE);
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        registerReceiver(receiver, filter);
    }*/

    public void startPrint(final GoodBean goodBean, final String weigh,final String totalFee, final int copyNum){
        if(goodBean==null) {
            Utils.showToast(context,"请选择商品后打印！");
            return;
        }else if(copyNum<=0) {
            Utils.showToast(context,"请输入正确的打印份数！");
            return;
        }
        threadPool = ThreadPool.getInstantiation();
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                        !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                    mHandler.obtainMessage(CONN_PRINTER).sendToTarget();
                    return;
                }
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                    sendLabel(goodBean,weigh,totalFee,copyNum);
                } else {
                    mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
                }
            }
        });
    }

    /**
     * 发送标签
     *
     */
    void sendLabel(GoodBean goodBean,String weigh,String totalFee,int copyNum) {
        LabelCommand tsc = new LabelCommand();
//        tsc.addHome();
        // 设置标签尺寸，按照实际尺寸设置
        tsc.addSize(58, 37);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(2);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        // 设置原点坐标
        tsc.addReference(0, 0);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();
//        tsc.addOffset(0);
        // 绘制简体中文,18
        tsc.addText(20, 0, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                goodBean.getGoodsName());
        tsc.addDensity(LabelCommand.DENSITY.DNESITY5);

        Date date=new Date(System.currentTimeMillis());
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tsc.addText(20, 60, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "打印日期:"+ df.format(date));
        String singlePrice = Utils.numdf.format(goodBean.getGoodsPrice());
        if(weigh==null&&Double.valueOf(singlePrice)!=Double.valueOf(totalFee.substring(1))) {
            singlePrice=totalFee.substring(1);
        }
        if(goodBean.getGoodsUnit()!=null&&!TextUtils.isEmpty(goodBean.getGoodsUnit())) {
            tsc.addText(20, 100, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "单价:"+ singlePrice +"元/"+goodBean.getGoodsUnit());
        }else {
            tsc.addText(20, 100, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "单价:"+ singlePrice +"元");
        }
        if(weigh==null) {//标品
            tsc.addText(20, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "数量:1");
        }else {
            tsc.addText(20, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "重量:"+weigh);
        }

        tsc.addText(20, 180, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_3,
                "计价:"+totalFee);
        tsc.addDensity(LabelCommand.DENSITY.DNESITY15);
        tsc.addText(20, 260, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                Constant.STORE_P+Constant.STORE_M);

       /* tsc.addText(0, 80, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                "可以打印几个可以打印几个可以打印几个可以打印几个");
        tsc.addDensity(LabelCommand.DENSITY.DNESITY8);
        tsc.addText(0, 160, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_3, LabelCommand.FONTMUL.MUL_3,
                "可以打印几个可以打印几个可以打印几个可以打印几个");
        tsc.addDensity(LabelCommand.DENSITY.DNESITY15);*/
//        tsc.addText(20, 0, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
//                "测试标签打印机2222");
        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.gprinter);
//        tsc.addBitmap(20, 50, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);

//        tsc.addQRCode(250, 80, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.smarnet.cc");
        // 绘制一维条码
        String price = totalFee.substring(1);
        double tfee = Double.valueOf(price) * 100;
        String moneyFormat = Utils.codedf.format(tfee);//5位

        String goodsId = goodBean.getGoodsId();
        String idFormat = Utils.codedf.format(Double.valueOf(goodsId));//5位

        if(Double.valueOf(price)>=1000) {
            tsc.add1DBarcode(340, 240, LabelCommand.BARCODETYPE.EAN13, 80, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_270,
                    "98"+idFormat+moneyFormat);
        }else {
            tsc.add1DBarcode(340, 240, LabelCommand.BARCODETYPE.EAN13, 80, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_270,
                    "99"+idFormat+moneyFormat);
        }

        // 打印标签
        tsc.addDensity(LabelCommand.DENSITY.DNESITY1
        );
        tsc.addPrint(copyNum, 1);
        // 打印标签后 蜂鸣器响
        tsc.addLimitFeed(30);
        tsc.addSound(2, 100);
//        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null) {
            return;
        }
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
    }

    boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        return ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85)
                || (vid == 6790 && pid == 30084)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 768)
                || (vid == 26728 && pid == 1024) || (vid == 26728 && pid == 1280)
                || (vid == 26728 && pid == 1536));
    }

    private static final String TAG = "UsbDeviceList";
    public void getUsbDeviceList(Context context) {
        this.context=context;
        UsbManager manager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
        // Get the list of attached devices
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devices.values().iterator();
        int count = devices.size();
        Log.d(DEBUG_TAG, "count " + count);
        if (count > 0) {
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                String devicename = device.getDeviceName();
                if (checkUsbDevicePidVid(device)) {
//                    mUsbDeviceArrayAdapter.add(devicename);
                    LogUtils.e(TAG,"find device start connect!");
                    if (manager.hasPermission(device)) {
                        usbConn(device);
                    } else {//请求权限
                        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                        manager.requestPermission(device, mPermissionIntent);
                    }
                    break;
                }
            }
        } else {
            String noDevices = context.getResources().getText(R.string.none_usb_device)
                    .toString();
            LogUtils.e(TAG, "noDevices " + noDevices);
//            mUsbDeviceArrayAdapter.add(noDevices);
        }
    }
    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

   /* private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String noDevices = getResources().getText(R.string.none_usb_device).toString();
            if (!info.equals(noDevices)) {
                String address = info;
                // Create the result Intent and include the MAC address
                Intent intent = new Intent();
                intent.putExtra(USB_NAME, address);
                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);

                //通过USB设备名找到USB设备
                UsbDevice usbDevice = Utils.getUsbDeviceFromName(UsbDeviceList.this, address);
                //判断USB设备是否有权限
                if (usbManager.hasPermission(usbDevice)) {
                    usbConn(usbDevice);
                } else {//请求权限
                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(UsbDeviceList.this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(usbDevice, mPermissionIntent);
                }
            }

            finish();
        }
    };*/
    /**
     * usb连接
     *
     * @param usbDevice
     */
    private void usbConn(UsbDevice usbDevice) {
        new DeviceConnFactoryManager.Build()
                .setId(id)
                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.USB)
                .setUsbDevice(usbDevice)
                .setContext(context)
                .build();
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        DeviceConnFactoryManager.closeAllPort();
        if (threadPool != null) {
            threadPool.stopThreadPool();
        }
        if(receiver!=null) {
            unregisterReceiver(receiver);
        }
    }*/

    private String getConnDeviceInfo() {
        String str = "";
        DeviceConnFactoryManager deviceConnFactoryManager = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id];
        if (deviceConnFactoryManager != null
                && deviceConnFactoryManager.getConnState()) {
            if ("USB".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "USB\n";
                str += "USB Name: " + deviceConnFactoryManager.usbDevice().getDeviceName();
            } else if ("WIFI".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "WIFI\n";
                str += "IP: " + deviceConnFactoryManager.getIp() + "\t";
                str += "Port: " + deviceConnFactoryManager.getPort();
            } else if ("BLUETOOTH".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "BLUETOOTH\n";
                str += "MacAddress: " + deviceConnFactoryManager.getMacAddress();
            } else if ("SERIAL_PORT".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "SERIAL_PORT\n";
                str += "Path: " + deviceConnFactoryManager.getSerialPortPath() + "\t";
                str += "Baudrate: " + deviceConnFactoryManager.getBaudrate();
            }
        }
        return str;
    }
    TickePrinterStateListener tickePrinterStateListener;
    public BroadcastReceiver registPrintReceiver(Activity mActivity, TickePrinterStateListener tickePrinterStateListener) {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_QUERY_PRINTER_STATE);
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        mActivity.registerReceiver(receiver, filter);
        LogUtils.e(TAG,"注册时的receiver="+receiver);
        this.tickePrinterStateListener=tickePrinterStateListener;
        return receiver;
    }
}
