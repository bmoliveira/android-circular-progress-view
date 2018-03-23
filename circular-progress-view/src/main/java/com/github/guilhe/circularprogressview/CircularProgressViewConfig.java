package com.github.guilhe.circularprogressview;

import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.view.animation.DecelerateInterpolator;

@SuppressWarnings("WeakerAccess, unused")
public final class CircularProgressViewConfig {
    private static final float defaultViewPadding = 10;
    private static final float defaultShadowPadding = 5;
    private static final float defaultStrokeThickness = 10;
    private static final int defaultMaxWidthDP = 100;
    private static final int defaultMax = 100;
    private static final int defaultStartingAngle = 270;
    private static final int defaultAnimationMillis = 1000;
    private static final int defaultProgressColor = Color.BLACK;
    private static final float defaultBackgroundAlpha = 0.3f;
    private static final TimeInterpolator defaultInterpolator = new DecelerateInterpolator();

    public static class Builder {
        private float viewPadding = defaultViewPadding;
        private float shadowPadding = defaultShadowPadding;
        private float strokeThickness = defaultStrokeThickness;
        private int maxWidthDP = defaultMaxWidthDP;
        private int max = defaultMax;
        private int startingAngle = defaultStartingAngle;
        private int animationMillis = defaultAnimationMillis;
        private int progressColor = defaultProgressColor;
        private float backgroundAlpha = defaultBackgroundAlpha;
        private TimeInterpolator interpolator = defaultInterpolator;


        public Builder() {}

        public Builder setViewPadding(float viewPadding) {
            this.viewPadding = viewPadding;
            return this;
        }

        public Builder setShadowPadding(float shadowPadding) {
            this.shadowPadding = shadowPadding;
            return this;
        }

        public Builder setStrokeThickness(float strokeThickness) {
            this.strokeThickness = strokeThickness;
            return this;
        }

        public Builder setMaxWidthDP(int maxWidthDP) {
            this.maxWidthDP = maxWidthDP;
            return this;
        }

        public Builder setMax(int max) {
            this.max = max;
            return this;
        }

        public Builder setStartingAngle(int startingAngle) {
            this.startingAngle = startingAngle;
            return this;
        }

        public Builder setAnimationMillis(int animationMillis) {
            this.animationMillis = animationMillis;
            return this;
        }

        public Builder setProgressColor(int progressColor) {
            this.progressColor = progressColor;
            return this;
        }

        public Builder setBackgroundAlpha(float backgroundAlpha) {
            this.backgroundAlpha = backgroundAlpha;
            return this;
        }

        public Builder setInterpolator(TimeInterpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public CircularProgressViewConfig build() {
            return new CircularProgressViewConfig(viewPadding, shadowPadding, strokeThickness,
                maxWidthDP, max, startingAngle, animationMillis, progressColor, backgroundAlpha,
                interpolator);
        }
    }

    /**
     * Default values constructor.
     */
    public CircularProgressViewConfig() {
        this.viewPadding = defaultViewPadding;
        this.shadowPadding = defaultShadowPadding;
        this.strokeThickness = defaultStrokeThickness;
        this.maxWidthDP = defaultMaxWidthDP;
        this.max = defaultMax;
        this.startingAngle = defaultStartingAngle;
        this.animationMillis = defaultAnimationMillis;
        this.progressColor = defaultProgressColor;
        this.backgroundAlpha = defaultBackgroundAlpha;
        this.interpolator = defaultInterpolator;
    }

    /**
     * Custom values constructor.
     */
    private CircularProgressViewConfig(
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
