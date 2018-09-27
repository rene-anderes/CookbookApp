package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.R;
import android.anderes.org.cookbook.repository.Resource;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class IngredientListFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private IngredientListViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = this.getActivity();
        final View recyclerView = activity.findViewById(R.id.item_detail_ingredient_list);
        assert recyclerView != null;
        final IngredientListAdapter adapter = setupRecyclerView((RecyclerView) recyclerView);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            final String itemId = getArguments().getString(ARG_ITEM_ID);
            viewModel = ViewModelProviders.of(this).get(IngredientListViewModel.class);
            viewModel.setRepository(ServiceLocatorForApp.getInstance().getIngredientRepository());

            viewModel.getIngredients(itemId).observe(this, resource -> {
                if (resource.status == Resource.Status.SUCCESS) {
                    adapter.setIngredients(resource.data);
                } else if (resource.status == Resource.Status.ERROR) {
                    Log.v("GUI", resource.status + " - " + resource.message);
                }
            });
        }
    }

    private IngredientListAdapter setupRecyclerView(@NonNull RecyclerView recyclerView) {
        final IngredientListAdapter adapter = new IngredientListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        return adapter;
    }
}
