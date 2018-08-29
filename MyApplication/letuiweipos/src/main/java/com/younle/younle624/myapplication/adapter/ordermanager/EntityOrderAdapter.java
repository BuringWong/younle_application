package com.younle.younle624.myapplication.adapter.ordermanager;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.BillDetailActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.EntityOrderBean;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/7/19.
 * 微信/e-mail:tt090423@126.com
 */
public class EntityOrderAdapter extends BaseAdapter {
    private  Context context;
    private List<EntityOrderBean.ListBean> data;
    private DecimalFormat df = new DecimalFormat("0.00");
    public EntityOrderAdapter(Context context) {
        this.context=context;
    }
    public void setData(List<EntityOrderBean.ListBean> data) {
        this.data=data;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.service_entity_item, null);
            holder.al_date_account = (RelativeLayout) convertView.findViewById(R.id.al_date_account);
            holder.order_date = (TextView) convertView.findViewById(R.id.order_date);
            holder.order_day_total = (TextView) convertView.findViewById(R.id.order_day_total);
            holder.tv_voucher_name = (TextView) convertView.findViewById(R.id.tv_voucher_name);
            holder.buy_num = (TextView) convertView.findViewById(R.id.buy_num);
            holder.actually_pay = (TextView) convertView.findViewById(R.id.actually_pay);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.day_depart_line=convertView.findViewById(R.id.day_depart_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            EntityOrderBean.ListBean entityOrderBean = data.get(position);
            if (position == 0) {//第一个显示日期，后面的如果和前面的相同，隐藏日期
                holder.al_date_account.setVisibility(View.VISIBLE);
                holder.order_date.setText(entityOrderBean.getDate());
                holder.order_day_total.setText("收入" + df.format(entityOrderBean.getIncome()));
            } else if(position>=1) {
                EntityOrderBean.ListBean beforeBean = data.get(position - 1);
                if(beforeBean.getDate().equals(entityOrderBean.getDate())) {
                    holder.al_date_account.setVisibility(View.GONE);
                }else {
                    holder.al_date_account.setVisibility(View.VISIBLE);
                    holder.order_date.setText(entityOrderBean.getDate());
                    holder.order_day_total.setText("收入" + df.format(entityOrderBean.getIncome()));
                }
            }
            holder.tv_voucher_name.setText(entityOrderBean.getGoodsName());
//            holder.buy_num.setText("购买数量："+entityOrderBean.getOrderId());
            holder.buy_num.setText("购买数量："+entityOrderBean.getBuyNum());
            holder.actually_pay.setText("实付金额："+df.format(entityOrderBean.getOrderAmount()));
            holder.tv_status.setText("订单状态："+entityOrderBean.getStatus());
            convertView.setOnClickListener(new DetailOnClickListener(entityOrderBean.getOrderId()));
            return convertView;

    }
    class ViewHolder{
        RelativeLayout al_date_account;
        TextView order_date;
        TextView order_day_total;
        TextView tv_voucher_name;
        TextView buy_num;
        TextView actually_pay;
        TextView tv_status;
        View day_depart_line;
    }
    class DetailOnClickListener implements View.OnClickListener{
        private  String orderId;
        public DetailOnClickListener(String orderId) {
            this.orderId=orderId;
        }
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(context,BillDetailActivity.class);
            intent.putExtra(Constant.FROME_WHERE,Constant.ENTITY_DATA);
            intent.putExtra(Constant.ORDER_ID,orderId);
            intent.putExtra(Constant.GOOD_ID,"0");
            context.startActivity(intent);
        }
    }
}
    /**
     * 第二级内容 售出订单，完成订单
     *//*
    class GridAdapte1 extends BaseAdapter{
        private  EntityOrderBean.MsgBean.ListBean gridData1;

        public GridAdapte1(EntityOrderBean.MsgBean.ListBean griddata1) {
            this.gridData1=griddata1;
        }

        @Override
        public int getCount() {
            if(gridData1.getCreateOrder().getDetail()==null||gridData1.getFinishOrder().getDetail()==null) {
                return 1;
            }else {
                return 2;
            }
        }

        @Override
        public Object getItem(int position) {
            return 2000;
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
                convertView=View.inflate(context,R.layout.service_entity_item,null);
                holder.sell_or_cancel= (TextView) convertView.findViewById(R.id.sell_or_cancel);
                holder.tv_posorder_allin= (TextView) convertView.findViewById(R.id.tv_posorder_allin);
                holder.gv_sell_finish= (MyListView) convertView.findViewById(R.id.gv_sell_finish);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            GridAdapter2 gridAdapter2;
            switch (position) {
                case 0 :
                    if(gridData1.getCreateOrder().getDetail()!=null) {
                        holder.sell_or_cancel.setText("售出订单(" + gridData1.getCreateOrder().getDetail().size() + ")");
                        holder.tv_posorder_allin.setText("预期收入："+gridData1.getCreateOrder().getIncome());
                        gridAdapter2=new GridAdapter2();
                        gridAdapter2.setData(gridData1.getCreateOrder().getDetail());
                        gridAdapter2.setDataType(Constant.SELL_DATA);
                        holder.gv_sell_finish.setAdapter(gridAdapter2);
                    }else {
                        holder.sell_or_cancel.setText("完成订单(" + gridData1.getFinishOrder().getDetail().size() + ")");
                        holder.tv_posorder_allin.setText("实际收入："+gridData1.getFinishOrder().getIncome());
                        gridAdapter2=new GridAdapter2();
                        gridAdapter2.setData(gridData1.getFinishOrder().getDetail());
                        gridAdapter2.setDataType(Constant.FINISH_DATA);
                        holder.gv_sell_finish.setAdapter(gridAdapter2);
                    }
                    break;
                case 1:
                        holder.sell_or_cancel.setText("完成订单(" + gridData1.getFinishOrder().getDetail().size() + ")");
                        holder.tv_posorder_allin.setText("实际收入："+gridData1.getFinishOrder().getIncome());
                        gridAdapter2=new GridAdapter2();
                        gridAdapter2.setData(gridData1.getFinishOrder().getDetail());
                        gridAdapter2.setDataType(Constant.FINISH_DATA);
                        holder.gv_sell_finish.setAdapter(gridAdapter2);
                    break;
            }
            return convertView;
        }
    }*/

   /* *//**
     * 第三级内容
     *//*
    class GridAdapter2<T> extends BaseAdapter{

        private  List<T> detail;
        private int dataType=-1;

        public GridAdapter2() {

        }
        public void setData(List<T> detail) {
            this.detail=detail;
        }
        public void setDataType(int dataType) {
            this.dataType=dataType;
        }
        @Override
        public int getCount() {
            return detail.size();
        }

        @Override
        public Object getItem(int position) {
            return detail.get(position);
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
                convertView=View.inflate(context,R.layout.entity_order_info,null);
                holder.tv_voucher_name= (TextView) convertView.findViewById(R.id.tv_voucher_name);
                holder.tv_voucher_price= (TextView) convertView.findViewById(R.id.tv_voucher_price);
                holder.tv_posorder_deadtime= (TextView) convertView.findViewById(R.id.tv_posorder_deadtime);
                holder.tv_voucher_status= (TextView) convertView.findViewById(R.id.tv_voucher_status);
                holder.tv_voucher_payer= (TextView) convertView.findViewById(R.id.tv_voucher_payer);
                holder.line=convertView.findViewById(R.id.line);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            if(position==detail.size()-1) {
                holder.line.setVisibility(View.GONE);
            }else {
                holder.line.setVisibility(View.VISIBLE);
            }
            String orderId="";
            switch (dataType) {
                case Constant.SELL_DATA :
                    EntityOrderBean.MsgBean.ListBean.CreateOrderBean.DetailBean detailBean = (EntityOrderBean.MsgBean.ListBean.CreateOrderBean.DetailBean) detail.get(position);
                    holder.tv_voucher_name.setText(detailBean.getGoodsName());
                    holder.tv_voucher_price.setText("购买数量："+detailBean.getBuyNum());
                    holder.tv_posorder_deadtime.setText("实付金额：" + detailBean.getOrderAmount());
                    holder.tv_voucher_payer.setVisibility(View.GONE);
                    holder.tv_voucher_status.setText("订单状态：" + detailBean.getStatus());
                    orderId = detailBean.getOrderId();
                    break;
                case Constant.FINISH_DATA:
                    EntityOrderBean.MsgBean.ListBean.FinishOrderBean.DetailBean detailBean2 = (EntityOrderBean.MsgBean.ListBean.FinishOrderBean.DetailBean) detail.get(position);
                    holder.tv_voucher_name.setText(detailBean2.getGoodsName());
                    holder.tv_voucher_price.setText("购买数量："+detailBean2.getBuyNum());
                    holder.tv_posorder_deadtime.setText("实付金额："+detailBean2.getOrderAmount());
                    holder.tv_voucher_payer.setVisibility(View.GONE);
                    holder.tv_voucher_status.setText("订单状态：" + detailBean2.getStatus());
                    orderId = detailBean2.getOrderId();
                    break;
            }
            //点击查看订单详情
            convertView.setOnClickListener(new DetailOnClickListener(orderId));
            return convertView;
        }
    }*/

