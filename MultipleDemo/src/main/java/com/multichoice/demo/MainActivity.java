package com.multichoice.demo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void useWayFirst(View view) {
        startActivity(new Intent(this, UseWayFirst.class));
    }

    public void useWaySecond(View view) {
        startActivity(new Intent(this, UseWaySecond.class));
    }

    public void useWayThird(View view) {
        startActivity(new Intent(this, UseWayThird.class));
    }
}
