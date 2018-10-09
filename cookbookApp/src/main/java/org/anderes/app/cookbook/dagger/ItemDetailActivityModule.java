package org.anderes.app.cookbook.dagger;

import org.anderes.app.cookbook.gui.ItemDetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ItemDetailActivityModule {
    @ContributesAndroidInjector
    abstract ItemDetailActivity contributeActivityInjector();
}
