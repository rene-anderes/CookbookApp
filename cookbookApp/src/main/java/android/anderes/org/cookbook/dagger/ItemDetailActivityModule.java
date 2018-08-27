package android.anderes.org.cookbook.dagger;

import android.anderes.org.cookbook.gui.ItemDetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ItemDetailActivityModule {
    @ContributesAndroidInjector
    abstract ItemDetailActivity contributeActivityInjector();
}
