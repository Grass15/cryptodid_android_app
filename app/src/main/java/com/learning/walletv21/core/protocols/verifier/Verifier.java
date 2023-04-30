package com.learning.walletv21.core.protocols.verifier;

import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.learning.walletv21.core.protocols.ProverThread;
import com.learning.walletv21.core.protocols.javamodels.Claim;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Verifier {

    private String url = "";
    private Claim vc;
    private ClientEndpoint finalResponseEndpoint = new ClientEndpoint();
    private Gson gson = new Gson();
    public List<Claim> vcs = new ArrayList<Claim>();

    public Verifier(){

    }
    public Verifier(String url) {
        this.url = url;
    }

    public Verifier(String url, List<Claim> vcs) {
        this.url = url;
        this.vcs = vcs;
    }

    public Verifier(String url, Claim vc) {
        this.url = url;
        this.vc = vc;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public void setVc(Claim vc) {
        this.vc = vc;
    }


    public void AppendVC(Claim vc){
        this.vcs.add(vc);
    }

    public void AppendVCs(List<Claim> vcs){
        if(vcs != null)
           this.vcs.addAll(vcs);
    }
    public void verifySingleVC()  throws InterruptedException, ParseException, IOException, ClassNotFoundException{
        if(vc != null){

            if (!Objects.equals(this.url, "")) {
                Log.d("Verifyer","Started Verifier Thread");
                finalResponseEndpoint.createWebSocketClient("ws://" + this.url + "/finalResponse");
                Claim ageClaim;
                Thread ageVerification;
                try {
                    ageClaim = this.vc;
                    ProverThread ageProverThread = new ProverThread(this.url, ageClaim, ageClaim.getFhe(), "age");
                    ageVerification = new Thread(ageProverThread);
                    ageVerification.start();
                    ageVerification.join();

                    try {
                        String[] finalResponse = new String[]{"user.firstname", "user.lastname", "user.address", "user.username", "user.phone", "Maroc"/*, ageProverThread.getVerifierResponse()[2]*/};
                        finalResponseEndpoint.response = gson.toJson(finalResponse);
                        finalResponseEndpoint.webSocketClient.connect();
                        finalResponseEndpoint.webSocketClient.close();
                    } catch (Exception e) {
                        Log.d("Verifyer","Ooops something went wrong");
                    }finally {
                        ageVerification.destroy();

                    }

                } catch (Exception e) {

                    Log.d("Verifyer","At least one credential should be added");


                }
            }else{
                Log.d("Verifyer","Ensure you scanned the QR code");

            }

        }else{

        }
    }
    public void verifyMultipleVCs()  throws InterruptedException, ParseException, IOException, ClassNotFoundException{

        if(!vcs.isEmpty()){

            if (!Objects.equals(this.url, "")) {
                finalResponseEndpoint.createWebSocketClient("ws://" + this.url + "/finalResponse");
                Claim balanceClaim;
                Claim creditScoreClaim;
                Claim ageClaim;
                try {

                    balanceClaim = this.vcs.get(2);
                    creditScoreClaim = this.vcs.get(1);
                    ageClaim = this.vcs.get(0);
                    ProverThread balanceProverThread = new ProverThread(this.url, balanceClaim, balanceClaim.getFhe(), "balance");
                    ProverThread ageProverThread = new ProverThread(this.url, ageClaim, ageClaim.getFhe(), "age");
                    ProverThread creditScoreProverThread = new ProverThread(this.url, creditScoreClaim, creditScoreClaim.getFhe(), "creditScore");
                    Thread balanceVerification = new Thread(balanceProverThread);
                    Thread ageVerification = new Thread(ageProverThread);
                    Thread creditScoreVerification = new Thread(creditScoreProverThread);
                    balanceVerification.start();
                    ageVerification.start();
                    creditScoreVerification.start();
                    if (balanceVerification.isAlive() || ageVerification.isAlive() || creditScoreVerification.isAlive()) {
                        System.out.println("VThread"+ "One or more verification threads are still running");
                    } else {
                        System.out.println("VThread"+ "All verification threads have completed");
                    }
                    /*balanceVerification.join();
                    ageVerification.join();
                    creditScoreVerification.join();*/
                    Log.d("VThread","Starting verification");
                    try {
//
                        String[] finalResponse =new String[]{"user.firstname", "user.lastname", "user.address", "user.username", "user.phone", "Maroc", ageProverThread.getVerifierResponse()[2], balanceProverThread.getVerifierResponse()[2], creditScoreProverThread.getVerifierResponse()[2]};
                        finalResponseEndpoint.response = gson.toJson(finalResponse);
                        finalResponseEndpoint.webSocketClient.connect();
                        finalResponseEndpoint.webSocketClient.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Verifyer","Ooops something went wrong");
                    }

                } catch (Exception e) {
                    Log.d("Verifyer","At least one credential should be added");
                    e.printStackTrace();
                }
            }else{
                Log.d("Verifyer","Ensure you scanned the QR code");
            }



        }
    }


    public String toString(){
        return "" + this.url + " | " + this.vc.toString();
    }
}

