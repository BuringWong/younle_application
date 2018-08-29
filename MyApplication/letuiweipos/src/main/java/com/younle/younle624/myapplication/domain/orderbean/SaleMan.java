package com.younle.younle624.myapplication.domain.orderbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 我是奋斗 on 2016/7/16.
 * 微信/e-mail:tt090423@126.com
 */
public class SaleMan implements Serializable{
    /**
     * content : [{"id":2,"salename":"张三"},{"id":3,"salename":"李四"}]
     * name : 销售
     */

    private List<SalesBean> sales;

    public List<SalesBean> getSales() {
        return sales;
    }

    public void setSales(List<SalesBean> sales) {
        this.sales = sales;
    }

    public static class SalesBean implements Serializable{
        private boolean isChecked;
        private String name;
        /**
         * id : 2
         * salename : 张三
         */

        private List<ContentBean> content;

        public boolean isChecked() {
            return isChecked;
        }

        public void setIsChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class ContentBean implements Serializable {
            private int id;
            private String salename;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSalename() {
                return salename;
            }

            public void setSalename(String salename) {
                this.salename = salename;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "id=" + id +
                        ", salename='" + salename + '\'' +
                        '}';
            }
        }
    }



  /*  private String name;
    private  int waiterId;

    public SaleMan() {
    }

    public SaleMan(String name, int waiterId) {
        this.name = name;
        this.waiterId = waiterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(int waiterId) {
        this.waiterId = waiterId;
    }

    @Override
    public String toString() {
        return "SaleMan{" +
                "name='" + name + '\'' +
                ", waiterId=" + waiterId +
                '}';
    }*/
}
