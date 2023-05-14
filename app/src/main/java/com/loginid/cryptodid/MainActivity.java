package com.loginid.cryptodid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.loginid.cryptodid.model.Claim;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("tfhemain");
    }
    public static List<Claim> claims;
    public static DbDriver driver;
    public static File path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = new DbDriver(this);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        path = getFilesDir();
    }
    @Override
    protected void onDestroy() {
        driver.close();
        super.onDestroy();
    }


}