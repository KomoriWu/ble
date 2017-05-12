package com.example.txtledbluetooth.bean;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * Created by KomoriWu
 * on 2017-05-12.
 */
@Table(name = "light_rgb1")
public class LightRgb1 extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("color0")
    private String color0;

    public LightRgb1() {
    }

    public LightRgb1(String name, String color0) {
        this.name = name;
        this.color0 = color0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor0() {
        return color0;
    }

    public void setColor0(String color0) {
        this.color0 = color0;
    }
}
