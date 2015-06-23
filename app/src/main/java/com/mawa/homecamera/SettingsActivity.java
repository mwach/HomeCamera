package com.mawa.homecamera;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mawa.homecamera.accordion.utils.FontUtils;
import com.mawa.homecamera.accordion.widget.AccordionView;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);

//        LinearLayout ll = (LinearLayout) v.findViewById(R.id.settings_layout);
//        TextView tv = new TextView(this);
//        tv.setText("Added in runtime...");
//        FontUtils.setCustomFont(tv, getAssets());
//        ll.addView(tv);

    }
}
