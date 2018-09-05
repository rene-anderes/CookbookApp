package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.database.RecipeAbstractEntity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>  {

    private final LayoutInflater mInflater;
    private List<RecipeAbstractEntity> recipes;

    ItemListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
