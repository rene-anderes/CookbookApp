package android.anderes.org.cookbook.dagger;

import android.anderes.org.cookbook.gui.ItemListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ItemListActivityModule {
    @ContributesAndroidInjector
    abstract ItemListActivity contributeActivityInjector();

}
