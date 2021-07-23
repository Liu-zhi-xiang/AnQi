package com.motorbike.anqi.service;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.comm.ObserverManager;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.init.MyApplication;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.view.CustomDialogView;
import com.motorbike.anqi.view.XToast;

import java.util.List;

/**
 * @author lzx
 * @date 2019/1/16
 * @info
 */
public class BleManagerHandler {

    private String devicename;

    private boolean bluetoothLink=false;

    private BleconnectInterface bleconnectInterface;

    private BleManagerHandler(){
        devicename="PTT-Z";
    }

    public void  setBleconnectInterface(BleconnectInterface bleconnectInterface){
        this.bleconnectInterface=bleconnectInterface;
    }

    public static synchronized BleManagerHandler getInstance(){
        return BleHandler.bma;
    }
    private static class BleHandler{
        private static final BleManagerHandler bma=new BleManagerHandler();

    }

    public   BleDevice isDeviceConnected(String Devicename) {
        //检测已连接设备
        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        if (bleDeviceList != null && bleDeviceList.size() > 0) {
           return getDevice(bleDeviceList,Devicename);
        }
        return null;
    }

    /**
     * 要扫描的list
     * @param bleDeviceList
     *
     */
    private BleDevice getDevice(List<BleDevice> bleDeviceList,String Devicename){
        for (int x = 0; x < bleDeviceList.size(); x++) {
            BleDevice bleDevice = bleDeviceList.get(x);
            if (bleDevice != null) {
                String name = bleDevice.getName();
                if (!TextUtils.isEmpty(name)){
                    Log.e("ccc", "已连接" + "-----" + name);
                    if (name.equals(Devicename)) {
                        return bleDevice;
                    }
                }
            }
        }
        return null;
    }
    public void  connectedDevice()
    {
        if (!BleManager.getInstance().isBlueEnable()) {
            BleManager.getInstance().enableBluetooth();
        }else{
            BleDevice bleDevice= isDeviceConnected("PTT-Z");
            if (bleDevice!=null){
                connectBle(bleDevice);
            }else {
                startScan();
            }
        }
    }
    private int  size=0;
    private void startScan()
    {

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {

            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Log.e("ccc",scanResultList.size()+"   size");
                if (size!=scanResultList.size()){
                    //或多次调用，防止重复分析扫描结果（一定概率）
                    size=scanResultList.size();
                    BleDevice bleDevice=getDevice(scanResultList, devicename);
                    if (bleDevice!=null){
                        BleManager.getInstance().cancelScan();
                        connectBle(bleDevice);
                    }else {
//                        XToast.info("Ble未查找到");
                        bleconnectInterface.toastMsg("Ble未查找到");
                    }
                }
                if (BleManager.getInstance().isBlueEnable()&&scanResultList.size()==0){
                    Log.e("aaaa","size==0");
//                    XToast.info("蓝牙连接需要，请打开GPS定位");
                    bleconnectInterface.toastMsg("蓝牙连接需要，请打开GPS定位");
                }
            }
        });
    }
    private static int keyNum=0;
    /**
     * 连接蓝牙
     * @param bleDevice
     */
    private void connectBle(final BleDevice bleDevice)
    {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }
            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
//                startScan();
                keyNum=0;
                bluetoothLink=false;
//                connectBle(bleDevice);
//                Toast.makeText(MainActivityTwo.this, "连接失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                BleManager.getInstance().cancelScan();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (bleDevice != null){
                        bluetoothLink=true;
                        if (bleconnectInterface!=null){
                            bleconnectInterface.isconnect();
                        }
                        BleManager.getInstance().notify(
                                bleDevice,
                                "0000ffe0-0000-1000-8000-00805f9b34fb",
                                "0000ffe1-0000-1000-8000-00805f9b34fb",
                                b);
                        bleconnectInterface.toastMsg("Ble连接成功");
//                        XToast.info("Ble连接成功");
                    }else {
                        bluetoothLink=false;
//                        XToast.info("Ble连接失败");
                        bleconnectInterface.toastMsg("Ble连接失败");
                    }
                }
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                keyNum=0;
                if (bleconnectInterface!=null){
                    bleconnectInterface.isnotconnect();
                }
                bluetoothLink=false;
                if (isActiveDisConnected) {
//                    XToast.info("Ble连接断开");
                    bleconnectInterface.toastMsg("Ble连接断开");
                } else {
//                    XToast.info("蓝牙连接断开");
                    bleconnectInterface.toastMsg("蓝牙连接断开");
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

            }
        });
    }

    public interface BleconnectInterface{
        void isconnect();
        void isnotconnect();
        void toastMsg(String msg);
    }
    public  static BleNotifyCallback b=new BleNotifyCallback()
    {

        @Override
        public void onNotifySuccess()
        {
            Log.e("ccc", "notify success");
        }

        @Override
        public void onNotifyFailure(final BleException exception) {
            Log.e("ccc", exception.toString()+"1");
        }

        @Override
        public void onCharacteristicChanged(final byte[] data) {
            if (data.length > 0&& HexUtil.formatHexString(data, true).equals("00")) {
                keyNum++;
                NimVoicerImpl nimVoicer = NimVoicerImpl.getInstance();
                if (nimVoicer != null) {
                    if (keyNum > 0) {
                        if (keyNum % 2 == 0) {
                            nimVoicer.offMkf(false);
                            BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.Barley,"0");
                            Log.e("ccc","Mkf=false");
                        } else {
                            nimVoicer.offMkf(true);
                            BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class,HttpTagUtil.Barley,"1");
                            Log.e("ccc","Mkf=true");
                        }
                    }
                }
            }
            Log.e("ccc", HexUtil.formatHexString(data, true) + "      data");
        }
    };

}