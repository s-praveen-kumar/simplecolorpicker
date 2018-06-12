package com.arunsoft.colorpickerlib;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

public class ColorPickerDialog extends AlertDialog.Builder {

    private static final int PADDING = 16; // padding for grid
    private static final int CUSTOM_COLOR = -1;
    private int[] colors;
    private OnColorSelectedListener listener;
    private boolean supportTransparency;

    public ColorPickerDialog(@NonNull Context context) {
        super(context);
    }

    public static void showColorDialog(Context context, @Nullable String title, int[] presetColors, boolean supportTransparency, final OnColorSelectedListener listener) {
        ColorPickerDialog dialog = new ColorPickerDialog(context);
        if (title == null)
            title = "Choose Color";
        dialog.setTitle(title);
        dialog.setColors(presetColors, supportTransparency);
        dialog.setListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                listener.onColorSelected(color);
            }

            @Override
            public void onCancelled() {
                listener.onCancelled();
            }
        });
        dialog.create().show();
    }

    private void setColors(int[] colors, boolean supportTransparency) {
        this.colors = colors;
        this.supportTransparency = supportTransparency;
        init();
    }

    private void init() {
        final ColorGridView colorGridView = new ColorGridView(getContext());
        colorGridView.setPadding(PADDING, PADDING, PADDING, PADDING);
        colorGridView.setSelection(0);
        setView(colorGridView);
        colorGridView.setColors(colors, supportTransparency);
        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCancelled();
            }
        });
        setNeutralButton("Custom Color", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onColorSelected(colorGridView.getSelectedColor());
            }
        });
    }

    private void setListener(OnColorSelectedListener listener) {
        this.listener = listener;
    }


    public interface OnColorSelectedListener {
        void onColorSelected(int color);

        void onCancelled();
    }
}
