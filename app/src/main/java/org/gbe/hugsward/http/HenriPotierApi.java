package org.gbe.hugsward.http;

import org.gbe.hugsward.model.Book;
import org.gbe.hugsward.model.Offer;
import org.gbe.hugsward.model.OfferList;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by gbe on 9/12/15.
 */
public interface HenriPotierApi {
    @GET("/books/{isbnMap}/commercialOffers")
    Call<OfferList> getOffer(@Path("isbnMap") String isbnList);

    @GET("/books")
    Call<List<Book>> getBooks();
}
