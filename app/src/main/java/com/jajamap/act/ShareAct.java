package com.jajamap.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jajamap.R;


public class ShareAct extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_demo);
    }

    public void startShareDemo(View view) {
        Intent intent = new Intent();
        intent.setClass(this, ShareDemoAct.class);
        startActivity(intent);

    }

}
