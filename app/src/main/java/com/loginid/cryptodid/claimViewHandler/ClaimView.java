package com.loginid.cryptodid.claimViewHandler;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loginid.cryptodid.R;

public class ClaimView extends RecyclerView.ViewHolder {


    TextView titleView,typeView,nameView,contentView;
    Button deleteButton;


    public ClaimView(@NonNull  View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
        typeView = itemView.findViewById(R.id.type);
        nameView = itemView.findViewById(R.id.issuerName);
        contentView = itemView.findViewById(R.id.content);
        deleteButton = itemView.findViewById(R.id.deleteClaim);
    }
}
