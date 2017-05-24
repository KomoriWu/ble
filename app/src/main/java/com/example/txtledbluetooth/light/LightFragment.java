package com.example.txtledbluetooth.light;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.base.BaseFragment;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.Lighting;
import com.example.txtledbluetooth.bean.MusicInfo;
import com.example.txtledbluetooth.light.presenter.LightPresenter;
import com.example.txtledbluetooth.light.presenter.LightPresenterImpl;
import com.example.txtledbluetooth.light.view.LightView;
import com.example.txtledbluetooth.utils.AlertUtils;
import com.example.txtledbluetooth.utils.MusicUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KomoriWu
 * on 2017-04-19.
 */

public class LightFragment extends BaseFragment implements LightView, LightAdapter.
        OnItemClickListener, LightAdapter.OnIvRightClickListener {
    @BindView(R.id.tv_switch)
    TextView tvSwitch;
    @BindView(R.id.switch_view)
    Switch aSwitch;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LightAdapter mLightAdapter;
    private LightPresenter mLightPresenter;
    private String mLightName;
    private String[] mLightNames;
    private boolean mIsReturn;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, null);
        ButterKnife.bind(this, view);
        mLightPresenter = new LightPresenterImpl(this, getActivity());
        mLightNames = getActivity().getResources().getStringArray(R.array.lighting_name);
        initLightData();
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                tvSwitch.setText(isChecked ? getString(R.string.on_capital) :
                        getString(R.string.off_capital));
                if (mIsReturn) return;
                mLightPresenter.operateSwitchBluetooth(isChecked);
            }
        });
        aSwitch.setChecked(true);
        return view;
    }

    private void initLightData() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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


}
