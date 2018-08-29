package com.younle.younle624.myapplication.activity.manager.printdeviceset;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.printsetting.GroupInfoBean;
import com.younle.younle624.myapplication.domain.printsetting.PrintGroupBean;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.younle.younle624.myapplication.utils.SaveUtils.getObject;

public class PrintGroupSettingActivity extends Activity {
    private ListView lv_groups;
    private ProgressBar pb_loading;
    private TextView tv_loading;
    private LinearLayout ll_loading;
    private List<PrintGroupBean> data;
    private ImageView iv_jiazai_filure;
    private GroupAdapter adapter;
    private TextView tv_title;
    private PrintGroupBean checkedGroup;
    /**
     * 1.本地打印机
     * 2.蓝牙打印机
     * 3.云打印机
     */
    private int which_printer;
    private int position;
    private SavedPrinter currentPrinter;
    private List<SavedPrinter> savedPrinters;
    private List<String> printGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_group_setting);
        lv_groups = (ListView)findViewById(R.id.lv_groups);
        pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        tv_loading = (TextView)findViewById(R.id.tv_loading);
        ll_loading = (LinearLayout)findViewById(R.id.ll_loading);
        iv_jiazai_filure = (ImageView)findViewById(R.id.iv_jiazai_filure);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("选择分组");

        which_printer = getIntent().getIntExtra("which_printer",-1);
        position = getIntent().getIntExtra("position", 0);

        switch (which_printer) {
            case 1 :
                currentPrinter  = (SavedPrinter) getObject(this,Constant.LOCAL_PRINTER);
                break;
            case 2 :
                currentPrinter  = (SavedPrinter) getObject(this,Constant.BT_PRINTER);
                break;
            case 3 :
                savedPrinters = (List<SavedPrinter>) getObject(this, Constant.YUN_PRINTERS);
                currentPrinter = savedPrinters.get(position);
                break;
        }
        if(currentPrinter==null) {
            currentPrinter=new SavedPrinter();
            currentPrinter.setPrintGroupName("全部分组");
            printGroups=new ArrayList<>();
            currentPrinter.setPrintGroupId(printGroups);
        }else {
            printGroups = currentPrinter.getPrintGroupId();
        }
        initData();
        setlistener();
    }

    private void setlistener() {
        lv_groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) {
                    data.get(0).setChecked(!data.get(0).isChecked());
                    if(data.get(0).isChecked()) {
                        for (int j = 1; j < data.size(); j++) {
                            data.get(j).setChecked(true);
                        }
                    }else {
//                        for (int j = 1; j < data.size(); j++) {
//                            data.get(j).setChecked(false);
//                        }
                    }
                }else {
                    int checkNum=0;
                    for (int j = 0; j < data.size(); j++) {
                        if(data.get(j).isChecked()) {
                            checkNum++;
                        }
                    }
                    if(checkNum<=1&&data.get(i).isChecked()) {
                        Utils.showToast(PrintGroupSettingActivity.this,"请至少选择一个品类！");
                        return ;
                    }

                    if(data.get(0).isChecked()) {
                        data.get(0).setChecked(false);
                    }
                    data.get(i).setChecked(!data.get(i).isChecked());
                }
                adapter.notifyDataSetChanged();
            }
        });

        //确定
        findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCheckedBean();
            }
        });
        //返回
        findViewById(R.id.ll_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCheckedBean();
            }
        });
    }

    private void getCheckedBean() {
        PrintGroupBean checkedBean=null;
        printGroups.clear();

        String groupName="";
        if(data!=null&&data.size()>0) {
            boolean allCheck=true;
            for (int i = 1; i < data.size(); i++) {
                if(data.get(i).isChecked()) {
                    checkedBean = data.get(i);
                    printGroups.add(checkedBean.getId());
                    groupName+=checkedBean.getGroup_name()+" ";
                }else {
                    allCheck=false;
                }
            }
            if(allCheck) {
                printGroups.clear();
                groupName="全部分组";
            }
            setPrinterState(groupName);
        }else{
            printGroups.clear();
            setPrinterState("全部分组");
        }
        Intent intent=new Intent(PrintGroupSettingActivity.this,TicketSettingActivity.class);
        intent.putExtra("which_printer",which_printer);
        intent.putExtra("position",position);
        startActivity(intent);
        finish();

    }

    private void setPrinterState(String groupName) {
        currentPrinter.setPrintGroupId(printGroups);
        currentPrinter.setPrintGroupName(groupName);
        switch (which_printer) {
            case 1 :
                SaveUtils.saveObject(this,Constant.LOCAL_PRINTER,currentPrinter);
                break;
            case 2 :
                SaveUtils.saveObject(this,Constant.BT_PRINTER,currentPrinter);
                break;
            case 3 :
                SaveUtils.saveObject(this,Constant.YUN_PRINTERS, savedPrinters);
                List<SavedPrinter>yunList= (List<SavedPrinter>) SaveUtils.getObject(this,Constant.YUN_PRINTERS);
                break;
        }
    }

    //back
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK) {
            getCheckedBean();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 联网获取数据
     */
    private void initData() {
        NetWorks netWorks=new NetWorks(this);
        Map<String, String> params = netWorks.getPublicParams();
        params.put("store_id", Constant.STORE_ID);
        params.put("advid",Constant.ADV_ID);
        netWorks.Request(UrlConstance.GOODS_SPLIT_INFO, params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("获取分组信息 onError():"+e.toString());
                pb_loading.setVisibility(View.GONE);
                iv_jiazai_filure.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("获取分组信息 onResponse():"+response.toString());
                praseJson(response);
            }
        });
    }


    private void praseJson(String json) {
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            if(code==200) {
                GroupInfoBean groupInfoBean = new Gson().fromJson(json, GroupInfoBean.class);
                ll_loading.setVisibility(View.GONE);
                showData(groupInfoBean);
            }else {
                String msg = jsonObject.getString("msg");
                pb_loading.setVisibility(View.GONE);
                tv_loading.setText(msg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showData(GroupInfoBean groupInfoBean) {
        PrintGroupBean msgBean=new PrintGroupBean();
        msgBean.setChecked(false);
        msgBean.setGroup_name("全部");
        msgBean.setId("0");
        data = groupInfoBean.getMsg();
        data.add(0,msgBean);
        if(printGroups.size()<=0) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setChecked(true);
            }
        }else {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setChecked(printGroups.contains(data.get(i).getId()));
            }
        }

        adapter = new GroupAdapter();
        lv_groups.setAdapter(adapter);
    }

    class GroupAdapter extends BaseAdapter {

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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if(convertView==null) {
                convertView=View.inflate(PrintGroupSettingActivity.this,R.layout.group_info_layout,null);
                holder=new ViewHolder();
                holder.tvGroupName= (TextView) convertView.findViewById(R.id.tv_group_name);
                holder.ivIcon= (ImageView) convertView.findViewById(R.id.iv_group_check_logo);
                holder.line=convertView.findViewById(R.id.line);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.tvGroupName.setText(data.get(i).getGroup_name());
            if(data.get(i).isChecked()) {
                holder.ivIcon.setBackgroundResource(R.drawable.group_checked);
            }else {
                holder.ivIcon.setBackgroundResource(R.drawable.group_unchecked);
            }
            if(i==data.size()-1) {
                holder.line.setVisibility(View.GONE);
            }else {
                holder.line.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

    }
    class ViewHolder{
        TextView tvGroupName;
        ImageView ivIcon;
        View line;
    }

}
