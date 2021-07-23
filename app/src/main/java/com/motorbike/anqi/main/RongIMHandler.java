package com.motorbike.anqi.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.motorbike.anqi.MainActivityTwo;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.bean.UsreBean;
import com.motorbike.anqi.broadcast.MyPrivateConversationProvider;
import com.motorbike.anqi.broadcast.MySendMessageListener;
import com.motorbike.anqi.greendao.gen.DBManager;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.service.MyConnectionStatusListener;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * @author lzx
 * @date 2019/1/17
 * @info
 */
public class RongIMHandler {
    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link } 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token    融云--------从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */

    private static int messageSize = 0;
    public static void connect(String token,final Context context)
    {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                BaseRequesUrl.RongIMLj=false;
                BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(MainActivityTwo.class, HttpTagUtil.RONGYUN_TOKEN,"0");
                Log.e("rongyun", "tkeencuo"+" Token 过期");

            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {

                Log.e("rongyun", "--onSuccess" + userid);
                BaseRequesUrl.RongIMLj=true;
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider(){
                    @Override
                    public UserInfo getUserInfo(String s) {
                        Log.e("UserInfo", "id=" + s);
                        List<UsreBean> usreBeanList = DBManager.getInstance(context).queryUserList(s);
                        if (usreBeanList != null && usreBeanList.size() > 0) {
                            Log.e("UserInfo", "size" + usreBeanList.size());
                            for (int x = 0; x < usreBeanList.size(); x++) {
                                UsreBean usreBean = usreBeanList.get(x);
                                Log.e("UserInfo", usreBean.getUserId() + "===" + usreBean.getUserName() + "=====" + usreBean.getHeanImg());
                                if (usreBean.getUserId().equals(s)) {
                                    return new UserInfo(usreBean.getUserName(), usreBean.getUserId(), Uri.parse(usreBean.getHeanImg()));
                                }
                            }
                        }
                        return null;
                    }
                }, true);
                RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
                //设置连接状态信息提供监听
                RongIM.setConnectionStatusListener(new MyConnectionStatusListener());
                RongIM.getInstance().registerConversationTemplate(new MyPrivateConversationProvider());
                RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
                messageSize=0;
                RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                    @Override
                    public boolean onReceived(io.rong.imlib.model.Message message, int i) {
                        Intent intent = new Intent();
                        intent.setAction("myMessage");
                        messageSize=i+1;
                        intent.putExtra("message", messageSize+"");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        Log.e("aaaa", "messageSize==" + messageSize);
                        return false;
                    }
                });
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                BaseRequesUrl.RongIMLj=false;
                Log.e("rongyun", "--onError" + errorCode.getValue());
            }
        });
    }
}
