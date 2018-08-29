package com.younle.younle624.myapplication.myservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.application.MyApplication;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.SavedFailOrder;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.MD5;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by bert_dong on 2016/8/11 0011.
 * 邮箱：18701038771@163.com
 *
 *  失败订单轮询的 Service
 */
public class FailOrderService extends Service {

    private String TAG = "FailOrderService";
    private static final int HANDLER_DELAY_SEND = 1;
    private long delayTime = 300000;//毫秒数  60000测试用
    private List<SavedFailOrder> listFail;
    private DbManager db;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_DELAY_SEND://执行延时操作
                    LogUtils.e(TAG,"延迟发送请求操作正在进行中...");
                    //如果数据库有数据：发网络请求：有返回，下一条数据继续请求，没有返回当前数据继续发请求;数据库没数据：则停止发请求
                    try {
                        listFail = db.findAll(SavedFailOrder.class);
                        if (listFail != null){
                            LogUtils.e(TAG,"listFail.size()"+listFail.size());
                            if (listFail.size() <= 0){
                                LogUtils.e(TAG, "listFail.size()<=0服务停止...");
                                handler.removeCallbacksAndMessages(null);
                                //停止自己
                                LogUtils.e(TAG,"handler-listFail.size()=<0-stopSelf");
                                stopSelf();
                            }else{
                                requireNet();
                            }
                        }else{
                            //停止自己
                            LogUtils.e(TAG,"handler-listFail==null-stopSelf");
                            stopSelf();
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                break;
            }
        }
    };

    /**
     * 请求网络：发送订单失败信息
     */
    private void requireNet() {
        List<SavedFailOrder> listFailUpdate = new ArrayList<>();
        //每次最大同步100条：返回成功就删除前100条
        if(listFail.size() >100){
            for(int i=0;i<100;i++){
                listFailUpdate.add(listFail.get(i));
            }
        }else{
            for(int j=0;j<listFail.size();j++){
                listFailUpdate.add(listFail.get(j));
            }
        }
        String listFails = new Gson().toJson(listFailUpdate);
        LogUtils.e(TAG,"listFails="+listFails);
        LogUtils.e(TAG,"设备id="+Constant.DEVICE_IMEI);
        LogUtils.e(TAG,"troken="+Constant.ACCESS_TOKEN);
        LogUtils.e(TAG, "账户account=" + Constant.USER_ACCOUNT);
        LogUtils.e(TAG, "账户password=" + Constant.PASSWORD);
        //获取时间戳 1473410972494并截取前10位
        String currentTime = Utils.getCurrentTime();
        //获取token
        String token = Utils.getToken(currentTime,this);

        OkHttpUtils.post()
                .url(UrlConstance.FAIL_ORDER_SUBMIT)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams("order", listFails)
                .addParams("deviceid",Constant.DEVICE_IMEI)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {//延迟一定时间继续请求
                        LogUtils.e(TAG, "onError()=" + e);
                        //没有返回值继续请求剩余数据库的失败订单
                        getRandomTime();
                        handler.sendEmptyMessageDelayed(HANDLER_DELAY_SEND, delayTime);
                    }

                    @Override
                    public void onResponse(String response) {//只要有返回就是成功
                        LogUtils.e(TAG, "onResponse():" + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int isSuccess = jsonObject.getInt("code");

                            if (200 == isSuccess) {
                                LogUtils.e(TAG, "发送订单失败信息返回的token:" + Constant.ACCESS_TOKEN);
                                //cancleFailOrder();
                                LogUtils.e(TAG, "删除之前一百条数据");
                                //删除之前一百条数据
                                List<SavedFailOrder> failAllLists = db.findAll(SavedFailOrder.class);

                                if (failAllLists != null){
                                    if(failAllLists.size() > 0 ){
                                        if(failAllLists.size() >100){
                                            for(int k=0;k<100;k++){
                                                failAllLists.remove(0);
                                            }
                                            db.dropTable(SavedFailOrder.class);
                                            LogUtils.e(TAG, "删除以后failAllLists.size():" + failAllLists.size());
                                            for(int m=0;m<failAllLists.size();m++){
                                                db.save(failAllLists.get(m));
                                            }
                                        }else{
                                            db.dropTable(SavedFailOrder.class);
                                            LogUtils.e(TAG, "list.size()<100");
                                            stopSelf();
                                            return;
                                        }
                                    }
                                }
                            }else if(404 == isSuccess){
                                LogUtils.e(TAG,"404stopSelf");
                                SaveUtils.writeLogtoFile("service_log", "404,"+response);
                                stopSelf();
                                return;
                            }else if (3003 == isSuccess) {
                                LogUtils.e(TAG,"3003stopSelf");
                                SaveUtils.writeLogtoFile("service_log", "3003," + response);
                                stopSelf();
                                return;
                            }

                            //有返回值继续请求剩余数据库的失败订单
                            getRandomTime();
                            LogUtils.e(TAG, "继续发送handler");
                            handler.sendEmptyMessageDelayed(HANDLER_DELAY_SEND, delayTime);
                        } catch (Exception e) {
                            LogUtils.e(TAG,"Exception e"+e);
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 失败订单撤销接口
     */
    private void cancleFailOrder() {
        /*LogUtils.e(TAG,"调用撤销接口...");
        LogUtils.e(TAG,Constant.PARAMS_NAME_POSTOKEN+":"+Constant.ACCESS_TOKEN);
        LogUtils.e(TAG,Constant.PARAMS_NAME_USERACCOUNT+":"+Constant.USER_ACCOUNT);
        LogUtils.e(TAG,Constant.PARAMS_NAME_IMEI+":"+Constant.DEVICE_IMEI);
        LogUtils.e(TAG,Constant.PARAMS_NAME_DEVICENAME+":."+Constant.DEVICE_NAME);
        LogUtils.e(TAG,Constant.PARAMS_NAME_MODEL+":"+Constant.DEVICE_MODEL);
        LogUtils.e(TAG,"deviceid:"+Constant.DEVICE_IMEI);
        LogUtils.e(TAG,"avd_id:"+Constant.ADV_ID);

        //获取时间戳 1473410972494并截取前10位
        String currentTime = Utils.getCurrentTime();
        //获取token
        String token = Utils.getToken(currentTime,this);
        OkHttpUtils.post()
                .url(UrlConstance.PAY_WX_CANCEL)
                .addParams(Constant.PARAMS_NAME_POSTOKEN, token)
                .addParams(Constant.PARAMS_NAME_TIMEAUTH, currentTime)
                .addParams(Constant.PARAMS_NAME_USERACCOUNT, Constant.USER_ACCOUNT)
                .addParams(Constant.PARAMS_NAME_IMEI, Constant.DEVICE_IMEI)
                .addParams(Constant.PARAMS_NAME_DEVICENAME, Constant.DEVICE_NAME)
                .addParams(Constant.PARAMS_NAME_MODEL, Constant.DEVICE_MODEL)
                .addParams("userkey", MD5.md5(MD5.md5(Constant.PASSWORD)))//密码
                .addParams("deviceid", Constant.DEVICE_IMEI)
                .addParams("advid", Constant.ADV_ID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e(TAG,"撤销接口response:"+response);
                    }
                });*/
    }

    /**
     * 获取随机时间 5到10分钟请求一次（201702131158改）
     */
    private void getRandomTime() {
        delayTime = (3 + (int)(Math.random()*7))*100000;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e(TAG, "调用onCreate()服务开始...");
        initDB();
        //初始化service时候就同步一次
        LogUtils.e(TAG, "开始服务：无延迟同步一次：");
        handler.sendEmptyMessage(HANDLER_DELAY_SEND);
    }

    /**
     * 初始化DB
     */
    private void initDB() {
        if (db == null){
            MyApplication myAppinstance = MyApplication.getInstance();
            DbManager.DaoConfig daoConfig = myAppinstance.getDaoConfig();
            db = x.getDb(daoConfig);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        LogUtils.e(TAG,"FailOrderService:onDestroy()");
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


}
