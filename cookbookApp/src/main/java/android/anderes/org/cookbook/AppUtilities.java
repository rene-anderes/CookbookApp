package android.anderes.org.cookbook;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppUtilities implements AppConfiguration {

    private final Context context;

    public AppUtilities(Context context) {
        this.context = context;
    }

    @Override
    public boolean isOnline() {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean isFullSync() {
        return true;
    }

    @Override
    public boolean isBackgroundSync() {
        return false;
    }
}
