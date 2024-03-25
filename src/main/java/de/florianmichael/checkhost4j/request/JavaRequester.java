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

package de.florianmichael.checkhost4j.request;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class JavaRequester implements IRequester {

    public static final JavaRequester INSTANCE = new JavaRequester("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:71.0) Gecko/20100101 Firefox/71.0");

    private final String agent;

    public JavaRequester(String agent) {
        this.agent = agent;
    }

    @Override
    public String get(URI target) throws Exception {
        final HttpURLConnection connection = (HttpURLConnection) target.toURL().openConnection();

        connection.setRequestProperty("User-Agent", agent);
        connection.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            output.append(line).append(System.lineSeparator());
        }
        br.close();
        connection.disconnect();
        return output.toString();
    }

}
