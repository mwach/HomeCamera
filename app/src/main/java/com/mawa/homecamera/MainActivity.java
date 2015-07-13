package com.mawa.homecamera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mawa.homecamera.processing.video.MotionDetector;

import java.io.IOException;

import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private Settings settings;

    private static final int UNDEFINED = -1;

    private Camera camera;
    private TextureView textureView;

    private ImageButton recordButton = null;
    private SurfaceTexture surface = null;

    private boolean isRecording = false;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new Settings();

        textureView = (TextureView)findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(this);

        recordButton = (ImageButton) findViewById(R.id.record_button);

        detectCameras();
        setGuiIDLEMode();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(settings.getDefaultCameraId() == UNDEFINED){
            showAlertDialogAndExit(getString(R.string.camera_not_found));
        }
    }

    private void detectCameras() {
        settings.setBackCameraId(findCameraId(Camera.CameraInfo.CAMERA_FACING_BACK));
        settings.setFrontCameraId(findCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT));
    }

    private int findCameraId(int cameraId) {
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == cameraId) {
                return cameraId;
            }
        }
        return UNDEFINED;
    }

    @Override
    protected void onPause() {
        releaseCamera();
        super.onPause();
    }

    private MotionDetector md = new MotionDetector();
    private void startPreview() {
        try {
            camera.setPreviewTexture(surface);
            camera.setPreviewCallback(md);
            camera.startPreview();
            isRecording = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // Something bad happened
        }
    }

    private void startRecording() {

        if (getCamera() != null) {

            setGuiRecordMode();
            startPreview();
            Toast.makeText(MainActivity.this, getString(R.string.recording_started), LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this, getString(R.string.recording_started), LENGTH_LONG).show();
        }
    }

    private void stopRecording() {

        Toast.makeText(MainActivity.this, getString(R.string.recording_ended), LENGTH_LONG).show();
        setGuiIDLEMode();
        releaseCamera();
    }

    private Camera getCamera() {

        if(camera != null){
            return camera;
        }
        cameraId = settings.getDefaultCameraId();

            try {
                camera = Camera.open(cameraId);
            } catch (RuntimeException ex) {
                Log.e(TAG, "getCamera", ex);
            }
        return camera;
    }


    private void releaseCamera() {
        if(getCamera() != null) {
            camera.release();
            camera = null;
        }
        isRecording = false;
    }

    private void setGuiRecordMode() {
        recordButton.setImageResource(R.mipmap.ic_launcher);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });
        textureView.setVisibility(View.VISIBLE);
    }

    private void setGuiIDLEMode() {
        recordButton.setImageResource(R.mipmap.record);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
        textureView.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSettingsActivity() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surface = surface;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        releaseCamera();
        return true;
    }

    private MotionDetector motionDetector;
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        new Handler().post(new Runnable() {
//            public void run() {
//                Bitmap bmp = textureView.getBitmap();
////                int[] pixelArray = new int[bmp.getHeight() * bmp.getWidth()];
////                bmp.getPixels(pixelArray, 0, 0, 0, 0, bmp.getWidth(), bmp.getHeight());
//                int imgW = bmp.getWidth();
//                int imgH = bmp.getHeight();
//                Log.e(TAG, "" + bmp.getHeight());
//            }
//        });
//        Log.e(TAG, "" + 1);

    }

    private void showAlertDialogAndExit(String message){
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                .create();
        alert.setTitle(getString(R.string.error));
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.close_app), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();

    }

}
