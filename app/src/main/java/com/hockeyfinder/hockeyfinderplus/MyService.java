package com.hockeyfinder.hockeyfinderplus;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Objects;

public class MyService extends IntentService {

    private NotificationManager mNotificationManager;
    private int notificationID = 10;
    private static final String TAG = "MyService";

    ArrayList<Integer> counter = new ArrayList<>();
    ArrayList<String> notify = new ArrayList<>();

    String dateTime1 = "";

    long timeMillis1;

    File sdDir = new File(Environment.getExternalStorageDirectory().getPath());


    public MyService() {

        super("MyServiceName");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        new backgroundThread().execute();

        Log.w(TAG, "SERVICE: started");

    }

    protected class backgroundThread extends AsyncTask<Void, Void, Void> {

        TinyDB tinydb = new TinyDB(getApplicationContext());

        final ArrayList<String> value = tinydb.getList("arena");
        final ArrayList<String> notification = tinydb.getList("notification");
        final Object[] mStringArray2 = value.toArray();

        @Override
        protected Void doInBackground(Void... params) {

            CheckConnection ch = new CheckConnection();

            boolean status = ch.isNetworkAvailable(getApplicationContext());

            Calendar c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DATE);
            int month2 = c.get(Calendar.MONTH) + 1;
            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
            String month = month_date.format(c.getTime());


            String mydate2 = month2 + "/" + dayOfWeek;
            String mydate = month + " " + dayOfWeek;


            Log.w(TAG, "DATE " + mydate);
            Log.w(TAG, "DATE " + mydate2);

            todayGameCheck(notification, mydate, mydate2);


            if (status) {
                notify.clear();
                writeFile1();
                    Log.wtf(TAG, "TIMER: set/10 min");

                for (Object aMStringArray2 : mStringArray2)
                    Log.wtf(TAG, "SEARCH: arena " + aMStringArray2);

                displayNotification(value, notification);
                    Log.wtf(TAG, "TIMER: set/10 min");

            } else {
                Log.wtf(TAG, "NO NETWORK CONNECTION");
            }

            tinydb.putListInt("counter", counter);
                return null;
        }
    }

    public void onDestroy() {

        Log.wtf(TAG, "SERVICE: Service Destroyed");

        killNotifications(counter);

        Log.w(TAG, "NOTIFICATIONS: canceled");

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void displayNotification(ArrayList<String> value, ArrayList<String> notification) {

        File file = new File(sdDir.getAbsolutePath() + "/HockeyFinder/Data/hfhtml1.txt");

        String testHtml = null; // from commons io

        try {

            testHtml = FileUtils.readFileToString(file);

        } catch (IOException e) {

            e.printStackTrace();

            Log.i(TAG, e.getMessage());

        }

        String[] date = StringUtils.substringsBetween(testHtml, "<span class=\"date\">", "</span>");
        String[] title = StringUtils.substringsBetween(testHtml, "<h4 style=\"text-align: center;font-weight: bold\">", "&nbsp;");
        String[] time = StringUtils.substringsBetween(testHtml, "<span class=\"time\">", "</span>");
        String[] players = StringUtils.substringsBetween(testHtml, "<p style=\"\"><span><span class=\"badge badge-info\">", "</span>");
        String[] goalies = StringUtils.substringsBetween(testHtml, "player spot(s) and <span class=\"badge badge-info\">", "</span>");
        String[] address = StringUtils.substringsBetween(testHtml, "<address>","</address>");
        String[] link = StringUtils.substringsBetween(testHtml, "<i class=\"icon-zoom-in m-icon-white\"></i></a>  \n" +
                "\t\t\t\t\t\t\t\t<a href=\"","\" class=\"btn black pull-right\">");

        for (int i = 0; i < title.length; i++) {

            //noinspection StatementWithEmptyBody
            if (value.contains(title[i])) {
                String  dCheck= (title[i] + " " + date[i] + " " + time[i]);
                File myFile = new File(sdDir.getAbsolutePath() + "/HockeyFinder/Data/dateCheck.txt");
                if (myFile.exists()) {
                    try {
                        FileInputStream in = new FileInputStream(myFile);
                        int len;
                        byte[] data1 = new byte[16384];
                            while (-1 != (len = in.read(data1))) {
                                if (new String(data1, 0, len).contains(dCheck)) {

                                    TinyDB tinydb = new TinyDB(getApplicationContext());
                                    final ArrayList<Integer> counter2 = tinydb.getListInt("counter2");
                                    if (counter2.contains(notificationID +i)){
                                        notification(notification, players[i], goalies[i], address[i], link[i], title [i], title[i], date[i], time[i], i, dCheck );
                                        Log.i(TAG, "NOTIFICATIONS " + counter2 + " " + (notificationID +i) + " UPDATED" );
                                    }else {

                                        timeMillis1 = System.currentTimeMillis();
                                        String dateInMilliseconds = Objects.toString(timeMillis1, null);
                                        String dateFormat = "hh:mm:ss dd/MM/yyyy";
                                        dateTime1 = DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
                                        notify.add(len + " contains: " + dCheck);
                                    }
                                } else {
                                        writeToFile(dCheck);
                                        counter.add(i);
                                        notification(notification, players[i], goalies[i], address[i], link[i], title [i], title[i], date[i], time[i], i, dCheck );
                                }
                            }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        //noinspection ResultOfMethodCallIgnored
                        myFile.createNewFile();
                        writeToFile(dCheck);
                        timeMillis1 = System.currentTimeMillis();
                        String dateInMilliseconds = Objects.toString(timeMillis1, null);
                        String dateFormat = "hh:mm:ss dd/MM/yyyy";
                        dateTime1 = DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
                        counter.add(i);
                        notification(notification, players[i], goalies[i], address[i], link[i], title [i], title[i], date[i], time[i], i, dCheck );
                    } catch (Exception e) {

                        e.printStackTrace();

                        Log.i(TAG, e.getMessage());

                    }

                }

            } else {

            }

        }

        Object[] mStringArray = notify.toArray();
        for (Object aMStringArray : mStringArray) Log.wtf(TAG, "NOTIFYCHECK: " + aMStringArray);
        notification(notification," "," ", " ", " ", "Service Run", "GAMECHECK:", " ", dateTime1, 1002, "Game Check Run");

    }

    protected void writeFile1() {
            HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
            HttpGet httpget = new HttpGet("http://www.hockeyfinder.com/skate-times/type/MN/1/"); // Set the action you want to do
            HttpResponse response; // Executeit

            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                InputStream is; // Create an InputStream with the response
                is = entity.getContent();
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) // Read line by line
                    sb.append(line).append("\n");
                String resString = sb.toString(); // Result is here
                is.close(); // Close the stream

                File myFile = new File(sdDir.getAbsolutePath() + "/HockeyFinder/Data/hfhtml1.txt");

                //noinspection ResultOfMethodCallIgnored
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(resString);

                Log.w(TAG, "FILEWRITTEN: " + "hfhtml1.txt");

                myOutWriter.close();
                fOut.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, e.getMessage());
            }

    }

    public int[] killNotifications(ArrayList<Integer> integers){

        int[] ret = new int[integers.size()];

        Iterator<Integer> iterator = integers.iterator();

        for (int i = 0; i < ret.length; i++){

            ret[i] = iterator.next();
            mNotificationManager.cancel(notificationID + ret[i]);
            String beanCount = Integer.toString(ret[i]);
            Log.i(TAG, "COUNTER: killed " + beanCount);

        }

        counter.clear();
        Log.i(TAG, "COUNTER: cleared");
        return ret;

    }

    public class CheckConnection {

        public boolean isNetworkAvailable(Context context) {

            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public void notification(ArrayList<String> notification, String players, String goalies, String address, String link, String ticker, String title, String date, String time, int i, String dCheck ){

        String tx1 = "";
        String tx2 = "";
        String tx3 = "";

        String[] textStr = address.split("\n");
        for (int n = 0; n < textStr.length; n++) {
            textStr[n] = textStr[n].trim();
            textStr[n] = textStr[n].replace("<br>", "");
            textStr[n] = textStr[n].replace("\t\t", "");
        }

        if (textStr.length > 2) {
            tx1 = textStr[1];
            tx2 = textStr[2];
            tx3 = textStr[3];
        }
        int requestID = (int) System.currentTimeMillis();

        String link2 = "http://hockeyfinder.com" + link;

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String uri2 = "tel:" + tx3.trim() ;
        Intent intent1 = new Intent(Intent.ACTION_DIAL);
        intent1.setData(Uri.parse(uri2));
        PendingIntent pIntent1 = PendingIntent.getActivity(this, 0, intent1, 0);

        String uri3 = "google.navigation:q=" + tx1 + " " + tx2 ;
        Intent intent2 = new Intent(Intent.ACTION_VIEW);
        intent2.setData(Uri.parse(uri3));
        PendingIntent pIntent2 = PendingIntent.getActivity(this, 0, intent2, 0);

        Log.i(TAG, "LINK " + link);

        Intent intent3 = new Intent(this, NotificationReceiver.class);
        intent3.putExtra("akey", link2);
        PendingIntent pIntent3 = PendingIntent.getActivity(this, requestID, intent3, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)

        .setTicker(ticker)
        .setContentTitle(title)
        .setContentText(date + " " + time)
        .setSmallIcon(R.drawable.ic_launcher)

        .addAction(R.drawable.ic_action_call, "", pIntent1)

        .addAction(R.drawable.ic_action_locate, "", pIntent2)

        .addAction(R.drawable.ic_action_paste, "", pIntent3);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine("Player Spots: " + players);
        inboxStyle.addLine("Goalie Spots: " + goalies);
        inboxStyle.addLine("Address: " + tx1);
        inboxStyle.addLine("Address: " + tx2);
        inboxStyle.addLine("Phone: " + tx3);
        inboxStyle.addLine("Web: www.hockeyfinder.com" + link);

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Game Details:");

        mBuilder.setStyle(inboxStyle);

        if(notification.contains("LED")){
            mBuilder.setLights(Color.GREEN, 1000, 10000);
        }else {
        }

        if(notification.contains("VIBRATE")){
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        }else {
        }

        if(notification.contains("SOUND")){
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            mBuilder.setSound(uri);
        }else {
        }

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID + i, mBuilder.build());

        Log.w(TAG, "CREATED: " + i + " " + dCheck);

    }

    public void writeToFile(String dCheck){

        File myFile = new File(sdDir.getAbsolutePath() + "/HockeyFinder/Data/dateCheck.txt");

        try {
            String separator = System.getProperty("line.separator");
            FileOutputStream fOut;
            fOut = new FileOutputStream(myFile, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(separator);
            myOutWriter.append(dCheck);
            myOutWriter.close();
            fOut.close();
            Log.w(TAG, "FILE WRITTEN: dateCheck.txt"  + dCheck);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void todayGameCheck(ArrayList<String> notification, String mydate, String mydate2){

            File myFile = new File(sdDir.getAbsolutePath() + "/HockeyFinder/Data/htmlleague.txt");

            if (myFile.exists()) {
                try {
                    FileInputStream in = new FileInputStream(myFile);
                    int len;
                    byte[] data1 = new byte[16384];
                    while (-1 != (len = in.read(data1))) {
                        if (new String(data1, 0, len).contains(mydate)) {

                            int of = 123456;
                            leagueNotification(notification, "PickUp Game", of);

                        }
                        if (new String(data1, 0, len).contains(mydate2)) {

                            int of = 234567;
                            leagueNotification(notification, "League Game", of);

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }

            } else {

            }

    }

    public void leagueNotification(ArrayList<String> notification, String type, int of){

        Intent resultIntent = new Intent(this, LeagueGames.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)

                .setTicker("GAME TODAY")
                .setContentTitle(type)
                .setContentText("Click Here To View Games")
                .setSmallIcon(R.drawable.ic_launcher);

        if(notification.contains("LED")){
            mBuilder.setLights(Color.GREEN, 1000, 10000);
        }else {
        }

        if(notification.contains("VIBRATE")){
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        }else {
        }

        if(notification.contains("SOUND")){
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            mBuilder.setSound(uri);
        }else {
        }

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID + of, mBuilder.build());

    }

}