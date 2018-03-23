package com.github.guilhe.circularprogressview_sample;

import android.content.Context;
import android.util.AttributeSet;

import com.github.guilhe.circularprogressview.CircularProgressView;
import com.github.guilhe.circularprogressview.CircularProgressViewConfig;

public class CustomCircularProgressView extends CircularProgressView {
  public CustomCircularProgressView(Context context) {
    super(context);
  }

  public CustomCircularProgressView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomCircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected CircularProgressViewConfig generateConfigs() {
    return new CircularProgressViewConfig.Builder().setViewPadding(0f).build();
  }
}
