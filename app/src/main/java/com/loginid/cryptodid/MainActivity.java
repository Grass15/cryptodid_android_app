package com.loginid.cryptodid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.loginid.cryptodid.model.Claim;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Claim> claims;
    public static DbDriver driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = new DbDriver(this);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onDestroy() {
        driver.close();
        super.onDestroy();
    }


}