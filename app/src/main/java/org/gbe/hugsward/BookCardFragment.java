package org.gbe.hugsward;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import model.BookCart;

/**
 * The fragment for adding or removing a given book from the shopping cart
 */
public class BookCardFragment extends Fragment {

    private static final String BOOK_PARCEL_KEY = "Book";
    private static final String BOOK_CART_PARCEL_KEY = "BookCart";

    @Bind(R.id.book_card_image)
    ImageView ivBookCover;

    @Bind(R.id.plus_button)
    Button btnPlusButton;

    @Bind(R.id.minus_button)
    Button btnMinusButton;

    @Bind(R.id.counter)
    TextView tvCounter;

    private Book mBook;
    private BookCart mCart;

    public BookCardFragment() {
    }

    public static BookCardFragment newInstance(Book book, BookCart cart) {
        BookCardFragment f = new BookCardFragment();
        Bundle b = new Bundle();
        b.putParcelable(BOOK_PARCEL_KEY, book);
        b.putParcelable(BOOK_CART_PARCEL_KEY, cart);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_card_layout, container, false);
        if (savedInstanceState != null) {
            mBook = savedInstanceState.getParcelable(BOOK_PARCEL_KEY);
            mCart = savedInstanceState.getParcelable(BOOK_CART_PARCEL_KEY);
        } else {
            mBook = getArguments().getParcelable(BOOK_PARCEL_KEY);
            mCart = getArguments().getParcelable(BOOK_CART_PARCEL_KEY);
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
        outState.putParcelable(BOOK_CART_PARCEL_KEY, mCart);
    }

    @OnClick(R.id.plus_button)
    protected void incrementQuantity(){
        if(! mCart.isAtMaxOrdered(mBook)) {
            mCart.add(mBook);
            onQuantityChanged();
        }
    }

    @OnClick(R.id.minus_button)
    protected void decrementQuantity() {
        if( ! mCart.isAtMinOrdered(mBook)) {
            mCart.remove(mBook);
            onQuantityChanged();
        }
    }

    private void onQuantityChanged() {
        btnMinusButton.setEnabled(!mCart.isAtMinOrdered(mBook));
        btnPlusButton.setEnabled(!mCart.isAtMaxOrdered(mBook));
        tvCounter.setText(mCart.getQuantity(mBook).toString());
    }
}
