package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.R;
import android.anderes.org.cookbook.ServiceLocatorForApp;
import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.anderes.org.cookbook.receiver.NetworkStatusReceiver;
import android.anderes.org.cookbook.receiver.SyncReceiver;
import android.anderes.org.cookbook.repository.Resource;
import android.anderes.org.cookbook.service.CookbookSyncService;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import dagger.android.AndroidInjection;

import static android.anderes.org.cookbook.AppConstants.BROADCAST_NETWORK_ACTION;
import static android.anderes.org.cookbook.AppConstants.BROADCAST_SYNC_ACTION;
import static android.anderes.org.cookbook.repository.Resource.*;

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
    private NetworkStatusReceiver networkStatusReceiver;
    private final View.OnClickListener syncOnClick =
            view -> {
                final Intent i = new Intent(this, CookbookSyncService.class);
                startService(i);
                final SyncReceiver receiver = new SyncReceiver(view);
                final IntentFilter intentFilter = new IntentFilter(BROADCAST_SYNC_ACTION);
                LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_list);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        onCreateSyncButton();


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        viewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
        viewModel.setRepository(ServiceLocatorForApp.getInstance().getRecipeAbstractRepository());
    }

    private void onCreateSyncButton() {
        final FloatingActionButton syncButton = findViewById(R.id.fab);
        syncButton.setOnClickListener(syncOnClick);
        final boolean isOnline = ServiceLocatorForApp.getInstance().getAppConfiguration().isOnline();
        syncButton.setAlpha(isOnline ? 1 : 0.5f);
        syncButton.setEnabled(isOnline);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        final ItemListAdapter adapter = setupRecyclerView((RecyclerView) recyclerView);

        final Observer<Resource<List<RecipeAbstractEntity>>> observer = resource -> {
            if (resource == null) {
                return;
            }
            if (resource.status == Status.SUCCESS) {
                // Update the cached copy of the recipes in the adapter.
                adapter.setRecipes(resource.data);
            } else if (resource.status == Status.LOADING) {
                Snackbar.make(recyclerView, R.string.msg_recipes_loading, Snackbar.LENGTH_SHORT).show();
            } else if (resource.status == Status.ERROR) {
                Log.v("GUI", resource.status + " - " + resource.message);
                Snackbar.make(recyclerView, resource.status + " - " + resource.message, Snackbar.LENGTH_INDEFINITE).show();
            }
        };
        viewModel.getRecipes().observe(this, observer);
        registerReceiver();
    }

    private void registerReceiver() {
        networkStatusReceiver = new NetworkStatusReceiver(findViewById(R.id.fab));
        final IntentFilter intentFilter = new IntentFilter(BROADCAST_NETWORK_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(networkStatusReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.getRecipes().removeObservers(this);
        unregisterReceiver();
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkStatusReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            final Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ItemListAdapter setupRecyclerView(@NonNull RecyclerView recyclerView) {
        final ItemListAdapter adapter = new ItemListAdapter(this, mTwoPane);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        return adapter;
    }

}
