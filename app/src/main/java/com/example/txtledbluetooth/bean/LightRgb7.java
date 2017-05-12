package com.example.txtledbluetooth.bean;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * Created by KomoriWu
 * on 2017-05-12.
 */
@Table(name = "light_rgb7")
public class LightRgb7 extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("color0")
    private String color0;
    @SerializedName("color1")
    private String color1;
    @SerializedName("color2")
    private String color2;
    @SerializedName("color3")
    private String color3;
    @SerializedName("color4")
    private String color4;
    @SerializedName("color5")
    private String color5;
    @SerializedName("color6")
    private String color6;

    public LightRgb7() {
    }

    public LightRgb7(String name, String color0, String color1, String color2, String color3,
                     String color4, String color5, String color6) {
        this.name = name;
        this.color0 = color0;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.color5 = color5;
        this.color6 = color6;
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

    public String getColor3() {
        return color3;
    }

    public void setColor3(String color3) {
        this.color3 = color3;
    }

    public String getColor4() {
        return color4;
    }

    public void setColor4(String color4) {
        this.color4 = color4;
    }

    public String getColor5() {
        return color5;
    }

    public void setColor5(String color5) {
        this.color5 = color5;
    }

    public String getColor6() {
        return color6;
    }

    public void setColor6(String color6) {
        this.color6 = color6;
    }
}
