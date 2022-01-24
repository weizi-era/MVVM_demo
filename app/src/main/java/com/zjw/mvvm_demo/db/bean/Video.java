package com.zjw.mvvm_demo.db.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "video")
public class Video {

    @PrimaryKey(autoGenerate = true)
    private int uid = 0;
    @SerializedName("title")
    private String title;
    @SerializedName("share_url")
    private String shareUrl;
    @SerializedName("author")
    private String author;
    @SerializedName("item_cover")
    private String itemCover;
    @SerializedName("hot_words")
    private String hotWords;


    public Video() {
    }

    @Ignore
    public Video(String title, String shareUrl, String author, String itemCover, String hotWords) {
        this.title = title;
        this.shareUrl = shareUrl;
        this.author = author;
        this.itemCover = itemCover;
        this.hotWords = hotWords;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getItemCover() {
        return itemCover;
    }

    public void setItemCover(String itemCover) {
        this.itemCover = itemCover;
    }

    public String getHotWords() {
        return hotWords;
    }

    public void setHotWords(String hotWords) {
        this.hotWords = hotWords;
    }
}
