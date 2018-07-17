/*MIT License

Copyright (c) 2018 S Praveen Kumar

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/
package com.arunsoft.simplecolorpicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.arunsoft.colorpickerlib.ColorPickerDialog;

public class TestActivity extends AppCompatActivity {
    private View preview;
    private int color = 0xff000000;
    private static final int[] presets = {0xffffff, 0x000000, 0xff0000, 0x00ff00, 0x0000ff, 0xffff00, 0xff00ff, 0x00ffff};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        preview = findViewById(R.id.preview);

    }

    public void showDialogSimple(View v) {
        /*Simple implementation*/
        new ColorPickerDialog.Builder(this).setListener(new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                preview.setBackgroundColor(color);
                TestActivity.this.color = color;
            }

            @Override
            public void onCancelled() {

            }
        }).show();
    }

    public void showDialogNormal(View v) {
        /*Customized implementation*/
        new ColorPickerDialog.Builder(this).setPresets(presets).setCurrentColor(color).setTitle("Color for background").setShowAlphaBar(false).setListener(new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                preview.setBackgroundColor(color);
                TestActivity.this.color = color;
            }

            @Override
            public void onCancelled() {

            }
        }).show();
    }
}
