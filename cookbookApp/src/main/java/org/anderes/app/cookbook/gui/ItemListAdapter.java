package org.anderes.app.cookbook.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.anderes.app.cookbook.R;
import org.anderes.app.cookbook.database.RecipeAbstractEntity;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.GERMAN;
import static org.anderes.app.cookbook.R.layout.item_list_content;

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
        if (recipes != null && !recipes.isEmpty()) {
            RecipeAbstractEntity current = recipes.get(position);
            itemViewHolder.recipeTitle.setText(current.getTitle());
            final String updateDate = String.format(GERMAN," %1$td.%1$tm.%1$ty %1$tT", new Date(current.getLastUpdate()));
            itemViewHolder.recipeUpdate.setText(updateDate);
            itemViewHolder.itemView.setTag(recipes.get(position));
            itemViewHolder.itemView.setOnClickListener(mOnClickListener);
        } else {
            // Covers the case of data not being ready yet.
            itemViewHolder.recipeTitle.setText("No Recipes");
        }
    }

    @Override
    public int getItemCount() {
        if (recipes != null) {
            return recipes.size();
        }
        return 0;
    }

    void setRecipes(final List<RecipeAbstractEntity> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView recipeTitle;
        private final TextView recipeUpdate;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.list_item_title_id);
            recipeUpdate = itemView.findViewById(R.id.list_update_id);
        }
    }
}
