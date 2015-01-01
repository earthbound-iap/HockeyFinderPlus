package com.hockeyfinder.hockeyfinderplus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class LeagueGames extends Activity {

    File sdDir = new File(Environment.getExternalStorageDirectory().getPath());

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.league_games);

        WebView webView = (WebView) findViewById(R.id.webView4);
        webView.getSettings().setJavaScriptEnabled(true);

        File file = new File(sdDir.getAbsolutePath() + "/HockeyFinder/Data/htmlLeague.txt");

        String testHtml = null; // from commons io

        try {

            testHtml = FileUtils.readFileToString(file);

        } catch (IOException e) {

            e.printStackTrace();

            Log.i("bad", e.getMessage());

        }

        webView.loadData(testHtml, "text/html", "UTF-8");

    }

}
