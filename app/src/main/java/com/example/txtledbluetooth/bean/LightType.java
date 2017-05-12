package com.example.txtledbluetooth.bean;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KomoriWu
 * on 2017-05-12.
 */
@Table(name = "light_model")
public class LightType extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("speed")
    private int speed;
    @SerializedName("brightness")
    private int brightness;
    @SerializedName("rgb_color_list")
    private ArrayList<RgbColor> rgbColorArrayList;

    public LightType() {
    }

    public LightType(String name, int speed, int brightness, ArrayList<RgbColor> rgbColorArrayList) {
        this.name = name;
        this.speed = speed;
        this.brightness = brightness;
        this.rgbColorArrayList = rgbColorArrayList;
    }

    public ArrayList<RgbColor> getRgbColorArrayList() {
        return rgbColorArrayList;
    }

    public void setRgbColorArrayList(ArrayList<RgbColor> rgbColorArrayList) {
        this.rgbColorArrayList = rgbColorArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

}
