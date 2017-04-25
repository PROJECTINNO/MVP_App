package p5e610.balance;

import android.content.Intent;
import android.provider.ContactsContract;
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

//import p5e610.database.HerokuDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        final EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
        final EditText etLastName = (EditText) findViewById(R.id.etLastName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
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
                final String PasswordConfirm = etPasswordConfirm.getText().toString();


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("Register Act", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
//                HerokuDatabaseHelper dh = HerokuDatabaseHelper.getInstance(getApplicationContext());
//                try {
                //HerokuDatabaseHelper dh = HerokuDatabaseHelper.getInstance(getApplicationContext());
//                try {
//                    if(dh.usernameTaken(userName)) {
//                        Toast.makeText(getApplicationContext(), R.string.username_taken, Toast.LENGTH_LONG).show();
//                    } else {
//                        dh.addUser(firstName, lastName, userName, email, password, false);
//                        Intent continueIntent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        RegisterActivity.this.startActivity(continueIntent);
//                        finish();
//                    }
//                } catch (Exception e) {
//                    //TODO replace  with different behavior
//                    e.printStackTrace();
//                }
                //         }
//                } catch(Exception e) {
//                    //TODO replace  with different behavior
//                    e.printStackTrace();
//                }
//            }

            }

            ;

//        btnReturn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent returnIntent = new Intent(RegisterActivity.this, LoginActivity.class);
//                RegisterActivity.this.startActivity(returnIntent);
//                finish();
//            }
//
//            });

        });
    }
}