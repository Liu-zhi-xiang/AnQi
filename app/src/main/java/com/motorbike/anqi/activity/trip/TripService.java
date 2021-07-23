package com.motorbike.anqi.activity.trip;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.Toast;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.motorbike.anqi.activity.map.LocationService;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.init.MyApplication;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TripService extends Service implements SensorEventListener {
    //定位
    private LocationClient client = null;
    private LocationClientOption mOption;
    public MyLocationListener myListener = new MyLocationListener();
    private double allDistence;
    private float rapidly,speed,meanSpead;
    private String speedStr="0",meanSpeedStr="0",rapidlyStr="0",distenceStr="0",cntStr="0",city,addressStr,bend="0",district="";
    //计时器
    private int cnt = 0;
    private Timer timer = new Timer(true);
    private TimerTask timerTask ;
    //陀螺仪
    private Sensor gyroscopeSensor;
    private SensorManager sensorManager;
    private MyOrientationDetector myOrientationDetector;
    // 将纳秒转化为秒
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private float angle[] = new float[3];
    private boolean isRecord=false;
    private int oneAngle[] =new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    private int twoAngle[] =new int[]{16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
    private int threeAngle[]=new int[]{31,32,33,34,35,36,37,38,39,40,41,42,43,44,45};

    private double lat,lng;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        allDistence=0;
        speed=0;
        meanSpead=0;
        rapidly=0;
        addressStr="";
        city="";
        bend="0";
        rapidlyStr="0";
        speedStr="0";
        meanSpeedStr="0";
        distenceStr="0";
        cntStr="0";
        initLocation(intent);
        initSensor();
        initSersor();
        final DecimalFormat decimalFormat=new DecimalFormat("0.00");
        timerTask = new TimerTask() {
            @Override
            public void run() {
                cnt++;
                //里程
                allDistence= (speed / 3.6) * cnt;
                if (allDistence<0){
                    allDistence=-allDistence;
                }
                distenceStr=decimalFormat.format((allDistence / 1000));
                //均速
                if (allDistence>0&&cnt>0){
                    meanSpead= (float) (allDistence/cnt);
                    meanSpeedStr=decimalFormat.format((meanSpead * 3.6));
                }else {
                    meanSpeedStr="0.00";
                }
                Bundle bundle=new Bundle();
                bundle.putInt("time",cnt);
                bundle.putString("rapidly",rapidlyStr);//急速
                bundle.putString("speedStr",speedStr);
                bundle.putString("meanSpeedStr",meanSpeedStr);
                bundle.putString("distenceStr",distenceStr);
                bundle.putString("cntStr",cntStr);
                bundle.putString("city",city);
                bundle.putString("addressStr",addressStr);
                bundle.putString("bend",bend);
                bundle.putDouble("lat",lat);
                bundle.putDouble("lng",lng);
                bundle.putString("district",district);
                BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(TripSpeedActivity.class, HttpTagUtil.TripSpeed, bundle);
                Log.e("ccc", cnt + "cccccc");
            }
        };
        timer.schedule(timerTask, 0, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //初始化定位
    private void initLocation(Intent intent) {
        client = new LocationClient(getApplicationContext());
        //声明LocationClient类
        client.registerLocationListener(myListener);
        mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(2000);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
        mOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
        mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        mOption.setTimeOut(10 * 1000);
        client.setLocOption(mOption);
        client.start();
    }

    /**
     * 获取屏幕的旋转角度
     */
    private void initSersor(){
        myOrientationDetector=new MyOrientationDetector(getApplicationContext());
        myOrientationDetector.enable();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (timestamp!=0){

            final float dT = (event.timestamp - timestamp) * NS2S;
            // 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
            angle[0] += event.values[0] * dT;
            angle[1] += event.values[1] * dT;
            angle[2] += event.values[2] * dT;
            // 将弧度转化为角度
            float anglex = (float) Math.toDegrees(angle[0]);//方位角
            float angley = (float) Math.toDegrees(angle[1]);//俯仰角
            float anglez = (float) Math.toDegrees(angle[2]);//横滚角
            Log.e("vvv",anglex+  "    anglex");
            Log.e("vvv",angley+  "    angley");
            Log.e("vvv",anglez+  "    anglez");
            float jiaodu=Math.abs(angley);
            if (jiaodu>90){
                jiaodu=jiaodu-90;
            }
//            bend= String.valueOf(Math.floor(jiaodu));
        }
        timestamp = event.timestamp;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null && location.getLocType() == BDLocation.TypeServerError) {
                return;
            }
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            lat=location.getLatitude();
            lng=location.getLongitude();
            city=location.getCity();
            district=location.getDistrict();
            addressStr=location.getAddrStr();
            Log.e("aaa",location.getGpsAccuracyStatus()+"  GpsAccuracyStatus");
            Log.e("aaa",addressStr+"  addressStr");
            Log.e("aaa",location.getLocType()+"  type");
            Log.e("aaa",location.getNetworkLocationType()+"  NetworkLocationType");
            Log.e("aaa", location.getLatitude() + "      latitude");
            Log.e("aaa", location.getLongitude() + "      longitude");
            DecimalFormat decimalFormat=new DecimalFormat("0.00");
            //速度
            speed = location.getSpeed();
            speedStr=decimalFormat.format(speed);
            if (speed>=20){
                isRecord=true;
            }
            //极速
            if (speed > rapidly) {
                rapidly = speed;
                rapidlyStr=decimalFormat.format(rapidly);
            }
            Log.e("aaa", speed + "      speed");
//            Toast.makeText(getApplicationContext(), distenceStr+ "  distence", Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), speedStr + "  speed", Toast.LENGTH_LONG).show();

            //百公里加速
            if (cnt<60){
                if (speed>=100){
                    cntStr=cnt+"";
                }else {
                    cntStr="0";
                }
            }else {
                cntStr="0";
            }

            if (isRecord==true){
                angleList.add(jiaodu);
                if (angleList!=null&&angleList.size()>0){
                    int sumAngle=0;
                    for (int x=0;x<angleList.size();x++){
                        sumAngle+=angleList.get(x);
                    }
                    if (sumAngle>0){
                        int  averageAngle= sumAngle/angleList.size();
                        int  maxAngle=Collections.max(angleList);
                        jiaodu=averageAngle-maxAngle;
                        bend=String.valueOf(Math.abs(jiaodu));
                    }
                }
            }else {
                bend="0";
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!timerTask.cancel()) {
            timerTask.cancel();
            timer.cancel();
            cnt=0;
        }
        if (client!=null){
            client.stop();
        }
        myOrientationDetector.disable();
    }

    private void stopTime(){
        if (!timerTask.cancel()) {
            timerTask.cancel();
            timer.cancel();
            cnt=0;
        }
    }
    private void initSensor(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
    }
    private int jiaodu;
    private List<Integer> angleList=new ArrayList() ;
    public class MyOrientationDetector extends OrientationEventListener {
        public MyOrientationDetector(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            Log.e("uuu",orientation+"  度");
            if (orientation>=90 && orientation < 180){
                jiaodu=orientation-90;
            }else if (orientation >=180 && orientation < 270){
                jiaodu=orientation-180;
            }else if (orientation >=270 && orientation<360){
                jiaodu=orientation-270;
            }else if (orientation >=0 && orientation < 90){
                jiaodu=orientation;
            }


        }
    }


}
