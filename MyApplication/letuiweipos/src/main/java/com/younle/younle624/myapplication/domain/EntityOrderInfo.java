package com.younle.younle624.myapplication.domain;

import java.util.List;

/**
 * Created by 我是奋斗 on 2016/6/6.
 * 微信/e-mail:tt090423@126.com
 */
public class EntityOrderInfo {
    private String dealTime;
    private List<Entity> entities;

    public EntityOrderInfo() {
    }

    public EntityOrderInfo(String dealTime, List<Entity> entities) {
        this.dealTime = dealTime;
        this.entities = entities;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return "ServiceOrderInfo{" +
                "dealTime='" + dealTime + '\'' +
                ", serviceEntities=" + entities +
                '}';
    }

    public static class Entity {
        private String entityName;
        private String num;
        private String total;
        private String buyer;
        private String status;

        public Entity() {
        }

        public Entity(String entityName, String num, String total, String buyer, String status) {
            this.entityName = entityName;
            this.num = num;
            this.total = total;
            this.buyer = buyer;
            this.status = status;
        }

        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "entityName='" + entityName + '\'' +
                    ", num='" + num + '\'' +
                    ", total='" + total + '\'' +
                    ", buyer='" + buyer + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }


}
