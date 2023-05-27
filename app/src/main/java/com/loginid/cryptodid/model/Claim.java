package com.loginid.cryptodid.model;

import java.io.Serializable;
import java.util.Date;

public class Claim implements Serializable {

    String title;
    int id;
    String type;
    String issuerName;
    String content;
    Date expirationDate;
    Date issuingDate;
    String attributeName;


    public Claim(String title, String type, String issuerName, String content, String attributeName) {
        this.title = title;
        this.type = type;
        this.issuerName = issuerName;
        this.content = content;
        this.attributeName = attributeName;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
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

    public String getTitle() {
        return title;
    }
    public String getType() {
        return type;
    }
    public String getIssuerName() {
        return issuerName;
    }
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return super.toString() + this.issuerName + this.type + this.id;
    }
}