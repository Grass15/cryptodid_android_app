package com.loginid.cryptodid.protocols;


import com.google.gson.Gson;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.claimVerifier.ClientEndpoint;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;


public class ProverThread implements Runnable  {
    private String[] verifierResponse;
    private String verifierStatus;
    private Claim claim;
    private MG_FHE fhe;
    private ClientEndpoint proofEndpoint = new ClientEndpoint();
    private ClientEndpoint isGreaterEndpoint = new ClientEndpoint();
    private ClientEndpoint merkleTreeEndpoint = new ClientEndpoint();
    private Gson gson = new Gson();
    private CountDownLatch latch;

    public ProverThread(String host, Claim claim, MG_FHE fhe, String type) {

        this.claim = claim;
        this.fhe = fhe;
        latch = new CountDownLatch(1);
        proofEndpoint.createWebSocketClient("ws://"+host+"/" + type +"Proof");
        isGreaterEndpoint.createWebSocketClient("ws://"+host+"/isGreater");
        merkleTreeEndpoint.createWebSocketClient("ws://"+host+"/setMerkleTree");

    }


    @Override
    public void run() {
        try {
            ProofParameters proofParameters = new ProofParameters(claim, fhe);
            proofEndpoint.response = gson.toJson(proofParameters);
            proofEndpoint.webSocketClient.connect();
            proofEndpoint.latch.await();
            Proof proof = gson.fromJson(proofEndpoint.response, Proof.class);
            proofEndpoint.webSocketClient.close();
            for (int i = 0; i < 1000; i++) {
                BigInteger A = fhe.decrypt(proof.L[i]);
                if ((A.mod(new BigInteger("2", 10))).intValue() == 1) {
                    proof.verification[i] = true;
                } else {
                    proof.verification[i] = false;
                }
            }
            /* Generate a Merkle Tree Commitment on the value of L*/


            int hash = Arrays.hashCode(proof.verification); //To make sure prover doesn't cheat
            //int proof_index = verifier.getProofIndex(hash);
            //Verify circuit
            MG_FHE.MG_Cipher [] base = new MG_FHE.MG_Cipher[8];
            for (int i = 0; i < 8; i++) {
                base[i] = proof.base[i];
            }
            MG_FHE.MG_Cipher er_verify = fhe.ZERO;
            IsGreaterParameters isGreaterParameters = new IsGreaterParameters(claim.ciphers, base, 7, fhe);
            isGreaterEndpoint.response = gson.toJson(isGreaterParameters);
            isGreaterEndpoint.webSocketClient.connect();
            isGreaterEndpoint.latch.await();
            er_verify = gson.fromJson(isGreaterEndpoint.response, MG_FHE.MG_Cipher.class) ; //er is is true if odd, false if even
            isGreaterEndpoint.webSocketClient.close();
            er_verify = er_verify.add(proof.CK, fhe.h, fhe.N);
            /* 3. Request Verifier to verify claim (for example age > 18) */

            if(er_verify.is_equal(proof.L[proof.proof_index])) {
                verifierStatus = "Verifier is Honest";
            } else {
                verifierStatus = "Verifier is Dishonest";
            }
            SetMerkleTreeParameters setMerkleTreeParameters = new SetMerkleTreeParameters(proof.verification, hash, proof.proof_index);
            merkleTreeEndpoint.response = gson.toJson(setMerkleTreeParameters);
            merkleTreeEndpoint.webSocketClient.connect();
            merkleTreeEndpoint.latch.await();
            verifierResponse = gson.fromJson(merkleTreeEndpoint.response, String[].class);
            merkleTreeEndpoint.webSocketClient.close();
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
