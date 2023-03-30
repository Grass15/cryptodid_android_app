package com.loginid.cryptodid.scanner;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.loginid.cryptodid.R;

public class Scanner {
    private String resultedUrl;
    private String resultOtherData;
    private int resultedPort;
    public ScanOptions options;
    private ActivityResultLauncher<ScanOptions> barLauncher;

    public Scanner(Fragment callerFragment) {
         this.barLauncher = callerFragment.registerForActivityResult(new ScanContract(), result -> {
            if (result != null && result.getContents() != null) {
                QrDecoder decodedData = new QrDecoder(result.getContents());
                this.resultedUrl = decodedData.getUrl();
                this.resultedPort = decodedData.getPort();
                this.resultOtherData = decodedData.getOtherData();
            }
        });
    }


    public String getResultedUrl(){return this.resultedUrl;}
    public String getResultOtherData(){return this.resultOtherData;}
    public int getResultedPort(){return this.resultedPort;}
    public void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(ScanActivity.class);
        this.options = options;
        //this.barLauncher.launch(options);
    }

}
