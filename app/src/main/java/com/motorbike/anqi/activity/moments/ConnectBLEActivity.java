package com.motorbike.anqi.activity.moments;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.content.Context;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;
import com.motorbike.anqi.R;
import com.motorbike.anqi.activity.voice.VoiceHomeActivity;
import com.motorbike.anqi.beseAdapter.CommonAdapter;
import com.motorbike.anqi.beseAdapter.MultiItemTypeAdapter;
import com.motorbike.anqi.beseAdapter.base.ViewHolder;
import com.motorbike.anqi.comm.ObserverManager;
import com.motorbike.anqi.handler.BaseHandlerOperate;
import com.motorbike.anqi.init.BaseActivity;
import com.motorbike.anqi.init.HttpTagUtil;
import com.motorbike.anqi.nim.voiceroom.NimVoicerImpl;
import com.motorbike.anqi.view.CustomDialogView;
import com.motorbike.anqi.xrecyclerview.XRecyclerView;
import com.motorbike.anqi.xrecyclerview.progressindicator.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 连接BLE页面
 */
public class ConnectBLEActivity extends BaseActivity implements View.OnClickListener, MultiItemTypeAdapter.OnItemClickListener {
    private XRecyclerView recyclerView;
    private TextView title;
    private List<BleDevice> mDevices;
    private MyAdapter myAdapter;
    private boolean bluetoothLink = false;
    private LinearLayout llRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_ble);

        initView();
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        mDevices = new ArrayList<>();
        title = findViewById(R.id.tvTitle);
        title.setText("设备列表");
        findViewById(R.id.llBack).setOnClickListener(this);
        findViewById(R.id.llRight).setOnClickListener(this);
        recyclerView = findViewById(R.id.xRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                recyclerView.loadMoreComplete();

            }
        });
        myAdapter = new MyAdapter(ConnectBLEActivity.this, R.layout.device_list_item, mDevices);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(ConnectBLEActivity.this);
//        startScan();//开启ble扫描
        startNameScan();
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.llRight:

                break;

        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

        if (mDevices.size() > 0 && mDevices != null) {
            BleManager.getInstance().cancelScan();
            connectBle(mDevices.get(position - 1));
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    /**
     *只扫描名字PTT_Z的，扫描到首个符合规则的设备后，便停止扫描链接该设备
     */
    private void startNameScan() {
        //配置扫描规则
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, "PTT-Z")         // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(5000)              // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        //扫描到首个符合扫描规则的设备后，便停止扫描，然后连接该设备
        BleManager.getInstance().scanAndConnect(new BleScanAndConnectCallback() {
            @Override
            public void onScanFinished(BleDevice scanResult) {
                // 扫描结束，结果即为扫描到的第一个符合扫描规则的BLE设备，如果为空表示未搜索到（主线程）

            }

            @Override
            public void onStartConnect() {
                // 开始连接（主线程）
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                // 连接失败（主线程）
                keyNum = 0;
                bluetoothLink = false;
                toastMsg("连接失败");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                // 连接成功，BleDevice即为所连接的BLE设备（主线程）
                Log.e("ttttt",status+"     successStatus");
                BleManager.getInstance().cancelScan();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (bleDevice != null) {
                        bluetoothLink = true;
                        BleManager.getInstance().notify(
                                bleDevice,
                                "0000ffe0-0000-1000-8000-00805f9b34fb",
                                "0000ffe1-0000-1000-8000-00805f9b34fb",
                                b);
                        toastMsg("Ble连接成功");
                    } else {
                        bluetoothLink = false;
                        toastMsg("Ble连接失败");
                    }
                }
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                // 连接断开，isActiveDisConnected是主动断开还是被动断开（主线程）
                keyNum = 0;
                bluetoothLink = false;
                if (isActiveDisConnected) {
                    toastMsg("Ble连接断开");
                } else {
                    toastMsg("蓝牙连接断开");
                    ObserverManager.getInstance().notifyObserver(device);
                }
            }

            @Override
            public void onScanStarted(boolean success) {
                // 开始扫描（主线程）
            }

            @Override
            public void onScanning(BleDevice bleDevice) {

            }
        });
    }


    /**
     * 开启蓝牙扫描
     */
    private void startScan() {

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                showLoading();
                mDevices.clear();
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
                Log.e("ttttt", scanResultList.size() + "   size");
                dismissLoading();
                if (BleManager.getInstance().isBlueEnable() && scanResultList.size() == 0) {
                    toastMsg("蓝牙连接需要，请打开GPS定位");
                }
                if (mDevices != null && mDevices.size() > 0) {
                    mDevices.clear();
                }
                if (scanResultList.size() > 0 && scanResultList != null) {
                    mDevices.addAll(scanResultList);
                    Log.e("ttttt", mDevices.size() + "   mDeviceSize");

                    if (myAdapter != null) {
                        myAdapter.notifyDataSetChanged();
                    }
                } else {
                    toastMsg("未扫描到Ble设备");
                }

            }

        });
    }

    private static int keyNum = 0;

    /**
     * 连接Ble设备
     *
     * @param bleDevice
     */
    private void connectBle(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
//                startScan();
                keyNum = 0;
                bluetoothLink = false;
//                connectBle(bleDevice);
//                Toast.makeText(MainActivityTwo.this, "连接失败", Toast.LENGTH_LONG).show();
                toastMsg("连接失败");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                BleManager.getInstance().cancelScan();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (bleDevice != null) {
                        bluetoothLink = true;
                        BleManager.getInstance().notify(
                                bleDevice,
                                "0000ffe0-0000-1000-8000-00805f9b34fb",
                                "0000ffe1-0000-1000-8000-00805f9b34fb",
                                b);
                        toastMsg("Ble连接成功");
                    } else {
                        bluetoothLink = false;
                        toastMsg("Ble连接失败");
                    }
                }
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                keyNum = 0;
                bluetoothLink = false;
                if (isActiveDisConnected) {
                    toastMsg("Ble连接断开");
                } else {
                    toastMsg("蓝牙连接断开");
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

            }
        });
    }

    public static BleNotifyCallback b = new BleNotifyCallback() {

        @Override
        public void onNotifySuccess() {
            Log.e("ccc", "notify success");
        }

        @Override
        public void onNotifyFailure(final BleException exception) {
            Log.e("ccc", exception.toString() + "1");
        }

        @Override
        public void onCharacteristicChanged(final byte[] data) {
            if (data.length > 0 && HexUtil.formatHexString(data, true).equals("00")) {
                keyNum++;
                NimVoicerImpl nimVoicer = NimVoicerImpl.getInstance();
                if (nimVoicer != null) {
                    if (keyNum > 0) {
                        if (keyNum % 2 == 0) {
                            nimVoicer.offMkf(false);
                            BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.Barley, "0");
                            Log.e("ccc", "Mkf=false");
                        } else {
                            nimVoicer.offMkf(true);
                            BaseHandlerOperate.getBaseHandlerOperate().putMessageKey(VoiceHomeActivity.class, HttpTagUtil.Barley, "1");
                            Log.e("ccc", "Mkf=true");
                        }
                    }
                }
            }
            Log.e("ccc", HexUtil.formatHexString(data, true) + "      data");
        }
    };

    /**
     * 消息提示
     *
     * @param msg
     */
    private void toastMsg(String msg) {
        final CustomDialogView dialog = new CustomDialogView(this);
        dialog.setTitle("Ble消息提示");
        dialog.setMessage(msg);
        dialog.setYesOnclickListener("确定", new CustomDialogView.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class MyAdapter extends CommonAdapter<BleDevice> {
        public MyAdapter(Context context, int layoutId, List<BleDevice> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final BleDevice searchResult, final int position) {
            if (searchResult != null) {
                String name = "";
                if (!TextUtils.isEmpty(searchResult.getName())) {
                    name = searchResult.getDevice().getName();
                }
                TextView device_name = holder.getView(R.id.device_name);
                TextView tvState = holder.getView(R.id.tvState);
                Log.e("ttttt", searchResult.getName() + "  name");
                Log.e("ttttt", searchResult.getMac() + "  mac");
                if (TextUtils.isEmpty(name)) {
                    device_name.setText(searchResult.getDevice().getAddress());
                } else {
                    device_name.setText(name);
                }
                if (bluetoothLink == true) {
                    tvState.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
