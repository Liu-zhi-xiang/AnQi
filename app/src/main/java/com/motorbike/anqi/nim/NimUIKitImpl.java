package com.motorbike.anqi.nim;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.motorbike.anqi.moduil.chatroom.ChatRoomMemberChangedObservable;
import com.motorbike.anqi.moduil.chatroom.ChatRoomProvider;
import com.motorbike.anqi.moduil.user.IUserInfoProvider;
import com.motorbike.anqi.moduil.user.UserInfoObservable;
import com.motorbike.anqi.nim.cache.DataCacheManager;
import com.motorbike.anqi.nim.provider.DefaultChatRoomProvider;
import com.motorbike.anqi.nim.provider.DefaultUserInfoProvider;
import com.motorbike.anqi.util.LogUtil;
import com.motorbike.anqi.util.StorageType;
import com.motorbike.anqi.util.StorageUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * UIKit能力实现类。
 */
public final class NimUIKitImpl {

    // context
    private static Context context;

    // 自己的用户帐号
    private static String account;

    private static UIKitOptions options;

    // 用户信息提供者
    private static IUserInfoProvider userInfoProvider;

    // userInfo 变更监听
    private static UserInfoObservable userInfoObservable;

    // contact 变化监听


    // 聊天室提供者
    private static ChatRoomProvider chatRoomProvider;

    // 聊天室成员变更通知
    private static ChatRoomMemberChangedObservable chatRoomMemberChangedObservable;

    // 缓存构建成功
    private static boolean buildCacheComplete = false;

    //初始化状态监听
    private static UIKitInitStateListener initStateListener;

    /*
     * ****************************** 初始化 ******************************
     */
    public static void init(Context context) {
        init(context, new UIKitOptions(), null);
    }
    public static void init(Context context, UIKitOptions options) {
        init(context, options, null);
    }

    public static void init(Context context, IUserInfoProvider userInfoProvider) {
        init(context, new UIKitOptions(), userInfoProvider);
    }

    public static void init(Context context, UIKitOptions options, IUserInfoProvider userInfoProvider) {
        NimUIKitImpl.context = context.getApplicationContext();
        NimUIKitImpl.options = options;
        // init tools
        StorageUtil.init(context, options.appCacheDir);
//        ScreenUtil.init(context);



        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtil.init(path, Log.DEBUG);


//        if (!TextUtils.isEmpty(getAccount())) {
//            if (options.initAsync) {
//                DataCacheManager.buildDataCacheAsync(); // build data cache on auto login
//            } else {
//                DataCacheManager.buildDataCache(); // build data cache on auto login
//                buildCacheComplete = true;
//            }
//        }
    }

    public static boolean isInitComplete() {
        return !options.initAsync || TextUtils.isEmpty(account) || buildCacheComplete;
    }

    public static void setInitStateListener(UIKitInitStateListener listener) {
        initStateListener = listener;
    }

    public static void notifyCacheBuildComplete() {
        buildCacheComplete = true;
        if (initStateListener != null) {
            initStateListener.onFinish();
        }
    }

    /*
    * ****************************** 登录登出 ******************************
    */
    public static AbortableFuture<LoginInfo> login(LoginInfo loginInfo, final RequestCallback<LoginInfo> callback) {

        AbortableFuture<LoginInfo> loginRequest = NIMClient.getService(AuthService.class).login(loginInfo);

        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                loginSuccess(loginInfo.getAccount());
                callback.onSuccess(loginInfo);
            }

            @Override
            public void onFailed(int code) {
                callback.onFailed(code);
            }

            @Override
            public void onException(Throwable exception) {

                callback.onException(exception);
            }
        });
        return loginRequest;
    }

    public static void loginSuccess(String account)
    {
        setAccount(account);
        DataCacheManager.buildDataCache();
        buildCacheComplete = true;

    }

    public static void logout() {
        DataCacheManager.clearDataCache();

    }



    public static UIKitOptions getOptions() {
        return options;
    }

    // 初始化用户信息提供者
    private static void initUserInfoProvider(IUserInfoProvider userInfoProvider) {

        if (userInfoProvider == null) {
            userInfoProvider = new DefaultUserInfoProvider();
        }

        NimUIKitImpl.userInfoProvider = userInfoProvider;
    }















    public static IUserInfoProvider getUserInfoProvider() {
        return userInfoProvider;
    }

    public static UserInfoObservable getUserInfoObservable() {
        if (userInfoObservable == null) {
            userInfoObservable = new UserInfoObservable(context);
        }
        return userInfoObservable;
    }





    public static void setChatRoomProvider(ChatRoomProvider provider) {
        chatRoomProvider = provider;
    }

    public static ChatRoomProvider getChatRoomProvider() {
        if (chatRoomProvider == null) {
            chatRoomProvider = new DefaultChatRoomProvider();
        }
        return chatRoomProvider;
    }

    public static ChatRoomMemberChangedObservable getChatRoomMemberChangedObservable() {
        if (chatRoomMemberChangedObservable == null) {
            chatRoomMemberChangedObservable = new ChatRoomMemberChangedObservable(context);
        }
        return chatRoomMemberChangedObservable;
    }







    public static void setAccount(String account) {
        NimUIKitImpl.account = account;
    }








    /*
    * ****************************** 在线状态 ******************************
    */





    /*
    * ****************************** basic ******************************
    */
    public static Context getContext() {
        return context;
    }

    public static String getAccount() {
        return account;
    }
}
