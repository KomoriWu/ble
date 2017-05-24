package com.example.txtledbluetooth.light.model;

import android.os.Bundle;
import android.util.Log;

import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SqlUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.List;
import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public class LightModelImpl implements LightModel {

    @Override
    public void WriteCommand(BluetoothClient client, String macAddress, UUID serviceUUID,
                             UUID characterUUID, String command,
                             OnInterfaceWriteCommand onInterfaceWriteCommand) {
        BleCommandUtils.divideFrameBleSendData(command.getBytes(), client,
                macAddress, serviceUUID, characterUUID, onInterfaceWriteCommand);
    }

    @Override
    public void openNotify(BluetoothClient client, String macAddress, UUID serviceUUID,
                           UUID characterUUID, final OnInterfaceOpenNotify onInterfaceOpenNotify) {

        client.notify(macAddress, serviceUUID, characterUUID, new BleNotifyResponse() {
            StringBuffer sbCommand = new StringBuffer();

            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                sbCommand.append(new String(value));
                if (value != null && value.length > 1) {
                    Log.d("notify", "----" + bytes2hex03(value));
                    if (value[value.length - 1] == 10 && value[value.length - 2] == 13) {
                        Log.d("notify", "command:" + sbCommand.toString());
//                        Log.d("notify", "length:" + sbCommand.toString().length());
                        onInterfaceOpenNotify.onNotify(sbCommand.toString());
                        sbCommand.setLength(0);
                    }
                }
            }

            @Override
            public void onResponse(int code) {
                if (code == Constants.REQUEST_SUCCESS) {
                    onInterfaceOpenNotify.onOpenNotifySuccess();
                }
            }
        });
    }

    public static String bytes2hex03(byte[] bytes) {
        final String HEX = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt((b >> 4) & 0x0f));
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt(b & 0x0f));
        }

        return sb.toString();
    }

    @Override
    public void saveLightColor(Bundle bundle) {
        String name = bundle.getString(Utils.SQL_NAME);
        int r = bundle.getInt(Utils.COLOR_R);
        int g = bundle.getInt(Utils.COLOR_G);
        int b = bundle.getInt(Utils.COLOR_B);
        int x = bundle.getInt(Utils.PIXEL_X);
        int y = bundle.getInt(Utils.PIXEL_Y);
        RgbColor rgbColor = new RgbColor(name, r, g, b, x, y);
        rgbColor.deleteRgbColorByName();
        rgbColor.save();
    }

    @Override
    public void saveLightType(Bundle bundle) {
        String name = bundle.getString(Utils.SQL_NAME);
        int popupPosition = bundle.getInt(Utils.POPUP_POSITION);
        int bright = bundle.getInt(Utils.SEEK_BAR_PROGRESS_BRIGHT);
        int speed = bundle.getInt(Utils.SEEK_BAR_PROGRESS_SPEED);
        boolean isOpen = bundle.getBoolean(Utils.PULSE_IS_OPEN);
        LightType lightType = new LightType(name, speed, bright, popupPosition, isOpen);
        lightType.deleteLightTypeByName();
        lightType.save();

    }

    @Override
    public int getLightType(String name) {
        List<LightType> lightTypeList = LightType.getLightTypeList(name);
        if (lightTypeList != null && lightTypeList.size() > 0) {
            LightType lightType = lightTypeList.get(0);
            return lightType.getPopupPosition();
        } else {
            return 0;
        }
    }

    @Override
    public RgbColor getLightColor(String sqlName, int position) {
        List<RgbColor> rgbColorList = RgbColor.getRgbColorList(sqlName + position);
        if (rgbColorList == null || rgbColorList.size() == 0) {
            rgbColorList = SqlUtils.getDefaultColors(sqlName, position);
        }
        return rgbColorList.get(0);
    }

    public interface OnInterfaceWriteCommand {
        void onWriteFailure();
    }

    public interface OnInterfaceOpenNotify {
        void onNotify(String command);

        void onOpenNotifySuccess();
    }

}
