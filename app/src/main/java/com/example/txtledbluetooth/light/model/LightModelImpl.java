package com.example.txtledbluetooth.light.model;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.txtledbluetooth.R;
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

import static com.orm.SugarRecord.save;

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
    public void openNotify(final Context context, BluetoothClient client, String macAddress,
                           UUID serviceUUID, UUID characterUUID, final OnInterfaceOpenNotify
                                   onInterfaceOpenNotify) {

        client.notify(macAddress, serviceUUID, characterUUID, new BleNotifyResponse() {
            StringBuffer sbCommand = new StringBuffer();

            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                sbCommand.append(new String(value));
                if (value != null && value.length > 1) {
                    Log.d("notify", "----" + bytes2hex03(value));
                    if ((value[value.length - 1] == 10 && value[value.length - 2] == 13) ||
                            value[value.length - 1] == 13) {
                        Log.d("notify", "command:" + sbCommand.toString());
                        String[] commands = sbCommand.toString().split("\\" + BleCommandUtils.
                                DIVISION);

                        int position = Utils.getItemPosition(commands, context);
                        boolean switchState = Integer.parseInt(commands[1]) == 1 ? true : false;
                        Bundle bundle = new Bundle();
                        bundle.putInt(Utils.POSITION, position);
                        bundle.putBoolean(Utils.SWITCH_STATE, switchState);
                        onInterfaceOpenNotify.onNotify(bundle);
                        if (!(!switchState && Integer.parseInt(commands[2]) == 0 &&
                                Integer.parseInt(commands[3]) == 0 &&
                                Integer.parseInt(commands[4]) == 0)) {
                            //非模式为关状态
                            saveNotify(context, position, commands);
                        }

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

    private void saveNotify(Context context, int position, String[] commands) {
        String[] itemNames = context.getResources().getStringArray(R.array.lighting_name);
        int blePosition = Integer.parseInt(commands[3]);
        int popupPosition = Integer.parseInt(commands[4]);
        int bright = Integer.parseInt(commands[5], 16);
        int speed = Integer.parseInt(commands[6], 16);
        boolean pulseState = Integer.parseInt(commands[7]) == 1 ? true : false;
        if (blePosition == 1 || blePosition == 13) {
            popupPosition = 0;
        } else if (blePosition == 2 || blePosition == 14) {
            popupPosition = 1;
        }
        //所有的Item
        String[] popupItems = Utils.getPopWindowItems(context, position);
        if (popupPosition > popupItems.length || position > itemNames.length) {
            throw new ArrayIndexOutOfBoundsException("popupPosition :" + popupPosition +
                    ",length :" + popupItems.length);
        } else {
            String lightName = itemNames[position];
            String sqlName = lightName + popupItems[popupPosition];

            Bundle bundle = new Bundle();
            bundle.putString(Utils.SQL_NAME, lightName);
            bundle.putInt(Utils.POPUP_POSITION, popupPosition);
            bundle.putInt(Utils.SEEK_BAR_PROGRESS_BRIGHT, bright);
            bundle.putInt(Utils.SEEK_BAR_PROGRESS_SPEED, speed);
            bundle.putBoolean(Utils.PULSE_IS_OPEN, pulseState);
            saveLightType(bundle);

            if (position == 0 || position == 9) {
                //item1 9特殊处理
                Bundle bundle2 = new Bundle();
                bundle2.putString(Utils.SQL_NAME, sqlName);
                bundle2.putInt(Utils.SEEK_BAR_PROGRESS_SPEED, speed);
                bundle2.putInt(Utils.SEEK_BAR_PROGRESS_BRIGHT, bright);
                bundle2.putBoolean(Utils.PULSE_IS_OPEN, pulseState);
                saveLightType(bundle2);
            }
            //颜色
            for (int i = 0; i < 7; i++) {
                if (commands.length > i + 9) {
                    saveLightColor(sqlName + i, commands[i + 8]);
                } else {
                    break;
                }
            }
        }
    }

    public void saveLightColor(String name, String rgb) {
        int color = Color.parseColor("#" + rgb);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        RgbColor rgbColor = new RgbColor(name, r, g, b);
        rgbColor.deleteRgbColorByName();
        rgbColor.save();
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
        void onNotify(Bundle bundle);

        void onOpenNotifySuccess();
    }

}
