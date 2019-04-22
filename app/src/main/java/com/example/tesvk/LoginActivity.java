package com.example.tesvk;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] scope = new String[]{
            VKScope.FRIENDS,
            VKScope.PHOTOS,
            VKScope.NOHTTPS
    };

    private final String TokenKey ="VK_ACCESS_TOKEN";
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.button);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (! VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                res.saveTokenToSharedPreferences(getApplicationContext(), TokenKey);
                startActivity(new Intent(LoginActivity.this, UserActivity.class));
                finish();
            }
            @Override
            public void onError(VKError error) {
                new AlertDialog.Builder(LoginActivity.this).setMessage(error.errorMessage).show();
                Toast.makeText(LoginActivity.this, "Ошибка авторизации.", Toast.LENGTH_SHORT).show();
                VKSdk.login(LoginActivity.this, scope);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        VKSdk.login(this, scope);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
