package com.example.txtledbluetooth.utils;

import android.content.Context;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.RgbColor;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public class BleCommandUtils {
    public static final String HEAD = "GP$";
    public static final String END = "$";

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
    public static final String COLOR_DATA = "YSD$";
    public static final String MODIFY_COLOR = "DYS$";
    public static final String MODIFY_BRIGHTNESS = "LDD$00";
    public static final String MODIFY_SPEED = "SDD$00";
    public static final String CONTROL_DATA = "KZD$";

    //其他设置
    public static final String LIGHT_SPEED = "espd,";
    public static final String LIGHT_BRIGHT = "elux,";
    public static final String CLOSE = HEAD + CLOSE_LIGHT + END;
    public static final String RESET = HEAD + "erst" + END;
    public static final String CLOSE_SOUND = HEAD + "esvt:0" + END;
    public static final String OPEN_SOUND = HEAD + "esvt:1" + END;


    //灯光速度
    public static String getLightSpeedCommand(String lightNo, String speedHex) {
        return HEAD + lightNo + "$" + MODIFY_SPEED + speedHex + END;
    }

    //灯光亮度
    public static String getLightBrightCommand(String lightNo, String brightHex) {
        return HEAD + lightNo + "$" + MODIFY_BRIGHTNESS + brightHex + END;
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
                if (!isFirstItem) {
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
            case 3:
            case 4:
            case 8:
            case 9:
            case 11:
            case 10:
                isFirstItem = popupPosition == 0 ? true : false;
                command.append(popupPosition + "$" + colors[0] + END);
                break;
            case 1:
            case 2:
            case 5:
            case 6:
            case 7:
            case 12:
                int count = getColorCount(popupItems[popupPosition], position);
                command.append(count + "$");
                for (int i = 0; i < count; i++) {
                    command.append(colors[i] + END);
                }
                break;
        }
        return HEAD + getLightNo(position, isFirstItem) + "$" + COLOR_DATA + command.toString();
    }

    private static int getColorCount(String popupItem, int position) {
        int count = 0;
        if (popupItem.contains("1")) {
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
        String command = "010" + position + "$";
        return HEAD + lightNo + "$" + MODIFY_COLOR + command + color + END;
    }
    //分包
    public static void divideFrameBleSendData(byte[] data, BluetoothClient client,
                                        String macAddress, UUID serviceUUID, UUID characterUUID) {
        int tmpLen = data.length;
        int start = 0;
        int end = 0;
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
            client.write(macAddress, serviceUUID,
                    characterUUID, sendData,
                    new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                        }
                    });

        }
    }

}
