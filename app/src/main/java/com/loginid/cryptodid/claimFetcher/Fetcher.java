package com.loginid.cryptodid.claimFetcher;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.loginid.cryptodid.MainActivity;
import com.loginid.cryptodid.claimFetcher.blinkid.BlinkActivity;
import com.loginid.cryptodid.claimFetcher.creditscore.CreditScoreActivity;
import com.loginid.cryptodid.claimFetcher.plaid.PlaidActivity;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.model.ClaimViewModel;
import com.loginid.cryptodid.protocols.Issuer;
import com.loginid.cryptodid.scanner.QrDecoder;
import com.loginid.cryptodid.scanner.Scanner;

import java.text.ParseException;


public class Fetcher {
    private String issuerUrl;
    private int issuerPort;
    private final int bankIssuerPort = 6666;
    private final int ageIssuerPort = 7777;
    private final int creditScoreIssuerPort = 8888;
    private Fragment callerFragment;
    private ActivityResultLauncher<Intent> plaidActivityLauncher;
    private ActivityResultLauncher<Intent> blinkActivityLauncher;
    private ActivityResultLauncher<Intent> creditScoreActivityLauncher;
    Issuer issuer = new Issuer();
    private ClaimViewModel claimViewModel;
    private Scanner scanner;
    private ActivityResultLauncher<ScanOptions> barLauncher;

    public Fetcher(Fragment callerFragment){
        this.callerFragment = callerFragment;
        claimViewModel =  new ViewModelProvider(callerFragment.requireActivity()).get(ClaimViewModel.class);
        this.barLauncher = callerFragment.registerForActivityResult(new ScanContract(), result -> {
            if (result != null && result.getContents() != null) {
                QrDecoder decodedData = new QrDecoder(result.getContents());
                this.issuerUrl = decodedData.getUrl();
                this.issuerPort = decodedData.getPort();
                try {
                    this.launchTrustedSource();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.plaidActivityLauncher = callerFragment.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null){
                            try {
                                storeClaim(data);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
        this.blinkActivityLauncher = callerFragment.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null){
                            try {
                                storeClaim(data);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
        this.creditScoreActivityLauncher = callerFragment.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null){
                            try {
                                storeClaim(data);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
        this.scanner = new Scanner(callerFragment);
    }


    public void getClaim() throws InterruptedException{
        Thread scan = new Thread() {
            public void run() {
                scanner.scanCode();
            }
        };
        scan.start();
        scan.join();
        this.barLauncher.launch(scanner.options);
    }
    public void launchTrustedSource() throws ParseException {

        if(issuerPort == bankIssuerPort){
            Intent plaidActivity = new Intent(callerFragment.getActivity(), PlaidActivity.class);
            this.plaidActivityLauncher.launch(plaidActivity);
        }
        else if(issuerPort == ageIssuerPort){
            Intent blinkActivity = new Intent(callerFragment.getActivity(), BlinkActivity.class);
            this.creditScoreActivityLauncher.launch(blinkActivity);
        }
        else if(issuerPort == creditScoreIssuerPort){
            Intent creditScoreActivity = new Intent(callerFragment.getActivity(), CreditScoreActivity.class);
            this.creditScoreActivityLauncher.launch(creditScoreActivity);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
            builder.setTitle("Error");
            builder.setMessage("Please Ensure you scanned the good QR ");
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    }
    public native int TFHE(int n1, String filepath, String attribute);

    public void storeClaim(Intent data) throws ParseException {
        int attribute = Integer.parseInt(data.getStringExtra("attribute"));
        String claimTitle = data.getStringExtra("claimTitle");
        String attr = data.getStringExtra("attr");
        String claimType = data.getStringExtra("claimType");
        String claimIssuerName = data.getStringExtra("issuerName");
        String claimContent = data.getStringExtra("claimContent");
        TFHE(attribute, String.valueOf(MainActivity.path), attr);
        issuer.setAttribute(attribute);
        Claim claim = issuer.getClaim(claimIssuerName,  claimType, claimTitle, claimContent, attr);
        claimViewModel.stickNewValue(claim);
    }
    public void storeAge() throws ParseException {
        int attribute = 23;
        String claimTitle = "Age Claim";
        String attr = "age";
        String claimType = "Age";
        String claimIssuerName = "Crypto DID: Expires on ";
        String claimContent = "You can use this claim to attest your age";
        TFHE(attribute, String.valueOf(MainActivity.path), attr);
        issuer.setAttribute(attribute);
        Claim claim = issuer.getClaim( claimIssuerName,  claimType, claimTitle, claimContent, attr);
        claimViewModel.stickNewValue(claim);
    }
}
