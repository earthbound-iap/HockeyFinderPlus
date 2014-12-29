package com.hockeyfinder.hockeyfinderplus;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;


public class MyWebViewPopup extends Fragment {


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

            //intercept map request and redirect
            @Override
            public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {

                if (url.startsWith("tel:")) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                } else if (url.startsWith("mailto:")) {
                    url = url.replaceFirst("mailto:", "");
                    url = url.trim();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("plain/text").putExtra(Intent.EXTRA_EMAIL, new String[]{url});
                    startActivity(i);
                } else if (url.startsWith("maps")) {
                    Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(searchAddress);
                } else if (url.startsWith("www.google")) {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(mapIntent);
                } else if (url.contains("hockeyfinder")) {
                    ((MainActivity)getActivity()).wv.loadUrl(url);
                }

                if (url.equals("http://pagead2.googlesyndication.com/pagead/show_ads.js")) {
                    ByteArrayInputStream test1 = null;
                    try {
                        test1 = new ByteArrayInputStream("// script blocked".getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return new WebResourceResponse("text/javascript", "UTF-8", test1);
                } else {
                    //noinspection deprecation
                    return super.shouldInterceptRequest(view, url);

                }

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.popup_layout, container, false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){

        super.onConfigurationChanged(newConfig);

    }

}
