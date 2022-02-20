package com.zjw.mvvm_demo.bean;

public class TodoState {

    private int uid;
    private boolean isSelected;

    public TodoState(int uid, boolean isSelected) {
        this.uid = uid;
        this.isSelected = isSelected;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
