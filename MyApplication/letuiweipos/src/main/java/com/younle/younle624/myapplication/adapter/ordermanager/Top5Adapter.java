package com.younle.younle624.myapplication.adapter.ordermanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.PosChartBean;

import java.util.List;

/**
 * 作者：Create by 我是奋斗 on2017/2/6 19:15
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class Top5Adapter<T> extends BaseAdapter {
    private Context context;
    private List<T> data;
    private int type=-1;

    public Top5Adapter(Context context) {
        this.context=context;
    }
    public void setData(List<T> data) {
        this.data=data;
    }
    public void setDataType(int type) {
        this.type=type;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null) {
            holder=new ViewHolder();
            view=View.inflate(context, R.layout.gv_top5_item,null);
            holder.topx= (TextView) view.findViewById(R.id.topx);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        switch (i) {
            case 0 :
                Drawable drawable= context.getResources().getDrawable(R.drawable.top1_circle);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.topx.setCompoundDrawables(drawable,null,null,null);
                break;
            case 1 :
                 drawable= context.getResources().getDrawable(R.drawable.top2_circle);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.topx.setCompoundDrawables(drawable,null,null,null);
                break;
            case 2 :
                 drawable= context.getResources().getDrawable(R.drawable.top3_circle);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.topx.setCompoundDrawables(drawable,null,null,null);
                break;
            case 3 :
                 drawable= context.getResources().getDrawable(R.drawable.top4_circle);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.topx.setCompoundDrawables(drawable,null,null,null);
                break;
            case 4 :
                 drawable= context.getResources().getDrawable(R.drawable.top5_circle);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.topx.setCompoundDrawables(drawable, null, null, null);
                
                break;
        }

        if((data.size()-(i+1))>=0) {
            T t = data.get(data.size()-(i+1));
            if(type==0) {
                PosChartBean.MsgBean.SaledTop5Bean saledTop5Bean= (PosChartBean.MsgBean.SaledTop5Bean) t;
                holder.topx.setText("TOP"+(i+1)+":"+saledTop5Bean.getGoodsName());
            }else if(type==1) {
                PosChartBean.MsgBean.IncomeTop5Bean incomeTop5Bean= (PosChartBean.MsgBean.IncomeTop5Bean) t;
                holder.topx.setText("TOP"+(i+1)+":"+incomeTop5Bean.getGoodsName());
            }
        }
        return view;
    }

   class ViewHolder{
        TextView topx;
    }

}
