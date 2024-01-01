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

package de.florianmichael.checkhost4j.model.result;

public class HTTPResult {

	public final double ping;
	public final String status;
	public final int errorCode;
	public final String address;

	public HTTPResult(double ping, String status, int errorCode, String address) {
		this.ping = ping;
		this.status = status;
		this.errorCode = errorCode;
		this.address = address;
	}

	public boolean isSuccessful() {
		return status != null && status.equalsIgnoreCase("OK");
	}
}
