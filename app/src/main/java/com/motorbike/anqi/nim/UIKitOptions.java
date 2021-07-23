package com.motorbike.anqi.nim;


import com.motorbike.anqi.R;
import com.netease.nimlib.sdk.media.record.RecordType;

/**
 * Created by hzchenkang on 2017/10/19.
 */

public class UIKitOptions {

    /**
     * 配置 APP 保存图片/语音/文件/log等数据缓存的目录(一般配置在SD卡目录)
     *
     * 默认为 /sdcard/{packageName}/
     */
    public String appCacheDir;

    /**
     * 独立聊天室模式，没有 IM 业务
     */
    public boolean independentChatRoom = false;

    /**
     * 是否加载表情贴图
     */

    public boolean loadSticker = true;




    /**
     * UIKit 异步初始化
     * 使用异步方式构建能缩短初始化时间，但同时必须查看初始化状态或者监听初始化成功通知
     */
    public boolean initAsync = false;

    /**
     * 使用云信托管账号体系，构建缓存
     */
    public boolean buildNimUserCache = true;


    /**
     * 录音类型，默认aac
     */
    public RecordType audioRecordType = RecordType.AAC;

    /**
     * 录音时长限制，单位秒，默认最长120s
     */
    public int audioRecordMaxTime = 120;






    /**
     * 返回默认的针对独立模式聊天室的 UIKitOptions
     * @return
     */

    public static UIKitOptions buildForIndependentChatRoom() {
        UIKitOptions options = new UIKitOptions();
        options.buildNimUserCache = false;
        options.loadSticker = false;
        options.independentChatRoom = true;
        return options;
    }
}
