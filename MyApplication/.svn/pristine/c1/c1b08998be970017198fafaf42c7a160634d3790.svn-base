package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.BillDetailActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.MsCenterOrderListBean;
import com.younle.younle624.myapplication.selfinterface.WxAppDetailAndSendListener;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;

import java.util.List;

/**
 * Created by bert_dong on 2017/6/16 0016.
 * 邮箱：18701038771@163.com
 */
public class AppletListAdapter extends BaseAdapter {

    private static final String TAG = "AppletListAdapter";
    private List<MsCenterOrderListBean.MsgBean.OrderListBean> orderInfo;
    private Context context;
    private WxAppDetailAndSendListener detailAndSendListener;

    public void setDetailAndSendListener(WxAppDetailAndSendListener detailAndSendListener) {
        this.detailAndSendListener = detailAndSendListener;
    }

    public AppletListAdapter(Context context) {
        this.context = context;
    }

    public void setData(MsCenterOrderListBean dataBean){
        this.orderInfo = dataBean.getMsg().getOrderList();
    }

    @Override
    public int getCount() {
        return orderInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return orderInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.applet_item_layout,null);
            holder.tv_order_date = (TextView) convertView.findViewById(R.id.tv_order_date);//日期
            holder.order_name = (TextView) convertView.findViewById(R.id.tv_order_name);//订单号
            holder.room_name = (TextView) convertView.findViewById(R.id.tv_room_name);//房间号
            holder.order_account = (TextView) convertView.findViewById(R.id.order_account);//订单总金额
            holder.order_content = (TextView) convertView.findViewById(R.id.order_content);//商品数
            holder.tv_order_time = (TextView) convertView.findViewById(R.id.tv_order_time);//订单时间
            holder.ll_remark_layout = (LinearLayout) convertView.findViewById(R.id.ll_remark_layout);
            holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);//备注
            holder.tv_is_checkout = (TextView) convertView.findViewById(R.id.tv_is_checkout);//
            holder.rl_community= (RelativeLayout) convertView.findViewById(R.id.rl_community);
            holder.community_line=convertView.findViewById(R.id.community_line);
            holder.tv_peisong= (TextView) convertView.findViewById(R.id.tv_peisong);
            holder.tv_detail= (TextView) convertView.findViewById(R.id.tv_detail);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MsCenterOrderListBean.MsgBean.OrderListBean infoBean = orderInfo.get(position);
        if(position == 0){
            holder.tv_order_date.setVisibility(View.VISIBLE);
            holder.tv_order_date.setText(infoBean.getAddtime().substring(0,10));
        }else if(position > 0){
            MsCenterOrderListBean.MsgBean.OrderListBean nextInfoBean = orderInfo.get(position - 1);
            String nextAddtime = nextInfoBean.getAddtime().substring(0,10);
            if(nextAddtime.equals(infoBean.getAddtime().substring(0,10))){
                holder.tv_order_date.setVisibility(View.GONE);
            }else{
                holder.tv_order_date.setVisibility(View.VISIBLE);
                holder.tv_order_date.setText(infoBean.getAddtime().substring(0,10));
            }
        }
        holder.order_name.setText("订单"+infoBean.getDayOrderNum());
        if(!"".equals(infoBean.getRoomName())){
            holder.room_name.setText(infoBean.getRoomName());
        }else{
            holder.room_name.setVisibility(View.GONE);
        }
        holder.order_account.setText("￥"+infoBean.getTotalFee());
        holder.order_content.setText("商品数量："+ Utils.dropZero(infoBean.getGoodsNum()));
        holder.tv_order_time.setText((infoBean.getAddtime().substring(10)+"下单").trim());

        if(TextUtils.equals(infoBean.getOrderType(),"3")) {
            holder.rl_community.setVisibility(View.VISIBLE);
            holder.community_line.setVisibility(View.VISIBLE);
            if(TextUtils.equals("0",infoBean.getDelivery_status())) {
                holder.tv_is_checkout.setTextColor(Color.parseColor("#2a9804"));
                holder.tv_is_checkout.setText("待配送");
                holder.tv_peisong.setVisibility(View.VISIBLE);
            }else if(TextUtils.equals("1",infoBean.getDelivery_status())){
                holder.tv_is_checkout.setTextColor(Color.parseColor("#333333"));
                holder.tv_is_checkout.setText("已配送");
                holder.tv_peisong.setVisibility(View.GONE);
            }else if(TextUtils.equals("2",infoBean.getDelivery_status())) {
                holder.tv_is_checkout.setTextColor(Color.parseColor("#2a9804"));
                holder.tv_is_checkout.setText("待自提");
                holder.tv_peisong.setVisibility(View.GONE);
            }else if(TextUtils.equals("3",infoBean.getDelivery_status())) {
                holder.tv_is_checkout.setTextColor(Color.parseColor("#333333"));
                holder.tv_is_checkout.setText("已自提");
                holder.tv_peisong.setVisibility(View.GONE);
            }
        }else {
            holder.rl_community.setVisibility(View.GONE);
            holder.community_line.setVisibility(View.GONE);
            if("1".equals(infoBean.getSuccess())){
                holder.tv_is_checkout.setText("已结账");
                holder.tv_is_checkout.setTextColor(Color.parseColor("#2A9804"));
            }else {
                holder.tv_is_checkout.setText("未结账");
                holder.tv_is_checkout.setTextColor(Color.parseColor("#FF5500"));
            }
        }

        if("".equals(infoBean.getOrderNote())){
            holder.ll_remark_layout.setVisibility(View.GONE);
        }else{
            holder.ll_remark_layout.setVisibility(View.VISIBLE);
            holder.tv_remark.setText(infoBean.getOrderNote());
        }
        holder.tv_peisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailAndSendListener!=null) {
                    detailAndSendListener.onClickSend(infoBean.getLogId());
                }
            }
        });
        holder.tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailAndSendListener!=null) {
                    detailAndSendListener.onClickDetail(infoBean.getOrderNo(),infoBean.getOrderType());
                }
            }
        });
        if(!TextUtils.equals("3",infoBean.getOrderType())) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e(TAG,"进入小程序详情页...");
                    Intent intent = new Intent(context, BillDetailActivity.class);
                    intent.putExtra(Constant.FROME_WHERE, Constant.APPLET_INFO_DATA);
                    if(infoBean.getOrderNo().contains("_")) {
                        intent.putExtra(Constant.ORDER_ID, infoBean.getOrderNo().substring(0,infoBean.getOrderNo().indexOf("_")));
                    }else {
                        intent.putExtra(Constant.ORDER_ID, infoBean.getOrderNo());
                    }
                    intent.putExtra("isRechargeLog", infoBean.getOrderType());
                    intent.putExtra("logid",infoBean.getLogId());

                    if("1".equals(infoBean.getSuccess())){
                        intent.putExtra("isSuccess", 1);
                    }else{
                        intent.putExtra("isSuccess", 0);
                    }

                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView order_name;
        TextView room_name;
        TextView tv_order_date;
        TextView order_account;
        TextView order_content;
        TextView tv_order_time;
        TextView tv_remark;
        TextView tv_is_checkout;
        LinearLayout ll_remark_layout;

        TextView tv_peisong;
        TextView tv_detail;
        RelativeLayout rl_community;
        View community_line;
    }
}
