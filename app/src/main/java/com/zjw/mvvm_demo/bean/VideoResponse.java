package com.zjw.mvvm_demo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {


    @SerializedName("reason")
    private String reason;
    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("result")
    private List<Result> result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result {
        @SerializedName("title")
        private String title;
        @SerializedName("share_url")
        private String shareUrl;
        @SerializedName("author")
        private String author;
        @SerializedName("item_cover")
        private String itemCover;
        @SerializedName("hot_value")
        private int hotValue;
        @SerializedName("hot_words")
        private String hotWords;
        @SerializedName("play_count")
        private int playCount;
        @SerializedName("digg_count")
        private int diggCount;
        @SerializedName("comment_count")
        private int commentCount;

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

        public int getHotValue() {
            return hotValue;
        }

        public void setHotValue(int hotValue) {
            this.hotValue = hotValue;
        }

        public String getHotWords() {
            return hotWords;
        }

        public void setHotWords(String hotWords) {
            this.hotWords = hotWords;
        }

        public int getPlayCount() {
            return playCount;
        }

        public void setPlayCount(int playCount) {
            this.playCount = playCount;
        }

        public int getDiggCount() {
            return diggCount;
        }

        public void setDiggCount(int diggCount) {
            this.diggCount = diggCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }
    }
}
