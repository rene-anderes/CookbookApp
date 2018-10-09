package org.anderes.app.cookbook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import org.anderes.app.cookbook.AppConstants;

public class NetworkStatusReceiver extends BroadcastReceiver {

    private final View view;

    public NetworkStatusReceiver(@NonNull final View view) {
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Network", "new network state: " + intent.getBooleanExtra(AppConstants.BROADCAST_NETWORK_STATUS_KEY, false));
        view.setAlpha(intent.getBooleanExtra(AppConstants.BROADCAST_NETWORK_STATUS_KEY, false) ? 1 : 0.5f);
        view.setEnabled(intent.getBooleanExtra(AppConstants.BROADCAST_NETWORK_STATUS_KEY, false));
    }
}
