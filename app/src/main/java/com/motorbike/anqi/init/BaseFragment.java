package com.motorbike.anqi.init;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.motorbike.anqi.R;
import com.motorbike.anqi.util.JcLog;
import com.motorbike.anqi.util.NetWorkHelper;
import com.motorbike.anqi.view.CustomProgressDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * @author lzx
 * @date 2017/12/25
 * @info
 */

public abstract class BaseFragment extends Fragment {
    public CustomProgressDialog customProgressDialog;
    private Context context;
    private String fragmentName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();

        fragmentName=getClass().getName();
        customProgressDialog =new CustomProgressDialog(context);

    }



    @Override
    public void onResume(){
        super.onResume();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }


    public void showLoading()
    {
        if (customProgressDialog != null && !customProgressDialog.isShowing()) {
//            customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            customProgressDialog.show();
        }
    }

    public void dismissLoading() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }

    protected void okHttp( String url, final Integer mTag, Map<String, Object> map) {
        if (TextUtils.isEmpty(url) ||  map == null) {
            httpRequestData(mTag,null,"请求参数为空",false);
            return;
        }
        if (!NetWorkHelper.isNetworkAvailable(context)) {
            httpRequestData(mTag,null,"无网络连接",false);
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

                JcLog.e(fragmentName+mTag,"===="+response.body());

                JSONObject jsonObject = null;
                String jsondata = "";
                String msg = "";
                int status=1;
                try {
                    jsonObject = new JSONObject(response.body().toString());
                    status = (int) jsonObject.getInt("status");
                    jsondata = (String) jsonObject.getString("data");
                    msg = (String) jsonObject.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status==1) {
                    httpRequestData(mTag, null,msg, false);
                }else if (status==0) {
                    httpRequestData(mTag, jsondata, msg, true);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e("aaa","请求错误");
                httpRequestData(mTag,null,"请求错误",false);
            }

            @Override
            public void onFinish()
            {
                super.onFinish();
            }
        });


    }

    protected void httpRequestData(Integer mTag,String data,String mag,boolean isHttp){
        if (!isHttp){
            dismissLoading();
            showToastTwo(mag);
            return;
        }
    }

    private Toast toast;

    public void showToast(String text)
    {
        if (toast == null) {
            toast = Toast.makeText(context,
                    text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastView = (LinearLayout) toast.getView();
            ImageView imageCodeProject = new ImageView(context);
            imageCodeProject.setImageResource(R.mipmap.guanbi);
            toastView.addView(imageCodeProject, 0);
            toast.show();
        } else {
            toast.cancel();
            toast = null;
        }
    }

    public void showToastTwo(String string)
    {
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        dismissLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(context);
        if (customProgressDialog!=null){
            customProgressDialog=null;
        }
    }
}
