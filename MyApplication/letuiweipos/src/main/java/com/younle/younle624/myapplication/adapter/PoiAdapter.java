package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.AroundPoi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/5/17.
 * 微信/e-mail:tt090423@126.com
 *
 * 联网请求地址后的适配器
 */
public class PoiAdapter extends BaseAdapter {
    private int selectedPostion=0;
    List<AroundPoi.ResultBean.PoisBean> poiList;
    private Context context;

    public PoiAdapter(Context context) {
        this.context=context;
        poiList=new ArrayList<>();
    }

    public void setData(List<AroundPoi.ResultBean.PoisBean> poiList) {
        this.poiList=poiList;
    }
    @Override
    public int getCount() {
        return poiList.size();
    }

    @Override
    public Object getItem(int position) {
        return poiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            convertView=View.inflate(context, R.layout.around_item,null);
            holder=new ViewHolder();
            holder.around_add= (TextView) convertView.findViewById(R.id.around_add);
            holder.around_name= (TextView) convertView.findViewById(R.id.around_name);
            holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        if(position==selectedPostion) {
            holder.iv_selected.setVisibility(View.VISIBLE);
        }else {
            holder.iv_selected.setVisibility(View.GONE);
        }
        holder.around_name.setText(poiList.get(position).getTitle());
        holder.around_add.setText(poiList.get(position).getAddress());
        return convertView;
    }

    public void setPosition(int position) {
        this.selectedPostion=position;
    }

    class ViewHolder{
        TextView around_name;
        TextView around_add;
        ImageView iv_selected;
    }

}
