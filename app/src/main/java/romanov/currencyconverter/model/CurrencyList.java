package romanov.currencyconverter.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import romanov.currencyconverter.model.Currency;

public class CurrencyList {

    @SerializedName("rows")
    @Expose
    private List<Currency> currency = null;

    public List<Currency> getCurrency() {
        return currency;
    }

    public void setCurrency(List<Currency> currency) {
        this.currency = currency;
    }

}
