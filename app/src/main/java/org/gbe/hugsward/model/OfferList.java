package org.gbe.hugsward.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gbe on 9/12/15.
 */
public class OfferList {

    private List<Offer> offers;

    public OfferList(Offer[] offers) {
        this.offers = Arrays.asList(offers);
    }

    public OfferList(List<Offer> offers) {
        this.offers = offers;
    }

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
