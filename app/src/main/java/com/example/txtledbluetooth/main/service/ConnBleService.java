package com.example.txtledbluetooth.main.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.main.model.MainModelImpl;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * Created by KomoriWu on 2017/6/24.
 */

@SuppressLint("Registered")
public class ConnBleService extends Service {
    private static final int SEARCH_TIMEOUT = 5000;
    private static final int SEARCH_TIMEOUT_NUMBER = 2;
    private static final int TIMER_DELAY = 1000;
    private static final int TIMER_PERIOD = 100;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private ConnBleObservable mConnBleObservable;

    @Override
    public void onCreate() {
        super.onCreate();
        mConnBleObservable = new ConnBleObservable();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new connBleBinder();
    }

    class connBleBinder extends Binder implements ConnBleInterface {

        @Override
        public void scanBle() {
            startTimer();
        }

        @Override
        public void addObserver(Observer observer) {
            mConnBleObservable.addObserver(observer);
        }
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    checkBluetooth();
                }
            };
        }
        if (mTimer != null) {
            mTimer.schedule(mTimerTask, TIMER_DELAY, TIMER_PERIOD);
        }
    }

    private void checkBluetooth() {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            //得到BluetoothAdapter的Class对象
            Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;
            Method method = null;
            method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState",
                    (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);
            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Set<BluetoothDevice> devices = adapter.getBondedDevices();

                for (BluetoothDevice device : devices) {
                    Log.d("bluename", "本机---" + device.getName());
                    Log.d("bluename", "本机---" + device.getAddress());
                    if (device.getName().contains(Utils.BLE_NAME)) {
                        @SuppressLint("PrivateApi")
                        Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod(
                                "isConnected", (Class[]) null);
                        method.setAccessible(true);
                        boolean isConnected = (boolean) isConnectedMethod.invoke(device, (
                                Object[]) null);
                        if (isConnected) {
                            Log.d("bluename", "connected:" + device.getAddress());
                            stopTimer();
                            mConnBleObservable.notifyChanged(device.getAddress());
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public class ConnBleObservable extends Observable {
        public void notifyChanged(String macAddress) {
            this.setChanged();
            this.notifyObservers(macAddress);
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }
}
