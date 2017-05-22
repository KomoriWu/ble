package com.example.txtledbluetooth.main.model;

import android.content.Context;
import android.text.TextUtils;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.List;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public class MainModelImpl implements MainModel {
    private static final int SEARCH_TIMEOUT = 5000;
    private static final int SEARCH_TIMEOUT_NUMBER = 3;

    @Override
    public void initBle(final Context context, final BluetoothClient client, final BleConnectOptions
            bleConnectOptions, final OnInitBleListener
                                onInitBleListener) {
        if (client.isBleSupported()) {
            if (client.isBluetoothOpened()) {
                SearchRequest request = new SearchRequest.Builder()
                        .searchBluetoothLeDevice(SEARCH_TIMEOUT, SEARCH_TIMEOUT_NUMBER).build();
                client.search(request, new SearchResponse() {
                    @Override
                    public void onSearchStarted() {

                    }

                    @Override
                    public void onDeviceFounded(SearchResult device) {
//                        if (device.getName().contains(Utils.BLE_NAME)) {
                            if (device.getAddress().contains("6A")) {  //调试
                                client.stopSearch();
                                connBle(context, client, bleConnectOptions, device.getAddress(),
                                        device.getName(), onInitBleListener);
//                            }
                        }
                    }

                    @Override
                    public void onSearchStopped() {
                        onInitBleListener.OnException(context.getString(R.string.search_stop));
                    }

                    @Override
                    public void onSearchCanceled() {
                    }
                });


            } else {
                onInitBleListener.onFailure(context.getString(R.string.open_ble));
            }
        } else {
            onInitBleListener.OnException(context.getString(R.string.no_support_ble));
        }

    }

    private void connBle(final Context context, BluetoothClient client, BleConnectOptions
            bleConnectOptions, final String address, final String name,
                         final OnInitBleListener onInitBleListener) {
        client.connect(address, bleConnectOptions, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    List<BleGattService> services = profile.getServices();
                    for (BleGattService service : services) {
                        if (service.getUUID().toString().contains(Utils.SEND_SERVICE)) {
                            List<BleGattCharacter> characters = service.getCharacters();
                            for (BleGattCharacter character : characters) {
                                //save uuid
                                SharedPreferenceUtils.saveMacAddress(context, address);
                                SharedPreferenceUtils.saveSendService(context,
                                        service.getUUID().toString());
                                SharedPreferenceUtils.saveSendCharacter(context,
                                        character.getUuid().toString());
                            }
                        }

                    }
                    onInitBleListener.onSuccess(name);
                } else {
                    onInitBleListener.OnException(context.getString(R.string.conn_timeout));
                }
            }
        });
    }

    public interface OnInitBleListener {
        void onSuccess(String name);

        void onFailure(String message);

        void OnException(String exception);

    }
}
