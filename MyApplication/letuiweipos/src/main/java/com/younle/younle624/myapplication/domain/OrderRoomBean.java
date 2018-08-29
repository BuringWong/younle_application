package com.younle.younle624.myapplication.domain;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 我是奋斗 on 2016/7/7.
 * 微信/e-mail:tt090423@126.com
 */
@Table(name = "order_room")
public class OrderRoomBean {
    @Column(name = "room_id",isId = true)
    private int roomId;
    @Column(name ="room_name")
    private String roomName;
    @Column(name ="low_resume")
    private double lowResume;
    @Column(name = "deposit")
    private double deposit;

}
