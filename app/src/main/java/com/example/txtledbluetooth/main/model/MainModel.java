package com.example.txtledbluetooth.main.model;

import android.content.Context;

import com.example.txtledbluetooth.main.presenter.MainPresenter;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public interface MainModel {
    void initBle(Context context, BluetoothClient client, BleConnectOptions bleConnectOptions,
                MainModelImpl.OnInitBleListener onInitBleListener);
}
