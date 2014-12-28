package com.hockeyfinder.hockeyfinderplus;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

public class MyWebView extends Fragment {

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

            //override loading outside hockeyfinder
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

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

                    ((MainActivity)getActivity()).webView.loadUrl(url);

                    return false;

                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                startActivity(intent);

                return true;

            }

            //intercept map request and redirect
            @Override
            public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {

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

}