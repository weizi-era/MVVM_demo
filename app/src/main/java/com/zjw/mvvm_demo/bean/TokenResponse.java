package com.zjw.mvvm_demo.bean;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {


    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("expires_in")
    private long expiresIn;
    @SerializedName("session_key")
    private String sessionKey;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("scope")
    private String scope;
    @SerializedName("session_secret")
    private String sessionSecret;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public void setSessionSecret(String sessionSecret) {
        this.sessionSecret = sessionSecret;
    }
}
