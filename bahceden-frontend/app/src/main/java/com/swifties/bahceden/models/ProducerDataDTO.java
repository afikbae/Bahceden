package com.swifties.bahceden.models;

public class ProducerDataDTO {
    private Double marketPriceMIN;
    private Double marketPriceAVG;
    private Double marketPriceMAX;
    private Double sellersMIN;
    private Double sellersAVG;
    private Double sellersMAX;
    private Double recommendedPrice;

    public Double getMarketPriceMIN() {
        return marketPriceMIN;
    }

    public void setMarketPriceMIN(Double marketPriceMIN) {
        this.marketPriceMIN = marketPriceMIN;
    }

    public Double getMarketPriceAVG() {
        return marketPriceAVG;
    }

    public void setMarketPriceAVG(Double marketPriceAVG) {
        this.marketPriceAVG = marketPriceAVG;
    }

    public Double getMarketPriceMAX() {
        return marketPriceMAX;
    }

    public void setMarketPriceMAX(Double marketPriceMAX) {
        this.marketPriceMAX = marketPriceMAX;
    }

    public Double getSellersMIN() {
        return sellersMIN;
    }

    public void setSellersMIN(Double sellersMIN) {
        this.sellersMIN = sellersMIN;
    }

    public Double getSellersAVG() {
        return sellersAVG;
    }

    public void setSellersAVG(Double sellersAVG) {
        this.sellersAVG = sellersAVG;
    }

    public Double getSellersMAX() {
        return sellersMAX;
    }

    public void setSellersMAX(Double sellersMAX) {
        this.sellersMAX = sellersMAX;
    }

    public Double getRecommendedPrice() {
        return recommendedPrice;
    }

    public void setRecommendedPrice(Double recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }
}
