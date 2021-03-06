package org.gbe.hugsward.http;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by gbe on 9/14/15.
 *  Retrofit-based HTTP service to access Henri Potier's API.
 */
public class HenriPotierSvc {

    private static final String BASE_URL = "http://henri-potier.xebia.fr/";
    private static HenriPotierApi mHenriPotierSvc = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(HenriPotierApi.class);

    public static HenriPotierApi getInstance() {
        return mHenriPotierSvc;
    }
}
