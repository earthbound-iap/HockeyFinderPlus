package com.hockeyfinder.hockeyfinderplus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;

public class NLService extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;

    ArrayList<Integer> counter2 = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hockeyfinder.hockeyfinderplu.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);


    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        TinyDB tinydb = new TinyDB(this);

        int new1 = sbn.getId();

        if (counter2.contains(Integer.valueOf(new1))){

        }else {

            counter2.add(new1);
        }

        tinydb.putListInt("counter2", counter2);

        Log.i(TAG,"********** onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new Intent("com.hockeyfinder.hockeyfinderplu.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

        TinyDB tinydb = new TinyDB(this);

        int new1 = sbn.getId();

            counter2.remove(Integer.valueOf(new1));

        tinydb.putListInt("counter2", counter2);

        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        Intent i = new Intent("com.hockeyfinder.hockeyfinderplu.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);
    }
    class NLServiceReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new Intent("com.hockeyfinder.hockeyfinderplu.NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i2 = new Intent("com.hockeyfinder.hockeyfinderplu.NOTIFICATION_LISTENER_EXAMPLE");
                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new Intent("com.hockeyfinder.hockeyfinderplu.NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);
            }
        }
    }
}