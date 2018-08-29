package com.yongle.letuiweipad.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.yongle.letuiweipad.activity.MainActivity;
import com.yongle.letuiweipad.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import ds.data.DataModel;
import ds.data.UPacketFactory;
import sunmi.ds.DSKernel;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.data.DataPacket;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class FpUtils {
    private static final String TAG="FpUtils";
    static String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/member_voucher/fourteen_member.jpg";
    static String path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/member_voucher/fourteen_voucher.jpg";
    static  String path3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/member_voucher/senven_member.jpg";
    static  String path4 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/member_voucher/senven_vocher.jpg";
    private static String[] paths={path1,path2,path3,path4};
    public static final int MEMBER=1;
    public static final int VOUCHER=2;

    public static void changFpImg(DSKernel dsKernel, long taskId) {
        try{
            if(dsKernel!=null) {
                String json = UPacketFactory.createJson(DataModel.SHOW_IMG_WELCOME, "def");
                dsKernel.sendCMD(DSKernel.getDSDPackageName(), json, taskId, null);
            }
        }catch (Exception e){
            LogUtils.e("TAG","轮播富平异常");
        }

    }

    public static void saveFile(DSKernel dsKernel, final Context context) {
        if(dsKernel==null) {
            return;
        }
        for (int i = 0; i < paths.length; i++) {
            startWrite(dsKernel, i,context);
        }
    }

    private static void startWrite(DSKernel dsKernel, final int i,final Context context) {
        dsKernel.sendFile(DSKernel.getDSDPackageName(), paths[i], new ISendCallback() {
            @Override
            public void onSendSuccess(long taskId) {
                LogUtils.e(TAG,"onSendSuccess："+taskId);
                switch (i) {
                    case 0 ://14寸
                        SpUtils.getInstance(context).save(Constant.MEMBER14,String.valueOf(taskId));
                        break;
                    case 1 :
                        SpUtils.getInstance(context).save(Constant.VOUCHER14,String.valueOf(taskId));
                        break;
                    case 2 ://7寸
                        SpUtils.getInstance(context).save(Constant.MEMBER7,String.valueOf(taskId));
                        break;
                    case 3 :
                        SpUtils.getInstance(context).save(Constant.VOUCHER7,String.valueOf(taskId));
                        break;
                }
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
                LogUtils.e(TAG,"onSendFail=="+errorId+" errorinfo=="+errorInfo);

            }

            @Override
            public void onSendProcess(long totle, long sended) {

            }
        });
    }

    public static void showTp(DSKernel dsKernel,String content) {
        if(dsKernel==null) {
            return;
        }
        String jsonStr = buildData("您的付款金额：", "￥" + Utils.keepTwoDecimal(content));
        DataPacket dsPacket = UPacketFactory.buildShowText(
                DSKernel.getDSDPackageName(), jsonStr, new ISendCallback() {
                    @Override
                    public void onSendSuccess(long l) {
                    }

                    @Override
                    public void onSendFail(int i, String s) {
                    }

                    @Override
                    public void onSendProcess(long l, long l1) {
                    }
                });
        dsKernel.sendCMD(dsPacket);
    }

    private static String buildData(String title, String content) {
        JSONObject json = new JSONObject();
        try {
            json.put("title", title);
            json.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static  void showMemberVocher(Activity context,int which){
        ((MainActivity)context).stopFpLb();
        String taskId;
        if(which==VOUCHER) {
            taskId = SpUtils.getInstance(context).getString(Constant.VOUCHER14,null);
        }else {
            taskId = SpUtils.getInstance(context).getString(Constant.MEMBER14,null);
        }
        LogUtils.e(TAG,"taskId=="+taskId);
        if(taskId!=null&&!TextUtils.isEmpty(taskId)) {
            Utils.showToast(context,"副屏已展示操作流程！");
            FpUtils.changFpImg(((MainActivity) context).dsKernel, Long.valueOf(taskId));
        }else {
            Utils.showToast(context,"未检测到相应文件");
        }
    }


}
