package com.yongle.letuiweipad.pagers.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class AboutPager extends ManagerBasePager {
    @BindView(R.id.tv_imei)TextView tvImei;
    @BindView(R.id.tv_current_advid)TextView tvCurrentAdvid;
    @BindView(R.id.tv_storepie_acctv)TextView tvStorepieAcctv;
    @BindView(R.id.tv_version)TextView tvVersion;
    @BindView(R.id.button)TextView button;
    @BindView(R.id.tv_right) TextView tvRight;
    @BindView(R.id.tv_left) TextView tv_left;
    private Dialog upLoadDia;

    @Override
    public View initView() {
        View baseView = View.inflate(mActivity, R.layout.about_layout, null);
        return baseView;
    }

    @Override
    public void initData(int position) {
        LogUtils.Log("关于本机initData position="+position);
        tvRight.setVisibility(View.GONE);
        tv_left.setText("关于本机");
        TelephonyManager tm = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if(deviceId==null) {
            deviceId= Settings.Secure.getString(mActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        tvImei.setText("当前设备IMEI： "+deviceId);
        tvCurrentAdvid.setText("绑定的品牌主账号： "+ Constant.ADV_NAME);
        tvStorepieAcctv.setText("当前使用的门店派账号： "+Constant.USER_ACCOUNT);
        String currentVersion = "未知版本";
        PackageManager manager = mActivity.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(mActivity.getPackageName(), 0);
            currentVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace(); //如果找不到对应的应用包信息, 就返回"未知版本"
        }
        tvVersion.setText("当前软件版本：" + currentVersion);
        button.setText("上传日志");
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        LogUtils.Log("开始上传");
        Utils.showWaittingDialog(mActivity,"日志上传中...");
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Utils.getToday()+"padlog.txt");
        OkHttpUtils.post()
                .addFile("log","padlog.txt",file)
                .addParams(Constant.PARAMS_NEME_ACCOUNT_ID,Constant.ACCOUNT_ID)
                .url(UrlConstance.UPDATE_LOG)
                .build()
                .connTimeOut(60000)
                .readTimeOut(60000)
                .writeTimeOut(60000)
                .execute(new StringCallback() {
                    @Override
                    public void inProgress(float progress) {
                        super.inProgress(progress);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.Log("error=="+e.toString());
                        Utils.showToast(mActivity,"日志上传失败，请重试!",1000);
                        Utils.dismissWaittingDialog();
                    }

                    @Override
                    public void onResponse(String response) {
                        Utils.dismissWaittingDialog();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if(code==200) {
                                Utils.showToast(mActivity,"成功上传日志!",1000);
                            }else {
                                Utils.showToast(mActivity,"日志上传失败，请重试!",1000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

}
