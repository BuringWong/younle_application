package com.younle.younle624.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 *
 1. 正常的初始化显示
    1). 重写onFinishInflate(): 得到menu子View
    2). 重写onMeasure(), 得到View及其子View的宽高
    3). 重写onLayout(), 对menu子View进行重新布局
 2. 滑动打开/关闭菜单
    2.1). 重写onTouchEvent(), 返回true
    2.2). 在move时, 计算事件的偏移, 使布局做对应的偏移(子View)
    2.3). 在up时, 根据总的偏移量, 判断是打开/关闭菜单
    2.4). 菜单的平滑打开/关闭
 3. 显示列表

 4. item滑动后可能不能自动关闭/打开
    事件冲突: 当前视图与父视图
    原因: 事件被ListView拦截消费
    解决: 反拦截
    实现:
        条件 : totalDx>totalDy && totalDx>8
        反拦截: getParent().requestDisallowInterceptTouchEvent(true);
 5. 内容视图设置了点击监听时, 不能在水平滑动
     事件冲突: 当前视图与子视图
     原因: 事件被TextView拦截消费
     解决: 拦截
     实现:
        重写onInterceptTouchEvent()
        条件 : totalDx>8
        拦截: onInterceptTouchEvent()返回true
 6. 限制只能同时打开一个item
    解决: 自定义监听器
 */
public class SlideLayout extends FrameLayout {

    private View menuView;
    private int viewWidth, viewHeight,menuWidth;
    private Scroller scroller;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        scroller = new Scroller(context);
    }

    //1). 重写onFinishInflate(): 得到menu子View
    @Override
    protected void onFinishInflate() {
       super.onFinishInflate();
        menuView = getChildAt(1);
    }

    //2). 重写onMeasure(), 得到View及其子View的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        menuWidth = menuView.getMeasuredWidth();
    }


    //3). 重写onLayout(), 对menu子View进行重新布局
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        menuView.layout(viewWidth, 0, viewWidth+menuWidth, viewHeight);
    }

    /*
    2.1). 重写onTouchEvent(), 返回true
    2.2). 在move时, 计算事件的偏移, 使布局做对应的偏移(子View)
    2.3). 在up时, 根据总的偏移量, 判断是打开/关闭菜单
    2.4). 菜单的平滑打开/关闭
     */

    private int lastX,downX,downY,lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downX = lastX = eventX;
                downY = lastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE :
                int dx = eventX-lastX;
                //滚动视图
                int toScrollX = getScrollX()-dx;
                //限制范围[0,menuWidth]
                if (toScrollX < 0) {
                    toScrollX=0;
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }
//                scrollTo(toScrollX, 0);
                lastX = eventX;
                lastY = eventY;

                //条件 : totalDx>totalDy && totalDx>8
                int totalDx = Math.abs(eventX - downX);
                int totalDy = Math.abs(eventY - downY);
                if (totalDx > totalDy && totalDx > 8) {
                    scrollTo(toScrollX, 0);
                    getParent().requestDisallowInterceptTouchEvent(true);//不让父视图拦截事件
                }
                break;
            case MotionEvent.ACTION_UP :
                //得到总的偏移量
                int totalScrollX = getScrollX();
                if (totalScrollX < menuWidth / 2) {//关
                    closeMenu();
                    scrollTo(0, 0);
                } else {//开
                    openMenu();
                }
                break;
        }

        return true;
    }

    /**
     * 平滑打开菜单 -->(menuWidth,0)
     */
    public void openMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), menuWidth - getScrollX(), 0);
        invalidate();

        if (onStateChangeListener != null) {
            onStateChangeListener.onOpen(this);
        }
    }

    /**
     * 平滑关闭菜单 --->(0, 0)
     */
    public void closeMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), 0);
        invalidate();

        if (onStateChangeListener != null) {
            onStateChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

    /*
        重写onInterceptTouchEvent()
        条件 : totalDx>8
        拦截: onInterceptTouchEvent()返回true
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;

        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downX = lastX = eventX;
                downY = lastY = eventY;

                if (onStateChangeListener != null) {
                    onStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE :
                //条件 : totalDx>8
                int totalDx = Math.abs(eventX - downX);
                if (totalDx > 8) {
                    intercept = true;//一旦返回true, 后面的事件交给当前layout处理了
                }
                break;
        }

        return intercept;
    }

    public interface OnStateChangeListener {
        public void onOpen(SlideLayout layout); //当layout打开时调用
        public void onClose(SlideLayout layout);//当layout关闭时调用
        public void onDown(SlideLayout layout); //当layout被按下时调用
    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }
}
