package net.yoga.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import net.yoga.R;
import net.yoga.fragments.SpecialSessionFragment;

public class SpecialSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_session);
        String videoType = getIntent().getStringExtra("sessiontype");
        if (savedInstanceState == null) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.special_videos_fragment, new SpecialSessionFragment(videoType));
            beginTransaction.commit();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
