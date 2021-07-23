package com.motorbike.anqi.nim;

import android.app.Activity;
import android.content.Context;


import com.motorbike.anqi.moduil.chatroom.ChatRoomMemberChangedObservable;
import com.motorbike.anqi.moduil.chatroom.ChatRoomProvider;
import com.motorbike.anqi.moduil.user.IUserInfoProvider;
import com.motorbike.anqi.moduil.user.UserInfoObservable;


/**
 * 云信UI组件接口/定制化入口
 * Created by huangjun on 2017/9/29.
 */

public class NimUIKit {

    /**
     * 初始化UIKit, 用户信息、联系人信息使用

     * @param context 上下文
     */
    public static void init(Context context) {
        NimUIKitImpl.init(context);
    }

    /**
     * 初始化UIKit, 用户信息、联系人信息使用

     * 若用户自行提供 userInfoProvider，contactProvider，请使用

     * @param context 上下文
     * @param option  自定义选项
     */
    public static void init(Context context, UIKitOptions option) {
        NimUIKitImpl.init(context, option);
    }

    /**
     * 初始化UIKit，须传入context以及用户信息提供者
     *
     * @param context          上下文
     * @param userInfoProvider 用户信息提供者

     */
    public static void init(Context context, IUserInfoProvider userInfoProvider) {
        NimUIKitImpl.init(context, userInfoProvider);
    }

    /**
     * 初始化UIKit，须传入context以及用户信息提供者
     *
     * @param context          上下文
     * @param option           自定义选项
     * @param userInfoProvider 用户信息提供者

     */
    public static void init(Context context, UIKitOptions option, IUserInfoProvider userInfoProvider) {
        NimUIKitImpl.init(context, option, userInfoProvider);
    }

    /**
     * 获取配置项
     *
     * @return UIKitOptions
     */
    public static UIKitOptions getOptions() {
        return NimUIKitImpl.getOptions();
    }










    /**
     * 手动登陆成功
     *
     * @param account 登陆成功账号
     */
    public static void loginSuccess(String account) {
        NimUIKitImpl.loginSuccess(account);
    }

    /**
     * 释放缓存，一般在注销时调用
     */
    public static void logout() {
        NimUIKitImpl.logout();
    }





    /**
     * 获取上下文
     *
     * @return 必须初始化后才有值
     */
    public static Context getContext() {
        return NimUIKitImpl.getContext();
    }


    /**
     * 设置当前登录用户的帐号
     *
     * @param account 帐号
     */
    public static void setAccount(String account) {
        NimUIKitImpl.setAccount(account);
    }

    /**
     * 获取当前登录的账号
     *
     * @return 必须登录成功后才有值
     */
    public static String getAccount() {
        return NimUIKitImpl.getAccount();
    }




    public static boolean isInitComplete() {
        return NimUIKitImpl.isInitComplete();
    }

    /**
     * 获取 “用户资料” 提供者
     *
     * @return 必须在初始化后获取
     */
    public static IUserInfoProvider getUserInfoProvider() {
        return NimUIKitImpl.getUserInfoProvider();
    }

    /**
     * 获取 “用户资料” 变更监听管理者
     * UIKit 与 app 之间 userInfo 数据更新通知接口
     *
     * @return UserInfoObservable
     */
    public static UserInfoObservable getUserInfoObservable() {
        return NimUIKitImpl.getUserInfoObservable();
    }



    /**
     * 设置聊天室信息提供者
     * 不设置将使用 uikit 内置默认
     *
     * @param provider ChatRoomProvider
     */
    public static void setChatRoomProvider(ChatRoomProvider provider) {
        NimUIKitImpl.setChatRoomProvider(provider);
    }

    /**
     * 获取聊天室信息提供者
     *
     * @return ChatRoomProvider
     */
    public static ChatRoomProvider getChatRoomProvider() {
        return NimUIKitImpl.getChatRoomProvider();
    }

    /**
     * 获取聊天室成员变更监听接口
     *
     * @return ChatRoomMemberChangedObservable
     */
    public static ChatRoomMemberChangedObservable getChatRoomMemberChangedObservable() {
        return NimUIKitImpl.getChatRoomMemberChangedObservable();
    }
}
