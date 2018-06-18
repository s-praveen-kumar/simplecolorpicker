package com.arunsoft.colorpickerlib;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

public class ColorPickerDialog extends AlertDialog.Builder {

    private static final int PADDING = 16; // padding for grid
    private int[] colors;
    private OnColorSelectedListener listener;
    private boolean supportTransparency;
    private boolean customColor = false;

    private ColorPickerDialog(@NonNull Context context) {
        super(context);
    }

    public static void showColorDialog(final Context context, @Nullable String title, final int[] presetColors, boolean supportTransparency, final int previousColor, final OnColorSelectedListener listener) {
        final ColorPickerDialog dialog = new ColorPickerDialog(context);
        if (title == null)
            title = "Choose Color";
        dialog.setTitle(title);
        dialog.setColors(presetColors, supportTransparency);
        final String finalTitle = title;
        dialog.setListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                listener.onColorSelected(color);
            }

            @Override
            public void onCancelled() {
                if (dialog.customColor) {
                    HSVPickerDialog hsvPickerDialog = new HSVPickerDialog(context);
                    hsvPickerDialog.setTitle(finalTitle);
                    hsvPickerDialog.setListener(listener);
                    hsvPickerDialog.setPreviousColor(previousColor);
                    hsvPickerDialog.create().show();
                } else
                    listener.onCancelled();
            }
        });
        dialog.create().show();
    }

    public static void showColorDialog(Context context, @Nullable String title, final int[] presetColors, OnColorSelectedListener listener) {
        showColorDialog(context, title, presetColors, false, 0xffffffff, listener);
    }

    public static void showColorDialog(Context context, @Nullable String title, OnColorSelectedListener listener) {
        showColorDialog(context, title, context.getResources().getIntArray(R.array.default_preset), false, 0xffffffff, listener);
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
                customColor = true;
                listener.onCancelled();
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
