package com.younle.younle624.myapplication.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.GoodBean;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.NoScrollGirdView;
import com.younle.younle624.myapplication.view.SelfImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/24.
 * 微信/e-mail:tt090423@126.com
 *
 * 消费明细的adapter
 */
public class ResumeDetailAdapter extends BaseAdapter {

    private String TAG = "ResumeDetailAdapter";
    private  SelfImageView iv_transfer;
    private  TextView tv_total_money;
    private Activity context;
    private int dataFrom =-1;
    private UnPayDetailsBean orderBean;
    private List<UnPayDetailsBean.MsgBean.OrderGoodsBean> goodList;
    private Handler handler;
    private boolean isShowMAbtn=false;
    private double kindsNum;
    private boolean containMember;

    public ResumeDetailAdapter(Activity context, TextView tv_total_money) {
        this.context=context;
        this.tv_total_money=tv_total_money;
    }

    public ResumeDetailAdapter(Activity context, TextView tv_total_money, SelfImageView iv_transfer,Handler handler,double kindsNum) {
        this.context=context;
        this.tv_total_money=tv_total_money;
        this.iv_transfer=iv_transfer;
        this.handler=handler;
        this.kindsNum = kindsNum;
    }

    public void setIsShowMAbtn(boolean isShowMAbtn) {
        this.isShowMAbtn=isShowMAbtn;
    }

    public void setData(UnPayDetailsBean orderBean) {
        this.orderBean = orderBean;
        goodList = orderBean.getMsg().getOrder_goods();
    }

    public void setDataFrom(int dataFrom) {
        this.dataFrom =dataFrom;
    }

    @Override
    public int getCount() {
        return goodList.size();
    }

    @Override
    public Object getItem(int position) {
        return 1000;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null) {
            holder=new ViewHolder();
            convertView=View.inflate(context,R.layout.order_detail_item,null);
            holder.tv_smallname= (TextView) convertView.findViewById(R.id.tv_smallname);
            holder.tv_price= (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_count= (TextView) convertView.findViewById(R.id.tv_count);
            holder.tv_total_account= (TextView) convertView.findViewById(R.id.tv_total_account);
            holder.btn_minus= (Button) convertView.findViewById(R.id.btn_minus);
            holder.btn_add= (Button) convertView.findViewById(R.id.btn_add);
            holder.member_price_logo= (ImageView) convertView.findViewById(R.id.member_price_logo);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        switch (dataFrom) {
            case Constant.ORDER_DETAIL :
                holder.btn_minus.setVisibility(View.GONE);
                holder.btn_add.setVisibility(View.GONE);
                break;
            case Constant.ORDER_DETAIL_ALL:
                if(isShowMAbtn){
                    holder.btn_minus.setVisibility(View.VISIBLE);
                }
                break;
        }

        final UnPayDetailsBean.MsgBean.OrderGoodsBean orderEntityBean = goodList.get(position);
        //判断是否为多规格商品
        holder.tv_smallname.setText(orderEntityBean.getGoods_name());
        //单价
        holder.tv_price.setText("￥"+Utils.keepTwoDecimal(String.valueOf(orderEntityBean.getGoods_price())));

        //单位
        if(orderEntityBean.getGoods_unit()!=null&&!"".equals(orderEntityBean.getGoods_unit())){
            holder.tv_count.setText(Utils.formatPrice(orderEntityBean.getGoods_num()) + " " + orderEntityBean.getGoods_unit());
        }else{
            holder.tv_count.setText(Utils.formatPrice(orderEntityBean.getGoods_num()));
        }
        //总价
        holder.tv_total_account.setText("￥" + Utils.keepTwoDecimal(""+orderEntityBean.getGoods_price() * orderEntityBean.getGoods_num()));

        if(orderEntityBean.getGoods_num()<=0){
            holder.btn_minus.setVisibility(View.GONE);
        }
        LogUtils.Log("containMember=="+containMember);
        if(containMember) {
            if(0==orderEntityBean.getIs_vip()) {
                holder.member_price_logo.setVisibility(View.INVISIBLE);
            }else {
                holder.member_price_logo.setVisibility(View.VISIBLE);
            }
        }else {
            holder.member_price_logo.setVisibility(View.GONE);

        }


        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String handle_is_weigh = orderEntityBean.getIs_weigh();
                double goods_num = orderEntityBean.getGoods_num();
                LogUtils.Log("即将减少的数量："+goods_num+"  当前剩余数量："+kindsNum);
                if("1".equals(handle_is_weigh)&&(kindsNum-goods_num<=0)) {//操作的是称重商品，并且当前仅剩称重商品
                    Utils.showToast(context, "订单中需至少包含一个商品，如果您想取消订单，请点击页面下方的“取消订单”");
                }else {
                    if(kindsNum>1){
                        minusGoodsNum(position);
                    }else{
                        Utils.showToast(context, "订单中需至少包含一个商品，如果您想取消订单，请点击页面下方的“取消订单”");
                    }
                }
            }
        });
        return convertView;
    }

    /**
     * 减少商品数量
     * @param position
     */
    private void minusGoodsNum(final int position) {
        int isHasRecord = 0;//默认没有减少过此商品
        final UnPayDetailsBean.MsgBean.OrderGoodsBean orderEntityBean = goodList.get(position);
        double num_minus_entity = orderEntityBean.getGoods_num();
        double tempMinus=0;
        String is_weigh = orderEntityBean.getIs_weigh();
        List<GoodBean> goodList_minus = Constant.localOrderBean.getGoodList();
        //记录本地存储数据：存起来同步到服务器
        if(goodList_minus!=null&&goodList_minus.size()>=0){
            for(int i=0;i<goodList_minus.size();i++){
                if(orderEntityBean.getId().equals(goodList_minus.get(i).getGoodsId())){
                    //判断有没有多规格
                    if(!"".equals(orderEntityBean.getSize_id())&&!"0".equals(orderEntityBean.getSize_id())){//操作的是多规格
                        //判断是否选了本次点击的多规格商品
                        if(orderEntityBean.getSize_id().equals(goodList_minus.get(i).getSize_id())){
                            double goodsNum = goodList_minus.get(i).getGoodsNum();
                            double subNum = goodList_minus.get(i).getSubNum();
                            if("0".equals(is_weigh)) {  //1.不是称重
                                if(goodsNum>0){
                                    tempMinus=1;
                                    goodList_minus.get(i).setGoodsNum(goodsNum-1);
                                    goodList_minus.get(i).setSubNum(subNum+1);
                                }
                            }else {//称重
                                if(goodsNum>0){
                                    tempMinus=goodList_minus.get(i).getGoodsNum();
                                    goodList_minus.get(i).setSubNum(goodList_minus.get(i).getGoodsNum());
                                    goodList_minus.get(i).setGoodsNum(0);
                                }
                            }
                            isHasRecord = 1;//多规格
                            break;
                        }else{
                            isHasRecord = 2;//不是多规格
                        }
                    }else{//非多规格
                        double goodsNum = goodList_minus.get(i).getGoodsNum();
                        double subNum = goodList_minus.get(i).getSubNum();
                        if("0".equals(is_weigh)) {
                            if(goodsNum>0){
                                tempMinus=1;
                                goodList_minus.get(i).setGoodsNum(goodsNum-1);
                                goodList_minus.get(i).setSubNum(subNum+1);
                            }
                        }else {
                            if(goodsNum>0){
                                tempMinus=goodList_minus.get(i).getGoodsNum();
                                goodList_minus.get(i).setSubNum(goodList_minus.get(i).getGoodsNum());
                                goodList_minus.get(i).setGoodsNum(0);
                            }
                        }
                        isHasRecord = 1;
                    }
                }
            }
            if(isHasRecord==0||isHasRecord==2){//没有多规格 或者有规格却没选过的商品
                if("0".equals(is_weigh)) {
                    tempMinus=1;
                }else {
                    tempMinus=orderEntityBean.getGoods_num();
                }
                GoodBean goodBean = new GoodBean();
//                goodBean.setGoodsNum(num_minus_entity - 1);
                goodBean.setGoodsNum(num_minus_entity - tempMinus);
                goodBean.setGoodsId(orderEntityBean.getId());
                goodBean.setSize_id(orderEntityBean.getSize_id());
                goodBean.setSubNum(tempMinus);
//                goodBean.setSubNum(1);
                goodList_minus.add(goodBean);
            }
        }else {

            if("0".equals(is_weigh)) {
                tempMinus=1;
            }else {
                tempMinus=orderEntityBean.getGoods_num();
            }
            GoodBean goodBean = new GoodBean();
//            goodBean.setGoodsNum(num_minus_entity - 1);
            goodBean.setGoodsNum(num_minus_entity - tempMinus);
            goodBean.setGoodsId(orderEntityBean.getId());
            goodBean.setSize_id(orderEntityBean.getSize_id());
//            goodBean.setSubNum(1);
            goodBean.setSubNum(tempMinus);
            ArrayList<GoodBean> goodBeanlists = new ArrayList<>();
            goodBeanlists.add(goodBean);
            Constant.localOrderBean.setGoodList(goodBeanlists);
        }
        double entity_num = orderEntityBean.getGoods_num();
        double finalNum = entity_num - tempMinus;
        if(finalNum<=0) {
            Utils.showToast(context,"此商品数量已经减少到0！");
        }
        kindsNum-=tempMinus;
        orderEntityBean.setGoods_num(finalNum);
        notifyDataSetChanged();
        handler.sendEmptyMessage(Constant.ORDER_DETAIL_CHANGE_TOTALFEE);
    }

    public void setContainMember(boolean containMember) {
        this.containMember = containMember;
    }

    class ViewHolder{
        TextView tv_bigname;
        TextView tv_smallname;
        TextView tv_price;
        TextView tv_count;
        TextView tv_total_account;
        Button btn_minus;
        Button btn_add;
        NoScrollGirdView gv_detail;
        ImageView member_price_logo;
    }
}
