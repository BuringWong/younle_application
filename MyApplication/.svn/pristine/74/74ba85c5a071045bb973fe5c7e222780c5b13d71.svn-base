package com.younle.younle624.myapplication.domain.orderbean;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/10/9.
 * 微信/e-mail:tt090423@126.com
 */
public class TestBean {

    /**
     * code : 200
     * msg : {"info":[{"id":"3","is_billing":"1","is_used":"1","minconsum":"0.00","product_typeid":"2","product_typename":"大厅(2)","roomdeposit":"0.00","roomname":"标准","useinfo":[{"end_time":"0","roomid":"3","start_time":"1475046001"}]},{"id":"4","is_billing":"1","is_used":"1","minconsum":"0.00","product_typeid":"2","product_typename":"大厅(2)","roomdeposit":"0.00","roomname":"标准2"}],"typename":[{"product_typeid":"1","product_typename":"大厅(1)"},{"product_typeid":"2","product_typename":"大厅(2)"},{"product_typeid":"3","product_typename":"大厅(3)"}]}
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
        /**
         * id : 3
         * is_billing : 1
         * is_used : 1
         * minconsum : 0.00
         * product_typeid : 2
         * product_typename : 大厅(2)
         * roomdeposit : 0.00
         * roomname : 标准
         * useinfo : [{"end_time":"0","roomid":"3","start_time":"1475046001"}]
         */

        private List<InfoBean> info;
        /**
         * product_typeid : 1
         * product_typename : 大厅(1)
         */

        private List<TypenameBean> typename;

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public List<TypenameBean> getTypename() {
            return typename;
        }

        public void setTypename(List<TypenameBean> typename) {
            this.typename = typename;
        }

        public static class InfoBean {
            private String id;
            private String is_billing;
            private String is_used;
            private String minconsum;
            private String product_typeid;
            private String product_typename;
            private String roomdeposit;
            private String roomname;
            /**
             * end_time : 0
             * roomid : 3
             * start_time : 1475046001
             */

            private List<UseinfoBean> useinfo;

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

            public String getIs_used() {
                return is_used;
            }

            public void setIs_used(String is_used) {
                this.is_used = is_used;
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

            public String getProduct_typename() {
                return product_typename;
            }

            public void setProduct_typename(String product_typename) {
                this.product_typename = product_typename;
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

            public List<UseinfoBean> getUseinfo() {
                return useinfo;
            }

            public void setUseinfo(List<UseinfoBean> useinfo) {
                this.useinfo = useinfo;
            }

            public static class UseinfoBean {
                private String end_time;
                private String roomid;
                private String start_time;

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
            }
        }

        public static class TypenameBean {
            private String product_typeid;
            private String product_typename;

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
        }
    }
}
