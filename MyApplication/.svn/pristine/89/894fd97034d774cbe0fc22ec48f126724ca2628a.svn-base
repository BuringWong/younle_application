package com.yongle.letuiweipad.pagers.manager.printersetting;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.utils.SaveUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class AddYunPrinterPager extends ManagerBasePager {
    @BindView(R.id.sure)Button sure;
    private View totalView;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_left) TextView tv_left;
    @BindView(R.id.et_id)
    EditText et_id;
    @BindView(R.id.et_screate_key)
    EditText et_screate_key;
    @BindView(R.id.et_new_name)
    EditText et_new_name;
    private List<SavedPrinter> savedPrinterList;

    @Override
    public View initView() {
        totalView = View.inflate(mActivity, R.layout.add_yunprinter_layout,null);
        return totalView;
    }

    @Override
    public void initData(int position) {
        tvRight.setVisibility(View.GONE);
        tv_left.setText("打印设置");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("返回");
        savedPrinterList = (List<SavedPrinter>) SaveUtils.getObject(mActivity, Constant.YUN_PRINTERS);

    }

    @OnClick(R.id.sure)
    void addYunPinter(View view){
        if(TextUtils.isEmpty(et_id.getText().toString())){
            Toast.makeText(mActivity, "请输入终端号", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(et_screate_key.getText().toString())){
            Toast.makeText(mActivity, "请输入密钥", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(et_new_name.getText().toString())){
            Toast.makeText(mActivity, "请输入打印机名称", Toast.LENGTH_SHORT).show();
            return;
        }
        addNewYunPrinter();
    }
    /**
     * 添加新云打印机
     */
    private void addNewYunPrinter() {
        //1.新建云打印存储对象 2.进行本地存储
        SavedPrinter savedPrinter = new SavedPrinter(et_id.getText().toString(),et_screate_key.getText().toString(),et_new_name.getText().toString());
        savedPrinter.setPrintGroupName("全部分组");
        savedPrinter.setPrintGroupId("0");
        savedPrinter.setType(2);
        savedPrinter.setPrintPermission("23457");
        if(savedPrinterList !=null) {
            savedPrinterList.add(savedPrinter);
        }else{
            savedPrinterList = new ArrayList<>();
            savedPrinterList.add(savedPrinter);
        }
        boolean success = SaveUtils.saveObject(mActivity, Constant.YUN_PRINTERS, savedPrinterList);
        if(success) {
            /*Constant.isfinishAddNewAc = true;
            finish();*/
            setFragment(new AddedPrinterPager());
        }else {
            Toast.makeText(mActivity, "保存失败，请稍后重试！", Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.tv_right)
    void backForward(View view){
        setFragment(new AddedPrinterPager());
    }

}
