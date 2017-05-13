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
import com.example.txtledbluetooth.utils.Utils;
import com.example.txtledbluetooth.widget.ColorPicker;
import com.nostra13.universalimageloader.utils.L;

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
    private int mPopupPosition = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == START_SORT) {
                if ((System.currentTimeMillis() - mFirstDrag) >= SORT_DELAY_MILLISECONDS) {
                    mEditLightPresenter.updateLightColor(BleCommandUtils.getLightNo(mPosition,false),
                            (int) radioGroup.getTag(), msg.obj.toString());
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
        intLightType();
        mPosition = getIntent().getIntExtra(Utils.LIGHT_MODEL_ID, 0);
        initPopupWindow();
        radioGroup.setOnCheckedChangeListener(this);
        mEditLightPresenter = new EditLightPresenterImpl(this, this, mColorPicker);

        onPopupWindowItemClick(mPopupPosition, tvChoseType.getText().toString());
        etColorWell.setOnEditorActionListener(this);
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
            seekBarBright.setProgress(lightType.getBrightness());
            seekBarSpeed.setProgress(lightType.getSpeed());
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
        if (rgbColorList != null && rgbColorList.size() > 0) {
            RgbColor rgbColor = rgbColorList.get(0);
            mColorPicker.setPaintPixel(rgbColor.getX(), rgbColor.getY());
            updateTvColor(rgbColor.getR(), rgbColor.getG(), rgbColor.getB(), rgbColor.getColorStr());
        }
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
        String r1 = Integer.toHexString(r);
        String g1 = Integer.toHexString(g);
        String b1 = Integer.toHexString(b);
        String colorStr = r1 + g1 + b1;
        updateTvColor(r, g, b, colorStr);

        mFirstDrag = System.currentTimeMillis();
        Message message = mHandler.obtainMessage();
        message.what = START_SORT;
        message.obj = colorStr;
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.COLOR_R, r);
        bundle.putInt(Utils.COLOR_G, g);
        bundle.putInt(Utils.COLOR_B, b);
        bundle.putInt(Utils.COLOR_INT, color);
        bundle.putFloat(Utils.PIXEL_X, x);
        bundle.putFloat(Utils.PIXEL_Y, y);
        bundle.putString(Utils.COLOR_STR, colorStr);
        message.setData(bundle);
        mHandler.sendMessageDelayed(message, SORT_DELAY_MILLISECONDS);

    }

    private void updateTvColor(int r, int g, int b, String colorStr) {
        etColorR.setText(r + "");
        etColorG.setText(g + "");
        etColorB.setText(b + "");
        etColorWell.setText(colorStr);
    }

    @Override
    public void revertColor() {
        radioGroup.check(R.id.rb_board1);
        setEtDefaultData();
        mEditLightPresenter.updateLightColor(BleCommandUtils.getLightNo(mPosition,false),
                (int) radioGroup.getTag(), getString(R.string.red_color));
        RgbColor.deleteAll(RgbColor.class);
        setViewBoardDefaultColor();
    }


    private void setViewBoardDefaultColor() {
        int[] colors = RgbColor.getRgbColors(mLightName + mModelTypeFlags);
        View view = viewBoard1;
        int defaultColor;
        if (colors != null && colors.length == 7) {
            for (int i = 0; i < 7; i++) {
                defaultColor = Utils.getDefaultColor(this, i);
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
                if (colors[i] == 0) {
                    view.setBackgroundColor(defaultColor);
                } else {
                    view.setBackgroundColor(colors[i]);
                }
            }
        }
    }

    @Override
    public void onPopupWindowItemClick(int position, String type) {
        mPopupPosition = position;
        tvChoseType.setText(type);
        radioGroup.check(R.id.rb_board1);
        initEditLightUi(type);
        initBleLightColor(position);
        setViewBoardDefaultColor();
        mPopWindow.dismiss();
    }

    private void initBleLightColor(int position) {
        mEditLightPresenter.initBleLightColor(position);
    }

    private void initEditLightUi(String type) {
        mModelTypeFlags = type;
        setPaintPixel();
        if (type.equals(getString(R.string.random)) || type.contains(getString(R.string.white)) || type.contains(getString(R.string.default_)) ||
                type.contains(getString(R.string.moon_light)) || type.contains(getString(R.string.full))) {
            mEditLightPresenter.setIsSetOnColorSelectListener(false);
            //fireworks
            if (mPosition == 1 || mPosition == 2 || mPosition == 3 || mPosition == 4) {
                layoutSpeed.setVisibility(View.VISIBLE);
            } else {
                layoutSpeed.setVisibility(View.GONE);
            }
            if (mPosition == 6) {
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
            } else {
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
            if (mPosition == 0 || mPosition == 1 || mPosition == 2 || mPosition == 3) {
//                tvRevert.setClickable(false);
                setEtNoData();
            }
        } else if (type.contains("1") || type.contains(getString(R.string.colored))) {
            mEditLightPresenter.setIsSetOnColorSelectListener(true);
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
        } else if (type.contains("7")) {
            mEditLightPresenter.setIsSetOnColorSelectListener(true);
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

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mColorPicker.isRecycled()) {
            mColorPicker.recycle();
        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        String[] temp = etColorWell.getText().toString().split("");
        if (temp.length == 7) {
            String strR = Integer.valueOf(temp[1] + temp[2], 16).toString();
            String strG = Integer.valueOf(temp[3] + temp[4], 16).toString();
            String strB = Integer.valueOf(temp[5] + temp[6], 16).toString();
            etColorR.setText(strR);
            etColorG.setText(strG);
            etColorB.setText(strB);
            mBgView.setBackgroundColor(Color.rgb(Integer.parseInt(strR), Integer.parseInt(strG),
                    Integer.parseInt(strB)));
        } else {
            Toast.makeText(this, R.string.color_value_hint, Toast.LENGTH_SHORT).show();
        }
        etColorWell.setCursorVisible(false);
        Utils.hideKeyboard(this);
        return false;
    }

    public void setEtNoData() {
        etColorR.setText("");
        etColorG.setText("");
        etColorB.setText("");
        etColorWell.setText("");
    }

    private void setEtDefaultData() {
        etColorR.setText("255");
        etColorG.setText("0");
        etColorB.setText("0");
        etColorWell.setText("ff0000");
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
            mEditLightPresenter.setLightSpeed(BleCommandUtils.getLightNo(mPosition,false),
                    seekBar.getProgress());
        } else if (seekBar.getId() == R.id.sb_brightness) {
            mEditLightPresenter.setLightBrightness(BleCommandUtils.getLightNo(mPosition,false),
                    seekBar.getProgress());
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
        int colorInt = data.getInt(Utils.COLOR_INT);
        String colorStr = data.getString(Utils.COLOR_STR);
        RgbColor rgbColor = new RgbColor(name, r, g, b, x, y, colorInt, colorStr);
        rgbColor.deleteRgbColorByName();
        rgbColor.save();
    }


}
