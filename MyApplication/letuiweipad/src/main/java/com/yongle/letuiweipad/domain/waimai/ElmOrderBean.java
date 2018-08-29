package com.yongle.letuiweipad.domain.waimai;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Create by 我是奋斗 on2016/12/30 14:09
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 * 饿了么订单实体类
 */
public class ElmOrderBean implements Serializable {


    /**
     * code : 200
     * data : {"active_at":"2017-01-03 18:01:33","activity_total":0,"address":"小小植物园b_602","consignee":"渣渣 先生","created_at":"2017-01-03 18:01:18","deliver_fee":0,"delivery_geo":"31.22987,121.47004","delivery_poi_address":"小小植物园b_602(上海市黄浦区黄陂北路249弄)","description":"变态辣","detail":{"abandoned_extra":[],"extra":[{"category_id":102,"description":"","id":-70000,"name":"餐盒","price":0.01,"quantity":1}],"group":[[{"attrs":[],"category_id":1,"garnish":[],"id":520925638,"name":"锅包肉","new_specs":[],"price":0.01,"quantity":1,"sale_mode":0,"sku_id":"133787853709","specs":["加辣","不要葱"]}]]},"eleme_part":0,"hongbao":0,"income":0.02,"invoice":"北京用乐科技","invoiced":1,"is_book":0,"is_online_paid":1,"order_id":"101899117025698701","original_price":0.02,"package_fee":0.01,"phone_list":["17346512596"],"refund_code":0,"restaurant_id":2128266,"restaurant_name":"用乐科技测试店","restaurant_number":2,"restaurant_part":0,"service_fee":0,"service_rate":0,"status_code":2,"total_price":0.02,"user_id":167743073,"user_name":"409eec6d4"}
     * fromplat : 2
     * message : ok
     * request_id : 8fafebcdffb94951ac5beacdfdce74fb
     */

    /**
     * active_at : 2017-01-03 18:01:33
     * activity_total : 0
     * address : 小小植物园b_602
     * consignee : 渣渣 先生
     * created_at : 2017-01-03 18:01:18
     * deliver_fee : 0
     * delivery_geo : 31.22987,121.47004
     * delivery_poi_address : 小小植物园b_602(上海市黄浦区黄陂北路249弄)
     * description : 变态辣
     * detail : {"abandoned_extra":[],"extra":[{"category_id":102,"description":"","id":-70000,"name":"餐盒","price":0.01,"quantity":1}],"group":[[{"attrs":[],"category_id":1,"garnish":[],"id":520925638,"name":"锅包肉","new_specs":[],"price":0.01,"quantity":1,"sale_mode":0,"sku_id":"133787853709","specs":["加辣","不要葱"]}]]}
     * eleme_part : 0
     * hongbao : 0
     * income : 0.02
     * invoice : 北京用乐科技
     * invoiced : 1
     * is_book : 0
     * is_online_paid : 1
     * order_id : 101899117025698701
     * original_price : 0.02
     * package_fee : 0.01
     * phone_list : ["17346512596"]
     * refund_code : 0
     * restaurant_id : 2128266
     * restaurant_name : 用乐科技测试店
     * restaurant_number : 2
     *
     * restaurant_part : 0
     * service_fee : 0
     * service_rate : 0
     * status_code : 2
     * total_price : 0.02
     * user_id : 167743073
     * user_name : 409eec6d4
     */
    private DataBean data;
    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
    public static class DataBean implements Serializable{
        private String deliver_time="";
        private String consignee;
        private String created_at;
        private float deliver_fee;
        private String delivery_poi_address;
        private String description;
        private DetailBean detail;
        private float income;
        private String invoice="";
        private int invoiced;
        private int is_online_paid;
        private String order_id;
        private float original_price;
        private float package_fee=-1;
        private String restaurant_name;
        private int restaurant_number;
        private float total_price;
        private List<String> phone_list;

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public float getDeliver_fee() {
            return deliver_fee;
        }

        public void setDeliver_fee(float deliver_fee) {
            this.deliver_fee = deliver_fee;
        }
        public String getDelivery_poi_address() {
            return delivery_poi_address;
        }

        public void setDelivery_poi_address(String delivery_poi_address) {
            this.delivery_poi_address = delivery_poi_address;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }
        public float getIncome() {
            return income;
        }

        public void setIncome(float income) {
            this.income = income;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public int getInvoiced() {
            return invoiced;
        }

        public void setInvoiced(int invoiced) {
            this.invoiced = invoiced;
        }
        public int getIs_online_paid() {
            return is_online_paid;
        }

        public void setIs_online_paid(int is_online_paid) {
            this.is_online_paid = is_online_paid;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public float getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(float original_price) {
            this.original_price = original_price;
        }

        public float getPackage_fee() {
            return package_fee;
        }

        public void setPackage_fee(float package_fee) {
            this.package_fee = package_fee;
        }
        public String getRestaurant_name() {
            return restaurant_name;
        }

        public void setRestaurant_name(String restaurant_name) {
            this.restaurant_name = restaurant_name;
        }

        public int getRestaurant_number() {
            return restaurant_number;
        }

        public void setRestaurant_number(int restaurant_number) {
            this.restaurant_number = restaurant_number;
        }
        public float getTotal_price() {
            return total_price;
        }

        public void setTotal_price(float total_price) {
            this.total_price = total_price;
        }
        public List<String> getPhone_list() {
            return phone_list;
        }

        public void setPhone_list(List<String> phone_list) {
            this.phone_list = phone_list;
        }

        public String getDeliver_time() {
            return deliver_time;
        }

        public void setDeliver_time(String deliver_time) {
            this.deliver_time = deliver_time;
        }

        public static class DetailBean implements Serializable{
            /**
             * category_id : 102
             * description :
             * id : -70000
             * name : 餐盒
             * price : 0.01
             * quantity : 1
             */

            private List<ExtraBean> extra;
            /**
             * attrs : []
             * category_id : 1
             * garnish : []
             * id : 520925638
             * name : 锅包肉
             * new_specs : []
             * price : 0.01
             * quantity : 1
             * sale_mode : 0
             * sku_id : 133787853709
             * specs : ["加辣","不要葱"]
             */

            private List<List<GroupBean>> group;

            public List<ExtraBean> getExtra() {
                return extra;
            }

            public void setExtra(List<ExtraBean> extra) {
                this.extra = extra;
            }

            public List<List<GroupBean>> getGroup() {
                return group;
            }

            public void setGroup(List<List<GroupBean>> group) {
                this.group = group;
            }

            public static class ExtraBean implements Serializable {
                private int category_id;
//                private String description;
//                private long id;
                private String name;
                private float price;
                private int quantity;

                public int getCategory_id() {
                    return category_id;
                }

                public void setCategory_id(int category_id) {
                    this.category_id = category_id;
                }
                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public float getPrice() {
                    return price;
                }

                public void setPrice(float price) {
                    this.price = price;
                }

                public int getQuantity() {
                    return quantity;
                }

                public void setQuantity(int quantity) {
                    this.quantity = quantity;
                }
            }

            public static class GroupBean implements Serializable {
//                private int category_id;
//                private int id;
                private String name;
                private float price;
                private int quantity;
//                private int sale_mode;
//                private String sku_id;
                private List<String> specs;
                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public float getPrice() {
                    return price;
                }

                public void setPrice(float price) {
                    this.price = price;
                }

                public int getQuantity() {
                    return quantity;
                }

                public void setQuantity(int quantity) {
                    this.quantity = quantity;
                }

                public List<String> getSpecs() {
                    return specs;
                }

                public void setSpecs(List<String> specs) {
                    this.specs = specs;
                }
            }
        }
    }
}
