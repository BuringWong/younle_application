package com.yongle.letuiweipad.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FuPingService extends Service {
    private FuPingService binder;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

        }
    };
    public FuPingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder iBinder=new Binder();
        return iBinder;
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        LogUtils.Log("FuPing的轮播service");
        startLunBo();
    }

    private void startLunBo() {
        List<Long> picIds= (List<Long>) SaveUtils.getObject(this, Constant.FP_PICID);
        if(picIds!=null&&picIds.size()>0) {
            Message message = handler.obtainMessage();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
