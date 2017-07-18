package android.kholoudelzalama.i_cook.activities;

import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.adapters.HomeAdapter;
import android.kholoudelzalama.i_cook.objects.Recipes;
import android.kholoudelzalama.i_cook.objects.User;
import android.kholoudelzalama.i_cook.trasformation.CircleTransform;
import android.kholoudelzalama.i_cook.utilities.NetworkConnectivity;
import android.kholoudelzalama.i_cook.utilities.NetworkUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import static android.kholoudelzalama.i_cook.R.id.tv_email;
import static android.kholoudelzalama.i_cook.R.id.tv_name;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<String> {


    private FirebaseAuth mAuth;

    private String uid;
    private User user;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private View navHeader;
    private ImageView profilePic;
    private TextView nameTextView, emailTextView;


    private static final int API_LOADER = 10;
    private static final int API_LOADER_NEXT_PAGE = 20;


    Gson gson;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private HomeAdapter adapter;

    Recipes recipes;


    public static final int PAGE_SIZE = 40;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isConnected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!NetworkConnectivity.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
            isConnected = false;
        }
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, SigninActivity.class));
            finish();
        } else {
            uid = mAuth.getCurrentUser().getUid();
        }

        gson = new Gson();
        recipes = new Recipes();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Navigation header view
        navHeader = navigationView.getHeaderView(0);
        nameTextView = (TextView) navHeader.findViewById(tv_name);
        emailTextView = (TextView) navHeader.findViewById(tv_email);
        profilePic = (ImageView) navHeader.findViewById(R.id.iv_pp);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        currentPage = 1;
                        getSupportLoaderManager().initLoader(API_LOADER, null, HomeActivity.this);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        getCurrentStudent();
        getSupportLoaderManager().initLoader(API_LOADER, null, HomeActivity.this);
        final StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        isLoading = true;

                        currentPage += 1;
                        progressBar.setVisibility(View.VISIBLE);
                        getSupportLoaderManager().initLoader(API_LOADER_NEXT_PAGE + currentPage, null, HomeActivity.this);


                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_find) {
            startActivity(new Intent(HomeActivity.this, SearchRecipeActivity.class));
        } else if (id == R.id.nav_fav) {
            startActivity(new Intent(HomeActivity.this, FavouritesActivity.class));
        } else if (id == R.id.nav_aboutus) {
            startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));

        } else if (id == R.id.nav_contactus) {
            startActivity(new Intent(HomeActivity.this, ContactUsActivity.class));
        } else if (id == R.id.nav_view) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this, SigninActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getCurrentStudent() {
        myRef = database.getReference(getString(R.string.fb_users));
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                DataSnapshot snapshot = dataSnapshot.child(uid);
                User u = new User();
                u = snapshot.getValue(User.class);

                nameTextView.setText(u.getName());
                emailTextView.setText(u.getEmail());
                try {
                    mStorageRef.child(getString(R.string.fb_pp_folder)).child(u.getPhoto()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.with(getBaseContext()).load(uri).placeholder(R.drawable.ic_action_account_circle_40).transform(new CircleTransform()).fit().centerCrop().into(profilePic);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(HomeActivity.this, exception.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });


                } catch (Exception e) {
                    Log.d("User P.P", e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Firebaseclass", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {


                forceLoad();
            }

            @Override
            public String loadInBackground() {

                try {
                    String searchQuery = null;
                    int from = PAGE_SIZE * (currentPage - 1);
                    int to = from + PAGE_SIZE;
                    URL url = NetworkUtils.buildUrl(searchQuery, String.valueOf(from), String.valueOf(to));
                    String result = NetworkUtils.getResponseFromHttpUrl(url);

                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        swipeRefreshLayout.setRefreshing(false);
        if (null == data) {
            if (isConnected) {
                Toast.makeText(this, getString(R.string.api_limits), Toast.LENGTH_LONG).show();
            }
        } else {
            recipes = gson.fromJson(data, Recipes.class);
            adapter = new HomeAdapter(HomeActivity.this, recipes);
            recyclerView.setAdapter(adapter);
            if (recipes.getHits().size() == 0) {
                isLastPage = true;
            }
            if (currentPage > 1) {
                progressBar.setVisibility(View.INVISIBLE);
                isLoading = false;
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
