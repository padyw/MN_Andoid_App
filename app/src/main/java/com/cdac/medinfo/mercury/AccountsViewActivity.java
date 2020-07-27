package com.cdac.medinfo.mercury;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cdac.medinfo.mercury.adapter.AccountAdapter;
import com.cdac.medinfo.mercury.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountsViewActivity extends AppCompatActivity implements AccountAdapter.ActionListener {

    RecyclerView recyclerviewAccountsView;
    ArrayList<Account> accountList = new ArrayList<>();
    AccountAdapter accountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_view);

        recyclerviewAccountsView = findViewById(R.id.recyclerviewAccountsView);

        accountAdapter = new AccountAdapter(this, accountList, this);
        recyclerviewAccountsView.setAdapter(accountAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerviewAccountsView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //load all accounts
        this.loadAccounts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(getString(R.string.add_account));
        menu.add(getString(R.string.close));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals(getString(R.string.close))){
            finish();
        }else if (item.getTitle().equals(getString(R.string.add_account))) {
            Intent intent = new Intent(this, AddAccountActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadAccounts() {

        this.accountList.clear();
        ArrayList<Account> list = (ArrayList<Account>) Account.getAllAccounts(this);
        for (Account account: list) {
            this.accountList.add(account);
        }

        if (this.accountList != null)
        {
            Log.e("loadAccounts", "" + accountList.size());
        }else
        {
            Log.e("loadAccounts", "" + this.accountList);
        }

        //refresh the recycler view
        accountAdapter.notifyDataSetChanged();
    }

    //for AccountAdapter.ActionListener  for on account item click
    @Override
    public void onClick(int position) {

        Log.e("AccountView onlcik", "" + position);
        Log.e("AccountView onlcik", "" + this.accountList.get(position).getUsername());

        Account account = this.accountList.get(position);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }
}