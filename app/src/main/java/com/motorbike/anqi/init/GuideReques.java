package com.motorbike.anqi.init;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.motorbike.anqi.bean.GuideBean;
import com.motorbike.anqi.interfaces.HttpAysnResultInterface;
import com.motorbike.anqi.interfaces.HttpAysnTaskInterface;
import com.motorbike.anqi.interfaces.HttpRequestTag;
import com.motorbike.anqi.util.NetWorkHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;


/**
 * 引导页
 *
 * @author LS
 */
public class GuideReques implements HttpAysnTaskInterface {
    private Context context;
    private Integer mTag;// Action 标签
    private HttpAysnResultInterface callback;
    private String TAG = "GetCodeService";
    private Type type;
    public GuideReques(Context context, Integer mTag, HttpAysnResultInterface callback) {
        super();
        this.context = context;
        this.mTag = mTag;
        this.callback = callback;
    }

    public void addPoicomment() {
        try {
//            if (!NetWorkHelper.isNetworkAvailable(context)) {
//                callback.dataCallBack(mTag, null,"无网络",false);
//                return;
//            }

            OkGo.<String>post(BaseRequesUrl.GuidePage)
                    .tag(context)// 请求的 tag, 主要用于取消对应的请求
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            requestComplete(mTag, new String(response.body()), true);
//                            try {
//                                Log.e(TAG, "==========验证码：" + response.body());
//                                //JSON 解析获得返回值
//                                JSONObject jsonObject = null;
//                                String msg = "";
//                                String data = "";
//                                int status=0;
//                                try {
//                                    jsonObject = new JSONObject(response.body());
//                                    status = (int) jsonObject.getInt("status");
//                                    msg = (String) jsonObject.getString("msg");
//                                    data = (String) jsonObject.getString("data");
//                                } catch (JSONException e){
//                                    e.printStackTrace();
//                                    callback.requestComplete(mTag,null,"请求错误",false);
//                                }
//                                if (status == 0) {
//                                    Log.e("aaaaa","okfabu");
//                                    callback.requestComplete(mTag, data,msg, true);
//                                }else {
//                                    callback.requestComplete(mTag, data,msg, false);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                callback.requestComplete(mTag,null,"请求错误",false);
//                            }
                        }
                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("aaa","请求错误");
//                            callback. requestComplete(mTag,null,"请求错误",false);
                        }

                        @Override
                        public void onFinish()
                        {
                            super.onFinish();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//





    private GuideBean parse(String json) {
        try {
            Log.e(TAG, "==========关于我们：" + json);
            GuideBean entity = new Gson().fromJson(json,
                    GuideBean.class);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void requestComplete(Object tag, Object result, boolean complete) {
        callback.dataCallBack(tag,parse(result.toString()));
    }
}
