package org.gbe.hugsward.UI;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.gbe.hugsward.R;
import org.gbe.hugsward.http.HenriPotierApi;
import org.gbe.hugsward.http.HenriPotierSvc;
import org.gbe.hugsward.model.BookCart;
import org.gbe.hugsward.model.Offer;
import org.gbe.hugsward.model.OfferList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

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

    private Call<OfferList> mCall;
    private NetworkStatusReceiver mReceiver;

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
        mHenriPotierService = HenriPotierSvc.getInstance();
        populateFooterInfo();
    }

    private void populateFooterInfo() {
        mTotalPrice = mBookCart.getTotalPrice();
        mFooter.tvTotal.setText(String.format( "%.2f €", mTotalPrice ));
        mFooter.tvDiscount.setText("Looking for the best discount...");
        mFooter.tvFinalPrice.setText(String.format("%.2f €", mTotalPrice));
        if (mBookCart.isEmpty()) {
            mFooter.tvDiscount.setText("");
        }else {
            if(isNetworkAvailable()) {
                fetchOfferList();
            } else {
                setNetworkStatusListener();  //TODO: (Context, Callback)
            }
        }
    }

    private void fetchOfferList() {

        mCall = mHenriPotierService.getOffers(mBookCart.getRequestString());
        mCall.enqueue(new Callback<OfferList>() {
            @Override
            public void onResponse(Response<OfferList> response) {
                mOfferList = response.body();
                applyBestOffer();
                unSetNetworkStatusListener();
            }

            @Override
            public void onFailure(Throwable t) {
                setNetworkStatusListener();
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

    @Override
    public void onDestroy() {
        if(mCall != null) {
            mCall.cancel();
        }
        unSetNetworkStatusListener();
        super.onDestroy();
    }

    private void unSetNetworkStatusListener() {
        if(mReceiver != null) {
            try {
                getActivity().unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException ex) {
                // Do nothing, we just want to make sure it's not registered anymore.
            }
        }
    }

    // TODO: Extract into a class
    private void setNetworkStatusListener() {
        unSetNetworkStatusListener();
        mReceiver = new NetworkStatusReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class NetworkStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            if(isNetworkAvailable()) {
                fetchOfferList();
            }
        }
    }
}
