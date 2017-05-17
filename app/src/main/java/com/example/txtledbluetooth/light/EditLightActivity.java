package com.example.txtledbluetooth.light;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.base.BaseActivity;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.light.presenter.EditLightPresenter;
import com.example.txtledbluetooth.light.presenter.EditLightPresenterImpl;
import com.example.txtledbluetooth.light.view.EditLightView;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SqlUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.example.txtledbluetooth.widget.ColorPicker;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditLightActivity extends BaseActivity implements View.OnClickListener,
        PopupWindowAdapter.OnPopupItemClickListener, EditLightView, RadioGroup.
                OnCheckedChangeListener, TextView.OnEditorActionListener,
        SeekBar.OnSeekBarChangeListener {
    private static final int START_SORT = 1;
    private static final int SORT_DELAY_MILLISECONDS = 300;
    @BindView(R.id.tv_toolbar_right)
    TextView tvRevert;
    @BindView(R.id.tv_chose_color_type)
    TextView tvChoseType;
    @BindView(R.id.view_board1)
    View viewBoard1;
    @BindView(R.id.view_board2)
    View viewBoard2;
    @BindView(R.id.view_board3)
    View viewBoard3;
    @BindView(R.id.view_board4)
    View viewBoard4;
    @BindView(R.id.view_board5)
    View viewBoard5;
    @BindView(R.id.view_board6)
    View viewBoard6;
    @BindView(R.id.view_board7)
    View viewBoard7;
    private PopupWindow mPopWindow;
    private String[] mPopupItems;
    @BindView(R.id.color_picker)
    ColorPicker mColorPicker;
    @BindView(R.id.rg_color_board)
    RadioGroup radioGroup;
    @BindView(R.id.rb_board1)
    RadioButton rbBoard1;
    @BindView(R.id.rb_board2)
    RadioButton rbBoard2;
    @BindView(R.id.rb_board3)
    RadioButton rbBoard3;
    @BindView(R.id.rb_board4)
    RadioButton rbBoard4;
    @BindView(R.id.rb_board5)
    RadioButton rbBoard5;
    @BindView(R.id.rb_board6)
    RadioButton rbBoard6;
    @BindView(R.id.rb_board7)
    RadioButton rbBoard7;
    @BindView(R.id.layout_color_rgb)
    LinearLayout layoutColorRgb;
    @BindView(R.id.layout_speed)
    LinearLayout layoutSpeed;
    @BindView(R.id.layout_brightness)
    LinearLayout layoutBrightness;
    @BindView(R.id.et_r)
    EditText etColorR;
    @BindView(R.id.et_g)
    EditText etColorG;
    @BindView(R.id.et_b)
    EditText etColorB;
    @BindView(R.id.et_well)
    EditText etColorWell;
    @BindView(R.id.sb_speed)
    SeekBar seekBarSpeed;
    @BindView(R.id.sb_brightness)
    SeekBar seekBarBright;
    private View mBgView;
    private EditLightPresenter mEditLightPresenter;
    private int mPosition;
    private long mFirstDrag;
    private String mLightName;
    private String mModelTypeFlags;
    private String mLightNo;
    private int mPopupPosition = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == START_SORT) {
                if ((System.currentTimeMillis() - mFirstDrag) >= SORT_DELAY_MILLISECONDS) {
                    mEditLightPresenter.updateLightColor(mLightNo,
                            (int) radioGroup.getTag() + 1, msg.obj.toString());
                    saveColor(msg.getData());
                }
            }

        }
    };

    @Override
    public void init() {
        setContentView(R.layout.activity_edit_light);
        ButterKnife.bind(this);
        initToolbar();
        mLightName = getIntent().getStringExtra(Utils.LIGHT_MODEL_NAME);
        tvTitle.setText(mLightName);
        tvRevert.setVisibility(View.VISIBLE);
        tvRevert.setText(getString(R.string.revert));
        mPosition = getIntent().getIntExtra(Utils.LIGHT_MODEL_ID, 0);
        intLightType();
        initPopupWindow();
        radioGroup.setOnCheckedChangeListener(this);
        mEditLightPresenter = new EditLightPresenterImpl(this, this, mColorPicker);

        onPopupWindowItemClick(mPopupPosition, tvChoseType.getText().toString());
        etColorWell.setOnEditorActionListener(this);
        etColorR.setOnEditorActionListener(this);
        etColorG.setOnEditorActionListener(this);
        etColorB.setOnEditorActionListener(this);
        seekBarSpeed.setOnSeekBarChangeListener(this);
        seekBarBright.setOnSeekBarChangeListener(this);
        setViewBoardDefaultColor();
        rbBoard1.setChecked(true);
    }


    private void intLightType() {
        List<LightType> lightTypeList = LightType.getLightTypeList(mLightName);
        if (lightTypeList != null && lightTypeList.size() > 0) {
            LightType lightType = lightTypeList.get(0);
            mPopupPosition = lightType.getPopupPosition();

            HashMap<String, Integer> hashMap = Utils.getSeekBarProgress(mPosition);
            if (lightType.getBrightness() == 0) {
                seekBarBright.setProgress(hashMap.get(Utils.SEEK_BAR_PROGRESS_BRIGHT));
            } else {
                seekBarBright.setProgress(lightType.getBrightness());
            }
            if (lightType.getSpeed() == 0) {
                seekBarSpeed.setProgress(hashMap.get(Utils.SEEK_BAR_PROGRESS_SPEED));
            } else {
                seekBarSpeed.setProgress(lightType.getSpeed());
            }
        } else {
            HashMap<String, Integer> hashMap = Utils.getSeekBarProgress(mPosition);
            seekBarBright.setProgress(hashMap.get(Utils.SEEK_BAR_PROGRESS_BRIGHT));
            seekBarSpeed.setProgress(hashMap.get(Utils.SEEK_BAR_PROGRESS_SPEED));
        }
    }


    @OnClick({R.id.tv_toolbar_right, R.id.tv_chose_color_type})
    @Override
    public void onClick(View view) {
        mEditLightPresenter.viewOnclick(view, viewBoard1);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        mBgView = viewBoard1;
        switch (i) {
            case R.id.rb_board1:
                radioGroup.setTag(0);
                mBgView = viewBoard1;
                break;
            case R.id.rb_board2:
                radioGroup.setTag(1);
                mBgView = viewBoard2;
                break;
            case R.id.rb_board3:
                radioGroup.setTag(2);
                mBgView = viewBoard3;
                break;
            case R.id.rb_board4:
                radioGroup.setTag(3);
                mBgView = viewBoard4;
                break;
            case R.id.rb_board5:
                radioGroup.setTag(4);
                mBgView = viewBoard5;
                break;
            case R.id.rb_board6:
                radioGroup.setTag(5);
                mBgView = viewBoard6;
                break;
            case R.id.rb_board7:
                radioGroup.setTag(6);
                mBgView = viewBoard7;
                break;
        }
        mEditLightPresenter.viewOnclick(radioGroup, mBgView);

    }

    @Override
    public void setPaintPixel() {
        List<RgbColor> rgbColorList = RgbColor.getRgbColorList(mLightName + mModelTypeFlags +
                radioGroup.getTag());
        if (rgbColorList == null || rgbColorList.size() == 0) {
            rgbColorList = SqlUtils.getDefaultColors(mLightName + mModelTypeFlags, (Integer)
                    radioGroup.getTag());
        }
        RgbColor rgbColor = rgbColorList.get(0);
        mColorPicker.setPaintPixel(rgbColor.getX(), rgbColor.getY());
        updateTvColor(rgbColor.getR(), rgbColor.getG(), rgbColor.getB(), rgbColor.getColorStr());
    }

    public void initPopupWindow() {
        mPopupItems = Utils.getPopWindowItems(this, mPosition);
        tvChoseType.setText(mPopupItems[mPopupPosition]);
        mModelTypeFlags = mPopupItems[mPopupPosition];
        View popWindowView = getLayoutInflater().inflate(R.layout.popup_window, null);
        RecyclerView popupRecyclerView = (RecyclerView) popWindowView.findViewById(R.id.recycler_view);
        popupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        popupRecyclerView.setHasFixedSize(true);
        mPopWindow = new PopupWindow(popWindowView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        PopupWindowAdapter windowAdapter = new PopupWindowAdapter(mPopupItems, this, this);
        popupRecyclerView.setAdapter(windowAdapter);
    }

    @Override
    public void showPopWindow() {
        mPopWindow.showAsDropDown(tvChoseType, 0, 0, Gravity.LEFT | Gravity.TOP);
    }

    @Override
    public void setTvColor(int color, float x, float y) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        String r1 = getBothColor(r);
        String g1 = getBothColor(g);
        String b1 = getBothColor(b);
        String colorStr = r1 + g1 + b1;
        updateTvColor(r, g, b, colorStr);
        postUpdateHandler(r, g, b, color, x, y);

    }

    public String getBothColor(int str) {
        if (Integer.toString(str).getBytes().length < 2) {
            return "0" + Integer.toHexString(str);
        } else {
            return Integer.toHexString(str);
        }

    }

    private void updateTvColor(int r, int g, int b, String colorStr) {
        if (etColorR.isEnabled()) {
            etColorR.setText(r + "");
            etColorG.setText(g + "");
            etColorB.setText(b + "");
            etColorWell.setText(colorStr);
        }
    }

    @Override
    public void revertColor() {
        RgbColor.deleteRgbColors(mLightName + mModelTypeFlags);
        radioGroup.check(R.id.rb_board1);
        mEditLightPresenter.operateItemBluetooth(mLightName, mPosition, mPopupPosition);
        setViewBoardDefaultColor();
    }


    private void setViewBoardDefaultColor() {
        int[] colors = RgbColor.getRgbColors(mLightName + mModelTypeFlags);
        View view = viewBoard1;
        if (colors != null && colors.length == 7) {
            for (int i = 0; i < 7; i++) {
                switch (i) {
                    case 0:
                        view = viewBoard1;
                        break;
                    case 1:
                        view = viewBoard2;
                        break;
                    case 2:
                        view = viewBoard3;
                        break;
                    case 3:
                        view = viewBoard4;
                        break;
                    case 4:
                        view = viewBoard5;
                        break;
                    case 5:
                        view = viewBoard6;
                        break;
                    case 6:
                        view = viewBoard7;
                        break;
                }
                view.setBackgroundColor(colors[i]);
            }

            int r = Color.red(colors[0]);
            int g = Color.green(colors[0]);
            int b = Color.blue(colors[0]);
            String r1 = getBothColor(r);
            String g1 = getBothColor(g);
            String b1 = getBothColor(b);
            String colorStr = r1 + g1 + b1;
            updateTvColor(r, g, b, colorStr);
        }
    }

    @Override
    public void onPopupWindowItemClick(int position, String type) {
        mLightNo = BleCommandUtils.getLightNo(mPosition, position == 0 ? true : false);

        mPopupPosition = position;
        tvChoseType.setText(type);
        radioGroup.check(R.id.rb_board1);
        initEditLightUi(type);
        initBleLightColor(position);
        setViewBoardDefaultColor();
        mPopWindow.dismiss();
    }

    private void initBleLightColor(int popupPosition) {
        mEditLightPresenter.operateItemBluetooth(mLightName, mPosition, popupPosition);

    }

    private void initEditLightUi(String type) {
        mModelTypeFlags = type;
        setPaintPixel();
        if (type.equals(getString(R.string.random)) || type.contains(getString(R.string.white)) || type.contains(getString(R.string.default_)) ||
                type.contains(getString(R.string.moon_light)) || type.contains(getString(
                R.string.full)) || type.contains(getString(R.string.rainbow))) {
            mEditLightPresenter.setIsSetOnColorSelectListener(false);
            setEtEnable(false);
            //fireworks
            if (mPosition == 1 || mPosition == 2 || mPosition == 3 || mPosition == 4 ||
                    mPosition == 10) {
                layoutSpeed.setVisibility(View.VISIBLE);
            } else {
                layoutSpeed.setVisibility(View.GONE);
            }
            if (mPosition == 7) {
                mEditLightPresenter.setIsSetOnColorSelectListener(true);
                setEtEnable(true);
                rbBoard1.setVisibility(View.VISIBLE);
                rbBoard2.setVisibility(View.VISIBLE);
                rbBoard3.setVisibility(View.VISIBLE);
                rbBoard4.setVisibility(View.VISIBLE);
                rbBoard5.setVisibility(View.VISIBLE);
                rbBoard6.setVisibility(View.GONE);
                rbBoard7.setVisibility(View.GONE);
                viewBoard1.setVisibility(View.VISIBLE);
                viewBoard2.setVisibility(View.VISIBLE);
                viewBoard3.setVisibility(View.VISIBLE);
                viewBoard4.setVisibility(View.VISIBLE);
                viewBoard5.setVisibility(View.VISIBLE);
                viewBoard6.setVisibility(View.GONE);
                viewBoard7.setVisibility(View.GONE);
            } else if (mPosition == 11) {
                mEditLightPresenter.setIsSetOnColorSelectListener(true);
                setEtEnable(true);
                rbBoard1.setVisibility(View.VISIBLE);
                rbBoard2.setVisibility(View.GONE);
                rbBoard3.setVisibility(View.GONE);
                rbBoard4.setVisibility(View.GONE);
                rbBoard5.setVisibility(View.GONE);
                rbBoard6.setVisibility(View.GONE);
                rbBoard7.setVisibility(View.GONE);
                viewBoard1.setVisibility(View.VISIBLE);
                viewBoard2.setVisibility(View.GONE);
                viewBoard3.setVisibility(View.GONE);
                viewBoard4.setVisibility(View.GONE);
                viewBoard5.setVisibility(View.GONE);
                viewBoard6.setVisibility(View.GONE);
                viewBoard7.setVisibility(View.GONE);
            } else {
                mEditLightPresenter.setIsSetOnColorSelectListener(false);
                setEtEnable(false);
                rbBoard1.setVisibility(View.GONE);
                rbBoard2.setVisibility(View.GONE);
                rbBoard3.setVisibility(View.GONE);
                rbBoard4.setVisibility(View.GONE);
                rbBoard5.setVisibility(View.GONE);
                rbBoard6.setVisibility(View.GONE);
                rbBoard7.setVisibility(View.GONE);
                viewBoard1.setVisibility(View.GONE);
                viewBoard2.setVisibility(View.GONE);
                viewBoard3.setVisibility(View.GONE);
                viewBoard4.setVisibility(View.GONE);
                viewBoard5.setVisibility(View.GONE);
                viewBoard6.setVisibility(View.GONE);
                viewBoard7.setVisibility(View.GONE);
            }
            if (mPosition == 0 || mPosition == 1 || mPosition == 2 || mPosition == 3 ||
                    mPosition == 4 || mPosition == 5 || mPosition == 8 || mPosition == 10 || mPosition == 13) {
//                tvRevert.setClickable(false);
                setEtNoData();
            }
        } else if (type.contains("1") || type.contains(getString(R.string.colored)) || type.
                contains(getString(R.string.ice_blue))) {
            mEditLightPresenter.setIsSetOnColorSelectListener(true);
            setEtEnable(true);
            //moonlight
            if (mPosition == 0) {
                layoutSpeed.setVisibility(View.GONE);
            } else {
                layoutSpeed.setVisibility(View.VISIBLE);
            }
            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.GONE);
            rbBoard3.setVisibility(View.GONE);
            rbBoard4.setVisibility(View.GONE);
            rbBoard5.setVisibility(View.GONE);
            rbBoard6.setVisibility(View.GONE);
            rbBoard7.setVisibility(View.GONE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.GONE);
            viewBoard3.setVisibility(View.GONE);
            viewBoard4.setVisibility(View.GONE);
            viewBoard5.setVisibility(View.GONE);
            viewBoard6.setVisibility(View.GONE);
            viewBoard7.setVisibility(View.GONE);
        } else if (type.contains("2")) {
            mEditLightPresenter.setIsSetOnColorSelectListener(true);
            setEtEnable(true);
            layoutSpeed.setVisibility(View.VISIBLE);
            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.VISIBLE);
            rbBoard3.setVisibility(View.GONE);
            rbBoard4.setVisibility(View.GONE);
            rbBoard5.setVisibility(View.GONE);
            rbBoard6.setVisibility(View.GONE);
            rbBoard7.setVisibility(View.GONE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.VISIBLE);
            viewBoard3.setVisibility(View.GONE);
            viewBoard4.setVisibility(View.GONE);
            viewBoard5.setVisibility(View.GONE);
            viewBoard6.setVisibility(View.GONE);
            viewBoard7.setVisibility(View.GONE);
        } else if (type.contains("3")) {
            mEditLightPresenter.setIsSetOnColorSelectListener(true);
            setEtEnable(true);
            layoutSpeed.setVisibility(View.VISIBLE);
            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.VISIBLE);
            rbBoard3.setVisibility(View.VISIBLE);
            rbBoard4.setVisibility(View.GONE);
            rbBoard5.setVisibility(View.GONE);
            rbBoard6.setVisibility(View.GONE);
            rbBoard7.setVisibility(View.GONE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.VISIBLE);
            viewBoard3.setVisibility(View.VISIBLE);
            viewBoard4.setVisibility(View.GONE);
            viewBoard5.setVisibility(View.GONE);
            viewBoard6.setVisibility(View.GONE);
            viewBoard7.setVisibility(View.GONE);
        } else if (type.contains("7") || type.contains(getString(R.string.mood))) {
            mEditLightPresenter.setIsSetOnColorSelectListener(true);
            setEtEnable(true);
            layoutSpeed.setVisibility(View.VISIBLE);
            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.VISIBLE);
            rbBoard3.setVisibility(View.VISIBLE);
            rbBoard4.setVisibility(View.VISIBLE);
            rbBoard5.setVisibility(View.VISIBLE);
            rbBoard6.setVisibility(View.VISIBLE);
            rbBoard7.setVisibility(View.VISIBLE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.VISIBLE);
            viewBoard3.setVisibility(View.VISIBLE);
            viewBoard4.setVisibility(View.VISIBLE);
            viewBoard5.setVisibility(View.VISIBLE);
            viewBoard6.setVisibility(View.VISIBLE);
            viewBoard7.setVisibility(View.VISIBLE);
            if (type.contains(getString(R.string.mood))) {
                layoutSpeed.setVisibility(View.GONE);
                layoutBrightness.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mColorPicker.isRecycled()) {
            mColorPicker.recycle();
        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        EditText editText = (EditText) findViewById(textView.getId());
        editText.setSelection(editText.getText().length());
        if (editText == etColorR || editText == etColorG || editText == etColorB) {
            if (-1 < Integer.parseInt(editText.getText().toString().trim()) &&
                    Integer.parseInt(editText.getText().toString().trim()) < 256) {
                int r = Integer.parseInt(etColorR.getText().toString().trim());
                int g = Integer.parseInt(etColorG.getText().toString().trim());
                int b = Integer.parseInt(etColorB.getText().toString().trim());
                String r1 = getBothColor(r);
                String g1 = getBothColor(g);
                String b1 = getBothColor(b);
                String colorStr = r1 + g1 + b1;
                etColorWell.setText(colorStr);
                int colorInt = Color.rgb(r, g, b);
                mBgView.setBackgroundColor(colorInt);
                postUpdateHandler(r, g, b, colorInt, 0, 0);
            } else {
                Toast.makeText(this, R.string.color_value_hint, Toast.LENGTH_SHORT).show();
            }
        } else {
            String[] temp = etColorWell.getText().toString().split("");
            if (temp.length == 7) {
                String strR = Integer.valueOf(temp[1] + temp[2], 16).toString();
                String strG = Integer.valueOf(temp[3] + temp[4], 16).toString();
                String strB = Integer.valueOf(temp[5] + temp[6], 16).toString();
                int r = Integer.parseInt(strR);
                int g = Integer.parseInt(strG);
                int b = Integer.parseInt(strB);
                int colorInt = Color.rgb(r, g, b);
                etColorR.setText(strR);
                etColorG.setText(strG);
                etColorB.setText(strB);
                mBgView.setBackgroundColor(colorInt);
                postUpdateHandler(r, g, b, colorInt, 0, 0);
            } else {
                Toast.makeText(this, R.string.color_value_hint, Toast.LENGTH_SHORT).show();
            }
        }
        etColorWell.setCursorVisible(false);
        etColorR.setCursorVisible(false);
        etColorG.setCursorVisible(false);
        etColorB.setCursorVisible(false);
        Utils.hideKeyboard(this);
        return false;
    }

    private void postUpdateHandler(int r, int g, int b, int colorInt, float x, float y) {
        mFirstDrag = System.currentTimeMillis();
        Message message = mHandler.obtainMessage();
        message.what = START_SORT;
        message.obj = etColorWell.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.COLOR_R, r);
        bundle.putInt(Utils.COLOR_G, g);
        bundle.putInt(Utils.COLOR_B, b);
        bundle.putFloat(Utils.PIXEL_X, x);
        bundle.putFloat(Utils.PIXEL_Y, y);
        message.setData(bundle);
        mHandler.sendMessageDelayed(message, SORT_DELAY_MILLISECONDS);
    }

    public void setEtNoData() {
        etColorR.setText("");
        etColorG.setText("");
        etColorB.setText("");
        etColorWell.setText("");
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() == R.id.sb_speed) {
            mEditLightPresenter.setLightSpeed(mLightNo, seekBar.getProgress());
        } else if (seekBar.getId() == R.id.sb_brightness) {
            mEditLightPresenter.setLightBrightness(mLightNo, seekBar.getProgress());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LightType lightType = new LightType(mLightName, seekBarSpeed.getProgress(),
                seekBarBright.getProgress(), mPopupPosition);
        lightType.deleteLightTypeByName();
        lightType.save();
    }

    private void saveColor(Bundle data) {
        String name = mLightName + mModelTypeFlags + radioGroup.getTag();
        int r = data.getInt(Utils.COLOR_R);
        int g = data.getInt(Utils.COLOR_G);
        int b = data.getInt(Utils.COLOR_B);
        float x = data.getFloat(Utils.PIXEL_X);
        float y = data.getFloat(Utils.PIXEL_Y);
        RgbColor rgbColor = new RgbColor(name, r, g, b, x, y);
        rgbColor.deleteRgbColorByName();
        rgbColor.save();
    }

    private void setEtEnable(boolean isEnable) {
        etColorR.setEnabled(isEnable);
        etColorG.setEnabled(isEnable);
        etColorB.setEnabled(isEnable);
        etColorWell.setEnabled(isEnable);
    }
}
