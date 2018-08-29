package com.younle.younle624.myapplication.activity.manager.ordersetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.setting.SettingItemBean;
import com.younle.younle624.myapplication.utils.Utils;
import com.younle.younle624.myapplication.view.SelfLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class OrderSettingActivity extends Activity implements View.OnClickListener, SelfLinearLayout.ClickToReload {

    private ImageView iv_title;
    private TextView tv_title;
    private ListView lv_setting;
    private ProgressBar pb_loading;
    private TextView tv_loading;
    private SelfLinearLayout ll_loading;
    private SettingItemBean settingItemBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_setting);
        Utils.initToolBarState(this);
        initView();
        getDataFromeNet();
        setListener();
    }

    /**
     * 联网获取数据
     */
    private void getDataFromeNet() {
        reLoad();
        OkHttpUtils.post()
                .url(UrlConstance.SETTING_ITEMS)
                .addParams("storeid",Constant.STORE_ID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        loadFailure("网络异常，点击屏幕重新加载");
                    }
                    @Override
                    public void onResponse(String response) {
                        boolean toNextStep = Utils.checkSaveToken(OrderSettingActivity.this, response);
                        if(toNextStep){
                            praseJson(response);
                        }
                    }
                });
    }
    /**
     * 解析json
     * @param json
     */
    private void praseJson(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            String code = jsonObject.getString("code");

            if("20006".equals(code)) {
                String msg = jsonObject.getString("msg");
                loadFailure(msg);
            }else if("200".equals(code)) {
                ll_loading.setVisibility(View.GONE);
                Gson gson=new Gson();
                settingItemBean = gson.fromJson(json, SettingItemBean.class);
                showData();
            }else {
                String msg = jsonObject.getString("msg");
                loadFailure(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initView() {
        lv_setting = (ListView)findViewById(R.id.lv_setting);
        pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        tv_loading = (TextView)findViewById(R.id.tv_loading);
        ll_loading = (SelfLinearLayout)findViewById(R.id.ll_loading);
        iv_title = (ImageView)findViewById(R.id.iv_title);
        iv_title.setVisibility(View.VISIBLE);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("商品/服务/房间/人员设置");
    }
    private void setListener() {
        iv_title.setOnClickListener(this);
        ll_loading.setClickToReload(this);
    }
    private void showData() {
        SettingItemAdapter settingItemAdapter=new SettingItemAdapter();
        lv_setting.setAdapter(settingItemAdapter);
    }
    public void toNextActivity(int toWhere) {
        Intent intent=new Intent(this,BaseSettingActivity.class);
        intent.putExtra(Constant.FROME_WHERE,toWhere);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title:
                finish();
                break;
        }
    }
    /**
     * 联网获取数据失败
     */
    public void loadFailure(String msg){
        tv_loading.setText(msg);
        pb_loading.setVisibility(View.GONE);
    }
    public void reLoad(){
        pb_loading.setVisibility(View.VISIBLE);
        tv_loading.setText(R.string.try_loading);
    }

    @Override
    public void ClickToReload() {
        getDataFromeNet();
    }

    class SettingItemAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return settingItemBean.getMsg().size();
        }

        @Override
        public Object getItem(int position) {
            return settingItemBean.getMsg().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null) {
                convertView = View.inflate(OrderSettingActivity.this, R.layout.manger_alreadyset_item, null);
                holder=new ViewHolder();
                holder.tv_item_name= (TextView) convertView.findViewById(R.id.tv_item_name);
                holder.tv_desc= (TextView) convertView.findViewById(R.id.tv_desc);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            final SettingItemBean.MsgBean msgBean = settingItemBean.getMsg().get(position);
            switch (msgBean.getType()) {
                case 1 :
                    holder.tv_desc.setText(R.string.good_setting_item_desc);
                    break;
                case 2:
                    holder.tv_desc.setText(R.string.service_setting_item_desc);
                    break;
                case 3:
                    holder.tv_desc.setText(R.string.room_setting_item_desc);
                    break;
                case 4:
                    holder.tv_desc.setText(R.string.man_setting_item_desc);
                    break;
            }
                holder.tv_item_name.setText(msgBean.getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (msgBean.getType()) {
                        case 1 :
                            toNextActivity(Constant.GOODS_SETTING);
                            break;
                        case 2:
                            toNextActivity(Constant.SERVICE_SETTING);
                            break;
                        case 3:
                            toNextActivity(Constant.ROOM_SETTING);
                            break;
                        case 4:
                            toNextActivity(Constant.MAN_SETTING);
                            break;
                    }
                }
            });
            return convertView;
        }
       class ViewHolder{
            TextView tv_desc;
            TextView tv_item_name;
           
       }
    }
}
