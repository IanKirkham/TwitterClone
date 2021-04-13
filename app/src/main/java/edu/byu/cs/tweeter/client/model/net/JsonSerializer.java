package edu.byu.cs.tweeter.client.model.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.net.GsonFactory;

public class JsonSerializer {

    public static String serialize(Object requestInfo) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        return gson.toJson(requestInfo);
    }

    public static <T> T deserialize(String value, Class<T> returnType) {
        Gson gson = GsonFactory.gson;

        return gson.fromJson(value, returnType);
    }


    static class LocalDateTimeAdapter implements com.google.gson.JsonSerializer<LocalDateTime> {
        public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject root = new JsonObject();

            JsonObject date = new JsonObject();
            date.add("year", new JsonPrimitive(localDateTime.getYear()));
            date.add("month", new JsonPrimitive(localDateTime.getMonthValue()));
            date.add("day", new JsonPrimitive(localDateTime.getDayOfMonth()));

            JsonObject time = new JsonObject();
            time.add("hour", new JsonPrimitive(localDateTime.getHour()));
            time.add("minute", new JsonPrimitive(localDateTime.getMinute()));
            time.add("second", new JsonPrimitive(localDateTime.getSecond()));
            time.add("nano", new JsonPrimitive(localDateTime.getNano()));

            root.add("date", date);
            root.add("time", time);
            return root;
        }
    }
}
