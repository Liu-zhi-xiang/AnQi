package com.motorbike.anqi.nim.voiceroom;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.motorbike.anqi.activity.voice.RacingvoiceHomeActivity;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.nim.DemoCache;
import com.motorbike.anqi.service.MyVoiceService;
import com.motorbike.anqi.util.AudioManagerHandle;
import com.motorbike.anqi.util.LogUtil;
import com.motorbike.anqi.util.UserPreference;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;

/**
 * @author lzx
 * @date 2018/5/9
 * @info 语音室功能实现类
 */

public class NimVoicerImpl {

    private NimVoicerImpl() {
        userPreference = UserPreference.getUserPreference(DemoCache.getContext());
    }

    private static UserPreference userPreference = null;
    private static volatile NimVoicerImpl nimVoicer = null;

    public static NimVoicerImpl getInstance() {
        if (nimVoicer == null) {
            synchronized (NimVoicerImpl.class) {
                if (nimVoicer == null) {
                    nimVoicer = new NimVoicerImpl();
                }
            }
        }

        return nimVoicer;
    }

    /*
    ****************************** 登录登出 ******************************
   */

    //网易云手动登录
    private boolean Login_g = false;

    public boolean doLogin() {

        String tocken = userPreference.getToken();
        if (TextUtils.isEmpty(tocken) || TextUtils.isEmpty(userPreference.getAccid())) {
            return false;
        }
        if (!Login_g) {
            Login_g = true;
            login(new LoginInfo(userPreference.getAccid(), tocken), null);
        }
        return true;
    }

    public AbortableFuture<LoginInfo> login(LoginInfo loginInfo, final RequestCallback<LoginInfo> callback) {

        AbortableFuture<LoginInfo> loginRequest = NIMClient.getService(AuthService.class).login(loginInfo);

        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                BaseRequesUrl.wangyiServer = true;
                Login_g = false;
                Log.e("NimVoicer", loginInfo.getAccount() + "=success");
                if (callback != null)
                    callback.onSuccess(loginInfo);
            }

            @Override
            public void onFailed(int code) {
                if (code == -1) {
                    BaseRequesUrl.wangyiServer = false;
                }
                Login_g = false;
                if (callback != null)
                    callback.onFailed(code);
            }

            @Override
            public void onException(Throwable exception) {
                BaseRequesUrl.wangyiServer = false;
                Login_g = false;
                if (callback != null)
                    callback.onException(exception);
            }
        });
        return loginRequest;
    }


    //选账号创建
    public void onCreateRoom(final String roomName) {
        if (!BaseRequesUrl.wangyiServer) {
            Toast.makeText(DemoCache.getContext(), "语音初始化错误", Toast.LENGTH_SHORT).show();
            return;
        }
//        final String roomName = StringUtil.get32UUID();
        LogUtil.ui("create room " + roomName);
        // 创建房间

        AVChatManager.getInstance().createRoom(roomName, null, new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                LogUtil.i("startRtc", "create room success, roomId=" + roomName + ", chatId=" + avChatChannelInfo.getTimetagMs());
                //去加入房间
                BaseRequesUrl.XroomNo = BaseRequesUrl.uesrRoomNo;
//                    startRtc(true,null,roomName);
                Bundle bundle = new Bundle();
                bundle.putString("join", "0");
                bundle.putString("code", "0");
                BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(RacingvoiceHomeActivity.class, HttpTagUtil.OPPENROOM, bundle);
            }

            @Override
            public void onFailed(int code) {
                Bundle bundle = new Bundle();
                if (code == 417) {
                    BaseRequesUrl.XroomNo = BaseRequesUrl.uesrRoomNo;
                }
                bundle.putString("join", "1");
                bundle.putString("code", code + "");
                BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(RacingvoiceHomeActivity.class, HttpTagUtil.OPPENROOM, bundle);
                LogUtil.i("startRtc", "create room failed, code=" + code + ", roomId=" + roomName);
            }

            @Override
            public void onException(Throwable exception) {
                LogUtil.i("startRtc", "create room failed, e=" + exception.getMessage() + ", roomId=" + roomName);
            }
        });

    }

    /**
     * ************************************ 音视频事件 ***************************************
     */
    //加入房间
    public void startRtc(boolean a, final AVChatStateObserverLite observer, final String roomName) {

        if (!BaseRequesUrl.wangyiServer) {
            Toast.makeText(DemoCache.getContext(), "语音初始化错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (BaseRequesUrl.roomNmID.equals(roomName)) {
            LogUtil.i("startRtc", "你已经在房间里了, roomName=" + roomName);
        } else {
            // rtc init
            //开启音视频引擎
            boolean b = AVChatManager.getInstance().enableRtc();
            LogUtil.i("startRtc", "start rtc done" + b);
            // state observer
            if (observer != null) {
                AVChatManager.getInstance().observeAVChatState(observer, true);
            }
            // join
            if (a) {
                //普通模式
                AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
            } else {
                //观众模式
                AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.AUDIENCE);
            }
            Log.e("startRtc", "join room" + roomName);
            AVChatManager.getInstance().setParameter(AVChatParameters.KEY_AUDIO_REPORT_SPEAKER, true);
            AVChatManager.getInstance().joinRoom2(roomName, AVChatType.AUDIO, new AVChatCallback<AVChatData>() {
                @Override
                public void onSuccess(AVChatData data) {
                    long chatId = data.getChatId();
                    AudioManager mAudioManager = AudioManagerHandle.savemode(DemoCache.getContext());
                    //连接耳机不开扬声器
//                    if (!((AudioManager) DemoCache.getContext().getSystemService(Context.AUDIO_SERVICE)).isBluetoothA2dpOn()) {
//
//                    }else
                    if (mAudioManager.isBluetoothScoOn() || mAudioManager.isBluetoothA2dpOn()) {
                        Log.e("Audio", "蓝牙耳机已链接");
                        mAudioManager.setSpeakerphoneOn(false);
                        mAudioManager.setBluetoothA2dpOn(true);
                    } else {
                        offYSQ(true);//打开扬声器
                    }
                    BaseRequesUrl.roomNmID = BaseRequesUrl.XroomNo;
                    userPreference.setRoomNoCurrent(BaseRequesUrl.roomNmID);
                    userPreference.save();
                    Bundle bundle = new Bundle();
                    bundle.putString("join", "0");
                    bundle.putString("code", "0");
                    BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.TalkRoom, bundle);
                    LogUtil.i("startRtc", "join room success, roomId=" + roomName + ", chatId=" + chatId);
                }

                @Override
                public void onFailed(int code) {
                    BaseRequesUrl.roomNmID = "";
                    Bundle bundle = new Bundle();
                    bundle.putString("join", "1");
                    bundle.putString("code", code + "");
                    if (observer != null) {
                        AVChatManager.getInstance().observeAVChatState(observer, false);
                    }
                    BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.TalkRoom, bundle);
                    LogUtil.i("startRtc", "join room failed, code=" + code + ", roomId=" + roomName);
                }

                @Override
                public void onException(Throwable exception) {
                    BaseRequesUrl.roomNmID = "";
                    if (observer != null) {
                        AVChatManager.getInstance().observeAVChatState(observer, false);
                    }
                    LogUtil.i("startRtc", "join room failed, e=" + exception.getMessage() + ", roomId=" + roomName);
                }
            });
            LogUtil.i("startRtc", "start join room, roomId=" + roomName);
        }
    }

    /**
     * 设置静音是设置本地音频，也不发送本地音频数据。
     *
     * @param {@code true} 静音本地语音，{@code false} 取消本地语音静音
     */
    public void jingyin(boolean a) {
        //关闭语音发送.
        AVChatManager.getInstance().muteLocalAudio(a);
    }

    /**
     * 检查麦克风是否静音
     *
     * @return 麦克风是否静音
     */
    public boolean isMicrophoneMute() {
        return AVChatManager.getInstance().isMicrophoneMute();
    }

    /**
     * 指定某用户设置是否对其静音
     *
     * @param account 用户账号
     * @param ms      true 不解码语音流数据；false 解码语音流数据。
     */
    public void MaskSound(String account, boolean ms) {
        AVChatManager.getInstance().muteRemoteAudio(account, ms);
    }

    /**
     * @param no {@code true} 静音麦克风语音，{@code false} 取消麦克风静音
     */
    public void offMkf(boolean no) {
//        boolean isMute = AVChatManager.getInstance().isMicrophoneMute();
        AVChatManager.getInstance().setMicrophoneMute(no);
    }

    /**
     * 是否使用扬声器
     *
     * @param enable {@code true} 打开扬声器，{@code false} 关闭扬声器
     */
    public void offYSQ(boolean enable) {
        AVChatManager.getInstance().setSpeaker(enable);
        Log.e("mian", "打开扬声器");
    }

    //退出房间
    public void hangupRoom(final String roomName) {
        hangupRoom(roomName, true);
    }

    /**
     * 退出房间
     *
     * @param roomName
     * @param jk       清空加入房间记录
     */
    public void hangupRoom(String roomName, boolean jk) {
        try {
            AVChatManager.getInstance().leaveRoom2(roomName, null);
            AVChatManager.getInstance().observeAVChatState(null, false);
            AVChatManager.getInstance().disableRtc();
            BaseRequesUrl.jingyin = "1";
            BaseRequesUrl.roomNmID = "";
            Intent intent = new Intent(DemoCache.getContext(), MyVoiceService.class);

            DemoCache.getContext().stopService(intent);
            Log.e("ceshi", "stopService");
            if (jk) {
                userPreference.setRoomNoCurrent("");
                userPreference.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
