package com.zjw.mvvm_demo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WallPaperResponse {


    @SerializedName("msg")
    private String msg;
    @SerializedName("res")
    private Res res;
    @SerializedName("code")
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Res getRes() {
        return res;
    }

    public void setRes(Res res) {
        this.res = res;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class Res {
        @SerializedName("vertical")
        private List<Vertical> vertical;

        public List<Vertical> getVertical() {
            return vertical;
        }

        public void setVertical(List<Vertical> vertical) {
            this.vertical = vertical;
        }

        public static class Vertical {
            @SerializedName("views")
            private int views;
            @SerializedName("ncos")
            private int ncos;
            @SerializedName("rank")
            private int rank;
            @SerializedName("source_type")
            private String sourceType;
            @SerializedName("wp")
            private String wp;
            @SerializedName("xr")
            private boolean xr;
            @SerializedName("cr")
            private boolean cr;
            @SerializedName("favs")
            private int favs;
            @SerializedName("atime")
            private double atime;
            @SerializedName("id")
            private String id;
            @SerializedName("desc")
            private String desc;
            @SerializedName("thumb")
            private String thumb;
            @SerializedName("img")
            private String img;
            @SerializedName("rule")
            private String rule;
            @SerializedName("preview")
            private String preview;
            @SerializedName("store")
            private String store;
            @SerializedName("tag")
            private List<String> tag;
            @SerializedName("cid")
            private List<String> cid;
            @SerializedName("url")
            private List<?> url;

            public int getViews() {
                return views;
            }

            public void setViews(int views) {
                this.views = views;
            }

            public int getNcos() {
                return ncos;
            }

            public void setNcos(int ncos) {
                this.ncos = ncos;
            }

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }

            public String getSourceType() {
                return sourceType;
            }

            public void setSourceType(String sourceType) {
                this.sourceType = sourceType;
            }

            public String getWp() {
                return wp;
            }

            public void setWp(String wp) {
                this.wp = wp;
            }

            public boolean isXr() {
                return xr;
            }

            public void setXr(boolean xr) {
                this.xr = xr;
            }

            public boolean isCr() {
                return cr;
            }

            public void setCr(boolean cr) {
                this.cr = cr;
            }

            public int getFavs() {
                return favs;
            }

            public void setFavs(int favs) {
                this.favs = favs;
            }

            public double getAtime() {
                return atime;
            }

            public void setAtime(double atime) {
                this.atime = atime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getRule() {
                return rule;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }

            public String getPreview() {
                return preview;
            }

            public void setPreview(String preview) {
                this.preview = preview;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public List<String> getTag() {
                return tag;
            }

            public void setTag(List<String> tag) {
                this.tag = tag;
            }

            public List<String> getCid() {
                return cid;
            }

            public void setCid(List<String> cid) {
                this.cid = cid;
            }

            public List<?> getUrl() {
                return url;
            }

            public void setUrl(List<?> url) {
                this.url = url;
            }
        }
    }
}
