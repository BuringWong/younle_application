package com.younle.younle624.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.AllGoodsInfoBean;
import com.younle.younle624.myapplication.domain.orderbean.SaleMan;
import com.younle.younle624.myapplication.domain.orderbean.WaiterLeavelBean;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/23.
 * 微信/e-mail:tt090423@126.com
 * 服务，实物，服务人员，销售人员的左侧数据的adapter
 */
public class NewOrderLeftAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> data;
    private int type = -1;
    private String TAG = "NewOrderLeftAdapter<T>";

    public NewOrderLeftAdapter(Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.choose_store_item, null);
            holder.kindName = (TextView) convertView.findViewById(R.id.tv_store);
            holder.iv_left_line = (ImageView) convertView.findViewById(R.id.iv_left_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (type) {//商品的种类
            case Constant.DIALOG_DATA://对话框数据填充
                WaiterLeavelBean waiterLeavelBean = (WaiterLeavelBean) data.get(position);
                if (waiterLeavelBean.isChecked()) {
                    convertView.setBackgroundColor(Color.WHITE);
                    holder.iv_left_line.setVisibility(View.VISIBLE);
                } else {
                    convertView.setBackgroundColor(Color.rgb(242, 242, 242));
                    holder.iv_left_line.setVisibility(View.INVISIBLE);
                }
                holder.kindName.setText(waiterLeavelBean.getLeavelName());
                break;
            case Constant.SALEMAN_DATA://销售员数据
                SaleMan.SalesBean saleBean = (SaleMan.SalesBean) data.get(position);

                if (saleBean.isChecked()) {
                    convertView.setBackgroundColor(Color.WHITE);
                    holder.iv_left_line.setVisibility(View.VISIBLE);
                } else {
                    convertView.setBackgroundColor(Color.rgb(242, 242, 242));
                    holder.iv_left_line.setVisibility(View.INVISIBLE);
                }
                holder.kindName.setText(saleBean.getName());
                break;
            default:
                AllGoodsInfoBean.MsgBean.GoodsInfoBean goodKindsData = (AllGoodsInfoBean.MsgBean.GoodsInfoBean) data.get(position);
                if (goodKindsData.isChecked()) {
                    convertView.setBackgroundColor(Color.WHITE);
                    holder.iv_left_line.setVisibility(View.VISIBLE);
                } else {
                    convertView.setBackgroundColor(Color.rgb(242, 242, 242));
                    holder.iv_left_line.setVisibility(View.INVISIBLE);
                }
                holder.kindName.setText(goodKindsData.getTypeName());
        }
        return convertView;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setDataType(int type) {
        this.type = type;
    }

    class ViewHolder {
        TextView kindName;
        ImageView iv_left_line;
    }
}
