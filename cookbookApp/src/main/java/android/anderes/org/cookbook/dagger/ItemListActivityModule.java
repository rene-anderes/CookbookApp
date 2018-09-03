package android.anderes.org.cookbook.dagger;

import android.anderes.org.cookbook.gui.ItemListActivity;
import android.anderes.org.cookbook.NetworkApi;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ItemListActivityModule {
    @ContributesAndroidInjector
    abstract ItemListActivity contributeActivityInjector();

    @Provides
    static NetworkApi provideNetworkApi(){
        return new NetworkApi();
    }
}
