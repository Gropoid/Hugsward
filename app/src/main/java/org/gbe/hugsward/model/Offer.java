package org.gbe.hugsward.model;

/**
 * Created by gbe on 9/12/15.
 *  An offer as returned by {@link org.gbe.hugsward.http.HenriPotierApi#getOffers(String)}
 */
public class Offer {
    private String type;
    private Integer value;
    private Integer sliceValue;

    private transient DiscountCalculator calculator;


    /**
     * calculate the discounted price
     * @param price a price to make the calculation on.
     * @return the discounted price after applying this {@link Offer}'s discount.
     */
    public float applyDiscount(float price){
        if (calculator == null)
            setDiscountCalculator();
        return calculator.calculateDiscount(price);
    }

    /**
     * provides a descriptive string for this {@link Offer}
     * @return a short string, such as "- 15%"
     */
    public String getDescriptionString() {
        if (calculator == null)
            setDiscountCalculator();
        return calculator.getDescriptionString();
    }

    private void setDiscountCalculator() {
        if (null == type)
            throw new NullPointerException("Type is not set in this Offer");
        switch(type) {
            case "percentage":
                calculator = new PercentageDiscountCalculator();
                break;
            case "minus":
                calculator = new MinusDiscountCalculator();
                break;
            case "slice":
                calculator = new SliceDiscountCalculator();
                break;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getSliceValue() {
        return sliceValue;
    }

    public void setSliceValue(Integer sliceValue) {
        this.sliceValue = sliceValue;
    }

    private interface DiscountCalculator {
        float calculateDiscount(float price);
        String getDescriptionString();
    }

    private class PercentageDiscountCalculator implements DiscountCalculator{
        @Override
        public float calculateDiscount(float price) {
            if (value == null)
                throw new NullPointerException("No value is set in the Offer");
            return price * (100 - value) / 100;
        }

        @Override
        public String getDescriptionString() {
            if (value == null)
                throw new NullPointerException("No value is set in the Offer");
            return "- " + value.toString() + "%";
        }
    }

    private class MinusDiscountCalculator implements DiscountCalculator {
        @Override
        public float calculateDiscount(float price) {
            if (value == null)
                throw new NullPointerException("No value is set in the Offer");
            return price - value;
        }

        @Override
        public String getDescriptionString() {
            if (value == null)
                throw new NullPointerException("No value is set in the Offer");
            return "- " + value + "€";
        }
    }

    private class SliceDiscountCalculator implements DiscountCalculator {
        @Override
        public float calculateDiscount(float price) {
            if (value == null)
                throw new NullPointerException("No value is set in the Offer");
            if (sliceValue == null)
                throw new NullPointerException("No slice value is set in the Offer");
            float noDiscountSlice = price % sliceValue;
            int nSlices = (int)price / sliceValue;
            return nSlices*(sliceValue-value) +  noDiscountSlice;
        }

        @Override
        public String getDescriptionString() {
            if (value == null)
                throw new NullPointerException("No value is set in the Offer");
            if (sliceValue == null)
                throw new NullPointerException("No slice value is set in the Offer");
            return "- " + value + "€ /" + sliceValue + " €";
        }
    }

}
