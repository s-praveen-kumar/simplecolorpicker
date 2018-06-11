package com.arunsoft.colorpickerlib;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ColorGridView extends GridView {

    private static final int SPACING = 8;
    private int columns;
    private int[] colors;
    private ColorAdapter adapter;

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
        columns = getResources().getInteger(R.integer.columns);
        colors = new int[]{0xffffffff};
        adapter = new ColorAdapter(getContext(), colors);
        setNumColumns(columns);
        setHorizontalSpacing(SPACING);
        setVerticalSpacing(SPACING);
        setAdapter(adapter);
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
    }
}
