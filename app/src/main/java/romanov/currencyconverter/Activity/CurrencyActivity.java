package romanov.currencyconverter.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;
import romanov.currencyconverter.api.ApiService;
import romanov.currencyconverter.model.Currency;
import romanov.currencyconverter.adapter.CurrencyAdapter;
import romanov.currencyconverter.model.CurrencyList;
import romanov.currencyconverter.utils.InternetConnection;
import romanov.currencyconverter.R;
import romanov.currencyconverter.api.RetroClient;

public class CurrencyActivity extends AppCompatActivity {

    private ArrayList<Currency> currencyList;
    private ListView listView;
    private View parentView;
    private CurrencyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        currencyList = new ArrayList<>();
        parentView = findViewById(R.id.currency);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                               Intent intent = new Intent();
                intent.putExtra("code", currencyList.get(position).getCode());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //Check Internet Connection
        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;

            //Progress Dialog for user
            dialog = new ProgressDialog(CurrencyActivity.this);
            dialog.setTitle(getString(R.string.string_getting_json_title));
            dialog.setMessage(getString(R.string.string_getting_json_message));
            dialog.show();

            //Creating an object of our api interface
            ApiService api = RetroClient.getApiService();

            //Calling JSON
            retrofit2.Call<CurrencyList> call = api.getMyJSON();
            call.enqueue(new Callback<CurrencyList>() {
                @Override
                public void onResponse(retrofit2.Call<CurrencyList> call, Response<CurrencyList> response) {
                    //Dismiss Dialog
                    dialog.dismiss();

                    if (response.isSuccessful()){

                        //Got Successfully
                        currencyList = (ArrayList<Currency>) response.body().getCurrency();
                        adapter = new CurrencyAdapter(CurrencyActivity.this, currencyList);
                        listView.setAdapter(adapter);
                    }
                    else {
                        Snackbar.make(parentView, R.string.string_some_thing_wrong, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<CurrencyList> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        }
        else {
            Snackbar.make(parentView, R.string.string_internet_connection_unavailable, Snackbar.LENGTH_LONG).show();
        }
    }

}
