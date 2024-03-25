/*
 * This file is part of CheckHost4J - https://github.com/FlorianMichael/CheckHost4J
 * Copyright (C) 2023-2024 FlorianMichael/EnZaXD <florian.michael07@gmail.com> and contributors
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

package de.florianmichael.checkhost4j.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class ServerNode {

    public final String name;
    public final String countryCode;
    public final String country;
    public final String city;
    public final String ip;
    public final String asName;

    private ServerNode(String name, String countryCode, String country, String city, String ip, String asName) {
        this.name = name;
        this.countryCode = countryCode;
        this.country = country;
        this.city = city;
        this.ip = ip;
        this.asName = asName;
    }

    /**
     * Create a new {@link ServerNode} instance from the given data. The data must be a {@link JsonArray} with 5 elements.
     *
     * @param name The name of the server
     * @param data The data array
     * @return The new {@link ServerNode} instance
     */
    public static ServerNode of(final String name, final JsonArray data) {
        if (data.size() != 5) {
            throw new IllegalStateException("Expected 5 elements, got: " + data.size());
        }
        for (JsonElement element : data) {
            if (!element.isJsonPrimitive()) {
                throw new IllegalStateException("Expected a primitive element, got: " + element);
            }
        }
        return new ServerNode(name, data.get(0).getAsString(), data.get(1).getAsString(), data.get(2).getAsString(), data.get(3).getAsString(), data.get(4).getAsString());
    }

}
