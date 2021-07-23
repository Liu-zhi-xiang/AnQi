package com.motorbike.anqi;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;


import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clj.fastble.BleManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.motorbike.anqi.activity.loin.LoginActivity;
import com.motorbike.anqi.activity.my.MessageCenterActivity;
import com.motorbike.anqi.activity.my.UserCenterActivity;
import com.motorbike.anqi.activity.trip.TripSpeedActivity;
import com.motorbike.anqi.activity.voice.RacingvoiceHomeActivity;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.adapter.UserCarAdapter;
import com.motorbike.anqi.bean.IndexBean;
import com.motorbike.anqi.bean.LoginBean;
import com.motorbike.anqi.bean.RecommendRoomListBean;
import com.motorbike.anqi.bean.UserCarBean;
import com.motorbike.anqi.beseAdapter.CommonAdapter;
import com.motorbike.anqi.beseAdapter.MultiItemTypeAdapter;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.core.util.QRScannerHelper;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.handler.BaseHandlerUpDate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.init.MyApplication;
import com.motorbike.anqi.main.AutoMomentsActivity;
import com.motorbike.anqi.main.RongIMHandler;
import com.motorbike.anqi.main.che.AddMotorcycleTypeActivity;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.service.BleManagerHandler;
import com.motorbike.anqi.service.BluetoothConnectionReceiver;
import com.motorbike.anqi.service.MyVoiceService;
import com.motorbike.anqi.service.NoisyAudioStreamReceiver;
import com.motorbike.anqi.service.PhoneListenService;
import com.motorbike.anqi.service.WakeUpService;
import com.motorbike.anqi.util.AudioManagerHandle;
import com.motorbike.anqi.util.JobSchedulerManager;
import com.motorbike.anqi.util.MD5Util;
import com.motorbike.anqi.util.PermissionsConstans;
import com.motorbike.anqi.util.PixelFormat;
import com.motorbike.anqi.util.ScreenManager;
import com.motorbike.anqi.util.ScreenReceiverUtil;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.CustomDialogView;
import com.motorbike.anqi.view.InputDialog;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityTwo extends BaseActivity implements View.OnClickListener, MultiItemTypeAdapter.OnItemClickListener, AdapterView.OnItemClickListener, BaseHandlerUpDate, BleManagerHandler.BleconnectInterface {
    private XRecyclerView recyclerview;
    private MyAdapter myadapter;
    private int xrecyclerviewHeight = 0, marginTop = 0;
    private LinearLayout linearLayout, addlayout;
    private ImageView xiaImg, ivMessage,cheyouMessage,lanyeImg;
    private TextView tvAdd;
    private Map<String, Object> userCarMap, indexMap, addRoomMap,msgMap,updateCarMap;
    private UserPreference preference;
    private UserCarAdapter userCarAdapter;
    private List<RecommendRoomListBean> roomListBeans;
    private String roomNum;
    private QRScannerHelper mScannerHelper;
    private boolean bluetoothLink=false;
    // JobService，执行系统任务
    private JobSchedulerManager mJobManager;
    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;

    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            // 亮屏，移除"1像素"
            mScreenManager.finishActivity();
        }

        @Override
        public void onSreenOff() {
            // 接到锁屏广播，将SportsActivity切换到可见模式
            // "咕咚"、"乐动力"、"悦动圈"就是这么做滴
//            Intent intent = new Intent(SportsActivity.this,SportsActivity.class);
//            startActivity(intent);
            // 如果你觉得，直接跳出SportActivity很不爽
            // 那么，我们就制造个"1像素"惨案
            mScreenManager.startActivity();
        }

        @Override
        public void onUserPresent() {
            // 解锁，暂不用，保留
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_two);
        // 1. 注册锁屏广播监听器
        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);
        // 2. 启动系统任务
        mJobManager = JobSchedulerManager.getJobSchedulerInstance(this);
        mJobManager.startJobScheduler();
        preference = UserPreference.getUserPreference(this);
        String uisd=preference.getUserId();
        if (TextUtils.isEmpty(uisd)){
            startActivity(new Intent(this,LoginActivity.class));
            prompt("登录失败，重新登录！");
            finish();
        }else {
            BaseRequesUrl.uesrId = uisd;
            initView();
            //当前加入的房间，防止上次未正常退出
            if (!TextUtils.isEmpty(preference.getRoomNoCurrent())&&!preference.getRoomNoCurrent().equals(BaseRequesUrl.uesrRoomNo)) {
                signOutRoom(preference.getRoomNoCurrent());
            }
            BaseHandlerOperate.getBaseHandlerOperate().addKeyHandler(MainActivityTwo.class, this);
        }
        //获取定位及电话状态权限
        if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)&&!hasPermission(Manifest.permission.CALL_PHONE)&&!hasPermission(Manifest.permission.READ_PHONE_STATE)&&!hasPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)){
            requestPermission(PermissionsConstans.LOCATION_CODE, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.PROCESS_OUTGOING_CALLS,
                    Manifest.permission.CALL_PHONE,Manifest.permission.READ_PHONE_STATE);
        }
        //注册监听电话状态
        registerPhoneStateListener();
        //注册电话监听
        monitorEarphone();
        bleManagerHandler=BleManagerHandler.getInstance();
        bleManagerHandler.setBleconnectInterface(this);
    }

    private void initView()
    {

        BaseRequesUrl.account = preference.getLoginPhone();
        BaseRequesUrl.uesrHead = preference.getHeardImage();
        BaseRequesUrl.uesrName = preference.getUserNickname();
        BaseRequesUrl.uesrRoomNo = preference.getUserRoomNo();
        carBeanList=new ArrayList<>();
        findViewById(R.id.message_layout).setOnClickListener(this);
        findViewById(R.id.gengduo_layout).setOnClickListener(this);
        findViewById(R.id.saoyisao_layout).setOnClickListener(this);
        findViewById(R.id.journey_layout).setOnClickListener(this);
        findViewById(R.id.me_layout).setOnClickListener(this);
        findViewById(R.id.jinsu_img).setOnClickListener(this);
        findViewById(R.id.msgs_layout).setOnClickListener(this);
        findViewById(R.id.journey_layout).setOnClickListener(this);
        ivMessage = findViewById(R.id.ivMessage);
        cheyouMessage = findViewById(R.id.che_you_quan);
        addlayout = findViewById(R.id.add_layout);
        lanyeImg = findViewById(R.id.iv_labya);
        addlayout.setOnClickListener(this);
        xiaImg = findViewById(R.id.xia_imgview);
        tvAdd = findViewById(R.id.tvAdd);
        roomListBeans = new ArrayList<>();
        linearLayout = findViewById(R.id.xrecyclerView_layout);
        recyclerview = findViewById(R.id.home_xrecyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setPullRefreshEnabled(true);
        recyclerview.setLoadingMoreEnabled(false);
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh()
            {
                httpIndex(preference.getUserId());
            }

            @Override
            public void onLoadMore() {

            }
        });
        userCarMap = new HashMap<>();
        myadapter = new MyAdapter(this, R.layout.home_recyclerview_item, roomListBeans);
        recyclerview.setAdapter(myadapter);

        myadapter.setOnItemClickListener(this);
        myadapter.notifyDataSetChanged();
        initQRScanner();
        BaseRequesUrl.jpushId=preference.getJpushId();
        if (!TextUtils.isEmpty(preference.getCarType()))
        {
            tvAdd.setText(preference.getCarType());
        }else {
            tvAdd.setText("添加");
        }
        showLoading();
        httpIndex(preference.getUserId());
        msgHttp(preference.getUserId());
//        initCallPhone();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.message_layout:
                //车友圈
                cheyouMessage.setVisibility(View.GONE);
                startActivity(new Intent(this, AutoMomentsActivity.class));
                break;
            case R.id.me_layout:
                //个人中心
                startActivity(new Intent(this, UserCenterActivity.class));
                break;
            case R.id.jinsu_img:
                //竞速语音室
                String roomNO=preference.getRoomNoCurrent();
                Log.e("aaaaa","roomNO=="+roomNO);
                if (indexBean!=null){
                    if (!TextUtils.isEmpty(roomNO))
                    {
                        Intent intent = new Intent(MainActivityTwo.this, VoiceHomeActivity.class);
                        intent.putExtra("roomId", roomNO);
                        startActivity(intent);
                    } else {
                        Intent intentTwo = new Intent(MainActivityTwo.this, RacingvoiceHomeActivity.class);
                        intentTwo.putExtra("roomStatus", indexBean.getRoomStatus());
                        startActivity(intentTwo);
                    }
                }
                break;
            case R.id.msgs_layout:
                //消息中心
                startActivity(new Intent(this, MessageCenterActivity.class));
                break;
            case R.id.add_layout:
                httpUserCar(preference.getUserId());
                break;
            case R.id.saoyisao_layout:
                if (hasPermission(Manifest.permission.CAMERA)) {
                    doCameraPermission();
                } else {
                    requestPermission(PermissionsConstans.INSEPCT_CAMERA, Manifest.permission.CAMERA);
                }
                break;
            case R.id.journey_layout:
                //行程
                Intent intent=new Intent(this, TripSpeedActivity.class);
                intent.putExtra("roomNum",BaseRequesUrl.roomNo);
                intent.putExtra("runing","2");
                startActivity(intent);
                break;
            case R.id.gengduo_layout:
                if (!bluetoothLink)
                {
                    showAlertDialog("系统提示","  连接耳机BLE?");
                }
                break;
        }
    }

    @Override
    protected void onclickDialog()
    {
        super.onclickDialog();
        initBle();
    }

    private PopupWindow popWnd;
    private View popuview;
    private ListView listview;

    private void shouPopuWod()
    {
        if (userCarAdapter == null) {
            userCarAdapter = new UserCarAdapter(this);
        }
        if (popWnd == null) {
            popWnd = new PopupWindow(this);
            popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    xiaImg.setBackgroundResource(R.mipmap.xia);
                }
            });
        }
        if (popuview == null) {
            popuview = LayoutInflater.from(this).inflate(R.layout.ppopuview, null);
            listview = popuview.findViewById(R.id.popu_list);
        }
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        int x = ((carBeanList.size() < 5) ? carBeanList.size() : 5) + 1;
        params.height = PixelFormat.dip2px(this, x * 50);
        listview.setLayoutParams(params);
        listview.setAdapter(userCarAdapter);
        listview.setOnItemClickListener(this);
        userCarAdapter.setList(carBeanList);
        userCarAdapter.notifyDataSetChanged();
        popWnd.setContentView(popuview);
        popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setAnimationStyle(R.style.contextMenuAnim);
        popWnd.setFocusable(true);
        popWnd.setOutsideTouchable(true);
//            Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_rotate_shun);
//            xiaImg.startAnimation(animation);
        popWnd.showAsDropDown(addlayout);
        xiaImg.setBackgroundResource(R.mipmap.shang);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position)
    {
        if (roomListBeans != null){
            RecommendRoomListBean recommendRoomListBean = roomListBeans.get(position - 1);
            String isPassword = recommendRoomListBean.getIsPassWord();

            roomNum=recommendRoomListBean.getRoomNo();
            String  xroomid= preference.getRoomNoCurrent();
            if (!TextUtils.isEmpty(BaseRequesUrl.roomNmID)
                    &&roomNum.equals(BaseRequesUrl.roomNmID))
            {
                Intent intent = new Intent(this, VoiceHomeActivity.class);
                intent.putExtra("roomId", roomNum);
                startActivity(intent);
            }else if (!TextUtils.isEmpty(xroomid)
                    &&xroomid.equals(BaseRequesUrl.uesrRoomNo)
                    &&indexBean.getRoomStatus().equals("1"))
            {
               prompt("你的语音室已开启，无法加入其它房间");
            }else {
                if (!TextUtils.isEmpty(isPassword)){
                    if (isPassword.equals("1")){
                        showInputDialogTwo(recommendRoomListBean.getPassword(), roomNum);
                    } else {
                        jionRoom(roomNum, "");
                    }
                }
            }
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position)
    {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        popWnd.dismiss();
//        if (products.length==0){
//            startActivity(new Intent(this, AddMotorcycleTypeActivity.class));
//        }else if ( products.length>0&&position == products.length - 1) {
//            startActivity(new Intent(this, AddMotorcycleTypeActivity.class));
//        }
        String selectCar = carBeanList.get(position).getBrand() + carBeanList.get(position).getModels();
        showTip(selectCar);
        tvAdd.setText(selectCar);
        preference.setCarType(selectCar);
        preference.save();
        if (!TextUtils.isEmpty(carBeanList.get(position).getBrand())&&!TextUtils.isEmpty(carBeanList.get(position).getModels())){
            updateCarHttp(preference.getUserId(),carBeanList.get(position).getBrand(),carBeanList.get(position).getModels());
        }
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what){
            case HttpTagUtil.IndexPage:
//                httpIndex(preference.getUserId());
                break;
            case HttpTagUtil.RONGYUN_TOKEN:
                rongYun();
                break;
            case HttpTagUtil.TalkRoom:
                //意外关闭重新开启
                Intent intenttwo = new Intent(this, MyVoiceService.class);
                intenttwo.putExtra("roomNo", BaseRequesUrl.roomNmID);
                startService(intenttwo);
                Intent wakeUpIntent =new Intent(this, WakeUpService.class);
                startService(wakeUpIntent);

                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MyApplication.getInstance().AppExit();
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void isconnect()
    {
        bluetoothLink=true;
        lanyeImg.setImageResource(R.mipmap.lanye);

    }

    @Override
    public void isnotconnect()
    {
        lanyeImg.setImageResource(R.mipmap.lanye_an);
        bluetoothLink=false;

    }

    @Override
    public void toastMsg(String msg) {
        final CustomDialogView dialog = new CustomDialogView(this);
        dialog.setTitle("Ble消息提示");
        dialog.setMessage(msg);
        dialog.setYesOnclickListener("确定", new CustomDialogView.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean FocusChanged = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (!FocusChanged) {
            int x = linearLayout.getHeight();
            xrecyclerviewHeight = x / 2 ;
            marginTop = xrecyclerviewHeight / 2;
//            myadapter.notifyDataSetChanged();
            FocusChanged = true;
            Log.e("Aaaa", xrecyclerviewHeight + "===xrecyclerviewHeight2");
        }
    }

    @Override
    protected void doCameraPermission()
    {
        mScannerHelper.startScanner();
    }
    private BleManagerHandler bleManagerHandler;

    private List<UserCarBean> carBeanList;
    private IndexBean indexBean;
    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp)
    {
        super.httpRequestData(mTag, data, mag, isHttp);
        switch (mTag) {
            case HttpTagUtil.UserCarType://获取用户已有车型
                dismissLoading();
                if (data != null) {
                    List<UserCarBean> carList = new Gson().fromJson(data, new TypeToken<List<UserCarBean>>() {
                    }.getType());
                    if (carBeanList!=null){
                        carBeanList.clear();
                    }
                    carBeanList.addAll(carList);
                    if (carBeanList != null&&carBeanList.size()>0) {
                        shouPopuWod();
                        Log.e("aaa", carBeanList.size() + "");
                        userCarAdapter.notifyDataSetChanged();
                    }else{
                        startActivity(new Intent(this, AddMotorcycleTypeActivity.class));
                    }
                }
                break;
            case HttpTagUtil.IndexPage:
                dismissLoading();
                if (data != null) {
                    indexBean = new Gson().fromJson(data, IndexBean.class);
                    if (indexBean != null) {
                        recyclerview.refreshComplete();
                        List<RecommendRoomListBean> recommendRoomLis = indexBean.getRecommendRoomList();
                        List<RecommendRoomListBean> friendRoomList = indexBean.getFriendRoomList();
                        if (roomListBeans!=null){
                            roomListBeans.clear();
                        }
                        if (friendRoomList != null) {
                            roomListBeans.addAll(friendRoomList);
                        }
                        if (recommendRoomLis!=null){
                            roomListBeans.addAll(recommendRoomLis);
                        }
                        myadapter.notifyDataSetChanged();
                    }
                }
                break;
            case HttpTagUtil.AddRoom://扫描二维码加入房间
                dismissLoading();
                if (data != null){
                    if (!TextUtils.isEmpty(roomNum)) {
                        Intent intent = new Intent(this, VoiceHomeActivity.class);
                        intent.putExtra("roomId", roomNum);
                        startActivity(intent);
                    }
                }

                break;
            case HttpTagUtil.RONGYUN_TOKEN:
                if (data!=null){
                    Log.e("tocken","=========融云重新链接=======");
                    preference.setRyToken(data);
                    preference.save();
                    RongIMHandler.connect(data,this);
                }
                break;
            case HttpTagUtil.UnReadMsg://消息未读数
                dismissLoading();
                    if (!TextUtils.isEmpty(data)) {
                        if (data.equals("0")) {
                            ivMessage.setVisibility(View.GONE);
                        } else {
                            ivMessage.setVisibility(View.VISIBLE);
                        }
                    }
                Map<String,Object> map=new ArrayMap<>();
                map.put("userId",BaseRequesUrl.uesrId);
                okHttp(this,BaseRequesUrl.noteIsRemind,HttpTagUtil.noteIsRemind,map);
                break;
            case HttpTagUtil.UpdateUserCartype://更新车型
                dismissLoading();
                if (data!=null){

                }
                break;
            case HttpTagUtil.wangyiTocken:
                if (data!=null){
                    LoginBean loginBean = new Gson().fromJson(data, LoginBean.class);
                    if (loginBean!=null){
                        preference.setAccid(loginBean.getAccid());
                        preference.setToken(loginBean.getToken());
                        preference.save();
                        doLogin();
                    }
                    Log.e("kkk",preference.getToken());
                }
                break;
            case HttpTagUtil.noteIsRemind:
                if (!TextUtils.isEmpty(data)) {
                    if (data.equals("0")) {
                        cheyouMessage.setVisibility(View.GONE);
                    } else {
                        cheyouMessage.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!BaseRequesUrl.RongIMLj){
            String ryTocken = preference.getRyToken();
            if (!TextUtils.isEmpty(ryTocken)) {
                RongIMHandler.connect(ryTocken,this);
            } else {
                rongYun();
            }
        }
        if (!BaseRequesUrl.wangyiServer){
            doLogin();
        }
        if (!TextUtils.isEmpty(preference.getCarType()))
        {
            tvAdd.setText(preference.getCarType());
        }else {
            tvAdd.setText("添加");
        }
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        if (popWnd != null) {
            popWnd.dismiss();
        }
        httpIndex(preference.getUserId());
        msgHttp(preference.getUserId());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (!TextUtils.isEmpty(BaseRequesUrl.roomNmID)&&!BaseRequesUrl.roomNmID.equals(BaseRequesUrl.uesrRoomNo)){
            signOutRoom(preference.getRoomNoCurrent());
        }
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        unregisterReceiver(audioNoisyReceiver);
        unregisterReceiver(audioStreamReceiver);
        AudioManagerHandle.stopSDC(this);
        BaseHandlerOperate.getBaseHandlerOperate().removeKeyData(MainActivityTwo.class);
        Intent intent=new Intent(this,WakeUpService.class);
        stopService(intent);

        //房间销毁后，设置手机播放器为麦克风
        //获取音频服务
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);// 打开扬声器
//        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        audioManager.setMode(AudioManager.MODE_NORMAL);//可取值NORMAL（普通）, RINGTONE（铃声）, or IN_CALL（通话）
        // 指定调节音乐的音频，增大音量，而且不显示音量图形示意
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        // 指定调节音乐的音频，增大音量，而且显示音量图形示意
//        aManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    private void showInputDialogTwo(final String password, final String roormId)
    {
        final InputDialog inputDialog = new InputDialog(this);
        inputDialog.setTitle("房间密码");
        inputDialog.setOnbutClion(new InputDialog.OnbutClion() {
            @Override
            public void onokclick(String inputstr) {
                if (TextUtils.isEmpty(inputstr)){
                    return;
                }
                String str = MD5Util.encrypt(inputstr);
                if (str.equals(password)) {
                    jionRoom(roormId, inputstr);
                    inputDialog.dismiss();
                } else {
                    showTip("房间密码不正确,请重新输入!");
                }
            }

            @Override
            public void onFinshclick()
            {

            }
        });
        inputDialog.show();
    }

    /**
     * 初始化二维码扫描及回调
     */
    private void initQRScanner()
    {
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                if (!TextUtils.isEmpty(result)) {
                    String[] str = result.split(",");
                    if (str != null && str.length > 0) {
                        roomNum=str[0];
                        jionRoom(str[0],"");
                    }
                }
            }
        });
    }

    //网易云手动登录
    public void doLogin()
    {
        if (!NimVoicerImpl.getInstance().doLogin()){
            getTocken(preference.getLoginPhone());
        }
    }

    /**
     * 申请蓝牙权限
     */
    private void initBle()
    {
        if (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)) {
            doBlePermission();
        } else {
            requestPermission(PermissionsConstans.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    /**
     * 打开扫描蓝牙
     */
    @Override
    protected void doBlePermission()
    {
        super.doBlePermission();
        bleManagerHandler.connectedDevice();
//        /**
//         * 添加BLE列表并打开手机蓝牙
//         */
//        Log.e("tttttt",BleManager.getInstance().isSupportBle()+" 是否支持BLE");
//        BleManager.getInstance().enableBluetooth();//通过蓝牙适配器直接打开蓝牙
//        Intent intent=new Intent(this, ConnectBLEActivity.class);
//        startActivity(intent);
    }
    private BluetoothConnectionReceiver audioNoisyReceiver;
    private NoisyAudioStreamReceiver audioStreamReceiver;
    private void monitorEarphone()
    {
         audioNoisyReceiver = new BluetoothConnectionReceiver();
        //蓝牙状态广播监听
        IntentFilter audioFilter = new IntentFilter();
        audioFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        audioFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(audioNoisyReceiver, audioFilter);

        //耳机变化监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioManager.ACTION_HEADSET_PLUG);
        filter.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED);
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        filter.addAction(AudioManager.ACTION_HDMI_AUDIO_PLUG);
        filter.addAction(AudioManager.EXTRA_SCO_AUDIO_PREVIOUS_STATE);
        filter.addAction(AudioManager.EXTRA_SCO_AUDIO_STATE);
        audioStreamReceiver=new NoisyAudioStreamReceiver();
        registerReceiver(audioStreamReceiver, filter);


    }
    public class MyAdapter extends CommonAdapter<RecommendRoomListBean>
    {

        public MyAdapter(Context context, int layoutId, List<RecommendRoomListBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final RecommendRoomListBean roomListBean, int position) {
            RelativeLayout view = holder.getView(R.id.recitr_rv);
            if (xrecyclerviewHeight != 0)
            {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = xrecyclerviewHeight;
                view.setLayoutParams(layoutParams);
            }
            ImageView imageView=holder.getView(R.id.list_item_room);
//            if (!TextUtils.isEmpty(roomListBean.getBackUrl())){
//                Picasso.with(MainActivityTwo.this)
//                        .load(roomListBean.getBackUrl())
//                        .error(R.mipmap.img1)
//                        .resize(400,300)
//                        .into(imageView);
//            }
            TextView title_text = holder.getView(R.id.title_text);
            TextView tvNickname = holder.getView(R.id.tvNickname);
            TextView tvRoomNum = holder.getView(R.id.tvRoomNum);
            TextView tvMember=holder.getView(R.id.tvMember);//实时房间在线人数
            TextView kaqi_text = holder.getView(R.id.kaqi_text);
            if (roomListBean != null) {
                title_text.setText(roomListBean.getTheme());
                tvNickname.setText(roomListBean.getName());
                tvRoomNum.setText(roomListBean.getRoomNo());
                tvMember.setText(roomListBean.getRoomCount());
                String status = roomListBean.getStatus();//1 开启  0 关闭
                if (!TextUtils.isEmpty(status)){
                    if (status.equals("0")){
                        kaqi_text.setText("关闭");
                    }else{
                        kaqi_text.setText("开启");
                    }
                }
            }

        }
    }
    /**
     * 获取未读消息
     * @param userId
     */
    private void msgHttp(String userId)
    {

        msgMap=new HashMap<>();
        if (msgMap!=null)
        {
            msgMap.clear();
        }
        msgMap.put("userId",userId);
        okHttp(this,BaseRequesUrl.UnReadMsg,HttpTagUtil.UnReadMsg,msgMap);
    }
    /**
     * 获取用户已有车型接口
     *
     * @param userId
     */
    private void httpUserCar(String userId)
    {
        showLoading();
        if (!TextUtils.isEmpty(userId)) {
            if (userCarMap != null) {
                userCarMap.clear();
            }
            userCarMap.put("userId", userId);
            okHttp(this, BaseRequesUrl.UserCarType, HttpTagUtil.UserCarType, userCarMap);
        } else {
            showTip("信息不能为空");
        }
    }

    /**
     * 更新应用车型
     * @param userId
     * @param brand
     * @param models
     */
    private void updateCarHttp(String userId,String brand,String models)
    {
        showLoading();
        updateCarMap=new HashMap<>();
        if (updateCarMap!=null){
            updateCarMap.clear();
        }
        updateCarMap.put("userId",userId);
        updateCarMap.put("brand",brand);
        updateCarMap.put("models",models);
        okHttp(this,BaseRequesUrl.UpdateUserCartype,HttpTagUtil.UpdateUserCartype,updateCarMap);
    }

    /**
     * 扫描二维码加入房间
     *
     * @param roomNo
     * @param password
     */
    private void jionRoom(String roomNo,String password)
    {
        showLoading();
        addRoomMap = new HashMap<>();
        if (addRoomMap != null) {
            addRoomMap.clear();
        }
        addRoomMap.put("userId", preference.getUserId());
        addRoomMap.put("roomNo", roomNo);
        addRoomMap.put("password", password);
        okHttp(this, BaseRequesUrl.AddRoom, HttpTagUtil.AddRoom, addRoomMap);
    }

    /**
     * 首页数据接口
     *
     * @param userId
     */
    private void httpIndex(String userId)
    {

        indexMap = new HashMap<>();
        if (indexMap != null)
        {
            indexMap.clear();
        }
        indexMap.put("userId",userId);
        okHttp(this, BaseRequesUrl.IndexPage, HttpTagUtil.IndexPage, indexMap);
    }

    /**
     * 退出房间
     * @param roomId
     *
     */
    private void signOutRoom(String roomId)
    {
        //退出房间
        Map<String, Object> signOutMap = new HashMap<>();
        signOutMap.put("roomNo", roomId);
        signOutMap.put("userId", BaseRequesUrl.uesrId);
        okHttp(this, BaseRequesUrl.Exitroom, HttpTagUtil.Exitroom, signOutMap);
        NimVoicerImpl.getInstance().hangupRoom(roomId);

    }
    /**
     * 获取网易云token
     * @param phone
     */
    private void getTocken(String phone)
    {
        Map<String,Object> getTockenMap=new HashMap<>();
        if (getTockenMap!=null)
        {
            getTockenMap.clear();
        }
        getTockenMap.put("phone",phone);
        okHttp(this,BaseRequesUrl.wangyiTocken,HttpTagUtil.wangyiTocken,getTockenMap);
    }

    /**
     * 获取融云TOKEN
     */
    public void rongYun()
    {
        Map<String,Object> map=new ArrayMap<>();
        map.put("userId",BaseRequesUrl.uesrId);
        map.put("phone",BaseRequesUrl.account);
        okHttp(this,BaseRequesUrl.Rongyun,HttpTagUtil.RONGYUN_TOKEN,map);
    }

    /**
     * 注册电话监听服务
     */
    private void registerPhoneStateListener() {
        Intent intent = new Intent(this, PhoneListenService.class);
        intent.setAction(PhoneListenService.ACTION_REGISTER_LISTENER);
        startService(intent);
    }
}
