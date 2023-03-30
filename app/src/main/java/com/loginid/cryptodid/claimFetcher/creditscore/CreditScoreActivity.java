package com.loginid.cryptodid.claimFetcher.creditscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.loginid.cryptodid.R;
import com.loginid.cryptodid.claimFetcher.plaid.PlaidActivity;

import java.text.ParseException;

public class CreditScoreActivity extends AppCompatActivity {


    ImageButton btnTrans;
    ImageButton btnEq;
    private ActivityResultLauncher<Intent> transUnionActivityLauncher;
    private ActivityResultLauncher<Intent> equiActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creditscore_activity);
        this.transUnionActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if(data != null){
                                setResult(RESULT_OK, data);
                                finish();
                            }
                        }
                    }
                });
        this.equiActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if(data != null){
                                setResult(RESULT_OK, data);
                                finish();
                            }
                        }
                    }
                });

        btnTrans = (ImageButton) findViewById(R.id.transBtn);
        btnEq = (ImageButton) findViewById(R.id.eqBtn);

        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFormTrans();
            }
        });

        btnEq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFormEq();
            }
        });

    }
    private void showFormTrans() {
        Intent transFormActivity = new Intent(this, TransFormActivity.class);
        this.transUnionActivityLauncher.launch(transFormActivity);
    }
    private void showFormEq() {
        Intent equiFormActivity = new Intent(this, EqFormActivity.class);
        this.equiActivityLauncher.launch(equiFormActivity);
    }


}