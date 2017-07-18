package android.kholoudelzalama.i_cook.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.objects.User;
import android.kholoudelzalama.i_cook.utilities.NetworkConnectivity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

import static android.kholoudelzalama.i_cook.R.id.iv_pp;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final int GALLERY_INTENT = 10;
    private DatabaseReference myRef;
    private String uid;
    private User user;
    private FirebaseAuth auth;
    private StorageReference mStorageRef;
    EditText name;
    TextView email;
    ImageView userPic;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.user_prof));
        if (!NetworkConnectivity.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
        }

        auth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        name = (EditText) findViewById(R.id.editText_name);
        email = (TextView) findViewById(R.id.editText_email);
        userPic = (ImageView) findViewById(iv_pp);

        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();

        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_info));
        progressDialog.show();
        getCurrentStudent();
    }

    public void getCurrentStudent() {

        myRef = database.getReference(getString(R.string.fb_users));
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User s = new User();
                s = dataSnapshot.child(uid).getValue(User.class);
                name.setText(s.getName());
                email.setText(s.getEmail());

                try {
                    mStorageRef.child(getString(R.string.fb_pp_folder)).child(s.getPhoto()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(UserProfileActivity.this).load(uri).placeholder(R.drawable.loading).fit().centerCrop().into(userPic, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();                                    }
                                }

                                @Override
                                public void onError() {

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(UserProfileActivity.this, exception.getMessage().toString(), Toast.LENGTH_LONG).show();
                            // Handle any errors
                        }
                    });

                } catch (Exception e) {
                    Log.d("user Acoount class", e.getMessage());
                    progressDialog.dismiss();

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Firebaseclass", "Failed to read value.", error.toException());
            }

        });

    }

    public void saveChanges(View view) {
        myRef = database.getReference(getString(R.string.fb_users));
        myRef.child(uid).child(getString(R.string.fb_users_name)).setValue(name.getText().toString());
        Toast.makeText(UserProfileActivity.this, getString(R.string.info_changed_msg_suc), Toast.LENGTH_LONG).show();
    }

    public void editPic(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.show();
        if (requestCode == GALLERY_INTENT) {
            Uri uri = data.getData();
            myRef = database.getReference(getString(R.string.fb_users));
            mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference picRef = mStorageRef.child(getString(R.string.fb_pp_folder).toString()).child(auth.getCurrentUser().getUid().toString() + "pp");
            picRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            myRef.child(auth.getCurrentUser().getUid().toString()).child(getString(R.string.fb_users_pp)).setValue(auth.getCurrentUser().getUid().toString() + "pp");
                            Random rand = new Random();
                            int pickedNumber = rand.nextInt(40) + 1;
                            myRef.child(auth.getCurrentUser().getUid().toString()).child("is_changed").setValue(pickedNumber);
                            Toast.makeText(getApplicationContext(), getString(R.string.upload_msg_suc).toString(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), getString(R.string.upload_msg_fail).toString(), Toast.LENGTH_LONG).show();

                        }
                    });
        }
    }

    public void toChangePassword(View view) {
        Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}
