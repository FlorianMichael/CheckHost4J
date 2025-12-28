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

import com.google.gson.JsonObject;
import de.florianmichael.checkhost4j.model.Result;

import static de.florianmichael.checkhost4j.util.JsonParser.*;

/**
 * Wrapper class file for DNS results, see <a href="https://check-host.net/about/api">CheckHost API specification</a> for more information
 */
public class UDPResult extends Result {

	public static final UDPResult FAILED = new UDPResult(-1, -1, null, null);

	public final double timeout;
	public final double ping;
	public final String address;
	public final String error;

	private UDPResult(double timeout, double ping, String address, String error) {
		this.timeout = timeout;
		this.ping = ping;
		this.address = address;
		this.error = error;
	}

	public static UDPResult of(final JsonObject data) {
		return new UDPResult(getOptDouble(data, "timeout", 0), getOptDouble(data, "ping", 0), getOptString(data, "address"), getOptString(data, "error"));
	}

	@Override
	public boolean isSuccessful() {
		return error == null;
	}

}
