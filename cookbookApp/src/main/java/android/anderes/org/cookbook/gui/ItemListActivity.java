package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.NetworkApi;
import android.anderes.org.cookbook.R;
import android.anderes.org.cookbook.ServiceLocator;
import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.repository.RecipeAbstractRepository;
import android.anderes.org.cookbook.repository.Resource;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.anderes.org.cookbook.dummy.DummyContent;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;
    private ItemListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        final View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        final ItemListAdapter adapter = setupRecyclerView((RecyclerView) recyclerView);

        viewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
        final ServiceLocator serviceLocator = new ServiceLocatorForApp(this, "http://www.anderes.org/");
        viewModel.setRepository(serviceLocator.getRecipeAbstractRepository());
        viewModel.getRecipes().observe(this, new Observer<Resource<List<RecipeAbstractEntity>>>() {
            @Override
            public void onChanged(@Nullable final Resource<List<RecipeAbstractEntity>> resource) {
                if (resource.status == Resource.Status.SUCCESS) {
                    // Update the cached copy of the words in the adapter.
                    adapter.setRecipes(resource.data);
                } else {
                    Log.v("GUI", resource.status + " - " + resource.message);
                    Snackbar.make(recyclerView, resource.status + " - " + resource.message, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ItemListAdapter setupRecyclerView(@NonNull RecyclerView recyclerView) {
        final ItemListAdapter adapter = new ItemListAdapter(this, mTwoPane);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        return adapter;
    }

}
