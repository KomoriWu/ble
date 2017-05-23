package com.example.txtledbluetooth.light.model;

import android.os.Bundle;

import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SqlUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.BluetoothClient;

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

}
