package com.zjw.mvvm_demo.db.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "news")
public class News {

    @PrimaryKey(autoGenerate = true)
    private int uid = 0;
    @SerializedName("uniquekey")
    private String uniquekey;
    @SerializedName("title")
    private String title;
    @SerializedName("date")
    private String date;
    @SerializedName("category")
    private String category;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("url")
    private String url;
    @SerializedName("thumbnail_pic_s")
    private String thumbnailPicS;
    @SerializedName("is_content")
    private String isContent;

    public News() {
    }

    @Ignore
    public News(String uniquekey, String title, String date, String category, String authorName, String url, String thumbnailPicS, String isContent) {
        this.uniquekey = uniquekey;
        this.title = title;
        this.date = date;
        this.category = category;
        this.authorName = authorName;
        this.url = url;
        this.thumbnailPicS = thumbnailPicS;
        this.isContent = isContent;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailPicS() {
        return thumbnailPicS;
    }

    public void setThumbnailPicS(String thumbnailPicS) {
        this.thumbnailPicS = thumbnailPicS;
    }

    public String getIsContent() {
        return isContent;
    }

    public void setIsContent(String isContent) {
        this.isContent = isContent;
    }
}
