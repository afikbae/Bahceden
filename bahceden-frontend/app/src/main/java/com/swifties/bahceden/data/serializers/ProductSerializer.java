package com.swifties.bahceden.data.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.swifties.bahceden.models.Product;

import java.lang.reflect.Type;

public class ProductSerializer implements JsonSerializer<Product> {
    @Override
    public JsonElement serialize(Product src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("name", src.getName());
        obj.addProperty("producer", src.getProducer().getId());
        obj.addProperty("description", src.getDescription());
        obj.addProperty("rating", src.getRating());
        obj.addProperty("unitType", src.getUnitType().getValue());
        obj.addProperty("category", src.getCategory().getId());
        obj.addProperty("subCategory", src.getSubCategory().getId());
        obj.addProperty("pricePerUnit", src.getPricePerUnit());
        obj.addProperty("availableAmount", src.getAmountInStock());

        return obj;
    }
}
