package android.kholoudelzalama.i_cook.activities;

import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.objects.User;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactUsActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private EditText msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        msg =(EditText) findViewById(R.id.et_msg) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.contact_us));

    }

    public void sendMsg(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference(getString(R.string.fb_users));
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = new User();
                user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                String key;
                if(user.getMsgs()==null){
                    key =String.valueOf(0);
                }
                else {
                    key = String.valueOf(user.getMsgs().size());
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference(getString(R.string.fb_users));
                ref.child(mAuth.getCurrentUser().getUid()).child(getString(R.string.fb_msg)).child(key).setValue(msg.getText().toString());
                msg.setText("");
                Toast.makeText(ContactUsActivity.this,getString(R.string.msg_suc),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("RecipeActivity", "Failed to read value.", error.toException());
            }
        });

    }
}
