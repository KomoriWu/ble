package com.example.txtledbluetooth.bean;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * Created by KomoriWu
 * on 2017-05-12.
 */
@Table(name = "light_rgb3")
public class LightRgb3 extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("color0")
    private String color0;
    @SerializedName("color1")
    private String color1;
    @SerializedName("color2")
    private String color2;

    public LightRgb3() {
    }

    public LightRgb3(String name, String color0, String color1, String color2) {
        this.name = name;
        this.color0 = color0;
        this.color1 = color1;
        this.color2 = color2;
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

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }
}
