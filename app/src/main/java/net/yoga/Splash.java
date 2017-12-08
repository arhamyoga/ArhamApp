package net.yoga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import net.yoga.lib.FirebaseInstance;

/**
 * Created by mvnpavan on 08/11/17.
 */

public class Splash extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        FirebaseInstance.initialize(this);
        FirebaseInstance.logArhamAppStarted();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
