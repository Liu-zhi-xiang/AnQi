package com.motorbike.anqi.activity.voice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.loin.LoginActivity;
import com.motorbike.anqi.bean.MediaBean;
import com.motorbike.anqi.bean.RoomXqBean;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.handler.BaseHandlerUpDate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.init.LuntanFabuReques;
import com.motorbike.anqi.init.OppenRoomReques;
import com.motorbike.anqi.interfaces.HttpRequestTag;
import com.motorbike.anqi.moduil.SimpleAVChatStateObserver;
import com.motorbike.anqi.nim.DemoCache;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.permission.MPermission;
import com.motorbike.anqi.permission.annotation.OnMPermissionDenied;
import com.motorbike.anqi.permission.annotation.OnMPermissionGranted;
import com.motorbike.anqi.permission.annotation.OnMPermissionNeverAskAgain;
import com.motorbike.anqi.util.LGImgCompressor;
import com.motorbike.anqi.util.LogUtil;
import com.motorbike.anqi.util.PermissionsConstans;
import com.motorbike.anqi.util.UserPreference;
import com.motorbike.anqi.view.InputDialog;

import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 竞速语音室
 */
public class RacingvoiceHomeActivity extends BaseActivity implements View.OnClickListener, HttpRequestTag, LGImgCompressor.CompressListener, BaseHandlerUpDate {

    private LinearLayout rightLayout;
    private TextView titleTv, myroomNoTv,shangxinTv;
    private EditText roomNoEt, roomNameEt, roomPassEt, roompoplnumEt;
    private ImageView roomBgImg, passYanImg, zhumaiImg, ziyouImg,luxianIMg,headimg;
    private String roomNoStr,roomNameStr, roompoplnum,city;
    private boolean zhumao = true, passGone = true;
    private RoomXqBean roomXqBean;
    private UserPreference preference;
    private int roomshax=4;
    private Map<String,Object> verifyRoomMap,addRoomMap;
    private String roomStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racingvoice_home);
        initView();
    }

    private void initView()
    {
        roomStatus=getIntent().getStringExtra("roomStatus");
        if (TextUtils.isEmpty(roomStatus))
        {
            roomStatus="0";
        }
        fileList=new ArrayList<>();
        preference=UserPreference.getUserPreference(this);
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.luxian_ll).setOnClickListener(this);
        findViewById(R.id.goRoombut).setOnClickListener(this);
        findViewById(R.id.kaiqi_layout).setOnClickListener(this);
        findViewById(R.id.zhumao_layout).setOnClickListener(this);
        findViewById(R.id.zoyou_layout).setOnClickListener(this);
        findViewById(R.id.yanjing_layout).setOnClickListener(this);
        rightLayout = findViewById(R.id.add_layout);
        rightLayout.setVisibility(View.INVISIBLE);
        titleTv = findViewById(R.id.title_text);
        myroomNoTv = findViewById(R.id.myroom_no);
        titleTv.setText("竞速语音室");
        luxianIMg=findViewById(R.id.luxin_img);
        headimg=findViewById(R.id.head_img);
        roomNoEt = findViewById(R.id.room_no);
        roomNameEt = findViewById(R.id.room_name_et);
        roomPassEt = findViewById(R.id.room_pass_et);
        roompoplnumEt = findViewById(R.id.room_popo_xz_et);
        roomBgImg = findViewById(R.id.room_bg);
        passYanImg = findViewById(R.id.yanjing_img);
        zhumaiImg = findViewById(R.id.zhumai_img);
        ziyouImg = findViewById(R.id.ziyou_img);
        shangxinTv=findViewById(R.id.shangxin_tv);
        roomBgImg.setOnClickListener(this);
        roompoplnumEt.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                roompoplnum=s.toString();
            }
        });
        Picasso.with(this)
                .load(BaseRequesUrl.uesrHead)
                .config(Bitmap.Config.RGB_565)
                .centerCrop()
                .fit()
                .networkPolicy(NetworkPolicy.NO_STORE)
                .into(headimg);
        roomNameEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                roomNameStr=s.toString();

            }
        });
        roomPassEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                roomPassStr=s.toString();
            }
        });
        roomNoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                roomNoStr=s.toString();
            }
        });
        myroomNoTv.setText("房间号："+BaseRequesUrl.uesrRoomNo);
        BaseHandlerOperate.getBaseHandlerOperate().addKeyHandler(RacingvoiceHomeActivity.class,this);
        roomNUBXHNGA();
        if (roomStatus.equals("1")){
            checkPermission();
        }
    }

    private String type="1",roomPassStr="";
    private List<File> fileList;
    private Uri backUri;

    private void verifyRoom(String roomNo)
    {
        showLoading();
        verifyRoomMap=new HashMap<>();
        if (verifyRoomMap!=null)
        {
            verifyRoomMap.clear();
        }
        verifyRoomMap.put("roomNo",roomNo);
        okHttp(this,BaseRequesUrl.VerifyRoomPsd,HttpTagUtil.VerifyRoomPsd,verifyRoomMap);
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
        if (addRoomMap != null){
            addRoomMap.clear();
        }
        addRoomMap.put("userId", preference.getUserId());
        addRoomMap.put("roomNo", roomNo);
        addRoomMap.put("password", password);
        okHttp(this, BaseRequesUrl.AddRoom, HttpTagUtil.AddRoom, addRoomMap);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.luxian_ll:
                if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)){
                    requestPermission(PermissionsConstans.LOCATION_CODE,Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                }else {
                    doDINGWEIPermission();
                }
                break;
            case R.id.goRoombut:
                if(!TextUtils.isEmpty(roomNoStr)){
                    verifyRoom(roomNoStr);
                }else {
                    showTip("房间号不能为空");
                }
                break;
            case R.id.room_bg:
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    doSDCardPermission();
                }else {
                    requestPermission(PermissionsConstans.WRITE_STORAGE_CODE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                break;
            case R.id.zhumao_layout:
                if (!zhumao)
                {
                    zhumao = true;
                    type="1";
                    zhumaiImg.setBackgroundResource(R.mipmap.yys_select);
                    ziyouImg.setBackgroundResource(R.mipmap.yys_noselect);
                }
                break;
            case R.id.zoyou_layout:
                if (zhumao) {
                    zhumao = false;
                    type="2";
                    zhumaiImg.setBackgroundResource(R.mipmap.yys_noselect);
                    ziyouImg.setBackgroundResource(R.mipmap.yys_select);
                }
                break;
            case R.id.yanjing_layout:
                if (passGone) {
                    passGone = false;
                    roomPassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passYanImg.setBackgroundResource(R.mipmap.yys_eye);
                } else {
                    passGone = true;
                    roomPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passYanImg.setBackgroundResource(R.mipmap.yys_zye);
                }
                roomPassEt.setSelection(roomPassEt.getText().length());
                break;
            case R.id.kaiqi_layout:
                if ( !TextUtils.isEmpty(roomNameStr)) {
                    if (roomXqBean!=null){
                        if (backUri != null) {
                            if (!TextUtils.isEmpty(roompoplnum)){
                                int x=Integer.valueOf(roompoplnum);
                                if (x<=roomshax)
                                {
                                    checkPermission();
                                }else {
                                    prompt("房间人数超过上限");
                                }
                            }else {
                                prompt("请输入房间人数");
                            }
                        }else {
                            prompt("请加去房间图片");
                        }
                    }else {
                        prompt("团队路线未规划");
                    }
                } else {
                    prompt("房间名字与背景不能为空");
                }
                break;
        }
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        dismissLoading();
        switch (mTag){
            case HttpTagUtil.VerifyRoomPsd:
                if (isHttp&&data!=null){
                    if (data.equals("1")){
                        showInputDialog();
                    }else {
                        jionRoom(roomNoStr,"");
                    }
                }
                break;
            case HttpTagUtil.AddRoom:
                if (isHttp&&data != null){
                    if (!TextUtils.isEmpty(roomNoStr)) {
                        Intent intent = new Intent(this, VoiceHomeActivity.class);
                        intent.putExtra("roomId", roomNoStr);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            case HttpTagUtil.ROOMPOPOMUB:
                if(data!=null){

                    String mub=data.toString();
                    if (!TextUtils.isEmpty(mub)) {
                        shangxinTv.setText("(上限"+mub+"人)");
                        roompoplnum=mub;
                        roomshax=Integer.valueOf(mub);
                        roompoplnumEt.setText(mub);

                    }
                }
                break;

        }
    }

    private void starVoice()
    {
        Intent intent = new Intent(RacingvoiceHomeActivity.this, VoiceHomeActivity.class);
        intent.putExtra("roomId", BaseRequesUrl.uesrRoomNo);
        startActivity(intent);
        finish();
    }

    @Override
    protected void doDINGWEIPermission()
    {
        Intent i = new Intent(this, RoutePlanningActivity.class);
        i.putExtra("type","1");
        i.putExtra("roomId",BaseRequesUrl.uesrRoomNo);
        i.putExtra("roomName","sasdasd");
        startActivityForResult(i, HttpTagUtil.AllAddress);
    }

    @Override
    protected void doSDCardPermission()
    {
        super.doSDCardPermission();
        goToAlbum();
    }

    private void showInputDialog()
    {
        final InputDialog inputDialog = new InputDialog(this);
        inputDialog.setTitle("房间密码");
        inputDialog.setOnbutClion(new InputDialog.OnbutClion() {
            @Override
            public void onokclick(String inputstr) {
                if (!TextUtils.isEmpty(inputstr)){
                   jionRoom(roomNoStr,inputstr);
                   inputDialog.dismiss();
                }else {
                    showTip("房间密码不能为空");
                }

            }

            @Override
            public void onFinshclick() {

            }
        });
        inputDialog.show();
    }

    /**
     * 打开相册
     */
    private void goToAlbum()
    {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))//选择mime的类型
                .maxSelectable(1)//选择图片的最大数量限制
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//屏幕显示方向
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new PicassoEngine()) // 使用的图片加载引擎
                .theme(R.style.Matisse_Dracula) // 黑色背景
                .forResult(HttpTagUtil.REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }

    /**
     * 获取房间人数上限
     */
    private void roomNUBXHNGA()
    {
        Map<String,Object> httpMap=new ArrayMap<>();
        httpMap.put("userId",BaseRequesUrl.uesrId);
        okHttp(this,BaseRequesUrl.RoompopuMub,HttpTagUtil.ROOMPOPOMUB,httpMap);
    }
    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        if (requestCode == HttpTagUtil.REQUEST_CODE_CHOOSE ) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
            backUri=mSelected.get(0);
            Picasso.with(this)
                    .load(mSelected.get(0))
                    .config(Bitmap.Config.RGB_565)
                    .resize(120, 120)
                    .centerCrop()
                    .into(roomBgImg);
        }
        if (requestCode==HttpTagUtil.AllAddress)
        {
            roomXqBean=data.getParcelableExtra("aaa");
            city=data.getStringExtra("city");
            if (roomXqBean!=null){
                luxianIMg.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void requestComplete(Integer tag, Object result, String msg, boolean complete)
    {
        dismissLoading();
        if (!complete)
        {
            showTip(msg);
        }
        switch (tag){
            case HttpTagUtil.OPPENROOM:
                if (complete){
                    starVoice();
                    Log.e("aaaa", "创建成功");
                }else{
                    closeRoom(BaseRequesUrl.uesrRoomNo);
                }
                break;


        }
    }

    /**
     * 关闭房间
     * @param roomNo
     */
    private void closeRoom(String roomNo)
    {
        Map<String,Object> closeRoomMap=new HashMap<>();
        closeRoomMap.put("roomNo",roomNo);
        okHttp(this,BaseRequesUrl.CloseRoom,HttpTagUtil.CloseRoom,closeRoomMap);
    }
    @Override
    public void onCompressStart()
    {

    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult imageOutPath)
    {
        Log.d("release", "onCompressEnd outPath:" + imageOutPath.getOutPath());
        if (imageOutPath.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR)//压缩失败
            return;
        File file = new File(imageOutPath.getOutPath());
        if (fileList==null){
            fileList=new ArrayList<>();
        }else {
            fileList.clear();
        }
        fileList.add(file);
        httpcraateRoom();
    }

    private void httpcraateRoom()
    {
        String ispassword = "0";
        if (!TextUtils.isEmpty(roomPassStr)) {
            ispassword = "1";
        } else {
            ispassword="0";
        }
        new OppenRoomReques(this, HttpTagUtil.OPPENROOM, this).oppenroomHttp(roomNameStr
                , ispassword, roomPassStr, type, roompoplnum, city
                , roomXqBean.getStartAddress()
                , roomXqBean.getEndAddress()
                , roomXqBean.getSetTime()
                , roomXqBean.getRemark()
                , roomXqBean.getEndAddress()
                , roomXqBean.getLongitudeGather()
                , roomXqBean.getLatitudeGather()
                , roomXqBean.getLongitudeStart()
                , roomXqBean.getLatitudeStart()
                , roomXqBean.getLongitudeDestination()
                , roomXqBean.getLatitudeDestination()
                , fileList);
    }

    private  NimVoicerImpl nimVoicer;

    //权限认证通过
    private void onPermissionChecked()
    {

        Log.e("aaaa","--------------------chuangjain");
        showLoading();
        // 启动音频
        if (nimVoicer==null)
        {
            nimVoicer=NimVoicerImpl.getInstance();
        }
        BaseRequesUrl.XroomNo=BaseRequesUrl.uesrRoomNo;
        nimVoicer.onCreateRoom(BaseRequesUrl.uesrRoomNo);
    }

    /**
     * ************************************ 权限检查 ***************************************
     */
    private void checkPermission()
    {
        List<String> lackPermissions = AVChatManager.getInstance().checkPermission(RacingvoiceHomeActivity.this);
        if (lackPermissions.isEmpty())
        {
            onBasicPermissionSuccess();

        } else {
            String[] permissions = new String[lackPermissions.size()];
            for (int i = 0; i < lackPermissions.size(); i++) {
                permissions[i] = lackPermissions.get(i);
            }
            MPermission.with(RacingvoiceHomeActivity.this)
                    .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                    .permissions(permissions)
                    .request();
        }
    }

    private static final int BASIC_PERMISSION_REQUEST_CODE = 0x100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess()
    {
        onPermissionChecked();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed()
    {
        Toast.makeText(this, "音视频通话所需权限未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
//        onPermissionChecked();
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what){
            case HttpTagUtil.OPPENROOM:
                Bundle data = msg.getData();
                String join=data.getString("join");
                if (join.equals("0")){
                    if (roomStatus.equals("0")) {
                        creaeHoom();
                    }else {
                        starVoice();
                    }
                }else {
                    dismissLoading();
                    String code=data.getString("code");
                    if (!TextUtils.isEmpty(code))
                    {
                        if (code.equals("417"))
                        {
                            if (roomStatus.equals("0")) {
                                creaeHoom();
                            }else {
                                starVoice();
                            }
                        }else {
                            prompt("创建房间失败:"+code);
                        }
                    }

                }
                break;
        }
    }

    private void creaeHoom()
    {
        if (backUri==null){
            if (fileList==null){
                fileList=new ArrayList<>();
            }else{
                fileList.clear();
            }
            httpcraateRoom();
        }else {
            LGImgCompressor.getInstance(this).withListener(this).
                    starCompress(backUri.toString(), 600, 800, 500);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        BaseHandlerOperate.getBaseHandlerOperate().removeKeyData(RacingvoiceHomeActivity.class);
    }
}
