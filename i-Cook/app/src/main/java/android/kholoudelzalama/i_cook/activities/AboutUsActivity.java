package android.kholoudelzalama.i_cook.activities;

import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.utilities.NetworkConnectivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutUsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        aboutUs = (TextView) findViewById(R.id.tv_about_us);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.about_us));
        if (!NetworkConnectivity.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.fb_aboutus));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String content;
                content = dataSnapshot.getValue(String.class);
                if (content != null) {
                    aboutUs.setText(content);
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
