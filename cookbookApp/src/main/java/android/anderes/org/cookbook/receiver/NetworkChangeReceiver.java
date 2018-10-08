package android.anderes.org.cookbook.receiver;

import android.anderes.org.cookbook.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.anderes.org.cookbook.AppConstants.BROADCAST_NETWORK_ACTION;
import static android.anderes.org.cookbook.AppConstants.BROADCAST_NETWORK_STATUS_KEY;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Network", "Network-Status changed");
        sendFinishedMessage(context);
    }

    private void sendFinishedMessage(Context context) {
        final LocalBroadcastManager locBcMan = LocalBroadcastManager.getInstance(context);
        final Bundle messBundle = new Bundle();
        messBundle.putBoolean(BROADCAST_NETWORK_STATUS_KEY, isOnline(context));
        final Intent intent = new Intent(BROADCAST_NETWORK_ACTION);
        intent.putExtras(messBundle);
        locBcMan.sendBroadcast(intent);
    }

    public boolean isOnline(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
