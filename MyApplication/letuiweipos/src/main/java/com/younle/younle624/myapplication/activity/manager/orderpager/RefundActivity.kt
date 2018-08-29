package com.younle.younle624.myapplication.activity.manager.orderpager

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.PopupWindow
import com.google.gson.Gson
import com.igexin.sdk.GTServiceManager.context
import com.younle.younle624.myapplication.R
import com.younle.younle624.myapplication.adapter.RecyclerAdapter
import com.younle.younle624.myapplication.constant.Constant
import com.younle.younle624.myapplication.constant.UrlConstance
import com.younle.younle624.myapplication.domain.orderbean.PosPrintBean
import com.younle.younle624.myapplication.domain.waimai.WmPintData
import com.younle.younle624.myapplication.selfinterface.OnRefundGoodsSelectListener
import com.younle.younle624.myapplication.utils.LogUtils
import com.younle.younle624.myapplication.utils.NetWorks
import com.younle.younle624.myapplication.utils.Utils
import com.younle.younle624.myapplication.utils.notice.NoticePopuUtils
import com.younle.younle624.myapplication.utils.notice.SelfToast
import com.younle.younle624.myapplication.utils.printmanager.PrintUtils
import kotlinx.android.synthetic.main.activity_refund.*
import kotlinx.android.synthetic.main.titlebar_upgrade.*
import org.json.JSONException
import org.json.JSONObject
import woyou.aidlservice.jiuiv5.ICallback
import woyou.aidlservice.jiuiv5.IWoyouService
import java.lang.Exception
import java.util.*

class RefundActivity<T> : Activity() {
    private val toastCode = Arrays.asList(20103, 20107, 20108, 20117, 20119, 20121, 20133)

    var TAG="RefundActivity"
    var totalRefundNum=0.00;
    var totalRefundAcc=0.00;
    var refundList=ArrayList<PosPrintBean.MsgBean.GoodsInfoBean>()
    var posBean:PosPrintBean?=null
    var goodsInfo:ArrayList<PosPrintBean.MsgBean.GoodsInfoBean>?=null
    var netWorks:NetWorks= NetWorks(this)
    lateinit var iWoyouService:IWoyouService
    var conn:ServiceConnection= object:ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iWoyouService=IWoyouService.Stub.asInterface(service)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refund)
        tv_title.text="退款"
        PrintUtils.getInstance().initService(this,conn)

    }

    override fun onResume() {
        LogUtils.e(TAG,"onResume")
        super.onResume()
        posBean=intent.getSerializableExtra("goods_data") as PosPrintBean
        goodsInfo= posBean!!.msg.goodsInfo as ArrayList<PosPrintBean.MsgBean.GoodsInfoBean>
        var llManager = LinearLayoutManager(this)
        llManager.orientation=LinearLayoutManager.VERTICAL
        content.layoutManager=llManager
        content.setHasFixedSize(true)
        var decoraton= DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        content.addItemDecoration(decoraton)
        (content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations=false
        var adapter = RecyclerAdapter<PosPrintBean.MsgBean.GoodsInfoBean>(this)

        adapter.setData(goodsInfo,1)
        content.adapter=adapter
        adapter.notifyDataSetChanged()
        ll_cancel.setOnClickListener {
            finish()
        }

        for(item in goodsInfo!!){
            item.left_num=item.num.toDouble()-item.refund_num
            item.refund_num=0.00
        }
        adapter.setOnRefundGoodsSelectListener(object :OnRefundGoodsSelectListener{
            override fun onGoodsSelected() {
                totalRefundAcc=0.00
                totalRefundNum=0.00
                for (item in goodsInfo!!){
                    if (item.refund_num>0){
                        totalRefundNum+=item.refund_num
                        totalRefundAcc+=(item.refund_price*item.refund_num)
                    }
                }
                refund_total.setText("数量："+Utils.numf.format(totalRefundNum)+", 金额："+Utils.numf.format(totalRefundAcc))
            }
        })

        sure_refund.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (totalRefundNum<=0){
                    Utils.showToast(this@RefundActivity,"请选择要退款的商品")
                    return
                }
                showBindPup("确定要退款吗")
            }
        })

    }

    private  val MARK: Int=0
    private  val SURE: Int=1
    private  val MARK_FAILREFUND_ORDER: Int=2

    /**
     * 展示退款不可撤销的pop
     * @param msg
     * @return
     */
    fun showBindPup(msg: String): PopupWindow {
        return NoticePopuUtils.showBindPup(this, msg, R.id.refund_root, object : NoticePopuUtils.OnClickCallBack {
            override fun onClickYes() {
                startCircle()
                if (posBean!!.getMsg().getPayType().contains("记账")) {
                    startRefund(MARK)
                } else {
                    startRefund(SURE)
                }
            }
            override fun onClickNo() {
                LogUtils.Log("取消删除")
            }
        })

    }

    private fun startRefund(type: Int) {
        refundList.clear()
        for (item in goodsInfo!!){
            if (item.refund_num>0){
                refundList.add(item)
            }
        }

        var goodJson= Gson().toJson(refundList)
        LogUtils.e(TAG,"提交的商品："+goodJson)
        LogUtils.e(TAG,posBean!!.msg.orderNo)
        var params=netWorks.publicParams
        params.put(Constant.PARAMS_NEME_STORE_ID,Constant.STORE_ID)
        params.put(Constant.PARAMS_ADV_ID,Constant.ADV_ID)
        params.put("order_id",posBean!!.msg.orderid)
        params.put("order_type",posBean!!.msg.orderType.toString())
        params.put("refund_money",totalRefundAcc.toString())
        params.put("good_json",goodJson)
        params.put("order_no",posBean!!.msg.orderNo)
        params.put("out_order_no",posBean!!.msg.outOrderNo)
        params.put("refund_type",type.toString())
        netWorks.Request(UrlConstance.COMMIT_REFUND_ORDER,params,5000,0,object :NetWorks.OnNetCallBack{
            override fun onError(e: Exception?, flag: Int) {
                LogUtils.e(TAG,"e=="+e.toString())
            }

            override fun onResonse(response: String?, flag: Int) {
                LogUtils.e(TAG,"response=="+response.toString())
                if (type==MARK_FAILREFUND_ORDER){
                    praseReReFundJson(response)
                }else{
                    parseRefundJson(response)
                }
            }
        })
    }
    private fun praseReReFundJson(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val code = jsonObject.getInt("code")
            if (code == 200) {
                var refundNo:String=jsonObject.getJSONObject("msg").getString("refundNo")
                var refundtime:String=jsonObject.getJSONObject("msg").getString("refundtime")
                refunSuccess(response,refundNo,refundtime)

            } else {
                val msg = jsonObject.getString("msg")
                Utils.showToast(this, msg, 1000)
                stopCirle()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    /**
     * 解析退款结果
     * @param json
     */
    private fun parseRefundJson(json: String?) {
        try {
            val jsonObject = JSONObject(json)
            val code = jsonObject.getInt("code")
            val msg = jsonObject.getString("msg")
            if (code == 200) {
                //退款成功
                var refundNo:String=jsonObject.getJSONObject("msg").getString("refundNo")
                var refundtime:String=jsonObject.getJSONObject("msg").getString("refundtime")
                refunSuccess(json,refundNo,refundtime)
            } else {
                if (toastCode.contains(code)) {
                    //toast
                    stopCirle()
                    Utils.showToast(this, msg, 1000)
                } else {
                    //弹出
                    val alertDialog = NoticePopuUtils.refundErrorDia(this, msg, "暂不退款", "我已退款", "款项未退还", object : NoticePopuUtils.OnClickCallBack {
                        override fun onClickYes() {
                            startRefund(MARK_FAILREFUND_ORDER)
                        }
                        override fun onClickNo() {
                            stopCirle()
                        }
                    })
                    alertDialog.setOnCancelListener {
                        alertDialog.dismiss()
                        stopCirle()
                    }
                }
            }

        } catch (e: JSONException) {
            LogUtils.Log("退款返回json解析异常：" + e.toString())
            e.printStackTrace()
        }
    }

    /**
     * 退款中的ui
     */
    private fun startCircle() {
        iv_callbacking.setVisibility(View.VISIBLE)
        Utils.pbAnimation(this, iv_callbacking)
    }

    private fun stopCirle() {
        iv_callbacking.clearAnimation()
        iv_callbacking.setVisibility(View.GONE)
    }

    /**
     * 退款成功
     */
    private fun refunSuccess(json: String?,refundno:String?,refundtime:String?) {
        stopCirle()
        if (posBean!!.getMsg().payType.contains("记账")) {
            SelfToast.showToast(context, "已标记退款", 1000)
        } else {
            SelfToast.showToast(context, "已退款", 1000)
        }

        var printData:List<WmPintData> = PrintUtils.getInstance().formatRefundOrder(refundList,totalRefundAcc,refundno,Utils.df3.format((refundtime+"000").toLong())) as List<WmPintData>
        if (iWoyouService!=null){
            PrintUtils.getInstance().printWm(iWoyouService,printData, object : ICallback.Stub() {
                override fun onReturnString(result: String?) {
                }

                override fun onRaiseException(code: Int, msg: String?) {
                }

                override fun onRunResult(isSuccess: Boolean) {
                    if (isSuccess){
                        setResult(1,Intent())
                        finish()
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        unbindService(conn)
        super.onDestroy()
    }
}
