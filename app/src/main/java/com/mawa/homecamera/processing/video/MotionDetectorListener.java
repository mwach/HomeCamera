package com.mawa.homecamera.processing.video;

import android.graphics.Bitmap;

/**
 * Created by mawa on 15/07/15.
 */
public interface MotionDetectorListener {

    public void onFrame(Bitmap bitmap);
}
