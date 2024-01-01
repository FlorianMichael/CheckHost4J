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
import com.google.gson.JsonObject;
import de.florianmichael.checkhost4j.model.result.*;
import de.florianmichael.checkhost4j.request.IRequester;
import de.florianmichael.checkhost4j.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public enum ResultType {

    PING("ping", "Ping", (CheckResult<PingResult>) (requestId, requester, nodes) -> {
        final Map<ServerNode, PingResult> result = new HashMap<>();
        final JsonObject response = Constants.checkResult(requester, requestId);

        for (ServerNode node : nodes) {
            if (!response.has(node.name)) continue;
            if (response.get(node.name).isJsonNull()) continue;

            final JsonArray array = response.get(node.name).getAsJsonArray();
            for (JsonElement element : array) {
                if (element.isJsonArray()) {
                    final JsonArray pingEntries = element.getAsJsonArray();
                    final List<PingResult.PingEntry> resultEntries = new ArrayList<>();

                    for (JsonElement pingEntry : pingEntries) {
                        if (!pingEntry.isJsonArray()) continue;

                        final JsonArray pingEntryData = pingEntry.getAsJsonArray();
                        if (pingEntryData.size() != 2 && pingEntryData.size() != 3) {
                            resultEntries.add(new PingResult.PingEntry("Unable to resolve domain name.", -1, null));
                            continue;
                        }

                        String address = null;
                        if (pingEntryData.size() > 2) {
                            address = pingEntryData.get(2).getAsString();
                        }

                        resultEntries.add(new PingResult.PingEntry(
                                pingEntryData.get(0).getAsString(), // status
                                pingEntryData.get(1).getAsDouble(), // ping
                                address
                        ));
                    }

                    result.put(node, new PingResult(resultEntries));
                }
            }
        }

        return result;
    }),
    HTTP("http", "HTTP", (CheckResult<HTTPResult>) (requestId, requester, nodes) -> {
        final Map<ServerNode, HTTPResult> result = new HashMap<>();
        final JsonObject response = Constants.checkResult(requester, requestId);

        for (ServerNode node : nodes) {
            if (!response.has(node.name)) continue;
            if (response.get(node.name).isJsonNull()) continue;

            final JsonArray resultArray = response.get(node.name).getAsJsonArray();
            if (resultArray.size() != 1) {
                continue;
            }
            final JsonArray data = resultArray.get(0).getAsJsonArray();

            final double ping = data.get(1).getAsDouble();
            final String status = data.get(2).getAsString();
            final int error = data.size() > 3 && data.get(3).isJsonPrimitive() ? data.get(3).getAsInt() : -1;
            if (error == -1) continue;
            final String address = data.size() > 4 && data.get(4).isJsonPrimitive() ? data.get(4).getAsString() : null;

            result.put(node, new HTTPResult(ping, status, error, address));
        }

        return result;
    }),
    TCP("tcp", "TCP port", (CheckResult<TCPResult>) (requestId, requester, nodes) -> {
        final Map<ServerNode, TCPResult> result = new HashMap<>();
        final JsonObject response = Constants.checkResult(requester, requestId);

        for (ServerNode node : nodes) {
            if (!response.has(node.name)) continue;
            if (response.get(node.name).isJsonNull()) continue;

            final JsonArray resultArray = response.get(node.name).getAsJsonArray();
            if (resultArray.size() != 1) {
                continue;
            }

            final JsonObject data = resultArray.get(0).getAsJsonObject();
            double ping = 0;
            String address = null;
            String error = null;

            if (data.has("time")) ping = data.get("time").getAsDouble();
            if (data.has("address")) address = data.get("address").getAsString();
            if (data.has("error")) error = data.get("error").getAsString();

            result.put(node, new TCPResult(ping, address, error));
        }

        return result;
    }),
    UDP("udp", "UDP port", (CheckResult<UDPResult>) (requestId, requester, nodes) -> {
        final Map<ServerNode, UDPResult> result = new HashMap<>();
        final JsonObject response = Constants.checkResult(requester, requestId);

        for (ServerNode node : nodes) {
            if (!response.has(node.name)) continue;
            if (response.get(node.name).isJsonNull()) continue;

            final JsonArray resultArray = response.get(node.name).getAsJsonArray();
            if (resultArray.size() != 1) {
                continue;
            }

            final JsonObject data = resultArray.get(0).getAsJsonObject();
            double timeout = 0;
            double ping = 0;
            String address = null;
            String error = null;

            if (data.has("timeout")) timeout = data.get("timeout").getAsDouble();
            if (data.has("time")) ping = data.get("time").getAsDouble();
            if (data.has("address")) address = data.get("address").getAsString();
            if (data.has("error")) error = data.get("error").getAsString();

            result.put(node, new UDPResult(timeout, ping, address, error));
        }

        return result;
    }),
    DNS("dns", "DNS", (CheckResult<DNSResult>) (requestId, requester, nodes) -> {
        final Map<ServerNode, DNSResult> result = new HashMap<>();
        final JsonObject response = Constants.checkResult(requester, requestId);

        for (ServerNode node : nodes) {
            if (!response.has(node.name)) continue;
            if (response.get(node.name).isJsonNull()) continue;

            final JsonArray resultArray = response.get(node.name).getAsJsonArray();
            if (resultArray.size() != 1) {
                continue;
            }

            final JsonObject data = resultArray.get(0).getAsJsonObject();

            final Map<String, String[]> domains = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                if (entry.getKey().equals("TTL") || !entry.getValue().isJsonArray()) {
                    continue;
                }

                final JsonArray endpointJson = entry.getValue().getAsJsonArray();

                final String[] endpoints = new String[endpointJson.size()];
                for (int k = 0; k < endpointJson.size(); k++) {
                    if (endpointJson.get(k).isJsonPrimitive()) {
                        endpoints[k] = endpointJson.get(k).getAsString();
                    }
                }
                domains.put(entry.getKey(), endpoints);
            }
            result.put(node, new DNSResult(data.has("TTL") && data.get("TTL").isJsonPrimitive() ? data.get("TTL").getAsInt() : -1, domains));
        }

        return result;
    });

    private final String identifier;
    private final String displayName;
    private final CheckResult checkResult;

    ResultType(final String identifier, final String displayName, final CheckResult checkResult) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.checkResult = checkResult;
    }

    public String identifier() {
        return identifier;
    }

    public String displayName() {
        return displayName;
    }

    public CheckResult checkResult() {
        return checkResult;
    }

    /**
     * Performs the check-result API request for the result
     *
     * @param <T> The result type
     */
    public interface CheckResult<T> {

        /**
         * @param requestId The request ID from the previous request
         * @param requester The requester used to send API Requests
         * @param nodes     The list of all server nodes
         * @return All server nodes and their ping results
         */
        Map<ServerNode, T> perform(final String requestId, final IRequester requester, final List<ServerNode> nodes) throws Throwable;
    }

}
