package com.motorbike.anqi.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import android.util.Log;

import androidx.annotation.Nullable;


/**
 * Created by Administrator on 2019/3/21.
 */

public class WakeUpService extends Service {

    private Handler handlerDingshi;

    private  Runnable runnable =new Runnable(){
        @Override
        public void run(){
            // TODO Auto-generated method stub
            //
            Log.e("aaaa","定时器");
            wakeUp();
            if (handlerDingshi!=null)
            {
                handlerDingshi.postDelayed(runnable, 180000);
            }
            Log.e("1111111","222222222222222222222");
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        handlerDingshi=new Handler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (handlerDingshi==null){
            handlerDingshi=new Handler();
        }
        handlerDingshi.removeCallbacks(runnable);
        handlerDingshi.postDelayed(runnable, 180000);//每三分钟执行一次runnable.
        flags = START_STICKY;
        Log.e("1111111","333333333333333333333");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handlerDingshi!=null)
        {
            handlerDingshi.removeCallbacks(runnable);
            handlerDingshi=null;
        }
        Log.e("1111111","999999999999999999999999");
    }

    private void wakeUp(){
        Log.e("1111111","1111111111111111111111111");
        PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取电源管理器对象
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.PARTIAL_WAKE_LOCK, "bright");
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        wl.acquire();
        //点亮屏幕

        KeyguardManager km= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        //得到键盘锁管理器对象
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //参数是LogCat里用的Tag
        kl.disableKeyguard();
        //解锁

        /*
         * 这里写程序的其他代码
         *
         * */

        kl.reenableKeyguard();
        //重新启用自动加锁
        wl.release();
        //释放
    }
}
