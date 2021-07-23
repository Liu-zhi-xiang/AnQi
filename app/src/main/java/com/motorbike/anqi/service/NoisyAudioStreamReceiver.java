package com.motorbike.anqi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.util.AudioManagerHandle;
import com.motorbike.anqi.util.UserPreference;

/**
 * @author lzx
 * @date 2019/1/14
 * @info
 * 音频输出设备监听(耳机插拔)
 */
public class NoisyAudioStreamReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
//        ACTION_HEADSET_PLUG
//        ACTION_HDMI_AUDIO_PLUG
//        ACTION_MICROPHONE_MUTE_CHANGED
        String action=intent.getAction();

        Log.e("Audio","state====="+action);
        AudioManager mAudioManager =  AudioManagerHandle.savemode(context);
        if (AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED.equals(intent.getAction())){
            if (mAudioManager.isBluetoothScoOn()||mAudioManager.isBluetoothA2dpOn()){
                Log.e("Audio","蓝牙耳机已链接");
                mAudioManager.setSpeakerphoneOn(false);
                mAudioManager.setBluetoothA2dpOn(true);
//                mAudioManager.setBluetoothScoOn(true);
//                mAudioManager.startBluetoothSco();
            }else {
                mAudioManager.stopBluetoothSco();
                Log.e("Audio","蓝牙耳机未链接");
            }
        }else if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {


        }
    }
}


