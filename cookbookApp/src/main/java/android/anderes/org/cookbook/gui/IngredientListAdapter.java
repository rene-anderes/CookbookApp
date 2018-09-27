package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.R;
import android.anderes.org.cookbook.database.IngredientEntity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.anderes.org.cookbook.R.layout.item_detail_ingredient_list_content;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ItemViewHolder> {

    private List<IngredientEntity> ingredients;

    @NonNull
    @Override
    public IngredientListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(item_detail_ingredient_list_content, parent, false);

        return new IngredientListAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {
        if (ingredients == null) {
            return;
        }
        IngredientEntity current = ingredients.get(position);
        itemViewHolder.ingredientPortion.setText(current.getPortion());
        itemViewHolder.ingredientDescription.setText(current.getDescription());
        itemViewHolder.ingredientComment.setText(current.getComment());
        itemViewHolder.itemView.setTag(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) {
            return 0;
        }
        return ingredients.size();
    }

    public void setIngredients(@NonNull final List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView ingredientPortion;
        private final TextView ingredientDescription;
        private final TextView ingredientComment;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientPortion = itemView.findViewById(R.id.ingredient_portion);
            ingredientDescription = itemView.findViewById(R.id.ingredient_description);
            ingredientComment = itemView.findViewById(R.id.ingredient_comment);
        }
    }
}
