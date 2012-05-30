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
package com.eincs.pantheon.message;

import java.nio.charset.Charset;

/**
 * @author roth2520@gmail.com
 */
public enum PanteonContentType {

	TEXT_PLAIN("text/plain"),
	TEXT_HTML("text/html"),
	TEXT_CSS("text/css"),
	TEXT_JAVASCRIPT("text/javascript"),
	APPLICATION_JSON("text/json"),
	APPLICATION_XML("text/xml");
	
	private final String notation;
	PanteonContentType(String notation) {
		this.notation = notation;
	}
	
	public String getNotation() {
		return notation;
	}
	
	public String getNotationWithEncoding(Charset charset) {
		StringBuffer sb = new StringBuffer();
		sb.append(notation).append("; ").append("charset=")
				.append(charset.toString());
		return sb.toString();
	}
}
