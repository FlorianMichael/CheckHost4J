/*
 * This file is part of CheckHost4J - https://github.com/FlorianMichael/CheckHost4J
 * Copyright (C) 2023 FlorianMichael/EnZaXD and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
