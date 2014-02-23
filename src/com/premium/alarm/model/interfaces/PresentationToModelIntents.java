package com.premium.alarm.model.interfaces;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.premium.alarm.model.AlarmsService;

public class PresentationToModelIntents {

    public static final String ACTION_REQUEST_SNOOZE = "com.premium.alarm.model.interfaces.ServiceIntents.ACTION_REQUEST_SNOOZE";
    public static final String ACTION_REQUEST_DISMISS = "com.premium.alarm.model.interfaces.ServiceIntents.ACTION_REQUEST_DISMISS";

    public static PendingIntent createPendingIntent(Context context, String action, int id) {
        Intent intent = new Intent(action);
        intent.putExtra(Intents.EXTRA_ID, id);
        intent.setClass(context, AlarmsService.class);
        return PendingIntent.getService(context, id, intent, 0);
    }
}
