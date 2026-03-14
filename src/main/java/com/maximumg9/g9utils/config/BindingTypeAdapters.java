package com.maximumg9.g9utils.config;

import com.google.gson.*;
import com.maximumg9.g9utils.Util;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class BindingTypeAdapters {

    /// private final String translationKey;
    /// private final InputUtil.Key defaultKey;
    /// private final String category;
    /// private InputUtil.Key boundKey;
    /// private boolean pressed;
    /// private int timesPressed;
    ///
    public static class BindingSerializer<O extends Options> implements JsonSerializer<KeyBinding> {

        public BindingSerializer() {

        }

        @Override
        public JsonElement serialize(KeyBinding keyBinding, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id",keyBinding.getId());
            obj.addProperty("boundKey",keyBinding.getBoundKeyTranslationKey());
            return null;
        }
    }

    public static class BindingDeserializer<O extends Options> implements JsonDeserializer<KeyBinding> {
        private final HashMap<InputUtil.Key,KeyBinding> customBindings = new HashMap<>();
        private final HashMap<String,KeyBinding> bindingsById = new HashMap<>();
        public BindingDeserializer(O options) {
            for(KeyBinding binding : getAllRecursive(options, KeyBinding.class)) {
                bindingsById.put(binding.getId(),binding);
                customBindings.put(binding.getDefaultKey(),binding);
            }
        }

        public HashMap<InputUtil.Key, KeyBinding> getCustomBindings() {
            return customBindings;
        }

        @Override
        public KeyBinding deserialize(JsonElement json, Type keyBindingType, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String bindID = obj.get("id").getAsString();
            String boundTranslationKey = obj.get("boundKey").getAsString();
            KeyBinding binding = bindingsById.get(bindID);
            if(binding == null) throw new IllegalStateException("Key binding does not have default conditions");
            binding.setBoundKey(InputUtil.fromTranslationKey(boundTranslationKey));
            return binding;
        }
    }

    private static <T,O extends Options> List<T> getAllRecursive(O opt, Class<T> searchClass) {
        Class<O> optClass = Util.getClassStrict(opt);
        ArrayList<T> list = new ArrayList<>();
        for(Field field : optClass.getFields()) {
            try {
                Object value = field.get(opt);
                if(searchClass.isInstance(value)) {
                    list.add((T) value);
                } else if(value instanceof Options valueAsOpt) {
                    list.addAll(getAllRecursive(valueAsOpt,searchClass));
                }
            } catch (IllegalAccessException ignored) {}
        }
        return list;
    }
}
