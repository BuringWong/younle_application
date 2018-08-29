package com.yongle.letuiweipad.utils.bluetoothprinter;


import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yongle.letuiweipad.R;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.bluetoothprinter.print.GPrinterCommand;
import com.yongle.letuiweipad.utils.bluetoothprinter.print.PrintPic;
import com.yongle.letuiweipad.utils.bluetoothprinter.print.PrintQueue;
import com.yongle.letuiweipad.utils.bluetoothprinter.print.PrintUtil;
import com.yongle.letuiweipad.utils.zxing.encoding.EncodingHandler;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 * <p/>
 * print ticket service
 */
public class BtService extends IntentService {
    public BtService() {
        super("BtService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BtService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtils.Log("进服务了");
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(PrintUtil.ACTION_PRINT_TEST)) {
            ArrayList<String> print_data = intent.getStringArrayListExtra("print_data");
            printTest(print_data);
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT)) {
            print(intent.getByteArrayExtra(PrintUtil.PRINT_EXTRA));
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_TICKET)) {
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_BITMAP)) {
            printBitmapTest();
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_PAINTING)) {
            printPainting();
        }
    }

    private void printTest(ArrayList<String> print_data) {
        try {
            ArrayList<byte[]> bytes = new ArrayList<byte[]>();
            bytes.add(GPrinterCommand.text_normal_size);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < print_data.size(); j++) {
                    if(j==0||j==1||j==3) {
                        bytes.add(GPrinterCommand.center);
                        bytes.add(print_data.get(j).getBytes("gbk"));
                    }else {
                        bytes.add(GPrinterCommand.left);
                        bytes.add(print_data.get(j).getBytes("gbk"));
                    }
                    bytes.add(GPrinterCommand.print);

                }
                bytes.add(GPrinterCommand.print);
                bytes.add(GPrinterCommand.print);
                bytes.add(GPrinterCommand.print);
            }
          /*  String message = "北京用乐科技有限公司";
            bytes.add(GPrinterCommand.reset);
            bytes.add(message.getBytes("gbk"));
            bytes.add(GPrinterCommand.center);
            bytes.add(GPrinterCommand.print);
            bytes.add(GPrinterCommand.print);
            bytes.add(GPrinterCommand.print);*/
            PrintQueue.getQueue(getApplicationContext()).add(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void print(byte[] byteArrayExtra) {
        if (null == byteArrayExtra || byteArrayExtra.length <= 0) {
            return;
        }
        PrintQueue.getQueue(getApplicationContext()).add(byteArrayExtra);
    }

    private void printBitmapTest() {
     /*   BufferedInputStream bis;
        try {
            bis = new BufferedInputStream(getAssets().open(
                    "android.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }*/
        Resources r=getResources();
        @SuppressLint("ResourceType") InputStream is = r.openRawResource(R.drawable.member_card_icon);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
//        Bitmap bitmap = generateQRcode("www.baidu.com");
//        Bitmap bitmap = BitmapFactory.decodeStream(bis);
        PrintPic printPic = PrintPic.getInstance();
        printPic.init(bitmap);
        if (null != bitmap) {
            if (bitmap.isRecycled()) {
                bitmap = null;
            } else {
                bitmap.recycle();
                bitmap = null;
            }
        }
        byte[] bytes = printPic.printDraw();
        ArrayList<byte[]> printBytes = new ArrayList<byte[]>();
        printBytes.add(GPrinterCommand.reset);
        printBytes.add(GPrinterCommand.print);
        printBytes.add(bytes);
        LogUtils.Log("image bytes size is :" + bytes.length);
        printBytes.add(GPrinterCommand.print);
        PrintQueue.getQueue(getApplicationContext()).add(bytes);
    }

    /**
     * 根据str生成二维码
     * @param str
     */
    private Bitmap generateQRcode(String str) {
        try {
            Bitmap bitmap = EncodingHandler.createQRCode(str, 500);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printPainting() {
        byte[] bytes = PrintPic.getInstance().printDraw();
        ArrayList<byte[]> printBytes = new ArrayList<byte[]>();
        printBytes.add(GPrinterCommand.reset);
        printBytes.add(GPrinterCommand.print);
        printBytes.add(bytes);
        LogUtils.Log("image bytes size is :" + bytes.length);
        printBytes.add(GPrinterCommand.print);
        PrintQueue.getQueue(getApplicationContext()).add(bytes);
    }
}