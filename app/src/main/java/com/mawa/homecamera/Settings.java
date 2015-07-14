package com.mawa.homecamera;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mawa on 12/07/15.
 */
public class Settings {

    private static final int UNDEFINED = -1;

    public static final int FRONT_CAMERA = 1;
    public static final int BACK_CAMERA = 2;

    private static final String FRONT_CAMERA_ID = "FRONT_CAMERA_ID";
    private static final String BACK_CAMERA_ID = "BACK_CAMERA_ID";
    private static final String DEFAULT_CAMERA_ID = "DEFAULT_CAMERA_ID";

    private Settings(){}

    public static void setCameraId(Context context, int camera, int cameraId) {
        context.getSharedPreferences(MainActivity.ALARM_SERVICE, Context.MODE_PRIVATE).edit().putInt(
                camera == FRONT_CAMERA ? FRONT_CAMERA_ID : BACK_CAMERA_ID, cameraId).apply();
    }

    public static int getDefaultCameraId(Context context){
        return context.getSharedPreferences(MainActivity.ALARM_SERVICE, Context.MODE_PRIVATE).getInt(DEFAULT_CAMERA_ID, UNDEFINED);
    }

    public static void setDefaultCameraId(Context context, int cameraId){
        context.getSharedPreferences(MainActivity.ALARM_SERVICE, Context.MODE_PRIVATE).edit().putInt(DEFAULT_CAMERA_ID, cameraId).apply();
    }

    public static int getCameraId(Context context, int camera){
        return context.getSharedPreferences(MainActivity.ALARM_SERVICE, Context.MODE_PRIVATE).getInt(
                camera == FRONT_CAMERA ? FRONT_CAMERA_ID : BACK_CAMERA_ID, UNDEFINED);
    }

    public static boolean hasCamera(Context context, int cameraId){
        return getCameraId(context, cameraId) != UNDEFINED;
    }
}
