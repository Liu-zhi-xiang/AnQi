package com.motorbike.anqi.broadcast;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;



import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.activity.my.MessageCenterActivity;
import com.motorbike.anqi.activity.trip.TripSpeedActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.util.Logger;
import com.motorbike.anqi.util.SystemUtil;
import com.motorbike.anqi.util.UserPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @author lzx
 * @date 2017/11/28
 * @info
 */

public class MyJpuseReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private String type = "", roomNo = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        try {
            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            for (String key : bundle.keySet()) {
                if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        Logger.i(TAG, "This message has no Extra data");
                        continue;
                    }
                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it = json.keys();
                        while (it.hasNext()) {
                            String myKey = it.next();
                            if (myKey.equals("type")) {
                                type = json.optString(myKey);
                            } else if (myKey.equals("roomNo")) {
                                roomNo = json.optString(myKey);
                                BaseRequesUrl.roomNo = roomNo;
                            }
                            Log.e("nnn", type + "  type");
                            Log.e("nnn", roomNo + "  roomNo");
                            if (!TextUtils.isEmpty(roomNo)) {
                                BaseRequesUrl.roomNo = roomNo;
                            }
                        }
                    } catch (JSONException e) {
                        Logger.e(TAG, "Get message extra JSON error!");
                    }

                } else {

                }
            }

            Log.e("nnn", BaseRequesUrl.roomNo + "  BaseRequesUrl.roomNo");
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

                UserPreference userPreference = UserPreference.getUserPreference(context);
                userPreference.setJpushId(regId);
                BaseRequesUrl.jpushId = regId;
                userPreference.save();
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));


            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");


                //判断app进程是否存活
                if (SystemUtil.isAppAlive(context, "com.motorbike.anqi")) {
                    //如果存活的话，MessagesActivity，但要考虑一种情况，就是app的进程虽然仍然在
                    //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
                    //MessagesActivity,再按Back键就不会返回MainActivity了。所以在启动
                    //MessagesActivity，要先启动MainActivity。
                    Intent intentone = new Intent(context, MainActivityTwo.class);
                    ComponentName cmpName = intentone.resolveActivity(context.getPackageManager());
                    boolean bIsExist = false;
                    if (cmpName != null) { // 说明系统中存在这个activity
                        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                        List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
//					logi(TAG, "---startAndExit---taskInfoList.size:" + taskInfoList.size());
                        for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {

                            if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                                bIsExist = true;
                                Log.e("aaaaaaa", "qidun");
                                break;
                            } else {
                                bIsExist = false;
                                Log.e("aaaaaaa", "gunabi");
                            }
                        }
                    }
                    if (bIsExist) {
                        if (!TextUtils.isEmpty(type)) {
                            if (type.equals("4")) {
                                Intent intenttwo = new Intent(context, TripSpeedActivity.class);
                                intenttwo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intenttwo.putExtra("roomNum", roomNo);
                                intenttwo.putExtra("runing","1");
                                context.startActivity(intenttwo);
                            } else {
                                Intent intenttwo = new Intent(context, MessageCenterActivity.class);
                                Bundle bundletwo = new Bundle();

                                intenttwo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intenttwo.putExtras(bundletwo);
                                context.startActivity(intenttwo);
                            }
                        }

                    } else {
                        if (!TextUtils.isEmpty(type)) {
                            if (type.equals("4")) {
                                Intent mainIntent = new Intent(context, MainActivityTwo.class);
                                //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
                                //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
                                //如果Task栈不存在MainActivity实例，则在栈顶创建
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Intent detailIntent = new Intent(context, TripSpeedActivity.class);
                                Bundle bundletwo = new Bundle();

                                detailIntent.putExtras(bundletwo);
                                detailIntent.putExtra("roomNum", roomNo);
                                detailIntent.putExtra("runing","1");
                                Intent[] intents = {mainIntent, detailIntent};
                                context.startActivities(intents);
                            } else {
                                Intent mainIntent = new Intent(context, MainActivityTwo.class);
                                //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
                                //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
                                //如果Task栈不存在MainActivity实例，则在栈顶创建
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Intent detailIntent = new Intent(context, MessageCenterActivity.class);
                                Bundle bundletwo = new Bundle();

                                detailIntent.putExtras(bundletwo);
                                Intent[] intents = {mainIntent, detailIntent};
                                context.startActivities(intents);
                            }
                        }

                    }
                } else {
                    //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
                    //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入             //参数跳转到DetailActivity中去了
                    Log.i("NotificationReceiver", "the app process is dead");
//				Intent launchIntent = context.getPackageManager().
//						getLaunchIntentForPackage("com.tenhospital.shanghaihospital");
//				launchIntent.setFlags(
//						Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//				Bundle args = new Bundle();
//				args.putString("type", "2");
//				launchIntent.putExtra(BaseRequesUrL.EXTRA_BUNDLE, args);
//				context.startActivity(launchIntent);
                }


            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();

                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                        if (myKey.equals("type")) {

                        }
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


}
