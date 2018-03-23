package com.github.guilhe.circularprogressview;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("unused")
public class CircularProgressView extends View {

    private static final String TAG = CircularProgressView.class.getSimpleName();

    private final CircularProgressViewConfig viewDefaultsConfiguration = this.generateConfigs();

    private final float mDefaultViewPadding = dpToPx(viewDefaultsConfiguration.viewPadding);
    private final float mDefaultShadowPadding = dpToPx(viewDefaultsConfiguration.shadowPadding);
    private final float mDefaultStrokeThickness = dpToPx(viewDefaultsConfiguration.strokeThickness);
    private final int mDefaultMaxWidth = dpToPx(viewDefaultsConfiguration.maxWidthDP);

    private int mMax;
    private boolean mShadowEnabled;
    private boolean mProgressThumbEnabled;
    private int mStartingAngle;
    private float mProgress;
    private float mProgressStrokeThickness;
    private float mProgressIconThickness;
    private int mProgressColor;
    private int mBackgroundColor;
    private boolean mBackgroundAlphaEnabled;

    private RectF mProgressRectF;
    private RectF mShadowRectF;
    private Paint mBackgroundPaint;
    private Paint mProgressPaint;
    private Paint mShadowPaint;
    private int mLastValidRawMeasuredDim;
    private float mLastValidStrokeThickness;

    private TimeInterpolator mInterpolator;
    private Animator mProgressAnimator;
    private OnProgressChangeAnimationCallback mCallback;

    protected CircularProgressViewConfig generateConfigs() {
        return new CircularProgressViewConfig();
    }

    public interface OnProgressChangeAnimationCallback {
        void onProgressChanged(float progress);

        void onAnimationFinished(float progress);
    }

    public CircularProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mLastValidStrokeThickness = mDefaultStrokeThickness;
        mInterpolator = viewDefaultsConfiguration.interpolator;
        mProgressRectF = new RectF();
        mShadowRectF = new RectF();
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.STROKE);

        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressView, 0, 0);
            try {
                mMax = typedArray.getInt(R.styleable.CircularProgressView_max, viewDefaultsConfiguration.max);
                mShadowEnabled = typedArray.getBoolean(R.styleable.CircularProgressView_shadow, true);
                mProgressThumbEnabled = typedArray.getBoolean(R.styleable.CircularProgressView_progressThumb, false);
                mStartingAngle = typedArray.getInteger(R.styleable.CircularProgressView_startingAngle, viewDefaultsConfiguration.startingAngle);
                mProgress = typedArray.getFloat(R.styleable.CircularProgressView_progress, 0);
                mProgressStrokeThickness = typedArray.getDimension(R.styleable.CircularProgressView_progressBarThickness, mDefaultStrokeThickness);
                mProgressColor = typedArray.getInt(R.styleable.CircularProgressView_progressBarColor, viewDefaultsConfiguration.progressColor);
                mBackgroundColor = typedArray.getInt(R.styleable.CircularProgressView_backgroundColor, mProgressColor);
                mBackgroundAlphaEnabled = typedArray.getBoolean(R.styleable.CircularProgressView_backgroundAlphaEnabled, true);
            } finally {
                typedArray.recycle();
            }
        } else {
            mProgressStrokeThickness = mDefaultStrokeThickness;
            mShadowEnabled = true;
            mMax = viewDefaultsConfiguration.max;
            mStartingAngle = viewDefaultsConfiguration.startingAngle;
            mProgressColor = viewDefaultsConfiguration.progressColor;
            mBackgroundColor = mProgressColor;
            mBackgroundAlphaEnabled = true;
        }

        resetBackgroundPaint();
        mProgressPaint.setColor(mProgressColor);
        mShadowPaint.setColor(adjustAlpha(Color.BLACK, 0.2f));
        setThickness(mProgressStrokeThickness, false);
    }

    /**
     * Either width or height, this view will use Math.min(width, height) value.
     * If an invalid size is set it won't take effect and a last valid size will be used.
     * Check {@link #onMeasure(int, int)}
     *
     * @param size in pixels
     */
    public void setSize(int size) {
        getLayoutParams().height = size;
        requestLayout();
    }

    /**
     * This method changes the progress bar starting angle.
     * The default value is 270 and it's equivalent to 12 o'clock.
     *
     * @param angle where the progress bar starts.
     */
    public void setStartingAngle(int angle) {
        mStartingAngle = angle;
        invalidate();
    }

    public int getStartingAngle() {
        return mStartingAngle;
    }

    /**
     * Sets progress bar max value (100%)
     *
     * @param max value
     */
    public void setMax(int max) {
        mMax = max;
        invalidate();
    }

    public int getMax() {
        return mMax;
    }

    /**
     * Changes progress and background color
     *
     * @param color - Color
     */
    public void setColor(int color) {
        setProgressColor(color);
        setBackgroundColor(color);
    }

    /**
     * You can simulate the use of this method with by calling {@link #setColor(int)} with ContextCompat:
     * setBackgroundColor(ContextCompat.getColor(resId));
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setColorResource(@ColorRes int resId) {
        setColor(getContext().getColor(resId));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setColor(Color color) {
        setColor(color.toArgb());
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
        if (mBackgroundColor == -1) {
            setBackgroundColor(color);
        }
        mProgressPaint.setColor(color);
        invalidate();
    }

    /**
     * You can simulate the use of this method with by calling {@link #setProgressColor(int)} with ContextCompat:
     * setProgressColor(ContextCompat.getColor(resId));
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setProgressColorResource(@ColorRes int resId) {
        setProgressColor(getContext().getColor(resId));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setProgressColor(Color color) {
        setProgressColor(color.toArgb());
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
        resetBackgroundPaint();
        invalidate();
    }

    public void setBackgroundAlphaEnabled(boolean enabled) {
        mBackgroundAlphaEnabled = enabled;
        resetBackgroundPaint();
        invalidate();
    }

    public boolean isBackgroundAlphaEnabled(){
        return mBackgroundAlphaEnabled;
    }

    /**
     * You can simulate the use of this method with by calling {@link #setBackgroundColor(int)} with ContextCompat:
     * setBackgroundColor(ContextCompat.getColor(resId));
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setShadowColorResource(@ColorRes int resId) {
        setBackgroundColor(getContext().getColor(resId));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setBackgroundColor(Color color) {
        setBackgroundColor(color.toArgb());
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setShadowEnabled(boolean enable) {
        mShadowEnabled = enable;
        invalidate();
    }

    public boolean isShadowEnabled() {
        return mShadowEnabled;
    }

    public void setProgressThumbEnabled(boolean enable) {
        mProgressThumbEnabled = enable;
        invalidate();
    }

    public boolean isProgressThumbEnabled() {
        return mProgressThumbEnabled;
    }

    /**
     * Changes progressBar & progressIcon, background and shadow line width.
     *
     * @param thickness in pixels
     */
    public void setProgressStrokeThickness(float thickness) {
        setThickness(thickness, true);
    }

    private void setThickness(float thickness, boolean requestLayout) {
        mProgressStrokeThickness = thickness;
        mProgressIconThickness = mProgressStrokeThickness / 2;
        mBackgroundPaint.setStrokeWidth(mProgressStrokeThickness);
        mProgressPaint.setStrokeWidth(mProgressStrokeThickness);
        mShadowPaint.setStrokeWidth(mProgressStrokeThickness);
        if (requestLayout) {
            requestLayout();
        }
    }

    public float getProgressStrokeThickness() {
        return mProgressStrokeThickness;
    }

    public void setProgress(float progress) {
        setProgress(progress, false);
    }

    public void setProgress(float progress, boolean animate) {
        setProgress(progress, animate, viewDefaultsConfiguration.animationMillis);
    }

    public void setProgress(float progress, boolean animate, long duration) {
        setProgress(progress, animate, duration, true);
    }

    public float getProgress() {
        return mProgress;
    }

    public void resetProgress() {
        setProgress(0);
    }

    public void resetProgress(boolean animate) {
        resetProgress(animate, viewDefaultsConfiguration.animationMillis);
    }

    public void resetProgress(boolean animate, long duration) {
        setProgress(0, animate, duration, false);
    }

    public void setAnimationInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator == null ? viewDefaultsConfiguration.interpolator : interpolator;
    }

    public void setProgressAnimationCallback(OnProgressChangeAnimationCallback callback) {
        mCallback = callback;
    }

    private void resetBackgroundPaint() {
        mBackgroundPaint.setColor(mBackgroundAlphaEnabled ? adjustAlpha(mBackgroundColor, viewDefaultsConfiguration.backgroundAlpha) : mBackgroundColor);
    }

    private void setProgress(float progress, boolean animate, long duration, boolean clockwise) {
        if (animate) {
            if (mProgressAnimator != null) {
                mProgressAnimator.cancel();
            }
            mProgressAnimator = getAnimator(getProgress(), clockwise ? progress : 0, duration, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    setProgressValue(((Float) valueAnimator.getAnimatedValue()));
                    if (mCallback != null) {
                        mCallback.onProgressChanged(mProgress);
                    }
                }
            });
            mProgressAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mCallback != null) {
                        mCallback.onAnimationFinished(mProgress);
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    //not in use
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    //not in use
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    //not in use
                }
            });
            mProgressAnimator.start();
        } else {
            setProgressValue(progress);
        }
    }

    private void setProgressValue(float value) {
        mProgress = value;
        invalidate();
    }

    private ValueAnimator getAnimator(double current, double next, long duration, ValueAnimator.AnimatorUpdateListener updateListener) {
        ValueAnimator animator = new ValueAnimator();
        animator.setInterpolator(mInterpolator);
        animator.setDuration(duration);
        animator.setObjectValues(current, next);
        animator.setEvaluator(new FloatEvaluator() {
            public Integer evaluate(float fraction, float startValue, float endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        animator.addUpdateListener(updateListener);
        return animator;
    }

    /**
     * Changes color's alpha by the factor
     *
     * @param color  The color to change alpha
     * @param factor 1.0f (solid) to 0.0f (transparent)
     * @return int - A color with modified alpha
     */
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED ? MeasureSpec.getSize(heightMeasureSpec) : mDefaultMaxWidth;

        int rawMeasuredDim = Math.max(Math.min(width, height), 0);
        float arcDim = mProgressStrokeThickness + mDefaultViewPadding;
        mProgressRectF.set(arcDim, arcDim, rawMeasuredDim - arcDim, rawMeasuredDim - arcDim);

        //To avoid creating a messy composition
        if (mProgressRectF.width() <= mProgressStrokeThickness) {
            rawMeasuredDim = mLastValidRawMeasuredDim;
            mProgressRectF.set(arcDim, arcDim, rawMeasuredDim - arcDim, rawMeasuredDim - arcDim);
            setThickness(mLastValidStrokeThickness, false);
        }
        mLastValidRawMeasuredDim = rawMeasuredDim;
        mLastValidStrokeThickness = mProgressStrokeThickness;

        mShadowRectF.set(mProgressRectF.left, mDefaultShadowPadding + mProgressRectF.top, mProgressRectF.right, mDefaultShadowPadding + mProgressRectF.bottom);
        setMeasuredDimension(rawMeasuredDim, rawMeasuredDim);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Who doesn't love a bit of math? :)
        //cos(a) = adj / hyp <>cos(angle) = x / radius <>x = cos(angle) * radius
        //sin(a) = opp / hyp <>sin(angle) = y / radius <>y = sin(angle) * radius
        //x = cos(startingAngle + progressAngle) * radius + originX(center)
        //y = sin(startingAngle + progressAngle) * radius + originY(center)
        float angle = 360 * mProgress / mMax;
        float radius = getWidth() / 2 - mDefaultViewPadding - mProgressIconThickness - mProgressStrokeThickness / 2;
        double endX = (Math.cos(Math.toRadians(mStartingAngle + angle)) * radius);
        double endY = (Math.sin(Math.toRadians(mStartingAngle + angle)) * radius);
        if (mShadowEnabled) {
            if (mProgressThumbEnabled) {
                canvas.drawCircle((float) endX + mShadowRectF.centerX(), (float) endY + mShadowRectF.centerY(), mProgressIconThickness, mShadowPaint);
            }
            canvas.drawArc(mShadowRectF, mStartingAngle, angle, false, mShadowPaint);
        }
        canvas.drawOval(mProgressRectF, mBackgroundPaint);
        canvas.drawArc(mProgressRectF, mStartingAngle, angle, false, mProgressPaint);

        if (mProgressThumbEnabled) {
            canvas.drawCircle((float) endX + mProgressRectF.centerX(), (float) endY + mProgressRectF.centerY(), mProgressIconThickness, mProgressPaint);
        }
    }

    public int dpToPx(float dp) {
        return (int) Math.ceil(dp * Resources.getSystem().getDisplayMetrics().density);
    }
}