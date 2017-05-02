package p5e610.balance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import p5e610.user.AccountHandler;
import p5e610.user.User;

public class SplashActivity extends AppCompatActivity {
    public static int SPLASH_TIMEOUT = 3000;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String uid = user.getUid();
                    DatabaseReference childRef = mDatabase.child("users").child(uid);
                    childRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Intent loginIntent = new Intent(SplashActivity.this, UserActivity.class);
                            User localuser = dataSnapshot.getValue(User.class);
                            System.out.println(localuser.getName());
                            AccountHandler.setUser(localuser);
                            AccountHandler.setLogin(true);
                            if (AccountHandler.getUser()!= null){
//                                findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
//                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                SplashActivity.this.startActivity(loginIntent);
//                                finish();
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run(){
                                        Intent homeIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                        startActivity(homeIntent);
                                        finish();
                                    }
                                }, SPLASH_TIMEOUT);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
//                    Intent loginIntent = new Intent(LoginActivity.this, UserActivity.class);
//                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());
//                    findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
//                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    LoginActivity.this.startActivity(loginIntent);
//                    finish();
                } else {
                    // User is signed out
                    Log.d("LoginActivity", "onAuthStateChanged:signed_out");
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            Intent homeIntent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    }, SPLASH_TIMEOUT);
                }
                // ...
            }
        };

    }

}
