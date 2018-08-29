package com.younle.younle624.myapplication.domain.waimai;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Create by 我是奋斗 on2017/3/23 14:21
 * 邮箱：tt090423@126.com
 * 微信：17346512596
 */
public class NewElmOrderBean implements Serializable {

        /**
         * appId : 59758388
         * fromplat : 21
         * message : {"activeAt":"2017-03-23T14:56:23","activityTotal":0,"address":"上海交通大学(闵行校区)1001","book":false,"consignee":"庞一鹤 先生","createdAt":"2017-03-23T14:56:23","daySn":6,"deliverFee":0,"deliveryGeo":"121.43645,31.02567","deliveryPoiAddress":"上海交通大学(闵行校区)1001","description":"","downgraded":false,"elemePart":0,"groups":[{"items":[{"additions":[],"categoryId":1,"id":556687981,"name":"老王臊子面","price":0.01,"quantity":1,"skuId":-1,"total":0.01}],"name":"1号篮子","type":"normal"}],"hongbao":0,"id":"1203846973688183898","income":0.01,"invoiced":false,"onlinePaid":true,"orderId":"1203846973688183898","originalPrice":0.01,"packageFee":0,"phoneList":["18210396402"],"refundStatus":"noRefund","serviceFee":0,"serviceRate":0,"shopId":150994781,"shopName":"乐推微门店派测试店铺","shopPart":0,"status":"unprocessed","totalPrice":0.01,"userId":27889135,"vipDeliveryFeeDiscount":0}
         * requestId : 100000881167201640
         * shopId : 150994781
         * signature : 8AD560B1F6426DE5C472C86538814A5C
         * timestamp : 1490252183517
         * type : 10
         * userId : 256660322308421422
         * activeAt : 2017-03-23T14:56:23
         * activityTotal : 0
         * address : 上海交通大学(闵行校区)1001
         * book : false
         * consignee : 庞一鹤 先生
         * createdAt : 2017-03-23T14:56:23
         * daySn : 6
         * deliverFee : 0
         * deliveryGeo : 121.43645,31.02567
         * deliveryPoiAddress : 上海交通大学(闵行校区)1001
         * description :
         * downgraded : false
         * elemePart : 0
         * groups : [{"items":[{"additions":[],"categoryId":1,"id":556687981,"name":"老王臊子面","price":0.01,"quantity":1,"skuId":-1,"total":0.01}],"name":"1号篮子","type":"normal"}]
         * hongbao : 0
         * id : 1203846973688183898
         * income : 0.01
         * invoiced : false
         * onlinePaid : true
         * orderId : 1203846973688183898
         * originalPrice : 0.01
         * packageFee : 0
         * phoneList : ["18210396402"]
         * refundStatus : noRefund
         * serviceFee : 0
         * serviceRate : 0
         * shopId : 150994781
         * shopName : 乐推微门店派测试店铺
         * shopPart : 0
         * status : unprocessed
         * totalPrice : 0.01
         * userId : 27889135
         * vipDeliveryFeeDiscount : 0
         */
        private MessageBean message;

        public MessageBean getMessage() {
            return message;
        }

        public void setMessage(MessageBean message) {
            this.message = message;
        }

        public static class MessageBean implements Serializable {
            private double activityTotal;

            private String address;
            private String consignee;
            private String createdAt;
            private String deliverTime;
            private int daySn;
            private double deliverFee;
            private String deliveryPoiAddress;
            private String description;
            private String id;
            private double elemePart;
            private double hongbao;
            private boolean invoiced;
            private String invoice;
            private boolean onlinePaid;
            private double originalPrice;
            private double packageFee;
            private String shopName;
            private double shopPart;
            private double totalPrice;
            /**
             * items : [{"additions":[],"categoryId":1,"id":556687981,"name":"老王臊子面","price":0.01,"quantity":1,"skuId":-1,"total":0.01}]
             * name : 1号篮子
             * type : normal
             */

            private List<GroupsBean> groups;
            private List<String> phoneList;

            public double getActivityTotal() {
                return activityTotal;
            }

            public void setActivityTotal(double activityTotal) {
                this.activityTotal = activityTotal;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getConsignee() {
                return consignee;
            }

            public void setConsignee(String consignee) {
                this.consignee = consignee;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getDeliverTime() {
                return deliverTime;
            }

            public void setDeliverTime(String deliverTime) {
                this.deliverTime = deliverTime;
            }

            public int getDaySn() {
                return daySn;
            }

            public void setDaySn(int daySn) {
                this.daySn = daySn;
            }

            public double getDeliverFee() {
                return deliverFee;
            }

            public void setDeliverFee(double deliverFee) {
                this.deliverFee = deliverFee;
            }

            public String getDeliveryPoiAddress() {
                return deliveryPoiAddress;
            }

            public void setDeliveryPoiAddress(String deliveryPoiAddress) {
                this.deliveryPoiAddress = deliveryPoiAddress;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public double getElemePart() {
                return elemePart;
            }

            public void setElemePart(double elemePart) {
                this.elemePart = elemePart;
            }

            public double getHongbao() {
                return hongbao;
            }

            public void setHongbao(double hongbao) {
                this.hongbao = hongbao;
            }

            public boolean isInvoiced() {
                return invoiced;
            }

            public void setInvoiced(boolean invoiced) {
                this.invoiced = invoiced;
            }

            public String getInvoice() {
                return invoice;
            }

            public void setInvoice(String invoice) {
                this.invoice = invoice;
            }

            public boolean isOnlinePaid() {
                return onlinePaid;
            }

            public void setOnlinePaid(boolean onlinePaid) {
                this.onlinePaid = onlinePaid;
            }

            public double getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(double originalPrice) {
                this.originalPrice = originalPrice;
            }

            public double getPackageFee() {
                return packageFee;
            }

            public void setPackageFee(double packageFee) {
                this.packageFee = packageFee;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public double getShopPart() {
                return shopPart;
            }

            public void setShopPart(double shopPart) {
                this.shopPart = shopPart;
            }

            public double getTotalPrice() {
                return totalPrice;
            }

            public void setTotalPrice(double totalPrice) {
                this.totalPrice = totalPrice;
            }

            public List<GroupsBean> getGroups() {
                return groups;
            }

            public void setGroups(List<GroupsBean> groups) {
                this.groups = groups;
            }

            public List<String> getPhoneList() {
                return phoneList;
            }

            public void setPhoneList(List<String> phoneList) {
                this.phoneList = phoneList;
            }

            public static class GroupsBean implements Serializable {
                private String name;
                private String type;
                /**
                 * additions : []
                 * categoryId : 1
                 * id : 556687981
                 * name : 老王臊子面
                 * price : 0.01
                 * quantity : 1
                 * skuId : -1
                 * total : 0.01
                 */

                private List<ItemsBean> items;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public List<ItemsBean> getItems() {
                    return items;
                }

                public void setItems(List<ItemsBean> items) {
                    this.items = items;
                }

                public static class ItemsBean implements Serializable {
                    private int categoryId;
                    private String name;
                    private double price;
                    private int quantity;
                    private double total;

                    public int getCategoryId() {
                        return categoryId;
                    }

                    public void setCategoryId(int categoryId) {
                        this.categoryId = categoryId;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public double getPrice() {
                        return price;
                    }

                    public void setPrice(double price) {
                        this.price = price;
                    }

                    public int getQuantity() {
                        return quantity;
                    }

                    public void setQuantity(int quantity) {
                        this.quantity = quantity;
                    }

                    public double getTotal() {
                        return total;
                    }

                    public void setTotal(double total) {
                        this.total = total;
                    }
                }
            }
        }
}
