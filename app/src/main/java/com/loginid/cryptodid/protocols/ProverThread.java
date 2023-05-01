package com.loginid.cryptodid.protocols;


import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.loginid.cryptodid.claimVerifier.ClientEndpoint;
import com.loginid.cryptodid.model.Claim;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Arrays;

import com.google.gson.Gson;
import java.util.concurrent.CountDownLatch;


public class ProverThread implements Runnable  {
    private String[] verifierResponse;
    private String verifierStatus;
    private Claim claim;
    private MG_FHE fhe;
    private ClientEndpoint proofEndpoint = new ClientEndpoint();
    private ClientEndpoint responseEndpoint = new ClientEndpoint();
    private Gson gson = new Gson();
    private CountDownLatch latch;

    public ProverThread(String host, Claim claim, MG_FHE fhe, String type) {

        this.claim = claim;
        this.fhe = fhe;
        latch = new CountDownLatch(1);
        proofEndpoint.createWebSocketClient("ws://"+host+"/verifySIN");
        responseEndpoint.createWebSocketClient("ws://"+host+"/response");

    }


    @Override
    public void run() {
        try {
            ProofParameters proofParameters = new ProofParameters(claim, fhe);
            proofEndpoint.response = gson.toJson(proofParameters);
            proofEndpoint.webSocketClient.connect();
            proofEndpoint.latch.await();
            MG_FHE.MG_Cipher[] P = gson.fromJson(proofEndpoint.response, MG_FHE.MG_Cipher[].class);
            proofEndpoint.webSocketClient.close();
            BigInteger [] R = new BigInteger[1000];
            for (int k = 0; k < 1000; k++) {
                R[k] = fhe.decrypt(P[k]);
            }
            responseEndpoint.response = gson.toJson(R);
            responseEndpoint.webSocketClient.connect();
            responseEndpoint.latch.await();
            verifierResponse = gson.fromJson(responseEndpoint.response, String[].class);
            responseEndpoint.webSocketClient.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public String[] getVerifierResponse(){
        return verifierResponse;
    }
    public String getVerifierStatus(){
        return verifierStatus;
    }
}
