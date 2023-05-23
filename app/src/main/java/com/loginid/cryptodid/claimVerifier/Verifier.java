package com.loginid.cryptodid.claimVerifier;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.loginid.cryptodid.presentation.MainActivity;
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
    public List<String> userPres = new ArrayList<>();

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
    public void AddUserPresentation(List<String> data){
        if(data != null){
            this.userPres.addAll(data);
        }
    }

    public native int Decrypt(String ClaimPath, String SK_Path);

    public int verify(String attribute) throws InterruptedException, ParseException, IOException, ClassNotFoundException {
        String path = String.valueOf(MainActivity.getFilesFolder());

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
        System.out.println("signal");
        if (Objects.equals(attribute, "sin")){
            response = decryptAccessControlResult(path+"/Answer.data", path+"/"+attribute+"Keyset.key");
        }else{
            response = Decrypt(path+"/Answer.data", path+"/"+attribute+"Keyset.key");
        }
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
            //if (!vcs.isEmpty()) {

                if (!Objects.equals(this.url, "")) {
                    try{
                    finalResponseEndpoint.createWebSocketClient("ws://" + this.url + "/finalResponse");
                    getcppUrlEndpoint.createWebSocketClient("ws://" + this.url + "/cppUrl");
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
                            String[] tempData = {"","",""};
                            if(userPres.isEmpty()){
                                tempData[0] = "FabienK@team.loginid";
                                tempData[1] = "fabien";
                                tempData[2] = "korgo";
                            }else{
                                int i = 0;
                                for(String d : this.userPres){
                                    tempData[i] = d;
                                    i++;
                                }
                            }
//
                            String[] finalResponse = new String[]{tempData[1], tempData[2], "Rabat", tempData[0], "+212666068102", "Maroc", String.valueOf(ageStatus != 0), String.valueOf(balanceStatus != 0), String.valueOf(creditScoreStatus != 0)};
                            finalResponseEndpoint.webSocketClient.connect();
                            finalResponseEndpoint.latch.await();
                            finalResponseEndpoint.webSocketClient.send(gson.toJson(finalResponse));
                            finalResponseEndpoint.webSocketClient.close();
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
//            }else{
//                return new VerificationStatus("AT LEAST ONE VC", Status.ERROR);
//            }
      //  });
    }

    public native int decryptAccessControlResult(String ClaimPath, String SK_Path);
    public VerificationStatus accessControl()  throws InterruptedException, ParseException, IOException, ClassNotFoundException{
        if (!Objects.equals(this.url, "")) {
            try{
                getcppUrlEndpoint.createWebSocketClient("ws://" + this.url + "/cppUrl");
                getcppUrlEndpoint.latch = new CountDownLatch(2);
                getcppUrlEndpoint.webSocketClient.connect();
                getcppUrlEndpoint.latch.await();
                cppVerifierUrl = String.valueOf(getcppUrlEndpoint.response);
                getcppUrlEndpoint.webSocketClient.close();
                int sinStatus = verify("sin");
                finalResponseEndpoint.createWebSocketClient("ws://" + this.url + "/response");
                finalResponseEndpoint.webSocketClient.connect();
                finalResponseEndpoint.latch.await();
                finalResponseEndpoint.latch = new CountDownLatch(1);
                finalResponseEndpoint.webSocketClient.send(gson.toJson(sinStatus));
                finalResponseEndpoint.latch.await();
                String[] finalResponse = gson.fromJson(finalResponseEndpoint.response, String[].class);
                finalResponseEndpoint.webSocketClient.close();
                return new VerificationStatus(finalResponse[0] + " " + finalResponse[1], Status.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return new VerificationStatus("OOOPS SOMETHING WENT WRONG WHILE VERIFICATION", Status.ERROR);
            }
        } else {
            return new VerificationStatus("PLEASE VERIFY THE QR CODE", Status.ERROR);
        }
    }

    public native int decryptVotingResult(String ClaimPath, String SK_Path, int nbit);
    public VerificationStatus verifyVoting() throws InterruptedException, ParseException, IOException, ClassNotFoundException{
        String path = String.valueOf(MainActivity.getFilesFolder());
        int x1=1;
        int y1=1;
        int z1=1;

        ClientEndpoint proofEndpoint = new ClientEndpoint();
        proofEndpoint.createWebSocketClient("ws://" + this.url);
        //proofEndpoint.latch.await();
        proofEndpoint.webSocketClient.connect();
        proofEndpoint.latch.await();
        proofEndpoint.latch = new CountDownLatch(1);
        proofEndpoint.pos = 0;
        proofEndpoint.webSocketClient.send("VOTING");
        proofEndpoint.sendFile(path + "/CK.key", "CK.key");
        proofEndpoint.sendFile(path + "/CD.data", "CD.data");
        proofEndpoint.sendFile(path + "/PK.key", "PK.key");
        int A = (x1*x1*x1) + (y1*y1*y1) + (z1*z1*z1);
        proofEndpoint.webSocketClient.send(String.valueOf(A));
        //proofEndpoint.webSocketClient.send("DONE");
        proofEndpoint.latch.await();
        int r = decryptVotingResult(path+"/vot_answer.data", path+"/SK.key",1);
        int n = decryptVotingResult(path+"/H_NULL.data", path+"/SK.key",4);
        System.out.println("r:"+r);
        System.out.println("null:"+n);
        proofEndpoint.webSocketClient.send(String.valueOf(r));
        proofEndpoint.webSocketClient.send(String.valueOf(n));

        proofEndpoint.webSocketClient.close();
           return new VerificationStatus("Verified with success", Status.SUCCESS);
    }


    public String toString(){
        return "" + this.url + " | " + this.vc.toString();
    }
}

