package android.kholoudelzalama.i_cook.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.kholoudelzalama.i_cook.R;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.kholoudelzalama.i_cook.R.id.et_old_password;

public class ChangePasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText oldPassword;
    private EditText newPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.btn_reset_password));
        oldPassword = (EditText) findViewById(et_old_password);
        newPassword = (EditText) findViewById(R.id.et_new_password);
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_password));
    }

    public void resetPassword(View view) {
        final FirebaseUser user;
        AuthCredential credential;
        progressDialog.show();
        if (!TextUtils.isEmpty(oldPassword.getText().toString())&&!TextUtils.isEmpty(newPassword.getText().toString())) {
            if (newPassword.getText().length() < 6) {
                newPassword.setError(getString(R.string.password_limit));
                return;
            }
            user = FirebaseAuth.getInstance().getCurrentUser();
            credential = EmailAuthProvider
                    .getCredential(user.getEmail(), oldPassword.getText().toString());

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), getString(R.string.change_password_suc), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(ChangePasswordActivity.this, UserProfileActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            progressDialog.dismiss();
                                            newPassword.setError(getString(R.string.invalid_password));
                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                Log.d("old password class", "Error auth failed");
                                oldPassword.setError(getString(R.string.wrong_password));
                            }
                        }
                    });
        }
        else
        {
            progressDialog.dismiss();
            if(TextUtils.isEmpty(oldPassword.getText().toString())&&TextUtils.isEmpty(newPassword.getText().toString())) {
                oldPassword.setError(getString(R.string.enter_password));
                newPassword.setError(getString(R.string.enter_password));
            }
            else if(TextUtils.isEmpty(oldPassword.getText().toString())) {
                oldPassword.setError(getString(R.string.enter_password));
            }
            else if (TextUtils.isEmpty(newPassword.getText().toString())) {
                newPassword.setError(getString(R.string.enter_password));
            }
        }
    }

}
