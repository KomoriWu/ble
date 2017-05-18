package com.example.txtledbluetooth.light.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.bean.LightType;
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
    public void operateItemBluetooth(String lightName, int id) {
        String command = BleCommandUtils.getItemCommandByType(mContext, id, lightName);
        writeCommand(command);
        SharedPreferenceUtils.saveClickPosition(mContext, id);
    }

    @Override
    public void operateTvRightBluetooth(int id) {
        mLightView.editLight(id);
    }

    @Override
    public void operateSwitchBluetooth(boolean isChecked) {
        String command = isChecked ? BleCommandUtils.getOpenLightCommand(mContext) :
                BleCommandUtils.CLOSE;
        writeCommand(command);
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

    private void writeCommand(String command) {
        Log.d("BLE Write Command:", command);
        if (!TextUtils.isEmpty(command) && !TextUtils.isEmpty(mMacAddress)) {
            mLightModel.WriteCommand(MyApplication.getBluetoothClient(mContext), mMacAddress,
                    mServiceUUID, mCharacterUUID, command, new
                            LightModelImpl.OnInterfaceWriteCommand() {
                                @Override
                                public void onWriteFailure() {
                                    mLightView.onWriteFailure();
                                }
                            });
        }
    }
}
