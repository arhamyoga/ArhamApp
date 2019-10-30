package net.yoga.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.yoga.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    void launchSignUp(View v){
        Intent i = new Intent(this,SignUpActivity.class);
        startActivity(i);
        this.finish();
    }

    void launchMainScreen(View v){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        this.finish();
    }
}
