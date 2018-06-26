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
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class ColorGridView extends GridView {

    private static final int SPACING = 8;
    private int[] colors;
    private ColorAdapter adapter;
    private OnColorClickedListener listener;
    private int selectedColor = -1;

    public ColorGridView(Context context) {
        super(context);
        init();
    }

    public ColorGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int columns = getResources().getInteger(R.integer.columns);
        colors = new int[]{0xffffffff};
        adapter = new ColorAdapter(getContext(), colors);
        setNumColumns(columns);
        setChoiceMode(CHOICE_MODE_SINGLE);
        setHorizontalSpacing(SPACING);
        setVerticalSpacing(SPACING);
        setAdapter(adapter);
        setSelector(R.drawable.color_selector);
        setDrawSelectorOnTop(true);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedColor = colors[position];
                if (listener != null)
                    listener.onColorClicked(colors[position]);
            }
        });
    }

    public void setColors(int[] colors, boolean supportTransparency) {
        if (!supportTransparency) {
            for (int i = 0; i < colors.length; i++) {
                colors[i] = colors[i] | 0xff000000;
            }
        }
        this.colors = colors;
        adapter.setColors(this.colors);
        adapter.notifyDataSetChanged();
        selectedColor = colors[0];
    }

    public void setListener(OnColorClickedListener listener) {
        this.listener = listener;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public interface OnColorClickedListener {
        void onColorClicked(int color);
    }
}
