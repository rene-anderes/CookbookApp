package org.anderes.app.cookbook.dagger;

import org.anderes.app.cookbook.MyApplication;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Component(modules = { AndroidInjectionModule.class, ItemListActivityModule.class,
        ItemDetailActivityModule.class, CookbookModule.class })
public interface CookbookApplicationComponent extends AndroidInjector<MyApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MyApplication> {
    }
}
