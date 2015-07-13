package com.mawa.homecamera.processing.video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.Image;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by mawa on 13/07/15.
 */
public class MotionDetector implements Camera.PreviewCallback{
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        long time = System.currentTimeMillis();
        Camera.Parameters parameters = camera.getParameters();
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;

        YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(new Rect(0, 0, width, height), 50, out);

        byte[] bytes = out.toByteArray();
         Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        int w2 = bitmap.getWidth();
        int h2= bitmap.getHeight();
        bitmap = null;

        long timeEnd = System.currentTimeMillis();
        long delta = timeEnd - time;
        double fps = 1000.0/delta;

    }
}
