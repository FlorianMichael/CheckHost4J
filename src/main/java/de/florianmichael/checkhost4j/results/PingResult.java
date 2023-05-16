/*
 * This file is part of CheckHost4J - https://github.com/FlorianMichael/CheckHost4J
 * Copyright (C) 2023 FlorianMichael/EnZaXD and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.florianmichael.checkhost4j.results;

import java.util.List;

public record PingResult(List<PingEntry> pingEntries) {

	public boolean isSuccessful() {
		return pingEntries != null && !pingEntries.isEmpty();
	}

	public record PingEntry(String status, double ping, String address) {

		public boolean isSuccessful() {
			return (status != null && status.equalsIgnoreCase("OK")) && ping >= 0;
		}
	}
}
