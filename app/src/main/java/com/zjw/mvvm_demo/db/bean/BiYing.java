package com.zjw.mvvm_demo.db.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * 从网络返回的数据中抽取需要保存的字段
 */
@Entity(tableName = "biying")
public class BiYing {

    @PrimaryKey
    private int uid;
    private String url;
    private String urlbase;
    private String copyright;
    private String copyrightlink;
    private String title;

    @Ignore
    public BiYing(int uid, String url, String urlbase, String copyright, String copyrightlink, String title) {
        this.uid = uid;
        this.url = url;
        this.urlbase = urlbase;
        this.copyright = copyright;
        this.copyrightlink = copyrightlink;
        this.title = title;
    }

    public BiYing() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlbase() {
        return urlbase;
    }

    public void setUrlbase(String urlbase) {
        this.urlbase = urlbase;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCopyrightlink() {
        return copyrightlink;
    }

    public void setCopyrightlink(String copyrightlink) {
        this.copyrightlink = copyrightlink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
