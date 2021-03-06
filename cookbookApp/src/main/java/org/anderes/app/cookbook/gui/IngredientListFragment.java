package org.anderes.app.cookbook.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.anderes.app.cookbook.R;
import org.anderes.app.cookbook.ServiceLocatorProvider;
import org.anderes.app.cookbook.repository.Resource;

public class IngredientListFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert this.getActivity() != null;
        final View recyclerView = this.getActivity().findViewById(R.id.item_detail_ingredient_list);
        assert recyclerView != null;
        final IngredientListAdapter adapter = setupRecyclerView((RecyclerView) recyclerView);

        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            final String itemId = getArguments().getString(ARG_ITEM_ID);
            final IngredientListViewModel viewModel =
                    ViewModelProviders.of(this).get(IngredientListViewModel.class);
            viewModel.setRepository(ServiceLocatorProvider.getInstance().getIngredientRepository());

            viewModel.getIngredients(itemId).observe(this, resource -> {
                if (resource != null && resource.status == Resource.Status.SUCCESS) {
                    adapter.setIngredients(resource.data);
                } else {
                    Log.w("GUI", "ingredients can not be loaded");
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
