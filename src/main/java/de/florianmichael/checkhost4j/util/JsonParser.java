/*
 * This file is part of CheckHost4J - https://github.com/FlorianMichael/CheckHost4J
 * Copyright (C) 2023-2025 FlorianMichael/EnZaXD <florian.michael07@gmail.com> and contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.florianmichael.checkhost4j.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Utility class for parsing JSON objects and arrays
 */
public class JsonParser {

    public static final Gson GSON = new Gson();

    public static void checkSize(final JsonArray array, final int expectedSize) throws IllegalStateException {
        if (array.size() != expectedSize) {
            throw new IllegalStateException("Expected " + expectedSize + " elements, got: " + array.size());
        }
    }

    public static void checkPrimitives(final JsonArray array) throws IllegalStateException {
        for (JsonElement element : array) {
            if (!element.isJsonPrimitive() && !element.isJsonNull()) {
                throw new IllegalStateException("Expected a primitive element, got: " + element);
            }
        }
    }

    public static JsonObject getObject(final JsonObject object, final String key) throws IllegalStateException {
        if (!object.has(key) || !object.get(key).isJsonObject()) {
            throw new IllegalStateException("Expected element \"" + key + "\" in response");
        }
        return object.getAsJsonObject(key);
    }

    public static String getOptString(final JsonArray data, final int index) {
        return data.size() <= index ? null : data.get(index).getAsString();
    }

    public static String getOptString(final JsonObject data, final String key) {
        return data.has(key) ? data.get(key).getAsString() : null;
    }

    public static int getOptInt(final JsonArray data, final int index) {
        return data.size() <= index ? -1 : getInt(data.get(index));
    }

    public static int getOptInt(final JsonObject data, final String key, final int fallback) {
        return data.has(key) ? data.get(key).getAsInt() : fallback;
    }

    public static double getOptDouble(final JsonObject data, final String key, final double fallback) {
        return data.has(key) ? data.get(key).getAsDouble() : fallback;
    }

    public static double getDouble(final JsonElement element) {
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
            return -1;
        }
        return element.getAsDouble();
    }

    public static int getInt(final JsonElement element) {
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
            return -1;
        }
        return element.getAsInt();
    }

}
