package com.example.sandrini.pokeagenda;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity implements Runnable {

    private final int DELAY = 3000; //3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Handler h = new Handler();
        h.postDelayed(this, DELAY);
    }

    @Override
    public void run() {
        startActivity(new Intent(this, PokeLoginActivity.class));
        finish();
    }
}
