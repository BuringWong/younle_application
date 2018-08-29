package com.younle.younle624.myapplication.myservice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.LoginActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.AppLetReceiveBean;
import com.younle.younle624.myapplication.domain.SaveAppletOrderNoBean;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.domain.printsetting.YunPrintGroupBean;
import com.younle.younle624.myapplication.domain.waimai.ElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.MtOrderDetail;
import com.younle.younle624.myapplication.domain.waimai.NewElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.WmPintData;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.utils.printmanager.BTPrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.PrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.YunPrintUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import static com.igexin.sdk.GTServiceManager.context;

public class PrintService extends Service {
    private static final String TAG = "PrintService";
    private IWoyouService iWoyouService;
    public ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWoyouService = IWoyouService.Stub.asInterface(service);

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    private BluetoothService.BtPrintFinshCallBack btCallBack=new BluetoothService.BtPrintFinshCallBack() {
        @Override
        public void onError() {

        }
        @Override
        public void onFinish() {
                //1.计算下标，获取相应bean，改变其状态值，并发送消息到界面刷新视图
                //2.将订单保存到已当前日期命名的文件中
            try {
                String today = Utils.getToday();
                LogUtils.Log("bt_wm_data="+Constant.bt_wm_data.size() +" pre=="+Constant.bt_pre_size);
                Map apBean = Constant.bt_wm_data.get(Constant.bt_wm_data.size() - 1 -Constant.bt_pre_size );
                apBean.put("print", true);
                Constant.bt_pre_size++;
                SaveUtils.saveObject(PrintService.this, "bt"+today, Constant.bt_wm_data);
                SpUtils.getInstance(PrintService.this).save(Constant.BT_PARA_PRE_SIZE, Constant.bt_pre_size);
                EventBus.getDefault().post(true);
                //3.判断是否有新订单进来，有的话继续打印
                if(Constant.bt_wm_data.size()> Constant.bt_pre_size) {
                    Map map = Constant.bt_wm_data.get(Constant.bt_wm_data.size() - Constant.bt_pre_size-1);
                    String type= (String) map.get("type");
                    Object bean = map.get("bean");
                    if("MT".equals(type)) {
                        btprintData = PrintUtils.getInstance().formatPrintDataWithSize((MtOrderDetail) bean, null, null, PrintService.this, true);
                    }
                    if("ELM".equals(type)) {
                        btprintData = PrintUtils.getInstance().formatPrintDataWithSize(null, (ElmOrderBean) bean, null, PrintService.this, true);
                    }
                    if("NEW_ELM".equals(type)) {
                        btprintData = PrintUtils.getInstance().formatPrintDataWithSize(null, null, (NewElmOrderBean) bean, PrintService.this, true);
                    }
                    PrintByBluetooth();
                }else {
                    //如果没有外卖数据：检查是否有小程序数据
                    Constant.bt_isPrinting=false;
                    checkNotPrintOrOutLimit();
                }
            } catch (Exception e) {
                LogUtils.e(TAG,"蓝牙重新连接时候 Exception e="+e.toString());
                e.printStackTrace();
                dealReconnectNoWMdata();
            }
        }
    };
    private boolean appletConnectRequire=false;

    private void dealReconnectNoWMdata() {
        //如果没有外卖数据：此处会catch住一个数组下标越界错误：检查是否有小程序数据
        Constant.bt_isPrinting=false;
        saveLocalAndDelete(true);
        checkNotPrintOrOutLimit();
    }

    private ICallback printCallBack=new ICallback.Stub() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {
            if(isSuccess) {
                LogUtils.e(TAG,"本地打印完成了");
                //1.计算下标，获取相应bean，改变其状态值，并发送消息到界面刷新视图
                //2.将订单保存到已当前日期命名的文件中
                String today = Utils.getToday();
                LogUtils.Log("打印完成 pre_size==" + Constant.pre_size);
                int index = Constant.wm_data.size() - 1 - Constant.pre_size;
                Map apBean = Constant.wm_data.get(index);
                apBean.put("print", true);
                Constant.pre_size++;
                SaveUtils.saveObject(PrintService.this, today, Constant.wm_data);
                SpUtils.getInstance(PrintService.this).save(Constant.PARA_PRE_SIZE, Constant.pre_size);
                EventBus.getDefault().post(true);
                //3.判断是否有新订单进来，有的话继续打印
                LogUtils.WmLog("打印完成 pre_size==" + Constant.pre_size+"wm_data.size()=="+Constant.wm_data.size());
                if(Constant.wm_data.size()> Constant.pre_size) {
                    Map map = Constant.wm_data.get(Constant.wm_data.size() - Constant.pre_size-1);
                    String type= (String) map.get("type");
                    Object bean = map.get("bean");
                    if("MT".equals(type)) {
                        printData = PrintUtils.getInstance().formatPrintDataWithSize((MtOrderDetail) bean, null, null, PrintService.this, false);
                        //printYunWMData = YunPrintUtils.formatYunPrintDataWM((MtOrderDetail) bean, null, null, PrintService.this, false);
                        mtWmLog((MtOrderDetail) bean);
                    }
                    if("ELM".equals(type)) {
                        printData = PrintUtils.getInstance().formatPrintDataWithSize(null, (ElmOrderBean) bean, null, PrintService.this, false);
                        //printYunWMData = YunPrintUtils.formatYunPrintDataWM(null, (ElmOrderBean) bean, null, PrintService.this, false);
                        elmWmLog((ElmOrderBean) bean);
                    }
                    if("NEW_ELM".equals(type)) {
                        printData = PrintUtils.getInstance().formatPrintDataWithSize(null, null, (NewElmOrderBean) bean, PrintService.this, false);
                        //printYunWMData = YunPrintUtils.formatYunPrintDataWM(null, null, (NewElmOrderBean) bean, PrintService.this, false);
                        newElmWmLog((NewElmOrderBean) bean);
                    }
                    startPrint(printData);
                }else {
                    Constant.isPrinting=false;
                    checkNotPrintOrOutLimit();
                }
            }
        }

        @Override
        public void onReturnString(String result) throws RemoteException {

        }

        @Override
        public void onRaiseException(int code, String msg) throws RemoteException {

        }
    };
    private List<YunPrintGroupBean> oneByOneYunList;

    private String print_appler_order_no = "";
    private String applet_unionid = "";
    private String print_appler_order_no_bt = "";
    private List<WmPintData> printData;
    private Map printYunWMData;
    private List<WmPintData> yunPrintData;
    private List<WmPintData> btprintData;
    private AppLetReceiveBean evenAppLetBean;
    private MediaPlayer mediaPlayer;
    private int yun_print_index;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            LogUtils.Log("PrintService中的handler:"+handler);
            switch (msg.what) {
                case Constant.WAIT_BLUETOOTH_OPEN://等待蓝牙打开
//                    BTPrintUtils.getInstance().connectBtPrinter(PrintService.this, handler);
                    BTPrintUtils.getInstance().connectBtPrinterTest(mService, PrintService.this, handler);
                    break;
                case Constant.MESSAGE_STATE_CHANGE:
                    LogUtils.e(TAG,"收到了连接状态变更的消息");
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            LogUtils.e(TAG, "已连接");
//                            Toast.makeText(PrintService.this, "蓝牙已连接", Toast.LENGTH_SHORT).show();
                            if(appletConnectRequire) {
                                appletConnectRequire=false;
                                PrintByBluetoothApplet();
                            }else {
                                PrintByBluetooth();
                            }
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            LogUtils.e(TAG, "蓝牙正在连接");
//                            Toast.makeText(PrintService.this, "蓝牙正在连接", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                            break;
                        case BluetoothService.STATE_NONE:
                            LogUtils.e(TAG, "蓝牙无连接，请重新配置蓝牙打印机");
//                            Toast.makeText(PrintService.this, "无连接", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case Constant.ONE_BY_ONE_YUN_PRINT:
                   /* yun_print_index++;
                    if(yun_print_index< oneByOneYunList.size()) {
                        startAppletYunPrint();
                    }else {
                        yun_print_index=0;
                    }*/
                    break;
            }
        }
    };
    private BluetoothService mService;

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        PrintUtils.getInstance().initService(this, connService);
        mService=BluetoothService.getInstance();
        mService.setHandler(handler);
        SavedPrinter device= (SavedPrinter) SaveUtils.getObject(this, Constant.BT_PRINTER);
        if(device!=null&&mService.getState()!=BluetoothService.STATE_CONNECTED) {
            BTPrintUtils.getInstance().connectBtPrinterTest(mService,this,handler);
        }
        //检测是否还有未打印的小程序订单和未请求的超限订单：
        checkNotPrintOrOutLimit();
        super.onCreate();
    }

    public PrintService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.Log("PrintService  onStartCommond()");
        //启用前台服务，主要是startForeground()
        Notification.Builder builder=new Notification.Builder(this)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("乐推微门店派运行中")
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis());
        intent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }
        notification.flags=Notification.FLAG_SHOW_LIGHTS;
        startForeground(1, notification);
        return START_STICKY;
    }

    /**
     * 美团
     */
    @Subscribe
    public void onEventMainThread(MtOrderDetail mtOrderDetail) {
        LogUtils.Log("services中接到了recivier传递的订单");
        playBeep(1);
        LogUtils.e(TAG,"数组长度："+Constant.wm_data.size() +  "   preSize:"+Constant.pre_size+"  是否在打印中："+Constant.isPrinting);
        LogUtils.WmLog("services中接到了recivier传递的美团订单   数组长度："+Constant.wm_data.size() +  "   preSize:"+Constant.pre_size+"  是否在打印中："+Constant.isPrinting);
        if(Constant.wm_data.size()> Constant.pre_size&&!Constant.isPrinting) {
            printData = PrintUtils.getInstance().formatPrintDataWithSize(mtOrderDetail, null, null, PrintService.this, false);
            //printYunWMData = YunPrintUtils.formatYunPrintDataWM(mtOrderDetail, null, null, PrintService.this, false);
            mtWmLog(mtOrderDetail);
            startPrint(printData);
        }
        if(Constant.bt_wm_data.size()> Constant.bt_pre_size&&!Constant.bt_isPrinting) {
            btprintData = PrintUtils.getInstance().formatPrintDataWithSize(mtOrderDetail, null, null, PrintService.this, true);
            PrintByBluetooth();
        }
        printYunWMData = YunPrintUtils.formatYunPrintDataWM(mtOrderDetail, null, null, PrintService.this, false);
        startYunPrint();
    }

    private void mtWmLog(MtOrderDetail mtOrderDetail) {
        String daySeq = mtOrderDetail.getKey().getDaySeq();
        long orderId = mtOrderDetail.getKey().getOrderId();
        LogUtils.WmLog("开始打印：美团"+daySeq+"号订单 orderId为:"+orderId);
    }

    /**
     * elm
     */
    @Subscribe
    public void onEventMainThread(ElmOrderBean elmBean) {
        LogUtils.Log("services中接到了recivier传递的订单");
        playBeep(2);
        LogUtils.WmLog("services中接到了recivier传递的饿了么订单   数组长度："+Constant.wm_data.size() +  "   preSize:"+Constant.pre_size+"  是否在打印中："+Constant.isPrinting);

        if(Constant.wm_data.size()> Constant.pre_size&&!Constant.isPrinting) {
            printData = PrintUtils.getInstance().formatPrintDataWithSize(null, elmBean, null, PrintService.this, false);
            //printYunWMData = YunPrintUtils.formatYunPrintDataWM(null, elmBean, null, PrintService.this, false);
            elmWmLog(elmBean);
            startPrint(printData);
        }
        if(Constant.bt_wm_data.size()> Constant.bt_pre_size&&!Constant.bt_isPrinting) {
            btprintData = PrintUtils.getInstance().formatPrintDataWithSize(null, elmBean, null, PrintService.this, true);
            PrintByBluetooth();
        }
        printYunWMData = YunPrintUtils.formatYunPrintDataWM(null, elmBean, null, PrintService.this, false);
        startYunPrint();
    }

    private void elmWmLog(ElmOrderBean elmBean) {
        String order_id = elmBean.getData().getOrder_id();
        int restaurant_number = elmBean.getData().getRestaurant_number();
        LogUtils.WmLog("开始打印：饿了么"+restaurant_number+"号订单 orderId为:"+order_id);
    }

    /**
     * 新的elm
     */
    @Subscribe
    public void onEventMainThread(NewElmOrderBean newElmOrderBean) {
        LogUtils.Log("services中接到了recivier传递的订单");
        playBeep(2);
        LogUtils.Log("wm_data.size()==" + Constant.wm_data.size());
        LogUtils.Log("pre_size=="+Constant.pre_size);
        LogUtils.Log("isPrinting=="+Constant.isPrinting);

        LogUtils.WmLog("services中接到了recivier传递的新的elm订单  数组长度："+Constant.wm_data.size() +  "   preSize:"+Constant.pre_size+"  是否在打印中："+Constant.isPrinting);
        if(Constant.wm_data.size()> Constant.pre_size&&!Constant.isPrinting) {
            printData = PrintUtils.getInstance().formatPrintDataWithSize(null, null, newElmOrderBean, PrintService.this, false);
            //printYunWMData = YunPrintUtils.formatYunPrintDataWM(null, null, newElmOrderBean, PrintService.this, false);
            newElmWmLog(newElmOrderBean);
            startPrint(printData);
        }
        if(Constant.bt_wm_data.size()> Constant.bt_pre_size&&!Constant.bt_isPrinting) {
            btprintData = PrintUtils.getInstance().formatPrintDataWithSize(null,null,newElmOrderBean,PrintService.this,true);
            PrintByBluetooth();
        }
        printYunWMData = YunPrintUtils.formatYunPrintDataWM(null, null, newElmOrderBean, PrintService.this, false);
        startYunPrint();
    }

    private void newElmWmLog(NewElmOrderBean newElmOrderBean) {
        int daySn = newElmOrderBean.getMessage().getDaySn();
        String id = newElmOrderBean.getMessage().getId();
        LogUtils.WmLog("开始打印：饿了么"+daySn+"号订单 orderId为:"+id);
    }

    @Subscribe
    public void onEventMainThread(AppLetReceiveBean appLetReceiveBean) {
        LogUtils.e(TAG,"services中接到了recivier传递的小程序订单 是否正在打印："+Constant.isPrinting);
        playBeep(4);
        //1.判断此刻是否有外卖单正在打印
        //2.从数据集合里取数据，格式化数据然后打印：打印的时候考虑到外卖不能冲突
        //3.判断是否正在打印：如果在打印则等待此联打印完成后再打
        LogUtils.e(TAG,"onEventMainThread() Constant.isPrinting="+Constant.isPrinting);
        if(!Constant.isPrinting){
            if(Constant.applet_data!=null&&Constant.applet_data.size()>0){
                //1.取数据
                evenAppLetBean = Constant.applet_data.get(0);
                print_appler_order_no = evenAppLetBean.getOrderNo();
                applet_unionid = evenAppLetBean.getUnionid();

                LogUtils.e(TAG,"打印的appLetBean.json="+(new Gson().toJson(evenAppLetBean)));
                List<WmPintData> data = new ArrayList<>();
                //2.格式化数据:如果需要打两联就格式化一起打印了，需要打一联则格式化一联的数据
                PrintUtils.getInstance().formatAppletPrintData(PrintService.this,data,evenAppLetBean,false);
//                yunPrintData = formatAppletYunPrintData(evenAppLetBean,PrintService.this);
                //3.开始打印
                startPrintApplet(data);
            }
        }
        LogUtils.e(TAG,"onEventMainThread() Constant.bt_isPrinting="+Constant.bt_isPrinting);
        if(!Constant.bt_isPrinting&&Constant.bt_applet_data!=null&&Constant.bt_applet_data.size()>0){
            PrintByBluetoothApplet();
        }
        startPrintYunApplet(appLetReceiveBean);
    }
    /**
     * 小程序订单的云打印
     */
    private  void  startPrintYunApplet(AppLetReceiveBean appLetReceiveBean) {
        /*oneByOneYunList = formatAppletYunPrintData(appLetReceiveBean,PrintService.this);//顾客联
        if(oneByOneYunList !=null&& oneByOneYunList.size()>0) {
            YunPrintUtils.yunPrintOneByOneNew(this, oneByOneYunList,null,"9","8",false,handler);
        }*/
    }

    private  void  startPrintApplet(List<WmPintData> printData) {
        if(iWoyouService!=null) {
            Constant.isPrinting=true;
            //PrintUtils.getInstance().printWm(iWoyouService, printData, appletPrintCallBack);
            PrintUtils.getInstance().newPosPrint(iWoyouService, printData, appletPrintCallBack);
        }
    }

    /**
     * 蓝牙打印（输入输出流）
     */
    private void PrintByBluetoothApplet() {
        AppLetReceiveBean appLetBean = Constant.bt_applet_data.get(0);
        print_appler_order_no_bt = appLetBean.getOrderNo();
        clearBTPrintData();
        PrintUtils.getInstance().formatAppletPrintData(PrintService.this,btprintData,appLetBean,true);
        if(mService!=null&&mService.getState()==BluetoothService.STATE_CONNECTED&&btprintData!=null) {
            Constant.bt_isPrinting=true;
            BTPrintUtils.getInstance().wmBtPrint(mService, btprintData, this,appletBtCallBack);
        }
        if(mService.getState()!=BluetoothService.STATE_CONNECTED) {
            appletConnectRequire=true;
            BTPrintUtils.getInstance().connectBtPrinterTest(mService,this,handler);
        }
    }
    private BluetoothService.BtPrintFinshCallBack appletBtCallBack=new BluetoothService.BtPrintFinshCallBack() {
        @Override
        public void onError() {
        }
        @Override
        public void onFinish() {
            LogUtils.e(TAG,"小程序订单蓝牙打印完成了");
            //1.记录已打印的最近24小时的小程序的数据：移除存储在内存中的小程序蓝牙数据
            saveLocalAndDelete(true);
            //3.判断是否有新订单进来，有的话继续打印
            if(Constant.bt_applet_data!=null&&Constant.bt_applet_data.size()>0) {
                //3.开始打印
                PrintByBluetoothApplet();
            }else {
                LogUtils.e(TAG,"Constant.bt_isPrinting变量标注为不打印状态");
                Constant.bt_isPrinting=false;
            }
        }
    };

    private ICallback appletPrintCallBack=new ICallback.Stub() {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {
            if(isSuccess) {
                //1.记录已打印的当天小程序的数据并移除存储在内存中的小程序本地打印机数据
                saveLocalAndDelete(false);
                //3.判断是否有未打印的订单，有的话继续打印
                if(Constant.applet_data!=null&&Constant.applet_data.size()>0) {
                    //1.取数据
                    AppLetReceiveBean appLetBean = Constant.applet_data.get(0);
                    print_appler_order_no = appLetBean.getOrderNo();
                    applet_unionid = appLetBean.getUnionid();
                    List<WmPintData> data = new ArrayList<>();
                    //2.格式化数据:如果需要打两联就格式化一起打印了，需要打一联则格式化一联的数据
                    PrintUtils.getInstance().formatAppletPrintData(PrintService.this,data,appLetBean,false);
                    //yunPrintData = formatAppletYunPrintData(appLetBean,PrintService.this);
                    //3.开始打印
                    startPrintApplet(data);
                }else if(Constant.applet_out_limit_data!=null&&Constant.applet_out_limit_data.size()>0){
                    AppLetReceiveBean appLetReceiveBean = Constant.applet_out_limit_data.get(0);
                    Utils.getAppletDataFromNet(appLetReceiveBean,context);
                }else{
                    Constant.isPrinting=false;
                }
            }
        }

        @Override
        public void onReturnString(String result) throws RemoteException {

        }

        @Override
        public void onRaiseException(int code, String msg) throws RemoteException {

        }
    };

    private void saveLocalAndDelete(boolean isBt) {
        if(isBt){//处理蓝牙数据
            LogUtils.e(TAG,"saveLocalAndDelete() 蓝牙数据的order_no="+print_appler_order_no_bt);
            /*Iterator it = Constant.bt_applet_data.iterator();
            while(it.hasNext()){
                AppLetReceiveBean next = (AppLetReceiveBean) it.next();
                LogUtils.e(TAG,"next.getOrderNo()="+next.getOrderNo());
                if(print_appler_order_no_bt.equals(next.getOrderNo())){
                    LogUtils.e(TAG,"print_appler_order_no_bt="+print_appler_order_no_bt);
                    it.remove();
                }
            }*/
            if(Constant.bt_applet_data!=null&&Constant.bt_applet_data.size()>0){
                for(int i=0;i<Constant.bt_applet_data.size();i++){
                    LogUtils.e(TAG,"存储蓝牙打印数据 "+i+"="+Constant.bt_applet_data.get(i).getOrderNo());
                    if(print_appler_order_no_bt.equals(Constant.bt_applet_data.get(i).getOrderNo())){
                        Constant.bt_applet_data.remove(i);
                        break;
                    }
                }
            }
        }else{//处理本地打印机数据
            String has_print_applet = SpUtils.getInstance(PrintService.this).getString("has_print_applet", "");
            Gson gson = new Gson();
            if(!"".equals(has_print_applet)){
                LogUtils.e(TAG,"saveLocalAndDelete() 存储的有已经打印的订单...");
                SaveAppletOrderNoBean saveAppletOrderNoBean = gson.fromJson(has_print_applet, SaveAppletOrderNoBean.class);
                List<SaveAppletOrderNoBean.MsgBean.SaveDataBean> saveData = saveAppletOrderNoBean.getMsg().getSaveData();
                Iterator it = saveData.iterator();
                while(it.hasNext()){
                    SaveAppletOrderNoBean.MsgBean.SaveDataBean next = (SaveAppletOrderNoBean.MsgBean.SaveDataBean) it.next();
                    if(Utils.getCurrentTimeMill()-next.getTime() > 86400000){
                        it.remove();
                    }
                }
                SaveAppletOrderNoBean.MsgBean.SaveDataBean saveBean = new SaveAppletOrderNoBean.MsgBean.SaveDataBean(print_appler_order_no,Utils.getCurrentTimeMill(),applet_unionid);
                saveAppletOrderNoBean.getMsg().getSaveData().add(saveBean);
                SpUtils.getInstance(PrintService.this).save("has_print_applet",gson.toJson(saveAppletOrderNoBean));
                Constant.applet_data.remove(0);
            }else{
                LogUtils.e(TAG,"saveLocalAndDelete() 未存储已打印的订单...");
                SaveAppletOrderNoBean saveAppletOrderNoBean = new SaveAppletOrderNoBean();
                saveAppletOrderNoBean.setMsg(new SaveAppletOrderNoBean.MsgBean());
                SaveAppletOrderNoBean.MsgBean.SaveDataBean saveBean = new SaveAppletOrderNoBean.MsgBean.SaveDataBean(print_appler_order_no,Utils.getCurrentTimeMill(),applet_unionid);
                List<SaveAppletOrderNoBean.MsgBean.SaveDataBean> list = new ArrayList<>();
                list.add(saveBean);
                saveAppletOrderNoBean.getMsg().setSaveData(list);
                SpUtils.getInstance(PrintService.this).save("has_print_applet",gson.toJson(saveAppletOrderNoBean));
                Constant.applet_data.remove(0);
            }
        }
    }

    private  void  startYunPrint() {
        if(printYunWMData!=null&&printYunWMData.size()>0) {
            YunPrintUtils.formateWMOrderData(context,printYunWMData);
        }

    }
    private  void  startPrint(List<WmPintData> printData) {
        if(iWoyouService!=null) {
            Constant.isPrinting=true;
            PrintUtils.getInstance().printWm(iWoyouService, printData, printCallBack);
        }
    }

    //检查是否有未打印的小程序订单或者超限订单
    private void checkNotPrintOrOutLimit() {
        LogUtils.e(TAG,"检查本地或者超限订单数据....checkNotPrintOrOutLimit()");
        if((Constant.applet_data!=null&&Constant.applet_data.size()>0)||(Constant.bt_applet_data!=null&&Constant.bt_applet_data.size()>0)){
            LogUtils.e(TAG,"有未打印的本地数据");
            if(Constant.applet_data!=null&&Constant.applet_data.size()>0){
                //1.取数据
                AppLetReceiveBean appLetBean = Constant.applet_data.get(0);
                print_appler_order_no = appLetBean.getOrderNo();
                applet_unionid = appLetBean.getUnionid();
                List<WmPintData> data = new ArrayList<>();

                //2.格式化数据:如果需要打两联就格式化一起打印了，需要打一联则格式化一联的数据
                PrintUtils.getInstance().formatAppletPrintData(PrintService.this,data,appLetBean,false);
                //yunPrintData = formatAppletYunPrintData(appLetBean,PrintService.this);
                //3.开始打印
                startPrintApplet(data);
            }
            if(Constant.bt_applet_data!=null&&Constant.bt_applet_data.size()>0){
                if(!Constant.bt_isPrinting){
                    PrintByBluetoothApplet();
                }
            }
        }else if(Constant.applet_out_limit_data!=null&&Constant.applet_out_limit_data.size()>0){
            LogUtils.e(TAG,"有未打印的超限订单数据");
            AppLetReceiveBean appLetReceiveBean = Constant.applet_out_limit_data.get(0);
            Utils.getAppletDataFromNet(appLetReceiveBean,context);
        }
        LogUtils.e(TAG,"checkNotPrintOrOutLimit()->bottom");
    }

    private void clearBTPrintData() {
        if(btprintData!=null){
            btprintData.clear();
        }else{
            btprintData = new ArrayList<>();
        }
    }

    /**
     * 蓝牙打印（输入输出流）
     */
    private void PrintByBluetooth() {

        if(mService!=null&&mService.getState()==BluetoothService.STATE_CONNECTED&&btprintData!=null) {
            Constant.bt_isPrinting=true;
            BTPrintUtils.getInstance().wmBtPrint(mService, btprintData, this,btCallBack);
        }

        if(mService!=null&&mService.getState()!=BluetoothService.STATE_CONNECTED) {
            BTPrintUtils.getInstance().connectBtPrinterTest(mService,this,handler);
        }
    }

    /**
     * 播放提示音
     */
    private void playBeep(final int which) {

        AudioManager audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_VIBRATE);
        new Thread(){
            public void run(){
                if(mediaPlayer!=null&&mediaPlayer.isPlaying()) {
                    /*mediaPlayer.stop();
                    mediaPlayer.reset();*/
                }else {
                    switch (which) {
                        case 1 ://美团
                            mediaPlayer= MediaPlayer.create(PrintService.this, R.raw.mt_new_order);
                            break;
                        case 2 ://饿了么
                            mediaPlayer= MediaPlayer.create(PrintService.this, R.raw.elm_new_order);
                            break;
                        case 3://百度
                            mediaPlayer= MediaPlayer.create(PrintService.this, R.raw.bd_new_order);
                            break;
                        case 4://小程序
                            mediaPlayer= MediaPlayer.create(PrintService.this, R.raw.applet_order_comming_voice);
                            break;
                    }
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setVolume(1.0f,1.0f);
                    mediaPlayer.start();
                }
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        unbindService(connService);
        EventBus.getDefault().unregister(this);
        Intent intent=new Intent(this,PrintService.class);
        startService(intent);
        super.onDestroy();
    }
}
