package romanov.currencyconverter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import romanov.currencyconverter.model.Currency;
import romanov.currencyconverter.R;

public class CurrencyAdapter extends ArrayAdapter<Currency> {

    List<Currency> currencyList;
    Context context;
    private LayoutInflater mInflater;

    //Constructors
    public CurrencyAdapter(Context context, List<Currency> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        currencyList = objects;
    }

    @Override
    public Currency getItem(int position) {
        return currencyList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder vh;
        if(convertView==null){
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        Currency item = getItem(position);

        vh.textViewName.setText(item.getName());
        vh.textViewCode.setText(item.getCode());

        return vh.rootView;

    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final TextView textViewName;
        public final TextView textViewCode;

        public ViewHolder(RelativeLayout rootView, TextView textViewCode, TextView textViewName ) {
            this.rootView = rootView;
            this.textViewCode = textViewCode;
            this.textViewName = textViewName;

        }

        public static ViewHolder create(RelativeLayout rootView) {
            TextView textViewName = rootView.findViewById(R.id.textViewName);
            TextView textViewCode = rootView.findViewById(R.id.textViewCode);
            return new ViewHolder(rootView, textViewCode, textViewName);
        }
    }
}
