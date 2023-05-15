package com.loginid.cryptodid.claimFetcher;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.loginid.cryptodid.MainActivity;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.model.ClaimViewModel;
import com.loginid.cryptodid.protocols.Issuer;

import java.text.ParseException;


public class Fetcher {
    private String issuerUrl;
    private int issuerPort;
    private final int bankIssuerPort = 6666;
    private final int ageIssuerPort = 7777;
    private final int creditScoreIssuerPort = 8888;
    private Fragment callerFragment;
    Issuer issuer = new Issuer();

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
