package org.gbe.hugsward.UI;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.gbe.hugsward.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.gbe.hugsward.model.Book;
import org.gbe.hugsward.model.BookCart;

/**
 * The fragment for adding or removing a given book from the shopping cart
 */
public class BookCardFragment extends Fragment {

    private static final String BOOK_PARCEL_KEY = "Book";
    private static final String BOOK_CART_PARCEL_KEY = "BookCart";

    @Bind(R.id.book_card)
    CardView cvBookCard;

    @Bind(R.id.book_card_image)
    ImageView ivBookCover;

    @Bind(R.id.plus_button)
    Button btnPlusButton;

    @Bind(R.id.minus_button)
    Button btnMinusButton;

    @Bind(R.id.counter)
    TextView tvCounter;

    @Bind(R.id.tvBookTitle)
    TextView tvBookTitle;

    @Bind(R.id.tvBookPrice)
    TextView tvBookPrice;

    @Bind(R.id.vPalette)
    View vPalette;

    private Book mBook;
    private BookCart mCart;

    private Target mTarget;

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
        mTarget = new BookCoverTarget() ;
        ButterKnife.setDebug(true);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (mBook == null){
            tvBookTitle.setText(getString(R.string.book_retrieval_error));
            tvBookPrice.setText("");
            Picasso.with(getActivity()).load(R.drawable.placeholder340_500).fit().into(ivBookCover);
            btnMinusButton.setEnabled(false);
            btnPlusButton.setEnabled(false);
        } else {
            tvBookTitle.setText(mBook.getTitle());
            tvBookPrice.setText(mBook.getPrice() + "€");
            Picasso.with(getActivity()).load(mBook.getCover())
                    .placeholder(R.drawable.progress_wheel_animation)
                    .error(R.drawable.placeholder340_500)
                    .into(mTarget);
            onQuantityChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    private class BookCoverTarget implements Target{
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            ivBookCover.setImageBitmap(bitmap);
            ivBookCover.setScaleType(ImageView.ScaleType.FIT_CENTER);

            Palette.from(bitmap).generate(
                    new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch vibrant = palette.getVibrantSwatch();
                            Palette.Swatch muted = palette.getDarkVibrantSwatch();
                            if (vibrant != null) {
                                vPalette.setBackgroundColor(vibrant.getRgb());
                            }
                        }
                    });
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            ivBookCover.setScaleType(ImageView.ScaleType.CENTER);
            ivBookCover.setImageDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            ivBookCover.setScaleType(ImageView.ScaleType.CENTER);
            ivBookCover.setImageDrawable(placeHolderDrawable);
        }
    }


    private void onQuantityChanged() {
        btnMinusButton.setEnabled(!mCart.isAtMinOrdered(mBook));
        btnPlusButton.setEnabled(!mCart.isAtMaxOrdered(mBook));
        tvCounter.setText(getResources().getString(R.string.book_quantity_label) + mCart.getQuantity(mBook).toString());
    }
}
