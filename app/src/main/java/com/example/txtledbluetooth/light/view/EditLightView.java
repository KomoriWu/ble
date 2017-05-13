package com.example.txtledbluetooth.light.view;

/**
 * Created by KomoriWu
 * on 2017-04-25.
 */

public interface EditLightView {
    void showPopWindow();
    void setTvColor(int color,float x, float y);
    void revertColor();
    void setPaintPixel();
}
