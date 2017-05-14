package com.example.txtledbluetooth.light.presenter;

import android.view.View;


/**
 * Created by KomoriWu
 * on 2017-04-25.
 */

public interface EditLightPresenter {
    void viewOnclick(View clickView, View bgView);

    void setIsSetOnColorSelectListener(boolean isSetOnColorSelectListener);

    void setLightSpeed(String lightNo, int speed);

    void setLightBrightness(String lightNo, int brightness);

    void operateItemBluetooth(String lightName, int position,int popupPosition);

    void updateLightColor(String lightNo, int viewPosition, String color);

}
