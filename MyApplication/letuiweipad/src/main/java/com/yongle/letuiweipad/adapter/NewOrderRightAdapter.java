package com.yongle.letuiweipad.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.domain.goodinfo.GoodsTypeInfo;
import com.yongle.letuiweipad.selfinterface.GoodKindsOnClickListener;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/23.
 * 微信/e-mail:tt090423@126.com
 * 商品种类的adapter
 */
public class NewOrderRightAdapter extends BaseAdapter {
    private Context context;
    private List<GoodsTypeInfo> data;
    private int type = -1;
    private String TAG = "NewOrderRightAdapter<T>";
    private GoodKindsOnClickListener kindsOnClickListener;
    public NewOrderRightAdapter(Context context) {
        this.context = context;
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.order_right_item, null);
            holder.kinds_name = (TextView) convertView.findViewById(R.id.kinds_name);
            holder.right_line = (ImageView) convertView.findViewById(R.id.right_line);
//            holder.tv_kind_num= (TextView) convertView.findViewById(R.id.tv_kind_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GoodsTypeInfo kindsBean=  data.get(position);
        if (kindsBean.isChecked()) {
            convertView.setBackgroundColor(Color.WHITE);
            holder.right_line.setVisibility(View.VISIBLE);
        } else {
            convertView.setBackgroundColor(Color.parseColor("#ededed"));
            holder.right_line.setVisibility(View.INVISIBLE);
        }
        holder.kinds_name.setText(kindsBean.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for (int i = 0; i < data.size(); i++) {
                    if(i==position) {
                        data.get(i).setChecked(true);
                    }else {
                        data.get(i).setChecked(false);
                    }
                }
                NewOrderRightAdapter.this.notifyDataSetChanged();*/
                kindsOnClickListener.onGoodKindsClick(kindsBean.getTypeId());
            }
        });
        return convertView;
    }

    public void setKindsOnClickListener(GoodKindsOnClickListener kindsOnClickListener) {
        this.kindsOnClickListener = kindsOnClickListener;
    }

    public void setData(List<GoodsTypeInfo> data) {
        this.data = data;
    }

    public void setDataType(int type) {
        this.type = type;
    }

    class ViewHolder {
        TextView kinds_name;
        ImageView right_line;
    }
}
