package com.motorbike.anqi.activity.map;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.RoomXqBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 路线规划地图选点
 */
public class RouteChooseActivity extends BaseActivity implements SensorEventListener, BaiduMap.OnMapStatusChangeListener, BaiduMap.OnMapLoadedCallback, BaiduMap.OnMyLocationClickListener {
    private MapView mMapView;
    BaiduMap mBaiduMap;
    private ListView poiLv;
    private AutoCompleteTextView searchText;
    private SearchResultAdapter searchResultAdapter;
    // 定位相关
    private LocationService locationService;
    public MyLocationListenner myListener = new MyLocationListenner();

    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private ImageView imageView;

    private MyLocationData locData;
    private List<PoiInfo> resultData;
    //poi检索
    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch;

    //list头布局
    private TextView headTitle;
    private ImageView selectImg;
    private GeoCoder mSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_choose);
        // 地图初始化
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        imageView=findViewById(R.id.marker_img);

        poiLv=findViewById(R.id.serchpoiList);
        type=getIntent().getStringExtra("type");
        if (TextUtils.isEmpty(type)){
            type="0";
        }
        View view= LayoutInflater.from(this).inflate(R.layout.item_head_layout,null);
        headTitle=view.findViewById(R.id.head_text_title);
        selectImg=view.findViewById(R.id.head_image_check);
        findViewById(R.id.queding_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(newAddress)){
                    Intent i = new Intent();
                    i.putExtra("address", newAddress);
                    i.putExtra("lat", codelatLng.latitude + "");
                    i.putExtra("lon", codelatLng.longitude + "");
                    i.putExtra("city", city);
                    i.putExtra("type", type);
                    setResult(RESULT_OK, i);
                    finish();
                }else {
                    prompt("请选取地址");
                }
            }
        });
        poiLv.addHeaderView(view);
        searchResultAdapter = new SearchResultAdapter(this);
        poiLv.setAdapter(searchResultAdapter);
        poiLv.setOnItemClickListener(onItemClickListener);


        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        locationService = MyApplication.getInstance().locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(myListener);

//        int type = getIntent().getIntExtra("from", 0);
//        if (type == 0) {
//            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        }
        locationService.start();// 定位SDK

        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.setOnMyLocationClickListener(this);


        //poi检索
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        //poi热词检索
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);

        searchText = findViewById(R.id.keyWord);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.length() > 0) {
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(newText)
                            .city(city));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //逆地理位置编码
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(geoCoderlistener);

    }

    private Marker mMarkerA;
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        double x = event.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0){
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        imageView.setVisibility(View.VISIBLE);
        mBaiduMap.clear();
        mMarkerA=null;
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i)
    {

        switch (i) {
            case BaiduMap.OnMapStatusChangeListener.REASON_API_ANIMATION:
                isArtificial=false;
                break;
            case BaiduMap.OnMapStatusChangeListener.REASON_DEVELOPER_ANIMATION:
                isArtificial=false;
                break;
            case BaiduMap.OnMapStatusChangeListener.REASON_GESTURE:
                isArtificial=true;
                break;
        }
    }

    private boolean isArtificial=true;
    @Override
    public void onMapStatusChange(MapStatus mapStatus){

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus)
    {
        LatLng target = mapStatus.target;
        if (isArtificial){
            imageView.setVisibility(View.GONE);
            Log.e("c中心点", target.latitude + "===jingdu" + target.longitude + "==weidu");
            MarkerOptions ooA = new MarkerOptions().position(target).icon(bdA)
                    .zIndex(9).draggable(true);
            ooA.animateType(MarkerOptions.MarkerAnimateType.jump);

            mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
            geoCOde(target);
            poi(target);
        }
    }
    private LatLng codelatLng;//逆地理的经纬度，
    private void geoCOde(LatLng latLng)
    {
        codelatLng=latLng;
        ReverseGeoCodeOption geoCodeOption =new ReverseGeoCodeOption();
        //返回新数据
        geoCodeOption.newVersion(1);
        geoCodeOption.location(latLng);
        mSearch.reverseGeoCode(geoCodeOption);
    }

    private void poi(LatLng target)
    {
        try {;
            mPoiSearch.searchNearby(new PoiNearbySearchOption()
                    .keyword("道路")
                    .sortType(PoiSortType.distance_from_near_to_far)
                    .location(target)
                    .radius(500)
                    .pageCapacity(50)
                    .pageNum(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.purple_pin_two);

    @Override
    public void onMapLoaded()
    {
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onMyLocationClick(){
        locationService.start();// 定位SDK
        return false;
    }
    /**
     * 更新列表中的item
     * @param poiItems
     */
    private void updateListview(List<PoiInfo> poiItems)
    {
        searchResultAdapter.setSelectedPosition(-1);
        selectImg.setVisibility(View.VISIBLE);
        searchResultAdapter.setData(resultData);
        searchResultAdapter.notifyDataSetChanged();
    }

    OnGetGeoCoderResultListener geoCoderlistener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
            }
            //获取地理编码结果
        }
        @Override

        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                return;
            }
            String address=result.getAddress();
            headTitle.setText(address);
            newAddress=address;
            searchResultAdapter.notifyDataSetChanged();
            //获取反向地理编码结果
        }
    };
    private String newAddress,type,city="上海";
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position-1 != searchResultAdapter.getSelectedPosition()) {
                if (position<1){
                    selectImg.setVisibility(View.VISIBLE);
                    searchResultAdapter.setSelectedPosition(position-1);
                    if (codelatLng!=null) {
                        mapStatus(codelatLng);
                    }
                }else {
                    PoiInfo poiItem = (PoiInfo) searchResultAdapter.getItem(position-1);
                    LatLng curLatlng = poiItem.location;
                    mapStatus(curLatlng);
                    selectImg.setVisibility(View.GONE);
                    searchResultAdapter.setSelectedPosition(position-1);
                    newAddress=poiItem.address+poiItem.name;
                    codelatLng=poiItem.location;
                    city=poiItem.city;
                }
                searchResultAdapter.notifyDataSetChanged();
            }
        }
    };

    private void mapStatus(LatLng curLatlng)
    {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(curLatlng).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * Sug检索监听函数
     */
    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {

            if (res == null || res.getAllSuggestions() == null) {
                return;
                //未找到相关结果
            }
            final List<SuggestionResult.SuggestionInfo> allSuggestions = res.getAllSuggestions();
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < allSuggestions.size(); i++) {
                listString.add(allSuggestions.get(i).key);
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
            searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("MY", "setOnItemClickListener");
                    if (allSuggestions != null && allSuggestions.size() > position)
                    {
                        SuggestionResult.SuggestionInfo suggestionInfo=allSuggestions.get(position);
                        poi(suggestionInfo.pt);
                        mapStatus(suggestionInfo.pt);
                        geoCOde(suggestionInfo.pt);
                    }
                    //隐藏输入法
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(),0);
                }
            });
            //获取在线建议检索结果
        }
    };
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            chiliLocation(location);
        }

    }

    private void chiliLocation(BDLocation location)
    {
        try {
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            String guoji=location.getCountry();

            String aaa=location.getAddrStr().substring(guoji.length());
            Log.e("aaaa",aaa);
            headTitle.setText(aaa);
            newAddress=aaa;
            city=location.getCity();
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            mapStatus(ll);
            codelatLng=ll;
            poi(ll);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检索监听函数
     */
    private OnGetPoiSearchResultListener poiListener =new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (resultData!=null)
            {
                resultData.clear();
            }
            resultData=poiResult.getAllPoi();
            updateListview(resultData);
            Log.e("poi", poiResult.getTotalPoiNum()+"个数");

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };
    @Override
    protected void onPause(){
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onStop()
    {
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        // 关闭定位图层
        mPoiSearch.destroy();
        //取消注册传感器监听
        locationService.unregisterListener(myListener); //注销掉监听
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mSearch.destroy();
        mMapView = null;
        super.onDestroy();
    }


}
