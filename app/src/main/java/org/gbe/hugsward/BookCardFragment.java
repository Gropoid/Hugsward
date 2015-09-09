package org.gbe.hugsward;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.Book;

/**
 * The fragment for adding or removing a given book from the shopping cart
 */
public class BookCardFragment extends Fragment {

    private static final String BOOK_PARCEL_KEY = "Book";
    private static final String QUANTITY_IN_BASKET_KEY = "QuantityInBasket";

    private static final int MAX_ORDER = 10;

    @Bind(R.id.book_card_image)
    ImageView ivBookCover;

    @Bind(R.id.plus_button)
    Button btnPlusButton;

    @Bind(R.id.minus_button)
    Button btnMinusButton;

    @Bind(R.id.counter)
    TextView tvCounter;

    private Book mBook;

    private Integer mQuantity;

    public BookCardFragment() {
    }

    public static BookCardFragment newInstance(Book book, int qty) {
        BookCardFragment f = new BookCardFragment();
        Bundle b = new Bundle();
        b.putParcelable(BOOK_PARCEL_KEY, book);
        b.putInt(QUANTITY_IN_BASKET_KEY, qty);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_card_layout, container, false);
        if (savedInstanceState != null) {
            mBook = savedInstanceState.getParcelable("Book");
            mQuantity = savedInstanceState.getInt("QuantityInBasket");
        } else {
            mBook = getArguments().getParcelable(BOOK_PARCEL_KEY);
            mQuantity = getArguments().getInt(QUANTITY_IN_BASKET_KEY);
        }
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        Picasso.with(getActivity()).load(mBook.getCover())
                .placeholder(R.drawable.progress_wheel_animation)
                .fit().centerInside()
                .into(ivBookCover);
        onQuantityChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BOOK_PARCEL_KEY, mBook);
        outState.putInt(QUANTITY_IN_BASKET_KEY, mQuantity);
    }

    @OnClick(R.id.plus_button)
    protected void  incrementQuantity(){
        if( mQuantity < MAX_ORDER) {
            mQuantity++;
            onQuantityChanged();
        }
    }

    @OnClick(R.id.minus_button)
    protected void  decrementQuantity() {
        if( mQuantity > 0) {
            mQuantity--;
            onQuantityChanged();
        }
    }

    private void onQuantityChanged() {
        btnMinusButton.setEnabled(mQuantity > 0);
        btnPlusButton.setEnabled(mQuantity < MAX_ORDER);
        tvCounter.setText(mQuantity.toString());
    }
}
