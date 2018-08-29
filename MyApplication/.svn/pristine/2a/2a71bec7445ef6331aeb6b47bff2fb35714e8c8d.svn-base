package com.younle.younle624.myapplication.domain.setting;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/8/2.
 * 微信/e-mail:tt090423@126.com
 */
public class ManSettingBean implements Serializable{

    /**
     * code : 200
     * msg : [{"sortName":"初级技师","sortGoods":[{"id":"19","pname":"刘同","sname":"初级技师","usable":"1"}]},{"sortName":"中级技师","sortGoods":[{"id":"20","pname":"周世鸿","sname":"中级技师","usable":"1"}]},{"sortName":"高级技师","sortGoods":[{"id":"21","pname":"卓利伟","sname":"高级技师","usable":"1"}]}]
     */

    private int code;
    /**
     * sortName : 初级技师
     * sortGoods : [{"id":"19","pname":"刘同","sname":"初级技师","usable":"1"}]
     */

    private List<MsgBean> msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean implements Serializable{
        private String sortName;
        /**
         * id : 19
         * pname : 刘同
         * sname : 初级技师
         * usable : 1
         */

        private List<SortGoodsBean> sortGoods;

        public String getSortName() {
            return sortName;
        }

        public void setSortName(String sortName) {
            this.sortName = sortName;
        }

        public List<SortGoodsBean> getSortGoods() {
            return sortGoods;
        }

        public void setSortGoods(List<SortGoodsBean> sortGoods) {
            this.sortGoods = sortGoods;
        }

        public static class SortGoodsBean implements Serializable {
            private String id;
            private String pname;
            private String sname;
            private String usable;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPname() {
                return pname;
            }

            public void setPname(String pname) {
                this.pname = pname;
            }

            public String getSname() {
                return sname;
            }

            public void setSname(String sname) {
                this.sname = sname;
            }

            public String getUsable() {
                return usable;
            }

            public void setUsable(String usable) {
                this.usable = usable;
            }
        }
    }
}
