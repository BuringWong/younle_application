package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.BillDetailActivity;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.PosOrderBean;
import com.younle.younle624.myapplication.utils.ClicKUtils;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.MyListView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/19.
 * 微信/e-mail:tt090423@126.com
 *
 * pos机专用adapter
 */
public class OrderAdapter extends BaseAdapter {

    private List<PosOrderBean.MsgBean.OrderListBean> data;
    private String goodstype;
    private String goodId;
    private String sizeId;
    private Context context;
    private String remark;
    private String TAG = "OrderAdapter";
    private DecimalFormat df = new DecimalFormat("0.00");
    public OrderAdapter(Context context,String remark) {
        this.context = context;
        this.remark = remark;
    }
    public void setData(List data,String goodstype,String goodId, String sizeId) {
        this.data = data;
        this.goodstype = goodstype;
        this.goodId = goodId;
        this.sizeId = sizeId;
    }

    @Override
    public int getCount() {
        if(data != null && data.size() > 0) {
            return data.size();
        }else {
            return 0;
        }
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
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.pos_item,null);
            holder.al_date_account= (AutoRelativeLayout) convertView.findViewById(R.id.al_date_account);
            holder.tv_posorder_data = (TextView) convertView.findViewById(R.id.order_date);//日期
            holder.tv_posorder_allin = (TextView) convertView.findViewById(R.id.order_day_total);//金额
            holder.day_depart_line=convertView.findViewById(R.id.day_depart_line);
            holder.gv_sell_finish = (MyListView) convertView.findViewById(R.id.gv_sell_finish);//详细内容
            holder.al_service_entity= (LinearLayout) convertView.findViewById(R.id.al_service_entity);
            holder.order_name = (TextView) convertView.findViewById(R.id.order_name);//订单号
            holder.order_account = (TextView) convertView.findViewById(R.id.order_account);//订单总金额
            holder.iv_pay_way= (ImageView) convertView.findViewById(R.id.iv_pay_way);
            holder.tv_room_consume= (TextView) convertView.findViewById(R.id.tv_room_consume);
            holder.order_content = (TextView) convertView.findViewById(R.id.order_content);//商品数
            holder.tv_from_which_tools = (TextView) convertView.findViewById(R.id.tv_from_which_tools);//商品数
            holder.order_time = (TextView) convertView.findViewById(R.id.order_time);//订单时间
            holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);//订单时间
            holder.ll_remark_layout = (LinearLayout) convertView.findViewById(R.id.ll_remark_layout);//订单时间
            holder.line=convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PosOrderBean.MsgBean.OrderListBean orderListBean = data.get(position);
        if(position==0) {
            holder.al_date_account.setVisibility(View.VISIBLE);
            holder.tv_posorder_data.setText(orderListBean.getDate());
            holder.tv_posorder_allin.setText("总收入：" + df.format(orderListBean.getDayIncome()));
        }else if(position>=1) {
            PosOrderBean.MsgBean.OrderListBean beforeBean = data.get(position - 1);
            if(beforeBean.getDate().equals(orderListBean.getDate())) {
                //日期相同，textView消失，线消失
                holder.al_date_account.setVisibility(View.GONE);
                holder.day_depart_line.setVisibility(View.GONE);
            }else {//日期不同，显示textview
                holder.day_depart_line.setVisibility(View.GONE);
                holder.al_date_account.setVisibility(View.VISIBLE);
                holder.tv_posorder_data.setText(orderListBean.getDate());
                holder.tv_posorder_allin.setText("总收入：" + df.format(orderListBean.getDayIncome()));
            }
        }
        holder.order_account.setText("+￥" + df.format(orderListBean.getIncome()));
        holder.order_content.setText("含 "+ Utils.dropZero(orderListBean.getGoodsNum())+" 件商品");
        String payType = orderListBean.getPayType();
        //收款工具
        if(payType !=null){
            holder.tv_from_which_tools.setText(orderListBean.getPayTool());
        }
        if(orderListBean.getRoomName()!=null && !"".equals(orderListBean.getRoomName())){
            holder.tv_room_consume.setVisibility(View.VISIBLE);
            holder.tv_room_consume.setText(orderListBean.getRoomName());
            setDiscountAccount(holder.tv_room_consume,orderListBean.getRoomName(),remark);
        }else{
            holder.tv_room_consume.setVisibility(View.GONE);
        }
        holder.order_time.setText(orderListBean.getTime());
        if(payType!=null) {
            switch (payType) {
                case "现金收款" :
                    holder.iv_pay_way.setImageResource(R.drawable.cash_pay_pic);
                    break;
                case "现金收款(记账)" :
                    holder.iv_pay_way.setImageResource(R.drawable.cash_pay_pic);
                    break;
                case "刷卡收款" :
                    holder.iv_pay_way.setImageResource(R.drawable.post_card_pic);
                    break;
                case "刷卡收款(记账)" :
                    holder.iv_pay_way.setImageResource(R.drawable.post_card_pic);
                    break;
                case "微信收款" :
                    holder.iv_pay_way.setImageResource(R.drawable.wx_pay_pic);
                    break;
                case "微信收款(记账)" :
                    holder.iv_pay_way.setImageResource(R.drawable.wx_pay_pic);
                    break;
                case "支付宝收款" :
                    holder.iv_pay_way.setImageResource(R.drawable.zfb_pay_pic);
                    break;
                case "支付宝收款(记账)" :
                    holder.iv_pay_way.setImageResource(R.drawable.zfb_pay_pic);
                    break;
                case "会员卡余额收款" :
                    holder.iv_pay_way.setImageResource(R.drawable.member_vip_pay_pic);
                    break;
            }
        }
        setDiscountAccount(holder.order_name,"订单"+orderListBean.getDayOrderNum(),remark);

        if(orderListBean.getRemark()==null||"".equals(orderListBean.getRemark())){
            holder.ll_remark_layout.setVisibility(View.GONE);
        }else{
            setDiscountAccount(holder.tv_remark,orderListBean.getRemark(),remark);
            holder.ll_remark_layout.setVisibility(View.VISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ClicKUtils.isFastDoubleClick()) {
                    return;
                }else {
                    Intent intent = new Intent(context, BillDetailActivity.class);
                    intent.putExtra(Constant.FROME_WHERE, Constant.POS_DATA);
                    intent.putExtra(Constant.ORDER_ID, orderListBean.getOrderId());
                    intent.putExtra("isRechargeLog", orderListBean.getIsRechargeLog());
                    intent.putExtra("goodstype", goodstype);
                    intent.putExtra("sizeid",sizeId);
                    intent.putExtra(Constant.GOOD_ID, goodId);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    /**
     * 根据输入的文本改变textview的高亮显示
     * @param tv
     * @param show_content
     * @param search_content
     */
    private void setDiscountAccount(TextView tv,String show_content,String search_content){
        if("".equals(search_content)){
            tv.setText(show_content);
        }else{
            String low_show = show_content.toLowerCase();
            String low_search = search_content.toLowerCase();
            int start = low_show.indexOf(low_search.toString());
            if(start==-1) {
                tv.setText(show_content);
            }else {
                if(start >= 20) {
                    String temp = "..." + show_content.substring(start-7,show_content.length());
                    SpannableStringBuilder builder = new SpannableStringBuilder(temp);
                    ForegroundColorSpan span = new ForegroundColorSpan(Color.rgb(63,136,206));
                    builder.setSpan(span, 10, 10 + search_content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                }else if(start < 20) {
                    SpannableStringBuilder builder = new SpannableStringBuilder(show_content);
                    ForegroundColorSpan span = new ForegroundColorSpan(Color.rgb(63,136,206));
                    builder.setSpan(span,start,start + search_content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                }
            }
        }
    }

    class ViewHolder{
        TextView order_name;
        View line;
        View day_depart_line;
        AutoRelativeLayout al_date_account;
        TextView tv_posorder_data;
        TextView tv_posorder_allin;
        MyListView gv_sell_finish;
        LinearLayout al_service_entity;
        TextView order_account;
        ImageView iv_pay_way;
        TextView tv_room_consume;
        TextView order_content;
        TextView tv_from_which_tools;
        TextView order_time;
        TextView tv_remark;
        LinearLayout ll_remark_layout;
    }

}
