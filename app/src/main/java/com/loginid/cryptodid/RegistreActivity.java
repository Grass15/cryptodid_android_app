package com.loginid.cryptodid;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.loginid.cryptodid.model.User;

import java.util.concurrent.Executor;
import java.util.regex.Pattern;

public class RegistreActivity extends AppCompatActivity {

    EditText username,password,repassword;
    DbDriver driver;
    Button Registre;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);

        username = findViewById(R.id.email);
        password = findViewById(R.id.r_passwd);
        repassword = findViewById(R.id.re_passwd);
        Registre = findViewById(R.id.Register);
        driver = new DbDriver(this);



        //Biomtrecs Executor

        Executor executor = ContextCompat.getMainExecutor(this);

        Registre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                //TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)
                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)){
                    Toast.makeText(RegistreActivity.this, "please fill the missing input", Toast.LENGTH_SHORT).show();
                }else {
                    if(!(pass.equals(repass))  || !(pattern.matcher(user).matches())){
                        Toast.makeText(RegistreActivity.this, "Invalid email or bad password" + pass + repass, Toast.LENGTH_SHORT).show();
                    }else{
                        // Biometrics prompt
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                            BiometricPrompt biometricPrompt = new BiometricPrompt( RegistreActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                                @Override
                                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                    super.onAuthenticationError(errorCode, errString);
                                    Toast.makeText(RegistreActivity.this, "Cannot identify FingerPrint", Toast.LENGTH_SHORT).show();;
                                }

                                @Override
                                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                    super.onAuthenticationSucceeded(result);
                                    Toast.makeText(RegistreActivity.this, "Verified with success", Toast.LENGTH_SHORT).show();

                                    if(driver.register(user,pass)){
                                    User usera = new User(user,pass,null);
                                    Bundle data = new Bundle();
                                        //Redirecting to the Login page
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(RegistreActivity.this, "Ooops something went wrong", Toast.LENGTH_SHORT).show();
                                }

                                }

                                @Override
                                public void onAuthenticationFailed() {
                                    super.onAuthenticationFailed();
                                    Toast.makeText(RegistreActivity.this, "Ooops something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        if(driver.register(user,pass)){
                            User usera = new User(user,pass,null);
                            Bundle data = new Bundle();
                            //Redirecting to the Login page
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(RegistreActivity.this, "Ooops something went wrong", Toast.LENGTH_SHORT).show();
                        }



//                        //Launching Prompt
//                        BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
//                        promptInfo.setNegativeButtonText("Cancel");
//                        //promptInfo.setDeviceCredentialAllowed(true);
//                        biometricPrompt.authenticate(promptInfo.build());

                        /*if(driver.insertData(user,pass)){
                            User usera = new User(user,pass,null);
                            Bundle data = new Bundle();

                            //Nesting Biometrics
                            BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
                            promptInfo.setNegativeButtonText("Cancel");
                            //promptInfo.setDeviceCredentialAllowed(true);
                            biometricPrompt.authenticate(promptInfo.build());
                        }else{
                            Toast.makeText(RegistreActivity.this, "Ooops something went wrong", Toast.LENGTH_SHORT).show();
                        }*/
                    }

                }

            }
        });
    }


    //Biometrics Dialog
    BiometricPrompt.PromptInfo.Builder dialogMetric(){
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("To contunue you sign up please verify its you")
                .setSubtitle("Finger Biometrics Credentials");
    }



    }
