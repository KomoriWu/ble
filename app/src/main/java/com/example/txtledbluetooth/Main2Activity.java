package com.example.txtledbluetooth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.txtledbluetooth.widget.ColorPicker;

public class Main2Activity extends AppCompatActivity {
    public static final String TAG = Main2Activity.class.getSimpleName();
    private ColorPicker colorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        colorPicker = (ColorPicker) findViewById(R.id.color_picker);
        colorPicker.setOnColorChangedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, colorPicker.getColor() + "");
            }
        });
    }
}
