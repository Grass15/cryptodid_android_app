package com.loginid.cryptodid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

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
        verifier = new Verifier(this, this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.transaction_menu_fragment, container, false);
        Button fetchClaim = view.findViewById(R.id.fetchClaim);
        Button verifyClaim = view.findViewById(R.id.verifyclaim);
        fetchClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fetcher.getClaim();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        verifyClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    verifier.verifyClaim();
                } catch (InterruptedException | ParseException | IOException |
                         ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return view;
    }




}