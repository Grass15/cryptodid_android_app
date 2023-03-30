package com.loginid.cryptodid.claimFetcher.blinkid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.loginid.cryptodid.MainActivity;
import com.loginid.cryptodid.R;
import com.loginid.cryptodid.model.User;
import com.microblink.MicroblinkSDK;
import com.microblink.activity.result.ScanResult;
import com.microblink.activity.result.contract.MbScan;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.uisettings.BlinkIdUISettings;
import com.microblink.uisettings.UISettings;

public class BlinkActivity extends AppCompatActivity {

    private BlinkIdCombinedRecognizer recognizer;
    private RecognizerBundle recognizerBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MicroblinkSDK.setLicenseFile("license.key", this);
        setContentView(R.layout.blink_activity);

        // we'll use Machine Readable Travel Document recognizer
        recognizer = new BlinkIdCombinedRecognizer();

        // put our recognizer in bundle so that it can be sent via intent
        recognizerBundle = new RecognizerBundle(recognizer);
    }

    public void onScanButtonClick(View view) {
        // use default UI for scanning documents
        BlinkIdUISettings uiSettings = new BlinkIdUISettings(recognizerBundle);

        // start scan activity based on UI settings
        blinkIdScanLauncher.launch(uiSettings);
    }

    private final ActivityResultLauncher<UISettings> blinkIdScanLauncher = registerForActivityResult(
            new MbScan(),
            result -> {
                if (result.getResultStatus() == ScanResult.ResultStatus.FINISHED) {
                    // OK result code means scan was successful
                    onScanSuccess(result.getData());
                } else if (result.getResultStatus() == ScanResult.ResultStatus.EXCEPTION) {
                    Toast.makeText(this, result.getException().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    // user probably pressed Back button and cancelled scanning
                    onScanCanceled();
                }
            }

    );

    private void onScanSuccess(Intent data) {
        // update recognizer results with scanned data
        recognizerBundle.loadFromIntent(data);
        User user = MainActivity.driver.getUser();


        // you can now extract any scanned data from result, we'll just get primary id
        BlinkIdCombinedRecognizer.Result result = recognizer.getResult();
        user.firstname = result.getFirstName();
        user.lastname = result.getLastName();
        user.address = result.getAddress();
        user.phone = "+212 625980211";
        String name = result.getFullName();
        int age = result.getAge();
        if (name.isEmpty()) {
            name = result.getFirstName();
        }
        MainActivity.driver.updateUserInformation(user);
        Toast.makeText(this, "Name: " + name + "Age:" + age, Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        intent.putExtra("attribute", age+"");
        intent.putExtra("claimTitle", "Age Claim");
        intent.putExtra("claimType", "Age");
        intent.putExtra("issuerName", "Crypto DID: Expires on ");
        intent.putExtra("claimContent", "You can use this claim to attest your age");
        setResult(RESULT_OK, intent);
        finish();
    }
    private void onScanCanceled() {
        Toast.makeText(this, "Scan cancelled!", Toast.LENGTH_SHORT).show();
    }

}
