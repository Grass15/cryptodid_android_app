package com.loginid.cryptodid.claimVerifier;


import androidx.compose.animation.ExperimentalAnimationApi;

import com.google.gson.Gson;
import com.loginid.cryptodid.R;
import com.loginid.cryptodid.data.local.entity.VCEntity;
import com.loginid.cryptodid.model.SignatureVerificationParameters;
import com.loginid.cryptodid.presentation.MainActivity;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.utils.Status;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;



public class Verifier {

    private ClientEndpoint getcppUrlEndpoint = new ClientEndpoint();
    private String cppVerifierUrl = "";
    private String javaVerifierUrl = "";
    //private String url = "";
    private Claim vc;
    private ClientEndpoint finalResponseEndpoint = new ClientEndpoint();
    private Gson gson = new Gson();
    //public List<Claim> vcs = new ArrayList<Claim>();
    Dictionary<String, VCEntity> vcs= new Hashtable<>();
    public List<String> userPres = new ArrayList<>();
    private final String filesFolderPath = String.valueOf(MainActivity.getFilesFolder());
    X509Certificate x509certificate;
    byte[] certificateBytes;
    PrivateKey privateKey;


    public Verifier(
    ) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance("BKS");

        InputStream inputStream = MainActivity.getActivityContext().getResources().openRawResource(R.raw.keystore);
        keystore.load(inputStream, "loginid".toCharArray());
        // Get the private key and certificate from the keystore
        String alias = "myalias";
        String keyPass = "loginid";
        privateKey = (PrivateKey) keystore.getKey(alias, keyPass.toCharArray());
        x509certificate = (X509Certificate) keystore.getCertificate(alias);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(x509certificate);
        certificateBytes = baos.toByteArray();
    }
//    public Verifier(String url) {
//        this.url = url;
//    }
//
//    public Verifier(String url, List<Claim> vcs) {
//        this.url = url;
//        this.vcs = vcs;
//    }

//    public Verifier(String url, Claim vc) {
//        this.url = url;
//        this.vc = vc;
//    }
    public void setUrl(String url) {
        this.javaVerifierUrl = url;
    }

    public void setVc(Claim vc) {
        this.vc = vc;
    }

    public void AppendVC(String type, VCEntity vc){
        this.vcs.put(type, vc);
    }

//    public void AppendVCs(List<Claim> vcs){
//        if(vcs != null)
//           this.vcs.addAll(vcs);
//    }
    public void AddUserPresentation(List<String> data){
        if(data != null){
            this.userPres.addAll(data);
        }
    }

    public native int Decrypt(String ClaimPath, String SK_Path);

    public int verify(VCEntity vc) throws Exception {
        int response = 0;
        ClientEndpoint proofEndpoint = new ClientEndpoint();
        cppVerifierUrl = verifySignatureAndGetCppUrl(vc);
        String type = Objects.requireNonNull(vc.getVc()).getType();

        if(!Objects.equals(cppVerifierUrl, "") && cppVerifierUrl != null){
            proofEndpoint.createWebSocketClient("ws://" + cppVerifierUrl);
            proofEndpoint.webSocketClient.connect();
            proofEndpoint.latch.await();
            proofEndpoint.latch = new CountDownLatch(1);
            proofEndpoint.webSocketClient.send(type);
            proofEndpoint.sendFile(filesFolderPath+"/"+type+"Cloud.key", type+ "Cloud.key");
            proofEndpoint.sendFile(filesFolderPath+"/"+type+"Cloud.data", type+ "Cloud.data");
            proofEndpoint.sendFile(filesFolderPath+"/"+type+"PK.key", type+ "PK.key");
            proofEndpoint.latch.await();
            System.out.println("signal");
            if (Objects.equals(type, "sin")){
                response = decryptAccessControlResult(filesFolderPath+"/Answer.data", filesFolderPath+"/"+type+"Keyset.key");
            }else{
                response = Decrypt(filesFolderPath+"/Answer.data", filesFolderPath+"/"+type+"Keyset.key");
            }
            proofEndpoint.webSocketClient.close();
            System.out.println(type + ": " + response);
        }else {
            System.out.println("Signature verification failed");
        }
        return response;
    }

    public String verifySignatureAndGetCppUrl(VCEntity vc) throws Exception {
        Claim vcData = vc.getVc();
        SignatureVerificationParameters signatureVerificationParameters= new SignatureVerificationParameters(vcData.getEncryptedAttribute(), vcData.getSignature(), certificateBytes, vcData.getRevocationNonce(), vc.getVersion());
        ClientEndpoint signatureVerificationEndpoint = new ClientEndpoint();
        signatureVerificationEndpoint.createWebSocketClient("ws://" + this.javaVerifierUrl + "/signature");
        //signatureVerificationEndpoint.createWebSocketClient("ws://" + cppVerifierUrl);
        signatureVerificationEndpoint.webSocketClient.connect();
        signatureVerificationEndpoint.latch.await();
        signatureVerificationEndpoint.latch = new CountDownLatch(1);
        signatureVerificationEndpoint.webSocketClient.send(gson.toJson(signatureVerificationParameters));
        signatureVerificationEndpoint.latch.await();
        signatureVerificationEndpoint.webSocketClient.close();
        //signatureVerificationEndpoint.sendFile(filesFolderPath+"/"+vcEncryptionFileName, vcEncryptionFileName);
        return signatureVerificationEndpoint.response;

    }
    public String getResultText(boolean status){
        if(status){
            return "Verification positive for this attribute";
        }else{
            return "Verification negative for this attribute";
        }
    }

    public VerificationStatus verifyVCsRentalHouse() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
      //  return CompletableFuture.runAsync(() -> {
            if (!vcs.isEmpty()) {

                if (!Objects.equals(this.javaVerifierUrl, "")) {

                    try{
                    finalResponseEndpoint.createWebSocketClient("ws://" + this.javaVerifierUrl + "/finalResponse");
                    getcppUrlEndpoint.createWebSocketClient("ws://" + this.javaVerifierUrl + "/cppUrl");
                    getcppUrlEndpoint.latch = new CountDownLatch(2);
                    getcppUrlEndpoint.webSocketClient.connect();
                    getcppUrlEndpoint.latch.await();
                    cppVerifierUrl = String.valueOf(getcppUrlEndpoint.response);
                    getcppUrlEndpoint.webSocketClient.close();
                    int creditScoreStatus = verify(vcs.get("creditScore") );
                    int ageStatus = verify(vcs.get("age"));
                    int balanceStatus = verify(vcs.get("balance"));
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
            }else{
               return new VerificationStatus("AT LEAST ONE VC", Status.ERROR);
           }
      //  });
    }

    public native int decryptAccessControlResult(String ClaimPath, String SK_Path);
    public VerificationStatus accessControl()  throws InterruptedException, ParseException, IOException, ClassNotFoundException{
        if (!Objects.equals(this.javaVerifierUrl, "")) {
            try{
                getcppUrlEndpoint.createWebSocketClient("ws://" + this.javaVerifierUrl + "/cppUrl");
                getcppUrlEndpoint.latch = new CountDownLatch(2);
                getcppUrlEndpoint.webSocketClient.connect();
                getcppUrlEndpoint.latch.await();
                cppVerifierUrl = String.valueOf(getcppUrlEndpoint.response);
                getcppUrlEndpoint.webSocketClient.close();
                int sinStatus = verify(vcs.get("sin"));
                finalResponseEndpoint.createWebSocketClient("ws://" + this.javaVerifierUrl + "/sinResponse");
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

        int x1=1;
        int y1=1;
        int z1=1;

        ClientEndpoint proofEndpoint = new ClientEndpoint();
        proofEndpoint.createWebSocketClient("ws://" + this.javaVerifierUrl);
        //proofEndpoint.latch.await();
        proofEndpoint.webSocketClient.connect();
        proofEndpoint.latch.await();
        proofEndpoint.latch = new CountDownLatch(1);
        proofEndpoint.pos = 0;
        proofEndpoint.webSocketClient.send("VOTING");
        proofEndpoint.sendFile(filesFolderPath + "/CK.key", "CK.key");
        proofEndpoint.sendFile(filesFolderPath + "/CD.data", "CD.data");
        proofEndpoint.sendFile(filesFolderPath + "/PK.key", "PK.key");
        int A = (x1*x1*x1) + (y1*y1*y1) + (z1*z1*z1);
        proofEndpoint.webSocketClient.send(String.valueOf(A));
        //proofEndpoint.webSocketClient.send("DONE");
        proofEndpoint.latch.await();
        int r = decryptVotingResult(filesFolderPath+"/vot_answer.data", filesFolderPath+"/SK.key",1);
        int n = decryptVotingResult(filesFolderPath+"/H_NULL.data", filesFolderPath+"/SK.key",4);
        System.out.println("r:"+r);
        System.out.println("null:"+n);
        proofEndpoint.webSocketClient.send(String.valueOf(r));
        proofEndpoint.webSocketClient.send(String.valueOf(n));

        proofEndpoint.webSocketClient.close();
           return new VerificationStatus("Verified with success", Status.SUCCESS);
    }


    public String toString(){
        return "" + this.javaVerifierUrl + " | " + this.vc.toString();
    }

    public static byte[] signClaim(byte[] vcEncryptedData, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(vcEncryptedData);
        return signature.sign();
    }
}

