package com.zjw.mvvm_demo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecognitionResponse {

    @SerializedName("result_num")
    private int resultNum;
    @SerializedName("log_id")
    private long logId;
    @SerializedName("result")
    private List<Result> result;

    public int getResultNum() {
        return resultNum;
    }

    public void setResultNum(int resultNum) {
        this.resultNum = resultNum;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result {
        @SerializedName("keyword")
        private String keyword;
        @SerializedName("score")
        private double score;
        @SerializedName("root")
        private String root;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }
    }
}
