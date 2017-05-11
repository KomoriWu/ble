package com.example.txtledbluetooth.light.model;

import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public interface LightModel {
    void WriteCommand(BluetoothClient client, String macAddress, UUID serviceUUID,
                      UUID characterUUID, String command);

}
