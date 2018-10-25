package laiwei.mydagger2.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import javax.inject.Inject;

import laiwei.mydagger2.C;
import laiwei.mydagger2.R;
import laiwei.mydagger2.bean.Transaction;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.factory.TransModelFactory;
import laiwei.mydagger2.network.NetworkInfo;
import laiwei.mydagger2.viewmodel.TransViewModel;

/**
 * Created by laiwei on 2018/3/29 0029.
 */
public class TransactionActivity extends BaseActivity{
    @Inject
    TransModelFactory factory;
    TransViewModel viewModel;
    TextView balanceTv,balanceNum;
    TransAdapter adapter;
    Wallet defaultWallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        balanceTv = findViewById(R.id.balance);
        balanceNum = findViewById(R.id.balance_num);
        adapter = new TransAdapter();
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        defaultWallet = getIntent().getParcelableExtra(C.EXTRA_ADDRESS);
        viewModel = ViewModelProviders.of(this,factory).get(TransViewModel.class);
        viewModel.setDefaultWallet(defaultWallet);
        viewModel.balance().observe(this,this::onBalanceChanged);
        viewModel.transactions().observe(this,this::onGetTransactions);
    }

    private void onGetTransactions(Transaction[] transactions) {
        adapter.setTransactions(transactions);
    }

    private void onBalanceChanged(Map<String, String> balance) {
        NetworkInfo networkInfo = viewModel.getDefaultNetworkInfo();
        balanceTv.setText(balance.get(C.USD_SYMBOL));
        balanceNum.setText(balance.get(networkInfo.symbol) + " " + networkInfo.symbol);
    }

    private class TransAdapter extends BaseAdapter{
        private Transaction[] transactions;
        @Override
        public int getCount() {
            if(transactions != null)
                return transactions.length;
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            return transactions[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Transaction trans = transactions[position];
            if(convertView == null){
                convertView = LayoutInflater.from(TransactionActivity.this).inflate(R.layout.listview_item_trans,null);
            }
            ImageView typeIcon = convertView.findViewById(R.id.type_icon);
            TextView address = convertView.findViewById(R.id.address);
            TextView type = convertView.findViewById(R.id.type);
            TextView value = convertView.findViewById(R.id.value);

            boolean isSent = trans.from.toLowerCase().equals(defaultWallet.address);
            type.setText(isSent ? "Sent": "Received");
            if (isSent) {
                typeIcon.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
            } else {
                typeIcon.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
            }
            address.setText(isSent ? trans.to : trans.from);
            value.setTextColor(ContextCompat.getColor(TransactionActivity.this, isSent ? R.color.red : R.color.green));

            String valueStr;
            if (trans.value.equals("0")) {
                valueStr = "0 " + "ETH";
            } else {
                valueStr = (isSent ? "-" : "+") + getScaledValue(trans.value, 18) + " " + "ETH";
            }

            value.setText(valueStr);
            return convertView;
        }

        public void setTransactions(Transaction[] transactions) {
            this.transactions = transactions;
            notifyDataSetChanged();
        }

        private String getScaledValue(String valueStr, long decimals) {
            // Perform decimal conversion
            BigDecimal value = new BigDecimal(valueStr);
            value = value.divide(new BigDecimal(Math.pow(10, decimals)));
            int scale = 3 - value.precision() + value.scale();
            return value.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        }
    }
}
