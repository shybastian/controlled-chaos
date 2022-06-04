package edu.seb.controlled.core.entity;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class UserDeserializer implements JsonDeserializer<User> {
    List<String> requieredFields = Arrays.asList("id", "username");


    @Override
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        for (String fieldName : this.requieredFields) {
            if (object.get(fieldName) == null) {
                throw new JsonParseException("Required Field not found: " + fieldName);
            }
        }

        return new Gson().fromJson(object, User.class);
    }
}
