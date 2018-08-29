package com.younle.younle624.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.utils.LogUtils;

/**
 *
 * @author markmjw
 * @date 2013-10-08
 */
public class XFooterView extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;
    public final static int STATE_NO_MORE_DATA= 3;

    private final int ROTATE_ANIM_DURATION = 180;

    private View mLayout;

    private View mProgressBar;

    private TextView mHintView;

//    private ImageView mHintImage;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private int mState = STATE_NORMAL;
    private ImageView mHintImage;

    public XFooterView(Context context) {
        super(context);
        initView(context);
    }

    public XFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mLayout = LayoutInflater.from(context).inflate(R.layout.vw_footer, null);
        mLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        addView(mLayout);

        mProgressBar = mLayout.findViewById(R.id.footer_progressbar);
        mHintView = (TextView) mLayout.findViewById(R.id.footer_hint_text);
        mHintImage = (ImageView) mLayout.findViewById(R.id.footer_arrow);

        mRotateUpAnim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    /**
     * Set footer view state
     *
     * @see #STATE_LOADING
     * @see #STATE_NORMAL
     * @see #STATE_READY
     *
     * @param state
     */
    public void setState(int state) {
        if (state == mState) return;
        switch (state) {//各种状态设置控件的显示、值
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mHintImage.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_LOADING) {
                    mHintImage.clearAnimation();
                }
                mHintView.setVisibility(GONE);
                mProgressBar.setVisibility(GONE);
                break;
            /*case STATE_READY:
                mHintImage.clearAnimation();
                mHintImage.startAnimation(mRotateUpAnim);
                mHintView.setText(R.string.footer_hint_load_ready);
                mHintView.setText("放开加载更多...");
                break;*/
            case STATE_LOADING:
                mHintImage.clearAnimation();
                mHintImage.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mHintView.setVisibility(View.VISIBLE);
                mHintView.setText("正在加载更多...");
                break;
            case STATE_NO_MORE_DATA:
                LogUtils.Log("state_no_more_data");
                mHintView.setText(R.string.footer_hint_no_more_data);
                mHintView.setText("没有更多数据了！");
                mHintImage.setVisibility(GONE);
                mProgressBar.setVisibility(GONE);
                break;
        }
        mState = state;
    }

    /**
     * Set footer view bottom margin.
     *
     * @param margin
     */
    public void setBottomMargin(int margin) {
        if (margin < 0) return;
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.bottomMargin = margin;
        mLayout.setLayoutParams(lp);
    }

    /**
     * Get footer view bottom margin.
     *
     * @return
     */
    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * normal status
     */
    public void normal() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = 0;
        mLayout.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mLayout.setLayoutParams(lp);
    }

}
