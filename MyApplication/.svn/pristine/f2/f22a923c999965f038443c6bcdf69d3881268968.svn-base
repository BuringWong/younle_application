package com.yongle.letuiweipad.utils.printmanager;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.text.TextUtils;

import com.yongle.letuiweipad.application.ThreadPoolManager;
import com.yongle.letuiweipad.constant.Constant;
import com.yongle.letuiweipad.domain.AppLetReceiveBean;
import com.yongle.letuiweipad.domain.PosPrintBean;
import com.yongle.letuiweipad.domain.PrintTotalBean;
import com.yongle.letuiweipad.domain.WmPintData;
import com.yongle.letuiweipad.domain.createorder.DiscountInfo;
import com.yongle.letuiweipad.domain.createorder.GoodBean;
import com.yongle.letuiweipad.domain.createorder.OrderBean;
import com.yongle.letuiweipad.domain.createorder.UnPayDetailsBean;
import com.yongle.letuiweipad.domain.printer.SavedPrinter;
import com.yongle.letuiweipad.selfinterface.PrintSecond;
import com.yongle.letuiweipad.utils.LogUtils;
import com.yongle.letuiweipad.utils.SaveUtils;
import com.yongle.letuiweipad.utils.Utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import static com.yongle.letuiweipad.constant.Constant.STORE_TEL;

/**
 * 作者：Create by 我是奋斗 on2016/12/9 11:01
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 * 打印小票的工具类
 */
public class PrintUtils<T> {
    private static final String TAG = "PrintUtils";
    private String line32="-----------------------------------------";
    private String line27="--------------------------------";
    private String line35="---------------------";
    int size27=27;
    int size35=35;
    int size40=43;
    int size50=50;
    private String[] bottomData={"收银SaaS系统提供商:门店派","联系电话:400-9600-567",
            "乐推微网址:tui.younle.com",line32,"顾客联"};
    public static PrintUtils instance;
    private DateFormat df4 = new SimpleDateFormat("MM/dd HH:mm");
    private DecimalFormat numDf = new DecimalFormat("0.00");
    private boolean isbt;
    public int textSize=27;
    public static PrintUtils getInstance(){
        if(instance==null) {
            instance=new PrintUtils();
        }
        return instance;
    }

    /**
     * 直收和会员充值成功的打印
     * @param iWoyouService
     * @param headData  公共部分数据
     * @param forCustomer 是否是顾客联
     * @param memberData 会员数据，优惠信息，二维码
     * @param textFont
     *
     */
    public void zsmcPrint(final IWoyouService iWoyouService, final List<String> headData, final boolean forCustomer,
                          final List<String> memberData, final int textFont, final ICallback callback) {

        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.先打头部内容
                    for (int i = 0; i < headData.size(); i++) {
                        String headMsg = headData.get(i);
                        if(forCustomer){
                            switch (headMsg){
                                case "支付方式:刷卡收款(记账)":
                                    headMsg = "支付方式:刷卡收款";
                                    break;
                                case "支付方式:现金收款(记账)":
                                    headMsg = "支付方式:现金收款";
                                    break;
                                case "支付方式:微信收款(记账)":
                                    headMsg = "支付方式:微信收款";
                                    break;
                                case "支付方式:支付宝收款(记账)":
                                    headMsg = "支付方式:支付宝收款";
                                    break;
                            }
                        }
                        if(i==0||i==1||i==2) {
                            if(i==2&&forCustomer) {
                                iWoyouService.setFontSize(50, null);
                            }else if(i==1&&!forCustomer) {
                                iWoyouService.setFontSize(50,null);
                            }else {
                                iWoyouService.setFontSize(textSize, null);
                            }
                            iWoyouService.setAlignment(1, null);//居中
                            iWoyouService.printText(headMsg, null);
                            iWoyouService.lineWrap(1, null);
                        }else {
                            iWoyouService.setFontSize(textSize, null);
                            iWoyouService.setAlignment(0, null);//靠左
                            iWoyouService.printText(headMsg, null);
                            iWoyouService.lineWrap(1, null);
                        }
                    }
                    //2.再打会员部分内容
                    if(memberData!=null&&memberData.size()>0) {
                        for (int i = 0; i < memberData.size(); i++) {
                            String memberMsg = memberData.get(i);
                            if((memberData.contains("扫描下方二维码成为我店会员")||memberData.contains("会员可使用微信扫描下方二维码"))&&i==(memberData.size()-1)) {//二维码
                                if(memberMsg!=null&&!"".equals(memberMsg)){
                                    iWoyouService.setFontSize(textSize, null);
                                    iWoyouService.setAlignment(1, null);//居中
                                    iWoyouService.printQRCode(memberMsg, 4, 3, null);
                                    iWoyouService.lineWrap(1, null);
                                }
                            }else if("扫描下方二维码成为我店会员".equals(memberMsg)) {
                                iWoyouService.setFontSize(textSize, null);
                                iWoyouService.setAlignment(1, null);//居中
                                iWoyouService.printText(memberMsg, null);
                                iWoyouService.lineWrap(1, null);
                            }else {
                                iWoyouService.setFontSize(textSize,null);
                                iWoyouService.setAlignment(1, null);
                                iWoyouService.printText(memberMsg, null);
                                iWoyouService.lineWrap(1, null);
                            }
                        }
                    }
                    //3.最后打印底部信息联
                    if(forCustomer) {
                        for (int i = 0; i < bottomData.length; i++) {
                            String bottomMsg = bottomData[i];
                            iWoyouService.setFontSize(textSize, null);
                            iWoyouService.setAlignment(1, null);//靠左
                            iWoyouService.printText(bottomMsg, null);
                            iWoyouService.lineWrap(1, null);
                        }
                        iWoyouService.lineWrap(1, null);
                    }else {
                        iWoyouService.setFontSize(textSize, null);
                        iWoyouService.setAlignment(0, null);//靠左
                        iWoyouService.printText("商家联", null);
                        iWoyouService.lineWrap(1, null);
                        iWoyouService.lineWrap(1, null);
                    }
                    iWoyouService.lineWrap(1, null);
                    iWoyouService.lineWrap(1,null);
                    iWoyouService.cutPaper(callback);
                } catch (RemoteException e) {
                    LogUtils.Log("remoteException==" + e.toString());
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 外卖接单的打印
     * @param iWoyouService
     * @param data  公共部分数据
     * @param callback
     */
    public void printWm(final IWoyouService iWoyouService, final List<WmPintData> data, final ICallback callback) {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if(data!=null) {
                        for (int i = 0; i < data.size(); i++) {
                            String wmMsg = data.get(i).getMsg();
                            if("--------------------------------".equals(wmMsg)) {
                                if(textSize==27) {
                                    wmMsg="--------------------------";
                                }else {
                                    wmMsg="---------------------";
                                }
                            }
                            iWoyouService.setFontSize(data.get(i).getTextSize(), null);
                            iWoyouService.setAlignment(0, null);//靠左
                            iWoyouService.printText(wmMsg, null);
                            iWoyouService.lineWrap(1, null);
                            iWoyouService.cutPaper(null);
                        }
                        if(data.size()>0) {
                            iWoyouService.lineWrap(1, null);
                            iWoyouService.lineWrap(1, callback);
                        }else {
                            iWoyouService.lineWrap(0, callback);
                        }
                    }
                } catch (RemoteException e) {
                    LogUtils.Log("remoteException==" + e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * pos机预结单
     * @param refundList wmCenterFormat("商品信息", "-", data, size27);
    addFormatWmTest(data, "商品名称", "单价","数量", "金额", size27);
     * @param refund_no
     */
    public List<WmPintData> formatRefundOrder(ArrayList<PosPrintBean.MsgBean.GoodsInfoBean> refundList, double totalAcc,String refund_no, String time) {
        List<WmPintData> data=new ArrayList<>();
        isbt=false;
        data.add(new WmPintData(Constant.STORE_P+Constant.STORE_M,size35,WmPintData.CENTER));
        wmCenterFormat("退款商品","-",data,size27);
        addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
        for (int i = 0; i < refundList.size(); i++) {
            addFormatWmTest(data,refundList.get(i).getName(), Utils.numdf.format(refundList.get(i).getRefund_price()),Utils.numdf.format(refundList.get(i).getRefund_num()),Utils.numdf.format(refundList.get(i).getRefund_num()*refundList.get(i).getRefund_price()),size27);
        }
        data.add(new WmPintData(line27));
        data.add(new WmPintData("退款时间："+time));
        data.add(new WmPintData("退款金额："+Utils.numdf.format(totalAcc)));
        data.add(new WmPintData("退货单号："+refund_no));
        data.add(new WmPintData("操作账号："+Constant.USER_ACCOUNT));
        data.add(new WmPintData(""));
        return data;
    }
    /**
     * 一菜一单单的打印
     * @param iWoyouService
     * @param data  公共部分数据
     * @param callback
     */
    public void printWmOneByOne(final IWoyouService iWoyouService, final List<List<WmPintData>> data, final ICallback callback) {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if(data!=null) {
                        for (int i = 0; i < data.size(); i++) {
                            List<WmPintData> wmPintData = data.get(i);
                            for (int j = 0; j < wmPintData.size(); j++) {
                                String msg = wmPintData.get(j).getMsg();
                                int alin = wmPintData.get(j).getAlin();
                                int textSize = wmPintData.get(j).getTextSize();

                                iWoyouService.setFontSize(textSize, null);
                                iWoyouService.setAlignment(alin, null);//靠左
                                iWoyouService.printText(msg, null);
                                iWoyouService.lineWrap(1, null);
                            }
                            iWoyouService.lineWrap(1, null);
                            iWoyouService.cutPaper(null);
                        }
                    }
                } catch (RemoteException e) {
                    LogUtils.Log("remoteException==" + e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 格式化添加打印数据
     */
    public void addFormat(List<String> data,String left,String center, double center2, String right) {


        String center_two = Utils.dropZero(center2+"");
        int chinaNum = Utils.getChinaNum(left);
        int nameLength = left.length() - chinaNum + chinaNum * 2;

        int sideLen=0;
        if(textSize==27) {
            sideLen = 25;
            if(isbt){
                sideLen=13;
            }
        }else if(textSize==35) {
            sideLen=8;
            if(isbt){
                sideLen=10;
            }
        }
        if(nameLength>=sideLen) {
            data.add(left);
            addFormat(data, "", center, Double.valueOf(center_two),right);
        }else {
            String space0="";
            String space1="";
            String space2="";

            int leftLength = (left + center).length();
            int leftChinaNum = Utils.getChinaNum(left + center);
            int leftOther = leftLength - leftChinaNum;

            int centerLength = center_two.length();
            int centerChinaNum = Utils.getChinaNum(center_two);
            int centerOther = centerLength - centerChinaNum;

            int rightLength = right.length();
            int rightChinaNum = Utils.getChinaNum(right);
            int rightOther = rightLength - rightChinaNum;

            int leftTotal=0;
            int centerTotal=0;
            int rightTotal=0;
            if(isbt) {
                /*leftTotal=21;
                centerTotal=9;
                rightTotal=11;*/
                leftTotal=16;
                centerTotal=7;
                rightTotal=9;
            }else {
                if(textSize==27) {
                    if(center.length()>6){
                        leftTotal=21;
                        centerTotal=9;
                        rightTotal=11;
                    }else{
                        leftTotal=25;
                        centerTotal=7;
                        rightTotal=9;
                    }
                }else if(textSize==35) {
                    leftTotal=11;
                    centerTotal=5;
                    rightTotal=5;
                }
            }

            int rightSpaces = rightTotal - rightChinaNum*2 - rightOther;
            int centerSpaces = centerTotal - centerChinaNum*2 - centerOther;
            int leftSpaces = leftTotal - leftChinaNum*2 - leftOther;

            for (int a = 0; a < leftSpaces; a++) {
                space0+=" ";
            }
            for (int b = 0; b < centerSpaces; b++) {
                space1+=" ";
            }
            for (int c = 0; c < rightSpaces; c++) {
                space2+=" ";
            }
            data.add(left+space0+center+space1+center_two+space2+right);
        }
    }

    public void addFormatTest(List<String> data,String left, String center, String center2, String right) {

        center2 = Utils.dropZero(center2);

        int chinaNum = Utils.getChinaNum(left);
        int nameLength = left.length() - chinaNum + chinaNum * 2;

        LogUtils.e("addFormatTest","left="+left+",left.size()="+left.length());

        int sideLen=0;
        if(textSize==27) {
            sideLen = 25;
            if(isbt){
                sideLen=13;
            }
        }else if(textSize==35) {
            sideLen=8;
            if(isbt){
                sideLen=10;
            }
        }
        if(nameLength>=sideLen) {
            data.add(left);
            addFormatTest(data, "", center, center2,right);
        }else {
            String space0="";
            String space1="";
            String space2="";

            int leftLength = (left + center).length();
            int leftChinaNum = Utils.getChinaNum(left + center);
            int leftOther = leftLength - leftChinaNum;

            int centerLength = center2.length();
            int centerChinaNum = Utils.getChinaNum(center2);
            int centerOther = centerLength - centerChinaNum;

            int rightLength = right.length();
            int rightChinaNum = Utils.getChinaNum(right);
            int rightOther = rightLength - rightChinaNum;

            int leftTotal=0;
            int centerTotal=0;
            int rightTotal=0;
            if(isbt) {
                leftTotal=16;
                centerTotal=7;
                rightTotal=9;
            }else {
                if(textSize==27) {
                    leftTotal=25;
                    centerTotal=7;
                    rightTotal=9;
                }else if(textSize==35) {
                    leftTotal=11;
                    centerTotal=5;
                    rightTotal=5;
                }
            }

            int rightSpaces = rightTotal - rightChinaNum*2 - rightOther;
            int centerSpaces = centerTotal - centerChinaNum*2 - centerOther;
            int leftSpaces = leftTotal - leftChinaNum*2 - leftOther;

            for (int a = 0; a < leftSpaces; a++) {
                space0+=" ";
            }
            for (int b = 0; b < centerSpaces; b++) {
                space1+=" ";
            }
            for (int c = 0; c < rightSpaces; c++) {
                space2+=" ";
            }
            data.add(left+space0+center+space1+center2+space2+right);
        }

    }

    /**
     * 格式化添加打印数据
     * @param data
     * @param left
     * @param center
     * @param right
     */
    public void addFormatWmTest(List<WmPintData> data, String left, String center, String center2, String right, int textSize) {

        center2 = Utils.dropZero(center2);

        int chinaNum = Utils.getChinaNum(left);
        int nameLength = left.length() - chinaNum + chinaNum * 2;

        int sideLen=0;
        if(textSize==27) {
            sideLen = 25;
            if(isbt){
                sideLen=13;
            }
        }else if(textSize==35) {
            sideLen=8;
            if(isbt){
                sideLen=10;
            }
        }

        if(nameLength>=sideLen) {
            data.add(new WmPintData(textSize,left));
            addFormatWmTest(data, "", center, center2,right, textSize);
        }else {
            LogUtils.e("addFormat----","nameLength<sideLen");
            String space0="";
            String space1="";
            String space2="";

            int leftLength = (left + center).length();
            int leftChinaNum = Utils.getChinaNum(left + center);
            int leftOther = leftLength - leftChinaNum;

            int centerLength = String.valueOf(center2).length();
            int centerChinaNum = Utils.getChinaNum(center2+"");
            int centerOther = centerLength - centerChinaNum;

            int rightLength = right.length();
            int rightChinaNum = Utils.getChinaNum(right);
            int rightOther = rightLength - rightChinaNum;

            int leftTotal=0;
            int centerTotal=0;
            int rightTotal=0;
            if(isbt) {
                leftTotal=16;
                centerTotal=7;
                rightTotal=9;
            }else {
                if(textSize==27) {
                    leftTotal=25;
                    centerTotal=7;
                    rightTotal=9;
                   /* if(center.length()>6){
                        leftTotal=14;
                        centerTotal=2;
                        rightTotal=10;
                    }else{
                        leftTotal=14;
                        centerTotal=6;
                        rightTotal=7;
                    }*/
                }else if(textSize==35) {
                    leftTotal=11;
                    centerTotal=5;
                    rightTotal=5;
                }
            }

            int rightSpaces = rightTotal - rightChinaNum*2 - rightOther;
            int centerSpaces = centerTotal - centerChinaNum*2 - centerOther;
            int leftSpaces = leftTotal - leftChinaNum*2 - leftOther;

            for (int a = 0; a < leftSpaces; a++) {
                space0+=" ";
            }
            for (int b = 0; b < centerSpaces; b++) {
                space1+=" ";
            }
            for (int c = 0; c < rightSpaces; c++) {
                space2+=" ";
            }
            data.add(new WmPintData(textSize,left+space0+center+space1+center2+space2+right));
        }
    }

    /**
     * 初始化下单结账的打印数据
     */
    public void initFOPrintData(UnPayDetailsBean orderBean, DiscountInfo discountBean, List<String> headerData, List<String> memberData,
                                String printJZTime, String tradenum, String payWay, String transaction_id, String memberFreeMoney, boolean bt) {
        LogUtils.Log("initPrintData:"+discountBean);
        headerData.clear();
        memberData.clear();
        this.isbt=bt;
        textSize=27;
            switch (orderBean.getMsg().getVip()) {
                //1.不是会员时判断memberData是否为空，不为空再添加
                case 0 :
                    if(orderBean!=null) {
                        totalPrintHead(false, orderBean, headerData, printJZTime, tradenum, payWay, transaction_id,memberFreeMoney);
                    }
                    if(discountBean!=null) {
                        totalPrintMember(discountBean,memberData);
                    }
                    break;
                //2.是会员时，无需添加会员信息
                case 1:
                    if(orderBean!=null) {
                        totalPrintHead(true,orderBean,headerData,printJZTime,tradenum,payWay,transaction_id,memberFreeMoney);
                    }
                    break;
            }
    }

    /**
     * 初始化会员数据
     * @param discountBean
     * @param memberData
     */
    private void totalPrintMemberTest(DiscountInfo discountBean, List<WmPintData> memberData) {
        //会员折扣
        String url = discountBean.getUrl();
        List<DiscountInfo.BalanceRulesBean> balance_rules = discountBean.getBalance_rules();
        List<DiscountInfo.DiscountRulesBean> discount_rules = discountBean.getDiscount_rules();

        addLine(memberData);
        //会员充值
        String storeWechat = discountBean.getStoreWechat();
        if(url!=null&&!TextUtils.isEmpty(url)) {
            memberData.add(new WmPintData("扫描下方二维码成为我店会员", size27, WmPintData.CENTER));
        }
        if(balance_rules!=null&&balance_rules.size()>0) {
           for (int i = 0; i < balance_rules.size(); i++) {
               String info = "充值" + balance_rules.get(i).getMoney() + "元赠送" + balance_rules.get(i).getSong()+"元";
               if(isbt) {
                   wmCenterFormat(info," ", memberData,size27);
               }else {
                   wmCenterFormat(info," ", memberData,size27);
               }
           }
        }
        if(discount_rules!=null&&discount_rules.size()>0) {
            for (int i = 0; i < discount_rules.size(); i++) {
                String youhui = "购物满" + discount_rules.get(i).getMoney() + "元享" + discount_rules.get(i).getRate() + "折优惠";
                if(isbt) {
                    wmCenterFormat(youhui," ", memberData,size27);
                }else {
                    wmCenterFormat(youhui," ", memberData,size27);
                }
//                memberData.add(youhui);
            }
        }
        if(discountBean.getDiscount_type()==1) {
            wmCenterFormat("购物时商品可享受会员价"," ", memberData,size27);
        }

        if(url!=null&&!TextUtils.isEmpty(url)) {
            memberData.add(new WmPintData(size27, "   "));
            memberData.add(new WmPintData(4, url, WmPintData.CENTER, WmPintData.QRCODE));
        }
        //附加会员充值打印信息
        if(storeWechat!=null&&!"".equals(storeWechat)){
            memberData.add(new WmPintData("会员可使用微信扫描下方二维码", size27, WmPintData.CENTER));
            memberData.add(new WmPintData("关注商家公众号后查询余额变更明细", size27, WmPintData.CENTER));
            memberData.add(new WmPintData(size27, "   "));
            memberData.add(new WmPintData(4, storeWechat, WmPintData.CENTER, WmPintData.QRCODE));
        }
    }
    /**
     * 初始化会员数据
     * @param discountBean
     * @param memberData
     */
    private void totalPrintMember(DiscountInfo discountBean, List<String> memberData) {
        //会员折扣
        String url = discountBean.getUrl();
        List<DiscountInfo.BalanceRulesBean> balance_rules = discountBean.getBalance_rules();
        List<DiscountInfo.DiscountRulesBean> discount_rules = discountBean.getDiscount_rules();
        //会员充值
        String storeWechat = discountBean.getStoreWechat();

        if(url!=null&&!TextUtils.isEmpty(url)) {
//            centerFormat("扫描下方二维码成为我店会员",memberData);
            memberData.add("扫描下方二维码成为我店会员");
        }
        if(balance_rules!=null&&balance_rules.size()>0) {
            for (int i = 0; i < balance_rules.size(); i++) {
                String info = "充值" + balance_rules.get(i).getMoney() + "元赠送" + balance_rules.get(i).getSong()+"元";
                if(isbt) {
                    centerFormat(info," ", memberData);
                }else {
                    centerFormat(info," ", memberData);
                }
//               memberData.add(info);
            }
        }
        if(discount_rules!=null&&discount_rules.size()>0) {
            for (int i = 0; i < discount_rules.size(); i++) {
                String youhui = "购物满" + discount_rules.get(i).getMoney() + "元享" + discount_rules.get(i).getRate() + "折优惠";
                if(isbt) {
                    centerFormat(youhui," ", memberData);
                }else {
                    centerFormat(youhui," ", memberData);
                }
//                memberData.add(youhui);
            }
        }

        if(discountBean.getDiscount_type()==1) {
            centerFormat("购物时商品可享受会员价"," ", memberData);
        }

        LogUtils.e("直接收银二维码：","url="+url);
        if(url!=null&&!TextUtils.isEmpty(url)) {
            memberData.add("   ");
            memberData.add(url);
        }
        LogUtils.e("附加会员充值打印信息：","storeWechat="+storeWechat);
        //附加会员充值打印信息
        if(storeWechat!=null&&!"".equals(storeWechat)){
            memberData.add("会员可使用微信扫描下方二维码");
            memberData.add("关注商家公众号后查询余额变更明细");
            memberData.add("   ");
            memberData.add(storeWechat);
        }
    }

    /**
     * 居中格式化添加数据
     * @param info
     * @param data
     */
    private void centerFormat(String info, String elseChar,List<String> data) {
        int totalLegth;
        if(isbt) {
            totalLegth=32;
        }else {
            if(textSize==27) {
                totalLegth=41;
            }else if(textSize==35) {
                totalLegth=21;
            }else {
                totalLegth=27;
            }
        }
        int length = info.length();
        int chinaNum = Utils.getChinaNum(info);
        int otherChar = length - chinaNum;
        int spacesNum=(totalLegth-otherChar-chinaNum*2)/2;
        String space="";
        for (int i = 0; i < spacesNum; i++) {
            space+=elseChar;
        }
        data.add(space+info+space);
    }
    /**
     * 居中格式化添加数据
     * @param info
     * @param data
     */
    private void wmCenterFormat(String info, String elseChar,List<WmPintData> data,int textSize) {
        int totalLegth;
        if(isbt) {
            totalLegth=32;
        }else {
            if(textSize==27) {
                totalLegth=41;
            }else if(textSize==35) {
                totalLegth=21;
            }else {
                totalLegth=27;
            }
        }
        int length = info.length();
        int chinaNum = Utils.getChinaNum(info);
        int otherChar = length - chinaNum;
        int spacesNum=(totalLegth-otherChar-chinaNum*2)/2;
        String space="";
        for (int i = 0; i < spacesNum; i++) {
            space+=elseChar;
        }
        data.add(new WmPintData(textSize,space+info+space));
    }

    /**
     * 初始化公共数据
     *  @param isMember 是否为会员
     * @param orderBean 订单内容
     * @param headerData 存放打印数据的集合
     * @param printJZTime 结账时间
     * @param tradenum 支付单号
     * @param payWay 支付方式
     */
    private void totalPrintHead(boolean isMember, UnPayDetailsBean orderBean, List<String> headerData,
                                String printJZTime, String tradenum, String payWay,String transaction_id,String memberFreeMoney) {
        List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms = orderBean.getMsg().getOrder_rooms();
        List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_entity = orderBean.getMsg().getOrder_goods();
        //1.公共头
        headerData.add(Constant.STORE_P + Constant.STORE_M);
        headerData.add("电话：" + Constant.STORE_TEL);
        //1.是会员
        if (order_rooms != null && order_rooms.size() > 0) {
            if("1".equals(orderBean.getMsg().getOrder_rooms().get(0).getRoom_type())){
                headerData.add("桌台："+order_rooms.get(0).getRoomname());
            }else{
                headerData.add("房间："+order_rooms.get(0).getRoomname());
            }
        } else {
            headerData.add("订单序号：" + orderBean.getMsg().getQuery_num());
        }
        if(isbt){
            headerData.add(line27);
        }else{
            headerData.add(line32);
        }
        headerData.add("下单时间:"+ Utils.getCurrentM(Long.valueOf(orderBean.getMsg().getAddtime() + "000")));
        headerData.add("结账时间:" + printJZTime);
        if (order_rooms != null && order_rooms.size() > 0) {
            headerData.add("订单序号:"+orderBean.getMsg().getQuery_num());
        }
        headerData.add("应付金额:"+orderBean.getMsg().getTotal_fee()+"元");
        if(orderBean.getMsg().getVoucherinfo() != null){
            headerData.add("优惠券：" + orderBean.getMsg().getVoucherinfo().getInfo());
            isMember = false;
        }
        if(isMember) {
            if(orderBean.getMsg().getVip_discount()!=null) {
                headerData.add("会员折扣:"+orderBean.getMsg().getVip_discount()+"折");
            }
        }
        headerData.add("实付金额:"+orderBean.getMsg().getPayment()+"元");
        headerData.add("支付方式:"+payWay);
        if(memberFreeMoney!=null&&!TextUtils.isEmpty(memberFreeMoney)) {
            headerData.add("会员卡余额:"+memberFreeMoney+"元");
        }
        headerData.add("商户单号:"+tradenum);
        LogUtils.Log("交易单号："+transaction_id);
        if(transaction_id!=null&&!TextUtils.isEmpty(transaction_id)&&!TextUtils.equals(transaction_id,"null")) {
            headerData.add("交易单号:"+transaction_id);
        }
        headerData.add("操作员:" + Constant.USER_ACCOUNT);
        if(orderBean.getMsg().getRemark()!=null&&!TextUtils.isEmpty(orderBean.getMsg().getRemark())) {
            headerData.add("备注信息:"+orderBean.getMsg().getRemark());
        }
        //房间列表
        //房间列表不为空且包含收费房间时添加房间信息
        if (order_rooms != null && order_rooms.size() > 0&&containChargeRoom(order_rooms)) {
                centerFormat("房间信息","-",headerData);
                for (int i = 0; i < order_rooms.size(); i++) {
                    UnPayDetailsBean.MsgBean.OrderRoomsBean roomsBean = order_rooms.get(i);
                    if(!"0".equals(roomsBean.getIs_billing())) {
                        headerData.add("房间名:"+roomsBean.getRoomname());
                        headerData.add("开始时间:"+Utils.getPtintTime(roomsBean.getStart_time()+"000"));
                        if("0".equals(roomsBean.getEnd_time())) {
                            headerData.add("结束时间:" + printJZTime);
                        }else {
                            headerData.add("结束时间:"+Utils.getPtintTime(roomsBean.getEnd_time()+"000"));
                        }
                        headerData.add("房费:"+roomsBean.getTrue_income()+"元");
                        headerData.add("               ");
                    }
                }
        }
        //商品列表
        if((order_entity!=null&&order_entity.size()>0)) {
            centerFormat("商品信息", "-", headerData);
            addFormatTest(headerData,"商品名称","单价","数量","金额");
            if (order_entity != null && order_entity.size() > 0) {
                for (int i = 0; i < order_entity.size(); i++) {
                    String name = order_entity.get(i).getGoods_name();

                    double num = order_entity.get(i).getGoods_num();
                    String twoDecimal = Utils.keepTwoDecimal(num + "");
                    Double formatNum = Double.valueOf(twoDecimal);

                    double total_fee = order_entity.get(i).getTotal_fee();
                    String formatFee = Utils.keepTwoDecimal(total_fee + "");

                    double goods_price = order_entity.get(i).getGoods_price();
                    double ori_price = order_entity.get(i).getGoods_ori_price();

                    addFormat(headerData, name, ori_price+"",formatNum, formatFee);

                    addvipPriceNotice(headerData, goods_price, ori_price,num);

                }
            }
        }
        if(isbt){
            headerData.add(line27);
        }else{
            headerData.add(line32);
        }
    }

    /**
     * @param headerData
     * @param goods_price 实际计算价格 可能为会员价
     * @param ori_price 原价
     * @param num
     */
    private void addvipPriceNotice(List<String> headerData, double goods_price, double ori_price,double num) {
        if(ori_price-goods_price>0) {
            headerData.add("原单价:"+ori_price+"元,会员优惠:"+ Utils.dropZero(Utils.keepTwoDecimal(ori_price*num-goods_price*num+""))+"元");
//            headerData.add("\n");
        }
    }
    /**
     * 判断是否有包含收费的房间
     * @param order_rooms
     * @return
     */
    private boolean containChargeRoom(List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms) {
        for (int i = 0; i < order_rooms.size(); i++) {
            if(Double.valueOf(order_rooms.get(i).getTrue_income())>0) {
                return true;
            }
        }
        return false;
    }

    public void formatUnPayPrintData(OrderBean localBean,UnPayDetailsBean unPayDetailsBean, List<String> headData, boolean isbt,boolean total){
        headData.clear();
        this.isbt=isbt;
        textSize=27;
        //来自第一次提交到总订单，并且不是只有房间
        List<GoodBean> order_entity=null;
        List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_goods=null;
        if(total&&unPayDetailsBean!=null) {
            order_goods=unPayDetailsBean.getMsg().getOrder_goods();
        }else if(localBean!=null){
            order_entity=localBean.getGoodList();
        }

        if(!total&&(order_entity == null || order_entity.size() <= 0)) {
            return;
        }
        if(total&&(order_goods==null||order_goods.size()<=0)) {
            return;
        }


        headData.add(Constant.STORE_P + Constant.STORE_M);
        headData.add("电话：" + STORE_TEL);
        headData.add("订单序号:" + Constant.CURRENT_ORDER_NUM);
        if(isbt){
            headData.add(line27);
        }else{
            headData.add(line32);
        }
        headData.add("下单时间:" + Utils.getCurrentMin());
        headData.add("操作员:" + Constant.USER_ACCOUNT);
        if(unPayDetailsBean.getMsg().getRemark()!=null&&!TextUtils.isEmpty(unPayDetailsBean.getMsg().getRemark())){
            headData.add("备注："+unPayDetailsBean.getMsg().getRemark());
        }
        centerFormat("商品信息", "-", headData);
        addFormatTest(headData, "商品名称","单价", "数量", "金额");
        if(total) {
            for (int i = 0; i < order_goods.size(); i++) {
                String name = order_goods.get(i).getGoods_name();

                double num = order_goods.get(i).getGoods_num();
                Double formatNum = Double.valueOf(Utils.keepTwoDecimal(num + ""));

                double total_fee = order_goods.get(i).getTotal_fee();
                String formatFee = Utils.keepTwoDecimal(total_fee + "");

                double goods_price = order_goods.get(i).getGoods_price();//实际计算价格，可能为会员价
                double ori_price = order_goods.get(i).getGoods_ori_price();//原始价格

                addFormat(headData, name, ori_price+"",formatNum, formatFee);
                addvipPriceNotice(headData, goods_price, ori_price,num);
            }
        }else {
            for (int i = 0; i < order_entity.size(); i++) {
                String name = order_entity.get(i).getGoodsName();
                double num = order_entity.get(i).getGoodsNum();
                Double formatNum = Double.valueOf(Utils.keepTwoDecimal(num + ""));


                double unit_price = order_entity.get(i).getGoodsPrice();
                double vipPrice = order_entity.get(i).getVipPrice();
                if(vipPrice<=0) {
                    vipPrice=unit_price;
                }
                double total_fee = vipPrice*num;
                String formatFee = Utils.keepTwoDecimal(total_fee + "");

                addFormat(headData, name, unit_price+"",formatNum, formatFee);
                if(unPayDetailsBean.getMsg().getVip()==1) {
                    addvipPriceNotice(headData,vipPrice,unit_price,num);
                }
            }
        }


        if(isbt){
            headData.add(line27);
        }else{
            headData.add(line32);
        }
    }
    public void formatUnPayPrintMerChantData(Context context,OrderBean localBean,UnPayDetailsBean unPayDetailsBean, List<List<WmPintData>> headData, boolean isbt,boolean total){
        headData.clear();
        this.isbt=isbt;
        textSize=27;
        SavedPrinter printer;
        if(isbt) {
            printer= (SavedPrinter) SaveUtils.getObject(context,Constant.BT_PRINTER);
        }else {
            printer= (SavedPrinter) SaveUtils.getObject(context,Constant.LOCAL_PRINTER);
        }
        if(printer==null) {
            return;
        }
        String printGroupId = printer.getPrintGroupId();
        List<GoodBean> goodList=null;
        List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_goods=null;
        if(total&&unPayDetailsBean!=null) {
            order_goods=unPayDetailsBean.getMsg().getOrder_goods();
        }else if(localBean!=null){
            goodList=localBean.getGoodList();
        }
        if(!total&&(goodList==null||goodList.size()<=0)) {
            return;
        }
        if(total&&(order_goods==null||order_goods.size()<=0)) {
            return;
        }
        if(total) {
            for (int i = 0; i < order_goods.size(); i++) {
                List<WmPintData> detailData=new ArrayList<>();
                UnPayDetailsBean.MsgBean.OrderGoodsBean orderGoodsBean = order_goods.get(i);
                if(TextUtils.equals("0",printGroupId)||TextUtils.equals(printGroupId, orderGoodsBean.getGroup_id())) {
                    detailData.add(new WmPintData(size40,orderGoodsBean.getGoods_name()));
                    detailData.add(new WmPintData(size40,"数量:"+orderGoodsBean.getGoods_num()));
                    detailData.add(new WmPintData(size40,"订单序号:"+Constant.CURRENT_ORDER_NUM));
                    if(unPayDetailsBean.getMsg().getRemark()!=null&&!TextUtils.isEmpty(unPayDetailsBean.getMsg().getRemark())) {
                        detailData.add(new WmPintData(size40,"备注:"+unPayDetailsBean.getMsg().getRemark()));
                    }
                    detailData.add(new WmPintData(size27,"下单时间:"+Utils.getCurrentMin()));
                }
                List<WmPintData> spaces=new ArrayList<>();
                headData.add(detailData);
                if(isbt) {
                    spaces.add(new WmPintData("\n"));
                    headData.add(spaces);
                }
            }
        }else {
            for (int i = 0; i < goodList.size(); i++) {
                List<WmPintData> detailData=new ArrayList<>();
                GoodBean goodBean = goodList.get(i);
                if(TextUtils.equals("0",printGroupId)||TextUtils.equals(printGroupId, goodBean.getGroupId())) {
                    detailData.add(new WmPintData(size40,goodBean.getGoodsName()));
                    detailData.add(new WmPintData(size40,"数量:"+goodBean.getGoodsNum()));
                    detailData.add(new WmPintData(size40,"订单序号:"+Constant.CURRENT_ORDER_NUM));
                    if(unPayDetailsBean.getMsg().getRemark()!=null&&!TextUtils.isEmpty(unPayDetailsBean.getMsg().getRemark())) {
                        detailData.add(new WmPintData(size40,"备注:"+unPayDetailsBean.getMsg().getRemark()));
                    }
                    detailData.add(new WmPintData(size27,"下单时间:"+Utils.getCurrentMin()));
                }
                List<WmPintData> spaces=new ArrayList<>();
                headData.add(detailData);
                if(isbt) {
                    spaces.add(new WmPintData("\n"));
                    headData.add(spaces);
                }
            }
        }


    }

    /**
     * 初始化打印服务
     */
    public void initService(Activity context, ServiceConnection connService) {
        LogUtils.Log("initService()");
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        context.getApplicationContext().startService(intent);
        context.bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }


    /**
     * 会员充值打印数据的初始化
     * @param tradenum
     * @param tradetime
     * @param orderAmount
     * @param payWay
     * @param headerData
     * @param memberData
     */
    public void initMCPrintData( String tradenum, String tradetime, double orderAmount, String payWay,String memberCardNo,double beforeLeft,double currentLeft,
                                 List<String> headerData, List<String> memberData,String transaction_id,boolean bt) {
        headerData.clear();
        memberData.clear();
        textSize=27;
        //1.公共部分
        headerData.add(Constant.STORE_P + Constant.STORE_M);
        headerData.add("电话：" + Constant.STORE_TEL);
        headerData.add("会员充值");
        if(bt){
            headerData.add(line32);
        }else{
            headerData.add(line27);
        }
        headerData.add("实付金额:" + orderAmount + "元");
        headerData.add("支付方式:" + payWay);
        headerData.add("付款时间:" + Utils.getCurrentM(Long.valueOf(tradetime) * 1000));
        headerData.add("会员卡号:" + memberCardNo);
        headerData.add("充值前余额:" + numDf.format(beforeLeft) + "元");
        headerData.add("充值金额:" + numDf.format(currentLeft-beforeLeft) + "元");
        headerData.add("充值后余额:" + numDf.format(currentLeft) + "元");
//        headerData.add("充值后余额：" + numDf.format(Constant.currentLeft ) + "元");
        headerData.add("商户单号:"+tradenum);
        if(transaction_id!=null&&!TextUtils.isEmpty(transaction_id)) {
            headerData.add("交易单号:"+transaction_id);
        }
        headerData.add("操作员:" + Constant.USER_ACCOUNT);
        if(bt){
            headerData.add(line32);
        }else{
            headerData.add(line27);
        }
        //2.会员部分
        memberData.add("会员可使用微信扫描下方二维码");
        memberData.add("关注商家公众号后查询余额变更明细");
        LogUtils.e("二维码链接","Constant.query_member_left="+Constant.query_member_left);
        memberData.add(Constant.query_member_left);//这里需要传入商家二维码链接,会员充值，识别会员时获取
    }

    public void formatPosDetailData(Context context,List<WmPintData> data, PosPrintBean posBean, DiscountInfo discountBean,boolean bt) {
        data.clear();
        this.isbt=bt;
        textSize=27;
        String printItems="45";
        /*if(isbt) {
            printItems = SpUtils.getInstance(context).getString(Constant.bt_print_permission, "");
        }else {
            printItems = SpUtils.getInstance(context).getString(Constant.print_permission, "");
        }*/
        if(printItems.contains("5")) {
            posDetailCommonPart(data, posBean, true, discountBean);
            if(discountBean!=null) {
                totalPrintMemberTest(discountBean, data);
            }
            addLine(data);

            data.add(new WmPintData("收银SaaS系统提供商：门店派", size27, WmPintData.CENTER));
            data.add(new WmPintData("联系电话：400-9600-567", size27, WmPintData.CENTER));
            data.add(new WmPintData("乐推微网址：tui.younle.com", size27, WmPintData.CENTER));
            addLine(data);
            data.add(new WmPintData(size27, "顾客联"));
            if(printItems.contains("4")) {
                data.add(new WmPintData(size27, "\n"));
                posDetailCommonPart(data, posBean, false, discountBean);
                if(!line27.equals(data.get(data.size()-1).getMsg()) && !line32.equals(data.get(data.size()-1).getMsg())) {
                    addLine(data);
                }
                data.add(new WmPintData("商家联",size27,WmPintData.LEFT));
                if(isbt) {
                    data.add(new WmPintData(size27,"\n"));
                    data.add(new WmPintData(size27,"\n"));
                }
            }
        }else {
            if(printItems.contains("4")) {
                posDetailCommonPart(data, posBean, false, discountBean);
                if(!line27.equals(data.get(data.size()-1).getMsg())||!line32.equals(data.get(data.size()-1).getMsg())) {
                    addLine(data);
                }
                data.add(new WmPintData("商家联", size27, WmPintData.LEFT));
                if(isbt) {
                    data.add(new WmPintData(size27,"\n"));
                    data.add(new WmPintData(size27,"\n"));
                }
            }else {
                data.clear();
            }
        }
    }

    private void posDetailCommonPart(List<WmPintData> data, PosPrintBean posBean,boolean needPhone, DiscountInfo discountBean) {
        data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M, size27, WmPintData.CENTER));
        if (needPhone) {
            data.add(new WmPintData("电话:" + STORE_TEL, size27, WmPintData.CENTER));
        }
        String dayOrderNum = posBean.getMsg().getDayOrderNum();
        if("会员充值".endsWith(dayOrderNum)) {
            data.add(new WmPintData(dayOrderNum,size50,WmPintData.CENTER));
        }else {
            String isRefund = posBean.getMsg().getIsRefund();
            if ("1".endsWith(isRefund)) {//退款订单
                boolean isRemark=false;
                if (posBean.getMsg().getRoomName() != null && !TextUtils.isEmpty(posBean.getMsg().getRoomName())) {
                    data.add(new WmPintData(posBean.getMsg().getRoomName(), size35, WmPintData.CENTER));
                } else {
                    if (dayOrderNum.contains("（已退款）")) {
                        dayOrderNum = dayOrderNum.substring(0, dayOrderNum.indexOf("（已退款）"));
                    }
                    if (dayOrderNum.contains("（已标记退款）")) {
                        isRemark=true;
                        dayOrderNum = dayOrderNum.substring(0, dayOrderNum.indexOf("（已标记退款）"));
                    }
                    data.add(new WmPintData("订单序号：" + dayOrderNum, size50, WmPintData.CENTER));
                }
                if(isRemark){
                    data.add(new WmPintData("（已标记退款）", size50, WmPintData.CENTER));
                }else{
                    data.add(new WmPintData("（已退款）", size50, WmPintData.CENTER));
                }
            } else {//非退款订单
                if (posBean.getMsg().getRoomName() != null && !TextUtils.isEmpty(posBean.getMsg().getRoomName())) {
                    data.add(new WmPintData(posBean.getMsg().getRoomName(), size35, WmPintData.CENTER));
                } else {
                    if (dayOrderNum.contains("（已退款）")) {
                        dayOrderNum = dayOrderNum.substring(0, dayOrderNum.indexOf("（已退款）"));
                    }
                    if (dayOrderNum.contains("（已标记退款）")) {
                        dayOrderNum = dayOrderNum.substring(0, dayOrderNum.indexOf("（已标记退款）"));
                    }
                    data.add(new WmPintData("订单序号：" + dayOrderNum, size50, WmPintData.CENTER));
                }
            }
        }
        addLine(data);
        data.add(new WmPintData("下单时间:" + posBean.getMsg().getAddTime(), size27, WmPintData.LEFT));
        data.add(new WmPintData("结账时间:" + posBean.getMsg().getPayTime(), size27, WmPintData.LEFT));
        data.add(new WmPintData("应付金额:" + posBean.getMsg().getShouldPayMoney() + "元", size27, WmPintData.LEFT));
        data.add(new WmPintData("实付金额:" + posBean.getMsg().getFactPayMoney() + "元", size27, WmPintData.LEFT));
        if (!"1".equals(posBean.getMsg().getIsWxapp())) {
            if(needPhone){
                String msg = posBean.getMsg().getPayType();
                switch (posBean.getMsg().getPayType()){
                    case "刷卡收款(记账)":
                        msg = "刷卡收款";
                        break;
                    case "现金收款(记账)":
                        msg = "现金收款";
                        break;
                    case "微信收款(记账)":
                        msg = "微信收款";
                        break;
                    case "支付宝收款(记账)":
                        msg = "支付宝收款";
                        break;
                }
                data.add(new WmPintData("支付方式:" + msg, size27, WmPintData.LEFT));
            }else{
                data.add(new WmPintData("支付方式:" + posBean.getMsg().getPayType(), size27, WmPintData.LEFT));
            }
            data.add(new WmPintData("商户单号:" + posBean.getMsg().getOrderNo(), size27, WmPintData.LEFT));
            if(posBean.getMsg().getTransaction_id()!=null && !"".equals(posBean.getMsg().getTransaction_id())){
                data.add(new WmPintData("交易单号:" + posBean.getMsg().getTransaction_id(), size27, WmPintData.LEFT));
            }
            data.add(new WmPintData("操作员:" + Constant.USER_ACCOUNT, size27, WmPintData.LEFT));
        }
        if (posBean.getMsg().getOrderNote() != null && !TextUtils.isEmpty(posBean.getMsg().getOrderNote())) {
            data.add(new WmPintData("备注:" + posBean.getMsg().getOrderNote(), size27, WmPintData.LEFT));
        }

        List<PosPrintBean.MsgBean.GoodsInfoBean> goodsInfo = posBean.getMsg().getGoodsInfo();
        if (goodsInfo != null&&goodsInfo.size()>0) {
            if(!"会员充值".endsWith(dayOrderNum)){
                wmCenterFormat("商品信息", "-", data, size27);
                addFormatWmTest(data, "商品名称", "单价","数量", "金额", size27);
                for (int i = 0; i < goodsInfo.size(); i++) {
                    PosPrintBean.MsgBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(i);
                    if ("0".equals(goodsInfoBean.getType()) || "1".equals(goodsInfoBean.getType()) || "2".equals(goodsInfoBean.getType())) {
                        String num = goodsInfoBean.getNum();
                        String formatNum = Utils.keepTwoDecimal(num);
                        addFormatWmTest(data, goodsInfoBean.getName(), goodsInfoBean.getPrice()+"",
                                formatNum, numDf.format(Double.valueOf(num) * goodsInfoBean.getPrice()), size27);
                    }
                }
            }
        }
        //退款
        String isRefund = posBean.getMsg().getIsRefund();
        if("1".equals(isRefund)) {
            addLine(data);
            data.add(new WmPintData("退款时间:"+posBean.getMsg().getRefundTime(),size27,WmPintData.LEFT));
            data.add(new WmPintData("退款金额:"+posBean.getMsg().getRefundMoney()+"元",size27,WmPintData.LEFT));
            data.add(new WmPintData("操作账号:"+posBean.getMsg().getRefundOperator(),size27,WmPintData.LEFT));
            if(!needPhone) {
                addLine(data);
            }
        }
    }

    private void addLine(List<WmPintData> data) {
        if(isbt){
            data.add(new WmPintData(line27,size27,WmPintData.CENTER));
        }else{
            data.add(new WmPintData(line32,size27,WmPintData.CENTER));
        }
    }

    private void posDetailCommonPartForApplet(List<WmPintData> data, PosPrintBean posBean,boolean needPhone, DiscountInfo discountBean) {
        data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M,size27,WmPintData.CENTER));
        if(needPhone) {
            data.add(new WmPintData("电话:" + STORE_TEL,size27,WmPintData.CENTER));
        }
        if(posBean.getMsg().getRoomName()!=null&&!"".equals(posBean.getMsg().getRoomName())){
            data.add(new WmPintData(posBean.getMsg().getRoomName(),50,WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + posBean.getMsg().getDayOrderNum(),50,WmPintData.CENTER));
        }
        data.add(new WmPintData("顾客自助点单",size27,WmPintData.CENTER));
        //data.add(new WmPintData(size27, line27));
        addLine(data);
        data.add(new WmPintData("下单时间:" + posBean.getMsg().getAddTime(),size27,WmPintData.LEFT));
        data.add(new WmPintData("结账时间:" + posBean.getMsg().getPayTime(),size27,WmPintData.LEFT));
        data.add(new WmPintData("应付金额:"+posBean.getMsg().getShouldPayMoney()+"元",size27,WmPintData.LEFT));
        data.add(new WmPintData("实付金额:" + posBean.getMsg().getFactPayMoney() + "元", size27, WmPintData.LEFT));
        if(posBean.getMsg().getOrderNote()!=null&&!TextUtils.isEmpty(posBean.getMsg().getOrderNote())) {
            data.add(new WmPintData("备注:" + posBean.getMsg().getOrderNote(), size27, WmPintData.LEFT));
        }
        List<PosPrintBean.MsgBean.GoodsInfoBean> goodsInfo = posBean.getMsg().getGoodsInfo();
        if(goodsInfo!=null){
            wmCenterFormat("商品信息","-", data,size27);
            addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
            for (int i = 0; i < goodsInfo.size(); i++) {
                PosPrintBean.MsgBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(i);
                if("0".equals(goodsInfoBean.getType())||"1".equals(goodsInfoBean.getType())||"2".equals(goodsInfoBean.getType())) {
                    addFormatWmTest(data, goodsInfoBean.getName(),goodsInfoBean.getPrice()+"" ,goodsInfoBean.getNum(), numDf.format(Double.valueOf(goodsInfoBean.getNum()) * goodsInfoBean.getPrice()),size27);
                }
            }
        }
    }

    public  void newPosPrint(final IWoyouService iWoyouService, final List<WmPintData> printData, final boolean needBottomSpace, final ICallback callback
                                ) {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i < printData.size(); i++) {
                                WmPintData wmPintData = printData.get(i);
                                iWoyouService.setAlignment(wmPintData.getAlin(),null);
                                iWoyouService.setFontSize(wmPintData.getTextSize(),null);

                                if(wmPintData.getType()==1) {
                                    iWoyouService.printQRCode(wmPintData.getMsg(), 4, 3, null);
                                }else if(wmPintData.getType()==2) {
                                    iWoyouService.printBarCode(wmPintData.getMsg(), 7, 50,3, 2,null);
                                }else {
                                    iWoyouService.printText(wmPintData.getMsg(),null);
                                }
                                iWoyouService.lineWrap(1,null);
                                if(TextUtils.equals(wmPintData.getMsg(),"顾客联")) {
                                    iWoyouService.cutPaper(null);
                                }
                            }
                            if(needBottomSpace) {
                                iWoyouService.lineWrap(1, null);
                                iWoyouService.lineWrap(1, null);
                                iWoyouService.lineWrap(1, null);
                            }
                            iWoyouService.cutPaper(callback);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                     }
        });
    }
    /**
     * 打印汇总数据
     * @param printTotalBean
     * @param start
     * @param end
     */
    public List<WmPintData> initTotalPrintData(PrintTotalBean printTotalBean, String start, String end, boolean isbt) {
        this.isbt=isbt;
        List<WmPintData> data=new ArrayList<>();
        //收款渠道
        PrintTotalBean.PricechannelBean channel = printTotalBean.getPricechannel();
        //会员充值
        PrintTotalBean.VippriceBean viplog = printTotalBean.getVipprice();
        //商品销售记录
        PrintTotalBean.GoodssaleBean goodssale = printTotalBean.getGoodssale();

        data.add(new WmPintData("经营数据汇总",size35,WmPintData.CENTER));
        data.add(new WmPintData(size27," "));
        data.add(new WmPintData("商户名称:"+Constant.STORE_P+Constant.STORE_M,size27,WmPintData.LEFT));
        data.add(new WmPintData(size27,"区间:"+start+"至"+end));
        data.add(new WmPintData(size27,"打印时间:"+Utils.getCurrentM(System.currentTimeMillis())));
        wmCenterFormat("收入概况","-",data,size27);

        data.add(new WmPintData(size27, "微信支付"));
        data.add(new WmPintData(size27,"订单数:"+ channel.getWechatnum()+"  总金额:"+channel.getWechatpay()));
        data.add(new WmPintData(size27," "));

        data.add(new WmPintData(size27, "支付宝"));
        data.add(new WmPintData(size27,"订单数:"+channel.getAlinum()+"  总金额:"+channel.getAlipay()));
        data.add(new WmPintData(size27," "));

        data.add(new WmPintData(size27, "现金收款"));
        data.add(new WmPintData(size27,"订单数:"+channel.getCashnum()+"  总金额:"+channel.getCashpay()));
        data.add(new WmPintData(size27," "));
        data.add(new WmPintData(size27, "刷卡收款"));
        data.add(new WmPintData(size27,"订单数:"+channel.getSwingnum()+"  总金额:"+channel.getSwingpay()));
        data.add(new WmPintData(size27," "));
        if(isbt) {
            data.add(new WmPintData(size27," "));
        }
        data.add(new WmPintData(size27, "收入汇总"));
        data.add(new WmPintData(size27, "订单数:" + channel.getTotolnum() + "  总金额:" + channel.getTotolpay()));
        if(Constant.OPENED_PERMISSIONS.contains("2")) {
            wmCenterFormat("商品销售概况","-",data,size27);
            List<PrintTotalBean.GoodssaleBean.SaletypeinfoBean> saletypeinfo = goodssale.getSaletypeinfo();
            if(saletypeinfo!=null&&saletypeinfo.size()>0) {
                for (int i = 0; i < saletypeinfo.size(); i++) {
                    PrintTotalBean.GoodssaleBean.SaletypeinfoBean saletypeinfoBean = saletypeinfo.get(i);
                    if(saletypeinfoBean!=null) {
                        data.add(new WmPintData(size27, saletypeinfoBean.getTypename()));
                        data.add(new WmPintData(size27,"售出商品数:"+saletypeinfoBean.getNum()+"  总金额:"+saletypeinfoBean.getOriprice()));
                        data.add(new WmPintData(size27," "));
                    }
                }
            }
            data.add(new WmPintData(size27, "商品售出汇总"));
            data.add(new WmPintData(size27,"售出总数:"+goodssale.getSaletotal().getTotalnum()+"  总金额:"+goodssale.getSaletotal().getTotaloriprice()));
            data.add(new WmPintData(size27,"实收金额:"+goodssale.getSaletotal().getTotalprice()));
            data.add(new WmPintData(size27," "));

        }
        if(Constant.OPENED_PERMISSIONS.contains("4")||Constant.OPENED_PERMISSIONS.contains("8")) {
            wmCenterFormat("会员充值与消费","-",data,size27);
            data.add(new WmPintData(size27, "会员储值总额"));
            data.add(new WmPintData(size27,"订单数:"+ viplog.getGetnum()));
            data.add(new WmPintData(size27,"总充值:"+viplog.getGettotal()+" 总赠送:"+viplog.getTotalgive()));
            data.add(new WmPintData(size27," "));

            data.add(new WmPintData(size27, "储值消费总额"));
            data.add(new WmPintData(size27,"订单数:"+viplog.getPaynum()+"  总金额:"+viplog.getPaytotal()));
            data.add(new WmPintData(size27," "));
        }
        return data;
    }

    //格式化商家联的数据
    private void appletSellerGoods(List<WmPintData> data, AppLetReceiveBean appLetBean) {
        /*if(isbt){
            data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M,size27,WmPintData.CENTER));
        }else{
            wmCenterFormat(Constant.STORE_P + Constant.STORE_M," ",data,size27);
        }*/
        wmCenterFormat(Constant.STORE_P + Constant.STORE_M," ",data,size27);
        if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
            wmCenterFormat(appLetBean.getRoomName()," ",data,size35);
        }else{
            wmCenterFormat("订单序号:" + appLetBean.getDayOrderNum()," ",data,size35);
        }
        wmCenterFormat("顾客自助点单"," ",data,size27);
        data.add(new WmPintData(size27, line27));
        data.add(new WmPintData("下单时间:" + appLetBean.getAddTime(),size27,WmPintData.LEFT));
        if(appLetBean.getPayTime()!=null&&!"".equals(appLetBean.getPayTime())&&!"0".equals(appLetBean.getPayTime())){
            data.add(new WmPintData("结账时间:" + appLetBean.getPayTime(),size27,WmPintData.LEFT));
        }
        data.add(new WmPintData("应付金额:"+appLetBean.getShouldPayMoney()+"元",size27,WmPintData.LEFT));
        data.add(new WmPintData("实付金额:" + appLetBean.getFactPayMoney() + "元", size27, WmPintData.LEFT));
        if(appLetBean.getOrderNote()!=null&&!TextUtils.isEmpty(appLetBean.getOrderNote())) {
            data.add(new WmPintData("备注:" + appLetBean.getOrderNote(), size27, WmPintData.LEFT));
        }
        //商品信息
        formateGroupGoods(appLetBean,data);
    }

    //格式化商户联分组商品数据addGroupGoodsInfo
    private void formateGroupGoods(AppLetReceiveBean appLetBean, List<WmPintData> data) {
        List<AppLetReceiveBean.GoodsInfoBean> goodsInfo = appLetBean.getGoodsInfo();
        List<AppLetReceiveBean.groupInfoBean> groupInfo = appLetBean.getGroupInfo();
        //分组情况：有分组商品还有未分组商品
        if(groupInfo!=null&&groupInfo.size()>0){
            for(int i=0;i<groupInfo.size();i++){
                if(groupInfo.get(i).getId()!=null || !"".equals(groupInfo.get(i).getId()) || !"0".equals(groupInfo.get(i).getId())){
                    wmCenterFormat(groupInfo.get(i).getName(),"-", data,size27);
                    addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
                    for(int j=0;j<goodsInfo.size();j++){
                        if(groupInfo.get(i).getId().equals(goodsInfo.get(j).getGroup_id())){
                            addFormatWmTest(data, goodsInfo.get(j).getName(), goodsInfo.get(j).getPrice()+"",Utils.dropZero(goodsInfo.get(j).getNum()), numDf.format(Double.valueOf(goodsInfo.get(j).getNum()) * goodsInfo.get(j).getPrice()),size27);
                        }
                    }
                }
            }
            //接着处理未分组商品：首先判断是否还有未分组商品；进而添加
            boolean hasNoGroupGoods = false;
            for(int n=0;n<goodsInfo.size();n++){
                if(goodsInfo.get(n).getGroup_id()==null||"".equals(goodsInfo.get(n).getGroup_id())||"0".equals(goodsInfo.get(n).getGroup_id())){
                    hasNoGroupGoods = true;
                    break;
                }
            }
            if(hasNoGroupGoods){
                wmCenterFormat("其他","-", data,size27);
                addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
                for(int n=0;n<goodsInfo.size();n++){
                    if(goodsInfo.get(n).getGroup_id()==null||"".equals(goodsInfo.get(n).getGroup_id())||"0".equals(goodsInfo.get(n).getGroup_id())){
                        addFormatWmTest(data, goodsInfo.get(n).getName(),goodsInfo.get(n).getPrice()+"" ,Utils.dropZero(goodsInfo.get(n).getNum()), numDf.format(Double.valueOf(goodsInfo.get(n).getNum()) * goodsInfo.get(n).getPrice()),size27);
                    }
                }
            }
        }else{
            //所有商品没分组
            wmCenterFormat("商品信息","-", data,size27);
            addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
            for(int n=0;n<goodsInfo.size();n++){
                addFormatWmTest(data, goodsInfo.get(n).getName(), goodsInfo.get(n).getPrice()+"",Utils.dropZero(goodsInfo.get(n).getNum()), numDf.format(Double.valueOf(goodsInfo.get(n).getNum()) * goodsInfo.get(n).getPrice()),size27);
            }
        }
    }

    //格式化顾客联的数据
    private void appletCustomerGoods(List<WmPintData> data, AppLetReceiveBean appLetBean,boolean needPhone) {
        /*if(isbt){
            data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M,size27,WmPintData.CENTER));
        }else{
            wmCenterFormat(Constant.STORE_P + Constant.STORE_M," ",data,size27);
        }*/
        wmCenterFormat(Constant.STORE_P + Constant.STORE_M," ",data,size27);
        if(needPhone) {
            /*if(isbt){
                data.add(new WmPintData("电话:" + Constant.STORE_TEL,size27,WmPintData.CENTER));
            }else{
                wmCenterFormat("电话:" + Constant.STORE_TEL," ",data,size27);
            }*/
            wmCenterFormat("电话:" + STORE_TEL," ",data,size27);
        }
        if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
            wmCenterFormat(appLetBean.getRoomName()," ",data,size35);
        }else{
            wmCenterFormat("订单序号:" + appLetBean.getDayOrderNum()," ",data,size35);
        }
        wmCenterFormat("顾客自助点单"," ",data,size27);
        data.add(new WmPintData(size27, line27));
        data.add(new WmPintData("下单时间:" + appLetBean.getAddTime(),size27,WmPintData.LEFT));
        if(appLetBean.getPayTime()!=null&&!"".equals(appLetBean.getPayTime())&&!"0".equals(appLetBean.getPayTime())){
            data.add(new WmPintData("结账时间:" + appLetBean.getPayTime(),size27,WmPintData.LEFT));
        }
        data.add(new WmPintData("应付金额:"+appLetBean.getShouldPayMoney()+"元",size27,WmPintData.LEFT));
        data.add(new WmPintData("实付金额:" + appLetBean.getFactPayMoney() + "元", size27, WmPintData.LEFT));
        if(appLetBean.getOrderNote()!=null&&!TextUtils.isEmpty(appLetBean.getOrderNote())) {
            data.add(new WmPintData("备注:" + appLetBean.getOrderNote(), size27, WmPintData.LEFT));
        }
        wmCenterFormat("商品信息","-", data,size27);
        List<AppLetReceiveBean.GoodsInfoBean> goodsInfo = appLetBean.getGoodsInfo();
        if(appletWhichContain(goodsInfo,"2")||appletWhichContain(goodsInfo,"1")||appletWhichContain(goodsInfo,"0")) {
            addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
            for (int i = 0; i < goodsInfo.size(); i++) {
                AppLetReceiveBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(i);
                if("0".equals(goodsInfoBean.getType())||"1".equals(goodsInfoBean.getType())||"2".equals(goodsInfoBean.getType())) {
                    addFormatWmTest(data, goodsInfoBean.getName(),goodsInfoBean.getPrice()+"" ,goodsInfoBean.getNum(),
                            numDf.format(Double.valueOf(goodsInfoBean.getNum()) * goodsInfoBean.getPrice()),size27);
                }
            }
        }
        //二维码投放信息
        AppLetReceiveBean.TicketInfoBean ticketInfo = appLetBean.getTicketInfo();
        if(ticketInfo!=null) {
            data.add(new WmPintData(size27, line27));
            chargeInfoQRCode(ticketInfo, data);
        }
    }

    private boolean appletWhichContain(List<AppLetReceiveBean.GoodsInfoBean> goodsInfo,String which) {
        for (int i = 0; i < goodsInfo.size(); i++) {
            if(which.equals(goodsInfo.get(i).getType())) {
                return true;
            }
        }
        return false;
    }

    //小程序单列表详情页商家联分组
    private void posDetailForAppletSeller(List<WmPintData> data, PosPrintBean posBean,boolean needPhone) {

        data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M,size27,WmPintData.CENTER));
        if(needPhone) {
            data.add(new WmPintData("电话:" + STORE_TEL,size27,WmPintData.CENTER));
        }
        if(posBean.getMsg().getRoomName()!=null&&!"".equals(posBean.getMsg().getRoomName())){
            data.add(new WmPintData(posBean.getMsg().getRoomName(),50,WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + posBean.getMsg().getDayOrderNum(),50,WmPintData.CENTER));
        }
        data.add(new WmPintData("顾客自助点单",size27,WmPintData.CENTER));
        //data.add(new WmPintData(size27, line27));
        addLine(data);
        data.add(new WmPintData("下单时间:" + posBean.getMsg().getAddTime(),size27,WmPintData.LEFT));
        data.add(new WmPintData("结账时间:" + posBean.getMsg().getPayTime(),size27,WmPintData.LEFT));
        data.add(new WmPintData("应付金额:"+posBean.getMsg().getShouldPayMoney()+"元",size27,WmPintData.LEFT));
        data.add(new WmPintData("实付金额:" + posBean.getMsg().getFactPayMoney() + "元", size27, WmPintData.LEFT));
        data.add(new WmPintData("操作员:" + Constant.USER_ACCOUNT, size27, WmPintData.LEFT));
        if(posBean.getMsg().getOrderNote()!=null&&!TextUtils.isEmpty(posBean.getMsg().getOrderNote())) {
            data.add(new WmPintData("备注:" + posBean.getMsg().getOrderNote(), size27, WmPintData.LEFT));
        }
        //添加分组商品信息
        addGroupGoodsInfo(posBean,data);
    }

    private void addGroupGoodsInfo(PosPrintBean posBean, List<WmPintData> data) {

        List<PosPrintBean.MsgBean.GoodsInfoBean> goodsInfo = posBean.getMsg().getGoodsInfo();
        List<PosPrintBean.MsgBean.GroupInfoBean> groupInfo = posBean.getMsg().getGroupInfo();
        if(goodsInfo!=null){
            //分组情况：有分组商品还有未分组商品
            if(groupInfo!=null&&groupInfo.size()>0){
                for(int i=0;i<groupInfo.size();i++){
                    if(groupInfo.get(i).getId()!=null || !"".equals(groupInfo.get(i).getId()) || !"0".equals(groupInfo.get(i).getId())){
                        wmCenterFormat(groupInfo.get(i).getName(),"-", data,size27);
                        addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
                        for(int j=0;j<goodsInfo.size();j++){
                            if(groupInfo.get(i).getId().equals(goodsInfo.get(j).getGroup_id())){
                                addFormatWmTest(data, goodsInfo.get(j).getName(),goodsInfo.get(j).getPrice()+"" ,goodsInfo.get(j).getNum(), numDf.format(Double.valueOf(goodsInfo.get(j).getNum()) * goodsInfo.get(j).getPrice()),size27);
                            }
                        }
                    }
                }
                //接着处理未分组商品：首先判断是否还有未分组商品；进而添加
                boolean hasNoGroupGoods = false;
                for(int n=0;n<goodsInfo.size();n++){
                    if(goodsInfo.get(n).getGroup_id()==null||"".equals(goodsInfo.get(n).getGroup_id())||"0".equals(goodsInfo.get(n).getGroup_id())){
                        hasNoGroupGoods = true;
                        break;
                    }
                }
                if(hasNoGroupGoods){
                    wmCenterFormat("其他","-", data,size27);
                    addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
                    for(int n=0;n<goodsInfo.size();n++){
                        if(goodsInfo.get(n).getGroup_id()==null||"".equals(goodsInfo.get(n).getGroup_id())||"0".equals(goodsInfo.get(n).getGroup_id())){
                            addFormatWmTest(data, goodsInfo.get(n).getName(),goodsInfo.get(n).getPrice()+"" ,Utils.dropZero(goodsInfo.get(n).getNum()), numDf.format(Double.valueOf(goodsInfo.get(n).getNum()) * goodsInfo.get(n).getPrice()),size27);
                        }
                    }
                }
            }else{
                //所有商品没分组
                wmCenterFormat("商品信息","-", data,size27);
                addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
                for(int n=0;n<goodsInfo.size();n++){
                    addFormatWmTest(data, goodsInfo.get(n).getName(), goodsInfo.get(n).getPrice()+"",Utils.dropZero(goodsInfo.get(n).getNum()), numDf.format(Double.valueOf(goodsInfo.get(n).getNum()) * goodsInfo.get(n).getPrice()),size27);
                }
            }
        }
    }

    private void chargeInfoQRCode(AppLetReceiveBean.TicketInfoBean ticketInfo, List<WmPintData> data) {

        //会员折扣
        String url = ticketInfo.getUrl();
        List<AppLetReceiveBean.TicketInfoBean.BalanceRulesBean> balance_rules = ticketInfo.getBalance_rules();
        List<AppLetReceiveBean.TicketInfoBean.DiscountRulesBean> discount_rules = ticketInfo.getDiscount_rules();

        if(url!=null&&!TextUtils.isEmpty(url)) {
            wmCenterFormat("扫描下方二维码成为我店会员"," ",data,size27);
        }

        if(balance_rules!=null&&balance_rules.size()>0) {
            for (int i = 0; i < balance_rules.size(); i++) {
                String info = "充值" + balance_rules.get(i).getMoney() + "元赠送" + balance_rules.get(i).getSong()+"元";
                /*if(isbt) {
                    centerFormat(info," ", memberData);
                }else {
                    centerFormat(info," ", memberData);
                }*/
                wmCenterFormat(info," ",data,size27);
//               memberData.add(info);
            }
        }
        if(discount_rules!=null&&discount_rules.size()>0) {
            for (int i = 0; i < discount_rules.size(); i++) {
                String youhui = "购物满" + discount_rules.get(i).getMoney() + "元享" + discount_rules.get(i).getRate() + "折优惠";
                /*if(isbt) {
                    centerFormat(youhui," ", memberData);
                }else {
                    centerFormat(youhui," ", memberData);
                }*/
                wmCenterFormat(youhui," ",data,size27);
//                memberData.add(youhui);
            }
        }
        if(url!=null&&!TextUtils.isEmpty(url)) {
            /*memberData.add("   ");
            memberData.add(url);*/
            wmCenterFormat("   "," ",data,size27);
            data.add(new WmPintData(7, url, WmPintData.CENTER, WmPintData.QRCODE));
        }
    }

    public void startLoclPrint(Activity mActivity, IWoyouService iWoyouService, String localPrinterPermission, List<String> headerData, List<String> memberData, final PrintSecond printSecondCallBack) {

        if (iWoyouService != null) {
            if(localPrinterPermission!=null&&localPrinterPermission.contains("5")) {
                Utils.showWaittingDialog(mActivity,"小票打印中...");
                if(localPrinterPermission.contains("4")) {//同时打印
                    PrintUtils.getInstance().zsmcPrint(iWoyouService, headerData, true, memberData, 28, new ICallback.Stub() {
                        @Override
                        public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {
                            if(isSuccess) {
                                printSecondCallBack.doPrintSecond();
                            }
                        }
                    });
                }else {//只打印顾客联
                    PrintUtils.getInstance().zsmcPrint(iWoyouService, headerData, true, memberData, 28, new ICallback.Stub() {
                        @Override
                        public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {
                            if(isSuccess) {
                                Utils.dismissWaittingDialog();
                            }
                        }
                    });
                }
            }else {
                if(localPrinterPermission!=null&&localPrinterPermission.contains("4")) {//只打印商家联
                    Utils.showWaittingDialog(mActivity,"小票打印中...");
                    printSecondCallBack.doPrintSecond();
                }
            }
        }
    }
    public  void printSecond(IWoyouService iWoyouService,  List<String> headerData) {
        headerData.remove(1);
        PrintUtils.getInstance().zsmcPrint(iWoyouService, headerData, false, null, 22, new ICallback.Stub() {
            @Override
            public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {
                Utils.dismissWaittingDialog();
            }
        });
    }

}