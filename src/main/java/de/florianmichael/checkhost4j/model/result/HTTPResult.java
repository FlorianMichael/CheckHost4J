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

package de.florianmichael.checkhost4j.model.result;

import com.google.gson.JsonArray;
import de.florianmichael.checkhost4j.model.Result;

import static de.florianmichael.checkhost4j.util.JsonParser.*;

/**
 * Wrapper class file for DNS results, see <a href="https://check-host.net/about/api">CheckHost API specification</a> for more information
 */
public class HTTPResult extends Result {

	public static final HTTPResult FAILED = new HTTPResult(-1, -1, null, -1, null);

	public final int statusCode;
	public final double ping;
	public final String status;
	public final int errorCode;
	public final String address;

	private HTTPResult(int statusCode, double ping, String status, int errorCode, String address) {
		this.statusCode = statusCode;
		this.ping = ping;
		this.status = status;
		this.errorCode = errorCode;
		this.address = address;
	}

	public static HTTPResult of(final JsonArray data) {
		checkPrimitives(data);
		final int statusCode = getInt(data.get(0));
		final double ping = getDouble(data.get(1));
		final String status = data.get(2).getAsString();
		final int error = getOptInt(data, 3);
		if (error != -1) {
			final String address = getOptString(data, 4);
			return new HTTPResult(statusCode, ping, status, error, address);
		}
		return new HTTPResult(statusCode, ping, status, -1, null);
	}

	@Override
	public boolean isSuccessful() {
		return statusCode == 1;
	}

}
