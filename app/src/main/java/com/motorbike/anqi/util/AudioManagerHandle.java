package com.motorbike.anqi.util;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.motorbike.anqi.init.BaseRequesUrl;

/**
 * @author lzx
 * @date 2019/1/15
 * @info
 */
public class AudioManagerHandle {

    public static void mode(Context context)
    {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        logAudio(mAudioManager);
        if (mAudioManager.isBluetoothScoAvailableOffCall()) {
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            mAudioManager.setBluetoothA2dpOn(true);
            mAudioManager.setSpeakerphoneOn(false);
            Log.e("main", "切换到蓝牙耳机");
        }

    }
    public static void startSCO(Context context)
    {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        logAudio(mAudioManager);
        if (mAudioManager.isBluetoothScoAvailableOffCall()) {
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            mAudioManager.startBluetoothSco();
            mAudioManager.setBluetoothScoOn(true);
            mAudioManager.setBluetoothA2dpOn(false);
            mAudioManager.setSpeakerphoneOn(false);
            Log.e("main", "切换到蓝牙耳机");
        }
    }

    public static AudioManager savemode(Context context){
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //音频输出设备处理
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            AudioDeviceInfo[] audioDeviceInfos= mAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
//            if (audioDeviceInfos!=null) {
//                for (int j = 0; j < audioDeviceInfos.length; j++) {
//                    AudioDeviceInfo  audioDeviceInfop =audioDeviceInfos[j];
//
//                    Log.e("Audio","id="+audioDeviceInfop.getId()
//                            +"=type==="+audioDeviceInfop.getType()
//                            +"=name=="+audioDeviceInfop.getProductName()
//                            +"=isSink=="+audioDeviceInfop.isSink()
//                    );
//                }
//            }
//        }
        int mode= mAudioManager.getMode();
        int RingerMode= mAudioManager.getRingerMode();
        if (!BaseRequesUrl.mode){
            UserPreference userPreference=UserPreference.getUserPreference(context);
            userPreference.setMode(mode);
            userPreference.setRingmode(RingerMode);
            BaseRequesUrl.mode=true;
            userPreference.save();
        }
        logAudio(mAudioManager);
        return mAudioManager;
    }

    public static void setTSQ(Context context)
    {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setSpeakerphoneOn(true);
        mAudioManager.setBluetoothA2dpOn(false);
        logAudio(mAudioManager);

    }

    public static void stopSDC(Context context)
    {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        UserPreference userPreference=UserPreference.getUserPreference(context);
        mAudioManager.setMode(userPreference.getMode());
        mAudioManager.setRingerMode(userPreference.getRingmode());
        if (mAudioManager.isBluetoothScoOn())
        {
            mAudioManager.stopBluetoothSco();
        }
    }
    private static void logAudio(AudioManager mAudioManager )
    {
        if (mAudioManager==null){
            return;
        }
        int mode = mAudioManager.getMode();
        int RingerMode = mAudioManager.getRingerMode();
        Log.e("main", "mode=" + mode);
        Log.e("main", "RingerMode=" + RingerMode);
        if (mAudioManager.isSpeakerphoneOn()) {
            Log.e("main", "isSpeakerphoneOn");
            // Adjust output for Speakerphone.
        }
        if (mAudioManager.isBluetoothA2dpOn()) {
            Log.e("main", "isBluetoothA2dpOn");
            // Adjust output for Bluetooth.
        }
        if (mAudioManager.isWiredHeadsetOn()) {
            Log.e("main", "isWiredHeadsetOn");
            // Adjust output for headsets
        }

        if (mAudioManager.isBluetoothScoOn()) {
            Log.e("main", "isBluetoothScoOn");
            // If audio plays and noone can hear it, is it still playing?
        }
    }
}
