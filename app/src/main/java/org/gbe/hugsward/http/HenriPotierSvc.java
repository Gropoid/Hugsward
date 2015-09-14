package org.gbe.hugsward.http;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by gbe on 9/14/15.
 */
public class HenriPotierSvc {

    private static HenriPotierApi mHenriPotierSvc;

    public static synchronized HenriPotierApi getInstance() {
        if (mHenriPotierSvc == null)
        {
            return new Retrofit.Builder()
                    .baseUrl("http://henri-potier.xebia.fr/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(HenriPotierApi.class);
        } else {
            return mHenriPotierSvc;
        }
    }
}
