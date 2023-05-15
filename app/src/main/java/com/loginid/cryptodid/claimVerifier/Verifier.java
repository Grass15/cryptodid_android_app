package com.loginid.cryptodid.claimVerifier;

import android.content.DialogInterface;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.loginid.cryptodid.MainActivity;
import com.loginid.cryptodid.scanner.Scanner;

import android.widget.Toast;


import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;


public class Verifier {
    private int verifierPort;
    //private String verifierUrl = "192.168.1.9:8080";
    private String cppVerifierUrl = "192.168.11.102:8080";
    private String javaVerifierUrl = "192.168.11.100:8080";
    //private String verifierUrl = "cryptodid.herokuapp.com";
    private ClientEndpoint finalResponseEndpoint = new ClientEndpoint();
    private ClientEndpoint verificationEndpoint = new ClientEndpoint();
    private ClientEndpoint proofEndpoint = new ClientEndpoint();

    private Gson gson = new Gson();

    private Fragment callerFragment;

    private Scanner scanner;

    private ActivityResultLauncher<ScanOptions> barLauncher;

    public void showToast() {
        Toast.makeText(this.callerFragment.getActivity(), "Finger Print identified", Toast.LENGTH_SHORT).show();
    }

    public Verifier(Fragment callerFragment){
        this.callerFragment = callerFragment;
        this.scanner = new Scanner(callerFragment);
        this.barLauncher = callerFragment.registerForActivityResult(new ScanContract(), result -> {
            if (result != null && result.getContents() != null) {
                this.javaVerifierUrl = result.getContents();
                AlertDialog.Builder fpBuilder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
                fpBuilder.setTitle("Biometric");
                fpBuilder.setMessage("Fingerprint successfully identified");
                AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
                builder.setTitle("Authorisation Required");
                builder.setMessage("Do you agree to authorized Crytodid Stadium to access your SIN Verifiable Credential?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        fpBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                try {
                                    test();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }).show();
                    }
                }).show();
            }
        });
    }
    public void verifyClaim() throws InterruptedException, ParseException, IOException, ClassNotFoundException {
        Thread scan = new Thread() {
            public void run() {
                scanner.scanCode();
            }
        };
        scan.start();
        scan.join();
        this.barLauncher.launch(scanner.options);
    }

    public native int Decrypt(String ClaimPath, String SK_Path);

    public int verify(String attribute) throws InterruptedException, ParseException, IOException, ClassNotFoundException {
        String path = String.valueOf(MainActivity.path);
        int response;
        ClientEndpoint proofEndpoint = new ClientEndpoint();
//        proofEndpoint.createWebSocketClient("ws://" + verifierUrl +"/"+ attribute+ "Proof");
        proofEndpoint.createWebSocketClient("ws://" + cppVerifierUrl);
        //proofEndpoint.latch.await();
        proofEndpoint.webSocketClient.connect();
        proofEndpoint.latch.await();
        proofEndpoint.latch = new CountDownLatch(1);
        proofEndpoint.webSocketClient.send(attribute);
        proofEndpoint.sendFile(path+"/"+attribute+"Cloud.key", attribute+ "Cloud.key");
        proofEndpoint.sendFile(path+"/"+attribute+"Cloud.data", attribute+ "Cloud.data");
        proofEndpoint.sendFile(path+"/"+attribute+"PK.key", attribute+ "PK.key");
//        proofEndpoint.webSocketClient.send("DONE");
        proofEndpoint.latch.await();
        response = Decrypt(path+"/Answer.data", path+"/"+attribute+"Keyset.key");
        proofEndpoint.webSocketClient.close();
        System.out.println(attribute + ": " + response);
        return response;
    }

    public String getResultText(boolean status){
        if(status){
            return "You are authorized to access the building";
        }else{
            return "You are NOT authorized to access the building";
        }
    }

    public void test() throws ParseException, IOException, InterruptedException, ClassNotFoundException {
        if (!Objects.equals(javaVerifierUrl, "")) {
            verificationEndpoint.createWebSocketClient("ws://" + javaVerifierUrl + "/cppUrl");
            verificationEndpoint.latch = new CountDownLatch(2);
            verificationEndpoint.webSocketClient.connect();
            verificationEndpoint.latch.await();
            cppVerifierUrl = String.valueOf(verificationEndpoint.response);
            verificationEndpoint.webSocketClient.close();
            int sinStatus = verify("sin");
            AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
            builder.setTitle("Verification");
            //builder.setMessage("\nBalance: " + getResultText(balanceStatus != 0) + "\n\nCredit Score: " + getResultText(creditScoreStatus != 0) + "\n\nAge: " + getResultText(ageStatus != 0));
            finalResponseEndpoint.createWebSocketClient("ws://" + javaVerifierUrl + "/response");

            //String[] finalResponse = new String[]{user.firstname, user.lastname, user.address, user.username, user.phone, "Maroc", "Maroc", String.valueOf(ageStatus != 0), String.valueOf(balanceStatus != 0), String.valueOf(creditScoreStatus != 0)};
            finalResponseEndpoint.webSocketClient.connect();
            finalResponseEndpoint.latch.await();
            finalResponseEndpoint.latch = new CountDownLatch(1);
            finalResponseEndpoint.webSocketClient.send(gson.toJson(sinStatus));
            finalResponseEndpoint.latch.await();
            String[] finalResponse = gson.fromJson(finalResponseEndpoint.response, String[].class);
            finalResponseEndpoint.webSocketClient.close();
            builder.setMessage(finalResponse[0] +"\n\n"+ finalResponse[1]);
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
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



//    public void verify() throws InterruptedException, ParseException, IOException, ClassNotFoundException {
//        if (!Objects.equals(verifierUrl, "")) {
//            Claim sinVC;
//            try {
//                sinVC = MainActivity.driver.getClaimsFromACertainType("SIN").get(0);
//                ProverThread SINProverThread = new ProverThread(verifierUrl, sinVC, sinVC.getFhe(), "SIN");
//                Thread SINVerification = new Thread(SINProverThread);
//                SINVerification.start();
//                SINVerification.join();
//                AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
//                builder.setTitle("Verification Status");
//                try {
////                    builder.setMessage("Something went wrong");
//                    builder.setMessage(SINProverThread.getVerifierResponse()[1]);
//
//                } catch (Exception e) {
//                    builder.setMessage("Something went wrong");
//                }
//                //builder.setMessage(balanceProverThread.getVerifierStatus()+"\n"+balanceProverThread.getVerifierResponse()[1]);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).show();
//
//            } catch (Exception e) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
//                builder.setTitle("Missing verifiable credential");
//                builder.setMessage("At least One verifiable credential are missing");
//                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).show();
//            }
//        }else{
//            AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
//            builder.setTitle("Error");
//            builder.setMessage("Please Ensure you scanned the good QR ");
//            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            }).show();
//        }
//    }




}
