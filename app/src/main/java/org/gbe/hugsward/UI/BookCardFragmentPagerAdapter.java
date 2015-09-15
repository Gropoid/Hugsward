package org.gbe.hugsward.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.gbe.hugsward.model.Book;
import org.gbe.hugsward.model.BookCart;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gbe on 9/10/15.
 */
public class BookCardFragmentPagerAdapter extends FragmentPagerAdapter {

    private Book[] mBooks;
    private BookCart mCart;

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
    public int getItemPosition(Object object) {
        BookCardFragment f;
        try
        {
            f = (BookCardFragment)object;
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(object.toString() + " must be a BookCardFragment");
        }
        Book b = f.getBook();
        if (b == null) {
            if (mBooks[0] == null) {
                return POSITION_UNCHANGED;
            }
            else {
                return POSITION_NONE;
            }
        }
        List<Book> list = Arrays.asList(mBooks);
        int pos = list.indexOf(b);

        return (pos >= 0) ? pos : POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (mBooks == null)
            return 0;
        return mBooks.length;
    }

    @Override
    public long getItemId(int position) {
        if(mBooks[position] == null) return 0;
        return (long)mBooks[position].hashCode();
    }

    public void setData(Book[] books, BookCart cart) {
        mBooks = books;
        mCart = cart;
        notifyDataSetChanged();
    }
}
