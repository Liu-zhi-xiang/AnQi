package com.motorbike.anqi.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateUtils;
import android.text.format.Time;

/**
 * @author lzx
 * @date 2018/5/11
 * @info
 */

public class StatiPollMgr {
    /** 超期消息 */
    private static final int MSG_TIMEOUT = 1;

    /** 心跳周期 */
    private long mCardiacCycle;
    /** 默认心跳周期，即初始化而来的周期 */
    private long mDefaultCycle;


    public StatiPollMgr(){
    }

    /**
     * 开启心跳
     *
     * @param aCardiacCycle
     *            心跳周期
     */
    public void start(long aCardiacCycle) {
        mDefaultCycle = aCardiacCycle;
        mCardiacCycle = aCardiacCycle;
        checkDateChanging();
        stop();
        loop();
    }

    /**
     * 停止心跳
     */
    public void stop()
    {
        sPrivateHandler.get().removeMessages(MSG_TIMEOUT);
    }

    /**
     * 循环
     */
    private void loop()
    {
        Message msg = sPrivateHandler.get().obtainMessage(MSG_TIMEOUT, this);
        sPrivateHandler.get().sendMessageDelayed(msg, mCardiacCycle);
    }

    /**
     * 超期通知
     */
    public void onTimeOut() {
        // do something
    }

    /**
     * 检测将要跨天时，调整心跳周期；跨天之后，调回默认值
     */
    private void checkDateChanging() {
        Time time = new Time();
        time.setToNow();
        int hour = time.hour; //24小时制
        int minute = time.minute;
        if (hour == 23) { //SUPPRESS CHECKSTYLE
            int cycle = 61 - minute; // SUPPRESS CHECKSTYLE 12:01访问
            long timeSchedule = cycle * DateUtils.MINUTE_IN_MILLIS;
            if (timeSchedule < mCardiacCycle) {
                mCardiacCycle = timeSchedule;
            }
        } else {
            if (mCardiacCycle != mDefaultCycle) {
                mCardiacCycle = mDefaultCycle;
            }
        }
    }

    /**
     * Handler
     */
    private static final ThreadLocal<Handler> sPrivateHandler = new ThreadLocal<Handler>() {
        @Override
        protected Handler initialValue() {
            return new Handler(Looper.getMainLooper()) {

                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_TIMEOUT:
                            StatiPollMgr schedule = (StatiPollMgr) msg.obj;
                            if (schedule != null) {
                                schedule.onTimeOut();
                                schedule.checkDateChanging();
                                schedule.loop();
                            }
                            break;
                        default:
                            break;
                    }
                }

            };
        }
    };
}
