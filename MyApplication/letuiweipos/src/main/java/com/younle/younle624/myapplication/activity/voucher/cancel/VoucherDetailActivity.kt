package com.younle.younle624.myapplication.activity.voucher.cancel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.younle.younle624.myapplication.R
import com.younle.younle624.myapplication.adapter.ordermanager.PosDetailAdapter
import com.younle.younle624.myapplication.constant.UrlConstance
import com.younle.younle624.myapplication.domain.ZtBean
import com.younle.younle624.myapplication.utils.NetWorks
import com.younle.younle624.myapplication.utils.Utils
import kotlinx.android.synthetic.main.activity_voucher_detail.*
import kotlinx.android.synthetic.main.titlebar_all.*
import org.json.JSONObject
import java.lang.Exception

class VoucherDetailActivity : Activity() {

    private lateinit var orderBean: ZtBean
    private lateinit var tv_should_pay: TextView
    private lateinit var tv_discount_dec: TextView
    private lateinit var tv_discount_reduce_money: TextView
    private lateinit var tv_actual_pay: TextView
    private lateinit var tv_mark_msg: TextView
    private lateinit var tv_mark_state: TextView
    private lateinit var tv_add_time: TextView
    private lateinit var tv_deal_time: TextView
    private lateinit var tv_order_id: TextView
    private lateinit var tv_pay_way: TextView
    private lateinit var youhui_info: TextView
    private lateinit var ll_remark: LinearLayout

    private var code: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voucher_detail)
        orderBean =intent.getSerializableExtra("ztbean") as ZtBean
        code=intent.getStringExtra("code")
        title_all.setOnClickListener{
            finish()
        }
        tv_title.setText("核销内容")
        initHeaderFooter()
        var adapter= PosDetailAdapter<ZtBean.GoodsListBean>(this)
        var containMemberGood=false
        for(item in orderBean.goods_list){
            if (item.is_vip==1)
                containMemberGood= true
            break
        }
        adapter.setContainsVipGoods(containMemberGood)
        adapter.setData(orderBean.goods_list)
        lv_content.adapter = adapter
        lv_content.divider=null
        initBtnStatus()

        btn_hx.setOnClickListener{
            if (btn_hx.text.equals("返回")){
                finish()
            }else{
                hxOrder()
            }
        }
    }

    private fun initBtnStatus() {
        if(orderBean.order.is_refund==1){
            /*btn_hx.isEnabled=false
            btn_hx.setBackgroundResource(R.drawable.native_btn_selector)*/
            btn_hx.setText("返回")
        }else{
            if (orderBean.order.deliver_status==3){
                btn_hx.setText("返回")
                /*btn_hx.isEnabled=false
                btn_hx.setBackgroundResource(R.drawable.native_btn_selector)
                btn_hx.setText("已自提")
                btn_hx.setTextColor(Color.parseColor("#333333"))*/
            }else if(orderBean.order.deliver_status==2){
                btn_hx.isEnabled=true
            }
        }
    }

    private fun hxOrder() {
        var netWork= NetWorks(this)
        var params=netWork.publicParams
        params.put("action","1")
        params.put("code",code)
        Utils.showWaittingDialog(this,"正在核销")
        netWork.Request(UrlConstance.HX_VOUCHER_URL,params,5000,0,object:NetWorks.OnNetCallBack{
            override fun onError(e: Exception?, flag: Int) {
                Utils.dismissWaittingDialog()
                Utils.showToast(this@VoucherDetailActivity,"网络异常，请检查网络后重试！",2000)
            }

            override fun onResonse(response: String?, flag: Int) {
                Utils.dismissWaittingDialog()
                praseJson(response)
            }

        } )
    }

    private fun praseJson(json: String?) {
        try {
            var obj= JSONObject(json)
            var code=obj.getInt("code")
            val msg = obj.getString("msg")
            when(code) {
                200 -> {
                    startActivity(Intent(this@VoucherDetailActivity, CancelSuccess::class.java))
                    finish()
                }
                1001, 1002, 1003 -> Toast.makeText(this@VoucherDetailActivity, msg, Toast.LENGTH_SHORT).show()
                1006, 1007, 1008 -> {
                    val intent = Intent(this@VoucherDetailActivity, CancelFail::class.java)
                    intent.putExtra("error_msg", msg)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    val intent2 = Intent(this@VoucherDetailActivity, CancelFail::class.java)
                    intent2.putExtra("error_msg", msg)
                    startActivity(intent2)
                    finish()
                }
            }
        }catch (e: Exception){
            Toast.makeText(this@VoucherDetailActivity, "数据解析异常", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initHeaderFooter() {
        var header= View.inflate(this,R.layout.voucher_list_header,null)
        var tvQueryNum: TextView = header.findViewById(R.id.query_num) as TextView
        if (orderBean.order.deliver_status==3){
            tvQueryNum.setText(orderBean.order_title+"（已自提）")
        }else{
            tvQueryNum.setText(orderBean.order_title+"（待自提）")
        }

        var posFooter = View.inflate(this, R.layout.pos_detail_footer_for_coupons, null)
        posFooter.findViewById(R.id.rl_deliver_fee).visibility= View.GONE
        posFooter.findViewById(R.id.second_depart).visibility= View.GONE
        posFooter.findViewById(R.id.ll_refund_msg).visibility= View.GONE
        posFooter.findViewById(R.id.foot_line).visibility= View.VISIBLE
        posFooter.findViewById(R.id.rl_discount_coupons_info).visibility= View.GONE
        var merchant_id=posFooter.findViewById(R.id.merchant_id) as TextView
        merchant_id.setText("订单编号:")

        tv_should_pay=posFooter.findViewById(R.id.tv_should_pay) as TextView
        tv_discount_dec=posFooter.findViewById(R.id.tv_discount_dec) as TextView
        tv_discount_reduce_money=posFooter.findViewById(R.id.tv_discount_reduce_money) as TextView
        tv_actual_pay=posFooter.findViewById(R.id.tv_actual_pay) as TextView
        tv_mark_msg=posFooter.findViewById(R.id.tv_mark_msg) as TextView
        tv_mark_state=posFooter.findViewById(R.id.tv_mark_state) as TextView
        tv_add_time=posFooter.findViewById(R.id.tv_add_time) as TextView
        tv_deal_time=posFooter.findViewById(R.id.tv_deal_time) as TextView
        tv_order_id=posFooter.findViewById(R.id.tv_order_id) as TextView
        tv_pay_way=posFooter.findViewById(R.id.tv_pay_way) as TextView
        youhui_info=posFooter.findViewById(R.id.youhui_info) as TextView
        ll_remark=posFooter.findViewById(R.id.ll_remark) as LinearLayout

        lv_content.addHeaderView(header)
        lv_content.addFooterView(posFooter)
        tv_should_pay.setText(orderBean.money.total_fee)
        tv_actual_pay.setText(orderBean.money.payment)
        if (orderBean.money.price!=null&&!TextUtils.isEmpty(orderBean.money.price)){
            youhui_info.setText(orderBean.money.price)
        }else{
            youhui_info.visibility= View.GONE
        }
        if (orderBean.orderinfo!=null&&!TextUtils.isEmpty(orderBean.orderinfo)){
            ll_remark.visibility= View.VISIBLE
            tv_mark_state.visibility= View.GONE
            tv_mark_msg.setText(orderBean.orderinfo)

        }else{
            ll_remark.visibility= View.GONE
            tv_mark_state.visibility= View.VISIBLE
        }
        tv_add_time.setText(orderBean.order.addtime)
        tv_deal_time.setText(orderBean.order.paytime)
        tv_pay_way.setText(orderBean.order.pay_type)
        tv_order_id.setText(orderBean.order.order_no)
    }
}
