package android.anderes.org.cookbook.infrastructure;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.anderes.org.cookbook.LogConstants.REMOTE_ACCESS;

public class RemoteDataSource {

    private String url = "http://www.anderes.org/resources-api/recipes";
    private MyResources resources;

    private RemoteDataSource() {}

    public RemoteDataSource(final MyResources resources, final String url) {
        this.url = url;
        this.resources = resources;
    }

    public void getRecipeAbstractCollection(final RecipeAbstractResponse remoteData) {

        final ArrayList<RecipeAbstract> list = new ArrayList<>(1);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            final JSONObject object = response.optJSONObject(i);
                            try {
                                final RecipeAbstract recipe = new RecipeAbstract();
                                recipe.setTitle(object.getString("title"))
                                    .setRecipeId(object.getString("id"))
                                    .setLastUpdate(object.getLong("editingDate"));
                                list.add(recipe);
                            } catch (JSONException e) {
                                Log.e(REMOTE_ACCESS, e.getMessage());
                            }
                        }
                        remoteData.setStatus(RecipeAbstractResponse.Status.OK);
                        remoteData.setData(list);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(REMOTE_ACCESS, "Status-Code: " + error.networkResponse.statusCode);
                        remoteData.setStatus(RecipeAbstractResponse.Status.ERROR);
                        remoteData.setData(list);
                    }
                });
        resources.addToRequestQueue(jsonObjectRequest);
    }
}
