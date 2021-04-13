package edu.byu.cs.tweeter.model.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class GsonFactory {
    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    JsonObject jo = json.getAsJsonObject();
                    if (jo.get("year") != null) {
                        return LocalDateTime.of(jo.get("year").getAsInt(),
                                jo.get("monthValue").getAsInt(),
                                jo.get("dayOfMonth").getAsInt(),
                                jo.get("hour").getAsInt(),
                                jo.get("minute").getAsInt(),
                                jo.get("second").getAsInt(),
                                jo.get("nano").getAsInt());
                    } else {
                        JsonObject d = jo.getAsJsonObject("date");
                        JsonObject t = jo.getAsJsonObject("time");
                        return LocalDateTime.of(d.get("year").getAsInt(),
                                d.get("month").getAsInt(),
                                d.get("day").getAsInt(),
                                t.get("hour").getAsInt(),
                                t.get("minute").getAsInt(),
                                t.get("second").getAsInt(),
                                t.get("nano").getAsInt());
                    }
                }
            }).create();
}
