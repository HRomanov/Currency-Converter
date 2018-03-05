package romanov.currencyconverter.api;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import romanov.currencyconverter.api.ApiService;

public class RetroClient {

    private static final String CURRENCY_URL = "https://www.cryptonator.com";

    private static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(CURRENCY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService(){
        return getRetrofitInstance().create(ApiService.class);
    }

}
