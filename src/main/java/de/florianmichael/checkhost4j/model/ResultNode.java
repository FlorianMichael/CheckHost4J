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

import de.florianmichael.checkhost4j.request.IRequester;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultNode<T> {
    private final IRequester requester;

    private final ResultType type;
    private final String requestId;
    private final List<ServerNode> nodes;

    private Map<ServerNode, T> results;

    public ResultNode(IRequester requester, ResultType type, String requestId, List<ServerNode> nodes) {
        this.requester = requester;
        this.type = type;
        this.requestId = requestId;
        this.nodes = nodes;

        /*
         * Initialize the results map with null values
         * This map will be filled by the {@link #tickResults()} method
         */
        this.results = new HashMap<>();
        for (ServerNode node : this.nodes) {
            this.results.put(node, null);
        }
    }

    /**
     * Tick the results for this node, this will perform the check and store the
     * result in the results map.
     */
    @SuppressWarnings("unchecked")
    public void tickResults() throws Throwable {
        results = type.checkResult().perform(requestId, requester, nodes);
    }

    /**
     * @return Gets the results for all nodes, this list has to be ticked before using the {@link #tickResults()} method,
     * otherwise the results of all nodes will be null.
     */
    public Map<ServerNode, T> getResults() {
        return results;
    }

    public ResultType getType() {
        return type;
    }

    public List<ServerNode> getNodes() {
        return nodes;
    }
}
