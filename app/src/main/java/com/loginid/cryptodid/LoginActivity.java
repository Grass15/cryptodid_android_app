package com.loginid.cryptodid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.model.User;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    TextView gotoRegisterPage;
    DbDriver driver;
    Button Login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.passwd);
        Login = findViewById((R.id.Login));
        driver = new DbDriver(this);
        gotoRegisterPage = findViewById(R.id.register_page);

        gotoRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistreActivity.class);
                startActivity(intent);
            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String user = username.getText().toString();
                 String pass = password.getText().toString();
                 if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)){
                     Toast.makeText(LoginActivity.this, "Please fill the missing Input", Toast.LENGTH_SHORT).show();
                 }else{
                     if(driver.checkCred(user,pass)){
                         User usera = new User(user,pass,null);
                         Bundle data = new Bundle();
                         //Redirecting to the HomeActivity on Success
                         Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                         startActivity(intent);

                     }else{
                         Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                     }
                 }
            }
        });

    }

}