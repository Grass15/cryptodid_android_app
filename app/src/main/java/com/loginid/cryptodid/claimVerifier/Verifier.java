package com.loginid.cryptodid.claimVerifier;

import android.util.Log;

import com.google.gson.Gson;
import com.loginid.cryptodid.presentation.MainActivity;
import com.loginid.cryptodid.protocols.ProverThread;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.utils.Status;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class Verifier {

    private ClientEndpoint getcppUrlEndpoint = new ClientEndpoint();
    private String cppVerifierUrl = "";
    private String javaVerifierUrl = "";
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

    public native int Decrypt(String ClaimPath, String SK_Path);

    public int verify(String attribute) throws InterruptedException, ParseException, IOException, ClassNotFoundException {
        String path = String.valueOf(MainActivity.getFilesDir());
        int response;
        ClientEndpoint proofEndpoint = new ClientEndpoint();
        proofEndpoint.createWebSocketClient("ws://" + cppVerifierUrl);
        proofEndpoint.webSocketClient.connect();
        proofEndpoint.latch.await();
        proofEndpoint.latch = new CountDownLatch(1);
        proofEndpoint.webSocketClient.send(attribute);
        proofEndpoint.sendFile(path+"/"+attribute+"Cloud.key", attribute+ "Cloud.key");
        proofEndpoint.sendFile(path+"/"+attribute+"Cloud.data", attribute+ "Cloud.data");
        proofEndpoint.sendFile(path+"/"+attribute+"PK.key", attribute+ "PK.key");
        proofEndpoint.latch.await();
        response = Decrypt(path+"/Answer.data", path+"/"+attribute+"Keyset.key");
        proofEndpoint.webSocketClient.close();
        System.out.println(attribute + ": " + response);
        return response;
    }
    public String getResultText(boolean status){
        if(status){
            return "Verification positive for this attribute";
        }else{
            return "Verification negative for this attribute";
        }
    }

    public VerificationStatus verifyVCsRentalHouse()  throws InterruptedException, ParseException, IOException, ClassNotFoundException{
      //  return CompletableFuture.runAsync(() -> {
            if (!vcs.isEmpty()) {

                if (!Objects.equals(this.url, "")) {
                    try{
                    finalResponseEndpoint.createWebSocketClient("ws://" + this.url + "/finalResponse");
                    getcppUrlEndpoint.createWebSocketClient("ws://" + javaVerifierUrl + "/cppUrl");
                    getcppUrlEndpoint.latch = new CountDownLatch(2);
                    getcppUrlEndpoint.webSocketClient.connect();
                    getcppUrlEndpoint.latch.await();
                    cppVerifierUrl = String.valueOf(getcppUrlEndpoint.response);
                    getcppUrlEndpoint.webSocketClient.close();
                    int creditScoreStatus = verify("creditScore");
                    int ageStatus = verify("age");
                    int balanceStatus = verify("balance");
                        /*


                        CompletableFuture<Void> balanceVerification = CompletableFuture.runAsync(balanceProverThread);
                        CompletableFuture<Void> ageVerification = CompletableFuture.runAsync(ageProverThread);
                        CompletableFuture<Void> creditScoreVerification = CompletableFuture.runAsync(creditScoreProverThread);
                        CompletableFuture.allOf(balanceVerification, ageVerification, creditScoreVerification).join();

*/
                        try {
//
                            String[] finalResponse = new String[]{"user.firstname", "user.lastname", "user.address", "user.username", "user.phone", "Maroc", String.valueOf(ageStatus != 0), String.valueOf(balanceStatus != 0), String.valueOf(creditScoreStatus != 0)};
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

