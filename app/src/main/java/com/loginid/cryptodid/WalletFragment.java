package com.loginid.cryptodid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.loginid.cryptodid.WalletFragmentDirections;
import com.loginid.cryptodid.claimViewHandler.ClaimsContainerAdapter;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.model.ClaimViewModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment {

    private ClaimViewModel claimViewModel;
    public static List<Claim> claims;

    private DbDriver driver;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public WalletFragment() {
        // Required empty public constructor
    }

    public static WalletFragment newInstance() {
        WalletFragment fragment = new WalletFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        claimViewModel = new ViewModelProvider(requireActivity()).get(ClaimViewModel.class);
        try {
            claims = MainActivity.driver.getClaims();
        } catch (IOException | ParseException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.wallet_fragment, container, false);
        Button makeTransaction = view.findViewById(R.id.transaction);
        RecyclerView claimsContainer = view.findViewById(R.id.claimsContainer);
        // Inflate the layout for this fragment

        claimsContainer.setLayoutManager(new LinearLayoutManager(view.getContext()));
        claimViewModel.getData().observe(getViewLifecycleOwner(), new Observer<Claim>() {
            @Override
            public void onChanged(@Nullable Claim claim) {
                // Here we can update the UI
                if(claim != null && !claims.contains(claim)){
                    try {
                        System.out.println(MainActivity.driver.insertClaim(claim));
                        claims.add(claim);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    claimsContainer.setAdapter(new ClaimsContainerAdapter(view.getContext(), claims));
                }
            }

        });
        claimsContainer.setAdapter(new ClaimsContainerAdapter(view.getContext(), claims));
        makeTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections showMenu = WalletFragmentDirections.showTransactionMenu();
                Navigation.findNavController(view).navigate(showMenu);
            }
        });

        return view;
    }



}