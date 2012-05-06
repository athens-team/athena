/*
 * Copyright 2012 Athens Team
 *
 * This file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package net.rothlee.athens.message.attach;

import java.util.List;
import java.util.Map;

import net.rothlee.athens.utils.collections.MapWrapper;

/**
 * @author roth2520@gmail.com
 */
public class AthensParams extends MapWrapper<String, String> {

	public static AthensParams create(Map<String, List<String>> params) {
		AthensParams result = new AthensParams();
		for(Entry<String, List<String>> entry : params.entrySet()) {
			String key = entry.getKey();
			List<String> values = entry.getValue();
			if(values.size() > 0) {
				String value = values.get(values.size()-1);
				result.put(key, value);
			}
		}
		return result;
	}
	
	private AthensParams() { super(); }
	
	@SuppressWarnings("unchecked")
	public <T> T getParam(String key, Class<T> clazz) {
		T result = null;
		String value = get(key);
		if(value == null) {
			return null;
		}
		
		if(clazz.equals(String.class)) {
			result = (T) value;
		} else if(clazz.equals(Double.class)) {
			result = (T) Double.valueOf(value);
		} else if(clazz.equals(Float.class)) {
			result = (T) Float.valueOf(value);
		} else if(clazz.equals(Long.class)) {
			result = (T) Long.valueOf(value);
		} else if(clazz.equals(Integer.class)) {
			result = (T) Integer.valueOf(value);
		} else if(clazz.equals(Short.class)) {
			result = (T) Short.valueOf(value);
		} else if(clazz.equals(Byte.class)) {
			result = (T) Byte.valueOf(value);
		} else if(clazz.equals(Boolean.class)) {
			result = (T) Boolean.valueOf(value);
		} else  {
			throw new IllegalArgumentException();
		}
		return result;
	}
}
