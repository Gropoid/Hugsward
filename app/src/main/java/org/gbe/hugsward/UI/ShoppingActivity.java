package org.gbe.hugsward.UI;

import android.content.Intent;
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
import org.gbe.hugsward.model.Book;
import org.gbe.hugsward.model.BookCart;
import org.gbe.hugsward.model.Dummy;

public class ShoppingActivity extends AppCompatActivity {

    private static final String BOOK_CART_KEY = "BookCart";
    private static final String BOOK_LIST_KEY = "BookList";
    private static final String CURRENT_PAGE_KEY = "CurrentPage";

    @Bind(R.id.shopping_main_pager)
    ViewPager mPager;

    BookCardFragmentPagerAdapter mAdapter;

    Book[] mBooks;
    BookCart mCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        ButterKnife.bind(this);
        int current_page = 0;
        if(savedInstanceState == null) {
            mCart = new BookCart();
            Gson gson = new Gson();
            mBooks = gson.fromJson(Dummy.JsonAllBooks, Book[].class);
        }
        else {
            mCart = savedInstanceState.getParcelable(BOOK_CART_KEY);
            mBooks = (Book[])savedInstanceState.getParcelableArray(BOOK_LIST_KEY);
            current_page = savedInstanceState.getInt(CURRENT_PAGE_KEY, 0);
        }
        mAdapter = new BookCardFragmentPagerAdapter(getSupportFragmentManager(), mBooks, mCart);

        mPager.setAdapter(mAdapter);
        mPager.setPageTransformer(false, new ZoomOutPageTransformer());
        mPager.setOffscreenPageLimit(1);
        mPager.setCurrentItem(current_page, true);
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
        // Inflate the menu; this adds items to the action bar if it is present.
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

}
