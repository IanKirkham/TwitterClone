package edu.byu.cs.tweeter.client.model.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;


public class JsonSerializer {

    public static String serialize(Object requestInfo) {
        return (new Gson()).toJson(requestInfo);
    }

    public static <T> T deserialize(String value, Class<T> returnType) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        JsonObject jo = json.getAsJsonObject();
                        return LocalDateTime.of(jo.get("year").getAsInt(),
                                jo.get("monthValue").getAsInt(),
                                jo.get("dayOfMonth").getAsInt(),
                                jo.get("hour").getAsInt(),
                                jo.get("minute").getAsInt(),
                                jo.get("second").getAsInt(),
                                jo.get("nano").getAsInt());
                    }
                }).create();

//        return (new Gson()).fromJson(value, returnType);
        return gson.fromJson(value, returnType);
    }
}
