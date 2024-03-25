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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.florianmichael.checkhost4j.model.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.florianmichael.checkhost4j.util.JsonParser.*;

/**
 * Wrapper class file for DNS results, see <a href="https://check-host.net/about/api">CheckHost API specification</a> for more information
 */
public class PingResult extends Result {

    public static final PingResult FAILED = new PingResult(Collections.singletonList(PingEntry.FAILED));

    public final List<PingEntry> pingEntries;

    private PingResult(List<PingEntry> pingEntries) {
        this.pingEntries = pingEntries;
    }

    public static PingResult of(final JsonArray data) {
        final List<PingEntry> entries = new ArrayList<>();
        for (JsonElement element : data) {
            if (element.isJsonArray()) {
                entries.add(PingEntry.of(element.getAsJsonArray()));
            }
        }
        return new PingResult(entries);
    }

    /**
     * Get the lowest ping time from the list of ping entries or -1 if no entries are present
     *
     * @return the lowest ping time
     */
    public double getLowestPing() {
        return pingEntries.stream().mapToDouble(entry -> entry.ping).min().orElse(-1);
    }

    /**
     * Get the average ping time from the list of ping entries or -1 if no entries are present
     *
     * @return the average ping time
     */
    public double getAveragePing() {
        return pingEntries.stream().mapToDouble(entry -> entry.ping).average().orElse(-1);
    }

    /**
     * Get the highest ping time from the list of ping entries or -1 if no entries are present
     *
     * @return the highest ping time
     */
    public double getHighestPing() {
        return pingEntries.stream().mapToDouble(entry -> entry.ping).max().orElse(-1);
    }

    /**
     * Get the number of successful pings (status OK and ping higher/equal 0)
     *
     * @return the number of successful pings
     */
    public int getSuccessfulPings() {
        return (int) pingEntries.stream().filter(PingEntry::isSuccessful).count();
    }

    /**
     * Get the number of failed pings (status not OK or ping lower 0)
     *
     * @return the number of failed pings
     */
    public int getFailedPings() {
        return (int) pingEntries.stream().filter(entry -> !entry.isSuccessful()).count();
    }

    public int getTotalPings() {
        return pingEntries.size();
    }

    @Override
    public boolean isSuccessful() {
        return getFailedPings() == 0;
    }

    public static class PingEntry {

        public static final PingEntry FAILED = new PingEntry("Unable to resolve domain name.", -1, null);

        public final String status;
        public final double ping;
        public final String address;

        private PingEntry(String status, double ping, String address) {
            this.status = status;
            this.ping = ping;
            this.address = address;
        }

        public static PingEntry of(final JsonArray data) {
            checkPrimitives(data);
            return new PingEntry(data.get(0).getAsString(), getDouble(data.get(1)), getOptString(data, 2));
        }

        public boolean isSuccessful() {
            return status != null && status.equalsIgnoreCase("OK") && ping >= 0;
        }
    }

}
