package org.anderes.app.cookbook.gui;

import org.anderes.app.cookbook.R;
import org.anderes.app.cookbook.ServiceLocatorForApp;
import org.anderes.app.cookbook.database.RecipeEntity;
import org.anderes.app.cookbook.repository.Resource;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Date;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

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

        final Activity activity = this.getActivity();

        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID) && activity != null) {
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                final Bundle arguments = new Bundle();
                arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                        activity.getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
                final IngredientListFragment fragment = new IngredientListFragment();
                fragment.setArguments(arguments);
                ((FragmentActivity) activity).getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_ingredients, fragment)
                        .commit();
            }
        } else {
            Log.w("onCreate", "ItemDetailFragment: check the conditions!");
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = this.getActivity();
        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID) && activity != null) {
            final String itemId = getArguments().getString(ARG_ITEM_ID);
            ItemDetailViewModel viewModel = ViewModelProviders.of(this).get(ItemDetailViewModel.class);
            viewModel.setRepository(ServiceLocatorForApp.getInstance().getRecipeRepository());


            final CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);

            rootView.findViewById(R.id.preparation_title).setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.rating).setVisibility(View.INVISIBLE);

            viewModel.getRecipe(itemId).observe(this, resource -> {
                if (appBarLayout != null) {
                    appBarLayout.setTitle("...");
                }
                if (resource.status == Resource.Status.SUCCESS) {
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(resource.data.getTitle());
                    }
                    handleItemDetail(resource.data);
                } else if (resource.status == Resource.Status.LOADING) {
                    Snackbar.make(rootView, R.string.msg_recipes_loading, Snackbar.LENGTH_SHORT).show();
                } else if (resource.status == Resource.Status.ERROR) {
                    Log.v("GUI", resource.status + " - " + resource.message);
                    Snackbar.make(rootView, R.string.msg_recipes_error, Snackbar.LENGTH_INDEFINITE).show();
                }

            });

        } else {
            Log.w("onCreate", "ItemDetailFragment: check the conditions!");
        }
    }

    private void handleItemDetail(@NonNull final RecipeEntity recipe) {
        rootView.findViewById(R.id.preparation_title).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.rating).setVisibility(View.VISIBLE);
        if (recipe.getPreamble() != null && !recipe.getPreamble().isEmpty()) {
            final Spanned preamble = Html.fromHtml(recipe.getPreamble());
            ((TextView) rootView.findViewById(R.id.preamble)).setText(preamble);
        }
        final String noOfPerson = getResources().getString(R.string.item_detail_noOfPerson, recipe.getNoOfPeople());
        ((TextView) rootView.findViewById(R.id.noOfPerson)).setText(noOfPerson);
        final Spanned preparation = Html.fromHtml(recipe.getPreparation());
        ((TextView) rootView.findViewById(R.id.preparation)).setText(preparation);
        final String updateDate = String.format(" %1$tF %1$tT", new Date(recipe.getEditingDate()));
        final String updateText = getResources().getString(R.string.item_detail_lastUpdate, updateDate);
        ((TextView) rootView.findViewById(R.id.update)).setText(updateText);
        final String addingDate = String.format(" %1$tF %1$tT", new Date(recipe.getAddingDate()));
        final String addingText = getResources().getString(R.string.item_detail_adding, addingDate);
        ((TextView) rootView.findViewById(R.id.adding)).setText(addingText);
        final RatingBar ratingBar = rootView.findViewById(R.id.rating);
        ratingBar.setRating(recipe.getRating());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_detail, container, false);
        return rootView;
    }
}
