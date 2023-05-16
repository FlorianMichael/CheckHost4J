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

package de.florianmichael.checkhost4j.types;

import de.florianmichael.checkhost4j.results.*;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

public class CHResult<T> {
	
	private final ResultTypes type;
	private final List<CHServer> servers;
	private final String requestId;
	private T result;
	
	public CHResult(ResultTypes type, String requestId, List<CHServer> servers) throws IOException {
		this.type = type;
		this.requestId = requestId;
		this.servers = servers;

		this.update();
	}
	
	public String requestId() {
		return requestId;
	}
	
	public T getResult() {
		return result;
	}
	
	public ResultTypes type() {
		return type;
	}
	
	public List<CHServer> servers() {
		return servers;
	}
	
	@SuppressWarnings("unchecked")
	public void update() throws IOException {
		final Entry<String, List<CHServer>> entry = new Entry<>() {

			@Override
			public String getKey() {
				return requestId;
			}

			@Override
			public List<CHServer> getValue() {
				return servers;
			}

			@Override
			public List<CHServer> setValue(List<CHServer> value) {
				return servers;
			}
		};
		switch (type) {
			case PING -> result = (T) InternalInterface.ping(entry);
			case DNS -> result = (T) InternalInterface.dns(entry);
			case HTTP -> result = (T) InternalInterface.http(entry);
			case UDP -> result = (T) InternalInterface.udp(entry);
			case TCP -> result = (T) InternalInterface.tcp(entry);
		}
	}
}
