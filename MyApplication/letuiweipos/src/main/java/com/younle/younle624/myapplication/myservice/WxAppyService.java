package com.younle.younle624.myapplication.myservice;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.AppLetReceiveBean;
import com.younle.younle624.myapplication.domain.WxAppletBean;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.domain.printsetting.YunPrintGroupBean;
import com.younle.younle624.myapplication.domain.waimai.WmPintData;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.printmanager.BTPrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.PrintUtils;
import com.younle.younle624.myapplication.utils.printmanager.YunPrintUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class WxAppyService extends Service {
    public int count=0;
    private static final int FLUS = 1;
    private Gson gson=new Gson();
    private String timestamp=System.currentTimeMillis()/1000+"";
    private NetWorks netWorks=new NetWorks(WxAppyService.this);
    private int timeStep=5;
    private boolean appletConnectRequire=false;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case FLUS:
                    request();
//                    handler.sendEmptyMessageDelayed(FLUS,1000*timeStep);
                    break;
                case Constant.WAIT_BLUETOOTH_OPEN://等待蓝牙打开
                    BTPrintUtils.getInstance().connectBtPrinterTest(mService, WxAppyService.this, handler);
                    break;
                case Constant.MESSAGE_STATE_CHANGE:
                    LogUtils.e(TAG,"收到了连接状态变更的消息");
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            LogUtils.e(TAG, "已连接");
                            if(appletConnectRequire) {
                                appletConnectRequire=false;
                            }else {
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
                /*case Constant.ONE_BY_ONE_BT_PRINT :
                    bt_onebyone_index++;
                    if(bt_onebyone_index<oneByOneBtList.size()) {
                        printBtMerchant();
                    }else {
                        bt_onebyone_index=0;
                    }
                    break;*/
                case Constant.ONE_BY_ONE_YUN_PRINT:
                    if(yunUnPrintData!=null&&yunUnPrintData.size()>0) {
                        String orderNo = yunUnPrintData.get(0).getOrderNo();
                        String queryNum = yunUnPrintData.get(0).getDayOrderNum();
                        LogUtils.WmLog("已打印：queryNum="+queryNum+"  orderNo="+orderNo);
                        orderNo=null;
                        queryNum=null;
                        yunUnPrintData.remove(0);
                        yunPinting=false;
                        startYunPrint();
                    }
                    break;
                case Constant.YUN_PRINT_ERROR:
                    LogUtils.e(TAG,"云打印异常");
                    if(yunUnPrintData.size()>0) {
                        AppLetReceiveBean appLetReceiveBean = yunUnPrintData.get(0);
                        List<YunPrintGroupBean> yunPrintGroupBeans = YunPrintUtils.formatAppletYunPrintData(appLetReceiveBean, WxAppyService.this,false);
                        List<YunPrintGroupBean> yunNormalGroupBeans = YunPrintUtils.formatAppletYunPrintData(appLetReceiveBean, WxAppyService.this,true);
                        yunPrint(appLetReceiveBean,yunPrintGroupBeans,yunNormalGroupBeans);
                    }
                    break;
            }
        }
    };
    /*private Map printAppletMap;
    private Map YunPrintAppletMap;
    private List<YunPrintGroupBean> oneByOneYunList;
    private List<List<PrintItemObj>> oneByOneList;
    private List<WmPintData> appletBTPrintData;
    private List<List<WmPintData>> oneByOneBtList;*/

    private List<WmPintData> tempBtData;
    private String printItems;
    private MediaPlayer mediaPlayer;
    private final int  ERROR=4;
    private BluetoothService mService;
    private  int bt_onebyone_index;


    private IWoyouService iWoyouService;
    /**
     * 服务连接
     */
    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.Log("onServiceConnected");
            iWoyouService = IWoyouService.Stub.asInterface(service);

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService = null;
        }
    };
    private String btPrintItems="";
    private boolean isPrinting =false;
    private boolean btFinished=true;
    private boolean yunFinished=true;
    private boolean yunPinting=false;
    private List<SavedPrinter> savedPrinterses;


    public WxAppyService() {
    }


    @Override
    public void onCreate() {
        LogUtils
                .e(TAG,"onCreate()");

        PrintUtils.getInstance().initService(this, connService);

        SpUtils.getInstance(WxAppyService.this).save("LAST_TIME_STAMP",timestamp);

        String savedTime=SpUtils.getInstance(this).getString("LAST_TIME_STAMP",null);
        if(savedTime!=null) {
            timestamp=savedTime;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                request();
            }
        },2000);

    }

    private static final String TAG = "WxAppyService";

    public void request(){
        LogUtils.WmLog("requestTime:"+timestamp);
        Map<String, String> params = netWorks.getPublicParams();
        params.put("advid",Constant.ADV_ID);
        params.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID);
        params.put("timestamp",timestamp);
        params.put(Constant.PARAMS_NAME_VERSIONCODE,Constant.VERSION_CODE+"");

        netWorks.Request(UrlConstance.GET_WXAPP_ORDER, params, 10000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                handler.sendEmptyMessageDelayed(FLUS,1000*timeStep);
                LogUtils.e(TAG,"轮询数据异常："+e.toString());
            }

            @Override
            public void onResonse(String response, int flag) {
                handler.sendEmptyMessageDelayed(FLUS,1000*timeStep);
                LogUtils.e(TAG,"轮询数据respoonse："+response.toString());
                praseJson(response);
            }
        });
    }
    List<AppLetReceiveBean> unPrintData=new ArrayList<>();
    List<AppLetReceiveBean> yunUnPrintData=new ArrayList<>();
    private void praseJson(String json) {
        try {
            JSONObject obj=new JSONObject(json);
            int code = obj.getInt("code");
            if(code==200) {
                String orderDetail = obj.getString("msg");
                WxAppletBean wxAppletBean = gson.fromJson(orderDetail, WxAppletBean.class);
                timestamp=wxAppletBean.getRequestTime()+"";
                LogUtils.WmLog("nextTime:"+timestamp);
                SpUtils.getInstance(WxAppyService.this).save("LAST_TIME_STAMP",timestamp);
                timeStep=wxAppletBean.getStep();
                if(wxAppletBean.getOrderList()!=null&&wxAppletBean.getOrderList().size()>0) {
                    playBeep(5);
                    EventBus.getDefault().post("manager_applet_come");
                    //本地打印
                    unPrintData.addAll(wxAppletBean.getOrderList());
                    yunUnPrintData.addAll(wxAppletBean.getOrderList());
                    startLocalPrint();

                    startYunPrint();

                    //云打印
                    /*for (int i = 0; i < wxAppletBean.getOrderList().size(); i++) {
                        synchronized (this){
                            initAppletPtintData(wxAppletBean.getOrderList().get(i));
                        }
                    }*/
                }
            }
        } catch (JSONException e) {
            LogUtils.e(TAG,"数据解析异常："+e.toString());

        }

    }

    private  void  startPrintApplet() {
        if(iWoyouService!=null) {
            isPrinting=true;
            if(data==null||data.size()<=0) {
                locPrintFinish();
                return;
            }
            //PrintUtils.getInstance().printWm(iWoyouService, printData, appletPrintCallBack);
            PrintUtils.getInstance().newPosPrint(iWoyouService, data, new ICallback.Stub() {
                @Override
                public void onRunResult(boolean isSuccess) throws RemoteException {
                    LogUtils.e(TAG,"onRunResult()"+isSuccess);
                    if(isSuccess) {

                        locPrintFinish();
                    }
                }

                @Override
                public void onReturnString(String result) throws RemoteException {

                }

                @Override
                public void onRaiseException(int code, String msg) throws RemoteException {

                }
            });
        }
    }


    private void locPrintFinish() {
        isPrinting =false;
        unPrintData.remove(0);
        LogUtils.e(TAG,"本地打印完成，待打印："+unPrintData.size());
        startLocalPrint();
    }
    private List<WmPintData> data=new ArrayList<>();

    private void startLocalPrint() {
        if(isPrinting) {
            return;
        }
        LogUtils.e(TAG,"unPrintData=="+unPrintData.size());
        if(unPrintData.size()>0) {
            try {
                PrintUtils.getInstance().formatAppletPrintData(this,data,unPrintData.get(0),false);
                startPrintApplet();
            } catch (Exception e) {
                LogUtils.e(TAG,"startLocalPrint():e="+e.toString());
                e.printStackTrace();
            }
        }
    }
    private void startYunPrint() {
        LogUtils.e(TAG,"startYunPrint() 待打印队列： "+yunUnPrintData.size());
        if(yunPinting) {
            return;
        }
        if(yunUnPrintData.size()>0) {
            AppLetReceiveBean appLetReceiveBean = yunUnPrintData.get(0);
            List<YunPrintGroupBean> yunPrintGroupBeans = YunPrintUtils.formatAppletYunPrintData(appLetReceiveBean, this,false);
            List<YunPrintGroupBean> yunNormalGroupBeans = YunPrintUtils.formatAppletYunPrintData(appLetReceiveBean, this,true);
            yunPrint(appLetReceiveBean,yunPrintGroupBeans,yunNormalGroupBeans);
        }
    }

    /**
     * 开始云打印
     * @param oneByOneYunList
     */
    private void yunPrint(AppLetReceiveBean appLetBean, List<YunPrintGroupBean> oneByOneYunList,List<YunPrintGroupBean> yunNormalList) {
        LogUtils.e(TAG,"yunPrint()");
        if(oneByOneYunList.size()>0||yunNormalList.size()>0) {
            savedPrinterses = (List<SavedPrinter>) SaveUtils.getObject(WxAppyService.this, Constant.YUN_PRINTERS);
            if(savedPrinterses !=null && savedPrinterses.size()>0){
                yunPinting=true;
                if(appLetBean.getOrderType()==4) {
                    YunPrintUtils.yunPrintOneByOneNew(this, savedPrinterses,oneByOneYunList,yunNormalList,"a","b",false,handler);
                }else {
                    YunPrintUtils.yunPrintOneByOneNew(this, savedPrinterses,oneByOneYunList,yunNormalList,"9","8",false,handler);
                }
            }
        }
    }
    /**
     * 播放提示音
     */
    private void playBeep(int which) {
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()) {
         /*   mediaPlayer.stop();
            mediaPlayer.reset();*/
        }else{
            if(which==ERROR) {
                mediaPlayer= MediaPlayer.create(this, R.raw.paper_error);
                mediaPlayer.setVolume(1.0f,1.0f);
                mediaPlayer.start();
                return;
            }
            switch (which) {
                case 1 ://美团
                    mediaPlayer= MediaPlayer.create(this, R.raw.mt_new_order);
                    break;
                case 2 ://饿了么
                    mediaPlayer= MediaPlayer.create(this, R.raw.elm_new_order);
                    break;
                case 3://百度
                    mediaPlayer= MediaPlayer.create(this, R.raw.bd_new_order);
                    break;
                case 5://小程序订单
                    mediaPlayer= MediaPlayer.create(this, R.raw.applet_order_comming_voice);
                    break;
            }
            mediaPlayer.setVolume(1.0f,1.0f);
            mediaPlayer.start();
        }
    }


    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        unbindService(connService);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
