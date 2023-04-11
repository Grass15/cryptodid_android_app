package com.loginid.cryptodid.claimVerifier;

import android.app.ProgressDialog;
import android.content.DialogInterface;

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
import android.view.View;
import android.widget.ProgressBar;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.google.gson.Gson;


public class Verifier {
    private int verifierPort;
    //private String verifierUrl = "192.168.11.102:8080";
    private String verifierUrl = "";
    private ClientEndpoint finalResponseEndpoint = new ClientEndpoint();

    private Gson gson = new Gson();

    private Fragment callerFragment;
    MG_FHE fhe = new MG_FHE(11,512);
    private Scanner scanner;

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
