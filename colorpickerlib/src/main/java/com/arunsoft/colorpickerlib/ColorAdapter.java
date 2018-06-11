package com.arunsoft.colorpickerlib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

class ColorAdapter extends BaseAdapter {
    private Context context;

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    private int[] colors;

    public ColorAdapter(@NonNull Context context, @NonNull int[] colors) {
        this.context = context;
        this.colors = colors;
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int position) {
        return colors[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CircleView circleView = new CircleView(context);
        circleView.setColor(colors[position]);
        return circleView;
    }
}
