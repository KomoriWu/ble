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

    void initBleLightColor(int position);

    void updateLightColor(String lightNo, int position, String color);

}
