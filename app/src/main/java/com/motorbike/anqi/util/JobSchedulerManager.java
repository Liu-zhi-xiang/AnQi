package com.motorbike.anqi.util;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.motorbike.anqi.service.AliveJobService;


/**JobScheduler管理类，单例模式
 * 执行系统任务
 *
 * Created by jianddongguo on 2017/7/10.
 * http://blog.csdn.net/andrexpert
 */

public class JobSchedulerManager {
    private static final int JOB_ID = 1;
    private static JobSchedulerManager mJobManager;
    private JobScheduler mJobScheduler;
    private static Context mContext;

    private JobSchedulerManager(Context ctxt){
        this.mContext = ctxt;
        mJobScheduler = (JobScheduler)ctxt.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public final static JobSchedulerManager getJobSchedulerInstance(Context ctxt){
        if(mJobManager == null){
            mJobManager = new JobSchedulerManager(ctxt);
        }
        return mJobManager;
    }

    @TargetApi(21)
    public void startJobScheduler(){
        Log.d("11111"," 11111111111111");
        // 如果JobService已经启动或API<21，返回
        if(AliveJobService.isJobServiceAlive() || isBelowLOLLIPOP()){
            Log.d("11111","22222222222222222");
            return;
        }
        // 构建JobInfo对象，传递给JobSchedulerService
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,new ComponentName(mContext, AliveJobService.class));

        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); //设置需要的网络条件，默认NETWORK_TYPE_NONE
        // //设置间隔时间，不断的触发任务的启动
//        builder.setPeriodic(3000);
        // 设置任务运行最少延迟时间，与setPeriodic相似，只是间隔时间不确定，不能与setPeriodic一起使用，
        builder.setMinimumLatency(1000);
        builder.setPersisted(true);// //设备重启之后你的任务是否还要继续执行
        builder.setRequiresCharging(true);// 当插入充电器，执行该任务
        builder.setRequiresDeviceIdle(false);// 设置手机是否空闲的条件,默认false
        JobInfo info = builder.build();
        //开始定时执行该系统任务
        mJobScheduler.schedule(info);
    }

    @TargetApi(21)
    public void stopJobScheduler(){
        if(isBelowLOLLIPOP())
            return;
        mJobScheduler.cancelAll();
    }

    private boolean isBelowLOLLIPOP(){
        // API< 21
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
