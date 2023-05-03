package com.loginid.cryptodid.claimVerifier;

import android.util.Log;

import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.ProverThread;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.utils.Status;

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

    public VerificationStatus verifyVCsRentalHouse()  throws InterruptedException, ParseException, IOException, ClassNotFoundException{
      //  return CompletableFuture.runAsync(() -> {
            if (!vcs.isEmpty()) {

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
                    /*
                    if (balanceVerification.isAlive() || ageVerification.isAlive() || creditScoreVerification.isAlive()) {
                        System.out.println("VThread"+ "One or more verification threads are still running");
                    } else {
                        System.out.println("VThread"+ "All verification threads have completed");
                    }

                     */
                        System.out.println("VThread" + "Starting verification");
                    balanceVerification.join();
                    ageVerification.join();
                    creditScoreVerification.join();
                        /*


                        CompletableFuture<Void> balanceVerification = CompletableFuture.runAsync(balanceProverThread);
                        CompletableFuture<Void> ageVerification = CompletableFuture.runAsync(ageProverThread);
                        CompletableFuture<Void> creditScoreVerification = CompletableFuture.runAsync(creditScoreProverThread);
                        CompletableFuture.allOf(balanceVerification, ageVerification, creditScoreVerification).join();

*/
                        try {
//
                            String[] finalResponse = new String[]{"user.firstname", "user.lastname", "user.address", "user.username", "user.phone", "Maroc", ageProverThread.getVerifierResponse()[2].toString(), balanceProverThread.getVerifierResponse()[2].toString(), creditScoreProverThread.getVerifierResponse()[2].toString()};
                            finalResponseEndpoint.response = gson.toJson(finalResponse);
                            finalResponseEndpoint.webSocketClient.connect();
                            return new VerificationStatus("VERIFIED WITH SUCCESS", Status.SUCCESS);

                        } catch (Exception e) {
                            e.printStackTrace();
                            return new VerificationStatus("OOOPS SOMETHING WENT WRONG WHILE VERIFICATION", Status.ERROR);
                        }finally {
                            finalResponseEndpoint.webSocketClient.close();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        return new VerificationStatus("OOOPS SOMETHING WENT WRONG WHILE VERIFICATION", Status.ERROR);
                    }
                } else {
                    return new VerificationStatus("PLEASE VERIFY THE QR CODE", Status.ERROR);
                }
            }else{
                return new VerificationStatus("AT LEAST ONE VC", Status.ERROR);
            }
      //  });
    }


    public String toString(){
        return "" + this.url + " | " + this.vc.toString();
    }
}

