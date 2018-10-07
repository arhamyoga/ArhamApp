package net.yoga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Add in Oncreate() funtion after setContentView()*/
        WebView webView = (WebView) findViewById(R.id.introWebView);
        String path = "file:///android_asset/intro.html";
        webView.loadUrl(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_share:
                Log.d("Main Activity","Share Action");
                shareAction();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void openYoga(View view) {
        Intent intent = new Intent(this, YogaActivity.class);
        startActivity(intent);
    }
}
