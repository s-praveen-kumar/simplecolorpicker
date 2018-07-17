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
package com.arunsoft.colorpickerlib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class HSVPickerDialog extends AlertDialog.Builder {

    private ColorPickerDialog.OnColorSelectedListener listener;
    private ColorPickerSubViews.SVView svView;
    private ColorPickerSubViews.HueBar hueBar;
    private TextView preview, alphaLabel;
    private SeekBar alphaBar;
    private EditText editText;
    private boolean manualEdit = true;
    private boolean showAlphaBar = true;

    public HSVPickerDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.color_picker_view, null);
        setView(view);
        svView = view.findViewById(R.id.color_picker_svView);
        hueBar = view.findViewById(R.id.color_picker_hueBar);
        preview = view.findViewById(R.id.color_picker_preview);
        alphaBar = view.findViewById(R.id.color_picker_alphaBar);
        editText = view.findViewById(R.id.color_picker_edit);
        alphaLabel = view.findViewById(R.id.color_picker_alpha_label);
        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onColorSelected(getColor());
                }
                hueBar.dispose();
                svView.dispose();
            }
        });
        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onCancelled();
                hueBar.dispose();
                svView.dispose();
            }
        });
        svView.setOnSVChangedListener(new ColorPickerSubViews.SVView.OnSVChangedListener() {
            @Override
            public void onSVChanged(float s, float v) {
                updatePreview();
            }
        });
        hueBar.setOnHueChangedListener(new ColorPickerSubViews.HueBar.OnHueChangedListener() {
            @Override
            public void onHueChanged(int hue) {
                svView.setHue(hue);
                updatePreview();
            }
        });
        alphaBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    svView.setAlpha(progress);
                    updatePreview();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!manualEdit)
                    return;
                try {
                    int color = (int) Long.parseLong(String.valueOf(s), 16);
                    float[] hsv = new float[3];
                    Color.colorToHSV(color, hsv);
                    hueBar.setHue((int) hsv[0]);
                    svView.setSat(hsv[1]);
                    svView.setVal(hsv[2]);
                    svView.setHue((int) hsv[0]);
                    if (showAlphaBar) {
                        svView.setAlpha(Color.alpha(color));
                        alphaBar.setProgress(Color.alpha(color));
                    }
                    if (s.length() == 8) {
                        updatePreview();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updatePreview() {
        int color = getColor();
        preview.setBackgroundColor(color);
        manualEdit = false;
        editText.setText(String.format("%08x", color));
        manualEdit = true;
    }

    private int getColor() {
        return Color.HSVToColor(showAlphaBar ? alphaBar.getProgress() : 255, new float[]{hueBar.getSelectedHue(), svView.getSat(), svView.getVal()});
    }

    public void setListener(ColorPickerDialog.OnColorSelectedListener listener) {
        this.listener = listener;
    }

    public void setPreviousColor(int previousColor) {
        float[] hsv = new float[3];
        Color.colorToHSV(previousColor, hsv);
        hueBar.setHue((int) hsv[0]);
        svView.setSat(hsv[1]);
        svView.setVal(hsv[2]);
        alphaBar.setProgress(Color.alpha(previousColor));
        svView.setHue((int) hsv[0]);
        svView.setAlpha(showAlphaBar ? Color.alpha(previousColor) : 255);
        updatePreview();
    }

    public void shouldShowAlpha(boolean showAlphaBar) {
        this.showAlphaBar = showAlphaBar;
        if (!showAlphaBar) {
            alphaBar.setVisibility(View.GONE);
            alphaLabel.setVisibility(View.GONE);
        }
    }
}
