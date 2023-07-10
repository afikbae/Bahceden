package com.swifties.bahceden.data.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CustomerApi;
import com.swifties.bahceden.models.Category;
import com.swifties.bahceden.models.Comment;
import com.swifties.bahceden.models.Customer;
import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.Product;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDeserializer implements JsonDeserializer<Product> {
    @Override
    public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Product p = new Product();
        JsonObject jObj = json.getAsJsonObject();

        p.setId(jObj.get("id").getAsInt());
        p.setName(jObj.get("name").getAsString());
        if (jObj.get("description") != null && !jObj.get("description").isJsonNull())
            p.setDescription(jObj.get("description").getAsString());
        p.setCategory(context.deserialize(jObj.get("category"), Category.class));
        p.setUnitType(context.deserialize(jObj.get("unitType"), Product.UnitType.class));
        p.setPricePerUnit(jObj.get("pricePerUnit").getAsInt());
        p.setProducer(context.deserialize(jObj.get("producer"), Producer.class));
        if (!jObj.get("imageURL").isJsonNull())
            p.setImageURL(jObj.get("imageURL").getAsString());
        p.setAmountInStock(jObj.get("availableAmount").getAsInt());
        if (!jObj.get("rating").isJsonNull())
            p.setRating(jObj.get("rating").getAsDouble());

        JsonElement commentObjs = jObj.get("comments");

        if (commentObjs != null && commentObjs.isJsonArray())
        {
            List<Comment> comments = new ArrayList<>();
            p.setComments(comments);
            for (JsonObject commentObject : commentObjs.getAsJsonArray().asList().stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList()))
            {
                Comment c = new Comment();
                comments.add(c);
                c.setId(commentObject.get("id").getAsInt());
                c.setProduct(p);
                c.setMessage(commentObject.get("message").getAsString());
                c.setCountOfLikes(commentObject.get("countOfLikes").getAsInt());
                if (commentObject.get("rating") != null && !commentObject.get("rating").isJsonNull())
                    c.setRatingGiven((int)commentObject.get("rating").getAsDouble());
                JsonElement parentElement = commentObject.get("parent");
                if (parentElement != null && !parentElement.isJsonNull())
                {
                    int parentId = parentElement.getAsInt();
                    for (Comment comm: comments)
                    {
                        if (comm.getId() == parentId)
                            comm.setChildComment(c);
                    }
                }
                JsonElement author = commentObject.get("author");
                if (author.isJsonObject())
                {
                    JsonObject authorObj = author.getAsJsonObject();
                    Customer authorCustomer = new Customer();
                    authorCustomer.setId(authorObj.get("id").getAsInt());
                    authorCustomer.setName(authorObj.get("name").getAsString());
                    authorCustomer.setEmail(authorObj.get("email").getAsString());
                    if (authorObj.get("profileImageURL") != null && !authorObj.get("profileImageURL").isJsonNull())
                        authorCustomer.setProfileImageURL(authorObj.get("profileImageURL").getAsString());
                    else
                        authorCustomer.setProfileImageURL("http://10.0.2.2:8080/images/noProfile.png");
                    c.setAuthor(authorCustomer);
                }
                else if (author.isJsonPrimitive() && author.getAsJsonPrimitive().getAsInt() == -1)
                {
                    c.setAuthor(p.getProducer());
                }
            }
        }

        return p;
    }
}
