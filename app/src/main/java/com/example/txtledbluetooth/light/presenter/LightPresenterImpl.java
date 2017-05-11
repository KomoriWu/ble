package com.example.txtledbluetooth.light.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.bean.Lighting;
import com.example.txtledbluetooth.light.model.LightModel;
import com.example.txtledbluetooth.light.model.LightModelImpl;
import com.example.txtledbluetooth.light.view.LightView;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-22.
 */

public class LightPresenterImpl implements LightPresenter {
    private LightView mLightView;
    private Context mContext;
    private LightModel mLightModel;
    private String mMacAddress;
    private UUID mServiceUUID;
    private UUID mCharacterUUID;

    public LightPresenterImpl(LightView mLightView, Context mContext) {
        this.mLightView = mLightView;
        this.mContext = mContext;
        mLightModel = new LightModelImpl();
        String serviceUUID = SharedPreferenceUtils.getSendService(mContext);
        String characterUUID = SharedPreferenceUtils.getSendCharacter(mContext);
        if (!TextUtils.isEmpty(serviceUUID)) {
            mServiceUUID = UUID.fromString(serviceUUID);
        }
        if (!TextUtils.isEmpty(characterUUID)) {
            mCharacterUUID = UUID.fromString(characterUUID);
        }
        mMacAddress = SharedPreferenceUtils.getMacAddress(mContext);
    }


    @Override
    public void operateItemBluetooth(boolean mIsChecked, int id) {
        if (mIsChecked) {
            String command = "";
            switch (id) {
                case 0:
                    command = BleCommandUtils.MOON_LIGHT_COMMAND;
                    break;
                case 1:
                    command = BleCommandUtils.FIREWORK_COMMAND;
                    break;
                case 2:
                    command = BleCommandUtils.BLUE_SKIES_COMMAND;
                    break;
                case 3:
                    command = BleCommandUtils.RAINBOW_COMMAND;
                    break;
                case 4:
                    command = BleCommandUtils.PULSATE_COMMAND;
                    break;
                case 5:
                    command = BleCommandUtils.GLOW_COMMAND;
                    break;
                case 6:
                    command = BleCommandUtils.MONOCHROME_COMMAND;
                    break;
            }
            if (!TextUtils.isEmpty(command) && !TextUtils.isEmpty(mMacAddress)) {
                mLightModel.WriteCommand(MyApplication.getBluetoothClient(mContext), mMacAddress
                        , mServiceUUID, mCharacterUUID, command);
            }
            SharedPreferenceUtils.saveClickPosition(mContext, id);
        } else {
            mLightView.showHintDialog();
        }
    }

    @Override
    public void operateTvRightBluetooth(int id) {
        mLightView.editLight(id);
    }

    @Override
    public void operateSwitchBluetooth(boolean isChecked) {
        String command = isChecked ? BleCommandUtils.OPEN : BleCommandUtils.CLOSE;
        if (!TextUtils.isEmpty(command) && !TextUtils.isEmpty(mMacAddress)) {
            mLightModel.WriteCommand(MyApplication.getBluetoothClient(mContext),
                    SharedPreferenceUtils.getMacAddress(mContext), mServiceUUID,
                    mCharacterUUID, command);
        }
    }

    @Override
    public void showLightData() {
        new initDataAsyncTask().execute();
    }

    private class initDataAsyncTask extends AsyncTask<Void, Void, ArrayList<Lighting>> {
        ArrayList<Lighting> lightingList;
        List<Boolean> list;

        @Override
        protected ArrayList<Lighting> doInBackground(Void... voids) {
            lightingList = Utils.getLightList(mContext);
            list = new ArrayList<>();
            for (int i = 0; i < lightingList.size(); i++) {
                if (SharedPreferenceUtils.getClickPosition(mContext) == i) {
                    list.add(true);
                } else {
                    list.add(false);
                }
            }
            return lightingList;
        }

        @Override
        protected void onPostExecute(ArrayList<Lighting> lightingArrayList) {
            mLightView.showLightData(lightingArrayList, list);
        }
    }
}
