package com.younle.younle624.myapplication.pagers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.younle.younle624.myapplication.R;
import com.younle.younle624.myapplication.activity.manager.orderpager.AboutH5Activity;
import com.younle.younle624.myapplication.activity.voucher.cancel.CancelPackage;
import com.younle.younle624.myapplication.activity.voucher.cancel.VoucherDetailActivity;
import com.younle.younle624.myapplication.basepager.BasePager;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.constant.UrlConstance;
import com.younle.younle624.myapplication.domain.VoucherCardInfo;
import com.younle.younle624.myapplication.domain.VoucherRead;
import com.younle.younle624.myapplication.domain.VoucherScanResult;
import com.younle.younle624.myapplication.domain.ZtBean;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.NetWorks;
import com.younle.younle624.myapplication.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import zxing.activity.CaptureActivity;

/**
 * Created by 我是奋斗 on 2016/5/9.
 * 微信/e-mail:tt090423@126.com
 */
@SuppressLint("ValidFragment")
public class VoucherPager extends BasePager implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "VoucherPager";
    private TextView tv_title;
    private AlertDialog alertDialog;
    private String input = "";
    private int action=0;
    private RadioGroup rg_actions;
    private View line_left;
    private View line_right;
    public VoucherPager(Activity activity) {
        super(activity);
        EventBus.getDefault().register(this);
    }

    public static final VoucherPager newInstance(Activity activity){
        VoucherPager fragment = new VoucherPager(activity);
        return fragment;
    }
    public  void clearBeforeData(){
        LogUtils.e(TAG,"action=="+action);
        if(et_code!=null) {
            et_code.setText("");
            if(action==0)
                et_code.setHint("在此输入兑换码");
            else
                et_code.setHint("在此输入自提码");
        }
    }
    private EditText et_code;
    private LinearLayout al_input_hx;
    private LinearLayout al_scan_hx;
    private TextView btn_voucher_search;
    private TextView about_ltw_voucher_ex;
    private TextView about_wx_voucher;
    private TextView about_ltw_voucher;
    private View tab_line;
    private View selected_line;
    @Override
    public View initView() {
        LogUtils.Log("voucherpager initView()");
        View view = View.inflate(mActivity, R.layout.vouchers_catainer, null);
        et_code = (EditText) view.findViewById(R.id.et_code);
        al_input_hx = (LinearLayout) view.findViewById(R.id.al_input_hx);
        al_scan_hx = (LinearLayout) view.findViewById(R.id.al_scan_hx);
        about_ltw_voucher = (TextView) view.findViewById(R.id.about_ltw_voucher);
        rg_actions = (RadioGroup) view.findViewById(R.id.rg_actions);
        line_left = view.findViewById(R.id.line_left);
        line_right = view.findViewById(R.id.line_right);
        tab_line = view.findViewById(R.id.tab_line);
        selected_line = view.findViewById(R.id.selected_line);
        btn_voucher_search = (TextView) view.findViewById(R.id.btn_voucher_search);
        about_ltw_voucher_ex = (TextView) view.findViewById(R.id.about_ltw_voucher_ex);
        about_wx_voucher = (TextView) view.findViewById(R.id.about_wx_voucher);
        about_ltw_voucher = (TextView) view.findViewById(R.id.about_ltw_voucher);
        if(!Constant.OPENED_PERMISSIONS.contains("9")) {
            rg_actions.setVisibility(View.GONE);
            tab_line.setVisibility(View.GONE);
            selected_line.setVisibility(View.GONE);
        }
        //标题栏
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("核销");
        setListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setListener() {
        al_input_hx.setOnClickListener(this);
        al_scan_hx.setOnClickListener(this);
        about_ltw_voucher.setOnClickListener(this);
        //about_wx_voucher.setOnClickListener(this);
        et_code.setOnFocusChangeListener(this);
        rg_actions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rb_left) {
                    line_left.setVisibility(View.VISIBLE);
                    line_right.setVisibility(View.INVISIBLE);
                    btn_voucher_search.setText("券号核销");
                    Drawable drawable = mActivity.getResources().getDrawable(R.drawable.vouvher_num_icon);
                    btn_voucher_search.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                    btn_voucher_search.setCompoundDrawablePadding(Utils.dip2px(mActivity,15));
                    about_ltw_voucher_ex.setText("您可在此核销");
                    et_code.setHint("在此输入兑换码");
                    about_wx_voucher.setVisibility(View.VISIBLE);
                    about_ltw_voucher.setVisibility(View.VISIBLE);
                    action=0;
                }else if(checkedId==R.id.rb_right) {
                    line_left.setVisibility(View.INVISIBLE);
                    line_right.setVisibility(View.VISIBLE);
                    btn_voucher_search.setText("自提码核销");
                    et_code.setHint("在此输入自提码");
                    btn_voucher_search.setCompoundDrawables(null,null,null,null);
                    about_ltw_voucher_ex.setText("您可以在此核销社区门店小程序的自提码");
                    about_wx_voucher.setVisibility(View.GONE);
                    about_ltw_voucher.setVisibility(View.GONE);
                    action=1;
                }
            }
        });
        rg_actions.check(R.id.rb_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.al_input_hx ://输入核销
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                input = et_code.getText().toString();
                if(input!=null&&!TextUtils.isEmpty(input)) {
                    searchVoucher(input);
                }else {
                    Utils.showToast(mActivity,"请输入核销码！", Toast.LENGTH_LONG);
                }

                break;
            case R.id.al_scan_hx://启动扫码
                input = "";
                Intent intent=new Intent(mActivity, CaptureActivity.class);
                intent.putExtra(Constant.FROME_WHERE,Constant.VOUCHER_PAGER);
                mActivity.startActivity(intent);
                break;
            case R.id.about_ltw_voucher:
                Intent intent1 = new Intent(mActivity, AboutH5Activity.class);
                intent1.putExtra("fromwhere",0);
                mActivity.startActivity(intent1);
                break;
            case R.id.about_wx_voucher:
                Intent intent2 = new Intent(mActivity, AboutH5Activity.class);
                intent2.putExtra("fromwhere",1);
                mActivity.startActivity(intent2);
                break;
        }
    }

    private void searchVoucher(String content){
        LogUtils.e(TAG,"action=="+action);
        NetWorks netWorks=new NetWorks(mActivity);
        Map<String, String> params = netWorks.getPublicParams();
        params.put("account_id", Constant.ACCOUNT_ID);
        params.put("advid", Constant.ADV_ID);
        params.put("storeid", Constant.STORE_ID);//门店id
        params.put("code", content);
        params.put("action",action+"");
        Utils.showWaittingDialog(mActivity,"正在检索");
        netWorks.Request(UrlConstance.VOUCHER_READ_URL, params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.e(TAG,"搜索卡券：e="+e.toString());
                Utils.dismissWaittingDialog();
                Toast.makeText(mActivity, "连接超时，请检查网络后重新尝试", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.e(TAG,"response:"+response);

                Utils.dismissWaittingDialog();
                praseJson(response);
            }
        });
    }

    private void praseJson(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
            int code = jsonObject.getInt("code");
            String message;
            if(code==200) {//此时需要区分解析的是H5还是卡券 0是H5 1是卡券
                Gson gson=new Gson();
                if(action==0) {//卡券
                    int card_type = jsonObject.getInt("card_type");
                    switch (card_type){
                        case 0:
                            VoucherRead voucherRead = gson.fromJson(json, VoucherRead.class);
                            toCancelActivity(voucherRead);
                            et_code.setText("");
                            break;
                        case 1:
                            VoucherCardInfo voucherCardInfo = gson.fromJson(json, VoucherCardInfo.class);
                            toCancelActivity(voucherCardInfo);
                            et_code.setText("");
                            break;
                    }
                }else if(action==1) {//自提核销
                    ZtBean ztBean = gson.fromJson(jsonObject.getString("msg"), ZtBean.class);
                    Intent intent=new Intent(mActivity, VoucherDetailActivity.class);
                    intent.putExtra("code",input);
                    intent.putExtra("ztbean",ztBean);
                    mActivity.startActivity(intent);
                    et_code.setText("");
                }
            }else {
                switch (code) {
                    case 1001:
                        message="输入的兑换码不存在";
                        break;
                    case 1002:
                        message="门店ID不能为空";
                        break;
                    case 1003:
                        message="兑换码不适用此门店";
                        break;
                    default:
                        if(jsonObject.getString("msg") != null){
                            message = jsonObject.getString("msg");
                        }else{
                            message = "未定义的核销码错误";
                        }
                }
                Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Toast.makeText(mActivity, "数据解析异常，请联系乐推微解决", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

    }
    /**
     * 扫描结束后的回调
     * @param result
     */
    @Subscribe
    public void onEventMainThread(VoucherScanResult result) {
        input = result.getResult();
        //2.联网请求，判断代金券状态
        if(input!=null&&!TextUtils.isEmpty(input)) {
            searchVoucher(result.getResult());
        }else {
            Utils.showToast(mActivity,"请输入核销码！",Toast.LENGTH_LONG);
        }
    }

    //H5核销信息
    private void toCancelActivity(VoucherRead voucherRead) {
        Intent intent=new Intent(mActivity, CancelPackage.class);
        intent.putExtra(Constant.VOUCHER_READ_BEAN,voucherRead);
        intent.putExtra(Constant.FROME_WHERE,0);
        mActivity.startActivity(intent);
    }
    //卡券信息
    private void toCancelActivity(VoucherCardInfo voucherCardInfo ) {
        Intent intent=new Intent(mActivity, CancelPackage.class);
        voucherCardInfo.getMsg().setCode(input);
        intent.putExtra(Constant.VOUCHER_CARD_BEAN,voucherCardInfo);
        intent.putExtra(Constant.FROME_WHERE,1);
        mActivity.startActivity(intent);
    }
    /**
     * 解注册广播接收器
     */
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            et_code.setHint("");
        }
    }
}