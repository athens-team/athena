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
package com.eincs.athens;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author roth2520@gmail.com
 */
public class DataUtils {

	private DataUtils() {
	}

	public static String toResponseString(JConvertable convertable)
			throws JSONException {
		return convertable.toJSON().toString();
	}

	public static String toResponseString(
			List<? extends JConvertable> convertableList) throws JSONException {
		JSONObject result = new JSONObject();
		JSONArray list = new JSONArray();
		for (JConvertable convertable : convertableList) {
			list.put(convertable.toJSON());
		}
		result.put("result", list);
		return result.toString();
	}

	public static String toResponseString(boolean b) throws JSONException {
		JSONObject result = new JSONObject();
		result.put("result", b);
		return result.toString();
	}

	public static String toResponseString(String value) throws JSONException {
		JSONObject result = new JSONObject();
		result.put("result", value);
		return result.toString();
	}

	public static String toResponseStringError(String msg) throws JSONException {
		JSONObject result = new JSONObject();
		result.put("error", msg);
		return result.toString();
	}

	public interface JConvertable {
		public JSONObject toJSON() throws JSONException;
	}

}
