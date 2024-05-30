package com.tatanstudios.astropollococina.activitys.loginuser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.fragmentos.loginuser.FragmentLogin;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FragmentLogin fragmentLogin = new FragmentLogin();

        Bundle bundle = new Bundle();
        fragmentLogin.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContenedor, fragmentLogin)
                .addToBackStack(null)
                .commit();


    }

}