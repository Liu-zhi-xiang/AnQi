package com.motorbike.anqi.activity.trip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Message;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.my.ManageCarActivity;
import com.motorbike.anqi.activity.my.TopListActivity;
import com.motorbike.anqi.activity.voice.RacingvoiceHomeActivity;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.adapter.BrandAdapter;
import com.motorbike.anqi.adapter.ModelAdapter;
import com.motorbike.anqi.bean.CheckCarBean;
import com.motorbike.anqi.bean.MsgListBean;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.handler.BaseHandlerUpDate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.init.MyApplication;
import com.motorbike.anqi.main.che.AddMotorcycleTypeActivity;
import com.motorbike.anqi.util.PixelFormat;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CustomDialogView;

import java.lang.reflect.TypeVariable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 行程
 */
public class TripSpeedActivity extends BaseActivity implements View.OnClickListener, BaseHandlerUpDate {

    private TextView tvTime, tvJiaoDu, tvKm, tvRapidly, tvMeanSpeed, tvKmAcceleration,tvSpeed,tvTrip;
    private ImageView ivMark;
    //地图模块
    private boolean isStart = true;                 //是否点击

    private Map<String,Object> tripMap,checkMap,locationMap;
    private String city,startAdd,endAdd,rapidly,speedStr,meanSpeedStr,distenceStr,cntStr,rideTime,bend,district;
    private String roomNum,startTime;
    private String cityTwo,startAddTwo,endAddTwo,rapidlyTwo="0",speedStrTwo="0",
            meanSpeedStrTwo="0",distenceStrTwo="0",cntStrTwo="0",rideTimeTwo="0",bendTwo="0";
    private static final int BAIDU_READ_PHONE_STATE =100;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };

    private UserPreference preference;
    private String runing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_speed);
        preference=UserPreference.getUserPreference(this);
        BaseHandlerOperate.getBaseHandlerOperate().addKeyHandler(TripSpeedActivity.class,this);
        roomNum=getIntent().getStringExtra("roomNum");
        runing=getIntent().getStringExtra("runing");

        initView();
        checkPermissions(needPermissions);
        Log.e("mmm", runing + "        runing");
    }

    private void initView() {
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.zhuce_ll).setOnClickListener(this);
        findViewById(R.id.add_layout).setOnClickListener(this);
        findViewById(R.id.ivTop).setOnClickListener(this);
        tvTrip=findViewById(R.id.tvTrip);
        tvSpeed=findViewById(R.id.tvSpeed);
        tvTime = findViewById(R.id.tvTime);
        tvKm = findViewById(R.id.tvKm);
        tvJiaoDu = findViewById(R.id.tvJiaoDu);
        tvRapidly = findViewById(R.id.tvRapidly);
        tvMeanSpeed = findViewById(R.id.tvMeanSpeed);
        tvKmAcceleration = findViewById(R.id.tvKmAcceleration);
        ivMark=findViewById(R.id.ivMark);

        ivMark.setImageResource(R.drawable.am_0);
        tvSpeed.setText("0.00");
        if (TextUtils.isEmpty(BaseRequesUrl.newRuning)){
            BaseRequesUrl.newRuning=runing;
            Log.e("mmm", BaseRequesUrl.newRuning + "        BaseRequesUrl.newRuning");
        }
        if (!TextUtils.isEmpty(BaseRequesUrl.newRuning)){
           if (!BaseRequesUrl.newRuning.equals(runing)){
               Log.e("mmm",  "       1111111111");

               tvSpeed.setText("0.00");
               tvJiaoDu.setText("0"+"度");
               tvKmAcceleration.setText("0.00");
               tvKm.setText("0.00");
               tvRapidly.setText("0.00");
               tvMeanSpeed.setText("0.00");
               tvTime.setText("00:00:00");
           }
        }
    }

    /**
     * 上传行程接口
     * @param userId
     * @param ridingTime
     * @param ridingKm
     * @param avgSpeed
     * @param accelerate100km
     * @param bend
     * @param extreme
     * @param roomNo
     * @param city
     * @param startAddr
     * @param endAddr
     * @param startTime
     * @param carType
     */
    private void http(String userId,String ridingTime,String ridingKm,String avgSpeed,String accelerate100km
                        ,String bend,String extreme,String roomNo,String city,String startAddr,String endAddr,String startTime,String carType)
    {
        showLoading();
        tripMap=new HashMap<>();
        if (tripMap!=null){
            tripMap.clear();
        }
        tripMap.put("userId",userId);
        tripMap.put("ridingTime",ridingTime);
        tripMap.put("ridingKm",ridingKm);
        tripMap.put("avgSpeed",avgSpeed);
        tripMap.put("accelerate100km",accelerate100km);
        tripMap.put("bend",bend);
        tripMap.put("startTime",startTime);
        tripMap.put("carType",carType);
        tripMap.put("extreme",extreme);
        tripMap.put("roomNo",roomNo);//团队行程传(个程置空）
        tripMap.put("city",city);//个人行程传(团队置空）
        tripMap.put("startAddr",startAddr);//个人行程时传(团队置空）
        tripMap.put("endAddr",endAddr);//个人行程时传(团队置空）
        okHttp(this, BaseRequesUrl.TripRecord,HttpTagUtil.TripRecord,tripMap);
    }

    /**
     * check开启行程前是否选择车型
     * @param roomNo
     * @param userId
     */
    private void checkCarSelect(String roomNo,String userId)
    {
        showLoading();
        checkMap=new HashMap<>();
        if (checkMap!=null){
            checkMap.clear();
        }
        checkMap.put("roomNo",roomNo);
        checkMap.put("userId",userId);
        okHttp(this,BaseRequesUrl.CheckCarSelect,HttpTagUtil.CheckCarSelect,checkMap);
    }

    /**
     * 实时上传房间成员位置信息接口
     * @param latitude
     * @param longitude
     * @param showArea
     */
    private void loactionHttp(String latitude,String longitude,String showArea)
    {
       locationMap=new HashMap<>();
       if (locationMap!=null){
           locationMap.clear();
       }
       locationMap.put("userId",preference.getUserId());
       locationMap.put("longitude",longitude);
       locationMap.put("latitude",latitude);
       locationMap.put("showArea",showArea);
        Log.e("www",latitude+"     lat");
        Log.e("www",longitude+"     lng");
        Log.e("www",showArea+"     address");
        okHttp(this,BaseRequesUrl.friendsposition,HttpTagUtil.friendsposition,locationMap);
    }
    /**
     * 如果当前用户正在行程中，接受邀请发起行程时，强制结束
     */
    private void isRestart(){
        if (!TextUtils.isEmpty(roomNum)){
            if (roomNum.equals("0")){
                if (!rideTimeTwo.equals("0")&&!distenceStrTwo.equals("0")&&!meanSpeedStrTwo.equals("0")&&!cntStrTwo.equals("0")
                        &&!bendTwo.equals("0")&&!rapidlyTwo.equals("0")&&!TextUtils.isEmpty(cityTwo)&&!TextUtils.isEmpty(startAddTwo)&&!TextUtils.isEmpty(endAddTwo)&&!TextUtils.isEmpty(startTime)){
                    if (!TextUtils.isEmpty(preference.getCarType())){
                        http(BaseRequesUrl.uesrId,rideTimeTwo,distenceStrTwo,meanSpeedStrTwo,cntStrTwo,bendTwo,rapidlyTwo,"",cityTwo,startAddTwo,endAddTwo,startTime,preference.getCarType());
                    }else {
                        Log.e("111111111111","1111111111111111");
                        showTip("您还未添加车型,请返回首页添加车型!");
                        startActivity(new Intent(this, MainActivityTwo.class));
                    }
                }
            }else {
                if (!rideTimeTwo.equals("0")&&!distenceStrTwo.equals("0")&&!meanSpeedStrTwo.equals("0")&&!cntStrTwo.equals("0")
                        &&!bendTwo.equals("0")&&!rapidlyTwo.equals("0")&&!TextUtils.isEmpty(startTime)){
                    if (!TextUtils.isEmpty(preference.getCarType())){
                        http(BaseRequesUrl.uesrId,rideTimeTwo,distenceStrTwo,meanSpeedStrTwo,cntStrTwo,bendTwo,rapidlyTwo,roomNum,"","","",startTime,preference.getCarType());
                    }else {
                        Log.e("111111111111","222222222222222");
                        showTip("您还未添加车型,请返回首页添加车型!");
                        startActivity(new Intent(this, MainActivityTwo.class));
                    }
                }
            }
        }
    }
    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag){
            case HttpTagUtil.TripRecord:
                dismissLoading();
                showTip(mag);
                if (data!=null){
                }
                break;
            case HttpTagUtil.CheckCarSelect:
                dismissLoading();
                if (data!=null){
                    List<CheckCarBean> carBeanList=new Gson().fromJson(data,new TypeToken<List<CheckCarBean>>(){}.getType());
                    if (carBeanList!=null&&carBeanList.size()>0){

                        for (int i=0;i<carBeanList.size();i++){
                            String name=carBeanList.get(i).getNickname();
                            Log.e("bbbbbb",name+ "     carName");
                            String type=carBeanList.get(i).getType();// 1  绑定过+ 选过   2 绑定过+ 设过默认值   3  绑定过+ 选过 + 设过默认值   4 未绑定  5 其它情况
                            if (!TextUtils.isEmpty(type)){
                                if (type.equals("4")){
                                    showTip("未绑定车型!");
                                    startActivity(new Intent(this,AddMotorcycleTypeActivity.class));
                                }else if (type.equals("5")){
                                    showTip("您还未添加车型,请返回首页添加车型!");
                                    startActivity(new Intent(this, MainActivityTwo.class));
                                }else {
                                    showAlertStartDialog("提示信息", "行程开始时，是否使用"+preference.getCarType()+"开始行程?");

                                }
                            }
                        }

                    }
                }
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.zhuce_ll:
                if (isStart) {
                    isRestart();
                    Intent stopIntent = new Intent(this, TripService.class);
                    stopService(stopIntent);
                    if (!TextUtils.isEmpty(roomNum)){
                        if (roomNum.equals("0")){
                            checkCarSelect("",preference.getUserId());
                        }else {
                            checkCarSelect(roomNum,"");
                        }
                    }

                } else {
                    showAlertStopDialog("提示信息", "确定结束行程?");
                }
                break;
            case R.id.add_layout:
                startActivity(new Intent(this, TopListActivity.class));
                break;
            case R.id.ivTop://top榜单
                startActivity(new Intent(this, TripTOPListActivity.class));
                break;
        }
    }

    /**
     * 自定义dialog提示框
     *  开始行程
     * @param
     */
    public void showAlertStartDialog(String title, String masg) {
        final CustomDialogView dialog = new CustomDialogView(this);
        dialog.setTitle(title);
        dialog.setMessage(masg);
        dialog.setYesOnclickListener("确定", new CustomDialogView.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                boolean ok=isOPen();
                if (ok){
                    isStart = false;
                    Intent intent = new Intent(TripSpeedActivity.this, TripService.class);
                    startService(intent);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate =  new Date(System.currentTimeMillis());
                    startTime=simpleDateFormat.format(curDate);
                    Log.e("startTime",startTime+"    startTime");
                    tvTrip.setText("END");
                    dialog.dismiss();
                }else {
                    showTip("系统检测到未开启GPS定位服务");
                    Intent intent=new Intent();
                    intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
        });
        dialog.setNoOnclickListener("切换", new CustomDialogView.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
                Intent intent=new Intent(TripSpeedActivity.this, ManageCarActivity.class);
                startActivityForResult(intent,510);
            }
        });
        dialog.show();
    }

    /**
     * 自定义dialog提示框
     *  结束行程
     * @param
     */
    public void showAlertStopDialog(String title, String masg) {
        final CustomDialogView dialog = new CustomDialogView(this);
        dialog.setTitle(title);
        dialog.setMessage(masg);
        dialog.setYesOnclickListener("确定", new CustomDialogView.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                boolean ok=isOPen();
                if (ok){
                    ivMark.setImageResource(R.drawable.am_0);
                    tvSpeed.setText("0.00");
                    Intent intent = new Intent(TripSpeedActivity.this, TripService.class);
                    stopService(intent);
                    tvTrip.setText("GO");
                    isStart = true;
                    if (!TextUtils.isEmpty(roomNum)){
                        if (roomNum.equals("0")){
                            if (!TextUtils.isEmpty(rideTime)&&!TextUtils.isEmpty(distenceStr)&&!TextUtils.isEmpty(meanSpeedStr)&&!TextUtils.isEmpty(cntStr)
                                    &&!TextUtils.isEmpty(bend)&&!TextUtils.isEmpty(rapidly)&&!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(startAdd)&&!TextUtils.isEmpty(endAdd)&&!TextUtils.isEmpty(startTime)){
                                if (!TextUtils.isEmpty(preference.getCarType())){
                                    http(BaseRequesUrl.uesrId,rideTime,distenceStr,meanSpeedStr,cntStr,bend,rapidly,"",city,startAdd,endAdd,startTime,preference.getCarType());
                                }else {
                                    Log.e("111111111111","333333333333333333");
                                    showTip("您还未添加车型，请添加并选择车型！");
                                    startActivity(new Intent(TripSpeedActivity.this, MainActivityTwo.class));
                                }
                            }
                        }else {
                            if (!TextUtils.isEmpty(rideTime)&&!TextUtils.isEmpty(distenceStr)&&!TextUtils.isEmpty(meanSpeedStr)&&!TextUtils.isEmpty(cntStr)
                                    &&!TextUtils.isEmpty(bend)&&!TextUtils.isEmpty(rapidly)&&!TextUtils.isEmpty(startTime)){
                                if (!TextUtils.isEmpty(preference.getCarType())){
                                    http(BaseRequesUrl.uesrId,rideTime,distenceStr,meanSpeedStr,cntStr,bend,rapidly,roomNum,"","","",startTime,preference.getCarType());
                                }else {
                                    Log.e("111111111111","44444444444444444");
                                    showTip("您还未添加车型，请添加并选择车型！");
                                    startActivity(new Intent(TripSpeedActivity.this, MainActivityTwo.class));
                                }
                            }
                        }
                    }
                    dialog.dismiss();
                }else {
                    showTip("系统检测到未开启GPS定位服务");
                    Intent intent=new Intent();
                    intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
        });
        dialog.setNoOnclickListener("取消", new CustomDialogView.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 510:
                    if (data!=null){
                        String carBand = data.getExtras().getString("carBand");
                        String carModels = data.getExtras().getString("carModels");
                        if (!TextUtils.isEmpty(carBand)&&!TextUtils.isEmpty(carModels)){
                            preference.setCarType(carBand+carModels);
                            preference.save();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case HttpTagUtil.TripSpeed:
                Log.e("ccc","         handle");
                tvTrip.setText("END");
                Bundle bundle=msg.getData();
                int cnt=bundle.getInt("time");
                rapidly=bundle.getString("rapidly");
                speedStr=bundle.getString("speedStr");
                meanSpeedStr=bundle.getString("meanSpeedStr");
                distenceStr=bundle.getString("distenceStr");
                cntStr=bundle.getString("cntStr");
                city=bundle.getString("city");
                bend=bundle.getString("bend");
                String addressStr=bundle.getString("addressStr");
                Log.e("mmm",addressStr+" addressStr");
                Log.e("mmm",city+" city");
                district=bundle.getString("district");
                double lat=bundle.getDouble("lat");
                double lng=bundle.getDouble("lng");
                if (!TextUtils.isEmpty(addressStr)){
                    if (TextUtils.isEmpty(startAdd)){
                        startAdd=addressStr;
                        startAddTwo=startAdd;
                    }else {
                        endAdd=addressStr;
                        endAddTwo=endAdd;
                    }
                }
                Log.e("mmm",startAdd+" startAdd");
                Log.e("mmm",endAdd+" endAdd");
                rideTime=getStringTime(cnt);
                rideTimeTwo=rideTime;
                if (!TextUtils.isEmpty(BaseRequesUrl.newRuning)){
                    Log.e("mmm",  "       2222222222");
                    if (!BaseRequesUrl.newRuning.equals(runing)){
                        Log.e("mmm",  "       33333333333");
                        tvSpeed.setText("0.00");
                        tvJiaoDu.setText("0"+"度");
                        tvKmAcceleration.setText("0.00");
                        tvKm.setText("0.00");
                        tvRapidly.setText("0.00");
                        tvMeanSpeed.setText("0.00");
                        tvTime.setText("00:00:00");
                    }else {
                        Log.e("mmm",  "       444444444444");
                        tvTime.setText(rideTime);
                    }
                }else {
                    Log.e("mmm",  "      BaseRequesUrl.newRuning==null");
                }
                if (!TextUtils.isEmpty(rapidly)){
                    tvRapidly.setText(rapidly);
                    rapidlyTwo=rideTime;
                }
                if (!TextUtils.isEmpty(distenceStr)){
                    tvKm.setText(distenceStr);
                    distenceStrTwo=distenceStr;
                }
                if (!TextUtils.isEmpty(meanSpeedStr)){
                    tvMeanSpeed.setText(meanSpeedStr);
                    meanSpeedStrTwo=meanSpeedStr;
                }
                if (!TextUtils.isEmpty(cntStr)){
                    tvKmAcceleration.setText(cntStr);
                    cntStrTwo=cntStr;
                }
                if (!TextUtils.isEmpty(speedStr)){
                    tvSpeed.setText(speedStr);
                    speedStrTwo=speedStr;
                }
                if (!TextUtils.isEmpty(bend)){
                    tvJiaoDu.setText(bend+"度");
                    bendTwo=bend;
                }
                TypedArray ar = this.getResources().obtainTypedArray(R.array.action_mark);
                int len = ar.length();
                int[] resIds = new int[len];
                for (int i = 0; i < len; i++)
                    resIds[i] = ar.getResourceId(i, 0);
                if (!TextUtils.isEmpty(speedStr)){
                    double vaule=Double.valueOf(speedStr)/3.4;
                    int markId= (int) Math.round(vaule);
                    if (markId==69){
                        markId=70;
                    }
                    ivMark.setImageResource(resIds[markId]);
                    ar.recycle();
                }

                //实时上传房间成员位置信息
                if ( (cnt /60) % 1 == 0){
                    if (!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(district)){
                        loactionHttp(lat+"",lng+"",city+district);
                    }
                }
                break;
        }
    }

    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }
    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param
     * @return true 表示开启
     */
    private  boolean isOPen() {
        LocationManager locationManager
                = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean ok=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return ok;
    }
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    0);
        }
    }
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            //requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 0:
                BAIDU_READ_PHONE_STATE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到权限，做相应处理
                    //调用定位SDK应确保相关权限均被授权，否则会引起定位失败
                    Log.e("aaa","quanxian");
                } else{
                    //没有获取到权限，做特殊处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions( new String[]{ Manifest.permission.READ_PHONE_STATE },BAIDU_READ_PHONE_STATE );
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseHandlerOperate.getBaseHandlerOperate().removeKeyData(TripSpeedActivity.class);
    }
}
