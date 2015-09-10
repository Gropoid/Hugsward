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

public class ShoppingActivity extends AppCompatActivity {

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
        if(savedInstanceState == null) {
            mCart = new BookCart();
            Gson gson = new Gson();
            mBooks = gson.fromJson(Dummy.JsonAllBooks, Book[].class);

            mAdapter = new BookCardFragmentPagerAdapter(getSupportFragmentManager(), mBooks, mCart);
            mPager.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
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
