package org.anderes.app.cookbook.dagger;

import org.anderes.app.cookbook.gui.ItemListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ItemListActivityModule {
    @ContributesAndroidInjector
    abstract ItemListActivity contributeActivityInjector();

}
