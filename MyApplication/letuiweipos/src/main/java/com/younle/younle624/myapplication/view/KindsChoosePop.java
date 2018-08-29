package com.younle.younle624.myapplication.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.adapter.ordermanager.FilterAdapter;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.GoodKinds;

import java.util.ArrayList;
import java.util.List;

public class KindsChoosePop extends PopupWindow {
    private Context mContext;
    private View view;
    private ListView lv_current_kinds;
    private List<String> data;
    private FilterAdapter adapter;

    public KindsChoosePop(Context mContext, AdapterView.OnItemClickListener itemClickListener) {
        this.mContext=mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.akpopu_layout, null);
        View puplayout = view.findViewById(R.id.ll_pop_layout);
        lv_current_kinds= (ListView) view.findViewById(R.id.lv_current_kinds);
        lv_current_kinds.setOnItemClickListener(itemClickListener);
//        getData();
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        touchDismiss();
        initPop();


    }

    /**
     * 从数据库或服务器拿数据设置到adapter中
     */
    public List<String> getData() {
        data = new ArrayList<>();
        String kind1="饮料";
        String kind2="水果";
        String kind3="快餐";
        String kind4="玩具";
        String kind5="服装";
        data.add(kind1);
        data.add(kind2);
        data.add(kind3);
        data.add(kind4);
        data.add(kind5);
        adapter=new FilterAdapter(mContext);
        adapter.setData(data);
        adapter.setDataFrom(Constant.KINDS_CHOOSE);
        lv_current_kinds.setAdapter(adapter);
        return data;
    }
    public void setData(List<GoodKinds> goodkinds) {
        adapter=new FilterAdapter(mContext);
        adapter.setDataFrom(Constant.KINDS_CHOOSE);
        adapter.setData(goodkinds);
        lv_current_kinds.setAdapter(adapter);
    }

    private void initPop() {
    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.pop_animation);
    }

    private void touchDismiss() {
        this.view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.ll_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
