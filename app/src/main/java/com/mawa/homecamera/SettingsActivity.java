package com.mawa.homecamera;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        List<Item> items = new ArrayList<>();
        int defaultCameraId = Settings.getDefaultCameraId(SettingsActivity.this);

        int defaultPosition = 0;
        int currentPosition = 0;
        for(int camera : new int[]{Settings.FRONT_CAMERA, Settings.BACK_CAMERA}){
            if(Settings.hasCamera(SettingsActivity.this, camera)) {
                int cameraId = Settings.getCameraId(SettingsActivity.this, camera);
                if(cameraId == defaultCameraId){
                    defaultPosition = currentPosition;
                }
                items.add(new Item(getString(camera == Settings.BACK_CAMERA ? R.string.back_camera : R.string.front_camera), cameraId));
                currentPosition++;
            };
        }

        final Spinner cameraSpinner = (Spinner)findViewById(R.id.spinner_preferable_camera);
        cameraSpinner.setAdapter(new ItemArrayAdapter(SettingsActivity.this, android.R.layout.simple_spinner_item, items.toArray(new Item[items.size()])));
        cameraSpinner.setSelection(defaultPosition);

        cameraSpinner.post(new Runnable() {
            @Override
            public void run() {
                cameraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view;
                        int tag = (int) tv.getTag();

                        Settings.setDefaultCameraId(SettingsActivity.this, tag);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

    }


    private static class Item{
        private String text;
        private int id;

        Item(String text, int id){
            this.text = text;
            this.id = id;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private static class ItemArrayAdapter extends ArrayAdapter<Item>{

        public ItemArrayAdapter(Context context, int resource, Item[] items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Item item = getItem(position);
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
                convertView.setTag(item.id);
            }
            return super.getView(position, convertView, parent);
        }
    }
}
