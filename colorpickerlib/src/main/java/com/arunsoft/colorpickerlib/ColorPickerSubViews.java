package com.arunsoft.colorpickerlib;

import android.content.Context;
import android.graphics.Bitmap;
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

        public int getSelectedHue() {
            return selectedHue;
        }

        public void setHue(int hue) {
            this.selectedHue = selectedHue;
            invalidate();
        }

    }

    public static class SVView extends View {
        private float sat, val;
        private int hue = 1;
        private Paint paint;
        private Bitmap cache;
        private boolean shouldRedraw = true;

        public SVView(Context context) {
            super(context);
            paint = new Paint();
        }

        public SVView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            paint = new Paint();
        }

        public SVView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            paint = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float w = (float) canvas.getWidth() / 100f;
            float h = (float) canvas.getHeight() / 100f;
            if (shouldRedraw) {
                drawBitmap(canvas.getWidth(), canvas.getHeight());
                shouldRedraw = false;
            }
            canvas.drawBitmap(cache, 0, 0, paint);
            paint.setColor(0xffffffff);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            canvas.drawCircle((val + 1) * w, (sat + 1) * h, 8, paint);
        }

        private void drawBitmap(int width, int height) {
            if (cache == null)
                cache = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(cache);
            float w = (float) width / 100f;
            float h = (float) height / 100f;
            float[] hsv = {hue, 1, 1};
            paint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    hsv[1] = (float) (i + 1) / 100f;
                    hsv[2] = (float) (j + 1) / 100f;
                    paint.setColor(Color.HSVToColor(hsv));
                    canvas.drawRect(j * w, i * h, (j + 1) * w, (i + 1) * h, paint);
                }
            }
        }

        public void setHue(int hue) {
            this.hue = hue;
            shouldRedraw = true;
            invalidate();
        }


        public float getSat() {
            return sat;
        }

        public void setSat(float sat) {
            this.sat = sat;
            invalidate();
        }

        public float getVal() {
            return val;
        }

        public void setVal(float val) {
            this.val = val;
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                sat = event.getY() / (float) getMeasuredHeight() * 100f;
                val = event.getX() / (float) getMeasuredWidth() * 100f;
                invalidate();
                return true;
            }
            return super.onTouchEvent(event);
        }
    }
}
