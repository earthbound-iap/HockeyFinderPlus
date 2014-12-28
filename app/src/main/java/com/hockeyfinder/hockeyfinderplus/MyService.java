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
import java.util.ArrayList;
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

        Log.w("MYSERVICE", "MYSERVICE: running");

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

        cancelNotification();

    }

    protected void cancelNotification() {

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

                                    timeMillis1 = System.currentTimeMillis();

                                    String dateInMilliseconds = Objects.toString(timeMillis1, null);

                                    String dateFormat = "hh:mm:ss dd/MM/yyyy";

                                    dateTime1 = DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();

                                    notify.add(len + " contains: " + dCheck);

                                } else {

                                        writeToFile(dCheck);

                                        counter.add(i);


                                        notification(notification, title [i], title[i], date[i], time[i], i, dCheck );

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

                        notification(notification, title [i], title[i], date[i], time[i], i, dCheck );

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

        notification(notification, "Service Run", "GAMECHECK:", " ", dateTime1, 1002, "Game Check Run");

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

        for (int i = 0; i < ret.length; i++)

        {

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

    public void notification(ArrayList<String> notification, String ticker, String title, String date, String time, int i, String dCheck ){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setTicker(ticker);

        mBuilder.setContentTitle(title);

        mBuilder.setContentText(date + " " + time);

        mBuilder.setSmallIcon(R.drawable.ic_launcher);

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

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

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

}