package com.swifties.bahceden.data.deserializers;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CustomerApi;
import com.swifties.bahceden.data.apis.ProducerApi;
import com.swifties.bahceden.data.apis.ProductApi;
import com.swifties.bahceden.models.Comment;
import com.swifties.bahceden.models.Customer;
import com.swifties.bahceden.models.Product;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentDeserializer implements JsonDeserializer<Comment> {
    @Override
    public Comment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (json.isJsonPrimitive() || (json.isJsonObject() && json.getAsJsonObject().size() == 1))
        {
            return null;
        }
        Comment c = new Comment();
        JsonObject jObj= json.getAsJsonObject();
        c.setId(jObj.get("id").getAsInt());
        Product p = new Product();
        p.setId(jObj.get("product").getAsInt());
        if (jObj.get("productName") != null)
            p.setName(jObj.get("productName").getAsString());
        c.setProduct(p);
        JsonElement author = jObj.get("author");
        if (jObj.get("rating") != null && !jObj.get("rating").isJsonNull())
            c.setRatingGiven((int)jObj.get("rating").getAsDouble());
        if (author.isJsonObject()) {
            JsonObject authorObj = author.getAsJsonObject();
            Customer authorCustomer = new Customer();
            authorCustomer.setId(authorObj.get("id").getAsInt());
            authorCustomer.setName(authorObj.get("name").getAsString());
            authorCustomer.setEmail(authorObj.get("email").getAsString());
            authorCustomer.setProfileImageURL(authorObj.get("profileImageURL").getAsString());
            c.setAuthor(authorCustomer);
        }



        c.setMessage(jObj.get("message").getAsString());
        c.setCountOfLikes(jObj.get("countOfLikes").getAsInt());
        return c;
    }
}
