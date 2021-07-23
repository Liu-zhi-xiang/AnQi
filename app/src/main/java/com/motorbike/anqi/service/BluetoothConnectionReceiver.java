package com.motorbike.anqi.service;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.util.AudioManagerHandle;
import com.motorbike.anqi.util.JcLog;
import com.netease.nimlib.sdk.avchat.AVChatManager;

/**
 * @author lzx
 * @date 2018/11/30
 * @info
 * Ble蓝牙连接监听
 */
public class BluetoothConnectionReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String blesen = intent.getAction();

        if (blesen.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
            //连接状态
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1);
            switch (state) {
                case BluetoothAdapter.STATE_CONNECTED:
                    Log.e("ble", "蓝牙连接");
                    if (TextUtils.isEmpty(BaseRequesUrl.roomNmID)){
                        break;
                    }
                    AudioManagerHandle.mode(context);
                    break;
                case BluetoothAdapter.STATE_DISCONNECTED:
                    //断开，切换音频输出
                    Log.e("ble", "蓝牙断开");
                    if (!TextUtils.isEmpty(BaseRequesUrl.roomNmID)) {
                        AudioManagerHandle.setTSQ(context);
                        AVChatManager.getInstance().setSpeaker(true);
                    }
                    break;
                default:
                    break;
            }
        } else if (blesen.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            //开关状态
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            switch (blueState) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.e("ble", "onReceive---------STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.e("ble", "onReceive---------STATE_ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.e("ble", "onReceive---------STATE_TURNING_OFF");
                    break;
                case BluetoothAdapter.STATE_OFF:
                    Log.e("ble", "onReceive---------STATE_OFF");
                    break;
                default:
                    break;
            }

        }
    }

}
