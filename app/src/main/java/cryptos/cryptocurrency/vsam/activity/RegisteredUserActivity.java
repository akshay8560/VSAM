package cryptos.cryptocurrency.vsam.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cryptos.cryptocurrency.vsam.R;

public class RegisteredUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_already_registered);
    }

    public void finish_user(View view) {
        onBackPressed();
    }
}
