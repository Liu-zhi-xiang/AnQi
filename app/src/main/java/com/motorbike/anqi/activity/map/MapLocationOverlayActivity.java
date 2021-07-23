package com.motorbike.anqi.activity.map;

import android.graphics.Color;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.motorbike.anqi.R;
import com.motorbike.anqi.bean.MemberListBean;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;


import java.util.ArrayList;
import java.util.List;

public class MapLocationOverlayActivity extends BaseActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private InfoWindow mInfoWindow;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdGround = BitmapDescriptorFactory
            .fromResource(R.mipmap.dingwei_ord);
    private List<Marker> markers;
    private TextView title;
    private ArrayList<MemberListBean> memberListBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location_overlay);
        initView();
    }

    private void initView()
    {
        memberListBeans=getIntent().getParcelableArrayListExtra("list");
        markers=new ArrayList<>();
        mMapView=findViewById(R.id.lovation_overlay_map);
        title=findViewById(R.id.tvTitle);
        title.setText("车友踪迹");
        findViewById(R.id.llBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
               int x= marker.getZIndex();
                TextView button = new TextView(getApplicationContext());
                button.setGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
                button.setBackgroundResource(R.drawable.popup);
               if (markers.size()>x)
               {
                   if (memberListBeans!=null&&memberListBeans.size()>x) {
                       button.setText(memberListBeans.get(x).getNickname()+":"+memberListBeans.get(x).getArea());
                       button.setTextColor(Color.BLACK);
                   }else {
                       button.setText("位置");
                       button.setTextColor(Color.BLACK);
                   }
               }
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, null);
                mBaiduMap.showInfoWindow(mInfoWindow);
                return false;
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        initOverlay();
    }

    @Override
    protected void onPause()
    {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }
    public void initOverlay()
    {
        // add marker overlay
        if (memberListBeans!=null){
            for (int x=0;x<memberListBeans.size();x++) {
                MemberListBean memberListBean = memberListBeans.get(x);
                if (memberListBean != null) {
                    Log.e("aaaa","memberListBean.getLatitude()=="+memberListBean.getLatitude().intern());
                    Double lat = Double.valueOf(memberListBean.getLatitude().trim());
                    Double lon = Double.valueOf(memberListBean.getLongitude().trim());
                    LatLng llA = new LatLng(lat, lon);
                    MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdGround)
                            .title("地址")
                            .draggable(false)
                            .zIndex(0);
                    Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
                    markers.add(mMarkerA);

                    if (memberListBean.getFriendId().equals(BaseRequesUrl.uesrId)) {
                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.target(llA).zoom(14.0f);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    }
                }
            }
        }

    }

    /**
     * 清除所有Overlay
     *
     * @param view
     */
    public void clearOverlay(View view)
    {
        mBaiduMap.clear();
        markers.clear();
    }

    /**
     * 重新添加Overlay
     *
     * @param view
     */
    public void resetOverlay(View view) {
        clearOverlay(null);
        initOverlay();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        clearOverlay(null);
        super.onDestroy();
        // 回收 bitmap 资源
        markers.clear();
    }

}
