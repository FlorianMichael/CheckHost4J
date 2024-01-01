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

package de.florianmichael.checkhost4j;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.florianmichael.checkhost4j.model.ResultNode;
import de.florianmichael.checkhost4j.model.ServerNode;
import de.florianmichael.checkhost4j.model.ResultType;
import de.florianmichael.checkhost4j.model.result.HTTPResult;
import de.florianmichael.checkhost4j.model.result.PingResult;
import de.florianmichael.checkhost4j.model.result.TCPResult;
import de.florianmichael.checkhost4j.request.IRequester;
import de.florianmichael.checkhost4j.request.JavaRequester;
import de.florianmichael.checkhost4j.util.Constants;
import de.florianmichael.checkhost4j.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckHost4J {
    public final static CheckHost4J INSTANCE = new CheckHost4J(JavaRequester.INSTANCE);

    private final IRequester requester;

    private CheckHost4J(final IRequester requester) {
        this.requester = requester;
    }

    /**
     * @param host     The host to check (e.g. google.com)
     * @param maxNodes The maximum amount of nodes to use
     * @return A {@link ResultNode} with the results
     * @throws Throwable If an error occurs
     */
    public ResultNode<PingResult> ping(final String host, final int maxNodes) throws Throwable {
        final Pair<String, List<ServerNode>> entry = getServers(ResultType.PING, host, maxNodes);

        return new ResultNode<>(requester, ResultType.PING, entry.getKey(), entry.getValue());
    }

    /**
     * @param host     The host to check (e.g. google.com)
     * @param maxNodes The maximum amount of nodes to use
     * @return A {@link ResultNode} with the results
     * @throws Throwable If an error occurs
     */
    public ResultNode<HTTPResult> http(final String host, final int maxNodes) throws Throwable {
        final Pair<String, List<ServerNode>> entry = getServers(ResultType.HTTP, host, maxNodes);

        return new ResultNode<>(requester, ResultType.HTTP, entry.getKey(), entry.getValue());
    }

    /**
     * @param host     The host to check (e.g. google.com)
     * @param maxNodes The maximum amount of nodes to use
     * @return A {@link ResultNode} with the results
     * @throws Throwable If an error occurs
     */
    public ResultNode<TCPResult> tcpPort(final String host, final int maxNodes) throws Throwable {
        final Pair<String, List<ServerNode>> entry = getServers(ResultType.TCP, host, maxNodes);

        return new ResultNode<>(requester, ResultType.TCP, entry.getKey(), entry.getValue());
    }

    /**
     * @param host     The host to check (e.g. google.com)
     * @param maxNodes The maximum amount of nodes to use
     * @return A {@link ResultNode} with the results
     * @throws Throwable If an error occurs
     */
    public ResultNode<TCPResult> udpPort(final String host, final int maxNodes) throws Throwable {
        final Pair<String, List<ServerNode>> entry = getServers(ResultType.UDP, host, maxNodes);

        return new ResultNode<>(requester, ResultType.UDP, entry.getKey(), entry.getValue());
    }

    /**
     * @param host     The host to check (e.g. google.com)
     * @param maxNodes The maximum amount of nodes to use
     * @return A {@link ResultNode} with the results
     * @throws Throwable If an error occurs
     */
    public ResultNode<TCPResult> dns(final String host, final int maxNodes) throws Throwable {
        final Pair<String, List<ServerNode>> entry = getServers(ResultType.DNS, host, maxNodes);

        return new ResultNode<>(requester, ResultType.DNS, entry.getKey(), entry.getValue());
    }

    /**
     * @param type     The type of the request (Ping, HTTP, TCP Port, ...)
     * @param host     The host to check (e.g. google.com)
     * @param maxNodes The maximum amount of nodes to use
     * @return A pair of the request ID and a list of nodes
     * @throws Throwable If an error occurs
     */
    private Pair<String, List<ServerNode>> getServers(final ResultType type, final String host, final int maxNodes) throws Throwable {
        final String response = requester.get(Constants.getServers(type.identifier(), host, maxNodes));

        final JsonObject main = Constants.GSON.fromJson(response, JsonObject.class);
        if (!main.has("nodes")) {
            throw new IOException("Invalid response!");
        }

        final List<ServerNode> servers = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : main.get("nodes").getAsJsonObject().entrySet()) {
            final JsonArray details = entry.getValue().getAsJsonArray();

            servers.add(new ServerNode(
                    entry.getKey(), // Node name

                    details.get(0).getAsString(), // Country code
                    details.get(1).getAsString(), // Country name
                    details.get(2).getAsString(), // City
                    details.get(3).getAsString(), // IP
                    details.get(4).getAsString() // AS Name
            ));
        }

        return new Pair<>(main.get("request_id").getAsString(), servers);
    }
}
