package com.maximumg9.g9utils.config;

import com.google.gson.*;
import net.minecraft.client.util.InputUtil;

import java.lang.reflect.Type;

public class BindingTypeAdapters {
    public static class BindingSerializer implements JsonSerializer<Keybind> {

        public BindingSerializer() {

        }

        @Override
        public JsonElement serialize(Keybind keybind, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject obj = new JsonObject();
            obj.addProperty("modifiers",keybind.modifiers);
            obj.addProperty("boundKey",keybind.key.getTranslationKey());
            return obj;
        }
    }

    public static class BindingDeserializer implements JsonDeserializer<Keybind> {
        public BindingDeserializer() {
        }

        @Override
        public Keybind deserialize(JsonElement json, Type keybindType, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            int modifiers = obj.get("modifiers").getAsInt();
            String boundKeyTranslationKey = obj.get("boundKey").getAsString();
            return new Keybind(InputUtil.fromTranslationKey(boundKeyTranslationKey),modifiers);
        }
    }
}
