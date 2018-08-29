package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by bert_dong on 2016/10/11 0011.
 * 邮箱：18701038771@163.com
 */
public class RoomsInfoBean {

    /**
     * code : 200
     * msg : {"costtype":["0","1","2"],"roominfo":[{"num":"2","product_typeid":"3","product_typename":"大厅","roomlist":[{"id":"5","is_billing":"3","minconsum":"0.00","product_typeid":"3","roomdeposit":"0.00","roomname":"标准1","rule":[{"info":[{"cost":"5.0","times":"0:00~12:00"},{"cost":"15.0","times":"12:00~18:00"},{"cost":"25.0","times":"18:00~24:00"}],"wday":1},{"info":[{"cost":"30.0","times":"0:00~12:00"},{"cost":"40.0","times":"12:00~18:00"},{"cost":"50.0","times":"18:00~24:00"}],"wday":2}],"used":"1","useinfo":[{"addtime":"1478765875","end_time":"0","roomid":"5","start_time":"1478765700"},{"addtime":"1478765875","end_time":"0","roomid":"5","start_time":"1478765700"},{"addtime":"1478765875","end_time":"0","roomid":"5","start_time":"1478765700"}]}]}]}
     */

    private int code;
    private MsgBean msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private List<String> costtype;
        /**
         * num : 2
         * product_typeid : 3
         * product_typename : 大厅
         * roomlist : [{"id":"5","is_billing":"3","minconsum":"0.00","product_typeid":"3","roomdeposit":"0.00","roomname":"标准1","rule":[{"info":[{"cost":"5.0","times":"0:00~12:00"},{"cost":"15.0","times":"12:00~18:00"},{"cost":"25.0","times":"18:00~24:00"}],"wday":1},{"info":[{"cost":"30.0","times":"0:00~12:00"},{"cost":"40.0","times":"12:00~18:00"},{"cost":"50.0","times":"18:00~24:00"}],"wday":2}],"used":"1","useinfo":[{"addtime":"1478765875","end_time":"0","roomid":"5","start_time":"1478765700"},{"addtime":"1478765875","end_time":"0","roomid":"5","start_time":"1478765700"},{"addtime":"1478765875","end_time":"0","roomid":"5","start_time":"1478765700"}]}]
         */

        private List<RoominfoBean> roominfo;

        public List<String> getCosttype() {
            return costtype;
        }

        public void setCosttype(List<String> costtype) {
            this.costtype = costtype;
        }

        public List<RoominfoBean> getRoominfo() {
            return roominfo;
        }

        public void setRoominfo(List<RoominfoBean> roominfo) {
            this.roominfo = roominfo;
        }

        public static class RoominfoBean {
            private String num;
            private String product_typeid;
            private String product_typename;
            private boolean isChoosed;
            /**
             * id : 5
             * is_billing : 3
             * minconsum : 0.00
             * product_typeid : 3
             * roomdeposit : 0.00
             * roomname : 标准1
             * rule : [{"info":[{"cost":"5.0","times":"0:00~12:00"},{"cost":"15.0","times":"12:00~18:00"},{"cost":"25.0","times":"18:00~24:00"}],"wday":1},{"info":[{"cost":"30.0","times":"0:00~12:00"},{"cost":"40.0","times":"12:00~18:00"},{"cost":"50.0","times":"18:00~24:00"}],"wday":2}]
             * used : 1
             * useinfo : [{"addtime":"1478765875","end_time":"0","roomid":"5","start_time":"1478765700"},{"addtime":"1478765875","end_time":"0","roomid":"5","start_time":"1478765700"},{"addtime":"1478765875","end_time":"0","roomid":"5","start_time":"1478765700"}]
             */

            private List<RoomlistBean> roomlist;

            public boolean isChoosed() {
                return isChoosed;
            }

            public void setIsChoosed(boolean isChoosed) {
                this.isChoosed = isChoosed;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getProduct_typeid() {
                return product_typeid;
            }

            public void setProduct_typeid(String product_typeid) {
                this.product_typeid = product_typeid;
            }

            public String getProduct_typename() {
                return product_typename;
            }

            public void setProduct_typename(String product_typename) {
                this.product_typename = product_typename;
            }

            public List<RoomlistBean> getRoomlist() {
                return roomlist;
            }

            public void setRoomlist(List<RoomlistBean> roomlist) {
                this.roomlist = roomlist;
            }

            public static class RoomlistBean {
                private String id;
                private String is_billing;
                private String minconsum;
                private String product_typeid;
                private String roomdeposit;
                private String roomname;
                private String used;
                private boolean Checked;
                /**
                 * info : [{"cost":"5.0","times":"0:00~12:00"},{"cost":"15.0","times":"12:00~18:00"},{"cost":"25.0","times":"18:00~24:00"}]
                 * wday : 1
                 */

                private List<RuleBean> rule;
                /**
                 * addtime : 1478765875
                 * end_time : 0
                 * roomid : 5
                 * start_time : 1478765700
                 */

                /*private List<UseinfoBean> useinfo;*/

                public boolean isChecked() {
                    return Checked;
                }

                public void setChecked(boolean checked) {
                    Checked = checked;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getIs_billing() {
                    return is_billing;
                }

                public void setIs_billing(String is_billing) {
                    this.is_billing = is_billing;
                }

                public String getMinconsum() {
                    return minconsum;
                }

                public void setMinconsum(String minconsum) {
                    this.minconsum = minconsum;
                }

                public String getProduct_typeid() {
                    return product_typeid;
                }

                public void setProduct_typeid(String product_typeid) {
                    this.product_typeid = product_typeid;
                }

                public String getRoomdeposit() {
                    return roomdeposit;
                }

                public void setRoomdeposit(String roomdeposit) {
                    this.roomdeposit = roomdeposit;
                }

                public String getRoomname() {
                    return roomname;
                }

                public void setRoomname(String roomname) {
                    this.roomname = roomname;
                }

                public String getUsed() {
                    return used;
                }

                public void setUsed(String used) {
                    this.used = used;
                }

                public List<RuleBean> getRule() {
                    return rule;
                }

                public void setRule(List<RuleBean> rule) {
                    this.rule = rule;
                }

                /*public List<UseinfoBean> getUseinfo() {
                    return useinfo;
                }

                public void setUseinfo(List<UseinfoBean> useinfo) {
                    this.useinfo = useinfo;
                }*/

                public static class RuleBean {
                    private int wday;
                    /**
                     * cost : 5.0
                     * times : 0:00~12:00
                     */

                    private List<InfoBean> info;

                    public int getWday() {
                        return wday;
                    }

                    public void setWday(int wday) {
                        this.wday = wday;
                    }

                    public List<InfoBean> getInfo() {
                        return info;
                    }

                    public void setInfo(List<InfoBean> info) {
                        this.info = info;
                    }

                    public static class InfoBean {
                        private String cost;
                        private String times;
                        private int wday;
                        /**
                         * 0:不是会员 1：是会员
                         */
                        private int isvip;

                        public int getIsvip() {
                            return isvip;
                        }

                        public void setIsvip(int isvip) {
                            this.isvip = isvip;
                        }

                        public int getWday() {
                            return wday;
                        }

                        public void setWday(int wday) {
                            this.wday = wday;
                        }

                        public String getCost() {
                            return cost;
                        }

                        public void setCost(String cost) {
                            this.cost = cost;
                        }

                        public String getTimes() {
                            return times;
                        }

                        public void setTimes(String times) {
                            this.times = times;
                        }
                    }
                }

                /*public static class UseinfoBean {
                    private String addtime;
                    private String end_time;
                    private String roomid;
                    private String start_time;

                    public String getAddtime() {
                        return addtime;
                    }

                    public void setAddtime(String addtime) {
                        this.addtime = addtime;
                    }

                    public String getEnd_time() {
                        return end_time;
                    }

                    public void setEnd_time(String end_time) {
                        this.end_time = end_time;
                    }

                    public String getRoomid() {
                        return roomid;
                    }

                    public void setRoomid(String roomid) {
                        this.roomid = roomid;
                    }

                    public String getStart_time() {
                        return start_time;
                    }

                    public void setStart_time(String start_time) {
                        this.start_time = start_time;
                    }
                }*/
            }
        }
    }
}
