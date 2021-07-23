package com.motorbike.anqi.util;


import io.rong.common.FileUtils;

/**
 * I/O write 方式打日志。支持多进程同时读写，不丢日志，写入效率略低。
 * <p>
 * Created by huangjun on 2017/3/8.
 */
public class NLogImpl extends LogBase {

    private static final String TAG = "Log";

    @Override
    void open(final boolean shrink) {
        if (shrink) {
            FileUtil.shrink(logPath, maxLength, baseLength);
            i(TAG, "shrink log success");
        }
    }

    @Override
    void writeLog(final String log) {
        FileUtil.appendFile(log, logPath);
    }

    @Override
    void forceFlush() {

    }

    @Override
    void close() {

    }
}