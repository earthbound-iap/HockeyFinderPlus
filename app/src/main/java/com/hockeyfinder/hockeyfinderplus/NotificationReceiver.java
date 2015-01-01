package com.hockeyfinder.hockeyfinderplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class NotificationReceiver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onNewIntent(getIntent());

    }

    @Override
    public void onNewIntent(Intent intent) {
        Intent i = getIntent();
        if (i != null) {
                // extract the extra-data in the Notification
                String uRl = i.getStringExtra("akey");

            String message = i.getExtras().getString("akey");

            Log.w(uRl, "MESSAGE: " + uRl);

                WebView webview = new WebView(this);

           webview.setWebViewClient(new WebViewClient());
                setContentView(webview);
                Log.w(uRl, "SIGN UP: " + uRl);
                webview.loadUrl(message);

            }
        }
    }



