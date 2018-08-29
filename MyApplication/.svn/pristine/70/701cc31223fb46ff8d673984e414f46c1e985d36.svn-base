package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.ModifyTimeBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bert_dong on 2017/3/21 0021.
 * 邮箱：18701038771@163.com
 */
public class ReservationAdapter extends BaseAdapter {

    private List<ModifyTimeBean.MsgBean> infoBean;
    private Context context;

    public ReservationAdapter(Context context, List<ModifyTimeBean.MsgBean> infoBean) {
        this.infoBean=infoBean;
        this.context=context;
    }

    @Override
    public int getCount() {
        return infoBean.size();
    }

    @Override
    public Object getItem(int position) {
        return infoBean.get(position);
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
            convertView = View.inflate(context, R.layout.reservation_time_info, null);
            holder.tv_reservation_info = (TextView) convertView.findViewById(R.id.tv_reservation_info);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ModifyTimeBean.MsgBean infoBean = this.infoBean.get(position);
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日 HH:mm");
        Date d_start = new Date(Long.valueOf(infoBean.getStart_time()+"000"));
        Date d_end = new Date(Long.valueOf(infoBean.getEnd_time()+"000"));
        holder.tv_reservation_info.setText(sf.format(d_start)+ " ~ " +sf.format(d_end));
        return convertView;
    }

    class ViewHolder{
        TextView tv_reservation_info;
    }
}
