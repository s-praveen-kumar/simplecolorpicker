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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerSubViews {
    public static class HueBar extends View {
        public static final int HORIZONTAL = 1, VERTICAL = 0;
        private int orientation = VERTICAL;
        private int selectedHue = 0;
        private Bitmap cache;
        private boolean shouldRedraw = true;
        private Paint paint;
        private OnHueChangedListener listener;

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
            if (shouldRedraw || cache.isRecycled()) {
                drawBitmap(canvas.getWidth(), canvas.getHeight());
                shouldRedraw = false;
            }
            canvas.drawBitmap(cache, 0, 0, paint);
            paint.setColor(0xffffffff);
            if (orientation == HORIZONTAL)
                canvas.drawRect(selectedHue * l, 0, (selectedHue + 1) * l, w, paint);
            else
                canvas.drawRect(0, selectedHue * l, w, (selectedHue + 1) * l, paint);
        }

        private void drawBitmap(int width, int height) {
            if (cache == null || cache.isRecycled())
                cache = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(cache);
            int w = orientation == HORIZONTAL ? height : width;
            float l = (orientation == HORIZONTAL ? width : height) / 360f;
            float[] hsv = {1, 1, 1};
            for (int i = 0; i < 360; i++) {
                hsv[0] = i + 1;
                paint.setColor(Color.HSVToColor(hsv));
                if (orientation == HORIZONTAL)
                    canvas.drawRect(i * l, 0, (i + 1) * l, w, paint);
                else
                    canvas.drawRect(0, i * l, w, (i + 1) * l, paint);
            }
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
            shouldRedraw = true;
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                int tHue;
                if (orientation == HORIZONTAL)
                    tHue = (int) (event.getX() / (float) getMeasuredWidth() * 360f);
                else
                    tHue = (int) (event.getY() / (float) getMeasuredHeight() * 360f);
                if (selectedHue != tHue) {
                    selectedHue = tHue;
                    invalidate();
                    if (listener != null)
                        listener.onHueChanged(selectedHue);
                }
                return true;
            }
            return super.onTouchEvent(event);
        }

        public int getSelectedHue() {
            return selectedHue;
        }

        public void setHue(int hue) {
            this.selectedHue = hue;
            shouldRedraw = true;
            invalidate();
        }

        public void dispose() {
            cache.recycle();
        }

        public void setOnHueChangedListener(OnHueChangedListener listener) {
            this.listener = listener;
        }

        public interface OnHueChangedListener {
            void onHueChanged(int hue);
        }

    }

    public static class SVView extends View {
        private float sat = 1, val = 1;
        private int hue = 1, alpha = 0xff;
        private Paint paint;
        private Bitmap cache;
        private boolean shouldRedraw = true;
        private OnSVChangedListener listener;

        public SVView(Context context) {
            super(context);
            paint = new Paint();
            setBackgroundResource(R.drawable.alpha_bg);
        }

        public SVView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            paint = new Paint();
            setBackgroundResource(R.drawable.alpha_bg);
        }

        public SVView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            paint = new Paint();
            setBackgroundResource(R.drawable.alpha_bg);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float w = (float) canvas.getWidth() / 100f;
            float h = (float) canvas.getHeight() / 100f;
            if (shouldRedraw || cache.isRecycled()) {
                drawBitmap(canvas.getWidth(), canvas.getHeight());
                shouldRedraw = false;
            }
            paint.setAlpha(alpha);
            canvas.drawBitmap(cache, 0, 0, paint);
            paint.setColor(0xffffffff);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            canvas.drawCircle(((val * 100) + 1) * w, ((sat * 100) + 1) * h, 8, paint);
        }

        private void drawBitmap(int width, int height) {
            if (cache == null || cache.isRecycled())
                cache = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(cache);
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
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

        public void setAlpha(int alpha) {
            this.alpha = alpha;
            invalidate();
        }


        public float getSat() {
            return sat;
        }

        public void setSat(float sat) {
            if (sat > 1f)
                sat = 1f;
            else if (sat < 0f)
                sat = 0f;
            this.sat = sat;
            invalidate();
        }

        public float getVal() {
            return val;
        }

        public void setVal(float val) {
            if (val > 1f)
                val = 1f;
            else if (val < 0f)
                val = 0f;
            this.val = val;
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                sat = event.getY() / (float) getMeasuredHeight();
                val = event.getX() / (float) getMeasuredWidth();
                if (sat > 1f)
                    sat = 1f;
                else if (sat < 0f)
                    sat = 0f;
                if (val > 1f)
                    val = 1f;
                else if (val < 0f)
                    val = 0f;
                invalidate();
                if (listener != null) {
                    listener.onSVChanged(sat, val);
                }
                return true;
            }
            return super.onTouchEvent(event);
        }

        public void dispose() {
            cache.recycle();
        }

        public void setOnSVChangedListener(OnSVChangedListener listener) {
            this.listener = listener;
        }

        public interface OnSVChangedListener {
            void onSVChanged(float s, float v);
        }
    }
}
