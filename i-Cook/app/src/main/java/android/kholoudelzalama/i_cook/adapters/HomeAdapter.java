package android.kholoudelzalama.i_cook.adapters;

import android.content.Context;
import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.activities.RecipeDetailsActivity;
import android.kholoudelzalama.i_cook.objects.Recipes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * Created by win on 10/07/2017.
 */

public class HomeAdapter  extends RecyclerView.Adapter<ViewHolder> {
    Recipes homeRecipes;
    Context mContext;
    Gson gson;

    public HomeAdapter(Context context, Recipes recipes) {
        homeRecipes = recipes;
        mContext = context;
        gson = new Gson();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.grid_item, parent, false);
            return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.label.setText(homeRecipes.getHits().get(position).getRecipe().getLabel());
        Picasso.with(mContext).load(homeRecipes.getHits().get(position).getRecipe().getImage()).placeholder(R.drawable.loading).fit().centerCrop().into(holder.recipePic, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
                String details = gson.toJson(homeRecipes.getHits().get(position).getRecipe());
                intent.putExtra(mContext.getString(R.string.recipe_details_extra), details);
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        try{
        return homeRecipes.getHits().size();
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


}
class ViewHolder extends RecyclerView.ViewHolder {

    public ImageView recipePic;
    public TextView label;


    public ViewHolder(View view) {
        super(view);
        recipePic = (ImageView) view.findViewById(R.id.iv_recipe_photo);
        label = (TextView) view.findViewById(R.id.tv_label);

    }
}
