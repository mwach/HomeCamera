package com.mawa.homecamera;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mawa on 12/07/15.
 */
class Settings {

    static final int UNDEFINED = -1;

    public static final int FRONT_CAMERA = 1;
    public static final int BACK_CAMERA = 2;

    private static int frontCameraId = UNDEFINED;
    private static int backCameraId = UNDEFINED;

    private static final String DEFAULT_CAMERA = "DEFAULT_CAMERA";

    private Settings(){}

    public static void setCameraId(int camera, int cameraId) {
        if(camera == FRONT_CAMERA){
            frontCameraId = cameraId;
        } else if(camera == BACK_CAMERA){
            backCameraId = cameraId;
        }
    }

    public static int getDefaultCamera(Context context){
        int defaultCamera = getSharedPreference(context).getInt(DEFAULT_CAMERA, UNDEFINED);
        if (defaultCamera == UNDEFINED){
            defaultCamera = hasCamera(BACK_CAMERA) ? BACK_CAMERA : FRONT_CAMERA;
            setDefaultCamera(context, defaultCamera);
        }
        return defaultCamera;
    }

    public static void setDefaultCamera(Context context, int cameraId){
        getSharedPreference(context).edit().putInt(DEFAULT_CAMERA, cameraId).apply();
    }

    public static int getCameraId(int camera){
        if(camera == FRONT_CAMERA){
            return frontCameraId;
        } else if(camera == BACK_CAMERA){
            return backCameraId;
        }
        return UNDEFINED;
    }

    public static boolean hasCamera(int cameraId){
        return getCameraId(cameraId) != UNDEFINED;
    }

    private static SharedPreferences preferences = null;
    private static synchronized SharedPreferences getSharedPreference(Context context){
        if(preferences == null) {
            preferences = context.getSharedPreferences(MainActivity.ALARM_SERVICE, Context.MODE_PRIVATE);
        }
        return preferences;
    }
}
