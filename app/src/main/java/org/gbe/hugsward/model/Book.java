package org.gbe.hugsward.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.gbe.hugsward.http.HenriPotierApi;

/**
 * Created by gbe on 9/9/15.
 *  Parcelable class to represent a Henri Potier book. Can be deserialized from {@link HenriPotierApi#getBooks()}.
 */
public class Book implements Parcelable {
    private String isbn;
    private String title;
    private int price;
    private String cover;

    @Override
    public int describeContents() {
        return 0;
    }

    private Book(Parcel p) {
        isbn = p.readString();
        title = p.readString();
        cover = p.readString();
        price = p.readInt();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(isbn);
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeInt(price);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
