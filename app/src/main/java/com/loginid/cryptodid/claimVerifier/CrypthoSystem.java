package com.loginid.cryptodid.claimVerifier;

/**
 * This interface defines a crypthosystem using TFHE library.
 */
public interface CrypthoSystem {
    /**
     * Encrypt method for encrypt a plaintext using TFHE library.
     *
     * @param plaintext the claim
     * @param keyset name of the private key to used, that already generated with keygen()
     * @return Return 0 if everything has passed successfully.
     */
    int encrypt(int plaintext, String keyset);

    /**
     * decrypt a ciphertext using TFHE library.
     * @param ciphertext name of the ciphertext that encrypted.
     * @param keyset Name of the key used for decryption
     * @return Return 0 if everything has passed successfully.
     */
    int decrypt(String ciphertext, String keyset);
    /**
     * the keys generation using TFHE library
     * @param KeySetName Name of the Secret key to be generated
     * @param CloudKeyName Name of the Cloud key to be generated
     * @return Return 0 if everything has passed successfully.
     */
    int keygen(String KeySetName, String CloudKeyName);


}
