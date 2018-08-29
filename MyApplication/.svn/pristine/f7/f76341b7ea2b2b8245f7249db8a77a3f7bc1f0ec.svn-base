package com.younle.younle624.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.younle.younle624.myapplication.utils.scanbar.ScanGunKeyEventHelper;

/**
 * 作者：Create by 我是奋斗 on2017/4/22 15:22
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class ScanRelativeLayout extends RelativeLayout {
    public ScanListener scanListener;
    private ScanGunKeyEventHelper scanGunKeyEventHelper=new ScanGunKeyEventHelper(new ScanGunKeyEventHelper.OnScanSuccessListener() {
        @Override
        public void onScanSuccess(String barcode) {
            if(scanListener!=null) {
                scanListener.OnScanFinish(barcode);
            }
        }
    });
    public ScanRelativeLayout(Context context) {
        super(context);
    }

    public ScanRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScanRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(scanGunKeyEventHelper!=null&&scanGunKeyEventHelper.isScanGunEvent(event)) {
            scanGunKeyEventHelper.analysisKeyEvent(event);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(scanGunKeyEventHelper.isScanGunEvent(event)) {
            scanGunKeyEventHelper.analysisKeyEvent(event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
*/
    public void setScanListener(ScanListener scanListener) {
        this.scanListener = scanListener;
    }

    public interface ScanListener{
        void OnScanFinish(String barcode);
    }
}
