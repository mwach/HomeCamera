package com.mawa.homecamera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListOfCameras();
    }

    private void populateListOfCameras() {

        List<String> items = new ArrayList<>();
        int defaultCamera = Settings.getDefaultCamera(SettingsActivity.this);

        int defaultPosition = 0;
        int currentPosition = 0;
        for(int camera : new int[]{Settings.FRONT_CAMERA, Settings.BACK_CAMERA}){
            if(Settings.hasCamera(camera)) {
                if(camera == defaultCamera){
                    defaultPosition = currentPosition;
                }
                currentPosition++;
                items.add(getString(camera == Settings.BACK_CAMERA ? R.string.back_camera : R.string.front_camera));
            }
        }

        final Spinner cameraSpinner = (Spinner)findViewById(R.id.spinner_preferable_camera);
        cameraSpinner.setAdapter(new ArrayAdapter(SettingsActivity.this, android.R.layout.simple_spinner_item, items.toArray(new String[items.size()])));
        cameraSpinner.setSelection(defaultPosition);

        cameraSpinner.post(new Runnable() {
            @Override
            public void run() {
                cameraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view;
                        String text = tv.getText().toString();

                        Settings.setDefaultCamera(SettingsActivity.this, text == getString(R.string.front_camera) ? Settings.FRONT_CAMERA : Settings.BACK_CAMERA);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

    }
}
