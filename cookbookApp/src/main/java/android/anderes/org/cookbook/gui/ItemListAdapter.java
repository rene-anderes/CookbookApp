package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.R;
import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.anderes.org.cookbook.R.layout.item_list_content;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>  {

    private List<RecipeAbstractEntity> recipes;
    private final ItemListActivity parent;
    private final boolean twoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final RecipeAbstractEntity item = (RecipeAbstractEntity) view.getTag();
            if (twoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.getRecipeId());
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                parent.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getRecipeId());

                context.startActivity(intent);
            }
        }
    };

    ItemListAdapter(@NonNull final ItemListActivity parent, boolean twoPane) {
         this.parent = parent;
         this.twoPane = twoPane;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        final View itemView = LayoutInflater.from(parent.getContext())
                                .inflate(item_list_content, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {
        if (recipes != null) {
            RecipeAbstractEntity current = recipes.get(position);
            itemViewHolder.recipeItemView.setText(current.getTitle());
            itemViewHolder.itemView.setTag(recipes.get(position));
            itemViewHolder.itemView.setOnClickListener(mOnClickListener);
        } else {
            // Covers the case of data not being ready yet.
            itemViewHolder.recipeItemView.setText("No Recipes");
        }
    }

    @Override
    public int getItemCount() {
        if (recipes != null) {
            return recipes.size();
        }
        return 0;
    }

    void setRecipes(@NonNull final List<RecipeAbstractEntity> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView recipeItemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeItemView = itemView.findViewById(R.id.item_title);
        }
    }
}
