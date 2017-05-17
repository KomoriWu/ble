package com.example.txtledbluetooth.bean;

import com.example.txtledbluetooth.utils.Utils;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KomoriWu
 * on 2017-05-17.
 */
@Table(name = "light_sb_progress")
public class LightSbProgress extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("speed")
    private int speed;
    @SerializedName("brightness")
    private int brightness;

    public LightSbProgress() {
    }

    public LightSbProgress(String name, int speed, int brightness) {
        this.name = name;
        this.speed = speed;
        this.brightness = brightness;
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

    public static HashMap<String, Integer> getSbProgressMap(String name, int position) {
        List<LightSbProgress> sbProgressList = LightSbProgress.find(LightSbProgress.class,
                "name = ?", name);
        HashMap<String, Integer> hashMap = new HashMap<>();
        if (sbProgressList == null || sbProgressList.size() == 0) {
            hashMap = Utils.getSeekBarProgress(position);
        } else {
            hashMap.put(Utils.SEEK_BAR_PROGRESS_BRIGHT, sbProgressList.get(0).getBrightness());
            hashMap.put(Utils.SEEK_BAR_PROGRESS_SPEED, sbProgressList.get(0).getSpeed());
        }
        return hashMap;
    }

    public void deleteSbProgressByName() {
        LightSbProgress.deleteAll(LightSbProgress.class, "name = ?", name);
    }

}
