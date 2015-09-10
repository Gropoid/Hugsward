package org.gbe.hugsward;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import model.Book;
import model.BookCardFragmentPagerAdapter;
import model.BookCart;
import model.Dummy;
import model.ZoomOutPageTransformer;

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
        mPager.setOffscreenPageLimit(5);
        mPager.setCurrentItem(current_page);
    }

    @Override
    public void onSaveInstanceState(Bundle b){
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
