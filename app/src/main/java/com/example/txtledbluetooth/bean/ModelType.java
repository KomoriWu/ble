package com.example.txtledbluetooth.bean;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * Created by KomoriWu
 * on 2017-05-12.
 */
@Table(name = "model_type")
public class ModelType extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("lightRgb1")
    private LightRgb1 lightRgb1;
    @SerializedName("lightRgb2")
    private LightRgb2 lightRgb2;
    @SerializedName("lightRgb3")
    private LightRgb3 lightRgb3;
    @SerializedName("lightRgb7")
    private LightRgb7 lightRgb7;

    public ModelType() {
    }

    public ModelType(String name, LightRgb1 lightRgb1, LightRgb2 lightRgb2, LightRgb3 lightRgb3,
                     LightRgb7 lightRgb7) {
        this.name = name;
        this.lightRgb1 = lightRgb1;
        this.lightRgb2 = lightRgb2;
        this.lightRgb3 = lightRgb3;
        this.lightRgb7 = lightRgb7;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LightRgb1 getLightRgb1() {
        return lightRgb1;
    }

    public void setLightRgb1(LightRgb1 lightRgb1) {
        this.lightRgb1 = lightRgb1;
    }

    public LightRgb2 getLightRgb2() {
        return lightRgb2;
    }

    public void setLightRgb2(LightRgb2 lightRgb2) {
        this.lightRgb2 = lightRgb2;
    }

    public LightRgb3 getLightRgb3() {
        return lightRgb3;
    }

    public void setLightRgb3(LightRgb3 lightRgb3) {
        this.lightRgb3 = lightRgb3;
    }

    public LightRgb7 getLightRgb7() {
        return lightRgb7;
    }

    public void setLightRgb7(LightRgb7 lightRgb7) {
        this.lightRgb7 = lightRgb7;
    }
}
