package com.zjw.mvvm_demo.bean;

import com.amap.api.services.weather.LocalWeatherLive;

public class LiveWeather {
    private String district;
    private LocalWeatherLive localWeatherLive;

    public LiveWeather(String district, LocalWeatherLive localWeatherLive) {
        this.district = district;
        this.localWeatherLive = localWeatherLive;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public LocalWeatherLive getLocalWeatherLive() {
        return localWeatherLive;
    }

    public void setLocalWeatherLive(LocalWeatherLive localWeatherLive) {
        this.localWeatherLive = localWeatherLive;
    }

}
