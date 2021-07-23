package com.motorbike.anqi.activity.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.google.gson.Gson;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.bean.RoomXqBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.util.StatusBarUtil;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * 语音室地图详情
 */
public  class VoiceMapRoomActivity extends BaseActivity implements View.OnClickListener, BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {
    private TextureMapView mMapView;
    private TextView titleTv,roomNameTv,fangzhuNameTv,renshuTv,cfdTv,mddTv,jjdTv,
    kssjTv,lcTv,jsbzTv;
    private ImageView fznagzHaedImg,backImg;
    private BaiduMap mBaidumap = null;
    BikingRouteResult nowResultbike = null;
    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    int nowSearchType = -1; // 当前进行的检索，供判断浏览节点时结果使用。
    int nodeIndex = -1; // 节点索引,供浏览节点时使用

    private
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    private TextView popupText = null; // 泡泡view
    NestedScrollView nestedScrollView;
    private String roomNo,room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_map_room);
        initView();
        roomNo=getIntent().getStringExtra("roomNo");
        roomxingqing(roomNo);
    }

    @Override
    protected void initSystemBarTint()
    {
        StatusBarUtil.transparencyBar(this);
    }

    private void initView()
    {
        findViewById(R.id.back_layout).setOnClickListener(this);
        titleTv=findViewById(R.id.title_text);
        roomNameTv=findViewById(R.id.room_name_tv);
        renshuTv=findViewById(R.id.renshu_tv);
        jsbzTv=findViewById(R.id.jsbz_tv);
        lcTv=findViewById(R.id.gl_tv);
        kssjTv=findViewById(R.id.kssj_tv);
        jjdTv=findViewById(R.id.jjd_tv);
        mddTv=findViewById(R.id.mdd_tv);
        cfdTv=findViewById(R.id.cfd_tv);
        fznagzHaedImg=findViewById(R.id.headImage);
        backImg=findViewById(R.id.back_img);
        fangzhuNameTv=findViewById(R.id.tvNickname);
        // 初始化地图
        mMapView = findViewById(R.id.map_view);
        mBaidumap = mMapView.getMap();
        // 地图点击事件处理
        nestedScrollView=findViewById(R.id.mipam_ingg);
        mBaidumap.setOnMapClickListener(this);
        mMapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    nestedScrollView.requestDisallowInterceptTouchEvent(false);
                }else{
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    nestedScrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

    }

    /**
     * 发起路线规划搜索示例
     * @param
     */
    public void searchButtonProcess(LatLng stat,LatLng end)
    {
        mBaidumap.clear();
        // 处理搜索按钮响应
        // 设置起终点信息，对于tranist search 来说，城市名无意义
//        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", startNodeStr);
        PlanNode stNode = PlanNode.withLocation(stat);
//        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", endNodeStr);
        PlanNode enNode = PlanNode.withLocation(end);

        // 实际使用中请对起点终点城市进行正确的设定
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .ridingType(1)
                    .from(stNode).to(enNode));
            nowSearchType = 4;
//        nodeClick(null);
    }


    /**
     * 节点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v)
    {
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = null;

        if (nowSearchType != 0 && nowSearchType != -1) {
            // 非跨城综合交通
            if (route == null || route.getAllStep() == null) {
                return;
            }

            // 设置节点索引
//            if (v.getId() == R.id.next) {
//                if (nodeIndex < route.getAllStep().size() - 1) {
//                    nodeIndex++;
//                } else {
//                    return;
//                }
//            } else if (v.getId() == R.id.pre) {
//                if (nodeIndex > 0) {
//                    nodeIndex--;
//                } else {
//                    return;
//                }
//            }
            // 获取节结果信息
            step = route.getAllStep().get(nodeIndex);
             if (step instanceof BikingRouteLine.BikingStep) {
                nodeLocation = ((BikingRouteLine.BikingStep) step).getEntrance().getLocation();
                nodeTitle = ((BikingRouteLine.BikingStep) step).getInstructions();
            }
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }

        // 移动节点至中心
        mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        popupText = new TextView(VoiceMapRoomActivity.this);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng)
    {

    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi)
    {
        return false;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result)
    {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            prompt("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            AlertDialog.Builder builder = new AlertDialog.Builder(VoiceMapRoomActivity.this);
            builder.setTitle("提示");
            builder.setMessage("检索地址有歧义，请重新设置。\n可通过getSuggestAddrInfo()接口获得建议查询信息");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            if (result.getRouteLines().size() > 0) {

                nowResultbike = result;
                route = result.getRouteLines().get(0);
                BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                int aaa=route.getDistance()/1000;
                lcTv.setText("全程"+aaa+"公里");
            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }



    }
    boolean useDefaultIcon = false;
    private void roomxingqing(String roomNo)
    {
        showLoading();
        Map<String,Object> map=new ArrayMap<>();
        map.put("roomNo",roomNo);
        okHttp(this, BaseRequesUrl.xiangqingRoom, HttpTagUtil.XIANGQROOM,map);
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        dismissLoading();
        if (!isHttp){
            prompt(mag);
          return;
        }
        switch (mTag){
            case HttpTagUtil.XIANGQROOM:
                if (data!=null){
                    RoomXqBean roomBean = new Gson().fromJson(data, RoomXqBean.class);
                    if (roomBean!=null){
                        try {
                            LatLng stat=new LatLng(Double.valueOf(roomBean.getLatitudeStart()),Double.valueOf(roomBean.getLongitudeStart()));
                            LatLng end=new LatLng(Double.valueOf(roomBean.getLatitudeDestination()),Double.valueOf(roomBean.getLongitudeDestination()));
                            searchButtonProcess(stat,end);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        roomNameTv.setText(roomBean.getTheme());
                        fangzhuNameTv.setText(roomBean.getNickname());
                        cfdTv.setText(roomBean.getStartAddress());
                        mddTv.setText(roomBean.getEndAddress());
                        renshuTv.setText(roomBean.getOnLineCount()+"/"+roomBean.getPersonTotal());
                        jjdTv.setText(roomBean.getSetAddress());
                        kssjTv.setText(roomBean.getSetTime());
                        jsbzTv.setText(roomBean.getRemark());
                        Picasso.with(VoiceMapRoomActivity.this)
                                .load(roomBean.getBackUrl())
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(backImg);
                        Picasso.with(VoiceMapRoomActivity.this)
                                .load(data)
                                .config(Bitmap.Config.RGB_565)
                                .centerCrop()
                                .fit()
                                .networkPolicy(NetworkPolicy.NO_STORE)
                                .into(fznagzHaedImg);
                    }
                }

                break;
        }
    }

    private class MyBikingRouteOverlay extends BikingRouteOverlay {

        public MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
//            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
//            }
            return null;
        }


    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();
        super.onDestroy();
    }
}
