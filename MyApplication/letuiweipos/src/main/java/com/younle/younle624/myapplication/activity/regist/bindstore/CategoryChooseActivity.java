package com.younle.younle624.myapplication.activity.regist.bindstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.domain.KindsChoose;
import com.younle.younle624.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类目选择界面
 */
public class CategoryChooseActivity extends Activity implements View.OnClickListener {
    private RadioButton btn_kind1;
    private RadioButton btn_kind2;
    private RadioButton btn_kind3;
    private ListView lv_kinds_info;
    private List<String> data;
    private List<String> data1;
    private List<String> data2;
    private List<String> data3;
    private int currentSelect=0;
    private KindAdapter adapter;
    private KindsChoose kindsChoose=new KindsChoose();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_choose);
        initView();
        initData();
    }

    private void initData() {
        data=new ArrayList<>();
        data1=new ArrayList<>();
        data2=new ArrayList<>();
        data3=new ArrayList<>();

        for (int i = 0; i < 20; i++) {
           String test="种类1数据";
            data1.add(test);
        }
        for (int i = 0; i < 20; i++) {
           String test="种类2数据";
            data2.add(test);
        }
        for (int i = 0; i < 20; i++) {
           String test="种类3数据";
            data3.add(test);
        }
        data=data1;
        adapter=new KindAdapter();
        lv_kinds_info.setAdapter(adapter);
        setListener();
    }

    private void setListener() {
        lv_kinds_info.setOnItemClickListener(new MyOnItemClickListener());
        btn_kind1.setOnClickListener(this);
        btn_kind2.setOnClickListener(this);
        btn_kind3.setOnClickListener(this);
    }

    /**
     * radiogroup的点击监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_kind1 :
                data=data1;
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_kind2:
                data=data2;
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_kind3:
                data=data3;
                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 获取当前处于选中状态的类目
     * @return
     */
    private int getCurrentSelect(){
        if(btn_kind1.isChecked()) {
            currentSelect=0;
            return 0;
        }else if(btn_kind2.isChecked()) {
            currentSelect = 1;
            return 1;
        }else if(btn_kind3.isChecked()) {
            currentSelect=2;
            return 2;
        }else {
            return 0;
        }
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            currentSelect=getCurrentSelect();
            if(currentSelect==0){
                btn_kind1.setText(data.get(position));
                kindsChoose.setFirstKinds(data.get(position));
                currentSelect=1;
                btn_kind2.setVisibility(View.VISIBLE);
                btn_kind2.setChecked(true);
                btn_kind1.setChecked(false);
                data=data2;
                adapter.notifyDataSetChanged();
            }else if(currentSelect==1) {
                btn_kind2.setText(data.get(position));
                kindsChoose.setSecondKinds(data.get(position));
                currentSelect=2;
                btn_kind3.setVisibility(View.VISIBLE);
                btn_kind3.setChecked(true);
                btn_kind2.setChecked(false);
                data=data3;
                adapter.notifyDataSetChanged();
            }else {
                btn_kind3.setText(data.get(position));
                kindsChoose.setThirdKinds(data.get(position));
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("kinds",kindsChoose);
                intent.putExtras(bundle);
                // 设置SecondActivity的结果码(resultCode)，并设置在当前结束后退回去的Activity
                CategoryChooseActivity.this.setResult(0, intent);
                finish();
            }
        }
    }


    private void initView() {
        btn_kind1 = (RadioButton)findViewById(R.id.btn_kind1);
        btn_kind2 = (RadioButton)findViewById(R.id.btn_kind2);
        btn_kind3 = (RadioButton)findViewById(R.id.btn_kind3);
        lv_kinds_info = (ListView)findViewById(R.id.lv_kinds_info);


    }

    class KindAdapter extends BaseAdapter{

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
                convertView=new TextView(CategoryChooseActivity.this);
                convertView.setMinimumHeight(Utils.dip2px(CategoryChooseActivity.this,40));
                holder.textView= (TextView) convertView;
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(data.get(position));
            
            return convertView;
        }
        class ViewHolder{
            TextView textView;
        }
    }
}
