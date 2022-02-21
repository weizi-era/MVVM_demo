package com.zjw.mvvm_demo.ui.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.SelectPhotoContract;
import com.zjw.mvvm_demo.databinding.ActivityRemindLocationBinding;
import com.zjw.mvvm_demo.utils.BitmapUtils;
import com.zjw.mvvm_demo.utils.PermissionUtils;

import java.util.List;

public class RemindLocationActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener, View.OnClickListener {

    private ActivityRemindLocationBinding binding;

    private MyLocationStyle myLocationStyle;
    private AMap aMap;
    private AMapLocationClientOption mLocationOption;
    private GeocodeSearch geocoderSearch = null;
    private DistrictSearch districtSearch;
    private DistrictSearchQuery districtSearchQuery;

    //解析成功标识码
    private static final int PARSE_SUCCESS_CODE = 1000;

    /**
     * 常规使用 通过意图进行跳转
     */
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    private String district = null;// 区/县

    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remind_location);

        setStatusBar(true);

        back(binding.toolbar);

        //基于个人隐私保护的关系，这里要设置为true，否则会出现地图白屏的情况
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);
        binding.mapView.onCreate(savedInstanceState);

        initMap();

        initSearch();

        binding.ibZoomin.setOnClickListener(this);
        binding.ibZoomout.setOnClickListener(this);
        binding.ibMyLocation.setOnClickListener(this);
        binding.editOk.setOnClickListener(this);

    }


    private void initMap() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = binding.mapView.getMap();
        }

        mLocationOption = new AMapLocationClientOption();
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_remind_location));
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        mLocationOption.setOnceLocationLatest(true);  //启动定位时SDK会返回最近3s内精度最高的一次定位结果
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        aMap.setMyLocationStyle(myLocationStyle); //设置定位蓝点的Style
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16.0f));
        aMap.getUiSettings().setMyLocationButtonEnabled(false); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true); // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.getUiSettings().setZoomControlsEnabled(false);
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnCameraChangeListener(this);
    }

    /**
     * 初始化搜索
     */
    private void initSearch() {
        try {
            //初始化地理编码搜索
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(this);

            //初始化城市行政区搜索
            districtSearch = new DistrictSearch(this);
            districtSearchQuery = new DistrictSearchQuery();
        } catch (AMapException e) {
            e.printStackTrace();
        }
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

            latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
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

            binding.etLocation.setText(regeocodeAddress.getFormatAddress());
            district = regeocodeAddress.getDistrict();
            Log.e("TAG", "区: " + district);

        } else {
            showMsg("获取地址失败");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        //拿到返回的坐标，然后去地图上定位，改变地图中心
        if (rCode == PARSE_SUCCESS_CODE) {
            Log.e("TAG", "onGeocodeSearched: 地址转坐标成功");
            List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
            if (geocodeAddressList != null && geocodeAddressList.size() > 0) {
                LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
                Log.e("TAG", "onGeocodeSearched: 坐标：" + latLonPoint.getLongitude() + "，" + latLonPoint.getLatitude());

                //切换地图中心
                switchMapCenter(geocodeResult, latLonPoint);
            }
        } else {
            showMsg("获取坐标失败");
        }
    }

    /**
     * 切换地图中心
     */
    private void switchMapCenter(GeocodeResult geocodeResult, LatLonPoint latLonPoint) {
        //显示解析后的坐标，
        double latitude = latLonPoint.getLatitude();
        double longitude = latLonPoint.getLongitude();
        //创建经纬度对象
        LatLng latLng = new LatLng(latitude, longitude);
        //改变地图中心点
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 30, 0));
        //在地图上添加marker
        aMap.addMarker(new MarkerOptions().position(latLng).title(geocodeResult.getGeocodeQuery().getLocationName()).snippet("DefaultMarker"));
        //动画移动
        aMap.animateCamera(mCameraUpdate);

        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 20, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        binding.mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        binding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        binding.mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        getGeocodeSearch(cameraPosition.target);
    }

    //逆地理编码获取当前位置信息
    private void getGeocodeSearch(LatLng targe) {
        if (geocoderSearch == null) {
            try {
                geocoderSearch = new GeocodeSearch(this);
            } catch (AMapException e) {
                e.printStackTrace();
            }
        }
        geocoderSearch.setOnGeocodeSearchListener(this);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(targe.latitude, targe.longitude), 1000, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_zoomin:
                scaleMap(true);
                break;
            case R.id.ib_zoomout:
                scaleMap(false);
                break;
            case R.id.ib_myLocation:
                location();
                break;
            case R.id.edit_ok:
                Intent intent = new Intent();
                intent.putExtra("location", binding.etLocation.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    /**
     * 点击定位按钮
     */
    public void location() {
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    /**
     * 放大/缩小 地图
     * @param isLarge true 放大  false 缩小
     */
    public void scaleMap(boolean isLarge) {
        CameraPosition cameraPosition = aMap.getCameraPosition();
        float mapZoom = cameraPosition.zoom;
        LatLng mapTarget = cameraPosition.target;
        if (isLarge) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapTarget, ++mapZoom));
        } else {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapTarget, --mapZoom));
        }

    }
}