package org.anderes.app.cookbook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import org.anderes.app.cookbook.AppConstants;

/**
 * Zeigt Meldungen des Synchronisation an
 */
public class SyncReceiver extends BroadcastReceiver {

    private final View view;

    /**
     * Zeigt Meldungen des Synchronisation an, dazu wird die Snckbar verwendet.
     * Von der übergebenen View wird der Parent genommen um die Meldung anzuzeigen.
     */
    public SyncReceiver(@NonNull final View view) {
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        final String message = bundle.getString(AppConstants.BROADCAST_SYNC_MESSAGE_KEY);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }
}
