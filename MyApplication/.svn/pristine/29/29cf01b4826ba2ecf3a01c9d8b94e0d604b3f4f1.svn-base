package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.RoomsInfoBean;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/23.
 * 微信/e-mail:tt090423@126.com
 */
public class KindsAdapter extends BaseAdapter {
    private  Context context;
    private List<RoomsInfoBean.MsgBean.RoominfoBean> data;
    public KindsAdapter(Context context) {
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
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.choose_store_item,null);
            holder.kindName= (TextView) convertView.findViewById(R.id.tv_store);
            holder.iv_left_line= (ImageView) convertView.findViewById(R.id.iv_left_line);
            holder.room_num=0;
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        if(data.get(position).isChoosed()) {
            convertView.setBackgroundColor(Color.WHITE);
            holder.kindName.setTextColor(Color.parseColor("#3f88cd"));
            holder.iv_left_line.setVisibility(View.VISIBLE);
        }else {
            convertView.setBackgroundColor(Color.rgb(242, 242, 242));
            holder.kindName.setTextColor(Color.parseColor("#292929"));
            holder.iv_left_line.setVisibility(View.GONE);
        }

        /*if(data.get(position).getRoomlist()!=null && data.get(position).getRoomlist().size()>0){
            //判断可用房间数
            holder.room_num = 0;
            for(int i=0;i<data.get(position).getRoomlist().size();i++){
                if(!"1".equals(data.get(position).getRoomlist().get(i).getUsed())){
                    holder.room_num++;
                }
            }
            holder.kindName.setText(data.get(position).getProduct_typename()+"("+holder.room_num+")");
        }else{
            holder.kindName.setText(data.get(position).getProduct_typename()+"(0)");
        }*/
        holder.kindName.setText(data.get(position).getProduct_typename()+"("+data.get(position).getNum()+")");
        return convertView;
    }

    public void setData(List<RoomsInfoBean.MsgBean.RoominfoBean> data) {
        this.data=data;
    }

    class ViewHolder {
        TextView kindName;
        ImageView iv_left_line;
        int room_num = 0;
    }
}
