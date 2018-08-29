package com.younle.younle624.myapplication.activity.regist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.MainActivity;

public class RegistSuccessActivity extends Activity {
    private static final int TOMAIN = 1;
    private Handler handler = new Handler(){
    public void handleMessage(Message msg){
        switch (msg.what) {
            case TOMAIN :
                RegistSuccessActivity.this.startActivity(new Intent(RegistSuccessActivity.this,MainActivity.class));
                break;
        }
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_success);
        handler.sendEmptyMessageDelayed(TOMAIN,3000);

    }

    /**
     * 注册成功后，登陆时back不可使
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       switch (keyCode) {
           case KeyEvent.KEYCODE_BACK :
                return true;
       }
        return super.onKeyDown(keyCode, event);
    }
}
