package com.cdac.medinfo.mercury.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdac.medinfo.mercury.R;
import com.cdac.medinfo.mercury.model.Account;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    public interface ActionListener {
        void onClick(int position);
    }

//    Context context;
//    ArrayList<Account> accountArrayList;
    private final Context context;
    private final ArrayList<Account> accountArrayList;
    private final ActionListener actionListener;

    public AccountAdapter(Context context, ArrayList<Account> accountArrayList, ActionListener actionListener) {
        this.context = context;
        this.accountArrayList = accountArrayList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.recyclerview_item_account, null);
        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Account account = accountArrayList.get(position);
        holder.textViewRecyclerviewAccountUsername.setText(account.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    actionListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView textViewRecyclerviewAccountUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            textViewRecyclerviewAccountUsername = itemView.findViewById(R.id.textViewRecyclerviewAccountUsername);
        }
    }
}
