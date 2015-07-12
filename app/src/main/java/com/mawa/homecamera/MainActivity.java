package com.mawa.homecamera;

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
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mawa.homecamera.camera.Preview;

import java.io.IOException;

import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    Camera camera;
    private TextureView textureView;

    ImageButton recordButton = null;
    private SurfaceTexture surface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textureView = (TextureView)findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(this);

        recordButton = (ImageButton) findViewById(R.id.record_button);
        enableGuiRecording();


    }

    boolean cameraFront = false;
    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    private void resetCam() {
        camera.startPreview();
    }

    private void startPreview() {
        try {
            camera.setPreviewTexture(surface);
            camera.startPreview();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // Something bad happened
        }
    }

    private void startRecording() {

        if (getCamera() != null) {

            startPreview();
            disableGuiRecording();
            Toast.makeText(MainActivity.this, getString(R.string.recording_started), LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this, getString(R.string.recording_started), LENGTH_LONG).show();
        }
    }

    private void stopRecording() {

        Toast.makeText(MainActivity.this, getString(R.string.recording_ended), LENGTH_LONG).show();
        enableGuiRecording();
        releaseCamera();
    }

    private Camera getCamera() {

        if(camera != null){
            return camera;
        }
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                camera = Camera.open(0);
            } catch (RuntimeException ex) {
                Log.e(TAG, "getCamera", ex);
            }
        }
        return camera;
    }


    private void releaseCamera() {
        if(camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void disableGuiRecording() {
        recordButton.setImageResource(R.mipmap.ic_launcher);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });
    }

    private void enableGuiRecording() {
        recordButton.setImageResource(R.mipmap.record);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
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
        camera.stopPreview();
        camera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        new Handler().post(new Runnable() {
//            public void run() {
//                Bitmap bmp = textureView.getBitmap();
//                int[] pixelArray = new int[bmp.getHeight() * bmp.getWidth()];
//                bmp.getPixels(pixelArray, 0, 0, 0, 0, bmp.getWidth(), bmp.getHeight());
//                int imgW = bmp.getWidth();
//                int imgH = bmp.getHeight();
//                Log.e(TAG, "" + bmp.getHeight());
//            }
//        });
        Log.e(TAG, "" + 1);

    }
}
