package com.helper.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonParser {

    private static Gson gson = new Gson();

    public static Gson getGson() {
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }

    /**
     * @param typeCast : new TypeToken<ModelName>() {}
     */
    public static <T> String toJsonExposed(Object item, TypeToken<T> typeCast) {
        String toJson = "";
        try {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .serializeNulls()
                    .create();
            toJson = gson.toJson(item, typeCast.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toJson;
    }
    /**
     * @param classOfT : ModelName.class
     */
    public static <T> String toJsonExposed(Object item, Class<T> classOfT) {
        String toJson = "";
        try {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .serializeNulls()
                    .create();
            toJson = gson.toJson(item, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toJson;
    }

    /**
     * @param typeCast : new TypeToken<ModelName>() {}
     */
    public static <T> T fromJsonExposed(String jsonValue, TypeToken<T> typeCast) {
        try {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .serializeNulls()
                    .create();
            return gson.fromJson(jsonValue, typeCast.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param typeCast : new TypeToken<ModelName>() {}
     */
    public static <T> String toJson(Object item, TypeToken<T> typeCast) {
        String toJson = "";
        try {
            toJson = new Gson().toJson(item, typeCast.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toJson;
    }
    /**
     * @param classOfT : ModelName.class
     */
    public static <T> String toJson(Object item, Class<T> classOfT) {
        String toJson = "";
        try {
            toJson = new Gson().toJson(item, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toJson;
    }


    /**
     * @param typeCast : new TypeToken<ModelName>() {}
     */
    public static <T> T fromJson(String jsonValue, TypeToken<T> typeCast) {
        try {
            return new Gson().fromJson(jsonValue, typeCast.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @param classOfT : ModelName.class
     */
    public static <T> T fromJson(String jsonValue, Class<T> classOfT) {
        try {
            return new Gson().fromJson(jsonValue, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param item : model that's convert
     * @param itemClassType : item class type
     * @param convertClassType : after convert class type
     * @apiNote : Required similar variables of both class.
     */
    public static <T> T convert(Object item, Class<?> itemClassType , Class<T> convertClassType) {
        return fromJson(toJson(item, itemClassType), convertClassType);
    }

}