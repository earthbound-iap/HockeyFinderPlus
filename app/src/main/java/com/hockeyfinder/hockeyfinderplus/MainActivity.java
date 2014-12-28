package com.hockeyfinder.hockeyfinderplus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



public class MainActivity extends Activity {

    @SuppressWarnings("deprecation")

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;

    ExpandableListView expListView;

    HashMap<String, List<String>> listDataChild;

    ExpandableListAdapter listAdapter;

    List<String> listDataHeader;

    public WebView wv;

    public WebView webView;

    private int lastExpandedPosition = -1;

    int popupCounter = 0;

    private NotificationManager mNotificationManager;

    private int notificationID = 10;

    int minutes1;

    String imageUrl ="";

    private PendingIntent pendingIntent;

    File sdDir = new File(Environment.getExternalStorageDirectory().getPath());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        File createDir = new File(sdDir.getAbsolutePath() + "/HockeyFinder/Data");
        //noinspection ResultOfMethodCallIgnored

        if (!createDir.exists()) {

            createDir.mkdirs();

        }

        final RelativeLayout context_popup = (RelativeLayout) findViewById(R.id.context_popup);

        context_popup.setVisibility(View.INVISIBLE);

        wv = (WebView) findViewById(R.id.webView2);
        webView = (WebView) findViewById(R.id.webView);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            //stopService(new Intent(this, MyService.class));

            // enabling action bar app icon and behaving it as toggle button
            if (getActionBar() != null) {

                getActionBar().setDisplayHomeAsUpEnabled(true);

                getActionBar().setHomeButtonEnabled(true);

            }

        //noinspection deprecation
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {

                    getActionBar();

                    webView = (WebView) findViewById(R.id.webView);

                    String name = webView.getTitle();

                    setTitle(name);


                    // calling onPrepareOptionsMenu() to show action bar icons
                    invalidateOptionsMenu();

                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {

                    setTitle("Navigation");

                    // calling onPrepareOptionsMenu() to hide action bar icons

                    invalidateOptionsMenu();

                }

            };

            mDrawerLayout.setDrawerListener(mDrawerToggle);

            android.app.Fragment fragment = new MyWebViewPopup();

            android.app.Fragment fragment2 = new MyWebView();

            android.app.Fragment fragment3 = new MyNavView();

            if (savedInstanceState == null) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.add(R.id.popup_frame, fragment);

                ft.add(R.id.content_frame, fragment2);

                ft.add(R.id.nav_frame, fragment3);

                ft.commit();

                FrameLayout popup_frame = (FrameLayout) findViewById(R.id.popup_frame);

                popup_frame.setVisibility(View.INVISIBLE);

        }

        // Adding child data
        final List<String> accountUrl;

            accountUrl = new ArrayList<>();

            accountUrl.add("http://www.hockeyfinder.com/my-games/");

            accountUrl.add("http://www.hockeyfinder.com/account/");

        final List<String> playerUrl;

            playerUrl = new ArrayList<>();

            playerUrl.add("http://www.hockeyfinder.com/skate-times/");

            playerUrl.add("http://www.hockeyfinder.com/tournaments/");

            playerUrl.add("(http://www.hockeyfinder.com/clinics/");

            playerUrl.add("http://www.hockeyfinder.com/hockeyleague/");

            playerUrl.add("http://www.hockeyfinder.com/rink-directory/");

        final List<String> refereeUrl;

            refereeUrl = new ArrayList<>();

            refereeUrl.add("http://www.hockeyfinder.com/referees-needed/");

        final List<String> organizerUrl;

            organizerUrl = new ArrayList<>();

            organizerUrl.add("http://www.hockeyfinder.com/promote-hockey-games/");

            organizerUrl.add("(http://www.hockeyfinder.com/clinics/");

            organizerUrl.add("http://www.hockeyfinder.com/rent-a-goalie-or-referee/");

            organizerUrl.add("http://www.hockeyfinder.com/rent-a-goalie-or-referee/");

        final List<String> leaguesUrl;

            leaguesUrl = new ArrayList<>();

            leaguesUrl.add("http://www.hockeyfinder.com/hockeyleague/");

            leaguesUrl.add("http://www.hockeyfinder.com/wham/");

            leaguesUrl.add("http://www.hockeyfinder.com/youthhockeyleague/");

            leaguesUrl.add("http://www.hockeyfinder.com/league-calendar/");

            leaguesUrl.add("http://www.hockeyfinder.com/photos/");

        final List<String> infoUrl;

            infoUrl = new ArrayList<>();

            infoUrl.add("http://www.hockeyfinder.com/register/");

            infoUrl.add("http://www.hockeyfinder.com/tour/");

            infoUrl.add("http://www.hockeyfinder.com/faq/");

            infoUrl.add("http://www.hockeyfinder.com/sponsorship/");

            infoUrl.add("http://www.hockeyfinder.com/custom-hockey-jerseys/");

            infoUrl.add("http://www.hockeyfinder.com/contact/");

        expListView = (ExpandableListView) findViewById(R.id.lvexp);

        prepareListData();

        listAdapter = new ExpandableListAdapter3(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                switch (groupPosition) {

                    case 0: {

                        String url = accountUrl.get(childPosition);

                        webView.loadUrl(url);

                        break;

                    }

                    case 1: {

                        String url = playerUrl.get(childPosition);

                        webView.loadUrl(url);

                        break;

                    }

                    case 2: {

                        String url = refereeUrl.get(childPosition);

                        webView.loadUrl(url);

                        break;

                    }

                    case 3: {

                        String url = organizerUrl.get(childPosition);

                        webView.loadUrl(url);

                        break;

                    }

                    case 4: {

                        String url = leaguesUrl.get(childPosition);

                        webView.loadUrl(url);

                        break;

                    }

                    case 5: {

                        String url = infoUrl.get(childPosition);

                        webView.loadUrl(url);

                        break;

                    }

                    default:

                }

                mDrawerLayout.closeDrawer(expListView);

                return false;

            }

        });

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;

            }

        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                if (lastExpandedPosition != -1

                        && groupPosition != lastExpandedPosition) {

                    expListView.collapseGroup(lastExpandedPosition);

                }

                lastExpandedPosition = groupPosition;

            }

        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {}

        });


    }


    @Override
    public void onBackPressed() {

        FrameLayout layout2 = (FrameLayout) findViewById(R.id.popup_frame);

        FrameLayout layout = (FrameLayout) findViewById(R.id.content_frame);

        if (layout.getVisibility() == View.VISIBLE && webView.canGoBack()) {

            webView.goBack();

        } else if (layout2.getVisibility() == View.VISIBLE && wv.canGoBack()) {

            wv.goBack();

        } else {

            exitNotification();

        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        this.registerForContextMenu(webView);

        this.registerForContextMenu(wv);

        mDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {

            return true;

        }

        switch (item.getItemId()) {

            case R.id.social:

                changeFragment();

                return true;

            case R.id.menu_item1:

                webView.loadUrl("http://www.hockeyfinder.com/login/classic/");

                return true;

            case R.id.menu_item3:

                webView.loadUrl("http://www.hockeyfinder.com/account/");

                return true;

            case R.id.menu_item4:

                webView.loadUrl("http://www.hockeyfinder.com/change-password/");

                return true;

            case R.id.menu_item8:

                webView.loadUrl("http://www.hockeyfinder.com/logout/");

                return true;

            case R.id.menu_item9:

                arenapopup();

                return true;

            default:

                return

                   super.onOptionsItemSelected(item);

        }

    }

    private void prepareListData() {

        listDataHeader = new ArrayList<>();

        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("My Account");

        listDataHeader.add("Player");

        listDataHeader.add("Referee");

        listDataHeader.add("Organizer");

        listDataHeader.add("Leagues");

        listDataHeader.add("Info/Misc.");

        // Adding child data
        List<String> account;

            account = new ArrayList<>();

            account.add("My Games");

            account.add("My Account");

        List<String> player;

            player = new ArrayList<>();

            player.add("Pickup Games");

            player.add("Tournaments");

            player.add("Clinics");

            player.add("Leagues");

            player.add("Rink Directory");

        List<String> referee;

            referee = new ArrayList<>();

            referee.add("Referees Needed");

        List<String> organizer;

            organizer = new ArrayList<>();

            organizer.add("Add Your Game");

            organizer.add("Clinic");

            organizer.add("Referee Finder");

            organizer.add("Goalie Finder");

        List<String> leagues;

            leagues = new ArrayList<>();

            leagues.add("Coed Adult Leagues");

            leagues.add("Women's Leagues");

            leagues.add("Youth Leagues");

            leagues.add("Leagues Calendar");


        List<String> info;

            info = new ArrayList<>();

            info.add("Register");

            info.add("Feature Tour");

            info.add("Hockey Finder FAQ");

            info.add("Sponsorship");

            info.add("Custom Jerseys");

            info.add("Contact Us");

        // Adding child data
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") final List<String> accountUrl;

        accountUrl = new ArrayList<>();

            accountUrl.add("http://www.hockeyfinder.com/my-games/");

            accountUrl.add("http://www.hockeyfinder.com/account/");

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") final List<String> playerUrl;

        playerUrl = new ArrayList<>();

            playerUrl.add("http://www.hockeyfinder.com/skate-times/");

            playerUrl.add("http://www.hockeyfinder.com/tournaments/");

            playerUrl.add("(http://www.hockeyfinder.com/clinics/");

            playerUrl.add("http://www.hockeyfinder.com/hockeyleague/");

            playerUrl.add("http://www.hockeyfinder.com/rink-directory/");

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") final List<String> refereeUrl;

            refereeUrl = new ArrayList<>();

            refereeUrl.add("http://www.hockeyfinder.com/referees-needed/");

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") final List<String> organizerUrl;

            organizerUrl = new ArrayList<>();

            organizerUrl.add("http://www.hockeyfinder.com/promote-hockey-games/");

            organizerUrl.add("(http://www.hockeyfinder.com/clinics/");

            organizerUrl.add("http://www.hockeyfinder.com/rent-a-goalie-or-referee/");

            organizerUrl.add("http://www.hockeyfinder.com/rent-a-goalie-or-referee/");

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") final List<String> leaguesUrl;

            leaguesUrl = new ArrayList<>();

            leaguesUrl.add("http://www.hockeyfinder.com/hockeyleague/");

            leaguesUrl.add("http://www.hockeyfinder.com/wham/");

            leaguesUrl.add("http://www.hockeyfinder.com/youthhockeyleague/");

            leaguesUrl.add("http://www.hockeyfinder.com/league-calendar/");

            leaguesUrl.add("http://www.hockeyfinder.com/photos/");

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") final List<String> infoUrl;

            infoUrl = new ArrayList<>();

            infoUrl.add("http://www.hockeyfinder.com/register/");

            infoUrl.add("http://www.hockeyfinder.com/tour/");

            infoUrl.add("http://www.hockeyfinder.com/faq/");

            infoUrl.add("http://www.hockeyfinder.com/sponsorship/");

            infoUrl.add("http://www.hockeyfinder.com/custom-hockey-jerseys/");

            infoUrl.add("http://www.hockeyfinder.com/contact/");

        listDataChild.put(listDataHeader.get(0), account); // Header, Child data

        listDataChild.put(listDataHeader.get(1), player);

        listDataChild.put(listDataHeader.get(2), referee);

        listDataChild.put(listDataHeader.get(3), organizer);

        listDataChild.put(listDataHeader.get(4), leagues);

        listDataChild.put(listDataHeader.get(5), info);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {

            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {

                try {

                    Method m = menu.getClass().getDeclaredMethod(

                            "setOptionalIconsVisible", Boolean.TYPE);

                    m.setAccessible(true);

                    m.invoke(menu, true);

                } catch (Exception e) {

                    throw new RuntimeException(e);

                }

            }

        }

        return super.onMenuOpened(featureId, menu);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);

        webView.saveState(outState);

        wv.saveState(outState);

    }

    @Override
    public void onPause() {

        super.onPause();

        webView.onPause();

        webView.pauseTimers();

        wv.onPause();

        wv.pauseTimers();

    }

    @Override
    public void onResume() {

        super.onResume();

        webView.onResume();

        webView.resumeTimers();

        wv.onResume();

        wv.resumeTimers();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        webView.removeAllViews();

        webView.loadUrl("about:blank");

        webView.stopLoading();

        webView.setWebChromeClient(null);

        webView.setWebViewClient(null);

        webView.destroy();

        webView = null;

        wv.removeAllViews();

        wv.loadUrl("about:blank");

        wv.stopLoading();

        wv.setWebChromeClient(null);

        wv.setWebViewClient(null);

        wv.destroy();

        wv = null;

    }

    protected void displayNotification() {

        Log.i("Start", "notification");

      /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder =

                new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("Hockey Finder");

        mBuilder.setContentText("Click Here To Open/Swipe To Close");

        mBuilder.setTicker("Notification Icon Enabled");

        mBuilder.setSmallIcon(R.drawable.ic_launcher);

      /* Increase notification number every time a new notification arrives */
      /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

      /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =

                stackBuilder.getPendingIntent(

                        0,

                        PendingIntent.FLAG_UPDATE_CURRENT

                );

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      /* notificationID allows you to update the notification later on. */
        int notificationID = 1001;

        mNotificationManager.notify(notificationID, mBuilder.build());

    }

    protected void exitNotification() {

        final RelativeLayout exit_popup1 = (RelativeLayout) findViewById(R.id.exit_popup);

        exit_popup1.setVisibility(View.VISIBLE);

        Button btn_show7 = (Button) findViewById(R.id.cancel_close);

        Button btn_show8 = (Button) findViewById(R.id.close_app);

        Button btn_show9 = (Button) findViewById(R.id.close_notification);

        View.OnClickListener onClickListener3 = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.cancel_close:

                        exit_popup1.setVisibility(View.INVISIBLE);

                        break;

                    case R.id.close_app:

                        finish();

                        break;

                    case R.id.close_notification:

                        displayNotification();

                        exit_popup1.setVisibility(View.INVISIBLE);

                        break;

                }

            }

        };

        btn_show7.setOnClickListener(onClickListener3);

        btn_show8.setOnClickListener(onClickListener3);

        btn_show9.setOnClickListener(onClickListener3);

    }

    protected void changeFragment() {

        FrameLayout layout = (FrameLayout) findViewById(R.id.content_frame);

        layout.setVisibility(View.GONE);

        FrameLayout layout2 = (FrameLayout) findViewById(R.id.popup_frame);

        layout2.setVisibility(View.VISIBLE);

        final ActionBar actionBar = getActionBar();

        if(actionBar!=null){

            actionBar.hide();

        }

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if(popupCounter==0){

            popupCounter = 1;

            wv.loadUrl("https://m.facebook.com/hockeyfindercom");

        }

    }

    @Override
    protected void onStop() {

        super.onStop();

        //startService(new Intent(this, MyService.class));
        displayNotification();

    }


    public void arenapopup(){

        final RelativeLayout arena_popup = (RelativeLayout) findViewById(R.id.arena_popup);

        arena_popup.setVisibility(View.VISIBLE);

        final CheckBox cbLED = (CheckBox) findViewById(R.id.cbLED);

        final CheckBox cbSOUND = (CheckBox) findViewById(R.id.cbSOUND);

        final CheckBox cbVIBRATE = (CheckBox) findViewById(R.id.cbVIBRATE);

        final CheckBox cbAll = (CheckBox) findViewById(R.id.cbAll);

        final CheckBox cbExcel = (CheckBox) findViewById(R.id.cbExcel);

        final CheckBox cbSchwans = (CheckBox) findViewById(R.id.cbSchwans);

        final CheckBox cbFogerty = (CheckBox) findViewById(R.id.cbFogerty);

        final CheckBox cbBrooklynPark = (CheckBox) findViewById(R.id.cbBrooklynPark);

        final CheckBox cbVadnais = (CheckBox) findViewById(R.id.cbVadnais);

        final CheckBox cbVeterans = (CheckBox) findViewById(R.id.cbVeterans);

        final CheckBox cbVictory = (CheckBox) findViewById(R.id.cbVictory);

        final CheckBox cbPolar = (CheckBox) findViewById(R.id.cbPolar);

        final CheckBox cbMNMade = (CheckBox) findViewById(R.id.cbMNMade);

        final CheckBox cbParadeIceGarden = (CheckBox) findViewById(R.id.cbParadeIceGarden);

        final CheckBox cbOscarJohnson = (CheckBox) findViewById(R.id.cbOscarJohnson);

        final CheckBox cbHighland = (CheckBox) findViewById(R.id.cbHighland);

        final CheckBox cbFLAAASC = (CheckBox) findViewById(R.id.cbFLAAASC);

        final CheckBox cb20 = (CheckBox) findViewById(R.id.cb20);

        final CheckBox cb40 = (CheckBox) findViewById(R.id.cb40);

        final CheckBox cb60 = (CheckBox) findViewById(R.id.cb60);

        Button cbSettings = (Button) findViewById(R.id.cbSettings);

        Button startArena = (Button) findViewById(R.id.startArena);

        Button clearArena = (Button) findViewById(R.id.clearArena);

        Button cancelNotify = (Button) findViewById(R.id.cancelNotify);

        Button cancelArena = (Button) findViewById(R.id.cancelArena);

        final RelativeLayout settings_popup = (RelativeLayout) findViewById(R.id.settings_popup);

        TinyDB tinydb = new TinyDB(getApplicationContext());

        final ArrayList<String> arena = tinydb.getList("arena");

        final ArrayList<String> notification = tinydb.getList("notification");

        final int minutes = tinydb.getInt("minutes");

        if(minutes == 1200000){

            cb20.setChecked(true);

            cb40.setChecked(false);

            cb60.setChecked(false);

        }else {

            cb20.setChecked(false);

        }

        if(minutes == 2400000){

            cb40.setChecked(true);

            cb20.setChecked(false);

            cb60.setChecked(false);

        }else {

            cb40.setChecked(false);

        }

        if(minutes == 3600000){

            cb60.setChecked(true);

            cb20.setChecked(false);

            cb40.setChecked(false);

        }else {

            cb60.setChecked(false);

        }

            if(notification.contains("LED")){

                cbLED.setChecked(true);

            }else {

                cbLED.setChecked(false);

            }

            if(notification.contains("SOUND")){

                cbSOUND.setChecked(true);

            }else {

                cbSOUND.setChecked(false);

            }

            if(notification.contains("VIBRATE")){

                cbVIBRATE.setChecked(true);

            }else {

                cbVIBRATE.setChecked(false);

            }

            if(arena.contains("Xcel Energy Center")){

                cbExcel.setChecked(true);

            }else {

                cbExcel.setChecked(false);

            }

            if(arena.contains("Schwan Super Rink")){

                cbSchwans.setChecked(true);

            }else {

                cbSchwans.setChecked(false);

            }

            if(arena.contains("Fogerty Arena")){

                cbFogerty.setChecked(true);

            }else {

                cbFogerty.setChecked(false);

            }

            if(arena.contains("Brooklyn Park Activity Center")){

                cbBrooklynPark.setChecked(true);

            }else {

                cbBrooklynPark.setChecked(false);

            }

            if(arena.contains("Vadnais Sports Center")){

                cbVadnais.setChecked(true);

            }else {

                cbVadnais.setChecked(false);

            }

            if(arena.contains("Veterans Memorial Community Center")){

                cbVeterans.setChecked(true);

            }else {

                cbVeterans.setChecked(false);

            }

            if(arena.contains("Victory Memorial Ice Arena")){

                cbVictory.setChecked(true);

            }else {

                cbVictory.setChecked(false);

            }

            if(arena.contains("Polar Arena")){

                cbPolar.setChecked(true);

            }else {

                cbPolar.setChecked(false);

            }

            if(arena.contains("Minnesota Made Ice Center")){

                cbMNMade.setChecked(true);

            }else {

                cbMNMade.setChecked(false);

            }

            if(arena.contains("Parade Ice Garden")){

                cbParadeIceGarden.setChecked(true);

            }else {

                cbParadeIceGarden.setChecked(false);

            }

            if(arena.contains("Oscar Johnson Arena")){

                cbOscarJohnson.setChecked(true);

            }else {

                cbOscarJohnson.setChecked(false);

            }

            if(arena.contains("Highland Arena")){

                cbHighland.setChecked(true);

            }else {

                cbHighland.setChecked(false);

            }

            if(arena.contains("FLAAA Sports Center")){

                cbFLAAASC.setChecked(true);

            }else {

                cbFLAAASC.setChecked(false);

            }




        View.OnClickListener onClickListener4 = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.cb20:

                        if (cbLED.isChecked()) {

                            cb40.setChecked(false);

                            cb60.setChecked(false);

                        } else {

                        }

                        break;

                    case R.id.cb40:

                        if (cbLED.isChecked()) {

                            cb20.setChecked(false);

                            cb60.setChecked(false);

                        } else {

                        }

                        break;

                    case R.id.cb60:

                        if (cbLED.isChecked()) {

                            cb20.setChecked(false);

                            cb40.setChecked(false);

                        } else {

                        }

                        break;

                    case R.id.cbSettings:

                        if (settings_popup.getVisibility() == View.VISIBLE) {

                            settings_popup.setVisibility(View.INVISIBLE);

                        } else {

                            settings_popup.setVisibility(View.VISIBLE);

                        }

                        break;

                    case R.id.cbAll:

                        if (cbAll.isChecked()) {

                            cbExcel.setChecked(true);

                            cbSchwans.setChecked(true);

                            cbFogerty.setChecked(true);

                            cbBrooklynPark.setChecked(true);

                            cbVadnais.setChecked(true);

                            cbVeterans.setChecked(true);

                            cbVictory.setChecked(true);

                            cbPolar.setChecked(true);

                            cbMNMade.setChecked(true);

                            cbParadeIceGarden.setChecked(true);

                            cbOscarJohnson.setChecked(true);

                            cbHighland.setChecked(true);

                            cbFLAAASC.setChecked(true);

                        } else {

                            cbExcel.setChecked(false);

                            cbSchwans.setChecked(false);

                            cbFogerty.setChecked(false);

                            cbBrooklynPark.setChecked(false);

                            cbVadnais.setChecked(false);

                            cbVeterans.setChecked(false);

                            cbVictory.setChecked(false);

                            cbPolar.setChecked(false);

                            cbMNMade.setChecked(false);

                            cbParadeIceGarden.setChecked(false);

                            cbOscarJohnson.setChecked(false);

                            cbHighland.setChecked(false);

                            cbFLAAASC.setChecked(false);

                        }

                        break;

                    case R.id.startArena:

                        if (cbLED.isChecked()) {

                            if(notification.contains("LED")){

                            }else {

                                notification.add("LED");
                            }

                        } else {

                            if(notification.contains("LED")){

                                notification.remove("LED");

                            }else {

                            }

                        }

                        if (cbSOUND.isChecked()) {

                            if(notification.contains("SOUND")){

                            }else {

                                notification.add("SOUND");

                            }

                        } else {

                            if(notification.contains("SOUND")){

                                notification.remove("SOUND");

                            }else {

                            }

                        }

                        if (cbVIBRATE.isChecked()) {

                            if(notification.contains("VIBRATE")){

                            }else {

                                notification.add("VIBRATE");

                            }

                        } else {

                            if(notification.contains("VIBRATE")){

                                notification.remove("VIBRATE");

                            }else {

                            }

                        }

                        if (cbExcel.isChecked()) {

                            if(arena.contains("Xcel Energy Center")){

                            }else {

                                arena.add("Xcel Energy Center");

                            }

                        } else {

                            if(arena.contains("Xcel Energy Center")){

                                arena.remove("Xcel Energy Center");

                            }else {

                            }

                        }

                        if (cbSchwans.isChecked()) {

                            if(arena.contains("Schwan Super Rink")){

                                //do nothing
                            }else {

                                arena.add("Schwan Super Rink");

                            }

                        } else {

                            if(arena.contains("Schwan Super Rink")){

                                arena.remove("Schwan Super Rink");

                            }else {

                            }

                        }

                        if (cbFogerty.isChecked()) {

                            if(arena.contains("Fogerty Arena")){

                            }else {

                                arena.add("Fogerty Arena");

                            }

                        } else {

                            if(arena.contains("Fogerty Arena")){

                                arena.remove("Fogerty Arena");

                            }else {

                            }

                        }

                        if (cbBrooklynPark.isChecked()) {

                            if(arena.contains("Brooklyn Park Activity Center")){

                            }else {

                                arena.add("Brooklyn Park Activity Center");

                            }

                        } else {

                            if(arena.contains("Brooklyn Park Activity Center")){

                                arena.remove("Brooklyn Park Activity Center");

                            }else {

                            }

                        }

                        if (cbVadnais.isChecked()) {

                            if(arena.contains("Vadnais Sports Center")){

                            }else {

                                arena.add("Vadnais Sports Center");

                            }

                        } else {

                            if(arena.contains("Vadnais Sports Center")){

                                arena.remove("Vadnais Sports Center");

                            }else {

                            }

                        }

                        if (cbVeterans.isChecked()) {

                            if(arena.contains("Veterans Memorial Community Center")){

                            }else {

                                arena.add("Veterans Memorial Community Center");

                            }

                        } else {

                            if(arena.contains("Veterans Memorial Community Center")){

                                arena.remove("Veterans Memorial Community Center");

                            }else {

                            }

                        }

                        if (cbVictory.isChecked()) {

                            if(arena.contains("Victory Memorial Ice Arena")){

                            }else {

                                arena.add("Victory Memorial Ice Arena");

                            }

                        } else {

                            if(arena.contains("Victory Memorial Ice Arena")){

                                arena.remove("Victory Memorial Ice Arena");

                            }else {

                            }

                        }

                        if (cbPolar.isChecked()) {

                            if(arena.contains("Polar Arena")){

                            }else {

                                arena.add("Polar Arena");

                            }

                        } else {

                            if(arena.contains("Polar Arena")){

                                arena.remove("Polar Arena");

                            }else {

                            }

                        }

                        if (cbMNMade.isChecked()) {

                            if(arena.contains("Minnesota Made Ice Center")){

                            }else {

                                arena.add("Minnesota Made Ice Center");

                            }

                        } else {

                            if(arena.contains("Minnesota Made Ice Center")){

                                arena.remove("Minnesota Made Ice Center");

                            }else {

                            }

                        }

                        if (cbParadeIceGarden.isChecked()) {

                            if(arena.contains("Parade Ice Garden")){

                            }else {

                                arena.add("Parade Ice Garden");

                            }

                        } else {

                            if(arena.contains("Parade Ice Garden")){

                                arena.remove("Parade Ice Garden");

                            }else {

                            }

                        }

                        if (cbOscarJohnson.isChecked()) {

                            if(arena.contains("Oscar Johnson Arena")){

                            }else {

                                arena.add("Oscar Johnson Arena");

                            }

                        } else {

                            if(arena.contains("Oscar Johnson Arena")){

                                arena.remove("Oscar Johnson Arena");

                            }else {

                            }

                        }

                        if (cbHighland.isChecked()) {

                            if(arena.contains("Highland Arena")){

                            }else {

                                arena.add("Highland Arena");

                            }

                        } else {

                            if(arena.contains("Highland Arena")){

                                arena.remove("Highland Arena");

                            }else {

                            }

                        }

                        if (cbFLAAASC.isChecked()) {

                            if(arena.contains("FLAAA Sports Center")){

                            }else {

                                arena.add("FLAAA Sports Center");

                            }

                        } else {

                            if(arena.contains("FLAAA Sports Center")){

                                arena.remove("FLAAA Sports Center");

                            }else {

                            }

                        }

                        if (cb20.isChecked()) {

                            minutes1 = 1200000;

                        }

                        if (cb40.isChecked()) {

                            minutes1 = 2400000;

                        }

                        if (cb60.isChecked()) {

                            minutes1 = 3600000;

                        }

                        TinyDB tinydb = new TinyDB(getApplicationContext());

                        tinydb.putList("arena", arena);

                        tinydb.putList("notification", notification);

                        tinydb.putInt("minutes", minutes1);

                        cancelAlarm();

                        startAlarm(minutes1);

                        arena_popup.setVisibility(View.INVISIBLE);

                        break;

                    case R.id.clearArena:

                        File file = new File(sdDir.getAbsolutePath() + "/HockeyFinder/dateCheck.txt");

                        //noinspection ResultOfMethodCa
                        file.delete();

                        break;

                    case R.id.cancelNotify:

                        tinydb = new TinyDB(getApplicationContext());

                        final ArrayList<Integer> counter = tinydb.getListInt("counter");

                        Object[] mStringArray = counter.toArray();

                        for (Object aMStringArray : mStringArray) Log.wtf("Count", "NOTIFYCHECK: " + aMStringArray);

                        killNotifications(counter);

                        counter.clear();

                        cancelAlarm();

                        break;

                    case R.id.cancelArena:

                        arena_popup.setVisibility(View.INVISIBLE);

                        break;

                }

            }

        };

        cbLED.setOnClickListener(onClickListener4);

        cbSOUND.setOnClickListener(onClickListener4);

        cbVIBRATE.setOnClickListener(onClickListener4);

        cb20.setOnClickListener(onClickListener4);

        cb40.setOnClickListener(onClickListener4);

        cb60.setOnClickListener(onClickListener4);

        cbAll.setOnClickListener(onClickListener4);

        cbExcel.setOnClickListener(onClickListener4);

        cbSchwans.setOnClickListener(onClickListener4);

        cbFogerty.setOnClickListener(onClickListener4);

        cbBrooklynPark.setOnClickListener(onClickListener4);

        cbVadnais.setOnClickListener(onClickListener4);

        cbVeterans.setOnClickListener(onClickListener4);

        cbVictory.setOnClickListener(onClickListener4);

        cbPolar.setOnClickListener(onClickListener4);

        cbMNMade.setOnClickListener(onClickListener4);

        cbParadeIceGarden.setOnClickListener(onClickListener4);

        cbOscarJohnson.setOnClickListener(onClickListener4);

        cbHighland.setOnClickListener(onClickListener4);

        cancelNotify.setOnClickListener(onClickListener4);

        cbSettings.setOnClickListener(onClickListener4);

        cbFLAAASC.setOnClickListener(onClickListener4);

        startArena.setOnClickListener(onClickListener4);

        clearArena.setOnClickListener(onClickListener4);

        cancelArena.setOnClickListener(onClickListener4);

    }


    public void startAlarm(int minutes) {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int interval = minutes;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        Log.i("ALARM/MANAGER", "set: " + minutes);

    }

    public void cancelAlarm() {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        manager.cancel(pendingIntent);

        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();

    }

    public void startAlarmAt() {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 8);

        calendar.set(Calendar.MINUTE, 00);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),

                1000 * 60 * 20, pendingIntent);

        Log.w("MAINACTIVITY", "SERVICE: started");

    }


    public int[] killNotifications(ArrayList<Integer> integers){

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int[] ret = new int[integers.size()];

        Iterator<Integer> iterator = integers.iterator();

        for (int i = 0; i < ret.length; i++)

        {

            ret[i] = iterator.next();

            mNotificationManager.cancel(notificationID + ret[i]);

            String beanCount = Integer.toString(ret[i]);

            Log.i("NOTIFICATION", "COUNTER: killed " + beanCount);

        }

        Log.i("NOTIFICATION", "COUNTER: cleared");

        return ret;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        WebView.HitTestResult result = ((WebView) v).getHitTestResult();

        if (result != null) {

            int type = result.getType();

            // Confirm type is an image
            if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

                imageUrl = result.getExtra();

                contextpopup(imageUrl);

            }

        }

    }

    public void downloadFile(String uRl2) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/HockeyFinder/Images/");

        if (!direct.exists()) {

            direct.mkdirs();

        }

        String baseName = FilenameUtils.getBaseName(uRl2);

        String extension = FilenameUtils.getExtension(uRl2);

        Log.i("SAVE/IMAGE",  "1 " + uRl2);

        DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl2);

        DownloadManager.Request request = new DownloadManager.Request(

                downloadUri);

        request.setAllowedNetworkTypes(

                DownloadManager.Request.NETWORK_WIFI

                        | DownloadManager.Request.NETWORK_MOBILE)

                .setAllowedOverRoaming(false)

                .setTitle("Demo")

                .setDescription("Something useful. No, really.")

                .setDestinationInExternalPublicDir("/HockeyFinder/Images/", baseName + "." + extension);

        mgr.enqueue(request);

        Log.i("SAVE/IMAGE",  "2 " + baseName + "." + extension);

        Toast.makeText(this, "sdCard/HockeyFinder/Images/" + baseName + "." + extension, Toast.LENGTH_LONG).show();

        imageUrl = "";

    }

    public void contextpopup(String imageUrl) {

        final RelativeLayout context_popup = (RelativeLayout) findViewById(R.id.context_popup);

        Button contextClose = (Button) findViewById(R.id.contextClose);

        Button contextSave = (Button) findViewById(R.id.contextSave);

        context_popup.setVisibility(View.VISIBLE);

        final String uRl2 = imageUrl;

        View.OnClickListener onClickListener5 = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                switch (view.getId()) {

                    case R.id.contextClose:

                        context_popup.setVisibility(View.INVISIBLE);

                        break;

                    case R.id.contextSave:

                        if(uRl2.endsWith("png")||uRl2.endsWith("jpg")||uRl2.endsWith("gif")||uRl2.endsWith("bmp")) {

                            downloadFile(uRl2);

                        } else {

                            Toast.makeText(getApplicationContext(), "Not An Image File", Toast.LENGTH_LONG).show();

                        }

                        context_popup.setVisibility(View.INVISIBLE);

                        break;

                }

            }

        };

        contextClose.setOnClickListener(onClickListener5);

        contextSave.setOnClickListener(onClickListener5);

    }

}
