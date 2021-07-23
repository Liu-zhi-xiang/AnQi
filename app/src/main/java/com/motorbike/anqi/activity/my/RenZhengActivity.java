package com.motorbike.anqi.activity.my;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.ParcelUuid;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motorbike.anqi.R;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.BaseRequesUrl;
import com.motorbike.anqi.init.HttpTagUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 认证
 */
public class RenZhengActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llCancel,llResult,llRenZheng;
    private EditText etNum;
    private TextView tvRenZheng,tvAgain,tvConfirm,tvDesc;
    private String num;
    private ImageView ivImage;
    private Map<String,Object> confimMap;
    private boolean isSuccess=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_zheng);
        initView();
    }

    private void initView() {
        llCancel=findViewById(R.id.llCancel);
        llCancel.setOnClickListener(this);
        llRenZheng=findViewById(R.id.llRenZheng);
        llResult=findViewById(R.id.llResult);
        tvRenZheng=findViewById(R.id.tvRenZheng);
        tvRenZheng.setOnClickListener(this);
        tvAgain=findViewById(R.id.tvAgain);
        tvAgain.setOnClickListener(this);
        ivImage=findViewById(R.id.ivImage);
        tvConfirm=findViewById(R.id.tvConfirm);
        tvDesc=findViewById(R.id.tvDesc);
        etNum=findViewById(R.id.etNum);
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                num=s.toString();
            }
        });
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {//得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);

            if(state == BluetoothAdapter.STATE_CONNECTED){
                Log.i("BLUETOOTH","BluetoothAdapter.STATE_CONNECTED");
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                Log.i("BLUETOOTH","devices:"+devices.size());

                for(BluetoothDevice device : devices){
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if(isConnected)
                    {
                        ParcelUuid[] uuids = device.getUuids();
                        BluetoothClass bluetoothClass = device.getBluetoothClass();

                        if (uuids!=null&&uuids.length>0) {
                            for (int x = 0; x < uuids.length; x++) {
                                Log.i("BLUETOOTH","connected--uuids:"+uuids[x]);
                            }
                        }
                        Log.i("BLUETOOTH","connected:"+device.getName()+"Uuids:"+device.getUuids().length+"addresss:"+device.getAddress()+"bluetoothClass:"+bluetoothClass.toString());
//                        deviceList.add(device);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 认证接口
     */
    private void confim(String serialNo,String userId){
        showLoading();
        confimMap=new HashMap<>();
        if (confimMap!=null){
            confimMap.clear();
        }
        confimMap.put("userId",userId);
        confimMap.put("serialNo",serialNo);
        okHttp(this, BaseRequesUrl.RenZheng, HttpTagUtil.RenZheng,confimMap);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llCancel:
                finish();
                break;
            case R.id.tvRenZheng:
                if (!TextUtils.isEmpty(num)){
                    confim(num,BaseRequesUrl.uesrId);
//                    llRenZheng.setVisibility(View.GONE);
//                    llResult.setVisibility(View.VISIBLE);
                }else {
                    showTip("请输入蓝牙耳机序列号!");
                }
                break;
            case R.id.tvAgain:
                if (isSuccess==false){
                    etNum.setText("");
                    llRenZheng.setVisibility(View.VISIBLE);
                    llResult.setVisibility(View.GONE);
                }else {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void httpRequestData(Integer mTag, String data, String mag, boolean isHttp) {
        dismissLoading();
        llResult.setVisibility(View.VISIBLE);
        llRenZheng.setVisibility(View.GONE);
        if (!isHttp) {
            ivImage.setImageResource(R.mipmap.fail);
            tvConfirm.setText("认证失败");
            tvDesc.setText(mag);
            isSuccess=false;
            tvAgain.setText("重新认证");
//            prompt(mag);
            Log.e("aaaa", "aaaa" + mTag);
        }else {
            ivImage.setImageResource(R.mipmap.rz_cg);
            tvConfirm.setText("恭喜您认证成功");
            tvDesc.setText("房间人数上限已提高,赶紧去看看吧");
            isSuccess=true;
            tvAgain.setText("返回");
        }

    }
}
