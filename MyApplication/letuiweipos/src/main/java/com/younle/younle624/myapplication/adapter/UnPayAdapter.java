package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.createorder.OrderDetailActivity;
import com.younle.younle624.myapplication.domain.UnPayBean;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.MyListView;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/23.
 * 微信/e-mail:tt090423@126.com
 */
public class UnPayAdapter extends BaseAdapter{
    private Context context;
    private List<UnPayBean.MsgBean> msgBeanList;
    //private UnPayBean.MsgBean msgBean;
    private String TAG = "UnPayAdapter";

    public UnPayAdapter(Context context) {
        this.context=context;
    }

    public void setData(List<UnPayBean.MsgBean> data) {
        this.msgBeanList=data;
    }

    @Override
    public int getCount() {
        return msgBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.unpay_list_first_item,null);
            holder.tv_order_time = (TextView) convertView.findViewById(R.id.tv_order_time);
            holder.lv_unpay_order_info = (MyListView) convertView.findViewById(R.id.lv_unpay_order_info);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        UnPayBean.MsgBean msgBean = this.msgBeanList.get(position);
        if(position==0) {
            holder.tv_order_time.setVisibility(View.VISIBLE);
            holder.tv_order_time.setText(msgBean.getAddtime());
            DetailAdapter detailAdapter = new DetailAdapter(msgBean);
            holder.lv_unpay_order_info.setAdapter(detailAdapter);
        }else if(position>=1) {
            UnPayBean.MsgBean beforeBean = this.msgBeanList.get(position - 1);
            if(beforeBean.getAddtime().equals(msgBean.getAddtime())) {
                //日期相同，textView消失，线消失
                holder.tv_order_time.setVisibility(View.GONE);
                DetailAdapter detailAdapter = new DetailAdapter(msgBean);
                holder.lv_unpay_order_info.setAdapter(detailAdapter);
            }else {//日期不同，显示textview
                holder.tv_order_time.setVisibility(View.VISIBLE);
                holder.tv_order_time.setText(msgBean.getAddtime());
                DetailAdapter detailAdapter = new DetailAdapter(msgBean);
                holder.lv_unpay_order_info.setAdapter(detailAdapter);
            }
        }
        /*holder.tv_order_time.setText(msgBean.getAddtime());
        DetailAdapter detailAdapter = new DetailAdapter();
        holder.lv_unpay_order_info.setAdapter(detailAdapter);*/
        return convertView;
    }

    /**
     *
     */
    class DetailAdapter extends BaseAdapter{
        private UnPayBean.MsgBean msgBean;
        public DetailAdapter(UnPayBean.MsgBean msgBean) {
            this.msgBean = msgBean;
        }

        @Override
        public int getCount() {
            return msgBean.getOrderlist().size();
        }

        @Override
        public Object getItem(int position) {
            return msgBean.getOrderlist().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView==null) {
                holder=new ViewHolder();
                convertView=View.inflate(context, R.layout.unpay_list_sec_item,null);
                holder.order_room = (TextView) convertView.findViewById(R.id.order_room);
                holder.order_goodnum = (TextView) convertView.findViewById(R.id.order_goodnum);
                holder.order_account = (TextView) convertView.findViewById(R.id.order_account);
                holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }

            final UnPayBean.MsgBean.OrderlistBean orderlistBean = msgBean.getOrderlist().get(position);
            holder.order_room.setText("订单序号："+orderlistBean.getQuery_num());
            holder.order_goodnum.setText("商品数量：" + Utils.formatPrice(Double.valueOf(orderlistBean.getGoods_num())));
            if(orderlistBean.getRoom_name()!=null&&!"".equals(orderlistBean.getRoom_name())){
                //20170406改：1代表桌台 0代表房间
                if("1".equals(orderlistBean.getRoom_type())){
                    holder.order_account.setVisibility(View.VISIBLE);
                    holder.order_account.setText("桌台："+orderlistBean.getRoom_name());
                }else if("0".equals(orderlistBean.getRoom_type())){
                    holder.order_account.setVisibility(View.VISIBLE);
                    holder.order_account.setText("房间："+orderlistBean.getRoom_name());
                }else{
                    holder.order_account.setVisibility(View.GONE);
                }
            }else{
                holder.order_account.setVisibility(View.GONE);
            }
            holder.order_time.setText(orderlistBean.getOrder_time()+"下单");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    LogUtils.e(TAG, "传递到OrderDetailActivity页面的订单id=" + orderlistBean.getId());
                    intent.putExtra("unpay_order_id", orderlistBean.getId());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        TextView order_room;
        TextView order_goodnum;
        TextView order_account;
        TextView order_time;
        TextView tv_order_time;
        MyListView lv_unpay_order_info;
    }
}
