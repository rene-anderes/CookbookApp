package android.anderes.org.cookbook;

import android.anderes.org.cookbook.dagger.DaggerCookbookApplicationComponent;
import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class MyApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        // sicher stellen, dass es nur eine (singleton) Instanz gibt
        // und zugleich die Default-Werte der Settings setzen
        // diese werden nur gesetzt, wenn sie noch nic gesetzt wurden
        ServiceLocatorForApp.getNewInstance(this)
                .getAppConfiguration().loadDefaultValuesForSettings();
        DaggerCookbookApplicationComponent.builder().create(this).inject(this);

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

}