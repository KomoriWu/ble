package com.example.txtledbluetooth.bean;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KomoriWu
 * on 2017-05-12.
 */
@Table(name = "rgb_color")
public class RgbColor extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("r")
    private int r;
    @SerializedName("g")
    private int g;
    @SerializedName("b")
    private int b;
    @SerializedName("x")
    private float x;
    @SerializedName("y")
    private float y;
    @SerializedName("color_int")
    private int colorInt;
    @SerializedName("color_str")
    private String colorStr;

    public RgbColor() {
    }

    public RgbColor(String name, int r, int g, int b, float x, float y, int colorInt, String colorStr) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.x = x;
        this.y = y;
        this.colorInt = colorInt;
        this.colorStr = colorStr;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getColorInt() {
        return colorInt;
    }

    public void setColorInt(int colorInt) {
        this.colorInt = colorInt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public String getColorStr() {
        return colorStr;
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
    }

    public static List<RgbColor> getRgbColorList(String name) {
        return RgbColor.find(RgbColor.class, "name = ?", name);
    }

    public static int[] getRgbColors(String name) {
        int[] colors = new int[7];
        for (int i = 0; i < 7; i++) {
            List<RgbColor> rgbColorList = getRgbColorList(name + i);
            if (rgbColorList != null && rgbColorList.size() > 0) {
                colors[i] = rgbColorList.get(0).getColorInt();
            }
        }

        return colors;
    }

    public static String[] getRgbColorStr(String name) {
        String[] colors = new String[7];
        for (int i = 0; i < 7; i++) {
            List<RgbColor> rgbColorList = getRgbColorList(name + i);
            if (rgbColorList != null && rgbColorList.size() > 0) {
                colors[i] = rgbColorList.get(0).getColorStr();
            }
        }

        return colors;
    }

    public void deleteRgbColorByName() {
        RgbColor.deleteAll(RgbColor.class, "name = ?", name);
    }
}
