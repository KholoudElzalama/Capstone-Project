package android.kholoudelzalama.i_cook.activities;

import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.fragments.FindResultActivityFragment;
import android.kholoudelzalama.i_cook.fragments.SearchRecipeActivityFragment;
import android.kholoudelzalama.i_cook.interfaces.RecipeListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

public class SearchRecipeActivity extends AppCompatActivity implements RecipeListener {

   private boolean onePane;
   private boolean twoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.drawer_find));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FrameLayout twoPaneui = (FrameLayout)findViewById(R.id.twopane);
        if(null==twoPaneui){

            twoPane=false;
            Log.d("test","one pane");

        }
        else{
            twoPane =true;
            Log.d("test","two pane") ;

        }
        if(null==savedInstanceState){
            SearchRecipeActivityFragment fragment = new SearchRecipeActivityFragment();
            fragment.setRecipeListener(this);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment,fragment).commit();
        }
    }

    @Override
    public void setselectedRecipe(String query) {
        if(twoPane){

            FindResultActivityFragment fragment = new FindResultActivityFragment();
            Bundle extra = new Bundle();
            extra.putString(getString(R.string.query_extra),query);
            fragment.setArguments(extra);
            getSupportFragmentManager().beginTransaction().replace(R.id.twopane,fragment).commit();
        }
        else{
            Intent intent =new Intent(this, FindResultActivity.class);
            intent.putExtra(getString(R.string.query_extra),query);
            startActivity(intent);
        }

    }
}
