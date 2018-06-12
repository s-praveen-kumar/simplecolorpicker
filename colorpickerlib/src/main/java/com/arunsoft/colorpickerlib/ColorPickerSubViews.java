package com.arunsoft.colorpickerlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerSubViews {
    public static class HueBar extends View {
        public static final int HORIZONTAL = 1, VERTICAL = 0;
        private int orientation = HORIZONTAL;
        private int selectedHue = 0;
        private Paint paint;

        public HueBar(Context context) {
            super(context);
            paint = new Paint();
        }

        public HueBar(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            paint = new Paint();
        }

        public HueBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            paint = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = orientation == HORIZONTAL ? canvas.getHeight() : canvas.getWidth();
            float l = (orientation == HORIZONTAL ? canvas.getWidth() : canvas.getHeight()) / 360f;
            float[] hsv = {1, 1, 1};
            for (int i = 0; i < 360; i++) {
                hsv[0] = i + 1;
                paint.setColor(Color.HSVToColor(hsv));
                if (orientation == HORIZONTAL)
                    canvas.drawRect(i * l, 0, (i + 1) * l, w, paint);
                else
                    canvas.drawRect(0, i * l, w, (i + 1) * l, paint);
            }
            paint.setColor(0xff000000);
            if (orientation == HORIZONTAL)
                canvas.drawRect(selectedHue * l, 0, (selectedHue + 1) * l, w, paint);
            else
                canvas.drawRect(0, selectedHue * l, w, (selectedHue + 1) * l, paint);
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.d("touch", "onTouchEvent: " + getMeasuredWidth() + " , " + getMeasuredHeight());
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                if (orientation == HORIZONTAL)
                    selectedHue = (int) (event.getX() / (float) getMeasuredWidth() * 360f);
                else
                    selectedHue = (int) (event.getY() / (float) getMeasuredHeight() * 360f);
                Log.d("touch", "onTouchEvent: " + event.getX() + " , " + event.getY());
                invalidate();
                return true;
            }
            return super.onTouchEvent(event);
        }
    }
}
