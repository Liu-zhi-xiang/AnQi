package com.motorbike.anqi.init;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;


import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.clj.fastble.BleManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.motorbike.anqi.activity.loin.LoginActivity;
import com.motorbike.anqi.activity.map.LocationService;
import com.motorbike.anqi.nim.DemoCache;
import com.motorbike.anqi.nim.NimUIKit;
import com.motorbike.anqi.nim.UIKitOptions;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.util.LogUtil;
import com.motorbike.anqi.util.PixelFormat;
import com.motorbike.anqi.util.UserPreference;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;


import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.OkHttpClient;

import static com.motorbike.anqi.moduil.NimSDKOptionConfig.getAppCacheDir;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * @author lzx
 * @date 2017/12/25
 * @info
 */

public class MyApplication extends Application{
    //屏幕的宽度
    public static int mScreenWidth;
    //屏幕的高度
    public static int mScreenHeight;
    //状态栏的高度
    public static int statusHeight;
    //虚拟按键的高度
    public static int virtKeyHeight=0;
    //不管是蓝牙连接方还是服务器方，得到socket对象后都传入
    public static BluetoothSocket bluetoothSocket;
    private static MyApplication instance;
    // Activity栈
    private static Stack<AppCompatActivity> activityStack;
    public static Context context;
    public int systemDpi;
    public LocationService locationService;
    public synchronized static MyApplication getInstance() {
        return instance;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        context=getApplicationContext();
        WindowManager wm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        userPreference=UserPreference.getUserPreference(this);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        statusHeight = PixelFormat.getStatusHeight(this);
        virtKeyHeight = PixelFormat.getBottomStatusHeight(this);
        int ss=PixelFormat.px2dip(this,statusHeight);
        systemDpi=virtKeyHeight/ss;
        Log.e("system","statusHeight=="+statusHeight+"px=====ss=="+ss+"dp====systemDpi==="+systemDpi);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        locationService = new LocationService(getApplicationContext());
        //网络
        initOkGo();
        //友盟
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"5ad41440a40fa310e40000c2");
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setQQZone("1106768065", "T1wUsRYM4GTph63d");
        PlatformConfig.setWeixin("wx17ced67b0bdd02c7", "6a5c9e0816fd1deb9d87f6ca7f0c02c7");
        //网易云初始化
        DemoCache.setContext(this);
        NIMClient.init(this, getLoginInfo(),null);
        if (NIMUtil.isMainProcess(this)){
            initUIKit();
        }
        //融云
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))){
            RongIMClient.init(this);
            RongIM.init(this);
        }
        //极光
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //蓝牙
        BleManager.getInstance().init(this);
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);
    }
    private UserPreference userPreference;


    private LoginInfo getLoginInfo()
    {
        String account = userPreference.getAccid();
        String token = userPreference.getToken();
        Log.e("aaaaa","account=="+account+"====token==="+token);
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account);
            return new LoginInfo(account, token);
        } else {
            BaseRequesUrl.wangyiServer =false;
            return null;
        }
    }


    private void initUIKit()
    {
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());
        registerLoginSyncDataStatus(true);
    }


    public  Context getContext()
    {
        return instance.getApplicationContext();

    }

    private UIKitOptions buildUIKitOptions()
    {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = getAppCacheDir(this) + "/app";
        return options;
    }
    private void initOkGo()
    {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
//        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //必须设置OkHttpClient
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(AppCompatActivity activity)
    {
        if (activityStack == null) {
            activityStack = new Stack<AppCompatActivity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        AppCompatActivity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(AppCompatActivity activity)
    {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (AppCompatActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(){
        try {
            finishAllActivity();
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 在App启动时向SDK注册登录后同步数据过程状态的通知
     * 调用时机：主进程Application onCreate中
     */

    /**
     * 状态
     */
    public void registerLoginSyncDataStatus(boolean register) {
        LogUtil.i("wengyiyun", "observe login sync data completed event on Application create");
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(statusCodeObserver, register);
    }
    Observer<StatusCode> statusCodeObserver=new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            switch (statusCode) {
                case INVALID:
                    Log.e("deng","未定义");
                    BaseRequesUrl.wangyiServer =false;
                    break;
                case KICKOUT:
                    Log.e("deng","被其他端的登录踢掉");
                    BaseRequesUrl.wangyiServer =false;
                    break;
                case SYNCING:
                    Log.e("deng","正在同步数据");
                    BaseRequesUrl.wangyiServer =false;
                    break;
                case LOGINED:
                    Log.e("deng","已成功登录");
                    BaseRequesUrl.wangyiServer =true;
                    break;
                case UNLOGIN:
                    Log.e("deng","未登录/登录失败");
                    NimVoicerImpl.getInstance().login(getLoginInfo() , new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            BaseRequesUrl.wangyiServer =true;
                            Log.e("aaa",param.getAccount()+"   success");
                        }

                        @Override
                        public void onFailed(int code) {
                            Log.e("aaa",code+"   fail");
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                    BaseRequesUrl.wangyiServer =false;
                    break;
                case FORBIDDEN:
                    Log.e("deng","被服务器禁止登录");
                    BaseRequesUrl.wangyiServer =false;
                    break;
                case PWD_ERROR:
                    Log.e("deng","用户名或密码错误");
                    BaseRequesUrl.wangyiServer =false;
                    userPreference.setToken("");
                    userPreference.save();
                    break;
                case VER_ERROR:
                    Log.e("deng","客户端版本错误");
                    BaseRequesUrl.wangyiServer =false;
                    break;
                case LOGINING:
                    Log.e("deng","正在登录中");
                    BaseRequesUrl.wangyiServer =false;
                    break;
                case CONNECTING:
                    Log.e("deng","正在连接服务器");
                    BaseRequesUrl.wangyiServer =false;
                    break;
                case NET_BROKEN:
                    Log.e("deng","网络连接已断开");
                    BaseRequesUrl.wangyiServer =false;
                    break;
                case KICK_BY_OTHER_CLIENT:
                    Log.e("deng","被同时在线的其他端主动踢掉");
                    BaseRequesUrl.wangyiServer =false;
                    break;
            }
        }
    };
    @Override
    public void onTerminate() {
        super.onTerminate();


      Log.e("aaaaa","退出了");
    }
}
