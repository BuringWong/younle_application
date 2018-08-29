package com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter;


import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;

import com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter.print.GPrinterCommand;
import com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter.print.PrintPic;
import com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter.print.PrintQueue;
import com.younle.younle624.myapplication.activity.manager.orderpager.bluetoothprinter.print.PrintUtil;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.printmanager.PicFromPrintUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import zxing.encoding.EncodingHandler;

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
            ArrayList<String> print_data = intent.getStringArrayListExtra(PrintUtil.ACTION_PRINT);
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

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
    byte[] start = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1B,
            0x40, 0x1B, 0x33, 0x00 };
    byte[] end = { 0x1d, 0x4c, 0x1f, 0x00 };

    private void printTest(ArrayList<String> print_data) {
        int memberEnd = print_data.indexOf("POS机软件系统提供商：乐推微");
        try {
            ArrayList<byte[]> bytes = new ArrayList<byte[]>();
            bytes.add(GPrinterCommand.text_normal_size);
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < print_data.size(); j++) {
                    if(j==0||j==1||j==3) {
                        bytes.add(GPrinterCommand.center);
                        bytes.add(print_data.get(j).getBytes("gbk"));
                    }else if(print_data.contains("扫描下方二维码成为我店会员")&&i==memberEnd-1) {
                        Bitmap bitmap = generateQRcode(print_data.get(j));
                        byte[] draw2PxPoint = PicFromPrintUtils.draw2PxPoint(bitmap);
                        bytes.add(start);
                        bytes.add(draw2PxPoint);
                        bytes.add(end);
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
        Bitmap bitmap = generateQRcode("tui.younle.com");
//        Bitmap compressBitmap = PicFromPrintUtils.compressBitmap(bitmap);
        byte[] bytes = PicFromPrintUtils.draw2PxPoint(bitmap);
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
            Bitmap bitmap = EncodingHandler.createQRCode(str, 192);
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