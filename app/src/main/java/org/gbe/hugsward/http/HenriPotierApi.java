package org.gbe.hugsward.http;

import org.gbe.hugsward.model.Book;
import org.gbe.hugsward.model.OfferList;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by gbe on 9/12/15.
 *  Retrofit interface for the Henri Potier web API
 */
public interface HenriPotierApi {
    @GET("/books/{isbnMap}/commercialOffers")
    Call<OfferList> getOffers(@Path("isbnMap") String isbnList);

    @GET("/books")
    Call<List<Book>> getBooks();
}
