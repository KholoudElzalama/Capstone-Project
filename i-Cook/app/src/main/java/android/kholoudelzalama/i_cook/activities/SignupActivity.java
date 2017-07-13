package android.kholoudelzalama.i_cook.activities;

import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.kholoudelzalama.i_cook.objects.User;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private EditText et_email;
    private EditText et_password;
    private EditText et_name;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et_email =(EditText)findViewById(R.id.et_email);
        et_password =(EditText)findViewById(R.id.et_password);
        et_name =(EditText) findViewById(R.id.et_name);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void toSignIn(View view){

        Intent intent = new Intent(SignupActivity.this,SigninActivity.class);
        startActivity(intent);
        finish();
    }

    public void signUp(View view){
        mAuth = FirebaseAuth.getInstance();

        final String email = et_email.getText().toString();
        final String name =  et_name.getText().toString();
        String password =  et_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getString(R.string.email_msg), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), getString(R.string.name_msg), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.password_msg), Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), getString(R.string.password_limit), Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignupActivity.this, getString(R.string.fb_auth_failed),Toast.LENGTH_SHORT).show();

                        }
                        else{
                            User user = new User();
                            user.setEmail(email);
                            user.setName(name);
                            myRef.child(getString(R.string.fb_users)).child(mAuth.getCurrentUser().getUid()).setValue(user);
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(SignupActivity.this,HomeActivity.class));
                            finish();

                        }


                        // ...
                    }
                });
    }
}
