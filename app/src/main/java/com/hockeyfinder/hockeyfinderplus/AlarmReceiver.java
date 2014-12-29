package com.hockeyfinder.hockeyfinderplus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myService = new Intent(context, MyService.class);
        context.startService(myService);
        Log.wtf("ALARMRECEIVER", "ALARMRECEIVER: RUNNING");

    }

}
