package com.motorbike.anqi.activity.voice;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.map.LocationService;
import com.motorbike.anqi.activity.map.VoiceMapRoomActivity;
import com.motorbike.anqi.activity.my.QRCodeActivity;
import com.motorbike.anqi.activity.my.TuiJianActivity;
import com.motorbike.anqi.activity.trip.TripSpeedActivity;
import com.motorbike.anqi.bean.AqRoomInfoStrBean;
import com.motorbike.anqi.bean.CheckCarBean;
import com.motorbike.anqi.bean.RoomBean;
import com.motorbike.anqi.bean.RoomUserBean;
import com.motorbike.anqi.bean.UserListBean;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.handler.BaseHandlerUpDate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.init.MyApplication;
import com.motorbike.anqi.main.che.AddMotorcycleTypeActivity;
import com.motorbike.anqi.moduil.SimpleAVChatStateObserver;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.permission.MPermission;
import com.motorbike.anqi.permission.annotation.OnMPermissionDenied;
import com.motorbike.anqi.permission.annotation.OnMPermissionGranted;
import com.motorbike.anqi.permission.annotation.OnMPermissionNeverAskAgain;
import com.motorbike.anqi.service.MyVoiceService;
import com.motorbike.anqi.service.WakeUpService;
import com.motorbike.anqi.util.AudioManagerHandle;
import com.motorbike.anqi.util.JobSchedulerManager;
import com.motorbike.anqi.util.LGImgCompressor;
import com.motorbike.anqi.util.LogUtil;
import com.motorbike.anqi.util.PermissionsConstans;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CircleImageView;

import com.motorbike.anqi.view.CustomDialogView;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 语音聊天室
 */
public class VoiceHomeActivity extends BaseActivity implements View.OnClickListener, BaseHandlerUpDate {
    private LinearLayout add_layout,llFriendFive,llFriendFour,llFriendThree,llFriendTwo,llFriendOne;
    private String roomId,leaderId,joinRoomName,isAdmin;
    private CircleImageView headImage,imgOne,imgTwo,imgThree,imgFour,imgFive;
    private TextView tvNickname,tvStartPoint,tvTime,tvRemark,tvNameOne,tvNameTwo,tvNameThree,tvNameFour,tvNameFive
            ,moshiTv,titleText,tvHomehao;
    private UserPreference preference;
    private ImageView labaIMg,mkfIMg;
    private NimVoicerImpl nimVoicer;
    private boolean offMkf=false,zhumao=true,fangzhu=false,beiJinmai=false;
    // 定位相关
    private LocationService locationService;
    public MyLocationListenner myListener = new MyLocationListenner();

    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private String cityjidaoInfo;
    private List<String> typeList;//记录房间成员是否添加过车型的集合

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            cityjidaoInfo=location.getCity()+location.getDistrict();
            Map<String,Object> map=new ArrayMap<>();
            map.put("userId",BaseRequesUrl.uesrId);
            map.put("longitude",mCurrentLon+"");
            map.put("latitude",mCurrentLat+"");
            map.put("showArea",cityjidaoInfo);
            okHttp(VoiceHomeActivity.this,BaseRequesUrl.friendsposition,HttpTagUtil.friendsposition,map);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_home);
        preference=UserPreference.getUserPreference(this);

        roomId=getIntent().getStringExtra("roomId");
        BaseHandlerOperate.getBaseHandlerOperate().addKeyHandler(VoiceHomeActivity.class,this);
        if (roomId.equals(BaseRequesUrl.uesrRoomNo)){
            fangzhu=true;
        }
        initView();
    }

    private List<LinearLayout> linearLayouts;
    private List<CircleImageView> imageViewList;
    private List<TextView> textViewsList;
    @SuppressLint("WrongViewCast")
    private void initView()
    {

        linearLayouts=new ArrayList<>();
        imageViewList=new ArrayList<>();
        textViewsList=new ArrayList<>();
        typeList=new ArrayList<>();
        findViewById(R.id.gengduo_layout).setOnClickListener(this);
        findViewById(R.id.back_layout).setOnClickListener(this);
        titleText=findViewById(R.id.title_text);
        add_layout=findViewById(R.id.add_layout);
        add_layout.setOnClickListener(this);
        tvNickname=findViewById(R.id.tvNickname);
        tvStartPoint=findViewById(R.id.tvStartPoint);
        tvTime=findViewById(R.id.tvTime);
        tvRemark=findViewById(R.id.tvRemark);
        imgOne=findViewById(R.id.imgOne);
        imgTwo=findViewById(R.id.imgTwo);
        imgThree=findViewById(R.id.imgThree);
        imgFour=findViewById(R.id.imgFour);
        imgFive=findViewById(R.id.imgFive);

        imageViewList.add(imgOne);
        imageViewList.add(imgTwo);
        imageViewList.add(imgThree);
        imageViewList.add(imgFour);
        imageViewList.add(imgFive);
        tvHomehao=findViewById(R.id.tv_homehao);
        tvNameOne=findViewById(R.id.tvNameOne);
        tvNameTwo=findViewById(R.id.tvNameTwo);
        tvNameThree=findViewById(R.id.tvNameThree);
        tvNameFour=findViewById(R.id.tvNameFour);
        tvNameFive=findViewById(R.id.tvNameFive);
        textViewsList.add(tvNameOne);
        textViewsList.add(tvNameTwo);
        textViewsList.add(tvNameThree);
        textViewsList.add(tvNameFour);
        textViewsList.add(tvNameFive);
        llFriendOne=findViewById(R.id.llFriendOne);
        llFriendTwo=findViewById(R.id.llFriendTwo);
        llFriendThree=findViewById(R.id.llFriendThree);
        llFriendFour=findViewById(R.id.llFriendFour);
        llFriendFive=findViewById(R.id.llFriendFive);
        linearLayouts.add(llFriendOne);
        linearLayouts.add(llFriendTwo);
        linearLayouts.add(llFriendThree);
        linearLayouts.add(llFriendFour);
        linearLayouts.add(llFriendFive);
        headImage=findViewById(R.id.headImage);
        labaIMg=findViewById(R.id.voice);
        labaIMg.setOnClickListener(this);
        mkfIMg=findViewById(R.id.micro);
        mkfIMg.setOnClickListener(this);
        moshiTv=findViewById(R.id.moshi_text);
        moshiTv.setOnClickListener(this);

        if (!TextUtils.isEmpty(roomId))
        {
            showLoading();
            tvHomehao.setText(roomId);
            httpRoom(roomId);
            dynamicRoomUser(roomId);
        }

    }

    @Override
    protected void doDINGWEIPermission()
    {
        super.doDINGWEIPermission();
        locationService = MyApplication.getInstance().locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(myListener);
        locationService.start();// 定位SDK
    }

    /**
     * 聊天室接口
     * @param roomId
     */
    private void httpRoom(String roomId)
    {
        showLoading();
        Map<String,Object> roomMap=new HashMap<>();
        roomMap.put("roomNo",roomId);
        okHttp(this, BaseRequesUrl.TalkRoom, HttpTagUtil.TalkRoom,roomMap);
    }

    /**
     * 收藏房间接口
     * @param roomId
     * @param userId
     */
    private void collectRoom(String roomId,String userId)
    {
        Map<String,Object> collectMap=new HashMap<>();
        collectMap.put("roomNo",roomId);
        collectMap.put("userId",userId);
        okHttp(this,BaseRequesUrl.CollectRoom,HttpTagUtil.CollectRoom,collectMap);
    }

    /**
     * 退出房间
     * @param roomId
     * @param userId
     */
    private void signOutRoom(String roomId, String userId)
    {

        if (fangzhu) {//关闭房间
            closeRoom(roomId);
        } else {//退出房间
            showLoading();
            Map<String, Object> signOutMap = new HashMap<>();
            signOutMap.put("roomNo", roomId);
            signOutMap.put("userId", userId);
            okHttp(this, BaseRequesUrl.Exitroom, HttpTagUtil.Exitroom, signOutMap);

        }
    }

    /**
     * 动态获取聊天室成员
     * @param roomNo
     */
    private void dynamicRoomUser(String roomNo)
    {
        Map<String,Object> getUserMap=new HashMap<>();
        getUserMap.put("roomNo",roomNo);
        okHttp(this,BaseRequesUrl.DynamicRoomUser,HttpTagUtil.DynamicRoomUser,getUserMap);
    }

    /**
     * 关闭房间
     * @param roomNo
     */
    private void closeRoom(String roomNo)
    {
        showLoading();
        Map<String,Object> closeRoomMap=new HashMap<>();
        closeRoomMap.put("roomNo",roomNo);
        okHttp(this,BaseRequesUrl.CloseRoom,HttpTagUtil.CloseRoom,closeRoomMap);
    }

    /**
     * 设置房间模式
     * @param a
     */
    private void setRoomMode(String a)
    {
        Map<String,Object> map=new ArrayMap<>();
        map.put("roomNo",roomId);
        map.put("microphone",a);
        okHttp(this, BaseRequesUrl.zhumaiRoom, HttpTagUtil.ZHUMAIROOM,map);
    }

    /**
     * 查看每个人是否选择过车型接口
     * @param roomNo
     */
    private void checkCarSelect(String roomNo)
    {
        showLoading();
        Map<String,Object> checkMap=new HashMap<>();
        checkMap.put("roomNo",roomNo);
        okHttp(this,BaseRequesUrl.CheckCarSelect,HttpTagUtil.CheckCarSelect,checkMap);
    }

    /**
     * 发起行程通知消息
     * @param userId
     * @param roomNo
     */
    private void noticeHttp(String userId,String roomNo)
    {
        showLoading();
        Map<String,Object> noticeMap=new HashMap<>();
        noticeMap.put("userId",userId);
        noticeMap.put("roomNo",roomNo);
        okHttp(this,BaseRequesUrl.StartTripMove,HttpTagUtil.StartTripMove,noticeMap);
    }

    private RoomBean roomBean=null;
    private int roomNum=0;

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp)
    {
        super.httpRequestData(mTag, data, mag, isHttp);
        dismissLoading();
        switch (mTag){
            case HttpTagUtil.TalkRoom:
                if (data!=null)
                {
                     roomBean=new Gson().fromJson(data,RoomBean.class);
                    if (roomBean!=null){
                        roomkou=true;
                        AqRoomInfoStrBean roomInfoStrBean= roomBean.getAqRoomInfoStr();
                        joinRoomName=roomInfoStrBean.getTheme();
                        leaderId=roomInfoStrBean.getLeaderId();
                        if (leaderId.equals(BaseRequesUrl.uesrId)){
                            fangzhu=true;
                        }else {
                            fangzhu=false;
                        }
                        Log.e("aaaa","joinRoomName"+joinRoomName);

                        if (roomInfoStrBean!=null){
                            if (roomInfoStrBean.getType().equals("1")){
                                moshiTv.setText("主麦模式");
                                zhumao=true;
                            }else {
                                moshiTv.setText("自由模式");
                                zhumao=false;
                            }
                            tvNickname.setText(roomInfoStrBean.getNickname());
                            tvTime.setText(roomInfoStrBean.getSetTime());
                            tvRemark.setText(roomInfoStrBean.getRemark());
                            tvStartPoint.setText(roomInfoStrBean.getSetAddress());
                            loadPicture(roomInfoStrBean.getHeaderImg(),headImage);
                        }
                        //申请权限
                        checkPermission();
                    }
                }
                break;
            case HttpTagUtil.CollectRoom://收藏房间
                showTip(mag);
                break;
            case HttpTagUtil.Exitroom://成员退出房间
                showTip("退出房间");
                nimVoicer.hangupRoom(roomId);
                BaseRequesUrl.roomNo="0";
                finish();
                break;
            case HttpTagUtil.DynamicRoomUser://房间成员
                if (data!=null){
                    RoomUserBean roomUserBean=new Gson().fromJson(data,RoomUserBean.class);
                    if (roomUserBean!=null){
                        popokou=true;
                        List<UserListBean> userListBeanList=roomUserBean.getUserList();
                        if (userListBeanList!=null&&userListBeanList.size()>0){
                            visiabe();
                            for (int x=0;x<userListBeanList.size();x++){
                                roomNum=userListBeanList.size();
                                UserListBean userListBean = userListBeanList.get(x);
                                String uid=userListBean.getUserId();
                                if (uid.equals(BaseRequesUrl.uesrId)){
                                    if (userListBean.getMicrophone().equals("1")){
                                        beiJinmai=false;
                                    }else {
                                        beiJinmai=true;
                                    }
                                }
                                linearLayouts.get(x).setVisibility(View.VISIBLE);
                                setText(userListBean.getNickname(),textViewsList.get(x));
                                loadPicture(userListBean.getHeaderImg(),imageViewList.get(x));
                            }
                        }else {
                            visiabe();
                        }
                        //申请权限
                        checkPermission();
                    }
                }
                break;
            case HttpTagUtil.CloseRoom://关闭房间
                nimVoicer.hangupRoom(roomId);
                if (clickDialogWhat==0&&fangzhu) {
                    Intent intentTwo = new Intent(this, RacingvoiceHomeActivity.class);
                    startActivity(intentTwo);
                }
                finish();
                break;
            case HttpTagUtil.CheckCarSelect://
                if (data!=null){
                    if (typeList!=null){
                        typeList.clear();
                    }
                    List<CheckCarBean> carBeanList=new Gson().fromJson(data,new TypeToken<List<CheckCarBean>>(){}.getType());
                    if (carBeanList!=null&&carBeanList.size()>0){
                        for (int x=0;x<carBeanList.size();x++){
                            String type=carBeanList.get(x).getType();
                            String userid=carBeanList.get(x).getUserId();
                            if (!TextUtils.isEmpty(type)){
                                if (type.equals("4")||type.equals("5")){
                                    typeList.add(userid);
                                }
                            }
                        }
                        if (typeList!=null&&typeList.size()>0){
//                                showTip("房间中有"+typeList.size()+"人未添加车辆,不能发起行程!");
                            showAlertDialogTwo("提示信息","房间中有"+typeList.size()+"人未添加车辆,不能发起行程!");
                        }else {
                            clickDialogWhat=0;
                            showAlertDialog("提示信息","确定进入行程?");
                        }
                    }
                }
                break;
            case HttpTagUtil.StartTripMove:
                if (data!=null){
                    showTip(mag);
                }
                Intent is=new Intent(VoiceHomeActivity.this, TripSpeedActivity.class);
                is.putExtra("roomNum",roomId);
                is.putExtra("runing","1");
                startActivity(is);
                break;
        }
    }

    private void startVedioServer()
    {
        Intent intenttwo = new Intent(this, MyVoiceService.class);
        intenttwo.putExtra("roomNo", roomId);
        startService(intenttwo);
        Intent intent=new Intent(this, WakeUpService.class);
        startService(intent);
    }

    @Override
    public void showAlertDialog(String title, String masg)
    {
        super.showAlertDialog(title, masg);
    }
    private int clickDialogWhat=0;
    @Override
    protected void onclickDialog()
    {
        super.onclickDialog();
        switch (clickDialogWhat){
            case 0:
                noticeHttp(preference.getUserId(),roomId);
                break;
            case 1:
                closeRoom(roomId);
                break;
                default:
                    break;

        }

    }

    /**
     * 设置好友显示
     */
    private void visiabe()
    {
        llFriendOne.setVisibility(View.INVISIBLE);
        llFriendTwo.setVisibility(View.INVISIBLE);
        llFriendThree.setVisibility(View.INVISIBLE);
        llFriendFour.setVisibility(View.INVISIBLE);
        llFriendFive.setVisibility(View.INVISIBLE);
    }

    private void setText(String data,TextView textView)
    {
        textView.setText(data);
    }

    private void loadPicture(String data, CircleImageView circleImageView)
    {
        Picasso.with(VoiceHomeActivity.this)
                .load(data)
                .config(Bitmap.Config.RGB_565)
                .centerCrop()
                .fit()
                .networkPolicy(NetworkPolicy.NO_STORE)
                .into(circleImageView);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.gengduo_layout:
                Intent intent=new Intent(this, AutofriendsListActivity.class);
                intent.putExtra("roomId",roomId);
                intent.putExtra("leaderId",leaderId);
                startActivity(intent);
                break;
            case R.id.add_layout:
                shouPopuWod();
                break;
            case R.id.back_layout:
                finish();
                break;
            case R.id.micro:
                if (offMkf){
                    offMkf=false;
                    mkfIMg.setBackgroundResource(R.mipmap.lts_ej);
                    prompt("打开麦克风");
                }else{
                    offMkf=true;
                    mkfIMg.setBackgroundResource(R.mipmap.jinmai);
                    prompt("关闭麦克风");
                }
                nimVoicer.offMkf(offMkf);
                break;
            case R.id.voice:
                if (BaseRequesUrl.jingyin.equals("1")){
                    BaseRequesUrl.jingyin="0";
                    labaIMg.setBackgroundResource(R.mipmap.jinting);
                    prompt("静音");
                }else{
                    BaseRequesUrl.jingyin="1";
                    prompt("打开扬声器");
                    labaIMg.setBackgroundResource(R.mipmap.lts_gy);
                    if (!((AudioManager)getSystemService(Context.AUDIO_SERVICE)).isBluetoothA2dpOn()) {
                        AudioManagerHandle.setTSQ(this);
                        AVChatManager.getInstance().setSpeaker(true);
                    }
                }
                break;
            case R.id.moshi_text:
                if (fangzhu) {
                    if (zhumao){
                        zhumao = false;
                        moshiTv.setText("自由模式");
                        setRoomMode("2");
                    } else {
                        zhumao = true;
                        moshiTv.setText("主麦模式");
                        setRoomMode("1");
                    }
                }
                break;
        }
    }

    private PopupWindow popWnd;
    private View popuview;
    private ListView listview;
    String[] products={"房间二维码", "发起行程", "分享","详情", "收藏","更改行程","退出房间"};
    private void shouPopuWod()
    {
        if (popWnd==null) {
            popWnd = new PopupWindow(this);

            }
            if (popuview == null) {
                popuview = LayoutInflater.from(this).inflate(R.layout.ppopuview, null);
                listview = popuview.findViewById(R.id.popu_list);
                listview.setAdapter(new ArrayAdapter<String>(this, R.layout.popu_item, products));
                popuWodOnCilck();
            }
            popWnd.setContentView(popuview);
            popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popWnd.setAnimationStyle(R.style.popu_Anim_you);
            popWnd.setFocusable(true);
            popWnd.setOutsideTouchable(true);
            popWnd.showAsDropDown(add_layout);

    }

    private void popuWodOnCilck()
    {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://房间二维码
                        Intent intent=new Intent(VoiceHomeActivity.this, QRCodeActivity.class);
                        intent.putExtra("roomId",roomId);
                        startActivity(intent);
                        break;
                    case 1://发起行程
                        //行程
                        if (fangzhu){
                            checkCarSelect(roomId);
                        }else {
                            if (!TextUtils.isEmpty(isAdmin)){
                                if (isAdmin.equals("1")){
                                    checkCarSelect(roomId);
                                }else {
                                    showToast("您没有权限发起行程");
                                }
                            }
                        }
                        break;
                    case 2://分享
                        showPopwindow("http://app.mi.com/","欢迎加入安骑","房间号"+roomId);
                        break;
                    case 3://详情
                        Intent intent1=new Intent(VoiceHomeActivity.this, VoiceMapRoomActivity.class);
                        intent1.putExtra("roomNo",roomId);
                        startActivity(intent1);
                        break;
                    case 4://收藏
                        collectRoom(roomId,preference.getUserId());
                        break;
                    case 5://更改行程
                        if (fangzhu){
                            Intent i = new Intent(VoiceHomeActivity.this, RoutePlanningActivity.class);
                            i.putExtra("type","2");
                            i.putExtra("roomId",roomId);
                            i.putExtra("roomName",joinRoomName);
                            startActivity(i);
                        }else {
                            prompt("无权限");
                        }
                        break;
                    case 6:
                        if (fangzhu){//关闭房间
                            clickDialogWhat=1;
                            showAlertDialog("提示信息","确定关闭房间?");
                        }else {
                            signOutRoom(roomId, BaseRequesUrl.uesrId);
                        }
                        break;
                }
            }
        });
    }

    /**
     * service信息回调
     * @param msg
     */
    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what){
            case HttpTagUtil.CarFriendList:
                String dataStr=  msg.obj.toString();
                if (!TextUtils.isEmpty(dataStr))
                {
                    httpRequestData(HttpTagUtil.DynamicRoomUser, dataStr, "", true);
                }
                break;
            case HttpTagUtil.CloseRoom:
                signOutRoom(roomId,BaseRequesUrl.uesrId);
                break;
            case HttpTagUtil.Barley:
                String mkf=  msg.obj.toString();
                if (!TextUtils.isEmpty(mkf)){
                    if (mkf.equals("0")){
                        offMkf=false;
                        mkfIMg.setBackgroundResource(R.mipmap.lts_ej);
                        prompt("打开麦克风");
                    }else {
                        offMkf=true;
                        mkfIMg.setBackgroundResource(R.mipmap.jinmai);
                        prompt("关闭麦克风");
                    }
                }
                break;
            case HttpTagUtil.DynamicRoomUser:
                Bundle bundle = msg.getData();
                String micrType= bundle.getString("micrType");
                isAdmin =bundle.getString("isAdmin");
                String microphone= bundle.getString("microphone");
                Log.e("aaaa","microphone=="+microphone);
                if (micrType.equals("1")){
                    moshiTv.setText("主麦模式");
                    zhumao=true;
                }else {
                    moshiTv.setText("自由模式");
                    zhumao=false;
                }
                break;
            case HttpTagUtil.TalkRoom:
                Bundle data = msg.getData();
                String join=data.getString("join");
                if (join.equals("0")){
                    startVedioServer();
                }else {
                    String  code=data.getString("code");
                    this.join=false;
                    if (code.equals("404")){
                        closeRoom(roomId);
                        prompt("房间已关闭");
                    }else {
                        prompt("房间加入失败(" + code + ")");
                        signOutRoom(roomId, BaseRequesUrl.uesrId);
                    }
                }
                break;
            case HttpTagUtil.Exitroom:
                finish();
                break;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            requestPermission(PermissionsConstans.LOCATION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }else {
            doDINGWEIPermission();
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        if (popWnd!=null){
            popWnd.dismiss();
        }
        if (!TextUtils.isEmpty(roomId)) {
            httpRoom(roomId);
            String xroomID = preference.getRoomNoCurrent();
            //---BaseRequesUrl.roomNmID---为null----说明未知原因已退出房间，重新启动
            if (!TextUtils.isEmpty(xroomID)&&TextUtils.isEmpty(BaseRequesUrl.roomNmID)) {
                onPermissionChecked();
            }
            initmkf();
        }
    }

    @Override
    protected void onStop()
    {
        //取消注册传感器监听
        if (locationService!=null) {
            locationService.unregisterListener(myListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        super.onStop();
        if (popWnd!=null&&popWnd.isShowing()){
            popWnd.dismiss();
            popWnd=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseHandlerOperate.getBaseHandlerOperate().removeKeyData(VoiceHomeActivity.class);
    }

    /**
     * ************************************ 权限检查 ***************************************
     */

    private boolean roomkou=false,popokou=false, join=false;

    private void checkPermission()
    {

        if (roomkou&&popokou) {
            titleText.setText(joinRoomName + "(" + roomNum + "/" + roomBean.getAqRoomInfoStr().getPersonTotal() + ")");
            if (!join){
                List<String> lackPermissions = AVChatManager.getInstance().checkPermission(VoiceHomeActivity.this);
                if (lackPermissions.isEmpty()) {
                    onBasicPermissionSuccess();
                } else {
                    String[] permissions = new String[lackPermissions.size()];
                    for (int i = 0; i < lackPermissions.size(); i++) {
                        permissions[i] = lackPermissions.get(i);
                    }
                    MPermission.with(VoiceHomeActivity.this)
                            .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                            .permissions(permissions)
                            .request();
                }
            }
        }

    }


    //权限认证通过
    private void onPermissionChecked()
    {
        // 启动音频
        nimVoicer=NimVoicerImpl.getInstance();
        BaseRequesUrl.XroomNo=roomId;
        boolean sss;
        if (fangzhu){
            sss=true;
        }else {
            if (beiJinmai){
                sss=false;
            }else {
                sss=true;
            }
        }
        nimVoicer.startRtc(sss,new SimpleAVChatStateObserver()
        {
            @Override
            public void onJoinedChannel(int code, String audioFile, String videoFile, int i) {
                if (code == 200) {

                } else {

                }
            }

            @Override
            public void onUserJoined(String account) {
                dynamicRoomUser(roomId);
                LogUtil.i("startRtc", "on user joined, account=" + account);
            }

            @Override
            public void onUserLeave(String account, int event) {
                dynamicRoomUser(roomId);
                LogUtil.i("startRtc", "on user Leave, account=" + account);
            }
            @Override
            public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {

            }
        },roomId);
        //初始化喇叭
        initmkf();
    }

    private void initmkf()
    {
        if (nimVoicer==null){
            return;
        }
        if (nimVoicer.isMicrophoneMute()){
            offMkf=true;
            mkfIMg.setBackgroundResource(R.mipmap.jinmai);
        }else{
            offMkf=false;
            mkfIMg.setBackgroundResource(R.mipmap.lts_ej);
        }
        if (BaseRequesUrl.jingyin.equals("1")){
            labaIMg.setBackgroundResource(R.mipmap.lts_gy);
        }else{
            labaIMg.setBackgroundResource(R.mipmap.jinting);
        }
    }

    private static final int BASIC_PERMISSION_REQUEST_CODE = 0x100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess()
    {
        if (!join){
            join = true;
            onPermissionChecked();
        }
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed()
    {
        prompt("音视频通话所需权限未全部授权，部分功能可能无法正常运行！");
//        onPermissionChecked();
    }

    //友盟分享
    private UMImage imageurl;
    private UMWeb web;
    private Dialog selectPhotoDialog;
    ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
    private void showPopwindow(final String shareUrl, final String title, final String content){
        //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.share_popwindow, null);
        LinearLayout shareWx = contentView.findViewById(R.id.shareWx);
        LinearLayout shareWxCircle = contentView.findViewById(R.id.shareWxCircle);
        LinearLayout shareQQ = contentView.findViewById(R.id.shareQQ);
        LinearLayout shareQQZone=contentView.findViewById(R.id.shareQQZone);
        TextView tvDel =  contentView.findViewById(R.id.tvDel);
        imageurl=new UMImage(this,R.mipmap.icon);//自定义分享面板，待确认是否定制面板成功
        imageurl.compressStyle = UMImage.CompressStyle.SCALE;
        web=new UMWeb(shareUrl);
        web.setThumb(imageurl);
        web.setTitle(title);
        web.setDescription(content);
        shareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(VoiceHomeActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(web)//分享内容
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });

        shareWxCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(VoiceHomeActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)//分享内容
                        .setCallback(shareListener)//回调监听器
                        .share();

            }
        });
        shareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(VoiceHomeActivity.this)
                        .setPlatform(SHARE_MEDIA.QQ)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();

            }
        });
        shareQQZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(VoiceHomeActivity.this)
                        .setPlatform(SHARE_MEDIA.QZONE)//传入平台
                        .withMedia(web)//分享内容
                        .setCallback(shareListener)//回调监听器
                        .share();
            }
        });
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotoDialog.dismiss();
            }
        });
        selectPhotoDialog = new Dialog(this);
        selectPhotoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectPhotoDialog.setContentView(contentView);
        Window regionWindow = selectPhotoDialog.getWindow();
        regionWindow.setGravity(Gravity.BOTTOM);
        regionWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        regionWindow.setWindowAnimations(R.style.view_animation);
        regionWindow.setBackgroundDrawable(dw);
        selectPhotoDialog.setCanceledOnTouchOutside(true);
        if (!selectPhotoDialog.isShowing()){
            selectPhotoDialog.show();
        }
    }

    private UMShareListener shareListener = new UMShareListener()
    {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            prompt("开始");
        }
        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            prompt("成功");
        }
        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            prompt("失败");
        }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            prompt("取消");
        }
    };

    /**
     * 自定义dialog提示框
     *
     * @param
     */
    public void showAlertDialogTwo(String title, String masg) {
        final CustomDialogView dialog = new CustomDialogView(this);
        dialog.setTitle(title);
        dialog.setMessage(masg);
        dialog.setYesOnclickListener("确定", new CustomDialogView.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                dialog.dismiss();

            }
        });
//        dialog.setNoOnclickListener("取消", new CustomDialogView.onNoOnclickListener() {
//            @Override
//            public void onNoClick() {
//                dialog.dismiss();
//            }
//        });
        dialog.show();
    }
}
