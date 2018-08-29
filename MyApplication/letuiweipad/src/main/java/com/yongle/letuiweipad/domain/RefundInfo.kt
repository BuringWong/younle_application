package com.younle.younle624.myapplication.domain.orderbean

import java.io.Serializable

/**
 * Created by BurNing.Wong on 2018/8/9 0009.
 * 邮箱：tt090423@126.com
 */
class RefundInfo :Serializable{
    var addtime:Long=0
    var goods_name:String=""
    var handle_person:String=""
    var refund_money:Double=0.00
    var refund_no:String=""
    var refund_num:Double=0.00
    override fun toString(): String {
        return "RefundInfo(addTime=$addtime, goods_name='$goods_name', handle_person='$handle_person', refund_money=$refund_money, refund_no='$refund_no', refund_num=$refund_num)"
    }

}