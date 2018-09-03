package android.anderes.org.cookbook.dagger;

import android.anderes.org.cookbook.MyApplication;

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
