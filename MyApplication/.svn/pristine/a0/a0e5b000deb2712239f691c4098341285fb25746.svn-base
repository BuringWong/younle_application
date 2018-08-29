package com.younle.younle624.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.younle.younle624.myapplication.utils.ClicKUtils;

/**
 * Created by 我是奋斗 on 2016/6/22.
 * 微信/e-mail:tt090423@126.com
 *
 * 点击空白处可重新加载的linearlayout
 */
public class SelfLinearLayout extends LinearLayout {
    ClickToReload clickToReload;
    public SelfLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                if(ClicKUtils.isFastDoubleClick()) {
                    return true;
                }else {
                    if(clickToReload!=null) {
                        clickToReload.ClickToReload();
                    }
                }
                break;
        }
        return true;
    }

    public void setClickToReload(ClickToReload clickToReload) {
        this.clickToReload = clickToReload;
    }

    public  interface ClickToReload{
        void ClickToReload();
    }
}
