package com.loginid.cryptodid.claimFetcher.plaid.network;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Metadata(
        mv = {1, 6, 0},
        k = 1,
        d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0015\u001a\u00020\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\n0\u0018R\u0019\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0019\u0010\u0011\u001a\n \u0005*\u0004\u0018\u00010\u00120\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006\u0019"},
        d2 = {"Lcom/plaid/linksample/network/AcoountRequester;", "", "()V", "accountsApi", "Lcom/plaid/linksample/network/AccountsApi;", "kotlin.jvm.PlatformType", "getAccountsApi", "()Lcom/plaid/linksample/network/AccountsApi;", "call", "Lretrofit2/Call;", "Lcom/plaid/linksample/network/ResponseClass;", "getCall", "()Lretrofit2/Call;", "request", "Lcom/plaid/linksample/network/Request;", "getRequest", "()Lcom/plaid/linksample/network/Request;", "retrofit", "Lretrofit2/Retrofit;", "getRetrofit", "()Lretrofit2/Retrofit;", "getAccounts", "", "callback", "Lretrofit2/Callback;", "app_debug"}
)
public final class AccountRequester {
    private static final Retrofit retrofit;
    private static final AccountsApi accountsApi;
    @NotNull
    private static final Request request;
    @NotNull
    private static final Call call;
    @NotNull
    public static final AccountRequester INSTANCE;

    public final Retrofit getRetrofit() {
        return retrofit;
    }

    public final AccountsApi getAccountsApi() {
        return accountsApi;
    }

    @NotNull
    public final Request getRequest() {
        return request;
    }

    @NotNull
    public final Call getCall() {
        return call;
    }

    public final void getAccounts(@NotNull Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        call.enqueue(callback);
    }

    private AccountRequester() {
    }

    static {
        AccountRequester var0 = new AccountRequester();
        INSTANCE = var0;
        retrofit = (new Retrofit.Builder()).baseUrl("https://sandbox.plaid.com").addConverterFactory((Converter.Factory)GsonConverterFactory.create()).build();
        accountsApi = (AccountsApi)retrofit.create(AccountsApi.class);
        request = new Request("63dec92bcf1981001288b438", "b8e1156221d8433d080068c9ccc200", "access-sandbox-3dcbad86-8f4c-44bd-beab-98b40f889690");
        call = accountsApi.getAccounts(request);
    }
}
