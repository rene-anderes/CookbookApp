package android.anderes.org.cookbook.infrastructure;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import javax.inject.Inject;
import javax.inject.Singleton;

public class MyResources {

    private RequestQueue mRequestQueue;

    @Inject
    public MyResources(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public void addToRequestQueue(final Request request) {
        mRequestQueue.add(request);
    }
}
