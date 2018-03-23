package com.github.guilhe.circularprogressview;

import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.view.animation.DecelerateInterpolator;

@SuppressWarnings("WeakerAccess, unused")
public class CircularProgressViewConfig {

    private final float DEFAULT_VIEW_PADDING = 10;
    private final float DEFAULT_SHADOW_PADDING = 5;
    private final float DEFAULT_STROKE_THICKNESS = 10;
    private final int DEFAULT_MAX_WIDTH = 100;
    private final int DEFAULT_MAX = 100;
    private final int DEFAULT_STARTING_ANGLE = 270;
    private final int DEFAULT_ANIMATION_MILLIS = 1000;
    private final int DEFAULT_PROGRESS_COLOR = Color.BLACK;
    private final float DEFAULT_BACKGROUND_ALPHA = 0.3f;
    private final TimeInterpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator();


    /**
     * Default values constructor
     */
    public CircularProgressViewConfig() {
        this.viewPadding = DEFAULT_VIEW_PADDING;
        this.shadowPadding = DEFAULT_SHADOW_PADDING;
        this.strokeThickness = DEFAULT_STROKE_THICKNESS;
        this.maxWidthDP = DEFAULT_MAX_WIDTH;
        this.max = DEFAULT_MAX;
        this.startingAngle = DEFAULT_STARTING_ANGLE;
        this.animationMillis = DEFAULT_ANIMATION_MILLIS;
        this.progressColor = DEFAULT_PROGRESS_COLOR;
        this.backgroundAlpha = DEFAULT_BACKGROUND_ALPHA;
        this.interpolator = DEFAULT_INTERPOLATOR;
    }

    /**
     * Custom values constructor. All null values will be assigned the default value.
     *
     * @param viewPadding will default to 10dp if null.
     * @param shadowPadding will default to 5dp if null.
     * @param strokeThickness will default to 10dp if null.
     * @param maxWidthDP will default to 100dp if null.
     * @param max will default to 100 if null.
     * @param startingAngle will default to 270 degrees if null.
     * @param animationMillis will default to 1000ms if null.
     * @param progressColor will default to Color.BLACK if null.
     * @param backgroundAlpha will default to 0.3f if null.
     * @param interpolator will default to new DecelerateInterpolator() if null.
     *
     */
    public CircularProgressViewConfig(
        Float viewPadding,
        Float shadowPadding,
        Float strokeThickness,
        Integer maxWidthDP,
        Integer max,
        Integer startingAngle,
        Integer animationMillis,
        Integer progressColor,
        Float backgroundAlpha,
        TimeInterpolator interpolator
    ) {
        if (viewPadding == null) { viewPadding = DEFAULT_VIEW_PADDING; }
        if (shadowPadding == null) { shadowPadding = DEFAULT_SHADOW_PADDING; }
        if (strokeThickness == null) { strokeThickness = DEFAULT_STROKE_THICKNESS; }
        if (maxWidthDP == null) { maxWidthDP = DEFAULT_MAX_WIDTH; }
        if (max == null) { max = DEFAULT_MAX; }
        if (startingAngle == null) { startingAngle = DEFAULT_STARTING_ANGLE; }
        if (animationMillis == null) { animationMillis = DEFAULT_ANIMATION_MILLIS; }
        if (progressColor == null) { progressColor = DEFAULT_PROGRESS_COLOR; }
        if (backgroundAlpha == null) { backgroundAlpha = DEFAULT_BACKGROUND_ALPHA; }
        if (interpolator == null) { interpolator = DEFAULT_INTERPOLATOR; }

        this.viewPadding = viewPadding;
        this.shadowPadding = shadowPadding;
        this.strokeThickness = strokeThickness;
        this.maxWidthDP = maxWidthDP;
        this.max = max;
        this.startingAngle = startingAngle;
        this.animationMillis = animationMillis;
        this.progressColor = progressColor;
        this.backgroundAlpha = backgroundAlpha;
        this.interpolator = interpolator;
    }

    final float viewPadding;
    final float shadowPadding;
    final float strokeThickness;
    final int maxWidthDP;
    final int max;
    final int startingAngle;
    final int animationMillis;
    final int progressColor;
    final float backgroundAlpha;
    final TimeInterpolator interpolator;
}
