/*
 * This file is part of CheckHost4J - https://github.com/FlorianMichael/CheckHost4J
 * Copyright (C) 2023 FlorianMichael/EnZaXD and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.florianmichael.checkhost4j.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.florianmichael.checkhost4j.CheckHostAPI;
import de.florianmichael.checkhost4j.results.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternalInterface {

    static Map<CHServer, PingResult> ping(Map.Entry<String, List<CHServer>> input) throws IOException {
        String id = input.getKey();
        List<CHServer> servers = input.getValue();
        JsonObject main = CheckHostAPI.performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, StandardCharsets.UTF_8));

        Map<CHServer, PingResult> result = new HashMap<>();
        for (CHServer server : servers) {
            if (!main.has(server.name())) continue;
            if (main.get(server.name()).isJsonNull()) continue;

            JsonArray jsonArray = main.get(server.name()).getAsJsonArray();
            for (int k = 0; k < jsonArray.size(); k++) {
                final JsonElement element = jsonArray.get(k);

                if (element.isJsonArray()) {
                    JsonArray innerJsonArray = element.getAsJsonArray();
                    List<PingResult.PingEntry> pEntries = new ArrayList<>();
                    for (int j = 0; j < innerJsonArray.size(); j++) {
                        if (!innerJsonArray.get(j).isJsonArray()) continue;

                        JsonArray ja3 = innerJsonArray.get(j).getAsJsonArray();
                        if (ja3.size() != 2 && ja3.size() != 3) {
                            pEntries.add(new PingResult.PingEntry("Unable to resolve domain name.", -1, null));
                            continue;
                        }
                        String status = ja3.get(0).getAsString();
                        double ping = ja3.get(1).getAsDouble();
                        String addr = null;
                        if (ja3.size() > 2) {
                            addr = ja3.get(2).getAsString();
                        }
                        PingResult.PingEntry pEntry = new PingResult.PingEntry(status, ping, addr);
                        pEntries.add(pEntry);
                    }
                    result.put(server, new PingResult(pEntries));
                }
            }
        }

        return result;
    }

    static Map<CHServer, TCPResult> tcp(Map.Entry<String, List<CHServer>> input) throws IOException {
        String id = input.getKey();
        List<CHServer> servers = input.getValue();
        JsonObject main = CheckHostAPI.performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, StandardCharsets.UTF_8));

        Map<CHServer, TCPResult> result = new HashMap<CHServer, TCPResult>();
        for (CHServer server : servers) {
            JsonArray ja = null;
            if (!main.has(server.name())) continue;
            if (main.get(server.name()).isJsonNull()) continue;

            ja = main.get(server.name()).getAsJsonArray();

            if (ja.size() != 1) continue;

            JsonObject obj = ja.get(0).getAsJsonObject();
            String error = null;
            if (obj.has("error")) {
                error = obj.get("error").getAsString();
            }
            String addr = null;
            if (obj.has("address")) {
                addr = obj.get("address").getAsString();
            }
            double ping = 0;
            if (obj.has("time")) {
                ping = obj.get("time").getAsDouble();
            }

            TCPResult res = new TCPResult(ping, addr, error);
            result.put(server, res);
        }

        return result;
    }

    static Map<CHServer, UDPResult> udp(Map.Entry<String, List<CHServer>> input) throws IOException {
        String id = input.getKey();
        List<CHServer> servers = input.getValue();
        JsonObject main = CheckHostAPI.performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, StandardCharsets.UTF_8));
        Map<CHServer, UDPResult> result = new HashMap<>();
        for (CHServer server : servers) {
            JsonArray ja = null;
            if (!main.has(server.name())) continue;
            if (main.get(server.name()).isJsonNull()) continue;

            ja = main.get(server.name()).getAsJsonArray();

            if (ja.size() != 1) continue;
            JsonObject obj = ja.get(0).getAsJsonObject();
            String error = null;
            if (obj.has("error")) {
                error = obj.get("error").getAsString();
            }
            String addr = null;
            if (obj.has("address")) {
                addr = obj.get("address").getAsString();
            }
            double ping = 0;
            if (obj.has("time")) {
                ping = obj.get("time").getAsDouble();
            }
            double timeout = 0;
            if (obj.has("timeout")) {
                timeout = obj.get("timeout").getAsDouble();
            }

            UDPResult res = new UDPResult(timeout, ping, addr, error);
            result.put(server, res);
        }

        return result;
    }

    static Map<CHServer, HTTPResult> http(Map.Entry<String, List<CHServer>> input) throws IOException {
        String id = input.getKey();
        List<CHServer> servers = input.getValue();
        JsonObject main = CheckHostAPI.performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, StandardCharsets.UTF_8));
        Map<CHServer, HTTPResult> result = new HashMap<CHServer, HTTPResult>();
        for (CHServer server : servers) {
            JsonArray ja = null;
            if (!main.has(server.name())) continue;
            if (main.get(server.name()).isJsonNull()) continue;

            ja = main.get(server.name()).getAsJsonArray();

            if (ja.size() != 1) continue;
            ja = ja.get(0).getAsJsonArray();

            double ping = ja.get(1).getAsDouble();
            String status = ja.get(2).getAsString();
            int error = ja.size() > 3 && ja.get(3).isJsonPrimitive() ? ja.get(3).getAsInt() : -1;
            if (error == -1)
                continue;
            String addr = ja.size() > 4 && ja.get(4).isJsonPrimitive() ? ja.get(4).getAsString() : null;

            HTTPResult res = new HTTPResult(status, ping, addr, error);
            result.put(server, res);
        }

        return result;
    }

    static Map<CHServer, DNSResult> dns(Map.Entry<String, List<CHServer>> input) throws IOException {
        String id = input.getKey();
        List<CHServer> servers = input.getValue();
        JsonObject main = CheckHostAPI.performGetRequest("https://check-host.net/check-result/" + URLEncoder.encode(id, StandardCharsets.UTF_8));
        Map<CHServer, DNSResult> result = new HashMap<CHServer, DNSResult>();
        for (CHServer server : servers) {
            JsonArray ja = null;
            if (!main.has(server.name())) continue;
            if (main.get(server.name()).isJsonNull()) continue;

            ja = main.get(server.name()).getAsJsonArray();

            if (ja.size() != 1) continue;
            JsonObject obj = ja.get(0).getAsJsonObject();

            Map<String, String[]> domainInfos = new HashMap<String, String[]>();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                if (entry.getKey().equals("TTL") || !entry.getValue().isJsonArray())
                    continue;
                JsonArray ja2 = entry.getValue().getAsJsonArray();
                String[] values = new String[ja2.size()];
                for (int k = 0; k < ja2.size(); k++) {
                    if (ja2.get(k).isJsonPrimitive()) {
                        values[k] = ja2.get(k).getAsString();
                    }
                }
                domainInfos.put(entry.getKey(), values);
            }
            DNSResult res = new DNSResult(obj.has("TTL") && obj.get("TTL").isJsonPrimitive() ? obj.get("TTL").getAsInt() : -1, domainInfos);
            result.put(server, res);
        }
        return result;
    }
}
