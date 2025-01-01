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

package de.florianmichael.checkhost4j.model;

import com.google.gson.JsonArray;

import static de.florianmichael.checkhost4j.util.JsonParser.*;

public class ServerNode {
    public static final int EXPECTED_SIZE = 5;

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
     * Create a new {@link ServerNode} instance from the given data. The data must be a {@link JsonArray} with {@link #EXPECTED_SIZE} elements.
     *
     * @param name The name of the server
     * @param data The data array
     * @return The new {@link ServerNode} instance
     */
    public static ServerNode of(final String name, final JsonArray data) {
        // Make sure the code below won't break unexpectedly
        checkSize(data, EXPECTED_SIZE);
        checkPrimitives(data);

        return new ServerNode(name, data.get(0).getAsString(), data.get(1).getAsString(), data.get(2).getAsString(), data.get(3).getAsString(), data.get(4).getAsString());
    }

}
