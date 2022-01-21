package com.zjw.mvvm_demo.db.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * 从网络返回的数据中抽取需要保存的字段
 */
@Entity(tableName = "wallpaper")
public class WallPaper {

    @PrimaryKey(autoGenerate = true)
    private int uid = 0;
    private String img;

    public WallPaper() {
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    @Ignore
    public WallPaper(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
