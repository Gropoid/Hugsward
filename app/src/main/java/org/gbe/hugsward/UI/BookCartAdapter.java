package org.gbe.hugsward.UI;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.Layout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.gbe.hugsward.R;
import org.gbe.hugsward.model.Book;
import org.gbe.hugsward.model.BookCart;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gbe on 9/11/15.
 */
public class BookCartAdapter extends ArrayAdapter<Pair<Book, Integer>> {


    public BookCartAdapter(Context context, int resource, BookCart cart) {
        super(context, resource, cart.getCartContents());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Pair<Book, Integer> item = getItem(position);
        VH holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cart_summary_item, parent, false);
            holder = new VH(convertView);
//            holder.tvTitle = (TextView)convertView.findViewById(R.id.tvBookTitle);
//            holder.tvUnitPrice = (TextView)convertView.findViewById(R.id.tvBookUnitPrice);
//            holder.tvQuantity = (TextView)convertView.findViewById(R.id.tvBookQuantity);
//            holder.tvTotalPrice = (TextView)convertView.findViewById(R.id.tvBookTotalPrice);
            convertView.setTag(holder);
        } else {
            holder = (VH)convertView.getTag();
        }
        holder.tvTitle.setText(item.first.getTitle());
        holder.tvUnitPrice.setText(String.format("%.2f €", (float)item.first.getPrice()));
        holder.tvQuantity.setText("Qty : " + item.second.toString());
        holder.tvTotalPrice.setText(String.format("%.2f €", (float) item.first.getPrice() * item.second));
        return convertView;
    }

    static class VH {
        @Bind(R.id.tvBookTitle)
        TextView tvTitle;
        @Bind (R.id.tvBookUnitPrice)
        TextView tvUnitPrice;
        @Bind(R.id.tvBookQuantity)
        TextView tvQuantity;
        @Bind(R.id.tvBookTotalPrice)
        TextView tvTotalPrice;

        public VH(View v) {
            ButterKnife.bind(this, v);
        }
    }
}


