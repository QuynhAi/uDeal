package edu.tacoma.uw.udeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText emailEdit;
    private EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);


        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "enter email or password" , Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TextView textBtn = (TextView) findViewById(R.id.registerNowText);
        textBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Login.this, AddNewUser.class);
                startActivity(intent);
            }
        });
    }


}
