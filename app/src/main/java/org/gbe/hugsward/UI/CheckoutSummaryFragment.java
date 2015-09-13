package org.gbe.hugsward.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.gbe.hugsward.R;
import org.gbe.hugsward.http.HenriPotierApi;
import org.gbe.hugsward.model.BookCart;
import org.gbe.hugsward.model.Offer;
import org.gbe.hugsward.model.OfferList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class CheckoutSummaryFragment extends Fragment {

    private static final String CART_KEY = "CartKey";
    private BookCart mBookCart;


    private HenriPotierApi mHenriPotierService;

    @Bind(R.id.checkout_summary_listview)
    ListView lvCheckoutSummary;

    FooterViewHolder mFooter;

    private OfferList mOfferList;
    private Offer mBestOffer;
    private float mTotalPrice;
    private float mFinalPrice;

    private BookCartAdapter mAdapter;

    public CheckoutSummaryFragment() {
    }

    public static CheckoutSummaryFragment newInstance(BookCart cart) {
        Bundle b = new Bundle();
        b.putParcelable(CART_KEY, cart);
        CheckoutSummaryFragment f = new CheckoutSummaryFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            mBookCart = getArguments().getParcelable(CART_KEY);
        } else {
            mBookCart = savedInstanceState.getParcelable(CART_KEY);
        }
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        View footer = inflater.inflate(R.layout.cart_summary_footer, container, false);
        mFooter = new FooterViewHolder(footer);
        footer.setTag(mFooter);
        ButterKnife.bind(this, v);
        mAdapter = new BookCartAdapter(getActivity(), R.layout.cart_summary_item, mBookCart);
        lvCheckoutSummary.setAdapter(mAdapter);
        lvCheckoutSummary.addFooterView(footer);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mHenriPotierService = new Retrofit.Builder()
                .baseUrl("http://henri-potier.xebia.fr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(HenriPotierApi.class);
        populateFooterInfo();
    }

    private void populateFooterInfo() {
//        mFooter.findViewById()
        mTotalPrice = mBookCart.getTotalPrice();
        mFooter.tvTotal.setText(String.format( "%.2f €", mTotalPrice ));
        mFooter.tvDiscount.setText("Looking for the best discount...");
        mFooter.tvFinalPrice.setText(String.format("%.2f €", mTotalPrice));
        if (mBookCart.isEmpty()) {
            mFooter.tvDiscount.setText("");
        }else {
            fetchOfferList();
        }
    }

    private void fetchOfferList() {
        Call<OfferList> call = mHenriPotierService.getOffer(mBookCart.getRequestString());
        call.enqueue(new Callback<OfferList>() {
            @Override
            public void onResponse(Response<OfferList> response) {
                mOfferList = response.body();
                applyBestOffer();
            }

            @Override
            public void onFailure(Throwable t) {
                showConnexionError();
            }
        });
    }

    private void showConnexionError() {
        mFooter.tvDiscount.setText(getResources().getString(R.string.connexion_error_label));
    }

    private void applyBestOffer() {
        mBestOffer = mOfferList.findBestOffer(mTotalPrice);
        mFinalPrice = mBestOffer.applyDiscount(mTotalPrice);
        mFooter.tvDiscount.setText("Discount applied : " + mBestOffer.getDescriptionString());
        mFooter.tvFinalPrice.setText(String.format("%.2f €", mFinalPrice));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CART_KEY, mBookCart);
    }

    public static class FooterViewHolder {
        @Bind(R.id.tvTotalPrice)
        TextView tvTotal;
        @Bind(R.id.tvAppliedDiscount)
        TextView tvDiscount;
        @Bind(R.id.tvFinalPrice)
        TextView tvFinalPrice;

        public FooterViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
