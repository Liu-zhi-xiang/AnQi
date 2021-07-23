package com.motorbike.anqi.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.BaseRequesUrl;

import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.util.AudioManagerHandle;
import com.motorbike.anqi.util.UserPreference;
import com.netease.nimlib.sdk.avchat.AVChatManager;


/**
 * @author lzx
 * @date 2018/11/30
 * @info
 */
public class PhoneStateReceiver extends BroadcastReceiver {
    private String PhoneListenService = "PhoneListenService";
    private int noState=0;
    private UserPreference preference;
    private boolean isAudienceRole;
    @Override
    public void onReceive(Context context, Intent intent) {
        preference=UserPreference.getUserPreference(context);
        String action = intent.getAction();
        Log.e(PhoneListenService, "PhoneStateReceiver action: " + action);

        String resultData = this.getResultData();
        Log.e(PhoneListenService, "PhoneStateReceiver getResultData: " + resultData);
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {//去电

        Log.e(PhoneListenService, "PhoneStateReceiver 去电: ");

        } else {//来电(存在以下三种情况)
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (noState==2){
                        //尝试恢复通讯
                        if (!TextUtils.isEmpty(preference.getRoomNoCurrent())){
                            isAudienceRole=AVChatManager.getInstance().isAudienceRole();//当前是否处于观众角色(也就是主麦或者自由模式)
                            //加入房间
                            NimVoicerImpl.getInstance().startRtc(isAudienceRole,null,preference.getRoomNoCurrent());
                            Log.e("calljianting", "加入房间++++++== ");
                        }
                    }
                    noState=0;
                    Toast.makeText(context,"挂断电话",Toast.LENGTH_SHORT).show();
                    Log.e(PhoneListenService, "PhoneStateReceiver 挂断: ");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    noState=1;
                    if (!TextUtils.isEmpty(preference.getRoomNoCurrent())){
                        //接通来电挂断网易云通讯
                        noState=2;
                        NimVoicerImpl.getInstance().hangupRoom(preference.getRoomNoCurrent(),false);
//                      关闭聊天页面
                        BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.Exitroom,"0");
                        Log.e(PhoneListenService, "退出房间++++++== ");
                    }
                    Toast.makeText(context,"接听电话",Toast.LENGTH_SHORT).show();
                    Log.e(PhoneListenService, "PhoneStateReceiver 接听: ");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e(PhoneListenService, "PhoneStateReceiver 响铃: ");
                    if (!TextUtils.isEmpty(preference.getRoomNoCurrent())){
                        //接通来电挂断网易云通讯
                        noState=2;
                        NimVoicerImpl.getInstance().hangupRoom(preference.getRoomNoCurrent(),false);
//                      关闭聊天页面
                        BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.Exitroom,"0");
                        Log.e(PhoneListenService, "退出房间++++++== ");
                    }
                    Toast.makeText(context,"电话响铃",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
