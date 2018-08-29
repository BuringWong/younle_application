package com.yongle.letuiweipad.pagers;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.basepager.BasePager;
import com.yongle.letuiweipad.pagers.scalepricetag.BpFragment;
import com.yongle.letuiweipad.pagers.scalepricetag.ScaleLeftFragment;
import com.yongle.letuiweipad.utils.LogUtils;

import butterknife.BindView;

/**
 * Created by 我是奋斗 on 2016/5/11.
 * 微信/e-mail:tt090423@126.com
 */
public class PriceTagPager extends BasePager implements View.OnClickListener {

    private View totalView;
    @BindView(R.id.fl_left)
    FrameLayout fl_left;
    @BindView(R.id.fl_right)
    FrameLayout fl_right;
    private Fragment targetFragment;
    private GoodsFragment goodsFragment;

    public PriceTagPager() {
        super();
    }

    @Override
    public View initView() {
        totalView = View.inflate(mActivity, R.layout.scale_pricetag_layout,null);
        return totalView;
    }

    private static final String TAG = "PriceTagPager";
    @Override
    public void initData(int position) {
        LogUtils.e(TAG,"postion="+position);
        if(position==4) {
            LogUtils.e(TAG,"称重打标签");
        }else if(position==5) {
            LogUtils.e(TAG,"标品打标签");
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(position==4) {
            targetFragment =new ScaleLeftFragment();
            if(goodsFragment==null) {
                goodsFragment=new GoodsFragment();
                goodsFragment.setFilterWeigh(true);
                ft.replace(R.id.fl_right, goodsFragment);
            }else {
                goodsFragment.setFilterWeigh(true);
                goodsFragment.refreshGoods(-1);
            }
        }else {
            if(goodsFragment==null) {
                goodsFragment=new GoodsFragment();
                goodsFragment.setFilterWeigh(false);
                ft.replace(R.id.fl_right, goodsFragment);
            }else {
                goodsFragment.setFilterWeigh(false);
                goodsFragment.refreshGoods(-1);
            }
            targetFragment =new BpFragment();

        }
        ft.replace(R.id.fl_left, targetFragment);

        ft.commitAllowingStateLoss();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        targetFragment.onHiddenChanged(hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {

    }
}