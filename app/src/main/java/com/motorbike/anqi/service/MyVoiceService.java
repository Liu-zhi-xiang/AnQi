package com.motorbike.anqi.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.bean.RoomUserBean;
import com.motorbike.anqi.bean.UserListBean;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.init.VoiceCheyouReques;
import com.motorbike.anqi.interfaces.HttpRequestTag;
import com.motorbike.anqi.nim.DemoCache;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.util.AudioManagerHandle;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;

import java.util.List;
import java.util.TimerTask;

/**
 * 语音室服务（监听语音室变动）
 * */
public class MyVoiceService extends Service {

    private int x=0;
    private Handler handlerDingshi;

    private  Runnable runnable =new Runnable(){
        @Override
        public void run(){
            // TODO Auto-generated method stub
            //
            Log.e("aaaa","定时器");
            if (TextUtils.isEmpty(roomNo)){
                onDestroy();
                return;
            }
            new VoiceCheyouReques(DemoCache.getContext(), HttpTagUtil.DynamicRoomUser, new HttpRequestTag() {
                @Override
                public void requestComplete(Integer tag, Object result, String msg, boolean complete) {
                    if (complete) {
                        if (result != null) {
                            String data = (String) result;
                            if (data != null) {
                                RoomUserBean roomUserBean = new Gson().fromJson(data, RoomUserBean.class);
                                setdata(roomUserBean,data);
                            }
                        }
                    }
                }
            }).addPoicomment(roomNo);
            if (handlerDingshi!=null)
            {
                handlerDingshi.postDelayed(runnable, 3000);
            }
        }
    };
    public MyVoiceService()
    {

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        handlerDingshi=new Handler();

    }
    private  String roomNo,leaderId;
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if (handlerDingshi==null){
            handlerDingshi=new Handler();
        }
        try {
            roomNo = intent.getStringExtra("roomNo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        handlerDingshi.removeCallbacks(runnable);
        handlerDingshi.postDelayed(runnable, 3000);//每三秒执行一次runnable.
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public boolean onUnbind(Intent intent)

    {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (!TextUtils.isEmpty(BaseRequesUrl.roomNmID)){
            BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(MainActivityTwo.class, HttpTagUtil.TalkRoom,"1");
        }
        if (handlerDingshi!=null)
        {
            Log.e("ceshi","handlerDingshi...removeCallbacks");
            handlerDingshi.removeCallbacks(runnable);
            handlerDingshi=null;
        }


    }
    private String jingyn="1";
    private void setdata(RoomUserBean roomUserBean,String data)
    {
        if (roomUserBean != null) {
            leaderId = roomUserBean.getLeaderId();
            List<UserListBean> userListBeanList = roomUserBean.getUserList();
            boolean zai=false;
            if (userListBeanList != null && userListBeanList.size() > 0) {
                int size=userListBeanList.size();
                if (size!=x) {
                    //人数变动，通知刷新页面
                    BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.CarFriendList, data);
                    x = size;
                }
                for (int x=0;x<size;x++){
                    UserListBean userListBean = userListBeanList.get(x);
                    String suserId=userListBean.getUserId();
                    BaseRequesUrl.RoomGL=userListBean.getIsAdmin();
                    // 静音模式
                    if (suserId.equals(BaseRequesUrl.uesrId)){
                        //1主麦模式 2 自由模式
                        zai=true;
                        if (!BaseRequesUrl.uesrId.equals(leaderId)){
                            /**
                             * 当前是否处于观众角色.
                             * <p>角色设置仅在多人会话时生效. </p>
                             *
                             * @return {@code true} 观众角色模式，{@code false} 正常用户模式
                             */
                            boolean guanzhong= AVChatManager.getInstance().isAudienceRole();
                            if (roomUserBean.getMicrType().equals("1")){
                                if (userListBean.getIsAdmin().equals("1")){
                                        if (guanzhong) {
                                            AVChatManager.getInstance().enableAudienceRole(false);
                                        }
                                } else {
                                    Log.e("aaaa", "设置观众模式");
                                    if (!guanzhong){
                                        AVChatManager.getInstance().enableAudienceRole(true);
                                    }
                                }
                            }else {
                                if (userListBean.getMicrophone().equals("1")) {
                                    if (guanzhong){
                                        AVChatManager.getInstance().enableAudienceRole(false);
                                    }
                                } else {
                                    //观众模式
                                    Log.e("aaaa", "设置观众模式");
                                    if (!guanzhong) {
                                        AVChatManager.getInstance().enableAudienceRole(true);
                                    }
                                }
                            }
                        }else {
                            AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
                        }
                        Bundle bundle=new Bundle();
                        bundle.putString("micrType",roomUserBean.getMicrType());
                        bundle.putString("isAdmin",userListBean.getIsAdmin());
                        bundle.putString("microphone",userListBean.getMicrophone());
                        BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.DynamicRoomUser, bundle);
                    }else {
                        //静音
                        if (!BaseRequesUrl.jingyin.equals(jingyn)){
                            if (BaseRequesUrl.jingyin.equals("0")) {
                                Log.e("aaaaa", "其他静音" + userListBean.getAccid());
                                NimVoicerImpl.getInstance().MaskSound(userListBean.getAccid(), true);
                            } else {
                                Log.e("aaaaa", "其他 laba" + userListBean.getAccid());
                                NimVoicerImpl.getInstance().MaskSound(userListBean.getAccid(), false);
                            }
                        }
                    }

                }
               jingyn=BaseRequesUrl.jingyin;
            }
            if (!zai)
            {
                BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.CloseRoom, "0");
                NimVoicerImpl.getInstance().hangupRoom(roomNo);
                onDestroy();
            }

        }


    }

}
