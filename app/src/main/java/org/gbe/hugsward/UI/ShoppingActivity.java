package org.gbe.hugsward.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import org.gbe.hugsward.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

import org.gbe.hugsward.http.HenriPotierApi;
import org.gbe.hugsward.http.HenriPotierSvc;
import org.gbe.hugsward.model.Book;
import org.gbe.hugsward.model.BookCart;
import org.gbe.hugsward.model.Dummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {

    private static final String BOOK_CART_KEY = "BookCart";
    private static final String BOOK_LIST_KEY = "BookList";
    private static final String CURRENT_PAGE_KEY = "CurrentPage";

    @Bind(R.id.shopping_main_pager)
    ViewPager mPager;

    private BookCardFragmentPagerAdapter mAdapter;

    private Book[] mBooks;
    private BookCart mCart;

    // Retrofit Call object is set as private member to allow cancellation.
    private Call<List<Book>> mCall;
    private NetworkStatusReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        ButterKnife.bind(this);
        int current_page = 0;
        if(savedInstanceState == null) {
            mCart = new BookCart();
            mAdapter = new BookCardFragmentPagerAdapter(getSupportFragmentManager(), mBooks, mCart);
            mPager.setAdapter(mAdapter);
            mPager.setPageTransformer(false, new ZoomOutPageTransformer());
            mPager.setOffscreenPageLimit(5);
        }
        else {
            mCart = savedInstanceState.getParcelable(BOOK_CART_KEY);
            mBooks = (Book[])savedInstanceState.getParcelableArray(BOOK_LIST_KEY);
            current_page = savedInstanceState.getInt(CURRENT_PAGE_KEY, 0);
            mAdapter = new BookCardFragmentPagerAdapter(getSupportFragmentManager(), mBooks, mCart);
            mPager.setAdapter(mAdapter);
            mPager.setPageTransformer(false, new ZoomOutPageTransformer());
            mPager.setOffscreenPageLimit(5);
        }

        if (isNetworkAvailable()) {
            fetchBookList();
        } else {
            onConnectionFailed();
        }

        mPager.setCurrentItem(current_page, true);
    }

    private void fetchBookList() {
        HenriPotierApi svc = HenriPotierSvc.getInstance();

        mCall = svc.getBooks();
        mCall.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Response<List<Book>> response) {
                mBooks = response.body().toArray(new Book[response.body().size()]);
                mAdapter.setData(mBooks, mCart);
            }

            @Override
            public void onFailure(Throwable t) {
                onConnectionFailed();
            }
        });
    }

    private void onConnectionFailed() {
        mBooks = new Book[]{null};
        mAdapter.setData(mBooks, mCart);
        Toast.makeText(getBaseContext(), "Internet access failed. Please try again later.", Toast.LENGTH_LONG).show();
        setNetworkStatusListener();
    }



    @Override
    public void onSaveInstanceState(Bundle b){
        super.onSaveInstanceState(b);
        b.putParcelable(BOOK_CART_KEY, mCart);
        b.putParcelableArray(BOOK_LIST_KEY, mBooks);
        b.putInt(CURRENT_PAGE_KEY, mPager.getCurrentItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_checkout) {
            Intent i = new Intent();
            i.setClass(this, CheckoutActivity.class);
            i.putExtra(BOOK_CART_KEY, mCart);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (mCall != null) {
            mCall.cancel();
        }
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    private void setNetworkStatusListener() {
        if(mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        mReceiver = new NetworkStatusReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class NetworkStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            if(isNetworkAvailable()) {
                fetchBookList();
            }
        }
    }
}
