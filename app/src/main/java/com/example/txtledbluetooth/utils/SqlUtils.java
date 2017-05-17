package com.example.txtledbluetooth.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.bean.RgbColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KomoriWu
 * on 2017-05-16.
 */

public class SqlUtils {
    public static final String DEFAULT_COLORS = "default_colors";

    public static void saveDefaultColors(Context context) {
        String[] itemNames = context.getResources().getStringArray(R.array.lighting_name);

        new RgbColor(DEFAULT_COLORS + itemNames[0] + Utils.getPopWindowItems(context, 0)[0] + 0,
                255, 255, 255).save();

        new RgbColor(DEFAULT_COLORS + itemNames[5] + Utils.getPopWindowItems(context, 5)[1] + 0,
                255, 255, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[5] + Utils.getPopWindowItems(context, 5)[2] + 1,
                0, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[5] + Utils.getPopWindowItems(context, 5)[2] + 3,
                0, 0, 255).save();

        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[0] + 0,
                255, 255, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[0] + 1,
                0, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[1] + 1,
                0, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[1] + 2,
                0, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 0,
                255, 255, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 1,
                0, 255, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 2,
                0, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 3,
                255, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 4,
                255, 0, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 5,
                255, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 5,
                0, 255, 0).save();

        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 0,
                0, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 1,
                0, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 2,
                255, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 3,
                255, 125, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 4,
                255, 0, 0).save();

        new RgbColor(DEFAULT_COLORS + itemNames[10] + Utils.getPopWindowItems(context, 10)[0] + 0,
                0, 0, 255).save();

        new RgbColor(DEFAULT_COLORS + itemNames[11] + Utils.getPopWindowItems(context, 11)[0] + 0,
                255, 125, 0).save();

        new RgbColor(DEFAULT_COLORS + itemNames[12] + Utils.getPopWindowItems(context, 12)[0] + 0,
                245, 208, 20).save();
        new RgbColor(DEFAULT_COLORS + itemNames[12] + Utils.getPopWindowItems(context, 12)[0] + 1,
                245, 208, 20).save();
        new RgbColor(DEFAULT_COLORS + itemNames[12] + Utils.getPopWindowItems(context, 12)[0] + 2,
                245, 122, 20).save();
        new RgbColor(DEFAULT_COLORS + itemNames[12] + Utils.getPopWindowItems(context, 12)[0] + 3,
                245, 122, 20).save();
        new RgbColor(DEFAULT_COLORS + itemNames[12] + Utils.getPopWindowItems(context, 12)[0] + 4,
                245, 122, 20).save();
        new RgbColor(DEFAULT_COLORS + itemNames[12] + Utils.getPopWindowItems(context, 12)[0] + 5,
                245, 208, 20).save();
        new RgbColor(DEFAULT_COLORS + itemNames[12] + Utils.getPopWindowItems(context, 12)[0] + 6,
                245, 208, 20).save();


        new RgbColor(DEFAULT_COLORS + 0, 255, 0, 0).save(); //view 1 红色
        new RgbColor(DEFAULT_COLORS + 1, 255, 255, 0).save();//view 2 黄色
        new RgbColor(DEFAULT_COLORS + 2, 0, 255, 0).save();//view 3 绿色
        new RgbColor(DEFAULT_COLORS + 3, 0, 255, 255).save();//view 4 浅绿
        new RgbColor(DEFAULT_COLORS + 4, 0, 0, 255).save();//view 5 蓝色
        new RgbColor(DEFAULT_COLORS + 5, 255, 0, 255).save();//view 6 紫色
        new RgbColor(DEFAULT_COLORS + 6, 255, 255, 255).save();//view 7 白色
    }

    public static List<RgbColor> getDefaultColors(String name, int position) {
        List<RgbColor> rgbColorList = RgbColor.getRgbColorList(DEFAULT_COLORS + name + position);
        if (rgbColorList == null || rgbColorList.size() == 0) {
            rgbColorList = RgbColor.getRgbColorList(DEFAULT_COLORS + position);
        }
        return rgbColorList;
    }

}