package com.mou;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler(Looper.myLooper()).postDelayed(next, 3000);
    }

    private Runnable next = () -> startActivity(new Intent(MainActivity.this, MainActivity2.class));

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}