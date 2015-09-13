package org.gbe.hugsward.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.gbe.hugsward.model.Book;
import org.gbe.hugsward.model.BookCart;

/**
 * Created by gbe on 9/10/15.
 */
public class BookCardFragmentPagerAdapter extends FragmentPagerAdapter {

    Book[] mBooks;
    BookCart mCart;

    public BookCardFragmentPagerAdapter(FragmentManager fm, Book[] books, BookCart cart) {
        super(fm);
        mBooks = books;
        mCart = cart;
    }

    @Override
    public Fragment getItem(int position) {
        return BookCardFragment.newInstance(mBooks[position], mCart);
    }

    @Override
    public int getCount() {
        return mBooks.length;
    }


}
