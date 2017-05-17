package com.example.txtledbluetooth.bean;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KomoriWu
 * on 2017-05-12.
 */
@Table(name = "light_type")
public class LightType extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("popup_position")
    private int popupPosition;


    public LightType() {
    }

    public LightType(String name, int popupPosition) {
        this.name = name;
        this.popupPosition = popupPosition;
    }


    public int getPopupPosition() {
        return popupPosition;
    }

    public void setPopupPosition(int popupPosition) {
        this.popupPosition = popupPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<LightType> getLightTypeList(String name) {
        return LightType.find(LightType.class, "name = ?", name);
    }

    public void deleteLightTypeByName() {
        LightType.deleteAll(LightType.class, "name = ?", name);
    }
}
