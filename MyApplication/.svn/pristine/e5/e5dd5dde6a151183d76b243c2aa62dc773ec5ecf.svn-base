package com.yongle.letuiweipad.pagers.manager;

import android.app.Dialog;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.activity.MainActivity;
import com.yongle.letuiweipad.basepager.ManagerBasePager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.constant.UrlConstance;
import com.yongle.letuiweipad.utils.FpUtils;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.NetWorks;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import sunmi.ds.DSKernel;
import sunmi.ds.callback.ICheckFileCallback;
import sunmi.ds.callback.ISendCallback;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class FuPingSettingPager extends ManagerBasePager {
    @BindView(R.id.reget)TextView reget;
    @BindView(R.id.tv_right) TextView tvRight;
    @BindView(R.id.tv_left) TextView tv_left;
    @BindView(R.id.tv_nosetting_notice) TextView tv_nosetting_notice;
    @BindView(R.id.iv_loading)ImageView ivLoading;
    @BindView(R.id.rl_loading)RelativeLayout rlLoading;
    @BindView(R.id.net_error)RelativeLayout netError;
    @BindView(R.id.no_setting)LinearLayout no_setting;
    @BindView(R.id.ll_refresh_success)LinearLayout ll_refresh_success;
    private final String TAG="fpSetting";
    private Dialog upLoadDia;
    private List<Long> resId;
    String[] urls={"https://img12.360buyimg.com/n12/s800x800_jfs/t10198/341/2049136605/181101/89253dbc/59ec3325N906f107e.jpg",
                    "https://img12.360buyimg.com/n12/s800x800_jfs/t12622/357/463401753/190011/26695d8e/5a0bbd3eN902f08c7.jpg",
                    "https://img12.360buyimg.com/n12/s800x800_jfs/t3298/58/1622979569/120892/64989235/57d0d400Nfd249af4.jpg",
                    "https://img10.360buyimg.com/n12/s800x800_jfs/t277/193/1005339798/768456/29136988/542d0798N19d42ce3.jpg",
                    "https://img13.360buyimg.com/n12/s800x800_jfs/t5665/314/2322218150/93343/9c8f12e3/592fcc4eN9440407e.jpg"};
    private int currentPosition=0;
    private int showPosition=0;
    private final String BASE_URL="http://tui.younle.com/upload/deskbanner/";
    private int totalPic;

    @Override
    public View initView() {
        View baseView = View.inflate(mActivity, R.layout.fuping_setting, null);
        return baseView;
    }

    @Override
    public void initData(int position) {
        resId=new ArrayList<>();
        LogUtils.Log("副屏设置 position="+position);
        tvRight.setVisibility(View.GONE);
        tv_left.setText("刷新图片");
        reget.setText("再次刷新");
//        cleanDSD();
        //1.联网请求数据
        requestData();
        //1.1网络异常显示neterror,loading 消失

        //1.2隐藏neterror，loading 网络正常，盘算是否有设置图片，没有的话显示noSetting 有的话更新下载

        //2更新成功后
    }

    private void requestData() {
        rlLoading.setVisibility(View.VISIBLE);
        ivLoading.clearAnimation();
        Utils.pbAnimation(mActivity,ivLoading);
        NetWorks netWorks=new NetWorks(mActivity);
        Map<String, String> params = netWorks.getPublicParams();
        params.put("advid",Constant.ADV_ID);
        params.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID);
        params.put("type","t1");
        params.put("size",14+"");
        netWorks.BannerRequest(UrlConstance.GET_BANNER, "正在请求数据...", params, 5000, 0, new NetWorks.OnNetCallBack() {
            @Override
            public void onError(Exception e, int flag) {
                LogUtils.Log("请求副屏banner onError() e=="+e.toString());
                Utils.showToast(mActivity,"网络异常！");
                netError.setVisibility(View.VISIBLE);
                rlLoading.setVisibility(View.GONE);
                no_setting.setVisibility(View.GONE);
            }

            @Override
            public void onResonse(String response, int flag) {
                LogUtils.Log("请求副屏banner onResonse() response=="+response.toString());
                netError.setVisibility(View.GONE);
                rlLoading.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code==200) {
                        JSONArray msg = jsonObject.getJSONObject("msg").getJSONArray("imgs");
                        if(msg==null||msg.length()<=0) {
                            ll_refresh_success.setVisibility(View.GONE);
                            no_setting.setVisibility(View.VISIBLE);
                            tv_nosetting_notice.setText("请在乐推微平台（tui.younle.com）为本门店:"+Constant.STORE_M+"，上传T1 七寸副屏广告图片，并点击再次刷新");
                            cleanDSD();
                            FpUtils.changFpImg(((MainActivity)mActivity).dsKernel,-1);
                        }else {
                            no_setting.setVisibility(View.GONE);
                            startDownLoadPic(msg);
                            LogUtils.e(TAG,"开始下载图片");
                        }
                    }else {
                        Utils.showToast(mActivity,"参数有误，请联系乐推微解决！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.Log("请求副屏banner 解析错误"+e.toString());

                }

            }
        });
    }

    @OnClick({R.id.reget,R.id.net_error_refresh})
    public void onViewClicked() {
        requestData();
        /*currentPosition=0;
        LogUtils.Log("开始上传");
        Utils.showWaittingDialog(mActivity,"刷新...");
        for (int i = 0; i <urls.length ; i++) {
            currentPosition=i;
            getImg("gg"+i+".png");
        }*/
    }
    void startDownLoadPic(JSONArray array) throws JSONException {
        Utils.showWaittingDialog(mActivity,"正在下载图片...");
        totalPic=array.length();
        currentPosition=0;
        for (int i = 0; i < array.length(); i++) {
            String name = (String) array.get(i);
            getImg("gg"+i+".png",BASE_URL+name);
        }
    }

    private void getImg( String name,String url) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fuping";
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(path,name) {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.e(TAG,"请求图片 onError()");
                        netError.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onResponse(File response) {
                        LogUtils.e(TAG,"请求图片 onresponse()");
                        if(ll_refresh_success.getVisibility()==View.GONE) {
                            ll_refresh_success.setVisibility(View.VISIBLE);
                        }
                        FuPingSettingPager.this.saveFile(response);
                    }

                    @Override
                    public void inProgress(float progress, long total) {
                    }
                });
    }

    private void saveFile(File file) {
        String path=file.getAbsolutePath();
        if(((MainActivity)mActivity).dsKernel==null) {
            LogUtils.e(TAG,"dskernel==null");
            return;
        }
        ((MainActivity)mActivity).dsKernel.sendFile(DSKernel.getDSDPackageName(), path, new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                LogUtils.e(TAG,"onSendSuccess() taskId=="+taskId);
                resId.add(taskId);
                if(resId.size()>=totalPic) {
                    cleanDSD();
                    SaveUtils.saveObject(mActivity, Constant.FP_PICID,resId);
                    List<Long> ids= (List<Long>) SaveUtils.getObject(mActivity,Constant.FP_PICID);
                    Utils.dismissWaittingDialog();
                    ((MainActivity)mActivity).startFpLb();
                }
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                LogUtils.Log("onSendFail=="+errorId+" errorinfo=="+errorInfo);

            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        });
    }
    /**
     * 清除副屏数据
     */
    private void cleanDSD() {
        List<Long> historyData= (List<Long>) SaveUtils.getObject(mActivity,Constant.FP_PICID);
        if(historyData==null||historyData.size()<=0) {
            return;
        }
        for (int i = 0; i < historyData.size(); i++) {
            ((MainActivity)mActivity).dsKernel.deleteFileExist(historyData.get(i), new ICheckFileCallback() {
                @Override
                public void onCheckFail() {
                    Log.d(TAG, "onCheckFail: ----------->");
                }

                @Override
                public void onResult(boolean exist) {
                    Log.d(TAG, "onResult: ---------->" + exist);
                }
            });
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.e(TAG,"onHiddenChanged ="+hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        LogUtils.e(TAG,"onPause()");
        super.onPause();
    }
}
