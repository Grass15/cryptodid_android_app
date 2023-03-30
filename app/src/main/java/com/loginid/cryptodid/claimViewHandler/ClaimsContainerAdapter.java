package com.loginid.cryptodid.claimViewHandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loginid.cryptodid.DbDriver;
import com.loginid.cryptodid.MainActivity;
import com.loginid.cryptodid.R;
import com.loginid.cryptodid.model.Claim;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;


public class ClaimsContainerAdapter extends RecyclerView.Adapter<ClaimView> {
    Context context;
    List<Claim> claims;
    private DbDriver driver;
    public ClaimsContainerAdapter(Context context, List<Claim> claims) {
        this.context = context;
        this.claims = claims;
        driver = new DbDriver(context);
    }
    @NonNull
    @Override
    public ClaimView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClaimView(LayoutInflater.from(context).inflate(R.layout.claim_viewlayout,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ClaimView holder,  int position) {
         Claim item = claims.get(position);
         holder.titleView.setText(item.getTitle());
         holder.nameView.setText(item.getIssuerName()+item.getExpirationDate());
         holder.typeView.setText(item.getType());
         holder.contentView.setText(claims.get(position).getContent());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.driver.dropClaim(item.getId());
                claims.remove(position);
                try {
                    claims = MainActivity.driver.getClaims();
                } catch (IOException | ParseException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.claims.size();
    }
}
