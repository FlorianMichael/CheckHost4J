/*
 * This file is part of CheckHost4J - https://github.com/FlorianMichael/CheckHost4J
 * Copyright (C) 2023-2026 FlorianMichael/EnZaXD <git@florianmichael.de> and contributors
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

package de.florianmichael.checkhost4j.model.result;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.florianmichael.checkhost4j.model.Result;

import java.util.HashMap;
import java.util.Map;

import static de.florianmichael.checkhost4j.util.JsonParser.*;

/**
 * Wrapper class file for DNS results, see <a href="https://check-host.net/about/api">CheckHost API specification</a> for more information
 */
public class DNSResult extends Result {

	public static final DNSResult FAILED = new DNSResult(-1, new HashMap<>());

	public final int ttl;
	public final Map<String, String[]> result;

	private DNSResult(int ttl, Map<String, String[]> result) {
		this.ttl = ttl;
		this.result = result;
	}

	public static DNSResult of(final JsonObject data) {
		final Map<String, String[]> domains = new HashMap<>();
		for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
			if (entry.getKey().equals("TTL") || !entry.getValue().isJsonArray()) {
				continue;
			}

			final JsonArray tll = entry.getValue().getAsJsonArray();
			final String[] addresses = new String[tll.size()];
			for (int k = 0; k < tll.size(); k++) {
				if (tll.get(k).isJsonPrimitive()) {
					addresses[k] = tll.get(k).getAsString();
				}
			}
			domains.put(entry.getKey(), addresses);
		}
		return new DNSResult(getOptInt(data, "TTL", -1), domains);
	}

	@Override
	public boolean isSuccessful() {
		return ttl >= 0;
	}

}
