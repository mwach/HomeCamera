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
import android.widget.ImageView;
import android.widget.Toast;

import com.mawa.homecamera.processing.video.CameraException;
import com.mawa.homecamera.processing.video.MotionDetector;
import com.mawa.homecamera.processing.video.MotionDetectorListener;

import java.io.IOException;

import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, MotionDetectorListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Camera camera;
    private TextureView previewTextureView;
    private SurfaceTexture surface = null;

    private ImageButton recordButton = null;

    private MotionDetector motionDetector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewTextureView = (TextureView)findViewById(R.id.texture_view);
        previewTextureView.setSurfaceTextureListener(this);

        recordButton = (ImageButton) findViewById(R.id.record_button);

        motionDetector = new MotionDetector();
        motionDetector.setListener(this);
        detectCameras();
        setGuiIDLEMode();
    }

    private void detectCameras() {
        Settings.setCameraId(Settings.FRONT_CAMERA, findCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT));
        Settings.setCameraId(Settings.BACK_CAMERA, findCameraId(Camera.CameraInfo.CAMERA_FACING_BACK));
        if(!Settings.hasCamera(Settings.BACK_CAMERA) && !Settings.hasCamera(Settings.FRONT_CAMERA)){
            showAlertDialogAndExit(getString(R.string.camera_not_found));
        }
    }

    private int findCameraId(int camera) {
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == camera) {
                return i;
            }
        }
        return Settings.UNDEFINED;
    }

    @Override
    protected void onPause() {
        releaseCamera();
        super.onPause();
    }

    private void startPreview() {
        try {
            camera.setPreviewTexture(surface);
            camera.setPreviewCallback(motionDetector);
            camera.startPreview();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // Something bad happened
        }
    }

    private void startRecording() {

        try {
            setGuiRecordMode();
            initCamera();
            startPreview();
            Toast.makeText(MainActivity.this, getString(R.string.recording_started), LENGTH_LONG).show();
        }catch (CameraException exc){
            showAlertDialogAndExit(exc.getMessage());
        }
    }

    private void stopRecording() {

        setGuiIDLEMode();
        releaseCamera();
        Toast.makeText(MainActivity.this, getString(R.string.recording_ended), LENGTH_LONG).show();
    }

    private Camera initCamera() throws CameraException {

        if(camera != null){
            return camera;
        }
        int cameraId = Settings.getCameraId(Settings.getDefaultCamera(MainActivity.this));

            try {
                camera = Camera.open(cameraId);
            } catch (RuntimeException ex) {
                Log.e(TAG, "initCamera", ex);
                throw new CameraException(getString(R.string.cannot_connect_camera));
            }
        return camera;
    }


    private void releaseCamera() {
        if(camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    private void setGuiRecordMode() {
        recordButton.setImageResource(R.mipmap.ic_launcher);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });
        previewTextureView.setVisibility(View.VISIBLE);
    }

    private void setGuiIDLEMode() {
        recordButton.setImageResource(R.mipmap.record);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
        previewTextureView.setVisibility(View.GONE);
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

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
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

    @Override
    public void onFrame(final Bitmap bitmap) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                iv.setImageBitmap(bitmap);
            }
        });
    }
}
