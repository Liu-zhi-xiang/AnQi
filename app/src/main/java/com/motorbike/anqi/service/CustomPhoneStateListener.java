package com.motorbike.anqi.service;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.util.UserPreference;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatAudioDevice;

/**
 * Created by Administrator on 2019/3/12.
 */

public class CustomPhoneStateListener extends PhoneStateListener {

    private Context mContext;
    private UserPreference preference;
    private int noState=0;
    private boolean isAudienceRole;
    public CustomPhoneStateListener(Context context) {
        mContext = context;
        preference=UserPreference.getUserPreference(context);
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        super.onServiceStateChanged(serviceState);
        Log.e("calljianting", "CustomPhoneStateListener onServiceStateChanged: " + serviceState);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        Log.e("calljianting", "CustomPhoneStateListener state: "
                + state + " incomingNumber: " + incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:      // 电话挂断
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
                Toast.makeText(mContext,"-挂断电话-",Toast.LENGTH_SHORT).show();
                Log.e("calljianting", "===电话挂断=== ");
                break;
            case TelephonyManager.CALL_STATE_RINGING:   // 电话响铃
                Log.e("calljianting", "===电话响铃=== ");
                Toast.makeText(mContext,"-电话响铃-",Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(preference.getRoomNoCurrent())){
                    //接通来电挂断网易云通讯
                    noState=2;
                    NimVoicerImpl.getInstance().hangupRoom(preference.getRoomNoCurrent(),false);
//                      关闭聊天页面
                    BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.Exitroom,"0");
                    Log.e("calljianting", "退出房间++++++== ");
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:   // 来电接通 或者 去电，去电接通  但是没法区分
                Log.e("calljianting", "===来电接通=== ");
                noState=1;
                if (!TextUtils.isEmpty(preference.getRoomNoCurrent())){
                    //接通来电挂断网易云通讯
                    noState=2;
                    NimVoicerImpl.getInstance().hangupRoom(preference.getRoomNoCurrent(),false);
//                      关闭聊天页面
                    BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.Exitroom,"0");
                    Log.e("calljianting", "退出房间++++++== ");
                }
                Toast.makeText(mContext,"-接听电话-",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
