package com.swifties.bahceden.uiclasses;

import com.swifties.bahceden.models.Category;
import com.swifties.bahceden.models.Product;

public class SpinnerCustomItem {

    private Category category;
    private Product.UnitType unitType;

    public Product.UnitType getUnitType() {
        return unitType;
    }

    public SpinnerCustomItem(Product.UnitType unitType, int itemIcon) {
        this.unitType = unitType;
        ItemIcon = itemIcon;
    }

    private String name;
    private int ItemIcon;

    public SpinnerCustomItem(Category category, int itemIcon) {
        this.category = category;
        ItemIcon = itemIcon;
    }

    public SpinnerCustomItem(String name, int itemIcon) {
        this.name = name;
        ItemIcon = itemIcon;
    }

    public String getItemName() {
        if (category != null)
            return category.getName();
        if (unitType != null)
            return unitType.name();
        return name;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getItemIcon() {
        return ItemIcon;
    }
}
