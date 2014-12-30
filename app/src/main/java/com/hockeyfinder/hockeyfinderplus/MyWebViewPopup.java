package com.hockeyfinder.hockeyfinderplus;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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
import android.support.v4.widget.DrawerLayout;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;


public class MyWebViewPopup extends Fragment {

    String imageUrl ="";
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeLayout2;
    String url3 = "";

    @SuppressLint("SetJavaScriptEnabled")@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RelativeLayout rlpopup1 = (RelativeLayout) getActivity().findViewById(R.id.rlpopup1);

        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        // set up the WebView
        if(getView() != null) {
            ((MainActivity) getActivity()).wv = (WebView) getView().findViewById(R.id.webView2);
            ((MainActivity) getActivity()).wv.getSettings().setJavaScriptEnabled(true);
            ((MainActivity) getActivity()).wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            ((MainActivity) getActivity()).wv.getSettings().setSupportMultipleWindows(true);
            ((MainActivity) getActivity()).wv.getSettings().setAllowUniversalAccessFromFileURLs (true);
            ((MainActivity) getActivity()).wv.getSettings().setLoadWithOverviewMode (true);
            //noinspection deprecation
            ((MainActivity) getActivity()).wv.getSettings().setSavePassword (true);
            ((MainActivity) getActivity()).wv.getSettings().setAllowFileAccess (true);
            ((MainActivity) getActivity()).wv.getSettings().setSupportZoom (true);
            ((MainActivity) getActivity()).wv.getSettings().setBuiltInZoomControls(true);
            swipeLayout2 = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_container2);
        }

        ((MainActivity) getActivity()).wv.setWebChromeClient(new WebChromeClient() {

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

        });



        //noinspection deprecation
        swipeLayout2.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ((MainActivity)getActivity()).wv.loadUrl(((MainActivity)getActivity()).wv.getUrl());

            }
        });

        ImageButton btn_show0 = (ImageButton)getView().findViewById(R.id.imageView5);
        ImageButton btn_show1 = (ImageButton)getView().findViewById(R.id.show_popup1);
        ImageButton btn_show2 = (ImageButton)getView().findViewById(R.id.show_popup2);
        ImageButton btn_show3 = (ImageButton)getView().findViewById(R.id.show_popup3);
        ImageButton btn_show4 = (ImageButton)getView().findViewById(R.id.show_popup4);
        ImageButton btn_show5 = (ImageButton)getView().findViewById(R.id.show_popup5);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                switch (view.getId())

                {

                    case R.id.imageView5:

                        FrameLayout layout2 = (FrameLayout)getActivity().findViewById(R.id.popup_frame);
                        FrameLayout layout = (FrameLayout)getActivity().findViewById(R.id.content_frame);

                        if (layout2.getVisibility() == View.VISIBLE) {
                            layout2.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            final ActionBar actionBar = getActivity().getActionBar();

                            if (actionBar != null) {
                                actionBar.show();
                            }

                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                        }
                        break;

                    case R.id.show_popup1:

                        url3 = "https://m.facebook.com/hockeyfindercom";
                        ((MainActivity)getActivity()).wv.loadUrl(url3);

                        break;

                    case R.id.show_popup2:

                        url3 = "http://www.youtube.com/user/Hockeyfinder";
                        ((MainActivity)getActivity()).wv.loadUrl(url3);

                        break;

                    case R.id.show_popup3:

                        url3 = "https://mobile.twitter.com/hockeyfinder";
                        ((MainActivity)getActivity()).wv.loadUrl(url3);

                        break;

                    case R.id.show_popup4:

                        url3 = "https://m.facebook.com/groups/hockeyfinder/";
                        ((MainActivity)getActivity()).wv.loadUrl(url3);

                        break;

                    case R.id.show_popup5:

                        url3 = "http://www.hockeyfinder.com/photos/";
                        ((MainActivity)getActivity()).wv.loadUrl(url3);

                        break;

                }

            }

        };

        btn_show0.setOnClickListener(onClickListener2);
        btn_show1.setOnClickListener(onClickListener2);
        btn_show2.setOnClickListener(onClickListener2);
        btn_show3.setOnClickListener(onClickListener2);
        btn_show4.setOnClickListener(onClickListener2);
        btn_show5.setOnClickListener(onClickListener2);

        ((MainActivity)getActivity()).wv.setDownloadListener(new DownloadListener() {

            public void onDownloadStart(String url, String userAgent,String contentDisposition, String mimetype,long contentLength) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }

        });

        //noinspection deprecation
        ((MainActivity)getActivity()).wv.setWebViewClient(new WebViewClient() {

            //override loading outside hockeyfinder
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains(".com")) {
                    ((MainActivity)getActivity()).wv.loadUrl(url);
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;

            }



            //set progress bar visibilty on page start
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                swipeLayout2.setRefreshing(true);
                ((MainActivity)getActivity()).wv.setVisibility(View.INVISIBLE);
                rlpopup1.setVisibility(View.VISIBLE);

            }

            //set progress bar visibilty on page finished
            @Override
            public void onPageFinished(WebView view, String url) {

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('header navbar navbar-inverse navbar-fixed-top')[0].style.display = 'none'; " +
                        "})()");

                swipeLayout2.setRefreshing(false);

                ((MainActivity)getActivity()).wv.setVisibility(View.VISIBLE);

                rlpopup1.setVisibility(View.INVISIBLE);

            }

        });

        ((MainActivity)getActivity()).wv.loadUrl("about:blank");

        ((MainActivity) getActivity()).wv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                WebView.HitTestResult result = ((MainActivity) getActivity()).wv.getHitTestResult();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.popup_layout, container, false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){

        super.onConfigurationChanged(newConfig);

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
                            ((MainActivity)getActivity()).wv.getSettings().setGeolocationDatabasePath( getActivity().getFilesDir().getPath() );
                            ((MainActivity)getActivity()).wv.loadUrl(uRl2);

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
