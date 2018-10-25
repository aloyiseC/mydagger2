package laiwei.mydagger2.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

import javax.inject.Inject;

import laiwei.mydagger2.C;
import laiwei.mydagger2.R;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.factory.SendModelFactory;
import laiwei.mydagger2.viewmodel.SendViewModel;

/**
 * Created by laiwei on 2018/4/2 0002.
 */
public class SendActivity extends BaseActivity implements View.OnClickListener{
    @Inject
    SendModelFactory sendModelFactory;
    SendViewModel sendViewModel;
    Wallet defaultWallet,walletSelected;
    EditText amount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Intent intent = getIntent();
        defaultWallet = intent.getParcelableExtra(C.EXTRA_ADDRESS);
        walletSelected = intent.getParcelableExtra(C.EXTRA_CONTRACT_ADDRESS);
        TextView defaultTv = findViewById(R.id.default_tv);
        TextView to = findViewById(R.id.to);
        defaultTv.setText(defaultWallet.address);
        to.setText(walletSelected.address);
        Button send = findViewById(R.id.btn);
        send.setOnClickListener(this);
        sendViewModel = ViewModelProviders.of(this,sendModelFactory).get(SendViewModel.class);
        amount = findViewById(R.id.amount);
        amount.requestFocus();
        amount.setEnabled(true);
        sendViewModel.sendTransaction().observe(this,this::onSuccess);
    }

    @Override
    public void onClick(View v) {
        String amountNum = amount.getText().toString();
        if(!TextUtils.isEmpty(amountNum))
         sendViewModel.createTransaction(defaultWallet.address,walletSelected.address,new BigInteger(amountNum),new BigInteger(C.DEFAULT_GAS_PRICE),
                 new BigInteger(C.DEFAULT_GAS_LIMIT));
    }

    private void onSuccess(String str){
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
    }
}
