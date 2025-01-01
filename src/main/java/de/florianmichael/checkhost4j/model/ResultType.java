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

import com.google.gson.JsonElement;
import de.florianmichael.checkhost4j.model.result.*;
import de.florianmichael.checkhost4j.util.TFunction;

import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
public enum ResultType {

    PING("ping", "Ping", response -> PingResult.of(response.getAsJsonArray()), () -> PingResult.FAILED),
    HTTP("http", "HTTP", response -> HTTPResult.of(response.getAsJsonArray()), () -> HTTPResult.FAILED),
    TCP("tcp", "TCP port", response -> TCPResult.of(response.getAsJsonObject()), () -> TCPResult.FAILED),
    UDP("udp", "UDP port", response -> UDPResult.of(response.getAsJsonObject()), () -> UDPResult.FAILED),
    DNS("dns", "DNS", response -> DNSResult.of(response.getAsJsonObject()), () -> DNSResult.FAILED);

    private final String identifier;
    private final String displayName;
    private final TFunction<JsonElement, Result> jsonToResult;
    private final Supplier<Result> failed;

    ResultType(String identifier, String displayName, TFunction<JsonElement, Result> jsonToResult, Supplier<Result> failed) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.jsonToResult = jsonToResult;
        this.failed = failed;
    }

    /**
     * Identifier for the official web API, only used for the API requests
     *
     * @return The identifier
     */
    public String identifier() {
        return identifier;
    }

    /**
     * Proper display name for the result type, can be used for user interfaces
     *
     * @return The display name
     */
    public String displayName() {
        return displayName;
    }

    /**
     * Convert the JSON response to a result object of this type or the failed object if the response is null
     *
     * @param response The JSON response
     * @return The result object
     * @throws Exception If the conversion fails
     */
    public Result convert(final JsonElement response) throws Exception {
        if (response == null) {
            return failed.get(); // Ensure new instance for each call
        } else {
            return jsonToResult.apply(response);
        }
    }

}
