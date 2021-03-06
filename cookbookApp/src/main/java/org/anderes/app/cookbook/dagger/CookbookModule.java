package org.anderes.app.cookbook.dagger;

import org.anderes.app.cookbook.MyApplication;
import android.app.Application;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjectionModule;

@Module(includes = AndroidInjectionModule.class)
public abstract class CookbookModule {

    /* Singleton annotation isn't necessary
        (in this case since Application instance is unique)
        but is here for convention. */
    @Binds
    @Singleton
    abstract Application application(MyApplication app);
}
