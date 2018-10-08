package android.anderes.org.cookbook;

public interface AppConfiguration {

    boolean isOnline();

    boolean isFullSync();

    boolean isBackgroundSync();

    boolean loadDefaultValuesForSettings();
}
