package com.example.txtledbluetooth.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.about.AboutFragment;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.base.BaseActivity;
import com.example.txtledbluetooth.dashboard.DashboardFragment;
import com.example.txtledbluetooth.light.LightFragment;
import com.example.txtledbluetooth.main.presenter.MainPresenter;
import com.example.txtledbluetooth.main.presenter.MainPresenterImpl;
import com.example.txtledbluetooth.main.view.MainView;
import com.example.txtledbluetooth.music.MusicFragment;
import com.example.txtledbluetooth.setting.SettingFragment;
import com.example.txtledbluetooth.sources.SourcesFragment;
import com.example.txtledbluetooth.utils.AlertUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainView {
    public static final int PERMISSION_REQUEST_CODE = 100;
    public static final int REQUEST_CODE_SETTING = 1;
    public static final int REQUEST_CODE_ALLOW = 2;
    @BindView(R.id.frame_content)
    FrameLayout frameContent;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tv_toolbar_right)
    TextView tvScan;
    private ActionBarDrawerToggle mDrawerToggle;
    private MainPresenter mPresenter;
    private DashboardFragment mDashboardFragment;
    private SourcesFragment mSourcesFragment;
    private MusicFragment mMusicFragment;
    private LightFragment mLightFragment;
    private SettingFragment mSettingFragment;
    private AboutFragment mAboutFragment;
    private long mExitTime;
    private Fragment mCurrentFragment;
    private ProgressDialog mProgressDialog;

    @Override
    public void init() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new MainPresenterImpl(this);
        initToolbar();
        tvScan.setText(R.string.scan);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setHomeAsUpIndicator(null);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        navigationView.setItemIconTintList(getResources().
                getColorStateList(R.drawable.menu_icon_dashboard));
        navigationView.setItemTextColor(getResources().
                getColorStateList(R.drawable.menu_icon_dashboard));
        navigationView.setItemBackground(getResources().getDrawable(R.drawable.menu_item));
        setupDrawerContent(navigationView);

        mCurrentFragment = new DashboardFragment();
        switchDashboard();
        initPermission();
//        switchMusic();

    }

    private void initPermission() {
        // 先判断是否有权限。
        if (AndPermission.hasPermission(this, Utils.getPermission(0),
                Utils.getPermission(1))) {
            mPresenter.initBle(this);

        } else {
            AndPermission.with(this)
                    .requestCode(PERMISSION_REQUEST_CODE)
                    .permission(Utils.getPermission(0), Utils.getPermission(1))
                    .send();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mPresenter.switchNavigation(menuItem.getItemId());
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onViewClicked() {
        initPermission();
    }

    @Override
    public void showProgress() {
        mProgressDialog =ProgressDialog.show(this, "", getString(R.string.init_the_bluetooth),
                true, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        AlertUtils.showAlertDialog(MainActivity.this, R.string.search_cancelled);
                        MyApplication.getBluetoothClient(MainActivity.this).stopSearch();
                    }
                });
    }

    @Override
    public void switchDashboard() {
        tvScan.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.dashboard);
        if (mDashboardFragment == null) {
            mDashboardFragment = new DashboardFragment();
        }
//        showFragment(mDashboardFragment);
        switchContent(mCurrentFragment, mDashboardFragment);
    }

    @Override
    public void switchSources() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText(R.string.sources);
        if (mSourcesFragment == null) {
            mSourcesFragment = new SourcesFragment();
        }
//        showFragment(mSourcesFragment);
        switchContent(mCurrentFragment, mSourcesFragment);
    }

    @Override
    public void switchMusic() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText(R.string.music);
        if (mMusicFragment == null) {
            mMusicFragment = new MusicFragment();
        }
//        showFragment(mMusicFragment);
        switchContent(mCurrentFragment, mMusicFragment);
    }

    @Override
    public void switchLighting() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText(R.string.lighting);
        if (mLightFragment == null) {
            mLightFragment = new LightFragment();
        }
//        showFragment(mLightFragment);
        switchContent(mCurrentFragment, mLightFragment);
    }

    @Override
    public void switchSettings() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText(R.string.settings);
        if (mSettingFragment == null) {
            mSettingFragment = new SettingFragment();
        }
//        showFragment(mSettingFragment);
        switchContent(mCurrentFragment, mSettingFragment);
    }

    @Override
    public void switchAbout() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText(R.string.about);
        if (mAboutFragment == null) {
            mAboutFragment = new AboutFragment();
        }
//        showFragment(mAboutFragment);
        switchContent(mCurrentFragment, mAboutFragment);
    }

    @Override
    public void hideProgress() {
        mProgressDialog.hide();
    }

    @Override
    public void showLoadSuccessMsg(String name) {
        AlertUtils.showAlertDialog(this, String.format(getString(R.string.conn_ble_success),
                name));
    }

    @Override
    public void showLoadFailMsg(String message) {
        final Intent intent = new Intent();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setNegativeButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivityForResult(intent, REQUEST_CODE_SETTING);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, REQUEST_CODE_ALLOW);
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void showLoadExceptionMsg(String exception) {
        AlertUtils.showAlertDialog(this, exception);
    }


    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();


    }

    public void switchContent(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.screen_left_out, R.anim.screen_right_in,
                    R.anim.screen_left_in, R.anim.screen_right_out);
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.hide(from).add(R.id.frame_content, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(from).show(to).commit();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_ALLOW && resultCode == RESULT_OK) ||
                requestCode == REQUEST_CODE_SETTING) {
            mPresenter.initBle(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, R.string.exit_program_hint, Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionYes(PERMISSION_REQUEST_CODE)
    private void getLocationYes(List<String> grantedPermissions) {
        // TODO 申请权限成功。
        mPresenter.initBle(this);
    }

    @PermissionNo(PERMISSION_REQUEST_CODE)
    private void getLocationNo(List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
        }
    }


}
