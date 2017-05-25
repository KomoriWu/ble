package com.example.txtledbluetooth.light;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.base.BaseFragment;
import com.example.txtledbluetooth.bean.Lighting;
import com.example.txtledbluetooth.light.presenter.LightPresenter;
import com.example.txtledbluetooth.light.presenter.LightPresenterImpl;
import com.example.txtledbluetooth.light.view.LightView;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KomoriWu
 * on 2017-04-19.
 */

public class LightFragment extends BaseFragment implements LightView, LightAdapter.
        OnItemClickListener, LightAdapter.OnIvRightClickListener {
    private static final int TIMER_DELAY = 1000;
    private static final int TIMER_PERIOD = 100;
    private static final int TIMER_MESSAGE = 1;
    @BindView(R.id.tv_switch)
    TextView tvSwitch;
    @BindView(R.id.switch_view)
    Switch aSwitch;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LightAdapter mLightAdapter;
    private LinearLayoutManager mLayoutManager;
    private LightPresenter mLightPresenter;
    private String mLightName;
    private String[] mLightNames;
    private boolean mIsReturn;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == TIMER_MESSAGE) {
                mLightPresenter.openNotify();
                stopTimer();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, null);
        ButterKnife.bind(this, view);
        mLightPresenter = new LightPresenterImpl(this, getActivity());
        mLightNames = getActivity().getResources().getStringArray(R.array.lighting_name);
        initLightData();
        tvSwitch.setText(aSwitch.isChecked() ? getString(R.string.on_capital) :
                getString(R.string.off_capital));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                tvSwitch.setText(isChecked ? getString(R.string.on_capital) :
                        getString(R.string.off_capital));
                if (mIsReturn) return;
                mLightPresenter.operateSwitchBluetooth(isChecked);
            }
        });
        startTimer();
        return view;
    }

    private void initLightData() {
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mLightAdapter = new LightAdapter(getActivity(), this, this);
        recyclerView.setAdapter(mLightAdapter);
        mLightPresenter.showLightData();
    }

    public void showLightData(ArrayList<Lighting> lightingList, List<Boolean> list) {
        mLightAdapter.setLightingList(lightingList, list);
    }

    @Override
    public void editLight(int id) {
        mLightName = mLightNames[id];
        Intent intent = new Intent(getActivity(), EditLightActivity.class);
        intent.putExtra(Utils.LIGHT_MODEL_ID, id);
        intent.putExtra(Utils.LIGHT_MODEL_NAME, mLightName);
        startActivity(intent);
        setSwitchChecked();
    }

    @Override
    public void onWriteFailure() {
//        AlertUtils.showAlertDialog(getActivity(), R.string.ble_write_failure_hint);
    }

    @Override
    public void onNotify(Bundle bundle) {
        int position = Utils.getItemPosition(bundle.getInt(Utils.POSITION), getActivity());
        boolean switchState = bundle.getBoolean(Utils.SWITCH_STATE);
        mIsReturn = true;
        aSwitch.setChecked(switchState);
        mIsReturn = false;
        mLayoutManager.scrollToPositionWithOffset(position, 0);
        mLightAdapter.setSelectItem(position);
    }


    @Override
    public void onItemClick(View view, int position) {
        mLightName = mLightNames[position];
        mLightPresenter.operateItemBluetooth(mLightName, position);
        mLightPresenter.operateItemSeekBar(mLightName, position);
        setSwitchChecked();
    }

    private void setSwitchChecked() {
        if (!aSwitch.isChecked()) {
            mIsReturn = true;
            aSwitch.setChecked(true);
            mIsReturn = false;
        }
    }

    @Override
    public void onIvRightClick(View view, int position) {
        mLightPresenter.operateTvRightBluetooth(position);
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    int status = MyApplication.getBluetoothClient(getActivity()).getConnectStatus(
                            SharedPreferenceUtils.getMacAddress(getActivity()));
                    Message message = new Message();
                    message.what = TIMER_MESSAGE;
                    if (status == Constants.STATUS_DEVICE_CONNECTED) {
                        mTimerHandler.sendMessage(message);
                    }
                }
            };
        }
        if (mTimer != null && mTimerTask != null) {
            mTimer.schedule(mTimerTask, TIMER_DELAY, TIMER_PERIOD);
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }


}
