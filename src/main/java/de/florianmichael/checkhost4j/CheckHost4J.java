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
import de.florianmichael.checkhost4j.model.result.*;
import de.florianmichael.checkhost4j.request.IRequester;
import de.florianmichael.checkhost4j.request.JavaRequester;
import de.florianmichael.checkhost4j.util.CHRequests;
import de.florianmichael.checkhost4j.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.florianmichael.checkhost4j.util.JsonParser.*;

/**
 * The main class to interact with the CheckHost4J API. You can either use {@link #getServers(ResultType, String, int)} to get a list of servers
 * and create the {@link ResultNode} yourself or use the convenience methods {@link #ping(String, int)}, {@link #http(String, int)}, {@link #tcpPort(String, int)},
 * {@link #udpPort(String, int)} and {@link #dns(String, int)} to get a {@link ResultNode} with the results.
 */
public class CheckHost4J {

    public static final CheckHost4J INSTANCE = new CheckHost4J(JavaRequester.INSTANCE);

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
    public ResultNode<UDPResult> udpPort(final String host, final int maxNodes) throws Throwable {
        final Pair<String, List<ServerNode>> entry = getServers(ResultType.UDP, host, maxNodes);

        return new ResultNode<>(requester, ResultType.UDP, entry.getKey(), entry.getValue());
    }

    /**
     * @param host     The host to check (e.g. google.com)
     * @param maxNodes The maximum amount of nodes to use
     * @return A {@link ResultNode} with the results
     * @throws Throwable If an error occurs
     */
    public ResultNode<DNSResult> dns(final String host, final int maxNodes) throws Throwable {
        final Pair<String, List<ServerNode>> entry = getServers(ResultType.DNS, host, maxNodes);

        return new ResultNode<>(requester, ResultType.DNS, entry.getKey(), entry.getValue());
    }

    /**
     * @param type     The type of the request (Ping, HTTP, TCP Port, ...)
     * @param host     The host to check (e.g. google.com)
     * @param maxNodes The maximum amount of nodes to use
     * @return A pair of the request ID and a list of nodes
     * @throws Throwable If an error occurs (e.g. invalid response)
     */
    public Pair<String, List<ServerNode>> getServers(final ResultType type, final String host, final int maxNodes) throws Throwable {
        final JsonObject response = CHRequests.getServers(requester, type.identifier(), host, maxNodes);

        final JsonObject nodes = getObject(response, "nodes");

        final List<ServerNode> servers = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : nodes.entrySet()) {
            final JsonArray node = entry.getValue().getAsJsonArray();
            servers.add(ServerNode.of(entry.getKey(), node));
        }

        return new Pair<>(response.get("request_id").getAsString(), servers);
    }

}
