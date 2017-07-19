package android.kholoudelzalama.i_cook.activities;

import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.adapters.HomeAdapter;
import android.kholoudelzalama.i_cook.objects.Hits;
import android.kholoudelzalama.i_cook.objects.Recipe;
import android.kholoudelzalama.i_cook.objects.Recipes;
import android.kholoudelzalama.i_cook.objects.User;
import android.kholoudelzalama.i_cook.utilities.NetworkConnectivity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.drawer_fav));

        if (!NetworkConnectivity.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference(getString(R.string.fb_users));


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getFav();

                    }
                }
        );


        getFav();

    }

    private void getFav() {
        if (mAuth.getCurrentUser() == null) {
            swipeRefreshLayout.setRefreshing(false);
            startActivity(new Intent(FavouritesActivity.this, SigninActivity.class));
            return;
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = new User();
                user = dataSnapshot.child(mAuth.getCurrentUser().getUid().toString()).getValue(User.class);
                Recipes r = new Recipes();
                if (user.getFavourites() == null) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(FavouritesActivity.this, getString(R.string.no_fav_toast), Toast.LENGTH_LONG).show();
                    adapter = new HomeAdapter(FavouritesActivity.this, r);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                } else {

                    List<Hits> hits = new ArrayList<Hits>();
                    for (Recipe re : user.getFavourites()) {
                        Hits h = new Hits();
                        h.setRecipe(re);
                        hits.add(h);
                    }
                    r.setHits(hits);
                    adapter = new HomeAdapter(FavouritesActivity.this, r);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("RecipeActivity", "Failed to read value.", error.toException());
            }
        });


    }
}
