package com.project.dailyselfie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Intent myIntent = new Intent(arg0, NotificationServiceAdapter.class);
		arg0.startService(myIntent);
	}
}
