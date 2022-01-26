package com.zjw.mvvm_demo.ui.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.adapter.ForecastAdapter;
import com.zjw.mvvm_demo.bean.LiveWeather;
import com.zjw.mvvm_demo.databinding.DialogWeatherBinding;
import com.zjw.mvvm_demo.databinding.MapFragmentBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MapFragment extends BaseFragment implements AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener, WeatherSearch.OnWeatherSearchListener {

    private MapViewModel mViewModel;
    private MapFragmentBinding binding;
    private MyLocationStyle myLocationStyle;

    public AMapLocationClientOption mLocationOption;
    public AMapLocationClient mLocationClient;

    //解析成功标识码
    private static final int PARSE_SUCCESS_CODE = 1000;

    private GeocodeSearch geocoderSearch = null;
    private String district = null;// 区/县

    private LocalWeatherLive liveResult;
    private LocalWeatherForecast forecastResult;

    private WeatherSearchQuery mQuery;
    private WeatherSearch weatherSearch;

    //天气预报列表
    private List<LocalDayWeatherForecast> weatherForecast;

    private AMap aMap;
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        //基于个人隐私保护的关系，这里要设置为true，否则会出现地图白屏的情况
        MapsInitializer.updatePrivacyShow(requireContext(), true, true);
        MapsInitializer.updatePrivacyAgree(requireContext(), true);
        binding.mapView.onCreate(savedInstanceState);

        //点击按钮显示天气弹窗
        binding.fabWeather.setOnClickListener(v -> showWeatherDialog());

        initMap();

        initSearch();
    }

    private void initMap() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = binding.mapView.getMap();
        }

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(myLocationStyle); //设置定位蓝点的Style
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
        aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true); // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);
    }

    /**
     * 初始化搜索
     */
    private void initSearch() {
        try {
            geocoderSearch = new GeocodeSearch(requireActivity());
            geocoderSearch.setOnGeocodeSearchListener(this);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索天气
     * @param type type
     */
    private void searchWeather(int type) {
        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        mQuery = new WeatherSearchQuery(district, type);
        try {
            weatherSearch = new WeatherSearch(requireContext());
            weatherSearch.setOnWeatherSearchListener(this);
            weatherSearch.setQuery(mQuery);
            weatherSearch.searchWeatherAsyn(); //异步搜索
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示天气弹窗
     */
    private void showWeatherDialog() {
        binding.fabWeather.hide();

        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle_Light);
        DialogWeatherBinding weatherBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.dialog_weather, null, false);
        //设置数据源
        weatherBinding.setLiveWeather(new LiveWeather(district, liveResult));
        //配置天气预报列表
        ForecastAdapter adapter = new ForecastAdapter(weatherForecast);
        weatherBinding.rvForecast.setLayoutManager(new LinearLayoutManager(requireActivity()));
        weatherBinding.rvForecast.setAdapter(adapter);

        dialog.setContentView(weatherBinding.getRoot());
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        dialog.setOnDismissListener(v -> binding.fabWeather.show());
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        binding.mapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        binding.mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        binding.mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if(location != null) {
            Log.e("TAG", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());

            //创建一个经纬度点，参数一是纬度，参数二是经度
            LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 20, GeocodeSearch.AMAP);
            //通过经纬度获取地址信息
            geocoderSearch.getFromLocationAsyn(query);

        } else {
            Log.e("TAG", "定位失败");
        }
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {

        //解析result获取地址描述信息
        if (rCode == PARSE_SUCCESS_CODE) {
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            //显示解析后的地址
            Log.e("TAG", "地址: " + regeocodeAddress.getFormatAddress());
            district = regeocodeAddress.getDistrict();
            Log.e("TAG", "区: " + district);
            // 搜索天气 实时天气和预报天气
            searchWeather(WeatherSearchQuery.WEATHER_TYPE_LIVE);
            searchWeather(WeatherSearchQuery.WEATHER_TYPE_FORECAST);
        } else {
            showMsg("获取地址失败");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 实时天气返回
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
        liveResult = localWeatherLiveResult.getLiveResult();
        if (liveResult != null) {
            Log.e("TAG", "onWeatherLiveSearched: " + new Gson().toJson(liveResult));

        } else {
            showMsg("实时天气数据为空");
        }
    }

    /**
     * 天气预报返回
     */
    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
        weatherForecast = localWeatherForecastResult.getForecastResult().getWeatherForecast();
        if (weatherForecast != null) {
            Log.e("TAG", "onWeatherForecastSearched: " + new Gson().toJson(weatherForecast));
            binding.fabWeather.show();
        } else {
            showMsg("天气预报数据为空");
        }
    }
}