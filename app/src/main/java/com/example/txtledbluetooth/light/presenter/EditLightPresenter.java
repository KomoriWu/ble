package com.example.txtledbluetooth.light.presenter;

import android.os.Bundle;
import android.view.View;

import com.example.txtledbluetooth.bean.RgbColor;


/**
 * Created by KomoriWu
 * on 2017-04-25.
 */

public interface EditLightPresenter {
    void viewOnclick(View clickView, View bgView,String sqlName, int position);

    void setIsSetOnColorSelectListener(boolean isSetOnColorSelectListener);

    void setLightSpeed(String lightNo, int speed,Bundle bundle);

    void setLightBrightness(String lightNo, int brightness,Bundle bundle);

    void operateItemBluetooth(String lightName, int position,int popupPosition);

    void updateLightColor(String lightNo, int viewPosition, String color, Bundle data);
    void saveLightType(String name, int popupPosition);


    RgbColor getLightColor(String sqlName, int position);

}
