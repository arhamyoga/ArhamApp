package net.yoga.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.yoga.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    void launchLogin(View v){
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        this.finish();
    }
}
