package org.gbe.hugsward.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.gbe.hugsward.R;
import org.gbe.hugsward.http.HenriPotierApi;
import org.gbe.hugsward.http.HenriPotierSvc;
import org.gbe.hugsward.model.Book;
import org.gbe.hugsward.model.BookCart;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


/**
 * Hugsward's main activity. Displays {@link BookCardFragment}s via a {@link ViewPager}.
 */
public class ShoppingActivity extends AppCompatActivity {

    private static final String BOOK_CART_KEY = "BookCart";
    private static final String BOOK_LIST_KEY = "BookList";
    private static final String CURRENT_PAGE_KEY = "CurrentPage";

    @BindView(R.id.shopping_main_pager)
    ViewPager mPager;

    private BookCardFragmentPagerAdapter mAdapter;

    private Book[] mBooks;
    private BookCart mCart;

    // Retrofit Call object is set as member to allow cancellation.
    private Call<List<Book>> mCall;
    private NetworkStatusMonitor mNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        ButterKnife.bind(this);
        int current_page = 0;
        mNetwork = new NetworkStatusMonitor(this, new Runnable() {
            @Override
            public void run() {
                fetchBookList();
            }
        });
        if (savedInstanceState == null) {
            mCart = new BookCart();
            mAdapter = new BookCardFragmentPagerAdapter(getSupportFragmentManager(), mBooks, mCart);
            mPager.setAdapter(mAdapter);
            mPager.setPageTransformer(false, new ZoomOutPageTransformer());
            mPager.setOffscreenPageLimit(5);
        } else {
            mCart = savedInstanceState.getParcelable(BOOK_CART_KEY);
            mBooks = (Book[]) savedInstanceState.getParcelableArray(BOOK_LIST_KEY);
            current_page = savedInstanceState.getInt(CURRENT_PAGE_KEY, 0);
            mAdapter = new BookCardFragmentPagerAdapter(getSupportFragmentManager(), mBooks, mCart);
            mPager.setAdapter(mAdapter);
            mPager.setPageTransformer(false, new ZoomOutPageTransformer());
            mPager.setOffscreenPageLimit(5);
        }

        mPager.setCurrentItem(current_page, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNetwork.isNetworkAvailable()) {
            fetchBookList();
        } else {
            onConnectionFailed();
        }
    }

    private void fetchBookList() {
        HenriPotierApi svc = HenriPotierSvc.getInstance();

        mCall = svc.getBooks();
        mCall.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Response<List<Book>> response) {
                mBooks = response.body().toArray(new Book[response.body().size()]);
                mCart.refresh(mBooks);
                mAdapter.setData(mBooks, mCart);
                mNetwork.unsetListener();
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
        mNetwork.setListener();
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
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
        // Cancel Retrofit call and unregister the Broadcast receiver
        if (mCall != null) {
            mCall.cancel();
        }
        mNetwork.unsetListener();
        super.onDestroy();
    }


}
