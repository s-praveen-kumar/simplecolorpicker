package com.arunsoft.simplecolorpicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.arunsoft.colorpickerlib.ColorPickerDialog;

public class TestActivity extends AppCompatActivity {
    private View preview;
    private int color = 0xff000000;
    private static final int[] presets = {0xffffff,0x000000,0xff0000,0x00ff00,0x0000ff,0xffff00,0xff00ff,0x00ffff};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        preview = findViewById(R.id.preview);

    }

    public void showDialogSimple(View v) {
        /*Simple implementation
        Uses default set of presets
        The result is returned in form of int.
         */
        ColorPickerDialog.showColorDialog(this, "Color For background", new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                preview.setBackgroundColor(color);
                TestActivity.this.color = color;
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    public void showDialogNormal(View v) {
        /*Customized implementation
        Uses the given presets.
        The boolean flag indicates that the preset colors doesn't have their own alpha component (To support 0xffffff format over 0xffffffff)
        The 5th parameter is the current color. It is shown by default in the custom color picker.
         */
        ColorPickerDialog.showColorDialog(this, "Color For background", presets, false, color, new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                preview.setBackgroundColor(color);
                TestActivity.this.color = color;
            }

            @Override
            public void onCancelled() {

            }
        });
    }
}
