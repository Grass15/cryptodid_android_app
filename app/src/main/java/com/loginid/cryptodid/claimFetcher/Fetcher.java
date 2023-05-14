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
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.model.ClaimViewModel;
import com.loginid.cryptodid.protocols.Issuer;
import com.loginid.cryptodid.protocols.MG_FHE;
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
    Issuer issuer = new Issuer();
    MG_FHE fhe = new MG_FHE(11,512);
    private ClaimViewModel claimViewModel;

    public Fetcher(Fragment callerFragment){
        this.callerFragment = callerFragment;
        claimViewModel =  new ViewModelProvider(callerFragment.requireActivity()).get(ClaimViewModel.class);
    }


    public native int TFHE(int n1, String filepath, String attribute);

    public void storeClaim() throws ParseException {
        int SIN = 121314615;
        String claimTitle = "SIN VC";
        String claimType = "SIN";
        String claimIssuerName = "Crypto DID: Expires on ";
        String claimContent = "You can use this verifiable credential to attest your SIN";
        TFHE(SIN, String.valueOf(MainActivity.path), "sin");
        issuer.setAttribute(SIN);
        Claim claim = issuer.getClaim(claimIssuerName,  claimType, claimTitle, claimContent, "sin");
        claimViewModel.stickNewValue(claim);
    }
}
