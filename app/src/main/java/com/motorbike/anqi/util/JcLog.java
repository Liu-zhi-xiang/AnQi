package com.motorbike.anqi.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author lzx
 * @date 2017/12/25
 * @info
 */

public class JcLog {

    public static final boolean NO=true;

    public static void d(String tag,String content){
        if (NO&&!TextUtils.isEmpty(tag)){
            Log.d(tag,content+"");
        }
    }

    /**
     * 截断输出日志
     * @param msg
     */
    public static void e(String tag, String msg)
    {
        if (!NO||tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.e(tag, msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.e(tag, logContent);
            }
            Log.e(tag, msg);// 打印剩余日志
        }
    }

}
