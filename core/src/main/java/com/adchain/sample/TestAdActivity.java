package com.adchain.sample;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.adchain.DummyAdAdapter;
import com.adchain.R;
import com.adchain.TestAdAdapter;


public class TestAdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ad);

        final String adapterName = getIntent().getStringExtra("name");

        TextView name = findViewById(R.id.name);
        Button button = findViewById(R.id.button);

        name.setText(adapterName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TestAdAdapter.hm.get(adapterName).onClick(null, 0);
            }
        });

    }

}