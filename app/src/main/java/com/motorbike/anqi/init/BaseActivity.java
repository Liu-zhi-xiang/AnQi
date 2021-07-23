package com.motorbike.anqi.init;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.loin.RegisterActivity;
import com.motorbike.anqi.util.JcLog;
import com.motorbike.anqi.util.NetWorkHelper;
import com.motorbike.anqi.util.PermissionsConstans;
import com.motorbike.anqi.util.StatusBarUtil;
import com.motorbike.anqi.util.XOutdatedUtils;
import com.motorbike.anqi.view.CustomDialogView;
import com.motorbike.anqi.view.CustomProgressDialog;
import com.motorbike.anqi.view.XToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author lzx
 * @date 2017/12/25
 * @info
 */

public class BaseActivity extends AppCompatActivity {

    protected String activityName;
    private CustomProgressDialog customProgressDialog;

    protected Map<Integer,String> urlmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityName = BaseFragment.class.getSimpleName();
        //设置保持屏幕常亮,CPU处于运行状态
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        customProgressDialog = new CustomProgressDialog(this);
        initSystemBarTint();
        urlmap=new ArrayMap<Integer,String>();
    }

    /**
     * 设置状态栏颜色
     */
    protected void initSystemBarTint()
    {
        StatusBarUtil.setStatusBarColor(this, setStatusBarColor());

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        }
    }

    /**
     * 子类可以重写改变状态栏颜色
     */
    protected int setStatusBarColor()
    {
        return getColorPrimary();
    }

    /**
     * 获取主题色
     */
    public int getColorPrimary()
    {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;

    }

    protected void okHttp(Context context, String url, final Integer mTag, Map<String, Object> map)
    {
        if (urlmap!=null){
            String o = (String) map.get(mTag);
            if (TextUtils.isEmpty(o)){
                urlmap.put(mTag,url);
            }else {
                httpRequestData(mTag,null,getString(R.string.load_now),false);
                return;
            }
        }
        if (TextUtils.isEmpty(url) || map == null) {
            httpRequestData(mTag, null, "请求参数为空", false);
            return;
        }
        if (!NetWorkHelper.isNetworkAvailable(context)) {
            httpRequestData(mTag, null, "无网络连接", false);
            return;
        }
        PostRequest<String> request = OkGo.<String>post(url).tag(context);
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            request.params(entry.getKey(), entry.getValue() + "");
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                JcLog.e(activityName + mTag, "====" + response.body());

                JSONObject jsonObject = null;
                String jsondata = "";
                String msg = "";
                int status = 1;
                try {
                    jsonObject = new JSONObject(response.body().toString());
                    status = (int) jsonObject.getInt("status");
                    jsondata = (String) jsonObject.getString("data");
                    msg = (String) jsonObject.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status == 1) {
                    httpRequestData(mTag, null, msg, false);
                } else if (status == 0) {
                    httpRequestData(mTag, jsondata, msg, true);
                }
                if (urlmap!=null) {
                    urlmap.remove(mTag);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e("aaa", "请求错误");

                httpRequestData(mTag, null, "请求错误", false);
                if (urlmap!=null) {
                    urlmap.remove(mTag);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (urlmap!=null) {
                    urlmap.remove(mTag);
                }
            }
        });


    }

    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp)
    {
        if (!isHttp) {
            dismissLoading();
            prompt(mag);
            Log.e("aaaa", "aaaa" + mTag);
            return;
        }
    }

    private Toast toast;

    public void prompt(String text)
    {
        XToast.info(text);
    }

    public void showToast(String text)
    {
        if (toast == null) {
            toast = Toast.makeText(this,
                    text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastView = (LinearLayout) toast.getView();
            ImageView imageCodeProject = new ImageView(this);
            imageCodeProject.setImageResource(R.mipmap.guanbi);
            toastView.addView(imageCodeProject, 0);
            toast.show();
        } else {
            toast.cancel();
            toast = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        OkGo.getInstance().cancelTag(this);
        dismissLoading();
    }

    public void showLoading()
    {
        if ((customProgressDialog != null && !customProgressDialog.isShowing())) {
//            customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (getBaseContext() != null) {
                customProgressDialog.show();
            }
        }
    }

    public void dismissLoading()
    {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }


    /**
     * 为子类提供一个权限检查方法
     *
     * @param permissions
     * @return
     */
    public boolean hasPermission(String... permissions)
    {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 权限请求申请
     *
     * @param requestCode 请求码
     * @param permissions 权限
     */
    public void requestPermission(int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsConstans.WRITE_STORAGE_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSDCardPermission();
                } else {
                    //TODO 提示用户权限未授予
                    Toast.makeText(BaseActivity.this, "WRITE_EXTERNAL_STORAGE 权限未开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionsConstans.SECH_PHONE_CODE:
                if (permissions != null && permissions.length == 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                            doCallPhone();
                        } else {
                            //TODO 提示用户权限未授予
                            Toast.makeText(BaseActivity.this, "WRITE_EXTERNAL_STORAGE 权限未开启", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //TODO 提示用户权限未授予
                        Toast.makeText(BaseActivity.this, "READ_PHONE_STATE 权限未开启", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        doCallPhone();
                    } else {
                        //TODO 提示用户权限未授予
                        Toast.makeText(BaseActivity.this, "READ_PHONE_STATE 权限未开启", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case PermissionsConstans.INSEPCT_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doCameraPermission();
                } else {
                    //TODO 提示用户权限未授予
                    Toast.makeText(BaseActivity.this, "CAMERA 权限未开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionsConstans.LOCATION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doDINGWEIPermission();
                } else {
                    //TODO 提示用户权限未授予
                    Toast.makeText(BaseActivity.this, "ACCESS_COARSE_LOCATION 权限未开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionsConstans.ACCESS_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doBlePermission();
                } else {
                    //TODO 提示用户权限未授予
                    Toast.makeText(BaseActivity.this, "ACCESS_COARSE_LOCATION 权限未开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionsConstans.READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //TODO 提示用户权限未授予
                    Toast.makeText(BaseActivity.this, "ACCESS_COARSE_LOCATION 权限未开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionsConstans.PROCESS_OUTGOING_CALLS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //TODO 提示用户权限未授予
                    Toast.makeText(BaseActivity.this, "ACCESS_COARSE_LOCATION 权限未开启", Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionsConstans.CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //TODO 提示用户权限未授予
                    Toast.makeText(BaseActivity.this, "ACCESS_COARSE_LOCATION 权限未开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 默认的写SD权限处理
     */
    protected void doSDCardPermission() {
        //TODO
    }

    /**
     * 默认的定位权限处理
     */
    protected void doDINGWEIPermission() {
        //TODO
    }

    /**
     * 默认的打电话处理
     */
    protected void doCallPhone() {
        //TODO

    }

    /**
     * 默认的相机权限处理
     */
    protected void doCameraPermission() {
        //TODO
    }

    /**
     * 默认的蓝牙权限处理
     */
    protected void doBlePermission(){

    }

    /**
     * 自定义dialog提示框
     *
     * @param
     */
    public void showAlertDialog(String title, String masg) {
        final CustomDialogView dialog = new CustomDialogView(this);
        dialog.setTitle(title);
        dialog.setMessage(masg);
        dialog.setYesOnclickListener("确定", new CustomDialogView.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                dialog.dismiss();
                onclickDialog();
            }
        });
        dialog.setNoOnclickListener("取消", new CustomDialogView.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 默认的dialog提示框处理
     */
    protected void onclickDialog() {
        //TODO

    }

    protected void showTip(String msg) {
        prompt(msg);
    }
}
