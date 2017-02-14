package p5e610.balance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        final EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
        final EditText etLastName = (EditText) findViewById(R.id.etLastName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final Button btnReturn = (Button) findViewById(R.id.btnCancel);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            // TODO: implement a web database instead
            @Override
            public void onClick(View v){
                final String firstName = etFirstName.getText().toString();
                final String lastName = etLastName.getText().toString();
                final String userName = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String PasswordConfirm = etPasswordConfirm.getText().toString();

                Intent continueIntent = new Intent(RegisterActivity.this, TestActivity.class);
                RegisterActivity.this.startActivity(continueIntent);
            }

        });
    }
}
