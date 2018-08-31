package android.anderes.org.cookbook.infrastructure;

import android.anderes.org.cookbook.model.RecipeAbstract;
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

    public void getRecipeAbstractCollection(final RemoteDataRecipeAbstract remoteDataRecipeAbstract) {

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
                        remoteDataRecipeAbstract.setStatus(RemoteDataRecipeAbstract.Status.OK);
                        remoteDataRecipeAbstract.setData(list);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(REMOTE_ACCESS, "Status-Code: " + error.networkResponse.statusCode);
                        remoteDataRecipeAbstract.setStatus(RemoteDataRecipeAbstract.Status.ERROR);
                        remoteDataRecipeAbstract.setData(list);
                    }
                });
        resources.addToRequestQueue(jsonObjectRequest);
    }
}
