package com.hockeyfinder.hockeyfinderplus;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;


public class MyNavView extends Fragment {

    private DrawerLayout mDrawerLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final FrameLayout layout2 = (FrameLayout)getActivity().findViewById(R.id.popup_frame);
        final FrameLayout layout = (FrameLayout)getActivity().findViewById(R.id.content_frame);

        ImageButton backD = (ImageButton) getActivity().findViewById(R.id.backD);
        ImageButton refreshD = (ImageButton) getActivity().findViewById(R.id.refreshD);
        ImageButton closeD = (ImageButton) getActivity().findViewById(R.id.closeD);
        ImageButton forwardD = (ImageButton) getActivity().findViewById(R.id.forwardD);

        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.backD:
                        if (layout.getVisibility() == View.VISIBLE && ((MainActivity) getActivity()).webView.canGoBack()) {
                            ((MainActivity) getActivity()).webView.goBack();
                        } else if (layout2.getVisibility() == View.VISIBLE && ((MainActivity) getActivity()).wv.canGoBack()) {
                            ((MainActivity) getActivity()).wv.goBack();
                        }
                        break;

                    case R.id.refreshD:
                        if (layout.getVisibility() == View.VISIBLE) {
                            ((MainActivity) getActivity()).webView.loadUrl(((MainActivity) getActivity()).webView.getUrl());
                        } else if (layout2.getVisibility() == View.VISIBLE) {
                            ((MainActivity) getActivity()).wv.loadUrl(((MainActivity) getActivity()).wv.getUrl());
                        }
                        break;

                    case R.id.closeD:
                        if (layout.getVisibility() == View.VISIBLE) {
                            ((MainActivity) getActivity()).exitNotification();
                        } else if (layout2.getVisibility() == View.VISIBLE) {
                            layout2.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            final ActionBar actionBar = getActivity().getActionBar();
                            if (actionBar != null) {
                                actionBar.show();
                            }
                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        }
                        break;

                    case R.id.forwardD:
                        if (layout.getVisibility() == View.VISIBLE && ((MainActivity) getActivity()).webView.canGoForward()) {
                            ((MainActivity) getActivity()).webView.goForward();
                        } else if (layout2.getVisibility() == View.VISIBLE && ((MainActivity) getActivity()).wv.canGoForward()) {
                            ((MainActivity) getActivity()).wv.goForward();
                        }
                        break;

                }

            }

        };

        backD.setOnClickListener(onClickListener2);
        refreshD.setOnClickListener(onClickListener2);
        closeD.setOnClickListener(onClickListener2);
        forwardD.setOnClickListener(onClickListener2);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        return inflater.inflate(R.layout.nav_fragment, container, false);

    }

}
