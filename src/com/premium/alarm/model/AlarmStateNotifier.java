package com.premium.alarm.model;

import android.content.Context;
import android.content.Intent;

import com.premium.alarm.model.AlarmCore.IStateNotifier;
import com.premium.alarm.model.interfaces.Intents;

/**
 * Broadcasts alarm state with an intent
 * 
 * @author Yuriy
 * 
 */
public class AlarmStateNotifier implements IStateNotifier {

    private final Context mContext;

    public AlarmStateNotifier(Context context) {
        mContext = context;
    }

    @Override
    public void broadcastAlarmState(int id, String action) {
        Intent intent = new Intent(action);
        intent.putExtra(Intents.EXTRA_ID, id);
        mContext.sendBroadcast(intent);
    }
}
