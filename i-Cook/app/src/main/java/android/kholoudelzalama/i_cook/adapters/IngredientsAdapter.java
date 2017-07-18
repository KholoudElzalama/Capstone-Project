package android.kholoudelzalama.i_cook.adapters;

import android.content.Context;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.objects.Recipe;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by win on 11/07/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Recipe recipe;
    Context mContext;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public IngredientsAdapter(Recipe r, Context c) {
        recipe = r;
        mContext = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.ingredient_item, parent, false);
            return new ViewHolderItem(view);
        } else if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.list_view_header, parent, false);
            return new ViewHolderHeader(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader vh = (ViewHolderHeader) holder;
            vh.src.setText(recipe.getSource());
        } else if (holder instanceof ViewHolderItem) {
            ViewHolderItem vh = (ViewHolderItem) holder;
            vh.ingredient.setText(recipe.getIngredientLines().get(position - 1));
        }

    }

    @Override
    public int getItemCount() {
        return recipe.getIngredientLines().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {

        public TextView ingredient;


        public ViewHolderItem(View view) {
            super(view);
            ingredient = (TextView) view.findViewById(R.id.tv_ingredient);

        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {

        public TextView src;


        public ViewHolderHeader(View view) {
            super(view);
            src = (TextView) view.findViewById(R.id.tv_src);

        }
    }
}


