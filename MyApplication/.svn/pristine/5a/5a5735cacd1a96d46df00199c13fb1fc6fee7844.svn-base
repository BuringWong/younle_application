package com.yongle.letuiweipad.utils.format;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 作者：Create by 我是奋斗 on2017/5/6 11:07
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class FormatUtils {
    public static int accuracy=2;

    public static void setPricePoint(final EditText et_new_price, final OnInputChangeListener inputChangeListner) {
        final boolean[] contains = {false};
        et_new_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //输入点默认加0
                if (accuracy==2&&s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_new_price.setText(s);
                    et_new_price.setSelection(2);
                }
                //限制不能输入0006，000.3这样格式的数据
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_new_price.setText(s.subSequence(0, 1));
                        et_new_price.setSelection(1);
                        return;
                    }
                }
                //限定只能输入一个小数点
                if (et_new_price.getText().toString().indexOf(".") >= 0) {
                    if (et_new_price.getText().toString().indexOf(".", et_new_price.getText().toString().indexOf(".") + 1) > 0) {
                        et_new_price.setText(et_new_price.getText().toString().substring(0, et_new_price.getText().toString().length() - 1));
                        et_new_price.setSelection(et_new_price.getText().toString().length());
                    }
                }
                //解决输入折扣点击两次小数点崩溃bug
                if (s.toString().indexOf(".") >= 0) {
                    if (s.toString().indexOf(".", s.toString().indexOf(".") + 1) > 0) {
                        s = s.subSequence(0,s.length()-1);
                    }
                }
                //限制小数点后两位
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > accuracy) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + accuracy+1);
                        et_new_price.setText(s);
                        et_new_price.setSelection(s.length());
                    }
                }
                //限制仅能输入1-10
                if(accuracy==1&&!TextUtils.isEmpty(s.toString())) {
                    if(".".equals(s.toString())) {
                        s="";
                        et_new_price.setText(s);
                        et_new_price.setSelection(0);
                    }else {
                        Double zk = Double.valueOf(s.toString());
                        if(zk<1||zk>=10) {
                            s=s.subSequence(0,s.length()-1);
                            et_new_price.setText(s);
                            et_new_price.setSelection(s.length());
                            return;
                        }
                    }
                }
                if(inputChangeListner!=null) {
                    inputChangeListner.inputChange();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                contains[0] = s.toString().contains(".");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().contains(".")&&s.toString().endsWith(".")) {

                }
                // TODO Auto-generated method stub
            }
        });
    }
    public interface OnInputChangeListener{
        void inputChange();
    }
}
