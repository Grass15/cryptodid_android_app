package com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest;

public class AccessTokenRequest {
    private String client_id;
    private String client_secret;
    private String grant_type;

    public AccessTokenRequest(String client_id, String client_secret, String grant_type) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.grant_type = grant_type;
    }
}

