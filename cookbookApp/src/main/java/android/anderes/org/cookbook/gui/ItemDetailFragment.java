package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.R;
import android.anderes.org.cookbook.ServiceLocator;
import android.anderes.org.cookbook.repository.Resource;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private ItemDetailViewModel viewModel;

    private View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(null /*mItem.details*/);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            String itemId = getArguments().getString(ARG_ITEM_ID);
            viewModel = ViewModelProviders.of(this).get(ItemDetailViewModel.class);
            viewModel.setRepository(ServiceLocatorForApp.getInstance().getRecipeRepository());

            viewModel.getRecipe(itemId).observe(this, resource -> {
                final Activity activity = this.getActivity();
                final CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                if (resource.status == Resource.Status.SUCCESS) {
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(resource.data.getTitle());
                    }
                    ((TextView) rootView.findViewById(R.id.item_detail)).setText(resource.data.getPreamble());
                    Snackbar.make(rootView, R.string.msg_recipes_updated, Snackbar.LENGTH_SHORT).show();
                } else if (resource.status == Resource.Status.LOADING) {
                    Snackbar.make(rootView, R.string.msg_recipes_loading, Snackbar.LENGTH_SHORT).show();
                } else if (resource.status == Resource.Status.ERROR) {
                    Log.v("GUI", resource.status + " - " + resource.message);
                    Snackbar.make(rootView, resource.status + " - " + resource.message, Snackbar.LENGTH_INDEFINITE).show();
                }

            });

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_detail, container, false);
        return rootView;
    }
}
