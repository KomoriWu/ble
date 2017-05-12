package com.example.txtledbluetooth.light.model;

import android.content.Context;

import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public class LightModelImpl implements LightModel {
    @Override
    public void WriteCommand(BluetoothClient client, String macAddress, UUID serviceUUID,
                             UUID characterUUID, String command) {
        client.write(macAddress, serviceUUID,
                characterUUID, command.getBytes(),
                new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    @Override
    public void saveLightDate(LightType lightType) {
        lightType.save();
    }
}
