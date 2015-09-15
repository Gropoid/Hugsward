package org.gbe.hugsward.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gbe on 9/12/15.
 *  This class represents a list of offers, and can be deserialized from
 *  {@link org.gbe.hugsward.http.HenriPotierApi#getOffers(String)}
 */
public class OfferList {

    private List<Offer> offers;

    public OfferList(Offer[] offers) {
        this.offers = Arrays.asList(offers);
    }

    public OfferList(List<Offer> offers) {
        this.offers = offers;
    }

    /**
     * For a given total price, calculates which offer in this {@link OfferList} is the most
     * interesting.
     * @param price a price to make calculations on.
     * @return The best offer in this {@link OfferList} for the provided price or null if there is
     * no offer in the {@link OfferList}
     */
    public Offer findBestOffer(float price) {
        if(offers == null || offers.size() == 0)
            return null;
        float bestPrice = price;
        Offer bestOffer = null;
        for(Offer o : offers) {
            float p = o.applyDiscount(price);
            if (p < bestPrice) {
                bestPrice = p;
                bestOffer = o;
            }
        }
        return bestOffer;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
