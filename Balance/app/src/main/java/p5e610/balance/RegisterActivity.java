package p5e610.balance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import p5e610.user.AccountHandler;
import p5e610.user.User;

//import p5e610.database.HerokuDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private void onAuthSuccess(FirebaseUser fbuser, User user) {
        // Write new user
        mDatabase.child("users").child(fbuser.getUid()).setValue(user);
        // Go to User Activity
        startActivity(new Intent(RegisterActivity.this, UserActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        final EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
        final EditText etLastName = (EditText) findViewById(R.id.etLastName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
//        final EditText etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final Button btnReturn = (Button) findViewById(R.id.btnCancel);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            // TODO: implement a web database instead
            @Override
            public void onClick(View v) {
                final String firstName = etFirstName.getText().toString();
                final String lastName = etLastName.getText().toString();
                final String userName = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String email = etEmail.getText().toString();
//                final String PasswordConfirm = etPasswordConfirm.getText().toString();
                final User localuser = new User(firstName, lastName, userName, email);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("Register Act", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                if (task.isSuccessful()) {
                                    AccountHandler.setUser(localuser);
                                    AccountHandler.setLogin(true);
                                    onAuthSuccess(task.getResult().getUser(), localuser);
                                }

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}