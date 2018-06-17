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
    private TextView preview;
    private SeekBar alphaBar;
    private EditText editText;
    private boolean manualEdit = true;

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
                    svView.setAlpha(Color.alpha(color));
                    alphaBar.setProgress(Color.alpha(color));
                    preview.setBackgroundColor(color);
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
        return Color.HSVToColor(alphaBar.getProgress(), new float[]{hueBar.getSelectedHue(), svView.getSat(), svView.getVal()});
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
        svView.setAlpha(Color.alpha(previousColor));
        updatePreview();
    }
}
