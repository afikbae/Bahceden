package com.swifties.bahceden.models;

import android.content.Context;

import com.swifties.bahceden.R;

public class ProductInformation {
    private Product product;
    private double amountOfSales;
    private double earnings;

    public ProductInformation(Product product, double amountOfSales, double earnings) {
        this.product = product;
        this.amountOfSales = amountOfSales;
        this.earnings = earnings;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getAmountOfSales() {
        return amountOfSales;
    }

    public void setAmountOfSales(double amountOfSales) {
        this.amountOfSales = amountOfSales;
    }

    public void increaseAmountOfSales(double offset) {
        this.amountOfSales += offset;
    }

    public void increaseEarnings(double offset) {
        this.earnings += offset;
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public String getEarningsInString() {
        return earnings + " ₺";
    }
    public String getAmountSoldInString() {
        Product.UnitType unitType = product.getUnitType();
        if (unitType == Product.UnitType.KILOGRAMS) {
            return amountOfSales + " KG";
        } else if (unitType == Product.UnitType.LITERS) {
            return amountOfSales + " L";
        }
        else {
            return amountOfSales + " Packages";
        }
    }
}
