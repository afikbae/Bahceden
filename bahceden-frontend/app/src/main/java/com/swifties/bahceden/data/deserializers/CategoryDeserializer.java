package com.swifties.bahceden.data.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.swifties.bahceden.models.Category;

import java.lang.reflect.Type;

public class CategoryDeserializer implements JsonDeserializer<Category> {
    @Override
    public Category deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String categoryName = json.getAsJsonObject().get("name").getAsString();
        int categoryId = json.getAsJsonObject().get("id").getAsInt();
        if (json.getAsJsonObject().get("parent").isJsonNull())
            return Category.getCategory(categoryId, categoryName, 0);
        else
            return Category.getCategory(categoryId, categoryName, json.getAsJsonObject().get("parent").getAsInt());
    }
}
