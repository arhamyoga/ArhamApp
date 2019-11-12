package net.yoga.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import net.yoga.R;
import net.yoga.model.User;

import static net.yoga.utils.Utils.isOnline;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView navHeadingName;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userName="";
    DrawerLayout drawer=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        String currentMobile = mAuth.getCurrentUser().getPhoneNumber();
        DocumentReference docRef = db.collection("users").document(currentMobile);

        //navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //navigation drawer listener
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    User currentUser = documentSnapshot.toObject(User.class);
                    userName = currentUser.getUserName();
                    Log.d("username",userName);
                    navHeadingName = navigationView.findViewById(R.id.navHeadingName);
                    userName = Character.toUpperCase(userName.charAt(0))+userName.substring(1);
                    navHeadingName.setText("Welcome \n"+userName);
                }
            }
        });

        /*Add in Oncreate() funtion after setContentView()*/
        WebView webView = (WebView) findViewById(R.id.introWebView);
        String path = "file:///android_asset/intro.html";
        webView.loadUrl(path);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_share:
//                Log.d("Main Activity","Share Action");
//                shareAction();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Log.d("Main Activity","Share Action");
                shareAction();
                break;
            case R.id.action_rateus:
                rateUs();
                break;
            case R.id.action_notifications:
                Intent i = new Intent(getApplicationContext(),NotificationsActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.action_arhamTimer:
                Intent i2 = new Intent(getApplicationContext(),ReminderActivity.class);
                startActivity(i2);
                finish();
                break;
            case R.id.action_logOut:
                if(isOnline(getApplicationContext())) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), Splash.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(findViewById(android.R.id.content),"Please check your internet...",Snackbar.LENGTH_SHORT).show();
                }
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void shareAction(){
        String urlString = "https://play.google.com/store/apps/details?id=net.yoga";
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String sAux = "\nA unique yoga app based on ancient Jain philosophy.\n\n";
            sAux = sAux + urlString;
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    private void rateUs(){
        Intent intent = new Intent("android.intent.action.VIEW",
                Uri.parse("https://play.google.com/store/apps/details?id=net.yoga"));
        startActivity(intent);
    }

    public void openYoga(View view) {
        Intent intent = new Intent(this, YogaActivity.class);
        startActivity(intent);
    }
}
