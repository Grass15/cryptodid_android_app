package com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("GetAccessToken")
    Call<AccessTokenResponse> getAccessToken(@Body AccessTokenRequest request);
}
