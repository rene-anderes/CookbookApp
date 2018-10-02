package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.R;
import android.anderes.org.cookbook.ServiceLocator;
import android.anderes.org.cookbook.database.RecipeEntity;
import android.anderes.org.cookbook.repository.Resource;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

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
            viewModel = ViewModelProviders.of(this).get(ItemDetailViewModel.class);
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
                    Snackbar.make(rootView, R.string.msg_recipes_updated, Snackbar.LENGTH_SHORT).show();
                } else if (resource.status == Resource.Status.LOADING) {
                    Snackbar.make(rootView, R.string.msg_recipes_loading, Snackbar.LENGTH_SHORT).show();
                } else if (resource.status == Resource.Status.ERROR) {
                    Log.v("GUI", resource.status + " - " + resource.message);
                    Snackbar.make(rootView, resource.status + " - " + resource.message, Snackbar.LENGTH_INDEFINITE).show();
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
        final String noOfPerson = getResources().getString(R.string.noOfPerson_item_detail, recipe.getNoOfPeople());
        ((TextView) rootView.findViewById(R.id.noOfPerson)).setText(noOfPerson);
        final Spanned preparation = Html.fromHtml(recipe.getPreparation());
        ((TextView) rootView.findViewById(R.id.preparation)).setText(preparation);
        final RatingBar ratingBar = rootView.findViewById(R.id.rating);
        ratingBar.setRating(recipe.getRating());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_detail, container, false);
        return rootView;
    }
}
