package romanov.currencyconverter.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import romanov.currencyconverter.model.ConverterNum;
import romanov.currencyconverter.model.CurrencyList;

public interface ApiService {
    @GET("/api/currencies")
    Call<CurrencyList> getMyJSON();
    @GET("/api/ticker/{fromCode}-{toCode}")
    Call<ConverterNum> getPriceJSON(@Path("fromCode") String fromCode, @Path("toCode") String toCode);

}
