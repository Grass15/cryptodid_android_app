package com.loginid.cryptodid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loginid.cryptodid.claimFetcher.Fetcher;
import com.loginid.cryptodid.claimVerifier.Verifier;

import java.io.IOException;
import java.text.ParseException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionMenuFragment extends BottomSheetDialogFragment {

    public TransactionMenuFragment() {
        // Required empty public constructor
    }
    private String issuerId = "";
    private TransactionMenuFragment fragment;
    private Fetcher fetcher;
    private Verifier verifier;

    public static TransactionMenuFragment newInstance(String param1, String param2) {
        TransactionMenuFragment fragment = new TransactionMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetcher = new Fetcher(this);
        verifier = new Verifier(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.transaction_menu_fragment, container, false);
        Context context = this.getContext();
        Button fetchClaim = view.findViewById(R.id.fetchClaim);
        Button verifyClaim = view.findViewById(R.id.verifyclaim);
        fetchClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    fetcher.storeClaim();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        verifyClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    verifier.verify();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        return view;
    }




}