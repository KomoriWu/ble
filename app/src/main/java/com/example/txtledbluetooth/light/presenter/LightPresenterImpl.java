package com.example.txtledbluetooth.light.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.Lighting;
import com.example.txtledbluetooth.light.model.LightModel;
import com.example.txtledbluetooth.light.model.LightModelImpl;
import com.example.txtledbluetooth.light.view.LightView;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

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
    private BluetoothClient mClient;

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
        mClient = MyApplication.getBluetoothClient(mContext);
        mMacAddress = SharedPreferenceUtils.getMacAddress(mContext);
    }


    @Override
    public void operateItemBluetooth(String lightName, int position) {
        String command = BleCommandUtils.getItemCommandByType(mContext, position, lightName);
        writeCommand(command);
        SharedPreferenceUtils.saveClickPosition(mContext, position);
    }

    @Override
    public void operateItemSeekBar(String lightName, int position) {
        int popupPosition = mLightModel.getLightType(lightName);
        String sqlName = position == 0 || position == 9 ? lightName +
                Utils.getPopWindowItems(mContext, position)[popupPosition] : lightName;
        HashMap<String, Integer> hashMap = LightType.getSbProgressMap(sqlName, position);
        int bright = hashMap.get(Utils.SEEK_BAR_PROGRESS_BRIGHT);
        int speed = hashMap.get(Utils.SEEK_BAR_PROGRESS_SPEED);
        String lightNo = BleCommandUtils.getLightNo(position, popupPosition == 0 ? true : false);
        if (Utils.isSBarSpeedVisible(position)) {
            writeCommand(BleCommandUtils.getLightSpeedCommand(lightNo, Integer.toHexString(speed)));
        }
        if (Utils.isSBarBrightVisible(position)) {
            writeCommand(BleCommandUtils.getLightBrightCommand(lightNo, Integer.toHexString(bright)));
        }
    }

    @Override
    public void operateTvRightBluetooth(int id) {
        mLightView.editLight(id);
    }

    @Override
    public void operateSwitchBluetooth(boolean isChecked) {
        String command = isChecked ? BleCommandUtils.OPEN : BleCommandUtils.CLOSE;
        writeCommand(command);
    }

    @Override
    public void openNotify() {
        String serviceUUID = SharedPreferenceUtils.getReceiveService(mContext);
        String characterUUID = SharedPreferenceUtils.getReceiveCharacter(mContext);
        UUID uuidService = null, uuidCharacter = null;
        if (!TextUtils.isEmpty(serviceUUID)) {
            uuidService = UUID.fromString(serviceUUID);
        }
        if (!TextUtils.isEmpty(characterUUID)) {
            uuidCharacter = UUID.fromString(characterUUID);
        }
        if (!TextUtils.isEmpty(mMacAddress)) {
            mLightModel.openNotify(mClient, mMacAddress, uuidService, uuidCharacter, new
                    LightModelImpl.OnInterfaceOpenNotify() {
                        @Override
                        public void onNotify(int position) {
                            mLightView.onNotify(Utils.getItemPosition(position, mContext));
                        }

                        @Override
                        public void onOpenNotifySuccess() {
                            writeCommand(BleCommandUtils.BACK_NOTIFY);
                        }

                    });

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

    private void writeCommand(String command) {
        Log.d("BLE Write Command:", command);
        if (!TextUtils.isEmpty(command) && !TextUtils.isEmpty(mMacAddress)) {
            mLightModel.WriteCommand(mClient, mMacAddress,
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
