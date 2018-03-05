package romanov.currencyconverter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import romanov.currencyconverter.model.Converter;

public class ConverterNum {

    @SerializedName("ticker")
    @Expose
    private Converter converter;


    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter ticker) {
        this.converter = ticker;
    }


}