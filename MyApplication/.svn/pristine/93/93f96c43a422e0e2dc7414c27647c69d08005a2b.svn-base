package com.younle.younle624.myapplication.activity.manager.orderpager.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class ServiceOrderDetailActivity extends Activity implements View.OnClickListener {
    private ListView lv_service_detail;
    private List<String> data;
    private Button btn_mark_reback;
    private IWoyouService iWoyouService;
    public ServiceConnection connService=new ServiceConnection() {
        //连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iWoyouService= IWoyouService.Stub.asInterface(service);//拿到打印服务的对象
        }
        //断开服务
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iWoyouService=null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_detail);
        initView();
        initData();
        initService();
    }
    private void initService() {
        Intent intent=new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    /**
     * 联网请求数据
     */
    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String payName="收款人：雷布斯";
            data.add(payName);
        }
        lv_service_detail.setAdapter(new BillAdapter());
    }

    private void initView() {
        lv_service_detail = (ListView)findViewById(R.id.lv_service_detail);
        btn_mark_reback = (Button)findViewById(R.id.btn_mark_reback);
        btn_mark_reback.setOnClickListener(this);
    }

    /**
     * 打印
     * @param v
     */
    @Override
    public void onClick(View v) {
        showDia();
    }
    private void showDia() {

        View button = View.inflate(this, R.layout.self_dia_layout,null);
        Button sure= (Button) button.findViewById(R.id.btn_sure);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("已将订单状态更改为退款")
                .setView(button)
                .show();
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    class BillAdapter extends BaseAdapter{

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
                convertView = View.inflate(ServiceOrderDetailActivity.this, R.layout.service_order_print_item, null);
                holder=new ViewHolder();
               holder.message= (TextView) convertView.findViewById(R.id.message);
                convertView.setTag(holder);

            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.message.setText(data.get(position));
            return convertView;
        }
        class ViewHolder{
            TextView message;
        }
    }
}
