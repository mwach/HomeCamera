package com.mawa.homecamera;

/**
 * Created by mawa on 12/07/15.
 */
public class Settings {

    private static final int UNDEFINED = -1;

    private int frontCameraId;
    private int backCameraId;


    public int getFrontCameraId() {
        return frontCameraId;
    }

    public void setFrontCameraId(int frontCameraId) {
        this.frontCameraId = frontCameraId;
    }

    public int getBackCameraId() {
        return backCameraId;
    }

    public void setBackCameraId(int backCameraId) {
        this.backCameraId = backCameraId;
    }

    public int getDefaultCameraId(){
        return getBackCameraId() != UNDEFINED ? getBackCameraId() : getFrontCameraId();
    }

}
