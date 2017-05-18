package com.example.txtledbluetooth.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.light.model.LightModelImpl;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public class BleCommandUtils {
    public static final String DIVISION = "$";
    public static final String HEAD = "GP" + DIVISION;
    public static final String END_MARK = "\r\n";

    //模式指令
    public static final String MOON_LIGHT1 = "lmol";
    public static final String MOON_LIGHT2 = "lmpl";
    public static final String FIREWORKS = "lfwk";
    public static final String HOT_WHEELS = "lhwl";
    public static final String SPECTRUM = "lrbw";
    public static final String FULL_SPECTRUM = "lfrb";
    public static final String PULSATE = "lclg";
    public static final String MORPH = "lmop";
    public static final String BEAT_METER = "lhst";
    public static final String CYCLE_ALL = "lcbn";
    public static final String CYCLE = "lclr";
    public static final String WAVE1 = "lwav";
    public static final String WAVE2 = "lwbv";
    public static final String SOLO = "lsol";
    public static final String MOOD = "lmod";
    public static final String AURORA = "laur";

    public static final String CLOSE_LIGHT = "etof";

    //指令类型
    public static final String COLOR_DATA = "YSD" + DIVISION;
    public static final String MODIFY_COLOR = "DYS" + DIVISION;
    public static final String MODIFY_BRIGHTNESS = "LDD" + DIVISION + "00";
    public static final String MODIFY_SPEED = "SDD" + DIVISION + "00";
    public static final String CONTROL_DATA = "KZD" + DIVISION;

    //其他设置
    public static final String LIGHT_SPEED = "espd,";
    public static final String LIGHT_BRIGHT = "elux,";
    public static final String CLOSE = HEAD + CLOSE_LIGHT + DIVISION + END_MARK;
    public static final String RESET = HEAD + "erst" + DIVISION;
    public static final String CLOSE_SOUND = HEAD + "esvt:0" + DIVISION;
    public static final String OPEN_SOUND = HEAD + "esvt:1" + DIVISION;


    //灯光速度
    public static String getLightSpeedCommand(String lightNo, String speedHex) {
        return HEAD + lightNo + DIVISION + MODIFY_SPEED + speedHex + DIVISION + END_MARK;
    }

    //灯光亮度
    public static String getLightBrightCommand(String lightNo, String brightHex) {
        return HEAD + lightNo + DIVISION + MODIFY_BRIGHTNESS + brightHex + DIVISION + END_MARK;
    }

    public static String getLightNo(int position, boolean isFirstItem) {
        String lightNo = "";
        switch (position) {
            case 0:
                if (isFirstItem) {
                    lightNo = MOON_LIGHT1;
                } else {
                    lightNo = MOON_LIGHT2;
                }

                break;
            case 1:
                lightNo = FIREWORKS;
                break;
            case 2:
                lightNo = HOT_WHEELS;
                break;
            case 3:
                lightNo = SPECTRUM;
                break;
            case 4:
                lightNo = FULL_SPECTRUM;
                break;
            case 5:
                lightNo = PULSATE;
                break;
            case 6:
                lightNo = MORPH;
                break;
            case 7:
                lightNo = BEAT_METER;
                break;
            case 8:
                lightNo = CYCLE_ALL;
                break;
            case 9:
                lightNo = CYCLE;
                break;
            case 10:
                if (isFirstItem) {
                    lightNo = WAVE1;
                } else {
                    lightNo = WAVE2;
                }
                break;
            case 11:
                lightNo = SOLO;
                break;
            case 12:
                lightNo = MOOD;
                break;
            case 13:
                lightNo = AURORA;
                break;
        }
        return lightNo;
    }

    public static String getOpenLightCommand(Context context) {
        int position = SharedPreferenceUtils.getClickPosition(context);
        String lightName = context.getResources().getStringArray(R.array.lighting_name)[position];
        return getItemCommandByType(context, position, -1, lightName);
    }

    public static String getItemCommandByType(Context context, int position, String lightName) {
        return getItemCommandByType(context, position, -1, lightName);
    }

    public static String getItemCommandByType(Context context, int position, int popupPosition,
                                              String lightName) {
        boolean isFirstItem = false;
        StringBuffer command = new StringBuffer("FF0");
        if (popupPosition == -1) {
            List<LightType> lightTypeList = LightType.getLightTypeList(lightName);
            if (lightTypeList != null && lightTypeList.size() > 0) {
                LightType lightType = lightTypeList.get(0);
                popupPosition = lightType.getPopupPosition();
            } else {
                popupPosition = 0;
            }
        }
        String[] popupItems = Utils.getPopWindowItems(context, position);
        String[] colors = RgbColor.getRgbColorStr(lightName + popupItems[popupPosition]);
        switch (position) {
            case 0:
                isFirstItem = popupPosition == 0 ? true : false;
                if (isFirstItem) {
                    command.append(1 + DIVISION + "ffffff" + DIVISION);
                } else {
                    command.append(1 + DIVISION + colors[0] + DIVISION);
                }
                break;
            case 3:
            case 4:
            case 8:
            case 9:
                command.append(popupPosition + DIVISION + colors[0] + DIVISION);
                break;
            case 10:
                isFirstItem = popupPosition == 0 ? true : false;
                int count = popupPosition == 0 ? 1 : 0;
                command.append(count + DIVISION + colors[0] + DIVISION);
                break;
            case 1:
            case 2:
            case 5:
            case 6:
            case 7:
            case 11:
            case 12:
                int count2 = getColorCount(popupItems[popupPosition], position);
                command.append(count2 + DIVISION);
                for (int i = 0; i < count2; i++) {
                    command.append(colors[i] + DIVISION);
                }
                break;
        }
        return HEAD + getLightNo(position, isFirstItem) + DIVISION + COLOR_DATA + command.toString() +
                END_MARK;
    }

    private static int getColorCount(String popupItem, int position) {
        int count = 0;
        if (popupItem.contains("1") || position == 11) {
            count = 1;
        } else if (popupItem.contains("2")) {
            count = 2;
        } else if (popupItem.contains("3")) {
            count = 3;
        } else if (popupItem.contains("7") || position == 12) {
            count = 7;
        } else if (position == 7) {
            count = 5;
        }
        return count;
    }

    public static String updateLightColor(String lightNo, int position, String color) {
        String command = "010" + position + DIVISION;
        return HEAD + lightNo + DIVISION + MODIFY_COLOR + command + color + DIVISION + END_MARK;
    }

    //分包
    public static void divideFrameBleSendData(byte[] data, BluetoothClient client,
                                              String macAddress, UUID serviceUUID,
                                              UUID characterUUID, final LightModelImpl.
            OnInterfaceWriteCommand onInterfaceWriteCommand) {
        int tmpLen = data.length;
        int start = 0;
        int end = 0;
        final boolean[] isShowDialog = {true};
        while (tmpLen > 0) {
            byte[] sendData = new byte[21];
            if (tmpLen >= 20) {
                end += 20;
                sendData = Arrays.copyOfRange(data, start, end);
                start += 20;
                tmpLen -= 20;
            } else {
                end += tmpLen;
                sendData = Arrays.copyOfRange(data, start, end);
                tmpLen = 0;
            }
            client.write(macAddress, serviceUUID, characterUUID, sendData,
                    new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            if (code == -1 && isShowDialog[0]) {
                                Log.d("tag", code + "");
                                onInterfaceWriteCommand.onWriteFailure();
                                isShowDialog[0] = false;
                            }
                        }
                    });
        }
    }
}
