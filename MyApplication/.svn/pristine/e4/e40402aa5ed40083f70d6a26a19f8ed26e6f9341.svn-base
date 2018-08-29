package com.younle.younle624.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tencent.tencentmap.mapsdk.map.MapView;

/**
 * Created by 我是奋斗 on 2016/5/17.
 * 微信/e-mail:tt090423@126.com
 */
public class SelfMapView extends MapView {
    public SelfMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP :
                if(onDragEvent!=null) {
                    onDragEvent.doSomeThing();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    private OnDragEvent onDragEvent;
    public interface OnDragEvent{
        public void doSomeThing();
    }

    public void setOnDragEvent(OnDragEvent onDragEvent) {
        this.onDragEvent = onDragEvent;
    }
}
