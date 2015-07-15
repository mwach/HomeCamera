package com.mawa.homecamera.processing.video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import java.io.ByteArrayOutputStream;

/**
 * Created by mawa on 13/07/15.
 */
public class MotionDetector implements Camera.PreviewCallback{

    private MotionDetectorListener listener = null;

    public void setListener(MotionDetectorListener motionDetectorListener){
        this.listener = motionDetectorListener;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        Camera.Parameters parameters = camera.getParameters();
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;

        YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(new Rect(0, 0, width, height), 50, out);

        byte[] bytes = out.toByteArray();
         Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        listener.onFrame(bitmap);
    }
}
