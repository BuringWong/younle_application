package com.yongle.letuiweipad.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.PosPrintBean;
import com.yongle.letuiweipad.domain.ZtBean;
import com.yongle.letuiweipad.utils.Utils;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/11/14.
 * 微信/e-mail:tt090423@126.com
 */
public class PosDetailAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> data;
    private boolean containsVipGoods;

    public PosDetailAdapter(Context context) {
        this.context=context;
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
        if(convertView==null) {
            convertView=View.inflate(context, R.layout.pos_detail_item,null);
            holder=new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.name);
            holder.num= (TextView) convertView.findViewById(R.id.num);
            holder.account= (TextView) convertView.findViewById(R.id.account);
            holder.end= (TextView) convertView.findViewById(R.id.end);
            holder.devide_line=convertView.findViewById(R.id.devide_line);
            holder.rl_mid= (LinearLayout) convertView.findViewById(R.id.rl_mid);
            holder.member_price_logo= (ImageView) convertView.findViewById(R.id.member_price_logo);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        T t = data.get(position);
        if(t instanceof PosPrintBean.MsgBean.GoodsInfoBean ) {
            PosPrintBean.MsgBean.GoodsInfoBean detailBean = (PosPrintBean.MsgBean.GoodsInfoBean) t;
            double acc = detailBean.getPrice() * Double.parseDouble(detailBean.getNum());
            String formatAcc = Utils.keepTwoDecimal(acc + "");
            holder.account.setText("￥" + Utils.dropZero(formatAcc));

            if(containsVipGoods) {
                int is_vip = detailBean.getIs_vip();
                if(0==is_vip) {
                    holder.member_price_logo.setVisibility(View.INVISIBLE);
                }else {
                    holder.member_price_logo.setVisibility(View.VISIBLE);
                }
            }else {
                holder.member_price_logo.setVisibility(View.GONE);
            }
            if(!"3".equals(detailBean.getType())) {//未知商品，实物和服务
                holder.name.setText(detailBean.getName());
                holder.num.setText("X"+ Utils.dropZero(Utils.keepTwoDecimal(detailBean.getNum()))+"       ");
                holder.end.setVisibility(View.GONE);
                convertView.setMinimumHeight(Utils.dip2px(context,50));
            }else {//房间
                holder.name.setText(detailBean.getName());
                holder.num.setText("    "+detailBean.getStart());
                holder.end.setText("至"+detailBean.getEnd());
                holder.end.setVisibility(View.VISIBLE);
                convertView.setMinimumHeight(Utils.dip2px(context,60));
            }
            if(position>0) {
                if(detailBean.getType().equals(((PosPrintBean.MsgBean.GoodsInfoBean)(data.get(position-1))).getType())) {
                    holder.devide_line.setVisibility(View.GONE);
                }else {
                    holder.devide_line.setVisibility(View.VISIBLE);
                }
            }
        }else {
            ZtBean.GoodsListBean detailBean= (ZtBean.GoodsListBean) t;
            holder.name.setText(detailBean.getGoods_name());
            holder.num.setText(detailBean.getGoods_num()+"");
            holder.account.setText("￥" + Constant.numDf.format(detailBean.getGoods_price() * detailBean.getGoods_num()));
            if(containsVipGoods) {
                int is_vip = detailBean.getIs_vip();
                if(0==is_vip) {
                    holder.member_price_logo.setVisibility(View.INVISIBLE);
                }else {
                    holder.member_price_logo.setVisibility(View.VISIBLE);
                }
            }else {
                holder.member_price_logo.setVisibility(View.GONE);
            }
            holder.end.setVisibility(View.GONE);
            holder.devide_line.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setData(List<T> goodsInfos) {
        data=goodsInfos;
    }

    public void setContainsVipGoods(boolean containsVipGoods) {
        this.containsVipGoods = containsVipGoods;
    }

    class ViewHolder{
        TextView name;
        TextView num;
        TextView account;
        TextView end;
        LinearLayout rl_mid;
        View devide_line;
        ImageView member_price_logo;
    }
}
