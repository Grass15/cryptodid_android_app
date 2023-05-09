/*
 * Copyright (c) 2020 Plaid Technologies, Inc. <support@plaid.com>
 */

package com.loginid.cryptodid.claimFetcher.plaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.loginid.cryptodid.R;
import com.loginid.cryptodid.claimFetcher.plaid.network.AccountRequester;
import com.loginid.cryptodid.claimFetcher.plaid.network.LinkTokenRequester;
import com.loginid.cryptodid.claimFetcher.plaid.network.ResponseClass;
import com.plaid.link.OpenPlaidLink;
import com.plaid.link.configuration.LinkTokenConfiguration;
import com.plaid.link.result.LinkExit;
import com.plaid.link.result.LinkSuccess;



import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlaidActivity extends AppCompatActivity {

  private TextView result;
  private TextView balanceTextView;
  private int balance;
  private Intent intent;

  private ActivityResultLauncher<LinkTokenConfiguration> linkAccountToPlaid = registerForActivityResult(
      new OpenPlaidLink(),
      result -> {
        if (result instanceof LinkSuccess) {
          intent = getIntent();
          showSuccess((LinkSuccess) result);
        } else {
          showFailure((LinkExit) result);
        }
      });

  private void showSuccess(LinkSuccess success) {
    //LinkAccount account = success.getMetadata().getAccounts().get(1);
    AccountRequester.INSTANCE.getAccounts((Callback)(new Callback() {
      public void onResponse(@NotNull Call call, @NotNull Response response) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(response, "response");
        if (response.isSuccessful()) {
          ResponseClass responseBody = (ResponseClass)response.body();
          if (responseBody != null) {
            balance = responseBody.getAccounts().get(1).getBalances().getAvailable();
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                balanceTextView.setText(getString(R.string.balance, balance +""));
                result.setText(getString(R.string.content_success, balance +""));
                //System.out.println(balance);
              }

            });
            intent = getIntent();
            intent.putExtra("attribute", (balance )+"");
            intent.putExtra("attr", "balance");
            intent.putExtra("claimTitle", "Bank Claim");
            intent.putExtra("claimType", "Balance");
            intent.putExtra("issuerName", "Crypto DID: Expires on ");
            intent.putExtra("claimContent", "You can use this claim to attest your balance");
            setResult(RESULT_OK, intent);
            finish();
          } else {
            System.out.println(responseBody);
          }
        }
      }
      public void onFailure(@NotNull Call call, @NotNull Throwable t) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(t, "t");
        String var3 = "request failed";
        System.out.println(var3);
      }
    }));
    //finish();

  }

  private void showFailure(LinkExit exit) {

    if (exit.getError() != null) {
      result.setText(getString(
          R.string.content_exit,
          exit.getError().getDisplayMessage(),
          exit.getError().getErrorCode()));
    } else {
      result.setText(getString(
          R.string.content_cancel,
          exit.getMetadata().getStatus() != null ? exit.getMetadata().getStatus().getJsonValue() : "unknown"));
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.plaid_activity);
    result = findViewById(R.id.result);
    balanceTextView = findViewById(R.id.balance);

    View button = findViewById(R.id.open_link);
    button.setOnClickListener(view -> {
      openLink();
    });
  }


  /**
   * For all Link configuration options, have a look at the
   * <a href="https://plaid.com/docs/link/android/#parameter-reference">parameter reference</>
   */
  private void openLink() {
    LinkTokenRequester.INSTANCE.getToken()
        .subscribe(this::onLinkTokenSuccess, this::onLinkTokenError);
  }

  private void onLinkTokenSuccess(String token) {
    LinkTokenConfiguration configuration = new LinkTokenConfiguration.Builder()
        .token(token)
        .build();
    linkAccountToPlaid.launch(configuration);
  }

  private void onLinkTokenError(Throwable error) {
    if (error instanceof java.net.ConnectException) {
      Toast.makeText(
          this,
          "Please run `sh start_server.sh <client_id> <sandbox_secret>`",
          Toast.LENGTH_LONG).show();
      return;
    }
    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
  }

}
