package com.yongle.letuiweipad.domain.goodinfo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by bert_dong on 2018/6/13 0013.
 * 邮箱：18701038771@163.com
 */
@Entity
public class GoodsTypeInfo  {
    @Id(autoincrement = true)
    private Long id;
    private int typeId;
    private String name;
    @Transient
    private boolean isChecked=false;
    @Generated(hash = 1618519997)
    public GoodsTypeInfo(Long id, int typeId, String name) {
        this.id = id;
        this.typeId = typeId;
        this.name = name;
    }
    @Generated(hash = 1654544899)
    public GoodsTypeInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getTypeId() {
        return this.typeId;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "GoodsTypeInfo{" +
                "id=" + id +
                ", typeId=" + typeId +
                ", name='" + name + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
