package com.loginid.cryptodid.claimVerifier;

import android.annotation.SuppressLint;

public class TFHECryptho implements CrypthoSystem{


    public int encrypt(int plaintext, String keyname) {
        /**
         * A native method that is implemented by the 'tfhe' native library,
         * which is packaged with this application.
         */
        return EncryptData(plaintext,keyname);
    }
    @SuppressLint("NotConstructor")
    public native int EncryptData(int plaintext, String keyname);
    /**
     * decrypt a ciphertext using TFHE library.
     *
     * @param ciphertext name of the ciphertext that encrypted.
     * @param keyset     Name of the key used for decryption
     * @return Return 0 if everything has passed successfully.
     */
    @Override
    public int decrypt(String ciphertext, String keyset) {
        return 0;
    }

    /**
     * Send data to the verifier
     *
     * @param file is the file name to send.
     */
    public int SendDatatoVerifier(int file){
        return SendFile(file);
    }

    @SuppressLint("NotConstructor")
    public native int SendFile(int fileOption);

    public int decrypt() {
        return DecryptData();
    }
    public native int DecryptData();
    /**
     * the keys generation using TFHE library
     * @param KeySetName Name of the Secret key to be generated
     * @param CloudKeyName Name of the Cloud key to be generated
     * @return Return 0 if everything has passed successfully.
     */
    public int keygen(String KeySetName, String CloudKeyName){
        return KeyGen(KeySetName,CloudKeyName);
    }

    public native int KeyGen(String KeySetName, String CloudKeyName);

    public void Verify(int k){
        VerifyCircuit(k);
    }
    public native void VerifyCircuit(int k);

    public int ReadDatafromVerifier(){
        return ReadFile();
    }

    public native int ReadFile();

}
