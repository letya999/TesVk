package com.example.tesvk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vk.sdk.VKSdk;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VKSdk.isLoggedIn())
            startActivity(new Intent(this, UserActivity.class));
        else
            startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
