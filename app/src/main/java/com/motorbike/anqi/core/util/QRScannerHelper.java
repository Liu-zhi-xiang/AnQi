package com.motorbike.anqi.core.util;

import android.app.Activity;
import android.content.Intent;

import com.motorbike.anqi.core.IntentIntegrator;
import com.motorbike.anqi.core.IntentResult;
import com.motorbike.anqi.core.barcodescanner.CaptureActivity;


/**
 * QRScannerHelper
 * Created by ysr on 2017/7/22 下午10:30.
 * 邮箱 ysr200808@163.com
 */

public class QRScannerHelper {

    private Activity mContext;
    private OnScannerCallBack mCallBack;


    public QRScannerHelper(Activity context) {
        this.mContext = context;
    }

    /**
     * 开启扫码界面
     */
    public void startScanner() {
        new IntentIntegrator(mContext)
                .setOrientationLocked(false)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("将二维码/条码放入框内，即可自动扫描")
                .initiateScan(); // 初始化扫描
    }

    /**
     * 设置扫码完成该的监听
     *
     * @param mCallBack
     */
    public void setCallBack(OnScannerCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * 该方法需要再activity的onActivityResult中调用获取返回的信息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCallBack == null) return;
        String result=null;
        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == CaptureActivity.SPOT_SUCCESS) {
            result = data.getStringExtra("data");
        } else {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            try {
                result = intentResult.getContents();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mCallBack.onScannerBack(result);
    }


    public interface OnScannerCallBack {
        void onScannerBack(String result);
    }
}
