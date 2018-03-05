package romanov.currencyconverter.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Callback;
import retrofit2.Response;
import romanov.currencyconverter.DataBase.DBHelper;
import romanov.currencyconverter.api.ApiService;
import romanov.currencyconverter.model.ConverterNum;
import romanov.currencyconverter.utils.InternetConnection;
import romanov.currencyconverter.R;
import romanov.currencyconverter.api.RetroClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View parentView;
    private TextView dbText;
    private String converter;
    private String endPrice;
    public String codeFrom = null;
    public String codeTo = null;
    final int REQUEST_CODE_FROM = 1;
    final int REQUEST_CODE_TO = 2;
    private Button btnConvertFrom;
    private Button btnConvertTo;
    private Button btnConvert;
    private Button btnClear;
    private EditText etNumPrice;
    DBHelper dbHelper;
    Date currentTime = Calendar.getInstance().getTime();
    String textDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        parentView = findViewById(R.id.parentLayout);
        dbText = findViewById(R.id.dbText);
        etNumPrice = findViewById(R.id.enterPrice);
        btnClear = findViewById(R.id.btnClear);
        btnConvertFrom = findViewById(R.id.convertFrom);
        btnConvertTo = findViewById(R.id.convertTo);
        btnConvert = findViewById(R.id.convert);

        dbHelper = new DBHelper(this);
        dbText.setMovementMethod(new ScrollingMovementMethod());
        dbShow();

        Intent intent = getIntent();
        codeFrom = intent.getStringExtra("code");
        codeTo = intent.getStringExtra("code");

        if (codeFrom == null) {
            codeFrom = "USD";
        }
        if (codeTo == null) {
            codeTo = "BTC";
        }

        btnConvertFrom.setText(codeFrom);
        btnConvertTo.setText(codeTo);

        btnConvertFrom.setOnClickListener(this);
        btnConvertTo.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        assert btnConvert != null;
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull final View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnConvert.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                //Check Internet Connection
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    final ProgressDialog dialog;

                    //Progress Dialog for user
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle(getString(R.string.string_getting_json_title));
                    dialog.setMessage(getString(R.string.string_getting_json_message));
                    dialog.show();

                    //Creating an object of our api interface
                    ApiService api = RetroClient.getApiService();

                    //Calling JSON
                    retrofit2.Call<ConverterNum> call = api.getPriceJSON(codeFrom, codeTo);
                    call.enqueue(new Callback<ConverterNum>() {
                        @Override
                        public void onResponse(retrofit2.Call<ConverterNum> call, Response<ConverterNum> response) {
                            //Dismiss Dialog
                            dialog.dismiss();

                            if (etNumPrice.getText().length() == 0) {
                                Snackbar.make(parentView, R.string.enterNumber, Snackbar.LENGTH_LONG).show();
                            } else {

                                if (response.isSuccessful()) {
                                    double numPrice = Double.parseDouble(String.valueOf(etNumPrice.getText()));

                                    //Got Successfully
                                    converter = response.body().getConverter().getPrice();
                                    double value = Double.parseDouble(converter.replace(",", "."));
                                    endPrice = String.format("%.8f", value * numPrice);

                                } else {
                                    Snackbar.make(parentView, R.string.string_some_thing_wrong, Snackbar.LENGTH_LONG).show();
                                }
                                dbEntry();
                                dbShow();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ConverterNum> call, Throwable t) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    Snackbar.make(parentView, R.string.string_internet_connection_unavailable, Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Intent intent;
        switch (view.getId()) {
            case R.id.convertFrom:
                intent = new Intent(this, CurrencyActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FROM);
                break;
            case R.id.convertTo:
                intent = new Intent(this, CurrencyActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TO);
                break;
            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CONVERT_STORY, null, null);
                dbText.setText(R.string.string_db_empty);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FROM:
                    codeFrom = data.getStringExtra("code");
                    btnConvertFrom.setText(codeFrom);
                    break;
                case REQUEST_CODE_TO:
                    codeTo = data.getStringExtra("code");
                    btnConvertTo.setText(codeTo);
                    break;
            }

        }
    }

    public void dbShow() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONVERT_STORY, null, null, null, null, null, null);

        StringBuffer buffer = new StringBuffer("");

        if (cursor.moveToFirst()) // делает первую запись cursor активной и проверяет есть ли в нем записи
        {
            //    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int codeFromIndex = cursor.getColumnIndex(DBHelper.KEY_CODE_fROM);
            int codeToIndex = cursor.getColumnIndex(DBHelper.KEY_CODE_TO);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            int enterNumIndex = cursor.getColumnIndex(DBHelper.KEY_ENTER_NUM);
            //int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME);
            do {

                textDB = cursor.getString(enterNumIndex) +
                        "  " + cursor.getString(codeFromIndex) +
                        " = " + cursor.getString(priceIndex) +
                        "  " + cursor.getString(codeToIndex) + "\n";// +
                // "  " + cursor.getString(timeIndex);
                buffer.insert(0, textDB);
            }
            while (cursor.moveToNext());
            if (textDB.contains("null")) {
                dbText.setText(R.string.string_db_empty);
                btnClear.setVisibility(View.GONE);
            } else {
                dbText.setText(buffer);
            }
        }
    }

    public void dbEntry() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();  // отвечает за добавление строк в таблицу

        // запись в бд
        contentValues.put(DBHelper.KEY_CODE_fROM, codeFrom);
        contentValues.put(DBHelper.KEY_CODE_TO, codeTo);
        contentValues.put(DBHelper.KEY_ENTER_NUM, String.valueOf(etNumPrice.getText()));
        contentValues.put(DBHelper.KEY_PRICE, endPrice);
        contentValues.put(DBHelper.KEY_TIME, String.valueOf(currentTime));

        database.insert(DBHelper.TABLE_CONVERT_STORY, null, contentValues);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("codeFrom", codeFrom);
        outState.putString("codeTo", codeTo);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        codeFrom = savedInstanceState.getString("codeFrom");
        codeTo = savedInstanceState.getString("codeTo");
        btnConvertFrom.setText(codeFrom);
        btnConvertTo.setText(codeTo);
    }

}
