package com.loginid.cryptodid.model;

import java.io.Serializable;
import java.util.Date;

public class Claim implements Serializable {
    String type;
    String issuerName;
    byte[] signature;
    byte[] revocationNonce;
    byte[] encryptedAttribute;
    Date expirationDate;
    Date issuingDate;
    int id;
    String title;
    String content;


    public Claim(String title, String type, String content, String issuerName, byte[] encryptedAttribute, byte[] signature) {
        this.title = title;
        this.type = type;
        this.content = content;
        this.issuerName = issuerName;
        this.encryptedAttribute = encryptedAttribute;
        this.signature = signature;
    }

    public Date getIssuingDate() {
        return issuingDate;
    }
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date date) {
        this.expirationDate = date;
    }

    public void setIssuingDate(Date date) {
        this.issuingDate = date;
    }

    public byte[] getEncryptedAttribute() {
        return encryptedAttribute;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getRevocationNonce() {
        return revocationNonce;
    }

    public void setRevocationNonce(byte[] revocationNonce) {
        this.revocationNonce = revocationNonce;
    }

    public byte[] getSignature() {
        return signature;
    }
}