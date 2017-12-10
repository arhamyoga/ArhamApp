package net.yoga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    public void openYoga(View view) {
        Intent intent = new Intent(this, YogaActivity.class);
        startActivity(intent);
    }
}
