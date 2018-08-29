package com.yongle.letuiweipad.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.selfinterface.OnWeighGoodChangeListener;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yongle.letuiweipad.constant.Constant.localOrderBean;
import static com.zhy.http.okhttp.log.LoggerInterceptor.TAG;

/**
 * Created by Administrator on 2017/9/13.
 */

public class GoodServiceAdapter extends BaseAdapter {
    private  Handler handler;
    private Context context;
    private List<GoodBean> data;
    private int pos_which_goods = 0;//商品的position
    private int last_pos = -1;//上一次点击位置
    private int last_localdata_pos = -1;//上一次多规格点击位置
    private boolean showMemberPrice=false;
    private boolean weighConnected=false;
    private OnWeighGoodChangeListener weighListener;

    public GoodServiceAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler=handler;
    }

    public void setData(List<GoodBean> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            convertView=View.inflate(context, R.layout.good_service_item,null);
            holder=new ViewHolder(convertView);
            holder.name= (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        GoodBean goodBean = data.get(position);
        if(goodBean.getHasStock()==1) {//有设置库存
            if(goodBean.getStock()<=0) {//售罄
                holder.al_stock.setVisibility(View.GONE);
                holder.sale_out.setVisibility(View.VISIBLE);
                holder.al_gs_item.setBackgroundResource(R.drawable.gs_item_saleout_shape);
                holder.name.setTextColor(Color.parseColor("#c0c0c0"));
                holder.goods_price.setTextColor(Color.parseColor("#c0c0c0"));
            }else {
                holder.al_stock.setVisibility(View.VISIBLE);
                holder.sale_out.setVisibility(View.GONE);
                holder.al_gs_item.setBackgroundResource(R.drawable.gs_item_shape);
                holder.name.setTextColor(Color.parseColor("#7c4c21"));
                holder.goods_price.setTextColor(Color.parseColor("#7c4c21"));
                holder.tv_stock.setText("剩余："+Utils.formatPrice(Double.valueOf(goodBean.getStock())));
                if(goodBean.getStockWarning()==1){//预警
                    holder.tv_stock_nervous.setVisibility(View.VISIBLE);
                    holder.al_stock.setBackgroundResource(R.drawable.stock_warning_shape);
                }else{
                    holder.tv_stock_nervous.setVisibility(View.GONE);
                    holder.al_stock.setBackgroundResource(R.drawable.item_bottom_shape);
                }
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.rl_price.getLayoutParams();
            layoutParams.setMargins(0,0,0,Utils.px2dip(context,2));
            holder.rl_price.setLayoutParams(layoutParams);
        }else{
            holder.al_stock.setVisibility(View.GONE);
            holder.sale_out.setVisibility(View.GONE);
            holder.al_gs_item.setBackgroundResource(R.drawable.gs_item_shape);
            holder.name.setTextColor(Color.parseColor("#7c4c21"));
            holder.goods_price.setTextColor(Color.parseColor("#7c4c21"));

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.rl_price.getLayoutParams();
            layoutParams.setMargins(0,0,0,Utils.px2dip(context,6));
            holder.rl_price.setLayoutParams(layoutParams);
        }

        double vipPrice = goodBean.getVipPrice();
        String goodsUnit = goodBean.getGoodsUnit();
        if(showMemberPrice &&vipPrice>=0) {
            //显示会员价
            holder.member_price_logo.setVisibility(View.VISIBLE);
            if(goodsUnit!=null&&!TextUtils.isEmpty(goodsUnit)) {
                holder.goods_price.setText(Utils.keepTwoDecimal(goodBean.getVipPrice()+"")+"元/" + goodsUnit);
            }else {
                holder.goods_price.setText(Utils.keepTwoDecimal(goodBean.getVipPrice()+"")+"元");
            }
        }else {
            //不显示会员价
            holder.member_price_logo.setVisibility(View.GONE);
            if(goodsUnit!=null&&!TextUtils.isEmpty(goodsUnit)) {
                holder.goods_price.setText(Utils.keepTwoDecimal(goodBean.getGoodsPrice()+"") + "/" + goodsUnit);
            }else {
                holder.goods_price.setText(Utils.keepTwoDecimal(goodBean.getGoodsPrice()+""));
            }
        }
        String goodsName = goodBean.getGoodsName();
        if(goodsName.length()>6) {
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            holder.name.setText(goodsName);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ll_name.getLayoutParams();
            if(goodsName.length()>12) {
                params.setMargins(0,0,0,0);
            }else {
                params.setMargins(0,35,0,0);
            }
            holder.ll_name.setLayoutParams(params);
        }else {
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            holder.name.setText(goodsName);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos_which_goods = position;
                String is_weigh = data.get(position).getIs_weigh();
                double stock = data.get(position).getStock();
                int hasStock = data.get(position).getHasStock();
                if(hasStock==1&&stock<=0) {
                    Utils.showToast(context,"该商品已售罄");
                    return;
                }
                if("0".equals(is_weigh)||!weighConnected) {
                    pos_which_goods = position;
                    add(position, data.get(position).getGoodsId(), localOrderBean.getGoodList(),0);
                }else {
                    if(weighListener !=null) {
                        weighListener.weigh(data.get(position),0,position);
                    }
                }
            }
        });
        return convertView;
    }

    public void setShowMemberPrice(boolean showMemberPrice) {
        LogUtils.Log("showMember:"+showMemberPrice);
        this.showMemberPrice = showMemberPrice;
    }
    public void setWigherState(boolean weighConnected) {
        LogUtils.Log("weighConnected:"+weighConnected);
        this.weighConnected = weighConnected;
    }
    public void setWeighListener(OnWeighGoodChangeListener onWeighChangeListener) {
        this.weighListener = onWeighChangeListener;
    }
    /**
     * 添加实物或者服务
     * @param position
     */
    private void add(final int position, final String goodsId, final List<GoodBean> goodList, final int tag) {
        startAdd(position, goodsId, goodList, tag);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){

                }
            }
        }).run();*/
    }

    /**
     * 先根据sizeid判断是否不为0来鉴定是否为多规格商品，
     * 1.如果不是多规格，根据goodid判断是新增还是改属性值
     * 2.如果是多规格，判断sizeid判断是新增还是改属性值
     * @param position
     * @param goodsId
     * @param goodList 已经选择的商品列表
     * @param tag
     */
    private void startAdd(int position, String goodsId, List<GoodBean> goodList, int tag) {
        LogUtils.Log("position=="+position+" goodsId="+goodsId);
        String size_id = data.get(position).getSizeId();
        if(size_id==null||TextUtils.equals("0",size_id)) {
            addNormeGood(position, goodsId, goodList,false);
        }else {
            addNormeGood(position, size_id, goodList,true);
        }
    }

    private void addNormeGood(int position, String localId, List<GoodBean> goodList,boolean isDgg) {
        if (goodList != null) {
            int size = goodList.size();
            if(isDgg) {
                for (int i = 0; i < size; i++) {
                    if (TextUtils.equals(localId,goodList.get(i).getSizeId())) {
                        double num = goodList.get(i).getGoodsNum();
                        goodList.get(i).setGoodsNum(num + 1);
                        data.get(position).setGoodsNum(num + 1);
                        if(showMemberPrice&&data.get(position).getVipPrice()>=0) {
                            UpdatePriceNum(1,Double.valueOf(data.get(position).getVipPrice()),0,i,position);
                        }else {
                            UpdatePriceNum(1,data.get(position).getGoodsPrice(),0,i,position);
                        }
                        return;
                    }
                }
            }else {
                for (int i = 0; i < size; i++) {
                    if (TextUtils.equals(localId,goodList.get(i).getGoodsId())) {
                        double num = goodList.get(i).getGoodsNum();
                        goodList.get(i).setGoodsNum(num + 1);
                        data.get(position).setGoodsNum(num + 1);
                        if(showMemberPrice&&data.get(position).getVipPrice()>=0) {
                            UpdatePriceNum(1,Double.valueOf(data.get(position).getVipPrice()),0,i,position);
                        }else {
                            UpdatePriceNum(1,data.get(position).getGoodsPrice(),0,i,position);
                        }
                        return;
                    }
                }
            }


            data.get(position).setGoodsNum(1);
            goodList.add(data.get(position));
            if(showMemberPrice&&data.get(position).getVipPrice()>=0) {
                UpdatePriceNum(1,data.get(position).getVipPrice(),0,size-1,position);
            }else {
                UpdatePriceNum(1,data.get(position).getGoodsPrice(),0,size-1,position);
            }
        } else {
            last_localdata_pos = 0;
            data.get(position).setGoodsNum(1);
            List<GoodBean> list = new ArrayList<>();
            list.add(data.get(position));
            localOrderBean.setGoodList(list);
            if(showMemberPrice&&data.get(position).getVipPrice()>=0) {
                UpdatePriceNum(1,data.get(position).getVipPrice(),0,0,position);
            }else {
                UpdatePriceNum(1,data.get(position).getGoodsPrice(),0,0,position);
            }
        }
    }

    //更新价格和商品数量:isEditOrClick=1代表输入的数量；0代表点击添加
    private void UpdatePriceNum(final double num, final double price, final int isEditOrClick,int selectedIndex,int adapterPosition) {
        last_pos = pos_which_goods;
        LogUtils.e(TAG,"UpdatePriceNum() num="+num+",price="+price);
        Message mes = Message.obtain();
        mes.what = Constant.TOTAL_ACCOUNT;
        Bundle bundle = new Bundle();
        bundle.putInt("e_c",isEditOrClick);
        bundle.putDouble("g_p",price);
        bundle.putDouble("g_n",num);
        bundle.putInt("target_poisition",selectedIndex);
        bundle.putInt("adapter_position",adapterPosition);
        mes.setData(bundle);
        handler.sendMessage(mes);
        LogUtils.e(TAG,"点击以后的记录位置信息：last_pos="+last_pos);
    }


    class  ViewHolder{
        TextView name;
        @BindView(R.id.goods_price)
        TextView goods_price;
        @BindView(R.id.tv_stock)
        TextView tv_stock;
        @BindView(R.id.tv_tight_inventory)
        ImageView sale_out;
        @BindView(R.id.al_gs_item)
        RelativeLayout al_gs_item;
      /*  @BindView(R.id.pup_tv_gw_num)
        TextView pup_tv_gw_num;*/
        @BindView(R.id.member_price_logo)
        ImageView member_price_logo;
        @BindView(R.id.tv_stock_nervous)
        TextView tv_stock_nervous;
        @BindView(R.id.al_stock)
        RelativeLayout al_stock;
        @BindView(R.id.rl_price)
        RelativeLayout rl_price;

        @BindView(R.id.ll_name)
        LinearLayout ll_name;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
