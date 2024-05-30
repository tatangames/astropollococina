package com.tatanstudios.astropollococina.activitys.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.fragmentos.splash.FragmentSplash;



public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, new FragmentSplash())
                .commit();
    }


}