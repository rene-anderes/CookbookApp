package android.anderes.org.cookbook;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Inject;
import javax.inject.Singleton;

public class MyResources {

    private RequestQueue mRequestQueue;

    @Inject
    @Singleton
    public MyResources(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
}
