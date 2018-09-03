package android.anderes.org.cookbook.dagger;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//@Module
public class CookbookTestModule {

    //@Provides
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://www.anderes.org/recipes-api/recipes")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
