package laiwei.mydagger2.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import javax.inject.Inject;

import laiwei.mydagger2.C;
import laiwei.mydagger2.R;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.factory.MainModelFactory;
import laiwei.mydagger2.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    @Inject
    MainModelFactory factory;
    MainViewModel viewModel;
    TextView textView;
    EditText passwordEdit;
    Wallet defaultWallet;
    WalletAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new WalletAdapter();
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        viewModel = ViewModelProviders.of(this,factory).get(MainViewModel.class);
        //viewModel.users().observe(this,this::show);
        viewModel.wallets().observe(this,this::showAccounts);
       // viewModel.createadWallet().observe(this,this::show);
        viewModel.exportedWallet().observe(this,this::openShareDialog);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
        textView = findViewById(R.id.text);
        passwordEdit = findViewById(R.id.password);
        Button btnExport = findViewById(R.id.btn1);
        btnExport.setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
    }

    public void show(Wallet wallet){
        textView.setText(wallet.address);
    }

    public void showAccounts(Wallet[] wallets){
        if(wallets.length > 0){
            defaultWallet = wallets[0];
            adapter.setWallets(wallets);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                viewModel.createWallet();
                break;
            case R.id.btn1:
                String password = passwordEdit.getText().toString();
                if(!TextUtils.isEmpty(password)){
                    viewModel.exportWallet(defaultWallet,password);
                }
                break;
            case R.id.btn2:
                Intent intent = new Intent(this,TransactionActivity.class);
                intent.putExtra(C.EXTRA_ADDRESS,defaultWallet);
                startActivity(intent);
                break;
            case R.id.btn3:
                Intent importIntent = new Intent(this,ImportKeystoreActivity.class);
                startActivityForResult(importIntent,0);
                break;
        }
    }

    private void openShareDialog(String jsonData) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Keystore");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, jsonData);
        startActivityForResult(
                Intent.createChooser(sharingIntent, "Share via"),
                C.SHARE_REQUEST_CODE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        defaultWallet = adapter.getWallet(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Wallet walletSelected = adapter.getWallet(position);
        if(!walletSelected.sameAddress(defaultWallet.address)){
            Intent intent = new Intent(this,SendActivity.class);
            intent.putExtra(C.EXTRA_ADDRESS,defaultWallet);
            intent.putExtra(C.EXTRA_CONTRACT_ADDRESS,walletSelected);
            startActivity(intent);
        }
        return true;
    }

    private class WalletAdapter extends BaseAdapter{
        Wallet[] wallets;

        @Override
        public int getCount() {
            int length = 0;
            if (wallets != null)
                length = wallets.length;
            return length;
        }

        @Override
        public Object getItem(int position) {
            return wallets[0];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Wallet wallet = wallets[position];
            if(null == convertView)
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.listview_item_main,null);
            CheckBox box = convertView.findViewById(R.id.checkbox);
            TextView walletTv = convertView.findViewById(R.id.wallet);

            boolean isDefault = wallet.sameAddress(defaultWallet.address);
            box.setChecked(wallet.sameAddress(defaultWallet.address));
            box.setClickable(false);
            walletTv.setText(wallet.address);
            walletTv.setTextColor(ContextCompat.getColor(MainActivity.this, isDefault ? R.color.color_444444 : R.color.color_aaaaaa));
            return convertView;
        }

        public void setWallets(Wallet[] wallets) {
            this.wallets = wallets;
            notifyDataSetChanged();
        }

        public Wallet getWallet(int position){
            return wallets[position];
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            viewModel.fetchAccounts();
        }
    }
}
