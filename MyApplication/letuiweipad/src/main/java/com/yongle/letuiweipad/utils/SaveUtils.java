package com.yongle.letuiweipad.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Created by 我是奋斗 on 2016/8/3.
 * 微信/e-mail:tt090423@126.com
 */
public class SaveUtils {
    public static boolean saveObject(Context context,String name,Object sod){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            //文件路径：/data/data/<package name>/files
            fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(sod);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
            LogUtils.Log("保存异常=="+e.toString());
            return false;
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }
    public static Object getObject(Context context,String name){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }
    public static void deleteFile(Context context,String name){
        File file=new File(context.getFilesDir(),name);
        if(file.exists()) {
            boolean delete = file.delete();
        }
    }
    /**
     * 打开日志文件并写入日志
     *
     * @return
     * **/
    public static void writeLogtoFile( String tag, String text) {// 新建或打开日志文件
            String currentM = Utils.getCurrentM(Utils.getCurrentTimeMill());
            String needWriteMessage = currentM + "    " +  tag + "    " + text+'\n';
            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),Utils.getToday()+"padlog.txt");
            try {
                OutputStreamWriter write = null;
                BufferedWriter out = null;
                if (file== null) {
                    file.createNewFile();
                }
                try {   // new FileOutputStream(fileName, true) 第二个参数表示追加写入
                    write = new OutputStreamWriter(new FileOutputStream(file,true), Charset.forName("gbk"));//一定要使用gbk格式
                    out = new BufferedWriter(write);
                } catch (Exception e) {
                }
                out.write(needWriteMessage);
                out.flush();
                out.close();
            } catch (Exception e) {

            }
    }
    public static void deleLog(){
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),Utils.getPreDay()+"padlog.txt");
        if(file!=null) {
            file.delete();
        }
    }

    public static void writeAssetTofp(Activity context,String src){
        String dstPath = "member_voucher";
        try {
            String[] fileNames = context.getResources().getAssets().list(src);
            if (fileNames.length > 0) {
                File file = new File(Environment.getExternalStorageDirectory(), dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    writeAssetTofp(context,fileName);
                }
            } else {
                File outFile=new File(Environment.getExternalStorageDirectory()+"/"+dstPath+"/"+src);
//                File outFile = new File(Environment.getExternalStorageDirectory(), dstPath+"/"+src);
                InputStream is =context.getResources(). getAssets().open(src);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            LogUtils.e("main"," 复制文件:"+e.toString());
            e.printStackTrace();
        }
    }
}
