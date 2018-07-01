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

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
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
                if (listener != null)
                    listener.onCancelled();
            }
        });
        setNeutralButton("Custom Color", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customColor = true;
                if (listener != null)
                    listener.onCancelled();
            }
        });
        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
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

    public static class Builder {
        private Context context;
        private String title = "";
        private int[] presets;
        private int currentColor = 0xffff0000;
        private OnColorSelectedListener listener;
        private boolean transparencyForPresets = false;
        private boolean showAlphaBar = true;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPresets(int[] presets, boolean transparencyForPresets) {
            this.presets = presets;
            this.transparencyForPresets = transparencyForPresets;
            return this;
        }

        public Builder setPresets(int[] presets) {
            this.presets = presets;
            return this;
        }

        public Builder setCurrentColor(int currentColor) {
            this.currentColor = currentColor;
            return this;
        }

        public Builder setListener(OnColorSelectedListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setShowAlphaBar(boolean showAlphaBar) {
            this.showAlphaBar = showAlphaBar;
            return this;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public void show() {
            final ColorPickerDialog dialog = new ColorPickerDialog(context);
            dialog.setTitle(title);
            if (presets == null)
                dialog.setColors(context.getResources().getIntArray(R.array.default_preset), false);
            else
                dialog.setColors(presets, transparencyForPresets);
            dialog.setListener(new OnColorSelectedListener() {
                @Override
                public void onColorSelected(int color) {
                    listener.onColorSelected(color);
                }

                @Override
                public void onCancelled() {
                    if (dialog.customColor) {
                        HSVPickerDialog hsvPickerDialog = new HSVPickerDialog(context);
                        hsvPickerDialog.shouldShowAlpha(showAlphaBar);
                        hsvPickerDialog.setTitle(title);
                        hsvPickerDialog.setListener(listener);
                        hsvPickerDialog.setPreviousColor(currentColor);
                        hsvPickerDialog.create().show();
                    } else
                        listener.onCancelled();
                }
            });
            dialog.create().show();
        }
    }
}
