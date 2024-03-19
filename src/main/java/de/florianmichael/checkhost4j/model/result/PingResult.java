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

import java.util.List;

public class PingResult {

	public final List<PingEntry> pingEntries;

	public PingResult(List<PingEntry> pingEntries) {
		this.pingEntries = pingEntries;
	}

	public boolean isSuccessful() {
		return pingEntries != null && !pingEntries.isEmpty();
	}

	public double getLowestPing() {
		return pingEntries.stream().mapToDouble(entry -> entry.ping).min().orElse(-1);
	}

	public double getAveragePing() {
		return pingEntries.stream().mapToDouble(entry -> entry.ping).average().orElse(-1);
	}

	public double getHighestPing() {
		return pingEntries.stream().mapToDouble(entry -> entry.ping).max().orElse(-1);
	}

	public static class PingEntry {

		public final String status;
		public final double ping;
		public final String address;

		public PingEntry(String status, double ping, String address) {
			this.status = status;
			this.ping = ping;
			this.address = address;
		}

		public boolean isSuccessful() {
			return (status != null && status.equalsIgnoreCase("OK")) && ping >= 0;
		}
	}

}
