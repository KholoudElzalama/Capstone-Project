package android.kholoudelzalama.i_cook.activities;

import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.adapters.IngredientsAdapter;
import android.kholoudelzalama.i_cook.objects.Recipe;
import android.kholoudelzalama.i_cook.objects.User;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class RecipeDetailsActivity extends AppCompatActivity {

    private String extra;
    private Recipe recipe;
    private Gson gson;

    private RecyclerView recyclerView;
    private FloatingActionButton  fab;
    private ImageView recipePic;


    private Toolbar toolbar;

    private boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.fb_users));
        gson = new Gson();
        isFav = false;
        extra = getIntent().getStringExtra(getString(R.string.recipe_details_extra));
        recipe = gson.fromJson(extra,Recipe.class);
        getSupportActionBar().setTitle(recipe.getLabel());
        recyclerView =(RecyclerView) findViewById(R.id.rv_ingreients);
        fab = (FloatingActionButton)findViewById(R.id.fab_fav);
        recipePic = (ImageView)findViewById(R.id.iv_recipe_photo);

        //src =(TextView)header.findViewById(R.id.tv_src);
        setTitle(recipe.getLabel());
        Picasso.with(this).load(recipe.getImage()).placeholder(R.drawable.loading).fit().centerCrop().into(recipePic, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
       // src.setText(recipe.getSource());
        recyclerView.setAdapter(new IngredientsAdapter(recipe,RecipeDetailsActivity.this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeDetailsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = new User();
                user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                try {

                    for (Recipe r : user.getFavourites()) {
                        if (r.getLabel().equals(recipe.getLabel())) {
                            isFav = true;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(isFav){
                    fab.setImageResource(R.drawable.ic_christmas_star_48);
                }
                else{
                    fab.setImageResource(R.drawable.ic_star_48);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("RecipeActivity", "Failed to read value.", error.toException());
            }
        });



    }

    public void toSource(View view){
        Uri webpage = Uri.parse(recipe.getUrl());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }
    public void makeFav(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.fb_users));
        if(isFav){
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User user = new User();
                    int index = -1;
                    user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                    for(int i =0 ;i< user.getFavourites().size();i++){
                        if(user.getFavourites().get(i).getLabel().equals(recipe.getLabel()) ){
                           index = i;
                        }
                    }
                    user.getFavourites().remove(index);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference(getString(R.string.fb_users));
                    ref.child(mAuth.getCurrentUser().getUid()).child(getString(R.string.fb_fav)).setValue(user.getFavourites());
                    fab.setImageResource(R.drawable.ic_star_48);
                    isFav =false;
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("RecipeActivity", "Failed to read value.", error.toException());
                }
            });
        }
        else{
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = new User();
                 user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                String key;
                if(user.getFavourites()==null){
                    key =String.valueOf(0);
                }
                else {
                    key = String.valueOf(user.getFavourites().size());
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference(getString(R.string.fb_users));
                ref.child(mAuth.getCurrentUser().getUid()).child(getString(R.string.fb_fav)).child(key).setValue(recipe);
                fab.setImageResource(R.drawable.ic_christmas_star_48);
                isFav=true;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("RecipeActivity", "Failed to read value.", error.toException());
            }
        });

    }
    }
}
