package com.motorbike.anqi.init;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.motorbike.anqi.bean.UserCenterBean;
import com.motorbike.anqi.interfaces.HttpAysnResultInterface;
import com.motorbike.anqi.interfaces.HttpAysnTaskInterface;
import com.motorbike.anqi.util.NetWorkHelper;


import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 *
 * @author LS
 */
public class HimInfoHttpReques implements HttpAysnResultInterface {
    private Context context;
    private Integer mTag;// Action 标签
    private HttpAysnTaskInterface callback;
    private String TAG = "GetCodeService";

    public HimInfoHttpReques(Context context, Integer mTag, HttpAysnTaskInterface callback) {
        super();
        this.context = context;
        this.mTag = mTag;
        this.callback = callback;
    }


    public void getModify(String userId) {
        try {
            if (!NetWorkHelper.isNetworkAvailable(context)) {
                callback.requestComplete(mTag,null,false);
                return;
            }
            OkGo.<String>post(BaseRequesUrl.PersonalCenter)
                    .params("userId",userId)
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e("response=======",response.body());
                            dataCallBack(mTag,parse(response.body()));
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("aaaa","result=="+response.body());
                            callback.requestComplete(mTag,null,false);
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //接口已通，待接入数据
    private UserCenterBean parse(String json) {
        try {
            //JSON 解析获得返回值
            JSONObject jsonObject = null;
            String data = "";
            String msg="";
            int status;
            try {
                jsonObject = new JSONObject(json);
                status = (int) jsonObject.getInt("status");
                msg =  jsonObject.getString("msg");
                data =  jsonObject.getString("data");

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            if (status==0&&data.length()>4) {
                UserCenterBean userinfobean = new Gson().fromJson(data, UserCenterBean.class);
                return userinfobean;
            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void dataCallBack(Object tag, Object result) {
        callback.requestComplete(tag,result,true);
    }
}
