package com.swifties.bahceden.data.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.swifties.bahceden.models.Comment;
import com.swifties.bahceden.models.Customer;

import java.lang.reflect.Type;

public class CommentSerializer implements JsonSerializer<Comment> {
    @Override
    public JsonElement serialize(Comment src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("product", src.getProduct().getId());
        if (src.getParent() != null)
            obj.addProperty("parent", src.getParent().getId());
        if (src.getAuthor() instanceof Customer)
            obj.addProperty("author", src.getAuthor().getId());
        else
            obj.addProperty("author", -1);
        obj.addProperty("countOfLikes", src.getCountOfLikes());
        obj.addProperty("message", src.getMessage());
        obj.addProperty("rating", src.getRatingGiven());

        return obj;
    }
}
