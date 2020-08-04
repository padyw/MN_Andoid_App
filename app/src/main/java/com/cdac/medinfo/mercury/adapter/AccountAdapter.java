package com.cdac.medinfo.mercury.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdac.medinfo.mercury.AccountsViewActivity;
import com.cdac.medinfo.mercury.R;
import com.cdac.medinfo.mercury.model.Account;

import java.util.ArrayList;

/*
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    public interface ActionListener {
        void onClick(int position);
    }

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
*/

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    public interface ActionListener {
        void onClick(int position);
        void onDelete(int position);
    }

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                //Inflating the Popup using xml file
                popupMenu.inflate(R.menu.popup_menu_edit_delete);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Log.e("onMenuItemClick ", "You Clicked : " + menuItem.getGroupId() + " " + menuItem.getItemId() + " " + menuItem.getTitle() + " " + position);

                        switch (menuItem.getItemId()) {
                            case R.id.popup_option_edit:

                                //Do operation if menu_item_one
                                Log.e("onMenuItemClick ", "You Clicked Edit ");
                                break;
                            case R.id.popup_option_delete:

                                //Do operation if menu_item_two
                                Log.e("onMenuItemClick ", "You Clicked Delete ");
                                actionListener.onDelete(position);
                                break;
                        }
                        return true;
//                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    public class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView textViewRecyclerviewAccountUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            textViewRecyclerviewAccountUsername = itemView.findViewById(R.id.textViewRecyclerviewAccountUsername);

//            this.itemView.setOnCreateContextMenuListener(this);
        }

//        @Override
//        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.setHeaderTitle("Select an Option");
//            contextMenu.in
//            contextMenu.add(this.getAdapterPosition(), 121,0, "Edit");
//            contextMenu.add(this.getAdapterPosition(), 122,0, "Delete");
//        }
    }
}
