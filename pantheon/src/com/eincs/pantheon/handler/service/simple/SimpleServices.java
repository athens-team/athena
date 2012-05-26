/*
 * Copyright 2012 Athens Team
 * 
 * This file to you under the Apache License, version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.eincs.pantheon.handler.service.simple;


import org.jboss.netty.handler.codec.http.HttpMethod;

import com.eincs.pantheon.handler.service.simple.SimpleServices.SimpleServiceId;
import com.eincs.pantheon.message.AthensRequest;
import com.eincs.pantheon.utils.collections.MapWrapper;

/**
 * @author roth2520@gmail.com
 */
public class SimpleServices extends MapWrapper<SimpleServiceId, SimpleService> {

	public void putByAnnotation(SimpleService service) {
		Bind bind = service.getClass().getAnnotation(Bind.class);
		if (bind != null) {
			String path = bind.path();
			String[] methods = bind.method();
			for (String method : methods) {
				HttpMethod httpMethod = HttpMethod.valueOf(method);
				SimpleServiceId serviceId = new SimpleServiceId(httpMethod,
						path);
				put(serviceId, service);
			}
		}
	}

	public boolean match(HttpMethod method, String path) {
		SimpleServiceId serviceId = new SimpleServiceId(method, path);
		return containsKey(serviceId);
	}

	public boolean match(AthensRequest request) {
		return match(request.getMethod(), request.getPath());
	}

	public SimpleService getService(HttpMethod method, String path) {
		SimpleServiceId serviceId = new SimpleServiceId(method, path);
		return get(serviceId);
	}

	public SimpleService getService(AthensRequest request) {
		return getService(request.getMethod(), request.getPath());
	}

	public static class SimpleServiceId implements Comparable<SimpleServiceId> {

		private HttpMethod method;
		private String path;

		public SimpleServiceId(HttpMethod method, String path) {
			this.method = method;
			this.path = path;
		}

		@Override
		public int compareTo(SimpleServiceId o) {
			int result = method.compareTo(o.method);
			if (result != 0) { return result; }
			return path.compareTo(o.path);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(method.toString());
			sb.append(":");
			sb.append(path);
			return sb.toString();
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) { return false; }
			if (!(o instanceof SimpleServiceId)) { return false; }
			return compareTo((SimpleServiceId) o) == 0;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}
	}
}
