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

public class ServerNode {
    public final String name;
    public final String countryCode;
    public final String country;
    public final String city;
    public final String ip;
    public final String asName;

    public ServerNode(String name, String countryCode, String country, String city, String ip, String asName) {
        this.name = name;
        this.countryCode = countryCode;
        this.country = country;
        this.city = city;
        this.ip = ip;
        this.asName = asName;
    }
}
