package android.kholoudelzalama.i_cook.fragments;

import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.interfaces.RecipeListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchRecipeActivityFragment extends Fragment {

    private EditText searchQuery;
    private Button find;
    private RecipeListener recipeListener;

    public SearchRecipeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_recipe, container, false);
        searchQuery =(EditText) rootView.findViewById(R.id.et_find);
        find=(Button)rootView.findViewById((R.id.find_btn));
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result();
            }
        });
        return rootView;
    }


    public void result(){
        if (TextUtils.isEmpty(searchQuery.getText().toString())){
            searchQuery.setError(getActivity().getString(R.string.query_error));
        }
        else{
            recipeListener.setselectedRecipe(searchQuery.getText().toString());
            searchQuery.setText("");
        }

    }

    public void setRecipeListener(RecipeListener r){
        recipeListener=r;
    }
}
