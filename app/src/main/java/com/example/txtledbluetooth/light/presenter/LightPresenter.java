package com.example.txtledbluetooth.light.presenter;

/**
 * Created by KomoriWu
 * on 2017-04-22.
 */

public interface LightPresenter {
    void operateItemBluetooth(boolean isChecked, int id);
    void operateTvRightBluetooth(int id);
    void operateSwitchBluetooth(boolean isChecked);
    void showLightData();
}
