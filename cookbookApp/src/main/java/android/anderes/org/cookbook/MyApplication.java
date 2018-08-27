package android.anderes.org.cookbook;

import android.anderes.org.cookbook.dagger.DaggerMyApplicationComponent;
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

        DaggerMyApplicationComponent.builder().create(this).inject(this);

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

}