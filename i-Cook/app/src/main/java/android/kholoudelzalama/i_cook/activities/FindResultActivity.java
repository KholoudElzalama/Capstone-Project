package android.kholoudelzalama.i_cook.activities;

import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.fragments.FindResultActivityFragment;
import android.kholoudelzalama.i_cook.utilities.NetworkConnectivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class FindResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!NetworkConnectivity.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
        }
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.query_extra),intent.getStringExtra(getString(R.string.query_extra)));
        FindResultActivityFragment fragment = new FindResultActivityFragment();
        fragment.setArguments(bundle);
        if(null==savedInstanceState){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment,fragment).commit();
        }

    }

}
