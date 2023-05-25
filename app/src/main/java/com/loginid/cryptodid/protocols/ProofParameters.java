package com.loginid.cryptodid.protocols;

import com.loginid.cryptodid.model.Claim;

import java.io.Serializable;
import java.security.cert.X509Certificate;

public class ProofParameters implements Serializable {
    public Claim claim;
    public MG_FHE fhe;

    public byte[] singatureBytes;

    public X509Certificate x509certificate;
    public ProofParameters(Claim claim, MG_FHE fhe, byte[] singatureBytes, X509Certificate x509Certificate) {
        this.claim = claim;
        this.fhe = fhe;
        this.singatureBytes = singatureBytes;
        this.x509certificate = x509Certificate;
    }
}
