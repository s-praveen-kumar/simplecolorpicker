package com.arunsoft.colorpickerlib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class ColorGridView extends GridView{

    private static final int SPACING = 8;
    private int[] colors;
    private ColorAdapter adapter;
    private OnColorClickedListener listener;

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
        setHorizontalSpacing(SPACING);
        setVerticalSpacing(SPACING);
        setAdapter(adapter);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listener!=null)
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
    }

    public void setListener(OnColorClickedListener listener) {
        this.listener = listener;
    }

    public interface OnColorClickedListener{
        void onColorClicked(int color);
    }
}
