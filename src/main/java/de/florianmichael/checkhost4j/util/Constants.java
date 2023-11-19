/*
 * This file is part of CheckHost4J - https://github.com/FlorianMichael/CheckHost4J
 * Copyright (C) 2023 FlorianMichael/EnZaXD and contributors
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
import com.google.gson.JsonObject;
import de.florianmichael.checkhost4j.request.IRequester;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class Constants {
    public final static Gson GSON = new Gson();

    public final static URI ROOT_URL = URI.create("https://check-host.net");

    public static URI getServers(final String type, final String target, final int maxNodes) throws UnsupportedEncodingException {
        return URI.create(ROOT_URL + "/check-" + type + "?host=" + URLEncoder.encode(target, "UTF-8") + "&max_nodes=" + maxNodes);
    }

    public static JsonObject checkResult(final IRequester requester, final String requestId) throws Throwable {
        final String result = requester.get(URI.create(ROOT_URL + "/check-result/" + URLEncoder.encode(requestId, "UTF-8")));

        return GSON.fromJson(result, JsonObject.class);
    }
}
