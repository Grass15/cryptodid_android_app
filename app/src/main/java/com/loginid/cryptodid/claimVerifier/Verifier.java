package com.loginid.cryptodid.claimVerifier;

import android.content.DialogInterface;
import android.os.Environment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.loginid.cryptodid.DbDriver;
import com.loginid.cryptodid.MainActivity;
import com.loginid.cryptodid.R;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.model.User;
import com.loginid.cryptodid.protocols.Issuer;
import com.loginid.cryptodid.protocols.MG_FHE;
import com.loginid.cryptodid.protocols.ProverThread;
import com.loginid.cryptodid.scanner.QrDecoder;
import com.loginid.cryptodid.scanner.Scanner;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import com.google.gson.Gson;


public class Verifier {

    private int verifierPort;
//    private String verifierUrl = "192.168.11.100:8080";
    private String verifierUrl = "192.168.1.10:8080";
    //private String verifierUrl = "cryptodid.herokuapp.com";
    private ClientEndpoint finalResponseEndpoint = new ClientEndpoint();
    private ClientEndpoint ageProofEndpoint = new ClientEndpoint();

    private Gson gson = new Gson();

    private Fragment callerFragment;
    MG_FHE fhe = new MG_FHE(11,512);
    private Scanner scanner;
    private String tmp;

    private ActivityResultLauncher<ScanOptions> barLauncher;

    public Verifier(Fragment callerFragment){
        this.callerFragment = callerFragment;
        this.scanner = new Scanner(callerFragment);
        this.barLauncher = callerFragment.registerForActivityResult(new ScanContract(), result -> {
            if (result != null && result.getContents() != null) {
                this.verifierUrl = result.getContents();
                AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
                builder.setTitle("Authorisation Required");
                builder.setMessage("Do you agree to share these information:\nFirstname, Lastname, Address, phone, email?");
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
                        try {
                            verify();
                        } catch (InterruptedException | ParseException | IOException |
                                 ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
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
        proofEndpoint.createWebSocketClient("ws://" + verifierUrl +"/"+ attribute+ "Proof");

        proofEndpoint.webSocketClient.connect();
        proofEndpoint.latch.await();
        proofEndpoint.sendFile(path+"/"+attribute+"Cloud.key", "cloud.key");
        proofEndpoint.sendFile(path+"/"+attribute+"Cloud.data", "cloud.data");
        proofEndpoint.sendFile(path+"/"+attribute+"PK.key", "PK.key");
        proofEndpoint.webSocketClient.send("DONE");
        proofEndpoint.latch.await();
        response = Decrypt(path+"/Answer.data", path+"/"+attribute+"Keyset.key");
        proofEndpoint.webSocketClient.close();
        return response;
    }
    public void test() throws ParseException, IOException, InterruptedException, ClassNotFoundException {
        int creditScoreStatus = verify("creditScore");
        int ageStatus = verify("age");
        int balanceStatus = verify("balance");
        AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
        builder.setTitle("Verification");
        builder.setMessage(" "+tmp);
        finalResponseEndpoint.createWebSocketClient("ws://" + verifierUrl + "/finalResponse");
        String[] finalResponse = new String[]{"Fabien", "KORGO", "Casablanca", "kograss20@gmail.com", "+212 62606103", "Maroc", String.valueOf(ageStatus != 0), String.valueOf(balanceStatus != 0), String.valueOf(creditScoreStatus != 0)};
        finalResponseEndpoint.webSocketClient.connect();
        finalResponseEndpoint.latch.await();
        finalResponseEndpoint.webSocketClient.send(gson.toJson(finalResponse));
        finalResponseEndpoint.webSocketClient.close();
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }


    public void verify() throws InterruptedException, ParseException, IOException, ClassNotFoundException {
        if (!Objects.equals(verifierUrl, "")) {
            finalResponseEndpoint.createWebSocketClient("ws://" + verifierUrl + "/finalResponse");
            Claim balanceClaim;
            Claim creditScoreClaim;
            Claim ageClaim;
            try {
                balanceClaim = MainActivity.driver.getClaimsFromACertainType("Balance").get(0);
                creditScoreClaim = MainActivity.driver.getClaimsFromACertainType("Credit Score").get(0);
                ageClaim = MainActivity.driver.getClaimsFromACertainType("Age").get(0);
                ProverThread balanceProverThread = new ProverThread(verifierUrl, balanceClaim, balanceClaim.getFhe(), "balance");
                ProverThread ageProverThread = new ProverThread(verifierUrl, ageClaim, ageClaim.getFhe(), "age");
                ProverThread creditScoreProverThread = new ProverThread(verifierUrl, creditScoreClaim, creditScoreClaim.getFhe(), "creditScore");
                Thread balanceVerification = new Thread(balanceProverThread);
                Thread ageVerification = new Thread(ageProverThread);
                Thread creditScoreVerification = new Thread(creditScoreProverThread);
                balanceVerification.start();
                ageVerification.start();
                creditScoreVerification.start();
                balanceVerification.join();
                ageVerification.join();
                creditScoreVerification.join();
                AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
                builder.setTitle("Verification Status");
                try {
//                    builder.setMessage("Something went wrong");
                    builder.setMessage(balanceProverThread.getVerifierStatus() + "\nBalance: " + balanceProverThread.getVerifierResponse()[1] + "\n\nCredit Score: " + creditScoreProverThread.getVerifierResponse()[1] + "\n\nAge: " + ageProverThread.getVerifierResponse()[1]);
                    User user = MainActivity.driver.getUser();
                    String[] finalResponse = new String[]{user.firstname, user.lastname, user.address, user.username, user.phone, "Maroc", ageProverThread.getVerifierResponse()[2], balanceProverThread.getVerifierResponse()[2], creditScoreProverThread.getVerifierResponse()[2]};
                    finalResponseEndpoint.response = gson.toJson(finalResponse);
                    finalResponseEndpoint.webSocketClient.connect();
                    finalResponseEndpoint.webSocketClient.close();
                } catch (Exception e) {
                    builder.setMessage("Something went wrong");
                }
                //builder.setMessage(balanceProverThread.getVerifierStatus()+"\n"+balanceProverThread.getVerifierResponse()[1]);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

            } catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.callerFragment.getView().getContext());
                builder.setTitle("Missing verifiable credential");
                builder.setMessage("At least One verifiable credential are missing");
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
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




}
