package org.anderes.app.cookbook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.anderes.app.cookbook.AppConstants;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Network", "Network-Status changed");
        sendFinishedMessage(context);
    }

    private void sendFinishedMessage(Context context) {
        final LocalBroadcastManager locBcMan = LocalBroadcastManager.getInstance(context);
        final Bundle messBundle = new Bundle();
        messBundle.putBoolean(AppConstants.BROADCAST_NETWORK_STATUS_KEY, isOnline(context));
        final Intent intent = new Intent(AppConstants.BROADCAST_NETWORK_ACTION);
        intent.putExtras(messBundle);
        locBcMan.sendBroadcast(intent);
    }

    public boolean isOnline(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
