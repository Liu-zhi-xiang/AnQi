package com.motorbike.anqi.service;

import android.util.Log;

import com.motorbike.anqi.init.BaseRequesUrl;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * @author
 * @date 2019/1/14
 * @info 融云链接
 */
public class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener
{

    private static final String TAG = "connect";

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED://连接成功。
                Log.e(TAG, "连接成功");
                BaseRequesUrl.RongIMLj=true;
                break;
            case DISCONNECTED://断开连接。
                Log.e(TAG, "断开连接");
                BaseRequesUrl.RongIMLj=false;
//                    connect(NetworkRequesUrL.toKen);
                RongIM.getInstance().disconnect();//有新消息时，仍然能够收到推送通知
                break;
            case CONNECTING://连接中。
                Log.e(TAG, "连接中");

                break;
            case NETWORK_UNAVAILABLE://网络不可用。
                Log.e(TAG, "网络不可用");
                BaseRequesUrl.RongIMLj=false;
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                Log.e(TAG, "用户账户在其他设备登录，本机会被踢掉线");
                BaseRequesUrl.RongIMLj=false;
//                    connect(NetworkRequesUrL.toKen);
                break;
        }
    }
}
