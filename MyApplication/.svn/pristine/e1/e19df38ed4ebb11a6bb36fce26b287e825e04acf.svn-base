package com.younle.younle624.myapplication.utils.printmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.younle.younle624.myapplication.application.ThreadPoolManager;
import com.younle.younle624.myapplication.constant.Constant;
import com.younle.younle624.myapplication.domain.AppLetReceiveBean;
import com.younle.younle624.myapplication.domain.PrintTotalBean;
import com.younle.younle624.myapplication.domain.UnPayDetailsBean;
import com.younle.younle624.myapplication.domain.orderbean.PosPrintBean;
import com.younle.younle624.myapplication.domain.paybean.DiscountInfo;
import com.younle.younle624.myapplication.domain.printsetting.SavedPrinter;
import com.younle.younle624.myapplication.domain.waimai.ElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.MtOrderDetail;
import com.younle.younle624.myapplication.domain.waimai.NewElmOrderBean;
import com.younle.younle624.myapplication.domain.waimai.WmPintData;
import com.younle.younle624.myapplication.utils.LogUtils;
import com.younle.younle624.myapplication.utils.SaveUtils;
import com.younle.younle624.myapplication.utils.SpUtils;
import com.younle.younle624.myapplication.utils.Utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import static com.younle.younle624.myapplication.constant.Constant.STORE_TEL;
import static com.younle.younle624.myapplication.utils.SaveUtils.getObject;

/**
 * 作者：Create by 我是奋斗 on2016/12/9 11:01
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 * 打印小票的工具类
 */
public class PrintUtils<T> {
    private static final String TAG = "PrintUtils";
    private String line32="--------------------------------";
    private String line27="---------------------------";
    private String line35="---------------------";
    int size27=27;
    int size22=22;
    int size35=35;
    int size40=43;
    int size50=50;

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
    public void zsmcPrint( final IWoyouService iWoyouService,final List<String> headData, final boolean forCustomer,
                          final List<String> memberData, final int textFont, final ICallback callback) {

        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.先打头部内容
                    for (int i = 0; i < headData.size(); i++) {
                        String headMsg = headData.get(i);
                        LogUtils.e(TAG,"headMsg0="+headMsg);
                        LogUtils.e(TAG,"forCustomer="+forCustomer);
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
                        LogUtils.e(TAG,"headMsg1="+headMsg);
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
                            if(headMsg.contains("原单价")) {
                                iWoyouService.printText(headMsg, null);
                                iWoyouService.lineWrap(1, null);
                            }else {
                                iWoyouService.printText(headMsg, null);
                            }
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
                        if(Constant.bottomData!=null&&Constant.bottomData.length>0) {
                            for (int i = 0; i < Constant.bottomData.length; i++) {
                                String bottomMsg = Constant.bottomData[i];
                                iWoyouService.setFontSize(textSize, null);
                                iWoyouService.setAlignment(0, null);//靠左
                                iWoyouService.printText(bottomMsg, null);
                                iWoyouService.lineWrap(1, null);
                            }
                        }

                        iWoyouService.setFontSize(textSize, null);
                        iWoyouService.setAlignment(0, null);//靠左
                        iWoyouService.printText(line27, null);
                        iWoyouService.lineWrap(1, null);

                        iWoyouService.printText("顾客联", null);
                        iWoyouService.lineWrap(1, null);

                    }else {
                        iWoyouService.setFontSize(textSize, null);
                        iWoyouService.setAlignment(0, null);//靠左
                        iWoyouService.printText("商家联", null);
                        iWoyouService.lineWrap(1, null);
                        iWoyouService.lineWrap(1, null);
                    }
                    iWoyouService.lineWrap(1, null);
                    iWoyouService.lineWrap(1, callback);
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
                            iWoyouService.setAlignment(data.get(i).getAlin(), null);//靠左
                            iWoyouService.printText(wmMsg, null);
                            iWoyouService.lineWrap(1, null);
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

    public void oneByOneData(UnPayDetailsBean orderBean,Context context,List<WmPintData> data,boolean bt){
        List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_entity = orderBean.getMsg().getOrder_goods();
        List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms = orderBean.getMsg().getOrder_rooms();

        List<String>  printerGroupId;
        SavedPrinter savedPrinter;
        if(bt) {
            savedPrinter= (SavedPrinter) SaveUtils.getObject(context,Constant.BT_PRINTER);
        }else {
            savedPrinter=(SavedPrinter) getObject(context,Constant.LOCAL_PRINTER);
        }
        if(savedPrinter==null) {
            return;
        }else {
            printerGroupId=savedPrinter.getPrintGroupId();
        }
        if(printerGroupId==null) {
            printerGroupId=new ArrayList<>();
        }
        LogUtils.e(TAG,"设定的打印分组："+printerGroupId);
        if (order_entity != null && order_entity.size() > 0) {
            int size=order_entity.size();
            for (int i = 0; i < size; i++) {
                String group_id = order_entity.get(i).getGroup_id();
                if(TextUtils.equals("0",order_entity.get(i).getId())) {
                    continue;
                }
                if(printerGroupId.size()<=0||printerGroupId.contains(group_id)) {
                    String name = order_entity.get(i).getGoods_name();
                    double num = order_entity.get(i).getGoods_num();
                    data.add(new WmPintData(name,size40,WmPintData.LEFT));
                    if(order_entity.get(i).getSize_name()!=null&&!TextUtils.isEmpty(order_entity.get(i).getSize_name())) {
                        data.add(new WmPintData("规格:"+order_entity.get(i).getSize_name(),size40,WmPintData.LEFT));
                    }
                    data.add(new WmPintData("数量:"+Utils.dropZero(Utils.keepTwoDecimal(num+"")),size40,WmPintData.LEFT));
                    if("".equals(Constant.CURRENT_ORDER_ROOM)) {
                        data.add(new WmPintData("订单序号：" + Constant.CURRENT_ORDER_NUM,size40,WmPintData.LEFT));
                    }else {
                        //1代表桌台 0代表房间
                        if(order_rooms.get(0).getRoom_type()!=null){
                            if("1".equals(order_rooms.get(0).getRoom_type())){
                                data.add(new WmPintData("桌台:"+Constant.CURRENT_ORDER_ROOM,size40,WmPintData.LEFT));
                            }else{
                                data.add(new WmPintData("房间:"+Constant.CURRENT_ORDER_ROOM,size40,WmPintData.LEFT));
                            }
                        }else{
                            data.add(new WmPintData("房间:"+Constant.CURRENT_ORDER_ROOM,size40,WmPintData.LEFT));
                        }
                        data.add(new WmPintData("订单序号：" + Constant.CURRENT_ORDER_NUM,size27,WmPintData.LEFT));
                    }
                    if(orderBean.getMsg().getRemark()!=null&&!TextUtils.isEmpty(orderBean.getMsg().getRemark())) {
                        data.add(new WmPintData("备注："+orderBean.getMsg().getRemark(),size40, WmPintData.LEFT));
                    }

                    if(orderBean.getMsg().getAddtime()!=null){
                        data.add(new WmPintData("下单时间:" + Utils.getPtintTime(orderBean.getMsg().getAddtime() + "000"),size27,WmPintData.LEFT));
                    }else{
                        data.add(new WmPintData("下单时间:" + Utils.getCurrentMin(),size27,WmPintData.LEFT));
                    }
                    if(isbt) {
                        data.add(new WmPintData(line32));
                    }else {
                        data.add(new WmPintData(line27));
                    }
                    data.add(new WmPintData("\n"));
                }
            }
        }
    }
    public void oneByOneAppletData(AppLetReceiveBean orderBean,List<WmPintData> data,Context context,boolean bt){
        List<AppLetReceiveBean.GoodsInfoBean> order_entity = orderBean.getGoodsInfo();
        List<String>  printerGroupId;
        SavedPrinter savedPrinter;
        if(bt) {
            savedPrinter = (SavedPrinter) getObject(context, Constant.BT_PRINTER);
        }else {
            savedPrinter = (SavedPrinter) getObject(context, Constant.LOCAL_PRINTER);
        }
        if(savedPrinter==null) {
            return ;
        }else {
            printerGroupId=savedPrinter.getPrintGroupId();
        }
        if(printerGroupId==null) {
            printerGroupId=new ArrayList<>();
        }
        //订单序号
        String orderNo;
        if(orderBean.getRoomName()!=null&&!TextUtils.isEmpty(orderBean.getRoomName())) {
            orderNo=orderBean.getRoomName();
        }else {
            orderNo="订单序号："+orderBean.getDayOrderNum();
        }
        //下单时间
        String orderTime;
        if(orderBean.getAddTime()!=null){
            orderTime="下单时间:" + orderBean.getAddTime();
        }else{
            orderTime="下单时间:" + Utils.getCurrentMin() ;
        }

        if (order_entity != null && order_entity.size() > 0) {
            int size=order_entity.size();
            for (int i = 0; i < size; i++) {
                if(TextUtils.equals("0",order_entity.get(i).getId())) {
                    continue;
                }
                String group_id = order_entity.get(i).getGroup_id();
                if(printerGroupId.size()<=0||printerGroupId.contains(group_id)) {
                    String name = order_entity.get(i).getName();
                    String num = order_entity.get(i).getNum();
                    data.add(new WmPintData(name,size40,WmPintData.LEFT));
                    if(order_entity.get(i).getSize_name()!=null&&!TextUtils.isEmpty(order_entity.get(i).getSize_name())) {
                        data.add(new WmPintData("规格:"+order_entity.get(i).getSize_name(),size40,WmPintData.LEFT));
                    }
                    data.add(new WmPintData("数量:"+Utils.dropZero(Utils.keepTwoDecimal(num)),size40,WmPintData.LEFT));
                    data.add(new WmPintData(orderNo,size40,WmPintData.LEFT));

                    if(orderBean.getRoomName()!=null&&!TextUtils.isEmpty(orderBean.getRoomName())) {
                        data.add(new WmPintData("订单序号："+orderBean.getDayOrderNum()));
                    }


                    data.add(new WmPintData(orderTime));
                    data.add(new WmPintData("\n"));
                    if(isbt) {
                        data.add(new WmPintData(size27, line32));
                    }else {
                        data.add(new WmPintData(size27, line27));
                    }
                    data.add(new WmPintData("\n"));
                }
            }
        }
    }
    public void normalMerchantFinishedOrder(PosPrintBean posPrintBean,List<WmPintData> data,Context context,boolean bt){
        List<String>  printerGroupId;
        SavedPrinter savedPrinter;
        if(bt) {
            savedPrinter = (SavedPrinter) getObject(context, Constant.BT_PRINTER);
        }else {
            savedPrinter = (SavedPrinter) getObject(context, Constant.LOCAL_PRINTER);
        }
        if(savedPrinter==null) {
            return ;
        }else {
            printerGroupId=savedPrinter.getPrintGroupId();
        }
        PosPrintBean.MsgBean appLetBean = posPrintBean.getMsg();
        if(printerGroupId.size()>0) {
            int size = appLetBean.getGoodsInfo().size();
            for (int i = 0; i < size; i++) {
                if(printerGroupId.contains(appLetBean.getGoodsInfo().get(i).getGroup_id())) {
                    break;
                }else {
                    if(i==size-1) {
                        return;
                    }
                }

            }
        }
        wmCenterFormat(Constant.STORE_P + Constant.STORE_M," ",data,size27);

        if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
            data.add(new WmPintData(appLetBean.getRoomName(),size40,WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + appLetBean.getDayOrderNum(),size40,WmPintData.CENTER));
        }
        if(appLetBean.getOrderType()==3) {
            if(appLetBean.getDeliveryStatus()==0||appLetBean.getDeliveryStatus()==1) {
                data.add(new WmPintData("期望送达时间",size27,WmPintData.CENTER));
                data.add(new WmPintData(appLetBean.getDeliveryTime(),size27,WmPintData.CENTER));
                wmCenterFormat("顾客信息","-",data, size27);
                data.add(new WmPintData(size40,appLetBean.getCneeAddress()));
                data.add(new WmPintData("姓名:"+appLetBean.getCneeName()));
                data.add(new WmPintData("手机号:"+appLetBean.getCneePhone()));
            }else {
                data.add(new WmPintData("自提:",size40,WmPintData.CENTER));
                wmCenterFormat("顾客信息","-",data,size27);
                data.add(new WmPintData("自提码："+appLetBean.getOrderid(),size40,WmPintData.LEFT));
                data.add(new WmPintData("自提时间："+appLetBean.getDeliveryTime()));
                data.add(new WmPintData("手机号："+appLetBean.getCneePhone()));
            }
        }else {
            wmCenterFormat("顾客自助点单"," ",data,size27);
            data.add(new WmPintData(size27, line27));
            data.add(new WmPintData("下单时间:" + appLetBean.getAddTime(),size27,WmPintData.LEFT));

            if(appLetBean.getOrderType()!=2) {
                if(appLetBean.getPayTime()!=null&&!"".equals(appLetBean.getPayTime())&&!"0".equals(appLetBean.getPayTime())){
                    data.add(new WmPintData("结账时间:" + appLetBean.getPayTime(),size27,WmPintData.LEFT));
                }
                data.add(new WmPintData("应付金额:"+appLetBean.getShouldPayMoney()+"元",size27,WmPintData.LEFT));
                if(appLetBean.getFactPayMoney()>0) {
                    data.add(new WmPintData("实付金额:" + appLetBean.getFactPayMoney() + "元", size27, WmPintData.LEFT));
                }
                data.add(new WmPintData("支付方式:"+appLetBean.getPayType()));
                if(appLetBean.getTransaction_id()!=null&&!TextUtils.isEmpty(appLetBean.getTransaction_id())) {
                    data.add(new WmPintData("商户单号:"+appLetBean.getOrderNo()));
                }

            }
            if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
                data.add(new WmPintData("订单序号:"+appLetBean.getDayOrderNum(),size27,WmPintData.LEFT));
            }
        }
        if(appLetBean.getOrderNote()!=null&&!TextUtils.isEmpty(appLetBean.getOrderNote())) {
            data.add(new WmPintData("备注:" + appLetBean.getOrderNote(), size27, WmPintData.LEFT));
        }
        wmCenterFormat("商品信息","-", data,size27);
//        List<AppLetReceiveBean.GoodsInfoBean> goodsInfo = appLetBean.getGoodsInfo();
        List<PosPrintBean.MsgBean.GoodsInfoBean> goodsInfo = appLetBean.getGoodsInfo();

        addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
        for (int i = 0; i < goodsInfo.size(); i++) {
            PosPrintBean.MsgBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(i);
            String group_id = goodsInfoBean.getGroup_id();
            if(printerGroupId.size()<=0||printerGroupId.contains(group_id)) {//分组打印
                if("0".equals(goodsInfoBean.getType())||"1".equals(goodsInfoBean.getType())||"2".equals(goodsInfoBean.getType())) {
                    double ori_price = goodsInfoBean.getOri_price();
                    double price = goodsInfoBean.getPrice();
                    int is_vip = goodsInfoBean.getIs_vip();
                    addFormatWmTest(data, goodsInfoBean.getName(),Utils.keepTwoDecimal(price+"") ,Utils.keepTwoDecimal(goodsInfoBean.getNum()),
                            numDf.format(Double.valueOf(goodsInfoBean.getNum()) *price),size27);
                    if(is_vip==1&&ori_price-goodsInfoBean.getPrice()>0) {
                        data.add(new WmPintData("原单价:"+Utils.keepTwoDecimal(ori_price+"")+",会员优惠:"+Utils.keepTwoDecimal(ori_price-price+"")));
                    }
                }
            }
        }

        data.add(new WmPintData(line27));
        if(appLetBean.getOrderType()==3) {
            data.add(new WmPintData("付款时间:"+appLetBean.getPayTime()));
            String deliveryCost = appLetBean.getDeliveryCost();
            data.add(new WmPintData("商品金额:"+Utils.keepTwoDecimal(appLetBean.getShouldPayMoney()-Double.valueOf(deliveryCost)+"")+"元"));
            if(appLetBean.getDiscount()>0) {
                data.add(new WmPintData("会员折扣:" + appLetBean.getDiscount() * 10 + "折"));
            }
            data.add(new WmPintData("配送费:"+ appLetBean.getDeliveryCost()));
            data.add(new WmPintData("实付金额:"+Utils.keepTwoDecimal(appLetBean.getFactPayMoney()+"")));
            data.add(new WmPintData("支付方式:"+appLetBean.getPayType()));
            data.add(new WmPintData("商户单号:"+appLetBean.getOrderNo()));
            data.add(new WmPintData(line27));
        }
        data.add(new WmPintData("商家联"));
        //二维码投放信息
        /*AppLetReceiveBean.TicketInfoBean ticketInfo = appLetBean.getTicketInfo();
        if(ticketInfo!=null) {
            chargeInfoQRCode(ticketInfo, data);
        }*/
    }
    public void normalMerchant(AppLetReceiveBean appLetBean,List<WmPintData> data,Context context,boolean bt){
        List<String>  printerGroupId;
        SavedPrinter savedPrinter;
        if(bt) {
            savedPrinter = (SavedPrinter) getObject(context, Constant.BT_PRINTER);
        }else {
            savedPrinter = (SavedPrinter) getObject(context, Constant.LOCAL_PRINTER);
        }
        if(savedPrinter==null) {
            return ;
        }else {
            printerGroupId=savedPrinter.getPrintGroupId();
        }
        if(printerGroupId==null) {
            printerGroupId=new ArrayList<>();
        }
        if(printerGroupId.size()>0) {//有设置打印分组，遍历商品，判断是否有要打印的内容
            int size = appLetBean.getGoodsInfo().size();
            for (int i = 0; i < size; i++) {
                if(printerGroupId.contains(appLetBean.getGoodsInfo().get(i).getGroup_id())) {
                    break;
                }else {
                    if(i==size-1) {
                        return;
                    }
                }

            }
        }

        wmCenterFormat(Constant.STORE_P + Constant.STORE_M," ",data,size27);

        if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
            data.add(new WmPintData(appLetBean.getRoomName(),size40,WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + appLetBean.getDayOrderNum(),size40,WmPintData.CENTER));
        }
        if(appLetBean.getOrderType()==4) {
            if(appLetBean.getDeliveryStatus()==0||appLetBean.getDeliveryStatus()==1) {
                data.add(new WmPintData("期望送达时间",size27,WmPintData.CENTER));
                data.add(new WmPintData(appLetBean.getDeliveryTime(),size27,WmPintData.CENTER));
                wmCenterFormat("顾客信息","-",data, size27);
                data.add(new WmPintData(appLetBean.getCneeAddress(),size40,WmPintData.LEFT));
                data.add(new WmPintData("姓名:"+appLetBean.getCneeName()));
                data.add(new WmPintData("手机号:"+appLetBean.getCneePhone()));
            }else {
                data.add(new WmPintData("自提",size40,WmPintData.CENTER));
                wmCenterFormat("顾客信息","-",data, size27);
                data.add(new WmPintData("自提码："+appLetBean.getZt_code(),size40,WmPintData.CENTER));
                data.add(new WmPintData("自提时间："+appLetBean.getDeliveryTime()));
                data.add(new WmPintData("手机号："+appLetBean.getCneePhone()));
            }

        }else {
            wmCenterFormat("顾客自助点单"," ",data,size27);
            data.add(new WmPintData(size27, line27));
            data.add(new WmPintData("下单时间:" + appLetBean.getAddTime(),size27,WmPintData.LEFT));

            if(TextUtils.equals("1",appLetBean.getPayStatus())) {
                if(appLetBean.getPayTime()!=null&&!"".equals(appLetBean.getPayTime())&&!"0".equals(appLetBean.getPayTime())){
                    data.add(new WmPintData("结账时间:" + appLetBean.getPayTime(),size27,WmPintData.LEFT));
                }
                data.add(new WmPintData("应付金额:"+appLetBean.getShouldPayMoney()+"元",size27,WmPintData.LEFT));
                if(appLetBean.getFactPayMoney()>0) {
                    data.add(new WmPintData("实付金额:" + appLetBean.getFactPayMoney() + "元", size27, WmPintData.LEFT));
                }
                data.add(new WmPintData("支付方式:"+appLetBean.getPayType()));
                if(appLetBean.getTransaction_id()!=null&&!TextUtils.isEmpty(appLetBean.getTransaction_id())) {
                    data.add(new WmPintData("商户单号:"+appLetBean.getOrderNo()));
                }

            }
            if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
                data.add(new WmPintData("订单序号:"+appLetBean.getDayOrderNum(),size27,WmPintData.LEFT));
            }
        }
        if(appLetBean.getOrderNote()!=null&&!TextUtils.isEmpty(appLetBean.getOrderNote())) {
            data.add(new WmPintData("备注:" + appLetBean.getOrderNote(), size27, WmPintData.LEFT));
        }
        wmCenterFormat("商品信息","-", data,size27);
        List<AppLetReceiveBean.GoodsInfoBean> goodsInfo = appLetBean.getGoodsInfo();
        if(appletWhichContain(goodsInfo,"2")||appletWhichContain(goodsInfo,"1")||appletWhichContain(goodsInfo,"0")) {
            addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
            for (int i = 0; i < goodsInfo.size(); i++) {
                AppLetReceiveBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(i);
                String group_id = goodsInfoBean.getGroup_id();
                if(printerGroupId.size()<=0||printerGroupId.contains(group_id)) {//分组打印
                    if("0".equals(goodsInfoBean.getType())||"1".equals(goodsInfoBean.getType())||"2".equals(goodsInfoBean.getType())) {
                        double ori_price = goodsInfoBean.getOri_price();
                        double price = goodsInfoBean.getPrice();
                        int is_vip = goodsInfoBean.getIs_vip();
                        addFormatWmTest(data, goodsInfoBean.getName(),Utils.keepTwoDecimal(price+"") ,Utils.keepTwoDecimal(goodsInfoBean.getNum()),
                                numDf.format(Double.valueOf(goodsInfoBean.getNum()) *price),size27);
                        if(is_vip==1&&ori_price-goodsInfoBean.getPrice()>0) {
                            data.add(new WmPintData("原单价:"+Utils.keepTwoDecimal(ori_price+"")+",会员优惠:"+Utils.keepTwoDecimal(ori_price-price+"")));
                        }
                    }
                }
            }
        }
        data.add(new WmPintData(line27));
        if(appLetBean.getOrderType()==4) {
            data.add(new WmPintData("付款时间:"+appLetBean.getPayTime()));
            String deliveryCost = appLetBean.getDeliveryCost();
            data.add(new WmPintData("商品金额:"+Utils.keepTwoDecimal(appLetBean.getShouldPayMoney()-Double.valueOf(deliveryCost)+"")+"元"));
            if(appLetBean.getDiscount()>0) {
                data.add(new WmPintData("会员折扣:" + appLetBean.getDiscount() * 10 + "折"));
            }
            data.add(new WmPintData("配送费:"+ appLetBean.getDeliveryCost()));
            data.add(new WmPintData("实付金额:"+Utils.keepTwoDecimal(appLetBean.getFactPayMoney()+"")));
            data.add(new WmPintData("支付方式:"+appLetBean.getPayType()));
            data.add(new WmPintData("商户单号:"+appLetBean.getOrderNo()));
            data.add(new WmPintData(line27));
        }
        data.add(new WmPintData("商家联"));
        //二维码投放信息
        /*AppLetReceiveBean.TicketInfoBean ticketInfo = appLetBean.getTicketInfo();
        if(ticketInfo!=null) {
            chargeInfoQRCode(ticketInfo, data);
        }*/
    }

    /**
     * 历史订单详情小程序商家联
     * @param orderBean
     * @param data
     * @param context
     * @param bt
     */
    public void oneByOneAppletHisData(PosPrintBean orderBean,List<WmPintData> data,Context context,boolean bt){

        List<String> printerGroupId;
        SavedPrinter savedPrinter;
        if(bt) {
            savedPrinter = (SavedPrinter) getObject(context, Constant.BT_PRINTER);
        }else {
            savedPrinter = (SavedPrinter) getObject(context, Constant.LOCAL_PRINTER);
        }
        if(savedPrinter==null) {
            return ;
        }else {
            printerGroupId=savedPrinter.getPrintGroupId();
        }
        if(printerGroupId==null) {
            printerGroupId=new ArrayList<>();
        }
        //订单序号
        String orderNo;
        if(orderBean.getMsg().getRoomName()!=null&&!TextUtils.isEmpty(orderBean.getMsg().getRoomName())) {
            orderNo=orderBean.getMsg().getRoomName();
        }else {
            orderNo="订单序号："+orderBean.getMsg().getDayOrderNum();
        }
        //下单时间
        String orderTime;
        if(orderBean.getMsg().getAddTime()!=null){
            orderTime="下单时间:" + orderBean.getMsg().getAddTime();
        }else{
            orderTime="下单时间:" + Utils.getCurrentMin() ;
        }
        List<PosPrintBean.MsgBean.GoodsInfoBean> order_entity = orderBean.getMsg().getGoodsInfo();
        if (order_entity != null && order_entity.size() > 0) {
            int size=order_entity.size();
            for (int i = 0; i < size; i++) {
                if(TextUtils.equals("0",order_entity.get(i).getId())) {
                    continue;
                }
                String group_id = order_entity.get(i).getGroup_id();
                if(printerGroupId.size()<=0||printerGroupId.contains(group_id)) {
                    String name = order_entity.get(i).getName();
                    String num = order_entity.get(i).getNum();
                    data.add(new WmPintData(name,size40,WmPintData.LEFT));
                    if(order_entity.get(i).getSize_name()!=null&&!TextUtils.isEmpty(order_entity.get(i).getSize_name())) {
                        data.add(new WmPintData("规格:"+order_entity.get(i).getSize_name(),size40,WmPintData.LEFT));
                    }
                    data.add(new WmPintData("数量:"+Utils.dropZero(Utils.keepTwoDecimal(num)),size40,WmPintData.LEFT));
                    data.add(new WmPintData(orderNo,size40,WmPintData.LEFT));

                    if(orderBean.getMsg().getRoomName()!=null&&!TextUtils.isEmpty(orderBean.getMsg().getRoomName())) {
                        data.add(new WmPintData("订单序号："+orderBean.getMsg().getDayOrderNum()));
                    }

                    data.add(new WmPintData(orderTime));
                    if(isbt) {
                        data.add(new WmPintData(""));
                        data.add(new WmPintData(line32));
                        data.add(new WmPintData(""));
                    }else {
                        data.add(new WmPintData(""));
                        data.add(new WmPintData(line27));
                        data.add(new WmPintData(""));
                    }
                }
            }
        }
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
            sideLen = 10;
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
                        leftTotal=14;
                        centerTotal=2;
                        rightTotal=10;
                    }else{
                        leftTotal=14;
                        centerTotal=6;
                        rightTotal=7;
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
            sideLen = 10;
            if(isbt){
                sideLen=13;
            }
        }else if(textSize==35) {
            sideLen=8;
            if(isbt){
                sideLen=10;
            }
        }
        LogUtils.e("addFormatTest","sideLen="+sideLen);
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
                /*leftTotal=21;
                centerTotal=9;
                rightTotal=11;*/
                leftTotal=16;
                centerTotal=7;
                rightTotal=9;
            }else {
                if(textSize==27) {
                    leftTotal=14;
                    centerTotal=6;
                    rightTotal=7;
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
    public void addFormatWmTest(List<WmPintData> data,String left, String center, String center2,String right,int textSize) {

        center2 = Utils.dropZero(center2);

        int chinaNum = Utils.getChinaNum(left);
        int nameLength = left.length() - chinaNum + chinaNum * 2;

        LogUtils.e("addFormat----","left="+left+",left.size()="+left.length());
        LogUtils.e("addFormat----","chinaNum="+chinaNum+",nameLength="+nameLength);

        int sideLen=0;
        if(textSize==27) {
            sideLen = 10;
            if(isbt){
                sideLen=13;
            }
        }else if(textSize==35) {
            sideLen=8;
            if(isbt){
                sideLen=10;
            }
        }

        LogUtils.e("addFormat----","sideLen="+sideLen);

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
                /*leftTotal=21;
                centerTotal=9;
                rightTotal=11;*/
                leftTotal=16;
                centerTotal=7;
                rightTotal=9;
            }else {
                if(textSize==27) {
                    /*leftTotal=14;
                    centerTotal=6;
                    rightTotal=7;*/
                    if(center.length()>6){
                        leftTotal=14;
                        centerTotal=2;
                        rightTotal=10;
                    }else{
                        leftTotal=14;
                        centerTotal=6;
                        rightTotal=7;
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
            data.add(new WmPintData(textSize,left+space0+center+space1+center2+space2+right));
        }
    }
    /**
     * 初始化下单结账的打印数据
     */
    public void initFOPrintData(UnPayDetailsBean orderBean, DiscountInfo discountBean, List<String> headerData,List<String> memberData,
                                String printJZTime, String tradenum, String payWay,String transaction_id,String memberFreeMoney,boolean bt) {
        LogUtils.Log("initPrintData:"+discountBean);
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

        wmCenterFormat("-","-", memberData,size27);
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
                wmCenterFormat(youhui," ", memberData,size27);
            }
        }else {
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
                centerFormat(youhui," ", memberData);
            }
        }else {
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
                totalLegth=27;
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
                totalLegth=27;
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
            headerData.add(line32);
        }else{
            headerData.add(line27);
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
        if(transaction_id!=null&&!TextUtils.isEmpty(transaction_id)) {
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
                    double goods_price = order_entity.get(i).getGoods_price();
                    double total_fee = order_entity.get(i).getTotal_fee();
                    double ori_price = order_entity.get(i).getGoods_ori_price();

                    addFormat(headerData, name, goods_price+"",num, String.valueOf(total_fee));
                    addvipPriceNotice(headerData, goods_price, ori_price);
                }
            }
        }
        if(isbt){
            headerData.add(line32);
        }else{
            headerData.add(line27);
        }
    }
    private void addvipPriceNotice(List<String> headerData, double goods_price, double ori_price) {
        if(ori_price-goods_price>0) {
            headerData.add("原单价:"+ori_price+"元,会员优惠:"+ Utils.dropZero(Utils.keepTwoDecimal(ori_price-goods_price+""))+"元");
//            headerData.add("\n");
        }
    }

    private void addvipPriceNoticeForDetail(List<WmPintData> headerData, double goods_price, double ori_price) {
        if(ori_price-goods_price>0) {
            headerData.add(new WmPintData("原单价:" + ori_price + "元,会员优惠:" + Utils.dropZero(Utils.keepTwoDecimal(ori_price - goods_price + "")) + "元"
                    ,size22,WmPintData.LEFT));
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

    public void firstComitFormatData(UnPayDetailsBean orderBean,List<String> headData,boolean isbt,boolean isGroup){
        this.isbt=isbt;
        textSize=27;
        //来自第一次提交到总订单，并且不是只有房间
        headData.clear();
        List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_entity = orderBean.getMsg().getOrder_goods();
        List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms = orderBean.getMsg().getOrder_rooms();
            if ((order_rooms != null && order_rooms.size() > 0) && (order_entity == null || order_entity.size() <= 0)) {
                return;
            } else {
                headData.add(Constant.STORE_P + Constant.STORE_M);
                headData.add("电话：" + STORE_TEL);
                if("".equals(Constant.CURRENT_ORDER_ROOM)) {
                    headData.add("订单序号:" + Constant.CURRENT_ORDER_NUM);
                }else {
                    //1代表桌台 0代表房间
                    if(order_rooms.get(0).getRoom_type()!=null){
                        if("1".equals(order_rooms.get(0).getRoom_type())){
                            headData.add("桌台:"+Constant.CURRENT_ORDER_ROOM);
                        }else{
                            headData.add("房间:"+Constant.CURRENT_ORDER_ROOM);
                        }
                    }else{
                        headData.add("房间:"+Constant.CURRENT_ORDER_ROOM);
                    }
                }
                if(isbt){
                    headData.add(line32);
                }else{
                    headData.add(line27);
                }
                //headData.add("下单时间:" + Utils.getCurrentMin());
                if(orderBean.getMsg().getAddtime()!=null){
                    headData.add("下单时间:" + Utils.getPtintTime(orderBean.getMsg().getAddtime() + "000"));
                }else{
                    headData.add("下单时间:" + Utils.getCurrentMin());
                }
                if(!"".equals(Constant.CURRENT_ORDER_ROOM)){
                    headData.add("订单序号:"+Constant.CURRENT_ORDER_NUM);
                }
                headData.add("操作员:" + Constant.USER_ACCOUNT);
                if(orderBean.getMsg().getRemark()!=null && !"".equals(orderBean.getMsg().getRemark())){
                    headData.add("备注："+orderBean.getMsg().getRemark());
                }

                if(isGroup){//有分组
                    addWithGroup(orderBean, headData, order_entity);
                } else {//没有分组
                    centerFormat("商品信息", "-", headData);
                    addFormatTest(headData, "商品名称","单价", "数量", "金额");
                    if (order_entity != null && order_entity.size() > 0) {
                        for (int i = 0; i < order_entity.size(); i++) {
                            String name = order_entity.get(i).getGoods_name();
                            double num = order_entity.get(i).getGoods_num();
                            double unit_price = order_entity.get(i).getGoods_price();
                            double total_fee = order_entity.get(i).getTotal_fee();
                            addFormat(headData, name, unit_price+"",num, String.valueOf(total_fee));
                            addvipPriceNotice(headData,unit_price,order_entity.get(i).getGoods_ori_price());
                        }
                    }
                }
                if(isbt){
                    headData.add(line32);
                }else{
                    headData.add(line27);
                }
            }
    }
    public void formatPreNormal(Context context,UnPayDetailsBean orderBean,List<WmPintData> headData,boolean isbt,boolean customer){
        this.isbt=isbt;
        textSize=27;
        //来自第一次提交到总订单，并且不是只有房间
        List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_entity = orderBean.getMsg().getOrder_goods();
        List<UnPayDetailsBean.MsgBean.OrderRoomsBean> order_rooms = orderBean.getMsg().getOrder_rooms();
            if ((order_rooms != null && order_rooms.size() > 0) && (order_entity == null || order_entity.size() <= 0)) {
                return;
            } else {
                List<String> printerGroupId=new ArrayList<>();
                if(!customer) {
                    SavedPrinter savedPrinter;
                    if(isbt) {
                        savedPrinter = (SavedPrinter) getObject(context, Constant.BT_PRINTER);
                    }else {
                        savedPrinter = (SavedPrinter) getObject(context, Constant.LOCAL_PRINTER);
                    }
                    if(savedPrinter==null) {
                        return ;
                    }else {
                        printerGroupId=savedPrinter.getPrintGroupId();
                    }

                    if(printerGroupId==null) {
                        printerGroupId=new ArrayList<>();
                    }

                    if(printerGroupId.size()>0) {
                        int size = order_entity.size();
                        for (int i = 0; i < size; i++) {
                            if(printerGroupId.contains(order_entity.get(i).getGroup_id())) {
                                break;
                            }else {
                                if(i==size-1) {
                                    return;
                                }
                            }
                        }
                    }
                }


                headData.add(new WmPintData(Constant.STORE_P + Constant.STORE_M,size27,WmPintData.CENTER));
                if(customer) {
                    headData.add(new WmPintData("电话：" + STORE_TEL,size27,WmPintData.CENTER));
                }
                if("".equals(Constant.CURRENT_ORDER_ROOM)) {
                    headData.add(new WmPintData("订单序号:" + Constant.CURRENT_ORDER_NUM,size40,WmPintData.CENTER));
                }else {
                    //1代表桌台 0代表房间
                    String roomDesk="";
                    if(order_rooms.get(0).getRoom_type()!=null){
                        if("1".equals(order_rooms.get(0).getRoom_type())){
                            roomDesk="桌台:"+Constant.CURRENT_ORDER_ROOM;
                        }else{
                            roomDesk="房间:"+Constant.CURRENT_ORDER_ROOM;
                        }
                    }else{
                        roomDesk="房间:"+Constant.CURRENT_ORDER_ROOM;
                    }
                    headData.add(new WmPintData(roomDesk,size40,WmPintData.CENTER));
                }
                if(isbt){
                    headData.add(new WmPintData(line32));
                }else{
                    headData.add(new WmPintData(line27));
                }

                if(orderBean.getMsg().getAddtime()!=null){
                    headData.add(new WmPintData("下单时间:" + Utils.getPtintTime(orderBean.getMsg().getAddtime() + "000")));
                }else{
                    headData.add(new WmPintData("下单时间:" + Utils.getCurrentMin()));
                }
                if(!"".equals(Constant.CURRENT_ORDER_ROOM)){
                    headData.add(new WmPintData("订单序号:" + Constant.CURRENT_ORDER_NUM));
                }
                headData.add(new WmPintData("操作员:" + Constant.USER_ACCOUNT));
                if(orderBean.getMsg().getRemark()!=null && !"".equals(orderBean.getMsg().getRemark())){
                    headData.add(new WmPintData("备注：" + orderBean.getMsg().getRemark()));
                }
                wmCenterFormat("商品信息", "-", headData,size27);
                addFormatWmTest(headData, "商品名称","单价", "数量", "金额",size27);
                if (order_entity != null && order_entity.size() > 0) {
                    for (int i = 0; i < order_entity.size(); i++) {
                        String name = order_entity.get(i).getGoods_name();
                        String num = Utils.keepTwoDecimal(order_entity.get(i).getGoods_num()+"");
                        double good_price = order_entity.get(i).getGoods_price();
                        double goods_ori_price = order_entity.get(i).getGoods_ori_price();//原价
                        double total_fee = order_entity.get(i).getTotal_fee();
                        if(!customer) {
                            String group_id = order_entity.get(i).getGroup_id();
                            if(printerGroupId.contains(group_id)||printerGroupId.size()<=0) {
                                addFormatWmTest(headData, name, Utils.keepTwoDecimal(good_price+""),num, Utils.keepTwoDecimal(total_fee+""),size27);
                                if(goods_ori_price -good_price>0) {
                                    headData.add(new WmPintData("原单价:"+goods_ori_price+"元,会员优惠:"+ Utils.dropZero(Utils.keepTwoDecimal(goods_ori_price-good_price+""))+"元"));
                                }
                            }
                        }else {
                            addFormatWmTest(headData, name, Utils.keepTwoDecimal(good_price+""),num, Utils.keepTwoDecimal(total_fee+""),size27);
                            if(goods_ori_price -good_price>0) {
                                headData.add(new WmPintData("原单价:"+goods_ori_price+"元,会员优惠:"+ Utils.dropZero(Utils.keepTwoDecimal(goods_ori_price-good_price+""))+"元"));
                            }
                        }
                    }
                }
                if(isbt){
                    headData.add(new WmPintData(line32));
                }else{
                    headData.add(new WmPintData(line27));
                }
                if(customer) {
                    addBottom(headData);
                }else {
                    headData.add(new WmPintData("商家联"));
                }
            }
    }

    private void addWithGroup(UnPayDetailsBean orderBean, List<String> headData, List<UnPayDetailsBean.MsgBean.OrderGoodsBean> order_entity) {
        //在添加数据到data里前 把数据整理好分组商品:
        String name;
        double num;
        double unit_price;
        double total_fee;
        int group_num = 0;
        //判断是否存在有分组的商品：
        boolean isHasGroupGoods = false;
        if(orderBean.getMsg().getGroup_info()!=null){
            group_num = orderBean.getMsg().getGroup_info().size();
        }
        if (order_entity != null && order_entity.size() > 0) {
            for(int k=0;k<group_num;k++){//分组循环
                if(orderBean.getMsg().getGroup_info().get(k).getGroup_id()!=null&&Integer.valueOf(orderBean.getMsg().getGroup_info().get(k).getGroup_id())>0){

                    for(int j=0;j<order_entity.size();j++){
                        if(orderBean.getMsg().getGroup_info().get(k).getGroup_id().equals(order_entity.get(j).getGroup_id())){
                            isHasGroupGoods = true;
                            break;
                        }
                    }
                    if(isHasGroupGoods){
                        //添加分组头
                        centerFormat(orderBean.getMsg().getGroup_info().get(k).getGroup_name(), "-", headData);
                        addFormatTest(headData,"商品名称","单价","数量","金额");
                        for (int i = 0; i < order_entity.size(); i++) {//添加分组数据循环
                            if(orderBean.getMsg().getGroup_info().get(k).getGroup_id().equals(order_entity.get(i).getGroup_id())/*&&!"".equals(order_entity.get(i).getGroup_id())*/){
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");//如果小数不足2位,会以0补足.
                                String format_total_fee = decimalFormat.format(order_entity.get(i).getTotal_fee());
                                name = order_entity.get(i).getGoods_name();
                                unit_price = order_entity.get(i).getGoods_price();
                                num = order_entity.get(i).getGoods_num();
                                total_fee = Double.valueOf(format_total_fee);
                                addFormat(headData, name, unit_price+"",num, String.valueOf(total_fee));
                                addvipPriceNotice(headData,unit_price,order_entity.get(i).getGoods_ori_price());
                            }
                        }
                        headData.add("                ");
                    }
                }
            }
            LogUtils.e("firstComitFormatData","有分组...3");
            boolean ishasOtherGoods = false;
            //循环添加完分组数据再添加未分组数据
            for(int h=0;h<order_entity.size();h++){
                if(order_entity.get(h).getGroup_id()==null||"0".equals(order_entity.get(h).getGroup_id())||"".equals(order_entity.get(h).getGroup_id())){
                    ishasOtherGoods = true;
                    break;
                }
            }
            if(ishasOtherGoods){
                if(group_num>0&&isHasGroupGoods){
                    centerFormat("其他","-",headData);
                    addFormatTest(headData, "商品名称","单价", "数量", "金额");
                }else{
                    centerFormat("商品信息", "-", headData);
                    addFormatTest(headData, "商品名称","单价", "数量", "金额");
                }
                for(int j=0;j<order_entity.size();j++){
                    if(order_entity.get(j).getGroup_id()==null||"0".equals(order_entity.get(j).getGroup_id())||"".equals(order_entity.get(j).getGroup_id())){
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");//如果小数不足2位,会以0补足.
                        String format_total_fee = decimalFormat.format(order_entity.get(j).getTotal_fee());
                        name = order_entity.get(j).getGoods_name();
                        num = order_entity.get(j).getGoods_num();
                        unit_price = order_entity.get(j).getGoods_price();
                        total_fee = Double.valueOf(format_total_fee);
                        addFormat(headData, name, unit_price+"",num, String.valueOf(total_fee));
                        addvipPriceNotice(headData,unit_price,order_entity.get(j).getGoods_ori_price());

                    }
                }
            }
        }
    }

    /**
     * 初始化打印服务
     */
    public void initService(Context context,ServiceConnection connService) {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        context.startService(intent);
        context.bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    /**
     * 直接收银的打印数据的初始化
     * @param query_num
     * @param tradenum
     * @param tradetime
     * @param discountBean
     * @param orderAmount
     * @param payWay
     * @param headerData
     * @param memberData
     */
    public void initZSPrintData(String query_num, String tradenum, String tradetime, DiscountInfo discountBean,
                                double orderAmount, String total_fee,String vip_discount,String payWay,
                                List<String> headerData, List<String> memberData,String transaction_id,boolean bt,String freemoney) {
        this.isbt=bt;
        textSize=27;
        //1.公共部分
        headerData.add("订单序号：" + query_num);
        headerData.add(line27);
        headerData.add("下单时间:" + Utils.getCurrentM(Long.valueOf(tradetime) * 1000));
        headerData.add("结账时间:" + Utils.getCurrentM(Long.valueOf(tradetime) * 1000));
        headerData.add("应付金额:" + total_fee + "元");
        headerData.add("实付金额:" + orderAmount + "元");
        /*if(Double.valueOf(vip_discount)<10&&Double.valueOf(vip_discount)>0){
            headerData.add("会员折扣：" + vip_discount + "折");
        }*/
        if(!"".equals(vip_discount)){
            headerData.add(vip_discount);
        }
        headerData.add("支付方式:" + payWay);
        if(!"0".equals(freemoney)&&!"".equals(freemoney)){
            headerData.add("会员卡余额:" + freemoney +"元");
        }
        headerData.add("商户单号:"+tradenum);
        if(transaction_id!=null&&!TextUtils.isEmpty(transaction_id)&&!"null".equals(transaction_id)) {
            headerData.add("交易单号："+transaction_id);
        }
        headerData.add("操作员:" + Constant.USER_ACCOUNT);
        if(isbt){
            headerData.add(line32);
        }else{
            headerData.add(line27);
        }
        addFormatTest(headerData, "商品名称","单价", "数量", "金额");
        addFormat(headerData, "未知商品", orderAmount+"", 1, orderAmount+"");
        if(isbt){
            headerData.add(line32);
        }else{
            headerData.add(line27);
        }
        //2.会员部分
        if(discountBean!=null) {
            totalPrintMember(discountBean, memberData);
        }
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
    public void initMCPrintData( String tradenum, String tradetime, double orderAmount, String payWay,
                                 List<String> headerData, List<String> memberData,String transaction_id,boolean bt) {
        textSize=27;
        //1.公共部分
        headerData.add("会员充值");
        if(bt){
            headerData.add(line32);
        }else{
            headerData.add(line27);
        }
        headerData.add("实付金额:" + orderAmount + "元");
        headerData.add("支付方式:" + payWay);
        headerData.add("付款时间:" + Utils.getCurrentM(Long.valueOf(tradetime) * 1000));
        try {
            String memberId = Constant.MEMBER_ID.substring(0, 4) + "****" + Constant.MEMBER_ID.substring(Constant.MEMBER_ID.length() - 4);
            headerData.add("会员卡号:" + memberId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        headerData.add("充值前余额:" + numDf.format(Constant.currentLeft) + "元");
        headerData.add("充值金额:" + Constant.toaccount + "元");
        headerData.add("充值后余额:" + numDf.format(Constant.currentLeft + Constant.toaccount) + "元");
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

    /**
     * 格式化美团饿了么打印数据格式
     * @param mtOrder
     * @param elmBean
     * @return
     */
    public List<WmPintData> formatPrintDataWithSize(MtOrderDetail mtOrder,ElmOrderBean elmBean,NewElmOrderBean newElmOrderBean,Context context,boolean isbt){

        textSize=35;
        this.isbt=isbt;
        String printSettings="";
        if(isbt) {
            printSettings = SpUtils.getInstance(context).getString(Constant.bt_print_permission, "");
        }else {
            printSettings = SpUtils.getInstance(context).getString(Constant.print_permission, "");
        }
        List<WmPintData> pData=new ArrayList<>();
        if(mtOrder!=null) {
            if(printSettings.contains("6")) {
                pData.add(new WmPintData(size27,"给商户"));
                pData.add(new WmPintData(size27,line27));
                addMtGongTong(mtOrder, pData);
                pData.add(new WmPintData(size27,line27));
                pData.add(new WmPintData(size27,"外卖多平台自动接单软件:乐推微门店派"));
                pData.add(new WmPintData(size27,"\n"));
            }
            if(printSettings.contains("7")) {
                pData.add(new WmPintData(size27,"给顾客"));
                pData.add(new WmPintData(size27,line27));
                addMtGongTong(mtOrder, pData);
                wmCenterFormat("商户信息", "*", pData, size27);
                pData.add(new WmPintData(size27, "商户名称:" + mtOrder.getKey().getPoiName()));
                pData.add(new WmPintData(size27,"商户电话:" + mtOrder.getKey().getPoiPhone()));
                pData.add(new WmPintData(size27,"商户地址:"+mtOrder.getKey().getPoiAddress()));
                pData.add(new WmPintData(size27,line27));
                pData.add(new WmPintData(size27,"\n"));
            }
        }

        if(elmBean!=null) {
            if(printSettings.contains("6")) {
                pData.add(new WmPintData(size27,"给商户"));
                pData.add(new WmPintData(size27,line27));
                addELmGongTong(elmBean, pData);
                pData.add(new WmPintData(size27,line27));
                pData.add(new WmPintData(size27,"外卖多平台自动接单软件:乐推微门店派"));
                pData.add(new WmPintData(size27,"\n"));
            }
            if(printSettings.contains("7")) {
                pData.add(new WmPintData(size27,"给顾客"));
                pData.add(new WmPintData(size27,line27));
                addELmGongTong(elmBean, pData);
                wmCenterFormat("商户信息", "*", pData, size27);
                pData.add(new WmPintData(size27,"商户名称：" + elmBean.getData().getRestaurant_name()));
                pData.add(new WmPintData(size27,"商户电话：" + Constant.ELM_STORE_TEL));
                pData.add(new WmPintData(size27,"商户地址："+Constant.ELM_STORE_ADD));
                pData.add(new WmPintData(size27,line27));
                pData.add(new WmPintData(size27,"\n"));
           }
        }
        if(newElmOrderBean!=null) {
            if(printSettings.contains("6")) {
                pData.add(new WmPintData(size27,"给商户"));
                pData.add(new WmPintData(size27,line27));
                addNewELmGongTong(newElmOrderBean, pData);
                pData.add(new WmPintData(size27,line27));
                pData.add(new WmPintData(size27,"外卖多平台自动接单软件:乐推微门店派"));
                pData.add(new WmPintData(size27,"\n"));
            }
            if(printSettings.contains("7")) {
                pData.add(new WmPintData(size27,"给顾客"));
                pData.add(new WmPintData(size27,line27));
                addNewELmGongTong(newElmOrderBean, pData);
                wmCenterFormat("商户信息", "*", pData,size27);
                pData.add(new WmPintData(size27,"商户名称:" + newElmOrderBean.getMessage().getShopName()));
                pData.add(new WmPintData(size27,"商户电话:" + Constant.ELM_STORE_TEL));
                pData.add(new WmPintData(size27,"商户地址:"+Constant.ELM_STORE_ADD));
                pData.add(new WmPintData(size27,line27));
                pData.add(new WmPintData(size27,"\n"));
            }
        }

        return pData;
    }

    /**
     * 添加美团商户和客户共同部分
     * @param mtOrder
     * @param pData
     */
    private void addMtGongTong(MtOrderDetail mtOrder, List<WmPintData> pData) {
        pData.add(new WmPintData(size40,"美团外卖 # " + mtOrder.getKey().getDaySeq()));
        if(1==mtOrder.getKey().getPayType()) {//货到付款
            pData.add(new WmPintData(size27,"[货到付款]"));
        }else {
            pData.add(new WmPintData(size27,"已在线支付"));
        }

        if(0==mtOrder.getKey().getDeliveryTime()) {
            pData.add(new WmPintData(size35,"期望送达时间:立即送达"));
        }else {
            String formatDTime = df4.format(mtOrder.getKey().getDeliveryTime()*1000);
            pData.add(new WmPintData(size35,"期望送达时间:" + formatDTime));
        }

        if(!TextUtils.isEmpty(mtOrder.getKey().getCaution())) {
            pData.add(new WmPintData(size27,line27));
            pData.add(new WmPintData(size35,"订单备注:"+mtOrder.getKey().getCaution()));
        }
        wmCenterFormat("订单信息", "*", pData, size27);
        wmCenterFormat("菜品", "-", pData, size27);
        addFormatWmTest(pData, "菜品名称", "单价", "数量", "金额", size27);
        List<MtOrderDetail.KeyBean.DetailBean> detailBeanList = mtOrder.getKey().getDetail();
        if(detailBeanList!=null&&detailBeanList.size()>0) {
            int total_box_num=0;
            double total_box_account=0;
            for (int i = 0; i < detailBeanList.size(); i++) {
                MtOrderDetail.KeyBean.DetailBean detailBean = detailBeanList.get(i);
                //计算总的餐盒数量和费用
                int box_num = detailBean.getBox_num();
                double box_price = detailBean.getBox_price();
                double boxAccount = box_price * box_num;
                total_box_num+=box_num;
                total_box_account+=boxAccount;

                int num = detailBean.getQuantity();
                String food_name = detailBean.getFood_name();
                String spec = detailBean.getSpec();
                String food_property = detailBean.getFood_property();
                double price = detailBean.getPrice();
                double total_fee = num * price;
                LogUtils.WmLog("spec="+spec+"  food_p="+food_property);
                if(spec!=null&&!TextUtils.isEmpty(spec.trim())) {
                    if(food_property!=null&&!TextUtils.isEmpty(food_property)) {
                        food_property=food_property.replace(",",")(");
                        addFormatWmTest(pData, food_name+"("+spec+")"+"("+food_property+")",price+"", num + "", numDf.format(total_fee), size35);
                    }else {
                        addFormatWmTest(pData, food_name+"("+spec+")",price+"", num + "", numDf.format(total_fee), size35);
                    }
                }else {
                    if(food_property!=null&&!TextUtils.isEmpty(food_property)) {
                        food_property=food_property.replace(",",")(");
                        addFormatWmTest(pData, food_name+"("+food_property+")",price+"", num + "", numDf.format(total_fee), size35);
                    }else {
                        addFormatWmTest(pData, food_name,price+"", num + "", numDf.format(total_fee), size35);
                    }
                }
            }
            wmCenterFormat("其他", "-", pData, size27);
            addFormatWmTest(pData, "餐盒费", " " ,total_box_num + "", numDf.format(total_box_account), size35);
        }
        //配送费用
        double shippingFee = mtOrder.getKey().getShippingFee();
        addFormatWmTest(pData, "配送费", " " ," ", numDf.format(shippingFee), size35);
        //优惠信息
        List<MtOrderDetail.KeyBean.ExtrasBean> extras = mtOrder.getKey().getExtras();
        if(extras!=null&&extras.size()>0) {
            for (int i = 0; i < extras.size(); i++) {
                MtOrderDetail.KeyBean.ExtrasBean extrasBean = extras.get(i);
                String youhuiName = extrasBean.getRemark();
                float reduce_fee = extrasBean.getReduce_fee();
                String youhuiAcc = numDf.format(reduce_fee);
                if(youhuiName!=null) {
                    addFormatWmTest(pData, youhuiName, "", "","-" + youhuiAcc, size27);
                }else {
                    if(reduce_fee>0) {
                        addFormatWmTest(pData, "", "", "","-" + youhuiAcc, size27);
                    }
                }
            }
        }
        pData.add(new WmPintData(size27,line27));
        pData.add(new WmPintData(size35,"金额总计:￥"+mtOrder.getKey().getTotal()));
        if(mtOrder.getKey().getDinnersNumber()>0) {
            pData.add(new WmPintData(size27,"用餐人数:"+mtOrder.getKey().getDinnersNumber()));
        }
        pData.add(new WmPintData(size27,"订单编号:"+mtOrder.getKey().getOrderIdView()));

        pData.add(new WmPintData(size27,"下单时间:"+df4.format(mtOrder.getKey().getCtime() * 1000)));

        int hasInvoiced = mtOrder.getKey().getHasInvoiced();
        double total = mtOrder.getKey().getTotal();
        if(1==hasInvoiced) {//1-需要
            pData.add(new WmPintData(size27,"发票抬头:"+mtOrder.getKey().getInvoiceTitle()));
            pData.add(new WmPintData(size27,"发票金额:" + total + "元"));
        }
        wmCenterFormat("客户信息", "*", pData, size27);
        String recipientAddress = mtOrder.getKey().getRecipientAddress();
        if(recipientAddress.contains("@#")) {
            recipientAddress=recipientAddress.substring(0,recipientAddress.indexOf("@#"));
        }
        pData.add(new WmPintData(size35,recipientAddress));
        pData.add(new WmPintData(size35,mtOrder.getKey().getRecipientName()));
        pData.add(new WmPintData(size35,"电话:" + mtOrder.getKey().getRecipientPhone()));
//        pData.add("\n");
    }
    /**
     * 添加饿了么商户和客户共同部分
     * @param elmBean
     * @param pData
     */
    private void addELmGongTong(ElmOrderBean elmBean, List<WmPintData> pData) {
        pData.add(new WmPintData(size40, "饿了么 # " + elmBean.getData().getRestaurant_number()));
        if (0 == elmBean.getData().getIs_online_paid()) {//货到付款
            pData.add(new WmPintData(size27, "[货到付款]"));
        } else {
            pData.add(new WmPintData(size27, "[已在线支付]"));
        }
        if (!(elmBean.getData().getDeliver_time() == null)) {
            pData.add(new WmPintData(size35,"期望送达时间:"+elmBean.getData().getDeliver_time()));
        } else {
            pData.add(new WmPintData(size35, "期望送达时间:立即送达"));
        }
        if (!TextUtils.isEmpty(elmBean.getData().getDescription())) {
            pData.add(new WmPintData(size27,line27));
            pData.add(new WmPintData(size35, "订单备注:" + elmBean.getData().getDescription()));
        }

        wmCenterFormat("订单信息", "*", pData, size27);
        wmCenterFormat("菜品", "-", pData, size27);
        addFormatWmTest(pData, "菜品名称", "单价","数量", "金额", size27);
        ElmOrderBean.DataBean.DetailBean details = elmBean.getData().getDetail();
        if (details != null) {
            // 1. group菜品
            List<List<ElmOrderBean.DataBean.DetailBean.GroupBean>> group = details.getGroup();
            if (group != null && group.size() > 0) {
                List<ElmOrderBean.DataBean.DetailBean.GroupBean> groupBeans = new ArrayList<>();
                for (int i = 0; i < group.size(); i++) {
                    List<ElmOrderBean.DataBean.DetailBean.GroupBean> secondGroup = group.get(i);
                    if (secondGroup != null && secondGroup.size() > 0) {
                        for (int j = 0; j < secondGroup.size(); j++) {
                            ElmOrderBean.DataBean.DetailBean.GroupBean groupBean = secondGroup.get(j);
                            List<String> specs = groupBean.getSpecs();
                            String specsStr = "";
                            if (specs != null && specs.size() > 0) {
                                for (int a = 0; a < specs.size(); a++) {
                                    specsStr += specs.get(a);
                                }
                            }
                            String name;
                            if (!TextUtils.isEmpty(specsStr)) {
                                name = groupBean.getName() + "[" + specsStr + "]";
                            } else {
                                name = groupBean.getName();
                            }
                            float price = groupBean.getPrice();
                            int quantity = groupBean.getQuantity();
                            addFormatWmTest(pData, name, price+"",quantity + "", numDf.format(price * quantity), size35);
                        }
                    }
                }
            }
            // 2. extra优惠信息
            wmCenterFormat("其他", "-", pData, size27);
            if (elmBean.getData().getPackage_fee() >= 0) {
                addFormatWmTest(pData, "餐盒费", " ", " ", numDf.format(elmBean.getData().getPackage_fee()), size35);
            }
            addFormatWmTest(pData, "配送费", " ", " ", numDf.format(elmBean.getData().getDeliver_fee()), size35);
            List<ElmOrderBean.DataBean.DetailBean.ExtraBean> extras = details.getExtra();
            if (extras != null && extras.size() > 0) {
                for (int i = 0; i < extras.size(); i++) {
                    ElmOrderBean.DataBean.DetailBean.ExtraBean extraBean = extras.get(i);
                    int category_id = extraBean.getCategory_id();
                    if (category_id == 2 || category_id == 102) {
                        continue;
                    } else {
                        String name = extraBean.getName();
                        float price = extraBean.getPrice();
                        int quantity = extraBean.getQuantity();
                        LogUtils.Log("name==" + name + " price==" + price + "  quantity==" + quantity);
                        addFormatWmTest(pData, name, price+"",quantity + "", numDf.format(price * quantity), size27);
                    }
                }
            }
        }
        pData.add(new WmPintData(size27,line27));

        pData.add(new WmPintData(size35, "金额总计:￥" + elmBean.getData().getTotal_price()));
//        pData.add("用餐人数："+elmBean.getData().getDescription());
        pData.add(new WmPintData(size27, "订单编号:" + elmBean.getData().getOrder_id()));
        pData.add(new WmPintData(size27, "下单时间:" + elmBean.getData().getCreated_at()));
        int invoiced = elmBean.getData().getInvoiced();
        float total_price = elmBean.getData().getTotal_price();
        if (1 == invoiced) {//不需要
            pData.add(new WmPintData(size27, "发票抬头:" + elmBean.getData().getInvoice()));
            pData.add(new WmPintData(size27, "发票金额:" + numDf.format(total_price) + "元"));
        }
        wmCenterFormat("客户信息", "*", pData, size27);
        pData.add(new WmPintData(size35, elmBean.getData().getDelivery_poi_address()));
        pData.add(new WmPintData(size35, elmBean.getData().getConsignee()));
        pData.add(new WmPintData(size35, "电话:" + elmBean.getData().getPhone_list().get(0)));
    }
    /**
     * 新的饿了么商户和客户共同部分
     * @param newElmBean
     * @param pData
     */
    private void addNewELmGongTong(NewElmOrderBean newElmBean, List<WmPintData> pData) {
        pData.add(new WmPintData(size40, "饿了么 # " + newElmBean.getMessage().getDaySn()));

        if (!newElmBean.getMessage().isOnlinePaid()) {//货到付款
            pData.add(new WmPintData(size27, "[货到付款]"));
        } else {
            pData.add(new WmPintData(size27, "[已在线支付]"));
        }
        if (!(newElmBean.getMessage().getDeliverTime() == null)) {
            pData.add(new WmPintData(size35, "期望送达时间:" + newElmBean.getMessage().getDeliverTime().replace("T", " ")));
        } else {
            pData.add(new WmPintData(size35, "期望送达时间:立即送达"));
        }
        if (!TextUtils.isEmpty(newElmBean.getMessage().getDescription())) {
            pData.add(new WmPintData(size27,line27));
            pData.add(new WmPintData(size35, "订单备注:" + newElmBean.getMessage().getDescription()));
        }
        wmCenterFormat("订单信息", "*", pData, size27);
        wmCenterFormat("菜品", "-", pData,size27);
        addFormatWmTest(pData, "菜品名称", "单价","数量", "金额",size27);
        List<NewElmOrderBean.MessageBean.GroupsBean> groups = newElmBean.getMessage().getGroups();

        if (groups != null && groups.size() > 0) {
            // 1. group菜品
            for (int i = 0; i < groups.size(); i++) {
                String type = groups.get(i).getType();
                List<NewElmOrderBean.MessageBean.GroupsBean.ItemsBean> items = groups.get(i).getItems();
                if ("normal".equals(type)) {
                    for (int j = 0; j < items.size(); j++) {
                        NewElmOrderBean.MessageBean.GroupsBean.ItemsBean itemsBean = items.get(j);
                        addFormatWmTest(pData, itemsBean.getName(), itemsBean.getPrice()+"",itemsBean.getQuantity()+"", numDf.format(itemsBean.getPrice() * itemsBean.getQuantity()),size35);
                    }
                }
            }
            for (int i = 0; i < groups.size(); i++) {
                String type = groups.get(i).getType();
                List<NewElmOrderBean.MessageBean.GroupsBean.ItemsBean> items = groups.get(i).getItems();
                if ("discount".equals(type)) {
                    wmCenterFormat("赠品", "-", pData,size27);
                    for (int a = 0; a < items.size(); a++) {
                        NewElmOrderBean.MessageBean.GroupsBean.ItemsBean itemsBean = items.get(a);
                        addFormatWmTest(pData, itemsBean.getName(), itemsBean.getPrice()+"", itemsBean.getQuantity()+"", numDf.format(itemsBean.getPrice() * itemsBean.getQuantity()),size27);
                    }
                }
            }
            for (int i = 0; i < groups.size(); i++) {
                String type = groups.get(i).getType();
                List<NewElmOrderBean.MessageBean.GroupsBean.ItemsBean> items = groups.get(i).getItems();
                if ("extra".equals(type)) {
                    wmCenterFormat("其他", "-", pData,size27);
                    for (int a = 0; a < items.size(); a++) {
                        NewElmOrderBean.MessageBean.GroupsBean.ItemsBean itemsBean = items.get(a);
                        wmAddTwoSide(pData, "配送费", newElmBean.getMessage().getDeliverFee() + "", size35);
                        addFormatWmTest(pData, itemsBean.getName(), itemsBean.getPrice()+"", itemsBean.getQuantity()+"",
                                numDf.format(itemsBean.getPrice() * itemsBean.getQuantity()),size35);
                    }
                }

            }
            wmCenterFormat("优惠", "-", pData, size27);
            wmAddTwoSide(pData, "商家优惠", "" + newElmBean.getMessage().getActivityTotal(), size35);
            wmAddTwoSide(pData, "红包抵扣", "" + newElmBean.getMessage().getHongbao(), size35);
            pData.add(new WmPintData(size27,line27));
            double total_price = newElmBean.getMessage().getTotalPrice();
            pData.add(new WmPintData(size27, "金额总计:￥" + total_price));
            pData.add(new WmPintData(size27, "订单编号:" + newElmBean.getMessage().getId()));
            pData.add(new WmPintData(size27, "下单时间:" + newElmBean.getMessage().getCreatedAt().replace("T", " ")));


            boolean isInvoiced = newElmBean.getMessage().isInvoiced();
            if (isInvoiced) {//不需要
                pData.add(new WmPintData(size27, "发票抬头：" + newElmBean.getMessage().getInvoice()));
                pData.add(new WmPintData(size27, "发票金额：" + numDf.format(total_price) + "元"));
            }
            wmCenterFormat("客户信息", "*", pData, size27);
            pData.add(new WmPintData(size35, newElmBean.getMessage().getAddress()));
            pData.add(new WmPintData(size35, newElmBean.getMessage().getConsignee()));
            pData.add(new WmPintData(size35, "电话:" + newElmBean.getMessage().getPhoneList().get(0)));
        }
    }

    private void wmAddTwoSide(List<WmPintData> data, String left, String right,int textSize) {
        int cnNum = Utils.getChinaNum(left);
        int leftTotal = left.length() - cnNum + cnNum * 2;

        int sideLen=0;
        if(textSize==27) {
            sideLen=18;
        }else if(textSize==35) {
            sideLen=14;
        }
        if(leftTotal>=sideLen) {
            data.add(new WmPintData(textSize, left));
            wmAddTwoSide(data, left, right, textSize);
        }else {
            String space="";
            int length = (left + right).length();
            int chinaNum = Utils.getChinaNum(left+right);
            int otherChar = length - chinaNum;
            int total=0;
            if(isbt) {
                total=32;
            }else {
                if(textSize==27) {
                    total=28;
                }else if(textSize==35) {
                    total=21;
                }
            }
            int count=total-otherChar-chinaNum*2;

            for (int a = 0; a < count; a++) {
                space+=" ";
            }
            data.add(new WmPintData(textSize, left + space + right));
        }
    }

    public void formatPosDetailData(Context context,List<WmPintData> data, PosPrintBean posBean, DiscountInfo discountBean,boolean bt,String customerPermission,
                                    String merchantPermission) {
        this.isbt=bt;
        textSize=27;
        String printItems;
        if(isbt) {
            printItems = SpUtils.getInstance(context).getString(Constant.bt_print_permission, "");
        }else {
            printItems = SpUtils.getInstance(context).getString(Constant.print_permission, "");
        }


        if(printItems.contains("5")) {
            posDetailCommonPart(data, posBean, true, discountBean);
            if(discountBean!=null) {
                totalPrintMemberTest(discountBean, data);
            }else {
                wmCenterFormat("-","-", data,size27);
            }
            addBottomData(data);
            data.add(new WmPintData(size27,line27));
            data.add(new WmPintData(size27, "顾客联"));

            if(printItems.contains("4")) {
                data.add(new WmPintData(size27, "\n"));
                posDetailCommonPart(data, posBean, false, discountBean);
                if(!line27.equals(data.get(data.size()-1).getMsg()) && !line32.equals(data.get(data.size()-1).getMsg())) {
                    if(isbt){
                        data.add(new WmPintData(line32,size27,WmPintData.CENTER));
                    }else{
                        data.add(new WmPintData(line27,size27,WmPintData.CENTER));
                    }
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
                    if(isbt){
                        data.add(new WmPintData(line32,size27,WmPintData.CENTER));
                    }else{
                        data.add(new WmPintData(line27,size27,WmPintData.CENTER));
                    }
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

    private void addBottomData(List<WmPintData> data) {
        if(Constant.bottomData!=null&&Constant.bottomData.length>0) {
            for (int i = 0; i < Constant.bottomData.length; i++) {
                data.add(new WmPintData(Constant.bottomData[i],size27,WmPintData.LEFT));
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
        if(isbt){
            data.add(new WmPintData(line32,size27,WmPintData.CENTER));
        }else{
            data.add(new WmPintData(line27,size27,WmPintData.CENTER));
        }
        data.add(new WmPintData("下单时间:" + posBean.getMsg().getAddTime(), size27, WmPintData.LEFT));
        data.add(new WmPintData("结账时间:" + posBean.getMsg().getPayTime(), size27, WmPintData.LEFT));

        if(posBean.getMsg().getRoomName() != null && !TextUtils.isEmpty(posBean.getMsg().getRoomName())) {
            data.add(new WmPintData("订单序号：" + dayOrderNum, size27, WmPintData.LEFT));
        }

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
                        addFormatWmTest(data, goodsInfoBean.getName(), goodsInfoBean.getPrice()+"",goodsInfoBean.getNum(), numDf.format(Double.valueOf(goodsInfoBean.getNum()) * goodsInfoBean.getPrice()), size27);
                    }
                    addvipPriceNoticeForDetail(data,goodsInfoBean.getPrice(),goodsInfoBean.getOri_price());
                }
            }
        }
        //退款
        String isRefund = posBean.getMsg().getIsRefund();
        if("1".equals(isRefund)) {
            if(isbt){
                data.add(new WmPintData(line32,size27,WmPintData.CENTER));
            }else{
                data.add(new WmPintData(line27,size27,WmPintData.CENTER));
            }
            data.add(new WmPintData("退款时间:"+posBean.getMsg().getRefundTime(),size27,WmPintData.LEFT));
            data.add(new WmPintData("退款金额:"+posBean.getMsg().getRefundMoney()+"元",size27,WmPintData.LEFT));
            data.add(new WmPintData("操作账号:"+posBean.getMsg().getRefundOperator(),size27,WmPintData.LEFT));
            if(!needPhone) {
                if(isbt){
                    data.add(new WmPintData(line32,size27,WmPintData.CENTER));
                }else{
                    data.add(new WmPintData(line27,size27,WmPintData.CENTER));
                }
            }
        }
    }


    private void posDetailCommonPartForApplet(List<WmPintData> data, PosPrintBean posBean,boolean needPhone) {
        data.add(new WmPintData(Constant.STORE_P + Constant.STORE_M,size27,WmPintData.CENTER));
        if(needPhone) {
            data.add(new WmPintData("电话:" + STORE_TEL,size27,WmPintData.CENTER));
        }
        if(posBean.getMsg().getRoomName()!=null&&!"".equals(posBean.getMsg().getRoomName())){
            data.add(new WmPintData(posBean.getMsg().getRoomName(),size40,WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + posBean.getMsg().getDayOrderNum(),size40,WmPintData.CENTER));
        }
        if(posBean.getMsg().getOrderType()==3) {
            if(posBean.getMsg().getDeliveryStatus()==0||posBean.getMsg().getDeliveryStatus()==1) {
                data.add(new WmPintData("期望送达时间:",size27,WmPintData.CENTER));
                data.add(new WmPintData(posBean.getMsg().getDeliveryTime(),size27,WmPintData.CENTER));
                wmCenterFormat("顾客信息","-",data,size27);
                data.add(new WmPintData(size40,posBean.getMsg().getCneeAddress()));
                data.add(new WmPintData("姓名："+posBean.getMsg().getCneeName()));
                data.add(new WmPintData("手机号："+posBean.getMsg().getCneePhone()));
            }else {
                data.add(new WmPintData("自提:",size40,WmPintData.CENTER));
                wmCenterFormat("顾客信息","-",data,size27);
                data.add(new WmPintData("自提码："+posBean.getMsg().getOrderid(),size40,WmPintData.LEFT));
                data.add(new WmPintData("自提时间："+posBean.getMsg().getDeliveryTime()));
                data.add(new WmPintData("手机号："+posBean.getMsg().getCneePhone()));
            }

        }else {
            data.add(new WmPintData("顾客自助点单",size27,WmPintData.CENTER));
            //data.add(new WmPintData(size27, line27));
            if(isbt){
                data.add(new WmPintData(line32,size27,WmPintData.CENTER));
            }else{
                data.add(new WmPintData(line27,size27,WmPintData.CENTER));
            }
            if(posBean.getMsg().getOrderType()!=2) {
                data.add(new WmPintData("下单时间:" + posBean.getMsg().getAddTime()));
                data.add(new WmPintData("结账时间:" + posBean.getMsg().getPayTime()));
                if(posBean.getMsg().getRoomName()!=null&&!"".equals(posBean.getMsg().getRoomName())){
                    data.add(new WmPintData("订单序号:" + posBean.getMsg().getDayOrderNum()));
                }

                data.add(new WmPintData("应付金额:"+posBean.getMsg().getShouldPayMoney()+"元"));
                data.add(new WmPintData("实付金额:" + posBean.getMsg().getFactPayMoney() + "元"));
                data.add(new WmPintData("支付方式:" + posBean.getMsg().getPayType()));
                data.add(new WmPintData("商户单号:" + posBean.getMsg().getOrderNo()));
                if(posBean.getMsg().getTransaction_id()!=null&&!TextUtils.isEmpty(posBean.getMsg().getTransaction_id())) {
                    data.add(new WmPintData("交易单号:" + posBean.getMsg().getTransaction_id()));
                }
                if(posBean.getMsg().getVoucherInfo() != null){
                    switch (posBean.getMsg().getVoucherInfo().getType()){
                        case "CASH":
                            data.add(new WmPintData("优惠券:" + posBean.getMsg().getVoucherInfo().getTitle()));
                            break;
                        case "DISCOUNT":
                            data.add(new WmPintData("优惠券:" + posBean.getMsg().getVoucherInfo().getTitle()));
                            break;
                        case "MEMBER_CARD":
                            data.add(new WmPintData("会员折扣:" + posBean.getMsg().getDiscount() * 10 + "折"));
                            break;
                    }
                }else{
                    if(posBean.getMsg().getDiscount()>0) {
                        data.add(new WmPintData("会员折扣:" + posBean.getMsg().getDiscount() * 10 + "折"));
                    }
                }

            }else{
                if(posBean.getMsg().getRoomName()!=null&&!"".equals(posBean.getMsg().getRoomName())){
                    data.add(new WmPintData("订单序号:" + posBean.getMsg().getDayOrderNum()));
                }
            }

            data.add(new WmPintData("操作员:" + Constant.USER_ACCOUNT));
        }

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
        if(posBean.getMsg().getOrderType()==3) {
            if(isbt){
                data.add(new WmPintData(line32,size27,WmPintData.CENTER));
            }else{
                data.add(new WmPintData(line27,size27,WmPintData.CENTER));
            }
            data.add(new WmPintData("付款时间："+posBean.getMsg().getPayTime()));
            String deliveryCost = posBean.getMsg().getDeliveryCost();
            data.add(new WmPintData("商品金额："+Utils.keepTwoDecimal(posBean.getMsg().getShouldPayMoney()-Double.valueOf(deliveryCost)+"")+"元"));
            double discount = posBean.getMsg().getDiscount();
            if(discount>0) {
                data.add(new WmPintData("会员折扣:" + posBean.getMsg().getDiscount() + "折"));
            }
            data.add(new WmPintData("配送费："+ deliveryCost));
            data.add(new WmPintData("实付金额："+Utils.keepTwoDecimal(posBean.getMsg().getFactPayMoney()+"")+"元"));
            data.add(new WmPintData("支付方式："+posBean.getMsg().getPayType()));
            data.add(new WmPintData("商户单号："+posBean.getMsg().getOrderNo()));
        }
    }

    public  void newPosPrint(final IWoyouService iWoyouService, final List<WmPintData> printData,final ICallback callback
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
                                }else {
                                    if(wmPintData.getMsg().contains("原单价")) {
                                        iWoyouService.printText(wmPintData.getMsg(), null);
                                        iWoyouService.lineWrap(1, null);
                                    }else {
                                        iWoyouService.printText(wmPintData.getMsg(), null);
                                    }
                                }
                                iWoyouService.lineWrap(1,null);
                            }
                            iWoyouService.lineWrap(1, null);
                            iWoyouService.lineWrap(1, null);
                            iWoyouService.lineWrap(1, null);
                            iWoyouService.lineWrap(1, callback);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                     }
        });
    }
    public void showCanNotPrintDia(Context context) {
        String btprintItems = SpUtils.getInstance(context).getString(Constant.bt_print_permission, "");
        String btReplace = btprintItems.replace("1", "").trim();
        if(TextUtils.isEmpty(btReplace)) {
            TextView tv=new TextView(context);
            tv.setText("无法打印\n请您至“管理-打印设置-小票设置”中设置打印内容。");
            tv.setTextSize(Utils.px2sp(context, 25));
            tv.setTextColor(Color.parseColor("#292929"));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLineSpacing(0, 1.3f);
            tv.setPadding(Utils.dip2px(context,10),Utils.dip2px(context,15),Utils.dip2px(context,10),Utils.dip2px(context,0));
            new AlertDialog.Builder(context)
                    .setView(tv)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
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
            data.add(new WmPintData(size27,"实收金额："+goodssale.getSaletotal().getTotalprice()));
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

    /**
     * 格式化小程序打印数据格式
     * @param context
     * @param data
     * @param appLetReceiveBean
     * @param bt
     */
    public void formatAppletPrintData(Context context, List<WmPintData> data, AppLetReceiveBean appLetReceiveBean, boolean bt) {
        data.clear();
        this.isbt=bt;
        textSize=27;
        String printItems;
        boolean oneByOne;
        int orderType = appLetReceiveBean.getOrderType();
        if(isbt) {
            printItems = SpUtils.getInstance(context).getString(Constant.bt_print_permission, "");
            oneByOne=orderType==4?SpUtils.getInstance(context).getBoolean(Constant.BT_COMMUNITY_OBO, false):SpUtils.getInstance(context).getBoolean(Constant.BT_APPLET_OBO, false);
        }else {
            printItems = SpUtils.getInstance(context).getString(Constant.print_permission, "");
            oneByOne=orderType==4?SpUtils.getInstance(context).getBoolean(Constant.LOCAL_COMMUNITY_OBO, false):SpUtils.getInstance(context).getBoolean(Constant.LOCAL_APPLET_OBO, false);
        }
        String customer_permission="";
        String merchant_permission="";
        if(orderType==4) {
            customer_permission="a";
            merchant_permission="b";

        }else {
            customer_permission="9";
            merchant_permission="8";
        }


        if(printItems.contains(customer_permission)) {//顾客联
            appletCustomerGoods(data, appLetReceiveBean, true);
            if("1".equals(appLetReceiveBean.getPayStatus())){
            }else{
                wmCenterFormat("-","-",data,size27);
            }
            if(Constant.bottomData!=null&&Constant.bottomData.length>0) {
                for (int i = 0; i < Constant.bottomData.length; i++) {
                    data.add(new WmPintData(size27, Constant.bottomData[i]));
                }
            }
            if(isbt) {
                data.add(new WmPintData(size27, line32));
            }else {
                data.add(new WmPintData(size27, line27));
            }
            data.add(new WmPintData(size27, "顾客联"));
            if(printItems.contains(merchant_permission)) {//商家联
                data.add(new WmPintData(size27, "\n"));
                if(oneByOne) {
                    oneByOneAppletData(appLetReceiveBean,data,context ,bt);
                }else {
                    normalMerchant(appLetReceiveBean,data,context,bt);
                }
            }
        }else {
            if(printItems.contains(merchant_permission)) {//商家联
                if(oneByOne) {
                    oneByOneAppletData(appLetReceiveBean,data,context ,bt);
                }else {
                    normalMerchant(appLetReceiveBean,data,context,bt);
                }
            }else {
                data.clear();
            }
        }
    }

    //格式化顾客联的数据
    private void appletCustomerGoods(List<WmPintData> data, AppLetReceiveBean appLetBean,boolean needPhone) {
        wmCenterFormat(Constant.STORE_P + Constant.STORE_M," ",data,size27);
        if(needPhone) {
            wmCenterFormat("电话:" + Constant.STORE_TEL," ",data,size27);
        }
        if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
            data.add(new WmPintData(appLetBean.getRoomName(),size40,WmPintData.CENTER));
        }else{
            data.add(new WmPintData("订单序号:" + appLetBean.getDayOrderNum(),size40,WmPintData.CENTER));
        }

        if(appLetBean.getOrderType()==4) {
            if(appLetBean.getDeliveryStatus()==0||appLetBean.getDeliveryStatus()==1) {
                data.add(new WmPintData("期望送达时间",size27,WmPintData.CENTER));
                data.add(new WmPintData(appLetBean.getDeliveryTime(),size27,WmPintData.CENTER));
                wmCenterFormat("顾客信息","-",data, size27);
                data.add(new WmPintData(appLetBean.getCneeAddress(),size40,WmPintData.LEFT));
                data.add(new WmPintData("姓名："+appLetBean.getCneeName()));
                data.add(new WmPintData("手机号："+appLetBean.getCneePhone()));
            }else {
                data.add(new WmPintData("自提",size40,WmPintData.CENTER));
                wmCenterFormat("顾客信息","-",data, size27);
                data.add(new WmPintData("自提码："+appLetBean.getZt_code(),size40,WmPintData.CENTER));
                data.add(new WmPintData("自提时间："+appLetBean.getDeliveryTime()));
                data.add(new WmPintData("手机号："+appLetBean.getCneePhone()));
            }

        }else {
            wmCenterFormat("顾客自助点单"," ",data,size27);
            data.add(new WmPintData(size27, line27));
            data.add(new WmPintData("下单时间:" + appLetBean.getAddTime(),size27,WmPintData.LEFT));

            if(TextUtils.equals("1",appLetBean.getPayStatus())) {
                if(appLetBean.getPayTime()!=null&&!"".equals(appLetBean.getPayTime())&&!"0".equals(appLetBean.getPayTime())){
                    data.add(new WmPintData("结账时间:" + appLetBean.getPayTime(),size27,WmPintData.LEFT));
                }
                data.add(new WmPintData("应付金额:"+appLetBean.getShouldPayMoney()+"元",size27,WmPintData.LEFT));
                if(appLetBean.getFactPayMoney()>0) {
                    data.add(new WmPintData("实付金额:" + appLetBean.getFactPayMoney() + "元", size27, WmPintData.LEFT));
                }
                data.add(new WmPintData("支付方式:"+appLetBean.getPayType()));
                if(appLetBean.getTransaction_id()!=null&&!TextUtils.isEmpty(appLetBean.getTransaction_id())) {
                    data.add(new WmPintData("商户单号:"+appLetBean.getOrderNo()));
                }

            }

            if(appLetBean.getRoomName()!=null&&!"".equals(appLetBean.getRoomName())){
                data.add(new WmPintData("订单序号:"+appLetBean.getDayOrderNum(),size27,WmPintData.LEFT));
            }


        }
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
                    double ori_price = goodsInfoBean.getOri_price();
                    double price = goodsInfoBean.getPrice();
                    int is_vip = goodsInfoBean.getIs_vip();

                    addFormatWmTest(data, goodsInfoBean.getName(),Utils.keepTwoDecimal(price+"") ,Utils.keepTwoDecimal(goodsInfoBean.getNum()),
                            numDf.format(Double.valueOf(goodsInfoBean.getNum()) *price),size27);
                    if(is_vip==1&&ori_price-goodsInfoBean.getPrice()>0) {
                        data.add(new WmPintData("原单价:"+Utils.keepTwoDecimal(ori_price+"")+",会员优惠:"+Utils.keepTwoDecimal(ori_price-price+"")));
                    }
                }
            }
        }
        data.add(new WmPintData(line27));
        if(appLetBean.getOrderType()==4) {
            data.add(new WmPintData("付款时间："+appLetBean.getPayTime()));
            String deliveryCost = appLetBean.getDeliveryCost();
            data.add(new WmPintData("商品金额："+Utils.keepTwoDecimal(appLetBean.getShouldPayMoney()-Double.valueOf(deliveryCost)+"")+"元"));
            if(appLetBean.getDiscount()>0) {
                data.add(new WmPintData("会员折扣:" + appLetBean.getDiscount() * 10 + "折"));
            }
            data.add(new WmPintData("配送费："+ appLetBean.getDeliveryCost()));
            data.add(new WmPintData("实付金额："+Utils.keepTwoDecimal(appLetBean.getFactPayMoney()+"")));
            data.add(new WmPintData("支付方式："+appLetBean.getPayType()));
            data.add(new WmPintData("商户单号："+appLetBean.getOrderNo()));
            data.add(new WmPintData(line27));
        }
        //二维码投放信息
        AppLetReceiveBean.TicketInfoBean ticketInfo = appLetBean.getTicketInfo();
        if(ticketInfo!=null) {
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

    /**
     * pos机预结单
     * @param context
     * @param posBean
     * @param bt
     */
    public List<WmPintData> formatPosOrder(Context context, UnPayDetailsBean posBean,boolean bt) {
        List<WmPintData> data=new ArrayList<>();
        this.isbt=bt;
        textSize=27;
        String printItems;
        boolean oneByOne;
        if(isbt) {
            printItems = SpUtils.getInstance(context).getString(Constant.bt_print_permission, "");
            oneByOne=SpUtils.getInstance(context).getBoolean(Constant.BT_PRE_OBO,false);
        }else {
            printItems = SpUtils.getInstance(context).getString(Constant.print_permission, "");
            oneByOne=SpUtils.getInstance(context).getBoolean(Constant.LOCAL_PRE_OBO,false);
        }

        String customer_permission="3";
        String merchant_permission="2";

        //权限判断
        if(printItems.contains(customer_permission)) {//顾客联
            formatPreNormal(context,posBean,data,isbt,true);
            if(printItems.contains(merchant_permission)) {//商家联
                if(oneByOne) {
                    oneByOneData(posBean,context,data,bt);
                }else {
                    formatPreNormal(context,posBean,data,isbt,false);
                }
            }
        }else {
            if(printItems.contains(merchant_permission)) {//商家联
                if(oneByOne) {
                    oneByOneData(posBean,context,data,bt);
                }else {
                    formatPreNormal(context,posBean,data,isbt,false);
                }
            }else {
                data.clear();
            }
        }
        return data;
    }
    /**
     * pos机预结单
     * @param refundList
     * @param refund_no
     */
    public List<WmPintData> formatRefundOrder(ArrayList<PosPrintBean.MsgBean.GoodsInfoBean> refundList, double totalAcc,String refund_no, String time) {
        List<WmPintData> data=new ArrayList<>();
        data.add(new WmPintData(Constant.STORE_P+Constant.STORE_M,size35,WmPintData.CENTER));
        wmCenterFormat("退款商品","-",data,size27);
        addFormatWmTest(data,"商品名称","单价","数量","金额",size27);
        for (int i = 0; i < refundList.size(); i++) {
            addFormatWmTest(data,refundList.get(i).getName(), Utils.numf.format(refundList.get(i).getRefund_price()),Utils.numf.format(refundList.get(i).getRefund_num()),Utils.numf.format(refundList.get(i).getRefund_num()*refundList.get(i).getRefund_price()),size27);
        }
        data.add(new WmPintData(line27));
        data.add(new WmPintData("退款时间："+time));
        data.add(new WmPintData("退款金额："+Utils.numf.format(totalAcc)));
        data.add(new WmPintData("退货单号："+refund_no));
        data.add(new WmPintData("操作账号："+Constant.USER_ACCOUNT));
        data.add(new WmPintData(""));
        return data;
    }
    //小程序消息列表详情页
    public void formatAppletDetailData(Context context,List<WmPintData> data, PosPrintBean posBean,
                                       DiscountInfo discountBean,boolean bt) {
        this.isbt=bt;
        textSize=27;
        String printItems;
        boolean oneByOne;
        int orderType = posBean.getMsg().getOrderType();
        if(isbt) {
            printItems = SpUtils.getInstance(context).getString(Constant.bt_print_permission, "");
            oneByOne=orderType==3?SpUtils.getInstance(context).getBoolean(Constant.BT_COMMUNITY_OBO, false):SpUtils.getInstance(context).getBoolean(Constant.BT_APPLET_OBO, false);
        }else {
            printItems = SpUtils.getInstance(context).getString(Constant.print_permission, "");
            oneByOne=orderType==3?SpUtils.getInstance(context).getBoolean(Constant.LOCAL_COMMUNITY_OBO, false):SpUtils.getInstance(context).getBoolean(Constant.LOCAL_APPLET_OBO, false);
        }

        String customer_permission="";
        String merchant_permission="";
        if(orderType==3) {
            customer_permission="a";
            merchant_permission="b";

        }else {
            customer_permission="9";
            merchant_permission="8";
        }

        //权限判断
        if(printItems.contains(customer_permission)) {//顾客联
            posDetailCommonPartForApplet(data, posBean, true);
            if(discountBean!=null) {
                totalPrintMemberTest(discountBean, data);
            }else{
                wmCenterFormat("-","-", data,size27);
            }
            addBottom(data);
            if(printItems.contains(merchant_permission)) {//商家联
                if(oneByOne) {
                    oneByOneAppletHisData(posBean,data,context ,bt);
                }else {
                    normalMerchantFinishedOrder(posBean,data,context,bt);
                }
            }
        }else {
            if(printItems.contains(merchant_permission)) {//商家联
                if(oneByOne) {
                    oneByOneAppletHisData(posBean,data,context ,bt);
                }else {
                    normalMerchantFinishedOrder(posBean,data,context,bt);
                }
            }else {
                data.clear();
            }
        }
    }

    private void addBottom(List<WmPintData> data) {
        if(Constant.bottomData!=null&&Constant.bottomData.length>0) {
            for (int i = 0; i < Constant.bottomData.length; i++) {
                data.add(new WmPintData(size27, Constant.bottomData[i]));
            }
        }
        if(isbt) {
            data.add(new WmPintData(line32));
        }else {
            data.add(new WmPintData(line27));
        }
        data.add(new WmPintData(size27, "顾客联"));
        data.add(new WmPintData(size27, "\n"));
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
                wmCenterFormat(info," ",data,size27);
            }
        }
        if(discount_rules!=null&&discount_rules.size()>0) {
            for (int i = 0; i < discount_rules.size(); i++) {
                String youhui = "购物满" + discount_rules.get(i).getMoney() + "元享" + discount_rules.get(i).getRate() + "折优惠";
                wmCenterFormat(youhui," ",data,size27);
            }
        }
        if(url!=null&&!TextUtils.isEmpty(url)) {
            /*memberData.add("   ");
            memberData.add(url);*/
            wmCenterFormat("   "," ",data,size27);
            data.add(new WmPintData(7, url, WmPintData.CENTER, WmPintData.QRCODE));
        }
    }
}