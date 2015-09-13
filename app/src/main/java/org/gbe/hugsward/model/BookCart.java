package org.gbe.hugsward.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by gbe on 9/10/15.
 */
public class BookCart implements Parcelable {
    private static final int MAX_ORDER = 10;
    Map<Book, Integer> mCart;

    public BookCart() {
        mCart = new Hashtable<>();
    }

    public Boolean isEmpty() {
        return mCart.size() == 0;
    }

    protected BookCart(Parcel in) {
        mCart = new Hashtable<>();
        Integer size = in.readInt();
        for (int i = 0 ; i < size ; i++) {
            Book b = in.readParcelable(Book.class.getClassLoader());
            Integer qty = in.readInt();
            mCart.put(b, qty);
        }
    }

    public Boolean isAtMaxOrdered(Book b) {
        return mCart.containsKey(b) && mCart.get(b) >= MAX_ORDER;
    }

    public Boolean isAtMinOrdered(Book b){
        return mCart.get(b) == null;
    }

    public void add(Book b) {
        if (!mCart.containsKey(b)) {
            mCart.put(b, 1);
        }
        else {
            if (!isAtMaxOrdered(b)) {
                mCart.put(b, mCart.get(b) + 1);
            }
        }
    }

    public void remove(Book b) {
        if (!mCart.containsKey(b)) return;
        if (!isAtMinOrdered(b)) {
            mCart.put(b, mCart.get(b) - 1);
            if (mCart.get(b) == 0) {
                mCart.remove(b);
            }
        }
    }

    public void update(Book book, Integer newQuantity){
        mCart.put(book, newQuantity);
        if(mCart.get(book) == 0 ) {
            mCart.remove(book);
        }
    }

    public Integer getQuantity(Book b) {
        if (mCart.containsKey(b)) {
            return mCart.get(b);
        }
        else {
            return 0;
        }
    }

    public String getRequestString() {
        String rv = new String();
        if (mCart.isEmpty()) {
            return "";
        } else {
            for(Book b : mCart.keySet()){
                int cnt = mCart.get(b);
                while(cnt>0){
                    rv += b.getIsbn() + "," ;
                    cnt--;
                }
            }
            rv = rv.substring(0, rv.length() - 1);  // remove ending comma
            return rv;
        }
    }

    public List<Pair<Book, Integer>> getCartContents() {
        List<Pair<Book, Integer>> list = new ArrayList<>();
        for(Map.Entry e : mCart.entrySet()) {
            list.add(new Pair<>((Book) e.getKey(), (Integer) e.getValue()));
        }
        return list;
    }

    public static final Creator<BookCart> CREATOR = new Creator<BookCart>() {
        @Override
        public BookCart createFromParcel(Parcel in) {
            return new BookCart(in);
        }

        @Override
        public BookCart[] newArray(int size) {
            return new BookCart[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCart.size());
        for(Map.Entry<Book, Integer> entry : mCart.entrySet()) {
            dest.writeParcelable(entry.getKey(), 0);
            dest.writeInt(entry.getValue());
        }
    }

    public float getTotalPrice() {
        float x = 0;
        for(Book b : mCart.keySet()) {
            x += (b.getPrice()) * mCart.get(b);
        }
        return x;
    }
}
