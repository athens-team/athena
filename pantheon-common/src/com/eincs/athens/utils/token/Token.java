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
package com.eincs.athens.utils.token;


/**
 * @author roth2520@gmail.com
 * @param <T>
 *            token content
 */
public interface Token<T> {

	long getExpiry();

	void setExpiry(long expiry);

	long getTimestamp();

	void setTimestamp(long timestamp);

	void setSignature(byte[] signature);

	byte[] getSignature();

	void setContent(T content);

	T getContent();

}
