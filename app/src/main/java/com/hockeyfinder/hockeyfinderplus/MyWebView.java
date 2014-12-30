package com.hockeyfinder.hockeyfinderplus;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

public class MyWebView extends Fragment {

    String imageUrl ="";

    private SwipeRefreshLayout swipeLayout;

    @SuppressLint("SetJavaScriptEnabled") @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RelativeLayout rlpopup2 = (RelativeLayout) getActivity().findViewById(R.id.rlpopup2);
        final RelativeLayout exit_popup1 = (RelativeLayout) getActivity().findViewById(R.id.exit_popup);
        final RelativeLayout arena_popup = (RelativeLayout) getActivity().findViewById(R.id.arena_popup);
        final RelativeLayout settings_popup = (RelativeLayout) getActivity().findViewById(R.id.settings_popup);

        exit_popup1.setVisibility(View.INVISIBLE);
        arena_popup.setVisibility(View.INVISIBLE);
        settings_popup.setVisibility(View.INVISIBLE);

        // set up the WebView
        if(getView() != null) {
            ((MainActivity) getActivity()).webView = (WebView) getView().findViewById(R.id.webView);
            ((MainActivity) getActivity()).webView.getSettings().setJavaScriptEnabled(true);
            ((MainActivity) getActivity()).webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            ((MainActivity) getActivity()).webView.getSettings().setSupportMultipleWindows(true);
            ((MainActivity) getActivity()).webView.getSettings().setAllowUniversalAccessFromFileURLs (true);
            ((MainActivity) getActivity()).webView.getSettings().setLoadWithOverviewMode (true);
            //noinspection deprecation
            ((MainActivity) getActivity()).webView.getSettings().setSavePassword (true);
            ((MainActivity) getActivity()).webView.getSettings().setAllowFileAccess (true);
            ((MainActivity) getActivity()).webView.getSettings().setSupportZoom (true);
            ((MainActivity) getActivity()).webView.getSettings().setBuiltInZoomControls(true);
            swipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_container);
        }

        ((MainActivity) getActivity()).webView.setWebChromeClient(new WebChromeClient() {

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

        });



        //noinspection deprecation
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ((MainActivity)getActivity()).webView.loadUrl(((MainActivity)getActivity()).webView.getUrl());

            }
        });

        ((MainActivity)getActivity()).webView.setDownloadListener(new DownloadListener() {

            public void onDownloadStart(String url, String userAgent,String contentDisposition, String mimetype,long contentLength) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }

        });

        //noinspection deprecation
        ((MainActivity)getActivity()).webView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("tel:")) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                    Log.i("Override", "telephone");
                    return false;
                } else if (url.startsWith("mailto:")) {
                    url = url.replaceFirst("mailto:", "");
                    url = url.trim();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("plain/text").putExtra(Intent.EXTRA_EMAIL, new String[]{url});
                    startActivity(i);
                    Log.i("Override", "e-mail");
                    return false;
                } else if (url.startsWith("geo:")) {
                    Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(searchAddress);
                    Log.i("Override", "maps");
                    return false;
                } else if (url.startsWith("www.google")) {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(mapIntent);
                    Log.i("Override", "nav");
                    return false;
                } else if (url.contains("hockeyfinder")) {
                    ((MainActivity)getActivity()).webView.loadUrl(url);
                    Log.i("Override", "HF");
                    return false;
                }
                view.loadUrl(url);
                return true;
            }



            //set progress bar visibilty on page start
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                swipeLayout.setRefreshing(true);

                ((MainActivity)getActivity()).webView.setVisibility(View.INVISIBLE);

                rlpopup2.setVisibility(View.VISIBLE);

            }

            //set progress bar visibilty on page finished
            @Override
            public void onPageFinished(WebView view, String url) {

                String name = ((MainActivity)getActivity()).webView.getTitle();

                getActivity().setTitle(name);

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('_55wp _52z5 _451a _3qet')[0].style.display = 'none'; " +
                        "})()");

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('header navbar navbar-inverse navbar-fixed-top')[0].style.display = 'none'; " +
                        "})()");





                final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeLayout.setRefreshing(false);

                        ((MainActivity)getActivity()).webView.setVisibility(View.VISIBLE);

                        rlpopup2.setVisibility(View.INVISIBLE);

                    }
                }, 2000);

            }

        });

        ((MainActivity)getActivity()).webView.loadUrl("http://www.hockeyfinder.com");

        ((MainActivity) getActivity()).webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                WebView.HitTestResult result = ((MainActivity) getActivity()).webView.getHitTestResult();

                int type = result.getType();

                imageUrl = result.getExtra();

                Log.i("URL LONG CLICK", imageUrl + " " + type);
                if (imageUrl != null) {

                    if (result != null) {


                        if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                            imageUrl = result.getExtra();
                            contextpopup(imageUrl, type);
                            Log.i("HTTP TEST", "image" + " " + imageUrl + " " + type);

                        } else if (type == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
                            if (imageUrl.contains("maps.google")) {
                                imageUrl = result.getExtra();
                                contextpopup(imageUrl, 1);
                                Log.i("HTTP TEST", "address" + " " + imageUrl + " " + "1");

                            } else if (imageUrl.contains("http://")) {
                                imageUrl = result.getExtra();
                                contextpopup(imageUrl, 2);
                                Log.i("HTTP TEST", "http" + " " + imageUrl + " " + "2");

                            } else {
                                contextpopup(imageUrl, type);
                                Log.i("HTTP TEST", "anchor type" + " " + imageUrl + " " + type);
                            }
                        } else if (type == WebView.HitTestResult.PHONE_TYPE) {
                            contextpopup(imageUrl, 1);
                            Log.i("HTTP TEST", "phone" + " " + imageUrl + " " + type);
                        }
                    }

                    return false;
                }
                return false;
            }

        });


    }

    public void contextpopup(String imageUrl, int type) {

        final RelativeLayout context_popup = (RelativeLayout) getActivity().findViewById(R.id.context_popup);

        Button contextClose = (Button) getActivity().findViewById(R.id.contextClose);
        Button contextSave = (Button) getActivity().findViewById(R.id.contextSave);
        Button openNav = (Button) getActivity().findViewById(R.id.openNav);
        Button openHTTP = (Button) getActivity().findViewById(R.id.openHTTP);

        context_popup.setVisibility(View.VISIBLE);

        final String uRl2 = imageUrl;

        if (type == 5) {

            openHTTP.setEnabled(true);
            openNav.setEnabled(false);
            contextSave.setEnabled(true);

        } else if (type == 1) {

            contextSave.setEnabled(false);
            openHTTP.setEnabled(true);
            openNav.setEnabled(true);

        } else if (type == 2) {

            openHTTP.setEnabled(true);
            openNav.setEnabled(false);
            contextSave.setEnabled(false);



        } else if (type == WebView.HitTestResult.PHONE_TYPE) {



        }

        View.OnClickListener onClickListener5 = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                switch (view.getId()) {



                    case R.id.openNav:
                        context_popup.setVisibility(View.INVISIBLE);
                        try {

                            String url3 = uRl2.replace("http://maps.google.com/?q=","").replace("+"," ").replace("%2C"," ");

                            Log.wtf("OPENING/NAV", url3);

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + url3));
                            getActivity().startActivity(intent);

                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), "no app Found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.openHTTP:
                        context_popup.setVisibility(View.INVISIBLE);
                        try {
                            ((MainActivity)getActivity()).webView.getSettings().setGeolocationDatabasePath( getActivity().getFilesDir().getPath() );
                            ((MainActivity)getActivity()).webView.loadUrl(uRl2);

                            Log.wtf("OPENING/WEBPAGE", uRl2);

                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), "no app Found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.contextClose:
                        context_popup.setVisibility(View.INVISIBLE);
                        break;

                    case R.id.contextSave:
                        if(uRl2.endsWith("png")||uRl2.endsWith("jpg")||uRl2.endsWith("gif")||uRl2.endsWith("bmp")) {
                            downloadFile(uRl2);
                        } else {
                            Toast.makeText(getActivity(), "Not An Image File", Toast.LENGTH_LONG).show();
                        }
                        context_popup.setVisibility(View.INVISIBLE);
                        break;

                }

            }

        };
        openHTTP.setOnClickListener(onClickListener5);
        openNav.setOnClickListener(onClickListener5);
        contextClose.setOnClickListener(onClickListener5);
        contextSave.setOnClickListener(onClickListener5);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        return inflater.inflate(R.layout.web_fragment, container, false);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){

        super.onConfigurationChanged(newConfig);

    }



    public void downloadFile(String uRl2) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/HockeyFinder/Images/");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        String baseName = FilenameUtils.getBaseName(uRl2);
        String extension = FilenameUtils.getExtension(uRl2);

        Log.i("SAVE/IMAGE",  "1 " + uRl2);

        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl2);

        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/HockeyFinder/Images/", baseName + "." + extension);

        mgr.enqueue(request);

        Log.i("SAVE/IMAGE",  "2 " + baseName + "." + extension);

        Toast.makeText(getActivity(), "sdCard/HockeyFinder/Images/" + baseName + "." + extension, Toast.LENGTH_LONG).show();

        imageUrl = "";

    }

}