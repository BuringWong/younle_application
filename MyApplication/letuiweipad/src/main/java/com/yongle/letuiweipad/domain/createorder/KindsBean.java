package com.yongle.letuiweipad.domain.createorder;

/**
 * Created by Administrator on 2017/9/15.
 */

public class KindsBean {
    private String name;
    private boolean checked;

    public KindsBean(String name) {
        this.name = name;
    }

    public KindsBean(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
