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

package de.florianmichael.checkhost4j.util;

import com.google.gson.JsonObject;
import de.florianmichael.checkhost4j.request.IRequester;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import static de.florianmichael.checkhost4j.util.JsonParser.GSON;

/**
 * Wrapper class file for API requests, methods here will return raw JSON data. Do not use, see {@link de.florianmichael.checkhost4j.CheckHost4J} for convenience methods.
 */
public class CHRequests {

    public static final URI ROOT_URL = URI.create("https://check-host.net");

    public static JsonObject getServers(final IRequester requester, final String type, final String target, final int maxNodes) throws Exception {
        final String result = requester.get(URI.create(ROOT_URL + "/check-" + type + "?host=" + encode(target) + "&max_nodes=" + maxNodes));

        return GSON.fromJson(result, JsonObject.class);
    }

    public static JsonObject checkResult(final IRequester requester, final String requestId) throws Exception {
        final String result = requester.get(URI.create(ROOT_URL + "/check-result/" + encode(requestId)));

        return GSON.fromJson(result, JsonObject.class);
    }

    public static String encode(final String input) throws UnsupportedEncodingException {
        return URLEncoder.encode(input, "UTF-8");
    }

}
