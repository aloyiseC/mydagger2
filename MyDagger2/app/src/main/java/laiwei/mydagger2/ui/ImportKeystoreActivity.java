package laiwei.mydagger2.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.databind.deser.Deserializers;

import javax.inject.Inject;

import laiwei.mydagger2.C;
import laiwei.mydagger2.R;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.factory.ImportModelFactory;
import laiwei.mydagger2.module.ImportKeystoreModule;
import laiwei.mydagger2.viewmodel.ImportKeystoreViewModel;

/**
 * Created by laiwei on 2018/4/4 0004.
 */
public class ImportKeystoreActivity extends BaseActivity implements View.OnClickListener{
    @Inject
    ImportModelFactory factory;
    ImportKeystoreViewModel viewModel;
    EditText keysore,password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_k);
        viewModel = ViewModelProviders.of(this,factory).get(ImportKeystoreViewModel.class);
        viewModel.importedWallet().observe(this,this::onImportWallet);
        keysore = findViewById(R.id.keystore_edit);
        password = findViewById(R.id.password_edit);
        findViewById(R.id.submit).setOnClickListener(this);
    }

    private void onImportWallet(Wallet wallet) {
        Intent result = new Intent();
        result.putExtra(C.Key.WALLET, wallet);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onClick(View v) {
        String keystoreStr = keysore.getText().toString();
        String passwordStr = password.getText().toString();
        if(!TextUtils.isEmpty(keystoreStr) && !TextUtils.isEmpty(passwordStr)){
            viewModel.importKeystore(keystoreStr,passwordStr);
        }
    }
}
